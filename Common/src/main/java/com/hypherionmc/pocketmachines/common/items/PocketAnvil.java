package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.items.base.BasePocketGuiItem;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.hypherionmc.pocketmachines.ModConstants.makeResourceKey;

public class PocketAnvil extends BasePocketGuiItem {

    private static final Component TITLE = Component.translatable("item.pocketmachines.pocket_anvil");

    public PocketAnvil() {
        super(makeResourceKey("pocket_anvil"));
    }

    @Override
    public void openMenu(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        player.openMenu(new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return TITLE;
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
                return new AnvilMenu(containerId, inventory);
            }
        });
        player.awardStat(Stats.INTERACT_WITH_ANVIL);
    }

}
