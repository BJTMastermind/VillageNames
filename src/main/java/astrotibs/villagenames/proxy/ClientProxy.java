package astrotibs.villagenames.proxy;

import astrotibs.villagenames.block.color.BlockRenderRegister;
import astrotibs.villagenames.config.GeneralConfig;
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
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

}
