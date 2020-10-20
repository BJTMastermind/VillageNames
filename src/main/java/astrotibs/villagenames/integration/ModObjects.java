package astrotibs.villagenames.integration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

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

	// --- Blocks ---//
 	
 	
	// Crops
	public static final String cropArtichokeHC = "harvestcraft:pamartichokecrop";
	public static final String cropAsparagusHC = "harvestcraft:pamasparaguscrop";
	public static final String cropBambooHC = "harvestcraft:pambambooshootcrop";
	public static final String cropBarleyHC = "harvestcraft:pambarleycrop";
	public static final String cropBeanHC = "harvestcraft:pambeancrop";
	public static final String cropBeetHC = "harvestcraft:pambeetcrop";
	public static final String cropBellpepperHC = "harvestcraft:pambellpeppercrop";
	public static final String cropBlackberryHC = "harvestcraft:pamblackberrycrop";
	public static final String cropBlueberryHC = "harvestcraft:pamblueberrycrop";
	public static final String cropBroccoliHC = "harvestcraft:pambroccolicrop";
	public static final String cropBrusselsproutHC = "harvestcraft:pambrusselsproutcrop";
	public static final String cropCabbageHC = "harvestcraft:pamcabbagecrop";
	public static final String cropCactusfruitHC = "harvestcraft:pamcactusfruitcrop"; // Planted on sand
	public static final String cropCandleberryHC = "harvestcraft:pamcandleberrycrop";
	public static final String cropCantaloupeHC = "harvestcraft:pamcantaloupecrop";
	public static final String cropCauliflowerHC = "harvestcraft:pamcauliflowercrop";
	public static final String cropCeleryHC = "harvestcraft:pamcelerycrop";
	public static final String cropChilipepperHC = "harvestcraft:pamchilipeppercrop";
	public static final String cropCoffeebeanHC = "harvestcraft:pamcoffeebeancrop";
	public static final String cropCornHC = "harvestcraft:pamcorncrop";
	public static final String cropCottonHC = "harvestcraft:pamcottoncrop";
	public static final String cropCranberryHC = "harvestcraft:pamcranberrycrop"; // Planted on water
	public static final String cropCucumberHC = "harvestcraft:pamcucumbercrop";
	public static final String cropCurryleafHC = "harvestcraft:pamcurryleafcrop";
	public static final String cropEggplantHC = "harvestcraft:pameggplantcrop";
	public static final String cropGarlicHC = "harvestcraft:pamgarliccrop";
	public static final String cropGingerHC = "harvestcraft:pamgingercrop";
	public static final String cropGrapeHC = "harvestcraft:pamgrapecrop";
	public static final String cropKiwiHC = "harvestcraft:pamkiwicrop";
	public static final String cropLeekHC = "harvestcraft:pamleekcrop";
	public static final String cropLettuceHC = "harvestcraft:pamlettucecrop";
	public static final String cropMustardseedHC = "harvestcraft:pammustardseedscrop";
	public static final String cropOatsHC = "harvestcraft:pamoatscrop";
	public static final String cropOkraHC = "harvestcraft:pamokracrop";
	public static final String cropOnionHC = "harvestcraft:pamonioncrop";
	public static final String cropParsnipHC = "harvestcraft:pamparsnipcrop";
	public static final String cropPeanutHC = "harvestcraft:pampeanutcrop";
	public static final String cropPeasHC = "harvestcraft:pampeascrop";
	public static final String cropPineappleHC = "harvestcraft:pampineapplecrop";
	public static final String cropRadishHC = "harvestcraft:pamradishcrop";
	public static final String cropRaspberryHC = "harvestcraft:pamraspberrycrop";
	public static final String cropRhubarbHC = "harvestcraft:pamrhubarbcrop";
	public static final String cropRiceHC = "harvestcraft:pamricecrop"; // Planted on water
	public static final String cropRutabegaHC = "harvestcraft:pamrutabagacrop";
	public static final String cropRyeHC = "harvestcraft:pamryecrop";
	public static final String cropScallionHC = "harvestcraft:pamscallioncrop";
	public static final String cropSeaweedHC = "harvestcraft:pamseaweedcrop"; // Planted on water
	public static final String cropSesameseedHC = "harvestcraft:pamsesameseedscrop";
	public static final String cropSoybeanHC = "harvestcraft:pamsoybeancrop";
	public static final String cropSpiceleafHC = "harvestcraft:pamspiceleafcrop";
	public static final String cropSpinachHC = "harvestcraft:pamspinachcrop";
	public static final String cropStrawberryHC = "harvestcraft:pamstrawberrycrop";
	public static final String cropSweetpotatoHC = "harvestcraft:pamsweetpotatocrop";
	public static final String cropTealeafHC = "harvestcraft:pamtealeafcrop";
	public static final String cropTomatoHC = "harvestcraft:pamtomatocrop";
	public static final String cropTurnipHC = "harvestcraft:pamturnipcrop";
	public static final String cropWaterchestnutHC = "harvestcraft:pamwaterchestnutcrop"; // Planted on water
	public static final String cropWhitemushroomHC = "harvestcraft:pamwhitemushroomcrop"; // Planted on log
	public static final String cropWintersquashHC = "harvestcraft:pamwintersquashcrop";
	public static final String cropZucchiniHC = "harvestcraft:pamzucchinicrop";
	public static final String cropKaleJAFFA = "jaffa:kaleCrop";
	
	// Mud
	public static final String mudBOP_classPath = "biomesoplenty.common.block.BlockBOPMud";
	
	
	// --- Items --- //
	
	// Desk
	public static final String deskBC = "bibliocraft:Desk";
	
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
		if (blockstate.getBlock()==Blocks.LOG || blockstate.getBlock()==Blocks.LOG2)
		{
			return blockstate.getBlock().getStateFromMeta(12 + blockstate.getBlock().getMetaFromState(blockstate)%4);
		}
		
		return blockstate;
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
	public static IBlockState chooseModCampfireBlockState(int relativeOrientation, EnumFacing coordBaseMode)
	{
		// No mod campfires exist. Return an upright torch.
		return Blocks.TORCH.getDefaultState();
	}
	
	// Lantern
    public static IBlockState chooseModLanternBlockState(boolean isHanging)
    {
    	// No mod lanterns exist. Return a glowstone block.
    	return Blocks.GLOWSTONE.getDefaultState();
    }
	
	// Stripped log
	/**
	 * Materials are: 0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=darkoak
	 * Orientations are: 0=vertical, 1=east-west, 2=north-south
	 */
	public static IBlockState chooseModStrippedLogState(int materialMeta, int orientation)
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
		return (materialMeta<4 ? Blocks.LOG : Blocks.LOG2).getStateFromMeta(orientation*4+materialMeta%4);
	}
	

	// Stripped wood
	/**
	 * Materials are: 0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=darkoak
	 * Orientations are: 0=vertical, 1=east-west, 2=north-south
	 */
	public static IBlockState chooseModStrippedWoodState(int materialMeta, int orientation)
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
		return (materialMeta<4 ? Blocks.LOG : Blocks.LOG2).getStateFromMeta(orientation*4+materialMeta%4);
	}
}
