package com.hypherionmc.pocketmachines.common.inventory;

import com.hypherionmc.pocketmachines.common.setup.ModContainers;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PocketChestInventory extends SimpleContainer implements MenuProvider, ISaveableContainer {

    private static final int INVENTORY_SIZE = 54;
    private static final Component TITLE = Component.translatable("item.pocketmachines.pocket_chest");
    //protected final NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);

    public PocketChestInventory(ValueInput input) {
        super(INVENTORY_SIZE);

        if (input != null)
            this.load(input);
    }

    public PocketChestInventory() {
        this(null);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new ChestMenu(ModContainers.GENERIC_9x6.get(), containerId, inventory, this, 6);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    public void load(ValueInput tag) {
        this.clearContent();
        ContainerHelper.loadAllItems(tag, this.getItems());
    }

    public void save(@NotNull ValueOutput tag) {
        ContainerHelper.saveAllItems(tag, this.getItems());
    }

    @Override
    public void setChanged() {
        PersistedMachines.markDirty();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
