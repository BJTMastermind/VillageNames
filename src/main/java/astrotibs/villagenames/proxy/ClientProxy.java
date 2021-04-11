package astrotibs.villagenames.proxy;

import astrotibs.villagenames.VillageNames;
import astrotibs.villagenames.block.color.BlockRenderRegister;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.antiqueatlas.VillageWatcherAA;
import astrotibs.villagenames.utility.Reference;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		if (GeneralConfig.addConcrete) {
			BlockRenderRegister.preInit();
		}
	}
	
	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		if (GeneralConfig.addConcrete) {
			BlockRenderRegister.registerBlockRenderer();
		}
		
		if (Loader.isModLoaded(Reference.ANTIQUE_ATLAS_MODID))
		{ 
			VillageWatcherAA.registerTextures();
		}
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		Thread versionCheckThread = new Thread(VillageNames.versionChecker, "Version Check");
		versionCheckThread.start();
	}
	
	/*
	public static void playEndPortalSound() {
		//Minecraft.getMinecraft().getSoundHandler().playSound(new EndPortalActivateSound() );
		Minecraft.getMinecraft().getSoundHandler().playSound(
				new PositionedSoundRecord(
						new ResourceLocation("VillageNames:block.end_portal.endportal"),
						SoundCategory.HOSTILE, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F)
				);
	}
	*/
}
