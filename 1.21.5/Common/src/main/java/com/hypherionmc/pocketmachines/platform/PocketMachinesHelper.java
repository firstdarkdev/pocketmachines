package com.hypherionmc.pocketmachines.platform;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiFunction;

public interface PocketMachinesHelper {

    public static final PocketMachinesHelper INSTANCE = InternalServiceUtil.load(PocketMachinesHelper.class);
    <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> creator, FeatureFlagSet flags);

}
