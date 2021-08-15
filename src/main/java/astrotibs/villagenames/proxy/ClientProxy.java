package astrotibs.villagenames.proxy;

import astrotibs.villagenames.block.color.BlockRenderRegister;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.antiqueatlas.VillageWatcherAA;
import astrotibs.villagenames.utility.Reference;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
}
