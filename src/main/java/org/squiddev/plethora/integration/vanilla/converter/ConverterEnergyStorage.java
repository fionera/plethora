package org.squiddev.plethora.integration.vanilla.converter;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.squiddev.plethora.api.Injects;
import org.squiddev.plethora.api.converter.DynamicConverter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Injects
public final class ConverterEnergyStorage extends DynamicConverter<ICapabilityProvider, IEnergyStorage> {
	@Nullable
	@Override
	public IEnergyStorage convert(@Nonnull ICapabilityProvider from) {
		return from.hasCapability(CapabilityEnergy.ENERGY, null)
			? from.getCapability(CapabilityEnergy.ENERGY, null)
			: null;
	}
}
