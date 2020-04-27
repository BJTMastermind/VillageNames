package astrotibs.villagenames.client.renderer.entity;

import java.util.List;

import com.google.common.collect.Lists;

import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.client.model.ModelZombieVillagerModern;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Added in v3.1

@SideOnly(Side.CLIENT)
public class RenderZombieVillagerModern extends RenderBiped<EntityZombie>
{
	// Base skin texture
	private static final ResourceLocation zombieVillagerBaseSkin = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/zombie_villager.png");
	
	// Biome-based types
	private static final ResourceLocation zombieVillagerTypeDesert  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/desert.png");
	private static final ResourceLocation zombieVillagerTypeJungle  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/jungle.png");
	private static final ResourceLocation zombieVillagerTypePlains  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/plains.png");
	private static final ResourceLocation zombieVillagerTypeSavanna = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/savanna.png");
	private static final ResourceLocation zombieVillagerTypeSnow    = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/snow.png");
	private static final ResourceLocation zombieVillagerTypeSwamp   = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/swamp.png");
	private static final ResourceLocation zombieVillagerTypeTaiga   = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/taiga.png");
	// Custom biome types
	private static final ResourceLocation zombieVillagerTypeForest   = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/forest.png");
	private static final ResourceLocation zombieVillagerTypeAquatic  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/aquatic.png");
	private static final ResourceLocation zombieVillagerTypeHighland = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/highland.png");
	private static final ResourceLocation zombieVillagerTypeMushroom = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/mushroom.png");
	private static final ResourceLocation zombieVillagerTypeMagical  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/magical.png");
	private static final ResourceLocation zombieVillagerTypeNether   = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/nether.png");
	private static final ResourceLocation zombieVillagerTypeEnd      = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/type/end.png");
	
	// Profession-based layer
	private static final ResourceLocation zombieVillagerProfessionArmorer = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/armorer.png");
	private static final ResourceLocation zombieVillagerProfessionButcher = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/butcher.png");
	private static final ResourceLocation zombieVillagerProfessionCartographer = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/cartographer.png");
	private static final ResourceLocation zombieVillagerProfessionCleric = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/cleric.png");
	private static final ResourceLocation zombieVillagerProfessionFarmer = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/farmer.png");
	private static final ResourceLocation zombieVillagerProfessionFisherman = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/fisherman.png");
	private static final ResourceLocation zombieVillagerProfessionFletcher = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/fletcher.png");
	private static final ResourceLocation zombieVillagerProfessionLeatherworker = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/leatherworker.png");
	private static final ResourceLocation zombieVillagerProfessionLibrarian = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/librarian.png");
	private static final ResourceLocation zombieVillagerProfessionMason = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/mason.png");
	private static final ResourceLocation zombieVillagerProfessionNitwit = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/nitwit.png");
	private static final ResourceLocation zombieVillagerProfessionShepherd = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/shepherd.png");
	private static final ResourceLocation zombieVillagerProfessionToolsmith = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/toolsmith.png");
	private static final ResourceLocation zombieVillagerProfessionWeaponsmith = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession/weaponsmith.png");
	
	// Profession level purses
	private static final ResourceLocation zombieVillagerProfessionLevelStone = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession_level/stone.png");
	private static final ResourceLocation zombieVillagerProfessionLevelIron = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession_level/iron.png");
	private static final ResourceLocation zombieVillagerProfessionLevelGold = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession_level/gold.png");
	private static final ResourceLocation zombieVillagerProfessionLevelEmerald = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession_level/emerald.png");
	private static final ResourceLocation zombieVillagerProfessionLevelDiamond = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/zombie_villager/profession_level/diamond.png");
	
	// Vanilla textures
    private static final ResourceLocation zombiePigmanTextures = new ResourceLocation("textures/entity/zombie_pigman.png");
    private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
    //private static final ResourceLocation zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
    
