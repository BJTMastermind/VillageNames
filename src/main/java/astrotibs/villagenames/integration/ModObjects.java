package astrotibs.villagenames.integration;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.village.StructureVillageVN;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

/**
 * A holder for string names for various mod items/blocks/etc for easy access
 */
// Added in v3.1trades
public class ModObjects {
	
	
    // ---------------- //
	// --- Entities --- //
    // ---------------- //
    
  	//public static final String GCAlienVillagerClass = "micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager";
  	public static final String MPKoentusVillagerClass = "stevekung.mods.moreplanets.moons.koentus.entities.EntityKoentusianVillager";
  	public static final String MPKoentusVillagerClassModern = "stevekung.mods.moreplanets.module.moons.koentus.entities.EntityKoentusianVillager";
  	public static final String MPFronosVillagerClass = "stevekung.mods.moreplanets.planets.fronos.entities.EntityFronosVillager";
  	public static final String MPFronosVillagerClassModern = "stevekung.mods.moreplanets.module.planets.fronos.entities.EntityFronosVillager";
  	
  	// Nibiru villager
  	public static final String MPNibiruVillagerClass = "stevekung.mods.moreplanets.planets.nibiru.entity.EntityNibiruVillager";
  	public static final String MPNibiruVillagerClassModern = "stevekung.mods.moreplanets.module.planets.nibiru.entity.EntityNibiruVillager";
  	
  	public static final String WitcheryGuardClass = "com.emoniph.witchery.entity.EntityVillageGuard";
  	//public static final String WitcheryVampireClass = "com.emoniph.witchery.entity.EntityVampire";
  	//public static final String WitcheryHobgoblinClass = "com.emoniph.witchery.entity.EntityGoblin";
  	//public static final String WitcheryHunterClass = "com.emoniph.witchery.entity.EntityWitchHunter";
  	
  	// Primitive Mobs class paths
  	public static final String PMTravelingMerchantClass = "net.daveyx0.primitivemobs.entity.passive.EntityTravelingMerchant"; //Counts as a Villager
  	public static final String PMLostMinerClass = "net.daveyx0.primitivemobs.entity.passive.EntityLostMiner"; //Counts as a Villager
  	public static final String PMSheepmanClass = "net.daveyx0.primitivemobs.entity.passive.EntitySheepman"; //Counts as a Villager
  	public static final String PMSheepmanSmithClass = "net.daveyx0.primitivemobs.entity.passive.EntitySheepmanSmith";
  	// Primitive Mobs unlocalized names
  	public static final String PMTravelingMerchantUnlocalized = "entity.primitivemobs.TravelingMerchant.name";
  	public static final String PMTravelingMerchantUnlocalizedModern = "entity.primitivemobs.traveling_merchant.name";
  	public static final String PMLostMinerUnlocalized = "entity.primitivemobs.LostMiner.name";
  	public static final String PMLostMinerUnlocalizedModern = "entity.primitivemobs.lost_miner.name";
  	public static final String PMSheepmanUnlocalized = "entity.primitivemobs.Sheepman.name";
  	public static final String PMSheepmanUnlocalizedModern = "entity.primitivemobs.sheepman.name";
  	public static final String PMSheepmanSmithUnlocalized = "entity.primitivemobs.SheepmanSmith.name";
	
	public static final String TQShopkeeperClass = "net.torocraft.toroquest.entities.EntityShopkeeper"; //Counts as a Villager
	public static final String TQFugitiveClass = "net.torocraft.toroquest.entities.EntityFugitive"; //Counts as a Villager
	public static final String TQGuardClass = "net.torocraft.toroquest.entities.EntityGuard";
	public static final String TQSentryClass = "net.torocraft.toroquest.entities.EntitySentry";
	public static final String TQVillageLordClass = "net.torocraft.toroquest.entities.EntityVillageLord";

	
	// Added in v3.1trades
 	
	// --------------------------------------------- //
	// --- Blocks and items reference for trades --- //
	// --------------------------------------------- //
	
	// Bark
	public static final String barkQu = "quark:bark";
	
	// Campfire
	public static final String campfireFMC = "futuremc:campfire";
	// Future Versions's Campfire sucks
	
