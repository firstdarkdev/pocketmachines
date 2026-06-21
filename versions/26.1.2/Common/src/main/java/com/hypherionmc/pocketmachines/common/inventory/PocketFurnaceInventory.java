package com.hypherionmc.pocketmachines.common.inventory;

import com.hypherionmc.pocketmachines.common.inventory.base.AbstractPocketFurnaceInventory;
import com.hypherionmc.pocketmachines.common.menus.PocketFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.NotNull;

public class PocketFurnaceInventory extends AbstractPocketFurnaceInventory {

    public PocketFurnaceInventory(ValueInput input) {
        super(RecipeType.SMELTING, input, Component.translatable("item.pocketmachines.pocket_furnace"));
    }

    public PocketFurnaceInventory() {
        super(RecipeType.SMELTING, Component.translatable("item.pocketmachines.pocket_furnace"));
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new PocketFurnaceMenu(containerId, inventory, this, this.containerData);
    }
}
