package astrotibs.villagenames.handler;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.Reference;
import astrotibs.villagenames.village.chestloot.ChestGenHooks;
import astrotibs.villagenames.village.chestloot.WeightedRandomChestContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChestLootHandler {
	
	/*
	 * Chest loot handler, needed for 1.9+
	 */
	@SubscribeEvent
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		
		final LootPool lootCategory;
		//Bonus chest
		if (event.getName().equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST)) {
			lootCategory = event.getTable().getPool("main"); //Pool2 might be more on par with what I need, but every chest has a "main" type.
			// main.addEntry(new LootEntryItem(ITEM, WEIGHT, QUALITY, FUNCTIONS, CONDITIONS, NAME));
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 1, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
			
		// Village Blacksmiths
		else if (event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH)) {
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 3, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
		
		// Mineshafts
		else if (event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)) {
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 2, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
		
		// Temples
		else if (event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID)) {
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 12, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
		else if (event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE)) {
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 10, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
		else if (event.getName().equals(LootTableList.CHESTS_IGLOO_CHEST)) {
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 8, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
		
		// Strongholds
		else if (event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR)) { // Corridor
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 4, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
		else if (event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CROSSING)) { // Crossing/hub
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 15, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
		else if (event.getName().equals(LootTableList.CHESTS_STRONGHOLD_LIBRARY)) { // Library
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 20, 0, new LootFunction[] {new SetCount(new LootCondition[0], new RandomValueRange(1,3))}, new LootCondition[0], Reference.MOD_ID + ":codex")); }
		
		// Other mods -- I'm just taking a guess at the chest addresses. Neither of these mods exist above 1.7.10 anyway.
		else if (event.getName().equals("chests/oceanFloorChest") && Loader.isModLoaded("Mariculture") ) { // Mariculture
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 20, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
		else if (event.getName().equals("chests/A_WIZARD_DID_IT") && Loader.isModLoaded("Artifacts") ) { // Dragon Artifacts
			lootCategory = event.getTable().getPool("main");
			if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 20, 0, new LootFunction[] {new SetCount(new LootCondition[0], new RandomValueRange(1,4))}, new LootCondition[0], Reference.MOD_ID + ":codex")); }
	}
	
	public static void modernVillageChests()
	{
		// Changed with each chest entry
		ChestGenHooks chestGenHooks; int stacks_min; int stacks_max;
		// These values are defaults for loot population: they are assumed when not explicitly listed in the 1.14+ json loot tables.
		int def_min = 1; int def_max = 1; int def_weight = 1;
		
		
		// ------------------------------------- //
		// --- General biome-specific chests --- //
		// ------------------------------------- //
		
		
		
		// --- Desert House --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_desert_house");
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.CLAY_BALL), def_min, def_max, def_weight},
			{new ItemStack(Items.DYE, 1, 2), def_min, def_max, def_weight},
			{new ItemStack(Blocks.CACTUS), 1, 4, 10},
			{new ItemStack(Items.WHEAT), 1, 7, 10},
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.BOOK), def_min, def_max, def_weight},
			{new ItemStack(Blocks.DEADBUSH), 1, 3, 2},
			{new ItemStack(Items.EMERALD), 1, 3, def_weight},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Plains House --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_plains_house");
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.GOLD_NUGGET), 1, 3, def_weight},
			{new ItemStack(Blocks.YELLOW_FLOWER), def_min, def_max, 2},
			{new ItemStack(Blocks.RED_FLOWER), def_min, def_max, def_weight},
			{new ItemStack(Items.POTATO), 1, 7, 10},
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.APPLE), 1, 5, 10},
			{new ItemStack(Items.BOOK), def_min, def_max, def_weight},
			{new ItemStack(Items.FEATHER), def_min, def_max, def_weight},
			{new ItemStack(Items.EMERALD), 1, 4, 2},
			{new ItemStack(Blocks.SAPLING), 1, 2, 5}, // Oak sapling
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Savanna House --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_savanna_house");
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.GOLD_NUGGET), 1, 3, def_weight},
			{new ItemStack(Blocks.GRASS), def_min, def_max, 5},
			{new ItemStack(Blocks.DOUBLE_PLANT, 1, 2), def_min, def_max, 5}, // Tall grass
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.WHEAT_SEEDS), 1, 5, 10},
			{new ItemStack(Items.EMERALD), 1, 4, 2},
			{new ItemStack(Blocks.SAPLING, 1, 4), 1, 2, 10}, // Acacia
			{new ItemStack(Items.SADDLE), def_min, def_max, def_weight},
			{new ItemStack(Blocks.TORCH), 1, 2, def_weight},
			{new ItemStack(Items.BUCKET), def_min, def_max, def_weight},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Snowy House --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_snowy_house");
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			//{new ItemStack(Blocks.BLUE_ICE), def_min, def_max, def_weight}, // TODO - Blue Ice
			{new ItemStack(Blocks.SNOW), def_min, def_max, 4},
			{new ItemStack(Items.POTATO), 1, 7, 10},
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.BEETROOT_SEEDS), 1, 5, 10},
			{new ItemStack(Items.BEETROOT_SOUP), def_min, def_max, def_weight},
			{new ItemStack(Blocks.FURNACE), def_min, def_max, def_weight},
			{new ItemStack(Items.EMERALD), 1, 4, def_weight},
			{new ItemStack(Items.SNOWBALL), 1, 7, 10},
			{new ItemStack(Items.COAL), 1, 4, 5},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Taiga House --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_taiga_house");
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.IRON_NUGGET), 1, 5, def_weight}, // Iron Nugget
			{new ItemStack(Blocks.TALLGRASS, 1, 2), def_min, def_max, 2}, // Fern
			{new ItemStack(Blocks.DOUBLE_PLANT, 1, 3), def_min, def_max, 2}, // Large Fern
			{new ItemStack(Items.POTATO), 1, 7, 10},
			{ModObjects.chooseModSweetBerriesItem(), 1, 7, 5},
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.PUMPKIN_SEEDS), 1, 5, 5},
			{new ItemStack(Items.PUMPKIN_PIE), def_min, def_max, def_weight},
			{new ItemStack(Items.EMERALD), 1, 4, 2},
			{new ItemStack(Blocks.SAPLING, 1, 1), 1, 5, 5}, // Spruce Sapling
			{ModObjects.chooseModWoodenSignItem(1), def_min, def_max, def_weight}, // Spruce Sign
			{new ItemStack(Blocks.LOG, 1, 1), 1, 5, 10}, // Spruce Log
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		
		// ------------------------------ //
		// --- Specific career chests --- //
		// ------------------------------ //
		
		
		
		// --- Armorer --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_armorer");
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.IRON_INGOT), 1, 3, 2},
			{new ItemStack(Items.BREAD), 1, 4, 4},
			{new ItemStack(Items.IRON_HELMET), def_min, def_max, def_weight},
			{new ItemStack(Items.EMERALD), def_min, def_max, def_weight},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Butcher --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_butcher");
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.EMERALD), def_min, def_max, def_weight},
			{new ItemStack(Items.PORKCHOP), 1, 3, 6},
			{new ItemStack(Items.WHEAT), 1, 3, 6},
			{new ItemStack(Items.BEEF), 1, 3, 6},
			{new ItemStack(Items.MUTTON), 1, 3, 6},
			{new ItemStack(Items.COAL), 1, 3, 3},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Cartographer --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_cartographer");
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.MAP), 1, 3, 10},
			{new ItemStack(Items.PAPER), 1, 5, 15},
			{new ItemStack(Items.COMPASS), def_min, def_max, 5},
			{new ItemStack(Items.BREAD), 1, 4, 15},
			{new ItemStack(Items.STICK), 1, 2, 5},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		// Add codex
		if (GeneralConfig.codexChestLoot)
		{
			chestGenHooks.addItem(new WeightedRandomChestContent(new ItemStack(ModItems.CODEX), def_min, 2, 4));
		}
		
		
		
		// --- Fisher Cottage --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_fisher");
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.EMERALD), def_min, def_max, def_weight},
			{new ItemStack(Items.FISH), 1, 3, 2},
			{new ItemStack(Items.FISH, 1, 1), 1, 3, def_weight},
			{new ItemStack(Items.WATER_BUCKET), 1, 3, def_weight},
			{ModObjects.chooseModBarrelItem(), 1, 3, def_weight},
			{new ItemStack(Items.CLAY_BALL), def_min, def_max, def_weight},
			{new ItemStack(Items.WHEAT_SEEDS), 1, 3, 3},
			{new ItemStack(Items.COAL), 1, 3, 2},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Fletcher --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_fletcher");
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.EMERALD), def_min, def_max, def_weight},
			{new ItemStack(Items.ARROW), 1, 3, 2},
			{new ItemStack(Items.FEATHER), 1, 3, 6},
			{new ItemStack(Items.EGG), 1, 3, 2},
			{new ItemStack(Items.FLINT), 1, 3, 6},
			{new ItemStack(Items.STICK), 1, 3, 6},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Mason --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_mason");
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		IBlockState smoothStoneBlockState = ModObjects.chooseModSmoothStoneBlockState();
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.CLAY_BALL), 1, 3, def_weight},
			{new ItemStack(Items.FLOWER_POT), def_min, def_max, def_weight},
			{new ItemStack(Blocks.STONE), def_min, def_max, 2},
			{new ItemStack(Blocks.STONEBRICK), def_min, def_max, 2},
			{new ItemStack(Items.BREAD), 1, 4, 4},
			{new ItemStack(Items.DYE, 1, 11), def_min, def_max, def_weight},
			{new ItemStack(smoothStoneBlockState.getBlock(), 1, smoothStoneBlockState.getBlock().getMetaFromState(smoothStoneBlockState)), def_min, def_max, def_weight},
			{new ItemStack(Items.EMERALD), def_min, def_max, def_weight},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		// Add codex
		if (GeneralConfig.codexChestLoot)
		{
			chestGenHooks.addItem(new WeightedRandomChestContent(new ItemStack(ModItems.CODEX), def_min, def_max, def_weight));
		}
		
		
		
		// --- Shepherd --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_shepherd");
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Blocks.WOOL, 1, 0), 1, 8, 6}, // White
			{new ItemStack(Blocks.WOOL, 1, 15), 1, 3, 3}, // Black
			{new ItemStack(Blocks.WOOL, 1, 7), 1, 3, 2}, // Gray
			{new ItemStack(Blocks.WOOL, 1, 12), 1, 3, 2}, // Brown
			{new ItemStack(Blocks.WOOL, 1, 8), 1, 3, 2}, // Light Gray
			{new ItemStack(Items.EMERALD), def_min, def_max, def_weight},
			{new ItemStack(Items.SHEARS), def_min, def_max, def_weight},
			{new ItemStack(Items.WHEAT), 1, 6, 6},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Tannery --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_tannery");
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.LEATHER), 1, 3, def_weight},
			{new ItemStack(Items.LEATHER_CHESTPLATE), def_min, def_max, 2},
			{new ItemStack(Items.LEATHER_BOOTS), def_min, def_max, 2},
			{new ItemStack(Items.LEATHER_HELMET), def_min, def_max, 2},
			{new ItemStack(Items.BREAD), 1, 4, 5},
			{new ItemStack(Items.LEATHER_LEGGINGS), def_min, def_max, 2},
			{new ItemStack(Items.SADDLE), def_min, def_max, def_weight},
			{new ItemStack(Items.EMERALD), 1, 4, def_weight},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Temple --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_temple");
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.REDSTONE), 1, 4, 2},
			{new ItemStack(Items.BREAD), 1, 4, 7},
			{new ItemStack(Items.ROTTEN_FLESH), 1, 4, 7},
			{new ItemStack(Items.DYE), 1, 11, def_weight}, // Lapis Lazuli
			{new ItemStack(Items.GOLD_INGOT), 1, 4, def_weight},
			{new ItemStack(Items.EMERALD), 1, 4, def_weight},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Toolsmith --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_toolsmith");
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.DIAMOND), 1, 3, def_weight},
			{new ItemStack(Items.IRON_INGOT), 1, 5, 5},
			{new ItemStack(Items.GOLD_INGOT), 1, 3, def_weight},
			{new ItemStack(Items.BREAD), 1, 3, 15},
			{new ItemStack(Items.IRON_PICKAXE), def_min, def_max, 5},
			{new ItemStack(Items.COAL), 1, 3, def_weight},
			{new ItemStack(Items.STICK), 1, 3, 20},
			{new ItemStack(Items.IRON_SHOVEL), def_min, def_max, 5},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		// Add codex
		if (GeneralConfig.codexChestLoot)
		{
			chestGenHooks.addItem(new WeightedRandomChestContent(new ItemStack(ModItems.CODEX), def_min, def_max, 3));
		}
		
		
		
		// --- Weaponsmith --- //
		
		chestGenHooks = ChestGenHooks.getInfo("vn_weaponsmith");
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.DIAMOND), 1, 3, 3},
			{new ItemStack(Items.IRON_INGOT), 1, 5, 10},
			{new ItemStack(Items.GOLD_INGOT), 1, 3, 5},
			{new ItemStack(Items.BREAD), 1, 3, 15},
			{new ItemStack(Items.APPLE), 1, 3, 15},
			{new ItemStack(Items.IRON_PICKAXE), def_min, def_max, 5},
			{new ItemStack(Items.IRON_SWORD), def_min, def_max, 5},
			{new ItemStack(Items.IRON_CHESTPLATE), def_min, def_max, 5},
			{new ItemStack(Items.IRON_HELMET), def_min, def_max, 5},
			{new ItemStack(Items.IRON_LEGGINGS), def_min, def_max, 5},
			{new ItemStack(Items.IRON_BOOTS), def_min, def_max, 5},
			{new ItemStack(Blocks.OBSIDIAN), 3, 7, 5},
			{new ItemStack(Blocks.SAPLING, 1, 0), 3, 7, 5}, // Oak Sapling
			{new ItemStack(Items.SADDLE), def_min, def_max, 3},
			{new ItemStack(Items.IRON_HORSE_ARMOR), def_min, def_max, def_weight},
			{new ItemStack(Items.GOLDEN_HORSE_ARMOR), def_min, def_max, def_weight},
			{new ItemStack(Items.DIAMOND_HORSE_ARMOR), def_min, def_max, def_weight},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
	}
	
	public static String getGenericLootForVillageType(FunctionsVN.VillageType villageType)
	{
		switch (villageType)
		{
		default:
		case PLAINS:  return "vn_plains_house";
		case DESERT:  return "vn_desert_house";
		case TAIGA:   return "vn_taiga_house";
		case SAVANNA: return "vn_savanna_house";
		case SNOWY:   return "vn_snowy_house";
		case JUNGLE:  return "vn_jungle_house";
		case SWAMP:   return "vn_swamp_house";
		}
	}
}
