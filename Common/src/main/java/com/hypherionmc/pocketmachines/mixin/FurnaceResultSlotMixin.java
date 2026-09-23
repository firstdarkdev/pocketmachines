package com.hypherionmc.pocketmachines.mixin;

import com.hypherionmc.pocketmachines.common.inventory.base.AbstractPocketFurnaceInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceResultSlot.class)
public class FurnaceResultSlotMixin extends Slot {

    @Final
    @Shadow
    private Player player;

    public FurnaceResultSlotMixin(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Inject(method = "checkTakeAchievements", at = @At("RETURN"))
    private void checkTakeAchievements(ItemStack carried, CallbackInfo ci) {
        if (this.player instanceof ServerPlayer serverPlayer && this.container instanceof AbstractPocketFurnaceInventory pocketFurnaceInventory) {
            pocketFurnaceInventory.awardUsedRecipesAndPopExperience(serverPlayer);
        }
    }

}
