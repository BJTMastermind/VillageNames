package astrotibs.villagenames.init;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class InventoryRender {
	public static void init() {
		//Blocks
		InventoryBlockRender(ModBlocksVN.lunarinGoldBrick, "lunarinGoldBrick");
		InventoryBlockRender(ModBlocksVN.lunarinIronBrick, "lunarinIronBrick");
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
