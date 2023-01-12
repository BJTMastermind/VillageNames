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
	
	// These values are defaults for loot population: they are assumed when not explicitly listed in the 1.14+ json loot tables.
	private static final int
	DEFAULT_LOOT_STACK_MINIMUM = 1,
	DEFAULT_LOOT_STACK_MAXIMUM = 1,
	DEFAULT_LOOT_STACK_WEIGHT = 1;
	
	private static final Object[][]
	ARMORER_CHEST_LOOT_ARRAY = new Object[][]{
		{new ItemStack(Items.IRON_INGOT), 1, 3, 2},
		{new ItemStack(Items.BREAD), 1, 4, 4},
		{new ItemStack(Items.IRON_HELMET), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
		{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
	},
	TOOLSMITHY_CHEST_LOOT_ARRAY = new Object[][]{
		{new ItemStack(Items.DIAMOND), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
		{new ItemStack(Items.IRON_INGOT), 1, 5, 5},
		{new ItemStack(Items.GOLD_INGOT), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
		{new ItemStack(Items.BREAD), 1, 3, 15},
		{new ItemStack(Items.IRON_PICKAXE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
		{new ItemStack(Items.COAL), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
		{new ItemStack(Items.STICK), 1, 3, 20},
		{new ItemStack(Items.IRON_SHOVEL), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
//	},
//	WEAPONSMITH_CHEST_LOOT_ARRAY = new Object[][]{
//		{new ItemStack(Items.DIAMOND), 1, 3, 3},
//		{new ItemStack(Items.IRON_INGOT), 1, 5, 10},
//		{new ItemStack(Items.GOLD_INGOT), 1, 3, 5},
//		{new ItemStack(Items.BREAD), 1, 3, 15},
//		{new ItemStack(Items.APPLE), 1, 3, 15},
//		{new ItemStack(Items.IRON_PICKAXE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
//		{new ItemStack(Items.IRON_SWORD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
//		{new ItemStack(Items.IRON_CHESTPLATE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
//		{new ItemStack(Items.IRON_HELMET), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
//		{new ItemStack(Items.IRON_LEGGINGS), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
//		{new ItemStack(Items.IRON_BOOTS), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
//		{new ItemStack(Blocks.OBSIDIAN), 3, 7, 5},
//		{new ItemStack(Blocks.SAPLING, 1, 0), 3, 7, 5}, // Oak Sapling
//		{new ItemStack(Items.SADDLE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 3},
//		{new ItemStack(Items.IRON_HORSE_ARMOR), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
//		{new ItemStack(Items.GOLDEN_HORSE_ARMOR), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
//		{new ItemStack(Items.DIAMOND_HORSE_ARMOR), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
	};
	
	/*
	 * Chest loot handler, needed for 1.9+
	 */
	@SubscribeEvent
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		
		final LootPool lootCategory;
		
		if (GeneralConfig.codexChestLoot) {
			//Bonus chest
			if (event.getName().equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST)) {
				lootCategory = event.getTable().getPool("main"); //Pool2 might be more on par with what I need, but every chest has a "main" type.
				// main.addEntry(new LootEntryItem(ITEM, WEIGHT, QUALITY, FUNCTIONS, CONDITIONS, NAME));
				if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 1, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex")); }
				
			// Village Blacksmiths
			else if (event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH)) {
				lootCategory = event.getTable().getPool("main");
				if (lootCategory != null) lootCategory.addEntry(new LootEntryItem(ModItems.CODEX, 3, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":codex"));}
			
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
	}
	
	public static void modernVillageChests()
	{
		// Changed with each chest entry
		ChestGenHooks chestGenHooks; int stacks_min; int stacks_max;
		
		
		// ------------------------------------- //
		// --- General biome-specific chests --- //
		// ------------------------------------- //
		
		
		
		// --- Desert House --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_DESERT_HOUSE);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.CLAY_BALL), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.DYE, 1, 2), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Blocks.CACTUS), 1, 4, 10},
			{new ItemStack(Items.WHEAT), 1, 7, 10},
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.BOOK), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Blocks.DEADBUSH), 1, 3, 2},
			{new ItemStack(Items.EMERALD), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Plains House --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_PLAINS_HOUSE);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.GOLD_NUGGET), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Blocks.YELLOW_FLOWER), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2},
			{new ItemStack(Blocks.RED_FLOWER), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.POTATO), 1, 7, 10},
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.APPLE), 1, 5, 10},
			{new ItemStack(Items.BOOK), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.FEATHER), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.EMERALD), 1, 4, 2},
			{new ItemStack(Blocks.SAPLING), 1, 2, 5}, // Oak sapling
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Savanna House --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_SAVANNA_HOUSE);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.GOLD_NUGGET), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Blocks.GRASS), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
			{new ItemStack(Blocks.DOUBLE_PLANT, 1, 2), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5}, // Tall grass
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.WHEAT_SEEDS), 1, 5, 10},
			{new ItemStack(Items.EMERALD), 1, 4, 2},
			{new ItemStack(Blocks.SAPLING, 1, 4), 1, 2, 10}, // Acacia
			{new ItemStack(Items.SADDLE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Blocks.TORCH), 1, 2, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.BUCKET), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Snowy House --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_SNOWY_HOUSE);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			//{new ItemStack(Blocks.BLUE_ICE), def_min, def_max, def_weight}, // TODO - Blue Ice
			{new ItemStack(Blocks.SNOW), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 4},
			{new ItemStack(Items.POTATO), 1, 7, 10},
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.BEETROOT_SEEDS), 1, 5, 10},
			{new ItemStack(Items.BEETROOT_SOUP), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Blocks.FURNACE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.EMERALD), 1, 4, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.SNOWBALL), 1, 7, 10},
			{new ItemStack(Items.COAL), 1, 4, 5},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Taiga House --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_TAIGA_HOUSE);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.field_191525_da), 1, 5, DEFAULT_LOOT_STACK_WEIGHT}, // Iron Nugget
			{new ItemStack(Blocks.TALLGRASS, 1, 2), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2}, // Fern
			{new ItemStack(Blocks.DOUBLE_PLANT, 1, 3), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2}, // Large Fern
			{new ItemStack(Items.POTATO), 1, 7, 10},
			{ModObjects.chooseModSweetBerriesItem(), 1, 7, 5},
			{new ItemStack(Items.BREAD), 1, 4, 10},
			{new ItemStack(Items.PUMPKIN_SEEDS), 1, 5, 5},
			{new ItemStack(Items.PUMPKIN_PIE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.EMERALD), 1, 4, 2},
			{new ItemStack(Blocks.SAPLING, 1, 1), 1, 5, 5}, // Spruce Sapling
			{ModObjects.chooseModWoodenSignItem(1), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT}, // Spruce Sign
			{new ItemStack(Blocks.LOG, 1, 1), 1, 5, 10}, // Spruce Log
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Jungle House --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_JUNGLE_HOUSE);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.BREAD), DEFAULT_LOOT_STACK_MINIMUM, 4, 10},
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, 4, 2},
			{new ItemStack(Items.field_191525_da), DEFAULT_LOOT_STACK_MINIMUM, 5, DEFAULT_LOOT_STACK_WEIGHT}, // Iron Nugget
			{new ItemStack(Blocks.SAPLING, 1, 3), DEFAULT_LOOT_STACK_MINIMUM, 5, 3}, // Jungle Sapling
			{new ItemStack(Blocks.LOG, 1, 3), DEFAULT_LOOT_STACK_MINIMUM, 5, 10}, // Jungle Log
			{ModObjects.chooseModWoodenSignItem(3), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT}, // Jungle Sign
			{new ItemStack(Blocks.VINE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
			{new ItemStack(Blocks.TORCH), DEFAULT_LOOT_STACK_MINIMUM, 2, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.FEATHER), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.CHICKEN), DEFAULT_LOOT_STACK_MINIMUM, 2, 3},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Swamp House --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_SWAMP_HOUSE);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.BREAD), DEFAULT_LOOT_STACK_MINIMUM, 4, 10},
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, 4, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.BOOK), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Blocks.VINE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
			{new ItemStack(Items.WATER_BUCKET), DEFAULT_LOOT_STACK_MINIMUM, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.COAL), DEFAULT_LOOT_STACK_MINIMUM, 4, 5},
			{new ItemStack(Items.FISH, 1, 0), DEFAULT_LOOT_STACK_MINIMUM, 2, DEFAULT_LOOT_STACK_WEIGHT}, // Raw Cod
			{new ItemStack(Blocks.OAK_FENCE), DEFAULT_LOOT_STACK_MINIMUM, 4, 2}, // Oak Fence
			{new ItemStack(Items.BOAT), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT}, // Oak Boat
			{new ItemStack(Items.PRISMARINE_SHARD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		
		// ------------------------------ //
		// --- Specific career chests --- //
		// ------------------------------ //
		
		
		
		// --- Armorer --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_ARMORER);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : ARMORER_CHEST_LOOT_ARRAY)
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Butcher --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_BUTCHER);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
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
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_CARTOGRAPHER);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.MAP), 1, 3, 10},
			{new ItemStack(Items.PAPER), 1, 5, 15},
			{new ItemStack(Items.COMPASS), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 5},
			{new ItemStack(Items.BREAD), 1, 4, 15},
			{new ItemStack(Items.STICK), 1, 2, 5},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		// Add codex
		if (GeneralConfig.codexChestLoot)
		{
			chestGenHooks.addItem(new WeightedRandomChestContent(new ItemStack(ModItems.CODEX), DEFAULT_LOOT_STACK_MINIMUM, 2, 4));
		}
		
		
		
		// --- Farm --- //
		// Custom by AstroTibs
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_FARM);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.WHEAT_SEEDS), DEFAULT_LOOT_STACK_MINIMUM, 5, 5},
			{new ItemStack(Items.POTATO), DEFAULT_LOOT_STACK_MINIMUM, 5, 2},
			{new ItemStack(Items.CARROT), DEFAULT_LOOT_STACK_MINIMUM, 5, 2},
			{new ItemStack(Items.BEETROOT_SEEDS), DEFAULT_LOOT_STACK_MINIMUM, 5, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.PUMPKIN_SEEDS), DEFAULT_LOOT_STACK_MINIMUM, 5, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.REEDS), DEFAULT_LOOT_STACK_MINIMUM, 5, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.BUCKET), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Fisher Cottage --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_FISHER);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.FISH), 1, 3, 2},
			{new ItemStack(Items.FISH, 1, 1), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.WATER_BUCKET), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{ModObjects.chooseModBarrelItem(), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.CLAY_BALL), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.WHEAT_SEEDS), 1, 3, 3},
			{new ItemStack(Items.COAL), 1, 3, 2},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Fletcher --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_FLETCHER);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.ARROW), 1, 3, 2},
			{new ItemStack(Items.FEATHER), 1, 3, 6},
			{new ItemStack(Items.EGG), 1, 3, 2},
			{new ItemStack(Items.FLINT), 1, 3, 6},
			{new ItemStack(Items.STICK), 1, 3, 6},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Library --- //
		// Custom
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_LIBRARY);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.PAPER), DEFAULT_LOOT_STACK_MINIMUM, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.DYE, 1, 0), DEFAULT_LOOT_STACK_MINIMUM, 3, 6}, // Ink sac
			{new ItemStack(Items.FEATHER), DEFAULT_LOOT_STACK_MINIMUM, 3, 6},
			{new ItemStack(Items.WRITABLE_BOOK), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.BOOK), DEFAULT_LOOT_STACK_MINIMUM, 3, 3},
			{new ItemStack(Items.APPLE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 15},
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			//{new ItemStack(FunctionsVN.getItemFromName(ModObjects.dustyBook_LB)), def_min, def_max, 2}, // Lost Book
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		// Add codex
		if (GeneralConfig.codexChestLoot)
		{
			chestGenHooks.addItem(new WeightedRandomChestContent(new ItemStack(ModItems.CODEX), DEFAULT_LOOT_STACK_MINIMUM, 2, 4));
		}
		
		
		
		// --- Mason --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_MASON);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		IBlockState smoothStoneBlockState = ModObjects.chooseModSmoothStoneBlockState();
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.CLAY_BALL), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.FLOWER_POT), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Blocks.STONE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2},
			{new ItemStack(Blocks.STONEBRICK), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2},
			{new ItemStack(Items.BREAD), 1, 4, 4},
			{new ItemStack(Items.DYE, 1, 11), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(smoothStoneBlockState.getBlock(), 1, smoothStoneBlockState.getBlock().getMetaFromState(smoothStoneBlockState)), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		// Add codex
		if (GeneralConfig.codexChestLoot)
		{
			chestGenHooks.addItem(new WeightedRandomChestContent(new ItemStack(ModItems.CODEX), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT));
		}
		
		
		
		// --- Shepherd --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_SHEPHERD);
		
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
			{new ItemStack(Items.EMERALD), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.SHEARS), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.WHEAT), 1, 6, 6},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Tannery --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_TANNERY);
		
		// Number of stacks in a chest
		stacks_min=1;
		stacks_max=5;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.LEATHER), 1, 3, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.LEATHER_CHESTPLATE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2},
			{new ItemStack(Items.LEATHER_BOOTS), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2},
			{new ItemStack(Items.LEATHER_HELMET), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2},
			{new ItemStack(Items.BREAD), 1, 4, 5},
			{new ItemStack(Items.LEATHER_LEGGINGS), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 2},
			{new ItemStack(Items.SADDLE), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.EMERALD), 1, 4, DEFAULT_LOOT_STACK_WEIGHT},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Temple --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_TEMPLE);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : new Object[][]{
			{new ItemStack(Items.REDSTONE), 1, 4, 2},
			{new ItemStack(Items.BREAD), 1, 4, 7},
			{new ItemStack(Items.ROTTEN_FLESH), 1, 4, 7},
			{new ItemStack(Items.DYE), 1, 11, DEFAULT_LOOT_STACK_WEIGHT}, // Lapis Lazuli
			{new ItemStack(Items.GOLD_INGOT), 1, 4, DEFAULT_LOOT_STACK_WEIGHT},
			{new ItemStack(Items.EMERALD), 1, 4, DEFAULT_LOOT_STACK_WEIGHT},
		})
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		
		
		
		// --- Toolsmith --- //
		
		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_TOOLSMITH);
		
		// Number of stacks in a chest
		stacks_min=3;
		stacks_max=8;
		
		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
		
		// Register chest entries: ItemStack, stackMin, stackMax, weight
		for (Object[] chestItemObject : TOOLSMITHY_CHEST_LOOT_ARRAY)
		{
			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
		}
		// Add codex
		if (GeneralConfig.codexChestLoot)
		{
			chestGenHooks.addItem(new WeightedRandomChestContent(new ItemStack(ModItems.CODEX), DEFAULT_LOOT_STACK_MINIMUM, DEFAULT_LOOT_STACK_MAXIMUM, 3));
		}
		
		
		
		// --- Weaponsmith --- //
		// Contents are identical to vanilla blacksmith chest!
		
