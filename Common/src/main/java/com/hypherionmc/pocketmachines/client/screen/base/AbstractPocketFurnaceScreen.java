package com.hypherionmc.pocketmachines.client.screen.base;

import com.hypherionmc.pocketmachines.common.menus.base.AbstractPocketFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class AbstractPocketFurnaceScreen<T extends AbstractPocketFurnaceMenu> extends AbstractFurnaceScreen<T> {

    public AbstractPocketFurnaceScreen(T menuType, Component recipeBookComponent, Inventory playerInventory, Component name, ResourceLocation texture, ResourceLocation litProgressSprite, ResourceLocation burnProgressSprite, List<RecipeBookComponent.TabInfo> tabs) {
        super(menuType, playerInventory, recipeBookComponent, name, texture, litProgressSprite, burnProgressSprite, tabs);
    }


}
