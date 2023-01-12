package astrotibs.villagenames.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.utility.Reference;
import net.minecraftforge.common.config.Configuration;

public class GeneralConfig {
	public static Configuration config;

	//public static String[] blackList;
	public static boolean wellSlabs;
	public static boolean nameSign;
	public static boolean nameEntities;
	public static String headerTags = "\u00a78\u00a7o";
	public static boolean villagerDropBook;
	public static boolean villagerSellsCodex;
	public static boolean recordStructureCoords; 
	public static boolean addJobToName;
	public static String nitwitProfession;
	public static boolean villageBanners;
	public static int signYaw;
	
	public static boolean concreteWell;
	
	
	public static boolean codexChestLoot;
	public static boolean versionChecker;
	
	public static boolean wellBoundary;
	public static boolean wellDecorations;
	public static boolean useVillageColors;
	public static boolean debugMessages;
	public static boolean nameGolems;
	
	public static String[] modNameMappingAutomatic;
	public static Map<String, List> modNameMappingAutomatic_map = new HashMap<String, List>();
	public static String[] modNameMappingClickable;
	public static Map<String, List> modNameMappingClickable_map = new HashMap<String, List>();
	public static String[] entitiesNameableLikePets;
	public static Set<String> entitiesNameableLikePets_set = new HashSet<String>();
	
	public static String[] modProfessionMapping;
	public static Map<String, List> modProfessionMapping_map = new HashMap<String, List>();
	
	public static int PMMerchantProfessionMap;
	public static int PMLostMinerProfessionMap;
	public static boolean TQVillageNames;
	
	public static String[] modStructureNames;
	public static Map<String, List> modStructureNames_map = new HashMap<String, List>();
	
	public static boolean modernVillagerSkins;

	public static boolean moddedVillagerHeadwear;

	public static String[] moddedVillagerHeadwearGraylist;
	public static Map<String, List> moddedVillagerHeadwearWhitelist_map = new HashMap<String, List>();
	public static Map<String, List> moddedVillagerHeadwearBlacklist_map = new HashMap<String, List>();
	
	public static String[] moddedVillagerModularSkins;
	public static Map<String, List> moddedVillagerCareerSkins_map = new HashMap<String, List>();
	public static ArrayList<String> careerAsset_a;
	public static ArrayList<String> zombieCareerAsset_a;
	public static ArrayList<String> professionID_a;
	public static ArrayList<Integer> careerID_a;
	public static ArrayList<String> profession_concat_career_a;
	
    public static boolean villagerSkinTones;
    public static float villagerSkinToneVarianceAnnealing;
    public static float villagerSkinToneVarianceScale;
    
	public static boolean modernVillagerTrades;
	//public static boolean villagerLegacyTrades;
	public static boolean treasureTrades;
	public static boolean writtenBookTrade;
	public static boolean swampHutMushroomPot;

	public static String[] modBamboo;
	public static String[] modBarrel;
	public static String[] modBell;
	public static String[] modBlastFurnace;
	public static String[] modBlueIce;
	public static String[] modBountifulStone;
	public static String[] modCampfire;
	public static String[] modCartographyTable;
	public static String[] modComposter;
	public static String[] modDye;
	public static String[] modFletchingTable;
	public static String[] modFlower;
	public static String[] modGrindstone;
	public static String[] modLantern;
	public static String[] modLoom;
	public static String[] modPrismarine;
	public static String[] modMossyStone;
	public static String[] modSandstone;
	public static String[] modSmithingTable;
	public static String[] modSmoker;
	public static String[] modSmoothStone;
	public static String[] modStoneBrickWall;
	public static String[] modStonecutter;
	public static String[] modStrippedLog;
	public static String[] modStrippedWood;
	public static String[] modSuspiciousStew;
	public static String[] modSweetBerries;
	public static String[] modWood;
	public static String[] modWoodenTable;
	public static String[] modWoodenTrapdoor;
	
	public static String[] zombieCureCatalysts;
	public static Map<String, List> zombieCureCatalysts_map = new HashMap<String, List>();
	public static String[] zombieCureGroups;
	public static Map<String, List> zombieCureGroups_map = new HashMap<String, List>();

	public static float harvestcraftCropFarmRate;
	public static boolean antiqueAtlasMarkerNames;
	
