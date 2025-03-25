package com.hypherionmc.pocketmachines.common.items.base;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BasePocketGuiItem extends Item {

    public BasePocketGuiItem(ResourceKey<Item> key) {
        super(new Properties().stacksTo(1).fireResistant().setId(key));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide && !player.isCrouching()) {
            this.openMenu(level, player, hand);
        }

        return InteractionResult.SUCCESS;
    }

    public abstract void openMenu(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand);

}
