package com.hypherionmc.pocketmachines.common.inventory;

import com.hypherionmc.pocketmachines.common.inventory.base.AbstractPocketFurnaceInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PocketSmokerInventory extends AbstractPocketFurnaceInventory {

    public PocketSmokerInventory(ValueInput input) {
        super(RecipeType.SMOKING, input, Component.translatable("item.pocketmachines.pocket_smoker"));
    }

    public PocketSmokerInventory() {
        super(RecipeType.SMOKING, Component.translatable("item.pocketmachines.pocket_smoker"));
    }

    @Override
    protected LootContext getLootContext(ServerLevel level, BlockPos pos) {
        return new LootContext.Builder(
                new LootParams.Builder(level)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                        .withParameter(LootContextParams.BLOCK_STATE, Blocks.SMOKER.defaultBlockState().setValue(BlockStateProperties.LIT, true))
                        .create(LootContextParamSets.BLOCK)
        ).create(Optional.empty());
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new SmokerMenu(containerId, inventory, this, this.containerData);
    }
}
