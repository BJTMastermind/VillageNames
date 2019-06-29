package astrotibs.villagenames.utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.name.NameGenerator;
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
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

//Added in v3.1
public class FunctionsVN {
	
	/**
	 * Determine the biometype of the biome the entity is currently in
	 */
	public static int returnBiomeTypeForEntityLocation(EntityLiving entity)
	{
		Biome entityBiome = entity.worldObj.getBiomeGenForCoords(entity.getPosition());
		return biomeToSkinType(entityBiome);
	}
	
	/**
	 * Inputs a biome and returns the skin type it translates to
	 */
	public static int biomeToSkinType(Biome biome)
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
	
	
    public static int pickRandomCareerForProfession(int profession, Random random)
    {
			switch(profession) {
			
			case 0: // FARMER
				return 1 + random.nextInt(4);
			
			case 1: // LIBRARIAN
				return 1 + random.nextInt((GeneralConfig.enableCartographer ? 2 : 1));
			
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

    
    /**
     * Check if the given entity is a original Zombie (normal, baby or villager), 
     * and not a inherited class (like Zombie Pigman).
     * Adapted from Villager Tweaks by sidben:
     * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/helper/GenericHelper.java
     */
    public static boolean isVanillaZombie(Entity entity) {
    	
    	if (entity == null) return false;
    	else if (entity instanceof EntityPigZombie) return false;
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
    	IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
    	int careerLevel = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerLevel", "field_175562_bw"});
    	MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
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
    		case 0: // Farmer type: summon Villager ~ ~ ~ {Profession:0}
    			
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
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.WHEAT, 20 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.POTATO, 26 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.CARROT, 22 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.BEETROOT, 15 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.BREAD, 6 ), 0, 5
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
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.PUMPKIN), 6 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.PUMPKIN_PIE, 4 ), 0, 5) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.APPLE, 4 ), 0, 5) );
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
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.MELON_BLOCK), Items.EMERALD)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.APPLE)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 5
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.MELON_BLOCK), 4 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5 // BE version; the JE version uses whole melons
	    										));
	    								
	    								// Slot 6
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack( Items.COOKIE, 18 ), 0, 4 // BE version; the JE version uses whole melons
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
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EGG, 16 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5 // BE version; the JE version uses whole melons
	    										));
	    								
	    								// Slot 8
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.CAKE, 1 ), 0, 5 // BE version; the JE version uses whole melons
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
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack( Items.GOLDEN_CARROT, 3 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), (ItemStack)null, new ItemStack( Items.SPECKLED_MELON, 3 ), 0, 5) );
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
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.STRING, 20 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.COAL, 10 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								// TODO - Emerald to Bucket of Cod (Added 1.13)
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Items.FISH, 6, 0 ), new ItemStack( Items.COOKED_FISH, 6, 0 ), 0, 5) );
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
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.FISHING_ROD)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.FISH, 15, 0 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								// TODO - Campfire (Added 1.14)
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Items.FISH, 6, 1 ), new ItemStack( Items.COOKED_FISH, 6, 1 ), 0, 5) );
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
										new ItemStack( Items.FISH, 13, 1 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
	    						// Slot 6: Enchanted Fishing Rod
								enchantvalue = 5 + random.nextInt(15);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
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
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.FISH, 6, 2 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( new Item[]{
										Items.BOAT, Items.ACACIA_BOAT, Items.BIRCH_BOAT, Items.DARK_OAK_BOAT, Items.JUNGLE_BOAT, Items.SPRUCE_BOAT
										}[random.nextInt(6)], 1 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) );
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
										new ItemStack( Items.FISH, 4, 3 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
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
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Item.getItemFromBlock(Blocks.WOOL), 18, new int[]{0,12,15,7}[random.nextInt(4)] ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6
	    										));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 2 ), (ItemStack)null, new ItemStack( Items.SHEARS, 1 ), 0, 5
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
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 0 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
									}
									else {
										while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeWhiteBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 15 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
									}
								}
								else {
									buyingList.add(new MerchantRecipe(
    										new ItemStack( Items.DYE, 12, new int[]{8,10,12}[random.nextInt(3)] ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
    										));
								}
								
																
								// Slot 4
								color1 = random.nextInt(16);
								if (random.nextBoolean())
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, color1), 0, 5
    										));
									if (random.nextBoolean())
    								{
										buyingList.add(new MerchantRecipe(
    											new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, (color1+random.nextInt(15)+1)%16), 0, 5
	    										));
    								}
								}
								else
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.CARPET), 4, color1), 0, 5
    										));
									if (random.nextBoolean())
    								{
										buyingList.add(new MerchantRecipe(
    											new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack(Item.getItemFromBlock(Blocks.CARPET), 4, (color1+random.nextInt(15)+1)%16), 0, 5
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
										new ItemStack( Items.DYE, 12, new int[]{1,7,9,11,14}[random.nextInt(5)] ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
								// Slot 6
								buyingList.add(new MerchantRecipe( // TODO - colorized bed (Added in 1.12)
										new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack( Items.BED, 1 ), 0, 5
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
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 2 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    								
	    							case 1: // Special handler for brown dye
	    								while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeBrownBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 3 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    								
	    							case 2: // Special handler for blue dye
	    								while (true) {
	    									moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlueBOP);
	    									if (moditem != null) {buyingList.add(new MerchantRecipe(
	    											new ItemStack( moditem, 12 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) ); break;}
	    									
	    									buyingList.add(new MerchantRecipe(
	    											new ItemStack( Items.DYE, 12, 4 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    											) );
	    									break;}
	    								break;
	    							}
	    							
	    						}
	    						else {
	    							buyingList.add(new MerchantRecipe(
											new ItemStack( Items.DYE, 12, new int[]{5,6,13}[random.nextInt(3)] ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
											));
	    						}
								
								// Slot 8
								color1 = random.nextInt(16);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack(Items.BANNER, 1, color1), 0, 5
										));
								if (random.nextBoolean())
								{
									buyingList.add(new MerchantRecipe(
											new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack(Items.BANNER, 1, (color1+random.nextInt(15)+1)%16), 0, 5
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
										new ItemStack( Items.EMERALD, 2 ), (ItemStack)null, new ItemStack( Items.PAINTING, 3 ), 0, 5
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
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.STICK, 32 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6
	    										));
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.ARROW, 16 ), 0, 5) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), new ItemStack( Item.getItemFromBlock(Blocks.GRAVEL), 10 ), new ItemStack( Items.FLINT, 10 ), 0, 5) );
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
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.BOW)
	    									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Item.getItemFromBlock(Blocks.GRAVEL), Items.EMERALD, Items.FLINT)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								// Slot 3
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.FLINT, 26 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 2 ), (ItemStack)null, new ItemStack( Items.BOW, 1 ), 0, 5
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
										new ItemStack( Items.STRING, 14 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
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
										new ItemStack( Items.FEATHER, 24 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
								
								// Slot 8
	    						enchantvalue = 5 + random.nextInt(15);
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
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
										new ItemStack( Item.getItemFromBlock(Blocks.TRIPWIRE_HOOK), 8 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
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
    			
				
    		case 1: // Librarian type: summon Villager ~ ~ ~ {Profession:1}
    			
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
        								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
        										new ItemStack( Items.PAPER, 24 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, ims.getCareer()==2 ? 6 : 4
        										));
        								
        								
    									// Slot 2
    									merchantRecipeArray = new ArrayList<MerchantRecipe>();
    									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 6 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.BOOKSHELF), 3 ), 0, 5) );
    									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
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
            						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
        							if (
        									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.BOOK, Items.EMERALD)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.COMPASS)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Item.getItemFromBlock(Blocks.BOOKSHELF))
        									) { // Trade block detected!
        								
        								
        								// Add a new block with one or more trades
        								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
        								

        								// Slot 3
        								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
        										new ItemStack( Items.BOOK, 4 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
        										));
        								
        								// Slot 4
        								merchantRecipeArray = new ArrayList<MerchantRecipe>();
        								//TODO - Lantern (Added in 1.14)
        								merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
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
            						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
        							if (
        									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.WRITTEN_BOOK, Items.EMERALD)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.CLOCK)
        									&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Item.getItemFromBlock(Blocks.GLASS))
        									) { // Trade block detected!
        								
        								// Add a new block with one or more trades
        								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
        								
        								
        								// Slot 5
    									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
    											new ItemStack( Items.DYE, 5, 0 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
    											));
    									
    									// Slot 6
    									merchantRecipeArray = new ArrayList<MerchantRecipe>();
    									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.GLASS), 4 ), 0, 5) );
    									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
    									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
    									
        								
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
    									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
    											new ItemStack( Items.WRITABLE_BOOK, 1 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 2
    											));
    									
    									// Slot 8
    									merchantRecipeArray = new ArrayList<MerchantRecipe>();
    									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), (ItemStack)null, new ItemStack( Items.COMPASS, 1 ), 0, 5) );
    									merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 5 ), (ItemStack)null, new ItemStack( Items.CLOCK, 1 ), 0, 5) );
    									merchantRecipeArray.add(modernEnchantedBookTrade(random, 2));
    									buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
    									
        								
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
        								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
        										new ItemStack( Items.EMERALD, 20 ), (ItemStack)null, new ItemStack( Items.NAME_TAG, 1 ), 0, 5
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
    					
    				case 2: // Cartographer
    					
    					switch(ims.getProfessionLevel())
    					{
	    					
	    					case 1: // Novice
	    						
	    						// Taken care of in VillagerTrades.modifyMerchantRecipeList
	    						break;
	    						
	    					case 2: // Apprentice
	    						
	    						// Slot 3
	    						buyingList.add( new MerchantRecipe(new ItemStack(Item.getItemFromBlock(Blocks.GLASS_PANE), 11), (ItemStack)null, new ItemStack(Items.EMERALD, 1), 0, 5) );
	    						
	    						// Slot 4
	    						// TODO - Ocean Explorer Map (Added in 1.11)
	    						
	    						break;
	    						
	    					case 3: // Journeyman
	    						
	    						// Slot 5
	    						buyingList.add( new MerchantRecipe(new ItemStack(Items.COMPASS, 1), (ItemStack)null, new ItemStack(Items.EMERALD, 1), 0, 5) );
	    						
	    						// Slot 6
	    						// TODO - Woodland Explorer Map (Added in 1.11)
	    						
	    						break;
	    						
	    					case 4: // Expert
	    						
        						// Slot 7
    							buyingList.add(new MerchantRecipe(
    									new ItemStack( Items.EMERALD, 7 ), (ItemStack)null, new ItemStack( Items.ITEM_FRAME, 1 ), 0, 5
    									));
    							
    							// Slot 8
    							color1 = random.nextInt(16);
    							buyingList.add(new MerchantRecipe(
    									new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack(Items.BANNER, 1, color1), 0, 5
    									));
    							if (random.nextBoolean())
    							{
    								buyingList.add(new MerchantRecipe(
    										new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack(Items.BANNER, 1, (color1+random.nextInt(15)+1)%16), 0, 5
    										));
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
	    									new ItemStack( Items.EMERALD, 2 ), (ItemStack)null, BannerGenerator.makeBanner(bannerNBT), 0, 5
	    									));
	    						}
	    						else // No banner was found or is available. INSTEAD, sell a new banner with a random design.
	    						{
	    							Object[] newRandomBanner = BannerGenerator.randomBannerArrays(villager.worldObj.rand, -1);
	    							ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
	    							ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
	    																	
	    							buyingList.add(new MerchantRecipe(
	    									new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, BannerGenerator.makeBanner(patternArray, colorArray), 0, 5
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
				
    		case 2: // Priest type: summon Villager ~ ~ ~ {Profession:2}
    			
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.ROTTEN_FLESH, 32 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6
												));
										
										// Slot 2
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.REDSTONE, 4 ), 0, 5
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.GOLD_INGOT, 3 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 4
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.DYE, 1, 4 ), 0, 5
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.RABBIT_FOOT, 2 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 6
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 4 ), (ItemStack)null, new ItemStack( Item.getItemFromBlock(Blocks.GLOWSTONE), 1 ), 0, 5
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
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.GLASS_BOTTLE, 9 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
										// Slot 8
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 5 ), (ItemStack)null, new ItemStack( Items.ENDER_PEARL, 1 ), 0, 5
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
										new ItemStack( Items.NETHER_WART, 22 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
								
								// Slot 10
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, 3), (ItemStack)null, new ItemStack( Items.EXPERIENCE_BOTTLE, 1 ), 0, 5
										));
								
								break;
						}
    					break;
    			}
				break;
				
				
    		case 3: // Blacksmith type: summon Villager ~ ~ ~ {Profession:3}
    			
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.COAL, 15 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6
												));
										
										// Slot 2
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 5 ), (ItemStack)null, new ItemStack( Items.IRON_HELMET, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 9 ), (ItemStack)null, new ItemStack( Items.IRON_CHESTPLATE, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 7 ), (ItemStack)null, new ItemStack( Items.IRON_LEGGINGS, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), (ItemStack)null, new ItemStack( Items.IRON_BOOTS, 1 ), 0, 3) );
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
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.IRON_INGOT, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.IRON_CHESTPLATE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 3
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.IRON_INGOT, 4 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 4
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
										// TODO - Bell (Added in 1.14)
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.CHAINMAIL_BOOTS, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack( Items.CHAINMAIL_LEGGINGS, 1 ), 0, 3) );
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
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.DIAMOND, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.DIAMOND_CHESTPLATE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 5
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.LAVA_BUCKET, 1 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 6
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.CHAINMAIL_HELMET, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), (ItemStack)null, new ItemStack( Items.CHAINMAIL_CHESTPLATE, 1 ), 0, 3) );
	    								
	    								// Added in v3.1banner - The shield sold has a village banner on it, if applicable.
	    								Object[] villageShieldData = BannerGenerator.getVillageBannerData(villager);
	    								NBTTagCompound bannerForShield = new NBTTagCompound();
	    								String villageNameForShield = "";
	    								if (villageShieldData!=null) {
	    									bannerForShield = (NBTTagCompound) villageShieldData[0];
	    									villageNameForShield = (String) villageShieldData[1];}
	    								
	    								merchantRecipeArray.add(new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 5 ), (ItemStack)null, 
	    										(villageShieldData!=null) ? BannerGenerator.makeShieldWithBanner(bannerForShield, villageNameForShield) : new ItemStack( Items.SHIELD, 1 )
	    										, 0, 3
	    										));
	    								
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));

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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.DIAMOND, 1 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 8
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
										enchantvalue = 5 + random.nextInt(15);
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_LEGGINGS, 1), enchantvalue, allowTreasure ), 0, 2
												));
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(6+random.nextInt(3)) ), (ItemStack)null,
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_BOOTS, 1), enchantvalue, allowTreasure ), 0, 2
												));
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
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
										new ItemStack( Items.EMERALD, enchantvalue+(6+random.nextInt(3)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_HELMET, 1), enchantvalue, allowTreasure ), 0, 2
										));
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.COAL, 15 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6
												));
										
										// Slot 2
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, new ItemStack( Items.IRON_AXE, 1 ), 0, 3
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.IRON_INGOT, 4 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 4
										enchantvalue = 5 + random.nextInt(15);
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.FLINT, 24 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
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
										new ItemStack( Items.DIAMOND, 1 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
	    						// Slot 8
								enchantvalue = 5 + random.nextInt(15);
								buyingList.add( new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
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
										new ItemStack( Items.EMERALD, enchantvalue+(6+random.nextInt(3)) ), (ItemStack)null,
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.COAL, 15 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6
												));
										
										// Slot 2
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.STONE_AXE, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.STONE_PICKAXE, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.STONE_SHOVEL, 1 ), 0, 3) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.STONE_HOE, 1 ), 0, 3) );
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
									int cbs = 0;
									if (
											hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.IRON_INGOT, Items.EMERALD)
											&& hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.IRON_PICKAXE)
											) { // Trade block detected!
										
										int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
										
										// Slot 3
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.IRON_INGOT, 4 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
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
										buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
												new ItemStack( Items.FLINT, 30 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
												));
										
										// Slot 6
										merchantRecipeArray = new ArrayList<MerchantRecipe>();
										enchantvalue = 5 + random.nextInt(15);
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.IRON_AXE, 1), enchantvalue, allowTreasure ), 0, 2
												));
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.IRON_SHOVEL, 1), enchantvalue, allowTreasure ), 0, 2
												));
										merchantRecipeArray.add(  new MerchantRecipe(
												new ItemStack( Items.EMERALD, enchantvalue+(2+random.nextInt(2)) ), (ItemStack)null,
												EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.IRON_PICKAXE, 1), enchantvalue, allowTreasure ), 0, 2
												));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), (ItemStack)null, new ItemStack( Items.DIAMOND_HOE, 1 ), 0, 2) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
										
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
										new ItemStack( Items.DIAMOND, 1 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
										));
	    						
	    						// Slot 8
								merchantRecipeArray = new ArrayList<MerchantRecipe>();
								enchantvalue = 5 + random.nextInt(15);
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, new ItemStack(Items.DIAMOND_AXE, 1), enchantvalue, allowTreasure ), 0, 2
										));
								merchantRecipeArray.add(  new MerchantRecipe(
										new ItemStack( Items.EMERALD, enchantvalue+(6+random.nextInt(2)) ), (ItemStack)null,
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
										new ItemStack( Items.EMERALD, enchantvalue+(13+random.nextInt(4)) ), (ItemStack)null,
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
    			
    			
    		case 4: // Butcher type: summon Villager ~ ~ ~ {Profession:4}
    			
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
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.CHICKEN, 14 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.RABBIT, 4 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.PORKCHOP, 7 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6) );
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
	    								
	    								// Slot 2
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.RABBIT_STEW, 1 ), 0, 5
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
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.COAL, 15 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_RABBIT, 5 ), 0, 5) ); // BE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_CHICKEN, 8 ), 0, 5) ); // JE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_PORKCHOP, 5 ), 0, 5) ); // JE price
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_MUTTON, 8 ), 0, 5) ); // VN price
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
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.BEEF, 10 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) );
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.MUTTON, 8 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) ); // BE price
								buyingList.add( merchantRecipeArray.get(random.nextInt(merchantRecipeArray.size())));
								
								// Slot 6
								buyingList.add(new MerchantRecipe(
										new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_BEEF, 5 ), 0, 5 // VN price
										));
								
								break;
								
		    				case 4: // Expert
		    					// TODO - Dried Kelp
		    				case 5: // Master
		    					// TODO - Sweet Berries (Added in 1.14)
		    					
		    					// INSTEAD, for levels 4 and 5, add a trade that was not previously added--and do so at an increased value to the player:
		    			    	ArrayList<MerchantRecipe> possibleHighButcherTrades = new ArrayList<MerchantRecipe>();
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.CHICKEN, Math.max(14 - ims.getProfessionLevel() + 1, 1) ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.RABBIT, Math.max(4 - ims.getProfessionLevel() + 1, 1) ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.PORKCHOP, Math.max(7 - ims.getProfessionLevel() + 1, 1) ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) );
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_RABBIT, Math.min(5 + ims.getProfessionLevel() - 2, 64) ), 0, 5) ); // BE price
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_CHICKEN, Math.min(8 + ims.getProfessionLevel() - 2, 64) ), 0, 5) ); // JE price
		    			    	possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_PORKCHOP, Math.min(5 + ims.getProfessionLevel() - 2, 64) ), 0, 5) ); // JE price
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 1 ), (ItemStack)null, new ItemStack( Items.COOKED_MUTTON, Math.min(8 + ims.getProfessionLevel() - 2, 64) ), 0, 5) ); // VN price
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.BEEF, Math.max(10 - ims.getProfessionLevel() + 3, 1) ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) );
								possibleHighButcherTrades.add(new MerchantRecipe( new ItemStack( Items.MUTTON, Math.max(8 - ims.getProfessionLevel() + 3, 1) ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5) ); // BE price
								
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
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.LEATHER, 6 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 6
	    										));
	    								
	    								
	    								// Slot 2
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								
	    								itemStackColorizable = new ItemStack(Items.LEATHER_LEGGINGS);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 3 ), (ItemStack)null, itemStackColorizable, 0, 2) );
	    								
	    								itemStackColorizable = new ItemStack(Items.LEATHER_CHESTPLATE);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 7 ), (ItemStack)null, itemStackColorizable, 0, 2) );
	    								
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
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.FLINT, 26 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								
	    								// Slot 4
	    								merchantRecipeArray = new ArrayList<MerchantRecipe>();
	    								
	    								itemStackColorizable = new ItemStack(Items.LEATHER_HELMET);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 5 ), (ItemStack)null, itemStackColorizable, 0, 2) );
	    								
	    								itemStackColorizable = new ItemStack(Items.LEATHER_BOOTS);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 4 ), (ItemStack)null, itemStackColorizable, 0, 2) );
	    								
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
    	    						int cbs = 0; // Cumulative Block Size. If all the below trade blocks are true, this will be equal to the size of the trade block.
	    							if (
	    									hasSameItems(buyingList.get(Math.min(i+(cbs++), buyingList.size()-1)), Items.EMERALD, Items.SADDLE)
	    									) { // Trade block detected!
	    								
	    								// Add a new block with one or more trades
	    								int addToSlot = i; // This makes sure that subsequent trades get added rightward of each other
	    								
	    								
	    								// Slot 5
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.RABBIT_HIDE, 9 ), (ItemStack)null, new ItemStack( Items.EMERALD, 1 ), 0, 5
	    										));
	    								
	    								
	    								// Slot 6
	    								itemStackColorizable = new ItemStack(Items.LEATHER_CHESTPLATE);
	    								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
	    								enchantvalue = 5 + random.nextInt(15);
	    								buyingList.add(MathHelper.clamp_int(addToSlot++, 0, Math.max(buyingList.size()-1,0)), new MerchantRecipe(
	    										new ItemStack( Items.EMERALD, 7 ), (ItemStack)null,
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
		    					// TOTO - Leather Horse Armor (Added in 1.14)
		    					
		    					
		    					// INSTEAD, for levels 4 and 5, add a trade that was not previously added--and do so at an increased value to the player:
		    			    	ArrayList<MerchantRecipe> possibleExpertLeatherworkerTrades = new ArrayList<MerchantRecipe>();
		    			    	
								itemStackColorizable = new ItemStack(Items.LEATHER_LEGGINGS);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, Math.max(3 - 2*(ims.getProfessionLevel()-1), 1) ), (ItemStack)null, itemStackColorizable, 0, 2) );
								
								itemStackColorizable = new ItemStack(Items.LEATHER_CHESTPLATE);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, Math.max(7 - 2*(ims.getProfessionLevel()-1), 1) ), (ItemStack)null, itemStackColorizable, 0, 2) );
		    			    	
								itemStackColorizable = new ItemStack(Items.LEATHER_HELMET);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, Math.max(5 - 2*(ims.getProfessionLevel()-2), 1) ), (ItemStack)null, itemStackColorizable, 0, 2) );
								
								itemStackColorizable = new ItemStack(Items.LEATHER_BOOTS);
								itemStackColorizable = FunctionsVN.colorizeItemstack(itemStackColorizable, FunctionsVN.combineDyeColors(new int[]{random.nextInt(16), random.nextInt(16)}));
								possibleExpertLeatherworkerTrades.add(new MerchantRecipe( new ItemStack( Items.EMERALD, Math.max(4 - 2*(ims.getProfessionLevel()-2), 1) ), (ItemStack)null, itemStackColorizable, 0, 2) );
								
								
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
										new ItemStack( Items.EMERALD, 5 ), (ItemStack)null,
										EnchantmentHelper.addRandomEnchantment(random, itemStackColorizable, enchantvalue, allowTreasure ), 0, 2
												));
	    						
								merchantRecipeArray.add(new MerchantRecipe( new ItemStack( Items.EMERALD, 6 ), (ItemStack)null, new ItemStack( Items.SADDLE, 1 ), 0, 3) );
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
    	        merchantRecipeArray.add( new MerchantRecipe(
    					new ItemStack(ModItems.VILLAGE_BOOK, 1), (ItemStack)null,
    					bookWithName
    					, 0, 2
    					) );
    	        
        	}
        	else if (career==2) // Cartographer
        	{
        		// Potion IDs taken from https://www.minecraftinfo.com/IDList.htm
        		
        		// --- STRONGHOLD --- //
    			merchantRecipeArray.add( new MerchantRecipe(new ItemStack(ModItems.STRONGHOLD_BOOK, 1), (ItemStack)null,
    					new ItemStack(Items.ENDER_EYE, 2)
    					, 0, 2
    					) );
    			
    			// --- FORTRESS --- //
    			merchantRecipeArray.add( new MerchantRecipe( new ItemStack(ModItems.FORTRESS_BOOK, 1), (ItemStack)null,
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_FIRE_RESISTANCE) 
    					, 0, 2
    					) );
    			
    			// --- SWAMP HUT --- //
    			merchantRecipeArray.add( new MerchantRecipe( new ItemStack(ModItems.SWAMP_HUT_BOOK, 1), (ItemStack)null,
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.STRONG_HARMING) 
    					, 0, 2
    					) );
    			merchantRecipeArray.add( new MerchantRecipe( new ItemStack(ModItems.SWAMP_HUT_BOOK, 1), (ItemStack)null,
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_HEALING) 
    					, 0, 2
    					) );
    			
    			// --- MONUMENT --- //
    			merchantRecipeArray.add( new MerchantRecipe(new ItemStack(ModItems.MONUMENT_BOOK, 1), (ItemStack)null,
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_WATER_BREATHING) 
    					, 0, 2
    					) );
    			
    			// --- JUNGLE TEMPLE --- //
    			merchantRecipeArray.add( new MerchantRecipe(new ItemStack(ModItems.JUNGLE_TEMPLE_BOOK, 1), (ItemStack)null,
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.STRONG_STRENGTH) 
    					, 0, 2
    					) );
    			
    			// --- IGLOO --- //
    			MerchantRecipe iglooForGoldenApple = new MerchantRecipe(new ItemStack(ModItems.IGLOO_BOOK, 1), (ItemStack)null,
    					new ItemStack(Items.GOLDEN_APPLE, 1)
    					, 0, 2
    					);
    			//iglooForGoldenApple.increaseMaxTradeUses(-6);
    			merchantRecipeArray.add( iglooForGoldenApple );
    			
    			MerchantRecipe iglooForSplashWeakness = new MerchantRecipe(new ItemStack(ModItems.IGLOO_BOOK, 1), (ItemStack)null,
    					PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.LONG_WEAKNESS) 
    					, 0, 2
    					);
    			//iglooForSplashWeakness.increaseMaxTradeUses(-6);
    			merchantRecipeArray.add( iglooForSplashWeakness ); // Splash Weakness (3:00)
    			
    			// --- VILLAGE -- //
    			ItemStack tagWithName = new ItemStack(Items.NAME_TAG, 1).setStackDisplayName( NameGenerator.newRandomName("villager-angel-demon-dragon-goblin-golem")[2] );
    			tagWithName.setRepairCost(99);
    			merchantRecipeArray.add( new MerchantRecipe(
    					new ItemStack(ModItems.VILLAGE_BOOK, 1), (ItemStack)null,
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
        int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        ItemStack itemstack = Items.ENCHANTED_BOOK.getEnchantedItemStack(new EnchantmentData(enchantment, i));
        int emeraldprice = 2 + random.nextInt(5 + i * 10) + 3 * i;

        if (enchantment.isTreasureEnchantment()) {emeraldprice *= 2;}
        
        if (emeraldprice > 64) {emeraldprice = 64;}

        return (new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldprice), new ItemStack(Items.BOOK), itemstack, 0, tradeuses));
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
    
    
}
