package com.hypherionmc.pocketmachines;

import com.hypherionmc.pocketmachines.common.setup.CommonSetup;
import com.hypherionmc.pocketmachines.common.setup.ModItems;
import com.hypherionmc.pocketmachines.common.setup.ModTabs;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;

public class PocketMachinesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonSetup.registerCommon();

        ServerLevelEvents.LOAD.register((minecraftServer, serverLevel) -> {
            if (minecraftServer.overworld() == serverLevel) {
                PersistedMachines.resetAll();
                PersistedMachines.setInstance(minecraftServer.overworld());
            }
        });

        ServerLevelEvents.UNLOAD.register((minecraftServer, serverLevel) -> {
            if (serverLevel == minecraftServer.overworld()) {
                PersistedMachines.resetAll();
            }
        });

        CreativeModeTabEvents.modifyOutputEvent(ModTabs.MOD_TAB.getResourceKey()).register(t -> t.acceptAll(ModItems.getTabStacks()));
    }
}
