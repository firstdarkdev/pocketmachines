package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.inventory.PocketBlastFurnaceInventory;
import com.hypherionmc.pocketmachines.common.items.base.BaseTickablePocketItem;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PocketBlastFurnace extends BaseTickablePocketItem<PocketBlastFurnaceInventory> {

    public PocketBlastFurnace() {
        super(PersistedMachines.POCKET_BLAST_FURNACE, "TG_BLAST_FURNACE_INDEX");
    }

    @Override
    public void openScreen(PocketBlastFurnaceInventory container, Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.openMenu(container);
    }

    @Override
    public void tickItem(PocketBlastFurnaceInventory container, @NotNull ItemStack stack, Level level, @NotNull Entity entity, int itemSlow, boolean isSelected) {
        container.tick(level);
    }

}
