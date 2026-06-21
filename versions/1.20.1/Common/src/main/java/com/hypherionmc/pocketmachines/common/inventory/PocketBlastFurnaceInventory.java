package com.hypherionmc.pocketmachines.common.inventory;

import com.hypherionmc.pocketmachines.common.inventory.base.AbstractPocketFurnaceInventory;
import com.hypherionmc.pocketmachines.common.menus.PocketBlastFurnaceMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class PocketBlastFurnaceInventory extends AbstractPocketFurnaceInventory {

    public PocketBlastFurnaceInventory(CompoundTag tag) {
        super(RecipeType.BLASTING, tag, Component.translatable("item.pocketmachines.pocket_blast_furnace"));
    }

    public PocketBlastFurnaceInventory() {
        super(RecipeType.BLASTING, null, Component.translatable("item.pocketmachines.pocket_blast_furnace"));
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new PocketBlastFurnaceMenu(containerId, inventory, this, this.containerData);
    }
}
