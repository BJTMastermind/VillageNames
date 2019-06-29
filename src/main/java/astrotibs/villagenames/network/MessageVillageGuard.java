package astrotibs.villagenames.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/network/MessageZombieVillagerProfession.java
 * @author AstroTibs
 */
public class MessageVillageGuard implements IMessage
{
    
	
    public MessageVillageGuard() {}

    public MessageVillageGuard(int entityID) {
        this.entityID = entityID;

    }

    
    
    private int entityID;

    
    public int getEntityID() {
        return this.entityID;
    }
    

    // Reads the packet
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityID = buf.readInt();

    }

    
    // Write the packet
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityID);

    }

    
    
    @Override 
    public String toString() {
        StringBuilder r = new StringBuilder();
        
        r.append("Entity ID = ");
        r.append(this.getEntityID());

        
        return r.toString();
    }
    
}
