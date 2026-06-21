package com.hypherionmc.pocketmachines.common.inventory;

import com.hypherionmc.pocketmachines.common.setup.ModContainers;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import com.hypherionmc.pocketmachines.mixin.accessor.SimpleContainerAccessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PocketChestInventory extends SimpleContainer implements MenuProvider, ISaveableContainer {

    private static final int INVENTORY_SIZE = 54;
    private static final Component TITLE = Component.translatable("item.pocketmachines.pocket_chest");
    protected final NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);

    public PocketChestInventory(CompoundTag tag, HolderLookup.Provider provider) {
        super(INVENTORY_SIZE);

        if (tag != null && provider != null)
            this.load(tag, provider);
    }

    public PocketChestInventory() {
        this(null, null);
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

    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items, provider);
    }

    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        ContainerHelper.saveAllItems(tag, this.items, provider);
    }

    @Override
    public void setChanged() {
        List<ContainerListener> changedListeners = ((SimpleContainerAccessor) this).getListeners();
        if (changedListeners != null) {
            for (ContainerListener iinventorychangedlistener : changedListeners) {
                iinventorychangedlistener.containerChanged(this);
            }
            PersistedMachines.markDirty();
        }
        PersistedMachines.markDirty();
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        ItemStack stack = ContainerHelper.removeItem(this.items, index, count);

        if (!stack.isEmpty())
            this.setChanged();

        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        ItemStack itemstack = this.items.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.items.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        this.items.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
