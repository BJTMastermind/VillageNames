package astrotibs.villagenames.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;


/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/tracker/ServerInfoTracker.java
 * @author AstroTibs
 *
 * Helper class to store certain server-side information until they are needed or expire.
 * 
 * Used to integrate actions in multiple classes or provide the functionality of non-existing Forge Hooks.
 * 
 */

public class ServerInfoTracker
{

    public static enum EventType {
    	
        ZOMBIE,         // Zombie was about to be cured, used to transfer info to the new villager
        VILLAGER,       // Villager was killed by zombie, used to transfer info in case he is zombified
        GUARD;			// Used for Witchery town guards keeping their names
    	
        private List<EventTracker>        tracker                 = new ArrayList<EventTracker>();
        public List<EventTracker> getTracker() {
            return tracker;
        }
    }


    private static HashMap<Integer, Integer> CuredZombies                 = new HashMap<Integer, Integer>();    // Tracks which players cured zombies (used to reward achievements)
    private static int                       CuredZombiesListLastChange   = 0;                              // Tracks when the list of cured zombies was last updated
    private static HashMap<Integer, Integer> CuredVillagers               = new HashMap<Integer, Integer>();    // Tracks a villager just cured by a player
    private static int                       CuredVillagersListLastChange = 0;                              // Tracks when the list of cured villagers was last updated
    private static final int                 ListExpiration               = 12000;                          // Number of ticks the list will stay alive after the last update, 12000 = 10 minutes

    private static boolean                   canStartTracking             = false;


    /**
     * Adds villager information that should be tracked on the server.
     * Used to track villager that were killed by zombies.
     */
    public static void add(EntityVillager villager, World world)
    {
    	IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
        ServerInfoTracker.add(EventType.VILLAGER, new EventTracker(villager, ims), world);
    }
    
    /**
     * Adds zombie information that should be tracked on the server.
     * Used to track zombies villagers that were about to be cured.
     */
    public static void add(EntityZombieVillager zombievillager, World world)
    {
    	//TODO - IEEP stuff
    	IModularSkin ims = zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
        ServerInfoTracker.add(EventType.ZOMBIE, new EventTracker(zombievillager, ims), world);
    }


    public static void add(EntityLiving guard, World world)
    {
        ServerInfoTracker.add(EventType.GUARD, new EventTracker(guard), world);
    }
    
    public static void startedCuringZombie(int playerID, int zombieID, World world)
    {
        if (zombieID > 0 && playerID > 0) {
            if (GeneralConfig.debugMessages) {
                LogHelper.info("> Player [" + playerID + "] started to cure zombie [" + zombieID + "]");
            }
            ServerInfoTracker.CuredZombies.put(zombieID, playerID);
            ServerInfoTracker.CuredZombiesListLastChange = world.getMinecraftServer().getTickCounter();//MinecraftServer.getServer().getTickCounter();
        }
    }

    public static void endedCuringZombie(int oldZombieID, int newVillagerID, World world)
    {
        // checks if the zombie that was cured was one being tracked
        final Integer playerID = ServerInfoTracker.CuredZombies.get(oldZombieID);

        if (newVillagerID > 0 && playerID != null && playerID > 0) {
            if (GeneralConfig.debugMessages) {
                LogHelper.info("> Player [" + playerID + "] cured villager [" + newVillagerID + "], formerly known as zombie [" + oldZombieID + "]");
            }
            ServerInfoTracker.CuredVillagers.put(newVillagerID, playerID);
            ServerInfoTracker.CuredVillagersListLastChange = world.getMinecraftServer().getTickCounter();//MinecraftServer.getServer().getTickCounter();
        }
    }



    private static void add(EventType type, EventTracker event, World world)
    {
        // Don't track anything at tick zero, since that's when the world loads
        if (ServerInfoTracker.ThisTick(world) == 0) {
            return;
        }
        
        if (event == null) {
            return;
        }
        if (!canStartTracking) {
            return;
        }
        
        // Adds the "tick of birth" to control when the event will expire
        event.setBirthTick(
        		world.getMinecraftServer().getTickCounter()//MinecraftServer.getServer().getTickCounter()
        		);


        // Adds the event to the specific list
        type.getTracker().add(event);

    }



