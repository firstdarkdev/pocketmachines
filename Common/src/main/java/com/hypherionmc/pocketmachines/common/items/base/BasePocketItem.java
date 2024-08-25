package com.hypherionmc.pocketmachines.common.items.base;

import com.hypherionmc.pocketmachines.common.inventory.ISaveableContainer;
import com.hypherionmc.pocketmachines.common.world.SaveHolder;
import lombok.Getter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BasePocketItem<T extends ISaveableContainer> extends Item {

    @Getter
    private final SaveHolder<T> saveHolder;
    final String NBT_KEY;

    public BasePocketItem(SaveHolder<T> saveHolder, String nbtKey) {
        super(new Properties().stacksTo(1).fireResistant());
        this.saveHolder = saveHolder;
        this.NBT_KEY = nbtKey;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level levelIn, @NotNull Player playerIn, @NotNull InteractionHand handIn) {
        if (!levelIn.isClientSide() && !playerIn.isCrouching()) {
            ItemStack stack = playerIn.getItemInHand(handIn);
            CustomData tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

            if (!tag.contains(NBT_KEY)) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, updateTag -> updateTag.putString(NBT_KEY, saveHolder.createInstance(playerIn)));
            }

            tag = stack.get(DataComponents.CUSTOM_DATA);
            CompoundTag compoundTag = tag.copyTag();
            openScreen(saveHolder.getInstance(compoundTag.getString(NBT_KEY), playerIn).getValue(), levelIn, playerIn, handIn);
        }

        return super.use(levelIn, playerIn, handIn);
    }

    public abstract void openScreen(T container, Level level, @NotNull Player player, @NotNull InteractionHand hand);
}
