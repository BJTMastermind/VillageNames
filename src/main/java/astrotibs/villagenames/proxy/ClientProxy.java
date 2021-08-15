package astrotibs.villagenames.proxy;

import astrotibs.villagenames.integration.antiqueatlas.VillageWatcherAA;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void init(FMLInitializationEvent e)
	{
		super.init(e);
		
		if (Loader.isModLoaded(Reference.ANTIQUE_ATLAS_MODID))
		{ 
			VillageWatcherAA.registerTextures();
		}
	}
	
	@Override
    public void registerItemSided(Item item) {
		{
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + item.delegate.name().getResourcePath(), "inventory"));
        }
	}
	
    @Override
    public void registerItemVariantModel(Item item, String name, int metadata)
    {
        ModelBakery.registerItemVariants(item, new ResourceLocation("villagenames:" + name));
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Reference.MOD_ID + ":" + name, "inventory"));
    }
	
	
}
