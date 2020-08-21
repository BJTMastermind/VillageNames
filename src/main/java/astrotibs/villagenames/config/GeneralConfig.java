package astrotibs.villagenames.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.utility.LogHelper;
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
	// Added in v3.1banner
	public static boolean villageBanners;
	public static int signYaw;
	
	public static boolean concreteWell;
	
	
	public static boolean codexChestLoot;
	public static boolean versionChecker;
	
	public static boolean wellBoundary;
	public static boolean wellDecorations;
	public static boolean debugMessages;
	public static boolean nameGolems;
	
	public static String[] modNameMappingAutomatic;
	public static String[] modNameMappingClickable;
	
	public static String[] modProfessionMapping;
	
	public static int PMMerchantProfessionMap;
	public static int PMLostMinerProfessionMap;
	public static boolean TQVillageNames;
	
	public static String[] modStructureNames;

	public static boolean modernVillagerSkins; // Added in v3.1

	public static boolean moddedVillagerHeadwear; // Added in v3.1.1

	// Added in v3.2
	public static String[] moddedVillagerHeadwearGraylist;
	public static ArrayList<String> moddedVillagerHeadwearWhitelist = new ArrayList<String>();
	public static ArrayList<String> moddedVillagerHeadwearBlacklist = new ArrayList<String>();
	
	// Added in v3.2
	public static String[] moddedVillagerModularSkins;
	public static Map<String, ArrayList> moddedVillagerCareerSkins;
	public static ArrayList<String> careerAsset_a;
	public static ArrayList<String> zombieCareerAsset_a;
	public static ArrayList<String> professionID_a;
    public static boolean villagerSkinTones;
    public static float villagerSkinToneVarianceAnnealing;
    public static float villagerSkinToneVarianceScale;
    
	public static boolean modernVillagerTrades;
	//public static boolean villagerLegacyTrades;
	public static boolean treasureTrades;
	public static boolean writtenBookTrade;
	public static boolean swampHutMushroomPot;
	
	public static String[] modDye;
	
	public static String[] zombieCureCatalysts;
	public static String[] zombieCureGroups;

    // --- Villages --- //
	public static boolean newVillageGenerator;
	public static int newVillageSize;
	public static int newVillageSpacingMedian;
	public static int newVillageSpacingSpread;
	public static String[] spawnBiomesNames;
	// Legacy Village buildings
	public static String componentLegacyHouse4Garden_string; public static ArrayList<Integer> componentLegacyHouse4Garden_vals;
	public static String componentLegacyChurch_string; public static ArrayList<Integer> componentLegacyChurch_vals;
	public static String componentLegacyHouse1_string; public static ArrayList<Integer> componentLegacyHouse1_vals;
	public static String componentLegacyWoodHut_string; public static ArrayList<Integer> componentLegacyWoodHut_vals;
	public static String componentLegacyHall_string; public static ArrayList<Integer> componentLegacyHall_vals;
	public static String componentLegacyField1_string; public static ArrayList<Integer> componentLegacyField1_vals;
	public static String componentLegacyField2_string; public static ArrayList<Integer> componentLegacyField2_vals;
	public static String componentLegacyHouse2_string; public static ArrayList<Integer> componentLegacyHouse2_vals;
	public static String componentLegacyHouse3_string; public static ArrayList<Integer> componentLegacyHouse3_vals;
	
	// Misc new village stuff
	public static boolean spawnModdedVillagers;
	public static boolean useVillageColors;
	
	
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
		// --- New Villages --- //
		newVillageGenerator = config.getBoolean("Activate New Village Generator", "Village Generator", true, "Use replacement village generation system. You may need to deactivate village generation from other mods. All other settings in this section require this to be true.");
		newVillageSize = config.getInt("Village Size", "Village Generator", 1, 1, 10, "How large villages are. Vanilla is 1.");
		newVillageSpacingMedian = config.getInt("Village Spacing: Median", "Village Generator", 20, 1, 100, "Median distance between villages. Vanilla is 20.");
		newVillageSpacingSpread = config.getInt("Village Spacing: Range", "Village Generator", 12, 1, 100, "Variation in distances between villages. Must be lower than Median value. Vanilla is 12.");

		
		ArrayList<Integer> ali; // For setting default values as integer lists
		
		// Legacy Village components
		ali = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 2, 4));
		componentLegacyHouse4Garden_string = config.getString("Component: Legacy Small House", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 4");
		componentLegacyHouse4Garden_vals = parseIntegerArray(componentLegacyHouse4Garden_string, ali);

		ali = new ArrayList<Integer>(Arrays.asList(0, 1, 0, 1, 1));
		componentLegacyChurch_string = config.getString("Component: Legacy Church", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 20");
		componentLegacyChurch_vals = parseIntegerArray(componentLegacyChurch_string, ali);

		ali = new ArrayList<Integer>(Arrays.asList(0, 1, 0, 1, 2));
		componentLegacyHouse1_string = config.getString("Component: Legacy Library", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 20");
		componentLegacyHouse1_vals = parseIntegerArray(componentLegacyHouse1_string, ali);

		ali = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 5));
		componentLegacyWoodHut_string = config.getString("Component: Legacy Hut", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 3");
		componentLegacyWoodHut_vals = parseIntegerArray(componentLegacyWoodHut_string, ali);

		ali = new ArrayList<Integer>(Arrays.asList(0, 1, 0, 1, 2));
		componentLegacyHall_string = config.getString("Component: Legacy Butcher Shop", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 15");
		componentLegacyHall_vals = parseIntegerArray(componentLegacyHall_string, ali);

		ali = new ArrayList<Integer>(Arrays.asList(0, 1, 1, 1, 4));
		componentLegacyField1_string = config.getString("Component: Legacy Large Farm", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 3");
		componentLegacyField1_vals = parseIntegerArray(componentLegacyField1_string, ali);

		ali = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 2, 4));
		componentLegacyField2_string = config.getString("Component: Legacy Small Farm", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 3");
		componentLegacyField2_vals = parseIntegerArray(componentLegacyField2_string, ali);

		ali = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 1, 1));
		componentLegacyHouse2_string = config.getString("Component: Legacy Smithy", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 15");
		componentLegacyHouse2_vals = parseIntegerArray(componentLegacyHouse2_string, ali);

		ali = new ArrayList<Integer>(Arrays.asList(0, 1, 0, 2, 3));
		componentLegacyHouse3_string = config.getString("Component: Legacy Large House", "Village Generator", convertIntegerArrayToString(ali), "Generation stats for this component in all villages. Vanilla weight is 8");
		componentLegacyHouse3_vals = parseIntegerArray(componentLegacyHouse3_string, ali);
		
		// Misc
		spawnModdedVillagers = config.getBoolean("Allow mod villagers in new structures", "Village Generator", false, "When modern structures spawn random villagers on generation, set this to true to allow non-vanilla professions.");
		useVillageColors = config.getBoolean("Use village colors", "Village Generator", true, "Whether to apply the village's colors to concrete, terracotta, carpet, etc.");
		
		spawnBiomesNames = config.getStringList("Spawn Biome Names", "Village Generator",
				new String[] {
						// Vanilla
						"Plains",
						"Desert",
						"Extreme Hills",
						"Forest",
						"Taiga",
						"Swampland",
						"Ice Plains",
						"MushroomIsland",
						"ForestHills",
						"TaigaHills",
						"Jungle",
						"JungleHills",
						"Birch Forest",
						"Birch Forest Hills",
						"Roofed Forest",
						"Cold Taiga",
						"Mega Taiga",
						"Mega Taiga Hills",
						"Savanna",
						"Mesa",
						"Sunflower Plains",
						"Flower Forest",
						"Mega Spruce Taiga",
						"Mega Spruce Taiga Hill",
						// Biomes o' Plenty
						"Bamboo Forest",
						"Bayou",
						"Bog",
						"Boreal Forest",
						"Canyon",
						"Chaparral",
						"Cherry Blossom Grove",
						"Coniferous Forest",
						"Snowy Coniferous Forest",
						"Deciduous Forest",
						"Dense Forest",
						"Eucalyptus Forest",
						"Flower Field",
						"Frost Forest",
						"Fungi Forest",
						"Garden",
						"Grassland",
						"Grove",
						"Heathland",
						"Highland",
						"Lavender Fields",
						"Lush Swamp",
						"Maple Woods",
						"Meadow",
						"Mountain",
						"Mystic Grove",
						"Orchard",
						"Outback",
						"Prairie",
						"Rainforest",
						"Redwood Forest",
						"Sacred Springs",
						"Seasonal Forest",
						"Shield",
						"Shrubland",
						"Steppe",
						"Temperate Rainforest",
						"Tropical Rainforest",
						"Tundra",
						"Wetland",
						"Woodland",
						"Xeric Shrubland",
						"Meadow Forest",
						"Oasis",
						"Scrubland",
						"Seasonal Forest Clearing",
						"Spruce Woods",
						// ATG
						"Rocky Steppe",
						// ExtrabiomeXL
						"Autumn Woods",
						"Forested Hills",
						"Green Hills",
						"Mini Jungle",
						"Mountain Taiga",
						"Pine Forest",
						"Redwood Lush",
						"Snowy Forest",
						"Snowy Rainforest",
						"Woodlands",
						// Highlands
						"Autumn Forest",
						"Badlands",
						"Birch Hills",
						"Highlands",
						"Lowlands",
						"Outback",
						"Pinelands",
						"Sahel",
						"Tall Pine Forest",
						"Tropics",
						},
				"Names of biomes which can spawn villages. Only used with Village Generator, and only applies to Overworld. Note that this list is EXCLUSIVE: other mod configs won't override this. You have to paste all biome names here.");
		
	    nameSign = config.getBoolean("Name Sign", Configuration.CATEGORY_GENERAL, true, "Town centers display their name on one or more signs.");
	    recordStructureCoords = config.getBoolean("Record Structure Coords", Configuration.CATEGORY_GENERAL, true, "Books generated by villagers or the Codex record the structure's coordinates.");
	    villagerDropBook = config.getBoolean("Villager drops book", Configuration.CATEGORY_GENERAL, false, "Village books are dropped by the villager rather than going directly into your inventory.");
	    villagerSellsCodex = config.getBoolean("Villager makes codex", Configuration.CATEGORY_GENERAL, true, "Librarian villagers will give you a codex if you right-click them while holding emerald, iron ingots, and/or gold ingots.");
	    
	    wellBoundary = config.getBoolean("Well boundary", Configuration.CATEGORY_GENERAL, true, "Whether to surround the well with colored blocks");
	    wellSlabs = config.getBoolean("Well slabs", Configuration.CATEGORY_GENERAL, true, "Replace the cobblestone rims of wells with stone slabs, making it easier for players and villagers to escape if they fall in.");

	    // Added in v3.1banner
	    villageBanners = config.getBoolean("Village Banner", Configuration.CATEGORY_GENERAL, true, "The town banner pattern is displayed at the town center.");
	    signYaw = config.getInt("Sign Yaw", Configuration.CATEGORY_GENERAL, 3, 0, 4, "If Village Banner is enabled: Degree to which well signs and banners should face inward. At 0 they face directly outward away from the well; at 4 they face each other.");
	    
	    wellDecorations = config.getBoolean("Allow well decorations", "Well Kill Switch", true, "Set this to false to disable all well decoration: sign, slabs, terracotta, concrete.");
	    
	    concreteWell = config.getBoolean("Concrete Well", Configuration.CATEGORY_GENERAL, true, "Whether to decorate wells with Concrete and Glazed Terracotta instead of stained clay");
	    
	    
	    
	    // --------------Professions-----------------//
	    treasureTrades = config.getBoolean("Treasure Trades", "villager professions", true, "High-level Librarians and Cartographers will offer enchanted books and treasures in exchange for " + Reference.MOD_NAME + " items.");
	    writtenBookTrade = config.getBoolean("Written Book Trade", "villager professions", true, "Change the vanilla Librarian trade to require a single written book instead of two identical written books");

	    // Changed in 3.1
	    modernVillagerSkins = config.getBoolean("Modern Villager Profession Skins", "villager professions", true, "Use the composite 1.14 Villager skins");
	    // Added in v3.1
	    modernVillagerTrades = config.getBoolean("Modern Villager Trades", "villager professions", true, "Use JE 1.14 / BE 1.12 trade offerings and add the Mason villager");

	    // Added in v3.1.1
	    moddedVillagerHeadwear = config.getBoolean("Modded Villager Headwear", "villager professions", false, "If modern skins are enabled: renders the headwear layer for non-vanilla villager professions, if one exists.");

	    // Added in v3.2
	    moddedVillagerHeadwearGraylist = config.getStringList("Modded Villager Headwear Graylist", "villager professions", new String[]{
				"forestry:apiarist", // Forestry Apiarist
				"-openblocks:radio", // Open Blocks Music Merchant
				"bewitchment:alchemist", // Bewitchment Alchemist... not sure if this is the ID because I can't get the thing to load
	    		},
	    		"(If modern skins are enabled) List of profession IDs for other mods' villagers. A normal value will be whitelisted: it will display that villager's headwear layer even if Modded Villager Headwear is false. "
	    		+ "Adding a negative sign in front of the ID int will blacklist the profession so that its headwear layer never renders.");
	    
	    // Extract the values and populate the white and black lists
	    for (String prof_s : moddedVillagerHeadwearGraylist)
	    {
	    	if (prof_s.trim().indexOf("-")==0)
	    	{
	    		// There is a hyphen out front, so this is a negative value. Pass it with the hyphen intact.
	    		moddedVillagerHeadwearBlacklist.add(prof_s.trim());
	    	}
	    	else
	    	{
	    		// There is no hyphen out front, so this is a positive value.
	    		moddedVillagerHeadwearWhitelist.add(prof_s.trim());
	    	}
	    }

	    // Added in v3.2
	    moddedVillagerModularSkins = config.getStringList("Modded Villager Modular Skins", "villager professions", new String[]{
				"for_apiarist|for_apiarist|forestry:apiarist", // Forestry
				"for_arborist|for_arborist|forestry:arborist", // Forestry
				"fa_archaeologist||fossil:archeologist", // Fossils and Archaeology
				"rc_engineer|rc_engineer|railcraft:trackman", // Railcraft
				"ob_musicmerchant||openblocks:radio", // Open Blocks
				"myc_archivist||mystcraft:archivist", // Mystcraft
				"bew_alchemist||bewitchment:alchemist", // Bewitchment -v3.2.3
	    		"hac_researcher|hac_researcher|dcs_climate:agri_researcher", // HeatAndClimateMod -v3.2.3
				"hac_researcher|hac_researcher|dcs_climate:engineer", // HeatAndClimateMod -v3.2.3
				"hac_trader|hac_trader|dcs_climate:trader", // HeatAndClimateMod -v3.2.3
				},
	    		"(If modern skins are enabled) List of profession IDs for other mods' villagers to render in the modular skin style. Format is: careerAsset|zombieCareerAsset|professionID\n"+
	    		"careerAsset: career skin png to be overlaid onto the villager, located in assets\\"+Reference.MOD_ID.toLowerCase()+"\\textures\\entity\\villager\\profession\n"+
	    				"The default values are all available in "+Reference.MOD_NAME+". You can access custom values with a resourcepack.\n"
	    						+ "zombieCareerAsset: a zombie career png, located in the corresponding zombie_villager directory. You may leave this value blank, in which case it will use the non-zombie career overlay.\n"
	    						+ "professionID: the ID associated with the mod profession.");
	    
	    // Assign the map now and immediately extract it into arrays for faster lookup
	    moddedVillagerCareerSkins = GeneralConfig.unpackModVillagerSkins(GeneralConfig.moddedVillagerModularSkins);
	    careerAsset_a = (ArrayList<String>)moddedVillagerCareerSkins.get("careerAsset");
	    zombieCareerAsset_a = (ArrayList<String>)moddedVillagerCareerSkins.get("zombieCareerAsset");
	    professionID_a = (ArrayList<String>)moddedVillagerCareerSkins.get("professionID");


	    villagerSkinTones = config.getBoolean("Display Skin Tones", "villager skin tones", true, "Display Gaussian-distributed random skin tones assigned to villagers");
	    villagerSkinToneVarianceAnnealing = config.getFloat("Skin Tone Variance Annealing", "villager skin tones", 8F/3, 0, Float.MAX_VALUE,
	    		"Statistical variance in skin tone for a population decreases as the number of skin-tone-affecting biome tags increases.\n"
	    		+ "Setting this value to zero eliminates that effect, making skin tone vary equally everywhere (aside from culling to the darkest/lightest tones).\n"
	    		+ "Increasing this value makes skin tone variation less likely in qualifying biomes.");
	    villagerSkinToneVarianceScale = config.getFloat("Skin Tone Variance Scale", "villager skin tones", 1F, 0, Float.MAX_VALUE,
	    		"Proportionality constant for variance everywhere, irrespective of biome. Set this to zero for absolutely no variation for a given biome.\n"
	    		+ "Skin tones are culled to the darkest and lightest values, so setting this arbitrarily high will result in ONLY the darkest or lightest villagers.\n"
	    		+ "I estimate that the distribution is flattest, and thus population variance is maximized, around a value of about 2.6.");
	    
	    zombieCureCatalysts = config.getStringList("Zombie Cure Catalysts", "villager professions", new String[]{
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
	    
	    zombieCureGroups = config.getStringList("Zombie Cure Groups", "villager professions", new String[]{
 				"vanilla|0.3|14"
 				},
 				"When curing a zombie villager, all blocks of the same named group will use these stats. "
 				+ "Format is: group|speedup|limit\n"
 				+ "group is the group name assigned in Zombie Cure Catalysts above.\n"
 				+ "speedup is the per-block percentage point boost in conversion speed. That is: a value of 1.0 increases the conversion by about 1 percentage point per group block found. "
 				+ "negative values will likewise reduce the conversion speed, making conversion take longer.\n"
 				+ "limit is the maximum number of blocks in this group that will apply the group speedup effect."
 				);
 		
 		
	    
    	//--------------Miscellaneous-----------------//
	    
	    versionChecker = config.getBoolean("Version Checker", "miscellaneous", false, "Displays a client-side chat message on login if there's an update available. If the URL pinged by the checker happens to be down, your game will freeze for a while on login. Turn this on at your own risk.");
	    codexChestLoot = config.getBoolean("Codex Chest Loot", "miscellaneous", true, "The Codex can appear as rare chest loot.");
	    debugMessages = config.getBoolean("Debug messages", "miscellaneous", false, "Print debug messages to the console, print the class paths of entities and blocks you right-click.");
	    swampHutMushroomPot = config.getBoolean("Swamp Hut Mushroom", "miscellaneous", true, "1.8+ has a bug where the clay pot in the Witch's swamp hut is empty. This flag will correctly place a red mushroom in the pot.");
	    
	    
	    
	    //--------------Names-----------------//
	    
	    nameEntities = config.getBoolean("Entity names", "Naming", true, "Entities reveal their names when you right-click them, or automatically if so assigned.");
	    addJobToName = config.getBoolean("Entity professions", "Naming", false, "An entity's name also includes its profession/title. You may need to right-click the entity to update its name plate.");
	    nameGolems = config.getBoolean("Golem names", "Naming", true, "Right-click village Golems to learn their name.");
	    
	    nitwitProfession = config.getString("Nitwit Profession", "Naming", "", "The career displayed for a Nitwit"); // Per-profession registry and spawning is broken in 1.8
	    
		// Automatic Names
		
		modNameMappingAutomatic = config.getStringList("Automatic Names", "Naming", new String[]{
				
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
				"demon|Lord of Torment|com.emoniph.witchery.entity.EntityLordOfTorment|add"
				
				},
				"List of entities that will generate a name automatically when they appear. Useful for aggressive or boss mobs.\n"
				+ "Format is: nameType|profession|classPath|addOrRemove\n"
				+ "nameType is the name pool for the entity, or a hyphenated series of pools like \"angel-golem\".\n"
				+ "profession is displayed if that config flag is enabled. It can be left blank for no profession.\n"
				+ "classPath is the mod's address to the entity class.\n"
								+ "nameType options:\n"
								+ "villager, dragon, golem, alien, angel, demon, goblin, custom\n"
				+ "addOrRemove - type \"add\" to automatically add names tags to ALL COPIES of this entity upon spawning, or \"remove\" to automatically remove.\n"
				+ "Be VERY CAUTIOUS about what entities you choose to add to this list!"
								);
		
	    

		// Clickable Names
	    
		modNameMappingClickable = config.getStringList("Clickable Names", "Naming", new String[]{
				
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
				"angel-goblin|Questing Ram|twilightforest.entity.passive.EntityTFQuestRam",
				
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
								+ "villager, dragon, golem, alien, angel, demon, goblin, custom\n"
								);
		
		
		//--------------Mod Integration-----------------//
		
	    modDye = config.getStringList("Mod Priority: Dye", "Mod Integration", new String[]{
	    		"futuremc",
 				"biomesoplenty",
 				"quark",
 				"botania",
 				},
 				"Priority order for referencing dye for villager trade offers. The version highest on the list and registered in your game will be used."
 				);
	    
		
		
		// Mapping for modded structures, and the creatures that can name them
		modStructureNames = config.getStringList("Mod Structures", "Mod Integration", new String[]{
				
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
				"List of mod structures that can be named with a Codex, or by right-clicking an entity in that structure (optional)."
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
		
		// New mod profession mapping
		// Changed IDs from integers to ProfessionForge.getRegistryName() strings
		modProfessionMapping = config.getStringList("Mod Professions", "Mod Integration", new String[]{
				"Apiarist|forestry:apiarist|4", // Forestry
				"Arborist|forestry:arborist|0", // Forestry
				"Archaeologist|fossil:archeologist|2", // Fossils and Archaeology
				"Engineer|railcraft:trackman|3", // Railcraft
				"Music Merchant|openblocks:radio|5", // Open Blocks
				"Archivist|mystcraft:archivist|1", // Mystcraft
				"Alchemist|bewitchment:alchemist|2", // Bewitchment -v3.2.3
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
		
				
		
		// Primitive Mobs villager mapping
	    //PMMerchantProfession = config.getString("PMMerchantProfession", "Mapping Professions", "Merchant", "The career displayed for Primitive Mobs's Traveling Merchant. Blank this out to display no profession regardless of addJobToName.");
	    PMMerchantProfessionMap = config.getInt("PM Traveling Merchant Profession ID", "Mod Integration", 0, 0, 5,
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
	    PMLostMinerProfessionMap = config.getInt("PM Lost Miner Profession ID", "Mod Integration", 3, 0, 5,
	    		"Which vanilla archetype the traveling merchant emulates in order to generate hint pages.\n"
				+ "Use this reference:\n"
				+ "-1=None\n"
				+ "0=Farmer\n"
				+ "1=Librarian\n"
				+ "2=Priest\n"
				+ "3=Blacksmith\n"
				+ "4=Butcher\n"
				+ "5=Nitwit\n");
	    
	    TQVillageNames = config.getBoolean("ToroQuest Village Names", "Mod Integration", true, "If you're using ToroQuest, write its town names into village books. Additionally, " + Reference.MOD_NAME + " well signs will not generate, since they do so before ToroQuest assigns a town name.");
	    
	    
	    if (config.hasChanged()) config.save();
		
	}
	

	/**
	 * Inputs a (Profession|ID|vanillaType) String list and breaks it into three array lists
	 */
	public static Map<String, ArrayList> unpackMappedProfessions(String[] inputList) {
		ArrayList<String>  otherModProfessions = new ArrayList<String>();
		ArrayList<String> otherModIDs = new ArrayList<String>(); // v3.2 - profession ints are deprecated
		ArrayList<Integer> vanillaProfMaps = new ArrayList<Integer>();
		
		for (String entry : inputList) {
			// Remove parentheses
			entry.replaceAll("\\)", "");
			entry.replaceAll("\\(", "");
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			
			// Initialize temp fields
			String otherModProfession="";
			String otherModID=""; // v3.2 - profession ints are deprecated
			int vanillaProfMap=-1;
			
			// Place entries into variables
			try {otherModProfession = splitEntry[0].trim();}               catch (Exception e) {otherModProfession="";}
			try {otherModID = splitEntry[1].trim();}                       catch (Exception e) {otherModID="";} // v3.2 - profession ints are deprecated
			try {vanillaProfMap = Integer.parseInt(splitEntry[2].trim());} catch (Exception e) {vanillaProfMap=-1;}
			
			if( !otherModProfession.equals("") && !otherModID.equals("") ) { // v3.2 - profession ints are deprecated
				otherModProfessions.add(otherModProfession);
				otherModIDs.add(otherModID);
				vanillaProfMaps.add(vanillaProfMap);
			}
		}
		
		Map<String,ArrayList> map =new HashMap();
		map.put("Professions",otherModProfessions);
		map.put("IDs",otherModIDs);
		map.put("VanillaProfMaps",vanillaProfMaps);
		
		return map;
	}
	
	

	/**
	 * Loads the (nameType|structureType|structureTitle|dimensionName|bookType|entityClassPath) string lists from othermods.cfg > Mod Structures
	 * and assigns them to this instance's variables.
	 */
	public static Map<String, ArrayList> unpackModStructures(String[] inputList) {
		
		ArrayList<String> otherModNameTypes = new ArrayList<String>();
		ArrayList<String> otherModStructureTypes = new ArrayList<String>();
		ArrayList<String> otherModStructureTitles = new ArrayList<String>();
		ArrayList<String> otherModDimensionNames = new ArrayList<String>();
		ArrayList<String> otherModBookTypes = new ArrayList<String>();
		ArrayList<String> otherModClassPaths = new ArrayList<String>();
		
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

		Map<String,ArrayList> map =new HashMap();
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
	public static Map<String, ArrayList> unpackMappedNames(String[] inputList) {
		
		ArrayList<String> otherModNameTypes = new ArrayList<String>();
		ArrayList<String> otherModProfessions = new ArrayList<String>();
		ArrayList<String> otherModClassPaths = new ArrayList<String>();
		ArrayList<String> addOrRemoveA = new ArrayList<String>();
		
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

		Map<String,ArrayList> map =new HashMap();
		map.put("NameTypes",otherModNameTypes);
		map.put("Professions",otherModProfessions);
		map.put("ClassPaths",otherModClassPaths);
		map.put("AddOrRemove",addOrRemoveA);
		
		return map;
	}
	
	
	/**
	 * Loads the (group|classPath|unlocName|meta) string lists and assigns them to this instance's variables.
	 */
	public static Map<String, ArrayList> unpackZombieCureCatalysts(String[] inputList) {
		ArrayList<String>  zombieCureCatalystGroups = new ArrayList<String>();
		ArrayList<String> zombieCureCatalystClassPaths = new ArrayList<String>();
		ArrayList<String> zombieCureCatalystUnlocNames = new ArrayList<String>();
		ArrayList<Integer> zombieCureCatalystMetas = new ArrayList<Integer>();
		
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
		
		Map<String,ArrayList> map = new HashMap();
		map.put("Groups",zombieCureCatalystGroups);
		map.put("ClassPaths",zombieCureCatalystClassPaths);
		map.put("UnlocNames",zombieCureCatalystUnlocNames);
		map.put("Metas",zombieCureCatalystMetas);
		
		return map;
	}
	
	/**
	 * Loads the (group|speedup|limit) string lists and assigns them to this instance's variables.
	 */
	public static Map<String, ArrayList> unpackZombieCureGroups(String[] inputList) {
		ArrayList<String>  zombieCureGroupGroups = new ArrayList<String>();
		ArrayList<Double> zombieCureGroupSpeedups = new ArrayList<Double>();
		ArrayList<Integer> zombieCureGroupLimits = new ArrayList<Integer>();
		
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
		
		Map<String,ArrayList> map = new HashMap();
		map.put("Groups",zombieCureGroupGroups);
		map.put("Speedups",zombieCureGroupSpeedups);
		map.put("Limits",zombieCureGroupLimits);
		
		return map;
	}

	// Added in v3.2
	/**
	 * Loads the (careerAsset|zombieCareerAsset|professionID) string lists and assigns them to this instance's variables.
	 */
	public static Map<String, ArrayList> unpackModVillagerSkins(String[] inputList) {
		ArrayList<String>  careerAsset_a = new ArrayList<String>();
		ArrayList<String> zombieCareerAsset_a = new ArrayList<String>();
		ArrayList<String> professionID_a = new ArrayList<String>();
		
		for (String entry : inputList) {
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
			
			// Place entries into variables
			try {careerAsset = splitEntry[0].trim();}       catch (Exception e) {}
			try {zombieCareerAsset = splitEntry[1].trim();} catch (Exception e) {}
			try {professionID = splitEntry[2].trim();}     	catch (Exception e) {}
			
			if(!careerAsset.equals("")) { // Something was actually assigned in the try block
				careerAsset_a.add(careerAsset);
				zombieCareerAsset_a.add(zombieCareerAsset);
				professionID_a.add(professionID);
			}
		}
		
		Map<String,ArrayList> map = new HashMap();
		map.put("careerAsset",careerAsset_a);
		map.put("zombieCareerAsset",zombieCareerAsset_a);
		map.put("professionID",professionID_a);
		
		return map;
	}

	/**
	 * Used to convert the comma-separated-integer string in the config value into an array of integers
	 * Returns the given default array if the user screws up.
	 */
	public static ArrayList<Integer> parseIntegerArray(String configvalue, ArrayList<Integer> defaultValues)
	{
		try
		{
			String[] sMPA1_stringarray = configvalue.split(",");
			ArrayList<Integer> integerArrayListToReturn = new ArrayList<Integer>();
			
			for (int i=0; i<sMPA1_stringarray.length; i++)
			{
				integerArrayListToReturn.add(Integer.parseInt(sMPA1_stringarray[i].trim()));
			}

			// HALL OF SHAME
			
			// User entered wrong number of parameters
			if (sMPA1_stringarray.length!=5)
			{
				LogHelper.error("Config entry " + configvalue + " requires five values, not " + sMPA1_stringarray.length + ". Using default values " + convertIntegerArrayToString(defaultValues) + " until this is fixed.");
				return defaultValues;
			}
			
			// User entered a negative component weight
			if (integerArrayListToReturn.get(0) < 0)
			{
				integerArrayListToReturn.set(0, 0);
				LogHelper.error("The first value of config entry " + configvalue + " is a weight and must not be less than zero. It will be set to 0 until this is fixed.");
			}
			
			// User's lower bound for number of structures is negative
			if ((integerArrayListToReturn.get(1) * GeneralConfig.newVillageSize + integerArrayListToReturn.get(2)) < 0)
			{
				LogHelper.error("Values two and three of config entry " + configvalue + " can result in fewer than zero of this structure component. Using default values " + convertIntegerArrayToString(defaultValues) + " until this is fixed.");
				return defaultValues;
			}
			
			// User's upper bound for number of structures is negative
			if ((integerArrayListToReturn.get(3) * GeneralConfig.newVillageSize + integerArrayListToReturn.get(4)) < 0)
			{
				LogHelper.error("Values four and five of config entry " + configvalue + " will result in fewer than zero of this structure component. Using default values " + convertIntegerArrayToString(defaultValues) + " until this is fixed.");
				return defaultValues;
			}
			
			// User's lower bound for number of structures is greater than their upper bound
			if ((integerArrayListToReturn.get(1) * GeneralConfig.newVillageSize + integerArrayListToReturn.get(2)) > (integerArrayListToReturn.get(3) * GeneralConfig.newVillageSize + integerArrayListToReturn.get(4)))
			{
				LogHelper.error("Values two through five of config entry " + configvalue + " result in a higher upper bound than a lower bound for this structure component. Using default values " + convertIntegerArrayToString(defaultValues) + " until this is fixed.");
				return defaultValues;
			}
			
			// This only happens if the user didn't cock up royally
			return integerArrayListToReturn;
		}
		catch (Exception e) // Config entry was malformed
		{
			LogHelper.error("Config entry " + configvalue + " was malformed. Check that it is five comma-separated integers. Using default values " + convertIntegerArrayToString(defaultValues) + " until this is fixed.");
			return defaultValues;
		}
	}
	
	/**
	 * Converts an integer arraylist back into a comma-separated string
	 */
	public static String convertIntegerArrayToString(ArrayList<Integer> arraylist)
	{
		String s=arraylist.get(0).toString();
		
		for (int i=1; i<arraylist.size(); i++) 
		{
			s+=","+arraylist.get(i).toString();
		}
		return s;
	}
}
