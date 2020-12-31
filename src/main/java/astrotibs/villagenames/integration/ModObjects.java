package astrotibs.villagenames.integration;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.village.StructureVillageVN;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * A holder for string names for various mod items/blocks/etc for easy access
 */
// Added in v3.1trades
public class ModObjects {
	
	// Constantly referenced domain names
	public static final String DOM_BIOMESOPLENTY = "biomesoplenty";
	public static final String DOM_FUTUREMC = "futuremc";
	public static final String DOM_HARVESTCRAFT = "harvestcraft";
	public static final String DOM_QUARK = "quark";
	public static final String DOM_VANILLABUILDERSEXTENSION = "vbe";
	
	
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
	public static final String barkQu = DOM_QUARK + ":bark";
	
	// Barrel
	public static final String barrelFMC = DOM_FUTUREMC + ":barrel";

	// Bell
	public static final String bellFMC = DOM_FUTUREMC + ":bell";
	
	// Blast Furnace
	public static final String blastFurnaceFMC = DOM_FUTUREMC + ":blast_furnace";
	
	// Blue Ice
	public static final String blueIceFMC = DOM_FUTUREMC + ":blue_ice";
	
	// Buttons
	public static final String buttonSpruceQu = DOM_QUARK + ":spruce_button";
	public static final String buttonBirchQu = DOM_QUARK + ":birch_button";
	public static final String buttonJungleQu = DOM_QUARK + ":jungle_button";
	public static final String buttonAcaciaQu = DOM_QUARK + ":acacia_button";
	public static final String buttonDarkOakQu = DOM_QUARK + ":dark_oak_button";
	
	// Campfire
	public static final String campfireFMC = DOM_FUTUREMC + ":campfire";
	public static final String campfireTAN = "toughasnails:campfire";
	// Future Versions's Campfire sucks

	// Cartography Table
	public static final String cartographyTableFMC = DOM_FUTUREMC + ":cartography_table";
	
	// Composter
	public static final String composterFMC = DOM_FUTUREMC + ":composter";
	
