package astrotibs.villagenames;

import java.io.File;

import astrotibs.villagenames.advancements.ModTriggers;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.client.renderer.entity.RenderVillagerModern;
import astrotibs.villagenames.client.renderer.entity.RenderZombieVillagerModern;
import astrotibs.villagenames.command.CommandBanner;
import astrotibs.villagenames.command.CommandName;
import astrotibs.villagenames.config.ConfigInit;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.config.village.VillageGeneratorConfigHandler;
import astrotibs.villagenames.handler.ChestLootHandler;
import astrotibs.villagenames.handler.DevVersionWarning;
import astrotibs.villagenames.handler.EntityMonitorHandler;
import astrotibs.villagenames.handler.ReputationHandler;
import astrotibs.villagenames.handler.ServerCleanExpired;
import astrotibs.villagenames.handler.ServerTrackerStarter;
import astrotibs.villagenames.handler.VersionChecker;
import astrotibs.villagenames.handler.VillagerTradeHandler;
import astrotibs.villagenames.init.InventoryRender;
import astrotibs.villagenames.integration.antiqueatlas.VillageWatcherAA;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.nbt.NBTUpdater;
import astrotibs.villagenames.network.MessageModernVillagerSkin;
import astrotibs.villagenames.network.MessageVillageGuard;
import astrotibs.villagenames.network.MessageZombieVillagerProfession;
import astrotibs.villagenames.network.NetworkHelper;
import astrotibs.villagenames.proxy.CommonProxy;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import astrotibs.villagenames.village.MapGenVillageVN;
import astrotibs.villagenames.village.StructureCreationHandlers;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.biomestructures.DesertStructures;
import astrotibs.villagenames.village.biomestructures.PlainsStructures;
import astrotibs.villagenames.village.biomestructures.SavannaStructures;
import astrotibs.villagenames.village.biomestructures.SnowyStructures;
import astrotibs.villagenames.village.biomestructures.TaigaStructures;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.relauncher.Side;

/*
 * Testing sword:
 * give @p golden_sword 1 0 {display:{Name:"Un-Instantiator"}, ench:[{id:16,lvl:1000},{id:34,lvl:99}]}
 * Loot level 3: {id:21,lvl:3}
 */

@Mod(	
		modid = Reference.MOD_ID,
		name = Reference.MOD_NAME,
		version = Reference.VERSION,
		guiFactory = Reference.GUI_FACTORY
		)
public final class VillageNames
{
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
	public static CommonProxy PROXY;

	public static SimpleNetworkWrapper VNNetworkWrapper; //Added from Dragon Artifacts
	
	public static File configDirectory;
	
	@Instance(Reference.MOD_ID)
	public static VillageNames instance;

	public static String currentConfigFolder = "VillageNames4";
	public static String[] oldConfigFolders = new String[]{"VillageNames3", "VillageNames"};
	
    // instantiate achievements
	/*
	public static Achievement maxrep;
	public static Achievement minrep;
	public static Achievement ghosttown;
	public static Achievement archaeologist;
	public static Achievement laputa;
	*/
	
	// Version checking instance
	public static VersionChecker versionChecker = new VersionChecker();
	public static boolean haveWarnedVersionOutOfDate = false;
	public static boolean devVersionWarned = false;

	/*
	 * The number of structures you need to use the Codex on to trigger the achievement.
	 * If the player does not use any mods that add valid searchable structures,
	 * AND they're using the 1.7 version of Village Names,
	 * AND they're not using its optional Monument or Igloo generation,
	 * then there are seven structures they can identify, so they have to identify them all.
	 * The structures are:
	 * 
	 * Village
	 * Desert Pyramid
	 * Jungle Pyramid
	 * Swamp Hut
	 * Mineshaft
	 * Stronghold
	 * Nether Fortress
	 */
	public static int numberStructuresArchaeologist = 7;
	
	
	// PRE-INIT
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

		configDirectory = new File(event.getModConfigurationDirectory(), currentConfigFolder);
		ConfigInit.init(configDirectory);
		
