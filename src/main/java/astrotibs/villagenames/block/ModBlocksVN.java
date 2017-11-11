package astrotibs.villagenames.block;

import astrotibs.villagenames.block.color.BlockConcrete;
import astrotibs.villagenames.block.color.BlockConcretePowder;
import astrotibs.villagenames.block.color.ItemBlockMetaColor;
import astrotibs.villagenames.material.ModMaterial;
import astrotibs.villagenames.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import astrotibs.villagenames.block.color.BlockGlazedTerracotta;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocksVN {
	public static final Block lunarinGoldBrick = new BlockLunarinGold(ModMaterial.gold, "lunaringoldbrick");
	public static final Block lunarinIronBrick = new BlockLunarinIron(Material.IRON, "lunarinironbrick");
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
		
		GameRegistry.register(blockConcrete, new ResourceLocation(Reference.MOD_ID, "concrete"));
        GameRegistry.register(new ItemBlockMetaColor(blockConcrete), new ResourceLocation(Reference.MOD_ID, "concrete"));
		
        GameRegistry.register(blockConcretePowder, new ResourceLocation(Reference.MOD_ID, "concrete_powder"));
        GameRegistry.register(new ItemBlockMetaColor(blockConcretePowder), new ResourceLocation(Reference.MOD_ID, "concrete_powder"));
        
        registerBlock(lunarinGoldBrick, "lunaringoldbrick");
        registerBlock(lunarinIronBrick, "lunarinironbrick");
        
		registerBlock(blockGlazedTerracottaWhite, "glazed_terracotta_white" );
		registerBlock(blockGlazedTerracottaOrange, "glazed_terracotta_orange" );
		registerBlock(blockGlazedTerracottaMagenta, "glazed_terracotta_magenta" );
		registerBlock(blockGlazedTerracottaLightBlue, "glazed_terracotta_light_blue" );
		registerBlock(blockGlazedTerracottaYellow, "glazed_terracotta_yellow" );
		registerBlock(blockGlazedTerracottaLime, "glazed_terracotta_lime" );
		registerBlock(blockGlazedTerracottaPink, "glazed_terracotta_pink" );
		registerBlock(blockGlazedTerracottaGray, "glazed_terracotta_gray" );
		registerBlock(blockGlazedTerracottaSilver, "glazed_terracotta_silver" );
		registerBlock(blockGlazedTerracottaCyan, "glazed_terracotta_cyan" );
		registerBlock(blockGlazedTerracottaPurple, "glazed_terracotta_purple" );
		registerBlock(blockGlazedTerracottaBlue, "glazed_terracotta_blue" );
		registerBlock(blockGlazedTerracottaBrown, "glazed_terracotta_brown" );
		registerBlock(blockGlazedTerracottaGreen, "glazed_terracotta_green" );
		registerBlock(blockGlazedTerracottaRed, "glazed_terracotta_red" );
		registerBlock(blockGlazedTerracottaBlack, "glazed_terracotta_black" );
		
	}
	
	public static void registerBlock(Block block, String name) {
        GameRegistry.register(block, new ResourceLocation(Reference.MOD_ID, name));
        GameRegistry.register(new ItemBlock(block), new ResourceLocation(Reference.MOD_ID, name));
    }
	
}
