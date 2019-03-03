package org.squiddev.plethora.integration.appliedenergistics;

import appeng.api.storage.data.IAEFluidStack;
import appeng.core.AppEng;
import net.minecraftforge.fluids.FluidStack;
import org.squiddev.plethora.api.Injects;
import org.squiddev.plethora.api.converter.DynamicConverter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Injects(AppEng.MOD_ID)
public class ConverterAEFluidStack extends DynamicConverter<IAEFluidStack, FluidStack> {
	@Nullable
	@Override
	public FluidStack convert(@Nonnull IAEFluidStack from) {
		return from.getFluidStack();
	}
}
