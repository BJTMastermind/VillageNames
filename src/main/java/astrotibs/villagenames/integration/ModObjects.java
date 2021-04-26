package astrotibs.villagenames.integration;

import astrotibs.villagenames.block.ModBlocksVN;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * A holder for string names for various mod items/blocks/etc for easy access
 */
// Added in v3.1trades
public class ModObjects {
	
	// Constantly referenced domain names
	public static final String DOM_BIOMESOPLENTY = "biomesoplenty";
	public static final String DOM_HARVESTCRAFT = "harvestcraft";
	public static final String DOM_SAMSBEETROOT = "samsbeetroot";
	
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
 	
 	

	// Added in v3.1trades
 	
	// --------------------------------------------- //
	// --- Blocks and items reference for trades --- //
	// --------------------------------------------- //
 	
	
	// --- Blocks ---//
	
 	
	// Crops
	public static final String cropArtichokeHC = DOM_HARVESTCRAFT + ":pamartichokeCrop";
	public static final String cropAsparagusHC = DOM_HARVESTCRAFT + ":pamasparagusCrop";
	public static final String cropBambooHC = DOM_HARVESTCRAFT + ":pambambooshootCrop";
	public static final String cropBarleyHC = DOM_HARVESTCRAFT + ":pambarleyCrop";
	public static final String cropBeanHC = DOM_HARVESTCRAFT + ":pambeanCrop";
	public static final String cropBeetHC = DOM_HARVESTCRAFT + ":pambeetCrop";
	public static final String cropBellpepperHC = DOM_HARVESTCRAFT + ":pambellpepperCrop";
	public static final String cropBlackberryHC = DOM_HARVESTCRAFT + ":pamblackberryCrop";
	public static final String cropBlueberryHC = DOM_HARVESTCRAFT + ":pamblueberryCrop";
	public static final String cropBroccoliHC = DOM_HARVESTCRAFT + ":pambroccoliCrop";
	public static final String cropBrusselsproutHC = DOM_HARVESTCRAFT + ":pambrusselsproutCrop";
	public static final String cropCabbageHC = DOM_HARVESTCRAFT + ":pamcabbageCrop";
	public static final String cropCactusfruitHC = DOM_HARVESTCRAFT + ":pamcactusfruitCrop"; // Planted on sand
	public static final String cropCandleberryHC = DOM_HARVESTCRAFT + ":pamcandleberryCrop";
	public static final String cropCantaloupeHC = DOM_HARVESTCRAFT + ":pamcantaloupeCrop";
	public static final String cropCauliflowerHC = DOM_HARVESTCRAFT + ":pamcauliflowerCrop";
	public static final String cropCeleryHC = DOM_HARVESTCRAFT + ":pamceleryCrop";
	public static final String cropChilipepperHC = DOM_HARVESTCRAFT + ":pamchilipepperCrop";
	public static final String cropCoffeebeanHC = DOM_HARVESTCRAFT + ":pamcoffeebeanCrop";
	public static final String cropCornHC = DOM_HARVESTCRAFT + ":pamcornCrop";
	public static final String cropCottonHC = DOM_HARVESTCRAFT + ":pamcottonCrop";
	public static final String cropCranberryHC = DOM_HARVESTCRAFT + ":pamcranberryCrop"; // Planted on water
	public static final String cropCucumberHC = DOM_HARVESTCRAFT + ":pamcucumberCrop";
	public static final String cropCurryleafHC = DOM_HARVESTCRAFT + ":pamcurryleafCrop";
	public static final String cropEggplantHC = DOM_HARVESTCRAFT + ":pameggplantCrop";
	public static final String cropGarlicHC = DOM_HARVESTCRAFT + ":pamgarlicCrop";
	public static final String cropGingerHC = DOM_HARVESTCRAFT + ":pamgingerCrop";
	public static final String cropGrapeHC = DOM_HARVESTCRAFT + ":pamgrapeCrop";
	public static final String cropKiwiHC = DOM_HARVESTCRAFT + ":pamkiwiCrop";
	public static final String cropLeekHC = DOM_HARVESTCRAFT + ":pamleekCrop";
	public static final String cropLettuceHC = DOM_HARVESTCRAFT + ":pamlettuceCrop";
	public static final String cropMustardseedHC = DOM_HARVESTCRAFT + ":pammustardseedsCrop";
	public static final String cropOatsHC = DOM_HARVESTCRAFT + ":pamoatsCrop";
	public static final String cropOkraHC = DOM_HARVESTCRAFT + ":pamokraCrop";
	public static final String cropOnionHC = DOM_HARVESTCRAFT + ":pamonionCrop";
	public static final String cropParsnipHC = DOM_HARVESTCRAFT + ":pamparsnipCrop";
	public static final String cropPeanutHC = DOM_HARVESTCRAFT + ":pampeanutCrop";
	public static final String cropPeasHC = DOM_HARVESTCRAFT + ":pampeasCrop";
	public static final String cropPineappleHC = DOM_HARVESTCRAFT + ":pampineappleCrop";
	public static final String cropRadishHC = DOM_HARVESTCRAFT + ":pamradishCrop";
	public static final String cropRaspberryHC = DOM_HARVESTCRAFT + ":pamraspberryCrop";
	public static final String cropRhubarbHC = DOM_HARVESTCRAFT + ":pamrhubarbCrop";
	public static final String cropRiceHC = DOM_HARVESTCRAFT + ":pamriceCrop"; // Planted on water
	public static final String cropRutabegaHC = DOM_HARVESTCRAFT + ":pamrutabagaCrop";
	public static final String cropRyeHC = DOM_HARVESTCRAFT + ":pamryeCrop";
	public static final String cropScallionHC = DOM_HARVESTCRAFT + ":pamscallionCrop";
	public static final String cropSeaweedHC = DOM_HARVESTCRAFT + ":pamseaweedCrop"; // Planted on water
	public static final String cropSesameseedHC = DOM_HARVESTCRAFT + ":pamsesameseedsCrop";
	public static final String cropSoybeanHC = DOM_HARVESTCRAFT + ":pamsoybeanCrop";
	public static final String cropSpiceleafHC = DOM_HARVESTCRAFT + ":pamspiceleafCrop";
	public static final String cropSpinachHC = DOM_HARVESTCRAFT + ":pamspinachCrop";
	public static final String cropStrawberryHC = DOM_HARVESTCRAFT + ":pamstrawberryCrop";
	public static final String cropSweetpotatoHC = DOM_HARVESTCRAFT + ":pamsweetpotatoCrop";
	public static final String cropTealeafHC = DOM_HARVESTCRAFT + ":pamtealeafCrop";
	public static final String cropTomatoHC = DOM_HARVESTCRAFT + ":pamtomatoCrop";
	public static final String cropTurnipHC = DOM_HARVESTCRAFT + ":pamturnipCrop";
	public static final String cropWaterchestnutHC = DOM_HARVESTCRAFT + ":pamwaterchestnutCrop"; // Planted on water
	public static final String cropWhitemushroomHC = DOM_HARVESTCRAFT + ":pamwhitemushroomCrop"; // Planted on log
	public static final String cropWintersquashHC = DOM_HARVESTCRAFT + ":pamwintersquashCrop";
	public static final String cropZucchiniHC = DOM_HARVESTCRAFT + ":pamzucchiniCrop";
	public static final String cropKaleJAFFA = "jaffa:kaleCrop";
	
