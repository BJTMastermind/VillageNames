package astrotibs.villagenames.handler;

import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.utility.Reference;
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
	
}