	public static void init(File configFile)
	{
		if (config == null)
		{
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}

	protected static void loadConfiguration()
	{
		// --- General --- //
	    nameSign = config.getBoolean("Name Sign", Reference.CATEGORY_GENERAL, true, "Town centers display their name on one or more signs.");
	    recordStructureCoords = config.getBoolean("Record Structure Coords", Reference.CATEGORY_GENERAL, true, "Books generated by villagers or the Codex record the structure's coordinates.");
	    villagerDropBook = config.getBoolean("Villager drops book", Reference.CATEGORY_GENERAL, false, "Village books are dropped by the villager rather than going directly into your inventory.");
	    villagerSellsCodex = config.getBoolean("Villager makes codex", Reference.CATEGORY_GENERAL, true, "Librarian villagers will give you a codex if you right-click them while holding emerald, iron ingots, and/or gold ingots.");
		useVillageColors = config.getBoolean("Use village colors", Reference.CATEGORY_GENERAL, true, "Whether to apply the village's colors to concrete, terracotta, carpet, etc.");
		
	    wellBoundary = config.getBoolean("Well boundary", Reference.CATEGORY_GENERAL, true, "Whether to surround the well with colored blocks");
	    wellSlabs = config.getBoolean("Well slabs", Reference.CATEGORY_GENERAL, true, "Replace the cobblestone rims of wells with stone slabs, making it easier for players and villagers to escape if they fall in.");

	    villageBanners = config.getBoolean("Village Banner", Reference.CATEGORY_GENERAL, true, "The town banner pattern is displayed at the town center.");
	    signYaw = config.getInt("Sign Yaw", Reference.CATEGORY_GENERAL, 3, 0, 4, "If Village Banner is enabled: Degree to which well signs and banners should face inward. At 0 they face directly outward away from the well; at 4 they face each other.");
	    
	    wellDecorations = config.getBoolean("Allow well decorations", Reference.CATEGORY_WELL_KILL_SWITCH, true, "Set this to false to disable all well decoration: sign, slabs, terracotta, concrete.");
	    
	    concreteWell = config.getBoolean("Concrete Well", Reference.CATEGORY_GENERAL, true, "Whether to decorate wells with Concrete and Glazed Terracotta instead of stained clay");
	    
	    
	    
	    // --------------Professions-----------------//
	    treasureTrades = config.getBoolean("Treasure Trades", Reference.CATEGORY_VILLAGER_PROFESSIONS, true, "High-level Librarians and Cartographers will offer enchanted books and treasures in exchange for " + Reference.MOD_NAME + " items.");
	    writtenBookTrade = config.getBoolean("Written Book Trade", Reference.CATEGORY_VILLAGER_PROFESSIONS, true, "Change the vanilla Librarian trade to require a single written book instead of two identical written books");

	    modernVillagerSkins = config.getBoolean("Modern Villager Profession Skins", Reference.CATEGORY_VILLAGER_PROFESSIONS, true, "Use the composite 1.14 Villager skins");
	    modernVillagerTrades = config.getBoolean("Modern Villager Trades", Reference.CATEGORY_VILLAGER_PROFESSIONS, true, "Use JE 1.14 / BE 1.12 trade offerings and add the Mason villager");

	    moddedVillagerHeadwear = config.getBoolean("Modded Villager Headwear", Reference.CATEGORY_VILLAGER_PROFESSIONS, false, "If modern skins are enabled: renders the headwear layer for non-vanilla villager professions, if one exists.");
	    
	    moddedVillagerHeadwearGraylist = config.getStringList("Modded Villager Headwear Graylist", Reference.CATEGORY_VILLAGER_PROFESSIONS, new String[]
	    		{
				// Bewitchment Alchemist... not sure if this is the ID because I can't get the thing to load
				"bewitchment:alchemist",
				// Custom Ideas
				"custom:guard",
				// Extra Utilities 2
				"extrautils2:alchemist",
    			// Forestry
				"forestry:apiarist",
				// Immersive Engineering
				"immersiveengineering:engineer|1",
				"immersiveengineering:engineer|2",
				"immersiveengineering:engineer|3",
				"immersiveengineering:engineer|4",
				"immersiveengineering:engineer|5",
				// Mine Trading Cards
				"is_mtc:card_master",
				// Open Blocks
				"-openblocks:radio",
				// Tolkien Tweaks - Mobs Edition
				"tolkienmobs:junk_dealer",
				"totemexpansion:witchdoctor",
	    		},
	    		"(If modern skins are enabled) List of profession IDs for other mods' villagers. A normal value will be whitelisted: it will display that villager's headwear layer even if Modded Villager Headwear is false. "
	    		+ "Adding a negative sign in front of the ID int will blacklist the profession so that its headwear layer never renders.");

	    // Extract the values and populate the white and black lists
	    moddedVillagerHeadwearWhitelist_map.clear();
	    moddedVillagerHeadwearWhitelist_map = unpackModdedVillagerHeadwearGraylist(moddedVillagerHeadwearGraylist, true);
	    moddedVillagerHeadwearBlacklist_map.clear();
	    moddedVillagerHeadwearBlacklist_map = unpackModdedVillagerHeadwearGraylist(moddedVillagerHeadwearGraylist, false);
	    
	    moddedVillagerModularSkins = config.getStringList("Modded Villager Modular Skins", Reference.CATEGORY_VILLAGER_PROFESSIONS, new String[]
	    		{
				// Actually Additions
				"aa_engineer|aa_engineer|actuallyadditions:engineer",
				"aa_jam|aa_jam|actuallyadditions:jamguy",
				// Animania
				"am_petseller|am_petseller|animania:pet_seller",
				// Bewitchment
				"bew_alchemist||bewitchment:alchemist",
				// ChocoCraft Plus
				"ccp_stablehand||chococraftplus:stablehand",
				// Custom Ideas
				"custom_guard|custom_guard|custom:guard",
				// Cyclic
				"cm_druid|cm_druid|cyclicmagic:druid",
				"cm_sage|cm_sage|cyclicmagic:sage",
				// Extra Utilities 2
				"eu_alchemist|eu_alchemist|extrautils2:alchemist",
				"eu_red_mechanic|eu_red_mechanic|extrautils2:red_mechanic",
				"eu_shady_merchant|eu_shady_merchant|extrautils2:shady_merchant",
    			// Forestry
				"for_apiarist|for_apiarist|forestry:apiarist",
				"for_arborist|for_arborist|forestry:arborist",
				// Fossils and Archaeology
				"fa_archaeologist||fossil:archeologist",
				// HeatAndClimateMod
	    		"hac_researcher|hac_researcher|dcs_climate:agri_researcher",
				"hac_researcher|hac_researcher|dcs_climate:engineer",
				"hac_trader|hac_trader|dcs_climate:trader",
				// Immersive Engineering
				"ie_engineer||immersiveengineering:engineer|1",
				"ie_machinist||immersiveengineering:engineer|2",
				"ie_electrician||immersiveengineering:engineer|3",
				"ie_outfitter||immersiveengineering:engineer|4",
				"ie_gunsmith||immersiveengineering:engineer|5",
				// Mine Trading Cards
				"mtc_cardmaster||is_mtc:card_master",
				"mtc_cardtrader||is_mtc:card_trader",
				// MiniHeads
				"mh_retailer|mh_retailer|miniheads:storeowner",
				// Mystcraft
				"myc_archivist||mystcraft:archivist",
				// Open Blocks
				"ob_musicmerchant||openblocks:radio",
				// PneumaticCraft
				"pc_mechanic||pneumaticcraft:mechanic",
				// Railcraft
				"rc_engineer|rc_engineer|railcraft:trackman",
				// Tolkien Tweaks - Mobs Edition
				"ttm_coin_trader|ttm_coin_trader|tolkienmobs:coin_trader",
				"ttm_grocery_store|ttm_grocery_store|tolkienmobs:grocery_store",
				"ttm_junk_trader|ttm_junk_trader|tolkienmobs:junk_dealer",
				"ttm_pet_merchant|ttm_pet_merchant|tolkienmobs:pet_merchant",
				// Totem Expansion
				"te_witch_doctor||totemexpansion:witchdoctor",
				},
	    		"(If modern skins are enabled) List of profession IDs for other mods' villagers to render in the modular skin style. Format is: careerAsset|zombieCareerAsset|professionID\n"+
	    		"careerAsset: career skin png to be overlaid onto the villager, located in assets\\"+Reference.MOD_ID.toLowerCase()+"\\textures\\entity\\villager\\profession\n"+
	    				"The default values are all available in "+Reference.MOD_NAME+". You can access custom values with a resourcepack.\n"
	    						+ "zombieCareerAsset: a zombie career png, located in the corresponding zombie_villager directory. You may leave this value blank, in which case it will use the non-zombie career overlay.\n"
	    						+ "professionID: the ID associated with the mod profession.");
	    
	    // Assign the map now and immediately extract it into arrays for faster lookup
	    moddedVillagerCareerSkins_map.clear();
	    moddedVillagerCareerSkins_map = GeneralConfig.unpackModVillagerSkins(GeneralConfig.moddedVillagerModularSkins);
	    careerAsset_a = (ArrayList<String>)moddedVillagerCareerSkins_map.get("careerAsset");
	    zombieCareerAsset_a = (ArrayList<String>)moddedVillagerCareerSkins_map.get("zombieCareerAsset");
	    professionID_a = (ArrayList<String>)moddedVillagerCareerSkins_map.get("professionID");
	    careerID_a = (ArrayList<Integer>)moddedVillagerCareerSkins_map.get("careerID");
	    profession_concat_career_a = (ArrayList<String>)moddedVillagerCareerSkins_map.get("IDs_concat_careers");


	    villagerSkinTones = config.getBoolean("Display Skin Tones", Reference.CATEGORY_VILLAGER_SKIN_TONES, true, "Display Gaussian-distributed random skin tones assigned to villagers");
	    villagerSkinToneVarianceAnnealing = config.getFloat("Skin Tone Variance Annealing", Reference.CATEGORY_VILLAGER_SKIN_TONES, 8F/3, 0, Float.MAX_VALUE,
	    		"Statistical variance in skin tone for a population decreases as the number of skin-tone-affecting biome tags increases.\n"
	    		+ "Setting this value to zero eliminates that effect, making skin tone vary equally everywhere (aside from culling to the darkest/lightest tones).\n"
	    		+ "Increasing this value makes skin tone variation less likely in qualifying biomes.");
	    villagerSkinToneVarianceScale = config.getFloat("Skin Tone Variance Scale", Reference.CATEGORY_VILLAGER_SKIN_TONES, 1F, 0, Float.MAX_VALUE,
	    		"Proportionality constant for variance everywhere, irrespective of biome. Set this to zero for absolutely no variation for a given biome.\n"
	    		+ "Skin tones are culled to the darkest and lightest values, so setting this arbitrarily high will result in ONLY the darkest or lightest villagers.\n"
	    		+ "I estimate that the distribution is flattest, and thus population variance is maximized, around a value of about 2.6.");
	    
	    
 		
 		
    	//--------------Miscellaneous-----------------//
	    
	    zombieCureCatalysts = config.getStringList("Zombie Cure Catalysts", Reference.CATEGORY_GENERAL, new String[]{
 				"vanilla|net.minecraft.block.BlockBed|tile.bed|-1",
 				"vanilla|net.minecraft.block.BlockPane|tile.fenceIron|-1"
 				},
 				"When performing the ritual to convert a zombie villager into a villager, having these blocks nearby (within a taxicab distance of 4) will speed up the process. "
 				+ "Format is: group|classPath|unlocName|meta\n"
 				+ "group is an arbitrary group name to which the block belongs, referenced in Zombie Cure Groups below.\n"
 				+ "classPath is the mod's address to the entity class.\n"
 				+ "unlocName is the unlocalized name of the block. This is used as an extra discriminator in case class path and meta aren't enough. "
 				+ "You can leave this blank to ignore it.\n"
 				+ "meta is integer meta value of the block. Enter -1 to ignore meta and count all blocks with that class path."
 				);
		// Populate the hashmap
	    zombieCureCatalysts_map.clear();
	    zombieCureCatalysts_map = unpackZombieCureCatalysts(zombieCureCatalysts);
	    
	    zombieCureGroups = config.getStringList("Zombie Cure Groups", Reference.CATEGORY_GENERAL, new String[]{
 				"vanilla|0.3|14"
 				},
 				"When curing a zombie villager, all blocks of the same named group will use these stats. "
 				+ "Format is: group|speedup|limit\n"
 				+ "group is the group name assigned in Zombie Cure Catalysts above.\n"
 				+ "speedup is the per-block percentage point boost in conversion speed. That is: a value of 1.0 increases the conversion by about 1 percentage point per group block found. "
 				+ "negative values will likewise reduce the conversion speed, making conversion take longer.\n"
 				+ "limit is the maximum number of blocks in this group that will apply the group speedup effect."
 				);
		// Populate the hashmap
	    zombieCureGroups_map.clear();
	    zombieCureGroups_map = unpackZombieCureGroups(zombieCureGroups);
	    
	    versionChecker = config.getBoolean("Version Checker", Reference.CATEGORY_MISCELLANEOUS, true, "Displays a client-side chat message on login if there's an update available.");
	    codexChestLoot = config.getBoolean("Codex Chest Loot", Reference.CATEGORY_MISCELLANEOUS, true, "The Codex can appear as rare chest loot.");
	    debugMessages = config.getBoolean("Debug messages", Reference.CATEGORY_MISCELLANEOUS, false, "Print debug messages to the console, print the class paths of entities and blocks you right-click.");
	    swampHutMushroomPot = config.getBoolean("Swamp Hut Mushroom", Reference.CATEGORY_MISCELLANEOUS, true, "1.8+ has a bug where the clay pot in the Witch's swamp hut is empty. This flag will correctly place a red mushroom in the pot.");
	    
	    
	    
	    //--------------Names-----------------//
	    
	    nameEntities = config.getBoolean("Entity names", Reference.CATEGORY_NAMING, true, "Entities reveal their names when you right-click them, or automatically if so assigned.");
	    addJobToName = config.getBoolean("Entity professions", Reference.CATEGORY_NAMING, false, "An entity's name also includes its profession/title. You may need to right-click the entity to update its name plate.");
	    nameGolems = config.getBoolean("Golem names", Reference.CATEGORY_NAMING, true, "Right-click village Golems to learn their name.");
	    
	    nitwitProfession = config.getString("Nitwit Profession", Reference.CATEGORY_NAMING, "", "The career displayed for a Nitwit"); // Per-profession registry and spawning is broken in 1.8
	    
		// Automatic Names
		
		modNameMappingAutomatic = config.getStringList("Automatic Names", Reference.CATEGORY_NAMING, new String[]{
				
				// Minecraft
				//"demon||net.minecraft.entity.boss.EntityWither|add",
				"villager-goblin|Witch|net.minecraft.entity.monster.EntityWitch|add",
				"alien-golem|Elder Guardian|net.minecraft.entity.monster.EntityElderGuardian|add",
				//"dragon-angel|Ender Dragon|net.minecraft.entity.boss.EntityDragon|add",
				"villager-demon|Evoker|net.minecraft.entity.monster.EntityEvoker|add",
				"villager-demon|Vindicator|net.minecraft.entity.monster.EntityVindicator|add",
				"villager-demon|Illusioner|net.minecraft.entity.monster.EntityIllusionIllager|add",
				
				// Hardcore Ender Expansion
				//"dragon-angel|Ender Dragon|chylex.hee.entity.boss.EntityBossDragon|add",
				//"demon|Ender Demon|chylex.hee.entity.boss.EntityBossEnderDemon|add",
				
				// Galacticraft
				"alien-demon|Evolved Skeleton Boss|micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss|add", // 1.7 and 1.10
				"alien-golem|Evolved Creeper Boss|micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss|add", // 1.7 and 1.10
				"alien-goblin||micdoodle8.mods.galacticraft.planets.venus.entities.EntitySpiderQueen|add", // 1.10
				
				// More Planets
				"alien-goblin|Evolved Witch|stevekung.mods.moreplanets.core.entities.EntityEvolvedWitch|add", // 1.7
				// Bosses
				"alien-golem|Diona Creeper Boss|stevekung.mods.moreplanets.planets.diona.entities.EntityDionaCreeperBoss|add", // 1.7
				"alien-golem|Fronos Creeper Boss|stevekung.mods.moreplanets.planets.fronos.entities.EntityFronosCreeperBossTemp|add", // 1.7
				"alien-golem|Kapteyn B Creeper Boss|stevekung.mods.moreplanets.planets.kapteynb.entities.EntityKapteynBCreeperBoss|add", // 1.7
				"alien-goblin|Evolved Infected Spider Boss|stevekung.mods.moreplanets.planets.nibiru.entities.EntityEvolvedInfectedSpiderBoss|add", // 1.7
				"alien-golem|Pluto Creeper Boss|stevekung.mods.moreplanets.planets.pluto.entities.EntityPlutoCreeperBoss|add", // 1.7
				"alien-angel|Cheese Cube Boss|stevekung.mods.moreplanets.planets.polongnius.entities.EntityCheeseCubeEyeBoss|add", // 1.7
				"alien-demon-golem|Evolved Sirius Blaze Boss|stevekung.mods.moreplanets.planets.siriusb.entities.EntityEvolvedSiriusBlazeBoss|add", // 1.7
				"alien-demon|Infected Crystallized Slime Boss|stevekung.mods.moreplanets.module.planets.diona.entity.EntityInfectedCrystallizeSlimeBoss|add", // 1.10
				"alien-angel|Cheese Cube Boss|stevekung.mods.moreplanets.module.planets.chalos.entity.EntityCheeseCubeEyeBoss|add", // 1.10
				
				// NetherEx
				"demon-goblin||nex.entity.passive.EntityPigtificate",
				"demon-goblin||nex.entity.passive.EntityPigtificateLeader",
				
				// Galaxy Space
				"alien-demon-angel|Evolved Boss Ghast|galaxyspace.galaxies.milkyway.SolarSystem.moons.io.entities.EntityBossGhast|add",
				"alien-demon-golem|Evolved Boss Blaze|galaxyspace.galaxies.milkyway.SolarSystem.planets.ceres.entities.EntityBossBlaze|add",
				
				// Primitive Mobs
				"villager|Summoner|net.daveyx0.primitivemobs.entity.monster.EntityDSummoner|add",
				
				// Special Mobs
				"villager-goblin|Witch of Domination|fathertoast.specialmobs.entity.witch.EntityDominationWitch|add",
				"villager-goblin|Witch of Shadows|fathertoast.specialmobs.entity.witch.EntityShadowsWitch|add",
				"villager-goblin|Witch of the Wilds|fathertoast.specialmobs.entity.witch.EntityWildsWitch|add",
				"villager-goblin|Witch of the Wind|fathertoast.specialmobs.entity.witch.EntityWindWitch|add",
								
				// Twilight Forest
				"villager-golem||twilightforest.entity.EntityTFArmoredGiant|add",
				"villager-golem||twilightforest.entity.EntityTFGiantMiner|add",
				//Bosses
				"dragon|Naga|twilightforest.entity.boss.EntityTFNaga|add",
				"dragon|Hydra|twilightforest.entity.boss.EntityTFHydra|add",
				"demon-golem|Knight Phantom|twilightforest.entity.boss.EntityTFKnightPhantom|add",
				"demon|Twilight Lich|twilightforest.entity.boss.EntityTFLich|add",
				"goblin|Minoshroom|twilightforest.entity.boss.EntityTFMinoshroom|add",
				"angel|Snow Queen|twilightforest.entity.boss.EntityTFSnowQueen|add",
				"demon-angel|Ur-ghast|twilightforest.entity.boss.EntityTFUrGhast|add",
				"goblin-golem|Alpha Yeti|twilightforest.entity.boss.EntityTFYetiAlpha|add",
				
				// Thaumcraft
				
				// Witchery
				"villager-demon||com.emoniph.witchery.entity.EntityVampire|add",
				"villager|Witch Hunter|com.emoniph.witchery.entity.EntityWitchHunter|add",
				"demon|Horned Huntsman|com.emoniph.witchery.entity.EntityHornedHuntsman|add",
				
				},
				"List of entities that will generate a name automatically when they appear. Useful for aggressive or boss mobs.\n"
				+ "Format is: nameType|profession|classPath|addOrRemove\n"
				+ "nameType is the name pool for the entity, or a hyphenated series of pools like \"angel-golem\".\n"
				+ "profession is displayed if that config flag is enabled. It can be left blank for no profession.\n"
				+ "classPath is the mod's address to the entity class.\n"
								+ "nameType options:\n"
								+ "villager, dragon, golem, alien, angel, demon, goblin, pet, custom\n"
				+ "addOrRemove - type \"add\" to automatically add names tags to ALL COPIES of this entity upon spawning, or \"remove\" to automatically remove.\n"
				+ "Be VERY CAUTIOUS about what entities you choose to add to this list!"
								);
		// Populate the hashmap
		modNameMappingAutomatic_map.clear();
		modNameMappingAutomatic_map = unpackMappedNames(modNameMappingAutomatic);
		
	    

		// Clickable Names
	    
		modNameMappingClickable = config.getStringList("Clickable Names", Reference.CATEGORY_NAMING, new String[]{
				
				// Galacticraft
				"alien||micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager",
				
				// More Planets
				"alien||stevekung.mods.moreplanets.moons.koentus.entities.EntityKoentusianVillager", // 1.7
				"alien||stevekung.mods.moreplanets.module.moons.koentus.entities.EntityKoentusianVillager", // 1.10
				"alien-villager-goblin||stevekung.mods.moreplanets.planets.fronos.entities.EntityFronosVillager", // 1.7
				"alien-villager-goblin||stevekung.mods.moreplanets.module.planets.fronos.entities.EntityFronosVillager", // 1.10
				"alien-villager-angel||stevekung.mods.moreplanets.planets.nibiru.entity.EntityNibiruVillager", // 1.7
				"alien-villager-angel||stevekung.mods.moreplanets.module.planets.nibiru.entity.EntityNibiruVillager", // 1.10
				
				// Natura
				"goblin-demon||mods.natura.entity.ImpEntity",
				
				// Thaumcraft
				"goblin||thaumcraft.common.entities.monster.EntityPech",
				
				// ToroQuest
				"villager|Sentry|"+ModObjects.TQSentryClass,
		        "villager|Guard|"+ModObjects.TQGuardClass,
		        "villager|Lord|"+ModObjects.TQVillageLordClass,
		        //"villager||"+Reference.TQFugitiveClass,
		        //"villager||"+Reference.TQShopkeeperClass,
		        
				// Twilight Forest
				"angel-golem-goblin|Questing Ram|twilightforest.entity.passive.EntityTFQuestRam",
				
				// Witchery
				"villager|Guard|com.emoniph.witchery.entity.EntityVillageGuard",
				"goblin||com.emoniph.witchery.entity.EntityGoblin",
				"goblin-demon||com.emoniph.witchery.entity.EntityImp",
				"demon||com.emoniph.witchery.entity.EntityDemon",
				
				// Primitive Mobs
				"villager|Traveling Merchant|net.daveyx0.primitivemobs.entity.passive.EntityTravelingMerchant",
				"villager|Miner|net.daveyx0.primitivemobs.entity.passive.EntityLostMiner",
				"villager||net.daveyx0.primitivemobs.entity.passive.EntitySheepman",
				"villager|Blacksmith|net.daveyx0.primitivemobs.entity.passive.EntitySheepmanSmith",
				
				// NetherEx
				"villager-goblin||nex.entity.passive.EntityPigtificate",
				"villager-goblin|Chief|nex.entity.passive.EntityPigtificateLeader",
				
				// Improving Minecraft
				"villager-goblin||imc.common.EntityPigman"
				
				},
				"List of entities that can generate a name when right-clicked. Format is: nameType|profession|classPath\n"
				+ "nameType is the name pool for the entity, or a hyphenated series of pools like \"angel-golem\".\n"
				+ "profession is displayed if that config flag is enabled. It can be left blank for no profession.\n"
				+ "classPath is mod's address to the entity class.\n"
								+ "nameType options:\n"
								+ "villager, dragon, golem, alien, angel, demon, goblin, pet, custom\n"
								);
		// Populate the hashmap
		modNameMappingClickable_map.clear();
		modNameMappingClickable_map = unpackMappedNames(modNameMappingClickable);
		
		
		// Forced pet names
		entitiesNameableLikePets = config.getStringList("Entities Nameable Like Pets", Reference.CATEGORY_NAMING, new String[]{
				ModObjects.AM_DraftHorse_Stallion_classpath,
				ModObjects.AM_DraftHorse_Mare_classpath,
				ModObjects.AM_DraftHorse_Foal_classpath,
				},
				"List of class paths of entities that receive a random Pet name when right-clicked with a blank nametag, irrespective of if they're tamed or who tamed them.\n"
				+ "Use this for entities that can't receive a Pet name in the intended way (typically because owner ID is stored differently or not stored at all)."
								);
		// Populate hash set
		entitiesNameableLikePets_set.clear();
		for (int i=0; i< entitiesNameableLikePets.length; i++) {entitiesNameableLikePets_set.add(entitiesNameableLikePets[i]);}
		
		
		//--------------Mod Integration-----------------//

		harvestcraftCropFarmRate = config.getFloat("Crop rate: Harvestcraft", Reference.CATEGORY_MOD_INTEGRATION, 0.25F, 0F, 1F, "Generate Harvestcraft crops in farms. Only used with Village Generator. Set to 0 for no HC crops.");
		antiqueAtlasMarkerNames = config.getBoolean("Antique Atlas: Village Marker Names", Reference.CATEGORY_MOD_INTEGRATION, true, "Label a new village marker with the village's name in your Antique Atlases.");

		modBamboo = config.getStringList("Mod Priority: Bamboo", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"futuremc",
 				"growthcraft",
 				"biomesoplenty",
 				"sakura",
 				"futureversions",
 				"bamboozled",
 				},
 				"Priority order for referencing Bamboo for village generation. The version highest on the list and registered in your game will be used."
 				);
		
		modBarrel = config.getStringList("Mod Priority: Barrel", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing Barrels for village generation and villager trades. The version highest on the list and registered in your game will be used."
 				);
		
		modBell = config.getStringList("Mod Priority: Bell", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing Bells for villager trades. The version highest on the list and registered in your game will be used."
 				);
		
		modBlastFurnace = config.getStringList("Mod Priority: Blast Furnace", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing Blast Furnace for village generation. The version highest on the list and registered in your game will be used."
 				);
		
		modBlueIce = config.getStringList("Mod Priority: Blue Ice", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing Blue Ice for village generation. The version highest on the list and registered in your game will be used."
 				);
		
	    modBountifulStone = config.getStringList("Mod Priority: Bountiful Stone", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"quark",
 				"vanillabuildersextension",
 				"futureversions",
 				},
 				"Priority order for referencing Granite, Diorite, and Andesite for things like walls and stairs. The version highest on the list and registered in your game will be used."
 				);
	    