    // Re-added in v3.2 to allow for profession-based vanilla zombie textures
    private static final ResourceLocation zombieVillagerTextures = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");
    private static final ResourceLocation zombieVillagerFarmerLocation = new ResourceLocation("textures/entity/zombie_villager/zombie_farmer.png");
    private static final ResourceLocation zombieVillagerLibrarianLoc = new ResourceLocation("textures/entity/zombie_villager/zombie_librarian.png");
    private static final ResourceLocation zombieVillagerPriestLocation = new ResourceLocation("textures/entity/zombie_villager/zombie_priest.png");
    private static final ResourceLocation zombieVillagerSmithLocation = new ResourceLocation("textures/entity/zombie_villager/zombie_smith.png");
    private static final ResourceLocation zombieVillagerButcherLocation = new ResourceLocation("textures/entity/zombie_villager/zombie_butcher.png");
    
    
    private final ModelBiped field_82434_o;
    private final ModelZombieVillagerModern zombieVillagerModel;
    private final List<LayerRenderer<EntityZombie>> field_177121_n;
    private final List<LayerRenderer<EntityZombie>> field_177122_o;

    public RenderZombieVillagerModern(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelZombie(), 0.5F, 1.0F);
        
        // New render layers for biome type and profession
		this.addLayer(new LayerZombieVillagerBiomeType(this));
		this.addLayer(new LayerZombieVillagerProfession(this));
		//this.addLayer(new LayerZombieVillagerProfessionLevel(this));
		
        
        LayerRenderer layerrenderer = (LayerRenderer)this.layerRenderers.get(0);
        this.field_82434_o = this.modelBipedMain;
        this.zombieVillagerModel = new ModelZombieVillagerModern();
        this.addLayer(new LayerHeldItem(this));
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
        this.field_177122_o = Lists.newArrayList(this.layerRenderers);

        if (layerrenderer instanceof LayerCustomHead)
        {
            this.removeLayer(layerrenderer);
            this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
        }

        this.removeLayer(layerbipedarmor);
        this.addLayer(new LayerVillagerArmor(this));
        this.field_177121_n = Lists.newArrayList(this.layerRenderers);
        
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    @Override
    public void doRender(EntityZombie entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.func_82427_a(entity);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityZombie zombie)
    {
        //return entity.isVillager() ? zombieVillagerTextures : zombieTextures;
    	if (zombie instanceof EntityPigZombie) { // Is a zombie pigman
    		return zombiePigmanTextures;
    	}
    	else if ( zombie.isVillager() ) // Is a zombie villager
    	{
    		// v3.2 - allow for modular mod villager skins
    		IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
    		int indexofmodprof = GeneralConfig.professionID_a.indexOf(zombie.getVillagerTypeForge().getRegistryName().toString());
    		
    		if (
    				GeneralConfig.modernVillagerSkins
    				&& ims != null
    				&& (
    						ims.getProfession() <= 4
    						|| (
    								indexofmodprof > -1
    								&& (
    										!((String) GeneralConfig.careerAsset_a.get(indexofmodprof)).equals("")
    										|| !((String) GeneralConfig.zombieCareerAsset_a.get(indexofmodprof)).equals("")
    										)
    									)
    						)
    				)
    		{
    			return zombieVillagerBaseSkin;
    		}
    		// v3.2 - Re-enable vanilla style professionized skins
    		else
    		{
    			switch (ims.getProfession())
    		    {
    		        case 0:
    		            return zombieVillagerFarmerLocation;
    		        case 1:
    		            return zombieVillagerLibrarianLoc;
    		        case 2:
    		            return zombieVillagerPriestLocation;
    		        case 3:
    		            return zombieVillagerSmithLocation;
    		        case 4:
    		            return zombieVillagerButcherLocation;
    		        case 5:
    		        default:
    		        	return zombieVillagerTextures;
    		    }
    			
    		}
        } 
        else { // Is an ordinary zombie
            return zombieTextures; // The default zombie skin
        }
    }
    