	// Dye
	public static final String dyeBlueBOP = "biomesoplenty:blue_dye";
	public static final String dyeBrownBOP = "biomesoplenty:brown_dye";
	//public static final String dyeGreenBOP = "biomesoplenty:green_dye";
	public static final String dyeWhiteBOP = "biomesoplenty:white_dye";
	public static final String dyeBlackBOP = "biomesoplenty:black_dye";
	public static final String dyeFMC = "futuremc:dye"; // 0: white, 1: blue, 2: brown, 3: black
	public static final String dyeQuark = "quark:root_dye"; // 0: blue, 1: black, 2: white
	public static final String dyeBotania = "botania:dye"; // 0: white, 11: blue, 12: brown, 15: black
	
	
	// Kelp and Kelp Accessories
	public static final String kelpBOP = "biomesoplenty:seaweed";
	
	// Lantern
	public static final String lanternFMC = "futuremc:lantern";
	
	// Stripped log
	public static final String strippedLogOakFMC = "futuremc:stripped_oak_log";
	public static final String strippedLogSpruceFMC = "futuremc:stripped_spruce_log";
	public static final String strippedLogBirchFMC = "futuremc:stripped_birch_log";
	public static final String strippedLogJungleFMC = "futuremc:stripped_jungle_log";
	public static final String strippedLogAcaciaFMC = "futuremc:stripped_acacia_log";
	public static final String strippedLogDarkOakFMC = "futuremc:stripped_dark_oak_log";
	
	// Stripped Wood
	public static final String strippedWoodOakFMC = "futuremc:stripped_oak_wood";
	public static final String strippedWoodSpruceFMC = "futuremc:stripped_spruce_wood";
	public static final String strippedWoodBirchFMC = "futuremc:stripped_birch_wood";
	public static final String strippedWoodJungleFMC = "futuremc:stripped_jungle_wood";
	public static final String strippedWoodAcaciaFMC = "futuremc:stripped_acacia_wood";
	public static final String strippedWoodDarkOakFMC = "futuremc:stripped_dark_oak_wood";
	
	// Suspicious Stew
	public static final String suspiciousStewFMC = "futuremc:suspicious_stew";
	
	// Sweet Berries
	public static final String sweetBerriesFMC = "futuremc:sweet_berries";
	
	
	
	// --------------------------- //
	// --- Generator Functions --- //
	// --------------------------- //

