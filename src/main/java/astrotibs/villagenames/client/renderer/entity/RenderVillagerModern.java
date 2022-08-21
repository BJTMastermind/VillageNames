package astrotibs.villagenames.client.renderer.entity;

import astrotibs.villagenames.client.model.ModelVillagerModern;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.ieep.ExtendedVillager;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Adapted from Villager Mantle Fix by MJaroslav
 * https://github.com/MJaroslav/VillagerMantleFix/blob/1.7.10/src/main/java/mjaroslav/mcmods/villagermantlefix/client/renderer/entity/RenderVillagerAlt.java
 * @author AstroTibs
 */

@SideOnly(Side.CLIENT)
public class RenderVillagerModern extends RenderLiving<EntityVillager> {
	
	// Constructor
	public RenderVillagerModern(RenderManager renderManagerIn) {
		//this.mainModel = new ModelVillagerModern(0.0F);
		//this.villagerModel = (ModelVillagerModern) this.mainModel;
		//this.setRenderPassModel(new ModelVillagerModern(0.1F));
		super(renderManagerIn, new ModelVillagerModern(0.0F), 0.5F);
		this.addLayer(new LayerVillagerBiomeType(this));
		this.addLayer(new LayerVillagerProfession(this));
		this.addLayer(new LayerVillagerProfessionLevel(this));
	}
	
	// ------------------------------ //
	// --- Skin resource elements --- //
	// ------------------------------ //

	static final String VAD = "textures/entity/villager/"; // Villager address, because it's used so damn much
	static final String MIDLC = (Reference.MOD_ID).toLowerCase(); // Same with Mod ID

