package com.hypherionmc.pocketmachines.client.screen;

import com.hypherionmc.pocketmachines.client.screen.base.AbstractPocketFurnaceScreen;
import com.hypherionmc.pocketmachines.common.menus.PocketBlastFurnaceMenu;
import net.minecraft.client.gui.screens.recipebook.BlastingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PocketBlastFurnaceScreen extends AbstractPocketFurnaceScreen<PocketBlastFurnaceMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/blast_furnace.png");

    public PocketBlastFurnaceScreen(PocketBlastFurnaceMenu menu, Inventory playerInventory, Component name) {
        super(menu, new BlastingRecipeBookComponent(), playerInventory, name, TEXTURE);
    }

}
