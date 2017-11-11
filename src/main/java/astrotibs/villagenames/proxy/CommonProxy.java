package astrotibs.villagenames.proxy;

import astrotibs.villagenames.handler.ItemEventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		//FMLCommonHandler.instance().bus().register(VillageNames.Modevents);
		//MinecraftForge.EVENT_BUS.register(VillageNames.Modevents);
		//ModRegistry.registerItems();
    }
	
	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
	}
	
	public void postInit(FMLPostInitializationEvent e)  {
		
	}
	
}
