package astrotibs.villagenames.ieep;

import astrotibs.villagenames.config.GeneralConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;


/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/common/ExtendedVillagerZombie.java
 * @author AstroTibs
 * 
 * Adds extra NBT info to zombie villagers, so I can track info 
 * like the villager original profession. 
 */
public class ExtendedZombieVillager implements IExtendedEntityProperties
{

    public    final static String Identifier = "VillagerInfo";
    protected final static String ProfessionKey = "Profession";             // Controls zombie villager profession
    protected final static String CareerKey = "Career";             // Controls zombie villager profession
	public final static String biomeKey = "BiomeType"; // Added in v3.1
	public final static String professionLevelKey = "ProfessionLevel"; // Added in v3.1
    protected final static String InitializedKey = "Defined";               // Controls if a zombie villager was assigned a profession
    
    //@SuppressWarnings("unused")
    private final EntityZombie zombieWoof;
    protected World myWorld;

    
    
    //---------------------------------------------------------
    // Properties
    //---------------------------------------------------------
    private int profession;
    private int career;
	private int biomeType; // Added in v3.1
	private int professionLevel; // Added in v3.1
    private Boolean hasValidData;      // TODO: Attempt to refactor and get rid of this property
    
    
    public int getProfession()
    {
        return this.profession;
    }
    
    public int getCareer()
    {
        return this.career;
    }
    
    public void setProfession(int profession)
    {
        this.profession = profession >= 0 ? profession : -1;
        this.hasValidData = true;
    }
    
    public void setCareer(int career)
    {
        this.career = career >= 0 ? career : -1;
        this.hasValidData = true;
    }
    

    // Added in v3.1
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
		return this.professionLevel;
	}
	public void setProfessionLevel(int pl)
	{
		this.professionLevel = pl;
		//this.hasValidData = true;
	}
    
    
    public void pickRandomProfessionAndCareer() {
        if (this.profession <= -1 && this.career <= -1 && (this.hasValidData == null || !this.hasValidData)) {
            int p = this.myWorld.rand.nextInt(6); // Uniformly distributed between 0 and 5
            int c = -1;
            
				switch(p) {
				
	            // Added break conditions in v3.1.1 to actually properly select sub-professions for zombie villagers 
				case 0: // FARMER
					c = 1 + this.myWorld.rand.nextInt(4);
					break;
					
				case 1: // LIBRARIAN
					c = 1 + (GeneralConfig.modernVillagerTrades ? this.myWorld.rand.nextInt(2) : 0); // Added in v3.1trades - Cartographer
					break;
					
				case 2: // PRIEST
					c = 1;// + this.myWorld.rand.nextInt(1);
					break;
					
				case 3: // BLACKSMITH
					c = 1 + this.myWorld.rand.nextInt(GeneralConfig.modernVillagerTrades ? 4 : 3); // Added in v3.1trades - Mason
					break;
					
				case 4: // BUTCHER
					c = 1 + this.myWorld.rand.nextInt(2);
					break;
					
				case 5: // NITWIT
					c = 1;// + this.myWorld.rand.nextInt(1);
					break;
			}
				
	    	this.setProfession(p);
	        this.setCareer(c);
        }
    }
    /*
    public void pickRandomProfession()
    {
        if (this.profession <= -1 && (this.hasValidData == null || !this.hasValidData)) {
            int p = this.myWorld.rand.nextInt(6); // Uniformly distributed between 0 and 5
            this.setProfession(p);
        }
    }
    
    
    public void pickRandomCareer()
    {
        if (this.career <= -1 && (this.hasValidData == null || !this.hasValidData)) {
        	
            int profession = this.getProfession();
        	
			switch(profession) {
			
				case 0: // FARMER
					career = 1 + this.myWorld.rand.nextInt(4);
				
				case 1: // LIBRARIAN
					career = 1;// + this.myWorld.rand.nextInt(2);
				
				case 2: // PRIEST
					career = 1;// + this.myWorld.rand.nextInt(1);
				
				case 3: // BLACKSMITH
					career = 1 + this.myWorld.rand.nextInt(3);
				
				case 4: // BUTCHER
					career = 1 + this.myWorld.rand.nextInt(2);
				
				case 5: // NITWIT
					career = 1;// + this.myWorld.rand.nextInt(1);
					
				default:
					career = 0;
			}
        	
            this.setCareer(career);
        }
    }
    */
    
    
    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------
    public ExtendedZombieVillager(EntityZombie zombie)
    {
        this.zombieWoof = zombie;
        this.profession = -1;
        this.career = -1;
		this.biomeType = -1; // Added in v3.1
		this.professionLevel = -1; // Added in v3.1
        this.hasValidData = false;
    }
    
    
    
    //---------------------------------------------------------
    // Methods
    //---------------------------------------------------------
   
    public static final void register(EntityZombie zombie)
    {
        zombie.registerExtendedProperties(ExtendedZombieVillager.Identifier, new ExtendedZombieVillager(zombie));
    }
    
    public static final ExtendedZombieVillager get(EntityZombie zombie) {
        return (ExtendedZombieVillager)zombie.getExtendedProperties(ExtendedZombieVillager.Identifier);
    }
    
    
    @Override
    public void saveNBTData(NBTTagCompound compound) {
    	
        if (this.hasValidData == null) {
            this.profession = -1;
            this.career = -1;
            this.biomeType = -1; // Added in v3.1
            this.professionLevel = -1; // Added in v3.1
            this.hasValidData = false;
        }
        
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger(ProfessionKey, this.profession);
        properties.setInteger(CareerKey, this.career);
        properties.setInteger(biomeKey, this.biomeType); // Added in v3.1
        properties.setInteger(professionLevelKey, this.professionLevel); // Added in v3.1
        properties.setBoolean(InitializedKey, this.hasValidData);

        compound.setTag(Identifier, properties); 
    }

    
    @Override
    public void loadNBTData(NBTTagCompound compound) {
    	
        NBTTagCompound properties = (NBTTagCompound)compound.getTag(Identifier);

        if (properties == null) {
            hasValidData = false;
            profession = -1;
            career = -1;
            biomeType = -1; // Added in v3.1
            professionLevel = -1; // Added in v3.1
        } 
        else {
            this.profession = properties.getInteger(ProfessionKey);
            this.career = properties.getInteger(CareerKey);
            this.biomeType = properties.getInteger(biomeKey); // Added in v3.1
            this.professionLevel = properties.getInteger(professionLevelKey); // Added in v3.1
            this.hasValidData = properties.getBoolean(InitializedKey);
        }

    }

    
    @Override
    public void init(Entity entity, World world) {
    	
        myWorld = world;
    }

}