	// Mud
	public static final String mudBOP_classPath = "biomesoplenty.common.block.BlockBOPMud";
	
	// Quicksand
	public static final String quicksandBOP_classPath = "biomesoplenty.common.block.BlockBOPSand";
	
	
	// --- Items --- //
	
	// Beetroot
	public static final String beetrootSeedSB = DOM_SAMSBEETROOT + ":beetroot_seed";
	public static final String beetrootItemSB = DOM_SAMSBEETROOT + ":beetroot_item";
	public static final String beetrootSoupSB = DOM_SAMSBEETROOT + ":beetroot_soup";
	
	// Desk
	public static final String deskBC = "bibliocraft:Desk";
	
	// Dye
	public static final String dyeBlueBOP = DOM_BIOMESOPLENTY + ":blue_dye";
	public static final String dyeBrownBOP = DOM_BIOMESOPLENTY + ":brown_dye";
	public static final String dyeGreenBOP = DOM_BIOMESOPLENTY + ":green_dye";
	public static final String dyeWhiteBOP = DOM_BIOMESOPLENTY + ":white_dye";
	public static final String dyeBlackBOP = DOM_BIOMESOPLENTY + ":black_dye";

	// Kelp and Kelp Accessories
	public static final String kelpBOP = DOM_BIOMESOPLENTY + ":seaweed";
	

	
	
	// --------------------------- //
	// --- Generator Functions --- //
	// --------------------------- //
	
	// Bark
	public static IBlockState chooseModBarkState(IBlockState blockstate)
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
	
