package com.hypherionmc.pocketmachines.common.world;

import com.hypherionmc.pocketmachines.common.inventory.*;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


public final class PersistedMachines extends SavedData {

    private static final String NAME = "PocketMachinesData";
    private static final List<SaveHolder<?>> registeredItems = new ArrayList<>();
    private static PersistedMachines INSTANCE;

    @Getter @Nullable
    private static ServerLevel level;

    // Holders
    public static final SaveHolder<PocketFurnaceInventory> POCKET_FURNACE = register(PocketFurnaceInventory::new, PocketFurnaceInventory::new, "TG_FURNACE");
    public static final SaveHolder<PocketBlastFurnaceInventory> POCKET_BLAST_FURNACE = register(PocketBlastFurnaceInventory::new, PocketBlastFurnaceInventory::new, "TG_BLAST_FURNACE");
    public static final SaveHolder<PocketChestInventory> POCKET_CHEST = register(PocketChestInventory::new, PocketChestInventory::new, "TG_CHEST");
    public static final SaveHolder<PocketBrewingStandInventory> POCKET_BREWING_STAND = register(PocketBrewingStandInventory::new, PocketBrewingStandInventory::new, "TG_BREWING_STAND");
    public static final SaveHolder<PocketSmokerInventory> POCKET_SMOKER = register(PocketSmokerInventory::new, PocketSmokerInventory::new, "TG_SMOKER");

    // INTERNAL METHODS
    public PersistedMachines() {}

    public static PersistedMachines load(CompoundTag tag) {
        PersistedMachines s = new PersistedMachines();
        s.read(tag);
        return s;
    }

    public static void resetAll() {
        registeredItems.forEach(SaveHolder::clear);
        INSTANCE = null;
    }

    public static void setInstance(ServerLevel level) {
        if (level == null) return;

        DimensionDataStorage dimensionDataStorage = level.getDataStorage();
        INSTANCE = dimensionDataStorage.computeIfAbsent(PersistedMachines::load, PersistedMachines::new, NAME);
        PersistedMachines.level = level;
    }

    public static boolean hasLevel() {
        return level != null;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        registeredItems.forEach(i -> i.write(nbt));
        return nbt;
    }

    void read(CompoundTag nbt) {
        registeredItems.forEach(i -> i.read(nbt));
    }

    private static <T extends ISaveableContainer> SaveHolder<T> register(Supplier<T> constuctor, Function<CompoundTag, T> deserializer, String tag) {
        SaveHolder<T> holder = new SaveHolder<>(constuctor, deserializer, tag);
        registeredItems.add(holder);
        return holder;
    }

    public static void markDirty() {
        if (INSTANCE != null)
            INSTANCE.setDirty();
    }
}
