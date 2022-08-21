package astrotibs.villagenames.ieep;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;


/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/common/ExtendedVillagerZombie.java
 * @author AstroTibs
 */
public class ExtendedVillageGuard implements IExtendedEntityProperties
{

    public    final static String Identifier = "VillagerInfo";
   protected final static String InitializedKey = "Defined"; // Controls if a village guard was assigned a profession
    
    //@SuppressWarnings("unused")
    private final EntityLiving guard;
    protected World myWorld;

    
    
    //---------------------------------------------------------
    // Properties
    //---------------------------------------------------------
    
    private Boolean hasValidData; // TODO: Attempt to refactor and get rid of this property
    
    
    
    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------
    public ExtendedVillageGuard(EntityLiving guard)
    {
        this.guard = guard;
        this.hasValidData = false;
    }
    
    
    
    //---------------------------------------------------------
    // Methods
    //---------------------------------------------------------
   
    public static final void register(EntityLiving guard)
    {
        guard.registerExtendedProperties(ExtendedVillageGuard.Identifier, new ExtendedVillageGuard(guard));
    }
    
    public static final ExtendedVillageGuard get(EntityLiving guard) {
        return (ExtendedVillageGuard)guard.getExtendedProperties(ExtendedVillageGuard.Identifier);
    }
    
    
    @Override
    public void saveNBTData(NBTTagCompound compound) {
    	
        if (this.hasValidData == null) {
            this.hasValidData = false;
        }
        
        NBTTagCompound properties = new NBTTagCompound();
        properties.setBoolean(InitializedKey, this.hasValidData);

        compound.setTag(Identifier, properties); 
    }

    
    @Override
    public void loadNBTData(NBTTagCompound compound) {
    	
        NBTTagCompound properties = (NBTTagCompound)compound.getTag(Identifier);

        if (properties == null) {
            hasValidData = false;
        } 
        else {
            this.hasValidData = properties.getBoolean(InitializedKey);
        }

    }

    
    @Override
    public void init(Entity entity, World world) {
    	
        myWorld = world;
    }

}
