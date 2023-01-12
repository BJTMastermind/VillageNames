package astrotibs.villagenames.proxy;

import astrotibs.villagenames.VillageNames;
import astrotibs.villagenames.capabilities.CapabilityAttach;
import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinStorage;
import astrotibs.villagenames.config.ConfigReloader;
import astrotibs.villagenames.handler.ChestLootHandler;
import astrotibs.villagenames.handler.EntityInteractHandler;
import astrotibs.villagenames.handler.SpawnNamingHandler;
import astrotibs.villagenames.handler.WellDecorateEvent;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.JsonSerializableSet;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		
    }
	
	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new EntityInteractHandler());
		MinecraftForge.EVENT_BUS.register(new WellDecorateEvent());
		MinecraftForge.EVENT_BUS.register(new ConfigReloader());
		MinecraftForge.EVENT_BUS.register(new SpawnNamingHandler());
		MinecraftForge.EVENT_BUS.register(new CapabilityAttach());
		MinecraftForge.EVENT_BUS.register(new ChestLootHandler());
		
		// Achievements
		registerAchievements();
		
		registerCapabilities();
	}
	
	public void postInit(FMLPostInitializationEvent e) {}
	
	protected void registerAchievements() {
		
		// Achievement parameters: CommandRegisterName  LangReference  pageX  pageY  Icon  RequiredAchievement
		
		/*
		 * Name explanation from https://ghibli.fandom.com/wiki/Laputa:_Castle_in_the_Sky
		 * 
		 * There are three decipher the morse code in the film, which was never decoded fully. In the Japanese show Tsukai! Akashiya TV
		 * a former soldier reveals its hidden meaning. The first code can be heard in the first chapter of the film. Muska take a call,
		 * before he got knocked out by Sheeta hitting with an empty bottle.
		 * 
		 * In the past, fans have dismissed this message as jibberish, recording it as a repetition of a series of dots and dashes […_ …_ …_],
		 * which translates to nothing more than V V V. However, Sakai and his soldier mates, who have experience in deciphering codes with
		 * no definite beginning and end, discovered that the message contained the code [.._. .. _.. . ._.. .. _ _._ _ ], which spells out the word fidelity.
		 */
		
    	VillageNames.laputa = new Achievement(Reference.MOD_ID.toLowerCase()+".achievement.laputa", "laputa",
    			1, -2, Blocks.RED_FLOWER, (Achievement)null).initIndependentStat().registerStat();//.setSpecial();
		
		VillageNames.maxrep = new Achievement(Reference.MOD_ID.toLowerCase()+".achievement.maxrep", "maxrep",
    			3, 0, Items.EMERALD, (Achievement)null).initIndependentStat().registerStat();
    	
    	VillageNames.archaeologist = new Achievement(Reference.MOD_ID.toLowerCase()+".achievement.archaeologist", "archaeologist",
    			-1, 0, ModItems.CODEX, (Achievement)null).setSerializableClazz(JsonSerializableSet.class).registerStat();
    	
    	VillageNames.ghosttown = new Achievement(Reference.MOD_ID.toLowerCase()+".achievement.ghosttown", "ghosttown",
    			0, 2, ModItems.VILLAGE_BOOK, (Achievement)null).initIndependentStat().registerStat();
    	
    	VillageNames.minrep = new Achievement(Reference.MOD_ID.toLowerCase()+".achievement.minrep", "minrep",
    			2, 2, new ItemStack(Items.SKULL, 1, 1), (Achievement)null).initIndependentStat().registerStat();
    	
    	
    	// Need to register the stats so that the achievements will be saved
    	//ghosttown.registerStat();
    	
    	// My Little Achievement Page
    	AchievementPage.registerAchievementPage(new AchievementPage(Reference.MOD_NAME, new Achievement[] {
    			VillageNames.maxrep,
    			VillageNames.archaeologist,
    			VillageNames.minrep,
    			VillageNames.ghosttown,
    			VillageNames.laputa
    			}));
		
	}
	
	protected void registerCapabilities()
	{
		CapabilityManager.INSTANCE.register(IModularSkin.class, new ModularSkinStorage(), ModularSkin.class);
	}
	
}
