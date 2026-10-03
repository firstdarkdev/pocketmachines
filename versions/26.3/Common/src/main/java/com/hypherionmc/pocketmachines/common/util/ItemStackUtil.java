package com.hypherionmc.pocketmachines.common.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class ItemStackUtil {

    public static ItemStack getStackFromTemplate(ItemStackTemplate template) {
        return template != null ? template.create() : ItemStack.EMPTY;
    }

}
