package com.hypherionmc.pocketmachines.common.inventory.base;

import com.hypherionmc.pocketmachines.common.inventory.ISaveableContainer;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import com.hypherionmc.pocketmachines.mixin.accessor.SimpleContainerAccessor;
import com.hypherionmc.pocketmachines.platform.PocketMachinesHelper;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractPocketFurnaceInventory extends SimpleContainer implements MenuProvider, ISaveableContainer {

    protected final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final Component name;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private int litTime, litDuration, cookingProgress, cookingTotalTime;
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;

    public final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> litTime;
                case 1 -> litDuration;
                case 2 -> cookingProgress;
                case 3 -> cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    litTime = value;
                    break;
                case 1:
                    litDuration = value;
                    break;
                case 2:
                    cookingProgress = value;
                    break;
                case 3:
                    cookingTotalTime = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public AbstractPocketFurnaceInventory(RecipeType<? extends AbstractCookingRecipe> recipeType, CompoundTag nbt, HolderLookup.Provider provider, Component name) {
        super(3);
        this.recipeType = recipeType;
        this.name = name;

        quickCheck = RecipeManager.createCheck(recipeType);

        if (nbt != null && provider != null)
            this.load(nbt, provider);
    }

    public AbstractPocketFurnaceInventory(RecipeType<? extends AbstractCookingRecipe> recipeType, Component name) {
        this(recipeType, null, null, name);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player playerInventory) {
        return this.createMenu(containerId, inventory);
    }

    protected abstract AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory);

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.name;
    }

    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, provider);
        this.litTime = tag.getShort("BurnTime");
        this.cookingProgress = tag.getShort("CookTime");
        this.cookingTotalTime = tag.getShort("CookTimeTotal");
        this.litDuration = 0;
        CompoundTag compoundTag2 = tag.getCompound("RecipesUsed");

        for (String string : compoundTag2.getAllKeys()) {
            this.recipesUsed.put(ResourceLocation.parse(string), compoundTag2.getInt(string));
        }
    }

    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        tag.putShort("BurnTime", (short)this.litTime);
        tag.putShort("CookTime", (short)this.cookingProgress);
        tag.putShort("CookTimeTotal", (short)this.cookingTotalTime);
        ContainerHelper.saveAllItems(tag, this.items, provider);
        CompoundTag compoundTag2 = new CompoundTag();
        this.recipesUsed.forEach((resourceLocation, integer) -> compoundTag2.putInt(resourceLocation.toString(), integer));
        tag.put("RecipesUsed", compoundTag2);
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    public void tick(Level level) {
        boolean isLit = this.isLit();
        boolean bl2 = false;
        if (this.isLit()) {
            this.litTime--;
        }

        ItemStack itemStack = this.items.get(1);
        ItemStack itemStack2 = this.items.get(0);
        boolean bl3 = !itemStack2.isEmpty();
        boolean bl4 = !itemStack.isEmpty();
        if (this.litDuration == 0) {
            this.litDuration = this.getBurnDuration(itemStack);
        }

        if (this.isLit() || bl4 && bl3) {
            RecipeHolder<?> recipeHolder;
            if (bl3) {
                recipeHolder = this.quickCheck.getRecipeFor(new SingleRecipeInput(itemStack2), level).orElse(null);
            } else {
                recipeHolder = null;
            }

            int i = this.getMaxStackSize();
            if (!this.isLit() && canBurn(level.registryAccess(), recipeHolder, this.items, i)) {
                this.litTime = this.getBurnDuration(itemStack);
                this.litDuration = this.litTime;
                if (this.isLit()) {
                    bl2 = true;
                    if (bl4) {
                        Item item = itemStack.getItem();
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            Item item2 = item.getCraftingRemainingItem();
                            this.items.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                        }
                    }
                }
            }

            if (this.isLit() && canBurn(level.registryAccess(), recipeHolder, this.items, i)) {
                this.cookingProgress++;
                if (this.cookingProgress == this.cookingTotalTime) {
                    this.cookingProgress = 0;
                    this.cookingTotalTime = getTotalCookTime(level);
                    if (burn(level.registryAccess(), recipeHolder, this.items, i)) {
                        this.setRecipeUsed(recipeHolder);
                    }

                    bl2 = true;
                }
            } else {
                this.cookingProgress = 0;
            }
        } else if (!this.isLit() && this.cookingProgress > 0) {
            this.cookingProgress = Mth.clamp(this.cookingProgress - 2, 0, this.cookingTotalTime);
        }

        if (isLit != this.isLit()) {
            bl2 = true;
        }

        if (bl2) {
            setChanged();
        }
    }

    private static boolean canBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipeHolder, NonNullList<ItemStack> nonNullList, int i) {
        if (!nonNullList.get(0).isEmpty() && recipeHolder != null) {
            ItemStack itemStack = recipeHolder.value().getResultItem(registryAccess);
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = nonNullList.get(2);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItemSameComponents(itemStack2, itemStack)) {
                    return false;
                } else {
                    return itemStack2.getCount() < i && itemStack2.getCount() < itemStack2.getMaxStackSize() || itemStack2.getCount() < itemStack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private static boolean burn(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipeHolder, NonNullList<ItemStack> nonNullList, int i) {
        if (recipeHolder != null && canBurn(registryAccess, recipeHolder, nonNullList, i)) {
            ItemStack itemStack = nonNullList.get(0);
            ItemStack itemStack2 = recipeHolder.value().getResultItem(registryAccess);
            ItemStack itemStack3 = nonNullList.get(2);
            if (itemStack3.isEmpty()) {
                nonNullList.set(2, itemStack2.copy());
            } else if (ItemStack.isSameItemSameComponents(itemStack3, itemStack2)) {
                itemStack3.grow(1);
            }

            if (itemStack.is(Blocks.WET_SPONGE.asItem()) && !nonNullList.get(1).isEmpty() && nonNullList.get(1).is(Items.BUCKET)) {
                nonNullList.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeHolder) {
        if (recipeHolder != null) {
            ResourceLocation resourceLocation = recipeHolder.id();
            this.recipesUsed.addTo(resourceLocation, 1);
        }
    }

    protected int getBurnDuration(ItemStack itemStack) {
        return PocketMachinesHelper.INSTANCE.getBurnTime(itemStack);
    }

    private int getTotalCookTime(Level level) {
        SingleRecipeInput singleRecipeInput = new SingleRecipeInput(this.getItem(0));
        return this.quickCheck
                .getRecipeFor(singleRecipeInput, level)
                .map(recipeHolder -> recipeHolder.value().getCookingTime())
                .orElse(200);
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
    public @NotNull ItemStack getItem(int pIndex) {
        return this.items.get(pIndex);
    }

    @Override
    public @NotNull ItemStack removeItem(int pIndex, int pCount) {
        return ContainerHelper.removeItem(this.items, pIndex, pCount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.items, pIndex);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        ItemStack itemStack2 = this.items.get(i);
        boolean bl = !itemStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack2, itemStack);
        this.items.set(i, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
        if (i == 0 && !bl) {
            this.cookingTotalTime = 200;
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int i, @NotNull ItemStack itemStack) {
        if (i == 2) {
            return false;
        } else if (i != 1) {
            return true;
        } else {
            ItemStack itemStack2 = this.items.get(1);
            return PocketMachinesHelper.INSTANCE.getBurnTime(itemStack) > 0 || itemStack.is(Items.BUCKET) && !itemStack2.is(Items.BUCKET);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

}
