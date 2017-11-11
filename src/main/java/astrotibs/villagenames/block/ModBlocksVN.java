package astrotibs.villagenames.block;

import astrotibs.villagenames.block.color.BlockConcrete;
import astrotibs.villagenames.block.color.BlockConcretePowder;
import astrotibs.villagenames.block.color.BlockGlazedTerracotta;
import astrotibs.villagenames.block.color.ItemBlockMetaColor;
import astrotibs.villagenames.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocksVN {
	public static final BlockVN lunarinGoldBrick = new BlockLunarinGold();
	public static final BlockVN lunarinIronBrick = new BlockLunarinIron();
	public static final Block blockConcrete = new BlockConcrete("concrete");
	public static final Block blockConcretePowder = new BlockConcretePowder("concrete_powder");
	public static final Block blockGlazedTerracottaWhite = new BlockGlazedTerracotta(EnumDyeColor.WHITE);
	public static final Block blockGlazedTerracottaOrange = new BlockGlazedTerracotta(EnumDyeColor.ORANGE);
	public static final Block blockGlazedTerracottaMagenta = new BlockGlazedTerracotta(EnumDyeColor.MAGENTA);
	public static final Block blockGlazedTerracottaLightBlue = new BlockGlazedTerracotta(EnumDyeColor.LIGHT_BLUE);
	public static final Block blockGlazedTerracottaYellow = new BlockGlazedTerracotta(EnumDyeColor.YELLOW);
	public static final Block blockGlazedTerracottaLime = new BlockGlazedTerracotta(EnumDyeColor.LIME);
	public static final Block blockGlazedTerracottaPink = new BlockGlazedTerracotta(EnumDyeColor.PINK);
	public static final Block blockGlazedTerracottaGray = new BlockGlazedTerracotta(EnumDyeColor.GRAY);
	public static final Block blockGlazedTerracottaSilver = new BlockGlazedTerracotta(EnumDyeColor.SILVER);
	public static final Block blockGlazedTerracottaCyan = new BlockGlazedTerracotta(EnumDyeColor.CYAN);
	public static final Block blockGlazedTerracottaPurple = new BlockGlazedTerracotta(EnumDyeColor.PURPLE);
	public static final Block blockGlazedTerracottaBlue = new BlockGlazedTerracotta(EnumDyeColor.BLUE);
	public static final Block blockGlazedTerracottaBrown = new BlockGlazedTerracotta(EnumDyeColor.BROWN);
	public static final Block blockGlazedTerracottaGreen = new BlockGlazedTerracotta(EnumDyeColor.GREEN);
	public static final Block blockGlazedTerracottaRed = new BlockGlazedTerracotta(EnumDyeColor.RED);
	public static final Block blockGlazedTerracottaBlack = new BlockGlazedTerracotta(EnumDyeColor.BLACK);

	
	
	public static void init() {
		
		GameRegistry.registerBlock(lunarinGoldBrick, "lunarinGoldBrick");
		GameRegistry.registerBlock(lunarinIronBrick, "lunarinIronBrick");
		GameRegistry.registerBlock(blockConcrete, ItemBlockMetaColor.class, "concrete");
		GameRegistry.registerBlock(blockConcretePowder, ItemBlockMetaColor.class, "concrete_powder");
		GameRegistry.registerBlock(blockGlazedTerracottaWhite, "glazed_terracotta_white");
		GameRegistry.registerBlock(blockGlazedTerracottaOrange, "glazed_terracotta_orange");
		GameRegistry.registerBlock(blockGlazedTerracottaMagenta, "glazed_terracotta_magenta");
		GameRegistry.registerBlock(blockGlazedTerracottaLightBlue, "glazed_terracotta_light_blue");
		GameRegistry.registerBlock(blockGlazedTerracottaYellow, "glazed_terracotta_yellow");
		GameRegistry.registerBlock(blockGlazedTerracottaLime, "glazed_terracotta_lime");
		GameRegistry.registerBlock(blockGlazedTerracottaPink, "glazed_terracotta_pink");
		GameRegistry.registerBlock(blockGlazedTerracottaGray, "glazed_terracotta_gray");
		GameRegistry.registerBlock(blockGlazedTerracottaSilver, "glazed_terracotta_silver");
		GameRegistry.registerBlock(blockGlazedTerracottaCyan, "glazed_terracotta_cyan");
		GameRegistry.registerBlock(blockGlazedTerracottaPurple, "glazed_terracotta_purple");
		GameRegistry.registerBlock(blockGlazedTerracottaBlue, "glazed_terracotta_blue");
		GameRegistry.registerBlock(blockGlazedTerracottaBrown, "glazed_terracotta_brown");
		GameRegistry.registerBlock(blockGlazedTerracottaGreen, "glazed_terracotta_green");
		GameRegistry.registerBlock(blockGlazedTerracottaRed, "glazed_terracotta_red");
		GameRegistry.registerBlock(blockGlazedTerracottaBlack, "glazed_terracotta_black");
				
	}
}
