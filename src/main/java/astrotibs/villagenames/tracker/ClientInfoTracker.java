package astrotibs.villagenames.tracker;

import java.util.HashMap;

import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.network.MessageModernVillagerSkin;
import astrotibs.villagenames.network.MessageVillageGuard;
import astrotibs.villagenames.network.MessageZombieVillagerProfession;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/tracker/ClientInfoTracker.java
 * 
 * @author AstroTibs
 *
 * Helper class to manage client-side information.
 *
 */
public class ClientInfoTracker
{
    /*
     * This map holds packets sent from Server to Client indicating which mob
     * has the extended properties, so the client can load the correct skin / overlay / particles.
     * 
     * The info comes from [MetworkHelper.Hanlder] methods and must be loaded
     * when the client executes [EntityJoinWorldEvent].
     * 
     * I can't use the info before that because when the client receives the packet,
     * the local world still haven't loaded all entities (at least on the first load of the world).
     *  
     */
	
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, MessageZombieVillagerProfession> LoadedZombies = new HashMap<Integer, MessageZombieVillagerProfession>();
    
    @SideOnly(Side.CLIENT)
    public static void addZombieMessage(MessageZombieVillagerProfession message) {
        ClientInfoTracker.LoadedZombies.put(message.getEntityID(), message);
    }

    @SideOnly(Side.CLIENT)
    public static MessageZombieVillagerProfession getZombieMessage(int entityID) {
        MessageZombieVillagerProfession msg = ClientInfoTracker.LoadedZombies.get(entityID);
        ClientInfoTracker.LoadedZombies.remove(entityID);         // removes from the list, it's not needed anymore
        return msg;
    }
    
    
    

    // Village Guard part
    
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, MessageVillageGuard> LoadedGuards = new HashMap<Integer, MessageVillageGuard>();
    
    
    @SideOnly(Side.CLIENT)
    public static void addGuardMessage(MessageVillageGuard message) {
        ClientInfoTracker.LoadedGuards.put(message.getEntityID(), message);
    }
    
    @SideOnly(Side.CLIENT)
    public static MessageVillageGuard getGuardMessage(int entityID) {
    	MessageVillageGuard msg = ClientInfoTracker.LoadedGuards.get(entityID);
        ClientInfoTracker.LoadedGuards.remove(entityID);         // removes from the list, it's not needed anymore
        return msg;
    }
    
    
    
    // Reintroduced and renovated in v3.1
    
    /**
     * Attempts to locate the entity by its ID and apply the 
     * extended properties.
     */
    
    @SideOnly(Side.CLIENT)
    public static void SyncZombieMessage(int entityID) {

        // Seeks if the entity ID is loaded
        WorldClient world = Minecraft.getMinecraft().world;
        if (world == null) return;
        Entity entity = world.getEntityByID(entityID);

        // If found the entity, attempt to sync with info sent by the server 
        if (entity instanceof EntityZombieVillager) {
            ClientInfoTracker.SyncZombieMessage((EntityZombieVillager)entity);
        }
    }
    
    
    
