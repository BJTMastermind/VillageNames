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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A holder for string names for various mod items/blocks/etc for easy access
 */
// Added in v3.1trades
public class ModObjects {
	
	// Constantly referenced domain names
	public static final String DOM_BIOMESOPLENTY = "biomesoplenty";
	public static final String DOM_HARVESTCRAFT = "harvestcraft";
	public static final String DOM_QUARK = "quark";
	
	
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
 	
	// Andesite
	public static final String andesiteSlab_Qu = DOM_QUARK + ":stone_andesite_slab";
	public static final String andesiteBricksSlab_Qu = DOM_QUARK + ":stone_andesite_bricks_slab";

	// Bamboo
	// Stalks (Blocks)
	public static final String bambooStalk_BoP = DOM_BIOMESOPLENTY + ":bamboo";
	// Saplings (Items)
	public static final String sapling0_BoP = DOM_BIOMESOPLENTY + ":sapling_0"; // Meta 2
	// Leaves
	public static final String bambooLeaves_BoP = DOM_BIOMESOPLENTY + ":leaves_0";
	
	// Bark
	public static final String barkQu = DOM_QUARK + ":bark";
	
	// Campfire
	public static final String campfireTAN = "toughasnails:campfire";
	 	
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

	// Desk
	public static final String deskBC = "bibliocraft:Desk";
	
	// Diorite
	public static final String dioriteSlab_Qu = DOM_QUARK + ":stone_diorite_slab";
	public static final String dioriteBricksSlab_Qu = DOM_QUARK + ":stone_diorite_bricks_slab";
	
	// Dye
	public static final String dyeBlueBOP = DOM_BIOMESOPLENTY + ":blue_dye";
	public static final String dyeBrownBOP = DOM_BIOMESOPLENTY + ":brown_dye";
	public static final String dyeGreenBOP = DOM_BIOMESOPLENTY + ":green_dye";
	public static final String dyeWhiteBOP = DOM_BIOMESOPLENTY + ":white_dye";
	public static final String dyeBlackBOP = DOM_BIOMESOPLENTY + ":black_dye";

	// Kelp and Kelp Accessories
	public static final String kelpBOP = DOM_BIOMESOPLENTY + ":seaweed";
	
	// Mossy Stone
	
	// Mud
	public static final String mudBOP_classPath = "biomesoplenty.common.block.BlockBOPMud";
	
	// Prismarine
	public static final String prismarineStairs_Qu = DOM_QUARK + ":prismarine_stairs";
	public static final String prismarineSlab_Qu = DOM_QUARK + ":prismarine_slab";
	public static final String prismarineWall_Qu = DOM_QUARK + ":prismarine_rough_wall";
	
	// Smooth Sandstone
	public static final String smoothSandstoneQu = DOM_QUARK + ":sandstone_new";
	
	// Smooth Sandstone Slab
	public static final String smoothSandstoneSlabQu = DOM_QUARK + ":sandstone_smooth_slab";
	public static final String smoothRedSandstoneSlabQu = DOM_QUARK + ":red_sandstone_smooth_slab";
	
	// Smooth Stone 
	public static final String smoothStoneQu = DOM_QUARK + ":polished_stone";
	
	// Stairs
	public static final String dioriteStairs_Qu = DOM_QUARK + ":stone_diorite_stairs";
	public static final String graniteStairs_Qu = DOM_QUARK + ":stone_granite_stairs";
	// Brick stairs
	public static final String andesiteBrickStairs_Qu = DOM_QUARK + ":stone_andesite_bricks_stairs";
	public static final String dioriteBrickStairs_Qu = DOM_QUARK + ":stone_diorite_bricks_stairs";
	public static final String graniteBrickStairs_Qu = DOM_QUARK + ":stone_granite_bricks_stairs";
	
	// Trapdoor
	public static final String trapdoorSpruceQu = DOM_QUARK + ":spruce_trapdoor";
	public static final String trapdoorBirchQu = DOM_QUARK + ":birch_trapdoor";
	public static final String trapdoorJungleQu = DOM_QUARK + ":jungle_trapdoor";
	public static final String trapdoorAcaciaQu = DOM_QUARK + ":acacia_trapdoor";
	public static final String trapdoorDarkOakQu = DOM_QUARK + ":dark_oak_trapdoor";
	
	// Walls
	public static final String stoneBrickWall_Qu = DOM_QUARK + ":stonebrick_wall"; 
	public static final String sandstoneWall_white_Qu = DOM_QUARK + ":sandstone_wall"; 
	public static final String sandstoneWall_red_Qu = DOM_QUARK + ":red_sandstone_wall"; 
	public static final String dioriteWall_Qu = DOM_QUARK + ":stone_diorite_wall";
	public static final String graniteWall_Qu = DOM_QUARK + ":stone_granite_wall";
	
	
	
