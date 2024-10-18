package com.hypherionmc.pocketmachines;

import com.hypherionmc.pocketmachines.common.setup.CommonSetup;
import com.hypherionmc.pocketmachines.common.setup.ModItems;
import com.hypherionmc.pocketmachines.common.setup.ModTabs;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

public class PocketMachinesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonSetup.registerCommon();

        ServerWorldEvents.LOAD.register((minecraftServer, serverLevel) -> {
            if (minecraftServer.overworld() == serverLevel) {
                PersistedMachines.resetAll();
                PersistedMachines.setInstance(minecraftServer.overworld());
            }
        });

        ServerWorldEvents.UNLOAD.register((minecraftServer, serverLevel) -> {
            if (serverLevel == minecraftServer.overworld()) {
                PersistedMachines.resetAll();
            }
        });

        //ItemGroupEvents.modifyEntriesEvent(ModTabs.MOD_TAB.getResourceKey()).register(t -> t.acceptAll(ModItems.getTabStacks()));
    }
}
