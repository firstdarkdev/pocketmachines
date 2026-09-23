package com.hypherionmc.pocketmachines.common.inventory.base;

import com.google.common.collect.Lists;
import com.hypherionmc.pocketmachines.common.inventory.ISaveableContainer;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import com.hypherionmc.pocketmachines.platform.PocketMachinesHelper;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractPocketFurnaceInventory extends SimpleContainer implements MenuProvider, ISaveableContainer {

    private static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
    //protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private int litTimeRemaining, litTotalTime, cookingTimer, cookingTotalTime;
    private float speedMultiplier = 1.0f;
    private final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;
    protected final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final Component name;

    public final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> litTimeRemaining;
                case 1 -> litTotalTime;
                case 2 -> cookingTimer;
                case 3 -> cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    litTimeRemaining = value;
                    break;
                case 1:
                    litTotalTime = value;
                    break;
                case 2:
                    cookingTimer = value;
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

    public AbstractPocketFurnaceInventory(RecipeType<? extends AbstractCookingRecipe> recipeType, ValueInput valueInput, Component name) {
        super(3);
        this.recipeType = recipeType;
        this.name = name;

        quickCheck = RecipeManager.createCheck(recipeType);

        if (valueInput != null)
            this.load(valueInput);
    }

    public AbstractPocketFurnaceInventory(RecipeType<? extends AbstractCookingRecipe> recipeType, Component name) {
        this(recipeType, null, name);
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

    public void load(ValueInput tag) {
        this.clearContent();
        ContainerHelper.loadAllItems(tag, this.getItems());
        this.litTimeRemaining = tag.getShortOr("BurnTime", (short) 0);
        this.cookingTimer = tag.getShortOr("CookTime", (short) 0);
        this.cookingTotalTime = tag.getShortOr("CookTimeTotal", (short) 0);
        this.litTotalTime = tag.getShortOr("LitTotalTime", (short) 0);
        this.recipesUsed.clear();
        this.recipesUsed.putAll(tag.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
    }

    public void save(@NotNull ValueOutput tag) {
        tag.putShort("BurnTime", (short)this.litTimeRemaining);
        tag.putShort("CookTime", (short)this.cookingTimer);
        tag.putShort("CookTimeTotal", (short)this.cookingTotalTime);
        tag.putShort("LitTotalTime", (short)this.litTotalTime);
        ContainerHelper.saveAllItems(tag, this.getItems());
        tag.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
    }

    public void tick(ServerLevel level, BlockPos pos) {
        boolean changed = false;
        boolean isLit;
        boolean wasLit;

        if (this.litTimeRemaining > 0) {
            wasLit = true;
            --this.litTimeRemaining;
            isLit = this.litTimeRemaining > 0;
        } else {
            wasLit = false;
            isLit = false;
        }

        ItemStack fuel = getItems().get(1);
        ItemStack ingredient = getItems().get(0);
        boolean hasIngredient = !ingredient.isEmpty();
        boolean hasFuel = !fuel.isEmpty();

        if (isLit || hasFuel && hasIngredient) {
            if (hasIngredient) {
                SingleRecipeInput input = new SingleRecipeInput(ingredient);
                RecipeHolder<? extends AbstractCookingRecipe> recipe = quickCheck.getRecipeFor(input, level).orElse(null);

                if (recipe != null) {
                    int maxStackSize = getMaxStackSize();
                    ItemStack burnResult = recipe.value().assemble(input);

                    if (!burnResult.isEmpty() && canBurn(getItems(), maxStackSize, burnResult)) {
                        if (!isLit) {
                            int newLitTime = getBurnDuration(level, fuel, pos);
                            float newSpeedMultiplier = getSpeedMultiplier(level, fuel, pos);
                            this.litTimeRemaining = newLitTime;
                            this.litTotalTime = newLitTime;
                            this.speedMultiplier = newSpeedMultiplier;

                            if (this.cookingTotalTime > 0 && this.cookingTimer < this.cookingTotalTime) {
                                float completionRatio = (float)this.cookingTimer / (float)this.cookingTotalTime;
                                this.cookingTotalTime = getTotalCookTime(recipe);
                                this.cookingTimer = (int)Math.ceil(completionRatio * (float)this.cookingTotalTime);
                            }

                            if (newLitTime > 0) {
                                consumeFuel(level, pos, getItems(), fuel);
                                isLit = true;
                                changed = true;
                            }
                        }

                        if (isLit) {
                            ++this.cookingTimer;

                            if (this.cookingTimer >= this.cookingTotalTime) {
                                this.cookingTimer = 0;
                                this.cookingTotalTime = getTotalCookTime(recipe);
                                burn(getItems(), ingredient, burnResult);
                                setRecipeUsed(recipe);
                                changed = true;
                            }
                        } else {
                            this.cookingTimer = 0;
                        }
                    } else {
                        this.cookingTimer = 0;
                    }
                }
            } else {
                this.cookingTimer = 0;
            }
        } else if (this.cookingTimer > 0) {
            this.cookingTimer = Mth.clamp(this.cookingTimer - 2, 0, this.cookingTotalTime);
        }

        if (wasLit != isLit) {
            changed = true;
        }

        if (changed) {
            setChanged();
        }
    }

    private void consumeFuel(ServerLevel level, BlockPos pos, NonNullList<ItemStack> items, ItemStack fuel) {
        ItemStackTemplate remainder = PocketMachinesHelper.INSTANCE.getCraftingRemainder(fuel);
        ItemStack newFuel = fuel;
        fuel.shrink(1);

        if (remainder != null) {
            if (fuel.isEmpty()) {
                newFuel = remainder.create();
            } else {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remainder.create());
            }
        }

        items.set(1, newFuel);
    }

    private boolean canBurn(NonNullList<ItemStack> items, int maxStackSize, ItemStack burnResult) {
        ItemStack resultItemStack = items.get(2);

        if (resultItemStack.isEmpty()) {
            return true;
        } else if (!ItemStack.isSameItemSameComponents(resultItemStack, burnResult)) {
            return false;
        } else {
            int resultCount = resultItemStack.getCount() + burnResult.getCount();
            int maxResultCount = Math.min(maxStackSize, burnResult.getMaxStackSize());
            return resultCount <= maxResultCount;
        }
    }

    private void burn(NonNullList<ItemStack> items, ItemStack inputItemStack, ItemStack result) {
        ItemStack resultItemStack = items.get(2);
        if (resultItemStack.isEmpty()) {
            items.set(2, result.copy());
        } else {
            resultItemStack.grow(result.getCount());
        }

        if (inputItemStack.is(Items.WET_SPONGE) && !items.get(1).isEmpty() && items.get(1).is(Items.BUCKET)) {
            items.set(1, new ItemStack(Items.WATER_BUCKET));
        }

        inputItemStack.shrink(1);
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeHolder) {
        if (recipeHolder != null) {
            this.recipesUsed.addTo(recipeHolder.id(), 1);
        }
    }

    protected int getBurnDuration(ServerLevel level, ItemStack fuel, BlockPos pos) {
        return ResolvableInt.getFromItem(fuel, DataComponents.COOKING_FUEL, CookingFuel::burnTime, getLootContext(level, pos), 0);
    }

    protected float getSpeedMultiplier(ServerLevel level, ItemStack fuel, BlockPos pos) {
        return ResolvableFloat.getFromItem(fuel, DataComponents.COOKING_FUEL, CookingFuel::speedMultiplier, getLootContext(level, pos), 1.0f);
    }

    private int getTotalCookTime(RecipeHolder<? extends AbstractCookingRecipe> recipe) {
        int cookingTotalTime = recipe.value().cookingTime();
        return this.speedMultiplier > 0.0F ? (int)Math.ceil((float)cookingTotalTime / this.speedMultiplier) : cookingTotalTime;
    }

    private int getTotalCookTime(ServerLevel level) {
        SingleRecipeInput input = new SingleRecipeInput(getItem(0));
        return quickCheck.getRecipeFor(input, level).map(this::getTotalCookTime).orElse(200);
    }

    @Override
    public void setChanged() {
        PersistedMachines.markDirty();
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.setItem(slot, itemStack, false);
    }

    public void setItem(int slot, ItemStack itemStack, boolean insideTransaction) {
        ItemStack oldStack = this.getItems().get(slot);
        boolean same = !itemStack.isEmpty() && ItemStack.isSameItemSameComponents(oldStack, itemStack);
        this.getItems().set(slot, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
        if (slot == 0 && !same) {
            Level level = PersistedMachines.getLevel();
            if (level instanceof ServerLevel serverLevel) {
                if (!insideTransaction) {
                    this.cookingTotalTime = getTotalCookTime(serverLevel);
                    this.cookingTimer = 0;
                    this.setChanged();
                }
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if (slot == 2) {
            return false;
        } else if (slot != 1) {
            return true;
        } else {
            ItemStack fuelSlot = this.getItems().get(1);
            return itemStack.has(DataComponents.COOKING_FUEL) || itemStack.is(Items.BUCKET) && !fuelSlot.is(Items.BUCKET);
        }
    }

    protected LootContext getLootContext(ServerLevel level, BlockPos pos) {
        return new LootContext.Builder(
                new LootParams.Builder(level)
                        .create(LootContextParamSets.EMPTY)
        ).create(Optional.empty());
    }

    public boolean isLit() {
        return this.litTimeRemaining > 0;
    }

    public void awardUsedRecipesAndPopExperience(final ServerPlayer player) {
        List<RecipeHolder<?>> recipesToAward = this.getRecipesToAwardAndPopExperience(player.level(), player.position());
        player.awardRecipes(recipesToAward);

        for(RecipeHolder<?> recipe : recipesToAward) {
            player.triggerRecipeCrafted(recipe, this.getItems());
        }

        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(final ServerLevel level, final Vec3 position) {
        List<RecipeHolder<?>> recipesToAward = Lists.newArrayList();

        for (Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.reference2IntEntrySet()) {
            level.recipeAccess().byKey(entry.getKey()).ifPresent((recipe) -> {
                recipesToAward.add(recipe);
                createExperience(level, position, entry.getIntValue(), ((AbstractCookingRecipe) recipe.value()).experience());
            });
        }

        return recipesToAward;
    }

    private static void createExperience(final ServerLevel level, final Vec3 position, final int amount, final float value) {
        int xpReward = Mth.floor((float)amount * value);
        float xpFraction = Mth.frac((float)amount * value);
        if (xpFraction != 0.0F && level.getRandom().nextFloat() < xpFraction) {
            ++xpReward;
        }

        ExperienceOrb.award(level, position, xpReward);
    }
}
