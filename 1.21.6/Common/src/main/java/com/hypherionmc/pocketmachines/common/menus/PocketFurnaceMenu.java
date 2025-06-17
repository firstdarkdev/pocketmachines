package com.hypherionmc.pocketmachines.common.menus;

import com.hypherionmc.pocketmachines.common.menus.base.AbstractPocketFurnaceMenu;
import com.hypherionmc.pocketmachines.common.setup.ModContainers;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;

public class PocketFurnaceMenu extends AbstractPocketFurnaceMenu {

    public PocketFurnaceMenu(int containerId, Inventory playerInventory) {
        super(ModContainers.FURNACE.get(), RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, playerInventory);
    }

    public PocketFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(ModContainers.FURNACE.get(), RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, containerId, playerInventory, container, containerData);
    }

}
