package com.hypherionmc.pocketmachines.platform;

import com.google.auto.service.AutoService;
import com.hypherionmc.pocketmachines.mixin.accessor.MenuTypeAccess;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.function.BiFunction;

@AutoService(PocketMachinesHelper.class)
public class FabricPocketMachinesHelper implements PocketMachinesHelper {

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> creator, FeatureFlagSet flags) {
        return MenuTypeAccess.pocketmachines_create(creator::apply, flags);
    }

    @Override
    public ItemStackTemplate getCraftingRemainder(ItemStack stack) {
        FabricItem fabricItem = (FabricItem) stack.getItem();
        return fabricItem.getCraftingRemainder(stack);
    }
}
