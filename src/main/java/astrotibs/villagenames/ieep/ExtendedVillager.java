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
	public final static String biomeKey = "BiomeType"; // Added in v3.1
	public final static String careerVNKey = "Career"; // Added in v3.1trades - to allow for Cartographer and Mason
	public final static String professionLevelVNKey = "ProfessionLevel"; // Added in v3.1trades
	public final static String skinToneKey = "SkinTone"; // Added in v3.2
	protected final static String InitializedKey = "Defined";               // Controls if a villager was assigned a career and career level
    
	private final EntityVillager villager;
	private int biomeType; // Added in v3.1
	private int professionLevelVN; // Added in v3.1trades - Used to modernize villager trades  
	private int careerVN; // Added in v3.1trades - Used to modernize villager trades  
	private int skinTone; // Added in v3.2
    private Boolean hasValidData;
    protected World theWorld;
    
	public ExtendedVillager(EntityVillager villager) {
		this.villager = villager;
		this.biomeType = -1; // Added in v3.1
		this.professionLevelVN = 0; // Added in v3.1trades
		this.skinTone = -99; // Added in v3.2
		this.careerVN = 0; // Added in v3.1trades
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
	
	// Added in v3.1
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
	
	// Added in v3.1trades
	public void setProfessionLevelVN(int pl)
	{
		this.professionLevelVN = pl;
	}
	public int getProfessionLevelVN()
	{
		return this.professionLevelVN;
	}
	
	// Added in v3.1trades
	public void setCareerVN(int c)
	{
		this.careerVN = c;
	}
	public int getCareerVN()
	{
		return this.careerVN;
	}

	// Added in v3.2
	public int getSkinTone() {return this.skinTone;}
	public void setSkinTone(int pl) {this.skinTone = pl;}
	
	// Save any custom data that needs saving here
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		
        if (this.hasValidData == null) {
            this.biomeType = -1; // Added in v3.1
            this.professionLevelVN = 0; // Added in v3.1trades
            this.careerVN = 0; // Added in v3.1trades
            this.skinTone = -99; // Added in v3.2
            this.hasValidData = false;
        }
        
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger(biomeKey, this.biomeType); // Added in v3.1
        properties.setInteger(professionLevelVNKey, this.professionLevelVN); // Added in v3.1trades
        properties.setInteger(careerVNKey, this.careerVN); // Added in v3.1trades
        properties.setInteger(skinToneKey, this.skinTone); // Added in v3.2
        properties.setBoolean(InitializedKey, this.hasValidData);

        compound.setTag(VN_VILLAGER_TAGS, properties); 
	}

	// Load whatever data you saved
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		
        NBTTagCompound properties = (NBTTagCompound)compound.getTag(VN_VILLAGER_TAGS);

        if (properties == null) {
            hasValidData = false;
            professionLevelVN = 0; // Added in v3.1trades
            careerVN = 0; // Added in v3.1trades
            biomeType = -1; // Added in v3.1
            skinTone = -99; // Added in v3.2
        } 
        else {
            this.biomeType = properties.getInteger(biomeKey); // Added in v3.1
            this.professionLevelVN = properties.getInteger(professionLevelVNKey); // Added in v3.1trades
            this.careerVN = properties.getInteger(careerVNKey); // Added in v3.1trades
            this.skinTone = properties.getInteger(skinToneKey); // Added in v3.2
            this.hasValidData = properties.getBoolean(InitializedKey);
        }
	}

	@Override
	public void init(Entity entity, World world) {
        theWorld = world;
	}
	
}
