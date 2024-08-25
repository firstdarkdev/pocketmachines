package com.hypherionmc.pocketmachines.common.setup;

import com.hypherionmc.pocketmachines.ModConstants;
import com.hypherionmc.pocketmachines.reg.RegistrationProvider;
import com.hypherionmc.pocketmachines.reg.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class ModTabs {

    private static final RegistrationProvider<CreativeModeTab> TABS = RegistrationProvider.get(BuiltInRegistries.CREATIVE_MODE_TAB, ModConstants.MOD_ID);

    public static final RegistryObject<CreativeModeTab, CreativeModeTab> MOD_TAB = TABS.register("pocket_machines", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, -1).displayItems((generator, output) -> output.acceptAll(ModItems.getTabStacks())).title(Component.translatable("itemGroup.pocket_machines")).icon(() -> ModItems.POCKET_FURNACE.get().getDefaultInstance()).build());

    static void loadAll() {}

}
