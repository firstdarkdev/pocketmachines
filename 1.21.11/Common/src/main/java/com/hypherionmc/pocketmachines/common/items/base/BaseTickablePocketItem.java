package com.hypherionmc.pocketmachines.common.items.base;

import com.hypherionmc.pocketmachines.common.inventory.ISaveableContainer;
import com.hypherionmc.pocketmachines.common.world.SaveHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTickablePocketItem<T extends ISaveableContainer> extends BasePocketItem<T> {

    public BaseTickablePocketItem(SaveHolder<T> saveHolder, String nbtKey, ResourceKey<Item> key) {
        super(saveHolder, nbtKey, key);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, ServerLevel levelIn, @NotNull Entity entityIn, EquipmentSlot slot) {
        if (!levelIn.isClientSide() && entityIn instanceof Player player) {
            CustomData tag = stack.get(DataComponents.CUSTOM_DATA);
            if (tag == null)
                return;

            if (getSaveHolder().isEmpty())
                return;

            CompoundTag compoundTag = tag.copyTag();
            tickItem(getSaveHolder().getInstance(compoundTag.getStringOr(NBT_KEY, NBT_KEY), player).getValue(), stack, levelIn, entityIn, slot);
        }
    }

    public abstract void tickItem(T container, @NotNull ItemStack stack, ServerLevel level, @NotNull Entity entity, EquipmentSlot slot);

}
