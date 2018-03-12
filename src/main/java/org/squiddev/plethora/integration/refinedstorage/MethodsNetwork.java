package org.squiddev.plethora.integration.refinedstorage;

import com.google.common.collect.Maps;
import com.raoulvdberge.refinedstorage.RS;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.task.ICraftingTask;
import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import com.raoulvdberge.refinedstorage.tile.TileController;
import dan200.computercraft.api.lua.LuaException;
import net.minecraft.item.ItemStack;
import org.squiddev.plethora.api.method.*;
import org.squiddev.plethora.integration.ItemFingerprint;
import org.squiddev.plethora.integration.vanilla.NullableItemStack;
import org.squiddev.plethora.integration.vanilla.meta.MetaItemBasic;

import java.util.*;

public class MethodsNetwork {
	@BasicObjectMethod.Inject(
		value = INetworkNode.class, modId = RS.ID, worldThread = true,
		doc = "function():int -- Get the energy usage of this RefinedStorage node"
	)
	public static Object[] getNodeEnergyUsage(IContext<INetworkNode> context, Object[] args) {
		return new Object[]{context.getTarget().getEnergyUsage()};
	}

	@BasicObjectMethod.Inject(
		value = INetwork.class, modId = RS.ID, worldThread = true,
		doc = "function():int -- Get the energy usage of this RefinedStorage network"
	)
	public static Object[] getNetworkEnergyUsage(IContext<INetwork> context, Object[] args) {
		return new Object[]{context.getTarget().getEnergyUsage()};
	}

	@BasicObjectMethod.Inject(
		value = TileController.class, modId = RS.ID, worldThread = true,
		doc = "function():int -- Get the energy stored usage in this RefinedStorage network"
	)
	public static Object[] getNetworkEnergyStored(IContext<TileController> context, Object[] args) {
		return new Object[]{context.getTarget().getEnergy().getEnergyStored()};
	}

	@BasicObjectMethod.Inject(
		value = INetwork.class, modId = RS.ID, worldThread = true,
		doc = "function():table -- List all items which are stored in the network"
	)
	public static Object[] listAvailableItems(IContext<INetwork> context, Object[] args) {
		INetwork network = context.getTarget();
		Collection<ItemStack> items = network.getItemStorageCache().getList().getStacks();
		Set<ItemIdentity> seen = new HashSet<>();

		int i = 0;
		Map<Integer, Map<Object, Object>> output = Maps.newHashMapWithExpectedSize(items.size());
		for (ItemStack stack : items) {
			seen.add(new ItemIdentity(stack));
			output.put(++i, MetaItemBasic.getBasicProperties(stack));
		}

		for (ICraftingPattern pattern : network.getCraftingManager().getPatterns()) {
			for (ItemStack stack : pattern.getOutputs()) {
				if (stack != null && seen.add(new ItemIdentity(stack))) {
					Map<Object, Object> result = MetaItemBasic.getBasicProperties(stack);
					result.put("count", 0);
					output.put(++i, result);
				}
			}
		}

		return new Object[]{output};
	}

	@BasicMethod.Inject(
		value = INetwork.class, modId = RS.ID,
		doc = "function(item:string|table):table -- Search for an item in the network. " +
			"You can specify the item as a string, with or without the damage value ('minecraft:stone' or 'minecraft:stone@0') " +
			"or as a table with 'name', 'damage' and 'nbthash' fields. You must specify the 'name', but you can " +
			"leave the other fields empty."
	)
	public static MethodResult findItem(final IUnbakedContext<INetwork> context, Object[] args) throws LuaException {
		final ItemFingerprint fingerprint = ItemFingerprint.fromLua(args.length == 0 ? null : args[0]);

		return MethodResult.nextTick(() -> {
			IContext<INetwork> baked = context.bake();
			INetwork network = baked.getTarget();

			for (ItemStack stack : network.getItemStorageCache().getList().getStacks()) {
				if (fingerprint.matches(stack)) {
					return MethodResult.result(baked.makeChildId(NullableItemStack.normal(stack)).getObject());
				}
			}

			for (ICraftingPattern pattern : network.getCraftingManager().getPatterns()) {
				for (ItemStack stack : pattern.getOutputs()) {
					if (fingerprint.matches(stack)) {
						return MethodResult.result(baked.makeChildId(NullableItemStack.empty(stack)).getObject());
					}
				}
			}

			return MethodResult.empty();
		});
	}

	@BasicMethod.Inject(
		value = INetwork.class, modId = RS.ID,
		doc = "function(item:string|table):table -- Search all items in the network. " +
			"You can specify the item as a string, with or without the damage value ('minecraft:stone' or 'minecraft:stone@0') " +
			"or as a table with 'name', 'damage' and 'nbthash' fields. You must specify the 'name', but you can " +
			"leave the other fields empty."
	)
	public static MethodResult findItems(final IUnbakedContext<INetwork> context, Object[] args) throws LuaException {
		final ItemFingerprint fingerprint = ItemFingerprint.fromLua(args.length == 0 ? null : args[0]);

		return MethodResult.nextTick(() -> {
			IContext<INetwork> baked = context.bake();
			INetwork network = baked.getTarget();
			Set<ItemIdentity> seen = new HashSet<>();

			int i = 0;
			Map<Integer, Object> out = Maps.newHashMap();
			for (ItemStack stack : network.getItemStorageCache().getList().getStacks()) {
				if (fingerprint.matches(stack)) {
					seen.add(new ItemIdentity(stack));
					out.put(++i, baked.makeChildId(NullableItemStack.normal(stack)).getObject());
				}
			}

			for (ICraftingPattern pattern : network.getCraftingManager().getPatterns()) {
				for (ItemStack stack : pattern.getOutputs()) {
					if (stack != null && fingerprint.matches(stack) && seen.add(new ItemIdentity(stack))) {
						out.put(++i, baked.makeChildId(NullableItemStack.empty(stack)).getObject());
					}
				}
			}

			return MethodResult.result(out);
		});
	}

	@BasicObjectMethod.Inject(
		value = INetwork.class, modId = RS.ID, worldThread = true,
		doc = "function():table -- List all crafting tasks in the network"
	)
	public static Object[] getCraftingTasks(IContext<INetwork> context, Object[] args) {
		List<ICraftingTask> tasks = context.getTarget().getCraftingManager().getTasks();

		int i = 0;
		Map<Integer, Object> output = Maps.newHashMapWithExpectedSize(tasks.size());
		for (ICraftingTask task : tasks) {
			output.put(++i, context.makeChildId(task).getObject());
		}

		return new Object[]{output};
	}
}