	// Bed
	public static void setModBedBlock(World world, int x, int y, int z, int orientationMeta, int colorMeta)
	{
		BlockPos pos = new BlockPos(x, y, z);
		Block modblock = Blocks.bed;
		
		// Set the bed block and metadata here
		world.setBlockState(pos, modblock.getStateFromMeta(orientationMeta), 2);
	}
	
	
	// Beetroot Crop
	public static Block chooseModBeetrootCropBlock()
	{
		Block modblock = Block.getBlockFromName(ModObjects.beetrootSeedSB);
		if (modblock != null) {return modblock;}
		
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
		
	
	// Blast Furnace
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModBlastFurnaceState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		Block modblock = Blocks.furnace;
		int meta = StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode);
		
		return modblock.getStateFromMeta(meta);
	}
	
	
	// Blue Ice
	// Added in 1.13
    public static IBlockState chooseModBlueIceBlockState()
    {
    	// None are found, so return ordinary packed ice
    	return Blocks.packed_ice.getDefaultState();
    }
	
	
	// Concrete
	public static IBlockState chooseModConcreteState(int color)
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
	public static IBlockState chooseModCampfireBlockState(int relativeOrientation, EnumFacing coordBaseMode)
	{
		// No mod campfires exist. Return an upright torch.
		return Blocks.torch.getDefaultState();
	}
	
	// Cartography Table
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModCartographyTableState()
	{
		return Blocks.crafting_table.getDefaultState();
	}
	
	
	// Composter
	public static IBlockState chooseModComposterState()
	{
		return null;
	}
	
	
	// Coral
	public static IBlockState chooseGreenCoralOrPottedCactus()
	{
		return Blocks.flower_pot.getStateFromMeta(9);
	}

	/**
	 * Select a diorite stairs block from a mod; returns null otherwise
	 */
	// Diorite Stairs
	// Added in 1.14
	public static Block chooseModDioriteStairsBlock()
	{
		return null;
	}
	public static IBlockState chooseModDioriteSlabState(boolean upper)
	{
		return null;
	}
	public static IBlockState chooseModPolishedDioriteSlabState(boolean upper)
	{
		return null;
	}
	public static IBlockState chooseModDioriteWallState()
	{
		return null;
	}
	
	// Fletching Table
	public static IBlockState chooseModFletchingTableState()
	{
		return Blocks.crafting_table.getDefaultState();
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
		// TODO - Botania available in 1.10
		return null;
	}
	public static IBlockState chooseModGraniteWallState()
	{
		// TODO - Botania available in 1.10
		return null;
	}
	
	// Grass Path block if able; returns Gravel otherwise.
	public static IBlockState chooseModPathState()
	{
		return Blocks.gravel.getDefaultState();
	}
	
	
	// Grindstone
	public static IBlockState chooseModGrindstone(int orientation, EnumFacing coordBaseMode)
	{
		return Blocks.anvil.getStateFromMeta(StructureVillageVN.chooseAnvilMeta(orientation, coordBaseMode));
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
	
	
	// Lantern
    public static IBlockState chooseModLanternBlockState(boolean isHanging)
    {
    	// No mod lanterns exist. Return a glowstone block.
    	return Blocks.glowstone.getDefaultState();
    }
	
	
	// Lectern

	// Array of meta values for furnaces indexed by [orientation][horizIndex]
	// 1: north-facing
	// 2: east-facing
	// 3: south-facing
	// 0: west-facing
	public static final int[][] BIBLIOCRAFT_DESK_META_ARRAY = new int[][]{
		{3,0,1,2}, // fore-facing (away from you)
		{2,3,2,3}, // right-facing
		{1,2,3,0}, // back-facing (toward you)
		{0,1,0,1}, // left-facing
	};
	/**
	 * orientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int chooseBibliocraftDeskMeta(int orientation, EnumFacing coordBaseMode)
	{
		return (StructureVillageVN.ANVIL_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()]+2)%4;
	}
	
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
			modblock = Blocks.bookshelf;
		}
		
		if (setTE)
		{
			world.setBlockState(new BlockPos(x, y, z), modblock.getStateFromMeta(woodMeta), 2);
			
			// Set the tile entity so that you can assign the orientation via NBT 
			NBTTagCompound nbtCompound = new NBTTagCompound();
        	TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));
        	tileentity.writeToNBT(nbtCompound);
        	nbtCompound.setInteger("angle", chooseBibliocraftDeskMeta(orientation, coordBaseMode));
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
		return Blocks.crafting_table.getDefaultState();
	}
	
	
	// Mossy stone
	public static IBlockState chooseModMossyStoneBrickWallState()
	{
		return null;
	}
	public static IBlockState choosModMossyStoneBrickStairsBlock()
	{
		return null;
	}
	public static IBlockState chooseModMossyCobblestoneStairsBlock()
	{
		return null;
	}
	
	
	/**
	 * Added in 1.14
	 * Returns non-smooth stair versions on failure
	 */
	public static Block chooseModSmoothSandstoneStairsBlock(boolean isRed)
	{
		return isRed ? Blocks.red_sandstone_stairs : Blocks.sandstone_stairs;
	}
	
	
	// Sandstone Wall - Added in 1.14
	public static IBlockState chooseModSandstoneWall(boolean isRed)
	{
		// TODO - Railcraft is 1.10
		return null;
	}
	
	
	// Cut Sandstone Slab - Added in 1.14
	public static IBlockState chooseModCutSandstoneSlab(boolean upper)
	{
		return null;
	}
	
	
	// Smooth Sandstone - Added in 1.14
	public static IBlockState chooseModSmoothSandstoneState(boolean isRed)
	{
		return (isRed?Blocks.double_stone_slab2:Blocks.double_stone_slab).getStateFromMeta(9);
	}
	
	
	// Smooth Sandstone Slab - Added in 1.14
	/**
	 * Returns regular sandstone slab on a failure
	 */
	public static IBlockState chooseModSmoothSandstoneSlab(boolean upper, boolean isred)
	{
		if (isred) {return Blocks.stone_slab2.getStateFromMeta(upper?8:0);} // Sandstone slab
		else {return Blocks.stone_slab.getStateFromMeta(upper?9:1);} // Red sandstone slab
	}
	
	
	// Sign - Added in 1.14
	public static ItemStack chooseModWoodenSignItem(int materialMeta)
	{
		// If all else fails, grab the vanilla version
		return new ItemStack(Items.sign, 1);
	}
	
	
	// Smithing Table
	public static IBlockState chooseModSmithingTable()
	{
		return Blocks.crafting_table.getDefaultState();
	}
	
	
	// Smoker
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModSmokerState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		return Blocks.furnace.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode));
	}
	
	
	// Smooth Stone - Added in 1.14
	public static IBlockState chooseModSmoothStoneBlockState()
	{
		return Blocks.double_stone_slab.getStateFromMeta(8);
	}
	
	
	// Stone brick wall
	public static IBlockState chooseModStoneBrickWallState()
	{
		return null;
	}
	
	
	// Stripped log - Added in 1.13
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
		return (materialMeta<4 ? Blocks.log : Blocks.log2).getStateFromMeta(orientation*4+materialMeta%4);
	}
	

	// Stripped wood - Added in 1.13?
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
		return (materialMeta<4 ? Blocks.log : Blocks.log2).getStateFromMeta(orientation*4+materialMeta%4);
	}
	
	
	// Stonecutter
	/**
	 * Orientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModStonecutterState(int orientation)
	{
		return Blocks.crafting_table.getDefaultState();
	}
	
	
	// Sweet Berries - Added in 1.14
	public static ItemStack chooseModSweetBerriesItem()
	{
		return null;
	}
	
	
	// Trap door
	/*
	public static Block chooseModWoodenTrapdoor(int materialMeta)
	{
		if (materialMeta==0) {return Blocks.trapdoor;}
		
		Block modblock=null;
		
		switch (materialMeta)
		{
			case 1: modblock = Block.getBlockFromName(ModObjects.trapdoorSpruceMD); break;
			case 2: modblock = Block.getBlockFromName(ModObjects.trapdoorBirchMD); break;
			case 3: modblock = Block.getBlockFromName(ModObjects.trapdoorJungleMD); break;
			case 4: modblock = Block.getBlockFromName(ModObjects.trapdoorAcaciaMD); break;
			case 5: modblock = Block.getBlockFromName(ModObjects.trapdoorDarkOakMD); break;
		}
		if (modblock != null) {return modblock;}
		
		// If all else fails, grab the vanilla version
		return Blocks.trapdoor;
	}
	*/
	
	// Wood block (has bark on all surfaces)
	// Added as craftable in 1.13
	public static IBlockState chooseModWoodBlockState(IBlockState blockstate)
	{
		Block block = blockstate.getBlock();
		
		// Pass the original block if it's not a vanilla log
		if (block!=Blocks.log && block!=Blocks.log2) {return blockstate;}
		return chooseModBarkState(blockstate);
	}
}