		// Log a warning to the user if an old config folder is detected
		for (String oldConfigFolder : oldConfigFolders)
		{
			if (new File(event.getModConfigurationDirectory(), oldConfigFolder).exists())
			{
				LogHelper.warn(
						"ATTENTION! The old configuration folder " + oldConfigFolder + " will NOT BE USED in this version of "+Reference.MOD_NAME+"! "
								+ "A new " + currentConfigFolder + " folder has been created. Old config values HAVE NOT BEEN COPIED OVER.");
				LogHelper.warn("Remove the "+ oldConfigFolder + " folder (save a backup!) to prevent this message in the future.");
				break;
			}
		}
		
		
		
		// Moved down here to make sure config fires first!?
		ModItems.init();
		ModBlocksVN.init();
		
		// New village generator
		if (VillageGeneratorConfigHandler.newVillageGenerator)
		{
			// New village generator
			MapGenStructureIO.registerStructure(MapGenVillageVN.Start.class, "MapGenVillageVN");

			// Village Misc
	        MapGenStructureIO.registerStructureComponent(StructureVillageVN.PathVN.class, "VNPath"); // Path
	        MapGenStructureIO.registerStructureComponent(StructureVillageVN.DecorTorch.class, "VNDecTor"); // Decor Torch
	        
	        // Village Centers
	        MapGenStructureIO.registerStructureComponent(PlainsStructures.PlainsFountain01.class, "VNPlF01"); // Fountain
	        MapGenStructureIO.registerStructureComponent(PlainsStructures.PlainsMeetingPoint1.class, "VNPlMP1"); // Plains Well
	        MapGenStructureIO.registerStructureComponent(PlainsStructures.PlainsMeetingPoint2.class, "VNPlMP2"); // Plains Market
	        MapGenStructureIO.registerStructureComponent(PlainsStructures.PlainsMeetingPoint3.class, "VNPlMP3"); // Central Tree
	        MapGenStructureIO.registerStructureComponent(DesertStructures.DesertMeetingPoint1.class, "VNDeMP1"); // Fountain and Building
	        MapGenStructureIO.registerStructureComponent(DesertStructures.DesertMeetingPoint2.class, "VNDeMP2"); // Desert Well
	        MapGenStructureIO.registerStructureComponent(DesertStructures.DesertMeetingPoint3.class, "VNDeMP3"); // Desert Market
	        MapGenStructureIO.registerStructureComponent(TaigaStructures.TaigaMeetingPoint1.class, "VNTaMP1"); // Grass patch with two structures
	        MapGenStructureIO.registerStructureComponent(TaigaStructures.TaigaMeetingPoint2.class, "VNTaMP2"); // Taiga Well
	        MapGenStructureIO.registerStructureComponent(SavannaStructures.SavannaMeetingPoint1.class, "VNSaMP1"); // Savanna Market
	        MapGenStructureIO.registerStructureComponent(SavannaStructures.SavannaMeetingPoint2.class, "VNSaMP2"); // Savanna Fountain
	        MapGenStructureIO.registerStructureComponent(SavannaStructures.SavannaMeetingPoint3.class, "VNSaMP3"); // Savanna double well
	        MapGenStructureIO.registerStructureComponent(SavannaStructures.SavannaMeetingPoint4.class, "VNSaMP4"); // Savanna single well
	        MapGenStructureIO.registerStructureComponent(SnowyStructures.SnowyMeetingPoint1.class, "VNSnMP1"); // Snowy Ice Spire
	        MapGenStructureIO.registerStructureComponent(SnowyStructures.SnowyMeetingPoint2.class, "VNSnMP2"); // Frozen Fountain
	        MapGenStructureIO.registerStructureComponent(SnowyStructures.SnowyMeetingPoint3.class, "VNSnMP3"); // Snowy Pavilion
	        
	        // Village Structures
	        registerVillageComponentBuilding(PlainsStructures.PlainsAccessory1.class, "VNPlAcc1", new StructureCreationHandlers.PlainsAccessory1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsAnimalPen1.class, "VNPlAnP1", new StructureCreationHandlers.PlainsAnimalPen1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsAnimalPen2.class, "VNPlAnP2", new StructureCreationHandlers.PlainsAnimalPen2_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsAnimalPen3.class, "VNPlAnP3", new StructureCreationHandlers.PlainsAnimalPen3_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsArmorerHouse1.class, "VNPlArm1", new StructureCreationHandlers.PlainsArmorerHouse1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsBigHouse1.class, "VNPlBiH1", new StructureCreationHandlers.PlainsBigHouse1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsButcherShop1.class, "VNPlBut1", new StructureCreationHandlers.PlainsButcherShop1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsButcherShop2.class, "VNPlBut2", new StructureCreationHandlers.PlainsButcherShop2_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsCartographer1.class, "VNPlCar1", new StructureCreationHandlers.PlainsCartographer1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsFisherCottage1.class, "VNPlFis1", new StructureCreationHandlers.PlainsFisherCottage1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsFletcherHouse1.class, "VNPlFle1", new StructureCreationHandlers.PlainsFletcherHouse1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsLargeFarm1.class, "VNPlLFa1", new StructureCreationHandlers.PlainsLargeFarm1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsLibrary1.class, "VNPlLib1", new StructureCreationHandlers.PlainsLibrary1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsLibrary2.class, "VNPlLib2", new StructureCreationHandlers.PlainsLibrary2_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsMasonsHouse1.class, "VNPlMas1", new StructureCreationHandlers.PlainsMasonsHouse1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsMediumHouse1.class, "VNPlMeH1", new StructureCreationHandlers.PlainsMediumHouse1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsMediumHouse2.class, "VNPlMeH2", new StructureCreationHandlers.PlainsMediumHouse2_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsMeetingPoint4.class, "VNPlMeP4", new StructureCreationHandlers.PlainsMeetingPoint4_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsMeetingPoint5.class, "VNPlMeP5", new StructureCreationHandlers.PlainsMeetingPoint5_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsShepherdsHouse1.class, "VNPlShe1", new StructureCreationHandlers.PlainsShepherdsHouse1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallFarm1.class, "VNPlSFa1", new StructureCreationHandlers.PlainsSmallFarm1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallHouse1.class, "VNPlSmH1", new StructureCreationHandlers.PlainsSmallHouse1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallHouse2.class, "VNPlSmH2", new StructureCreationHandlers.PlainsSmallHouse2_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallHouse3.class, "VNPlSmH3", new StructureCreationHandlers.PlainsSmallHouse3_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallHouse4.class, "VNPlSmH4", new StructureCreationHandlers.PlainsSmallHouse4_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallHouse5.class, "VNPlSmH5", new StructureCreationHandlers.PlainsSmallHouse5_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallHouse6.class, "VNPlSmH6", new StructureCreationHandlers.PlainsSmallHouse6_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallHouse7.class, "VNPlSmH7", new StructureCreationHandlers.PlainsSmallHouse7_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsSmallHouse8.class, "VNPlSmH8", new StructureCreationHandlers.PlainsSmallHouse8_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsStable1.class, "VNPlSta1", new StructureCreationHandlers.PlainsStable1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsStable2.class, "VNPlSta2", new StructureCreationHandlers.PlainsStable2_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsTannery1.class, "VNPlTan2", new StructureCreationHandlers.PlainsTannery1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsTemple3.class, "VNPlTem3", new StructureCreationHandlers.PlainsTemple3_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsTemple4.class, "VNPlTem4", new StructureCreationHandlers.PlainsTemple4_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsToolSmith1.class, "VNPlTSm1", new StructureCreationHandlers.PlainsToolSmith1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsWeaponsmith1.class, "VNPlWSm1", new StructureCreationHandlers.PlainsWeaponsmith1_Handler());
	        registerVillageComponentBuilding(PlainsStructures.PlainsStreetDecor1.class, "VNPlStD1", new StructureCreationHandlers.PlainsStreetDecor1_Handler());
	        
	        registerVillageComponentBuilding(DesertStructures.DesertAnimalPen1.class, "VNDeAnP1", new StructureCreationHandlers.DesertAnimalPen1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertAnimalPen2.class, "VNDeAnP2", new StructureCreationHandlers.DesertAnimalPen2_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertArmorer1.class, "VNDeArm1", new StructureCreationHandlers.DesertArmorer1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertButcherShop1.class, "VNDeBut1", new StructureCreationHandlers.DesertButcherShop1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertCartographerHouse1.class, "VNDeCar1", new StructureCreationHandlers.DesertCartographerHouse1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertFarm1.class, "VNDeFar1", new StructureCreationHandlers.DesertFarm1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertFarm2.class, "VNDeFar2", new StructureCreationHandlers.DesertFarm2_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertFisher1.class, "VNDeFis1", new StructureCreationHandlers.DesertFisher1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertFletcherHouse1.class, "VNDeFle1", new StructureCreationHandlers.DesertFletcherHouse1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertLargeFarm1.class, "VNDeLFa1", new StructureCreationHandlers.DesertLargeFarm1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertLibrary1.class, "VNDeLib1", new StructureCreationHandlers.DesertLibrary1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertMason1.class, "VNDeMas1", new StructureCreationHandlers.DesertMason1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertMediumHouse1.class, "VNDeMeH1", new StructureCreationHandlers.DesertMediumHouse1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertMediumHouse2.class, "VNDeMeH2", new StructureCreationHandlers.DesertMediumHouse2_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertShepherdHouse1.class, "VNDeShe1", new StructureCreationHandlers.DesertShepherdHouse1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertSmallHouse1.class, "VNDeSmH1", new StructureCreationHandlers.DesertSmallHouse1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertSmallHouse2.class, "VNDeSmH2", new StructureCreationHandlers.DesertSmallHouse2_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertSmallHouse3.class, "VNDeSmH3", new StructureCreationHandlers.DesertSmallHouse3_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertSmallHouse4.class, "VNDeSmH4", new StructureCreationHandlers.DesertSmallHouse4_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertSmallHouse5.class, "VNDeSmH5", new StructureCreationHandlers.DesertSmallHouse5_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertSmallHouse6.class, "VNDeSmH6", new StructureCreationHandlers.DesertSmallHouse6_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertSmallHouse7.class, "VNDeSmH7", new StructureCreationHandlers.DesertSmallHouse7_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertSmallHouse8.class, "VNDeSmH8", new StructureCreationHandlers.DesertSmallHouse8_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertTannery1.class, "VNDeTan1", new StructureCreationHandlers.DesertTannery1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertTemple1.class, "VNDeTem1", new StructureCreationHandlers.DesertTemple1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertTemple2.class, "VNDeTem2", new StructureCreationHandlers.DesertTemple2_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertToolSmith1.class, "VNDeTSm1", new StructureCreationHandlers.DesertToolSmith1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertWeaponsmith1.class, "VNDeWSm1", new StructureCreationHandlers.DesertWeaponsmith1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertStreetDecor1.class, "VNDeStD1", new StructureCreationHandlers.SavannaStreetDecor1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertStreetSubstitute1.class, "VNDeStS1", new StructureCreationHandlers.DesertStreetSubstitute1_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertStreetSubstitute2.class, "VNDeStS2", new StructureCreationHandlers.DesertStreetSubstitute2_Handler());
	        registerVillageComponentBuilding(DesertStructures.DesertStreetSubstitute3.class, "VNDeStS3", new StructureCreationHandlers.DesertStreetSubstitute3_Handler());
	        
	        registerVillageComponentBuilding(TaigaStructures.TaigaAnimalPen1.class, "VNTaAnP1", new StructureCreationHandlers.TaigaAnimalPen1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaArmorer2.class, "VNTaArm2", new StructureCreationHandlers.TaigaArmorer2_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaArmorerHouse1.class, "VNTaArm1", new StructureCreationHandlers.TaigaArmorerHouse1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaButcherShop1.class, "VNTaBut1", new StructureCreationHandlers.TaigaButcherShop1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaCartographerHouse1.class, "VNTaCar1", new StructureCreationHandlers.TaigaCartographerHouse1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaFisherCottage1.class, "VNTaFis1", new StructureCreationHandlers.TaigaFisherCottage1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaFletcherHouse1.class, "VNTaFle1", new StructureCreationHandlers.TaigaFletcherHouse1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaLargeFarm1.class, "VNTaLFa1", new StructureCreationHandlers.TaigaLargeFarm1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaLargeFarm2.class, "VNTaLFa2", new StructureCreationHandlers.TaigaLargeFarm2_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaLibrary1.class, "VNTaLib1", new StructureCreationHandlers.TaigaLibrary1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaMasonsHouse1.class, "VNTaMas1", new StructureCreationHandlers.TaigaMasonsHouse1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaMediumHouse1.class, "VNTaMeH1", new StructureCreationHandlers.TaigaMediumHouse1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaMediumHouse2.class, "VNTaMeH2", new StructureCreationHandlers.TaigaMediumHouse2_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaMediumHouse3.class, "VNTaMeH3", new StructureCreationHandlers.TaigaMediumHouse3_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaMediumHouse4.class, "VNTaMeH4", new StructureCreationHandlers.TaigaMediumHouse4_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaShepherdsHouse1.class, "VNTaShe1", new StructureCreationHandlers.TaigaShepherdsHouse1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaSmallFarm1.class, "VNTaSFa1", new StructureCreationHandlers.TaigaSmallFarm1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaSmallHouse1.class, "VNTaSmH1", new StructureCreationHandlers.TaigaSmallHouse1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaSmallHouse2.class, "VNTaSmH2", new StructureCreationHandlers.TaigaSmallHouse2_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaSmallHouse3.class, "VNTaSmH3", new StructureCreationHandlers.TaigaSmallHouse3_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaSmallHouse4.class, "VNTaSmH4", new StructureCreationHandlers.TaigaSmallHouse4_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaSmallHouse5.class, "VNTaSmH5", new StructureCreationHandlers.TaigaSmallHouse5_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaTannery1.class, "VNTaTan1", new StructureCreationHandlers.TaigaTannery1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaTemple1.class, "VNTaTem1", new StructureCreationHandlers.TaigaTemple1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaToolSmith1.class, "VNTaTSm1", new StructureCreationHandlers.TaigaToolSmith1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaWeaponsmith1.class, "VNTaWSm1", new StructureCreationHandlers.TaigaWeaponsmith1_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaWeaponsmith2.class, "VNTaWSm2", new StructureCreationHandlers.TaigaWeaponsmith2_Handler());
	        registerVillageComponentBuilding(TaigaStructures.TaigaStreetDecor1.class, "VNTaStD1", new StructureCreationHandlers.TaigaStreetDecor1_Handler());
	        
	        registerVillageComponentBuilding(SavannaStructures.SavannaAnimalPen1.class, "VNSaAnP1", new StructureCreationHandlers.SavannaAnimalPen1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaAnimalPen2.class, "VNSaAnP2", new StructureCreationHandlers.SavannaAnimalPen2_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaAnimalPen3.class, "VNSaAnP3", new StructureCreationHandlers.SavannaAnimalPen3_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaArmorer1.class, "VNSaArm1", new StructureCreationHandlers.SavannaArmorer1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaButchersShop1.class, "VNSaBut1", new StructureCreationHandlers.SavannaButchersShop1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaButchersShop2.class, "VNSaBut2", new StructureCreationHandlers.SavannaButchersShop2_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaCartographer1.class, "VNSaCar1", new StructureCreationHandlers.SavannaCartographer1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaFisherCottage1.class, "VNSaFis1", new StructureCreationHandlers.SavannaFisherCottage1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaFletcherHouse1.class, "VNSaFle1", new StructureCreationHandlers.SavannaFletcherHouse1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaLargeFarm1.class, "VNSaLFa1", new StructureCreationHandlers.SavannaLargeFarm1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaLargeFarm2.class, "VNSaLFa2", new StructureCreationHandlers.SavannaLargeFarm2_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaLibrary1.class, "VNSaLib1", new StructureCreationHandlers.SavannaLibrary1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaMason1.class, "VNSaMas1", new StructureCreationHandlers.SavannaMason1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaMediumHouse1.class, "VNSaMeH1", new StructureCreationHandlers.SavannaMediumHouse1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaMediumHouse2.class, "VNSaMeH2", new StructureCreationHandlers.SavannaMediumHouse2_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaShepherd1.class, "VNSaShe1", new StructureCreationHandlers.SavannaShepherd1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallFarm.class, "VNSaSmFa", new StructureCreationHandlers.SavannaSmallFarm_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallHouse1.class, "VNSaSmH1", new StructureCreationHandlers.SavannaSmallHouse1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallHouse2.class, "VNSaSmH2", new StructureCreationHandlers.SavannaSmallHouse2_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallHouse3.class, "VNSaSmH3", new StructureCreationHandlers.SavannaSmallHouse3_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallHouse4.class, "VNSaSmH4", new StructureCreationHandlers.SavannaSmallHouse4_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallHouse5.class, "VNSaSmH5", new StructureCreationHandlers.SavannaSmallHouse5_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallHouse6.class, "VNSaSmH6", new StructureCreationHandlers.SavannaSmallHouse6_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallHouse7.class, "VNSaSmH7", new StructureCreationHandlers.SavannaSmallHouse7_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaSmallHouse8.class, "VNSaSmH8", new StructureCreationHandlers.SavannaSmallHouse8_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaTannery1.class, "VNSaTsn1", new StructureCreationHandlers.SavannaTannery1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaTemple1.class, "VNSaTem1", new StructureCreationHandlers.SavannaTemple1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaTemple2.class, "VNSaTem2", new StructureCreationHandlers.SavannaTemple2_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaToolSmith1.class, "VNSaTSm1", new StructureCreationHandlers.SavannaToolSmith1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaWeaponsmith1.class, "VNSaWSm1", new StructureCreationHandlers.SavannaWeaponsmith1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaWeaponsmith2.class, "VNSaWSm2", new StructureCreationHandlers.SavannaWeaponsmith2_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaStreetDecor1.class, "VNSaStD1", new StructureCreationHandlers.SavannaStreetDecor1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaStreetSubstitute1.class, "VNSaStS1", new StructureCreationHandlers.SavannaStreetSubstitute1_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaStreetSubstitute2.class, "VNSaStS2", new StructureCreationHandlers.SavannaStreetSubstitute2_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaStreetSubstitute3.class, "VNSaStS3", new StructureCreationHandlers.SavannaStreetSubstitute3_Handler());
	        registerVillageComponentBuilding(SavannaStructures.SavannaStreetSubstitute4.class, "VNSaStS4", new StructureCreationHandlers.SavannaStreetSubstitute4_Handler());
	        
	        registerVillageComponentBuilding(SnowyStructures.SnowyAnimalPen1.class, "VNSnAnP1", new StructureCreationHandlers.SnowyAnimalPen1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyAnimalPen2.class, "VNSnAnP2", new StructureCreationHandlers.SnowyAnimalPen2_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyArmorerHouse1.class, "VNSnArH1", new StructureCreationHandlers.SnowyArmorerHouse1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyArmorerHouse2.class, "VNSnArH2", new StructureCreationHandlers.SnowyArmorerHouse2_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyButchersShop1.class, "VNSnBut1", new StructureCreationHandlers.SnowyButchersShop1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyButchersShop2.class, "VNSnBut2", new StructureCreationHandlers.SnowyButchersShop2_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyCartographerHouse1.class, "VNSnCar1", new StructureCreationHandlers.SnowyCartographerHouse1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyFarm1.class, "VNSnFar1", new StructureCreationHandlers.SnowyFarm1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyFarm2.class, "VNSnFar2", new StructureCreationHandlers.SnowyFarm2_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyFisherCottage.class, "VNSnFisC", new StructureCreationHandlers.SnowyFisherCottage_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyFletcherHouse1.class, "VNSnFle1", new StructureCreationHandlers.SnowyFletcherHouse1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyLibrary1.class, "VNSnLib1", new StructureCreationHandlers.SnowyLibrary1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyMasonsHouse1.class, "VNSnMas1", new StructureCreationHandlers.SnowyMasonsHouse1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyMasonsHouse2.class, "VNSnMas2", new StructureCreationHandlers.SnowyMasonsHouse2_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyMediumHouse1.class, "VNSnMeH1", new StructureCreationHandlers.SnowyMediumHouse1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyMediumHouse2.class, "VNSnMeH2", new StructureCreationHandlers.SnowyMediumHouse2_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyMediumHouse3.class, "VNSnMeH3", new StructureCreationHandlers.SnowyMediumHouse3_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyShepherdsHouse1.class, "VNSnShe1", new StructureCreationHandlers.SnowyShepherdsHouse1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowySmallHouse1.class, "VNSnSmH1", new StructureCreationHandlers.SnowySmallHouse1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowySmallHouse2.class, "VNSnSmH2", new StructureCreationHandlers.SnowySmallHouse2_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowySmallHouse3.class, "VNSnSmH3", new StructureCreationHandlers.SnowySmallHouse3_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowySmallHouse4.class, "VNSnSmH4", new StructureCreationHandlers.SnowySmallHouse4_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowySmallHouse5.class, "VNSnSmH5", new StructureCreationHandlers.SnowySmallHouse5_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowySmallHouse6.class, "VNSnSmH6", new StructureCreationHandlers.SnowySmallHouse6_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowySmallHouse7.class, "VNSnSmH7", new StructureCreationHandlers.SnowySmallHouse7_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowySmallHouse8.class, "VNSnSmH8", new StructureCreationHandlers.SnowySmallHouse8_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyTannery1.class, "VNSnTan1", new StructureCreationHandlers.SnowyTannery1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyTemple1.class, "VNSnTem1", new StructureCreationHandlers.SnowyTemple1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyToolSmith1.class, "VNSnTSm1", new StructureCreationHandlers.SnowyToolSmith1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyWeaponSmith1.class, "VNSnWSm1", new StructureCreationHandlers.SnowyWeaponSmith1_Handler());
	        registerVillageComponentBuilding(SnowyStructures.SnowyStreetDecor1.class, "VNSnStD1", new StructureCreationHandlers.SnowyStreetDecor1_Handler());
	        
	        	        
	        // Listener that interrupts old village generation with the new one
			MinecraftForge.TERRAIN_GEN_BUS.register(new MapGenVillageVN());

			// Chest hooks
			ChestLootHandler.modernVillageChests();
			
			LogHelper.info("Registered new Village generator");
		}
		
