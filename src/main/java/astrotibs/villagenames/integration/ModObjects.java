package astrotibs.villagenames.integration;

import java.util.Random;

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
import net.minecraft.nbt.NBTTagList;
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
	public static final String DOM_ANIMANIA = "animania";
	public static final String DOM_BIOMESOPLENTY = "biomesoplenty";
	public static final String DOM_CHOCOLATEQUESTREPOURED = "cqrepoured";
	public static final String DOM_FUTUREMC = "futuremc";
	public static final String DOM_FUTUREVERSIONS = "futureminecraf";
	public static final String DOM_HARVESTCRAFT = "harvestcraft";
	public static final String DOM_MACAWSFURNITURE = "mcwfurnitures";
	public static final String DOM_MRCRAYFISHSFURNITUREMOD = "cfm";
	public static final String DOM_QUARK = "quark";
	public static final String DOM_RUSTIC = "rustic";
	public static final String DOM_VANILLABUILDERSEXTENSION = "vbe";
	public static final String DOM_VARIEDCOMMODITIES = "variedcommodities";
	
	
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
	
	public static final String AM_DraftHorse_Stallion = "com.animania.addons.farm.common.entity.horses.HorseDraft$EntityStallionDraftHorse";
	public static final String AM_DraftHorse_Mare = "com.animania.addons.farm.common.entity.horses.HorseDraft$EntityMareDraftHorse";
	public static final String AM_DraftHorse_Foal = "com.animania.addons.farm.common.entity.horses.HorseDraft$EntityFoalDraftHorse";
	
	
 	
	// --------------------------------------------- //
	// --- Blocks and items reference for trades --- //
	// --------------------------------------------- //
	
	// Andesite
	public static final String andesiteSlab_Qu = DOM_QUARK + ":stone_andesite_slab";
	public static final String andesiteSlab_FV = DOM_FUTUREVERSIONS + ":andesiteslab";

	public static final String andesiteBrickSlab_Qu = DOM_QUARK + ":stone_andesite_bricks_slab";
	
	public static final String polishedAndesiteSlab_VBE = DOM_VANILLABUILDERSEXTENSION + ":slabandesitesmooth";
	public static final String polishedAndesiteSlab_FV = DOM_FUTUREVERSIONS + ":polishedandesiteslab";

	// Bamboo
	// Stalks (Blocks)
	public static final String bambooStalk_BoP = DOM_BIOMESOPLENTY + ":bamboo";
	public static final String bambooStalk_GC = "growthcraft_bamboo:bamboo_stalk";
	public static final String bambooStalk_Sa = "sakura:bamboo";
	public static final String bambooStalk_Bam = "bamboozled:bamboo";
	public static final String bambooStalk_FMC = DOM_FUTUREMC + ":bamboo";
	public static final String bambooStalk_FV = DOM_FUTUREVERSIONS + ":bamboo2";
	// Shoots
	public static final String sapling_BoP = DOM_BIOMESOPLENTY + ":sapling_0"; // Meta 2
	public static final String bambooShoot_GC = "growthcraft_bamboo:bamboo_shoot";
	public static final String bambooShoot_Sa = "sakura:bamboo_shoot";
	// Leaves
	public static final String bambooLeaves_BoP = DOM_BIOMESOPLENTY + ":leaves_0"; // Meta 2
	public static final String bambooLeaves_GC = "growthcraft_bamboo:bamboo_leaves";
	
	// Bark
	public static final String woodQu = DOM_QUARK + ":bark";
	public static final String woodFV_oak = DOM_FUTUREVERSIONS + ":oakwood";
	public static final String woodFV_spruce = DOM_FUTUREVERSIONS + ":sprucewood";
	public static final String woodFV_birch = DOM_FUTUREVERSIONS + ":birchwood";
	public static final String woodFV_jungle = DOM_FUTUREVERSIONS + ":junglewood";
	public static final String woodFV_acacia = DOM_FUTUREVERSIONS + ":acaciawood";
	public static final String woodFV_dark_oak = DOM_FUTUREVERSIONS + ":darkoakwood";
	
	// Barrel
	public static final String barrelFMC = DOM_FUTUREMC + ":barrel";
	public static final String barrelFV = DOM_FUTUREVERSIONS + ":barrel";

	// Bell
	public static final String bellFMC = DOM_FUTUREMC + ":bell";
	public static final String bellFV = DOM_FUTUREVERSIONS + ":bell";
	
	// Blast Furnace
	public static final String blastFurnaceFMC = DOM_FUTUREMC + ":blast_furnace";
	public static final String blastFurnaceFV = DOM_FUTUREVERSIONS + ":blastfurnace";
	
	// Blue Ice
	public static final String blueIceFMC = DOM_FUTUREMC + ":blue_ice";
	public static final String blueIceFV = DOM_FUTUREVERSIONS + ":blueice";
	
	// Buttons
	public static final String buttonSpruceQu = DOM_QUARK + ":spruce_button";
	public static final String buttonBirchQu = DOM_QUARK + ":birch_button";
	public static final String buttonJungleQu = DOM_QUARK + ":jungle_button";
	public static final String buttonAcaciaQu = DOM_QUARK + ":acacia_button";
	public static final String buttonDarkOakQu = DOM_QUARK + ":dark_oak_button";
	
	// Campfire
	public static final String campfireFMC = DOM_FUTUREMC + ":campfire";
	public static final String campfireFV = DOM_FUTUREVERSIONS + ":campfire";
	public static final String campfireTAN = "toughasnails:campfire";
	public static final String campfire_JAC = "jac:campfire_lit";
	// Future Versions's Campfire sucks

	// Cartography Table
	public static final String cartographyTableFMC = DOM_FUTUREMC + ":cartography_table";
	public static final String cartographyTableFV = DOM_FUTUREVERSIONS + ":cartographytable";
	
	// Composter
	public static final String composterFMC = DOM_FUTUREMC + ":composter";
	public static final String composterFV = DOM_FUTUREVERSIONS + ":composter";
	
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
	
	// Diorite
	public static final String dioriteSlab_Qu = DOM_QUARK + ":stone_diorite_slab";
	public static final String dioriteSlab_FV = DOM_FUTUREVERSIONS + ":dioriteslab";
	
	public static final String dioriteBrickSlab_Qu = DOM_QUARK + ":stone_diorite_bricks_slab";
	
	public static final String polishedDioriteSlab_VBE = DOM_VANILLABUILDERSEXTENSION + ":slabdioritesmooth";
	public static final String polishedDioriteSlab_FV = DOM_FUTUREVERSIONS + ":polisheddioriteslab";
	
	// Dusty Book
	public static final String dustyBook_LB = "lostbooks:random_book";
	
	// Dye
	public static final String dyeBlueBOP = DOM_BIOMESOPLENTY + ":blue_dye";
	public static final String dyeBrownBOP = DOM_BIOMESOPLENTY + ":brown_dye";
	//public static final String dyeGreenBOP = DOM_BIOMESOPLENTY + ":green_dye";
	public static final String dyeWhiteBOP = DOM_BIOMESOPLENTY + ":white_dye";
	public static final String dyeBlackBOP = DOM_BIOMESOPLENTY + ":black_dye";
	public static final String dyeFMC = DOM_FUTUREMC + ":dye"; // 0: white, 1: blue, 2: brown, 3: black
	public static final String dyeQuark = DOM_QUARK + ":root_dye"; // 0: blue, 1: black, 2: white
	public static final String dyeBotania = "botania:dye"; // 0: white, 11: blue, 12: brown, 15: black
	public static final String dyeBlackFV = DOM_FUTUREVERSIONS + ":blackdye";
	public static final String dyeBlueFV = DOM_FUTUREVERSIONS + ":bluedye";
	public static final String dyeBrownFV = DOM_FUTUREVERSIONS + ":browndye";
	public static final String dyeWhiteFV = DOM_FUTUREVERSIONS + ":whitedye";
	
	// Fletching Table
	public static final String fletchingTableFMC = DOM_FUTUREMC + ":fletching_table";
	public static final String fletchingTableFV = DOM_FUTUREVERSIONS + ":fletchingtable";
	
	// Flowers
	public static final String flowerCornflowerFMC = DOM_FUTUREMC + ":cornflower";
	public static final String flowerLilyOfTheValleyFMC = DOM_FUTUREMC + ":lily_of_the_valley";
	public static final String flowerCornflowerFV = DOM_FUTUREVERSIONS + ":cornflower";
	public static final String flowerLilyOfTheValleyFV = DOM_FUTUREVERSIONS + ":lilyofthevalley";
	
	// Grindstone
	public static final String grindstoneFMC = DOM_FUTUREMC + ":grindstone";
	public static final String grindstoneFV = DOM_FUTUREVERSIONS + ":grindstone";
	
	// Kelp and Kelp Accessories
	public static final String kelpBOP = DOM_BIOMESOPLENTY + ":seaweed";
	public static final String driedKelpBlock = DOM_FUTUREVERSIONS + ":driedkelpblock";
	
	// Lantern
	public static final String lanternFMC = DOM_FUTUREMC + ":lantern";
	public static final String lanternCh = "charm:iron_lantern";
	public static final String lanternFV = DOM_FUTUREVERSIONS + ":lantern";
	
	// Lectern
	public static final String lecternFV = DOM_FUTUREVERSIONS + ":lectern";
	
	// Loom
	public static final String loomFMC = DOM_FUTUREMC + ":loom";
	public static final String loomFV = DOM_FUTUREVERSIONS + ":loom";

	// Mossy Stone
	public static final String mossyStoneBrickWall_Qu = DOM_QUARK + ":stonebrick_mossy_wall";
	public static final String mossyStoneBrickWall_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallstonebrickmossy";
	public static final String mossyStoneBrickWall_FMC = DOM_FUTUREMC + ":mossy_stone_brick_wall";
	public static final String mossyStoneBrickWall_FV = DOM_FUTUREVERSIONS + ":mossystonebrickwall";
	public static final String mossyCobblestoneSlab_VBE = DOM_VANILLABUILDERSEXTENSION + ":slabstonemoss";
	public static final String mossyCobblestoneSlab_FV = DOM_FUTUREVERSIONS + ":mossycobblestoneslab";
	public static final String mossyStoneBrickSlab_Qu = DOM_QUARK + ":stonebrick_mossy_slab";
	public static final String mossyStoneBrickSlab_VBE = DOM_VANILLABUILDERSEXTENSION + ":slabstonebrickmossy";
	public static final String mossyStoneBrickSlab_FV = DOM_FUTUREVERSIONS + ":mossystonebrickslab";
	
	public static final String mossyCobblestoneStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsstonemoss";
	public static final String mossyCobblestoneStairs_FV = DOM_FUTUREVERSIONS + ":mossycobblestonestairs";
	
	public static final String mossyStoneBrickStairs_Qu = DOM_QUARK + ":stonebrick_mossy_stairs";
	public static final String mossyStoneBrickStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsstonebrickmossy";
	public static final String mossyStoneBrickStairs_FV = DOM_FUTUREVERSIONS + ":mossystonebrickstairs";
	
	// Mud
	public static final String mudBOP_classPath = "biomesoplenty.common.block.BlockBOPMud";

	// Pressure Plates
	public static final String pressurePlateSpruceQu = DOM_QUARK + ":spruce_pressure_plate";
	public static final String pressurePlateBirchQu = DOM_QUARK + ":birch_pressure_plate";
	public static final String pressurePlateJungleQu = DOM_QUARK + ":jungle_pressure_plate";
	public static final String pressurePlateAcaciaQu = DOM_QUARK + ":acacia_pressure_plate";
	public static final String pressurePlateDarkOakQu = DOM_QUARK + ":dark_oak_pressure_plate";

	// Prismarine
	public static final String prismarineStairs_Qu = DOM_QUARK + ":prismarine_stairs";
	public static final String prismarineStairs_FV = DOM_FUTUREVERSIONS + ":stairsPrismarineRough";
	public static final String prismarineSlab_Qu = DOM_QUARK + ":prismarine_slab";
	public static final String prismarineSlab_FV = DOM_FUTUREVERSIONS + ":slabPrismarineRough";
	public static final String prismarineWall_Qu = DOM_QUARK + ":prismarine_rough_wall";
	public static final String prismarineWall_FV = DOM_FUTUREVERSIONS + ":wallPrismarineRough";
	
	// Smithing Table
	public static final String smithingTableFMC = DOM_FUTUREMC + ":smithing_table";
	public static final String smithingTableFV = DOM_FUTUREVERSIONS + ":smithingtable";
	
	// Smoker
	public static final String smokerFMC = DOM_FUTUREMC + ":smoker";
	public static final String smokerFV = DOM_FUTUREVERSIONS + ":smoker";
	
	// Smooth Sandstone
	public static final String smoothSandstone_white_Qu = DOM_FUTUREMC + ":smooth_sandstone";
	public static final String smoothSandstone_red_Qu = DOM_FUTUREMC + ":smooth_red_sandstone";
	public static final String smoothSandstone_white_FMC = DOM_FUTUREMC + ":smooth_sandstone";
	public static final String smoothSandstone_red_FMC = DOM_FUTUREMC + ":smooth_red_sandstone";
	public static final String smoothSandstone_white_FV = DOM_FUTUREVERSIONS + ":smoothsandstone";
	public static final String smoothSandstone_red_FV = DOM_FUTUREVERSIONS + ":redsmoothsandstone";
	
	// Smooth Sandstone Slab
	public static final String smoothSandstoneSlabQu = DOM_QUARK + ":sandstone_smooth_slab";
	public static final String smoothRedSandstoneSlabQu = DOM_QUARK + ":red_sandstone_smooth_slab";
	public static final String smoothSandstoneSlabFV = DOM_FUTUREVERSIONS + ":smoothsandstoneslab";
	public static final String smoothRedSandstoneSlabFV = DOM_FUTUREVERSIONS + ":redsmoothsandstoneslab";
	
	// Smooth Stone 
	public static final String smoothStoneQu = DOM_QUARK + ":polished_stone";
	public static final String smoothStoneFMC = DOM_FUTUREMC + ":smooth_stone";
	public static final String smoothStoneFV = DOM_FUTUREVERSIONS + ":smoothstone";
	
	// Stairs
	public static final String andesiteStairs_Qu = DOM_QUARK + ":stone_andesite_stairs";
	public static final String andesiteStairs_FV = DOM_FUTUREVERSIONS + ":andesitestair";
	public static final String polishedAndesiteStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsandesitesmooth";
	public static final String polishedAndesiteStairs_FV = DOM_FUTUREVERSIONS + ":polishedandesitestair";
	
	public static final String dioriteStairs_Qu = DOM_QUARK + ":stone_diorite_stairs";
	public static final String dioriteStairs_FV = DOM_FUTUREVERSIONS + ":dioritestair";
	public static final String polishedDioriteStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsdioritesmooth";
	public static final String polishedDioriteStairs_FV = DOM_FUTUREVERSIONS + ":polisheddioritestair";
	
	public static final String graniteStairs_Qu = DOM_QUARK + ":stone_granite_stairs";
	public static final String graniteStairs_FV = DOM_FUTUREVERSIONS + ":granitestair";
	public static final String polishedGraniteStairs_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsgranitesmooth";
	public static final String polishedGraniteStairs_FV = DOM_FUTUREVERSIONS + ":polishedgranitestair";
	
	public static final String andesiteBrickStairs_Qu = DOM_QUARK + ":stone_andesite_bricks_stairs";
	public static final String dioriteBrickStairs_Qu = DOM_QUARK + ":stone_diorite_bricks_stairs";
	public static final String graniteBrickStairs_Qu = DOM_QUARK + ":stone_granite_bricks_stairs";
	
	public static final String smoothSandstoneStairs_white_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairssandstonesmooth";
	public static final String smoothSandstoneStairs_red_VBE = DOM_VANILLABUILDERSEXTENSION + ":stairsredsandstonesmooth";
	public static final String smoothSandstoneStairs_white_FV = DOM_FUTUREVERSIONS + ":smoothsandstonestairs";
	public static final String smoothSandstoneStairs_red_FV = DOM_FUTUREVERSIONS + ":smoothredsandstonestairs";
	
	
	// Stone cutter
	public static final String stoneCutterFMC = DOM_FUTUREMC + ":stonecutter";
	public static final String stoneCutterFV = DOM_FUTUREVERSIONS + ":stonecutter";
	
	// Stripped log
	public static final String strippedLogOakFMC = DOM_FUTUREMC + ":stripped_oak_log";
	public static final String strippedLogSpruceFMC = DOM_FUTUREMC + ":stripped_spruce_log";
	public static final String strippedLogBirchFMC = DOM_FUTUREMC + ":stripped_birch_log";
	public static final String strippedLogJungleFMC = DOM_FUTUREMC + ":stripped_jungle_log";
	public static final String strippedLogAcaciaFMC = DOM_FUTUREMC + ":stripped_acacia_log";
	public static final String strippedLogDarkOakFMC = DOM_FUTUREMC + ":stripped_dark_oak_log";
	public static final String strippedLogOakVC = DOM_FUTUREVERSIONS + ":strippedoaklog";
	public static final String strippedLogSpruceVC = DOM_FUTUREVERSIONS + ":strippedbirchlog";
	public static final String strippedLogBirchVC = DOM_FUTUREVERSIONS + ":strippedsprucelog";
	public static final String strippedLogJungleVC = DOM_FUTUREVERSIONS + ":strippedjunglelogrecipe";
	public static final String strippedLogAcaciaVC = DOM_FUTUREVERSIONS + ":strippedacaialog";
	public static final String strippedLogDarkOakVC = DOM_FUTUREVERSIONS + ":strippeddarkoaklog";
	
	// Stripped Wood
	public static final String strippedWoodOakFMC = DOM_FUTUREMC + ":stripped_oak_wood";
	public static final String strippedWoodSpruceFMC = DOM_FUTUREMC + ":stripped_spruce_wood";
	public static final String strippedWoodBirchFMC = DOM_FUTUREMC + ":stripped_birch_wood";
	public static final String strippedWoodJungleFMC = DOM_FUTUREMC + ":stripped_jungle_wood";
	public static final String strippedWoodAcaciaFMC = DOM_FUTUREMC + ":stripped_acacia_wood";
	public static final String strippedWoodDarkOakFMC = DOM_FUTUREMC + ":stripped_dark_oak_wood";
	public static final String strippedWoodOakFV = DOM_FUTUREVERSIONS + ":strippedoakwood";
	public static final String strippedWoodSpruceFV = DOM_FUTUREVERSIONS + ":strippedsprucewood";
	public static final String strippedWoodBirchFV = DOM_FUTUREVERSIONS + ":strippedbirchwood";
	public static final String strippedWoodJungleFV = DOM_FUTUREVERSIONS + ":strippedjunglewood";
	public static final String strippedWoodAcaciaFV = DOM_FUTUREVERSIONS + ":strippedacaciawood";
	public static final String strippedWoodDarkOakFV = DOM_FUTUREVERSIONS + ":strippeddarkoakwood";
		
	// Suspicious Stew
	public static final String suspiciousStewFMC = DOM_FUTUREMC + ":suspicious_stew";
	public static final String suspiciousStewFV_1 = DOM_FUTUREVERSIONS + ":suspiciousstew1"; // Regeneration
	public static final String suspiciousStewFV_2 = DOM_FUTUREVERSIONS + ":ss2"; // Jump Boost
	public static final String suspiciousStewFV_3 = DOM_FUTUREVERSIONS + ":ss3"; // Poison
	public static final String suspiciousStewFV_4 = DOM_FUTUREVERSIONS + ":ss4"; // Wither
	public static final String suspiciousStewFV_5 = DOM_FUTUREVERSIONS + ":ss5"; // Weakness
	public static final String suspiciousStewFV_6 = DOM_FUTUREVERSIONS + ":ss6"; // Blindness
	public static final String suspiciousStewFV_7 = DOM_FUTUREVERSIONS + ":ss7"; // Fire Resistance
	public static final String suspiciousStewFV_8 = DOM_FUTUREVERSIONS + ":ss8"; // Saturation
	public static final String suspiciousStewFV_9 = DOM_FUTUREVERSIONS + ":ss9"; // Speed
	public static final String suspiciousStewFV_10 = DOM_FUTUREVERSIONS + ":ss10"; // Saturation II
	
	// Sweet Berries
	public static final String sweetBerriesFMC = DOM_FUTUREMC + ":sweet_berries";
	public static final String sweetBerriesFV = DOM_FUTUREVERSIONS + ":sweetberry";

	// Trapdoor
	public static final String trapdoorSpruceQu = DOM_QUARK + ":spruce_trapdoor";
	public static final String trapdoorBirchQu = DOM_QUARK + ":birch_trapdoor";
	public static final String trapdoorJungleQu = DOM_QUARK + ":jungle_trapdoor";
	public static final String trapdoorAcaciaQu = DOM_QUARK + ":acacia_trapdoor";
	public static final String trapdoorDarkOakQu = DOM_QUARK + ":dark_oak_trapdoor";
	public static final String trapdoorSpruceFV = DOM_FUTUREVERSIONS + ":sprucetrapdoor";
	public static final String trapdoorBirchFV = DOM_FUTUREVERSIONS + ":birchtrapdoor";
	public static final String trapdoorJungleFV = DOM_FUTUREVERSIONS + ":jungletrapdoor";
	public static final String trapdoorAcaciaFV = DOM_FUTUREVERSIONS + ":acaciatrapdoor";
	public static final String trapdoorDarkOakFV = DOM_FUTUREVERSIONS + ":darkoaktrapdoor";
	
	// Walls
	public static final String stoneBrickWall_Qu = DOM_QUARK + ":stonebrick_wall";
	public static final String stoneBrickWall_FMC = DOM_FUTUREMC + ":stone_brick_wall";
	public static final String stoneBrickWall_FV = DOM_FUTUREVERSIONS + ":stonebrickwall";
	public static final String sandstoneWall_white_Qu = DOM_QUARK + ":sandstone_wall";
	public static final String sandstoneWall_white_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallsandstone";
	public static final String sandstoneWall_white_FMC = DOM_FUTUREMC + ":sandstone_wall"; 
	public static final String sandstoneWall_white_FV = DOM_FUTUREVERSIONS + ":sandstonewall"; 
	public static final String sandstoneWall_red_Qu = DOM_QUARK + ":red_sandstone_wall";
	public static final String sandstoneWall_red_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallredsandstone";
	public static final String sandstoneWall_red_FMC = DOM_FUTUREMC + ":red_sandstone_wall"; 
	public static final String sandstoneWall_red_FV = DOM_FUTUREVERSIONS + ":redsandstonewall"; 
	public static final String dioriteWall_Qu = DOM_QUARK + ":stone_diorite_wall";
	public static final String dioriteWall_VBE = DOM_VANILLABUILDERSEXTENSION + ":walldiorite";
	public static final String dioriteWall_FV = DOM_FUTUREVERSIONS + ":walldiorite";
	public static final String graniteWall_Qu = DOM_QUARK + ":stone_granite_wall";
	public static final String graniteWall_VBE = DOM_VANILLABUILDERSEXTENSION + ":wallgranite";
	public static final String graniteWall_FV = DOM_FUTUREVERSIONS + ":wallgranite";

	// Wooden Table
	public static final String table_BC = "bibliocraft:table";
	public static final String table_oak_CQR = DOM_CHOCOLATEQUESTREPOURED+":table_oak";
	public static final String table_spruce_CQR = DOM_CHOCOLATEQUESTREPOURED+":table_spruce";
	public static final String table_birch_CQR = DOM_CHOCOLATEQUESTREPOURED+":table_birch";
	public static final String table_jungle_CQR = DOM_CHOCOLATEQUESTREPOURED+":table_jungle";
	public static final String table_acacia_CQR = DOM_CHOCOLATEQUESTREPOURED+":table_acacia";
	public static final String table_dark_oak_CQR = DOM_CHOCOLATEQUESTREPOURED+":table_dark";
	public static final String table_oak_MCF = DOM_MACAWSFURNITURE+":nightstand_11";
	public static final String table_spruce_MCF = DOM_MACAWSFURNITURE+":spruce_nightstand_11";
	public static final String table_birch_MCF = DOM_MACAWSFURNITURE+":birch_nightstand_11";
	public static final String table_jungle_MCF = DOM_MACAWSFURNITURE+":jungle_nightstand_11";
	public static final String table_acacia_MCF = DOM_MACAWSFURNITURE+":acacia_nightstand_11";
	public static final String table_dark_oak_MCF = DOM_MACAWSFURNITURE+":dark_oak_nightstand_11";
	public static final String table_oak_CFM = DOM_MRCRAYFISHSFURNITUREMOD+":table_oak";
	public static final String table_spruce_CFM = DOM_MRCRAYFISHSFURNITUREMOD+":table_spruce";
	public static final String table_birch_CFM = DOM_MRCRAYFISHSFURNITUREMOD+":table_birch";
	public static final String table_jungle_CFM = DOM_MRCRAYFISHSFURNITUREMOD+":table_jungle";
	public static final String table_acacia_CFM = DOM_MRCRAYFISHSFURNITUREMOD+":table_acacia";
	public static final String table_dark_oak_CFM = DOM_MRCRAYFISHSFURNITUREMOD+":table_dark_oak";
	public static final String table_oak_Rus = DOM_RUSTIC+":table_oak";
	public static final String table_spruce_Rus = DOM_RUSTIC+":table_spruce";
	public static final String table_birch_Rus = DOM_RUSTIC+":table_birch";
	public static final String table_jungle_Rus = DOM_RUSTIC+":table_jungle";
	public static final String table_acacia_Rus = DOM_RUSTIC+":table_acacia";
	public static final String table_dark_oak_Rus = DOM_RUSTIC+":table_big_oak";
	public static final String table_VC = DOM_VARIEDCOMMODITIES+":table";
	
	
	
	// ------------------------ //
	// --- Animania animals --- //
	// ------------------------ //
	
	// --- Chicken --- //
	// Male
	public static final String rooster_leghorn = DOM_ANIMANIA + ":rooster_leghorn";
	public static final String rooster_orpington = DOM_ANIMANIA + ":rooster_orpington";
	public static final String rooster_plymouth_rock = DOM_ANIMANIA + ":rooster_plymouth_rock";
	public static final String rooster_rhode_island_red = DOM_ANIMANIA + ":rooster_rhode_island_red";
	public static final String rooster_wyandotte = DOM_ANIMANIA + ":rooster_wyandotte";
	// Female
	public static final String hen_leghorn = DOM_ANIMANIA + ":hen_leghorn";
	public static final String hen_orpington = DOM_ANIMANIA + ":hen_orpington";
	public static final String hen_plymouth_rock = DOM_ANIMANIA + ":hen_plymouth_rock";
	public static final String hen_rhode_island_red = DOM_ANIMANIA + ":hen_rhode_island_red";
	public static final String hen_wyandotte = DOM_ANIMANIA + ":hen_wyandotte";
	// Baby
	public static final String chick_leghorn = DOM_ANIMANIA + ":chick_leghorn";
	public static final String chick_orpington = DOM_ANIMANIA + ":chick_orpington";
	public static final String chick_plymouth_rock = DOM_ANIMANIA + ":chick_plymouth_rock";
	public static final String chick_rhode_island_red = DOM_ANIMANIA + ":chick_rhode_island_red";
	public static final String chick_wyandotte = DOM_ANIMANIA + ":chick_wyandotte";
	
	// --- Cow --- //
	// Male
	public static final String bull_angus = DOM_ANIMANIA + ":bull_angus";
	public static final String bull_friesian = DOM_ANIMANIA + ":bull_friesian";
	public static final String bull_hereford = DOM_ANIMANIA + ":bull_hereford";
	public static final String bull_highland = DOM_ANIMANIA + ":bull_highland";
	public static final String bull_holstein = DOM_ANIMANIA + ":bull_holstein";
	public static final String bull_jersey = DOM_ANIMANIA + ":bull_jersey";
	public static final String bull_longhorn = DOM_ANIMANIA + ":bull_longhorn";
	public static final String bull_mooshroom = DOM_ANIMANIA + ":bull_mooshroom";
	// Female
	public static final String cow_angus = DOM_ANIMANIA + ":cow_angus";
	public static final String cow_friesian = DOM_ANIMANIA + ":cow_friesian";
	public static final String cow_hereford = DOM_ANIMANIA + ":cow_hereford";
	public static final String cow_highland = DOM_ANIMANIA + ":cow_highland";
	public static final String cow_holstein = DOM_ANIMANIA + ":cow_holstein";
	public static final String cow_jersey = DOM_ANIMANIA + ":cow_jersey";
	public static final String cow_longhorn = DOM_ANIMANIA + ":cow_longhorn";
	public static final String cow_mooshroom = DOM_ANIMANIA + ":cow_mooshroom";
	// Baby
	public static final String calf_angus = DOM_ANIMANIA + ":calf_angus";
	public static final String calf_friesian = DOM_ANIMANIA + ":calf_friesian";
	public static final String calf_hereford = DOM_ANIMANIA + ":calf_hereford";
	public static final String calf_highland = DOM_ANIMANIA + ":calf_highland";
	public static final String calf_holstein = DOM_ANIMANIA + ":calf_holstein";
	public static final String calf_jersey = DOM_ANIMANIA + ":calf_jersey";
	public static final String calf_longhorn = DOM_ANIMANIA + ":calf_longhorn";
	public static final String calf_mooshroom = DOM_ANIMANIA + ":calf_mooshroom";
	
	// --- Goat --- //
	// Male
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
	public static final String stallion_draft = DOM_ANIMANIA + ":stallion_draft";
	// Female
	public static final String mare_draft = DOM_ANIMANIA + ":mare_draft";
	// Baby
	public static final String foal_draft = DOM_ANIMANIA + ":foal_draft";
	
	// --- Pig --- //
	// Male
	public static final String hog_duroc = DOM_ANIMANIA + ":hog_duroc";
	public static final String hog_hampshire = DOM_ANIMANIA + ":hog_hampshire";
	public static final String hog_large_black = DOM_ANIMANIA + ":hog_large_black";
	public static final String hog_large_white = DOM_ANIMANIA + ":hog_large_white";
	public static final String hog_old_spot = DOM_ANIMANIA + ":hog_old_spot";
	public static final String hog_yorkshire = DOM_ANIMANIA + ":hog_yorkshire";
	// Female
	public static final String sow_duroc = DOM_ANIMANIA + ":sow_duroc";
	public static final String sow_hampshire = DOM_ANIMANIA + ":sow_hampshire";
	public static final String sow_large_black = DOM_ANIMANIA + ":sow_large_black";
	public static final String sow_large_white = DOM_ANIMANIA + ":sow_large_white";
	public static final String sow_old_spot = DOM_ANIMANIA + ":sow_old_spot";
	public static final String sow_yorkshire = DOM_ANIMANIA + ":sow_yorkshire";
	// Baby
	public static final String piglet_duroc = DOM_ANIMANIA + ":piglet_duroc";
	public static final String piglet_hampshire = DOM_ANIMANIA + ":piglet_hampshire";
	public static final String piglet_large_black = DOM_ANIMANIA + ":piglet_large_black";
	public static final String piglet_large_white = DOM_ANIMANIA + ":piglet_large_white";
	public static final String piglet_old_spot = DOM_ANIMANIA + ":piglet_old_spot";
	public static final String piglet_yorkshire = DOM_ANIMANIA + ":piglet_yorkshire";
	
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
	

	public static final String[] animania_chicken = new String[]{
		rooster_leghorn, rooster_orpington, rooster_plymouth_rock, rooster_rhode_island_red, rooster_wyandotte,
		hen_leghorn, hen_orpington, hen_plymouth_rock, hen_rhode_island_red, hen_wyandotte,
		//chick_leghorn, chick_orpington, chick_plymouth_rock, chick_rhode_island_red, chick_wyandotte,
	};
	public static final String[] animania_goat = new String[]{
		buck_fainting, buck_kiko, buck_kinder, buck_nigerian_dwarf, buck_pygmy,
		doe_fainting, doe_kiko, doe_kinder, doe_nigerian_dwarf, doe_pygmy,
		//kid_fainting, kid_kiko, kid_kinder, kid_nigerian_dwarf, kid_pygmy,
	};
	public static final String[] animania_pig = new String[]{
		hog_duroc, hog_hampshire, hog_large_black, hog_large_white, hog_old_spot, hog_yorkshire,
		sow_duroc, sow_hampshire, sow_large_black, sow_large_white, sow_old_spot, sow_yorkshire,
		//piglet_duroc, piglet_hampshire, piglet_large_black, piglet_large_white, piglet_old_spot, piglet_yorkshire,
	};
	public static final String[] animania_sheep = new String[]{
		ram_dorper, ram_dorset, ram_friesian, ram_jacob, ram_merino, ram_suffolk,
		ewe_dorper, ewe_dorset, ewe_friesian, ewe_jacob, ewe_merino, ewe_suffolk,
		//lamb_dorper, lamb_dorset, lamb_friesian, lamb_jacob, lamb_merino, lamb_suffolk,
	};
	public static final String[] animania_cow = new String[]{
		bull_angus, bull_friesian, bull_hereford, bull_highland, bull_holstein, bull_jersey, bull_longhorn, 
		cow_angus, cow_friesian, cow_hereford, cow_highland, cow_holstein, cow_jersey, cow_longhorn, 
		//calf_angus, calf_friesian, calf_hereford, calf_highland, calf_holstein, calf_jersey, calf_longhorn, 
	};
	public static final String[] animania_mooshroom = new String[]{
		bull_mooshroom,
		cow_mooshroom,
		//calf_mooshroom,
	};
	public static final String[] animania_horse = new String[]{
		stallion_draft, 
		mare_draft, 
		//foal_draft, 
	};
	
	
	// --------------------------- //
	// --- Generator Functions --- //
	// --------------------------- //
	
	
	// Andesite
	// Added in 1.14
	public static Block chooseModAndesiteStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.andesiteStairs_Qu);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.andesiteStairs_FV);
				if (modblock != null) {return modblock;}
			}
		}
		
		return null;
	}
	public static Block chooseModPolishedAndesiteStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.polishedAndesiteStairs_VBE);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.polishedAndesiteStairs_FV);
				if (modblock != null) {return modblock;}
			}
		}
		
		return null;
	}
	public static Block chooseModAndesiteBrickStairsBlock()
	{
		Block modblock=null;
		
		modblock = Block.getBlockFromName(ModObjects.andesiteBrickStairs_Qu);
		if (modblock != null) {return modblock;}
		
		return null;
	}
	public static IBlockState chooseModAndesiteSlabState(boolean upper)
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.andesiteSlab_Qu);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.andesiteSlab_FV);
				if (modobject != null) {return modobject.getStateFromMeta(upper?0:1);}
			}
		}
		
		return null;
	}
	public static IBlockState chooseModAndesiteBrickSlabState(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.andesiteBrickSlab_Qu);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return null;
	}
	public static IBlockState chooseModPolishedAndesiteSlabState(boolean upper)
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(ModObjects.polishedAndesiteSlab_VBE);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.polishedAndesiteSlab_FV);
				if (modobject != null) {return modobject.getStateFromMeta(upper?0:1);}
			}
		}
		
		return null;
	}
	
	// Bamboo
	public static IBlockState chooseModBambooStalk(int state)
	{
		String[] modprioritylist = GeneralConfig.modBamboo;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooStalk_FMC);
				if (modblock != null) {return modblock.getStateFromMeta(state);} // 0:thin base; 1-2:thin base with leaves; 3: thick base; 4-5:thick base with leaves
			}
			else if (mod.toLowerCase().trim().equals("biomesoplenty"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooStalk_BoP);
				if (modblock != null) {return modblock.getStateFromMeta(0);}
			}
			else if (mod.toLowerCase().trim().equals("growthcraft"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooStalk_GC);
				if (modblock != null) {return modblock.getStateFromMeta(0);}
			}
			else if (mod.toLowerCase().trim().equals("sakura"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooStalk_Sa);
				if (modblock != null) {return modblock.getStateFromMeta(state);} // 0 for base or 1 for cap
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooStalk_FV);
				if (modblock != null) {return modblock.getStateFromMeta(0);}
			}
			else if (mod.toLowerCase().trim().equals("bamboozled"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooStalk_Bam);
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
			
			if (mod.toLowerCase().trim().equals("biomesoplenty"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.sapling_BoP));
				if (moditem != null) {return new ItemStack(moditem, 1, 2);}
			}
			else if (mod.toLowerCase().trim().equals("growthcraft"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.bambooShoot_GC));
				if (moditem != null) {return new ItemStack(moditem);}
			}
			else if (mod.toLowerCase().trim().equals("sakura"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.bambooShoot_Sa));
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
			
			if (mod.toLowerCase().trim().equals("biomesoplenty"))
			{
				modblock = Block.getBlockFromName(ModObjects.bambooLeaves_BoP);
				if (modblock != null) {return modblock.getStateFromMeta(2);}
			}
			else if (mod.toLowerCase().trim().equals("growthcraft"))
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
		String[] modprioritylist = GeneralConfig.modBarrel;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.barrelFMC);
				if (modobject != null) {return new ItemStack(modobject);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.barrelFV);
				if (modobject != null) {return new ItemStack(modobject);}
			}
		}
		
		return null;
	}
	public static IBlockState chooseModBarrelBlockState(EnumFacing coordBaseMode, int orientationIfBarrel, int orientationIfChest)
	{
		String[] modprioritylist = GeneralConfig.modBarrel;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject=null;
				
				modobject = Block.getBlockFromName(ModObjects.barrelFMC);
				if (modobject != null) {return modobject.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(orientationIfBarrel, coordBaseMode));}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject=null;
				
				modobject = Block.getBlockFromName(ModObjects.barrelFV);
				if (modobject != null) {return modobject.getStateFromMeta(orientationIfBarrel==-1?3:
						(new int[][]{
							{5,1,5,1},
							{1,5,1,5},
							{5,1,5,1},
							{1,5,1,5},
						})[orientationIfBarrel][coordBaseMode.getHorizontalIndex()]
						);}
			}
		}
		
		return Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(orientationIfChest, coordBaseMode));
	}
	
	
	// Bark
	public static IBlockState chooseModWoodState(IBlockState blockstate)
	{
		boolean returnVanilla = false;
		
		String[] modprioritylist = GeneralConfig.modWood;
		
		if (blockstate.getBlock()==Blocks.LOG)
		{
			for (String mod : modprioritylist)
			{
				Block modblock=null;
				
				if (mod.toLowerCase().trim().equals("quark"))
				{
					modblock = Block.getBlockFromName(ModObjects.woodQu);
					if (modblock != null) {return modblock.getStateFromMeta(blockstate.getBlock().getMetaFromState(blockstate)%4);}
				}
				else if (mod.toLowerCase().trim().equals("futureversions"))
				{
					switch (blockstate.getBlock().getMetaFromState(blockstate)%4)
					{
					case 0: modblock = Block.getBlockFromName(ModObjects.woodFV_oak); break;
					case 1: modblock = Block.getBlockFromName(ModObjects.woodFV_spruce); break;
					case 2: modblock = Block.getBlockFromName(ModObjects.woodFV_birch); break;
					case 3: modblock = Block.getBlockFromName(ModObjects.woodFV_jungle); break;
					}
					if (modblock != null) {return modblock.getDefaultState();}
				}
				
				// Found no modded version
				returnVanilla = true;
			}
			
		}
		else if (blockstate.getBlock()==Blocks.LOG2)
		{
			for (String mod : modprioritylist)
			{
				Block modblock=null;
				
				if (mod.toLowerCase().trim().equals("quark"))
				{
					modblock = Block.getBlockFromName(ModObjects.woodQu);
					if (modblock != null) {return modblock.getStateFromMeta(blockstate.getBlock().getMetaFromState(blockstate)%4 + 4);}
				}
				else if (mod.toLowerCase().trim().equals("futureversions"))
				{
					switch (blockstate.getBlock().getMetaFromState(blockstate)%4)
					{
					case 0: modblock = Block.getBlockFromName(ModObjects.woodFV_acacia); break;
					case 1: modblock = Block.getBlockFromName(ModObjects.woodFV_dark_oak); break;
					}
					if (modblock != null) {return modblock.getDefaultState();}
				}
				
				// Found no modded version
				returnVanilla = true;
			}
		}
		
		// Modify the vanilla version to have bark on all sides
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
	
	// Bell
	public static Item chooseBellItem()
	{
		String[] modprioritylist = GeneralConfig.modBell;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Item modobject = FunctionsVN.getItemFromName(ModObjects.bellFMC);
				if (modobject != null) {return modobject;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Item modobject = FunctionsVN.getItemFromName(ModObjects.bellFV);
				if (modobject != null) {return modobject;}
			}
		}
		
		return null;
	}
	
	
	// Blackstone
	public static Block chooseModPolishedBlackstoneButton()
	{
		return null;
	}
	
	
	// Blast Furnace
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModBlastFurnaceState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		String[] modprioritylist = GeneralConfig.modBlastFurnace;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.blastFurnaceFMC);
		    	if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseBlastFurnaceMeta(furnaceOrientation, coordBaseMode));}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.blastFurnaceFV);
		    	if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode));}
			}
		}
		
		return Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode));
	}
	
	
	// Blue Ice
	// Added in 1.13
    public static IBlockState chooseModBlueIceBlockState()
    {
    	String[] modprioritylist = GeneralConfig.modBlueIce;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modblock = Block.getBlockFromName(ModObjects.blueIceFMC);
				if (modblock!=null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.blueIceFV);
				if (modblock!=null) {return modblock.getDefaultState();}
			}
		}
    	
		// None are found, so return ordinary packed ice
		return Blocks.PACKED_ICE.getDefaultState();
    }
	
    
	// Brick Wall
	public static IBlockState chooseModBrickWallState()
	{
		return null;
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
			
			if (mod.toLowerCase().trim().equals("futuremc"))
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
			else if (mod.toLowerCase().trim().equals("justacampfire"))
			{
				modblock = Block.getBlockFromName(ModObjects.campfire_JAC);
				
				if (modblock!=null)
				{
					int horizIndex = coordBaseMode.getHorizontalIndex();
					int campfireMeta=0;
					
		    		switch (relativeOrientation)
		    		{
		    		case 0: // Facing away
		    			campfireMeta = new int[]{2,3,0,1}[MathHelper.clamp(horizIndex,0,3)]; break;
		    		case 1: // Facing right
		    			campfireMeta = new int[]{1,2,1,2}[MathHelper.clamp(horizIndex,0,3)]; break;
		    		case 2: // Facing you
		    			campfireMeta = new int[]{0,1,2,3}[MathHelper.clamp(horizIndex,0,3)]; break;
		    		case 3: // Facing left
		    			campfireMeta = new int[]{3,0,3,0}[MathHelper.clamp(horizIndex,0,3)]; break;
		    		}
					
					return modblock.getStateFromMeta(campfireMeta);
				}
			}
			else if (mod.toLowerCase().trim().equals("toughasnails"))
			{
				modblock = Block.getBlockFromName(ModObjects.campfireTAN);
				if (modblock != null) {return modblock.getStateFromMeta(1);} // 1 is "lit"
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.campfireFV);
				if (modblock != null) {return modblock.getDefaultState();}
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
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.campfireFMC));
				if (moditem != null) {return moditem;}
			}
			else if (mod.toLowerCase().trim().equals("justacampfire"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.campfire_JAC));
				if (moditem != null) {return moditem;}
			}
			else if (mod.toLowerCase().trim().equals("toughasnails"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.campfireTAN));
				if (moditem != null) {return moditem;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.campfireFV));
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
		String[] modprioritylist = GeneralConfig.modCartographyTable;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.cartographyTableFMC);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseCartographyTableMeta(furnaceOrientation, coordBaseMode));}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.cartographyTableFV);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseCartographyTableMeta(furnaceOrientation, coordBaseMode));}
			}
		}
		
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Composter
	public static IBlockState chooseModComposterState()
	{
		String[] modprioritylist = GeneralConfig.modComposter;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.composterFMC);
				if (modobject != null) {return modobject.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.composterFV);
				if (modobject != null) {return modobject.getDefaultState();}
			}
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
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteStairs_Qu);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteStairs_FV);
				if (modblock != null) {return modblock;}
			}
		}
		
		return null;
	}
	public static Block chooseModPolishedDioriteStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.polishedDioriteStairs_VBE);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.polishedDioriteStairs_FV);
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
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteWall_Qu);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteWall_VBE);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.dioriteWall_FV);
				if (modblock != null) {return modblock.getDefaultState();}
			}
		}
		
		return null;
	}
	public static IBlockState chooseModDioriteSlabState(boolean upper)
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.dioriteSlab_Qu);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.dioriteSlab_FV);
				if (modobject != null) {return modobject.getStateFromMeta(upper?0:1);}
			}
		}
		
		return null;
	}
	public static IBlockState chooseModDioriteBrickSlabState(boolean upper)
	{
		Block modobject=null;
		
		modobject = Block.getBlockFromName(ModObjects.dioriteBrickSlab_Qu);
		if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
		
		return null;
	}
	public static IBlockState chooseModPolishedDioriteSlabState(boolean upper)
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(ModObjects.polishedDioriteSlab_VBE);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.polishedDioriteSlab_FV);
				if (modobject != null) {return modobject.getStateFromMeta(upper?0:1);}
			}
		}
		
		return null;
	}
	
	// Dried Kelp Block
	public static IBlockState chooseModDriedKelpBlockState()
	{
		Block modobject = Block.getBlockFromName(ModObjects.driedKelpBlock);
		if (modobject!=null) {return modobject.getDefaultState();}
		
		return null;
	}
	
	
	// Dye
	public static ItemStack chooseModBlackDye()
	{
		String[] modprioritylist = GeneralConfig.modDye;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeFMC);
				if (moditem != null) {return new ItemStack(moditem, 1, 3);}
			}
			else if (mod.toLowerCase().trim().equals("biomesoplenty"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlackBOP);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
			else if (mod.toLowerCase().trim().equals("quark"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeQuark);
				if (moditem != null) {return new ItemStack(moditem, 1, 1);}
			}
			else if (mod.toLowerCase().trim().equals("botania"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBotania);
				if (moditem != null) {return new ItemStack(moditem, 1, 15);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlackFV);
				if (moditem != null) {return new ItemStack(moditem, 1);}
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
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeFMC);
				if (moditem != null) {return new ItemStack(moditem, 1, 1);}
			}
			else if (mod.toLowerCase().trim().equals("biomesoplenty"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlueBOP);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
			else if (mod.toLowerCase().trim().equals("quark"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeQuark);
				if (moditem != null) {return new ItemStack(moditem, 1, 0);}
			}
			else if (mod.toLowerCase().trim().equals("botania"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBotania);
				if (moditem != null) {return new ItemStack(moditem, 1, 11);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBlueFV);
				if (moditem != null) {return new ItemStack(moditem, 1);}
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
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeFMC);
				if (moditem != null) {return new ItemStack(moditem, 1, 2);}
			}
			else if (mod.toLowerCase().trim().equals("biomesoplenty"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBrownBOP);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
			// TODO - add Quark support if they decide to add brown dye
			else if (mod.toLowerCase().trim().equals("botania"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBotania);
				if (moditem != null) {return new ItemStack(moditem, 1, 12);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBrownFV);
				if (moditem != null) {return new ItemStack(moditem, 1);}
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
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeFMC);
				if (moditem != null) {return new ItemStack(moditem, 1, 0);}
			}
			else if (mod.toLowerCase().trim().equals("biomesoplenty"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeWhiteBOP);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
			else if (mod.toLowerCase().trim().equals("quark"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeQuark);
				if (moditem != null) {return new ItemStack(moditem, 1, 2);}
			}
			else if (mod.toLowerCase().trim().equals("botania"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeBotania);
				if (moditem != null) {return new ItemStack(moditem, 1, 0);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.dyeWhiteFV);
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
		}
		return null;
	}
	
	
	// Fletching Table
	public static IBlockState chooseModFletchingTableState(int furnaceOrientation, EnumFacing coordBaseMode)
	{
		String[] modprioritylist = GeneralConfig.modFletchingTable;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.fletchingTableFMC);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseCartographyTableMeta(furnaceOrientation, coordBaseMode));}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.fletchingTableFV);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseCartographyTableMeta(furnaceOrientation, coordBaseMode));}
			}
		}
		
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Flowers
	public static IBlockState chooseModCornflower()
	{
		String[] modprioritylist = GeneralConfig.modFlower;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modblock = Block.getBlockFromName(ModObjects.flowerCornflowerFMC);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.flowerCornflowerFV);
				if (modblock != null) {return modblock.getDefaultState();}
			}
		}
		return null;
	}
	public static IBlockState chooseModLilyOfTheValley()
	{
		String[] modprioritylist = GeneralConfig.modFlower;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modblock = Block.getBlockFromName(ModObjects.flowerLilyOfTheValleyFMC);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.flowerLilyOfTheValleyFV);
				if (modblock != null) {return modblock.getDefaultState();}
			}
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
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteStairs_Qu);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteStairs_FV);
				if (modblock != null) {return modblock;}
			}
		}
		
		return null;
	}
	public static Block chooseModPolishedGraniteStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modBountifulStone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.polishedGraniteStairs_VBE);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.polishedGraniteStairs_FV);
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
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteWall_Qu);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteWall_VBE);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.graniteWall_FV);
				if (modblock != null) {return modblock.getDefaultState();}
			}
		}
		
		return null;
	}
	
	
	// Grindstone
	public static IBlockState chooseModGrindstone(int orientation, EnumFacing coordBaseMode, boolean isHanging)
	{
		String[] modprioritylist = GeneralConfig.modGrindstone;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modblock = Block.getBlockFromName(ModObjects.grindstoneFMC);
				if (modblock != null)
				{
					return modblock.getStateFromMeta(isHanging ?
							StructureVillageVN.chooseFMCGrindstoneHangingMeta(orientation, coordBaseMode)
							:StructureVillageVN.chooseBlastFurnaceMeta(orientation, coordBaseMode)); // Standing meta is same as the blast furnace
				}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modblock = Block.getBlockFromName(ModObjects.grindstoneFV);
				if (modblock != null)
				{
					return modblock.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(orientation, coordBaseMode)); // Standing meta is same as the blast furnace
				}
			}
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
			
			if (mod.toLowerCase().trim().equals("charm"))
			{
				modblock = Block.getBlockFromName(ModObjects.lanternCh);
				if (modblock != null) {return modblock.getStateFromMeta(isHanging? 1:0);}
			}
			else if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modblock = Block.getBlockFromName(ModObjects.lanternFMC);
				if (modblock != null) {return modblock.getStateFromMeta(isHanging? 0:1);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.lanternFV);
				if (modblock != null) {return modblock.getDefaultState();} // It seems to set the hanging state based on whether there's a block above it
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
			
			if (mod.toLowerCase().trim().equals("charm"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.lanternCh));
				if (moditem != null) {return moditem;}
			}
			else if (mod.toLowerCase().trim().equals("futuremc"))
			{
				moditem = Item.getItemFromBlock(Block.getBlockFromName(ModObjects.lanternFMC));
				if (moditem != null) {return moditem;}
			}
		}
    	
    	return null;
	}
	
	
	// Lectern
	// Carpet color only applies to Bibliocraft writing desks. Set to -1 for no carpet.
	public static void setModLecternState(World world, int x, int y, int z, int orientation, EnumFacing coordBaseMode, int woodMeta, int carpetColor)
	{
		Block modblock=null;
		boolean setTE = false; // Flagged as true if you need to set a tile entity
		
		modblock = Block.getBlockFromName(ModObjects.lecternFV);
		if (modblock != null) {world.setBlockState(new BlockPos(x, y, z), modblock.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(orientation, coordBaseMode)), 2); return;}
		
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
        	
        	// Add carpet
        	if (carpetColor!=-1)
        	{
        		// Add carpet as an inventory item
        		NBTTagCompound deskinvo = new NBTTagCompound();
        		deskinvo.setByte("Count", (byte)1);
        		deskinvo.setByte("Slot", (byte)9);
        		deskinvo.setShort("Damage", (short)carpetColor);
        		deskinvo.setString("id", "minecraft:carpet");
        		NBTTagList taglist = new NBTTagList();
        		taglist.appendTag(deskinvo);
        		nbtCompound.setTag("Inventory", taglist);
        	}
        	
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
		String[] modprioritylist = GeneralConfig.modLoom;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.loomFMC);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode));}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.loomFV);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode));}
			}
		}
		
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Mossy stone
	public static IBlockState chooseModMossyCobblestoneWallState()
	{
		String[] modprioritylist = GeneralConfig.modMossyStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickWall_Qu);
				if (modobject != null) {return modobject.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickWall_VBE);
				if (modobject != null) {return modobject.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickWall_FMC);
				if (modobject != null) {return modobject.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickWall_FV);
				if (modobject != null) {return modobject.getDefaultState();}
			}
		}
		return null;
	}
	public static IBlockState chooseModMossyCobblestoneSlabState(boolean upper)
	{
		String[] modprioritylist = GeneralConfig.modMossyStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyCobblestoneSlab_VBE);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyCobblestoneSlab_FV);
				if (modobject != null) {return modobject.getStateFromMeta(upper?0:1);}
			}
		}
		return null;
	}
	public static IBlockState chooseModMossyStoneBrickSlabState(boolean upper)
	{
		String[] modprioritylist = GeneralConfig.modMossyStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickSlab_Qu);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickSlab_VBE);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickSlab_FV);
				if (modobject != null) {return modobject.getStateFromMeta(upper?0:1);}
			}
		}
		return null;
	}
	public static Block chooseModMossyCobblestoneStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modMossyStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyCobblestoneStairs_VBE);
				if (modobject != null) {return modobject;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyCobblestoneStairs_FV);
				if (modobject != null) {return modobject;}
			}
		}
		return null;
	}
	public static Block chooseModMossyStoneBrickStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modMossyStone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickStairs_Qu);
				if (modobject != null) {return modobject;}
			}
			else if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickStairs_VBE);
				if (modobject != null) {return modobject;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.mossyStoneBrickStairs_FV);
				if (modobject != null) {return modobject;}
			}
		}
		return null;
	}
	public static IBlockState chooseModMossyStoneBrickWallState()
	{
		return null;
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
	
	// Prismarine Stairs
	public static Block chooseModPrismarineStairsBlock()
	{
		String[] modprioritylist = GeneralConfig.modPrismarine;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.prismarineStairs_Qu);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(ModObjects.prismarineStairs_FV);
				if (modblock != null) {return modblock;}
			}
		}
		
		return null;
	}
	
	// Prismarine Slab
	/**
	 * Returns regular sandstone slab on a failure
	 */
	public static IBlockState chooseModPrismarineSlabState(boolean upper)
	{
		String[] modprioritylist = GeneralConfig.modPrismarine;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.prismarineSlab_Qu);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(ModObjects.prismarineSlab_FV);
				if (modobject != null) {return modobject.getStateFromMeta(upper?0:1);}
			}
		}
		
		return null;
	}
	
	// Prismarine Wall
	public static IBlockState chooseModPrismarineWallState()
	{
    	String[] modprioritylist = GeneralConfig.modPrismarine;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modblock = Block.getBlockFromName(ModObjects.prismarineWall_Qu);
				if (modblock != null) {return modblock.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modblock = Block.getBlockFromName(ModObjects.prismarineWall_FV);
				if (modblock != null) {return modblock.getDefaultState();}
			}
		}
		
		return null;
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
		String[] modprioritylist = GeneralConfig.modSmithingTable;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.smithingTableFMC);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode));}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.smithingTableFV);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(furnaceOrientation, coordBaseMode));}
			}
		}
		
		return Blocks.CRAFTING_TABLE.getDefaultState();
	}
	
	
	// Smoker
	/**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static IBlockState chooseModSmokerState(int orientation, EnumFacing coordBaseMode)
	{
		String[] modprioritylist = GeneralConfig.modSmoker;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modblock = Block.getBlockFromName(ModObjects.smokerFMC);
				if (modblock != null)
				{
					return modblock.getStateFromMeta(StructureVillageVN.chooseBlastFurnaceMeta(orientation, coordBaseMode)); // Standing meta is same as the blast furnace
				}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modblock = Block.getBlockFromName(ModObjects.smokerFV);
				if (modblock != null)
				{
					return modblock.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(orientation, coordBaseMode));
				}
			}
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
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.smoothSandstone_red_Qu : ModObjects.smoothSandstone_white_Qu);
			}
			else if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.smoothSandstone_red_FMC : ModObjects.smoothSandstone_white_FMC);
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.smoothSandstone_red_FV : ModObjects.smoothSandstone_white_FV);
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
		String[] modprioritylist = GeneralConfig.modSandstone;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("quark"))
			{
				Block modobject = Block.getBlockFromName(isred ? ModObjects.smoothRedSandstoneSlabQu : ModObjects.smoothSandstoneSlabQu);
				if (modobject != null) {return modobject.getStateFromMeta(upper?8:0);}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(isred ? ModObjects.smoothRedSandstoneSlabFV : ModObjects.smoothSandstoneSlabFV);
				if (modobject != null) {return modobject.getStateFromMeta(upper?0:1);}
			}
		}
		
		// Return the vanilla version (not smooth)
		return isred ? Blocks.STONE_SLAB2.getStateFromMeta(upper?8:0) : Blocks.STONE_SLAB.getStateFromMeta(upper?9:1);
	}
	
	
	/**
	 * Added in 1.14
	 * Returns non-smooth stair versions on failure
	 */
	public static Block chooseModSmoothSandstoneStairsBlock(boolean isRed)
	{
		String[] modprioritylist = GeneralConfig.modSandstone;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modblock = Block.getBlockFromName(isRed ? ModObjects.smoothSandstoneStairs_red_VBE : ModObjects.smoothSandstoneStairs_white_VBE);
				if (modblock != null) {return modblock;}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modblock = Block.getBlockFromName(isRed ? ModObjects.smoothSandstoneStairs_red_FV : ModObjects.smoothSandstoneStairs_white_FV);
				if (modblock != null) {return modblock;}
			}
		}
		
		return isRed ? Blocks.RED_SANDSTONE_STAIRS : Blocks.SANDSTONE_STAIRS;
	}
	
	
	// Sandstone Wall
	public static IBlockState chooseModSandstoneWall(boolean isRed)
	{
		String[] modprioritylist = GeneralConfig.modSandstone;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.sandstoneWall_red_Qu : ModObjects.sandstoneWall_white_Qu);
			}
			else if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.sandstoneWall_red_FMC : ModObjects.sandstoneWall_white_FMC);
			}
			else if (mod.toLowerCase().trim().equals("vanillabuildersextension"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.sandstoneWall_red_VBE : ModObjects.sandstoneWall_white_VBE);
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(isRed ? ModObjects.sandstoneWall_red_FV : ModObjects.sandstoneWall_white_FV);
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
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.smoothStoneQu);
			}
			else if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modobject = Block.getBlockFromName(ModObjects.smoothStoneFMC);
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.smoothStoneFV);
			}
			
			if (modobject != null) {return modobject.getDefaultState();}
		}
		
		return Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(8);
	}
	
	
	// Stone brick wall
	public static IBlockState chooseModStoneBrickWallState()
	{
		String[] modprioritylist = GeneralConfig.modStoneBrickWall;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("quark"))
			{
				modobject = Block.getBlockFromName(ModObjects.stoneBrickWall_Qu);
				if (modobject != null) {return modobject.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futuremc"))
			{
				modobject = Block.getBlockFromName(ModObjects.stoneBrickWall_FMC);
				if (modobject != null) {return modobject.getDefaultState();}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				modobject = Block.getBlockFromName(ModObjects.stoneBrickWall_FV);
				if (modobject != null) {return modobject.getDefaultState();}
			}
		}
		return null;
	}
	
		
	// Stonecutter
	public static IBlockState chooseModStonecutterState(int orientation, EnumFacing coordBaseMode)
	{
		String[] modprioritylist = GeneralConfig.modStonecutter;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.stoneCutterFMC);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseBlastFurnaceMeta(orientation, coordBaseMode));}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modobject = Block.getBlockFromName(ModObjects.stoneCutterFV);
				if (modobject!=null) {return modobject.getStateFromMeta(StructureVillageVN.chooseBlastFurnaceMeta(orientation, coordBaseMode));}
			}
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
		String[] modprioritylist = GeneralConfig.modStrippedLog;
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("futuremc"))
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
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block logBlock=null;
				
				switch (materialMeta)
				{
				case 0: logBlock = Block.getBlockFromName(ModObjects.strippedLogOakVC); break;
				case 1: logBlock = Block.getBlockFromName(ModObjects.strippedLogSpruceVC); break;
				case 2: logBlock = Block.getBlockFromName(ModObjects.strippedLogBirchVC); break;
				case 3: logBlock = Block.getBlockFromName(ModObjects.strippedLogJungleVC); break;
				case 4: logBlock = Block.getBlockFromName(ModObjects.strippedLogAcaciaVC); break;
				case 5: logBlock = Block.getBlockFromName(ModObjects.strippedLogDarkOakVC); break;
				}
				if (logBlock != null) {return logBlock.getStateFromMeta(orientation==0 ? 3 : orientation==1 ? 1 : 5);}
			}
		}
		
		return (materialMeta<4 ? Blocks.LOG : Blocks.LOG2).getStateFromMeta(orientation*4+materialMeta%4);
	}
	

	// Stripped wood
	/**
	 * Materials are: 0=oak, 1=spruce, 2=birch, 3=jungle, 4=acacia, 5=darkoak
	 * Orientations are: 0=vertical, 1=east-west, 2=north-south
	 */
	public static IBlockState chooseModStrippedWoodState(IBlockState blockstate, int orientation)
	{
		String[] modprioritylist = GeneralConfig.modStrippedWood;
		Block blockIn=blockstate.getBlock();
		int metaIn=blockIn.getMetaFromState(blockstate);
		int materialMeta = blockIn.getMetaFromState(blockstate) + (blockIn==Blocks.LOG2 ? 4:0);
		
		for (String mod : modprioritylist)
		{
			Block modobject=null;
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Block modblock = null;
				
				switch (materialMeta)
				{
				case 0: modblock = Block.getBlockFromName(ModObjects.strippedWoodOakFMC); break;
				case 1: modblock = Block.getBlockFromName(ModObjects.strippedWoodSpruceFMC); break;
				case 2: modblock = Block.getBlockFromName(ModObjects.strippedWoodBirchFMC); break;
				case 3: modblock = Block.getBlockFromName(ModObjects.strippedWoodJungleFMC); break;
				case 4: modblock = Block.getBlockFromName(ModObjects.strippedWoodAcaciaFMC); break;
				case 5: modblock = Block.getBlockFromName(ModObjects.strippedWoodDarkOakFMC); break;
				}
				if (modblock != null) {return modblock.getStateFromMeta(orientation%3*4);}
			}
			if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modblock = null;
				
				switch (materialMeta)
				{
				case 0: modblock = Block.getBlockFromName(ModObjects.strippedWoodOakFV); break;
				case 1: modblock = Block.getBlockFromName(ModObjects.strippedWoodSpruceFV); break;
				case 2: modblock = Block.getBlockFromName(ModObjects.strippedWoodBirchFV); break;
				case 3: modblock = Block.getBlockFromName(ModObjects.strippedWoodJungleFV); break;
				case 4: modblock = Block.getBlockFromName(ModObjects.strippedWoodAcaciaFV); break;
				case 5: modblock = Block.getBlockFromName(ModObjects.strippedWoodDarkOakFV); break;
				}
				if (modblock != null) {return modblock.getStateFromMeta(orientation==0 ? 3 : orientation==1 ? 1 : 5);}
			}
		}
		
		// Pass the original block if it's not a vanilla log
		if (blockIn!=Blocks.LOG && blockIn!=Blocks.LOG2) {return blockstate;}
		return chooseModWoodState(blockstate);
	}
	
	
	public static ItemStack chooseModSuspiciousStew(Random random)
	{
		String[] modprioritylist = GeneralConfig.modSuspiciousStew;
		
		for (String mod : modprioritylist)
		{
			Item moditem=null;
			
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFMC);
				
				if (moditem != null)
				{
					ItemStack moditemstack = new ItemStack(moditem, 1);
					
					// Only six potion effects are allowed
					int[][] potionEffects = new int[][]{
						//{12, 80}, // Allium (Fire Resistance)
						{15, 160}, // Azure Bluet (Blindness)
						//{23, 7}, // Blue Orchid (Saturation)
						{23, 7}, // Dandelion (Saturation)
						//{10, 160}, // Oxeye Daisy (Regeneration)
						{16, 100}, // Poppy (Night Vision)
						//{18, 180}, // Red Tulip (Weakness)
						{18, 180}, // Orange Tulip (Weakness)
						//{18, 180}, // White Tulip (Weakness)
						//{18, 180}, // Pink Tulip (Weakness)
						{8, 120}, // Lily of the Valley (High Jump)
						{19, 240}, // Cornflower (Poison)
						//{20, 160}, // Wither Rose (Wither)
					};
					
					int chosenPotionEffect = random.nextInt(6);
					
//					NBTTagCompound tagcompound3 = new NBTTagCompound();
//					tagcompound3.setByte("Count", (byte)1);
//					tagcompound3.setShort("Damage", (short)0);
//					tagcompound3.setString("id", "minecraft:milk_bucket");
//					
//					NBTTagList curativeItems = new NBTTagList();
//					curativeItems.appendTag(tagcompound3);
					
					NBTTagCompound tagcompound2 = new NBTTagCompound();
					//tagcompound2.setByte("Ambient", (byte)0);
					//tagcompound2.setByte("Amplifier", (byte)0);
					//tagcompound2.setByte("ShowParticles", (byte)1);
					//tagcompound2.setTag("CurativeItems", curativeItems);
					tagcompound2.setByte("Id", (byte)potionEffects[chosenPotionEffect][0]);
					tagcompound2.setInteger("Duration", potionEffects[chosenPotionEffect][1]);
					
					NBTTagList customPotionEffects = new NBTTagList();
					customPotionEffects.appendTag(tagcompound2);
					
					
					NBTTagCompound tagcompound = new NBTTagCompound();
					
					tagcompound.setTag("CustomPotionEffects", customPotionEffects);
					
					moditemstack.setTagCompound(tagcompound);
					
					return moditemstack;
				}
			}
			else if (mod.toLowerCase().trim().equals("futureversions"))
			{
				// Many of these effects aren't sold by villagers and don't even correctly correspond to vanilla,
				// so I'm leaving them all in to give Future Versions SOMETHING unique
				switch (random.nextInt(10))
				{
					case 0: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_1); break; // Regeneration 6s [BE] (Oxeye Daisy) 
					case 1: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_2); break; // Jump Boost 4s [BE] (Cornflower)
					case 2: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_3); break; // Poison 10s [BE] (Lily of the Valley)
					case 3: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_4); break; // Wither 6s [BE] (Wither Rose)
					case 4: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_5); break; // Weakness 7s [BE] (Tulips)
					case 5: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_6); break; // Blindness 6s [BE] (Azure Bluet)
					case 6: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_7); break; // Fire Resistance 2s [BE] (Allium)
					case 7: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_8); break; // Saturation 4s [no edition] (Blue Orchid / Dandelion)
					case 8: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_9); break; // Speed 4s [no edition] (no flower: FV uses Poppy)
					case 9: moditem = FunctionsVN.getItemFromName(ModObjects.suspiciousStewFV_10); break; // Saturation II 4s [no edition] (no flower: FV uses dandelion)
				}
				
				if (moditem != null) {return new ItemStack(moditem, 1);}
			}
		}
    	
    	return null;
	}
	
	
	// Sweet Berries - Added in 1.14
	public static Item chooseModSweetBerriesItem()
	{
		String[] modprioritylist = GeneralConfig.modSweetBerries;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("futuremc"))
			{
				Item modobject = FunctionsVN.getItemFromName(ModObjects.sweetBerriesFMC);
				if (modobject!=null) {return modobject;}
			}
			if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Item modobject = FunctionsVN.getItemFromName(ModObjects.sweetBerriesFV);
				if (modobject!=null) {return modobject;}
			}
		}
		
		return null;
	}
	
	
	// Trapdoor
	public static Block chooseModWoodenTrapdoor(int materialMeta)
	{
		String[] modprioritylist = GeneralConfig.modWoodenTrapdoor;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("quark"))
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
			}
			if (mod.toLowerCase().trim().equals("futureversions"))
			{
				Block modblock=null;
				
				switch (materialMeta)
				{
					case 1: modblock = Block.getBlockFromName(ModObjects.trapdoorSpruceFV); break;
					case 2: modblock = Block.getBlockFromName(ModObjects.trapdoorBirchFV); break;
					case 3: modblock = Block.getBlockFromName(ModObjects.trapdoorJungleFV); break;
					case 4: modblock = Block.getBlockFromName(ModObjects.trapdoorAcaciaFV); break;
					case 5: modblock = Block.getBlockFromName(ModObjects.trapdoorDarkOakFV); break;
				}
				if (modblock != null) {return modblock;}
			}
		}
		
		// If all else fails, grab the vanilla version
		return Blocks.TRAPDOOR;
	}
	
	
	// Wooden table (Vanilla is a fence with a pressure plate on top)
	public static IBlockState[] chooseModWoodenTable(int materialMeta)
	{
		String[] modprioritylist = GeneralConfig.modWoodenTable;
		
		Block modblock = null;
		
		for (String mod : modprioritylist)
		{
			if (mod.toLowerCase().trim().equals("chocolatequestrepoured"))
			{
				modblock=null;
				
				switch (materialMeta)
				{
				case 0: modblock = Block.getBlockFromName(ModObjects.table_oak_CQR); break;
				case 1: modblock = Block.getBlockFromName(ModObjects.table_spruce_CQR); break;
				case 2: modblock = Block.getBlockFromName(ModObjects.table_birch_CQR); break;
				case 3: modblock = Block.getBlockFromName(ModObjects.table_jungle_CQR); break;
				case 4: modblock = Block.getBlockFromName(ModObjects.table_acacia_CQR); break;
				case 5: modblock = Block.getBlockFromName(ModObjects.table_dark_oak_CQR); break;
				}
				
				if (modblock != null)
				{
					return new IBlockState[] {
							Blocks.AIR.getDefaultState(),
							modblock.getDefaultState()
							};
				}
			}
			else if (mod.toLowerCase().trim().equals("macawsfurniture"))
			{
				modblock=null;
				
				switch (materialMeta)
				{
				case 0: modblock = Block.getBlockFromName(ModObjects.table_oak_MCF); break;
				case 1: modblock = Block.getBlockFromName(ModObjects.table_spruce_MCF); break;
				case 2: modblock = Block.getBlockFromName(ModObjects.table_birch_MCF); break;
				case 3: modblock = Block.getBlockFromName(ModObjects.table_jungle_MCF); break;
				case 4: modblock = Block.getBlockFromName(ModObjects.table_acacia_MCF); break;
				case 5: modblock = Block.getBlockFromName(ModObjects.table_dark_oak_MCF); break;
				}
				
				if (modblock != null)
				{
					return new IBlockState[] {
							Blocks.AIR.getDefaultState(),
							modblock.getDefaultState()
							};
				}
			}
			else if (mod.toLowerCase().trim().equals("mrcrayfishsfurnituremod"))
			{
				modblock=null;
				
				switch (materialMeta)
				{
				case 0: modblock = Block.getBlockFromName(ModObjects.table_oak_CFM); break;
				case 1: modblock = Block.getBlockFromName(ModObjects.table_spruce_CFM); break;
				case 2: modblock = Block.getBlockFromName(ModObjects.table_birch_CFM); break;
				case 3: modblock = Block.getBlockFromName(ModObjects.table_jungle_CFM); break;
				case 4: modblock = Block.getBlockFromName(ModObjects.table_acacia_CFM); break;
				case 5: modblock = Block.getBlockFromName(ModObjects.table_dark_oak_CFM); break;
				}
				
				if (modblock != null)
				{
					return new IBlockState[] {
							Blocks.AIR.getDefaultState(),
							modblock.getDefaultState()
							};
				}
			}
			else if (mod.toLowerCase().trim().equals("rustic"))
			{
				modblock=null;
				
				switch (materialMeta)
				{
				case 0: modblock = Block.getBlockFromName(ModObjects.table_oak_Rus); break;
				case 1: modblock = Block.getBlockFromName(ModObjects.table_spruce_Rus); break;
				case 2: modblock = Block.getBlockFromName(ModObjects.table_birch_Rus); break;
				case 3: modblock = Block.getBlockFromName(ModObjects.table_jungle_Rus); break;
				case 4: modblock = Block.getBlockFromName(ModObjects.table_acacia_Rus); break;
				case 5: modblock = Block.getBlockFromName(ModObjects.table_dark_oak_Rus); break;
				}
				
				if (modblock != null)
				{
					return new IBlockState[] {
							Blocks.AIR.getDefaultState(),
							modblock.getDefaultState()
							};
				}
			}
			else if (mod.toLowerCase().trim().equals("variedcommodities"))
			{
				modblock = Block.getBlockFromName(ModObjects.table_VC);
				
				if (modblock != null)
				{
					return new IBlockState[] {
							Blocks.AIR.getDefaultState(),
							modblock.getStateFromMeta(materialMeta)
							};
				}
			}
			else if (mod.toLowerCase().trim().equals("minecraft"))
			{
				return chooseVanillaWoodenTable(materialMeta);
			}
			else if (mod.toLowerCase().trim().equals("bibliocraft"))
			{
				modblock = Block.getBlockFromName(ModObjects.table_BC);
				
				if (modblock != null)
				{
					return new IBlockState[] {
							Blocks.AIR.getDefaultState(),
							modblock.getStateFromMeta(materialMeta)
							};
				}
			}
		}
		// If all else fails, grab the vanilla version
		return chooseVanillaWoodenTable(materialMeta);
	}
	public static IBlockState[] chooseVanillaWoodenTable(int materialMeta)
	{
		IBlockState fenceState;
		
		switch(materialMeta)
		{
		default:
		case 0:
			fenceState = Blocks.OAK_FENCE.getDefaultState(); break;
		case 1:
			fenceState = Blocks.SPRUCE_FENCE.getDefaultState(); break;
		case 2:
			fenceState = Blocks.BIRCH_FENCE.getDefaultState(); break;
		case 3:
			fenceState = Blocks.JUNGLE_FENCE.getDefaultState(); break;
		case 4:
			fenceState = Blocks.ACACIA_FENCE.getDefaultState(); break;
		case 5:
			fenceState = Blocks.DARK_OAK_FENCE.getDefaultState(); break;
		}
		
		return new IBlockState[] {
				ModObjects.chooseModPressurePlate(materialMeta).getDefaultState(),
				fenceState
				};
	}
}
