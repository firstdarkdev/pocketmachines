package com.hypherionmc.pocketmachines.common.setup;

import com.hypherionmc.pocketmachines.ModConstants;
import com.hypherionmc.pocketmachines.common.items.*;
import com.hypherionmc.pocketmachines.reg.RegistrationProvider;
import com.hypherionmc.pocketmachines.reg.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ModItems {

    static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(BuiltInRegistries.ITEM, ModConstants.MOD_ID);

    // Furnaces
    public static RegistryObject<PocketFurnace> POCKET_FURNACE = ITEMS.register("pocket_furnace", PocketFurnace::new);
    public static RegistryObject<PocketBlastFurnace> POCKET_BLAST_FURNACE = ITEMS.register("pocket_blast_furnace", PocketBlastFurnace::new);
    public static RegistryObject<PocketSmoker> POCKET_SMOKER = ITEMS.register("pocket_smoker", PocketSmoker::new);

    // Chests
    public static RegistryObject<PocketChest> POCKET_CHEST = ITEMS.register("pocket_chest", PocketChest::new);
    public static RegistryObject<PocketEnderChest> POCKET_ENDER_CHEST = ITEMS.register("pocket_ender_chest", PocketEnderChest::new);

    // Other
    public static RegistryObject<PocketAnvil> POCKET_ANVIL = ITEMS.register("pocket_anvil", PocketAnvil::new);
    public static RegistryObject<PocketCraftingTable> POCKET_CRAFTER = ITEMS.register("pocket_crafting", PocketCraftingTable::new);
    public static RegistryObject<PocketBrewingStand> POCKET_BREWING_STAND = ITEMS.register("pocket_brewing_stand", PocketBrewingStand::new);
    public static RegistryObject<PocketEnchanter> POCKET_ENCHANTER = ITEMS.register("pocket_enchanting_table", PocketEnchanter::new);
    public static RegistryObject<PocketGrindstone> POCKET_GRINDSTONE = ITEMS.register("pocket_grindstone", PocketGrindstone::new);
    public static RegistryObject<PocketStonecutter> POCKET_STONECUTTER = ITEMS.register("pocket_stonecutter", PocketStonecutter::new);
    public static RegistryObject<PocketCartographyTable> POCKET_CARTOGRAPHY_TABLE = ITEMS.register("pocket_cartography_table", PocketCartographyTable::new);
    public static RegistryObject<PocketSmithingTable> POCKET_SMITHING_TABLE = ITEMS.register("pocket_smithing_table", PocketSmithingTable::new);
    public static RegistryObject<PocketLoom> POCKET_LOOM = ITEMS.register("pocket_loom", PocketLoom::new);

    // Internal Stuff
    static void loadAll() {}

    public static List<ItemStack> getTabStacks() {
        return ITEMS.getEntries().stream().map(r -> r.get().getDefaultInstance()).toList();
    }
}
