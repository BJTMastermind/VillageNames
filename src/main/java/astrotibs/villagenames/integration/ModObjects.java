package astrotibs.villagenames.integration;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.FunctionsVN;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
 	
 	

	// Added in v3.1trades
 	
	// --------------------------------------------- //
	// --- Blocks and items reference for trades --- //
	// --------------------------------------------- //

	// --- Blocks ---//
	
	
	// --- Items --- //
	
	// Beetroot
	public static final String beetrootSeedSB = "samsbeetroot:beetroot_seed";
	public static final String beetrootItemSB = "samsbeetroot:beetroot_item";
	public static final String beetrootSoupSB = "samsbeetroot:beetroot_soup";
	
	// Dye
	public static final String dyeBlueBOP = "biomesoplenty:blue_dye";
	public static final String dyeBrownBOP = "biomesoplenty:brown_dye";
	public static final String dyeGreenBOP = "biomesoplenty:green_dye";
	public static final String dyeWhiteBOP = "biomesoplenty:white_dye";
	public static final String dyeBlackBOP = "biomesoplenty:black_dye";

	// Kelp and Kelp Accessories
	public static final String kelpBOP = "biomesoplenty:seaweed";
	

	
	
	// --------------------------- //
	// --- Generator Functions --- //
	// --------------------------- //
	
	// Bark
	public static IBlockState chooseModBark(IBlockState blockstate)
	{
		if (blockstate.getBlock()==Blocks.log || blockstate.getBlock()==Blocks.log2)
		{
			return blockstate.getBlock().getStateFromMeta(12 + blockstate.getBlock().getMetaFromState(blockstate)%4);
		}
		
		return blockstate;
	}
	
	
	// Barrel
	public static ItemStack chooseModBarrelItem()
	{
		return null;
	}
	// Uses furnace metas. 1 is vertical, and horizontal are 2, 3, 4, 5. 0 is inverted
	public static IBlockState chooseModBarrelBlockState()
	{
		return null;
	}
	
	
	// Beetroot Seeds
	public static ItemStack chooseModBeetrootSeeds()
	{
		Item moditem = FunctionsVN.getItemFromName(ModObjects.beetrootSeedSB);
		if (moditem != null) {return new ItemStack(moditem, 1);}
		
		return null;
	}
	
	// Beetroot Soup
	public static ItemStack chooseModBeetrootSoup()
	{
		Item moditem = FunctionsVN.getItemFromName(ModObjects.beetrootSoupSB);
		if (moditem != null) {return new ItemStack(moditem, 1);}
		
		return null;
	}
	
	// Concrete
	public static IBlockState chooseModConcrete(int color)
	{
		if (GeneralConfig.addConcrete)
		{
			return ModBlocksVN.blockConcrete.getStateFromMeta(color);
		}
		return null;
	}
	
	// Campfire
	/**
     * Give this method the orientation of the campfire and the base mode of the structure it's in,
     * and it'll give you back the required meta value for construction.
     * For relative orientations, use:
     * 
     * HANGING:
     * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
     *   
     * STANDING:
     * 0=fore-facing (away from you); 4=right-facing; 8=back-facing (toward you); 12=left-facing
     */
	public static IBlockState chooseModCampfireBlockState(int relativeOrientation, int horizIndex)
	{
		// No mod campfires exist. Return an upright torch.
		return Blocks.torch.getDefaultState();
	}
	
	// Lantern
    public static IBlockState chooseModLanternBlockState(boolean isHanging)
    {
    	// No mod lanterns exist. Return a glowstone block.
    	return Blocks.glowstone.getDefaultState();
    }
	
	// Glazed Terracotta
	public static IBlockState chooseModGlazedTerracotta(int colorMeta, int facingMeta)
	{
		if (GeneralConfig.addConcrete)
		{
			return FunctionsVN.getGlazedTerracotaFromMetas(colorMeta, facingMeta);
		}
		return null;
	}
	
	// Grass Path block if able; returns Gravel otherwise.
	public static IBlockState chooseModPathBlock()
	{
		return Blocks.gravel.getDefaultState();
	}
	
	
	// Iron Nugget
	public static ItemStack chooseModIronNugget()
	{
		return null;
	}

	
	
	// Sign
	public static ItemStack chooseModWoodenSignItem(int materialMeta)
	{
		// If all else fails, grab the vanilla version
		return new ItemStack(Items.sign, 1);
	}
	
	
	// Smooth Stone
	public static IBlockState chooseModSmoothStoneBlockState()
	{
		return Blocks.double_stone_slab.getStateFromMeta(8);
	}
	
	
	// Stripped log
	/**
	 * Materials are: 0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=darkoak
	 * Orientations are: 0=vertical, 1=east-west, 2=north-south
	 */
	public static IBlockState chooseModStrippedLog(int materialMeta, int orientation)
	{
		/*
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
		*/
		return (materialMeta<4 ? Blocks.log : Blocks.log2).getStateFromMeta(orientation*4+materialMeta%4);
	}
	

	// Stripped wood
	/**
	 * Materials are: 0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=darkoak
	 * Orientations are: 0=vertical, 1=east-west, 2=north-south
	 */
	public static IBlockState chooseModStrippedWood(int materialMeta, int orientation)
	{
		/*
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
		*/
		return (materialMeta<4 ? Blocks.log : Blocks.log2).getStateFromMeta(orientation*4+materialMeta%4);
	}
	
	
	// Sweet Berries
	public static ItemStack chooseModSweetBerriesItem()
	{
		return null;
	}
}
