package astrotibs.villagenames.integration;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.FunctionsVN;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	// The following were added to Harvestcraft in 1.12
	public static final String cropAgaveHC = "harvestcraft:pamagavecrop";
	public static final String cropAmaranthHC = "harvestcraft:pamamaranthcrop";
	public static final String cropArrowrootHC = "harvestcraft:pamarrowrootcrop";
	public static final String cropCassavaHC = "harvestcraft:pamcassavacrop";
	public static final String cropChickpeaHC = "harvestcraft:pamchickpeacrop";
	public static final String cropElderberryHC = "harvestcraft:pamelderberrycrop";
	public static final String cropFlaxHC = "harvestcraft:pamflaxcrop";
	public static final String cropGigapickleHC = "harvestcraft:pamgigapicklecrop";
	public static final String cropGreengrapeHC = "harvestcraft:pamgreengrapecrop";
	public static final String cropHuckleberryHC = "harvestcraft:pamhuckleberrycrop";
	public static final String cropJicamaHC = "harvestcraft:pamjicamacrop";
	public static final String cropJuniperHC = "harvestcraft:pamjuniperberrycrop";
	public static final String cropJuteHC = "harvestcraft:pamjutecrop";
	public static final String cropKaleHC = "harvestcraft:pamkalecrop";
	public static final String cropKenafHC = "harvestcraft:pamkenafcrop";
	public static final String cropKohlrabiHC = "harvestcraft:pamkohlrabicrop";
	public static final String cropLentilHC = "harvestcraft:pamlentilcrop";
	public static final String cropMilletHC = "harvestcraft:pammilletcrop";
	public static final String cropMulberryHC = "harvestcraft:pammulberrycrop";
	public static final String cropQuinoaHC = "harvestcraft:pamquinoacrop";
	public static final String cropSisalHC = "harvestcraft:pamsisalcrop";
	public static final String cropTaroHC = "harvestcraft:pamtarocrop";
	public static final String cropTomatilloHC = "harvestcraft:pamtomatillocrop";
	
	// Desk
	public static final String deskBC = "bibliocraft:desk";
	
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
	
	// Sandstone walls
	public static final String sandstoneWallQu = "quark:sandstone_wall";
	public static final String redSandstoneWallQu = "quark:red_sandstone_wall";
	
	// Smooth Sandstone Slab
	public static final String smoothSandstoneSlabQu = "quark:sandstone_smooth_slab";
	public static final String smoothRedSandstoneSlabQu = "quark:red_sandstone_smooth_slab";
	
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

	// Bark
	public static IBlockState chooseModBark(IBlockState blockstate)
	{
		if (blockstate.getBlock()==Blocks.LOG)
		{
			Block tryBark = Block.getBlockFromName(ModObjects.barkQu);
			
			if (tryBark != null) // EF bark exists
			{
				return tryBark.getStateFromMeta(blockstate.getBlock().getMetaFromState(blockstate)%4);
			}
		}
		else if (blockstate.getBlock()==Blocks.LOG2)
		{
			Block tryBark = Block.getBlockFromName(ModObjects.barkQu);
			
			if (tryBark != null) // EF bark exists
			{
				return tryBark.getStateFromMeta(blockstate.getBlock().getMetaFromState(blockstate)%4+4);
			}
		}
		
		// If the above is not allowed, return a vanilla log with meta 12+ to simulate bark
		if (blockstate.getBlock()==Blocks.LOG || blockstate.getBlock()==Blocks.LOG2)
		{
			return blockstate.getBlock().getStateFromMeta(12 + blockstate.getBlock().getMetaFromState(blockstate)%4);
		}
		
		// If it's not even a log, return it as normal
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
	
	
	// Sandstone Wall
	public static IBlockState chooseModSandstoneWall(boolean isRed)
	{
		Block modblock = Block.getBlockFromName(isRed ? ModObjects.redSandstoneWallQu : ModObjects.sandstoneWallQu);
		if (modblock != null) {return modblock.getDefaultState();}
		
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
		
		return (materialMeta<4 ? Blocks.LOG : Blocks.LOG2).getStateFromMeta(orientation*4+materialMeta%4);
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
		
		return (materialMeta<4 ? Blocks.LOG : Blocks.LOG2).getStateFromMeta(orientation*4+materialMeta%4);
	}
}