	// --------------------------- //
	// --- Generator Functions --- //
	// --------------------------- //
	
	
	// Andesite
	public static IBlockState chooseModAndesiteSlabBlock(boolean upper)
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.andesiteSlab_Qu);
		if (modblock != null) {return modblock.getStateFromMeta(upper? 8:0);}
		
		return null;
	}
	public static IBlockState chooseModAndesiteBrickslabBlock(boolean upper)
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.andesiteBricksSlab_Qu);
		if (modblock != null) {return modblock.getStateFromMeta(upper? 8:0);}
		
		return null;
	}
	public static Block chooseModAndesiteBrickStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.andesiteBrickStairs_Qu);
		if (modblock != null) {return modblock;}
		
		// TODO - Botania available in 1.10
		return null;
	}

	// Bamboo
	public static IBlockState chooseModBambooStalk()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.bambooStalk_BoP);
		if (modblock != null) {return modblock.getStateFromMeta(0);}
		
		return null;
	}
	// Shoot
	public static ItemStack chooseModBambooShoot()
	{
		Item moditem=null;
		
		moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.sapling0_BoP));
		if (moditem != null) {return new ItemStack(moditem, 1, 2);}
		
		return null;
	}
	// Leaves
	public static IBlockState chooseModBambooLeaves()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.bambooLeaves_BoP);
		if (modblock != null) {return modblock.getStateFromMeta(2);}
		
		return null;
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
	public static IBlockState chooseModCampfireBlockState(int relativeOrientation, EnumFacing coordBaseMode)
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.campfireTAN);
		if (modblock != null) {return modblock.getStateFromMeta(1);} // 1 is "lit"
		
		// No mod campfires exist. Return an upright torch.
		return Blocks.TORCH.getDefaultState();
	}
	public static Item chooseModCampfireItem()
	{
    	Item moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.campfireTAN));
		if (moditem != null) {return moditem;}
		
		return null;
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
	public static Block chooseModDioriteBrickStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.dioriteBrickStairs_Qu);
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
	public static Block chooseModGraniteBrickStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.graniteBrickStairs_Qu);
		if (modblock != null) {return modblock;}
		
		// TODO - Botania available in 1.10
		return null;
	}
	public static IBlockState chooseModDioriteSlabBlock(boolean upper)
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.dioriteSlab_Qu);
		if (modblock != null) {return modblock.getStateFromMeta(upper? 8:0);}
		
		return null;
	}
	public static IBlockState chooseModDioriteBrickslabBlock(boolean upper)
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.dioriteBricksSlab_Qu);
		if (modblock != null) {return modblock.getStateFromMeta(upper? 8:0);}
		
		return null;
	}
	public static Block chooseModPolishedDioriteStairsBlock()
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
	
	// Fletching Table
	public static IBlockState chooseModFletchingTableState()
	{
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Flowers
	public static IBlockState chooseModCornflower()
	{
		return null;
	}
	public static IBlockState chooseModLilyOfTheValley()
	{
		return null;
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
	public static IBlockState chooseModGrindstone(int orientation, EnumFacing coordBaseMode, boolean isHanging)
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
	
	
	// Mossy stone
	public static IBlockState chooseModMossyStoneBrickWallState()
	{
		return null;
	}
	public static Block chooseModMossyCobblestoneStairsBlock()
	{
		return null;
	}
	public static Block chooseModMossyStoneBrickStairsBlock()
	{
		return null;
	}
	public static IBlockState chooseModMossyStoneBrickSlabState(boolean upper)
	{
		return null;
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
	
	// Prismarine
	public static Block chooseModPrismarineStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.prismarineStairs_Qu);
		if (modblock != null) {return modblock;}
		
		return null;
	}
	public static IBlockState chooseModPrismarineSlabBlock(boolean upper)
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.prismarineSlab_Qu);
		if (modblock != null) {return modblock.getStateFromMeta(upper? 8:0);}
		
		return null;
	}
	public static IBlockState chooseModPrismarineWallBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.prismarineWall_Qu);
		if (modblock != null) {return modblock.getDefaultState();}
		
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
	
	
	// Stone brick wall
	public static IBlockState chooseModStoneBrickWallState()
	{
		Block modblock=null;

		modblock = Block.getBlockFromName(ModObjects.stoneBrickWall_Qu);
		if (modblock != null) {return modblock.getDefaultState();}
		
		return null;
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
