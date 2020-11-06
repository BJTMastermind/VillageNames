package astrotibs.villagenames.utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.name.NameGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

//Added in v3.1
public class FunctionsVN
{
	// Represents the 1.14+ village types
	public static enum VillageType
	{
		PLAINS, DESERT, TAIGA, SAVANNA, SNOWY;
		
		/**
		 * Determine the biometype to generate village buildings
		 */
		public static VillageType getVillageTypeFromBiome(World world, int posX, int posZ)
		{
			Biome biome = world.getBiome(new BlockPos(posX, 0, posZ));
			return getVillageTypeFromBiome(biome);
		}
		public static VillageType getVillageTypeFromBiome(BiomeProvider worldChunkManager, int posX, int posZ)
		{
			Biome biome = worldChunkManager.getBiome(new BlockPos(posX, 0, posZ));
			return getVillageTypeFromBiome(biome);
		}
		public static VillageType getVillageTypeFromBiome(Biome biome)
		{
			Set<BiomeDictionary.Type> typeTags = BiomeDictionary.getTypes(biome);
			
			// Ordered by personal priority. The first of these to be fulfilled gets returned
			if (biome.getBiomeName().toLowerCase().contains("taiga")) {return TAIGA;}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.CONIFEROUS) {return TAIGA;}}
			if (biome.getBiomeName().toLowerCase().contains("savanna")) {return SAVANNA;}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.SAVANNA) {return SAVANNA;}}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.SNOWY) {return SNOWY;}}
			if (biome.getBiomeName().toLowerCase().contains("desert")) {return DESERT;}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.SANDY) {return DESERT;}}
			//for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.PLAINS) {return PLAINS;}}
			
			// If none apply, send back Plains
			return PLAINS;
		}
		public static VillageType getVillageTypeFromName(String name, VillageType defaultType)
		{
			if (name.toUpperCase().equals("PLAINS"))       {return PLAINS;}
			else if (name.toUpperCase().equals("DESERT"))  {return DESERT;}
			else if (name.toUpperCase().equals("TAIGA"))   {return TAIGA;}
			else if (name.toUpperCase().equals("SAVANNA")) {return SAVANNA;}
			else if (name.toUpperCase().equals("SNOWY"))   {return SNOWY;}
			return defaultType;
		}
	}
	
	public static enum MaterialType
	{
		OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK,
		SAND, MESA, SNOW, MUSHROOM; // Added three more for special non-wood cases
		
		/**
		 * Determine the wood type to return for a given biome
		 */
		public static MaterialType getMaterialTemplateForBiome(World world, int posX, int posZ)
		{
			Biome biome = world.getBiome(new BlockPos(posX, 0, posZ));
			return getMaterialTemplateForBiome(biome);
		}
		public static MaterialType getMaterialTemplateForBiome(BiomeProvider worldChunkManager, int posX, int posZ)
		{
			Biome biome = worldChunkManager.getBiome(new BlockPos(posX, 0, posZ));
			return getMaterialTemplateForBiome(biome);
		}
		public static MaterialType getMaterialTemplateForBiome(Biome biome)
		{
			if (biome.getBiomeName().toLowerCase().contains("birch")) {return BIRCH;}
			if (biome.getBiomeName().toLowerCase().contains("roofed forest")) {return DARK_OAK;}
			
			Set<BiomeDictionary.Type> typeTags = BiomeDictionary.getTypes(biome);
			if (biome.getBiomeName().toLowerCase().contains("taiga")) {return SPRUCE;}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.CONIFEROUS) {return SPRUCE;}}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.JUNGLE) {return JUNGLE;}}
			if (biome.getBiomeName().toLowerCase().contains("savanna")) {return ACACIA;}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.SAVANNA) {return ACACIA;}}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.MESA) {return MESA;}}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.MUSHROOM) {return MUSHROOM;}}
			
			// Snow is only returned if there are no trees and it's a wasteland:
			boolean isSnowy = false;
			boolean isWasteland = false;
			for (BiomeDictionary.Type type : typeTags)
			{
				if (type==BiomeDictionary.Type.SNOWY) {isSnowy=true;}
				if (type==BiomeDictionary.Type.WASTELAND) {isWasteland=true;}
				if (type==BiomeDictionary.Type.DENSE || type==BiomeDictionary.Type.FOREST || type==BiomeDictionary.Type.LUSH || type==BiomeDictionary.Type.SPARSE) {isSnowy=false; isWasteland=false; break;}
			}
			if (isSnowy && isWasteland) {return SNOW;}
			
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.SANDY) {return SAND;}}
			
			// If none apply, send back Oak
			return OAK;
		}
		public static MaterialType getMaterialTypeFromName(String name, MaterialType defaultType)
		{
			if (name.toUpperCase().equals("OAK"))           {return OAK;}
			else if (name.toUpperCase().equals("SPRUCE"))   {return SPRUCE;}
			else if (name.toUpperCase().equals("BIRCH"))    {return BIRCH;}
			else if (name.toUpperCase().equals("JUNGLE"))   {return JUNGLE;}
			else if (name.toUpperCase().equals("ACACIA"))   {return ACACIA;}
			else if (name.toUpperCase().equals("DARK_OAK")) {return DARK_OAK;}
			else if (name.toUpperCase().equals("SAND"))     {return SAND;}
			else if (name.toUpperCase().equals("MESA"))     {return MESA;}
			else if (name.toUpperCase().equals("SNOW"))     {return SNOW;}
			else if (name.toUpperCase().equals("MUSHROOM")) {return MUSHROOM;}
			return defaultType;
		}
	}
	
	/**
	 * Determine the biometype of the biome the entity is currently in
	 */
	public static int returnBiomeTypeForEntityLocation(EntityLiving entity)
	{
		Biome entityBiome = entity.world.getBiome(entity.getPosition());
		return biomeToSkinType(entityBiome);
	}
	
	/**
	 * Inputs a biome and returns the skin type it translates to
	 */
	public static int biomeToSkinType(Biome biome)
	{
		// Get a list of tags for this biome
		Set<Type> typeTags = BiomeDictionary.getTypes(biome);
		// Bytes used to count conditions
		byte b = 0; byte b1 = 0;
		
		// Now check the type list against a series of conditions to determine which biome skin int to return.
		// These are arranged in priority order.
		
		// Nether type (13)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.NETHER)
			{
				return 13;
			}
		}
		
		// End type (12)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.END)
			{
				return 12;
			}
		}
		
		// Snow type (11)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.SNOWY)
			{
				return 11;
			}
		}
		
		// Mushroom type (10)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.MUSHROOM)
			{
				return 10;
			}
		}
		
		// Savanna type (9)
		b = 0;
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.HOT) {b |= 1;}
			if (type==BiomeDictionary.Type.SAVANNA) {b |= 2;}
			if (b==3)
			{
				return 9;
			}
		}
		
		// Desert type (8)
		b = 0; b1 = 0;
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.SANDY) {b |= 1; b1 |= 1;}
			if (type==BiomeDictionary.Type.HOT) {b |= 2;}
			if (type==BiomeDictionary.Type.DRY) {b |= 4;}
			if (type==BiomeDictionary.Type.MESA) {b1 |= 2;}
			if (b==7 || b1==3)
			{
				return 8;
			}
		}
		
		// Taiga type (7)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.CONIFEROUS)
			{
				return 7;
			}
		}
		
		// Swamp type (6)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.SWAMP)
			{
				return 6;
			}
		}
		
		// Jungle type (5)
		b = 0;
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.COLD || type==BiomeDictionary.Type.SPARSE) {b = 0; break;}
			if (type==BiomeDictionary.Type.JUNGLE) {b |= 1;}
			if (type==BiomeDictionary.Type.WET) {b |= 2;}
		}
		if (b==3)
		{
			return 5;
		}
		
		// Aquatic type (4)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.OCEAN || type==BiomeDictionary.Type.RIVER || type==BiomeDictionary.Type.BEACH)
			{
				return 4;
			}
		}
		
		// Forest type (3)
		b = 0;
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.SPARSE) {b = 0; break;}
			if (type==BiomeDictionary.Type.FOREST) {b |= 1;}
		}
		if (b==1)
		{
			return 3;
		}
		
		// Highland type (2)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.MOUNTAIN || type==BiomeDictionary.Type.HILLS)
			{
				return 2;
			}
		}
		
		// Magical type (1)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.MAGICAL)
			{
				return 1;
			}
		}
		
		// Plains type (0)
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.PLAINS)
			{
				return 0;
			}
		}
		
		// In case none of these ticked off, return -1.
		// This will cause the 
		return -1;
	}


    /**
     * Produces a shuffled array of integers from value a to value b.
     * Primarily used to randomize the colored wool variations granted to shepherds.
     */
    public static int[] shuffledIntArray(int a, int b, Random rgen)
    {
		int size = b-a+1;
		int[] array = new int[size];
 
		for(int i=0; i< size; i++) {array[i] = a+i;}
 
		for (int i=0; i<array.length; i++)
		{
		    int randomPosition = rgen.nextInt(array.length);
		    int temp = array[i];
		    array[i] = array[randomPosition];
		    array[randomPosition] = temp;
		}
		
		return array;
	}
    
	
    public static int pickRandomCareerForProfession(int profession, Random random)
    {
			switch(profession) {
			
			case 0: // FARMER
				return 1 + random.nextInt(4);
			
			case 1: // LIBRARIAN
				return 1 + random.nextInt(2);
			
			case 2: // PRIEST
				return 1;// + this.myWorld.rand.nextInt(1);
			
			case 3: // BLACKSMITH
				return 1 + random.nextInt(GeneralConfig.modernVillagerTrades ? 4 : 3); // Added in v3.1trades - Mason
			
			case 4: // BUTCHER
				return 1 + random.nextInt(2);
			
			case 5: // NITWIT
				return 1;// + this.myWorld.rand.nextInt(1);
		}
			return -1;
    }
	
    // Added in v3.1trades
    
    /**
     * Inputs a villager and checks its trades, modifying the "current level" of trades if necessary.
     * @param villager
     */
    public static void modernizeVillagerTrades(EntityVillager villager)
    {
    	// Ignores this for non-vanilla villagers
    	String villagerForgeProfessionRegistryName = villager.getProfessionForge().getRegistryName().toString();
		if (villagerForgeProfessionRegistryName.length()<10 || !villagerForgeProfessionRegistryName.substring(0, 10).equals("minecraft:")) {return;}
		
    	Random random = new Random(); // v3.2.1 - deactivated seed random
    	
    	IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
    	int careerLevel = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerLevel", "field_175562_bw"});
    	MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );

    	if (buyingList==null) {return;} // To prevent crashes I guess?
    	
    	MerchantRecipe merchantrecipe;
		Item moditem; // Used as a placeholder to attempt to add modded trades to villagers
		ArrayList<MerchantRecipe> merchantRecipeArray; // Used when a random item is needed to fill a slot
		int enchantvalue; // Used as a holder for enchantment levels to help tweak enchanted item prices
		Enchantment enchantment;
		int color1; // Used when one OR two colorized items appear
		ItemStack itemStackColorizable; // Holder for itemstacks that can be dyed
		boolean allowTreasure = true; // Whether enchanted villager items should have treasure enchantments
		
		// Increment the VN profession level just to make sure you don't keep searching.
		ims.setProfessionLevel(ims.getProfessionLevel()+1);
		
    	switch (ims.getProfession())
    	{
    		case 0: // Farmer type: summon minecraft:villager ~ ~ ~ {Profession:0}
    			
    			switch (ims.getCareer())
    			{
    				case 1: // Farmer
    					
    					switch (ims.getProfessionLevel())
    					{
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.WHEAT, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.POTATO, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.CARROT, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.BREAD)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.WHEAT, 20 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.POTATO, 26 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.CARROT, 22 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.BEETROOT, 15 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.BREAD, 6 ), 0, 5
	    										));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 2: // Apprentice
	    						
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.PUMPKIN), Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.PUMPKIN_PIE)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.PUMPKIN), 6 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.PUMPKIN_PIE, 4 ), 0, 5) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.APPLE, 4 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 3: // Journeyman
	    						
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.MELON_BLOCK), Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.APPLE)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 5
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.MELON_BLOCK), 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5 // BE version; the JE version uses whole melons
	    										));
	    								
	    								// Slot 6
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKIE, 18 ), 0, 4 // BE version; the JE version uses whole melons
	    										));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 4: // Expert
	    						
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.COOKIE)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.CAKE)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 7
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EGG, 16 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5 // BE version; the JE version uses whole melons
	    										));
	    								
	    								// Slot 8
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.CAKE, 1 ), 0, 5 // BE version; the JE version uses whole melons
	    										));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 5: // Master
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.GOLDEN_CARROT)
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.SPECKLED_MELON)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 9
	    						merchantRecipeArray = new ArrayList<MerchantRecipe>();
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.GOLDEN_CARROT, 3 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.SPECKLED_MELON, 3 ), 0, 5) );
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
    					}
    					break;
    				
    				case 2: // Fisherman
    					
    					switch (ims.getProfessionLevel())
    					{
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.STRING, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.COAL, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.FISH, Items.EMERALD, Items.COOKED_FISH)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.STRING, 20 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.COAL, 10 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								// TODO - Emerald to Bucket of Cod (Added 1.13)
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Items.FISH, 6, 0 ), new ItemStack( Items.COOKED_FISH, 6, 0 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 2: // Apprentice
	    						
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.FISHING_ROD)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.FISH, 15, 0 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								// TODO - Campfire (Added 1.14)
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Items.FISH, 6, 1 ), new ItemStack( Items.COOKED_FISH, 6, 1 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 3: // Journeyman
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.FISH, 1, 1), new ItemStack(Items.EMERALD, 1))
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.FISHING_ROD)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 5
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.FISH, 13, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
	    						// Slot 6: Enchanted Fishing Rod
								enchantvalue = 5 + random.nextInt(15);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.FISHING_ROD, 1), enchantvalue, allowTreasure ), 0, 2 ) );
								
								break;
	    						
	    					case 4: // Expert
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.FISH, 1, 2), new ItemStack(Items.EMERALD, 1))
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.BOAT, Items.EMERALD)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 7
								merchantRecipeArray = new ArrayList<MerchantRecipe>();
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.FISH, 6, 2 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( FunctionsVN.returnRandomBoatTypeForVillager(villager), 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5) );
								buyingList.add(merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
	    						
	    					case 5: // Master
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.FISH, 1, 3), new ItemStack(Items.EMERALD, 1))
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 8
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.FISH, 4, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
								
								break;
    					}
    					break;
    				
    				case 3: // Shepherd
    					
    					switch (ims.getProfessionLevel())
    					{
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.WOOL), Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.SHEARS)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.WOOL), 18, new int[]{0,12,15,7}[random.nextInt(4)] ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6
	    										));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 2 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.SHEARS, 1 ), 0, 5
	    										));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 2: // Apprentice
	    						
	    						// Counting all 16 wool is an ass ache. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(Blocks.WOOL))
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						
								// Slot 3
								if (random.nextInt(5)<=1) {
									if (random.nextBoolean()) {
										while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlackBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 0 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
									}
									else {
										while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeWhiteBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 15 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
									}
								}
								else {
									buyingList.add(new MerchantRecipe(
    										new ItemStack( Items.DYE, 12, new int[]{8,10,12}[random.nextInt(3)] ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
    										));
								}
								
																
								// Slot 4
								color1 = random.nextInt(16);
								if (random.nextBoolean())
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, color1), 0, 5
    										));
									if (random.nextBoolean())
    								{
										buyingList.add(new MerchantRecipe(
    											new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, (color1+random.nextInt(15)+1)%16), 0, 5
	    										));
    								}
								}
								else
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(Blocks.CARPET), 4, color1), 0, 5
    										));
									if (random.nextBoolean())
    								{
										buyingList.add(new MerchantRecipe(
    											new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Item.getItemFromBlock(Blocks.CARPET), 4, (color1+random.nextInt(15)+1)%16), 0, 5
	    										));
    								}
								}
								
	    						break;
	    						
	    					case 3: // Journeyman
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 1), new ItemStack(Items.EMERALD)) // Red dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 7), new ItemStack(Items.EMERALD)) // Light Gray dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 9), new ItemStack(Items.EMERALD)) // Pink dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 11), new ItemStack(Items.EMERALD)) // Yellow dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 14), new ItemStack(Items.EMERALD)) // Orange dye
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.BED)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 5
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.DYE, 12, new int[]{1,7,9,11,14}[random.nextInt(5)] ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
								// Slot 6
								color1 = random.nextInt(16);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), ModObjects.getModBedItemstack(color1),
										0, 5
										));
								
								break;
	    						
	    					case 4: // Expert
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 2), new ItemStack(Items.EMERALD)) // Green dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 3), new ItemStack(Items.EMERALD)) // Brown dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.EMERALD)) // Blue dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 5), new ItemStack(Items.EMERALD)) // Purple dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 6), new ItemStack(Items.EMERALD)) // Cyan dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.DYE, 1, 13), new ItemStack(Items.EMERALD)) // Magenta dye
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.BANNER)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots

	    						// Slot 7
	    						if (random.nextBoolean()) {
	    							switch (random.nextInt(3))
	    							{
	    							case 0: // Special handler for green dye
	    								while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeGreenBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 2 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    								
	    							case 1: // Special handler for brown dye
	    								while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeBrownBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    								
	    							case 2: // Special handler for blue dye
	    								while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlueBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    							}
	    							
	    						}
	    						else {
	    							buyingList.add(new MerchantRecipe(
											new ItemStack( Items.DYE, 12, new int[]{5,6,13}[random.nextInt(3)] ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
											));
	    						}
								
								// Slot 8
								color1 = random.nextInt(16);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.BANNER, 1, color1), 0, 5
										));
								if (random.nextBoolean())
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.BANNER, 1, (color1+random.nextInt(15)+1)%16), 0, 5
    										));
								}
								
								break;
	    						
	    					case 5: // Master
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.PAINTING)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, 2 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.PAINTING, 3 ), 0, 5
										));
								
								break;
    					}
    					break;
    					
    				case 4: // Fletcher
    					
    					switch (ims.getProfessionLevel())
    					{
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.STRING, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.ARROW)
	    									) { // Trade block detected!
		    							
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.STICK, 32 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6
	    										));
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.ARROW, 16 ), 0, 5) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.GRAVEL), 10 ), new ItemStack( Items.FLINT, 10 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 2: // Apprentice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.BOW)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.GRAVEL), Items.EMERALD, Items.FLINT)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.FLINT, 26 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 2 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.BOW, 1 ), 0, 5
	    										));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 3: // Journeyman
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.STRING, Items.EMERALD)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 5
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.STRING, 14 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
								// Slot 6
								// TODO - Crossbow (Added in 1.14)
								
								break;
	    						
	    					case 4: // Expert
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.FEATHER, Items.EMERALD)
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.BOW) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 7
	    						buyingList.add(new MerchantRecipe(
										new ItemStack( Items.FEATHER, 24 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
								
								// Slot 8
	    						enchantvalue = 5 + random.nextInt(15);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.BOW, 1), enchantvalue, allowTreasure ), 0, 2 ) );
								
								break;
	    						
	    					case 5: // Master
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Item.getItemFromBlock(Blocks.TRIPWIRE_HOOK), Items.EMERALD)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								buyingList.add(new MerchantRecipe(
										new ItemStack( Item.getItemFromBlock(Blocks.TRIPWIRE_HOOK), 8 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
								
								// TODO - Slot 10 would be either a Tipped Arrow or a Crossbow (Added in 1.14) 
								
			    				while (true)
			    				{
			    					PotionType dippingpotion = (PotionType)PotionType.REGISTRY.getRandomObject(random);
			    					
			    					if (dippingpotion.getEffects()!=null && dippingpotion.getEffects().size()>0 )
									{
			    						ItemStack fivetippedarrows = PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW, 5), dippingpotion);
			    						
			    						if (fivetippedarrows != null)
			    						{
			    							buyingList.add(new MerchantRecipe(
													new ItemStack( Items.EMERALD, 2 ), new ItemStack( Items.ARROW, 5 ), fivetippedarrows, 0, 3
													));
											break;
			    						}
									}
			    				}
								break;
    					}
    					break;
    			}
				break;
    			
				
    		case 1: // Librarian type: summon minecraft:villager ~ ~ ~ {Profession:1}
    			
    			switch(ims.getCareer())
    			{
    				case 1: // Librarian
    					
    					switch(ims.getProfessionLevel())
    					{
	    					
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
        						for (int i=0; i<buyingList.size(); i++)
        						{
            						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
        							if (
        									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.PAPER, Items.EMERALD)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.BOOK, Items.EMERALD, Items.ENCHANTED_BOOK)
        									) { // Trade block detected!
        								
        								// Add a new block with one or more trades
        								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
        								
        								
        								// Slot 1
        								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
        										new ItemStack( Items.PAPER, 24 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, ims.getCareer()==2 ? 6 : 4
        										));
        								
        								
    									// Slot 2
    									merchantRecipeArray = new ArrayList<MerchantRecipe>();
    									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 6 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Item.getItemFromBlock(Blocks.BOOKSHELF), 3 ), 0, 5) );
    									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
    									buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
    									
        								
        								// Erase the old block...
        								eraseTrades(buyingList, addToSlot, cbs);
        								
        								// We don't need to keep searching.
        								break;
        							}
        						}
	        					
	    						break;
	    						
	    					case 2: // Apprentice
	    						
        						// Go forward through list and identify when you get the vanilla trades
        						for (int i=0; i<buyingList.size(); i++)
        						{
            						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
        							if (
        									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.BOOK, Items.EMERALD)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.COMPASS)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Item.getItemFromBlock(Blocks.BOOKSHELF))
        									) { // Trade block detected!
        								
        								
        								// Add a new block with one or more trades
        								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
        								

        								// Slot 3
        								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
        										new ItemStack( Items.BOOK, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
        										));
        								
        								// Slot 4
        								merchantRecipeArray = new ArrayList<MerchantRecipe>();
        								//TODO - Lantern (Added in 1.14)
        								merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
        								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
        								
        								
        								// Erase the old block...
        								eraseTrades(buyingList, addToSlot, cbs);
        								
        								// We don't need to keep searching.
        								break;
        							}
        						}
	        					
	        					break;
	    						
	    					case 3: // Journeyman
	    						
        						// Go forward through list and identify when you get the vanilla trades
        						for (int i=0; i<buyingList.size(); i++)
        						{
            						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
        							if (
        									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.WRITTEN_BOOK, Items.EMERALD)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.CLOCK)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Item.getItemFromBlock(Blocks.GLASS))
        									) { // Trade block detected!
        								
        								// Add a new block with one or more trades
        								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
        								
        								
        								// Slot 5
    									buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
    											new ItemStack( Items.DYE, 5, 0 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
    											));
    									
    									// Slot 6
    									merchantRecipeArray = new ArrayList<MerchantRecipe>();
    									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Item.getItemFromBlock(Blocks.GLASS), 4 ), 0, 5) );
    									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
    									buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
    									
        								
        								// Erase the old block...
        								eraseTrades(buyingList, addToSlot, cbs);
        								
        								// We don't need to keep searching.
        								break;
        							}
        						}
	        					
	        					break;
	    						
	    					case 4: // Expert
	    						
        						// Go BACKWARD through list and yeet the first enchanted book
        						for (int i=buyingList.size()-1; i>=0; i--)
        						{
            						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
        							if (
        									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.BOOK, Items.EMERALD, Items.ENCHANTED_BOOK)
        									) { // Trade block detected!
        								
        								// Add a new block with one or more trades
        								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
        								
        								
    									// Slot 7
    									buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
    											new ItemStack( Items.WRITABLE_BOOK, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 2
    											));
    									
    									// Slot 8
    									merchantRecipeArray = new ArrayList<MerchantRecipe>();
    									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COMPASS, 1 ), 0, 5) );
    									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 5 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.CLOCK, 1 ), 0, 5) );
    									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
    									buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
    									
        								
        								// Erase the old block...
        								eraseTrades(buyingList, addToSlot, cbs);
        								
        								// We don't need to keep searching.
        								break;
        							}
        						}
	        					
	        					break;
	    						
	    					case 5: // Master
	    						
        						// Go BACKWARD through list and yeet the first enchanted book
        						for (int i=buyingList.size()-1; i>=0; i--)
        						{
            						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
        							if (
        									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.BOOK, Items.EMERALD, Items.ENCHANTED_BOOK)
        									) { // Trade block detected!
        								
        								// Add a new block with one or more trades
        								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other

        								// Slot 9
        								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
        										new ItemStack( Items.EMERALD, 20 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.NAME_TAG, 1 ), 0, 5
        										));
        								
        								// Erase the old block...
        								eraseTrades(buyingList, addToSlot, cbs);
        								
        								// We don't need to keep searching.
        								break;
        							}
        						}
	    						break;
	    						
	    						
	    					// Higher cases reserved for treasure trades
	    						
	    					case 6:
	    						
	    						// Go BACKWARD through list and yeet the first enchanted book
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.NAME_TAG)
	    									) { // Trade block detected!
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, i, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						
	    						// Continue downward and add a treasure trade
	    						
	    					case 7:
	    					case 8:
	        					// Add treasure trade
	    						if (GeneralConfig.treasureTrades) {addTreasureTrade(buyingList, villager, 1, random);}
	    						break;
	    						
    					}
    					break;
    					
    				case 2: // Cartographer: summon minecraft:villager ~ ~ ~ {Profession:1}
    					
    					switch(ims.getProfessionLevel())
    					{
	    					
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
	    							int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.PAPER, Items.EMERALD)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 1
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack(Items.PAPER, 24), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.EMERALD, 1), 0, 6
	    										));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack(Items.EMERALD, 7), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.MAP, 1), 0, 5
	    										));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 2: // Apprentice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
	    							
	    							int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.COMPASS, Items.EMERALD)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack(Item.getItemFromBlock(Blocks.GLASS_PANE), 11), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.EMERALD, 1), 0, 5
	    										));
	    								
	    								// Slot 4
	    	    						// Ocean Explorer Map (Added in 1.11)
	    								// I'm instead going to use the BE trade: Item Frame
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 7 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.ITEM_FRAME, 1 ), 0, 5
	    										));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 3: // Journeyman
	    						
	    						// Go BACKWARD through list and identify when you get the vanilla trades. This is because you'll already have added Emerald->Map
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							
	    							int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.MAP)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 5
	    								// Ocean OR Woodland Explorer Map; inspired by BE
	    								boolean isOceanMonument = random.nextBoolean();
	    								
	    								MerchantRecipe explorermap = modernExplorerMapTrade(villager,
	    										isOceanMonument ? 13 : 14,
	    										isOceanMonument ? "Monument" : "Mansion",
	    										isOceanMonument ? MapDecoration.Type.MONUMENT : MapDecoration.Type.MANSION,
	    												0, 2);
	    								if (explorermap != null)
	    								{
	    									buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), explorermap);
	    								}
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
	    						
	    					case 4: // Expert
	    						
	    						// Go BACKWARD through list and identify when you get the vanilla trades in order not to eliminate the wrong explorer map
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.COMPASS, Items.FILLED_MAP)
	    									&& checkExplorerMapType(buyingList.get(Math.min(i, buyingList.size()-1)).getItemToSell()).equals("filled_map.monument")
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.COMPASS, Items.FILLED_MAP)
	    									&& checkExplorerMapType(buyingList.get(Math.min(i+1, buyingList.size()-1)).getItemToSell()).equals("filled_map.mansion")
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 7 - BE: Compass
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack(Items.COMPASS, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.EMERALD, 1), 0, 5
	    										));
	    								
	    								// Slot 8
	        							color1 = random.nextInt(16);
	        							buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	        									new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.BANNER, 1, color1), 0, 5
	        									));
	        							if (random.nextBoolean())
	        							{
	        								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	        										new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack(Items.BANNER, 1, (color1+random.nextInt(15)+1)%16), 0, 5
	        										));
	        							}
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	        					break;
	    						
	    					case 5: // Master
	    						
    							// Slot 9 TODO - Globe Pattern
	    						

	    						// Changed in v3.1banner
	    						// Instead, offer a banner with the village's pattern!
	    						
	    		    			Object[] villageBannerData = BannerGenerator.getVillageBannerData(villager);
	    						NBTTagCompound bannerNBT = new NBTTagCompound();
	    						String villageNameForBanner = "";
	    						if (villageBannerData!=null) {
	    							bannerNBT = (NBTTagCompound) villageBannerData[0];
	    							villageNameForBanner = (String) villageBannerData[1];}
	    						
	    						if (!(villageNameForBanner.equals("")))
	    						{
	    							buyingList.add(new MerchantRecipe(
	    									new ItemStack( Items.EMERALD, 2 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), BannerGenerator.makeBanner(bannerNBT, villageNameForBanner), 0, 5
	    									));
	    						}
	    						else // No banner was found or is available. INSTEAD, sell a new banner with a random design.
	    						{
	    							Object[] newRandomBanner = BannerGenerator.randomBannerArrays(villager.world.rand, -1, -1);
	    							ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
	    							ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
	    																	
	    							buyingList.add(new MerchantRecipe(
	    									new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), BannerGenerator.makeBanner(patternArray, colorArray), 0, 5
	    									));
	    						}
	    		    			    							
	        					break;
	    					
	    					case 6:
	    					case 7: // Changed in v3.1banner to allow treasures up to level 8
	    					case 8:
	    						if (GeneralConfig.treasureTrades) {addTreasureTrade(buyingList, villager, 2, random);}
	    						break;
    					}
    					break;
    					
    			}
    			break;
				
    		case 2: // Priest type: summon minecraft:villager ~ ~ ~ {Profession:2}
    			
    			switch (ims.getCareer())
    			{
    				case 1: // Cleric
    					
		    			switch (ims.getProfessionLevel())
						{
		    				case 1: // Novice
		    					
		    					// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
		    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.ROTTEN_FLESH, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.GOLD_INGOT, Items.EMERALD)
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 1
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.ROTTEN_FLESH, 32 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6
												));
										
										// Slot 2
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.REDSTONE, 4 ), 0, 5
												));
										
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
		    					
		    				case 2: // Apprentice
		    					
		    					// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
		    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.REDSTONE)
											&& hasSameItemsAndMeta(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), new ItemStack(Items.EMERALD), new ItemStack(Items.DYE, 1, 4))
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										
										// Slot 3
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.GOLD_INGOT, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 4
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.DYE, 1, 4 ), 0, 5
												));
										
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
		    					
		    				case 3: // Journeyman
		    					
		    					// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.ENDER_PEARL)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Item.getItemFromBlock(Blocks.GLOWSTONE))
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 5
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.RABBIT_FOOT, 2 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 6
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Item.getItemFromBlock(Blocks.GLOWSTONE), 1 ), 0, 5
												));
										
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
		    					
		    				case 4: // Expert
		    					
		    					// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									
		    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.EXPERIENCE_BOTTLE)
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 7
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								// TODO - Scute (Added in 1.13)
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.GLASS_BOTTLE, 9 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
										// Slot 8
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 5 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.ENDER_PEARL, 1 ), 0, 5
												));
										
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
		    					
		    				case 5: // Master

	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.NETHER_WART, Items.EMERALD)
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.EXPERIENCE_BOTTLE)
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.NETHER_WART, 22 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
								
								// Slot 10
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, 3), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EXPERIENCE_BOTTLE, 1 ), 0, 5
										));
								
								break;
						}
    					break;
    			}
				break;
				
				
    		case 3: // Blacksmith type: summon minecraft:villager ~ ~ ~ {Profession:3}
    			
    			switch (ims.getCareer())
    			{
    				case 1: // Armorer
    					
    					switch(ims.getProfessionLevel())
    					{
    						case 1: // Novice
    							
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.COAL, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.IRON_HELMET)
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 1
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.COAL, 15 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6
												));
										
										// Slot 2
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 5 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.IRON_HELMET, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 9 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.IRON_CHESTPLATE, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 7 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.IRON_LEGGINGS, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.IRON_BOOTS, 1 ), 0, 3) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));

										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 2: // Apprentice
    							
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.IRON_INGOT, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.IRON_CHESTPLATE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 3
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.IRON_INGOT, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 4
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
										// TODO - Bell (Added in 1.14)
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.CHAINMAIL_BOOTS, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.CHAINMAIL_LEGGINGS, 1 ), 0, 3) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));

										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 3: // Journeyman
    							
    							// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.DIAMOND, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.DIAMOND_CHESTPLATE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 5
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.LAVA_BUCKET, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 6
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.CHAINMAIL_HELMET, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.CHAINMAIL_CHESTPLATE, 1 ), 0, 3) );
	    								
	    								// Added in v3.1banner - The shield sold has a village banner on it, if applicable.
	    								Object[] villageShieldData = BannerGenerator.getVillageBannerData(villager);
	    								NBTTagCompound bannerForShield = new NBTTagCompound();
	    								String villageNameForShield = "";
	    								if (villageShieldData!=null) {
	    									bannerForShield = (NBTTagCompound) villageShieldData[0];
	    									villageNameForShield = (String) villageShieldData[1];}
	    								
	    								merchantRecipeArray.add(new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 5 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), 
	    										(villageShieldData!=null) ? BannerGenerator.makeShieldWithBanner(bannerForShield, villageNameForShield) : new ItemStack( Items.SHIELD, 1 )
	    										, 0, 3
	    										));
	    								
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 4: // Expert
    							
    							// Go forward through list and identify when you get the vanilla trades
    							for (int i=0; i<buyingList.size(); i++)
    							{
    								int cbs = 0;
    								if (
    										hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.CHAINMAIL_BOOTS)
    										&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.CHAINMAIL_LEGGINGS)
    										&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.CHAINMAIL_HELMET)
    										&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.CHAINMAIL_CHESTPLATE)
    										) { // Trade block detected!
    									
    									int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
    									
										// Slot 7
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.DIAMOND, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 8
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
										enchantvalue = 5 + random.nextInt(15);
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_LEGGINGS, 1), enchantvalue, allowTreasure ), 0, 2
												));
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(6+random.nextInt(3)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_BOOTS, 1), enchantvalue, allowTreasure ), 0, 2
												));
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
    									// We don't need to keep searching.
    									break;
    								}
    							}
    							break;
    							
    						case 5: // Master

    							for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0;
									if (
	    									(hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.DIAMOND_HELMET) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted() )		
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.DIAMOND_CHESTPLATE) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted() )
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
    							merchantRecipeArray = new ArrayList<MerchantRecipe>();
								enchantvalue = 5 + random.nextInt(15);
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(6+random.nextInt(3)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_HELMET, 1), enchantvalue, allowTreasure ), 0, 2
										));
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_CHESTPLATE, 1), enchantvalue, allowTreasure ), 0, 2
										));
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
    					}
    					break;
    				
    				case 2: // Weaponsmith

    					switch(ims.getProfessionLevel())
    					{
    						case 1: // Novice
    							
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.COAL, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.IRON_AXE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 1
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.COAL, 15 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6
												));
										
										// Slot 2
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.IRON_AXE, 1 ), 0, 3
												));

										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 2: // Apprentice
    							
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.IRON_INGOT, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.IRON_SWORD)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 3
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.IRON_INGOT, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 4
										enchantvalue = 5 + random.nextInt(15);
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.IRON_SWORD, 1), enchantvalue, allowTreasure ), 0, 2
												));
										
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 3: // Journeyman
    							
    							// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.DIAMOND, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.DIAMOND_SWORD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.DIAMOND_AXE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 5
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.FLINT, 24 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 6
										// TODO - Bell (Added in 1.14)
										
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 4: // Expert
    							
    							for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0;
									if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.DIAMOND, Items.EMERALD)		
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.DIAMOND_AXE) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 7
								buyingList.add( new MerchantRecipe(
										new ItemStack( Items.DIAMOND, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
	    						// Slot 8
								enchantvalue = 5 + random.nextInt(15);
								buyingList.add( new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_AXE, 1), enchantvalue, allowTreasure ), 0, 2
										));
								
								break;
    							
    						case 5: // Master

	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0;
									if (
	    									(hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.DIAMOND_SWORD) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								enchantvalue = 5 + random.nextInt(15);
								buyingList.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(6+random.nextInt(3)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_SWORD, 1), enchantvalue, allowTreasure ), 0, 2
										));
								
								break;
    					}
    					break;
    				
    				case 3: // Toolsmith

    					switch(ims.getProfessionLevel())
    					{
    						case 1: // Novice
    							
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.COAL, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.IRON_SHOVEL)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 1
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.COAL, 15 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6
												));
										
										// Slot 2
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.STONE_AXE, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.STONE_PICKAXE, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.STONE_SHOVEL, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.STONE_HOE, 1 ), 0, 3) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));

										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 2: // Apprentice
    							
    							// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.IRON_INGOT, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.IRON_PICKAXE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 3
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.IRON_INGOT, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 4
										// TODO - Bell (Added in 1.14)
										
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 3: // Journeyman
    							
    							// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.DIAMOND, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.DIAMOND_PICKAXE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 5
										buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.FLINT, 30 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 6
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
										enchantvalue = 5 + random.nextInt(15);
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.IRON_AXE, 1), enchantvalue, allowTreasure ), 0, 2
												));
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.IRON_SHOVEL, 1), enchantvalue, allowTreasure ), 0, 2
												));
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.IRON_PICKAXE, 1), enchantvalue, allowTreasure ), 0, 2
												));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.DIAMOND_HOE, 1 ), 0, 2) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
										
										// Erase the old block...
										eraseTrades(buyingList, addToSlot, cbs);
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    							
    						case 4: // Expert
    							
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0;
									if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.DIAMOND, Items.EMERALD)		
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.DIAMOND_AXE) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.DIAMOND_SHOVEL) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 7
								buyingList.add( new MerchantRecipe(
										new ItemStack( Items.DIAMOND, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
	    						// Slot 8
								merchantRecipeArray = new ArrayList<MerchantRecipe>();
								enchantvalue = 5 + random.nextInt(15);
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_AXE, 1), enchantvalue, allowTreasure ), 0, 2
										));
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(6+random.nextInt(2)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_SHOVEL, 1), enchantvalue, allowTreasure ), 0, 2
										));
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
    							
    						case 5: // Master

	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0;
									if (
	    									(hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.DIAMOND_PICKAXE)	&& buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
	    						enchantvalue = 5 + random.nextInt(15);
								buyingList.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_PICKAXE, 1), enchantvalue, allowTreasure ), 0, 2
										));
								
								break;
    					}
    					break;
    				
    				case 4: // Mason
    					/*
    					switch(ims.getProfessionLevel())
    					{
    						case 1: // Novice
    							
    							// Go forward through list and idenfi
    							
    						case 2: // Apprentice
    							
    							
    							
    						case 3: // Journeyman
    							
    							
    							
    						case 4: // Expert
    							
    							for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0;
									if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.QUARTZ, Items.EMERALD)		
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY))		
	    									
	    									|| ((GeneralConfig.addConcrete) &&
		    									( hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.WHITE_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.ORANGE_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.MAGENTA_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.LIGHT_BLUE_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.YELLOW_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.LIME_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.PINK_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.GRAY_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.SILVER_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.CYAN_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.PURPLE_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.BLUE_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.BROWN_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.GREEN_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.RED_GLAZED_TERRACOTTA))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(ModBlocksVN.BLACK_GLAZED_TERRACOTTA))
	    											))
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
								
								break;
    							
    						case 5: // Master
    							
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							int cbs = 0;
									if (hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Item.getItemFromBlock(Blocks.QUARTZ_BLOCK))) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						
	    						
								break;
    					}*/
    				break;
    			}
    			break;
    			
    			
    		case 4: // Butcher type: summon minecraft:villager ~ ~ ~ {Profession:4}
    			
    			switch (ims.getCareer())
    			{
	    			
					case 1: // Butcher
						
		    			switch (ims.getProfessionLevel())
						{
		    				case 1: // Novice
		    					
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.PORKCHOP, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.CHICKEN, Items.EMERALD)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.CHICKEN, 14 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.RABBIT, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.PORKCHOP, 7 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.RABBIT_STEW, 1 ), 0, 5
	    										));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
		    					
		    				case 2: // Apprentice

	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.COAL, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.COOKED_PORKCHOP)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.COOKED_CHICKEN)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other

	    								// Slot 3
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.COAL, 15 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_RABBIT, 5 ), 0, 5) ); // BE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_CHICKEN, 8 ), 0, 5) ); // JE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_PORKCHOP, 5 ), 0, 5) ); // JE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_MUTTON, 8 ), 0, 5) ); // VN price
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
		    					
		    				case 3: // Journeyman
		    					
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.BEEF, Items.EMERALD)
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.MUTTON, Items.EMERALD)
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.COOKED_BEEF)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 5
	    						merchantRecipeArray = new ArrayList<MerchantRecipe>();
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.BEEF, 10 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.MUTTON, 8 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5) ); // BE price
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								// Slot 6
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_BEEF, 5 ), 0, 5 // VN price
										));
								
								break;
								
		    				case 4: // Expert

		    					// Dried Kelp Block to Emerald
		    					while (true)
		    					{
									moditem = FunctionsVN.getItemFromName(ModObjects.kelpBOP);
									
									if (moditem != null)
									{
										buyingList.add(new MerchantRecipe(
											new ItemStack( moditem, 64 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
											));
										return;
									}
									break;
								}
		    					
		    					
		    				case 5: // Master
		    					// TODO - Sweet Berries (Added in 1.14)
		    					
		    					// INSTEAD, for levels 4 and 5, add a trade that was not previously added--and do so at an increased value to the player:
		    			    	ArrayList<MerchantRecipe> possibleHighButcherTrades = new ArrayList<MerchantRecipe>();
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.CHICKEN, Math.max(14 - ims.getProfessionLevel() + 1, 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.RABBIT, Math.max(4 - ims.getProfessionLevel() + 1, 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.PORKCHOP, Math.max(7 - ims.getProfessionLevel() + 1, 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_RABBIT, Math.min(5 + ims.getProfessionLevel() - 2, 64) ), 0, 5) ); // BE price
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_CHICKEN, Math.min(8 + ims.getProfessionLevel() - 2, 64) ), 0, 5) ); // JE price
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_PORKCHOP, Math.min(5 + ims.getProfessionLevel() - 2, 64) ), 0, 5) ); // JE price
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.COOKED_MUTTON, Math.min(8 + ims.getProfessionLevel() - 2, 64) ), 0, 5) ); // VN price
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.BEEF, Math.max(10 - ims.getProfessionLevel() + 3, 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5) );
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.MUTTON, Math.max(8 - ims.getProfessionLevel() + 3, 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5) ); // BE price
								
								
								// We'll comb through the possibilities until one is selected and added, or none are selected because they all match already-existing trades.
		    					while (possibleHighButcherTrades.size()>0)
		    					{
		    						// Pop off a random recipe to evaluate. If we don't use it, it will be discarded.
		    						MerchantRecipe chosenRecipe = possibleHighButcherTrades.remove(random.nextInt(possibleHighButcherTrades.size()));
		    						
		    			        	// 2: check to see if that type already exists on the list
		    			        	boolean matched = false;
		    			        	for (int i=0; i<buyingList.size(); i++)
		    			    		{
		    			    			if (
		    			    					hasSameItems(buyingList.get(Math.min(i, buyingList.size()-1)), chosenRecipe.getItemToBuy().getItem(),
		    			    							chosenRecipe.getSecondItemToBuy() == null ? null : chosenRecipe.getSecondItemToBuy().getItem(),
		    			    							chosenRecipe.getItemToSell().getItem())
		    			    					)
		    			    			{
		    			    				matched=true;
		    			    				break;
		    			    			}
		    			    		}
		    			        	
		    			        	// 3: If not, add it and break out.
		    			        	if (!matched)
		    			        	{
		    			        		buyingList.add(chosenRecipe);
		    			        		break;
		    			        	}
		    			        	// 4: If so, draw again.
		    			    	}
		    					
		    					break;
		    					
						}
						break;
						
					case 2: // Leatherworker
						
		    			switch (ims.getProfessionLevel())
						{
		    				case 1: // Novice
		    					
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.LEATHER, Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.LEATHER_LEGGINGS)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 1
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.LEATHER, 6 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 6
	    										));
	    								
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								
	    								itemStackColorizable = new ItemStack(Items.LEATHER_LEGGINGS);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 3 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), itemStackColorizable, 0, 2) );
	    								
	    								itemStackColorizable = new ItemStack(Items.LEATHER_CHESTPLATE);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 7 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), itemStackColorizable, 0, 2) );
	    								
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
		    					
		    				case 2: // Apprentice
		    					
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
    	    						
    	    						// Extract an NBT Tag compound just so you can check whether it's been colorized
    	    						//NBTTagCompound nbttagcompound = (buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)).getItemToSell()).getTagCompound();
    	    						//if (nbttagcompound == null) {nbttagcompound = new NBTTagCompound();}
    	    						
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.LEATHER_CHESTPLATE) && (buyingList.get(Math.min(i+(cbs-1), buyingList.size()-1)).getItemToSell().isItemEnchanted())
	    									//&& (!nbttagcompound.hasKey("display", 10))
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.FLINT, 26 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								
	    								itemStackColorizable = new ItemStack(Items.LEATHER_HELMET);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 5 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), itemStackColorizable, 0, 2) );
	    								
	    								itemStackColorizable = new ItemStack(Items.LEATHER_BOOTS);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), itemStackColorizable, 0, 2) );
	    								
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
		    					
		    				case 3: // Journeyman

	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.SADDLE)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 5
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.RABBIT_HIDE, 9 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								
	    								// Slot 6
	    								itemStackColorizable = new ItemStack(Items.LEATHER_CHESTPLATE);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								enchantvalue = 5 + random.nextInt(15);
	    								buyingList.add(MathHelper.clamp(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 7 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
	    										EnchantmentHelper.addRandomEnchantment(random, itemStackColorizable, enchantvalue, allowTreasure ), 0, 2
	    												));
	    								
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
		    					
		    				case 4: // Expert
		    					
		    					// TODO - Scute (Added in 1.13)
		    					// TODO - Leather Horse Armor (Added in 1.14)
		    					
		    					
		    					// INSTEAD, for levels 4 and 5, add a trade that was not previously added--and do so at an increased value to the player:
		    			    	ArrayList<MerchantRecipe> possibleExpertLeatherworkerTrades = new ArrayList<MerchantRecipe>();
		    			    	
								itemStackColorizable = new ItemStack(Items.LEATHER_LEGGINGS);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, Math.max(3 - 2*(ims.getProfessionLevel()-1), 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), itemStackColorizable, 0, 2) );
								
								itemStackColorizable = new ItemStack(Items.LEATHER_CHESTPLATE);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, Math.max(7 - 2*(ims.getProfessionLevel()-1), 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), itemStackColorizable, 0, 2) );
		    			    	
								itemStackColorizable = new ItemStack(Items.LEATHER_HELMET);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, Math.max(5 - 2*(ims.getProfessionLevel()-2), 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), itemStackColorizable, 0, 2) );
								
								itemStackColorizable = new ItemStack(Items.LEATHER_BOOTS);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, Math.max(4 - 2*(ims.getProfessionLevel()-2), 1) ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), itemStackColorizable, 0, 2) );
								

								// We'll comb through the possibilities until one is selected and added, or none are selected because they all match already-existing trades.
		    					while (possibleExpertLeatherworkerTrades.size()>0)
		    					{
		    						// Pop off a random recipe to evaluate. If we don't use it, it will be discarded.
		    						MerchantRecipe chosenRecipe = possibleExpertLeatherworkerTrades.remove(random.nextInt(possibleExpertLeatherworkerTrades.size()));
		    						
		    			        	// 2: check to see if that type already exists on the list
		    			        	boolean matched = false;
		    			        	for (int i=0; i<buyingList.size(); i++)
		    			    		{
		    			    			if (
		    			    					hasSameItems(buyingList.get(Math.min(i, buyingList.size()-1)), chosenRecipe.getItemToBuy().getItem(),
		    			    							chosenRecipe.getSecondItemToBuy() == null ? null : chosenRecipe.getSecondItemToBuy().getItem(),
		    			    							chosenRecipe.getItemToSell().getItem())
		    			    					&& // Also check if sell items are concurrently enchanted 
		    			    					(
		    			    							( !buyingList.get(Math.min(i, buyingList.size()-1)).getItemToSell().isItemEnchanted() && !chosenRecipe.getItemToSell().isItemEnchanted() )
		    			    							|| ( buyingList.get(Math.min(i, buyingList.size()-1)).getItemToSell().isItemEnchanted() && chosenRecipe.getItemToSell().isItemEnchanted() )
		    			    							)
		    			    					)
		    			    			{
		    			    				matched=true;
		    			    				break;
		    			    			}
		    			    		}
		    			        	
		    			        	// 3: If not, add it and break out.
		    			        	if (!matched)
		    			        	{
		    			        		buyingList.add(chosenRecipe);
		    			        		break;
		    			        	}
		    			        	// 4: If so, draw again.
		    			    	}
		    					
		    					break;
		    					
		    				case 5: // Master

	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.EMERALD, Items.SADDLE)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 9
	    						merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    						
	    						itemStackColorizable = new ItemStack(Items.LEATHER_HELMET);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								enchantvalue = 5 + random.nextInt(15);
								merchantRecipeArray.add( new MerchantRecipe(
										new ItemStack( Items.EMERALD, 5 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
										EnchantmentHelper.addRandomEnchantment(random, itemStackColorizable, enchantvalue, allowTreasure ), 0, 2
												));
	    						
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 6 ), new ItemStack( Item.getItemFromBlock(Blocks.AIR)), new ItemStack( Items.SADDLE, 1 ), 0, 3) );
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
						}
						break;

    			}
				break;
				
    	}
    	
    	ReflectionHelper.setPrivateValue(EntityVillager.class, villager, buyingList, new String[]{"buyingList", "field_70963_i"});
    }
    
    
    /**
     * Checks a merchant trade against the two buying items and the selling item.
     */
    public static boolean hasSameItems(MerchantRecipe recipe, Item itemToBuy, @Nullable Item secondItemToBuy, Item itemToSell)
    {
        return
        		itemToBuy == recipe.getItemToBuy().getItem()
        		&& itemToSell == recipe.getItemToSell().getItem() ?
        				(secondItemToBuy == null || secondItemToBuy == Item.getItemFromBlock(Blocks.AIR))
        				&& (recipe.getSecondItemToBuy() == null || recipe.getSecondItemToBuy().getItem() == Item.getItemFromBlock(Blocks.AIR))
        				|| (secondItemToBuy != null && secondItemToBuy != Item.getItemFromBlock(Blocks.AIR))
        				&& (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().getItem() != Item.getItemFromBlock(Blocks.AIR) )
        				&& secondItemToBuy == recipe.getSecondItemToBuy().getItem()
        				: false;
    }
    public static boolean hasSameItems(MerchantRecipe recipe, Item itemToBuy, Item itemToSell)
    {return hasSameItems(recipe, itemToBuy, null, itemToSell);}
    
    /**
     * Checks a merchant trade against the two buying items and the selling item, discriminating with meta
     */
    private static boolean hasSameItemsAndMeta(MerchantRecipe recipe, ItemStack itemStackToBuy, @Nullable ItemStack secondItemStackToBuy, ItemStack itemStackToSell)
    {
        return 
        		(itemStackToBuy.getItem() == recipe.getItemToBuy().getItem() && (itemStackToBuy.getMetadata() == recipe.getItemToBuy().getMetadata() || itemStackToBuy.getItemDamage() == recipe.getItemToBuy().getItemDamage() ))
        		&& (itemStackToSell.getItem() == recipe.getItemToSell().getItem() && (itemStackToSell.getMetadata() == recipe.getItemToSell().getMetadata() || itemStackToSell.getItemDamage() == recipe.getItemToSell().getItemDamage() )) ?
        				(secondItemStackToBuy == null || secondItemStackToBuy.getItem() == Item.getItemFromBlock(Blocks.AIR))
        				&& (recipe.getSecondItemToBuy() == null || recipe.getSecondItemToBuy().getItem() == Item.getItemFromBlock(Blocks.AIR) )
        				|| (secondItemStackToBuy != null && secondItemStackToBuy.getItem() != Item.getItemFromBlock(Blocks.AIR))
        				&& (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().getItem() != Item.getItemFromBlock(Blocks.AIR) )
        				&& (secondItemStackToBuy.getItem() == recipe.getSecondItemToBuy().getItem() && (secondItemStackToBuy.getMetadata() == recipe.getSecondItemToBuy().getMetadata() || secondItemStackToBuy.getItemDamage() == recipe.getSecondItemToBuy().getItemDamage()) )
        				: false;
    }
    public static boolean hasSameItemsAndMeta(MerchantRecipe recipe, ItemStack itemToBuy, ItemStack itemToSell)
    {return hasSameItemsAndMeta(recipe, itemToBuy, null, itemToSell);}
    

    /**
     * Erase N trades from the trade list, starting at index i.
     */
    private static void eraseTrades(MerchantRecipeList buyingList, int i, int N)
    {for (int r=0; r < N; r++) {
    	try {buyingList.remove(i);}
    	catch (Exception e) {if (GeneralConfig.debugMessages) LogHelper.error("Exception trying to remove villager trade index "+i);}
    	}}
    
    
    /**
     * Item equivalent of Block.getBlockFromName(String)
     */
    public static Item getItemFromName(String name)
    {
        ResourceLocation resourcelocation = new ResourceLocation(name);
        
        if (Item.REGISTRY.containsKey(resourcelocation))
        {
            return (Item)Item.REGISTRY.getObject(resourcelocation);
        }
        else
        {
            try
            {
                return (Item)Item.REGISTRY.getObjectById(Integer.parseInt(name));
            }
            catch (NumberFormatException var3)
            {
                return null;
            }
        }
    }
    
    /**
     * Randomizer to return a treasure trade for a Librarian or a Cartographer
     */
    private static void addTreasureTrade(MerchantRecipeList buyingList, EntityVillager villager, int career, Random random)
    {
    	// summon minecraft:villager ~ ~ ~ {Profession:1}
    	
    	// 1: select a Treasure Trade
    	ArrayList<MerchantRecipe> merchantRecipeArray = new ArrayList<MerchantRecipe>();
    	int enchantLevel=1; //used for level-based enchant books
    	
    	while (true)
    	{
    		if (career==1) // Librarian
        	{
        		// --- MINESHAFT --- //
        		enchantLevel = 4 + random.nextInt(2);
                MerchantRecipe mineshaftForEnchantBook = new MerchantRecipe(
        				new ItemStack(ModItems.MINESHAFT_BOOK, 1),
        				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("efficiency"), enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( mineshaftForEnchantBook );
                
                enchantLevel = 3;//2 + random.nextInt(2);
                MerchantRecipe mineshaftForFortuneBook = new MerchantRecipe(
        				new ItemStack(ModItems.MINESHAFT_BOOK, 1),
        				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("fortune"), enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( mineshaftForFortuneBook );
                
                
                // --- STRONGHOLD --- //
        		enchantLevel = 1;
                MerchantRecipe strongholdForInfinity = new MerchantRecipe(
        				new ItemStack(ModItems.STRONGHOLD_BOOK, 1),
        				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("infinity"), enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( strongholdForInfinity );
                
                
                // --- FORTRESS --- //
        		enchantLevel = 3 + random.nextInt(2);
                MerchantRecipe fortressForFeatherBook = new MerchantRecipe(
        				new ItemStack(ModItems.FORTRESS_BOOK, 1),
        				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("feather_falling"), enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( fortressForFeatherBook );
                
                
                // --- MONUMENT --- //
        		enchantLevel = 1;
                MerchantRecipe monumentForAquaBook = new MerchantRecipe(
        				new ItemStack(ModItems.MONUMENT_BOOK, 1),
        				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("aqua_affinity"), enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( monumentForAquaBook );
                
        		
                // --- JUNGLE TEMPLE --- //
        		enchantLevel = 4 + random.nextInt(2);
                MerchantRecipe jungleTempleForBaneBook = new MerchantRecipe(
        				new ItemStack(ModItems.JUNGLE_TEMPLE_BOOK, 1),
        				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("bane_of_arthropods"), enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( jungleTempleForBaneBook );
        		
                
                // --- DESERT PYRAMID --- //
                enchantLevel = 3 + random.nextInt(2);
                MerchantRecipe desertPyramidForBlastProtectionBook = new MerchantRecipe(
        				new ItemStack(ModItems.DESERT_PYRAMID_BOOK, 1),
        				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("blast_protection"), enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( desertPyramidForBlastProtectionBook );
        		
                enchantLevel = 4 + random.nextInt(2);
                MerchantRecipe desertPyramidForSmiteBook = new MerchantRecipe(
        				new ItemStack(ModItems.DESERT_PYRAMID_BOOK, 1),
        				new ItemStack(Items.EMERALD, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(Enchantment.getEnchantmentByLocation("smite"), enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( desertPyramidForSmiteBook );
        		
        		
        		// --- VILLAGE -- //
        		
        		// Initialize book
    	        ItemStack bookWithName = new ItemStack(Items.WRITTEN_BOOK);
    	        if (bookWithName.getTagCompound() == null) {bookWithName.setTagCompound(new NBTTagCompound());} // Priming the book to receive information
    	        
        		// Book author
        		String bookAuthor = villager.getCustomNameTag();

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

    			// Generated names
    	        String[] locationName = NameGenerator.newRandomName("village-mineshaft-stronghold-temple-fortress-monument-endcity-mansion-alienvillage", new Random());
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
    	        merchantRecipeArray.add( new MerchantRecipe(
    					new ItemStack(ModItems.VILLAGE_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					bookWithName, 0, 2) );
    	        
        	}
        	else if (career==2) // Cartographer
        	{
        		// Potion IDs taken from https://www.minecraftinfo.com/IDList.htm
        		
        		// --- STRONGHOLD --- //
    			merchantRecipeArray.add( new MerchantRecipe( new ItemStack(ModItems.STRONGHOLD_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					new ItemStack(Items.ENDER_EYE, 2)
    					, 0, 2
    					) );
    			
    			// --- FORTRESS --- //
    			merchantRecipeArray.add( new MerchantRecipe( new ItemStack(ModItems.FORTRESS_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_FIRE_RESISTANCE) 
    					, 0, 2
    					) );
    			
    			// --- SWAMP HUT --- //
    			merchantRecipeArray.add( new MerchantRecipe( new ItemStack(ModItems.SWAMP_HUT_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.STRONG_HARMING) 
    					, 0, 2
    					) );
    			merchantRecipeArray.add( new MerchantRecipe( new ItemStack(ModItems.SWAMP_HUT_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_HEALING) 
    					, 0, 2
    					) );
    			
    			// --- MONUMENT --- //
    			merchantRecipeArray.add( new MerchantRecipe(new ItemStack(ModItems.MONUMENT_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_WATER_BREATHING) 
    					, 0, 2
    					) );
    			
    			// --- JUNGLE TEMPLE --- //
    			merchantRecipeArray.add( new MerchantRecipe(new ItemStack(ModItems.JUNGLE_TEMPLE_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_STRENGTH) 
    					, 0, 2
    					) );
    			
    			// --- IGLOO --- //
    			MerchantRecipe iglooForGoldenApple = new MerchantRecipe(new ItemStack(ModItems.IGLOO_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					new ItemStack(Items.GOLDEN_APPLE, 1)
    					, 0, 2
    					);
    			//iglooForGoldenApple.increaseMaxTradeUses(-6);
    			merchantRecipeArray.add( iglooForGoldenApple );
    			
    			MerchantRecipe iglooForSplashWeakness = new MerchantRecipe(new ItemStack(ModItems.IGLOO_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.LONG_WEAKNESS) 
    					, 0, 2
    					);
    			//iglooForSplashWeakness.increaseMaxTradeUses(-6);
    			merchantRecipeArray.add( iglooForSplashWeakness ); // Splash Weakness (3:00)
    			
    			// --- VILLAGE -- //
    			ItemStack tagWithName = new ItemStack(Items.NAME_TAG, 1).setStackDisplayName( NameGenerator.newRandomName("villager-angel-demon-dragon-goblin-golem-pet", new Random())[2] );
    			tagWithName.setRepairCost(99);
    			merchantRecipeArray.add( new MerchantRecipe(
    					new ItemStack(ModItems.VILLAGE_BOOK, 1), new ItemStack( Item.getItemFromBlock(Blocks.AIR)),
    					tagWithName
    					, 0, 2
    					) );
        	}
        	else {break;}
    		
        	MerchantRecipe chosenRecipe = merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size()));
        	
        	// 2: check to see if that type already exists on the list
        	boolean matched = false;
        	for (int i=0; i<buyingList.size(); i++)
    		{
        		
    			if (
    					hasSameItems(buyingList.get(Math.min(i, buyingList.size()-1)), chosenRecipe.getItemToBuy().getItem(),
    							chosenRecipe.getSecondItemToBuy() == null ? null : chosenRecipe.getSecondItemToBuy().getItem(),
    							chosenRecipe.getItemToSell().getItem())
    					)
    			{
    				matched=true;
    				break;
    			}
    		}
        	
        	// 3: If not, add it and break out.
        	if (!matched)
        	{
        		buyingList.add(chosenRecipe);
        		break;
        	}
        	// 4: If so, draw again.
    	}
    }
    
    /**
     * Combines colors and determines the resulting integer-encoded color.
     * Colors are input as an int array of any length, where each element is a dye meta color (e.g. 2 is magenta).
     */
    public static int combineDyeColors(int[] metaStream)
    {
    	// Dye integers
    	final int[] colorInts = new int[]{
    			16777215, // White
    			14188339, // Orange
    			11685080, // Magenta
    			 6724056, // Light blue
    			15066419, // Yellow
    			 8375321, // Lime
    			15892389, // Pink
    			 5000268, // Gray
    			10066329, // Light gray
    			 5013401, // Cyan
    			 8339378, // Purple
    			 3361970, // Blue
    			 6704179, // Brown
    			 6717235, // Green
    			10040115, // Red
    			 1644825  // Black
    		};
    	
    	// Sum up r, g, b values
    	
    	// Initialize holder r, g, b
    	int r = 0; int g = 0; int b = 0;
    	
    	for (int i=0; i<metaStream.length; i++)
    	{
    		r += colorInts[metaStream[i]]/(256*256);
    		g += (colorInts[metaStream[i]]/256)%256;
    		b += colorInts[metaStream[i]]%256;
    	}
    	
    	// Divide r, g, b by number of combined dyes
    	r /= metaStream.length;
    	g /= metaStream.length;
    	b /= metaStream.length;
    	
    	// Re-encode r, g, b into final integer
    	return r*(256*256) + g*(256) + b;
    }
    
    
    /**
     * Colorizes an itemstack using an encoded color integer. This is used to give a random two-dye color to leather armor for the leatherworker. 
     */
    public static ItemStack colorizeItemstack(ItemStack colorizableitemstack, int colorInt)
    {
		// Get the itemstack's tag compound
        NBTTagCompound nbttagcompound = colorizableitemstack.getTagCompound();
        
        // If the itemstack has no tag compound, make one
        if (nbttagcompound == null)
        {
            nbttagcompound = new NBTTagCompound();
            colorizableitemstack.setTagCompound(nbttagcompound);
        }
        
        // Form the display tag and apply it to the compound if needed.
        NBTTagCompound tagcompounddisplay = nbttagcompound.getCompoundTag("display");
        if (!nbttagcompound.hasKey("display", 10)) {nbttagcompound.setTag("display", tagcompounddisplay);}
        
        // Apply the color
        tagcompounddisplay.setInteger("color", colorInt);
        
        return colorizableitemstack;
    }
    
    
    /**
     * Adds a random enchanted book to a villager's trade offers in the style of 1.14
     * Copied and modified from EntityVillager.ListEnchantedBookForEmeralds
     */
    public static MerchantRecipe modernEnchantedBookTrade(Random random, int tradeuses)
    {
        Enchantment enchantment = (Enchantment)Enchantment.REGISTRY.getRandomObject(random);
        int i = MathHelper.getInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        ItemStack itemstack = Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(enchantment, i));
        int emeraldprice = 2 + random.nextInt(5 + i * 10) + 3 * i;

        if (enchantment.isTreasureEnchantment()) {emeraldprice *= 2;}
        
        if (emeraldprice > 64) {emeraldprice = 64;}

        return (new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldprice), new ItemStack(Items.BOOK), itemstack, 0, tradeuses));
    }
    
    
    /**
     * Adapted from EntityVillager.TreasureMapForEmeralds because it's not visible.
     */
    public static MerchantRecipe modernExplorerMapTrade(IMerchant merchant,
    		int value, String destination, MapDecoration.Type destinationType, int toolUsesIn, int maxTradeUsesIn)
    {
    	
        int i = value;
        World world = merchant.getWorld();
        BlockPos blockpos = world.findNearestStructure(destination, merchant.getPos(), true);

        if (blockpos != null)
        {
            ItemStack itemstack = ItemMap.setupNewMap(world, (double)blockpos.getX(), (double)blockpos.getZ(), (byte)2, true, true);
            ItemMap.renderBiomePreviewMap(world, itemstack);
            MapData.addTargetDecoration(itemstack, blockpos, "+", destinationType);
            itemstack.setTranslatableName("filled_map." + destination.toLowerCase(Locale.ROOT));
            return new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack, toolUsesIn, maxTradeUsesIn);
        }
        else
        {
        	return null;
        }
    }
    
    
    /**
     * Used to check what type of Explorer map, if any, you're looking at.
     */
    public static String checkExplorerMapType(ItemStack itemToCheck)
    {
		// Get the itemstack's tag compound
        NBTTagCompound nbttagcompound = itemToCheck.getTagCompound();
        
        // If the itemstack has no tag compound, make one
        if (nbttagcompound == null)
        {
            nbttagcompound = new NBTTagCompound();
            itemToCheck.setTagCompound(nbttagcompound);
        }
        
        // Form the display tag and apply it to the compound if needed.
        NBTTagCompound tagcompounddisplay = nbttagcompound.getCompoundTag("display");
        if (!nbttagcompound.hasKey("display", 10)) {return "";}
        
        if (tagcompounddisplay.hasKey("LocName")) {return tagcompounddisplay.getString("LocName");}
        // Apply the color
        
        return "";
    }
    
    
    // Added in v3.1banner
    
    /**
     * Inputs an array of objects and a corresponding array of weights, and returns a randomly-selected element
     * with a probability proportional to its weight.
     * 
     * These inputs must be equal length. If they are not, you get back null.
     * Additionally, and this goes without saying: the individual weights must be non-negative and their sum must be positive.
     * 
     * Adapted from https://stackoverflow.com/questions/6737283/weighted-randomness-in-java
     */
    public static Object weightedRandom(Object elementArray, double[] weightArray, Random random)
    {
    	if (Array.getLength(elementArray) != weightArray.length) {return null;}
    	else
    	{
    		// Compute the total weight of all items together
    		double totalWeight = 0D;
    		for (int i=0; i<weightArray.length; i++ )
    		{
    		    totalWeight += weightArray[i];
    		}
    		
    		// Now choose a random item
    		int randomIndex = -1;
    		double randomObject = random.nextDouble() * totalWeight;
    		for (int i = 0; i < Array.getLength(elementArray); ++i)
    		{
    			randomObject -= weightArray[i];
    		    if (randomObject <= 0.0d)
    		    {
    		        randomIndex = i;
    		        break;
    		    }
    		}
    		return Array.get(elementArray, randomIndex);
    	}
    }

    // Added in v3.2
	/**
	 * Determine the skinTone of the biome the entity is currently in
	 */
	public static int returnSkinToneForEntityLocation(EntityLiving entity)
	{
		Biome entityBiome = entity.world.getBiome(entity.getPosition());
		return biomeToSkinTone(entityBiome, new Random());
	}
	
	/**
	 * Inputs a biome and returns a randomized skin tone value.
	 * Values are drawn from a Gaussian distribution and then rounded to the nearest integer.
	 * The mean value of the distribution is the sum of values associated with each biome tag. Most are 0 but some increase or decrease.
	 * The StDev value decreases as the Poisson error for number of biome tags that have non-zero value.
	 * Config values determine the overall StDev scale, and how quickly the StDev shrinks with number of biome tags.
	 * Possible output values range from -4 (darkest) to 3 (lightest) with 0 being the standard Minecraft villager.
	 */
	public static int biomeToSkinTone(Biome biome, Random random)
	{
		// Get a list of tags for this biome
		Set<Type> typeTags = BiomeDictionary.getTypes(biome);
		
		// Now check the type list, modifying the median for particular biomes.
		int skin_mu = 0; // Center of the Gaussian distribution; start as 0 (default villager)
		int skin_tags = 0; // How many skin-altering tags were counted
		
		for (BiomeDictionary.Type type : typeTags)
		{
			// Cold, snowy, high-latitude values lighten skin:
			if (type==BiomeDictionary.Type.COLD)
			{
				skin_mu += 1;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.WET)
			{
				skin_mu += 1;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.CONIFEROUS)
			{
				skin_mu += 2;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.NETHER)
			{
				skin_mu += 2;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.END)
			{
				skin_mu += 2;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.MOUNTAIN)
			{
				skin_mu += 1;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.SNOWY)
			{
				skin_mu += 2;
				skin_tags++;
				continue;
			}
			
			// Hot, dry, low-latitude values darken skin:
			if (type==BiomeDictionary.Type.HOT)
			{
				skin_mu -= 1;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.DRY)
			{
				skin_mu -= 1;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.SAVANNA)
			{
				skin_mu -= 2;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.JUNGLE)
			{
				skin_mu -= 1;
				skin_tags++;
				continue;
			}
			if (type==BiomeDictionary.Type.MESA)
			{
				skin_mu -= 1;
				skin_tags++;
				continue;
			}
		}
		
		// Now, draw a Gaussian-distributed random value centered on skin_mu:
		
		// Standard deviation, reduced by number of skin tags found and influenced by user's config values
		float sigma = MathHelper.sqrt( 1D/(skin_tags*GeneralConfig.villagerSkinToneVarianceAnnealing + 1D) ) * GeneralConfig.villagerSkinToneVarianceScale; 
		double skin_r = random.nextGaussian()*sigma + skin_mu;
		
		// Return this value clamped to the darkest and lightest values
		return MathHelper.clamp((int) Math.round(skin_r), -4, 3);
	}
    

	
	/**
	 * Use this to adjust the villagers' trade when you talk to them.
	 * It essentially scans for and removes all vanilla trades that aren't allowed by the new trade system.
	 */
	public static void monitorVillagerTrades(EntityVillager villager)
	{
    	NBTTagCompound compound = new NBTTagCompound();
    	villager.writeEntityToNBT(compound);
		int profession = compound.getInteger("Profession");
		int career = compound.getInteger("Career");
		int careerLevel = compound.getInteger("CareerLevel");
		//career = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"});

    	// Try modifying trades
		// Modified in v3.1trades
        // Check trading list against modern trades
		IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
		
		// Get the current buying list
		MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
		if (GeneralConfig.modernVillagerTrades // Added condition in v3.1trades
        		//&& villager.ticksExisted%4==0 // Check only ever four ticks
        		&& (buyingList!=null && buyingList.size()>0)
        		&& ims.getProfessionLevel() < careerLevel)
        {
        	// Modernize the trades
			FunctionsVN.modernizeVillagerTrades(villager);
        }
		
		// If you're talking to a vanilla Villager, check the trades list
		if (profession == 1) {
			
			// summon minecraft:villager ~ ~ ~ {Profession:1}
			if (
					career == 1 && careerLevel >= 3
					&& GeneralConfig.writtenBookTrade
					) { // Fix the Librarian's written book trade
				//if (GeneralConfigHandler.debugMessages) {LogHelper.info("This is a villager with profession " + profession + ", career " + career + ", careerLevel " + careerLevel);}
				try {
					
					//if (GeneralConfigHandler.debugMessages) {LogHelper.info( "buyingList " + buyingList + " with size " + buyingList.size() );}
					for (int i=5; i < buyingList.size(); i++) { // The written book trade is at least at index 5
						
						MerchantRecipe extractedRecipe = buyingList.get(i);
						ItemStack itemToBuy1 = extractedRecipe.getItemToBuy();
						ItemStack itemToBuy2 = extractedRecipe.getSecondItemToBuy();
						ItemStack itemToSell = extractedRecipe.getItemToSell();
						//if (GeneralConfigHandler.debugMessages) {LogHelper.info("itemToBuy1 " + itemToBuy1 + ", itemToBuy2 " + itemToBuy2 + ", itemToSell " + itemToSell );}
						if (
								itemToBuy1.getItem() == Items.WRITTEN_BOOK && itemToBuy1.getCount() == 2
								&& itemToBuy2.getCount() == 0
								&& itemToSell.getItem() == Items.EMERALD && itemToSell.getCount() == 1
								) { // This is the malformed trade. Fix it below.
							//if (GeneralConfigHandler.debugMessages) {LogHelper.info("naughty trade detected" );}
							buyingList.set(i, new MerchantRecipe( new ItemStack(Items.WRITTEN_BOOK, 1), new ItemStack(Items.EMERALD, 1) ));
							if (GeneralConfig.debugMessages) {LogHelper.info("Replacing malformed written book trade for Librarian with ID " + villager.getEntityId());}
							break;
						}
					}
				}
				catch (Exception e) {}
			}

			if (
					(
						   (career == 1 && careerLevel > 6) // Librarian
						|| (career == 2 && careerLevel > 4) // Cartographer
							)
					&& GeneralConfig.treasureTrades
					&& !GeneralConfig.modernVillagerTrades // Added in v3.1trades
					) { // Villager is a Cartographer. Weed out the higher-level trades

				try {
					
					if ( buyingList.size() > careerLevel + (career==2 ? 1 : 5) ) {

						// First, do a scan and remove duplicates.
						
						for (int i=(career==2 ? 5 : 11); i < buyingList.size(); i++) {
							
							ItemStack stackBuyToCompare  = buyingList.get(i).getItemToBuy();  // Villager BUYS item from you
							ItemStack stackSellToCompare = buyingList.get(i).getItemToSell(); // Villager SELLS item to you
							
							for (int j=buyingList.size()-1; j>i; j--) {
								
								ItemStack stackBuyToEvaluate  = buyingList.get(j).getItemToBuy();
								ItemStack stackSellToEvaluate = buyingList.get(j).getItemToSell();
								
								Set enchantmentCompare  = EnchantmentHelper.getEnchantments(stackSellToCompare).keySet();
								Set enchantmentEvaluate  = EnchantmentHelper.getEnchantments(stackSellToEvaluate).keySet();
								
								if (
										stackBuyToCompare.getItem()  == stackBuyToEvaluate.getItem()
										&& stackSellToCompare.getItem() == stackSellToEvaluate.getItem()
										&& enchantmentCompare.equals(enchantmentEvaluate) // Compares the enchantments of the trades. Both are -1 and so returns "true" if not both are enchanted books.
										) {
									// This is a duplicate trade. Remove it.
									//if (GeneralConfigHandler.debugMessages) {LogHelper.info("Buying list length " + buyingList.size() + ". Duplicate trade detected at index " + j);}
									buyingList.remove(j);
								}
							}
						}
						
						// Then, randomly remove entries from the end until the trading list is the right length.
						int loopKiller = 0;
						
						while ( buyingList.size() > careerLevel + (career==2 ? 1 : 5) ) {
							
							int indexToRemove = (careerLevel-1 + (career==2 ? 1 : 5)) + villager.world.rand.nextInt( buyingList.size() - (careerLevel-1 + (career==2 ? 1 : 5)) );
							
							//if (GeneralConfigHandler.debugMessages) {LogHelper.info("Buying list length " + buyingList.size() + ". Removing excess trade at index " + indexToRemove);}
							buyingList.remove( indexToRemove );
							
							loopKiller++;
							if (loopKiller >=100) {
								if (GeneralConfig.debugMessages) {
									LogHelper.warn("Infinite loop suspected while pruning librarian trade list.");
								}
								break;
							}
						}
					}
					
					
				}
				catch (Exception e) {//Something went wrong.
					
				}
			}
			
		}
		

	}
	
	
	/**
	 * Inputs a villager (ideally, a fisherman) and returns a RANDOM boat type to buy.
	 */
	public static Item returnRandomBoatTypeForVillager(EntityVillager villager)
	{
		Item[] boats = new Item[]{
				Items.ACACIA_BOAT,
				Items.BIRCH_BOAT,
				Items.DARK_OAK_BOAT,
				Items.JUNGLE_BOAT,
				Items.BOAT,
				Items.SPRUCE_BOAT
		};
		
		int[] randomOrder = shuffledIntArray(0, 5, villager.getRNG());
		for (int i=0; i<6 ; i++) {if (boats[randomOrder[i]]!=null) {return boats[randomOrder[i]];}}
		
		// Failsafe, return ordinary boat.
		return Items.BOAT;
	}

	
	/**
	 * Inputs an entity and returns a wood type for that entity based on its location.
	 * This is just in case we need to base type on where that entity is. Useful for
	 * if a villager wants to sell an item made of a particular kind of wood.
	 * 
	 * If you are in a biome with certain specific tags or words in the name, it will
	 * return a specific wood. Otherwise, if you are in a biome with the "forest" tag
	 * it will return "oak." Failing all of that, it returns a random wood type.
	 */
	public static String chooseRandomWoodTypeFromLocation(EntityLiving entity)
	{
		Biome biome = entity.getEntityWorld().getBiome(entity.getPosition());
		
		Set<Type> typeTags = BiomeDictionary.getTypes(biome);
		
		ArrayList<String> boatTypes = new ArrayList<String>();
		
		// Add wood types to pool based on name
		if (biome.getBiomeName().toLowerCase().contains("birch")) {boatTypes.add("birch");}
		if (biome.getBiomeName().toLowerCase().contains("roofed")) {boatTypes.add("darkoak");}
		
		boolean isForest = false;
		
		// Add wood types to pool based on biome tags
		for (BiomeDictionary.Type type : typeTags)
		{
			if (type==BiomeDictionary.Type.CONIFEROUS) {boatTypes.add("spruce"); continue;}
			if (type==BiomeDictionary.Type.JUNGLE) {boatTypes.add("jungle"); continue;}
			if (type==BiomeDictionary.Type.SAVANNA) {boatTypes.add("acacia"); continue;}
			if (type==BiomeDictionary.Type.FOREST) {isForest=true; continue;}
		}
		
		// Now, pick a boat type from the tags available
		if (boatTypes.size() > 0) {return boatTypes.get(entity.getRNG().nextInt(boatTypes.size()));}
		
		// If none of the above applied, and the "isForest" tag is true, return oak.
		if (isForest) {return "oak";}
		
		return (new String[]{"acacia", "birch", "darkoak", "jungle", "oak", "spruce"})[entity.getRNG().nextInt(6)];
	}

	/**
	 * Returns the median value of an int array.
	 * If the returned value is a halfway point, round up or down depending on if the average value is higher or lower than the median.
	 * If it's the same, specify based on roundup parameter.
	 */
	public static int medianIntArray(ArrayList<Integer> array, boolean roundup)
	{
		if (array.size() <=0) return -1;
		
		Collections.sort(array);
		
		//if (GeneralConfig.debugMessages) {LogHelper.info("array: " + array);}
		
		if (array.size() % 2 == 0)
		{
			// Array is even-length. Find average of the middle two values.
			int totalElements = array.size();
			int sumOfMiddleTwo = array.get(totalElements / 2) + array.get(totalElements / 2 - 1);
			
			if (sumOfMiddleTwo%2==0)
			{
				// Average of middle two values is integer
				//LogHelper.info("Median chosen type A: " + sumOfMiddleTwo/2);
				return sumOfMiddleTwo/2;
			}
			else
			{
				// Average of middle two is half-integer.
				// Round this based on whether the average is higher.
				double median = (double)sumOfMiddleTwo/2;
				
				double average = 0;
				for (int i : array) {average += i;}
				average /= array.size();
				
				if (average < median)
				{
					//LogHelper.info("Median chosen type B: " + MathHelper.floor_double(median) );
					return MathHelper.floor(median);
				}
				else if (average > median)
				{
					//LogHelper.info("Median chosen type C: " + MathHelper.ceiling_double_int(median) );
					return MathHelper.ceil(median);
				}
				else
				{
					//LogHelper.info("Median chosen type D: " + (roundup ? MathHelper.ceiling_double_int(median) : MathHelper.floor_double(median)));
					return roundup ? MathHelper.ceil(median) : MathHelper.floor(median);
				}
			}
		}
		else
		{
			// Array is odd-length. Take the middle value.
			//LogHelper.info("Median chosen type E: " + array.get(array.size()/2));
			return array.get(array.size()/2);
		}
	}

	/**
	 * This method inputs three integers and returns a unique long value based on them.
	 * The purpose of this is to be used as Random.setSeed(unique + worldseed) to ensure that
	 * randomized values for e.g. village names are deterministic based on their coordinates
	 * so that they can be regenerated as necessary.
	 */
	public static long getUniqueLongForXYZ(int x, int y, int z)
	{
		// Find out which of x and/or z are negative in order to discriminate "quadrant"
		boolean xIsNegative = x<0; boolean zIsNegative = z<0;
		// set the inputs to non-negative
		x = Math.abs(x); y = Math.abs(y); z = Math.abs(z);
		
		return ((x+y+z)*(x+y+z+1)*(x+y+z+2)/6 + (y+z)*(y+z+1)/2 + y + (zIsNegative? 1:0)) * (xIsNegative? -2:2);
	}
	
	/**
	 * Color metas are the same as dye.
     * Facing metas are in [0, 3]. The order is S-W-N-E
     */
	public static IBlockState getGlazedTerracotaFromMetas(int colorMeta, int facingMeta)
	{
		Block block = ModBlocksVN.WHITE_GLAZED_TERRACOTTA;
		
		switch (colorMeta)
		{
		// Case 0 is handled by default
		case 1: block=ModBlocksVN.ORANGE_GLAZED_TERRACOTTA; break;
		case 2: block=ModBlocksVN.MAGENTA_GLAZED_TERRACOTTA; break;
		case 3: block=ModBlocksVN.LIGHT_BLUE_GLAZED_TERRACOTTA; break;
		case 4: block=ModBlocksVN.YELLOW_GLAZED_TERRACOTTA; break;
		case 5: block=ModBlocksVN.LIME_GLAZED_TERRACOTTA; break;
		case 6: block=ModBlocksVN.PINK_GLAZED_TERRACOTTA; break;
		case 7: block=ModBlocksVN.GRAY_GLAZED_TERRACOTTA; break;
		case 8: block=ModBlocksVN.SILVER_GLAZED_TERRACOTTA; break;
		case 9: block=ModBlocksVN.CYAN_GLAZED_TERRACOTTA; break;
		case 10: block=ModBlocksVN.PURPLE_GLAZED_TERRACOTTA; break;
		case 11: block=ModBlocksVN.BLUE_GLAZED_TERRACOTTA; break;
		case 12: block=ModBlocksVN.BROWN_GLAZED_TERRACOTTA; break;
		case 13: block=ModBlocksVN.GREEN_GLAZED_TERRACOTTA; break;
		case 14: block=ModBlocksVN.RED_GLAZED_TERRACOTTA; break;
		case 15: block=ModBlocksVN.BLACK_GLAZED_TERRACOTTA; break;
		}
		
		return block.getStateFromMeta(facingMeta);
	}
}