    private void func_82427_a(EntityZombie zombie)
    {
        if (zombie.isVillager())
        {
            this.mainModel = this.zombieVillagerModel;
            this.layerRenderers = this.field_177121_n;
        }
        else
        {
            this.mainModel = this.field_82434_o;
            this.layerRenderers = this.field_177122_o;
        }

        this.modelBipedMain = (ModelBiped)this.mainModel;
    }
    
    @Override
    protected void rotateCorpse(EntityZombie bat, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        if (bat.isConverting())
        {
            p_77043_3_ += (float)(Math.cos((double)bat.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }

        super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
    }
    
    
    /**
     * Below are the three additional layers associated with biome and profession.
     * @author AstroTibs
     */
	@SideOnly(Side.CLIENT)
	public class LayerZombieVillagerBiomeType implements LayerRenderer<EntityZombie>
	{
	    private final RenderZombieVillagerModern zombieVillagerLayerRenderer;
	    private final ModelZombieVillagerModern zombieVillagerLayerModel = new ModelZombieVillagerModern(0.0F, 0.1F, false);
	    
	    public LayerZombieVillagerBiomeType(RenderZombieVillagerModern villagerRenderIn)
	    {
	        this.zombieVillagerLayerRenderer = villagerRenderIn;
	    }
	    
	    @Override
	    public void doRenderLayer(EntityZombie zombie, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
	    	
			if (
					ims.getProfession() >= 0
					// v3.2: Is vanilla OR is a modular type
					&& (ims.getProfession() <= 4 || GeneralConfig.professionID_a.indexOf(zombie.getVillagerTypeForge().getRegistryName().toString())!=-1)
					&& !zombie.isInvisible()
					)
			{
				// Biome type skins
				if (GeneralConfig.modernVillagerSkins)
				{/**/
					switch (ims.getBiomeType())
					{
						case 11:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeSnow); break;
						case 9:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeSavanna); break;
						case 8:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeDesert); break;
						case 3:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeForest); break;
						case 7:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeTaiga); break;
						case 6:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeSwamp); break;
						case 5:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeJungle); break;
						case 4:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeAquatic); break;
						case 2:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeHighland); break;
						case 10:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeMushroom); break;
						case 1:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeMagical); break;
						case 13:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeNether); break;
						case 12:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypeEnd); break;
						default:
						case 0:
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerTypePlains); break;
					}/**/
					
				}
	            this.zombieVillagerLayerModel.setModelAttributes(this.zombieVillagerLayerRenderer.getMainModel());
	            this.zombieVillagerLayerModel.render(zombie, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
			}
	    	
	    }

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	public class LayerZombieVillagerProfession implements LayerRenderer<EntityZombie>
	{
	    private final RenderZombieVillagerModern zombieVillagerLayerRenderer;
	    private final ModelZombieVillagerModern zombieVillagerLayerModel = new ModelZombieVillagerModern(0.0F, 0.2F, false);
	    
	    public LayerZombieVillagerProfession(RenderZombieVillagerModern villagerRenderIn)
	    {
	        this.zombieVillagerLayerRenderer = villagerRenderIn;
	    }
	    
	    @Override
	    public void doRenderLayer(EntityZombie zombie, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
	    	
	    	// Changed in v3.2 to allow for modded skins
			if (ims.getProfession() >= 0 && !zombie.isInvisible() && !zombie.isChild())
			{
				if (ims.getProfession() <= 4) // Changed in v3.2: there is no official Profession 5!
				{
					// Career skins
					if (GeneralConfig.modernVillagerSkins)
					{
						/**/
			        	int career = ims.getCareer();
						
					    switch (ims.getProfession())
					    {
				        case 0: // Farmer type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionFarmer); break;
				        	case 2:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionFisherman); break;
				        	case 3:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionShepherd); break;
				        	case 4:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionFletcher); break;
				        	}
				        	break;
				        case 1: // Librarian type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionLibrarian); break;
				        	case 2:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionCartographer); break;
				        	}
				        	break;
				        case 2: // Priest type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionCleric); break;
				        	}
				        	break;
				        case 3: // Smith type
				        	switch (career)
				        	{
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionArmorer); break;
				        	case 2:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionWeaponsmith); break;
				        	default:
				        	case 3:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionToolsmith); break;
				        	case 4:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionMason); break;
				        	}
				        	break;
				        case 4: // Butcher type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionButcher); break;
				        	case 2:
				        		this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionLeatherworker); break;
				        	}
				        	break;
			        	// Fixed in v3.2: there is no vanilla Nitwit at this stage.
				        default: // Nitwit
				        	this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionNitwit); break;
					    }
					}
				}
				else
				{
					// Mod profession skins
					int indexofmodprof = GeneralConfig.professionID_a.indexOf(zombie.getVillagerTypeForge().getRegistryName().toString());
					if (indexofmodprof > -1) // Has a skin asset mapping
					{
						final String zombieCareerProfRootname = (String) GeneralConfig.zombieCareerAsset_a.get(indexofmodprof);
						final String careerProfRootname = (String) GeneralConfig.careerAsset_a.get(indexofmodprof);
						
						if (!(zombieCareerProfRootname).equals("")) // The zombie version mapping isn't blank
						{
				        	final ResourceLocation modCareerSkin = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/villager/profession/"+zombieCareerProfRootname+".png");
				        	this.zombieVillagerLayerRenderer.bindTexture(modCareerSkin);
						}
						else if (!(careerProfRootname).equals("")) // The non-zombie version mapping isn't blank
						{
				        	final ResourceLocation modCareerSkin = new ResourceLocation((Reference.MOD_ID).toLowerCase(), "textures/entity/villager/profession/"+careerProfRootname+".png");
				        	this.zombieVillagerLayerRenderer.bindTexture(modCareerSkin);
						}
						else
						{
							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionNitwit);
						}
					}
					else
					{
						// If all else fails, bind the nitwit.
						this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionNitwit);
					}
				}
				
	            this.zombieVillagerLayerModel.setModelAttributes(this.zombieVillagerLayerRenderer.getMainModel());
	            this.zombieVillagerLayerModel.render(zombie, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
			}
	    	
	    }

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}
	
	/*
	@SideOnly(Side.CLIENT)
	public class LayerZombieVillagerProfessionLevel implements LayerRenderer<EntityZombie>
	{
	    private final RenderZombieVillagerModern zombieVillagerLayerRenderer;
	    private final ModelZombieVillagerModern zombieVillagerLayerModel = new ModelZombieVillagerModern(0.0F, 0.3F, false);
	    
	    public LayerZombieVillagerProfessionLevel(RenderZombieVillagerModern villagerRenderIn)
	    {
	        this.zombieVillagerLayerRenderer = villagerRenderIn;
	    }
	    
	    @Override
	    public void doRenderLayer(EntityZombie zombie, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
	    	
			if (ims.getProfession() >= 0 && see biome type for this && !zombie.isInvisible())
			{
				// Biome type skins
				if (GeneralConfig.modernVillagerSkins)
				{
					//LogHelper.info((ExtendedVillager.get(villager)).getProfessionLevel());
					switch (ims.getProfessionLevel())
					{
						case 1: this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionLevelStone); break;
						case 2: this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionLevelIron); break;
						case 3: this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionLevelGold); break;
						case 4: this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionLevelEmerald); break;
						case 5: this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionLevelDiamond); break;
					}
				}
	            this.zombieVillagerLayerModel.setModelAttributes(this.zombieVillagerLayerRenderer.getMainModel());
	            this.zombieVillagerLayerModel.render(zombie, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
			}
	    }

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}
    */
}