    /**
     * Attempts to locate a message with Extended Properties and apply to the zombie. 
     */
    // Reintroduced and renovated in v3.1
    @SideOnly(Side.CLIENT)
    public static void SyncZombieMessage(EntityZombieVillager zombievillager) {
        
        // Try to locate messages sent by the server, containing special zombie info
        MessageZombieVillagerProfession msg = ClientInfoTracker.getZombieMessage(zombievillager.getEntityId());
        
        // If a message was found, update the local zombie with that info
        if (msg != null) {
        	IModularSkin ims = zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
        	ims.setProfession(msg.getProfession());
        	ims.setCareer(msg.getCareer());
        	ims.setBiomeType(msg.getBiomeType()); // Added in v3.1
        	ims.setProfessionLevel(msg.getProfessionLevel()); // Added in v3.1
            ims.setSkinTone(msg.getSkinTone()); // Added in v3.2
        } 
    }
    
    
    
    
    /**
     * Attempts to locate the entity by its ID and apply the 
     * extended properties.
     */
    // Reintroduced and renovated in v3.1
    @SideOnly(Side.CLIENT)
    public static void SyncGuardMessage(int entityID) {

        // Seeks if the entity ID is loaded
        WorldClient world = Minecraft.getMinecraft().world;
        if (world == null) return;
        Entity entity = world.getEntityByID(entityID);
        
        // If found the entity, attempt to sync with info sent by the server 
        if ( entity.getClass().toString().substring(6).equals(ModObjects.WitcheryGuardClass) ) {
            ClientInfoTracker.SyncGuardMessage((EntityLiving)entity);
        }    
    }
    
    
    /**
     * Attempts to locate a message with Extended Properties and apply to the guard. 
     */
    // Reintroduced and renovated in v3.1
    @SideOnly(Side.CLIENT)
    public static void SyncGuardMessage(EntityLiving guard) {
        
        // Try to locate messages sent by the server, containing special zombie info
        MessageVillageGuard msg = ClientInfoTracker.getGuardMessage(guard.getEntityId());
        
        // If a message was found, update the local guard with that info
        if (msg != null) {
        	IModularSkin ims = guard.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
            //properties.setProfession(msg.getProfession());
            //properties.setCareer(msg.getCareer());
        } 
    }
    
    
    
    
    
    // Added in v3.1
    
    // --------------------------------- //
    // --- Modern Villager Skin part --- //
    // --------------------------------- //
    
    @SideOnly(Side.CLIENT)
    private static HashMap<Integer, MessageModernVillagerSkin> loadedVillagers = new HashMap<Integer, MessageModernVillagerSkin>();
    
    @SideOnly(Side.CLIENT)
    public static void addModernVillagerMessage(MessageModernVillagerSkin message) {
        ClientInfoTracker.loadedVillagers.put(message.getEntityID(), message);
    }
    @SideOnly(Side.CLIENT)
    public static MessageModernVillagerSkin getModernVillagerMessage(int entityID) {
    	MessageModernVillagerSkin msg = ClientInfoTracker.loadedVillagers.get(entityID);
        ClientInfoTracker.loadedVillagers.remove(entityID);         // removes from the list, it's not needed anymore
        return msg;
    }
    
    /**
     * Attempts to locate the entity by its ID and apply the 
     * extended properties.
     */
    @SideOnly(Side.CLIENT)
    public static void syncModernVillagerMessage(int entityID) {

        // Seeks if the entity ID is loaded
        WorldClient world = Minecraft.getMinecraft().world;
        if (world == null) return;
        Entity entity = world.getEntityByID(entityID);

        // If found the entity, attempt to sync with info sent by the server 
        if (
        		entity instanceof EntityVillager
        		&& ((EntityVillager)entity).getProfession() >=0
        		//&& ((EntityVillager)entity).getProfession() <=5 Removed in v3.2 to ensure syncing
        		) {
            ClientInfoTracker.syncModernVillagerMessage((EntityVillager)entity);
        }
        
    }

    /**
     * Attempts to locate a message with Extended Properties and apply to the villager. 
     */
    @SideOnly(Side.CLIENT)
    public static void syncModernVillagerMessage(EntityVillager villager) {
        
        // Try to locate messages sent by the server, containing special zombie info
        MessageModernVillagerSkin msg = ClientInfoTracker.getModernVillagerMessage(villager.getEntityId());
        
        // If a message was found, update the local zombie with that info
        if (msg != null) {
        	IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
        	ims.setProfession(msg.getProfession());
        	ims.setCareer(msg.getCareer());
        	ims.setBiomeType(msg.getBiomeType()); // Added in v3.1
        	ims.setProfessionLevel(msg.getProfessionLevel()); // Added in v3.1
        	ims.setSkinTone(msg.getSkinTone()); // Added in v3.2
        } 
    }
    
    
}