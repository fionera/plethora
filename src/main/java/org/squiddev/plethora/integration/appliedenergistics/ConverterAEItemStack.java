package org.squiddev.plethora.integration.appliedenergistics;

import appeng.api.storage.data.IAEItemStack;
import appeng.core.AppEng;
import net.minecraft.item.ItemStack;
import org.squiddev.plethora.api.Injects;
import org.squiddev.plethora.api.converter.DynamicConverter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Injects(AppEng.MOD_ID)
public class ConverterAEItemStack extends DynamicConverter<IAEItemStack, ItemStack> {
	@Nullable
	@Override
	public ItemStack convert(@Nonnull IAEItemStack from) {
		return from.getStackSize() == 0 ? from.getDefinition() : from.createItemStack();
	}
}
