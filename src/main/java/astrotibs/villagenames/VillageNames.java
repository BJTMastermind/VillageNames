package astrotibs.villagenames;

import java.io.File;

import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.client.renderer.entity.RenderVillagerModern;
import astrotibs.villagenames.client.renderer.entity.RenderZombieVillagerModern;
import astrotibs.villagenames.command.CommandName;
import astrotibs.villagenames.config.ConfigInit;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.handler.DevVersionWarning;
import astrotibs.villagenames.handler.EntityMonitorHandler;
import astrotibs.villagenames.handler.ReputationHandler;
import astrotibs.villagenames.handler.ServerCleanExpired;
import astrotibs.villagenames.handler.ServerTrackerStarter;
import astrotibs.villagenames.handler.VersionChecker;
import astrotibs.villagenames.handler.VillagerTradeHandler;
import astrotibs.villagenames.init.InventoryRender;
import astrotibs.villagenames.init.Recipes;
import astrotibs.villagenames.integration.ModChecker;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.nbt.NBTUpdater;
import astrotibs.villagenames.network.MessageModernVillagerSkin;
import astrotibs.villagenames.network.MessageVillageGuard;
import astrotibs.villagenames.network.MessageZombieVillagerProfession;
import astrotibs.villagenames.network.NetworkHelper;
import astrotibs.villagenames.proxy.CommonProxy;
import astrotibs.villagenames.sounds.EventSounds;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
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
public final class VillageNames {
	
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
	public static CommonProxy PROXY;

	public static SimpleNetworkWrapper VNNetworkWrapper; //Added from Dragon Artifacts
	
	//public static final Block FLAG_ID = Blocks.PLANKS;
	
	public static File configDirectory;
	
	@Instance(Reference.MOD_ID)
	public static VillageNames instance;
	
	public static String currentConfigFolder = "VillageNames3";
	public static String oldConfigFolder = "VillageNames";
	public static String obsoleteConfigFolder = "VillageNamesOLD";
	
    // instantiate achievements
	public static Achievement maxrep;
	public static Achievement minrep;
	public static Achievement ghosttown;
	public static Achievement archaeologist;
	public static Achievement laputa;

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
	public void preInit(FMLPreInitializationEvent event) {

		ModChecker.printDetectedMods();
		
		configDirectory = new File(event.getModConfigurationDirectory(), currentConfigFolder);
		ConfigInit.init(configDirectory);
		
		
		// ------------------------------------------- //		
		// Relocate old config files and make new ones //
		// ------------------------------------------- //
		
		// Log a warning to the user if the old config folder is detected
		File oldConfigDirectory = new File(event.getModConfigurationDirectory(), oldConfigFolder);
		if ( new File(event.getModConfigurationDirectory(), oldConfigFolder).exists() ) {
			LogHelper.warn(
					"ATTENTION! The old configuration folder " + oldConfigFolder + " exists. It will NOT BE USED in this version of Village Names! A new " 
							+ currentConfigFolder + " folder has been created.");
			if ( new File(event.getModConfigurationDirectory(), obsoleteConfigFolder).exists() ) {
				// A folder with the name I'm trying to rename to already exists
				LogHelper.error("For some reason, you have both a " + oldConfigFolder + " and " + obsoleteConfigFolder + " folder. Note that NEITHER FOLDER IS USED!");
			}
			else {
				// No folder with the renaming name exists, so (try to) rename the old folder
				try {
					oldConfigDirectory.renameTo( new File(event.getModConfigurationDirectory(), obsoleteConfigFolder) );
					LogHelper.warn("The previous config folder has been renamed to "+obsoleteConfigFolder+". It will NOT BE USED anymore.");
				}
				catch (Exception e) {
					LogHelper.error("Failed to rename " + oldConfigFolder + " folder to "+obsoleteConfigFolder);
				}
			}
		}
		else if ( new File(event.getModConfigurationDirectory(), obsoleteConfigFolder).exists() ) {
			LogHelper.info("Old config files are located in config/"+obsoleteConfigFolder+". They will NOT BE USED anymore.");
		}
		
		
		
		// Moved down here to make sure config fires first!?
		ModItems.init();
		ModBlocksVN.init();

		// --- Sounds --- //
		MinecraftForge.EVENT_BUS.register(new EventSounds());
		
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
		
        // Register sound events
        EventSounds.registerSounds();
        
        PROXY.preInit(event); //TODO - this was removed until v3.1 - why?


		// Reworked in v3.1: new network channel stuff
		
		// Establish the channel
        VNNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_CHANNEL);
        
        // Register different messages here
        
        int messageID = 0;
		
        VNNetworkWrapper.registerMessage(NetworkHelper.ZombieVillagerProfessionHandler.class, MessageZombieVillagerProfession.class, messageID++, Side.CLIENT);
        VNNetworkWrapper.registerMessage(NetworkHelper.VillageGuardHandler.class, MessageVillageGuard.class, messageID++, Side.CLIENT);
        VNNetworkWrapper.registerMessage(NetworkHelper.ModernVillagerSkinHandler.class, MessageModernVillagerSkin.class, messageID++, Side.CLIENT);
		
        
        
		// Worldgen stuff
		// set up key bindings

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
    		   //TextFormatting.GRAY +   // Formatting removed because URL is too long
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
	public void load(FMLInitializationEvent event) {
		
		// register crafting recipes
		Recipes.init();

		//If this code runs on the server, you will get a crash.
		if (event.getSide()== Side.CLIENT) {
			InventoryRender.init();
		}
		
		// package registering
		
		// general event handlers
		
		// Renderers
		//EventRegister.register();
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
	public void postInit(FMLPostInitializationEvent event) {
		
		// cover your ass here
		// e.g. get list of all blocks added into game from other mods
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
	    // register server commands
		event.registerServerCommand(new CommandName());
	}
	
	/*
	// Way to convert from color meta int into string formatting (for e.g. signs)
	public static String mapColorMetaToStringFormat(int colorMeta) {
		HashMap<Integer, String> signColorToFormat = new HashMap<Integer, String>();//new HashMap();
		// This hashmap translates the town's name color on the sign to a color meta value.
		// This meta should be universal through e.g. wool, clay, etc
		signColorToFormat.put(0, "\u00a7f"); //white
		signColorToFormat.put(1, "\u00a76"); //gold
		signColorToFormat.put(2, "\u00a7d"); //light_purple
		signColorToFormat.put(3, "\u00a79"); //blue
		signColorToFormat.put(4, "\u00a7e"); //yellow
		signColorToFormat.put(5, "\u00a7a"); //green
		signColorToFormat.put(6, "\u00a7c"); //red
		signColorToFormat.put(7, "\u00a78"); //dark_gray
		signColorToFormat.put(8, "\u00a77"); //gray
		//signColorToFormat.put(9, "\u00a7b"); //aqua
		signColorToFormat.put(9, "\u00a73"); //dark_aqua
		signColorToFormat.put(10, "\u00a75"); //dark_purple
		signColorToFormat.put(11, "\u00a71"); //dark_blue
		signColorToFormat.put(12, "\u00a70"); //black
		signColorToFormat.put(13, "\u00a72"); //dark_green
		signColorToFormat.put(14, "\u00a74"); //dark_red
		signColorToFormat.put(15, "\u00a70"); //black
		
		// Return a "town color" string
		return signColorToFormat.get(colorMeta);
	}
	*/
}
