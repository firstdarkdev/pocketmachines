package com.hypherionmc.pocketmachines.client.screen;

import com.hypherionmc.pocketmachines.client.screen.base.AbstractPocketFurnaceScreen;
import com.hypherionmc.pocketmachines.common.menus.PocketBlastFurnaceMenu;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PocketBlastFurnaceScreen extends AbstractPocketFurnaceScreen<PocketBlastFurnaceMenu> {

    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/blast_furnace/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/blast_furnace/burn_progress");
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/blast_furnace.png");

    public PocketBlastFurnaceScreen(PocketBlastFurnaceMenu menu, Inventory playerInventory, Component name) {
        super(menu, new SmeltingRecipeBookComponent(), playerInventory, name, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE);
    }

}