    public static EventTracker seek(EventType type, Vec3i position, World world)
    //public static EventTracker seek(EventType type, Vec3 position)
    {
    	
        int rangeLimit;             // how far the code will search for an entity.
        int ageTolerance;           // how old the entry can be considered valid.

        switch (type) {

            case VILLAGER:
                rangeLimit = 1;         // Seek at the exact spot (maybe should be zero?)
                ageTolerance = 5;       // Only 5 ticks tolerance to play safe, since "zombification" happens on the same tick
                break;

            case ZOMBIE:
                rangeLimit = 1;         // Seek at the exact spot (maybe should be zero?)
                ageTolerance = 3;       // Only 3 ticks tolerance to play safe, since the cure should happen on the next 1 tick
                break;
            
            case GUARD:                 // Custom type for Witchery
                rangeLimit = 1;         
                ageTolerance = 5;       
                break;
                
                
            default:
                return null;

        }

        return seekValueOnList(type.getTracker(), position, rangeLimit, ageTolerance, world);

    }


    private static EventTracker seekValueOnList(List<EventTracker> list, Vec3i position, int rangeLimit, int ageTolerance, World world)
    {
        final int thisTick = ServerInfoTracker.ThisTick(world);

        for (final EventTracker et : list) {
            if (et.getBirthTick() > 0 && et.getBirthTick() + ageTolerance >= thisTick
            		&& position.distanceSq(et.getPosition()) <= rangeLimit) {
            		
                if (GeneralConfig.debugMessages) {
                    LogHelper.info("ServerInfoTracker > found a valid target [" + et + "]");
                }

                et.expireNow();
                return et;
            }
        }

        return null;
    }



    public static void removeCuredZombiesFromTracker(World world, int zombieID)
    {
        final Integer playerID = ServerInfoTracker.CuredZombies.get(zombieID);

        if (playerID != null && playerID > 0) {
        	
            // Removes the tracking regardless of finding a player
            ServerInfoTracker.CuredZombies.remove(zombieID);
        }
    }


    
    public static void removeCuredVillagersFromTracker(World world, int villagerID)
    {
        final Integer playerID = ServerInfoTracker.CuredVillagers.get(villagerID);

        if (playerID != null && playerID > 0) {

            // Removes the tracking regardless of finding a player
            ServerInfoTracker.CuredVillagers.remove(villagerID);
        }
    }
	


    public static void cleanExpired(World world)
    {
        if (!ServerInfoTracker.canStartTracking) {
            return;
        }

        final int thisTick = ServerInfoTracker.ThisTick(world);


        // Clean expired from the events tracker
        final int expireAllUntil = thisTick - 100;     // Maximum amount of time an entry stays at the list

        cleanExpiredList(ServerInfoTracker.EventType.VILLAGER.getTracker(), expireAllUntil);
        cleanExpiredList(ServerInfoTracker.EventType.ZOMBIE.getTracker(), expireAllUntil);
        cleanExpiredList(ServerInfoTracker.EventType.GUARD.getTracker(), expireAllUntil);
        

        // Clear the cured zombie tracker
        if (CuredZombiesListLastChange + ListExpiration < thisTick
        		&& CuredZombies.size()>0) {
            
            ServerInfoTracker.CuredZombies.clear();
        }

        if (CuredVillagersListLastChange + ListExpiration < thisTick
        		&& CuredVillagers.size()>0) {
        	
            ServerInfoTracker.CuredVillagers.clear();
        }

    }

    private static void cleanExpiredList(List<EventTracker> list, int limit)
    {
        if (list.size() > 0) {
            final Iterator<EventTracker> i = list.iterator();

            while (i.hasNext()) {
                final EventTracker et = i.next();
                if (et.getBirthTick() < 0 || et.getBirthTick() < limit) {
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("removing: " + et);
                    }
                    i.remove();
                }
            }

        }
    }



    public static void startTracking()
    {
        
        canStartTracking = true;

        EventType.ZOMBIE.getTracker().clear();
        EventType.VILLAGER.getTracker().clear();
        EventType.GUARD.getTracker().clear();

        CuredZombies.clear();
        CuredVillagers.clear();
    }

    
    private static int ThisTick(World world) {
        return world.getMinecraftServer().getTickCounter();//MinecraftServer.getServer().getTickCounter();
    }
    


}
