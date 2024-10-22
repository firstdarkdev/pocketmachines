package com.hypherionmc.pocketmachines.common.menus;

import com.hypherionmc.pocketmachines.common.menus.base.AbstractPocketFurnaceMenu;
import com.hypherionmc.pocketmachines.common.setup.ModContainers;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;

public class PocketBlastFurnaceMenu extends AbstractPocketFurnaceMenu {

    public PocketBlastFurnaceMenu(int containerId, Inventory playerInventory) {
        super(ModContainers.BLAST_FURNACE.get(), RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, containerId, playerInventory);
    }

    public PocketBlastFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(ModContainers.BLAST_FURNACE.get(), RecipeType.BLASTING, RecipePropertySet.BLAST_FURNACE_INPUT, RecipeBookType.BLAST_FURNACE, containerId, playerInventory, container, containerData);
    }

}
