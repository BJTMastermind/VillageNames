package astrotibs.villagenames.tracker;

import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.tracker.ServerInfoTracker.EventType;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;


/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/tracker/EventTracker.java
 * @author AstroTibs
 */

public class EventTracker
{

    //private final Vec3i  _position;
	//private final Vec3   eventPos;
	private final Vec3i  eventPos;
    private final String customName;
    private final Object extraInfo;
    private int          tickWhenAdded;
    private final int    entityID;

    static final String PMTMUnloc = "Traveling Merchant";
    static final String PMTMUnlocModern = "Traveling Merchant";
    static final String PMShUnloc = "Sheepman";
    static final String PMShUnlocModern = "Sheepman";
    static final String PMSSUnloc = "Sheepman Smith";
    
    public Vec3i getPosition()
    //public Vec3 getPosition()
    {
        return eventPos;
    }

    public String getCustomName()
    {
        return customName;
    }
    
    public Object getExtraInfo()
    {
        return extraInfo;
    }

    public int getEntityID()
    {
        return entityID;
    }

    public void setBirthTick(int tick)
    {
        this.tickWhenAdded = tick;
    }

    public int getBirthTick()
    {
        return tickWhenAdded;
    }

    public void expireNow()
    {
        this.tickWhenAdded = -1;
    }



    private EventTracker(int entityID, Vec3i pos, String customName, Object extraInfo) {
        this.entityID = entityID;
        this.customName = customName;
        this.eventPos = pos;
        this.extraInfo = extraInfo;
        this.tickWhenAdded = 0;
    }

    public EventTracker(EntityVillager villager, IModularSkin ims) {
        this(
        		villager.getEntityId(),
        		new Vec3i(villager.posX, villager.posY + 0.5D, villager.posZ),
        		villager.getCustomNameTag(),
        		
        		new Object[] {
        				
        				villager.getProfession(),

        				ims.getCareer(),
        				villager.isChild(),
        				(GeneralConfig.modernVillagerSkins) ? ims.getBiomeType() : -1,
                		(GeneralConfig.modernVillagerSkins) ? ims.getProfessionLevel() : -1,
						(GeneralConfig.modernVillagerSkins && GeneralConfig.villagerSkinTones) ? ims.getSkinTone() : -99
        				
        				/*
        				(Integer)ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"}),
        				villager.isChild(),
        				(GeneralConfig.modernVillagerSkins) ? (villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null)).getBiomeType() : -1,
        				(Integer)ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerLevel", "field_175562_bw"}),
        				*/
        				}
        		);
    }
    
    
    public EventTracker(EntityLiving guard) {
        this(
        		guard.getEntityId(),
        		new Vec3i(guard.posX, guard.posY + 0.5D, guard.posZ),
        		guard.getCustomNameTag(),
        		new Object[] {}
        		);
    }
    
    
    public EventTracker(EntityZombieVillager zombievillager, IModularSkin ims) {
    	this(
    			zombievillager.getEntityId(),
    			new Vec3i(zombievillager.posX, zombievillager.posY + 0.5D, zombievillager.posZ),
    			zombievillager.getCustomNameTag(),
    			
	   			new Object[] {
	   					ims.getProfession(),
	   					ims.getCareer(),
	       				zombievillager.isChild(),
	       				(GeneralConfig.modernVillagerSkins) ? ims.getBiomeType() : -1,
	       				(GeneralConfig.modernVillagerSkins) ? ims.getProfessionLevel() : -1,
        				(GeneralConfig.villagerSkinTones) ? ims.getSkinTone() : -99,
	       				}
    			);
    }

