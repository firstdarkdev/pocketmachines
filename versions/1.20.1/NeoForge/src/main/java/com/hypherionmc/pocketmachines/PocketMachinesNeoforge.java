package com.hypherionmc.pocketmachines;

import com.hypherionmc.pocketmachines.client.setup.ClientSetup;
import com.hypherionmc.pocketmachines.common.setup.CommonSetup;
import com.hypherionmc.pocketmachines.common.world.PersistedMachines;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModConstants.MOD_ID)
public class PocketMachinesNeoforge {

    public PocketMachinesNeoforge() {
        CommonSetup.registerCommon();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        MinecraftForge.EVENT_BUS.register(this);
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
