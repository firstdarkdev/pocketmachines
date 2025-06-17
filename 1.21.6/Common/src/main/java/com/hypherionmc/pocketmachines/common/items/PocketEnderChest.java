package com.hypherionmc.pocketmachines.common.items;

import com.hypherionmc.pocketmachines.common.items.base.BasePocketGuiItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.hypherionmc.pocketmachines.ModConstants.makeResourceKey;

public class PocketEnderChest extends BasePocketGuiItem {

    private final Component TITLE = Component.translatable("item.pocketmachines.pocket_ender_chest");

    public PocketEnderChest() {
        super(makeResourceKey("pocket_ender_chest"));
    }

    @Override
    public void openMenu(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        PlayerEnderChestContainer container = player.getEnderChestInventory();

        player.openMenu(new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return TITLE;
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
                return ChestMenu.threeRows(containerId, inventory, container);
            }
        });
    }
}
