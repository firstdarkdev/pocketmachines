package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.items.base.BasePocketGuiItem;
import lombok.NoArgsConstructor;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class PocketAnvil extends BasePocketGuiItem {

    private static final Component TITLE = Component.translatable("item.pocketmachines.pocket_anvil");

    @Override
    public void openMenu(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.openMenu(new SimpleMenuProvider((containerId, playerInventory, playerEntity) -> new AnvilMenu(containerId, playerInventory, ContainerLevelAccess.create(level, player.blockPosition())) {
            @Override
            public boolean stillValid(Player arg) {
                return true;
            }
        }, TITLE));

        player.awardStat(Stats.INTERACT_WITH_ANVIL);
    }

}
