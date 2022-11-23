package astrotibs.villagenames.proxy;

import astrotibs.villagenames.capabilities.CapabilityAttach;
import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinStorage;
import astrotibs.villagenames.config.ConfigReloader;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.handler.ChestLootHandler;
import astrotibs.villagenames.handler.EntityInteractHandler;
import astrotibs.villagenames.handler.SpawnNamingHandler;
import astrotibs.villagenames.handler.WellDecorateEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;


public class CommonProxy
{
    public void registerRenderers() {}
    //public void registerColouring() {}
    public void registerItemVariantModel(Item item, String name, int metadata) {}
    public void registerBlockSided(Block block) {}
    public void registerItemSided(Item item) {}
    //public void registerFluidBlockRendering(Block block, String name) {}
    //public void spawnParticle(BOPParticleTypes type, double x, double y, double z, Object... info) {}
    //public void replaceBOPBucketTexture() {}
    
    

	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register( new EntityInteractHandler() );
		MinecraftForge.EVENT_BUS.register( new WellDecorateEvent() );
		MinecraftForge.EVENT_BUS.register( new ConfigReloader() );
		MinecraftForge.EVENT_BUS.register( new SpawnNamingHandler() );
		MinecraftForge.EVENT_BUS.register( new CapabilityAttach() );
		if (GeneralConfig.codexChestLoot) MinecraftForge.EVENT_BUS.register(new ChestLootHandler());
		
		// Capabilities
		registerCapabilities();
	}

	protected void registerCapabilities()
	{
		CapabilityManager.INSTANCE.register(IModularSkin.class, new ModularSkinStorage(), ModularSkin::new);
	}
	
}