	// Crops
	public static final String cropArtichokeHC = DOM_HARVESTCRAFT + ":pamartichokecrop";
	public static final String cropAsparagusHC = DOM_HARVESTCRAFT + ":pamasparaguscrop";
	public static final String cropBambooHC = DOM_HARVESTCRAFT + ":pambambooshootcrop";
	public static final String cropBarleyHC = DOM_HARVESTCRAFT + ":pambarleycrop";
	public static final String cropBeanHC = DOM_HARVESTCRAFT + ":pambeancrop";
	public static final String cropBeetHC = DOM_HARVESTCRAFT + ":pambeetcrop";
	public static final String cropBellpepperHC = DOM_HARVESTCRAFT + ":pambellpeppercrop";
	public static final String cropBlackberryHC = DOM_HARVESTCRAFT + ":pamblackberrycrop";
	public static final String cropBlueberryHC = DOM_HARVESTCRAFT + ":pamblueberrycrop";
	public static final String cropBroccoliHC = DOM_HARVESTCRAFT + ":pambroccolicrop";
	public static final String cropBrusselsproutHC = DOM_HARVESTCRAFT + ":pambrusselsproutcrop";
	public static final String cropCabbageHC = DOM_HARVESTCRAFT + ":pamcabbagecrop";
	public static final String cropCactusfruitHC = DOM_HARVESTCRAFT + ":pamcactusfruitcrop"; // Planted on sand
	public static final String cropCandleberryHC = DOM_HARVESTCRAFT + ":pamcandleberrycrop";
	public static final String cropCantaloupeHC = DOM_HARVESTCRAFT + ":pamcantaloupecrop";
	public static final String cropCauliflowerHC = DOM_HARVESTCRAFT + ":pamcauliflowercrop";
	public static final String cropCeleryHC = DOM_HARVESTCRAFT + ":pamcelerycrop";
	public static final String cropChilipepperHC = DOM_HARVESTCRAFT + ":pamchilipeppercrop";
	public static final String cropCoffeebeanHC = DOM_HARVESTCRAFT + ":pamcoffeebeancrop";
	public static final String cropCornHC = DOM_HARVESTCRAFT + ":pamcorncrop";
	public static final String cropCottonHC = DOM_HARVESTCRAFT + ":pamcottoncrop";
	public static final String cropCranberryHC = DOM_HARVESTCRAFT + ":pamcranberrycrop"; // Planted on water
	public static final String cropCucumberHC = DOM_HARVESTCRAFT + ":pamcucumbercrop";
	public static final String cropCurryleafHC = DOM_HARVESTCRAFT + ":pamcurryleafcrop";
	public static final String cropEggplantHC = DOM_HARVESTCRAFT + ":pameggplantcrop";
	public static final String cropGarlicHC = DOM_HARVESTCRAFT + ":pamgarliccrop";
	public static final String cropGingerHC = DOM_HARVESTCRAFT + ":pamgingercrop";
	public static final String cropGrapeHC = DOM_HARVESTCRAFT + ":pamgrapecrop";
	public static final String cropKiwiHC = DOM_HARVESTCRAFT + ":pamkiwicrop";
	public static final String cropLeekHC = DOM_HARVESTCRAFT + ":pamleekcrop";
	public static final String cropLettuceHC = DOM_HARVESTCRAFT + ":pamlettucecrop";
	public static final String cropMustardseedHC = DOM_HARVESTCRAFT + ":pammustardseedscrop";
	public static final String cropOatsHC = DOM_HARVESTCRAFT + ":pamoatscrop";
	public static final String cropOkraHC = DOM_HARVESTCRAFT + ":pamokracrop";
	public static final String cropOnionHC = DOM_HARVESTCRAFT + ":pamonioncrop";
	public static final String cropParsnipHC = DOM_HARVESTCRAFT + ":pamparsnipcrop";
	public static final String cropPeanutHC = DOM_HARVESTCRAFT + ":pampeanutcrop";
	public static final String cropPeasHC = DOM_HARVESTCRAFT + ":pampeascrop";
	public static final String cropPineappleHC = DOM_HARVESTCRAFT + ":pampineapplecrop";
	public static final String cropRadishHC = DOM_HARVESTCRAFT + ":pamradishcrop";
	public static final String cropRaspberryHC = DOM_HARVESTCRAFT + ":pamraspberrycrop";
	public static final String cropRhubarbHC = DOM_HARVESTCRAFT + ":pamrhubarbcrop";
	public static final String cropRiceHC = DOM_HARVESTCRAFT + ":pamricecrop"; // Planted on water
	public static final String cropRutabegaHC = DOM_HARVESTCRAFT + ":pamrutabagacrop";
	public static final String cropRyeHC = DOM_HARVESTCRAFT + ":pamryecrop";
	public static final String cropScallionHC = DOM_HARVESTCRAFT + ":pamscallioncrop";
	public static final String cropSeaweedHC = DOM_HARVESTCRAFT + ":pamseaweedcrop"; // Planted on water
	public static final String cropSesameseedHC = DOM_HARVESTCRAFT + ":pamsesameseedscrop";
	public static final String cropSoybeanHC = DOM_HARVESTCRAFT + ":pamsoybeancrop";
	public static final String cropSpiceleafHC = DOM_HARVESTCRAFT + ":pamspiceleafcrop";
	public static final String cropSpinachHC = DOM_HARVESTCRAFT + ":pamspinachcrop";
	public static final String cropStrawberryHC = DOM_HARVESTCRAFT + ":pamstrawberrycrop";
	public static final String cropSweetpotatoHC = DOM_HARVESTCRAFT + ":pamsweetpotatocrop";
	public static final String cropTealeafHC = DOM_HARVESTCRAFT + ":pamtealeafcrop";
	public static final String cropTomatoHC = DOM_HARVESTCRAFT + ":pamtomatocrop";
	public static final String cropTurnipHC = DOM_HARVESTCRAFT + ":pamturnipcrop";
	public static final String cropWaterchestnutHC = DOM_HARVESTCRAFT + ":pamwaterchestnutcrop"; // Planted on water
	public static final String cropWhitemushroomHC = DOM_HARVESTCRAFT + ":pamwhitemushroomcrop"; // Planted on log
	public static final String cropWintersquashHC = DOM_HARVESTCRAFT + ":pamwintersquashcrop";
	public static final String cropZucchiniHC = DOM_HARVESTCRAFT + ":pamzucchinicrop";
	// The following were added to Harvestcraft in 1.12
	public static final String cropAgaveHC = DOM_HARVESTCRAFT + ":pamagavecrop";
	public static final String cropAmaranthHC = DOM_HARVESTCRAFT + ":pamamaranthcrop";
	public static final String cropArrowrootHC = DOM_HARVESTCRAFT + ":pamarrowrootcrop";
	public static final String cropCassavaHC = DOM_HARVESTCRAFT + ":pamcassavacrop";
	public static final String cropChickpeaHC = DOM_HARVESTCRAFT + ":pamchickpeacrop";
	public static final String cropElderberryHC = DOM_HARVESTCRAFT + ":pamelderberrycrop";
	public static final String cropFlaxHC = DOM_HARVESTCRAFT + ":pamflaxcrop";
	public static final String cropGigapickleHC = DOM_HARVESTCRAFT + ":pamgigapicklecrop";
	public static final String cropGreengrapeHC = DOM_HARVESTCRAFT + ":pamgreengrapecrop";
	public static final String cropHuckleberryHC = DOM_HARVESTCRAFT + ":pamhuckleberrycrop";
	public static final String cropJicamaHC = DOM_HARVESTCRAFT + ":pamjicamacrop";
	public static final String cropJuniperHC = DOM_HARVESTCRAFT + ":pamjuniperberrycrop";
	public static final String cropJuteHC = DOM_HARVESTCRAFT + ":pamjutecrop";
	public static final String cropKaleHC = DOM_HARVESTCRAFT + ":pamkalecrop";
	public static final String cropKenafHC = DOM_HARVESTCRAFT + ":pamkenafcrop";
	public static final String cropKohlrabiHC = DOM_HARVESTCRAFT + ":pamkohlrabicrop";
	public static final String cropLentilHC = DOM_HARVESTCRAFT + ":pamlentilcrop";
	public static final String cropMilletHC = DOM_HARVESTCRAFT + ":pammilletcrop";
	public static final String cropMulberryHC = DOM_HARVESTCRAFT + ":pammulberrycrop";
	public static final String cropQuinoaHC = DOM_HARVESTCRAFT + ":pamquinoacrop";
	public static final String cropSisalHC = DOM_HARVESTCRAFT + ":pamsisalcrop";
	public static final String cropTaroHC = DOM_HARVESTCRAFT + ":pamtarocrop";
	public static final String cropTomatilloHC = DOM_HARVESTCRAFT + ":pamtomatillocrop";
	