		// Listener that will fire on world loading, to generate the new nbt files from your old ones.
		MinecraftForge.EVENT_BUS.register(new NBTUpdater());
		
		// Event Handlers
        MinecraftForge.EVENT_BUS.register(new ServerTrackerStarter());
        MinecraftForge.EVENT_BUS.register(new EntityMonitorHandler());
        MinecraftForge.EVENT_BUS.register(new ServerCleanExpired());
        MinecraftForge.EVENT_BUS.register(new ReputationHandler());
        
        // Version check monitor
        if (GeneralConfig.versionChecker) {MinecraftForge.EVENT_BUS.register(versionChecker);}
        if ((Reference.VERSION).contains("DEV")) {MinecraftForge.EVENT_BUS.register(new DevVersionWarning());}

		// Establish the channel
        VNNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_CHANNEL);
        
        // Register different messages here
        int messageID = 0;
		
        VNNetworkWrapper.registerMessage(NetworkHelper.ZombieVillagerProfessionHandler.class, MessageZombieVillagerProfession.class, messageID++, Side.CLIENT);
        VNNetworkWrapper.registerMessage(NetworkHelper.VillageGuardHandler.class, MessageVillageGuard.class, messageID++, Side.CLIENT);
        VNNetworkWrapper.registerMessage(NetworkHelper.ModernVillagerSkinHandler.class, MessageModernVillagerSkin.class, messageID++, Side.CLIENT);
		
		/**
		 * The following overrides the mcmod.info file!
		 * Adapted from Jabelar's Magic Beans:
		 * https://github.com/jabelar/MagicBeans-1.7.10/blob/e48456397f9c6c27efce18e6b9ad34407e6bc7c7/src/main/java/com/blogspot/jabelarminecraft/magicbeans/MagicBeans.java
		 */
        event.getModMetadata().autogenerated = false ; // stops it from complaining about missing mcmod.info
        
        event.getModMetadata().name = 			// name 
        		TextFormatting.GOLD + 
        		Reference.MOD_NAME;
        
        event.getModMetadata().version = 		// version 
        		TextFormatting.YELLOW+
        		Reference.VERSION;
        
        event.getModMetadata().credits = 		// credits 
        		TextFormatting.AQUA +
        		"Thanks to Pahimar, MineMaarten, and Jabelar for their tutorials; to Darian Stephens for playtesting; and to whrrgarbl for her help and support.";
        
        event.getModMetadata().authorList.clear();
        event.getModMetadata().authorList.add(  // authorList - added as a list
        		TextFormatting.BLUE +
        		"AstroTibs");
        
       event.getModMetadata().url = 			// url
    		   //TextFormatting.GRAY +
        		Reference.URL;
        
       event.getModMetadata().description = 	// description
	       		TextFormatting.GREEN +
	       		"Generates random names for villages, villagers, and other structures and entities.";
       
       event.getModMetadata().logoFile = "assets/villagenames/vn_banner.png";
       
       
       // --- New Villager Profession/Career stuff --- //
       
       if (GeneralConfig.treasureTrades) {
    	   
    	   // summon minecraft:villager ~ ~ ~ {Profession:1}
    	   
    	   VillagerRegistry.VillagerProfession professionLibrarian = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:librarian"));
    	   
    	   VillagerRegistry.VillagerCareer careerLibrarian = professionLibrarian.getCareer(0);    // 1.11 changed this to 0: it's the index, not the career value
    	   VillagerRegistry.VillagerCareer careerCartographer = professionLibrarian.getCareer(1); // 1.11 changed this to 1: it's the index, not the career value
    	   
    	   // Re-worked in v3.1trades
    	   // Add new VN Treasure trades -- limit per librarian/cartographer
    	   if (GeneralConfig.treasureTrades && !GeneralConfig.modernVillagerTrades) {
    		   for (int i=0; i<3; i++) {
    			   careerLibrarian.addTrade(i+7, (new VillagerTradeHandler()).new LibrarianVN() );
    			   careerCartographer.addTrade(i+4, (new VillagerTradeHandler()).new CartographerVN() );
        	   }
    	   }
       }
       
       // Added in v3.1trades
       if (GeneralConfig.modernVillagerTrades) {
    	   
    	   // summon minecraft:villager ~ ~ ~ {Profession:3}
    	   
    	   VillagerRegistry.VillagerProfession profBlacksmith = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:smith"));
    	   VillagerRegistry.VillagerCareer carMason = new VillagerCareer(profBlacksmith, "mason");
    	   
    	   // Add new VN Mason trades
		   carMason.addTrade(1, (new VillagerTradeHandler()).new MasonNovice() );
    	   carMason.addTrade(2, (new VillagerTradeHandler()).new MasonApprentice() );
    	   carMason.addTrade(3, (new VillagerTradeHandler()).new MasonJourneyman() );
    	   carMason.addTrade(4, (new VillagerTradeHandler()).new MasonExpert() );
    	   carMason.addTrade(5, (new VillagerTradeHandler()).new MasonMaster() );
       }
	}
        
    
	
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);//For the populating event
		
		//If this code runs on the server, you will get a crash.
		if (event.getSide()== Side.CLIENT) {
			InventoryRender.init();
		}
		
		// Register the Advancements listeners
		ModTriggers.registerTriggers();
		
		// To prevent torches from melting snow blocks
		Blocks.SNOW.setTickRandomly(false);
		
		PROXY.init(event);
		
		// key handling
		
		// Re-added in v3.1
        if (event.getSide() == Side.CLIENT) {
            //Minecraft.getMinecraft().getRenderManager().entityRenderMap.remove(EntityZombie.class);
            //Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityZombie.class, new VNRenderZombie(Minecraft.getMinecraft().getRenderManager()));
    		
        	// Changed in v3.1
            if (GeneralConfig.modernVillagerSkins)
            {
            	//RenderingRegistry.registerEntityRenderingHandler(EntityVillager.class, new RenderVillagerModern()); // Modern modular villagers
            	Minecraft.getMinecraft().getRenderManager().entityRenderMap.remove(EntityVillager.class);
                Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityVillager.class, new RenderVillagerModern(Minecraft.getMinecraft().getRenderManager()));
        		
            	//RenderingRegistry.registerEntityRenderingHandler(EntityZombie.class, new RenderZombieVillagerModern());  // Modern modular zombie villagers
                Minecraft.getMinecraft().getRenderManager().entityRenderMap.remove(EntityZombieVillager.class);
                Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityZombieVillager.class,
                		//new RenderZombieVillagerModern(Minecraft.getMinecraft().getRenderManager())
                		new RenderZombieVillagerModern(Minecraft.getMinecraft().getRenderManager())
                		);
            }
        }
        
        // Other mod stuff
        if (Loader.isModLoaded(Reference.ANTIQUE_ATLAS_MODID))
        {
        	MinecraftForge.EVENT_BUS.register(new VillageWatcherAA()); // Antique Atlas map listener
        }
	}
	
	// POST-INIT
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
	    // register server commands
		event.registerServerCommand(new CommandName());
		event.registerServerCommand(new CommandBanner()); // Added in v3.1.1
	}
	
	/**
	 * For streamlining structure registry of component buildings (not village centers)
	 * @param structureClass - The class of the building
	 * @param structureShorthand - Abbreviated name for the structure, loaded into nbt structure files
	 * @param handlerClass - The creation handler class in StructureCreationHandlers
	 */
	public static void registerVillageComponentBuilding(Class structureClass, String structureShorthand, IVillageCreationHandler handlerClass)
	{
        VillagerRegistry.instance().registerVillageCreationHandler(handlerClass);
        MapGenStructureIO.registerStructureComponent(structureClass, structureShorthand);
	}
}
