package astrotibs.villagenames.utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.ieep.ExtendedVillager;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.name.NameGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

// Added in v3.1
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
			BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(posX, 0, posZ));
			return getVillageTypeFromBiome(biome);
		}
		public static VillageType getVillageTypeFromBiome(WorldChunkManager worldChunkManager, int posX, int posZ)
		{
			BiomeGenBase biome = worldChunkManager.getBiomeGenerator(new BlockPos(posX, 0, posZ));
			return getVillageTypeFromBiome(biome);
		}
		public static VillageType getVillageTypeFromBiome(BiomeGenBase biome)
		{
			BiomeDictionary.Type[] typeTags = BiomeDictionary.getTypesForBiome(biome);
			
			// Ordered by personal priority. The first of these to be fulfilled gets returned
			if (biome.biomeName.toLowerCase().contains("taiga")) {return TAIGA;}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.CONIFEROUS) {return TAIGA;}}
			if (biome.biomeName.toLowerCase().contains("savanna")) {return SAVANNA;}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.SAVANNA) {return SAVANNA;}}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.SNOWY) {return SNOWY;}}
			if (biome.biomeName.toLowerCase().contains("desert")) {return DESERT;}
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
			BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(posX, 0, posZ));
			return getMaterialTemplateForBiome(biome);
		}
		public static MaterialType getMaterialTemplateForBiome(WorldChunkManager worldChunkManager, int posX, int posZ)
		{
			BiomeGenBase biome = worldChunkManager.getBiomeGenerator(new BlockPos(posX, 0, posZ));
			return getMaterialTemplateForBiome(biome);
		}
		public static MaterialType getMaterialTemplateForBiome(BiomeGenBase biome)
		{
			if (biome.biomeName.toLowerCase().contains("birch")) {return BIRCH;}
			if (biome.biomeName.toLowerCase().contains("roofed forest")) {return DARK_OAK;}
			
			BiomeDictionary.Type[] typeTags = BiomeDictionary.getTypesForBiome(biome);
			if (biome.biomeName.toLowerCase().contains("taiga")) {return SPRUCE;}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.CONIFEROUS) {return SPRUCE;}}
			for (BiomeDictionary.Type type : typeTags) {if (type==BiomeDictionary.Type.JUNGLE) {return JUNGLE;}}
			if (biome.biomeName.toLowerCase().contains("savanna")) {return ACACIA;}
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
		BiomeGenBase entityBiome = entity.worldObj.getBiomeGenForCoords(entity.getPosition());
		return biomeToSkinType(entityBiome);
	}
	
	/**
	 * Inputs a biome and returns the skin type it translates to
	 */
	public static int biomeToSkinType(BiomeGenBase biome)
	{
		// Get a list of tags for this biome
		BiomeDictionary.Type[] typeTags = BiomeDictionary.getTypesForBiome(biome);
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
    
	
    /**
     * Check if the given entity is a original Zombie (normal, baby or villager), 
     * and not a inherited class (like Zombie Pigman).
     * Adapted from Villager Tweaks by sidben:
     * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/helper/GenericHelper.java
     */
    public static boolean isVanillaZombie(Entity entity) {
    	
    	if (entity == null) return false;
    	else if (entity instanceof EntityPigZombie) return false;
    	//else if (entity.getClass().toString().contains("EntityZombieVillager")) return true; // Added in to allow Et Futurum backported villagers
    	else return entity instanceof EntityZombie;
    }
    
    // Added in v3.1trades
    
    /**
     * Inputs a villager and checks its trades, modifying the "current level" of trades if necessary.
     * @param villager
     */
    public static void modernizeVillagerTrades(EntityVillager villager)
    {
    	Random random = villager.worldObj.rand;
    	ExtendedVillager ev = ExtendedVillager.get(villager);
    	final int professionLevel = ev.getProfessionLevel();
    	MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );

    	if (buyingList==null) {return;} // To prevent crashes I guess?
    	
    	MerchantRecipe merchantrecipe;
		Item moditem; // Used as a placeholder to attempt to add modded trades to villagers
		ArrayList<MerchantRecipe> merchantRecipeArray; // Used when a random item is needed to fill a slot
		int enchantvalue; // Used as a holder for enchantment levels to help tweak enchanted item prices
		Enchantment enchantment;
		int color1; // Used when one OR two colorized items appear
		ItemStack itemStackColorizable; // Holder for itemstacks that can be dyed
		
		// Increment the VN profession level just to make sure you don't keep searching.
		ev.setProfessionLevelVN(ev.getProfessionLevelVN()+1);
		
    	switch (ev.getProfession())
    	{
    		case 0: // Farmer type: summon Villager ~ ~ ~ {Profession:0}
    			
    			switch (ev.getCareerVN())
    			{
    				case 1: // Farmer
    					
    					switch (ev.getProfessionLevelVN())
    					{
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.wheat, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.potato, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.carrot, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.bread)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.wheat, 20 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.potato, 26 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.carrot, 22 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
	    								while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.beetrootItemSB);
	    									if (moditem != null) {merchantRecipeArray.add(new MerchantRecipe( new ItemStack( moditem, 15 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) ); break;}
	    									break;}
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.bread, 6 ), 0, 5
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
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.pumpkin), Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.pumpkin_pie)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.pumpkin), 6 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.pumpkin_pie, 4 ), 0, 5) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.apple, 4 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
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
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.melon_block), Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.apple)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 5
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.melon_block), 4 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5 // BE version; the JE version uses whole melons
	    										));
	    								
	    								// Slot 6
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack( Items.cookie, 18 ), 0, 4 // BE version; the JE version uses whole melons
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
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.cookie)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.cake)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 7
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.egg, 16 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5 // BE version; the JE version uses whole melons
	    										));
	    								
	    								// Slot 8
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cake, 1 ), 0, 5 // BE version; the JE version uses whole melons
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
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.golden_carrot)
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.speckled_melon)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 9
	    						merchantRecipeArray = new ArrayList<MerchantRecipe>();
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack( Items.golden_carrot, 3 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 4 ), (ItemStack)null, new ItemStack( Items.speckled_melon, 3 ), 0, 5) );
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
    					}
    					break;
    				
    				case 2: // Fisherman
    					
    					switch (ev.getProfessionLevelVN())
    					{
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.string, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.coal, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.fish, Items.emerald, Items.cooked_fish)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.string, 20 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.coal, 10 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								// TODO - Emerald to Bucket of Cod
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), new ItemStack( Items.fish, 6, 0 ), new ItemStack( Items.cooked_fish, 6, 0 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
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
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.fishing_rod)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.fish, 15, 0 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								// TODO - Campfire
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), new ItemStack( Items.fish, 6, 1 ), new ItemStack( Items.cooked_fish, 6, 1 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
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
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.fish, 1, 1), new ItemStack(Items.emerald, 1))
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.fishing_rod)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 5
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.fish, 13, 1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
	    						
	    						// Slot 6: Enchanted Fishing Rod
								enchantvalue = 5 + random.nextInt(15);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.fishing_rod, 1), enchantvalue ), 0, 2 ) );
								
								break;
	    						
	    					case 4: // Expert
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.fish, 1, 2), new ItemStack(Items.emerald, 1))
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.boat, Items.emerald)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 7
	    						// TODO - multiple boat types
								merchantRecipeArray = new ArrayList<MerchantRecipe>();
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.fish, 6, 2 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( FunctionsVN.returnRandomBoatTypeForVillager(villager), 1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5) );
								buyingList.add(merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
	    						
	    					case 5: // Master
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.fish, 1, 3), new ItemStack(Items.emerald, 1))
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 8
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.fish, 4, 3 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
								
								break;
    					}
    					break;
    				
    				case 3: // Shepherd
    					
    					switch (ev.getProfessionLevelVN())
    					{
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.wool), Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.shears)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.wool), 18, new int[]{0,12,15,7}[random.nextInt(4)] ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6
	    										));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.emerald, 2 ), (ItemStack)null, new ItemStack( Items.shears, 1 ), 0, 5
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
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(Blocks.wool))
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
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.dye, 12, 0 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) );
	    									break;}
									}
									else {
										while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeWhiteBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.dye, 12, 15 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) );
	    									break;}
									}
								}
								else {
									buyingList.add(new MerchantRecipe(
    										new ItemStack( Items.dye, 12, new int[]{8,10,12}[random.nextInt(3)] ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
    										));
								}
								
																
								// Slot 4
								color1 = random.nextInt(16);
								if (random.nextBoolean())
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, color1), 0, 5
    										));
									if (random.nextBoolean())
    								{
										buyingList.add(new MerchantRecipe(
    											new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, (color1+random.nextInt(15)+1)%16), 0, 5
	    										));
    								}
								}
								else
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.carpet), 4, color1), 0, 5
    										));
									if (random.nextBoolean())
    								{
										buyingList.add(new MerchantRecipe(
    											new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.carpet), 4, (color1+random.nextInt(15)+1)%16), 0, 5
	    										));
    								}
								}
								
	    						break;
	    						
	    					case 3: // Journeyman
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 1), new ItemStack(Items.emerald)) // Red dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 7), new ItemStack(Items.emerald)) // Light Gray dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 9), new ItemStack(Items.emerald)) // Pink dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 11), new ItemStack(Items.emerald)) // Yellow dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 14), new ItemStack(Items.emerald)) // Orange dye
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.bed)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 5
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.dye, 12, new int[]{1,7,9,11,14}[random.nextInt(5)] ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
	    						
								// Slot 6
								buyingList.add(new MerchantRecipe( // TODO - colorized bed
										new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack( Items.bed, 1 ), 0, 5
										));
								
								break;
	    						
	    					case 4: // Expert
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 2), new ItemStack(Items.emerald)) // Green dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 3), new ItemStack(Items.emerald)) // Brown dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 4), new ItemStack(Items.emerald)) // Blue dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 5), new ItemStack(Items.emerald)) // Purple dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 6), new ItemStack(Items.emerald)) // Cyan dye
	    									|| hasSameItemsAndMeta(buyingList.get(Math.max(0, i)), new ItemStack(Items.dye, 1, 13), new ItemStack(Items.emerald)) // Magenta dye
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.banner)
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
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.dye, 12, 2 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    								
	    							case 1: // Special handler for brown dye
	    								while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeBrownBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.dye, 12, 3 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    								
	    							case 2: // Special handler for blue dye
	    								while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlueBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.dye, 12, 4 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    							}
	    							
	    						}
	    						else {
	    							buyingList.add(new MerchantRecipe(
											new ItemStack( Items.dye, 12, new int[]{5,6,13}[random.nextInt(3)] ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
											));
	    						}
								
								// Slot 8
								color1 = random.nextInt(16);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack(Items.banner, 1, color1), 0, 5
										));
								if (random.nextBoolean())
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack(Items.banner, 1, (color1+random.nextInt(15)+1)%16), 0, 5
    										));
								}
								
								break;
	    						
	    					case 5: // Master
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.painting)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.emerald, 2 ), (ItemStack)null, new ItemStack( Items.painting, 3 ), 0, 5
										));
								
								break;
    					}
    					break;
    					
    				case 4: // Fletcher
    					
    					switch (ev.getProfessionLevelVN())
    					{
	    					case 1: // Novice
	    						
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.string, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.arrow)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.stick, 32 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6
	    										));
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.arrow, 16 ), 0, 5) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.gravel), 10 ), new ItemStack( Items.flint, 10 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
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
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.bow)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.gravel), Items.emerald, Items.flint)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.flint, 26 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.emerald, 2 ), (ItemStack)null, new ItemStack( Items.bow, 1 ), 0, 5
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
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.string, Items.emerald)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 5
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.string, 14 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
	    						
								// Slot 6
								// TODO - Crossbow
								
								break;
	    						
	    					case 4: // Expert
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.feather, Items.emerald)
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.bow) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 7
	    						buyingList.add(new MerchantRecipe(
										new ItemStack( Items.feather, 24 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
								
								// Slot 8
	    						enchantvalue = 5 + random.nextInt(15);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.bow, 1), enchantvalue ), 0, 2 ) );
								
								break;
	    						
	    					case 5: // Master
	    						
	    						// There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Item.getItemFromBlock(Blocks.tripwire_hook), Items.emerald)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								buyingList.add(new MerchantRecipe(
										new ItemStack( Item.getItemFromBlock(Blocks.tripwire_hook), 8 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
								
								// TODO - Slot 10 would be either a Crossbow or a Tipped Arrow 
								
								break;
    					}
    					break;
    					
    			}
				break;
    			
    		case 1: // Librarian type: summon Villager ~ ~ ~ {Profession:1}
    			
    			// Cartographer doesn't exist in 1.8. Weed out all Librarian trades, and then re-seed the trading list depending on whether it's a cartographer.
    			switch (ev.getProfessionLevelVN())
				{
    				case 1: // Novice
    					
						// Go forward through list and identify when you get the vanilla trades
						for (int i=0; i<buyingList.size(); i++)
						{
							
    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
							if (
									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.paper, Items.emerald)
									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.book, Items.emerald, Items.enchanted_book)
									) { // Trade block detected!
								
								// Add a new block with one or more trades
								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
								
								// Slot 1 - common to both Librarian and Cartographer
								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
										new ItemStack( Items.paper, 24 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, ev.getCareerVN()==2 ? 6 : 4
										));
								
								if (ev.getCareerVN()==1) { // Librarian
									
									// Slot 2
									merchantRecipeArray = new ArrayList<MerchantRecipe>();
									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 6 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.bookshelf), 3 ), 0, 5) );
									
									enchantment = Enchantment.enchantmentsBookList[villager.worldObj.rand.nextInt(Enchantment.enchantmentsBookList.length)];
									enchantvalue = MathHelper.getRandomIntegerInRange(villager.worldObj.rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
									merchantRecipeArray.add(
											modernEnchantedBookTrade(random, 2)
											);
									
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
									
								}
								else if (ev.getCareerVN()==2) { // Cartographer

									// Slot 2
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
											new ItemStack( Items.emerald, 7 ), (ItemStack)null, new ItemStack( Items.map, 1 ), 0, 5
											));
									
								}
								
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
    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
							if (
									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.book, Items.emerald)
									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.compass)
									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Item.getItemFromBlock(Blocks.bookshelf))
									) { // Trade block detected!
								
								
								
								// Add a new block with one or more trades
								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other

								if (ev.getCareerVN()==1) { // Librarian

									// Slot 3
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
											new ItemStack( Items.book, 4 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
											));
									
									// Slot 4
									merchantRecipeArray = new ArrayList<MerchantRecipe>();
									//TODO - Lantern
									enchantment = Enchantment.enchantmentsBookList[villager.worldObj.rand.nextInt(Enchantment.enchantmentsBookList.length)];
									enchantvalue = MathHelper.getRandomIntegerInRange(villager.worldObj.rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
									
								}
								else if (ev.getCareerVN()==2) { // Cartographer

									// Slot 3
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
											new ItemStack( Item.getItemFromBlock(Blocks.glass_pane), 11 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
											));
									
									// Slot 4 TODO - Ocean Explorer Map
								}
								
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
    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
							if (
									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.written_book, Items.emerald)
									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.clock)
									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Item.getItemFromBlock(Blocks.glass))
									) { // Trade block detected!
								
								// Add a new block with one or more trades
								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other

								if (ev.getCareerVN()==1) { // Librarian

									// Slot 5
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
											new ItemStack( Items.dye, 5, 0 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
											));
									
									// Slot 6
									merchantRecipeArray = new ArrayList<MerchantRecipe>();
									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.glass), 4 ), 0, 5) );
									enchantment = Enchantment.enchantmentsBookList[villager.worldObj.rand.nextInt(Enchantment.enchantmentsBookList.length)];
									enchantvalue = MathHelper.getRandomIntegerInRange(villager.worldObj.rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
									
								}
								else if (ev.getCareerVN()==2) { // Cartographer
									
									// Slot 5
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
											new ItemStack( Items.compass, 1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
											));
									
									// Slot 6 TODO - Woodland Explorer Map
								}
								
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
    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
							if (
									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.book, Items.emerald, Items.enchanted_book)
									) { // Trade block detected!
								
								// Add a new block with one or more trades
								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other

								if (ev.getCareerVN()==1) { // Librarian

									// Slot 7
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
											new ItemStack( Items.writable_book, 1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 2
											));
									
									// Slot 8
									merchantRecipeArray = new ArrayList<MerchantRecipe>();
									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 4 ), (ItemStack)null, new ItemStack( Items.compass, 1 ), 0, 5) );
									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 5 ), (ItemStack)null, new ItemStack( Items.clock, 1 ), 0, 5) );
									enchantment = Enchantment.enchantmentsBookList[villager.worldObj.rand.nextInt(Enchantment.enchantmentsBookList.length)];
									enchantvalue = MathHelper.getRandomIntegerInRange(villager.worldObj.rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
									
								}
								else if (ev.getCareerVN()==2) { // Cartographer
									
									// Slot 7
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
											new ItemStack( Items.emerald, 7 ), (ItemStack)null, new ItemStack( Items.item_frame, 1 ), 0, 5
											));
									
									// Slot 8
									color1 = random.nextInt(16);
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack(Items.banner, 1, color1), 0, 5
											));
									if (random.nextBoolean())
									{
										buyingList.add(new MerchantRecipe(
												new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack(Items.banner, 1, (color1+random.nextInt(15)+1)%16), 0, 5
	    										));
									}
									
								}
								
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
    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
							if (
									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.book, Items.emerald, Items.enchanted_book)
									) { // Trade block detected!
								
								// Add a new block with one or more trades
								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other

								if (ev.getCareerVN()==1) { // Librarian

									// Slot 9
									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
											new ItemStack( Items.emerald, 20 ), (ItemStack)null, new ItemStack( Items.name_tag, 1 ), 0, 5
											));
									
								}
								else if (ev.getCareerVN()==2) { // Cartographer
									
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
		    									new ItemStack( Items.emerald, 2 ), (ItemStack)null, BannerGenerator.makeBanner(bannerNBT), 0, 5
		    									));
		    						}
		    						else // No banner was found or is available. INSTEAD, sell a new banner with a random design.
		    						{
		    							Object[] newRandomBanner = BannerGenerator.randomBannerArrays(villager.worldObj.rand, -1, -1);
		    							ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
		    							ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
		    																	
		    							buyingList.add(new MerchantRecipe(
		    									new ItemStack( Items.emerald, 3 ), (ItemStack)null, BannerGenerator.makeBanner(patternArray, colorArray), 0, 5
		    									));
		    						}
									
								}
								
								// Erase the old block...
								eraseTrades(buyingList, addToSlot, cbs);
								
								// We don't need to keep searching.
								break;
							}
						}
    					break;
    					
    				case 6: // 1.8+ has an enchanted book in level six. Remove it.
    					
    					// Go BACKWARD through list and yeet the first enchanted book
						for (int i=buyingList.size()-1; i>=0; i--)
						{
							int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
							if (
									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.name_tag)
									) { // Trade block detected!
								
								// Erase the old block...
								eraseTrades(buyingList, i, cbs);
								
								// Add treasure trade
								if (GeneralConfig.treasureTrades) {addTreasureTrade(buyingList, villager, ev.getCareerVN(), random);}
								
								// We don't need to keep searching.
								break;
							}
						}
    					break;
    					
    				case 7:
    					// Add treasure trade
						if (GeneralConfig.treasureTrades) {addTreasureTrade(buyingList, villager, ev.getCareerVN(), random);}
						break;
						
    				case 8:
    					// Add treasure trade
						if (GeneralConfig.treasureTrades) {addTreasureTrade(buyingList, villager, ev.getCareerVN(), random);} // Removed career condition in v3.1banner because a Master can sell a village banner.
						break;
				}
				break;
				
    		case 2: // Priest type: summon Villager ~ ~ ~ {Profession:2}
    			
    			switch (ev.getCareerVN())
    			{
    				case 1: // Cleric
    					
		    			switch (ev.getProfessionLevelVN())
						{
		    				case 1: // Novice
		    					
		    					// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									
		    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.rotten_flesh, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.gold_ingot, Items.emerald)
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 1
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.rotten_flesh, 32 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6
												));
										
										// Slot 2
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.redstone, 4 ), 0, 5
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
									
		    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.redstone)
											&& hasSameItemsAndMeta(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), new ItemStack(Items.emerald), new ItemStack(Items.dye, 1, 4))
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 3
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.gold_ingot, 3 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
												));
										
										// Slot 4
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.dye, 1, 4 ), 0, 5
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
									
		    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.ender_eye)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Item.getItemFromBlock(Blocks.glowstone))
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 5
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.rabbit_foot, 2 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
												));
										
										// Slot 6
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.emerald, 4 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.glowstone), 1 ), 0, 5
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
									
		    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.experience_bottle)
											) { // Trade block detected!
										
										// Add a new block with one or more trades
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 7
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								// TODO - Scute
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.glass_bottle, 9 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
										// Slot 8
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.emerald, 5 ), (ItemStack)null, new ItemStack( Items.ender_pearl, 1 ), 0, 5
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
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.nether_wart, Items.emerald)
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.experience_bottle)
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.nether_wart, 22 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
								
								// Slot 10
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.emerald, 3), (ItemStack)null, new ItemStack( Items.experience_bottle, 1 ), 0, 5
										));
								
								break;
						}
    					break;
    			}
				break;
				
    		case 3: // Blacksmith type: summon Villager ~ ~ ~ {Profession:3}
    			
    			// I'm going to handle the Blacksmith class differently.
    			// I'll switch by profession level, and then by ORDINARY career, using a CareerVN if condition for when it's a Mason 
    			
    			int cbs_blacksmith = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
				int addToSlot_blacksmith = 0; // This makes sure that subsequent trades get added rightward of each other
				
    			switch (ev.getProfessionLevelVN())
				{
    				case 1: // Novice
    					
    					switch (ev.getCareer()) // Search and remove stuff based on vanilla career
    	    			{
	    					case 1: // Armorer
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.coal, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.iron_helmet)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											// Slot 1
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.coal, 15 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6
													));
											
											// Slot 2
											merchantRecipeArray = new ArrayList<MerchantRecipe>();
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 5 ), (ItemStack)null, new ItemStack( Items.iron_helmet, 1 ), 0, 3) );
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 9 ), (ItemStack)null, new ItemStack( Items.iron_chestplate, 1 ), 0, 3) );
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 7 ), (ItemStack)null, new ItemStack( Items.iron_leggings, 1 ), 0, 3) );
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 4 ), (ItemStack)null, new ItemStack( Items.iron_boots, 1 ), 0, 3) );
		    								buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
	    						
	    					case 2: // Weaponsmith
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.coal, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.iron_axe)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											
											// Slot 1
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.coal, 15 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6
													));
											
											// Slot 2
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack( Items.iron_axe, 1 ), 0, 3
													));
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
	    						
	    					case 3: // Toolsmith
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.coal, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.iron_shovel)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											
											// Slot 1
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.coal, 15 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6
													));
											
											// Slot 2
											merchantRecipeArray = new ArrayList<MerchantRecipe>();
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.stone_axe, 1 ), 0, 3) );
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.stone_pickaxe, 1 ), 0, 3) );
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.stone_shovel, 1 ), 0, 3) );
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.stone_hoe, 1 ), 0, 3) );
		    								buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    	    			}
    					
    					// If this was a Mason all along, add a new block with one or more trades
    					if (ev.getCareerVN()==4)
						{
							// Slot 1
							buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
									new ItemStack( Items.clay_ball, 10 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6
									));
							
							// Slot 2
							buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
									new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.brick, 10 ), 0, 5
									));
						}
    					
						// Erase the old block...
						eraseTrades(buyingList, addToSlot_blacksmith, cbs_blacksmith);
						
    					break;
    					
    				case 2: // Apprentice
    					
    					switch (ev.getCareer()) // Search and remove stuff based on vanilla career
    	    			{
	    					case 1: // Armorer
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.iron_ingot, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.iron_chestplate)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											
											// Slot 3
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.iron_ingot, 4 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
													));
											
											// Slot 4
											merchantRecipeArray = new ArrayList<MerchantRecipe>();
											// TODO - Bell
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.chainmail_boots, 1 ), 0, 3) );
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 3 ), (ItemStack)null, new ItemStack( Items.chainmail_leggings, 1 ), 0, 3) );
		    								buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
	    						
	    					case 2: // Weaponsmith
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.iron_ingot, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.iron_sword)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											
											// Slot 3
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.iron_ingot, 4 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
													));
											
											// Slot 4
											enchantvalue = 5 + random.nextInt(15);
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.emerald, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
													EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.iron_sword, 1), enchantvalue ), 0, 2
													));
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
	    						
	    					case 3: // Toolsmith
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.iron_ingot, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.iron_pickaxe)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											
											// Slot 3
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.iron_ingot, 4 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
													));
											
											// Slot 4
											// TODO - Bell
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    	    			}
    					
    					// If this was a Mason all along, add a new block with one or more trades
    					if (ev.getCareerVN()==4)
						{
							// Slot 3
							buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
									new ItemStack( Item.getItemFromBlock(Blocks.stone), 20, 0 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
									));
							
							// Slot 4
							buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
									new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.stonebrick), 4, 3 ), 0, 5
									));
						}
    					
						// Erase the old block...
						eraseTrades(buyingList, addToSlot_blacksmith, cbs_blacksmith);
						
    					break;
    				
    				
    				case 3: // Journeyman
    					
    					switch (ev.getCareer()) // Search and remove stuff based on vanilla career
    	    			{
	    					case 1: // Armorer
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.diamond, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.diamond_chestplate)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											
											// Slot 5
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.lava_bucket, 1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
													));
											
											// Slot 6
											merchantRecipeArray = new ArrayList<MerchantRecipe>();
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.chainmail_helmet, 1 ), 0, 3) );
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 4 ), (ItemStack)null, new ItemStack( Items.chainmail_chestplate, 1 ), 0, 3) );
		    								// TODO - Shield
		    								buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
	    						
	    					case 2: // Weaponsmith
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.diamond, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.diamond_sword)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.diamond_axe)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											
											// Slot 5
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.flint, 24 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
													));
											
											// Slot 6
											// Bell
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
	    						
	    					case 3: // Toolsmith
	    						
	    						// Go forward through list and identify when you get the vanilla trades
								for (int i=0; i<buyingList.size(); i++)
								{
									cbs_blacksmith = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.diamond, Items.emerald)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.diamond_pickaxe)
											) { // Trade block detected!
										
										addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Add a new block with one or more trades IF this is not a Mason
										if (ev.getCareerVN()!=4)
										{
											
											// Slot 5
											buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
													new ItemStack( Items.flint, 30 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
													));
											
											// Slot 6
											merchantRecipeArray = new ArrayList<MerchantRecipe>();
											enchantvalue = 5 + random.nextInt(15);
											merchantRecipeArray.add(  new MerchantRecipe(
													new ItemStack( Items.emerald, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
													EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.iron_axe, 1), enchantvalue ), 0, 2
													));
											merchantRecipeArray.add(  new MerchantRecipe(
													new ItemStack( Items.emerald, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
													EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.iron_shovel, 1), enchantvalue ), 0, 2
													));
											merchantRecipeArray.add(  new MerchantRecipe(
													new ItemStack( Items.emerald, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
													EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.iron_pickaxe, 1), enchantvalue ), 0, 2
													));
		    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 4 ), (ItemStack)null, new ItemStack( Items.diamond_hoe, 1 ), 0, 2) );
		    								buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
										}
										
										// We don't need to keep searching.
										break;
									}
								}
								break;
    	    			}
    					
    					// If this was a Mason all along, add a new block with one or more trades
    					if (ev.getCareerVN()==4)
						{
							// Slot 5
							buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
									new ItemStack( Item.getItemFromBlock(Blocks.stone), 16, random.nextInt(3)*2 +1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
									));
							
							// Slot 6
							buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
									new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.stone), 4, random.nextInt(3)*2 +2 ), 0, 5
									));
						}
    					
						// Erase the old block...
						eraseTrades(buyingList, addToSlot_blacksmith, cbs_blacksmith);
						
    					break;
    				
    				
    				case 4: // Expert
    					
    					if (ev.getCareer()==1) // Remove trades added in when vanilla career is 1
    					{
    						// Go forward through list and identify when you get the vanilla trades
							for (int i=0; i<buyingList.size(); i++)
							{
								cbs_blacksmith = 0;
								if (
										hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.chainmail_boots)
										&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.chainmail_leggings)
										&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.chainmail_helmet)
										&& hasSameItems(buyingList.get(Math.min(i+(cbs_blacksmith++), buyingList.size()-1)), Items.emerald, Items.chainmail_chestplate)
										) { // Trade block detected!
									
									addToSlot_blacksmith = i; // This makes sure that subsequent trades get added rightward of each other
									
									// Add a new block with one or more trades IF this is not a Mason
									if (ev.getCareerVN()!=4)
									{
										// Slot 7
										buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.diamond, 1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
												));
										
										// Slot 8
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
										enchantvalue = 5 + random.nextInt(15);
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.emerald, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_leggings, 1), enchantvalue ), 0, 2
												));
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.emerald, enchantvalue+(6+random.nextInt(3)) ), (ItemStack)null,
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_boots, 1), enchantvalue ), 0, 2
												));
	    								buyingList.add(MathHelper.clamp_int(addToSlot_blacksmith++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								eraseTrades(buyingList, addToSlot_blacksmith, cbs_blacksmith);
									}
									
									// We don't need to keep searching.
									break;
								}
							}
    					}
    					
    					switch (ev.getCareerVN()) // There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
    					{
	    					case 2: // Weaponsmith
	    						
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							cbs_blacksmith = 0;
									if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.diamond, Items.emerald)		
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.diamond_axe) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 7
								buyingList.add( new MerchantRecipe(
										new ItemStack( Items.diamond, 1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
	    						
	    						// Slot 8
								enchantvalue = 5 + random.nextInt(15);
								buyingList.add( new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_axe, 1), enchantvalue ), 0, 2
										));
								
								break;
	    						
	    					case 3: // Toolsmith
	    						
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							cbs_blacksmith = 0;
									if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.diamond, Items.emerald)		
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.diamond_axe) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.diamond_shovel) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 7
								buyingList.add( new MerchantRecipe(
										new ItemStack( Items.diamond, 1 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
	    						
	    						// Slot 8
								merchantRecipeArray = new ArrayList<MerchantRecipe>();
								enchantvalue = 5 + random.nextInt(15);
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_axe, 1), enchantvalue ), 0, 2
										));
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(6+random.nextInt(2)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_shovel, 1), enchantvalue ), 0, 2
										));
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
	    						
	    					case 4: // Mason

	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							cbs_blacksmith = 0;
									if (
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.quartz, Items.emerald)		
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(Blocks.stained_hardened_clay))		
	    									
	    									|| ((GeneralConfig.addConcrete) &&
		    									( hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaWhite))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaOrange))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaMagenta))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaLightBlue))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaYellow))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaLime))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaPink))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaGray))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaSilver))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaCyan))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaPurple))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaBlue))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaBrown))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaGreen))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaRed))
			                        			|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(ModBlocksVN.blockGlazedTerracottaBlack))
	    											))
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 7
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.quartz, 12 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
										));
	    						
	    						// Slot 8
								color1 = random.nextInt(16);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.stained_hardened_clay), 1, color1), 0, 5
										));
								if (random.nextBoolean())
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.stained_hardened_clay), 1, (color1+random.nextInt(15)+1)%16), 0, 5
    										));
								}
								if (GeneralConfig.addConcrete)
								{
									color1 = random.nextInt(16);
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(
													new Block[]{
															ModBlocksVN.blockGlazedTerracottaWhite,
						                        			ModBlocksVN.blockGlazedTerracottaOrange,
						                        			ModBlocksVN.blockGlazedTerracottaMagenta,
						                        			ModBlocksVN.blockGlazedTerracottaLightBlue,
						                        			ModBlocksVN.blockGlazedTerracottaYellow,
						                        			ModBlocksVN.blockGlazedTerracottaLime,
						                        			ModBlocksVN.blockGlazedTerracottaPink,
						                        			ModBlocksVN.blockGlazedTerracottaGray,
						                        			ModBlocksVN.blockGlazedTerracottaSilver,
						                        			ModBlocksVN.blockGlazedTerracottaCyan,
						                        			ModBlocksVN.blockGlazedTerracottaPurple,
						                        			ModBlocksVN.blockGlazedTerracottaBlue,
						                        			ModBlocksVN.blockGlazedTerracottaBrown,
						                        			ModBlocksVN.blockGlazedTerracottaGreen,
						                        			ModBlocksVN.blockGlazedTerracottaRed,
						                        			ModBlocksVN.blockGlazedTerracottaBlack
															}[color1]
													), 1), 0, 5
											));
									if (random.nextBoolean())
									{
										buyingList.add(new MerchantRecipe(
												new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(
														new Block[]{
																ModBlocksVN.blockGlazedTerracottaWhite,
							                        			ModBlocksVN.blockGlazedTerracottaOrange,
							                        			ModBlocksVN.blockGlazedTerracottaMagenta,
							                        			ModBlocksVN.blockGlazedTerracottaLightBlue,
							                        			ModBlocksVN.blockGlazedTerracottaYellow,
							                        			ModBlocksVN.blockGlazedTerracottaLime,
							                        			ModBlocksVN.blockGlazedTerracottaPink,
							                        			ModBlocksVN.blockGlazedTerracottaGray,
							                        			ModBlocksVN.blockGlazedTerracottaSilver,
							                        			ModBlocksVN.blockGlazedTerracottaCyan,
							                        			ModBlocksVN.blockGlazedTerracottaPurple,
							                        			ModBlocksVN.blockGlazedTerracottaBlue,
							                        			ModBlocksVN.blockGlazedTerracottaBrown,
							                        			ModBlocksVN.blockGlazedTerracottaGreen,
							                        			ModBlocksVN.blockGlazedTerracottaRed,
							                        			ModBlocksVN.blockGlazedTerracottaBlack
																}[(color1+random.nextInt(15)+1)%16]
														), 1), 0, 5
	    										));
									}
								}
								
								break;
	    						
    					}
    					
    					break;
    				
    				
    				case 5: // Master
    					
    					switch (ev.getCareerVN()) // There is no equivalent level in 1.8. Go BACKWARDS and remove EVERY pertinent trade before adding the valid ones at the end.
    					{
    						case 1: // Armorer
    							
    							for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							cbs_blacksmith = 0;
									if (
	    									(hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.diamond_helmet) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted() )		
	    									|| (hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.diamond_chestplate) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted() )
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
    							merchantRecipeArray = new ArrayList<MerchantRecipe>();
								enchantvalue = 5 + random.nextInt(15);
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(6+random.nextInt(3)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_helmet, 1), enchantvalue ), 0, 2
										));
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_chestplate, 1), enchantvalue ), 0, 2
										));
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								break;
    							
	    					case 2: // Weaponsmith
	    						
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							cbs_blacksmith = 0;
									if (
	    									(hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.diamond_sword) && buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								enchantvalue = 5 + random.nextInt(15);
								buyingList.add(  new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(6+random.nextInt(3)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_sword, 1), enchantvalue ), 0, 2
										));
								
								break;
	    						
	    					case 3: // Toolsmith
	    						
	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							cbs_blacksmith = 0;
									if (
	    									(hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.diamond_pickaxe)	&& buyingList.get(Math.max(0, i)).getItemToSell().isItemEnchanted())
	    									) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
	    						enchantvalue = 5 + random.nextInt(15);
								buyingList.add(  new MerchantRecipe(
										new ItemStack( Items.emerald, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.diamond_pickaxe, 1), enchantvalue ), 0, 2
										));
								
								break;
	    						
	    					case 4: // Mason

	    						for (int i=buyingList.size()-1; i>=0; i--)
	    						{
	    							cbs_blacksmith = 0;
									if (hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Item.getItemFromBlock(Blocks.quartz_block))) {
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Slot 9
								buyingList.add( new MerchantRecipe(
										new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.quartz_block), 1, random.nextBoolean() ? 2 : 0 ), 0, 5
										));
	    						
								break;
	    						
    					}
    					
    					break;
    					
				}
				break;
				
    		case 4: // Butcher type: summon Villager ~ ~ ~ {Profession:4}
    			
    			switch (ev.getCareerVN())
    			{
	    			
					case 1: // Butcher
						
		    			switch (ev.getProfessionLevelVN())
						{
		    				case 1: // Novice
		    					
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.porkchop, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.chicken, Items.emerald)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 1
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.chicken, 14 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.rabbit, 4 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.porkchop, 7 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.rabbit_stew, 1 ), 0, 5
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
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.coal, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.cooked_porkchop)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.cooked_chicken)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other

	    								// Slot 3
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.coal, 15 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_rabbit, 5 ), 0, 5) ); // BE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_chicken, 8 ), 0, 5) ); // JE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_porkchop, 5 ), 0, 5) ); // JE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_mutton, 8 ), 0, 5) ); // VN price
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
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
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.beef, Items.emerald)
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.mutton, Items.emerald)
	    									|| hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.cooked_beef)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 5
	    						merchantRecipeArray = new ArrayList<MerchantRecipe>();
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.beef, 10 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.mutton, 8 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5) ); // BE price
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								// Slot 6
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_beef, 5 ), 0, 5 // VN price
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
											new ItemStack( moditem, 64 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
											));
										return;
									}
									break;
								}
		    					
		    					
		    				case 5: // Master
		    					// TODO - Sweet Berries
		    					
		    					// INSTEAD, for levels 4 and 5, add a trade that was not previously added--and do so at an increased value to the player:
		    			    	ArrayList<MerchantRecipe> possibleHighButcherTrades = new ArrayList<MerchantRecipe>();
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.chicken, Math.max(14 - ev.getProfessionLevelVN() + 1, 1) ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.rabbit, Math.max(4 - ev.getProfessionLevelVN() + 1, 1) ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.porkchop, Math.max(7 - ev.getProfessionLevelVN() + 1, 1) ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_rabbit, Math.min(5 + ev.getProfessionLevelVN() - 2, 64) ), 0, 5) ); // BE price
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_chicken, Math.min(8 + ev.getProfessionLevelVN() - 2, 64) ), 0, 5) ); // JE price
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_porkchop, Math.min(5 + ev.getProfessionLevelVN() - 2, 64) ), 0, 5) ); // JE price
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.emerald, 1 ), (ItemStack)null, new ItemStack( Items.cooked_mutton, Math.min(8 + ev.getProfessionLevelVN() - 2, 64) ), 0, 5) ); // VN price
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.beef, Math.max(10 - ev.getProfessionLevelVN() + 3, 1) ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5) );
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.mutton, Math.max(8 - ev.getProfessionLevelVN() + 3, 1) ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5) ); // BE price
								
		    					while (true)
		    					{
		    						MerchantRecipe chosenRecipe = possibleHighButcherTrades.get(random.nextInt(possibleHighButcherTrades.size()));
		    			        	
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
						
		    			switch (ev.getProfessionLevelVN())
						{
		    				case 1: // Novice
		    					
	    						// Go forward through list and identify when you get the vanilla trades
	    						for (int i=0; i<buyingList.size(); i++)
	    						{
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.leather, Items.emerald)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.leather_leggings)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 1
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.leather, 6 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 6
	    										));
	    								
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								
	    								itemStackColorizable = new ItemStack(Items.leather_leggings);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 3 ), (ItemStack)null, itemStackColorizable, 0, 2) );
	    								
	    								itemStackColorizable = new ItemStack(Items.leather_chestplate);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 7 ), (ItemStack)null, itemStackColorizable, 0, 2) );
	    								
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								
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
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
    	    						
    	    						// Extract an NBT Tag compound just so you can check whether it's been colorized
    	    						//NBTTagCompound nbttagcompound = (buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)).getItemToSell()).getTagCompound();
    	    						//if (nbttagcompound == null) {nbttagcompound = new NBTTagCompound();}
    	    						
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.leather_chestplate) && (buyingList.get(Math.min(i+(cbs-1), buyingList.size()-1)).getItemToSell().isItemEnchanted())
	    									//&& (!nbttagcompound.hasKey("display", 10))
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.flint, 26 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    										));
	    								
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								
	    								itemStackColorizable = new ItemStack(Items.leather_helmet);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 5 ), (ItemStack)null, itemStackColorizable, 0, 2) );
	    								
	    								itemStackColorizable = new ItemStack(Items.leather_boots);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 4 ), (ItemStack)null, itemStackColorizable, 0, 2) );
	    								
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								
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
    	    						int cbs = 0; // Cumulative Block Size. If all the blow trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.emerald, Items.saddle)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 5
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.rabbit_hide, 9 ), (ItemStack)null, new ItemStack( Items.emerald, 1 ), 0, 5
	    										));
	    								
	    								
	    								// Slot 6
	    								itemStackColorizable = new ItemStack(Items.leather_chestplate);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								enchantvalue = 5 + random.nextInt(15);
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.emerald, 7 ), (ItemStack)null,
	    										EnchantmentHelper.addRandomEnchantment(random, itemStackColorizable, enchantvalue ), 0, 2
	    												));
	    								
	    								
	    								// Erase the old block...
	    								eraseTrades(buyingList, addToSlot, cbs);
	    								
	    								// We don't need to keep searching.
	    								break;
	    							}
	    						}
	    						break;
		    					
		    				case 4: // Expert
		    					
		    					// TODO - Scute
		    					// TODO - Leather Horse Armor
		    					
		    					
		    					// INSTEAD, for levels 4 and 5, add a trade that was not previously added--and do so at an increased value to the player:
		    			    	ArrayList<MerchantRecipe> possibleExpertLeatherworkerTrades = new ArrayList<MerchantRecipe>();
		    			    	
								itemStackColorizable = new ItemStack(Items.leather_leggings);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.emerald, Math.max(3 - 2*(ev.getProfessionLevelVN()-1), 1) ), (ItemStack)null, itemStackColorizable, 0, 2) );
								
								itemStackColorizable = new ItemStack(Items.leather_chestplate);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.emerald, Math.max(7 - 2*(ev.getProfessionLevelVN()-1), 1) ), (ItemStack)null, itemStackColorizable, 0, 2) );
		    			    	
								itemStackColorizable = new ItemStack(Items.leather_helmet);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.emerald, Math.max(5 - 2*(ev.getProfessionLevelVN()-2), 1) ), (ItemStack)null, itemStackColorizable, 0, 2) );
								
								itemStackColorizable = new ItemStack(Items.leather_boots);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.emerald, Math.max(4 - 2*(ev.getProfessionLevelVN()-2), 1) ), (ItemStack)null, itemStackColorizable, 0, 2) );
								
								
		    					while (true)
		    					{
		    						MerchantRecipe chosenRecipe = possibleExpertLeatherworkerTrades.get(random.nextInt(possibleExpertLeatherworkerTrades.size()));
		    			        	
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
	    									hasSameItems(buyingList.get(Math.max(0, i)), Items.emerald, Items.saddle)
	    									) { // Trade block detected!
	    								
	    								// Erase the entry
	    								eraseTrades(buyingList, i, 1);
	    							}
	    						}
	    						
	    						// Append the new trades as the final slots
	    						
	    						// Slot 9
	    						merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    						
	    						itemStackColorizable = new ItemStack(Items.leather_helmet);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								enchantvalue = 5 + random.nextInt(15);
								merchantRecipeArray.add( new MerchantRecipe(
										new ItemStack( Items.emerald, 5 ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, itemStackColorizable, enchantvalue ), 0, 2
												));
	    						
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.emerald, 6 ), (ItemStack)null, new ItemStack( Items.saddle, 1 ), 0, 3) );
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
    private static boolean hasSameItems(MerchantRecipe recipe, Item itemToBuy, @Nullable Item secondItemToBuy, Item itemToSell)
    {
        return
        		itemToBuy == recipe.getItemToBuy().getItem()
        		&& itemToSell == recipe.getItemToSell().getItem() ?
        				secondItemToBuy == null
        				&& recipe.getSecondItemToBuy() == null
        				|| secondItemToBuy != null
        				&& recipe.getSecondItemToBuy() != null
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
        		(itemStackToBuy.getItem() == recipe.getItemToBuy().getItem() && itemStackToBuy.getMetadata() == recipe.getItemToBuy().getMetadata())
        		&& (itemStackToSell.getItem() == recipe.getItemToSell().getItem() && itemStackToSell.getMetadata() == recipe.getItemToSell().getMetadata()) ?
        				secondItemStackToBuy == null
        				&& recipe.getSecondItemToBuy() == null
        				|| secondItemStackToBuy != null
        				&& recipe.getSecondItemToBuy() != null
        				&& (secondItemStackToBuy.getItem() == recipe.getSecondItemToBuy().getItem() && secondItemStackToBuy.getMetadata() == recipe.getSecondItemToBuy().getMetadata())
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

        if (Item.itemRegistry.containsKey(resourcelocation))
        {
            return (Item)Item.itemRegistry.getObject(resourcelocation);
        }
        else
        {
            try
            {
                return (Item)Item.itemRegistry.getObjectById(Integer.parseInt(name));
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
    	// summon Villager ~ ~ ~ {Profession:1}
    	
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
        				new ItemStack(ModItems.mineshaftbook, 1),
        				new ItemStack(Items.emerald, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.efficiency, enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( mineshaftForEnchantBook );
                
                enchantLevel = 3;//2 + random.nextInt(2);
                MerchantRecipe mineshaftForFortuneBook = new MerchantRecipe(
        				new ItemStack(ModItems.mineshaftbook, 1),
        				new ItemStack(Items.emerald, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.fortune, enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( mineshaftForFortuneBook );
                
                
                // --- STRONGHOLD --- //
        		enchantLevel = 1;
                MerchantRecipe strongholdForInfinity = new MerchantRecipe(
        				new ItemStack(ModItems.strongholdbook, 1),
        				new ItemStack(Items.emerald, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.infinity, enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( strongholdForInfinity );
                
                
                // --- FORTRESS --- //
        		enchantLevel = 3 + random.nextInt(2);
                MerchantRecipe fortressForFeatherBook = new MerchantRecipe(
        				new ItemStack(ModItems.fortressbook, 1),
        				new ItemStack(Items.emerald, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.featherFalling, enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( fortressForFeatherBook );
                
                
                // --- MONUMENT --- //
        		enchantLevel = 1;
                MerchantRecipe monumentForAquaBook = new MerchantRecipe(
        				new ItemStack(ModItems.monumentbook, 1),
        				new ItemStack(Items.emerald, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.aquaAffinity, enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( monumentForAquaBook );
                
        		
                // --- JUNGLE TEMPLE --- //
        		enchantLevel = 4 + random.nextInt(2);
                MerchantRecipe jungleTempleForBaneBook = new MerchantRecipe(
        				new ItemStack(ModItems.jungletemplebook, 1),
        				new ItemStack(Items.emerald, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.baneOfArthropods, enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( jungleTempleForBaneBook );
        		
                
                // --- DESERT PYRAMID --- //
                enchantLevel = 3 + random.nextInt(2);
                MerchantRecipe desertPyramidForBlastProtectionBook = new MerchantRecipe(
        				new ItemStack(ModItems.desertpyramidbook, 1),
        				new ItemStack(Items.emerald, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.blastProtection, enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( desertPyramidForBlastProtectionBook );
        		
                enchantLevel = 4 + random.nextInt(2);
                MerchantRecipe desertPyramidForSmiteBook = new MerchantRecipe(
        				new ItemStack(ModItems.desertpyramidbook, 1),
        				new ItemStack(Items.emerald, Math.min( (random.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.smite, enchantLevel))
        				, 0, 2
                		);
        		merchantRecipeArray.add( desertPyramidForSmiteBook );
        		
        		
        		// --- VILLAGE -- //
        		
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
        		String bookContents = ""; // Start on line 2
        		switch (random.nextInt(8)) {
        		case 0:
        			bookContents = "If you've gone and built something grand, but don't know what name to give it--why not use this name:";
        			break;
        		case 1:
        			bookContents = "Here's a custom-generated name for you to use, if you wish:";
        			break;
        		case 2:
        			bookContents = "Coming up with names can be difficult. If you're drawing a blank, why not use this name:";
        			break;
        		case 3:
        			bookContents = "Here's a unique name you can give to something if you need some inspiration:";
        			break;
        		case 4:
        			bookContents = Reference.MOD_NAME+" uses a Markov chain to generate names for entities and structures."
        					+ " If you've built something and you want to use VN to generate a new name for it, you can use this one:";
        			bookAuthor = "AstroTibs";
        			break;
        		case 5:
        			bookContents = "Feeling uninspired? Have writer's block? Feel free to use this customized location name:";
        			break;
        		case 6:
        			bookContents = "Maybe you've just built or discovered something, and you're not sure what to name it. Why not name it this:";
        			break;
        		case 7:
        			bookContents = "Coming up with a good, authentic location name can be hard. Well, this name might be neither good nor authentic, but maybe you'll use it anyway:";
        			break;
        		}
        		
        		// Generated name
        		String[] locationName = NameGenerator.newRandomName("village-mineshaft-stronghold-temple-fortress-monument-endcity-mansion-alienvillage", new Random());
        		bookContents += "\n\n" + (locationName[1]+" "+locationName[2]+" "+locationName[3]).trim();
        		
        		// Put it all together
        		ItemStack bookWithName = new ItemStack(Items.written_book);
        		if (bookWithName.getTagCompound() == null) {bookWithName.setTagCompound(new NBTTagCompound());} // Priming the book to receive information
        		
        		bookWithName.getTagCompound().setString("title", bookTitle ); // Set the title
        		
        		if (bookAuthor!=null && !bookAuthor.equals("")) { // Set the author
        			try { bookWithName.getTagCompound().setString("author", bookAuthor.indexOf("(")!=-1 ? bookAuthor.substring(0, bookAuthor.indexOf("(")).trim() : bookAuthor ); }
        			// If the target's name starts with a parenthesis for some reason, this will crash with an index OOB exception. In that case, add no author name.
        			catch (Exception e) {}
        		}
        		else {bookWithName.getTagCompound().setString("author", "");}
        		
        		NBTTagList pagesTag = new NBTTagList();
        		pagesTag.appendTag(new NBTTagString(bookContents));
        		bookWithName.getTagCompound().setTag("pages", pagesTag);
        		
        		// Add the trade
                merchantRecipeArray.add( new MerchantRecipe(
        				new ItemStack(ModItems.villagebook, 1), (ItemStack)null,
        				bookWithName
    					, 0, 2
    					) );
        	}
        	else if (career==2) // Cartographer
        	{
        		// Potion IDs taken from https://www.minecraftinfo.com/IDList.htm
        		
    			// --- STRONGHOLD --- //
    	        MerchantRecipe strongholdForEnderEye = new MerchantRecipe(new ItemStack(ModItems.strongholdbook, 1), (ItemStack)null,
    					new ItemStack(Items.ender_eye, 2)
    					, 0, 2
    					);
    	        merchantRecipeArray.add( strongholdForEnderEye ); // Ender Eye
    			
    	        // --- FORTRESS --- //
    	        MerchantRecipe fortressForFireResistance = new MerchantRecipe( new ItemStack(ModItems.fortressbook, 1), (ItemStack)null,
    					new ItemStack(Items.potionitem, 1, 8259)
    					, 0, 2
    					);
    			merchantRecipeArray.add( fortressForFireResistance ); // Fire Resistance (8:00)
    			
    			// --- SWAMP HUT --- //
    			MerchantRecipe swampHutForHarmingPotion = new MerchantRecipe(new ItemStack(ModItems.swamphutbook, 1), (ItemStack)null,
    					new ItemStack(Items.potionitem, 1, 16428) 
    					, 0, 2
    					);
    			merchantRecipeArray.add( swampHutForHarmingPotion ); // Splash Harming II
    			
    			MerchantRecipe swampHutForHealingPotion = new MerchantRecipe(new ItemStack(ModItems.swamphutbook, 1), (ItemStack)null,
    					new ItemStack(Items.potionitem, 1, 8229) 
    					, 0, 2
    					);
    			merchantRecipeArray.add( swampHutForHealingPotion ); // Healing II
    			
    			// --- MONUMENT --- //
    			MerchantRecipe monumentForWaterBreathing = new MerchantRecipe(new ItemStack(ModItems.monumentbook, 1), (ItemStack)null,
    					new ItemStack(Items.potionitem, 1, 8269)
    					, 0, 2
    					);
    			merchantRecipeArray.add( monumentForWaterBreathing ); // Water Breathing (8:00)
    			
    			// --- JUNGLE TEMPLE --- //
    			MerchantRecipe jungleTempleForStrength = new MerchantRecipe(new ItemStack(ModItems.jungletemplebook, 1), (ItemStack)null,
    					new ItemStack(Items.potionitem, 1, 8233)
    					, 0, 2
    					);
    			merchantRecipeArray.add( jungleTempleForStrength ); // Strength II (1:30)
    			
    			// --- IGLOO --- //
    			if (GeneralConfig.addIgloos) {
    				MerchantRecipe iglooForGoldenApple = new MerchantRecipe(new ItemStack(ModItems.igloobook, 1), (ItemStack)null,
        					new ItemStack(Items.golden_apple, 1)
        					, 0, 2
        					);
    				//iglooForGoldenApple.func_82783_a(-6);
    				merchantRecipeArray.add( iglooForGoldenApple );
    				
    				MerchantRecipe iglooForSplashWeakness = new MerchantRecipe(new ItemStack(ModItems.igloobook, 1), (ItemStack)null,
        					new ItemStack(Items.potionitem, 1, 16456)
        					, 0, 2
        					);
    				//iglooForSplashWeakness.func_82783_a(-6);
    				merchantRecipeArray.add( iglooForSplashWeakness ); // Splash Weakness (3:00)
    			}
    			
    			// --- VILLAGE -- //
    			String[] entityName = NameGenerator.newRandomName("villager-angel-demon-dragon-goblin-golem-pet", new Random());
    	        ItemStack tagWithName = new ItemStack(Items.name_tag, 1).setStackDisplayName( (entityName[1]+" "+entityName[2]+" "+entityName[3]).trim() );
    			tagWithName.setRepairCost(99);
    			merchantRecipeArray.add( new MerchantRecipe(
    					new ItemStack(ModItems.villagebook, 1), (ItemStack)null,
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
        Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
        int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
        int emeraldprice = 2 + random.nextInt(5 + i * 10) + 3 * i;
        
        if (emeraldprice > 64)
        {
            emeraldprice = 64;
        }
        
        return (new MerchantRecipe(new ItemStack(Items.emerald, emeraldprice), new ItemStack(Items.book), itemstack, 0, tradeuses));
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
		BiomeGenBase entityBiome = entity.worldObj.getBiomeGenForCoords(entity.getPosition());
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
	public static int biomeToSkinTone(BiomeGenBase biome, Random random)
	{
		// Get a list of tags for this biome
		BiomeDictionary.Type[] typeTags = BiomeDictionary.getTypesForBiome(biome);
		
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
		float sigma = MathHelper.sqrt_double( 1D/(skin_tags*GeneralConfig.villagerSkinToneVarianceAnnealing + 1D) ) * GeneralConfig.villagerSkinToneVarianceScale; 
		double skin_r = random.nextGaussian()*sigma + skin_mu;
		
		// Return this value clamped to the darkest and lightest values
		return MathHelper.clamp_int((int) Math.round(skin_r), -4, 3);
	}

	
	/**
	 * Use this to adjust the villagers' trade when you talk to them.
	 * It essentially scans for and removes all vanilla trades that aren't allowed by the new trade system.
	 */
	public static void monitorVillagerTrades(EntityVillager villager)
	{
    	ExtendedVillager ev = ExtendedVillager.get(villager);
    	Random random = villager.worldObj.rand;
    	NBTTagCompound compound = new NBTTagCompound();
    	villager.writeEntityToNBT(compound);
		int profession = compound.getInteger("Profession");
		int career = compound.getInteger("Career");
		//int careerLevel = compound.getInteger("CareerLevel");
		int careerLevel = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerLevel", "field_175562_bw"});
		//career = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"});
		MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
		
		
    	// Try modifying trades
        // Modified in v3.1trades
        // Check trading list against modern trades
        if (GeneralConfig.modernVillagerTrades // Added condition in v3.1trades
        		//&& villager.ticksExisted%4==0 // Check only ever four ticks
        		&& (buyingList!=null && buyingList.size()>0) )
        {
        	// Update the villager's VN career -- used mostly to create the Cartographer or Mason.
        	if (ev.getCareerVN() == 0 )
        	{
    			ev.setCareerVN(
    					(villager.getProfession() == 1 && random.nextBoolean()) ? 2 : // 50% chance a Prof1 will be a Cartographer
    					(villager.getProfession() == 3 && random.nextInt(4)==0) ? 4 : // 25% chance a Prof3 will be a Mason
    					ev.getCareer()
    					);
    		}
        	
        	// Then, modernize the trades
        	if (ev.getProfessionLevelVN() < ev.getProfessionLevel() && GeneralConfig.modernVillagerTrades)
        	{
        		FunctionsVN.modernizeVillagerTrades(villager);
        	}
        	
        }
        
		
		// If you're talking to a vanilla Villager, check the trades list
		if (profession == 1) {
			
			// summon Villager ~ ~ ~ {Profession:1}
			
			if (
					career == 1 && careerLevel >= 3
					&& GeneralConfig.writtenBookTrade
					) { // Fix the Librarian's written book trade
				
				try {
					
					// Get the current buying list
					
					for (int i=5; i < buyingList.size(); i++) { // The written book trade is at least at index 5
						
						MerchantRecipe extractedRecipe = buyingList.get(i);
						ItemStack itemToBuy1 = extractedRecipe.getItemToBuy();
						ItemStack itemToBuy2 = extractedRecipe.getSecondItemToBuy();
						ItemStack itemToSell = extractedRecipe.getItemToSell();
						
						if (
								itemToBuy1.getItem() == Items.written_book && itemToBuy1.stackSize == 2
								&& itemToBuy2 == null
								&& itemToSell.getItem() == Items.emerald
								) { // This is the malformed trade. Fix it below.
							buyingList.set(i, new MerchantRecipe( new ItemStack(Items.written_book, 1), null, new ItemStack(Items.emerald, 1) ));
							if (GeneralConfig.debugMessages) {LogHelper.info("Replacing malformed written book trade for Librarian with ID " + villager.getEntityId());}
							break;
						}
					}
				}
				catch (Exception e) {}
			}
			
			// How many high levels are devoted to treasure trades. This limits you from being able to extract every trade from a single Librarian.
			int treasureTradeCount = 3; 
			if ( !GeneralConfig.modernVillagerTrades &&
					(
						   (career == 1 && careerLevel > 6 && careerLevel <= 6 + treasureTradeCount)
						|| (career == 2 && careerLevel > 3 && careerLevel <= 3 + treasureTradeCount)
							)
					&& GeneralConfig.treasureTrades
					) { // Villager is a Cartographer. Weed out the higher-level trades
				
				try {
					
					// Get the current buying list
					buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
					
					// If the librarian's career level is greater than a certain trade threshold, add new trades.
					if ( buyingList.size() < careerLevel + (career==2 ? 0 : 5) ) {
						//if (GeneralConfigHandler.debugMessages) {LogHelper.info("Librarian careerLevel is " + careerLevel + " and list length is " + buyingList.size() + ". Adding VN trades.");}
						
						
						
						// Librarian treasure trades
						
						int enchantLevel = 1; //used for level-based enchant books 
						
						// --- MINESHAFT --- //
						enchantLevel = 4 + villager.worldObj.rand.nextInt(2);
				        MerchantRecipe mineshaftForEnchantBook = new MerchantRecipe(
		        				new ItemStack(ModItems.mineshaftbook, 1),
		        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
		        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.efficiency, enchantLevel))
				        		);
						buyingList.add( mineshaftForEnchantBook );
				        
				        enchantLevel = 3;//2 + villager.worldObj.rand.nextInt(2);
				        MerchantRecipe mineshaftForFortuneBook = new MerchantRecipe(
		        				new ItemStack(ModItems.mineshaftbook, 1),
		        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
		        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.fortune, enchantLevel))
				        		);
						buyingList.add( mineshaftForFortuneBook );
				        
				        
				        // --- STRONGHOLD --- //
						enchantLevel = 1;
				        MerchantRecipe strongholdForInfinity = new MerchantRecipe(
		        				new ItemStack(ModItems.strongholdbook, 1),
		        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
		        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.infinity, enchantLevel))
				        		);
						buyingList.add( strongholdForInfinity );
				        
				        
				        // --- FORTRESS --- //
						enchantLevel = 3 + villager.worldObj.rand.nextInt(2);
				        MerchantRecipe fortressForFeatherBook = new MerchantRecipe(
		        				new ItemStack(ModItems.fortressbook, 1),
		        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
		        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.featherFalling, enchantLevel))
				        		);
						buyingList.add( fortressForFeatherBook );
				        
				        
				        // --- MONUMENT --- //
						enchantLevel = 1;
				        MerchantRecipe monumentForAquaBook = new MerchantRecipe(
		        				new ItemStack(ModItems.monumentbook, 1),
		        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
		        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.aquaAffinity, enchantLevel))
				        		);
						buyingList.add( monumentForAquaBook );
				        
						
				        // --- JUNGLE TEMPLE --- //
						enchantLevel = 4 + villager.worldObj.rand.nextInt(2);
				        MerchantRecipe jungleTempleForBaneBook = new MerchantRecipe(
		        				new ItemStack(ModItems.jungletemplebook, 1),
		        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
		        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.baneOfArthropods, enchantLevel))
				        		);
						buyingList.add( jungleTempleForBaneBook );
						
				        
				        // --- DESERT PYRAMID --- //
				        enchantLevel = 3 + villager.worldObj.rand.nextInt(2);
				        MerchantRecipe desertPyramidForBlastProtectionBook = new MerchantRecipe(
		        				new ItemStack(ModItems.desertpyramidbook, 1),
		        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
		        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.blastProtection, enchantLevel))
				        		);
						buyingList.add( desertPyramidForBlastProtectionBook );
						
				        enchantLevel = 4 + villager.worldObj.rand.nextInt(2);
				        MerchantRecipe desertPyramidForSmiteBook = new MerchantRecipe(
		        				new ItemStack(ModItems.desertpyramidbook, 1),
		        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
		        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.smite, enchantLevel))
				        		);
						buyingList.add( desertPyramidForSmiteBook );
						
						
						// --- VILLAGE -- //
						
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
						String bookContents = ""; // Start on line 2
						switch (random.nextInt(8)) {
						case 0:
							bookContents = "If you've gone and built something grand, but don't know what name to give it--why not use this name:";
							break;
						case 1:
							bookContents = "Here's a custom-generated name for you to use, if you wish:";
							break;
						case 2:
							bookContents = "Coming up with names can be difficult. If you're drawing a blank, why not use this name:";
							break;
						case 3:
							bookContents = "Here's a unique name you can give to something if you need some inspiration:";
							break;
						case 4:
							bookContents = Reference.MOD_NAME+" uses a Markov chain to generate names for entities and structures."
									+ " If you've built something and you want to use VN to generate a new name for it, you can use this one:";
							bookAuthor = "AstroTibs";
							break;
						case 5:
							bookContents = "Feeling uninspired? Have writer's block? Feel free to use this customized location name:";
							break;
						case 6:
							bookContents = "Maybe you've just built or discovered something, and you're not sure what to name it. Why not name it this:";
							break;
						case 7:
							bookContents = "Coming up with a good, authentic location name can be hard. Well, this name might be neither good nor authentic, but maybe you'll use it anyway:";
							break;
						}
						
						// Generated name
						String[] locationName = NameGenerator.newRandomName("village-mineshaft-stronghold-temple-fortress-monument-endcity-mansion-alienvillage", new Random());
						bookContents += "\n\n" + (locationName[1]+" "+locationName[2]+" "+locationName[3]).trim();
						
						// Put it all together
						ItemStack bookWithName = new ItemStack(Items.written_book);
						if (bookWithName.getTagCompound() == null) {bookWithName.setTagCompound(new NBTTagCompound());} // Priming the book to receive information
						
						bookWithName.getTagCompound().setString("title", bookTitle ); // Set the title
						
						if (bookAuthor!=null && !bookAuthor.equals("")) { // Set the author
							try { bookWithName.getTagCompound().setString("author", bookAuthor.indexOf("(")!=-1 ? bookAuthor.substring(0, bookAuthor.indexOf("(")).trim() : bookAuthor ); }
							// If the target's name starts with a parenthesis for some reason, this will crash with an index OOB exception. In that case, add no author name.
							catch (Exception e) {}
						}
						else {bookWithName.getTagCompound().setString("author", "");}
						
						NBTTagList pagesTag = new NBTTagList();
						pagesTag.appendTag(new NBTTagString(bookContents));
						bookWithName.getTagCompound().setTag("pages", pagesTag);
						
						// Add the trade
				        buyingList.add( new MerchantRecipe(
								new ItemStack(ModItems.villagebook, 1),
								bookWithName) );
						
						
						
						// Cartographer treasure trades
						
						// Potion IDs taken from https://www.minecraftinfo.com/IDList.htm
						
						// --- STRONGHOLD --- //
				        MerchantRecipe strongholdForEnderEye = new MerchantRecipe(new ItemStack(ModItems.strongholdbook, 1), new ItemStack(Items.ender_eye, 2));
				        buyingList.add( strongholdForEnderEye ); // Ender Eye
						
				        // --- FORTRESS --- //
				        MerchantRecipe fortressForFireResistance = new MerchantRecipe( new ItemStack(ModItems.fortressbook, 1), new ItemStack(Items.potionitem, 1, 8259));
						buyingList.add( fortressForFireResistance ); // Fire Resistance (8:00)
						
						// --- SWAMP HUT --- //
						MerchantRecipe swampHutForHarmingPotion = new MerchantRecipe(new ItemStack(ModItems.swamphutbook, 1), new ItemStack(Items.potionitem, 1, 16428) );
						buyingList.add( swampHutForHarmingPotion ); // Splash Harming II
						
						MerchantRecipe swampHutForHealingPotion = new MerchantRecipe(new ItemStack(ModItems.swamphutbook, 1), new ItemStack(Items.potionitem, 1, 8229) );
						buyingList.add( swampHutForHealingPotion ); // Healing II
						
						// --- MONUMENT --- //
						MerchantRecipe monumentForWaterBreathing = new MerchantRecipe(new ItemStack(ModItems.monumentbook, 1), new ItemStack(Items.potionitem, 1, 8269));
						buyingList.add( monumentForWaterBreathing ); // Water Breathing (8:00)
						
						// --- JUNGLE TEMPLE --- //
						MerchantRecipe jungleTempleForStrength = new MerchantRecipe(new ItemStack(ModItems.jungletemplebook, 1), new ItemStack(Items.potionitem, 1, 8233));
						buyingList.add( jungleTempleForStrength ); // Strength II (1:30)
						
						// --- IGLOO --- //
						if (GeneralConfig.addIgloos) {
							MerchantRecipe iglooForGoldenApple = new MerchantRecipe(new ItemStack(ModItems.igloobook, 1), new ItemStack(Items.golden_apple, 1));
							//iglooForGoldenApple.func_82783_a(-6);
							buyingList.add( iglooForGoldenApple );
							
							MerchantRecipe iglooForSplashWeakness = new MerchantRecipe(new ItemStack(ModItems.igloobook, 1), new ItemStack(Items.potionitem, 1, 16456));
							//iglooForSplashWeakness.func_82783_a(-6);
							buyingList.add( iglooForSplashWeakness ); // Splash Weakness (3:00)
						}
						
						// --- VILLAGE -- //
						String[] entityName = NameGenerator.newRandomName("villager-angel-demon-dragon-goblin-golem-pet", new Random());
				        ItemStack tagWithName = new ItemStack(Items.name_tag, 1).setStackDisplayName( (entityName[1]+" "+entityName[2]+" "+entityName[3]).trim() );
						tagWithName.setRepairCost(99);
						buyingList.add( new MerchantRecipe(
								new ItemStack(ModItems.villagebook, 1),
								tagWithName) );
				        
					}
					
					if ( buyingList.size() > careerLevel + (career==2 ? 0 : 5) )
					{
						// First, do a scan and remove duplicates.
						
						for (int i=(career==2 ? 3 : 11); i < buyingList.size(); i++)
						{
							ItemStack stackBuyToCompare  = buyingList.get(i).getItemToBuy();  // Villager BUYS item from you
							ItemStack stackSellToCompare = buyingList.get(i).getItemToSell(); // Villager SELLS item to you
							
							for (int j=buyingList.size()-1; j>i; j--)
							{
								ItemStack stackBuyToEvaluate  = buyingList.get(j).getItemToBuy();
								ItemStack stackSellToEvaluate = buyingList.get(j).getItemToSell();
								
								Set enchantmentCompare  = EnchantmentHelper.getEnchantments(stackSellToCompare).keySet();
								Set enchantmentEvaluate  = EnchantmentHelper.getEnchantments(stackSellToEvaluate).keySet();
								
								if (
										   stackBuyToCompare.getItem()  == stackBuyToEvaluate.getItem()
										&& stackSellToCompare.getItem() == stackSellToEvaluate.getItem()
										&& stackSellToCompare.getMetadata() == stackSellToEvaluate.getMetadata()
										&& enchantmentCompare.equals(enchantmentEvaluate) // Compares the enchantments of the trades. Both are -1 and so returns "true" if not both are enchanted books.
										)
								{
									// This is a duplicate trade. Remove it.
									//if (GeneralConfigHandler.debugMessages) {LogHelper.info("Buying list length " + buyingList.size() + ". Duplicate trade detected at index " + j);}
									buyingList.remove(j);
								}
							}
						}
						
						// Then, randomly remove entries from the end until the trading list is the right length.
						int loopKiller = 0;
						
						while ( buyingList.size() > careerLevel + (career==2 ? 0 : 5) ) {
							
							int indexToRemove = (careerLevel-1 + (career==2 ? 0 : 5)) + villager.worldObj.rand.nextInt( buyingList.size() - (careerLevel-1 + (career==2 ? 0 : 5)) );
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
					
					// Finally, cram the adjusted buying list back into the cartographer.
					ReflectionHelper.setPrivateValue(EntityVillager.class, villager, buyingList, new String[]{"buyingList", "field_70963_i"});
					
				}
				catch (Exception e) {}//Something went wrong.

			}
		}
    	
		
	}
	
	
	/**
	 * Inputs a villager (ideally, a fisherman) and returns a RANDOM boat type to buy.
	 */
	public static Item returnRandomBoatTypeForVillager(EntityVillager villager)
	{
		Item[] boats = new Item[]{
				null,
				null,
				null,
				null,
				Items.boat,
				null
		};
		
		int[] randomOrder = shuffledIntArray(0, 5, villager.getRNG());
		for (int i=0; i<6 ; i++) {if (boats[randomOrder[i]]!=null) {return boats[randomOrder[i]];}}
		
		// Failsafe, return ordinary boat.
		return Items.boat;
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
		BiomeGenBase biome = entity.worldObj.getBiomeGenForCoords(entity.getPosition());
		
		BiomeDictionary.Type[] typeTags = BiomeDictionary.getTypesForBiome(biome);
		
		ArrayList<String> boatTypes = new ArrayList<String>();
		
		// Add wood types to pool based on name
		if (biome.biomeName.toLowerCase().contains("birch")) {boatTypes.add("birch");}
		if (biome.biomeName.toLowerCase().contains("roofed")) {boatTypes.add("darkoak");}
		
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
					return MathHelper.floor_double(median);
				}
				else if (average > median)
				{
					//LogHelper.info("Median chosen type C: " + MathHelper.ceiling_double_int(median) );
					return MathHelper.ceiling_double_int(median);
				}
				else
				{
					//LogHelper.info("Median chosen type D: " + (roundup ? MathHelper.ceiling_double_int(median) : MathHelper.floor_double(median)));
					return roundup ? MathHelper.ceiling_double_int(median) : MathHelper.floor_double(median);
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
		Block block = ModBlocksVN.blockGlazedTerracottaWhite;
		
		switch (colorMeta)
		{
		// Case 0 is handled by default
		case 1: block=ModBlocksVN.blockGlazedTerracottaOrange; break;
		case 2: block=ModBlocksVN.blockGlazedTerracottaMagenta; break;
		case 3: block=ModBlocksVN.blockGlazedTerracottaLightBlue; break;
		case 4: block=ModBlocksVN.blockGlazedTerracottaYellow; break;
		case 5: block=ModBlocksVN.blockGlazedTerracottaLime; break;
		case 6: block=ModBlocksVN.blockGlazedTerracottaPink; break;
		case 7: block=ModBlocksVN.blockGlazedTerracottaGray; break;
		case 8: block=ModBlocksVN.blockGlazedTerracottaSilver; break;
		case 9: block=ModBlocksVN.blockGlazedTerracottaCyan; break;
		case 10: block=ModBlocksVN.blockGlazedTerracottaPurple; break;
		case 11: block=ModBlocksVN.blockGlazedTerracottaBlue; break;
		case 12: block=ModBlocksVN.blockGlazedTerracottaBrown; break;
		case 13: block=ModBlocksVN.blockGlazedTerracottaGreen; break;
		case 14: block=ModBlocksVN.blockGlazedTerracottaRed; break;
		case 15: block=ModBlocksVN.blockGlazedTerracottaBlack; break;
		}
		
		return block.getStateFromMeta(facingMeta);
	}
	
	/*
	// Way to convert from color meta int into string formatting (for e.g. signs)
	public static String mapColorMetaToStringFormat(int colorMeta) {
		HashMap<Integer, String> signColorToFormat = new HashMap<Integer, String>();//new HashMap();
		// This hashmap translates the town's name color on the sign to a color meta value.
		// This meta should be universal through e.g. wool, clay, etc
		signColorToFormat.put(0, "\u00a7f"); //white
		signColorToFormat.put(1, "\u00a76"); //gold
		signColorToFormat.put(2, "\u00a7d"); //light_purple
		signColorToFormat.put(3, "\u00a79"); //blue
		signColorToFormat.put(4, "\u00a7e"); //yellow
		signColorToFormat.put(5, "\u00a7a"); //green
		signColorToFormat.put(6, "\u00a7c"); //red
		signColorToFormat.put(7, "\u00a78"); //dark_gray
		signColorToFormat.put(8, "\u00a77"); //gray
		//signColorToFormat.put(9, "\u00a7b"); //aqua
		signColorToFormat.put(9, "\u00a73"); //dark_aqua
		signColorToFormat.put(10, "\u00a75"); //dark_purple
		signColorToFormat.put(11, "\u00a71"); //dark_blue
		signColorToFormat.put(12, "\u00a70"); //black
		signColorToFormat.put(13, "\u00a72"); //dark_green
		signColorToFormat.put(14, "\u00a74"); //dark_red
		signColorToFormat.put(15, "\u00a70"); //black
		
		// Return a "town color" string
		return signColorToFormat.get(colorMeta);
	}
	*/
}
