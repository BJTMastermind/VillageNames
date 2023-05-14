package astrotibs.villagenames.init;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class InventoryRender {
	public static void init() {
		//Blocks
		if (GeneralConfig.addLunarinBlocks)
		{
			InventoryBlockRender(ModBlocksVN.lunarinGoldBrick, "lunarinGoldBrick");
			InventoryBlockRender(ModBlocksVN.lunarinIronBrick, "lunarinIronBrick");
		}
		
		//Items
		InventoryItemRender(ModItems.codex, "codex");
		InventoryItemRender(ModItems.endcitybook, "endcitybook");
		InventoryItemRender(ModItems.fortressbook, "fortressbook");
		InventoryItemRender(ModItems.mansionbook, "mansionbook");
		InventoryItemRender(ModItems.mineshaftbook, "mineshaftbook");
		InventoryItemRender(ModItems.monumentbook, "monumentbook");
		InventoryItemRender(ModItems.strongholdbook, "strongholdbook");
		InventoryItemRender(ModItems.templebook, "templebook");
		InventoryItemRender(ModItems.villagebook, "villagebook");
		
		// V3 books
		InventoryItemRender(ModItems.jungletemplebook, "jungletemplebook");
		InventoryItemRender(ModItems.desertpyramidbook, "desertpyramidbook");
		InventoryItemRender(ModItems.swamphutbook, "swamphutbook");
		InventoryItemRender(ModItems.igloobook, "igloobook");
		
		InventoryItemRender(ModItems.moonvillagebook, "moonvillagebook");
		InventoryItemRender(ModItems.koentusvillagebook, "koentusvillagebook");
		InventoryItemRender(ModItems.fronosvillagebook, "fronosvillagebook");
		InventoryItemRender(ModItems.nibiruvillagebook, "nibiruvillagebook");
		InventoryItemRender(ModItems.abandonedbasebook, "abandonedbasebook");
		
		
	}

	public static void InventoryBlockRender(Block block, String blockName) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + blockName, "inventory"));
	}
	
	public static void InventoryItemRender(Item item, String itemName) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + itemName, "inventory"));
	}
}
