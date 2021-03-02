package astrotibs.villagenames.utility;

public class Reference
{
	// Contains common constants for the mod
	public static final String MOD_ID = "villagenames";
	public static final String MOD_NAME = "Village Names";
	public static final String VERSION = "4.1.5";
	public static final String URL = "https://www.curseforge.com/minecraft/mc-mods/village-names";
	public static final String VERSION_CHECKER_URL = "https://gitgud.io/AstroTibs/VillageNames/raw/1.12.2/CURRENT_VERSION";
	public static final String MOD_CHANNEL = "vnChannel";
	public static final String CLIENT_PROXY = "astrotibs.villagenames.proxy.ClientProxy";
	public static final String SERVER_PROXY = "astrotibs.villagenames.proxy.ServerProxy";
	public static final String COMMON_PROXY = "astrotibs.villagenames.proxy.CommonProxy";
	public static final String GUI_FACTORY = "astrotibs.villagenames.config.gui.VNGuiFactory";
	
    public static final String VILLAGER_CLASS = "net.minecraft.entity.passive.EntityVillager";
    public static final int STREET_WIDTH = 3;
	
	// Vanilla village component class paths
	public static final String House4Garden_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$House4Garden";
	public static final String Church_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$Church";
	public static final String House1_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$House1";
	public static final String WoodHut_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$WoodHut";
	public static final String Hall_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$Hall";
	public static final String Field1_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$Field1";
	public static final String Field2_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$Field2";
	public static final String House2_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$House2";
	public static final String House3_CLASS = "net.minecraft.world.gen.structure.StructureVillagePieces$House3";
	
    // Config values
    public static final String FOLDER_NAMEPIECES = "namepieces";
    public static final String FOLDER_NEWVILLAGES = "newvillages";
    public static final String CATEGORY_VILLAGE_GENERATOR = "Village Generator";
    public static final String VN_BUILDING_CLASSPATH_STUB = "astrotibs.villagenames.village.biomestructures.";
    public static final String PLAINS_BUILDING_STUB =  "PlainsStructures$Plains";
    public static final String DESERT_BUILDING_STUB =  "DesertStructures$Desert";
    public static final String TAIGA_BUILDING_STUB =   "TaigaStructures$Taiga";
    public static final String SAVANNA_BUILDING_STUB = "SavannaStructures$Savanna";
    public static final String SNOWY_BUILDING_STUB =   "SnowyStructures$Snowy";
}
