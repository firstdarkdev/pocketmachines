package com.hypherionmc.pocketmachines;

import com.hypherionmc.pocketmachines.client.setup.ClientSetup;
import com.hypherionmc.pocketmachines.common.setup.CommonSetup;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@Mod(ModConstants.MOD_ID)
public class PocketMachinesNeoforge {

    public PocketMachinesNeoforge(IEventBus modEventBus) {
        CommonSetup.registerCommon();
        modEventBus.addListener(this::clientInit);
        NeoForge.EVENT_BUS.register(this);
    }

    public void clientInit(FMLClientSetupEvent event) {
        ClientSetup.registerClient();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        PersistedMachines.resetAll();
        PersistedMachines.setInstance(event.getServer().overworld());
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        PersistedMachines.resetAll();
    }

}