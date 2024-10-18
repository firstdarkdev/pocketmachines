package com.hypherionmc.pocketmachines.common.inventory;

import com.hypherionmc.pocketmachines.common.inventory.base.AbstractPocketFurnaceInventory;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.NotNull;

public class PocketSmokerInventory extends AbstractPocketFurnaceInventory {

    public PocketSmokerInventory(CompoundTag nbt, HolderLookup.Provider provider) {
        super(RecipeType.SMOKING, nbt, provider, Component.translatable("item.pocketmachines.pocket_smoker"));
    }

    public PocketSmokerInventory() {
        super(RecipeType.SMOKING, Component.translatable("item.pocketmachines.pocket_smoker"));
    }

    @Override
    protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
        return super.getBurnDuration(fuelValues, itemStack) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new SmokerMenu(containerId, inventory, this, this.containerData);
    }
}
