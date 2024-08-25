package com.hypherionmc.pocketmachines.platform;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

public interface PocketMachinesHelper {

    public static final PocketMachinesHelper INSTANCE = InternalServiceUtil.load(PocketMachinesHelper.class);

    int getBurnTime(ItemStack stack);
    <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> creator, FeatureFlagSet flags);

}
