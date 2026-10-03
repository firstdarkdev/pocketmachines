package com.hypherionmc.pocketmachines.client.setup;

import com.hypherionmc.pocketmachines.client.screen.PocketBlastFurnaceScreen;
import com.hypherionmc.pocketmachines.client.screen.PocketFurnaceScreen;
import com.hypherionmc.pocketmachines.common.setup.ModContainers;
import com.hypherionmc.pocketmachines.mixin.accessor.MenuScreensAccess;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;

public class ModScreens {

    static void loadAll() {
        MenuScreensAccess.pocketmachines_register(ModContainers.FURNACE.get(), PocketFurnaceScreen::new);
        MenuScreensAccess.pocketmachines_register(ModContainers.BLAST_FURNACE.get(), PocketBlastFurnaceScreen::new);
        MenuScreensAccess.pocketmachines_register(ModContainers.GENERIC_9x6.get(), ContainerScreen::new);
    }

}
