package com.hypherionmc.pocketmachines.common.inventory;

import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public interface ISaveableContainer {
    void save(@NotNull ValueOutput input);
}
