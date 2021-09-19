package astrotibs.villagenames.utility;

import net.minecraft.util.text.TextFormatting;

public class Reference
{
	// Contains common constants for the mod
	public static final String MOD_ID = "villagenames";
	public static final String MOD_NAME = "Village Names";
	public static final String MOD_NAME_COLORIZED = TextFormatting.GOLD + MOD_NAME;
	public static final String VERSION = "4.3.4b";
	public static final String URL = "https://www.curseforge.com/minecraft/mc-mods/village-names";
	public static final String VERSION_CHECKER_URL = "https://gitgud.io/AstroTibs/VillageNames/-/raw/1.12.2/CURRENT_VERSION";
	public static final String MOD_CHANNEL = "vnChannel";
	public static final String CLIENT_PROXY = "astrotibs.villagenames.proxy.ClientProxy";
	public static final String SERVER_PROXY = "astrotibs.villagenames.proxy.ServerProxy";
	public static final String COMMON_PROXY = "astrotibs.villagenames.proxy.CommonProxy";
	public static final String GUI_FACTORY = "astrotibs.villagenames.config.gui.VNGuiFactory";
	
    public static final String VILLAGER_CLASS = "net.minecraft.entity.passive.EntityVillager";
    public static final int STREET_WIDTH = 3;

	// Mod variables
	public static final String ANTIQUE_ATLAS_MODID = "antiqueatlas";
	
	// Vanilla village component class paths
	public static final String
	House4Garden_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$House4Garden",
	Church_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$Church",
	House1_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$House1",
	WoodHut_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$WoodHut",
	Hall_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$Hall",
	Field1_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$Field1",
	Field2_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$Field2",
	House2_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$House2",
	House3_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$House3";
	
    // Config values
    public static final String
    FOLDER_NAMEPIECES = "namepieces",
	FOLDER_NEWVILLAGES = "newvillages",
    
	// Main config
	CATEGORY_GENERAL = "general",
	CATEGORY_VILLAGER_PROFESSIONS = "villager professions",
	CATEGORY_WELL_KILL_SWITCH = "well kill switch",
	CATEGORY_VILLAGER_SKIN_TONES = "villager skin tones",
	CATEGORY_MISCELLANEOUS = "miscellaneous",
	CATEGORY_NAMING = "naming",
	CATEGORY_MOD_INTEGRATION = "mod integration",
	CATEGORY_ZOMBIE_CONVERSION = "zombie conversion",
	
	// Syllable pools
	SYL_SUF = " syllable pool",
	CATEGORY_VILLAGER_SYLLABLE_POOL = "villager"+SYL_SUF,
	CATEGORY_VILLAGE_SYLLABLE_POOL = "village"+SYL_SUF,
	CATEGORY_TEMPLE_SYLLABLE_POOL = "temple"+SYL_SUF,
	CATEGORY_MINESHAFT_SYLLABLE_POOL = "mineshaft"+SYL_SUF,
	CATEGORY_FORTRESS_SYLLABLE_POOL = "fortress"+SYL_SUF,
	CATEGORY_STRONGHOLD_SYLLABLE_POOL = "stronghold"+SYL_SUF,
	CATEGORY_MONUMENT_SYLLABLE_POOL = "monument"+SYL_SUF,
	CATEGORY_END_CITY_SYLLABLE_POOL = "end city"+SYL_SUF,
	CATEGORY_MANSION_SYLLABLE_POOL = "mansion"+SYL_SUF,
	CATEGORY_GOLEM_SYLLABLE_POOL = "golem"+SYL_SUF,
	CATEGORY_PET_SYLLABLE_POOL = "pet"+SYL_SUF,
	CATEGORY_DRAGON_SYLLABLE_POOL = "dragon"+SYL_SUF,
	CATEGORY_ANGEL_SYLLABLE_POOL = "angel"+SYL_SUF,
	CATEGORY_DEMON_SYLLABLE_POOL = "demon"+SYL_SUF,
	CATEGORY_GOBLIN_SYLLABLE_POOL = "goblin"+SYL_SUF,
	CATEGORY_ALIEN_SYLLABLE_POOL = "alien"+SYL_SUF,
	CATEGORY_ALIEN_VILLAGE_SYLLABLE_POOL = "alien village"+SYL_SUF,
	CATEGORY_CUSTOM_SYLLABLE_POOL = "custom"+SYL_SUF,
	
	// Village generator
	CATEGORY_VILLAGE_GENERATOR = "village generator",
	
    VN_BUILDING_CLASSPATH_STUB = "astrotibs.villagenames.village.biomestructures.",
    PLAINS_BUILDING_STUB =  "PlainsStructures$Plains",
    DESERT_BUILDING_STUB =  "DesertStructures$Desert",
    TAIGA_BUILDING_STUB =   "TaigaStructures$Taiga",
    SAVANNA_BUILDING_STUB = "SavannaStructures$Savanna",
    SNOWY_BUILDING_STUB =   "SnowyStructures$Snowy",
    JUNGLE_BUILDING_STUB =   "JungleStructures$Jungle",
    SWAMP_BUILDING_STUB =   "SwampStructures$Swamp";
}
