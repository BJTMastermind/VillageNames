package astrotibs.villagenames.handler;

import astrotibs.villagenames.tracker.ServerInfoTracker;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/handler/TickEventHandler.java
 * @author AstroTibs
 */
public class ServerCleanExpired
{

    private static final int tickFactor = 300;  // Frequency of update


    @SubscribeEvent
    public void onPostServerTick(TickEvent.ServerTickEvent event)
    {

        if (event.phase == Phase.END) {

            final MinecraftServer server = MinecraftServer.getServer();
            if (server.getTickCounter() % tickFactor == 0) {

                // Cleanup SpecialEventsTracker expired content
                ServerInfoTracker.cleanExpired();
            }
        }
    }

}
