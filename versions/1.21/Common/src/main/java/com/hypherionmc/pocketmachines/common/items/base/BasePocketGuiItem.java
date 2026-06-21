package com.hypherionmc.pocketmachines.common.items.base;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BasePocketGuiItem extends Item {

    public BasePocketGuiItem() {
        super(new Properties().stacksTo(1).fireResistant());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide && !player.isCrouching()) {
            this.openMenu(level, player, hand);
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public abstract void openMenu(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand);


}
