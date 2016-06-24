package org.squiddev.plethora.integration.vanilla;

import com.google.common.collect.Maps;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.squiddev.plethora.api.meta.IMetaProvider;
import org.squiddev.plethora.api.meta.MetaProvider;

import java.util.Map;

/**
 * Basic properties for fluid stacks
 */
@MetaProvider(FluidStack.class)
public class MetaFluid implements IMetaProvider<FluidStack> {
	@Override
	public Map<Object, Object> getMeta(FluidStack fluidStack) {
		Map<Object, Object> data = Maps.newHashMap();
		data.put("amount", fluidStack.amount);

		Fluid fluid = fluidStack.getFluid();
		if (fluid != null) {
			data.put("name", fluid.getName());
			data.put("rawName", fluid.getUnlocalizedName(fluidStack));
			data.put("displayName", fluid.getLocalizedName(fluidStack));
		}

		return data;
	}
}
