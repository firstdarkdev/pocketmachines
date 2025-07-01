package com.hypherionmc.pocketmachines.mixin;

import com.hypherionmc.pocketmachines.common.inventory.PocketChestInventory;
import com.hypherionmc.pocketmachines.common.setup.ModItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {

    @Shadow @Final public Container container;

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void injectSlotCheck(ItemStack arg, CallbackInfoReturnable<Boolean> cir) {
        if (this.container instanceof PocketChestInventory) {
            if (arg.is(ModItems.POCKET_CHEST.get()))
                cir.setReturnValue(false);
        }
    }

}
