package astrotibs.villagenames.client.model;

import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Adapted from Villager Mantle Fix by MJaroslav
 * https://github.com/MJaroslav/VillagerMantleFix/blob/1.7.10/src/main/java/mjaroslav/mcmods/villagermantlefix/client/model/ModelVillagerAlt.java
 * @author AstroTibs
 */

@SideOnly(Side.CLIENT)
public class ModelZombieVillagerModern extends ModelBiped
{
	public ModelRenderer zombieVillagerHatRimHigh;
	public ModelRenderer zombieVillagerHatRimLow;
	
    public ModelZombieVillagerModern()
    {
        this(0.0F, 0.0F, false);
    }

    public ModelZombieVillagerModern(float modelSize, float thiccness, boolean isAnArmorLayer)
    {
        super(modelSize, 0.0F, 64, isAnArmorLayer ? 32 : 64);
        
        if (isAnArmorLayer)
        {
            this.bipedHead = new ModelRenderer(this, 0, 0);
            this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 8, 8, modelSize);
            this.bipedHead.setRotationPoint(0.0F, 0.0F + thiccness, 0.0F);
            
            this.bipedBody = new ModelRenderer(this, 16, 16);
            this.bipedBody.setRotationPoint(0.0F, 0.0F + thiccness, 0.0F);
            this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.1F);
            
            this.bipedRightLeg = new ModelRenderer(this, 0, 16);
            this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + thiccness, 0.0F);
            this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.1F);
            
            this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + thiccness, 0.0F);
            this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.1F);
        }
        else
        {
            this.bipedHead = new ModelRenderer(this);
            this.bipedHead.setRotationPoint(0.0F, 0.0F + thiccness, 0.0F);
            // Head texture
            this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, modelSize);
            // Nose texture
            this.bipedHead.setTextureOffset(24, 0).addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, modelSize);
            
            this.bipedHeadwear = new ModelRenderer(this);
            
            this.bipedHeadwear.setTextureOffset(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, modelSize + thiccness);
            this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + thiccness, 0.0F);
            
            
    		// Higher Rim -- used for Farmer and Fisherman
    		this.zombieVillagerHatRimHigh = new ModelRenderer(this).setTextureSize(64, 64);
    		//this.bipedHeadwear.setTextureOffset(15, 48);
    		this.bipedHeadwear.cubeList.add(new ModelPlane(this.zombieVillagerHatRimHigh, 15, 48, -8F, -6F, -8F, 16, 0, 16, 0));
    		
    		// Lower Rim -- used for Shepherd
    		this.zombieVillagerHatRimLow = new ModelRenderer(this).setTextureSize(64, 64);
    		//this.bipedHeadwear.setTextureOffset(32, 48);
    		this.bipedHeadwear.cubeList.add(new ModelPlane(this.zombieVillagerHatRimLow, 32, 48, -8F, -5F, -8F, 16, 0, 16, 0));
            
            
    		this.bipedBody = new ModelRenderer(this).setTextureSize(64, 64);
    		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
    		this.bipedBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, modelSize);
    		this.bipedBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, modelSize + thiccness);
    		
    		this.bipedRightArm = new ModelRenderer(this, 44, 22).setTextureSize(64, 64);
    		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
    		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
    		
    		this.bipedLeftArm = new ModelRenderer(this, 44, 22).setTextureSize(64, 64);
    		this.bipedLeftArm.mirror = true;
    		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
    		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
    		
    		this.bipedRightLeg = new ModelRenderer(this, 0, 22).setTextureSize(64, 64);
    		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
    		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
    		
    		this.bipedLeftLeg = new ModelRenderer(this, 0, 22).setTextureSize(64, 64);
    		this.bipedLeftLeg.mirror = true;
    		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
    		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
            
        }
    }
    
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        GlStateManager.pushMatrix();
        
        final IModularSkin ims = entityIn.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
		int prof = ims.getProfession();
		int careerID = ims.getCareer();
//		String profForge = ((EntityZombie)entityIn).getVillagerTypeForge().getRegistryName().toString();
		
		// Zombies don't support string-based profession IDs, so the career subtyping can't be used.
        boolean render_headwear =
        		!(prof > 4
//    				& !GeneralConfig.moddedVillagerHeadwearWhitelist.contains(prof)
//            		& (GeneralConfig.moddedVillagerHeadwearBlacklist.contains(prof) | !GeneralConfig.moddedVillagerHeadwear)
            		);
		
        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            
            if (render_headwear)
            {
            	this.bipedHeadwear.render(scale);
            }
            
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            
			if (render_headwear)
            {
            	this.bipedHeadwear.render(scale);
            }
            
        }

        GlStateManager.popMatrix();
    }
    
    
    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
    {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
        this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
        this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F);
        this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F);
        this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
        this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
    }
}