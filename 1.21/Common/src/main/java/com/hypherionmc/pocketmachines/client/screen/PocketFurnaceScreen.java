package com.hypherionmc.pocketmachines.client.screen;

import com.hypherionmc.pocketmachines.client.screen.base.AbstractPocketFurnaceScreen;
import com.hypherionmc.pocketmachines.common.menus.PocketFurnaceMenu;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PocketFurnaceScreen extends AbstractPocketFurnaceScreen<PocketFurnaceMenu> {

    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");

    public PocketFurnaceScreen(PocketFurnaceMenu menu, Inventory playerInventory, Component name) {
        super(menu, new SmeltingRecipeBookComponent(), playerInventory, name, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE);
    }
}
