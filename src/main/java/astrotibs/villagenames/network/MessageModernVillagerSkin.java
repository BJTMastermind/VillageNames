package astrotibs.villagenames.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/network/MessageZombieVillagerProfession.java
 * @author AstroTibs
 * 
 * Used to notify the client of the zombie villager profession, 
 * so it can render the correct skin.
 *
 */


public class MessageModernVillagerSkin implements IMessage
{
	// Constructors
	public MessageModernVillagerSkin() {}
    public MessageModernVillagerSkin(int entityID, int profession, int career, int biomeType, int professionLevel,
    		int professionLevelVN, int careerVN
    		, int skinTone
    		) {
        this.entityID = entityID;
        this.profession = profession;
        this.career = career;
        this.biomeType = biomeType;
        this.professionLevel = professionLevel;
        this.professionLevelVN = professionLevelVN;
        this.careerVN = careerVN;
        this.skinTone = skinTone;
    }

    // Fields to be used by this message
    private int entityID;
    private int profession;
    private int career;
    private int biomeType;
    private int professionLevel;
    private int professionLevelVN;
    private int careerVN;
    private int skinTone;


    // Getters
    public int getProfession() {return this.profession;}
    public int getCareer() {return this.career;}
    public int getEntityID() {return this.entityID;}
    public int getBiomeType() {return this.biomeType;}
    public int getProfessionLevel() {return this.professionLevel;}
    public int getProfessionLevelVN() {return this.professionLevelVN;}
    public int getCareerVN() {return this.careerVN;}
    public int getSkinTone() {return this.skinTone;}

    
    // Reads the packet
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityID = buf.readInt();
        this.profession = buf.readInt();
        this.career = buf.readInt();
        this.biomeType = buf.readInt();
        this.professionLevel = buf.readInt();
        this.professionLevelVN = buf.readInt();
        this.careerVN = buf.readInt();
        this.skinTone = buf.readInt();

        // note - maybe use ByteBufUtils
    }


    // Write the packet
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityID);
        buf.writeInt(this.profession);
        buf.writeInt(this.career);
        buf.writeInt(this.biomeType);
        buf.writeInt(this.professionLevel);
        buf.writeInt(this.professionLevelVN);
        buf.writeInt(this.careerVN);
        buf.writeInt(this.skinTone);

    }


    // Builds a string
    @Override 
    public String toString() {
        
    	StringBuilder r = new StringBuilder();
        
        r.append("Entity ID = ");
        r.append(this.getEntityID());
        r.append(", Profession = ");
        r.append(this.getProfession());
        r.append(", Career = ");
        r.append(this.getCareer());
        r.append(", BiomeType = ");
        r.append(this.getBiomeType());
        r.append(", Profession Level = ");
        r.append(this.getProfessionLevel());
        r.append(", Profession Level VN = ");
        r.append(this.getProfessionLevelVN());
        r.append(", Career VN = ");
        r.append(this.getCareerVN());
        r.append(", Skin Tone = ");
        r.append(this.getSkinTone());
        
        return r.toString();
    }
    
}
