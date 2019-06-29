package astrotibs.villagenames.init;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Recipes {
	
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
		GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(ModBlocksVN.LUNARIN_GOLD_BRICK),
				"ingotGold", "ingotGold", "ingotGold", "ingotGold",	"ingotGold", "ingotGold" )); // Much better
		// Turn Lunarin block back to ingots
		GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(Items.GOLD_INGOT, 6),new ItemStack(ModBlocksVN.LUNARIN_GOLD_BRICK) ));
		
		// Shapeless oreDict recipe
		GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(ModBlocksVN.LUNARIN_IRON_BRICK),
				"ingotIron", "ingotIron", "ingotIron", "ingotIron",	"ingotIron", "ingotIron" )); // Much better
		// Turn Lunarin block back to ingots
		GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(Items.IRON_INGOT, 6),	new ItemStack(ModBlocksVN.LUNARIN_IRON_BRICK) ));
		
		if (GeneralConfig.addConcrete) {
			// Add recipes for Concrete Powder
			for (int i = 0 ; i < 16; i++) {
				GameRegistry.addRecipe(new ShapelessOreRecipe( new ItemStack(ModBlocksVN.CONCRETE_POWDER, 8, i), oreDyeNames[i],
					new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Blocks.SAND, 1, 0), new ItemStack(Blocks.SAND, 1, 0),
					new ItemStack(Blocks.GRAVEL, 1, 0), new ItemStack(Blocks.GRAVEL, 1, 0), new ItemStack(Blocks.GRAVEL, 1, 0), new ItemStack(Blocks.GRAVEL, 1, 0) ));
			}
			// Add recipes for Glazed Terracotta
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 0), new ItemStack(ModBlocksVN.WHITE_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 1), new ItemStack(ModBlocksVN.ORANGE_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 2), new ItemStack(ModBlocksVN.MAGENTA_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 3), new ItemStack(ModBlocksVN.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 4), new ItemStack(ModBlocksVN.YELLOW_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 5), new ItemStack(ModBlocksVN.LIME_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 6), new ItemStack(ModBlocksVN.PINK_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 7), new ItemStack(ModBlocksVN.GRAY_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 8), new ItemStack(ModBlocksVN.SILVER_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 9), new ItemStack(ModBlocksVN.CYAN_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 10), new ItemStack(ModBlocksVN.PURPLE_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 11), new ItemStack(ModBlocksVN.BLUE_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 12), new ItemStack(ModBlocksVN.BROWN_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 13), new ItemStack(ModBlocksVN.GREEN_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 14), new ItemStack(ModBlocksVN.RED_GLAZED_TERRACOTTA, 1, 0), 0.1F);
			GameRegistry.addSmelting(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 15), new ItemStack(ModBlocksVN.BLACK_GLAZED_TERRACOTTA, 1, 0), 0.1F);
		}
		
	}
}
