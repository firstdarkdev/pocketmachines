package com.hypherionmc.pocketmachines.common.world;

import com.hypherionmc.pocketmachines.common.inventory.ISaveableContainer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public final class SaveHolder<T extends ISaveableContainer> {

    private final List<SaveItem<T>> items = new ArrayList<>();
    private final Supplier<T> constructor;
    private final Function<ValueInput, T> deserializer;
    private final String NBT_TAG_KEY;

    public String createInstance(Player player) {
        String id = generateRandomId();
        SaveItem<T> item = new SaveItem<>(constructor.get(), player.getStringUUID());
        item.setStackId(id);
        items.add(item);
        PersistedMachines.markDirty();
        return id;
    }

    public SaveItem<T> getInstance(String id, Player player) {
        return items.stream()
                .filter(i -> i.getStackId().equals(id) && i.getUserId().equals(player.getStringUUID()))
                .findFirst()
                .orElseGet(() -> {
                    SaveItem<T> item = new SaveItem<>(constructor.get(), player.getStringUUID());
                    item.setStackId(id);
                    items.add(item);
                    PersistedMachines.markDirty();
                    return item;
                });
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    ListTag write(HolderLookup.Provider provider) {
        ListTag itemsNbt = new ListTag();

        for (SaveItem<T> item : items) {
            TagValueOutput output = TagValueOutput.createWithContext(new ProblemReporter.Collector(), provider);
            item.getValue().save(output);
            CompoundTag itemTag = output.buildResult();
            itemTag.putString("UserId", item.getUserId());
            itemTag.putString("StackId", item.getStackId());
            itemsNbt.add(itemTag);
        }

        return itemsNbt;
    }

    void read(ListTag itemsNbt, HolderLookup.Provider provider) {
        for (int i = 0; i < itemsNbt.size(); i++) {
            CompoundTag compoundTag = itemsNbt.getCompoundOrEmpty(i);
            SaveItem<T> item = new SaveItem<>(deserializer.apply(TagValueInput.create(new ProblemReporter.Collector(), provider, compoundTag)), compoundTag.getStringOr("UserId", "INVALID"));
            item.setStackId(compoundTag.getStringOr("StackId", generateRandomId()));
            items.add(item);
        }
    }

    void clear() {
        items.clear();
    }

    @Getter @Setter
    @RequiredArgsConstructor
    public static final class SaveItem<T extends ISaveableContainer> {
        final T value;
        final String userId;

        String stackId;
    }

    private String generateRandomId() {
        String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder result = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int randomIndex = secureRandom.nextInt(BASE32_CHARS.length());
            result.append(BASE32_CHARS.charAt(randomIndex));
        }

        return result.toString();
    }
}