	// Desk
	public static final String deskBC = "bibliocraft:desk";
	
	// Dye
	public static final String dyeBlueBOP = DOM_BIOMESOPLENTY + ":blue_dye";
	public static final String dyeBrownBOP = DOM_BIOMESOPLENTY + ":brown_dye";
	//public static final String dyeGreenBOP = DOM_BIOMESOPLENTY + ":green_dye";
	public static final String dyeWhiteBOP = DOM_BIOMESOPLENTY + ":white_dye";
	public static final String dyeBlackBOP = DOM_BIOMESOPLENTY + ":black_dye";
	public static final String dyeFMC = DOM_FUTUREMC + ":dye"; // 0: white, 1: blue, 2: brown, 3: black
	public static final String dyeQuark = DOM_QUARK + ":root_dye"; // 0: blue, 1: black, 2: white
	public static final String dyeBotania = "botania:dye"; // 0: white, 11: blue, 12: brown, 15: black

	// Fletching Table
	public static final String fletchingTableFMC = DOM_FUTUREMC + ":fletching_table";
	
	// Grindstone
	public static final String grindstoneFMC = DOM_FUTUREMC + ":grindstone";
	
	// Kelp and Kelp Accessories
	public static final String kelpBOP = DOM_BIOMESOPLENTY + ":seaweed";
	
	// Lantern
	public static final String lanternFMC = DOM_FUTUREMC + ":lantern";
	public static final String lanternCh = "charm:iron_lantern";
	
	// Loom
	public static final String loomFMC = DOM_FUTUREMC + ":loom";
	
	// Mud
	public static final String mudBOP_classPath = "biomesoplenty.common.block.BlockBOPMud";

	// Pressure Plates
	public static final String pressurePlateSpruceQu = DOM_QUARK + ":spruce_pressure_plate";
	public static final String pressurePlateBirchQu = DOM_QUARK + ":birch_pressure_plate";
	public static final String pressurePlateJungleQu = DOM_QUARK + ":jungle_pressure_plate";
	public static final String pressurePlateAcaciaQu = DOM_QUARK + ":acacia_pressure_plate";
	public static final String pressurePlateDarkOakQu = DOM_QUARK + ":dark_oak_pressure_plate";
	
	// Smithing Table
	public static final String smithingTableFMC = DOM_FUTUREMC + ":smithing_table";
	
	// Smoker
	public static final String smokerFMC = DOM_FUTUREMC + ":smoker";
	
	// Smooth Sandstone
	public static final String smoothSandstone_white_Qu = DOM_FUTUREMC + ":smooth_sandstone";
	public static final String smoothSandstone_red_Qu = DOM_FUTUREMC + ":smooth_red_sandstone";
	public static final String smoothSandstone_white_FMC = DOM_FUTUREMC + ":smooth_sandstone";
	public static final String smoothSandstone_red_FMC = DOM_FUTUREMC + ":smooth_red_sandstone";
	
	// Smooth Sandstone Slab
	public static final String smoothSandstoneSlabQu = DOM_QUARK + ":sandstone_smooth_slab";
	public static final String smoothRedSandstoneSlabQu = DOM_QUARK + ":red_sandstone_smooth_slab";
	
	// Smooth Stone 
	public static final String smoothStoneQu = DOM_QUARK + ":polished_stone";
	public static final String smoothStoneFMC = DOM_FUTUREMC + ":smooth_stone";
	
