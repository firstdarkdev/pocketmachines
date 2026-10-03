package com.hypherionmc.pocketmachines.common.inventory;

import com.hypherionmc.pocketmachines.common.inventory.base.AbstractPocketFurnaceInventory;
import com.hypherionmc.pocketmachines.common.menus.PocketBlastFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
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

public class PocketBlastFurnaceInventory extends AbstractPocketFurnaceInventory {

    public PocketBlastFurnaceInventory(ValueInput input) {
        super(RecipeType.BLASTING, input, Component.translatable("item.pocketmachines.pocket_blast_furnace"));
    }

    public PocketBlastFurnaceInventory() {
        super(RecipeType.BLASTING, Component.translatable("item.pocketmachines.pocket_blast_furnace"));
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new PocketBlastFurnaceMenu(containerId, inventory, this, this.containerData);
    }

    @Override
    protected LootContext getLootContext(ServerLevel level, BlockPos pos) {
        return new LootContext.Builder(
                new LootParams.Builder(level)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                        .withParameter(LootContextParams.BLOCK_STATE, Blocks.BLAST_FURNACE.defaultBlockState().setValue(BlockStateProperties.LIT, true))
                        .create(LootContextParamSets.BLOCK)
        ).create(Optional.empty());
    }
}
