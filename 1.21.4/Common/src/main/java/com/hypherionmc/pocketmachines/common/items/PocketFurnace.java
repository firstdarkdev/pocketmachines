package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.inventory.PocketFurnaceInventory;
import com.hypherionmc.pocketmachines.common.items.base.BaseTickablePocketItem;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.hypherionmc.pocketmachines.ModConstants.makeResourceKey;

public class PocketFurnace extends BaseTickablePocketItem<PocketFurnaceInventory> {

    public PocketFurnace() {
        super(PersistedMachines.POCKET_FURNACE, "TG_FURNACE_INDEX", makeResourceKey("pocket_furnace"));
    }

    @Override
    public void openScreen(PocketFurnaceInventory container, Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.openMenu(container);
    }

    @Override
    public void tickItem(PocketFurnaceInventory container, @NotNull ItemStack stack, Level level, @NotNull Entity entity, int itemSlow, boolean isSelected) {
        container.tick((ServerLevel) level);
        CustomModelData data = stack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.EMPTY);
        String oldLitState = data.getString(0) == null ? "unlit" : data.getString(0);
        String currentLitState = container.isLit() ? "lit" : "unlit";

        if (!oldLitState.equals(currentLitState)) {
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(currentLitState), List.of()));
        }
    }
}
