package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.items.base.BasePocketGuiItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.hypherionmc.pocketmachines.ModConstants.makeResourceKey;

public class PocketCraftingTable extends BasePocketGuiItem {

    public PocketCraftingTable() {
        super(makeResourceKey("pocket_crafting"));
    }

    @Override
    public void openMenu(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.openMenu(this.buildMenu(level, player.blockPosition()));
    }

    private MenuProvider buildMenu(Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, playerInventory, playerEntity) -> new CraftingMenu(containerId, playerInventory, ContainerLevelAccess.create(level, pos)) {
            @Override
            public boolean stillValid(@NotNull Player arg) {
                return true;
            }
        }, Component.translatable("item.pocketmachines.pocket_crafting"));
    }
}
