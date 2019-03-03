package org.squiddev.plethora.integration.tesla;

import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.squiddev.plethora.api.Injects;
import org.squiddev.plethora.api.converter.DynamicConverter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Injects("tesla")
public class ConverterTeslaHolder extends DynamicConverter<ICapabilityProvider, ITeslaHolder> {
	@Nullable
	@Override
	public ITeslaHolder convert(@Nonnull ICapabilityProvider from) {
		return from.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null)
			? from.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null)
			: null;
	}
}
