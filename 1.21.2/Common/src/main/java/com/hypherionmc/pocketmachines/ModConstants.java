package com.hypherionmc.pocketmachines;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModConstants {

    public static final String MOD_ID = "pocketmachines";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ResourceKey<Item> makeResourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, name));
    }

}