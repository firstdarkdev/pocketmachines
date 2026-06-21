package com.hypherionmc.pocketmachines.client.screen.base;

import com.hypherionmc.pocketmachines.common.menus.base.AbstractPocketFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AbstractPocketFurnaceScreen<T extends AbstractPocketFurnaceMenu> extends AbstractFurnaceScreen<T> {

    public AbstractPocketFurnaceScreen(T menuType, AbstractFurnaceRecipeBookComponent recipeBookComponent, Inventory playerInventory, Component name, ResourceLocation texture, ResourceLocation litProgressSprite, ResourceLocation burnProgressSprite) {
        super(menuType, recipeBookComponent, playerInventory, name, texture, litProgressSprite, burnProgressSprite);
    }


}
