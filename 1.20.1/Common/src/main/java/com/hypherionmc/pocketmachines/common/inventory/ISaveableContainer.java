package com.hypherionmc.pocketmachines.common.inventory;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public interface ISaveableContainer {
    void save(@NotNull CompoundTag tag);
}
