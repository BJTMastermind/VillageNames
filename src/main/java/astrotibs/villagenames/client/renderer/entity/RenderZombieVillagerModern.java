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
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieVillagerModern extends RenderBiped<EntityZombieVillager>
{
	// ------------------------------ //
	// --- Skin resource elements --- //
	// ------------------------------ //
	
	static final String ZVAD = "textures/entity/zombie_villager/";
	
	// Base skin texture
	private static final ResourceLocation ZOMBIE_VILLAGER_BASE_SKIN = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("zombie_villager.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TIBS_SKIN = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("t_zv.png").toString());
	
	// Biome-based types
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_DESERT  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/desert.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_JUNGLE  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/jungle.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_PLAINS  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/plains.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_SAVANNA = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/savanna.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_SNOW    = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/snow.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_SWAMP   = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/swamp.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_TAIGA   = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/taiga.png").toString());
	// Custom biome types
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_FOREST   = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/forest.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_AQUATIC  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/aquatic.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_HIGHLAND = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/highland.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_MUSHROOM = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/mushroom.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_MAGICAL  = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/magical.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_NETHER   = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/nether.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_TYPE_END      = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("type/end.png").toString());
	
	// Profession-based layer
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_ARMORER = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/armorer.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_BUTCHER = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/butcher.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_CARTOGRAPHER = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/cartographer.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_CLERIC = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/cleric.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_FARMER = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/farmer.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_FISHERMAN = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/fisherman.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_FLETCHER = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/fletcher.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_LEATHERWORKER = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/leatherworker.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_LIBRARIAN = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/librarian.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_MASON = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/mason.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_NITWIT = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/nitwit.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_SHEPHERD = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/shepherd.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_TOOLSMITH = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/toolsmith.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_WEAPONSMITH = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession/weaponsmith.png").toString());
	
	// Profession level purses
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_LEVEL_STONE = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession_level/stone.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_LEVEL_IRON = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession_level/iron.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_LEVEL_GOLD = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession_level/gold.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_LEVEL_EMERALD = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession_level/emerald.png").toString());
	private static final ResourceLocation ZOMBIE_VILLAGER_PROFESSION_LEVEL_DIAMOND = new ResourceLocation((Reference.MOD_ID).toLowerCase(), (new StringBuilder()).append(ZVAD).append("profession_level/diamond.png").toString());
	
	// Vanilla textures
    private static final ResourceLocation ZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("textures/entity/zombie_pigman.png");
    private static final ResourceLocation ZOMBIE_TEXTURE = new ResourceLocation("textures/entity/zombie/zombie.png");
    
    // Re-added to allow for profession-based vanilla zombie textures
    private static final ResourceLocation ZOMBIE_VILLAGER_TEXTURE = new ResourceLocation((new StringBuilder()).append(ZVAD).append("zombie_villager.png").toString());
    private static final ResourceLocation ZOMBIE_VILLAGER_FARMER_LOCATION = new ResourceLocation((new StringBuilder()).append(ZVAD).append("zombie_farmer.png").toString());
    private static final ResourceLocation ZOMBIE_VILLAGER_LIBRARIAN_LOCATION = new ResourceLocation((new StringBuilder()).append(ZVAD).append("zombie_librarian.png").toString());
    private static final ResourceLocation ZOMBIE_VILLAGER_PRIEST_LOCATION = new ResourceLocation((new StringBuilder()).append(ZVAD).append("zombie_priest.png").toString());
    private static final ResourceLocation ZOMBIE_VILLAGER_SMITH_LOCATION = new ResourceLocation((new StringBuilder()).append(ZVAD).append("zombie_smith.png").toString());
    private static final ResourceLocation ZOMBIE_VILLAGER_BUTCHER_LOCATION = new ResourceLocation((new StringBuilder()).append(ZVAD).append("zombie_butcher.png").toString());
    
    
    private ModelBiped modelBipedMain;
    private final ModelZombieVillagerModern zombieVillagerModel;
    private final List<LayerRenderer<EntityZombieVillager>> field_177121_n;
    private final List<LayerRenderer<EntityZombieVillager>> field_177122_o;

    public RenderZombieVillagerModern(RenderManager renderManagerIn)
    {
        //super(renderManagerIn, new ModelZombie(), 0.5F, 1.0F);
    	super(renderManagerIn, new ModelZombie(), 0.5F);
        
        // New render layers for biome type and profession
		this.addLayer(new LayerZombieVillagerBiomeType(this));
		this.addLayer(new LayerZombieVillagerProfession(this));
		//this.addLayer(new LayerZombieVillagerProfessionLevel(this));
		
        
        LayerRenderer layerrenderer = (LayerRenderer)this.layerRenderers.get(0);
        this.modelBipedMain = new ModelBiped();//this.modelBipedMain;
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
            //this.removeLayer(layerrenderer); //TODO - we'll see what happens without this
            this.addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
        }

        //this.removeLayer(layerbipedarmor); //TODO - we'll see what happens without this
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
    public void doRender(EntityZombieVillager entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.func_82427_a(entity);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityZombieVillager zombievillager)
    {
		IModularSkin ims = zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
		String professionForge = zombievillager.getForgeProfession().getRegistryName().toString();
		int profession = ims==null? -1 : ims.getProfession();
//		int careerID = ims==null ? -1 :ims.getCareer();
//		int index_of_profession_and_career = Math.max(
//				GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(professionForge).append(-99).toString()), 
//				GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(professionForge).append(careerID).toString()));
		
		if (
				GeneralConfig.modernVillagerSkins
//				& (Reference.VANILLA_PROFESSIONS.contains(professionForge)
//					| (index_of_profession_and_career != -1)
//					)
				)
		{
	        // give @p minecraft:name_tag 1 0 {display:{Name:"Tibs"}}
	        // give @p minecraft:name_tag 1 0 {display:{Name:"AstroTibs"}}
			
			String trimmed_lc_zombievillager_name = zombievillager.getCustomNameTag().toLowerCase().trim();
			if (!trimmed_lc_zombievillager_name.equals(Reference.NAME_TIBS)
	        		& !trimmed_lc_zombievillager_name.equals(Reference.NAME_ASTROTIBS)
	        		& !(trimmed_lc_zombievillager_name.length()>=11 && trimmed_lc_zombievillager_name.substring(0, 11).equals(Reference.NAME_ASTROTIBS_OPENP))
	        		& !(trimmed_lc_zombievillager_name.length()>=6 && trimmed_lc_zombievillager_name.substring(0, 6).equals(Reference.NAME_TIBS_OPENP))
	        		)
	        {
				return ZOMBIE_VILLAGER_BASE_SKIN;
	        }
			else
			{
				return ZOMBIE_VILLAGER_TIBS_SKIN;
			}
		}
		else
		{
			switch (profession)
		    {
		        case 0:
		            return ZOMBIE_VILLAGER_FARMER_LOCATION;
		        case 1:
		            return ZOMBIE_VILLAGER_LIBRARIAN_LOCATION;
		        case 2:
		            return ZOMBIE_VILLAGER_PRIEST_LOCATION;
		        case 3:
		            return ZOMBIE_VILLAGER_SMITH_LOCATION;
		        case 4:
		            return ZOMBIE_VILLAGER_BUTCHER_LOCATION;
		        case 5:
		        default:
		        	return ZOMBIE_VILLAGER_TEXTURE;
		    }
			
		}
    }
    
    private void func_82427_a(EntityZombieVillager zombie)
    {
        this.mainModel = this.zombieVillagerModel;
        this.layerRenderers = this.field_177121_n;
        this.modelBipedMain = (ModelBiped)this.mainModel;
    }
    
    @Override
    protected void applyRotations(EntityZombieVillager entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        if (entityLiving.isConverting())
        {
            p_77043_3_ += (float)(Math.cos((double)entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }

        super.applyRotations(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
    }
    
    
    /**
     * Below are the three additional layers associated with biome and profession.
     * @author AstroTibs
     */
	@SideOnly(Side.CLIENT)
	public class LayerZombieVillagerBiomeType implements LayerRenderer<EntityZombieVillager>
	{
	    private final RenderZombieVillagerModern zombieVillagerLayerRenderer;
	    private final ModelZombieVillagerModern zombieVillagerLayerModel = new ModelZombieVillagerModern(0.0F, 0.1F, false);
	    
	    public LayerZombieVillagerBiomeType(RenderZombieVillagerModern villagerRenderIn)
	    {
	        this.zombieVillagerLayerRenderer = villagerRenderIn;
	    }
	    
	    @Override
	    public void doRenderLayer(EntityZombieVillager zombie, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
//	    	String professionForge = zombie.getForgeProfession().getRegistryName().toString();
//	    	int profession = ims==null? -1 : ims.getProfession();
//			int careerID = ims==null ? -1 :ims.getCareer();
//			int index_of_profession_and_career = Math.max(
//					GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(professionForge).append(-99).toString()), 
//					GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(professionForge).append(careerID).toString()));
	    	
			if (
//					(Reference.VANILLA_PROFESSIONS.contains(professionForge) | index_of_profession_and_career != -1)
//					&
					!zombie.isInvisible()
					)
			{
				// Biome type skins
				if (GeneralConfig.modernVillagerSkins)
				{
					switch (ims.getBiomeType())
					{
						case 11:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_SNOW); break;
						case 9:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_SAVANNA); break;
						case 8:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_DESERT); break;
						case 3:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_FOREST); break;
						case 7:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_TAIGA); break;
						case 6:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_SWAMP); break;
						case 5:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_JUNGLE); break;
						case 4:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_AQUATIC); break;
						case 2:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_HIGHLAND); break;
						case 10:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_MUSHROOM); break;
						case 1:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_MAGICAL); break;
						case 13:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_NETHER); break;
						case 12:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_END); break;
						default:
						case 0:
							this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_TYPE_PLAINS); break;
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
	
	
	@SideOnly(Side.CLIENT)
	public class LayerZombieVillagerProfession implements LayerRenderer<EntityZombieVillager>
	{
	    private final RenderZombieVillagerModern zombieVillagerLayerRenderer;
	    private final ModelZombieVillagerModern zombieVillagerLayerModel = new ModelZombieVillagerModern(0.0F, 0.2F, false);
	    
	    public LayerZombieVillagerProfession(RenderZombieVillagerModern villagerRenderIn)
	    {
	        this.zombieVillagerLayerRenderer = villagerRenderIn;
	    }
	    
	    @Override
	    public void doRenderLayer(EntityZombieVillager zombie, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
	    	int profession = ims==null? -1 : ims.getProfession();
	    	
			if (profession >= 0 & !zombie.isInvisible() & !zombie.isChild())
			{
				String professionForge = zombie.getForgeProfession().getRegistryName().toString();
				if (Reference.VANILLA_PROFESSIONS.contains(professionForge))
				{
					// Career skins
					if (GeneralConfig.modernVillagerSkins)
					{
			        	int career = ims.getCareer();
						
					    switch (profession)
					    {
				        case 0: // Farmer type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_FARMER); break;
				        	case 2:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_FISHERMAN); break;
				        	case 3:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_SHEPHERD); break;
				        	case 4:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_FLETCHER); break;
				        	}
				        	break;
				        case 1: // Librarian type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_LIBRARIAN); break;
				        	case 2:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_CARTOGRAPHER); break;
				        	}
				        	break;
				        case 2: // Priest type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_CLERIC); break;
				        	}
				        	break;
				        case 3: // Smith type
				        	switch (career)
				        	{
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_ARMORER); break;
				        	case 2:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_WEAPONSMITH); break;
				        	default:
				        	case 3:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_TOOLSMITH); break;
				        	case 4:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_MASON); break;
				        	}
				        	break;
				        case 4: // Butcher type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_BUTCHER); break;
				        	case 2:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_LEATHERWORKER); break;
				        	}
				        	break;
				        case 5: // Nitwit
				        default: // Errors get the Nitwit skin
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.zombieVillagerLayerRenderer.bindTexture(ZOMBIE_VILLAGER_PROFESSION_NITWIT); break;
				        	}
				        	break;
					    }
					}
				}
				else
				{
					// Mod profession skins
					int careerID = ims==null ? -1 :ims.getCareer();
					int index_of_profession_and_career = Math.max(
							GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(professionForge).append(-99).toString()), 
							GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(professionForge).append(careerID).toString()));
					
					if (index_of_profession_and_career != -1)
					{
						final String zombieCareerProfRootname = (String) GeneralConfig.zombieCareerAsset_a.get(index_of_profession_and_career);
						final String careerProfRootname = (String) GeneralConfig.careerAsset_a.get(index_of_profession_and_career);
						
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
//						else
//						{
//							this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionNitwit);
//						}
					}
//					else
//					{
//						// If all else fails, bind the nitwit.
//						this.zombieVillagerLayerRenderer.bindTexture(zombieVillagerProfessionNitwit);
//					}
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
	public class LayerZombieVillagerProfessionLevel implements LayerRenderer<EntityZombieVillager>
	{
	    private final RenderZombieVillagerModern zombieVillagerLayerRenderer;
	    private final ModelZombieVillagerModern zombieVillagerLayerModel = new ModelZombieVillagerModern(0.0F, 0.3F, false);
	    
	    public LayerZombieVillagerProfessionLevel(RenderZombieVillagerModern villagerRenderIn)
	    {
	        this.zombieVillagerLayerRenderer = villagerRenderIn;
	    }
	    
	    @Override
	    public void doRenderLayer(EntityZombieVillager zombie, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
	    	
			if (profession >= 0 & see biome type for this & !zombie.isInvisible())
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