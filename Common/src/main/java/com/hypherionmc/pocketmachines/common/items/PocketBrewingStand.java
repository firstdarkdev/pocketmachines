package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.inventory.PocketBrewingStandInventory;
import com.hypherionmc.pocketmachines.common.items.base.BaseTickablePocketItem;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PocketBrewingStand extends BaseTickablePocketItem<PocketBrewingStandInventory> {

    public PocketBrewingStand() {
        super(PersistedMachines.POCKET_BREWING_STAND, "TG_POCKET_BREWING_STAND_INDEX");
    }

    @Override
    public void openScreen(PocketBrewingStandInventory container, Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.openMenu(container);
    }

    @Override
    public void tickItem(PocketBrewingStandInventory container, @NotNull ItemStack stack, Level level, @NotNull Entity entity, int itemSlow, boolean isSelected) {
        container.tick(level);
    }
}
