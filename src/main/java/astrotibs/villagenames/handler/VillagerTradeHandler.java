package astrotibs.villagenames.handler;

import java.util.Random;

import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionUtils;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class VillagerTradeHandler
{

	
	// Added in v3.1trades
	
	// --------------------------- //
	// --- Modern Mason trades --- //
	// --------------------------- //
	
	// summon minecraft:villager ~ ~ ~ {Profession:3}
	
	public class MasonNovice implements EntityVillager.ITradeList
	{
		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
		{
			// Slot 1
			recipeList.add( new MerchantRecipe(new ItemStack(Items.CLAY_BALL, 10), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.EMERALD, 1), 0, 6) );
			
			// Slot 2
			recipeList.add( new MerchantRecipe(new ItemStack(Items.EMERALD, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.BRICK, 10), 0, 5) );
		}
	}
	
	public class MasonApprentice implements EntityVillager.ITradeList
	{
		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
		{
			// Slot 3
			recipeList.add( new MerchantRecipe(new ItemStack(Item.getItemFromBlock(Blocks.STONE), 20, 0), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.EMERALD, 1), 0, 5) );
			
			// Slot 4
			recipeList.add( new MerchantRecipe(new ItemStack(Items.EMERALD, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(Blocks.STONEBRICK), 4, 3), 0, 5) );
		}
	}
	
	public class MasonJourneyman implements EntityVillager.ITradeList
	{
		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
		{
			// Slot 5
			recipeList.add( new MerchantRecipe(new ItemStack(Item.getItemFromBlock(Blocks.STONE), 16, random.nextInt(3)*2 +1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.EMERALD, 1), 0, 5) );
			
			// Slot 6
			recipeList.add( new MerchantRecipe(new ItemStack(Items.EMERALD, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(Blocks.STONE), 4, random.nextInt(3)*2 +2), 0, 5) );
		}
	}
	
	public class MasonExpert implements EntityVillager.ITradeList
	{
		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
		{

			// Slot 7
			recipeList.add(new MerchantRecipe(
					new ItemStack( Items.QUARTZ, 12 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
					));
			
			// Slot 8
			int color1 = random.nextInt(16);
			recipeList.add(new MerchantRecipe(
					new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY), 1, color1), 0, 5
					));
			if (random.nextBoolean())
			{
				recipeList.add(new MerchantRecipe(
						new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY), 1, (color1+random.nextInt(15)+1)%16), 0, 5
						));
			}
			color1 = random.nextInt(16);
			recipeList.add(new MerchantRecipe(
					new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(
							new Block[]{
									Blocks.WHITE_GLAZED_TERRACOTTA,
                        			Blocks.ORANGE_GLAZED_TERRACOTTA,
                        			Blocks.MAGENTA_GLAZED_TERRACOTTA,
                        			Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
                        			Blocks.YELLOW_GLAZED_TERRACOTTA,
                        			Blocks.LIME_GLAZED_TERRACOTTA,
                        			Blocks.PINK_GLAZED_TERRACOTTA,
                        			Blocks.GRAY_GLAZED_TERRACOTTA,
                        			Blocks.SILVER_GLAZED_TERRACOTTA,
                        			Blocks.CYAN_GLAZED_TERRACOTTA,
                        			Blocks.PURPLE_GLAZED_TERRACOTTA,
                        			Blocks.BLUE_GLAZED_TERRACOTTA,
                        			Blocks.BROWN_GLAZED_TERRACOTTA,
                        			Blocks.GREEN_GLAZED_TERRACOTTA,
                        			Blocks.RED_GLAZED_TERRACOTTA,
                        			Blocks.BLACK_GLAZED_TERRACOTTA
									}[color1]
							), 1), 0, 5
					));
			if (random.nextBoolean())
			{
				recipeList.add(new MerchantRecipe(
						new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(
								new Block[]{
										Blocks.WHITE_GLAZED_TERRACOTTA,
	                        			Blocks.ORANGE_GLAZED_TERRACOTTA,
	                        			Blocks.MAGENTA_GLAZED_TERRACOTTA,
	                        			Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
	                        			Blocks.YELLOW_GLAZED_TERRACOTTA,
	                        			Blocks.LIME_GLAZED_TERRACOTTA,
	                        			Blocks.PINK_GLAZED_TERRACOTTA,
	                        			Blocks.GRAY_GLAZED_TERRACOTTA,
	                        			Blocks.SILVER_GLAZED_TERRACOTTA,
	                        			Blocks.CYAN_GLAZED_TERRACOTTA,
	                        			Blocks.PURPLE_GLAZED_TERRACOTTA,
	                        			Blocks.BLUE_GLAZED_TERRACOTTA,
	                        			Blocks.BROWN_GLAZED_TERRACOTTA,
	                        			Blocks.GREEN_GLAZED_TERRACOTTA,
	                        			Blocks.RED_GLAZED_TERRACOTTA,
	                        			Blocks.BLACK_GLAZED_TERRACOTTA
										}[(color1+random.nextInt(15)+1)%16]
								), 1), 0, 5
						));
			}
		}
	}
	
	public class MasonMaster implements EntityVillager.ITradeList
	{
		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
		{
			// Slot 9
			recipeList.add( new MerchantRecipe(
					new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Item.getItemFromBlock(Blocks.QUARTZ_BLOCK), 1, random.nextBoolean() ? 2 : 0 ), 0, 5
					));
		}
	}
	
	
	
	
	// ------------------------------------- //
	// --- Trades added previous to v3.1 --- //
	// ------------------------------------- //
	
	
	// ---------------------------------------------------------- //
	// --- Specialty trades added to reward you for exploring --- //
	// ---------------------------------------------------------- //
	
	public class LibrarianVN implements EntityVillager.ITradeList { // The special trades for the Librarian
		
		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
			
			int enchantLevel = 1; //used for level-based enchant books 
			
			
			
			// --- MINESHAFT --- //
			
			enchantLevel = 4 + random.nextInt(2);
	        MerchantRecipe mineshaftForEnchantBook = new MerchantRecipe(
    				new ItemStack(ModItems.MINESHAFT_BOOK, 1),
    				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
    				ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("efficiency"), enchantLevel))
	        		);
			recipeList.add( mineshaftForEnchantBook );
	        
	        enchantLevel = 3;//2 + random.nextInt(2);
	        MerchantRecipe mineshaftForFortuneBook = new MerchantRecipe(
    				new ItemStack(ModItems.MINESHAFT_BOOK, 1),
    				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
    				ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("fortune"), enchantLevel))
	        		);
			recipeList.add( mineshaftForFortuneBook );
	        
	        
	        
	        
	        // --- STRONGHOLD --- //
	        
			enchantLevel = 1;
	        MerchantRecipe strongholdForInfinity = new MerchantRecipe(
    				new ItemStack(ModItems.STRONGHOLD_BOOK, 1),
    				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
    				ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("infinity"), enchantLevel))
	        		);
			recipeList.add( strongholdForInfinity );
	        
	        
	        
	        // --- FORTRESS --- //
	        
			enchantLevel = 3 + random.nextInt(2);
	        MerchantRecipe fortressForFeatherBook = new MerchantRecipe(
    				new ItemStack(ModItems.FORTRESS_BOOK, 1),
    				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
    				ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("feather_falling"), enchantLevel))
	        		);
			recipeList.add( fortressForFeatherBook );
	        
	        
	        
	        // --- MONUMENT --- //
	        
			enchantLevel = 1;
	        MerchantRecipe monumentForAquaBook = new MerchantRecipe(
    				new ItemStack(ModItems.MONUMENT_BOOK, 1),
    				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
    				ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("aqua_affinity"), enchantLevel))
	        		);
			recipeList.add( monumentForAquaBook );
	        
	        
	        
	        // --- JUNGLE TEMPLE --- //
	        
			enchantLevel = 4 + random.nextInt(2);
	        MerchantRecipe jungleTempleForBaneBook = new MerchantRecipe(
    				new ItemStack(ModItems.JUNGLE_TEMPLE_BOOK, 1),
    				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
    				ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("bane_of_arthropods"), enchantLevel))
	        		);
			recipeList.add( jungleTempleForBaneBook );
			
	        
	        
	        // --- DESERT PYRAMID --- //
			
	        enchantLevel = 3 + random.nextInt(2);
	        MerchantRecipe desertPyramidForBlastProtectionBook = new MerchantRecipe(
    				new ItemStack(ModItems.DESERT_PYRAMID_BOOK, 1),
    				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
    				ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("blast_protection"), enchantLevel))
	        		);
			recipeList.add( desertPyramidForBlastProtectionBook );
			
	        enchantLevel = 4 + random.nextInt(2);
	        MerchantRecipe desertPyramidForSmiteBook = new MerchantRecipe(
    				new ItemStack(ModItems.DESERT_PYRAMID_BOOK, 1),
    				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
    				ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("smite"), enchantLevel))
	        		);
			recipeList.add( desertPyramidForSmiteBook );
			
	        
			
			// --- VILLAGE -- //
			
			// Initialize book
	        ItemStack bookWithName = new ItemStack(Items.WRITTEN_BOOK);
	        if (bookWithName.getTagCompound() == null) {bookWithName.setTagCompound(new NBTTagCompound());} // Priming the book to receive information
	        
	        
			// Book author
 			String bookAuthor = Reference.MOD_NAME; // Print this name if there is an error retrieving the merchant's name
 			try {bookAuthor = ((EntityLiving)merchant).getCustomNameTag();}
 			catch (Exception e) {}
	        

			// Book title
			String bookTitle = "Custom Structure Name";
			switch ( random.nextInt(16) ) {
			case 0:
				// limit:   "--------------------------------"
				bookTitle = "Your Own Unique Location Name";
				//bookTitle = "Your Very Own Unique Location Name"; // Too long
				break;
			case 1:
				// limit:   "--------------------------------"
				bookTitle = "A Random Gobbledygook Name";
				//bookTitle = "A Random Nonsense Gobbledygook Name"; // Too long
				break;
			case 2:
				// limit:   "--------------------------------"
				bookTitle = "Name Things And Influence People";
				//bookTitle = "How To Name Things And Influence People"; // Too long
				break;
			case 3:
				// limit:   "--------------------------------"
				bookTitle = "Deed To A Non-Existent Place";
				//bookTitle = "Deed To A Place That Doesn't Exist"; // Too long
				break;
			case 4:
				bookTitle = "A Brand-New Structure Name";
				break;
			case 5:
				bookTitle = "A New Structure Name For You!";
				break;
			case 6:
				bookTitle = "Naming Things For Dummies";
				break;
			case 7:
				bookTitle = "Naming Things And You";
				break;
			case 8:
				bookTitle = "Live, Laugh, Name, Love";
				break;
			case 9:
				bookTitle = "Markovian Name Generation";
				break;
			case 10:
				bookTitle = "A Tale Of One City Name";
				break;
			case 11:
				bookTitle = "The Case of the Un-Named Thing";
				break;
			case 12:
				bookTitle = "The Unnamed";
				bookAuthor = "H.P. Lovenames";
				break;
			case 13:
				bookTitle = "Custom Structure Name";
				break;
			case 14:
				bookTitle = "Name Inspiration";
				break;
			case 15:
				bookTitle = "A One-Of-A-Kind Title";
				break;
			}
	        
			
	        // Book contents
	        String bookContents = "{\"text\":\""; // As of 1.9 I need to enclose book contents in {"text":"lorem ipsum"}

			switch (random.nextInt(8)) {
			case 0:
				bookContents += "If you've gone and built something grand, but don't know what name to give it--why not use this name:";
				break;
			case 1:
				bookContents += "Here's a custom-generated name for you to use, if you wish:";
				break;
			case 2:
				bookContents += "Coming up with names can be difficult. If you're drawing a blank, why not use this name:";
				break;
			case 3:
				bookContents += "Here's a unique name you can give to something if you need some inspiration:";
				break;
			case 4:
				bookContents += Reference.MOD_NAME+" uses a Markov chain to generate names for entities and structures."
						+ " If you've built something and you want to use VN to generate a new name for it, you can use this one:";
				bookAuthor = "AstroTibs";
				break;
			case 5:
				bookContents += "Feeling uninspired? Have writer's block? Feel free to use this customized location name:";
				break;
			case 6:
				bookContents += "Maybe you've just built or discovered something, and you're not sure what to name it. Why not name it this:";
				break;
			case 7:
				bookContents += "Coming up with a good, authentic location name can be hard. Well, this name might be neither good nor authentic, but maybe you'll use it anyway:";
				break;
			}
	        
			// Generate the structure name
	        String[] locationName = NameGenerator.newRandomName("village-mineshaft-stronghold-temple-fortress-monument-endcity-mansion-alienvillage");
	        bookContents += "\n\n" + (locationName[1]+" "+locationName[2]+" "+locationName[3]).trim();
	        
	        bookContents += "\"}"; // As of 1.9 I need to enclose book contents in {"text":"lorem ipsum"}
	        
	        
	        // Assemble the book
	        
	        if (bookAuthor!=null && !bookAuthor.equals("")) {
	        	try { bookWithName.getTagCompound().setString("author", bookAuthor.indexOf("(")!=-1 ? bookAuthor.substring(0, bookAuthor.indexOf("(")).trim() : bookAuthor ); }
	        	// If the target's name starts with a parenthesis for some reason, this will crash with an index OOB exception. In that case, add no author name.
	        	catch (Exception e) {}
	        }
	        else {bookWithName.getTagCompound().setString("author", "");}
	        
	        bookWithName.getTagCompound().setString("title", bookTitle ); // Set the title
	        
	        NBTTagList pagesTag = new NBTTagList();
	        pagesTag.appendTag(new NBTTagString(bookContents));
	        bookWithName.getTagCompound().setTag("pages", pagesTag);
	        
	        
	        // Add the trade
	        recipeList.add( new MerchantRecipe(
					new ItemStack(ModItems.VILLAGE_BOOK, 1),
					bookWithName) );
	        
		}

	}
	
	
	
	public class CartographerVN implements EntityVillager.ITradeList { // The special trades for the Cartographer
		
		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
			
			// --- STRONGHOLD --- //
			recipeList.add( new MerchantRecipe(new ItemStack(ModItems.STRONGHOLD_BOOK, 1), new ItemStack(Items.ENDER_EYE, 2)) );
			
			// --- FORTRESS --- //
			recipeList.add( new MerchantRecipe( new ItemStack(ModItems.FORTRESS_BOOK, 1), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_FIRE_RESISTANCE) ) );
			
			// --- SWAMP HUT --- //
			recipeList.add( new MerchantRecipe( new ItemStack(ModItems.SWAMP_HUT_BOOK, 1), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.STRONG_HARMING) ) );
			recipeList.add( new MerchantRecipe( new ItemStack(ModItems.SWAMP_HUT_BOOK, 1), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_HEALING) ) );
			
			// --- MONUMENT --- //
			recipeList.add( new MerchantRecipe(new ItemStack(ModItems.MONUMENT_BOOK, 1), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_WATER_BREATHING) ) );
			
			// --- JUNGLE TEMPLE --- //
			recipeList.add( new MerchantRecipe(new ItemStack(ModItems.JUNGLE_TEMPLE_BOOK, 1), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_STRENGTH) ) );
			
			// --- IGLOO --- //
			MerchantRecipe iglooForGoldenApple = new MerchantRecipe(new ItemStack(ModItems.IGLOO_BOOK, 1), new ItemStack(Items.GOLDEN_APPLE, 1));
			//iglooForGoldenApple.increaseMaxTradeUses(-6);
			recipeList.add( iglooForGoldenApple );
			
			MerchantRecipe iglooForSplashWeakness = new MerchantRecipe(new ItemStack(ModItems.IGLOO_BOOK, 1), PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.LONG_WEAKNESS) );
			//iglooForSplashWeakness.increaseMaxTradeUses(-6);
			recipeList.add( iglooForSplashWeakness ); // Splash Weakness (3:00)
			
			// --- VILLAGE -- //
			ItemStack tagWithName = new ItemStack(Items.NAME_TAG, 1).setStackDisplayName( NameGenerator.newRandomName("villager-angel-demon-dragon-goblin-golem")[2] );
			tagWithName.setRepairCost(99);
			recipeList.add( new MerchantRecipe(
					new ItemStack(ModItems.VILLAGE_BOOK, 1),
					tagWithName) );
			
		}
	}
	

	
}