	// Stairs
	public static final String dioriteStairs_Qu = DOM_QUARK + ":stone_diorite_stairs";
	public static final String dioriteStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsdiorite";
	public static final String graniteStairs_Qu = DOM_QUARK + ":stone_granite_stairs";
	public static final String graniteStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsgranite";
	public static final String smoothSandstoneStairs_white_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairssandstonesmooth";
	public static final String smoothSandstoneStairs_red_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsredsandstonesmooth";
	
	// Stone cutter
	public static final String stoneCutterFMC = DOM_FUTUREMC + ":stonecutter";
	
	// Stripped log
	public static final String strippedLogOakFMC = DOM_FUTUREMC + ":stripped_oak_log";
	public static final String strippedLogSpruceFMC = DOM_FUTUREMC + ":stripped_spruce_log";
	public static final String strippedLogBirchFMC = DOM_FUTUREMC + ":stripped_birch_log";
	public static final String strippedLogJungleFMC = DOM_FUTUREMC + ":stripped_jungle_log";
	public static final String strippedLogAcaciaFMC = DOM_FUTUREMC + ":stripped_acacia_log";
	public static final String strippedLogDarkOakFMC = DOM_FUTUREMC + ":stripped_dark_oak_log";
	
	// Stripped Wood
	public static final String strippedWoodOakFMC = DOM_FUTUREMC + ":stripped_oak_wood";
	public static final String strippedWoodSpruceFMC = DOM_FUTUREMC + ":stripped_spruce_wood";
	public static final String strippedWoodBirchFMC = DOM_FUTUREMC + ":stripped_birch_wood";
	public static final String strippedWoodJungleFMC = DOM_FUTUREMC + ":stripped_jungle_wood";
	public static final String strippedWoodAcaciaFMC = DOM_FUTUREMC + ":stripped_acacia_wood";
	public static final String strippedWoodDarkOakFMC = DOM_FUTUREMC + ":stripped_dark_oak_wood";
	
	// Suspicious Stew
	public static final String suspiciousStewFMC = DOM_FUTUREMC + ":suspicious_stew";
	
	// Sweet Berries
	public static final String sweetBerriesFMC = DOM_FUTUREMC + ":sweet_berries";

	// Trapdoor
	public static final String trapdoorSpruceQu = DOM_QUARK + ":spruce_trapdoor";
	public static final String trapdoorBirchQu = DOM_QUARK + ":birch_trapdoor";
	public static final String trapdoorJungleQu = DOM_QUARK + ":jungle_trapdoor";
	public static final String trapdoorAcaciaQu = DOM_QUARK + ":acacia_trapdoor";
	public static final String trapdoorDarkOakQu = DOM_QUARK + ":dark_oak_trapdoor";

