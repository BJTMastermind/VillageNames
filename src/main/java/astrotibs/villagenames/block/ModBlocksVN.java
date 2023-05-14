package astrotibs.villagenames.block;

import astrotibs.villagenames.block.color.BlockConcrete;
import astrotibs.villagenames.block.color.BlockConcretePowder;
import astrotibs.villagenames.block.color.BlockGlazedTerracotta;
import astrotibs.villagenames.block.color.ItemBlockMetaColor;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocksVN {
	public static final BlockVN LUNARIN_GOLD_BRICK = new BlockLunarinGold();
	public static final BlockVN LUNARIN_IRON_BRICK = new BlockLunarinIron();
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
		
		if (GeneralConfig.addLunarinBlocks)
		{
			GameRegistry.registerBlock(LUNARIN_GOLD_BRICK, "lunarinGoldBrick");
			GameRegistry.registerBlock(LUNARIN_IRON_BRICK, "lunarinIronBrick");
		}
		
		if (GeneralConfig.addConcrete)
		{
			GameRegistry.registerBlock(CONCRETE, ItemBlockMetaColor.class, "concrete");
			GameRegistry.registerBlock(CONCRETE_POWDER, ItemBlockMetaColor.class, "concrete_powder");
			GameRegistry.registerBlock(WHITE_GLAZED_TERRACOTTA, "glazed_terracotta_white");
			GameRegistry.registerBlock(ORANGE_GLAZED_TERRACOTTA, "glazed_terracotta_orange");
			GameRegistry.registerBlock(MAGENTA_GLAZED_TERRACOTTA, "glazed_terracotta_magenta");
			GameRegistry.registerBlock(LIGHT_BLUE_GLAZED_TERRACOTTA, "glazed_terracotta_light_blue");
			GameRegistry.registerBlock(YELLOW_GLAZED_TERRACOTTA, "glazed_terracotta_yellow");
			GameRegistry.registerBlock(LIME_GLAZED_TERRACOTTA, "glazed_terracotta_lime");
			GameRegistry.registerBlock(PINK_GLAZED_TERRACOTTA, "glazed_terracotta_pink");
			GameRegistry.registerBlock(GRAY_GLAZED_TERRACOTTA, "glazed_terracotta_gray");
			GameRegistry.registerBlock(SILVER_GLAZED_TERRACOTTA, "glazed_terracotta_silver");
			GameRegistry.registerBlock(CYAN_GLAZED_TERRACOTTA, "glazed_terracotta_cyan");
			GameRegistry.registerBlock(PURPLE_GLAZED_TERRACOTTA, "glazed_terracotta_purple");
			GameRegistry.registerBlock(BLUE_GLAZED_TERRACOTTA, "glazed_terracotta_blue");
			GameRegistry.registerBlock(BROWN_GLAZED_TERRACOTTA, "glazed_terracotta_brown");
			GameRegistry.registerBlock(GREEN_GLAZED_TERRACOTTA, "glazed_terracotta_green");
			GameRegistry.registerBlock(RED_GLAZED_TERRACOTTA, "glazed_terracotta_red");
			GameRegistry.registerBlock(BLACK_GLAZED_TERRACOTTA, "glazed_terracotta_black");
		}
		
	}
}
