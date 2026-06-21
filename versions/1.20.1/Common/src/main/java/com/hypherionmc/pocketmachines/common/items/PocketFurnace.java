package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.inventory.PocketFurnaceInventory;
import com.hypherionmc.pocketmachines.common.items.base.BaseTickablePocketItem;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PocketFurnace extends BaseTickablePocketItem<PocketFurnaceInventory> {

    public PocketFurnace() {
        super(PersistedMachines.POCKET_FURNACE, "TG_FURNACE_INDEX");
    }

    @Override
    public void openScreen(PocketFurnaceInventory container, Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.openMenu(container);
    }

    @Override
    public void tickItem(PocketFurnaceInventory container, @NotNull ItemStack stack, Level level, @NotNull Entity entity, int itemSlow, boolean isSelected) {
        container.tick(level);

        CompoundTag tag = stack.getOrCreateTag();
        boolean oldLitState = tag.getInt("CustomModelData") == 1;

        if (oldLitState != container.isLit()) {
            tag.putInt("CustomModelData", container.isLit() ? 1 : 0);
            stack.setTag(tag);
        }
    }
}
