package com.hypherionmc.pocketmachines.common.inventory;

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
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class PocketBrewingStandInventory extends SimpleContainer implements MenuProvider, ISaveableContainer {

    private static final Component NAME = Component.translatable("item.pocketmachines.pocket_brewing_stand");
    private final NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    private boolean[] lastPotionCount;
    private Item ingredient;
    int brewTime, fuel;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int i) {
            return switch (i) {
                case 0 -> brewTime;
                case 1 -> fuel;
                default -> 0;
            };
        }

        @Override
        public void set(int i, int j) {
            switch (i) {
                case 0:
                    brewTime = j;
                    break;
                case 1:
                    fuel = j;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public PocketBrewingStandInventory(CompoundTag tag, HolderLookup.Provider provider) {
        super(5);
        if (tag != null && provider != null)
            this.load(tag, provider);
    }

    public PocketBrewingStandInventory() {
        this(null, null);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return NAME;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void tick(Level level) {
        ItemStack itemStack = this.items.get(4);
        if (this.fuel <= 0 && itemStack.is(Items.BLAZE_POWDER)) {
            this.fuel = 20;
            itemStack.shrink(1);
            setChanged();
        }

        boolean bl = isBrewable(level.potionBrewing(), this.items);
        boolean bl2 = this.brewTime > 0;
        ItemStack itemStack2 = this.items.get(3);
        if (bl2) {
            this.brewTime--;
            boolean bl3 = this.brewTime == 0;
            if (bl3 && bl) {
                doBrew(level, this.items);
            } else if (!bl || !itemStack2.is(this.ingredient)) {
                this.brewTime = 0;
            }

            setChanged();
        } else if (bl && this.fuel > 0) {
            this.fuel--;
            this.brewTime = 400;
            this.ingredient = itemStack2.getItem();
            setChanged();
        }

        boolean[] bls = this.getPotionBits();
        if (!Arrays.equals(bls, this.lastPotionCount)) {
            this.lastPotionCount = bls;
        }
    }

    private boolean[] getPotionBits() {
        boolean[] bls = new boolean[3];

        for (int i = 0; i < 3; i++) {
            if (!this.items.get(i).isEmpty()) {
                bls[i] = true;
            }
        }

        return bls;
    }

    private static boolean isBrewable(PotionBrewing potionBrewing, NonNullList<ItemStack> nonNullList) {
        ItemStack itemStack = nonNullList.get(3);
        if (itemStack.isEmpty()) {
            return false;
        } else if (!potionBrewing.isIngredient(itemStack)) {
            return false;
        } else {
            for (int i = 0; i < 3; i++) {
                ItemStack itemStack2 = nonNullList.get(i);
                if (!itemStack2.isEmpty() && potionBrewing.hasMix(itemStack2, itemStack)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static void doBrew(Level level, NonNullList<ItemStack> nonNullList) {
        ItemStack itemStack = nonNullList.get(3);
        PotionBrewing potionBrewing = level.potionBrewing();

        for (int i = 0; i < 3; i++) {
            nonNullList.set(i, potionBrewing.mix(itemStack, nonNullList.get(i)));
        }

        itemStack.shrink(1);
        ItemStack lv3 = itemStack.getItem().getCraftingRemainder();
        if (!lv3.isEmpty()) {
            if (itemStack.isEmpty()) {
                itemStack = lv3;
            }
        }

        nonNullList.set(3, itemStack);
    }


    public void load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        this.items.clear();
        ContainerHelper.loadAllItems(compoundTag, this.items, provider);
        this.brewTime = compoundTag.getShort("BrewTime");
        if (this.brewTime > 0) {
            this.ingredient = this.items.get(3).getItem();
        }

        this.fuel = compoundTag.getByte("Fuel");
    }

    public void save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        compoundTag.putShort("BrewTime", (short)this.brewTime);
        ContainerHelper.saveAllItems(compoundTag, this.items, provider);
        compoundTag.putByte("Fuel", (byte)this.fuel);
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
    public @NotNull ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.items, i);
    }

    @Override
    public boolean stillValid(@NotNull Player arg) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemStack) {
        if (i == 3) {
            PotionBrewing potionBrewing = PersistedMachines.hasLevel() ? PersistedMachines.getLevel().potionBrewing() : PotionBrewing.EMPTY;
            return potionBrewing.isIngredient(itemStack);
        } else {
            return i == 4
                    ? itemStack.is(Items.BLAZE_POWDER)
                    : (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.GLASS_BOTTLE))
                    && this.getItem(i).isEmpty();
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public void setItem(int pIndex, @NotNull ItemStack pStack) {
        if (pIndex >= 0 && pIndex < this.items.size()) {
            this.items.set(pIndex, pStack);
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new BrewingStandMenu(containerId, inventory, this, this.dataAccess);
    }
}