    /**
     * Updates a zombie villager with the villager info this object is tracking.
     */
    public void updateZombie(EntityJoinWorldEvent event, IModularSkin ims)
    {
    	EntityZombieVillager zombievillager = (EntityZombieVillager) event.getEntity();
    	
    	if (GeneralConfig.debugMessages) {LogHelper.info("EventTracker.updateZombie called with this.getCustomName(): " + this.getCustomName() + ", this.getObject(): " + this.getExtraInfo() );}
        
    	// Note: I must trust that this object actually contain a villager info. If not, the cast below will fail.
        final Object[] extraInfo = (Object[]) this.getExtraInfo();
        final int profession = (Integer) extraInfo[0];
        final int career     = (Integer) extraInfo[1];
        final boolean isBaby = (Boolean) extraInfo[2];
        final int biomeType = (Integer) extraInfo[3];
        final int professionLevel = (Integer) extraInfo[4];
        final int skinTone = (Integer) extraInfo[5];

        // Custom name
        //if (this.getCustomName() != "") {
        String customName = this.getCustomName();
        
        if ( !customName.equals("") && !customName.equals(null)
        		&& !customName.equals( PMTMUnloc )
        		&& !customName.equals( PMTMUnlocModern )
        		&& !customName.equals( PMShUnloc )
        		&& !customName.equals( PMShUnlocModern )
        		&& !customName.equals( PMSSUnloc )
        		) {
        	zombievillager.setCustomNameTag(this.getCustomName());
        	zombievillager.enablePersistence(); //Equivalent to EntityLiving.enablePersistence() in 1.8
        }

        // Adult or child
        zombievillager.setChild(isBaby);
        
        // Profession
        //if ( customName.equals( I18n.format(Reference.PMSheepmanSmithUnlocalized) ) ) {
        if ( customName.equals( PMSSUnloc ) ) {
        	ims.setProfession(3); // Hard-wired to be a blacksmith -- only works if the sheep has no name :-/
        }
        else if (profession >= 0) {// Allowing above 4 so that you can have modded professions  //&& profession <= 4) {   // vanilla professions
        	ims.setProfession(profession);
        } else {
        	ims.setProfession(-1);           // vanilla zombie villager
        }
        
        if ( career > 0 ) {
        	ims.setCareer(career);
        }
        else {
        	ims.setCareer(0);
        }
        

        // BiomeType
        if (ims.getBiomeType() <0)
        {
        	ims.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(zombievillager));
        }
        else
        {
        	ims.setBiomeType(biomeType);
        }


        // SkinTone
        if (ims.getSkinTone() == -99) {ims.setSkinTone(FunctionsVN.returnSkinToneForEntityLocation(zombievillager));}
        else {ims.setSkinTone(skinTone);}
        
        
        // ProfessionLevel
        ims.setProfessionLevel(professionLevel);
        
        zombievillager.setCanPickUpLoot(false);
        
