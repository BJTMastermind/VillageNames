package astrotibs.villagenames.block;

import astrotibs.villagenames.block.color.BlockConcrete;
import astrotibs.villagenames.block.color.BlockConcretePowder;
import astrotibs.villagenames.block.color.BlockGlazedTerracotta;
import astrotibs.villagenames.block.color.ItemBlockMetaColor;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.material.ModMaterial;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocksVN {
	public static final Block LUNARIN_GOLD_BRICK = new BlockLunarinGold(ModMaterial.gold, "lunaringoldbrick");
	public static final Block LUNARIN_IRON_BRICK = new BlockLunarinIron(Material.IRON, "lunarinironbrick");
	public static final Block CONCRETE = new BlockConcrete("concrete");
	public static final Block CONCRETE_POWDER = new BlockConcretePowder("concrete_powder");
	
	public static final Block WHITE_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.WHITE);
	public static final Block ORANGE_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.ORANGE);
	public static final Block MAGENTA_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.MAGENTA);
	public static final Block LIGHT_BLUE_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.LIGHT_BLUE);
	public static final Block YELLOW_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.YELLOW);
	public static final Block LIME_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.LIME);
	public static final Block PINK_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.PINK);
	public static final Block GRAY_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.GRAY);
	public static final Block SILVER_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.SILVER);
	public static final Block CYAN_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.CYAN);
	public static final Block PURPLE_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.PURPLE);
	public static final Block BLUE_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.BLUE);
	public static final Block BROWN_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.BROWN);
	public static final Block GREEN_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.GREEN);
	public static final Block RED_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.RED);
	public static final Block BLACK_GLAZED_TERRACOTTA = new BlockGlazedTerracotta(EnumDyeColor.BLACK);
	
	public static void init() {
		
		GameRegistry.register(CONCRETE, new ResourceLocation(Reference.MOD_ID, "concrete"));
        GameRegistry.register(new ItemBlockMetaColor(CONCRETE), new ResourceLocation(Reference.MOD_ID, "concrete"));
        GameRegistry.register(CONCRETE_POWDER, new ResourceLocation(Reference.MOD_ID, "concrete_powder"));
        GameRegistry.register(new ItemBlockMetaColor(CONCRETE_POWDER), new ResourceLocation(Reference.MOD_ID, "concrete_powder"));
        
		if (GeneralConfig.addLunarinBlocks)
		{
	        registerBlock(LUNARIN_GOLD_BRICK, "lunaringoldbrick");
	        registerBlock(LUNARIN_IRON_BRICK, "lunarinironbrick");
		}
        
		if (GeneralConfig.addConcrete) {
			registerBlock(WHITE_GLAZED_TERRACOTTA, "glazed_terracotta_white" );
			registerBlock(ORANGE_GLAZED_TERRACOTTA, "glazed_terracotta_orange" );
			registerBlock(MAGENTA_GLAZED_TERRACOTTA, "glazed_terracotta_magenta" );
			registerBlock(LIGHT_BLUE_GLAZED_TERRACOTTA, "glazed_terracotta_light_blue" );
			registerBlock(YELLOW_GLAZED_TERRACOTTA, "glazed_terracotta_yellow" );
			registerBlock(LIME_GLAZED_TERRACOTTA, "glazed_terracotta_lime" );
			registerBlock(PINK_GLAZED_TERRACOTTA, "glazed_terracotta_pink" );
			registerBlock(GRAY_GLAZED_TERRACOTTA, "glazed_terracotta_gray" );
			registerBlock(SILVER_GLAZED_TERRACOTTA, "glazed_terracotta_silver" );
			registerBlock(CYAN_GLAZED_TERRACOTTA, "glazed_terracotta_cyan" );
			registerBlock(PURPLE_GLAZED_TERRACOTTA, "glazed_terracotta_purple" );
			registerBlock(BLUE_GLAZED_TERRACOTTA, "glazed_terracotta_blue" );
			registerBlock(BROWN_GLAZED_TERRACOTTA, "glazed_terracotta_brown" );
			registerBlock(GREEN_GLAZED_TERRACOTTA, "glazed_terracotta_green" );
			registerBlock(RED_GLAZED_TERRACOTTA, "glazed_terracotta_red" );
			registerBlock(BLACK_GLAZED_TERRACOTTA, "glazed_terracotta_black" );
		}

	}
	
	public static void registerBlock(Block block, String name) {
        GameRegistry.register(block, new ResourceLocation(Reference.MOD_ID, name));
        GameRegistry.register(new ItemBlock(block), new ResourceLocation(Reference.MOD_ID, name));
    }
	
}
