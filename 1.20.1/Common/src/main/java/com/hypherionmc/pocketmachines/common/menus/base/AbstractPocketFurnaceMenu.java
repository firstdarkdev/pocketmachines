package com.hypherionmc.pocketmachines.common.menus.base;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class AbstractPocketFurnaceMenu extends AbstractFurnaceMenu {

    public AbstractPocketFurnaceMenu(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType recipeBookType, int containerId, Inventory playerInventory) {
        super(menuType, recipeType, recipeBookType, containerId, playerInventory);
    }

    public AbstractPocketFurnaceMenu(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType recipeBookType, int containerId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(menuType, recipeType, recipeBookType, containerId, playerInventory, container, containerData);
    }

    @Override
    public boolean stillValid(@NotNull Player arg) {
        return true;
    }
}
