package astrotibs.villagenames.init;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfigHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Recipes {
	
	public static boolean addConcrete = GeneralConfigHandler.addConcrete;
	static String[] oreDyeNames = new String[]{
			"dyeWhite",
			"dyeOrange",
			"dyeMagenta",
			"dyeLightBlue",
			"dyeYellow",
			"dyeLime",
			"dyePink",
			"dyeGray",
			"dyeLightGray",
			"dyeCyan",
			"dyePurple",
			"dyeBlue",
			"dyeBrown",
			"dyeGreen",
			"dyeRed",
			"dyeBlack"
			};
	
	
	public static void init() {
		// This is just a method inside this class that will register all the recipes.
		
		// Shapeless oreDict recipe
		GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(ModBlocksVN.lunarinGoldBrick),
				"ingotGold", "ingotGold", "ingotGold", "ingotGold",	"ingotGold", "ingotGold" )); // Much better
		// Turn Lunarin block back to ingots
		GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(Items.GOLD_INGOT, 6),new ItemStack(ModBlocksVN.lunarinGoldBrick) ));
		
		
		// Shapeless oreDict recipe
		GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(ModBlocksVN.lunarinIronBrick),
				"ingotIron", "ingotIron", "ingotIron", "ingotIron",	"ingotIron", "ingotIron" )); // Much better
		// Turn Lunarin block back to ingots
		GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(Items.IRON_INGOT, 6),	new ItemStack(ModBlocksVN.lunarinIronBrick) ));
		
		if (addConcrete) {
			// Add recipes for Concrete Powder
			for (int i = 0 ; i < 16; i++) {
				GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(ModBlocksVN.blockConcretePowder, 8, i), oreDyeNames[i],
					new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Blocks.SAND, 1, 0),
					new ItemStack(Blocks.GRAVEL, 1, 0), new ItemStack(Blocks.GRAVEL, 1, 0), new ItemStack(Blocks.GRAVEL, 1, 0), new ItemStack(Blocks.GRAVEL, 1, 0) ));
			}
		}
		// Add recipes for Glazed Terracotta
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 0), new ItemStack(ModBlocksVN.blockGlazedTerracottaWhite, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 1), new ItemStack(ModBlocksVN.blockGlazedTerracottaOrange, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 2), new ItemStack(ModBlocksVN.blockGlazedTerracottaMagenta, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 3), new ItemStack(ModBlocksVN.blockGlazedTerracottaLightBlue, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 4), new ItemStack(ModBlocksVN.blockGlazedTerracottaYellow, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 5), new ItemStack(ModBlocksVN.blockGlazedTerracottaLime, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 6), new ItemStack(ModBlocksVN.blockGlazedTerracottaPink, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 7), new ItemStack(ModBlocksVN.blockGlazedTerracottaGray, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 8), new ItemStack(ModBlocksVN.blockGlazedTerracottaSilver, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 9), new ItemStack(ModBlocksVN.blockGlazedTerracottaCyan, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 10), new ItemStack(ModBlocksVN.blockGlazedTerracottaPurple, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 11), new ItemStack(ModBlocksVN.blockGlazedTerracottaBlue, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 12), new ItemStack(ModBlocksVN.blockGlazedTerracottaBrown, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 13), new ItemStack(ModBlocksVN.blockGlazedTerracottaGreen, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 14), new ItemStack(ModBlocksVN.blockGlazedTerracottaRed, 1, 0), 0.1F);
		GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 15), new ItemStack(ModBlocksVN.blockGlazedTerracottaBlack, 1, 0), 0.1F);
		
	}
}