	// Base skin texture
	private static final ResourceLocation VILLAGER_BASE_SKIN     = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("villager.png").toString());
	
	// Biome-based types
	private static final ResourceLocation VILLAGER_TYPE_DESERT   = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/desert.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_JUNGLE   = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/jungle.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_PLAINS   = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/plains.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_SAVANNA  = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/savanna.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_SNOW     = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/snow.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_SWAMP    = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/swamp.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_TAIGA    = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/taiga.png").toString());
	// Custom biome types
	private static final ResourceLocation VILLAGER_TYPE_FOREST   = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/forest.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_AQUATIC  = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/aquatic.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_HIGHLAND = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/highland.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_MUSHROOM = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/mushroom.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_MAGICAL  = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/magical.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_NETHER   = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/nether.png").toString());
	private static final ResourceLocation VILLAGER_TYPE_END      = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("type/end.png").toString());
	
	// Profession-based layer
	private static final ResourceLocation VILLAGER_PROFESSION_ARMORER = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/armorer.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_BUTCHER = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/butcher.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_CARTOGRAPHER = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/cartographer.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_CLERIC = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/cleric.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_FARMER = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/farmer.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_FISHERMAN = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/fisherman.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_FLETCHER = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/fletcher.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_LEATHERWORKER = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/leatherworker.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_LIBRARIAN = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/librarian.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_MASON = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/mason.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_NITWIT = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/nitwit.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_SHEPHERD = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/shepherd.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_TOOLSMITH = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/toolsmith.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_WEAPONSMITH = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/weaponsmith.png").toString());
	
	// Profession level purses
	private static final ResourceLocation VILLAGER_PROFESSION_LEVEL_STONE = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession_level/stone.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_LEVEL_IRON = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession_level/iron.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_LEVEL_GOLD = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession_level/gold.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_LEVEL_EMERALD = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession_level/emerald.png").toString());
	private static final ResourceLocation VILLAGER_PROFESSION_LEVEL_DIAMOND = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession_level/diamond.png").toString());
	
	// Vanilla textures
    private static final ResourceLocation DEFAULT_OLD_NITWIT = new ResourceLocation((new StringBuilder()).append(VAD).append("villager.png").toString());
    private static final ResourceLocation DEFAULT_OLD_FARMER = new ResourceLocation((new StringBuilder()).append(VAD).append("farmer.png").toString());
    private static final ResourceLocation DEFAULT_OLD_LIBRARIAN = new ResourceLocation((new StringBuilder()).append(VAD).append("librarian.png").toString());
    private static final ResourceLocation DEFAULT_OLD_PRIEST = new ResourceLocation((new StringBuilder()).append(VAD).append("priest.png").toString());
    private static final ResourceLocation DEFAULT_OLD_SMITH = new ResourceLocation((new StringBuilder()).append(VAD).append("smith.png").toString());
    private static final ResourceLocation DEFAULT_OLD_BUTCHER = new ResourceLocation((new StringBuilder()).append(VAD).append("butcher.png").toString());

	// Skin tones, arranged lightest to darkest
    private static final ResourceLocation VILLAGER_SKIN_TONE_LIGHT3 = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("skintone/l3.png").toString());
    private static final ResourceLocation VILLAGER_SKIN_TONE_LIGHT2 = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("skintone/l2.png").toString());
    private static final ResourceLocation VILLAGER_SKIN_TONE_LIGHT1 = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("skintone/l1.png").toString());
    private static final ResourceLocation VILLAGER_SKIN_TONE_MEDIUM = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("skintone/m0.png").toString()); // Identical to default skin
    private static final ResourceLocation VILLAGER_SKIN_TONE_DARK1 = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("skintone/d1.png").toString());
    private static final ResourceLocation VILLAGER_SKIN_TONE_DARK2 = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("skintone/d2.png").toString());
    private static final ResourceLocation VILLAGER_SKIN_TONE_DARK3 = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("skintone/d3.png").toString());
    private static final ResourceLocation VILLAGER_SKIN_TONE_DARK4 = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("skintone/d4.png").toString());
    

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityVillager villager)
	{
		int profession = villager.getProfession();
//		int indexofmodprof = GeneralConfig.professionID_a.indexOf(profession);
		int career = (ExtendedVillager.get(villager)).getCareer();
		
		if (
				GeneralConfig.modernVillagerSkins
				& profession >= 0
				// Changed to allow modular mod villager skins; also, no official Prof 5
				& (profession <= 4
					| GeneralConfig.profession_concat_career_a.contains((new StringBuilder()).append(profession).append(-99).toString()) 
					| GeneralConfig.profession_concat_career_a.contains((new StringBuilder()).append(profession).append(career).toString())
					)
				)
		{
			if (GeneralConfig.villagerSkinTones)
			{
				switch ((ExtendedVillager.get(villager)).getSkinTone())
				{
					case 3: return VILLAGER_SKIN_TONE_LIGHT3;
					case 2: return VILLAGER_SKIN_TONE_LIGHT2;
					case 1: return VILLAGER_SKIN_TONE_LIGHT1;
					default:
					case 0: return VILLAGER_SKIN_TONE_MEDIUM;
					case -1: return VILLAGER_SKIN_TONE_DARK1;
					case -2: return VILLAGER_SKIN_TONE_DARK2;
					case -3: return VILLAGER_SKIN_TONE_DARK3;
					case -4: return VILLAGER_SKIN_TONE_DARK4;
				}
			}
			else {return VILLAGER_BASE_SKIN;}
		}
		else
		{
			// Condition for modern villager skins OFF
		    switch (profession)
		    {
		        case 0:
		            return DEFAULT_OLD_FARMER;
		        case 1:
		            return DEFAULT_OLD_LIBRARIAN;
		        case 2:
		            return DEFAULT_OLD_PRIEST;
		        case 3:
		            return DEFAULT_OLD_SMITH;
		        case 4:
		            return DEFAULT_OLD_BUTCHER;
		        default:
		        	return VillagerRegistry.getVillagerSkin(profession, DEFAULT_OLD_NITWIT);
		    }
		}
	}
	
	@SideOnly(Side.CLIENT)
	public class LayerVillagerBiomeType implements LayerRenderer<EntityVillager>
	{
	    private final RenderVillagerModern villagerLayerRenderer;
	    private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.1F);
	    
	    public LayerVillagerBiomeType(RenderVillagerModern villagerRenderIn)
	    {
	        this.villagerLayerRenderer = villagerRenderIn;
	    }
	    
	    public void doRenderLayer(EntityVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	int profession = villager.getProfession();
	    	int indexofmodprof = GeneralConfig.professionID_a.indexOf(profession);
	    	int careerID = (ExtendedVillager.get(villager)).getCareer();//ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"});
//	    	int index_of_profession_career_on_whitelist = GeneralConfig.profession_concat_career_a.indexOf(new StringBuilder().append(profession).append(careerID).toString());
	    	
//	    	& profession_career_whitelist.indexOf(new StringBuilder().append(prof).append(-99).toString())==-1
//			& index_of_profession_career_on_whitelist==-1
	    	
			if (
					profession >= 0
					& (profession <= 4
						| GeneralConfig.profession_concat_career_a.contains((new StringBuilder()).append(profession).append(-99).toString()) 
						| GeneralConfig.profession_concat_career_a.contains((new StringBuilder()).append(profession).append(careerID).toString())
						)
					& !villager.isInvisible()
					)
			{
				// Biome type skins
				if (GeneralConfig.modernVillagerSkins)
				{
					switch ((ExtendedVillager.get(villager)).getBiomeType())
					{
						case 11:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_SNOW); break;
						case 9:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_SAVANNA); break;
						case 8:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_DESERT); break;
						case 3:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_FOREST); break;
						case 7:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_TAIGA); break;
						case 6:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_SWAMP); break;
						case 5:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_JUNGLE); break;
						case 4:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_AQUATIC); break;
						case 2:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_HIGHLAND); break;
						case 10:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_MUSHROOM); break;
						case 1:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_MAGICAL); break;
						case 13:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_NETHER); break;
						case 12:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_END); break;
						default:
						case 0:
							this.villagerLayerRenderer.bindTexture(VILLAGER_TYPE_PLAINS); break;
					}
					
				}
	            this.villagerLayerModel.setModelAttributes(this.villagerLayerRenderer.getMainModel());
	            this.villagerLayerModel.render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
			}
	    	
	    }

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	public class LayerVillagerProfession implements LayerRenderer<EntityVillager>
	{
	    private final RenderVillagerModern villagerLayerRenderer;
	    private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.2F);

	    public LayerVillagerProfession(RenderVillagerModern villagerRenderIn)
	    {
	        this.villagerLayerRenderer = villagerRenderIn;
	    }
	    
	    @Override
	    public void doRenderLayer(EntityVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	int profession = villager.getProfession();
	    	
			if (profession >= 0 & !villager.isInvisible() & !villager.isChild())
			{
				if (profession <= 4) // Tthere is no official Profession 5!
				{
					// Vanilla profession skins
					if (GeneralConfig.modernVillagerSkins)
					{
			        	int career = (ExtendedVillager.get(villager)).getCareerVN();
						
					    switch (profession)
					    {
				        case 0: // Farmer type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_FARMER); break;
				        	case 2:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_FISHERMAN); break;
				        	case 3:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_SHEPHERD); break;
				        	case 4:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_FLETCHER); break;
				        	}
				        	break;
				        case 1: // Librarian type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_LIBRARIAN); break;
				        	case 2:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_CARTOGRAPHER); break;
				        	}
				        	break;
				        case 2: // Priest type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_CLERIC); break;
				        	}
				        	break;
				        case 3: // Smith type
				        	switch (career)
				        	{
				        	case 1:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_ARMORER); break;
				        	case 2:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_WEAPONSMITH); break;
				        	default:
				        	case 3:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_TOOLSMITH); break;
				        	case 4:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_MASON); break;
				        	}
				        	break;
				        case 4: // Butcher type
				        	switch (career)
				        	{
				        	default:
				        	case 1:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_BUTCHER); break;
				        	case 2:
				        		this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_LEATHERWORKER); break;
				        	}
				        	break;
				        // There is no vanilla Nitwit at this stage.
