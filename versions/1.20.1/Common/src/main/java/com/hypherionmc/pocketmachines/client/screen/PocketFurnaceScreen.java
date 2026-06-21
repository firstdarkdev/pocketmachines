package com.hypherionmc.pocketmachines.client.screen;

import com.hypherionmc.pocketmachines.client.screen.base.AbstractPocketFurnaceScreen;
import com.hypherionmc.pocketmachines.common.menus.PocketFurnaceMenu;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PocketFurnaceScreen extends AbstractPocketFurnaceScreen<PocketFurnaceMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");

    public PocketFurnaceScreen(PocketFurnaceMenu menu, Inventory playerInventory, Component name) {
        super(menu, new SmeltingRecipeBookComponent(), playerInventory, name, TEXTURE);
    }
}