//		chestGenHooks = ChestGenHooks.getInfo(Reference.VN_WEAPONSMITH);
//		
//		// Number of stacks in a chest
//		stacks_min=3;
//		stacks_max=8;
//		
//		chestGenHooks.setMin(stacks_min); chestGenHooks.setMax(stacks_max+1);
//		
//		// Register chest entries: ItemStack, stackMin, stackMax, weight
//		for (Object[] chestItemObject : WEAPONSMITH_CHEST_LOOT_ARRAY)
//		{
//			if (chestItemObject[0] != null) {chestGenHooks.addItem(new WeightedRandomChestContent((ItemStack)chestItemObject[0], (Integer)chestItemObject[1], (Integer)chestItemObject[2], (Integer)chestItemObject[3]));}
//		}
		
	}
	
	public static String getGenericLootForVillageType(FunctionsVN.VillageType villageType)
	{
		switch (villageType)
		{
		default:
		case PLAINS:  return Reference.VN_PLAINS_HOUSE;
		case DESERT:  return Reference.VN_DESERT_HOUSE;
		case TAIGA:   return Reference.VN_TAIGA_HOUSE;
		case SAVANNA: return Reference.VN_SAVANNA_HOUSE;
		case SNOWY:   return Reference.VN_SNOWY_HOUSE;
		case JUNGLE:  return Reference.VN_JUNGLE_HOUSE;
		case SWAMP:   return Reference.VN_SWAMP_HOUSE;
		}
	}
}
