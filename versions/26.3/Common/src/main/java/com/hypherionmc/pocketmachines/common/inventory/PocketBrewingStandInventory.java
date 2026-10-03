package com.hypherionmc.pocketmachines.common.inventory;

import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import com.hypherionmc.pocketmachines.platform.PocketMachinesHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BrewingFuel;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PocketBrewingStandInventory extends SimpleContainer implements MenuProvider, ISaveableContainer {

    private static final Component NAME = Component.translatable("item.pocketmachines.pocket_brewing_stand");
    private Item ingredient;
    int brewTime, totalBrewTime, fuel, totalFuel;
    float speedMultiplier = 1.0f;
    private final RecipeManager.CachedCheck<BrewingInput, BrewingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.BREWING);

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int i) {
            return switch (i) {
                case 0 -> brewTime;
                case 1 -> fuel;
                case 2 -> totalBrewTime;
                case 3 -> totalFuel;
                default -> 0;
            };
        }

        @Override
        public void set(int i, int j) {
            switch (i) {
                case 0 -> brewTime = j;
                case 1 -> fuel = j;
                case 2 -> totalBrewTime = j;
                case 3 -> totalFuel = j;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public PocketBrewingStandInventory(ValueInput input) {
        super(5);
        if (input != null)
            this.load(input);
    }

    public PocketBrewingStandInventory() {
        this(null);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return NAME;
    }

    protected int getUses(ServerLevel level, BrewingFuel brewingFuel, BlockPos pos) {
        return brewingFuel.uses().get(getLootContext(level, pos), 0);
    }

    protected float getSpeedMultiplier(ServerLevel level, BrewingFuel brewingFuel, BlockPos pos) {
        return brewingFuel.speedMultiplier().get(getLootContext(level, pos), 1.0f);
    }

    public void tick(ServerLevel level, BlockPos pos) {
        NonNullList<ItemStack> items = getItems();
        ItemStack fuel = items.get(4);
        BrewingFuel brewingFuel = fuel.get(DataComponents.BREWING_FUEL);

        if (this.fuel <= 0 && brewingFuel != null) {
            this.fuel = getUses(level, brewingFuel, pos);
            this.totalFuel = this.fuel;
            this.speedMultiplier = getSpeedMultiplier(level, brewingFuel, pos);
            ItemStackTemplate fuelRemainder = fuel.getItem().getCraftingRemainder();
            ItemStack newFuel = fuel;
            fuel.shrink(1);

            if (fuelRemainder != null) {
                if (fuel.isEmpty()) {
                    newFuel = fuelRemainder.create();
                } else {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), fuelRemainder.create());
                }
            }

            items.set(4, newFuel);
            setChanged();
        }

        boolean brewable = isBrewable(level);
        boolean isBrewing = this.brewTime > 0;
        ItemStack ingredient = items.get(3);

        if (isBrewing) {
            --this.brewTime;
            boolean isDoneBrewing = this.brewTime == 0;
            if (isDoneBrewing && brewable) {
                doBrew(level, pos);
            } else if (!brewable || !ingredient.is(this.ingredient)) {
                this.brewTime = 0;
            }

            setChanged();
        } else if (brewable && this.fuel > 0) {
            float speedMultiplier = this.speedMultiplier > 0.0F ? this.speedMultiplier : 1.0F;
            --this.fuel;
            this.brewTime = (int)Math.ceil((double) 400.0F / speedMultiplier);
            this.totalBrewTime = this.brewTime;
            this.ingredient = ingredient.getItem();
            setChanged();
        }
    }

    private boolean isBrewable(ServerLevel level) {
        NonNullList<ItemStack> items = getItems();
        ItemStack ingredient = items.get(3);

        if (ingredient.isEmpty()) return false;

        RecipeManager recipeManager = level.recipeAccess();
        if (!recipeManager.propertySet(RecipePropertySet.BREWING_REAGENTS).test(ingredient)) return false;

        for (int dest = 0; dest < 3; ++dest) {
            ItemStack stack = items.get(dest);
            if (!stack.isEmpty()) {
                Optional<RecipeHolder<BrewingRecipe>> recipe = quickCheck.getRecipeFor(new BrewingInput(stack, ingredient), level);
                if (recipe.isPresent()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void doBrew(final ServerLevel level, final BlockPos pos) {
        NonNullList<ItemStack> items = getItems();
        ItemStack ingredient = items.get(3);

        for(int dest = 0; dest < 3; ++dest) {
            ItemStack container = items.get(dest);
            BrewingInput input = new BrewingInput(container, ingredient);
            Optional<RecipeHolder<BrewingRecipe>> recipe = quickCheck.getRecipeFor(input, level);
            items.set(dest, recipe.isPresent() ? ((recipe.get()).value()).assemble(input) : container);
        }

        ItemStackTemplate remainder = PocketMachinesHelper.INSTANCE.getCraftingRemainder(ingredient);
        ingredient.shrink(1);
        if (remainder != null) {
            if (ingredient.isEmpty()) {
                ingredient = remainder.create();
            } else {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remainder.create());
            }
        }

        items.set(3, ingredient);
        level.levelEvent(1035, pos, 0);
    }

    public void load(ValueInput compoundTag) {
        this.clearContent();
        ContainerHelper.loadAllItems(compoundTag, this.getItems());
        this.brewTime = compoundTag.getShortOr("BrewTime", (short) 0);
        this.totalBrewTime = compoundTag.getShortOr("TotalBrewTime", (short) 0);
        if (this.brewTime > 0) {
            this.ingredient = this.getItems().get(3).getItem();
        }

        this.fuel = compoundTag.getByteOr("Fuel", (byte) 0);
        this.totalFuel = compoundTag.getByteOr("TotalFuel", (byte) 0);
        this.speedMultiplier = compoundTag.getFloatOr("speed_multiplier", 1.0F);
    }

    @Override
    public void save(@NotNull ValueOutput compoundTag) {
        compoundTag.putShort("BrewTime", (short)this.brewTime);
        compoundTag.putShort("TotalBrewTime", (short)this.totalBrewTime);
        ContainerHelper.saveAllItems(compoundTag, this.getItems());
        compoundTag.putByte("Fuel", (byte)this.fuel);
        compoundTag.putShort("TotalFuel", (short)this.totalFuel);
        compoundTag.putFloat("speed_multiplier", this.speedMultiplier);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if (slot == 4) {
            return itemStack.has(DataComponents.BREWING_FUEL);
        } else if (PersistedMachines.getLevel() == null) {
            return false;
        } else {
            RecipeAccess recipeAccess = PersistedMachines.getLevel().recipeAccess();
            return slot == 3 ? recipeAccess.propertySet(RecipePropertySet.BREWING_REAGENTS).test(itemStack) : PotionIngredient.isPotionInput(itemStack, recipeAccess) && this.getItem(slot).isEmpty();
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        PersistedMachines.markDirty();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new BrewingStandMenu(containerId, inventory, this, this.dataAccess);
    }

    protected LootContext getLootContext(ServerLevel level, BlockPos pos) {
        return new LootContext.Builder(
                new LootParams.Builder(level)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                        .withParameter(LootContextParams.BLOCK_STATE, Blocks.BREWING_STAND.defaultBlockState()
                                .setValue(BrewingStandBlock.HAS_BOTTLE[0], !getItems().get(0).isEmpty())
                                .setValue(BrewingStandBlock.HAS_BOTTLE[1], !getItems().get(1).isEmpty())
                                .setValue(BrewingStandBlock.HAS_BOTTLE[2], !getItems().get(2).isEmpty())
                        )
                        .create(LootContextParamSets.BLOCK)
        ).create(Optional.empty());
    }
}
