package org.squiddev.plethora.integration.baubles;

import baubles.api.BaublesApi;
import baubles.common.Baubles;
import dan200.computercraft.api.lua.LuaException;
import net.minecraftforge.items.IItemHandler;
import org.squiddev.plethora.api.Injects;
import org.squiddev.plethora.api.method.IContext;
import org.squiddev.plethora.api.module.IModuleContainer;
import org.squiddev.plethora.api.module.SubtargetedModuleObjectMethod;
import org.squiddev.plethora.gameplay.modules.PlethoraModules;
import org.squiddev.plethora.integration.EntityIdentifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

/**
 * Allows getting the player's baubles inventory
 */
@Injects(Baubles.MODID)
public class MethodIntrospectionBaublesInventory extends SubtargetedModuleObjectMethod<EntityIdentifier.Player> {
	public MethodIntrospectionBaublesInventory() {
		super(
			"getBaubles", Collections.singleton(PlethoraModules.INTROSPECTION_M), EntityIdentifier.Player.class, true,
			"function():table -- Get this player's baubles inventory"
		);
	}

	@Nullable
	@Override
	public Object[] apply(@Nonnull EntityIdentifier.Player target, @Nonnull IContext<IModuleContainer> context, @Nonnull Object[] args) throws LuaException {
		IItemHandler inventory = BaublesApi.getBaublesHandler(target.getPlayer());
		return new Object[]{context.makeChildId(inventory).getObject()};
	}
}