		modCampfire = config.getStringList("Mod Priority: Campfire", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
	    		"justacampfire",
 				"toughasnails",
 				"futureversions",
 				},
 				"Priority order for referencing the Campfire for village generation and villager trade offers. The version highest on the list and registered in your game will be used."
 				);
		
		modCartographyTable = config.getStringList("Mod Priority: Cartography Table", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing the Cartography Table for village generation. The version highest on the list and registered in your game will be used."
 				);
		
		modComposter = config.getStringList("Mod Priority: Composter", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing the Composter for village generation. The version highest on the list and registered in your game will be used."
 				);
		
	    modDye = config.getStringList("Mod Priority: Dye", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"biomesoplenty",
 				"quark",
 				"botania",
 				"futureversions",
 				},
 				"Priority order for referencing dye for villager trade offers. The version highest on the list and registered in your game will be used."
 				);
		
		modFletchingTable = config.getStringList("Mod Priority: Fletching Table", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing the Fletching Table for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
	    modFlower = config.getStringList("Mod Priority: Flower", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"futuremc",
	    		"futureversions",
 				},
 				"Priority order for referencing flowers for village generation. The version highest on the list and registered in your game will be used."
 				);
		
		modGrindstone = config.getStringList("Mod Priority: Grindstone", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing the Grindstone for village generation. The version highest on the list and registered in your game will be used."
 				);
		
	    modLantern = config.getStringList("Mod Priority: Lantern", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"charm",
	    		"futuremc",
	    		"futureversions",
 				},
 				"Priority order for referencing Lanterns for village generation and villager trade offers. The version highest on the list and registered in your game will be used."
 				);
	    
	    modLoom = config.getStringList("Mod Priority: Loom", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing the Loom for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
	    modMossyStone = config.getStringList("Mod Priority: Mossy Stone", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"etfuturum",
	    		"uptodate",
 				},
 				"Priority order for referencing mossy stone blocks for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
	    modSandstone = config.getStringList("Mod Priority: Sandstone", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"quark",
	    		"futuremc",
	    		"vanillabuildersextension",
	    		"futureversions",
 				},
 				"Priority order for referencing Sandstone variations for village generation. The version highest on the list and registered in your game will be used."
 				);
		
		modSmithingTable = config.getStringList("Mod Priority: Smithing Table", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing the Smithing Table for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
		modSmoker = config.getStringList("Mod Priority: Smoker", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing the Smoker for village generation. The version highest on the list and registered in your game will be used."
 				);
		
	    modSmoothStone = config.getStringList("Mod Priority: Smooth Stone", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"quark",
	    		"futuremc",
	    		"futureversions"
 				},
 				"Priority order for referencing Smooth Stone for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
	    modStoneBrickWall = config.getStringList("Mod Priority: Stone Brick Wall", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"quark",
	    		"futuremc",
	    		"futureversions"
 				},
 				"Priority order for referencing Stone Brick Walls for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
	    modStonecutter = config.getStringList("Mod Priority: Stonecutter", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"futuremc",
 				"futureversions",
 				},
 				"Priority order for referencing the Stonecutter for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
	    modStrippedLog = config.getStringList("Mod Priority: Stripped Log", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"futuremc",
	    		"futureversions",
 				},
 				"Priority order for referencing Stripped Logs for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
	    modStrippedWood = config.getStringList("Mod Priority: Stripped Wood", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"futuremc",
	    		"futureversions",
 				},
 				"Priority order for referencing Stripped Wood for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
	    modSuspiciousStew = config.getStringList("Mod Priority: Suspicious Stew", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"futuremc",
	    		"futureversions",
 				},
 				"Priority order for referencing Suspicious Stew for villager trades. The version highest on the list and registered in your game will be used."
 				);
	    
	    modSweetBerries = config.getStringList("Mod Priority: Sweet Berries", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"futuremc",
	    		"futureversions",
 				},
 				"Priority order for referencing Sweet Berries for villager trades. The version highest on the list and registered in your game will be used."
 				);
	    
	    modWood = config.getStringList("Mod Priority: Smooth Stone", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"quark",
	    		"futureversions",
 				},
 				"Priority order for referencing Wood blocks (bark on all sides) for village generation. The version highest on the list and registered in your game will be used."
 				);

	    modWoodenTable = config.getStringList("Mod Priority: Table", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
	    		"chocolatequestrepoured",
	    		"macawsfurniture",
	    		"mrcrayfishsfurnituremod",
	    		"rustic",
	    		"variedcommodities",
	    		"minecraft",
	    		"bibliocraft",
 				},
 				"Priority order for referencing Wooden Tables for village generation. The version highest on the list and registered in your game will be used. "
 				+ "The \"minecraft\" entry refers to the vanilla-style pressure plate atop a fence post."
 				);
		
	    modWoodenTrapdoor = config.getStringList("Mod Priority: Smooth Stone", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"quark",
	    		"futureversions",
 				},
 				"Priority order for referencing wooden trapdoors for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
		// Mapping for modded structures, and the creatures that can name them
		modStructureNames = config.getStringList("Mod Structures", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
				
				// Galacticraft
				"alienvillage|MoonVillage|Moon Village|Moon|moonvillage|micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager",
				"alienvillage|GC_AbandonedBase|Abandoned Base|Asteroid Belt|abandonedbase|", // 1.10
				
				// More Planets
				"alienvillage|FronosVillage|Fronos Village|Fronos|fronosvillage|stevekung.mods.moreplanets.planets.fronos.entities.EntityFronosVillager",
				"alienvillage|FronosVillage|Fronos Village|Fronos|fronosvillage|stevekung.mods.moreplanets.module.planets.fronos.entities.EntityFronosVillager",
				"alienvillage|KoentusVillage|Koentus Village|Koentus|koentusvillage|stevekung.mods.moreplanets.moons.koentus.entities.EntityKoentusianVillager",
				"alienvillage|KoentusVillage|Koentus Village|Koentus|koentusvillage|stevekung.mods.moreplanets.module.moons.koentus.entities.EntityKoentusianVillager",
				"alienvillage|NibiruVillage|Nibiru Village|Nibiru|nibiruvillage|stevekung.mods.moreplanets.planets.nibiru.entity.EntityNibiruVillager",
				"alienvillage|NibiruVillage|Nibiru Village|Nibiru|nibiruvillage|stevekung.mods.moreplanets.module.planets.nibiru.entity.EntityNibiruVillager",
				
				// Hardcore Ender Expansion
				"endcity|hardcoreenderdragon_EndTower|Dungeon Tower|The End|endcity|",
				"endcity|hardcoreenderdragon_EndIsland|Laboratory|The End|endcity|",
				
				// NetherEx -- doesn't register structures vanilla-style
				//"fortress-village|villages.pigtificate_nether|Nether Village|The Nether|fortress|nex.entity.passive.EntityPigtificate"
				
				
				},
				"List of mod structures that can be named with a Codex, or by right-clicking an entity in that structure (optional). "
				+ "Structures must have been generated in a manner similarly to vanilla (e.g. Galacticraft Moon Villages).\n"
				+ "Format is: nameType|structureType|structureTitle|dimensionName|bookType|entityClassPath\n"
				+ "nameType is your choice of name pool for the structure. Options: village, mineshaft, temple, stronghold, fortress, monument, endcity, mansion, alienvillage\n"
				+ "structureType how the mod saves the structure info--e.g. dimension/data/[structureType].dat\n"
				+ "structureTitle is the string type of the structure (e.g. \"Moon Village\"), which will be recorded into a book. It can be left blank.\n"
				+ "dimensionName is the name of the dimension that would be recorded into the book. It can be left blank.\n"
				+ "bookType is the kind of book that is generated. Options: village, mineshaft, temple, jungletemple, desertpyramid, swamphut, igloo, "
				+ "stronghold, fortress, monument, endcity, mansion, moonvillage, koentusvillage, fronosvillage, nibiruvillage, abandonedbase\n"
				+ "entityClassPath is the mod's address to the entity class that will generate this book (when inside the structure). "
					+ "It can be left blank, wherein the structure name can only be obtained via a Codex.\n");
		// Populate the hashmap
		modStructureNames_map.clear();
		modStructureNames_map = unpackModStructures(modStructureNames);
	    
	    modPrismarine = config.getStringList("Mod Priority: Prismarine", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
 				"quark",
	    		"futureversions",
 				},
 				"Priority order for referencing prismarine variations for village generation. The version highest on the list and registered in your game will be used."
 				);
	    
		// New mod profession mapping
		// Changed IDs from integers to ProfessionForge.getRegistryName() strings
		modProfessionMapping = config.getStringList("Mod Professions", Reference.CATEGORY_MOD_INTEGRATION, new String[]{
				// Actually Additions
				"Engineer|actuallyadditions:engineer|1",
				"Jam Guy|actuallyadditions:jamguy|0",
				// Bewitchment
				"Alchemist|bewitchment:alchemist|2",
				// ChocoCraft Plus
				"Stablehand|chococraftplus:stablehand|0",
				// Custom Ideas
				"Guard|custom:guard|-1",
				// Cylic
				"Druid|cyclicmagic:druid|2",
				"Sage|cyclicmagic:sage|1",
				// Extra Utilities 2
				"Alchemist|extrautils2:alchemist|2",
				"Mechanic|extrautils2:red_mechanic|3",
				"Shady Merchant|extrautils2:shady_merchant|-1",
				// Forestry
				"Apiarist|forestry:apiarist|4",
				"Arborist|forestry:arborist|0",
				// Fossils and Archaeology
				"Archaeologist|fossil:archeologist|2",
				// Immersive Engineering
				"Engineer|immersiveengineering:engineer|3|1",
				"Machinist|immersiveengineering:engineer|0|2",
				"Electrician|immersiveengineering:engineer|0|3",
				"Outfitter|immersiveengineering:engineer|0|4",
				"Gunsmith|immersiveengineering:engineer|0|5",
				// Mine Trading Cards
				"Card Master|is_mtc:card_master|2",
				"Card Trader|is_mtc:card_trader|0",
				// MiniHeads
				"Store Owner|miniheads:storeowner|-1",
				// Mystcraft
				"Archivist|mystcraft:archivist|1",
				// Open Blocks
				"Music Merchant|openblocks:radio|5",
				// Railcraft
				"Engineer|railcraft:trackman|3",
				// Tolkien Tweaks - Mobs Edition
				"Coin Banker|tolkienmobs:coin_trader|0",
				"Grocer|tolkienmobs:grocery_store|0",
				"Junk Dealer|tolkienmobs:junk_dealer|5",
				"Pet Supplier|tolkienmobs:pet_merchant|0",
				// Totem Expansion
				"Witch Doctor|totemexpansion:witchdoctor|2",
				},
				"List of professions for other mods' villagers. Format is: Name|ID|pageType\n"
				+ "Name is your choice of name for the profession.\n"
				+ "ID is the ID associated with the mod profession.\n"
				+ "pageType is the vanilla archetype the villager emulates in order to generate hint pages.\n"
								+ "Use this reference:\n"
								+ "-1=None\n"
								+ "0=Farmer\n"
								+ "1=Librarian\n"
								+ "2=Priest\n"
								+ "3=Blacksmith\n"
								+ "4=Butcher\n"
								+ "5=Nitwit\n");
		// Populate the hashmap
		modProfessionMapping_map.clear();
		modProfessionMapping_map = unpackMappedProfessions(modProfessionMapping);
				
		
		// Primitive Mobs villager mapping
	    //PMMerchantProfession = config.getString("PMMerchantProfession", "Mapping Professions", "Merchant", "The career displayed for Primitive Mobs's Traveling Merchant. Blank this out to display no profession regardless of addJobToName.");
	    PMMerchantProfessionMap = config.getInt("PM Traveling Merchant Profession ID", Reference.CATEGORY_MOD_INTEGRATION, 0, 0, 5,
	    		"Which vanilla archetype the traveling merchant emulates in order to generate hint pages.\n"
				+ "Use this reference:\n"
				+ "-1=None\n"
				+ "0=Farmer\n"
				+ "1=Librarian\n"
				+ "2=Priest\n"
				+ "3=Blacksmith\n"
				+ "4=Butcher\n"
				+ "5=Nitwit\n");
		
	    //PMLostMinerProfession = config.getString("PMLostMinerProfession", "Mapping Professions", "Miner", "The career displayed for Primitive Mobs's Lost Miner. Blank this out to display no profession regardless of addJobToName.");
	    PMLostMinerProfessionMap = config.getInt("PM Lost Miner Profession ID", Reference.CATEGORY_MOD_INTEGRATION, 3, 0, 5,
	    		"Which vanilla archetype the lost miner emulates in order to generate hint pages.\n"
				+ "Use this reference:\n"
				+ "-1=None\n"
				+ "0=Farmer\n"
				+ "1=Librarian\n"
				+ "2=Priest\n"
				+ "3=Blacksmith\n"
				+ "4=Butcher\n"
				+ "5=Nitwit\n");
	    
	    TQVillageNames = config.getBoolean("ToroQuest Village Names", Reference.CATEGORY_MOD_INTEGRATION, true, "If you're using ToroQuest, write its town names into village books. Additionally, " + Reference.MOD_NAME + " well signs will not generate, since they do so before ToroQuest assigns a town name.");
	    
	    
	    if (config.hasChanged()) config.save();
		
	}
	

	/**
	 * Inputs a (Profession|ID|vanillaType|careerID) String list and breaks it into three array lists
	 */
	public static Map<String, List> unpackMappedProfessions(String[] inputList)
	{
		List<String>  otherModProfessions = new ArrayList<String>();
		List<String> otherModIDs = new ArrayList<String>();
		List<Integer> vanillaProfMaps = new ArrayList<Integer>();
		List<Integer> careerID_a = new ArrayList<Integer>();
		List<String>  IDs_concat_careers = new ArrayList<String>();
		
		for (String entry : inputList)
		{
			entry.replaceAll("/", ""); // Forward slashses don't need to be escaped
			entry.replaceAll("\\\\", ""); // \ is BOTH String and regex; needs to be double-escaped. See https://stackoverflow.com/questions/1701839/string-replaceall-single-backslashes-with-double-backslashes
			entry.replaceAll("..", "");
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			
			// Initialize temp fields
			String otherModProfession="";
			String otherModID="";
			int vanillaProfMap=-1;
			int careerID=-99;
			
			// Place entries into variables
			try {otherModProfession = splitEntry[0].trim();}               catch (Exception e) {otherModProfession="";}
			try {otherModID = splitEntry[1].trim();}                       catch (Exception e) {otherModID="";}
			try {vanillaProfMap = Integer.parseInt(splitEntry[2].trim());} catch (Exception e) {vanillaProfMap=-1;}
			try {careerID = Integer.parseInt(splitEntry[3].trim());}       catch (Exception e) {careerID=-99;}
			String IDs_concat_career = (new StringBuilder()).append(otherModID).append(careerID).toString();
			
			if( !otherModProfession.equals("") && !otherModID.equals("") )
			{
				otherModProfessions.add(otherModProfession);
				otherModIDs.add(otherModID);
				vanillaProfMaps.add(vanillaProfMap);
				careerID_a.add(careerID);
				IDs_concat_careers.add(IDs_concat_career);
			}
		}
		
		Map<String, List> map = new HashMap();
		map.put("Professions",otherModProfessions);
		map.put("IDs",otherModIDs);
		map.put("VanillaProfMaps",vanillaProfMaps);
		map.put("careerID", careerID_a);
		map.put("IDs_concat_careers", IDs_concat_careers);
		
		return map;
	}
	
	

	/**
	 * Loads the (nameType|structureType|structureTitle|dimensionName|bookType|entityClassPath) string lists from othermods.cfg > Mod Structures
	 * and assigns them to this instance's variables.
	 */
	public static Map<String, List> unpackModStructures(String[] inputList) {
		
		List<String> otherModNameTypes = new ArrayList<String>();
		List<String> otherModStructureTypes = new ArrayList<String>();
		List<String> otherModStructureTitles = new ArrayList<String>();
		List<String> otherModDimensionNames = new ArrayList<String>();
		List<String> otherModBookTypes = new ArrayList<String>();
		List<String> otherModClassPaths = new ArrayList<String>();
		
		for (String entry : inputList) {
			// Remove parentheses
			entry.replaceAll("\\)", "");
			entry.replaceAll("\\(", "");
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			
			// Initialize temp fields
			String otherModNameType="";
			String otherModStructureType="FAILSAFE";
			String otherModStructureTitle="";
			String otherModDimensionName="";
			String otherModBookType="";
			String otherModClassPath="";
			
			// Place entries into variables
			try {otherModNameType = splitEntry[0].trim();}       catch (Exception e) {otherModNameType="";}
			try {otherModStructureType = splitEntry[1].trim();}  catch (Exception e) {otherModStructureType="FAILSAFE";}
			try {otherModStructureTitle = splitEntry[2].trim();} catch (Exception e) {otherModStructureTitle="";}
			try {otherModDimensionName = splitEntry[3].trim();}  catch (Exception e) {otherModDimensionName="";}
			try {otherModBookType = splitEntry[4].trim();}       catch (Exception e) {otherModBookType="";}
			try {otherModClassPath = splitEntry[5].trim();}      catch (Exception e) {otherModClassPath="";}
			
			if( !otherModNameType.equals("") && !otherModStructureType.equals("") && !otherModBookType.equals("") ) { // Something was actually assigned in the try block
				otherModNameTypes.add(otherModNameType);
				otherModStructureTypes.add(otherModStructureType);
				otherModStructureTitles.add(otherModStructureTitle);
				otherModDimensionNames.add(otherModDimensionName);
				otherModBookTypes.add(otherModBookType);
				otherModClassPaths.add(otherModClassPath);
				}
		}

		Map<String,List> map =new HashMap();
		map.put("NameTypes",otherModNameTypes);
		map.put("StructureTypes",otherModStructureTypes);
		map.put("StructureTitles",otherModStructureTitles);
		map.put("DimensionNames",otherModDimensionNames);
		map.put("BookTypes",otherModBookTypes);
		map.put("ClassPaths",otherModClassPaths);
		
		return map;
	}
	

	/**
	 * Loads the (nameType|profession|classPath|AddOrRemove) string lists from othermods.cfg > Automatic Names and othermods.cfg > Clickable Names
	 * and assigns them to this instance's variables.
	 */
	public static Map<String, List> unpackMappedNames(String[] inputList) {
		
		List<String> otherModNameTypes = new ArrayList<String>();
		List<String> otherModProfessions = new ArrayList<String>();
		List<String> otherModClassPaths = new ArrayList<String>();
		List<String> addOrRemoveA = new ArrayList<String>();
		
		for (String entry : inputList) {
			// Remove parentheses
			entry.replaceAll("\\)", "");
			entry.replaceAll("\\(", "");
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			
			// Initialize temp fields
			String otherModNameType="";
			String otherModProfession="";
			String otherModClassPath="";
			String addOrRemove="";
			
			// Place entries into variables
			try {otherModNameType = splitEntry[0].trim();}   catch (Exception e) {otherModNameType="";}
			try {otherModProfession = splitEntry[1].trim();} catch (Exception e) {otherModProfession="";}
			try {otherModClassPath = splitEntry[2].trim();}  catch (Exception e) {otherModClassPath="";}
			try {addOrRemove       = splitEntry[3].trim();}  catch (Exception e) {addOrRemove="";}
			
			if( !otherModClassPath.equals("") && !otherModNameType.equals("") ) { // Something was actually assigned in the try block
				
				otherModClassPaths.add(otherModClassPath);
				otherModNameTypes.add(otherModNameType);
				otherModProfessions.add(otherModProfession);
				addOrRemoveA.add(addOrRemove);
				
				}
		}

		Map<String, List> map =new HashMap();
		map.put("NameTypes",otherModNameTypes);
		map.put("Professions",otherModProfessions);
		map.put("ClassPaths",otherModClassPaths);
		map.put("AddOrRemove",addOrRemoveA);
		
		return map;
	}
	
	
	/**
	 * Loads the (group|classPath|unlocName|meta) string lists and assigns them to this instance's variables.
	 */
	public static Map<String, List> unpackZombieCureCatalysts(String[] inputList) {
		List<String>  zombieCureCatalystGroups = new ArrayList<String>();
		List<String> zombieCureCatalystClassPaths = new ArrayList<String>();
		List<String> zombieCureCatalystUnlocNames = new ArrayList<String>();
		List<Integer> zombieCureCatalystMetas = new ArrayList<Integer>();
		
		for (String entry : inputList) {
			// Remove parentheses
			entry.replaceAll("\\)", "");
			entry.replaceAll("\\(", "");
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			
			// Initialize temp fields
			String zombieCureCatalystGroup="";
			String zombieCureCatalystClassPath="";
			String zombieCureCatalystUnlocName="";
			int zombieCureCatalystMeta=-1;
			
			// Place entries into variables
			try {zombieCureCatalystGroup = splitEntry[0].trim();}                  catch (Exception e) {}
			try {zombieCureCatalystClassPath = splitEntry[1].trim();}              catch (Exception e) {}
			try {zombieCureCatalystUnlocName = splitEntry[2].trim();}              catch (Exception e) {}
			try {zombieCureCatalystMeta = Integer.parseInt(splitEntry[3].trim());} catch (Exception e) {}
			
			if(
					   !zombieCureCatalystGroup.equals("")
					&& !zombieCureCatalystClassPath.equals("")
					) { // Something was actually assigned in the try block
				zombieCureCatalystGroups.add(zombieCureCatalystGroup);
				zombieCureCatalystClassPaths.add(zombieCureCatalystClassPath);
				zombieCureCatalystUnlocNames.add(zombieCureCatalystUnlocName);
				zombieCureCatalystMetas.add(zombieCureCatalystMeta);
			}
		}
		
		Map<String, List> map = new HashMap();
		map.put("Groups",zombieCureCatalystGroups);
		map.put("ClassPaths",zombieCureCatalystClassPaths);
		map.put("UnlocNames",zombieCureCatalystUnlocNames);
		map.put("Metas",zombieCureCatalystMetas);
		
		return map;
	}
	
	
	/**
	 * Loads the (group|speedup|limit) string lists and assigns them to this instance's variables.
	 */
	public static Map<String, List> unpackZombieCureGroups(String[] inputList) {
		List<String>  zombieCureGroupGroups = new ArrayList<String>();
		List<Double> zombieCureGroupSpeedups = new ArrayList<Double>();
		List<Integer> zombieCureGroupLimits = new ArrayList<Integer>();
		
		for (String entry : inputList) {
			// Remove parentheses
			entry.replaceAll("\\)", "");
			entry.replaceAll("\\(", "");
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			
			// Initialize temp fields
			String zombieCureGroupGroup="";
			double zombieCureGroupSpeedup=0.0D;
			int zombieCureGroupLimit=-1;
			
			// Place entries into variables
			try {zombieCureGroupGroup = splitEntry[0].trim();}                       catch (Exception e) {}
			try {zombieCureGroupSpeedup = Double.parseDouble(splitEntry[1].trim());} catch (Exception e) {}
			try {zombieCureGroupLimit = Integer.parseInt(splitEntry[2].trim());}     catch (Exception e) {}
			
			if(!zombieCureGroupGroup.equals("")) { // Something was actually assigned in the try block
				zombieCureGroupGroups.add(zombieCureGroupGroup);
				zombieCureGroupSpeedups.add(zombieCureGroupSpeedup);
				zombieCureGroupLimits.add(zombieCureGroupLimit);
			}
		}
		
		Map<String, List> map = new HashMap();
		map.put("Groups",zombieCureGroupGroups);
		map.put("Speedups",zombieCureGroupSpeedups);
		map.put("Limits",zombieCureGroupLimits);
		
		return map;
	}

	
	/**
	 * Loads the (careerAsset|zombieCareerAsset|professionID|careerID) string lists and assigns them to this instance's variables.
	 */
	public static Map<String, List> unpackModVillagerSkins(String[] inputList)
	{
		List<String>  careerAsset_a = new ArrayList<String>();
		List<String>  zombieCareerAsset_a = new ArrayList<String>();
		List<String>  professionID_a = new ArrayList<String>();
		List<Integer> careerID_a = new ArrayList<Integer>();
		List<String>  IDs_concat_careers = new ArrayList<String>();
		
		for (String entry : inputList)
		{
			// Remove slashes and double dots to prevent address abuse
			entry.replaceAll("/", ""); // Forward slashses don't need to be escaped
			entry.replaceAll("\\\\", ""); // \ is BOTH String and regex; needs to be double-escaped. See https://stackoverflow.com/questions/1701839/string-replaceall-single-backslashes-with-double-backslashes
			entry.replaceAll("..", "");
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			
			// Initialize temp fields
			String careerAsset="";
			String zombieCareerAsset="";
			String professionID="";
			int careerID=-99;
			
			// Place entries into variables
			try {careerAsset = splitEntry[0].trim();}                catch (Exception e) {careerAsset="";}
			try {zombieCareerAsset = splitEntry[1].trim();}          catch (Exception e) {zombieCareerAsset="";}
			try {professionID = splitEntry[2].trim();}     	         catch (Exception e) {professionID="";}
			try {careerID = Integer.parseInt(splitEntry[3].trim());} catch (Exception e) {careerID=-99;}
			String IDs_concat_career = (new StringBuilder()).append(professionID).append(careerID).toString();
			
			if(!careerAsset.equals("")) // Something was actually assigned in the try block
			{
				careerAsset_a.add(careerAsset);
				zombieCareerAsset_a.add(zombieCareerAsset);
				professionID_a.add(professionID);
				careerID_a.add(careerID);
				IDs_concat_careers.add(IDs_concat_career);
			}
		}
		
		Map<String, List> map = new HashMap();
		map.put("careerAsset", careerAsset_a);
		map.put("zombieCareerAsset", zombieCareerAsset_a);
		map.put("professionID", professionID_a);
		map.put("careerID", careerID_a);
		map.put("IDs_concat_careers", IDs_concat_careers);
		
		return map;
	}
	
	
	/**
	 * Loads the (professionID|careerID) string lists and assigns them to this instance's variables.
	 */
	public static Map<String, List> unpackModdedVillagerHeadwearGraylist(String[] inputList, boolean isWhitelist)
	{
		List<String> professionID_a = new ArrayList<String>();
		List<Integer> careerID_a = new ArrayList<Integer>();
		List<String>  IDs_concat_careers = new ArrayList<String>();
		
		for (String entry : inputList)
		{
			// Remove slashes and double dots to prevent address abuse
			entry.replaceAll("/", ""); // Forward slashses don't need to be escaped
			entry.replaceAll("\\\\", ""); // \ is BOTH String and regex; needs to be double-escaped. See https://stackoverflow.com/questions/1701839/string-replaceall-single-backslashes-with-double-backslashes
			entry.replaceAll("..", "");
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			
			// Initialize temp fields
			String professionID="";
			int careerID=-99;
			
			// Place entries into variables
			try {professionID = splitEntry[0].trim();}     	         catch (Exception e) {professionID="";}
			try {careerID = Integer.parseInt(splitEntry[1].trim());} catch (Exception e) {careerID=-99;}
			String IDs_concat_career = (new StringBuilder()).append(professionID).append(careerID).toString();
			
			if(
					!professionID.equals("") &&
					((isWhitelist && professionID.indexOf("-")!=0)
					|| (!isWhitelist && professionID.indexOf("-")==0))
					) // Something was actually assigned in the try block
			{
				if (isWhitelist && professionID.indexOf("-")!=0)
				{
					professionID_a.add(professionID);
					careerID_a.add(careerID);
					IDs_concat_careers.add(IDs_concat_career);
				}
				else if (!isWhitelist && professionID.indexOf("-")==0)
				{
					professionID_a.add(professionID.substring(1));
					careerID_a.add(careerID);
					IDs_concat_careers.add(IDs_concat_career);
				}
			}
		}
		
		Map<String, List> map = new HashMap();
		map.put("professionID", professionID_a);
		map.put("careerID", careerID_a);
		map.put("IDs_concat_careers", IDs_concat_careers);
		
		return map;
	}
}