	// Campfire
	/**
     * Give this method the orientation of the campfire and the base mode of the structure it's in,
     * and it'll give you back the required meta value for construction.
     * For relative orientations, use:
     * 
     * HANGING:
     * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
     */
	public static IBlockState chooseModCampfireBlock(int relativeOrientation, int horizIndex)
	{
		Block tryCampfire = Block.getBlockFromName(ModObjects.campfireFMC);
		
		int campfireMeta=0;
		
		if (tryCampfire!=null)
		{
			
    		switch (relativeOrientation)
    		{
    		case 0: // Facing away
    			campfireMeta = new int[]{4,5,6,7}[MathHelper.clamp(horizIndex,0,3)];
    		case 1: // Facing right
    			campfireMeta = new int[]{7,4,7,4}[MathHelper.clamp(horizIndex,0,3)];
    		case 2: // Facing you
    			campfireMeta = new int[]{6,7,4,5}[MathHelper.clamp(horizIndex,0,3)];
    		case 3: // Facing left
    			campfireMeta = new int[]{5,6,5,6}[MathHelper.clamp(horizIndex,0,3)];
    		}
			
			return tryCampfire.getStateFromMeta(campfireMeta);
		}
		
		return Blocks.TORCH.getStateFromMeta(0);
	}
	public static Item chooseModCampfireItem()
	{
		Item moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.campfireFMC));
		if (moditem != null) {return moditem;}
		
		return null;
	}
	
	
	// Dye
	public static ItemStack chooseModBlackDye()
	{
		String[] modprioritylist = GeneralConfig.modDye;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeFMC);
				if (moditem != null) {return new ItemStack(moditem, 1, 3);}
			}
			else if (mod.toLowerCase().equals("biomesoplenty"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlackBOP);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
			else if (mod.toLowerCase().equals("quark"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeQuark);
				if (moditem != null) {return new ItemStack(moditem, 1, 1);}
			}
			else if (mod.toLowerCase().equals("botania"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBotania);
				if (moditem != null) {return new ItemStack(moditem, 1, 15);}
			}
		}
		return null;
	}
	public static ItemStack chooseModBlueDye()
	{
		String[] modprioritylist = GeneralConfig.modDye;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeFMC);
				if (moditem != null) {return new ItemStack(moditem, 1, 1);}
			}
			else if (mod.toLowerCase().equals("biomesoplenty"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlueBOP);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
			else if (mod.toLowerCase().equals("quark"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeQuark);
				if (moditem != null) {return new ItemStack(moditem, 1, 0);}
			}
			else if (mod.toLowerCase().equals("botania"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBotania);
				if (moditem != null) {return new ItemStack(moditem, 1, 11);}
			}
		}
		return null;
	}
	public static ItemStack chooseModBrownDye()
	{
		String[] modprioritylist = GeneralConfig.modDye;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeFMC);
				if (moditem != null) {return new ItemStack(moditem, 1, 2);}
			}
			else if (mod.toLowerCase().equals("biomesoplenty"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBrownBOP);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
			// TODO - add Quark support if they decide to add brown dye
			else if (mod.toLowerCase().equals("botania"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBotania);
				if (moditem != null) {return new ItemStack(moditem, 1, 12);}
			}
		}
		return null;
	}
	public static ItemStack chooseModWhiteDye()
	{
		String[] modprioritylist = GeneralConfig.modDye;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeFMC);
				if (moditem != null) {return new ItemStack(moditem, 1, 0);}
			}
			else if (mod.toLowerCase().equals("biomesoplenty"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeWhiteBOP);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
			else if (mod.toLowerCase().equals("quark"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeQuark);
				if (moditem != null) {return new ItemStack(moditem, 1, 2);}
			}
			else if (mod.toLowerCase().equals("botania"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBotania);
				if (moditem != null) {return new ItemStack(moditem, 1, 0);}
			}
		}
		return null;
	}
	
	
	
	// Lantern
    public static IBlockState chooseModLanternBlockState(boolean isHanging)
    {
    	Block tryLantern = Block.getBlockFromName(ModObjects.lanternFMC);
    	if (tryLantern!=null) {return tryLantern.getStateFromMeta(isHanging? 0:1);} // 1 is hanging, 0 is sitting, but they need to be assigned in reverse for some reason
    	
    	// None are found, so return ordinary glowstone
    	return Blocks.GLOWSTONE.getDefaultState();
    }
    
	public static Item chooseModLanternItem()
	{
		Item moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.lanternFMC));
		if (moditem != null) {return moditem;}
		
		return null;
	}
	
	// Stripped log
	/**
	 * Materials are: 0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=darkoak
	 * Orientations are: 0=vertical, 1=east-west, 2=north-south
	 */
	public static IBlockState chooseModStrippedLog(int materialMeta, int orientation)
	{
		Block logBlock=null;
		
		switch (materialMeta)
		{
		case 0: logBlock = Block.getBlockFromName(ModObjects.strippedLogOakFMC); break;
		case 1: logBlock = Block.getBlockFromName(ModObjects.strippedLogSpruceFMC); break;
		case 2: logBlock = Block.getBlockFromName(ModObjects.strippedLogBirchFMC); break;
		case 3: logBlock = Block.getBlockFromName(ModObjects.strippedLogJungleFMC); break;
		case 4: logBlock = Block.getBlockFromName(ModObjects.strippedLogAcaciaFMC); break;
		case 5: logBlock = Block.getBlockFromName(ModObjects.strippedLogDarkOakFMC); break;
		}
		if (logBlock != null) {return logBlock.getStateFromMeta(orientation%3*4);}
		
		return null;
	}
	

	// Stripped wood
	/**
	 * Materials are: 0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=darkoak
	 * Orientations are: 0=vertical, 1=east-west, 2=north-south
	 */
	public static IBlockState chooseModStrippedWood(int materialMeta, int orientation)
	{
		Block logBlock=null;
		
		switch (materialMeta)
		{
		case 0: logBlock = Block.getBlockFromName(ModObjects.strippedWoodOakFMC); break;
		case 1: logBlock = Block.getBlockFromName(ModObjects.strippedWoodSpruceFMC); break;
		case 2: logBlock = Block.getBlockFromName(ModObjects.strippedWoodBirchFMC); break;
		case 3: logBlock = Block.getBlockFromName(ModObjects.strippedWoodJungleFMC); break;
		case 4: logBlock = Block.getBlockFromName(ModObjects.strippedWoodAcaciaFMC); break;
		case 5: logBlock = Block.getBlockFromName(ModObjects.strippedWoodDarkOakFMC); break;
		}
		if (logBlock != null) {return logBlock.getStateFromMeta(orientation%3*4);}
		
		return null;
	}
}
