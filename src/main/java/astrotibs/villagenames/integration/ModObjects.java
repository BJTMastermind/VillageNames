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
	public static final String DOM_ANIMANIA = "animania";
	public static final String DOM_BIOMESOPLENTY = "biomesoplenty";
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
  	
	public static final String TQShopkeeperClass = "net.torocraft.toroquest.entities.EntityShopkeeper"; //Counts as a Villager
	public static final String TQFugitiveClass = "net.torocraft.toroquest.entities.EntityFugitive"; //Counts as a Villager
	public static final String TQGuardClass = "net.torocraft.toroquest.entities.EntityGuard";
	public static final String TQSentryClass = "net.torocraft.toroquest.entities.EntitySentry";
	public static final String TQVillageLordClass = "net.torocraft.toroquest.entities.EntityVillageLord";

	
	// Added in v3.1trades
 	
	// --------------------------------------------- //
	// --- Blocks and items reference for trades --- //
	// --------------------------------------------- //
	
	// Andesite
	public static final String andesiteSlab_Qu = DOM_QUARK + ":stone_andesite_slab";
	public static final String andesiteBrickSlab_Qu = DOM_QUARK + ":stone_andesite_bricks_slab";
	
	// Bamboo
	// Stalks (Blocks)
	public static final String bambooStalk_BoP = DOM_BIOMESOPLENTY + ":bamboo";
	public static final String bambooStalk_GC = "growthcraft_bamboo:bamboo_stalk";
	// Saplings (Items)
	public static final String sapling_BoP = DOM_BIOMESOPLENTY + ":sapling_0"; // Meta 2
	public static final String bambooShoot_GC = "growthcraft_bamboo:bamboo_shoot";
	// Leaves
	public static final String bambooLeaves_BoP = DOM_BIOMESOPLENTY + ":leaves_0"; // Meta 2
	public static final String bambooLeaves_GC = "growthcraft_bamboo:bamboo_leaves";
	
	// Bark
	public static final String barkQu = DOM_QUARK + ":bark";

	// Bed
	public static final String bedQu = DOM_QUARK + ":colored_bed_item";
	
	// Campfire
	public static final String campfireTAN = "toughasnails:campfire";
	
	// Colored Bed
	public static final String coloredBedBlock_root_Qu = DOM_QUARK + ":colored_bed_";
	
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
	public static final String cropKaleJAFFA = "jaffa:kalecrop";

	// Desk
	public static final String deskBC = "bibliocraft:desk";
	
	// Diorite
	public static final String dioriteSlab_Qu = DOM_QUARK + ":stone_diorite_slab";
	public static final String dioriteBrickSlab_Qu = DOM_QUARK + ":stone_diorite_bricks_slab";

	// Dye
	public static final String dyeBlueBOP = DOM_BIOMESOPLENTY + ":blue_dye";
	public static final String dyeBrownBOP = DOM_BIOMESOPLENTY + ":brown_dye";
	public static final String dyeGreenBOP = DOM_BIOMESOPLENTY + ":green_dye";
	public static final String dyeWhiteBOP = DOM_BIOMESOPLENTY + ":white_dye";
	public static final String dyeBlackBOP = DOM_BIOMESOPLENTY + ":black_dye";
	
	// Kelp and Kelp Accessories
	public static final String kelpBOP = DOM_BIOMESOPLENTY + ":seaweed";
	
	// Mossy Stone
	public static final String mossyCobblestoneStairsVBE = DOM_VANILLABUILDERSEXTENSION + ":stairsstonemoss";
	public static final String mossyStoneBrickStairsVBE = DOM_VANILLABUILDERSEXTENSION + ":stairsstonebrickmossy";
	public static final String mossyStoneBrickWallVBE = DOM_VANILLABUILDERSEXTENSION + ":wallstonebrickmossy";
	public static final String mossyCobblestoneSlabVBE = DOM_VANILLABUILDERSEXTENSION + ":slabstonemoss";
	public static final String mossyStoneBrickSlabVBE = DOM_VANILLABUILDERSEXTENSION + ":slabstonebrickmossy";
	
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
	public static final String smoothSandstoneSlabVBE = DOM_VANILLABUILDERSEXTENSION + ":slabsandstonesmooth";
	public static final String smoothRedSandstoneSlabQu = DOM_QUARK + ":red_sandstone_smooth_slab";
	public static final String smoothRedSandstoneSlabVBE = DOM_VANILLABUILDERSEXTENSION + ":slabredsandstonesmooth";
	
	// Smooth Stone 
	public static final String smoothStoneQu = DOM_QUARK + ":polished_stone";
	
	// Stairs
	public static final String dioriteStairs_Qu = DOM_QUARK + ":stone_diorite_stairs";
	public static final String dioriteStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsdiorite";
	public static final String graniteStairs_Qu = DOM_QUARK + ":stone_granite_stairs";
	public static final String graniteStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsgranite";
	public static final String smoothSandstoneStairs_white_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairssandstonesmooth";
	public static final String smoothSandstoneStairs_red_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsredsandstonesmooth";
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
	public static final String sandstoneWall_white_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallsandstone";
	public static final String sandstoneWall_red_Qu = DOM_QUARK + ":red_sandstone_wall";
	public static final String sandstoneWall_red_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallredsandstone";
	public static final String dioriteWall_Qu = DOM_QUARK + ":stone_diorite_wall";
	public static final String dioriteWall_VBE = DOM_VANILLABUILDERSEXTENSION + ":walldiorite";
	public static final String graniteWall_Qu = DOM_QUARK + ":stone_granite_wall";
	public static final String graniteWall_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallgranite";
	
	
	
	// ------------------------ //
	// --- Animania animals --- //
	// ------------------------ //
	
	// --- Chicken --- //
	// Male
	// Female
	// Baby
	
	// --- Cow --- //
	// Male
	public static final String bull_highland = DOM_ANIMANIA + ":bull_highland";
	public static final String bull_jersey = DOM_ANIMANIA + ":bull_jersey";
	public static final String bull_mooshroom = DOM_ANIMANIA + ":bull_mooshroom";
	// Female
	public static final String cow_highland = DOM_ANIMANIA + ":cow_highland";
	public static final String cow_jersey = DOM_ANIMANIA + ":cow_jersey";
	public static final String cow_mooshroom = DOM_ANIMANIA + ":cow_mooshroom";
	// Baby
	public static final String calf_highland = DOM_ANIMANIA + ":calf_highland";
	public static final String calf_jersey = DOM_ANIMANIA + ":calf_jersey";
	public static final String calf_mooshroom = DOM_ANIMANIA + ":calf_mooshroom";
	
	// --- Goat --- //
	// Male
	public static final String buck_alpine = DOM_ANIMANIA + ":buck_alpine";
	public static final String buck_angora = DOM_ANIMANIA + ":buck_angora";
	public static final String buck_fainting = DOM_ANIMANIA + ":buck_fainting";
	public static final String buck_kiko = DOM_ANIMANIA + ":buck_kiko";
	public static final String buck_kinder = DOM_ANIMANIA + ":buck_kinder";
	public static final String buck_nigerian_dwarf = DOM_ANIMANIA + ":buck_nigerian_dwarf";
	public static final String buck_pygmy = DOM_ANIMANIA + ":buck_pygmy";
	// Female
	public static final String doe_alpine = DOM_ANIMANIA + ":doe_alpine";
	public static final String doe_angora = DOM_ANIMANIA + ":doe_angora";
	public static final String doe_fainting = DOM_ANIMANIA + ":doe_fainting";
	public static final String doe_kiko = DOM_ANIMANIA + ":doe_kiko";
	public static final String doe_kinder = DOM_ANIMANIA + ":doe_kinder";
	public static final String doe_nigerian_dwarf = DOM_ANIMANIA + ":doe_nigerian_dwarf";
	public static final String doe_pygmy = DOM_ANIMANIA + ":doe_pygmy";
	// Baby
	public static final String kid_alpine = DOM_ANIMANIA + ":kid_alpine";
	public static final String kid_angora = DOM_ANIMANIA + ":kid_angora";
	public static final String kid_fainting = DOM_ANIMANIA + ":kid_fainting";
	public static final String kid_kiko = DOM_ANIMANIA + ":kid_kiko";
	public static final String kid_kinder = DOM_ANIMANIA + ":kid_kinder";
	public static final String kid_nigerian_dwarf = DOM_ANIMANIA + ":kid_nigerian_dwarf";
	public static final String kid_pygmy = DOM_ANIMANIA + ":kid_pygmy";
	
	// --- Horse --- //
	// Male
	// Female
	// Baby
	
	// --- Pig --- //
	// Male
	// Female
	// Baby
	
	// --- Sheep --- //
	// Male
	public static final String ram_dorper = DOM_ANIMANIA + ":ram_dorper";
	public static final String ram_dorset = DOM_ANIMANIA + ":ram_dorset";
	public static final String ram_friesian = DOM_ANIMANIA + ":ram_friesian";
	public static final String ram_jacob = DOM_ANIMANIA + ":ram_jacob";
	public static final String ram_merino = DOM_ANIMANIA + ":ram_merino";
	public static final String ram_suffolk = DOM_ANIMANIA + ":ram_suffolk";
	// Female
	public static final String ewe_dorper = DOM_ANIMANIA + ":ewe_dorper";
	public static final String ewe_dorset = DOM_ANIMANIA + ":ewe_dorset";
	public static final String ewe_friesian = DOM_ANIMANIA + ":ewe_friesian";
	public static final String ewe_jacob = DOM_ANIMANIA + ":ewe_jacob";
	public static final String ewe_merino = DOM_ANIMANIA + ":ewe_merino";
	public static final String ewe_suffolk = DOM_ANIMANIA + ":ewe_suffolk";
	// Baby
	public static final String lamb_dorper = DOM_ANIMANIA + ":lamb_dorper";
	public static final String lamb_dorset = DOM_ANIMANIA + ":lamb_dorset";
	public static final String lamb_friesian = DOM_ANIMANIA + ":lamb_friesian";
	public static final String lamb_jacob = DOM_ANIMANIA + ":lamb_jacob";
	public static final String lamb_merino = DOM_ANIMANIA + ":lamb_merino";
	public static final String lamb_suffolk = DOM_ANIMANIA + ":lamb_suffolk";
	
	
	public static final String[] animania_goat = new String[]{
		buck_fainting, buck_kiko, buck_kinder, buck_nigerian_dwarf, buck_pygmy,
		doe_fainting, doe_kiko, doe_kinder, doe_nigerian_dwarf, doe_pygmy,
		//kid_fainting, kid_kiko, kid_kinder, kid_nigerian_dwarf, kid_pygmy,
	};
	public static final String[] animania_sheep = new String[]{
		ram_dorper, ram_dorset, ram_friesian, ram_jacob, ram_merino, ram_suffolk,
		ewe_dorper, ewe_dorset, ewe_friesian, ewe_jacob, ewe_merino, ewe_suffolk,
		//lamb_dorper, lamb_dorset, lamb_friesian, lamb_jacob, lamb_merino, lamb_suffolk,
	};
	public static final String[] animania_mooshroom = new String[]{
		bull_mooshroom,
		cow_mooshroom,
		//calf_mooshroom,
	};
	
	
	// --------------------------- //
	// --- Generator Functions --- //
	// --------------------------- //

	
	// Andesite
	public static Block chooseModAndesiteBrickStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.andesiteBrickStairs_Qu);
		if (modblock != null) {return modblock;}
		
		return null;
	}
	public static IBlockState chooseModAndesiteSlabState(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.andesiteSlab_Qu);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return null;
	}
	public static IBlockState chooseModAndesiteBrickSlabState(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.andesiteBrickSlab_Qu);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return null;
	}
	
	// Bamboo
	public static IBlockState chooseModBambooStalk()
	{
		String[] modprioritylist = GeneralConfig.modBamboo;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("biomesoplenty"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooStalk_BoP);
				if (modblock != null) {return modblock.getStateFromMeta(0);}
			}
			else if (mod.toLowerCase().equals("growthcraft"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooStalk_GC);
				if (modblock != null) {return modblock.getStateFromMeta(0);}
			}
		}
		return null;
	}
	// Shoot
	public static ItemStack chooseModBambooShoot()
	{
		String[] modprioritylist = GeneralConfig.modBamboo;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().equals("biomesoplenty"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.sapling_BoP));
				if (moditem != null) {return new ItemStack(moditem, 1, 2);}
			}
			else if (mod.toLowerCase().equals("growthcraft"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.bambooShoot_GC));
				if (moditem != null) {return new ItemStack(moditem);}
			}
		}
		
		return null;
	}
	// Leaves
	public static IBlockState chooseModBambooLeaves()
	{
		String[] modprioritylist = GeneralConfig.modBamboo;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("biomesoplenty"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooLeaves_BoP);
				if (modblock != null) {return modblock.getStateFromMeta(2);}
			}
			else if (mod.toLowerCase().equals("growthcraft"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooLeaves_GC);
				if (modblock != null) {return modblock.getDefaultState();}
			}
		}
		
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
	public static ItemStack getModBedItemstack(int colorMeta)
	{
		Item moditem=FunctionsVN.getItemFromName(ModObjects.bedQu);
		
		if (moditem==null || colorMeta==14) {return new ItemStack(Items.BED);}
		else if (colorMeta==15) {colorMeta=14;} // Quark uses 14 for black because red is vanilla
		return new ItemStack(moditem, 1, colorMeta);
	}
	public static void setModBedBlock(World world, int x, int y, int z, int orientationMeta, int colorMeta)
	{
		BlockPos pos = new BlockPos(x, y, z);
		Block modblock=null;
		boolean setTE = false; // Flagged as true if you need to set a tile entity
		
		String quarkBedColorSuffix = "";
		switch(colorMeta)
		{
		case 0: quarkBedColorSuffix = "white"; break; // white
		case 1: quarkBedColorSuffix = "orange"; break;
		case 2: quarkBedColorSuffix = "magenta"; break;
		case 3: quarkBedColorSuffix = "light_blue"; break;
		case 4: quarkBedColorSuffix = "yellow"; break;
		case 5: quarkBedColorSuffix = "lime"; break; // lime
		case 6: quarkBedColorSuffix = "pink"; break;
		case 7: quarkBedColorSuffix = "gray"; break;
		case 8: quarkBedColorSuffix = "silver"; break;
		case 9: quarkBedColorSuffix = "cyan"; break;
		case 10: quarkBedColorSuffix = "purple"; break;
		case 11: quarkBedColorSuffix = "blue"; break;
		case 12: quarkBedColorSuffix = "brown"; break;
		case 13: quarkBedColorSuffix = "green"; break;
		case 15: quarkBedColorSuffix = "black"; break;
		}
		
		modblock = Block.getBlockFromName(ModObjects.coloredBedBlock_root_Qu+quarkBedColorSuffix);
		if (modblock == null || colorMeta==14) {modblock = Blocks.BED;}
		
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
	public static IBlockState chooseModComposterState()
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
	public static Block chooseModDioriteBrickStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.dioriteBrickStairs_Qu);
		if (modblock != null) {return modblock;}
		
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
	public static IBlockState chooseModDioriteSlabState(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.dioriteSlab_Qu);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return null;
	}
	public static IBlockState chooseModDioriteBrickSlabState(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.dioriteBrickSlab_Qu);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
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
	public static Block chooseModGraniteBrickStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.graniteBrickStairs_Qu);
		if (modblock != null) {return modblock;}
		
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
	
	
	// Mossy Stone
	public static Block chooseModMossyCobblestoneStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.mossyCobblestoneStairsVBE);
		if (modblock != null) {return modblock;}
		
		return null;
	}
	public static Block chooseModMossyStoneBrickStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.mossyStoneBrickStairsVBE);
		if (modblock != null) {return modblock;}
		
		return null;
	}
	public static IBlockState chooseModMossyStoneBrickWall()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.mossyStoneBrickWallVBE);
		if (modblock != null) {return modblock.getDefaultState();}
		
		return null;
	}
	public static IBlockState chooseModMossyCobblestoneSlab(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.mossyCobblestoneSlabVBE);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return null;
	}
	public static IBlockState chooseModMossyStoneBrickSlab(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickSlabVBE);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return null;
	}
	public static IBlockState chooseModMossyStoneBrickWallState()
	{
		return null;
	}
	
	// Prismarine Stairs
	public static Block chooseModPrismarineStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.prismarineStairs_Qu);
		if (modblock != null) {return modblock;}
		
		return null;
	}
	
	// Prismarine Slab
	/**
	 * Returns regular sandstone slab on a failure
	 */
	public static IBlockState chooseModPrismarineSlab(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.prismarineSlab_Qu);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return null;
	}
	
	// Prismarine Wall
	public static IBlockState chooseModPrismarineWallState()
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
		String[] modprioritylist = GeneralConfig.modSandstone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().equals("quark"))
			{
				modobject = Block.getBlockFromName(isred ? ModObjects.smoothRedSandstoneSlabQu : ModObjects.smoothSandstoneSlabQu);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(isred ? ModObjects.smoothRedSandstoneSlabVBE : ModObjects.smoothSandstoneSlabVBE);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
		}
		
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
	
	
	// Sandstone Wall - Added in 1.14
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
	
	
	// Sweet Berries - Added in 1.14
	public static ItemStack chooseModSweetBerriesItem()
	{
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
