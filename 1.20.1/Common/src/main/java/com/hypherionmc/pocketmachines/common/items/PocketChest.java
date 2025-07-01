package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.inventory.PocketChestInventory;
import com.hypherionmc.pocketmachines.common.items.base.BasePocketItem;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PocketChest extends BasePocketItem<PocketChestInventory> {

    public PocketChest() {
        super(PersistedMachines.POCKET_CHEST, "TG_CHEST");
    }

    @Override
    public void openScreen(PocketChestInventory container, Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.openMenu(container);
    }

}
