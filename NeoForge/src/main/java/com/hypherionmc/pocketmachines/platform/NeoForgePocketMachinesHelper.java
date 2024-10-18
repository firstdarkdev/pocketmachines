package com.hypherionmc.pocketmachines.platform;

import com.google.auto.service.AutoService;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiFunction;

@AutoService(PocketMachinesHelper.class)
public class NeoForgePocketMachinesHelper implements PocketMachinesHelper {

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> creator, FeatureFlagSet flags) {
        return new MenuType<>(creator::apply, flags);
    }
}
