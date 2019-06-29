package astrotibs.villagenames.block.color;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public final class BlockRenderRegister {
	
	public static String modid = Reference.MOD_ID;
	
	public static void registerBlockRenderer() {
	    
		for (int m=0; m<16; m++) {
			// These need to stay this way to properly render block-items
			registerBlockWithMeta(ModBlocksVN.blockConcrete, m, "concrete_"+EnumDyeColor.byMetadata(m).getName() );
			registerBlockWithMeta(ModBlocksVN.blockConcretePowder, m, "concrete_powder_"+EnumDyeColor.byMetadata(m).getName() );
			
		}
		// TODO - does not cause boot log errors
		// These register the IN-INVENTORY ITEM RENDERED BLOCKS.
		registerBlock(ModBlocksVN.blockGlazedTerracottaWhite, "glazed_terracotta_white" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaOrange, "glazed_terracotta_orange" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaMagenta, "glazed_terracotta_magenta" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaLightBlue, "glazed_terracotta_light_blue" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaYellow, "glazed_terracotta_yellow" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaLime, "glazed_terracotta_lime" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaPink, "glazed_terracotta_pink" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaGray, "glazed_terracotta_gray" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaSilver, "glazed_terracotta_silver" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaCyan, "glazed_terracotta_cyan" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaPurple, "glazed_terracotta_purple" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaBlue, "glazed_terracotta_blue" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaBrown, "glazed_terracotta_brown" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaGreen, "glazed_terracotta_green" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaRed, "glazed_terracotta_red" );
		registerBlock(ModBlocksVN.blockGlazedTerracottaBlack, "glazed_terracotta_black" );
		
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
	// the stuff below properly renders in-inventory blocks.
	public static void preInit() {
		ResourceLocation[] concreteRL = new ResourceLocation[16];
		ResourceLocation[] concretePowderRL = new ResourceLocation[16];
		ResourceLocation[] glazedTerracottaRL = new ResourceLocation[16];
		for (int m=0; m<16; m++) {
			// TODO - does not cause boot log errors
			//concreteRL[m] = new ResourceLocation( "VillageNames:concrete_"+EnumDyeColor.byMetadata(m).getName() );
			concreteRL[m] = new ResourceLocation( "minecraft:concrete_"+EnumDyeColor.byMetadata(m).getName() );
			//concretePowderRL[m] = new ResourceLocation( "VillageNames:concrete_powder_"+EnumDyeColor.byMetadata(m).getName() );
			concretePowderRL[m] = new ResourceLocation( "minecraft:concrete_powder_"+EnumDyeColor.byMetadata(m).getName() );
			glazedTerracottaRL[m] = new ResourceLocation( "minecraft:glazed_terracotta_"+EnumDyeColor.byMetadata(m).getName() );;
		}
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockConcrete), concreteRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockConcretePowder), concretePowderRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaWhite), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaOrange), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaMagenta), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaLightBlue), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaYellow), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaLime), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaPink), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaGray), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaSilver), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaCyan), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaPurple), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaBlue), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaBrown), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaGreen), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaRed), glazedTerracottaRL);
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaBlack), glazedTerracottaRL);
	}
	
}