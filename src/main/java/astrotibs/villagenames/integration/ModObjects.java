package astrotibs.villagenames.integration;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.village.StructureVillageVN;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

	// Bark
	public static final String barkQu = "quark:bark";
 	
	// Crops
	public static final String cropArtichokeHC = "harvestcraft:pamartichokeCrop";
	public static final String cropAsparagusHC = "harvestcraft:pamasparagusCrop";
	public static final String cropBambooHC = "harvestcraft:pambambooshootCrop";
	public static final String cropBarleyHC = "harvestcraft:pambarleyCrop";
	public static final String cropBeanHC = "harvestcraft:pambeanCrop";
	public static final String cropBeetHC = "harvestcraft:pambeetCrop";
	public static final String cropBellpepperHC = "harvestcraft:pambellpepperCrop";
	public static final String cropBlackberryHC = "harvestcraft:pamblackberryCrop";
	public static final String cropBlueberryHC = "harvestcraft:pamblueberryCrop";
	public static final String cropBroccoliHC = "harvestcraft:pambroccoliCrop";
	public static final String cropBrusselsproutHC = "harvestcraft:pambrusselsproutCrop";
	public static final String cropCabbageHC = "harvestcraft:pamcabbageCrop";
	public static final String cropCactusfruitHC = "harvestcraft:pamcactusfruitCrop"; // Planted on sand
	public static final String cropCandleberryHC = "harvestcraft:pamcandleberryCrop";
	public static final String cropCantaloupeHC = "harvestcraft:pamcantaloupeCrop";
	public static final String cropCauliflowerHC = "harvestcraft:pamcauliflowerCrop";
	public static final String cropCeleryHC = "harvestcraft:pamceleryCrop";
	public static final String cropChilipepperHC = "harvestcraft:pamchilipepperCrop";
	public static final String cropCoffeebeanHC = "harvestcraft:pamcoffeebeanCrop";
	public static final String cropCornHC = "harvestcraft:pamcornCrop";
	public static final String cropCottonHC = "harvestcraft:pamcottonCrop";
	public static final String cropCranberryHC = "harvestcraft:pamcranberryCrop"; // Planted on water
	public static final String cropCucumberHC = "harvestcraft:pamcucumberCrop";
	public static final String cropCurryleafHC = "harvestcraft:pamcurryleafCrop";
	public static final String cropEggplantHC = "harvestcraft:pameggplantCrop";
	public static final String cropGarlicHC = "harvestcraft:pamgarlicCrop";
	public static final String cropGingerHC = "harvestcraft:pamgingerCrop";
	public static final String cropGrapeHC = "harvestcraft:pamgrapeCrop";
	public static final String cropKiwiHC = "harvestcraft:pamkiwiCrop";
	public static final String cropLeekHC = "harvestcraft:pamleekCrop";
	public static final String cropLettuceHC = "harvestcraft:pamlettuceCrop";
	public static final String cropMustardseedHC = "harvestcraft:pammustardseedsCrop";
	public static final String cropOatsHC = "harvestcraft:pamoatsCrop";
	public static final String cropOkraHC = "harvestcraft:pamokraCrop";
	public static final String cropOnionHC = "harvestcraft:pamonionCrop";
	public static final String cropParsnipHC = "harvestcraft:pamparsnipCrop";
	public static final String cropPeanutHC = "harvestcraft:pampeanutCrop";
	public static final String cropPeasHC = "harvestcraft:pampeasCrop";
	public static final String cropPineappleHC = "harvestcraft:pampineappleCrop";
	public static final String cropRadishHC = "harvestcraft:pamradishCrop";
	public static final String cropRaspberryHC = "harvestcraft:pamraspberryCrop";
	public static final String cropRhubarbHC = "harvestcraft:pamrhubarbCrop";
	public static final String cropRiceHC = "harvestcraft:pamriceCrop"; // Planted on water
	public static final String cropRutabegaHC = "harvestcraft:pamrutabagaCrop";
	public static final String cropRyeHC = "harvestcraft:pamryeCrop";
	public static final String cropScallionHC = "harvestcraft:pamscallionCrop";
	public static final String cropSeaweedHC = "harvestcraft:pamseaweedCrop"; // Planted on water
	public static final String cropSesameseedHC = "harvestcraft:pamsesameseedsCrop";
	public static final String cropSoybeanHC = "harvestcraft:pamsoybeanCrop";
	public static final String cropSpiceleafHC = "harvestcraft:pamspiceleafCrop";
	public static final String cropSpinachHC = "harvestcraft:pamspinachCrop";
	public static final String cropStrawberryHC = "harvestcraft:pamstrawberryCrop";
	public static final String cropSweetpotatoHC = "harvestcraft:pamsweetpotatoCrop";
	public static final String cropTealeafHC = "harvestcraft:pamtealeafCrop";
	public static final String cropTomatoHC = "harvestcraft:pamtomatoCrop";
	public static final String cropTurnipHC = "harvestcraft:pamturnipCrop";
	public static final String cropWaterchestnutHC = "harvestcraft:pamwaterchestnutCrop"; // Planted on water
	public static final String cropWhitemushroomHC = "harvestcraft:pamwhitemushroomCrop"; // Planted on log
	public static final String cropWintersquashHC = "harvestcraft:pamwintersquashCrop";
	public static final String cropZucchiniHC = "harvestcraft:pamzucchiniCrop";
	public static final String cropKaleJAFFA = "jaffa:kaleCrop";
	
	// Mud
	public static final String mudBOP_classPath = "biomesoplenty.common.block.BlockBOPMud";

	// Smooth Sandstone
	public static final String smoothSandstoneQu = "quark:sandstone_new";
	
	// Smooth Sandstone Slab
	public static final String smoothSandstoneSlabQu = "quark:sandstone_smooth_slab";
	public static final String smoothRedSandstoneSlabQu = "quark:red_sandstone_smooth_slab";
	
	// Smooth Stone 
	public static final String smoothStoneQu = "quark:polished_stone";
	
	// Stairs
	public static final String dioriteStairs_Qu = "quark:stone_diorite_stairs";
	public static final String graniteStairs_Qu = "quark:stone_granite_stairs";
	
	// Trapdoor
	public static final String trapdoorSpruceQu = "quark:spruce_trapdoor";
	public static final String trapdoorBirchQu = "quark:birch_trapdoor";
	public static final String trapdoorJungleQu = "quark:jungle_trapdoor";
	public static final String trapdoorAcaciaQu = "quark:acacia_trapdoor";
	public static final String trapdoorDarkOakQu = "quark:dark_oak_trapdoor";
	
	// Walls
	public static final String sandstoneWall_white_Qu = "quark:sandstone_wall"; 
	public static final String sandstoneWall_red_Qu = "quark:red_sandstone_wall"; 
	public static final String dioriteWall_Qu = "quark:stone_diorite_wall";
	public static final String graniteWall_Qu = "quark:stone_granite_wall";
	
	
	
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
		BlockPos pos = new BlockPos(x, y, z);
		Block modblock = Blocks.BED;
		
		// Set the bed block and metadata here
		world.setBlockState(pos, modblock.getStateFromMeta(orientationMeta), 2);
	}
	
	
	// Blast Furnace
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModBlastFurnaceState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		Block modblock = Blocks.FURNACE;
		int meta = StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode);
		
		return modblock.getStateFromMeta(meta);
	}
	
	
	// Blue Ice
	// Added in 1.13
    public static IBlockState chooseModBlueIceBlockState()
    {
    	// None are found, so return ordinary packed ice
    	return Blocks.PACKED_ICE.getDefaultState();
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
	
	
	// Cartography Table
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModCartographyTableState()
	{
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Composter
	public static IBlockState chooseModComposter()
	{
		return null;
	}
	
	
	// Concrete
	public static IBlockState chooseModConcrete(int color)
	{
		if (GeneralConfig.addConcrete)
		{
			return ModBlocksVN.CONCRETE.getStateFromMeta(color);
		}
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
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.dioriteStairs_Qu);
		if (modblock != null) {return modblock;}
		
		// TODO - Botania available in 1.10
		return null;
	}
	public static IBlockState chooseModDioriteWallState()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.dioriteWall_Qu);
		if (modblock != null) {return modblock.getDefaultState();}
		
		// TODO - Botania available in 1.10
		return null;
	}
	
	
	// Fletching Table
	public static IBlockState chooseModFletchingTableState()
	{
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Glazed Terracotta
	public static IBlockState chooseModGlazedTerracottaState(int colorMeta, int facingMeta)
	{
		if (GeneralConfig.addConcrete)
		{
			return FunctionsVN.getGlazedTerracotaFromMetas(colorMeta, facingMeta);
		}
		return null;
	}
	
	
	/**
	 * Select a granite stairs block from a mod; returns null otherwise
	 */
	// Granite Stairs
	// Added in 1.14
	public static Block chooseModGraniteStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.graniteStairs_Qu);
		if (modblock != null) {return modblock;}
		
		// TODO - Botania available in 1.10
		return null;
	}
	public static IBlockState chooseModGraniteWallState()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.graniteWall_Qu);
		if (modblock != null) {return modblock.getDefaultState();}
		
		// TODO - Botania available in 1.10
		return null;
	}
	
	
	// Grindstone
	public static IBlockState chooseModGrindstone(int orientation, EnumFacing coordBaseMode)
	{
		return Blocks.ANVIL.getStateFromMeta(StructureVillageVN.chooseAnvilMeta(orientation, coordBaseMode));
	}
	
	
	// Lantern
    public static IBlockState chooseModLanternBlockState(boolean isHanging)
    {
    	// No mod lanterns exist. Return a glowstone block.
    	return Blocks.GLOWSTONE.getDefaultState();
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
	public static IBlockState chooseModLoom()
	{
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Iron Nugget
	// TODO - added in 1.11
	public static ItemStack chooseModIronNugget()
	{
		// TODO - Railcraft available in 1.10
		// TODO - Thermal Foundation available in 1.10
		// TODO - Tinkers Construct available in 1.8
		return null;
	}
	
	
	// Sign - Added in 1.14
	public static ItemStack chooseModWoodenSignItem(int materialMeta)
	{
		// If all else fails, grab the vanilla version
		return new ItemStack(Items.SIGN, 1);
	}
	
	
	// Smithing Table
	public static IBlockState chooseModSmithingTable()
	{
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Smoker
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModSmokerState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		return Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode));
	}
	
	
	// Smooth Sandstone - Added in 1.14
	public static IBlockState chooseModSmoothSandstoneBlockState(boolean isRed)
	{
		Block modblock = Block.getBlockFromName(ModObjects.smoothSandstoneQu);
		if (modblock != null) {return modblock.getStateFromMeta(isRed?2:0);}
		else {return (isRed?Blocks.DOUBLE_STONE_SLAB2:Blocks.DOUBLE_STONE_SLAB).getStateFromMeta(9);}
	}
	
	
	// Smooth Sandstone Slab - Added in 1.14
	/**
	 * Returns regular sandstone slab on a failure
	 */
	public static IBlockState chooseModSmoothSandstoneSlab(boolean upper, boolean isred)
	{
		Block modblock;
		
		if (isred)
		{
			modblock = Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu);
			if (modblock != null) {return modblock.getStateFromMeta(upper?8:0);}
			else {return Blocks.STONE_SLAB2.getStateFromMeta(upper?8:0);}
		}
		else
		{
			modblock = Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu);
			if (modblock != null) {return modblock.getStateFromMeta(upper?8:0);}
			else {return Blocks.STONE_SLAB.getStateFromMeta(upper?9:1);}
		}
	}
	
	
	/**
	 * Added in 1.14
	 * Returns non-smooth stair versions on failure
	 */
	public static Block chooseModSmoothSandstoneStairsBlock(boolean isRed)
	{
		return isRed ? Blocks.RED_SANDSTONE_STAIRS : Blocks.SANDSTONE_STAIRS;
	}
	
	
	// Sandstone Wall - Added in 1.14
	public static IBlockState chooseModSandstoneWall(boolean isRed)
	{
		// TODO - Railcraft is 1.10
		Block modblock=null;

		if (isRed) {modblock = Block.getBlockFromName(ModObjects.sandstoneWall_red_Qu);}
		else {modblock = Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu);}
		if (modblock != null) {return modblock.getDefaultState();}
		
		return null;
	}
	
	
	// Smooth Stone - Added in 1.14
	public static IBlockState chooseModSmoothStoneBlockState()
	{
		Block modblock=null;
		modblock = Block.getBlockFromName(ModObjects.smoothStoneQu);
		if (modblock != null) {return modblock.getDefaultState();}
		
		return Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(8);
	}
	
	
	// Stonecutter
	/**
	 * Orientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModStonecutterState(int orientation)
	{
		return Blocks.CRAFTING_TABLE.getDefaultState();
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
	
	
	// Sweet Berries - Added in 1.14
	public static ItemStack chooseModSweetBerriesItem()
	{
		return null;
	}
	
	
	// Wood block (has bark on all surfaces)
	// Added as craftable in 1.13
	public static IBlockState chooseModWoodBlockState(IBlockState blockstate)
	{
		Block block = blockstate.getBlock();
		
		// Pass the original block if it's not a vanilla log
		if (block!=Blocks.LOG && block!=Blocks.LOG2) {return blockstate;}
		return chooseModBarkState(blockstate);
	}
}
