package com.hypherionmc.pocketmachines.client;

import com.hypherionmc.pocketmachines.client.setup.ClientSetup;
import net.fabricmc.api.ClientModInitializer;

public class PocketMachinesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientSetup.registerClient();
    }
}
