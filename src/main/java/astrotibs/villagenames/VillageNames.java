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
import astrotibs.villagenames.handler.DevVersionWarning;
import astrotibs.villagenames.handler.EntityMonitorHandler;
import astrotibs.villagenames.handler.ReputationHandler;
import astrotibs.villagenames.handler.ServerCleanExpired;
import astrotibs.villagenames.handler.ServerTrackerStarter;
import astrotibs.villagenames.handler.VersionChecker;
import astrotibs.villagenames.handler.VillagerTradeHandler;
import astrotibs.villagenames.init.InventoryRender;
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
	        
	        // Listener that interrupts old village generation with the new one
			MinecraftForge.TERRAIN_GEN_BUS.register(new MapGenVillageVN());
			
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
}
