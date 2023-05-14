package astrotibs.villagenames.init;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class InventoryRender {
	public static void init() {
		//Blocks
		if (GeneralConfig.addLunarinBlocks)
		{
			InventoryBlockRender(ModBlocksVN.LUNARIN_GOLD_BRICK, "lunarinGoldBrick");
			InventoryBlockRender(ModBlocksVN.LUNARIN_IRON_BRICK, "lunarinIronBrick");
		}
		
		//Items
		InventoryItemRender(ModItems.CODEX, "codex");
		InventoryItemRender(ModItems.END_CITY_BOOK, "endcitybook");
		InventoryItemRender(ModItems.FORTRESS_BOOK, "fortressbook");
		InventoryItemRender(ModItems.MANSION_BOOK, "mansionbook");
		InventoryItemRender(ModItems.MINESHAFT_BOOK, "mineshaftbook");
		InventoryItemRender(ModItems.MONUMENT_BOOK, "monumentbook");
		InventoryItemRender(ModItems.STRONGHOLD_BOOK, "strongholdbook");
		InventoryItemRender(ModItems.TEMPLE_BOOK, "templebook");
		InventoryItemRender(ModItems.VILLAGE_BOOK, "villagebook");
		
		// V3 books
		InventoryItemRender(ModItems.JUNGLE_TEMPLE_BOOK, "jungletemplebook");
		InventoryItemRender(ModItems.DESERT_PYRAMID_BOOK, "desertpyramidbook");
		InventoryItemRender(ModItems.SWAMP_HUT_BOOK, "swamphutbook");
		InventoryItemRender(ModItems.IGLOO_BOOK, "igloobook");
		
		InventoryItemRender(ModItems.MOON_VILLAGE_BOOK, "moonvillagebook");
		InventoryItemRender(ModItems.KOENTUS_VILLAGE_BOOK, "koentusvillagebook");
		InventoryItemRender(ModItems.FRONOS_VILLAGE_BOOK, "fronosvillagebook");
		InventoryItemRender(ModItems.NIBIRU_VILLAGE_BOOK, "nibiruvillagebook");
		InventoryItemRender(ModItems.ABANDONED_BASE_BOOK, "abandonedbasebook");
	}

	public static void InventoryBlockRender(Block block, String blockName) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + blockName, "inventory"));
	}
	
	public static void InventoryItemRender(Item item, String itemName) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + itemName, "inventory"));
	}
}
