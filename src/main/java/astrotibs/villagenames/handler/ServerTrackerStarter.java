package astrotibs.villagenames.handler;

import astrotibs.villagenames.tracker.ServerInfoTracker;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/handler/WorldEventHandler.java
 * @author AstroTibs
 */
public class ServerTrackerStarter
{

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (!event.world.isRemote) {
            ServerInfoTracker.startTracking();
        }
    }

}
