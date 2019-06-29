package astrotibs.villagenames.block.color;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public final class BlockRenderRegister {
	
	public static String modid = Reference.MOD_ID;
	
	public static void registerBlockRenderer() {
	    
		for (int m=0; m<16; m++) {
			registerBlockWithMeta(ModBlocksVN.CONCRETE, m, "concrete_"+EnumDyeColor.byMetadata(m).getName() );
			registerBlockWithMeta(ModBlocksVN.CONCRETE_POWDER, m, "concrete_powder_"+EnumDyeColor.byMetadata(m).getName() );
			
		}
		registerBlock(ModBlocksVN.WHITE_GLAZED_TERRACOTTA, "glazed_terracotta_white" );
		registerBlock(ModBlocksVN.ORANGE_GLAZED_TERRACOTTA, "glazed_terracotta_orange" );
		registerBlock(ModBlocksVN.MAGENTA_GLAZED_TERRACOTTA, "glazed_terracotta_magenta" );
		registerBlock(ModBlocksVN.LIGHT_BLUE_GLAZED_TERRACOTTA, "glazed_terracotta_light_blue" );
		registerBlock(ModBlocksVN.YELLOW_GLAZED_TERRACOTTA, "glazed_terracotta_yellow" );
		registerBlock(ModBlocksVN.LIME_GLAZED_TERRACOTTA, "glazed_terracotta_lime" );
		registerBlock(ModBlocksVN.PINK_GLAZED_TERRACOTTA, "glazed_terracotta_pink" );
		registerBlock(ModBlocksVN.GRAY_GLAZED_TERRACOTTA, "glazed_terracotta_gray" );
		registerBlock(ModBlocksVN.SILVER_GLAZED_TERRACOTTA, "glazed_terracotta_silver" );
		registerBlock(ModBlocksVN.CYAN_GLAZED_TERRACOTTA, "glazed_terracotta_cyan" );
		registerBlock(ModBlocksVN.PURPLE_GLAZED_TERRACOTTA, "glazed_terracotta_purple" );
		registerBlock(ModBlocksVN.BLUE_GLAZED_TERRACOTTA, "glazed_terracotta_blue" );
		registerBlock(ModBlocksVN.BROWN_GLAZED_TERRACOTTA, "glazed_terracotta_brown" );
		registerBlock(ModBlocksVN.GREEN_GLAZED_TERRACOTTA, "glazed_terracotta_green" );
		registerBlock(ModBlocksVN.RED_GLAZED_TERRACOTTA, "glazed_terracotta_red" );
		registerBlock(ModBlocksVN.BLACK_GLAZED_TERRACOTTA, "glazed_terracotta_black" );
		
	}
	
	// register the renderer for the Items with special metadata
	public static void registerBlockWithMeta(Block block, int meta, String file) {
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    .register(Item.getItemFromBlock(block), meta, new ModelResourceLocation("minecraft:" + file, "inventory"));
	    //.register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(modid + ":" + file, "inventory"));
	}
	public static void registerBlock(Block block, String file) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("minecraft:" + file, "inventory"));
	    //.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + file, "inventory"));
	}
	
	// need to define that the block can have multiple models
	public static void preInit() {
		ResourceLocation[] concreteRL = new ResourceLocation[16];
		ResourceLocation[] concretePowderRL = new ResourceLocation[16];
		ResourceLocation[] glazedTerracottaRL = new ResourceLocation[16];
		for (int m=0; m<16; m++) {
			//concreteRL[m] = new ResourceLocation( "VillageNames:concrete_"+EnumDyeColor.byMetadata(m).getName() );
			concreteRL[m] = new ResourceLocation( "minecraft:concrete_"+EnumDyeColor.byMetadata(m).getName() );
			//concretePowderRL[m] = new ResourceLocation( "VillageNames:concrete_powder_"+EnumDyeColor.byMetadata(m).getName() );
			concretePowderRL[m] = new ResourceLocation( "minecraft:concrete_powder_"+EnumDyeColor.byMetadata(m).getName() );
			glazedTerracottaRL[m] = new ResourceLocation( "minecraft:glazed_terracotta_"+EnumDyeColor.byMetadata(m).getName() );
		}
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.CONCRETE), concreteRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.CONCRETE_POWDER), concretePowderRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.WHITE_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.ORANGE_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.MAGENTA_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.LIGHT_BLUE_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.YELLOW_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.LIME_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.PINK_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.GRAY_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.SILVER_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.CYAN_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.PURPLE_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.BLUE_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.BROWN_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.GREEN_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.RED_GLAZED_TERRACOTTA), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.BLACK_GLAZED_TERRACOTTA), glazedTerracottaRL);
	}
	
}