        // Strip gear
        if (zombievillager.hasItemInSlot(EntityEquipmentSlot.MAINHAND)) {zombievillager.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.AIR));}
        if (zombievillager.hasItemInSlot(EntityEquipmentSlot.OFFHAND)) {zombievillager.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.AIR));}
        if (zombievillager.hasItemInSlot(EntityEquipmentSlot.CHEST)) {zombievillager.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.AIR));}
        if (zombievillager.hasItemInSlot(EntityEquipmentSlot.FEET)) {zombievillager.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.AIR));}
        if (zombievillager.hasItemInSlot(EntityEquipmentSlot.HEAD)) {zombievillager.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.AIR));}
        if (zombievillager.hasItemInSlot(EntityEquipmentSlot.LEGS)) {zombievillager.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.AIR));}
        
    }
    
    

    /**
     * Updates a village guard entity with the villager info this object is tracking.
     * 
     */
    public void updateGuard(LivingUpdateEvent event, IModularSkin ims)
    {
    	EntityLiving guard = (EntityLiving) event.getEntity();
    	NBTTagCompound compound = new NBTTagCompound();
    	guard.writeEntityToNBT(compound);
		int targetAge = compound.getInteger("Age");
		
    	if (GeneralConfig.debugMessages) {LogHelper.info("EventTracker.updateGuard called with this.getCustomName(): " + this.getCustomName() + ", this.getObject(): " + this.getExtraInfo() );}
        
    	// Note: I must trust that this object actually contain a villager info. If not, the cast below will fail.
        final Object[] extraInfo = (Object[]) this.getExtraInfo();
        
        String customName = this.getCustomName();
        
        
        if ( !customName.equals("") && !customName.equals(null)
        		) {
        	
        	
        	// Go through the list and eliminate all entries with a matching name
            for (int i = (EventType.GUARD).getTracker().size()-1; i >=0; i-- ) {
            	if ( ((EventType.GUARD).getTracker().get(i)).customName == customName) {(EventType.GUARD).getTracker().remove(i);}
            }
        	
        	
        	// Then account for profession flag
        	
        	if ( // Remove any tag that already exists
        			customName.indexOf("(")!=-1
        			) { // Target has a job tag: remove it...
				customName = customName.substring(0, customName.indexOf("(")).trim();
			}
        	if ( // Add a profession tag if the flag is set
        			(
        					GeneralConfig.addJobToName
        					&& ( !(guard instanceof EntityVillager) || targetAge>=0 )
        					)
        			) { // Target is named but does not have job tag: add one!
        		customName = customName + " " + NameGenerator.getCareerTag(guard.getClass().toString().substring(6), 0, "", 0, "witcheryGuard");
        		customName = customName.trim();
			}
        	
        	guard.setCustomNameTag(customName);
        	guard.enablePersistence();
        }
        
    }
    
    
    
    
    /**
     * Updates a villager entity with the zombie info this object is tracking.
     * 
     * @param villager
     */
    public void updateVillager(EntityVillager villager, IModularSkin ims)
    {
    	if (GeneralConfig.debugMessages) {LogHelper.info("EventTracker.updateVillager called with this.getCustomName(): " + this.getCustomName());}

    	final Object[] extraInfo = (Object[]) this.getExtraInfo();
        final int profession = (Integer) extraInfo[0];
        final int career     = (Integer) extraInfo[1];
        final boolean isBaby = (Boolean) extraInfo[2];
        final int biomeType = (Integer) extraInfo[3];
        final int professionLevel = (Integer) extraInfo[4];
        final int skinTone = (Integer) extraInfo[5];

        String customName = this.getCustomName();
        
        // Custom name
        if ( !customName.equals("") && !customName.equals(null)
        		&& !customName.equals( PMTMUnloc )
        		&& !customName.equals( PMTMUnlocModern )
        		&& !customName.equals( PMShUnloc )
        		&& !customName.equals( PMShUnlocModern )
        		&& !customName.equals( PMSSUnloc )
        		) {
        	villager.setCustomNameTag(this.getCustomName());
        }
        
        // Adult or child
        villager.setGrowingAge(isBaby ? -24000 : 0);
        
        // Profession
        //if ( customName.equals( I18n.format(Reference.PMSheepmanSmithUnlocalized) ) ) {
        if ( customName.equals( PMSSUnloc ) )
        {
        	villager.setProfession(3); // Hard-wired to be a blacksmith -- only works if the sheep has no name :-/
        }
        else
        {// Allowing above 4 so that you can have modded professions  //&& profession <= 4) {   // vanilla professions
        	villager.setProfession(profession);
        }
        
        // Career
        if ( career > 0 ) {
        	ims.setCareer(career);
        }
        else {
        	ims.setCareer(0);
        }
        
        // BiomeType
        if (ims.getBiomeType() <0)
        {
        	ims.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(villager));
        }
        else
        {
        	ims.setBiomeType(biomeType);
        }

        // SkinTone
        if (ims.getSkinTone() == -99) {ims.setSkinTone(FunctionsVN.returnSkinToneForEntityLocation(villager));}
        else {ims.setSkinTone(skinTone);}
        
    }
    

    @Override
    public String toString()
    {
        final StringBuilder r = new StringBuilder();

        r.append("Entity ID = ");
        r.append(this.getEntityID());
        r.append(", Position = ");
        if (this.getPosition() == null) {
            r.append("NULL");
        } else {
            r.append(this.getPosition().toString());
        }
        r.append(", Tick of Birth = ");
        r.append(this.getBirthTick());
        r.append(", Custom Name = ");
        r.append(this.getCustomName());
        r.append(", Extra Info = ");
        if (this.getExtraInfo() == null) {
            r.append("NULL");
        } else {
            r.append(this.getExtraInfo().getClass().getName());
            r.append(":");
            r.append(this.getExtraInfo().toString());
        }

        return r.toString();
    }



}