//				        default: // Nitwit
//				        	this.villagerLayerRenderer.bindTexture(villagerProfessionNitwit); break;
					    }
					}
				}
				else
				{
					// Mod profession skins
//			    	int indexofmodprof = GeneralConfig.professionID_a.indexOf(profession);
			    	int careerID = (ExtendedVillager.get(villager)).getCareer();//ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"});
					int index_of_profession_and_career = Math.max(
							GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(profession).append(-99).toString()), 
							GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(profession).append(careerID).toString()));
					
			    	if (index_of_profession_and_career > -1)
					{
			        	final String profRootName = (String) (GeneralConfig.careerAsset_a.get(index_of_profession_and_career));
			        	final ResourceLocation modCareerSkin = new ResourceLocation(MIDLC, (new StringBuilder()).append(VAD).append("profession/").append(profRootName).append(".png").toString());
			        	this.villagerLayerRenderer.bindTexture(modCareerSkin);
					}
					/*else
					{
						// If all else fails, bind the nitwit.
						this.villagerLayerRenderer.bindTexture(villagerProfessionNitwit);
					}*/
				}
				
	            this.villagerLayerRenderer.getMainModel().render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
	            this.villagerLayerModel.render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
			}
	    	
	    }

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}
	

	@SideOnly(Side.CLIENT)
	public class LayerVillagerProfessionLevel implements LayerRenderer<EntityVillager>
	{
	    private final RenderVillagerModern villagerLayerRenderer;
	    private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.3F);

	    public LayerVillagerProfessionLevel(RenderVillagerModern villagerRenderIn)
	    {
	        this.villagerLayerRenderer = villagerRenderIn;
	    }
	    
	    public void doRenderLayer(EntityVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	    {
	    	int profession = villager.getProfession();
	    	int careerID = (ExtendedVillager.get(villager)).getCareer();
	    	int index_of_profession_and_career = Math.max(
					GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(profession).append(-99).toString()), 
					GeneralConfig.profession_concat_career_a.indexOf((new StringBuilder()).append(profession).append(careerID).toString()));
	    	
			if (
					index_of_profession_and_career > -1
					& !villager.isInvisible()
					)
			{
				// Profession levels
				if (GeneralConfig.modernVillagerSkins)
				{
					final int profLevel = (villager.isChild() | profession==5) ? 0 : (ExtendedVillager.get(villager)).getProfessionLevel();
					if (profLevel >= 5) {this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_LEVEL_DIAMOND);}
					switch (profLevel)
					{
						case 1: this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_LEVEL_STONE); break;
						case 2: this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_LEVEL_IRON); break;
						case 3: this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_LEVEL_GOLD); break;
						case 4: this.villagerLayerRenderer.bindTexture(VILLAGER_PROFESSION_LEVEL_EMERALD); break;
					}
				}
	            this.villagerLayerRenderer.getMainModel().render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
	            this.villagerLayerModel.render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
			}
	    }

		@Override
		public boolean shouldCombineTextures() {
			return true;
		}
	}
	
	// Added in to allow villagers to render as babies and not man-babies
	// summon Villager ~ ~ ~ {Age:-24000}
    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
	@Override
    protected void preRenderCallback(EntityVillager entitylivingbaseIn, float partialTickTime)
    {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0)
        {
            f = (float)((double)f * 0.5D);
            this.shadowSize = 0.25F;
        }
        else
        {
            this.shadowSize = 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }
}

