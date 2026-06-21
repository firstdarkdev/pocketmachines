package com.hypherionmc.pocketmachines.common.setup;

import com.hypherionmc.pocketmachines.ModConstants;
import com.hypherionmc.pocketmachines.common.menus.PocketBlastFurnaceMenu;
import com.hypherionmc.pocketmachines.common.menus.PocketFurnaceMenu;
import com.hypherionmc.pocketmachines.platform.PocketMachinesHelper;
import com.hypherionmc.pocketmachines.reg.RegistrationProvider;
import com.hypherionmc.pocketmachines.reg.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public class ModContainers {

    static final RegistrationProvider<MenuType<?>> CONTAINERS = RegistrationProvider.get(BuiltInRegistries.MENU, ModConstants.MOD_ID);

    public static RegistryObject<MenuType<?>, MenuType<PocketFurnaceMenu>> FURNACE = CONTAINERS.register("pocketfurnace", () -> PocketMachinesHelper.INSTANCE.createMenuType(PocketFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static RegistryObject<MenuType<?>, MenuType<PocketBlastFurnaceMenu>> BLAST_FURNACE = CONTAINERS.register("pocketblastfurnace", () -> PocketMachinesHelper.INSTANCE.createMenuType(PocketBlastFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static RegistryObject<MenuType<?>, MenuType<ChestMenu>> GENERIC_9x6 = CONTAINERS.register("generic_9x6", () -> PocketMachinesHelper.INSTANCE.createMenuType(ChestMenu::sixRows, FeatureFlags.DEFAULT_FLAGS));

    static void loadAll() {}

}
