package astrotibs.villagenames.ieep;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/*
 * Villager
 * summon Villager ~ ~ ~ {Profession:3}
 */

public class ExtendedVillager implements IExtendedEntityProperties {
	
	public final static String VN_VILLAGER_TAGS = "VNExtendedVillager";
	public final static String biomeKey = "BiomeType";
	public final static String careerVNKey = "Career"; // Added to allow for Cartographer and Mason
	public final static String professionLevelVNKey = "ProfessionLevel";
	public final static String skinToneKey = "SkinTone";
	protected final static String InitializedKey = "Defined"; // Controls if a villager was assigned a career and career level
    
	private final EntityVillager villager;
	private int biomeType;
	private int professionLevelVN; // Used to modernize villager trades  
	private int careerVN; // Used to modernize villager trades  
	private int skinTone;
    private Boolean hasValidData;
    protected World theWorld;
    
	public ExtendedVillager(EntityVillager villager) {
		this.villager = villager;
		this.biomeType = -1;
		this.professionLevelVN = 0;
		this.skinTone = -99;
		this.careerVN = 0;
	}

	/**
	* Used to register these extended properties for the villager during EntityConstructing event
	* This method is for convenience only; it will make your code look nicer
	*/
	public static final void register(EntityVillager villager)
	{
		villager.registerExtendedProperties(ExtendedVillager.VN_VILLAGER_TAGS, new ExtendedVillager(villager));
	}
	
	/**
	* Returns ExtendedPlayer properties for villager
	* This method is for convenience only; it will make your code look nicer
	*/
	public static final ExtendedVillager get(EntityVillager villager)
	{
		return (ExtendedVillager) villager.getExtendedProperties(VN_VILLAGER_TAGS);
	}
	
	public int getCareer() {
		return ReflectionHelper.getPrivateValue(EntityVillager.class, this.villager, new String[]{"careerId", "field_175563_bv"});

	}
	public void setCareer(int n) {
		ReflectionHelper.setPrivateValue(EntityVillager.class, this.villager, n, new String[]{"careerId", "field_175563_bv"});
        this.hasValidData = true;
	}
	
	public int getProfession()
	{
		return villager.getProfession();
	}
	public int getBiomeType()
	{
		return this.biomeType;
	}
	public void setBiomeType(int b)
	{
		this.biomeType = b;
		//this.hasValidData = true;
	}
	public int getProfessionLevel()
	{
		return ReflectionHelper.getPrivateValue(EntityVillager.class, this.villager, new String[]{"careerLevel", "field_175562_bw"});
	}
	
	public void setProfessionLevel(int pl)
	{
		ReflectionHelper.setPrivateValue(EntityVillager.class, this.villager, pl, new String[]{"careerLevel", "field_175562_bw"});
		//this.hasValidData = true;
	}
	
	public void setProfessionLevelVN(int pl)
	{
		this.professionLevelVN = pl;
	}
	public int getProfessionLevelVN()
	{
		return this.professionLevelVN;
	}
	
	public void setCareerVN(int c)
	{
		this.careerVN = c;
	}
	public int getCareerVN()
	{
		return this.careerVN;
	}

	public int getSkinTone() {return this.skinTone;}
	public void setSkinTone(int pl) {this.skinTone = pl;}
	
	// Save any custom data that needs saving here
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		
        if (this.hasValidData == null) {
            this.biomeType = -1;
            this.professionLevelVN = 0;
            this.careerVN = 0;
            this.skinTone = -99;
            this.hasValidData = false;
        }
        
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger(biomeKey, this.biomeType);
        properties.setInteger(professionLevelVNKey, this.professionLevelVN);
        properties.setInteger(careerVNKey, this.careerVN);
        properties.setInteger(skinToneKey, this.skinTone);
        properties.setBoolean(InitializedKey, this.hasValidData);

        compound.setTag(VN_VILLAGER_TAGS, properties); 
	}

	// Load whatever data you saved
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		
        NBTTagCompound properties = (NBTTagCompound)compound.getTag(VN_VILLAGER_TAGS);

        if (properties == null) {
            hasValidData = false;
            professionLevelVN = 0;
            careerVN = 0;
            biomeType = -1;
            skinTone = -99;
        } 
        else {
            this.biomeType = properties.hasKey(biomeKey) ? properties.getInteger(biomeKey) : -1;
            this.professionLevelVN = properties.getInteger(professionLevelVNKey);
            this.careerVN = properties.getInteger(careerVNKey);
            this.skinTone = properties.hasKey(skinToneKey) ? properties.getInteger(skinToneKey) : -99;
            this.hasValidData = properties.getBoolean(InitializedKey);
        }
	}

	@Override
	public void init(Entity entity, World world) {
        theWorld = world;
	}
	
}
