package astrotibs.villagenames.network;

import astrotibs.villagenames.utility.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHandler {
	private static SimpleNetworkWrapper INSTANCE;
	
    public static void init(){
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
	}
    
    public static void sendToServer(IMessage message){
        INSTANCE.sendToServer(message);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player){
        INSTANCE.sendTo(message, player);
    }
    
}
