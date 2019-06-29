package astrotibs.villagenames.handler;

import java.util.Iterator;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReputationHandler {
	
	private static final String repTagKey = "VNRep";
	
	// A second flag, UUIS_suffix, is used to determine whether VN rep or villageObj rep is the "correct" one.
	// This flag is wiped for every player in every village at every world load.
	private static final String repSuffix = "_rep";
	
	/**
	 * On loading a world, this listener will scan through all of the Village.dat entries
	 * and mark all the "VNCollected" tags as false, making the game think that nobody visited
	 * the village, so that the mod thinks that your VN reputation is the correct value.
	 * I have to do this because starting at 1.9, Minecraft has reputation bug where restarting
	 * the server will reset everyone's reputation everywhere to zero. Like in Fight Club.
	 */
	
	@SubscribeEvent
	public void repairerOfReputations(WorldEvent.Load event) {
		// Go through the list of villages
		World world = event.getWorld();
		
		try{
			// Load in the vanilla structure file
			MapGenStructureData structureData = (MapGenStructureData)world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Village");
			NBTTagCompound nbttagcompound = structureData.getTagCompound();
			
			// Iterate through the entries
			Iterator itr = nbttagcompound.getKeySet().iterator();
			
			while (itr.hasNext()) {
				Object element = itr.next();
				
				NBTBase nbtbase = nbttagcompound.getTag(element.toString());
				
				if (nbtbase.getId() == 10) { // Entry is an NBTTagCompound -- Likely a village entry.
					NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
					NBTTagCompound playerReps = new NBTTagCompound();
					
					if (nbttagcompound2.hasKey(repTagKey)) { // Village entry has stored info about VN reputations
		        		playerReps = (NBTTagCompound)(nbttagcompound2.getTag(repTagKey)); // Retrieve the entry that has all the player reps and cast it as a tag compound
					}
					
					Iterator repItr = playerReps.getKeySet().iterator();
					
					// Go through the list of recorded players
					while (repItr.hasNext()) {
						Object repElement = repItr.next();
						String playerUUID = repElement.toString();         // The UUID string of the player
						//String playerName = world.getPlayerEntityByName(playerUUID).getDisplayNameString();
						int VNRep = playerReps.getInteger(playerUUID); // The reputation of that player
						boolean VNCollected = playerReps.getBoolean(playerUUID+repSuffix); // The reputation of that player
						
						if (playerReps.hasKey(playerUUID+repSuffix)) {
							if (GeneralConfig.debugMessages) LogHelper.info("Resetting rep flag for player "+playerUUID+" in village "+element.toString());
							playerReps.setBoolean(playerUUID+repSuffix, false);
							structureData.markDirty();
						}
					}
				}
			}
			
		}
		catch (Exception e) {}
	}
	
	
    /**
     * Return the village reputation for a player
     * player: the EntityPlayerMP entity to check
     * villageTopTag: the village to check as given by the topmost entry in Village.dat (e.g. "[12,10]")
     * Village: the village that will be checked for a reputation value via villageCollectionObj
     */
    public static int getVNReputationForPlayer(EntityPlayerMP player, String villageTopTag, Village village)
    {
    	MapGenStructureData structureData = (MapGenStructureData)player.worldObj.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Village");
    	NBTTagCompound nbttagcompound = structureData.getTagCompound(); // Key entry list
    	NBTBase nbtbase = nbttagcompound.getTag( villageTopTag ); // Retrieve the specific village as indicated by villageTopTag
    	NBTTagCompound villageTag = (NBTTagCompound)nbtbase; // Cast this as a tag compound
    	NBTTagCompound playerReps = new NBTTagCompound();
    	int VNPlayerRep = 0; // Defaults to zero
    	boolean returnDefaultRep = false; // This is flagged as "true" if we're forced to return default reputation.
    	
    	// ------------------------------------------------- //
    	// --- VN reps recorded for at least one player! --- //
    	// ------------------------------------------------- //
    	
    	if (villageTag!=null) { // Otherwise, the game crashes when in an artificial village
    		
        	if (villageTag.hasKey(repTagKey)) {
        		playerReps = (NBTTagCompound)(villageTag.getTag(repTagKey)); // Retrieve the entry that has all the player reps and cast it as a tag compound
        		
        		// -------------------------------------------------- //
        		// --- We found an entry for the player's VN rep! --- //
        		// -------------------------------------------------- //
        		if (playerReps.hasKey(player.getUniqueID().toString())) {
        			
        			int PrevVNPlayerRep = playerReps.getInteger(player.getUniqueID().toString());
        			
        			if (village==null) { // There is no village in the collectionObj--presumably, it was annihilated. Return the VN rep.
        				VNPlayerRep = PrevVNPlayerRep;
        				//if (GeneralConfigHandler.debugMessages) LogHelper.info("This village was previously annihilated. Your reputation is now " + VNPlayerRep);
        			}
        			else { // There is a village in the collectionObj. Compare the VN reputation with its reputation.
        				int collectionRep = village.getPlayerReputation(player.getDisplayNameString());
        				if (PrevVNPlayerRep == collectionRep) { // They match.
        					// If it's zero, it's safe to assume that it's supposed to be zero.
        					VNPlayerRep = PrevVNPlayerRep; 
        					//if (GeneralConfigHandler.debugMessages) LogHelper.info("Your collection and VN reputation match: " + VNPlayerRep + " on tick " +player.worldObj.getMinecraftServer().getTickCounter());
        				}
        				else { // They do not match.
    						if (
    								//collectionRep != 0 || (
    								//villageTag.getBoolean(collectedKey)
    								playerReps.getBoolean(player.getUniqueID().toString()+repSuffix)
    								//&& player.ticksExisted > 0) // This will be zero if the player just signed in.
    								) { // This village was previously reported as "visited" by the player.
    							// Chances are, the VN version needs to be updated to the new collection version.
    							
    							if (GeneralConfig.debugMessages) LogHelper.info(
    									"There was a reputation mismatch. Player age is " + player.ticksExisted +
    									". We assume villageCollectionObj was correct: " + collectionRep +
    									" and not VN's: " + PrevVNPlayerRep);
    							
    							VNPlayerRep = collectionRep; 
    						}
    						else { // This village was previously reported as annihilated
    							// The village is no longer annihilated, so its reputation is going to return to zero.
    							
    							if (GeneralConfig.debugMessages) LogHelper.info(
    									"There was a reputation mismatch. We assume VN's was correct: " + PrevVNPlayerRep +
    									" and not villageCollectionObj's: " + collectionRep);
    							
    							// Set its reputation to whatever the VN version currently is.
    							village.modifyPlayerReputation(player.getDisplayNameString(), PrevVNPlayerRep-collectionRep);
    							VNPlayerRep = PrevVNPlayerRep; 
    						}
        				}
        			}
        		}
        		else {
            		returnDefaultRep = true; // Player's VN rep does not exist
        		}
        	}
        	else {
        		returnDefaultRep = true; // Nobody has VN rep in this village
        	}
    		
    		// ---------------------------------------------------------- //
    		// --- There is no VN reputation recorded for this player --- //
        	// --------- OR No VN reps recorded for any player! --------- //
    		// ---------------------------------------------------------- //
    		if (returnDefaultRep) {
    			// Check it against villageCollectionObj
    			if (village!=null) {
    				// Either someone else visited this village when it wasn't annihilated, 
    				// or this village has been brought back from annihilation,
    				// or it was not visited since VN3.
    				// In any case, the player has no recorded rep. There's nothing further we can do.
    				// Just take it from the collection.
    				VNPlayerRep = village.getPlayerReputation(player.getDisplayNameString());
    				if (GeneralConfig.debugMessages) LogHelper.info("You have no recorded VN rep in this town. It is now set to " + VNPlayerRep);
    			}
    		}
        	
        	villageTag.setTag(repTagKey, playerReps);
        	//villageTag.setBoolean(collectedKey, !(village==null));
    		playerReps.setInteger(player.getUniqueID().toString(), VNPlayerRep);
    		playerReps.setBoolean(player.getUniqueID().toString()+repSuffix, !(village==null)); // Also set a boolean, to be used with VN reputation cleanup
    		structureData.markDirty(); // You changed a value. You are a dirty boy.
        	
    		return VNPlayerRep;
    		
    	}
    	
    	else { // villageTag is null, so return 0 by default
    		
    		// It's possible you're dealing with an artificial village. Try getting the player rep.
    		
    		int nullVillageTagRep = 0;
    		
    		try {
    			nullVillageTagRep = village.getPlayerReputation(player.getDisplayNameString());
    		}
    		catch (Exception e) {} // Something went wrong. Probably because the village is null. Just leave it at zero.
    		
    		return nullVillageTagRep;
    	}
    	

    }
    
    
    /**
     * Set the village reputation for a player.
     */
	public static String getVillageTagPlayerIsIn(EntityPlayerMP player) {
		
		// First, check to see if the player is in a village bounding box as defined in Village.dat
		// If so, check to see if the player is also in a village as defined in villages.dat
		
    	MapGenStructureData structureData = (MapGenStructureData)player.worldObj.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Village");
		NBTTagCompound nbttagcompound = structureData.getTagCompound();
		
		Iterator itr = nbttagcompound.getKeySet().iterator();

		while (itr.hasNext()) {
			Object entry = itr.next();
			
			NBTBase nbtbase = nbttagcompound.getTag(entry.toString());
			
			if (nbtbase.getId() == 10) {
				NBTTagCompound villageTag = (NBTTagCompound)nbtbase;
				
				if (villageTag.getBoolean("Valid")) { // Was not generated as a junk entry
					int[] boundingBox = villageTag.getIntArray("BB");
					
					int posX = (int) player.posX;
					int posY = (int) player.posY;
					int posZ = (int) player.posZ;
					
					if (
							(
							   posX >= (boundingBox[0])
							&& posY >= (boundingBox[1])
							&& posZ >= (boundingBox[2])
							&& posX <= (boundingBox[3])
							&& posY <= (boundingBox[4])
							&& posZ <= (boundingBox[5])
							)) {
						// Player is inside the bounding box of this village
						
						return entry.toString();
					}
				}
			}
		}
		
		return "none"; // Player is not inside a village as defined by Village.dat
	}
	
}
