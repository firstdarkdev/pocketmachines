package com.hypherionmc.pocketmachines.common.items.base;

import com.hypherionmc.pocketmachines.common.inventory.ISaveableContainer;
import com.hypherionmc.pocketmachines.common.world.SaveHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTickablePocketItem<T extends ISaveableContainer> extends BasePocketItem<T> {

    public BaseTickablePocketItem(SaveHolder<T> saveHolder, String nbtKey) {
        super(saveHolder, nbtKey);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level levelIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (!levelIn.isClientSide && entityIn instanceof Player player) {
            CustomData tag = stack.get(DataComponents.CUSTOM_DATA);
            if (tag == null)
                return;

            if (getSaveHolder().isEmpty())
                return;

            CompoundTag compoundTag = tag.copyTag();
            tickItem(getSaveHolder().getInstance(compoundTag.getString(NBT_KEY), player).getValue(), stack, levelIn, entityIn, itemSlot, isSelected);
        }
    }

    public abstract void tickItem(T container, @NotNull ItemStack stack, Level level, @NotNull Entity entity, int itemSlow, boolean isSelected);

}
