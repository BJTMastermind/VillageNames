package astrotibs.villagenames.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;


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
}


/*
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		
    }
	
	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
	}
	
	public void postInit(FMLPostInitializationEvent e)  {
		
	}
	
}
*/