	// Walls
	public static final String sandstoneWall_white_Qu = DOM_QUARK + ":sandstone_wall";
	public static final String sandstoneWall_white_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallsandstone";
	public static final String sandstoneWall_white_FMC = DOM_FUTUREMC + ":sandstone_wall"; 
	public static final String sandstoneWall_red_Qu = DOM_QUARK + ":red_sandstone_wall";
	public static final String sandstoneWall_red_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallredsandstone";
	public static final String sandstoneWall_red_FMC = DOM_FUTUREMC + ":red_sandstone_wall"; 
	public static final String dioriteWall_Qu = DOM_QUARK + ":stone_diorite_wall";
	public static final String dioriteWall_VBE = DOM_VANILLABUILDERSEXTENSION + ":walldiorite";
	public static final String graniteWall_Qu = DOM_QUARK + ":stone_granite_wall";
	public static final String graniteWall_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallgranite";
	
	
	// --------------------------- //
	// --- Generator Functions --- //
	// --------------------------- //

	
	// Barrel
	public static ItemStack chooseModBarrelItem()
	{
		Block modobject = Block.getBlockFromName(ModObjects.barrelFMC);
		if (modobject != null) {return new ItemStack(modobject);}
		
		return null;
	}
	// Uses furnace metas. 1 is vertical, and horizontal are 2, 3, 4, 5. 0 is inverted
	public static Block chooseModBarrelBlockState()
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.barrelFMC);
		if (modobject != null) {return modobject;}
		
		return null;
	}
	
	
	// Bark
	public static IBlockState chooseModBarkState(IBlockState blockstate)
	{
		Block tryBark;
		boolean returnVanilla = false;
		
		if (blockstate.getBlock()==Blocks.LOG)
		{
			tryBark = Block.getBlockFromName(ModObjects.barkQu);
			if (tryBark != null) {return tryBark.getStateFromMeta(blockstate.getBlock().getMetaFromState(blockstate)%4);}
			else {returnVanilla = true;}
		}
		else if (blockstate.getBlock()==Blocks.LOG2)
		{
			tryBark = Block.getBlockFromName(ModObjects.barkQu);
			if (tryBark != null) {return tryBark.getStateFromMeta(blockstate.getBlock().getMetaFromState(blockstate)%4 + 4);}
			else {returnVanilla = true;}
		}
		
		
		if (returnVanilla) {return blockstate.getBlock().getStateFromMeta(12 + blockstate.getBlock().getMetaFromState(blockstate)%4);}
		
		return blockstate;
	}
	
	// Bed
	public static void setModBedBlock(World world, int x, int y, int z, int orientationMeta, int colorMeta)
	{
		Block block = Blocks.BED;
		BlockPos pos = new BlockPos(x,y,z);
		
		// Set the bed block and metadata here
		world.setBlockState(pos, block.getStateFromMeta(orientationMeta));
		
		// Set the tile entity so that you can assign the color via NBT 
		NBTTagCompound bedNBT = new NBTTagCompound();
    	TileEntity tileentity = world.getTileEntity(pos);
    	if (!(tileentity instanceof TileEntityBed)) {return;}
    	// If the entity is a bed, set its color info
    	tileentity.writeToNBT(bedNBT);
    	bedNBT.setInteger("color", colorMeta);
    	tileentity.readFromNBT(bedNBT);
    	world.setTileEntity(pos, tileentity);
	}
	
	
	// Blast Furnace
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModBlastFurnaceState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		Block modobject = Block.getBlockFromName(ModObjects.blastFurnaceFMC);
		int meta;
		
    	if (modobject == null) {modobject = Blocks.FURNACE; meta = StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode);}
    	else {meta = StructureVillageVN.chooseBlastFurnaceMeta(furnaceOrientation, coordBaseMode);}

		return modobject.getStateFromMeta(meta);
	}
	
	
	// Blue Ice
	// Added in 1.13
    public static IBlockState chooseModBlueIceBlockState()
    {
		Block modobject = Block.getBlockFromName(ModObjects.blueIceFMC);
    	// None are found, so return ordinary packed ice
		if (modobject == null) {modobject = Blocks.PACKED_ICE;}
		
		return modobject.getDefaultState();
    }
	
    
    // Button
    public static Block chooseWoodenButton(int materialMeta)
	{
    	if (materialMeta==0) {return Blocks.WOODEN_BUTTON;}
    	
    	Block modblock=null;
    	switch (materialMeta)
		{
			case 1: modblock = Block.getBlockFromName(ModObjects.buttonSpruceQu); break;
			case 2: modblock = Block.getBlockFromName(ModObjects.buttonBirchQu); break;
			case 3: modblock = Block.getBlockFromName(ModObjects.buttonJungleQu); break;
			case 4: modblock = Block.getBlockFromName(ModObjects.buttonAcaciaQu); break;
			case 5: modblock = Block.getBlockFromName(ModObjects.buttonDarkOakQu); break;
		}
		if (modblock != null) {return modblock;}
		
		return Blocks.WOODEN_BUTTON;
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
	public static IBlockState chooseModCampfireBlockState(int relativeOrientation, EnumFacing coordBaseMode)
	{
    	String[] modprioritylist = GeneralConfig.modCampfire;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("futuremc"))
			{
				modblock = Block.getBlockFromName(ModObjects.campfireFMC);
				
				if (modblock!=null)
				{
					int horizIndex = coordBaseMode.getHorizontalIndex();
					int campfireMeta=0;
					
		    		switch (relativeOrientation)
		    		{
		    		case 0: // Facing away
		    			campfireMeta = new int[]{4,5,6,7}[MathHelper.clamp(horizIndex,0,3)]; break;
		    		case 1: // Facing right
		    			campfireMeta = new int[]{7,4,7,4}[MathHelper.clamp(horizIndex,0,3)]; break;
		    		case 2: // Facing you
		    			campfireMeta = new int[]{6,7,4,5}[MathHelper.clamp(horizIndex,0,3)]; break;
		    		case 3: // Facing left
		    			campfireMeta = new int[]{5,6,5,6}[MathHelper.clamp(horizIndex,0,3)]; break;
		    		}
					
					return modblock.getStateFromMeta(campfireMeta);
				}
			}
			else if (mod.toLowerCase().equals("toughasnails"))
			{
				modblock = Block.getBlockFromName(ModObjects.campfireTAN);
				if (modblock != null) {return modblock.getStateFromMeta(1);} // 1 is "lit"
			}
		}
    	
		// Return upright torch by default
		return Blocks.TORCH.getStateFromMeta(0);
	}
	public static Item chooseModCampfireItem()
	{
    	String[] modprioritylist = GeneralConfig.modCampfire;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().equals("futuremc"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.campfireFMC));
				if (moditem != null) {return moditem;}
			}
			else if (mod.toLowerCase().equals("toughasnails"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.campfireTAN));
				if (moditem != null) {return moditem;}
			}
		}
		
		return null;
	}
	
	
	// Cartography Table
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModCartographyTableState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		Block modobject = Block.getBlockFromName(ModObjects.cartographyTableFMC);
		int meta;
		
    	if (modobject == null) {modobject = Blocks.CRAFTING_TABLE; meta = 0;}
    	else {meta = StructureVillageVN.chooseCartographyTableMeta(furnaceOrientation, coordBaseMode);}

		return modobject.getStateFromMeta(meta);
	}
	
	
	// Composter
	public static IBlockState chooseModComposterState()
	{
		Block modobject = Block.getBlockFromName(ModObjects.composterFMC);
		if (modobject != null) {return modobject.getDefaultState();}
		
		return null;
	}
	
	
	// Coral
	public static IBlockState chooseGreenCoralOrPottedCactus()
	{
		return Blocks.FLOWER_POT.getStateFromMeta(9);
	}
	
	
	/**
	 * Select a diorite stairs block from a mod; returns null otherwise
	 */
	// Diorite Stairs
	// Added in 1.14
	public static Block chooseModDioriteStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteStairs_Qu);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().equals("vanillabuildersextension"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteStairs_VBE);
				if (modblock != null) {return modblock;}
			}
		}
		
		return null;
	}
	public static IBlockState chooseModDioriteWallState()
	{
    	String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteWall_Qu);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().equals("vanillabuildersextension"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteWall_VBE);
				if (modblock != null) {return modblock.getDefaultState();}
			}
		}
		
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
	
	
	// Fletching Table
	public static IBlockState chooseModFletchingTableState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		Block modobject = Block.getBlockFromName(ModObjects.fletchingTableFMC);
		int meta;
		
    	if (modobject == null) {modobject = Blocks.CRAFTING_TABLE; meta = 0;}
    	else {meta = StructureVillageVN.chooseCartographyTableMeta(furnaceOrientation, coordBaseMode);}

		return modobject.getStateFromMeta(meta);
	}
	
	
	/**
	 * Select a granite stairs block from a mod; returns null otherwise
	 */
	// Granite Stairs
	// Added in 1.14
	public static Block chooseModGraniteStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteStairs_Qu);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().equals("vanillabuildersextension"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteStairs_VBE);
				if (modblock != null) {return modblock;}
			}
		}
		
		return null;
	}
	public static IBlockState chooseModGraniteWallState()
	{
    	String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteWall_Qu);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().equals("vanillabuildersextension"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteWall_VBE);
				if (modblock != null) {return modblock.getDefaultState();}
			}
		}
		
		return null;
	}
	
	
	// Grindstone
	public static IBlockState chooseModGrindstone(int orientation, EnumFacing coordBaseMode, boolean isHanging)
	{
		Block modblock = Block.getBlockFromName(ModObjects.grindstoneFMC);
		if (modblock != null)
		{
			return modblock.getStateFromMeta(isHanging ?
					StructureVillageVN.chooseGrindstoneHangingMeta(orientation, coordBaseMode)
					:StructureVillageVN.chooseBlastFurnaceMeta(orientation, coordBaseMode)); // Standing meta is same as the blast furnace
		}
		
		return Blocks.ANVIL.getStateFromMeta(StructureVillageVN.chooseAnvilMeta(orientation, coordBaseMode));
	}
	
	
	// Lantern
    public static IBlockState chooseModLanternBlockState(boolean isHanging)
    {
    	String[] modprioritylist = GeneralConfig.modLantern;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("charm"))
			{
				modblock = Block.getBlockFromName(ModObjects.lanternCh);
				if (modblock != null) {return modblock.getStateFromMeta(isHanging? 1:0);}
			}
			else if (mod.toLowerCase().equals("futuremc"))
			{
				modblock = Block.getBlockFromName(ModObjects.lanternFMC);
				if (modblock != null) {return modblock.getStateFromMeta(isHanging? 0:1);}
			}
		}
    	
    	// None are found, so return ordinary glowstone
    	return Blocks.GLOWSTONE.getDefaultState();
    }
	public static Item chooseModLanternItem()
	{
		String[] modprioritylist = GeneralConfig.modLantern;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().equals("charm"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.lanternCh));
				if (moditem != null) {return moditem;}
			}
			else if (mod.toLowerCase().equals("futuremc"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.lanternFMC));
				if (moditem != null) {return moditem;}
			}
		}
    	
    	return null;
	}
	
	
	// Lectern
	public static void setModLecternState(World world, int x, int y, int z, int orientation, EnumFacing coordBaseMode, int woodMeta)
	{
		Block modblock=null;
		boolean setTE = false; // Flagged as true if you need to set a tile entity
		
		modblock = Block.getBlockFromName(ModObjects.deskBC);
		if (modblock != null)
		{
			setTE = true;
		}
		else
		{
			modblock = Blocks.BOOKSHELF;
		}
		
		if (setTE)
		{
			world.setBlockState(new BlockPos(x, y, z), modblock.getStateFromMeta(woodMeta), 2);
			
			// Set the tile entity so that you can assign the orientation via NBT 
			NBTTagCompound nbtCompound = new NBTTagCompound();
        	TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
        	tileentity.writeToNBT(nbtCompound);
        	nbtCompound.setInteger("angle", StructureVillageVN.chooseBibliocraftDeskMeta(orientation, coordBaseMode));
        	tileentity.readFromNBT(nbtCompound);
        	world.setTileEntity(new BlockPos(x, y, z), tileentity);
		}
		else
		{
			world.setBlockState(new BlockPos(x, y, z), modblock.getDefaultState());
		}
	}
	
	
	// Loom
	public static IBlockState chooseModLoom(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		Block modobject = Block.getBlockFromName(ModObjects.loomFMC);
		int meta;
		
    	if (modobject == null) {modobject = Blocks.CRAFTING_TABLE; meta = 0;}
    	else {meta = StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode);}

		return modobject.getStateFromMeta(meta);
	}
	
    
    // Pressure Plate
    public static Block chooseModPressurePlate(int materialMeta)
	{
    	if (materialMeta==0) {return Blocks.WOODEN_PRESSURE_PLATE;}
    	
    	Block modblock=null;
    	switch (materialMeta)
		{
			case 1: modblock = Block.getBlockFromName(ModObjects.pressurePlateSpruceQu); break;
			case 2: modblock = Block.getBlockFromName(ModObjects.pressurePlateBirchQu); break;
			case 3: modblock = Block.getBlockFromName(ModObjects.pressurePlateJungleQu); break;
			case 4: modblock = Block.getBlockFromName(ModObjects.pressurePlateAcaciaQu); break;
			case 5: modblock = Block.getBlockFromName(ModObjects.pressurePlateDarkOakQu); break;
		}
		if (modblock != null) {return modblock;}
		
		return Blocks.WOODEN_PRESSURE_PLATE;
	}
	
	
	// Sign - Added in 1.14
	public static ItemStack chooseModWoodenSignItem(int materialMeta)
	{
		// If all else fails, grab the vanilla version
		return new ItemStack(Items.SIGN, 1);
	}
	
	
	// Smithing Table
	public static IBlockState chooseModSmithingTable(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		Block modobject = Block.getBlockFromName(ModObjects.smithingTableFMC);
		int meta;
		
    	if (modobject == null) {modobject = Blocks.CRAFTING_TABLE; meta = 0;}
    	else {meta = StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode);}

		return modobject.getStateFromMeta(meta);
	}
	
	
	// Smoker
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModSmokerState(int orientation, EnumFacing coordBaseMode)
	{
		Block modblock = Block.getBlockFromName(ModObjects.smokerFMC);
		if (modblock != null)
		{
			return modblock.getStateFromMeta(StructureVillageVN.chooseBlastFurnaceMeta(orientation, coordBaseMode)); // Standing meta is same as the blast furnace
		}
		
		return Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(orientation, coordBaseMode));
	}
	
	
	// Smooth Sandstone - Added in 1.14
	public static IBlockState chooseModSmoothSandstoneBlockState(boolean isRed)
	{
		String[] modprioritylist = GeneralConfig.modSandstone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().equals("quark"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.smoothSandstone_red_Qu : ModObjects.smoothSandstone_white_Qu);
			}
			else if (mod.toLowerCase().equals("futuremc"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.smoothSandstone_red_FMC : ModObjects.smoothSandstone_white_FMC);
			}
			
			if (modobject != null) {return modobject.getDefaultState();}
		}
		
		// If all else fails, return the vanilla version
		return (isRed?Blocks.DOUBLE_STONE_SLAB2:Blocks.DOUBLE_STONE_SLAB).getStateFromMeta(9);
	}
	
	
	// Smooth Sandstone Slab - Added in 1.14
	/**
	 * Returns regular sandstone slab on a failure
	 */
	public static IBlockState chooseModSmoothSandstoneSlab(boolean upper, boolean isred)
	{
		Block modobject = Block.getBlockFromName(isred ? ModObjects.smoothRedSandstoneSlabQu : ModObjects.smoothSandstoneSlabQu);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return isred ? Blocks.STONE_SLAB2.getStateFromMeta(upper?8:0) : Blocks.STONE_SLAB.getStateFromMeta(upper?9:1);
	}
	
	
	/**
	 * Added in 1.14
	 * Returns non-smooth stair versions on failure
	 */
	public static Block chooseModSmoothSandstoneStairsBlock(boolean isRed)
	{
		Block modblock = Block.getBlockFromName(isRed ? ModObjects.smoothSandstoneStairs_red_VBE : ModObjects.smoothSandstoneStairs_white_VBE);
		if (modblock != null) {return modblock;}
		
		return isRed ? Blocks.RED_SANDSTONE_STAIRS : Blocks.SANDSTONE_STAIRS;
	}
	
	
	// Sandstone Wall
	public static IBlockState chooseModSandstoneWall(boolean isRed)
	{
		String[] modprioritylist = GeneralConfig.modSandstone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().equals("quark"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.sandstoneWall_red_Qu : ModObjects.sandstoneWall_white_Qu);
			}
			else if (mod.toLowerCase().equals("futuremc"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.sandstoneWall_red_FMC : ModObjects.sandstoneWall_white_FMC);
			}
			else if (mod.toLowerCase().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.sandstoneWall_red_VBE : ModObjects.sandstoneWall_white_VBE);
			}
			
			if (modobject != null) {return modobject.getDefaultState();}
		}
		
		return null;
	}
	
	
	// Smooth Stone - Added in 1.14
	public static IBlockState chooseModSmoothStoneBlockState()
	{
		String[] modprioritylist = GeneralConfig.modSmoothStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.smoothStoneQu);
			}
			else if (mod.toLowerCase().equals("futuremc"))
			{
				modobject = Block.getBlockFromName(ModObjects.smoothStoneFMC);
			}
			
			if (modobject != null) {return modobject.getDefaultState();}
		}
		
		return Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(8);
	}
	
	
	// Stonecutter
	public static IBlockState chooseModStonecutterState(int orientation, EnumFacing coordBaseMode)
	{
		Block modblock = Block.getBlockFromName(ModObjects.stoneCutterFMC);
		if (modblock != null)
		{
			return modblock.getStateFromMeta(StructureVillageVN.chooseBlastFurnaceMeta(orientation, coordBaseMode)); // Standing meta is same as the blast furnace
		}
		
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Stripped log
	/**
	 * Materials are: 0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=darkoak
	 * Orientations are: 0=vertical, 1=east-west, 2=north-south
	 */
	public static IBlockState chooseModStrippedLogState(int materialMeta, int orientation)
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
	public static IBlockState chooseModStrippedWoodState(IBlockState blockstate, int orientation)
	{
		Block block=blockstate.getBlock();
		int materialMeta = block.getMetaFromState(blockstate) + (block==Blocks.LOG2 ? 4:0);
		
		switch (materialMeta)
		{
		case 0: block = Block.getBlockFromName(ModObjects.strippedWoodOakFMC); break;
		case 1: block = Block.getBlockFromName(ModObjects.strippedWoodSpruceFMC); break;
		case 2: block = Block.getBlockFromName(ModObjects.strippedWoodBirchFMC); break;
		case 3: block = Block.getBlockFromName(ModObjects.strippedWoodJungleFMC); break;
		case 4: block = Block.getBlockFromName(ModObjects.strippedWoodAcaciaFMC); break;
		case 5: block = Block.getBlockFromName(ModObjects.strippedWoodDarkOakFMC); break;
		}
		if (block != null) {return block.getStateFromMeta(orientation%3*4);}
		
		// Pass the original block if it's not a vanilla log
		if (block!=Blocks.LOG && block!=Blocks.LOG2) {return blockstate;}
		return chooseModBarkState(blockstate);
	}
	
	
	// Sweet Berries - Added in 1.14
	public static ItemStack chooseModSweetBerriesItem()
	{
		Item modobject = FunctionsVN.getItemFromName(ModObjects.sweetBerriesFMC);
		
		if (modobject!=null) {return new ItemStack(modobject);}
		return null;
	}
	
	
	// Trapdoor
	public static Block chooseModWoodenTrapdoor(int materialMeta)
	{
		Block modblock=null;
		
		switch (materialMeta)
		{
			case 1: modblock = Block.getBlockFromName(ModObjects.trapdoorSpruceQu); break;
			case 2: modblock = Block.getBlockFromName(ModObjects.trapdoorBirchQu); break;
			case 3: modblock = Block.getBlockFromName(ModObjects.trapdoorJungleQu); break;
			case 4: modblock = Block.getBlockFromName(ModObjects.trapdoorAcaciaQu); break;
			case 5: modblock = Block.getBlockFromName(ModObjects.trapdoorDarkOakQu); break;
		}
		if (modblock != null) {return modblock;}
		
		// If all else fails, grab the vanilla version
		return Blocks.TRAPDOOR;
	}
}
