package astrotibs.villagenames.handler;
import astrotibs.villagenames.config.GeneralConfigHandler;
import astrotibs.villagenames.config.OtherModConfigHandler;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.name.NamePieces;
import astrotibs.villagenames.nbt.VNWorldData;
import astrotibs.villagenames.nbt.VNWorldDataAbandonedBase;
import astrotibs.villagenames.nbt.VNWorldDataEndCity;
import astrotibs.villagenames.nbt.VNWorldDataEndIsland;
import astrotibs.villagenames.nbt.VNWorldDataEndTower;
import astrotibs.villagenames.nbt.VNWorldDataFortress;
import astrotibs.villagenames.nbt.VNWorldDataFronosVillage;
import astrotibs.villagenames.nbt.VNWorldDataKoentusVillage;
import astrotibs.villagenames.nbt.VNWorldDataMansion;
import astrotibs.villagenames.nbt.VNWorldDataMineshaft;
import astrotibs.villagenames.nbt.VNWorldDataMonument;
import astrotibs.villagenames.nbt.VNWorldDataMoonVillage;
import astrotibs.villagenames.nbt.VNWorldDataNibiruVillage;
import astrotibs.villagenames.nbt.VNWorldDataStronghold;
import astrotibs.villagenames.nbt.VNWorldDataTemple;
import astrotibs.villagenames.nbt.VNWorldDataVillage;
import astrotibs.villagenames.reference.Reference;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class ItemEventHandler {
	
	// All of this stuff is from sargeant / darthvader45 / whoever
	Random random = new Random();
	
	boolean nameVillagers = GeneralConfigHandler.nameVillagers;
	boolean villagerDropBook = GeneralConfigHandler.villagerDropBook;
	boolean addJobToName = GeneralConfigHandler.addJobToName;
	String nitwitProfession = GeneralConfigHandler.nitwitProfession;
	public static int[] otherModIDs = OtherModConfigHandler.otherModIDs;
	public static int[] vanillaProfMaps = OtherModConfigHandler.vanillaProfMaps;
	
	boolean babyMessageToggle = false;
	boolean outsiderMessageToggle = true;
	boolean crouchBookToggle = false;
	boolean nitwitBookToggle = false;
	
	// Mod name stuff
	public static String entityNameString;

	// Galacticraft names
	boolean nameGCAlienVillagers = OtherModConfigHandler.nameGCAlienVillagers;

	// More Planets names
	boolean nameMPKoentusVillagers = OtherModConfigHandler.nameMPKoentusVillagers;
	boolean nameMPFronosVillagers = OtherModConfigHandler.nameMPFronosVillagers;
	boolean nameMPNibiruVillagers = OtherModConfigHandler.nameMPNibiruVillagers;

	// Witchery names
	boolean nameWitcheryWitchHunter = OtherModConfigHandler.nameWitcheryWitchHunter;
	boolean nameWitcheryVampire = OtherModConfigHandler.nameWitcheryVampire;
	boolean nameWitcheryHobgoblins = OtherModConfigHandler.nameWitcheryHobgoblins;
	boolean nameWitcheryVillageGuards = OtherModConfigHandler.nameWitcheryVillageGuards;
	
	
	int villageRadiusBuffer = 32;
	
	// This switches over the search style from 1.8.9 to the new chunk style because of flaws in the search algorithm
	boolean useStructureChunkSearch = false;
	// This flags whether to calibrate official player rep with possibly bugged number
	boolean repairPlayerRep = true;
	
	
	// This event is used when the world is loaded, and sets all the reputation values to what they should be
	@SubscribeEvent
	public void headOffRepWiping(PlayerEvent.PlayerLoggedInEvent load) {
		World world = load.player.world;//.getWorld();
		if (!world.isRemote && repairPlayerRep) {
			
			int dx;
			int dy;
			int dz;
			int rsq;
			
			// Get list of official villages
			List vlist = world.getVillageCollection().getVillageList();
			Iterator vitr = vlist.iterator();

			// Get list of villagenames.dat villages
			VNWorldDataVillage data = VNWorldDataVillage.forWorld(world);
			NBTTagCompound tagCompound = data.getData();
			Set tagmapKeyset = tagCompound.getKeySet();
			Iterator itr = tagmapKeyset.iterator();
			
			// Iterate through villagenames.dat villages:
			while(itr.hasNext()) {
				Object VNVillage = itr.next();
				String townSignEntry = VNVillage.toString(); //Text name of village header (e.g. "x535y80z39")
				NBTTagCompound tagList = tagCompound.getTagList(townSignEntry, tagCompound.getId()).getCompoundTagAt(0);
				
				// Get the coordinates so you can compare it to vanilla villages
				int vnx = tagList.getInteger("signX");
				int vny = tagList.getInteger("signY");
				int vnz = tagList.getInteger("signZ");
				
				// Cycle through all the vanilla villages:
				while (vitr.hasNext()) {
					Village MCVillage = (Village)vitr.next();
					int vx = MCVillage.getCenter().getX();
					int vy = MCVillage.getCenter().getY();
					int vz = MCVillage.getCenter().getZ();
					int radius = MCVillage.getVillageRadius();
					
					// See if they match up
					dx = vx - vnx;
					dy = vy - vny;
					dz = vz - vnz;
					
					rsq = (dx*dx) + (dy*dy) + (dz*dz) ;
					
					if ( rsq <= ((radius+villageRadiusBuffer)*(radius+villageRadiusBuffer)) ) {
						// One has matched up. Transfer its reputation info.
						
						// Unpack the player reps:
						NBTTagCompound playerReps = tagList.getTagList("playerReps", tagList.getId()).getCompoundTagAt(0);
						
						Set repKeySet = playerReps.getKeySet();
						Iterator ritr = repKeySet.iterator();
						
						while(ritr.hasNext()) {
							Object repEntry = ritr.next();
							String playerName = repEntry.toString();
							int playerRep = playerReps.getInteger(playerName);
							int vanillaPlayerRep = MCVillage.getPlayerReputation(playerName);
							
							// Finally, FINALLY, update the "official" rep to reflect the backup rep:
							MCVillage.modifyPlayerReputation(playerName, playerRep-vanillaPlayerRep);
							
						}
						break;
					}
				}
			}
			
		}
	}
	
	
	
	// The below block is used to verify whether you've attacked a villager, and try to update reputation accordingly.
	@SubscribeEvent
	public void AttackVillager(AttackEntityEvent aEEvent) {
		if (repairPlayerRep && 
				aEEvent.getEntityPlayer() instanceof EntityPlayer
				) {
			EntityPlayer player = aEEvent.getEntityPlayer();
			if (
					!aEEvent.getEntityPlayer().world.isRemote &&
					aEEvent.getTarget() != null && aEEvent.getTarget() instanceof EntityVillager
					) {
				// cast the entity to type EntityVillager
				repairPlayerReputation( aEEvent.getEntityPlayer(), aEEvent.getTarget(), aEEvent.getEntityPlayer().getEntityWorld() );
			}
		}
	}
	
	
	@SubscribeEvent
	public void onEntityInteract(EntityInteract event) {
		
		if (event.getEntityPlayer() instanceof EntityPlayer
			&& !event.getEntityPlayer().world.isRemote) {
			EntityPlayer player = event.getEntityPlayer();
			ItemStack itemstack = player.inventory.getCurrentItem();
			
			//Use the below to figure out what you're looking at :3
			entityNameString = event.getTarget().getClass().toString();
			//entityNameString = entityNameString.substring(6, entityNameString.length());
			//player.sendMessage(new TextComponentString( "This is a "+event.getTarget().toString() +" right here." ) );
			//LogHelper.info("Interacted with a "+entityNameString);
			
			NBTTagCompound compound = new NBTTagCompound();
			String customName;
			int villagerAge;
			
			if (
					!event.getEntityPlayer().world.isRemote &&
					event.getTarget() != null && event.getTarget() instanceof EntityVillager
					) {
				
				// cast the entity to type EntityVillager
				EntityVillager villager = (EntityVillager)event.getTarget();
				
				if (repairPlayerRep) {repairPlayerReputation(event.getEntityPlayer(), event.getTarget(), event.getEntityPlayer().getEntityWorld() );}
				
				// and read its NBT data
				compound = new NBTTagCompound();
				villager.writeEntityToNBT(compound);
				customName = event.getTarget().getCustomNameTag();
				villagerAge = compound.getInteger("Age");
				int villagerProfession = compound.getInteger("Profession");
				int villagerCareer = compound.getInteger("Career");
				int tradeCount = (villager.getRecipes(event.getEntityPlayer())).size();
				
				/* 
				 * Now if this target is a Librarian and you're holding
				 * Emerald or gold ingot or iron ingot, there will be a special event.
				 */
				
				if (
						itemstack != null
						&& ( itemstack.getItem() == Items.EMERALD || itemstack.getItem() == Items.GOLD_INGOT || itemstack.getItem() == Items.IRON_INGOT )
						&& villagerProfession==1
						) {
					
					if ( villagerAge < 0 ) {
						// Villager is a baby so can't write you a book.
						babyMessageToggle = !babyMessageToggle;
	            		
	            		if (babyMessageToggle) {
	            			player.sendMessage(new TextComponentString( babyCantHelpString() ) );
	            		}
					}
					else if (!player.isSneaking()) {
						// Villager is an adult. Proceed.
						event.setCanceled(true);
						
						// These are the coordinates of the villager
	            		// used to determine which village center or name sign is closest
	            		double villagerX = event.getTarget().posX;
	            		double villagerY = event.getTarget().posY;
	            		double villagerZ = event.getTarget().posZ;
						
						// Figure out how large the village list is
	            		int radius = 0;
	            		//double distToVillage = 0;
	            		int popSize;
	            		
	            		// Finds the nearest village, but only if the given coordinates are within its bounding radius plus the buffer
	            		Village villageNearVillager = event.getEntityPlayer().world.getVillageCollection().getNearestVillage(new BlockPos(villagerX, villagerY, villagerZ), villageRadiusBuffer);//.findNearestVillage((int)villagerX, (int)villagerY, (int)villagerZ, villageRadiusBuffer);
	            		
	            		if (villageNearVillager != null && player.dimension==0) {
	            			int playerRep = villageNearVillager.getPlayerReputation(event.getEntityPlayer().getDisplayNameString());
	            			if (playerRep < 0) {
	            				// Your reputation is too low.
	            				player.sendMessage(new TextComponentString( "The villager does not trust you." ) );
	            			}
	            			else {
	            				// The villager trusts you.
	            				// Now we construct a codex based on material availability.
	            				
	            				int emeralds = 0;
	            				int ironIngots = 0;
	            				int goldIngots = 0;
	            				for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++)
	            				{
	            					ItemStack Stack = player.inventory.getStackInSlot(slot);

	            					if (Stack != null && Stack.getItem().equals(Items.EMERALD)) {emeralds += Stack.getCount();}
	            					if (Stack != null && Stack.getItem().equals(Items.IRON_INGOT)) {ironIngots += Stack.getCount();}
	            					if (Stack != null && Stack.getItem().equals(Items.GOLD_INGOT)) {goldIngots += Stack.getCount();}
	            					
	            				}
	            					            				
	            				/*
	            				 * We have the totals for emeralds and iron/gold ingots, and the player's rep.
	            				 * Now let's actually do the exchange.
	            				 */
	            				int emeraldReqiured = 4 - (playerRep+2)/5;
	            				int ironRequired = 8 - ((playerRep+1)*5)/12;
	            				int goldRequired = 4 - (playerRep+2)/5;
	            				
	            				if (itemstack.getItem() == Items.EMERALD) { // Trade with emeralds only
	            					if (emeralds >= emeraldReqiured+1) {
		            					event.getEntityPlayer().inventory.clearMatchingItems(Items.EMERALD, -1, emeraldReqiured+1, null);
		            					EntityItem eitem = (villagerDropBook ? event.getTarget() : event.getEntityPlayer()).entityDropItem(new ItemStack(ModItems.codex), 1);
		            			        eitem.setNoPickupDelay(); //No delay: directly into the inventory!
		            			        
		            			        // Increase the player's reputation for a successful trade
		            			        if(random.nextInt(2)==0) {villageNearVillager.modifyPlayerReputation(event.getEntityPlayer().getDisplayNameString(), 1);}
	            					}
	            					else {
	            						player.sendMessage(new TextComponentString( "Need more emeralds." ) );
	            					}
	            					
	            				}
	            				else if (itemstack.getItem() == Items.GOLD_INGOT) { // Trade with gold and emeralds
	            					if (emeralds >= emeraldReqiured) {
	            						if (goldIngots >= goldRequired) {
			            					event.getEntityPlayer().inventory.clearMatchingItems(Items.EMERALD, -1, emeraldReqiured, null);
			            					event.getEntityPlayer().inventory.clearMatchingItems(Items.GOLD_INGOT, -1, goldRequired, null);
			            					EntityItem eitem = (villagerDropBook ? event.getTarget() : event.getEntityPlayer()).entityDropItem(new ItemStack(ModItems.codex), 1);
			            			        eitem.setNoPickupDelay(); //No delay: directly into the inventory!
			            			        
			            			        // Increase the player's reputation for a successful trade
			            			        if(random.nextInt(2)==0) {villageNearVillager.modifyPlayerReputation(event.getEntityPlayer().getDisplayNameString(), 1);}
	            						}
	            						else {
	            							player.sendMessage(new TextComponentString( "Need more gold ingots." ) );
	            						}
	            						
	            					}
	            					else {
	            						if (goldIngots >= goldRequired) {
	            							player.sendMessage(new TextComponentString( "Need more emeralds." ) );
	            						}
	            						else {
	            							player.sendMessage(new TextComponentString( "Need more emeralds and gold ingots." ) );
	            						}
	            					}
	            					
	            				}
	            				else if (itemstack.getItem() == Items.IRON_INGOT) { // Trade with iron and emeralds
	            					if (emeralds >= emeraldReqiured) {
	            						if (ironIngots >= ironRequired) {
			            					event.getEntityPlayer().inventory.clearMatchingItems(Items.EMERALD, -1, emeraldReqiured, null);
			            					event.getEntityPlayer().inventory.clearMatchingItems(Items.IRON_INGOT, -1, ironRequired, null);
			            					EntityItem eitem = (villagerDropBook ? event.getTarget() : event.getEntityPlayer()).entityDropItem(new ItemStack(ModItems.codex), 1);
			            			        eitem.setNoPickupDelay(); //No delay: directly into the inventory!
			            			        
			            			        // Increase the player's reputation for a successful trade
			            			        if(random.nextInt(2)==0) {villageNearVillager.modifyPlayerReputation(event.getEntityPlayer().getDisplayNameString(), 1);}
	            						}
	            						else {
	            							player.sendMessage(new TextComponentString( "Need more iron ingots." ) );
	            						}
	            						
	            					}
	            					else {
	            						if (ironIngots >= ironRequired) {
	            							player.sendMessage(new TextComponentString( "Need more emeralds." ) );
	            						}
	            						else {
	            							player.sendMessage(new TextComponentString( "Need more emeralds and iron ingots." ) );
	            						}
	            					}
	            					
	            				}
	            				
	           				}
	            			
	            		}
	            		else {
	            			// No nearby villages found: let the user know that s/he can't get a Codex.
	            			String[] cantHelpArray = new String[]{
	            					"baffled",
	            					"befuddled",
	            					"confused",
	            					"nonplussed",
	            					"perplexed",
	            					"puzzled",
	            					"disoriented",
	            					"lost",
	            					"helpless",
	            					"uncomfortable"
	            				};
	            			String cantHelpAdjective = cantHelpArray[random.nextInt(cantHelpArray.length)];
	            			player.sendMessage(new TextComponentString( "The villager seems " + cantHelpAdjective + ", and glances around." ) );
	            		}
	            		
					}
					
				}
				
				/**
				 * End of trade circumvention bullshit. Thanks for playing.
				 */
				
				// I'm hoping what this does is prevent you from naming a villager with a name tag. We'll see.
				else if (
						nameVillagers && 
						itemstack != null
						&& itemstack.getItem() == Items.NAME_TAG
						&& itemstack.hasDisplayName()
						&& !itemstack.getDisplayName().equals(customName)//itemstack.hasDisplayName()
						&& !player.capabilities.isCreativeMode
						) {
					player.sendMessage(new TextComponentString("That's not this villager's name..."));
					event.setCanceled(true);
					if (!customName.equals("")){
						villager.setCustomNameTag(customName);
					}
					else {
						villager.setCustomNameTag("");
					}
				}
				// Now either create a Village Book if you're holding a normal book...
				else if (
						itemstack != null
						&& itemstack.getItem() == Items.BOOK
						) {
					
					nitwitBookToggle = !nitwitBookToggle;
					
					if ( villagerAge < 0 ) {
						// Villager is a baby so can't write you a book.
						babyMessageToggle = !babyMessageToggle;
	            		
	            		if (babyMessageToggle) {
	            			player.sendMessage(new TextComponentString( babyCantHelpString() ) );
	            		}
					}
					else if (villagerProfession!=5 || nitwitBookToggle) {
						
						// Villager is an adult. Proceed.
						event.setCanceled(true);
						
						// These are the coordinates of the villager
						// used to determine which village center or name sign is closest
						double villagerX = event.getTarget().posX;
						double villagerY = event.getTarget().posY;
						double villagerZ = event.getTarget().posZ;
						
						// Figure out how large the village list is
	            		int radius = 0;
	            		//double distToVillage = 0;
	            		int popSize;
	            		
	            		// Finds the nearest village, but only if the given coordinates are within its bounding radius plus the buffer
	            		Village villageNearVillager = event.getEntityPlayer().world.getVillageCollection().getNearestVillage(new BlockPos(villagerX, villagerY, villagerZ), villageRadiusBuffer);//.findNearestVillage((int)villagerX, (int)villagerY, (int)villagerZ, villageRadiusBuffer);
	            		
	            		if (villageNearVillager != null && player.dimension==0) {
	            			int playerRep = villageNearVillager.getPlayerReputation(event.getEntityPlayer().getUniqueID() );
	            			
	            			if (playerRep < 0) {
	            				// Your reputation is too low.
	            				player.sendMessage(new TextComponentString( "The villager does not trust you." ) );
	            			}
	            			else {
	            				// The villager trusts you.
	            				
	            				radius = villageNearVillager.getVillageRadius();
		            			popSize = villageNearVillager.getNumVillagers();
		            			int CX = villageNearVillager.getCenter().getX(); // Village X position
		            			int CY = villageNearVillager.getCenter().getY(); // Village Y position
		            			int CZ = villageNearVillager.getCenter().getZ(); // Village Z position
		            			
		            			// Now that the villager is determined to be within a most likely village,
		            			// Let's see if we can find a sign near that located village center!
		            			
		            			//System.out.println("Event Handler Initialized"); //Print to server console
		    					VNWorldDataVillage data = VNWorldDataVillage.forWorld(event.getWorld()); //event.entity.world
		    					
		    					// .getTagList() will return all the entries under the specific village name.
		    					NBTTagCompound tagCompound = data.getData();
		    					
		    					Set tagmapKeyset = tagCompound.getKeySet(); //Gets the town key list: "coordinates"
		    			        
		    					
		    			        Iterator itr = tagmapKeyset.iterator();
		    			        String townSignEntry;
		    			        
		    			        //Placeholders for villagenames.dat tags
		    			        boolean signLocated = false; //Use this to record whether or not a sign was found
		    			        
		    			        while(itr.hasNext()) {
		    			            Object element = itr.next();
		    			            
		    			            townSignEntry = element.toString(); //Text name of village header (e.g. "x535y80z39")
		    			            //The only index that has data is 0:
		    			            NBTTagCompound tagList = tagCompound.getTagList(townSignEntry, tagCompound.getId()).getCompoundTagAt(0);
		    			            
		    			            int townX = tagList.getInteger("signX");
		    			            int townY = tagList.getInteger("signY");
		    			            int townZ = tagList.getInteger("signZ");
		    			            
		    			            
		    			            Village villageNearSign = event.getEntityPlayer().world.getVillageCollection().getNearestVillage(new BlockPos(townX, townY, townZ), villageRadiusBuffer);//.findNearestVillage((int)townX, (int)townY, (int)townZ, villageRadiusBuffer);
		    			            
		    			            if (villageNearSign == villageNearVillager) {
		    			            	signLocated = true;
		    			            	if (!event.getEntityPlayer().world.isRemote) {
		    			            		
		    			            		if (!player.isSneaking()) {crouchBookToggle = true;}
		    			            		else {crouchBookToggle = !crouchBookToggle;}
		    			            		
		    			            		if (crouchBookToggle) {
		    			            			//event refers to the interaction with the villager
		    			            			//tagList is the villagename data
		    			            			//villageNearVillager is the corresponding vanilla data
		    			            			
		    			            			writeNewVillageBook(event, tagList, villageNearVillager, villagerProfession, villagerCareer);
		    			            		}
		    			            	
		    			            	}
		    			            	break;
		    			            }
		    			            
		    			        }
		    			        if (!signLocated) {
		    			        	// No well sign was found that matched the villager's village.
		    			        	// We can assume this is a village WITHOUT a sign. So let's at least give it a name!
		    			        	
		    			        	String[] newVillageName = NameGenerator.newVillageName();
	                        		String headerTags = newVillageName[0];
	                        		String namePrefix = newVillageName[1];
	                        		String nameRoot = newVillageName[2];
	                        		String nameSuffix = newVillageName[3];
	                        		
	                        		int[] codeColor_a = NamePieces.codeColors_default;
	                        		int townColorMeta = codeColor_a[random.nextInt(codeColor_a.length)];
	                        		// Allow a chance for uncommon colors to be used
	                        		if (townColorMeta==-1) {
	                        			int[] codeColors_remaining_a = NamePieces.codeColors_remaining;
	                        			townColorMeta = codeColors_remaining_a[random.nextInt(codeColors_remaining_a.length)];
	                        		}
	                        		
	                        		// Make the data bundle to save to NBT
	                        		NBTTagList nbttaglist = new NBTTagList();
	                        		
	                        		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	                                nbttagcompound1.setInteger("signX", CX);
	                                nbttagcompound1.setInteger("signY", CY);
	                                nbttagcompound1.setInteger("signZ", CZ);
	                                nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
	                                nbttagcompound1.setString("namePrefix", namePrefix);
	                                nbttagcompound1.setString("nameRoot", nameRoot);
	                                nbttagcompound1.setString("nameSuffix", nameSuffix);
	                                nbttaglist.appendTag(nbttagcompound1);
	                                // Save the data under a "Villages" entry with unique name based on sign coords
	                                data.getData().setTag("x"+CX+"y"+CY+"z"+CZ+"nosign", nbttaglist);
	                                
	                                data.markDirty();
	                        		
	                                signLocated = false;
	                                
	                                //event refers to the interaction with the villager
				            		//tagList is the villagename data
				            		//villageNearVillager is the corresponding vanilla data
				            		writeNewVillageBook(event, nbttagcompound1, villageNearVillager, villagerProfession, villagerCareer);
		    			        	
		    			        }
	            				
	            			}
	            			
	            			
	            		}
	            		else {
	            			if (outsiderMessageToggle && (villagerProfession!=5 || nitwitBookToggle) ) {
	            				String[] cantHelpArray = new String[]{
		            					"baffled",
		            					"befuddled",
		            					"bewildered",
		            					"clueless",
		            					"confused",
		            					"dumbfounded",
		            					"mystified",
		            					"nonplussed",
		            					"perplexed",
		            					"puzzled",
		            				};
		            			String cantHelpAdjective = cantHelpArray[random.nextInt(cantHelpArray.length)];
		            			player.sendMessage(new TextComponentString( "The villager gives you a " + cantHelpAdjective + " look." ) );
	            			}
	            			// No nearby villages found: let the user know that s/he can't get a Village Book.
	            			// Just to randomize the response the villager gives you if he can't locate a village ;)
	            		}
	            		
					}
					
				}
				
				// ...or NAME the villager if he's not named. Open the GUI too.
				else if ( nameVillagers && !player.isSneaking() ) {
					
					if (customName == "") { // Empty name field is represented by "", and not: null
						// Name the villager
						String nameRoot = NameGenerator.newVillagerName();
						if (addJobToName) { // Add job tag in parentheses after the name
							String careerTag = NameGenerator.getCareerTag(villagerProfession, villagerCareer, nitwitProfession);
							if (careerTag.length()>2) { // If the parentheses aren't empty, display them
								nameRoot += " "+careerTag;
							}
						}
						villager.setCustomNameTag(nameRoot);
					}
					else { // Villager already has a name. You may want to add (or remove) a career tag.
						if (customName.indexOf("(")==-1 && addJobToName) { // Villager is named but does not have job tag: add one!
							String careerTag = NameGenerator.getCareerTag(villagerProfession, villagerCareer, nitwitProfession);
							if (careerTag.length()>2) { // If the parentheses aren't empty, display them
								String nameRoot = customName+" "+careerTag;
								villager.setCustomNameTag(nameRoot);
							}
						}
						else if (customName.indexOf("(")!=-1 && !addJobToName) { // Villager has a job tag: remove it...
							String nameRoot = customName.substring(0, customName.indexOf("(")-1);
							villager.setCustomNameTag(nameRoot);
						}
					}
				}
			}
			
			else if (event.getTarget() instanceof EntityLiving) {
				
				// cast the entity to type EntityLiving
				EntityLiving target = (EntityLiving)event.getTarget();
				// and read its NBT data
				compound = new NBTTagCompound();
				target.writeEntityToNBT(compound);
				customName = target.getCustomNameTag();
				villagerAge = compound.getInteger("Age");
				int villagerProfession = compound.getInteger("Profession");
				int villagerCareer = compound.getInteger("Career");

				//LogHelper.info("Age: "+villagerAge+", Profession: "+villagerProfession+", Career: "+villagerCareer);
				
				// Disable name tags for these special entities
				if (
						itemstack != null
						&& itemstack.getItem() == Items.NAME_TAG
						&& itemstack.hasDisplayName()
						//&& player.isSneaking()
						&& !player.capabilities.isCreativeMode
						&& (
							   (nameGCAlienVillagers && entityNameString.equals(Reference.GCAlienVillagerClass))
							   || (nameMPKoentusVillagers && (entityNameString.equals(Reference.MPKoentusVillagerClass) || entityNameString.equals(Reference.MPKoentusVillagerClassModern)) )
							   || (nameMPFronosVillagers && (entityNameString.equals(Reference.MPFronosVillagerClass) || entityNameString.equals(Reference.MPFronosVillagerClassModern)))
							   || (nameMPNibiruVillagers && (entityNameString.equals(Reference.MPNibiruVillagerClass) || entityNameString.equals(Reference.MPNibiruVillagerClassModern)))
							   || (nameWitcheryVillageGuards && entityNameString.equals(Reference.WitcheryGuardClass))
							   || (nameWitcheryVampire && entityNameString.equals(Reference.WitcheryVampireClass))
							   || (nameWitcheryHobgoblins && entityNameString.equals(Reference.WitcheryHobgoblinClass))
							   || (nameWitcheryWitchHunter && entityNameString.equals(Reference.WitcheryHunterClass))
							)
						&& !event.getEntityPlayer().world.isRemote
						) {
					player.sendMessage(new TextComponentString("That name is just an epithet."));
					target.setCustomNameTag(customName);
					event.setCanceled(true);
				}
				
				// Name entities that would draw from same pool (village guard, witch hunter, vampire)
				if (
						(
							(nameWitcheryVillageGuards && entityNameString.equals(Reference.WitcheryGuardClass))
							|| (nameWitcheryWitchHunter && entityNameString.equals(Reference.WitcheryHunterClass))
							|| (nameWitcheryVampire && entityNameString.equals(Reference.WitcheryVampireClass))
							)
						&& !player.isSneaking()
						) {
					
					if ( customName.equals("") ) {
						// Name the entity
						String nameRoot = NameGenerator.newVillagerName();
						if (addJobToName) { // Add job tag in parentheses after the name
							if (entityNameString.equals(Reference.WitcheryGuardClass)) {nameRoot += " (Guard)";}
							else if (entityNameString.equals(Reference.WitcheryHunterClass)) {nameRoot += " (Witch Hunter)";}
						}
						target.setCustomNameTag(nameRoot);
					}
					else { // Entity already has a name. You may want to add (or remove) a career tag.
						if (customName.indexOf("(")==-1 && addJobToName) { // Entity is named but does not have job tag: add one!
							String careerTag;
							if (entityNameString.equals(Reference.WitcheryGuardClass)) {
								String nameRoot = customName += " (Guard)";
								target.setCustomNameTag(nameRoot);
								}
							else if (entityNameString.equals(Reference.WitcheryHunterClass)) {
								String nameRoot = customName += " (Witch Hunter)";
								target.setCustomNameTag(nameRoot);
								}
						}
						else if (customName.indexOf("(")!=-1 && !addJobToName) { // Entity has a job tag: remove it...
							String nameRoot = customName.substring(0, customName.indexOf("(")-1);
							target.setCustomNameTag(nameRoot);
						}
					}
				}
				
				// Create Alien Village book
				else if (
							(
								entityNameString.equals(Reference.GCAlienVillagerClass)
								|| ( entityNameString.equals(Reference.MPKoentusVillagerClass) || entityNameString.equals(Reference.MPKoentusVillagerClassModern) )
								|| ( entityNameString.equals(Reference.MPFronosVillagerClass) || entityNameString.equals(Reference.MPFronosVillagerClassModern) )
								|| ( entityNameString.equals(Reference.MPNibiruVillagerClass) || entityNameString.equals(Reference.MPNibiruVillagerClassModern) )
							)
						&& itemstack != null
						&& itemstack.getItem() == Items.BOOK
						) {
					if ( villagerAge < 0 ) {
						// Villager is a baby so can't write you a book.
						babyMessageToggle = !babyMessageToggle;
	            		
	            		if (babyMessageToggle) {
	            			player.sendMessage(new TextComponentString( babyCantHelpString() ) );
	            		}
					}
					else {
						
						// Villager is an adult. Proceed.
						event.setCanceled(true);
						
						// Figure out how large the village list is
						int radius = 0;
						//double distToVillage = 0;
						int popSize;
						
						// These are the coordinates of the villager
						// used to determine which village center or name sign is closest
						double villagerX = event.getTarget().posX;
						double villagerY = event.getTarget().posY;
						double villagerZ = event.getTarget().posZ;
						
						String s="";
						
						if (entityNameString.equals(Reference.GCAlienVillagerClass)) {s = "MoonVillage";}
						if (entityNameString.equals(Reference.MPKoentusVillagerClass) || entityNameString.equals(Reference.MPKoentusVillagerClassModern)) {s = "KoentusVillage";}
						if (entityNameString.equals(Reference.MPFronosVillagerClass) || entityNameString.equals(Reference.MPFronosVillagerClassModern)) {s = "FronosVillage";}
						if (entityNameString.equals(Reference.MPNibiruVillagerClass) || entityNameString.equals(Reference.MPNibiruVillagerClassModern)) {s = "NibiruVillage";}
						
						MapGenStructureData structureData;
						World worldIn = player.world;
						int[ ] BB = new int[6];
						boolean targetIsInsideAlienVillage=false;
						
						try {
							structureData = (MapGenStructureData)worldIn.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, s);
							NBTTagCompound nbttagcompound = structureData.getTagCompound();
							
							Iterator itr = nbttagcompound.getKeySet().iterator();
							
							while (itr.hasNext()) {
								Object element = itr.next();
								
								NBTBase nbtbase = nbttagcompound.getTag(element.toString());
								
								if (nbtbase.getId() == 10) {
									NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
									
									try {
										int[] boundingBox = nbttagcompound2.getIntArray("BB");
										// Now check to see if the target is inside the feature
										if (
											   villagerX >= boundingBox[0]
											&& villagerY >= boundingBox[1]
											&& villagerZ >= boundingBox[2]
											&& villagerX <= boundingBox[3]
											&& villagerY <= boundingBox[4]
											&& villagerZ <= boundingBox[5]
											) {
											
											// Target is inside bounding box.
											
											targetIsInsideAlienVillage = true;
											
											int ChunkX = nbttagcompound2.getInteger("ChunkX");
											int ChunkZ = nbttagcompound2.getInteger("ChunkZ");
											
											String structureName;
											String[] structureInfoArray = tryGetStructureInfo(s, boundingBox, worldIn);
											
											String namePrefix = structureInfoArray[0];
											String nameRoot = structureInfoArray[1];
											String nameSuffix = structureInfoArray[2];
											
											int signX; int signY; int signZ;
											
											// If none is found, these strings are "null" which parseInt does not like very much
											try {signX = Integer.parseInt(structureInfoArray[3]);} catch (Exception e) {}
											try {signY = Integer.parseInt(structureInfoArray[4]);} catch (Exception e) {}
											try {signZ = Integer.parseInt(structureInfoArray[5]);} catch (Exception e) {}
											
											// If a name was NOT returned, then we need to generate a new one, as is done below:
											
											int[] structureCoords = new int[] {
													(boundingBox[0]+boundingBox[3])/2,
													(boundingBox[1]+boundingBox[4])/2,
													(boundingBox[2]+boundingBox[5])/2,
													};
											
											if (structureInfoArray[0]==null && structureInfoArray[1]==null && structureInfoArray[2]==null) {
												//Structure has no name. Generate it here.
												
												if (s.equals("MoonVillage")) {
													VNWorldDataMoonVillage data = VNWorldDataMoonVillage.forWorld(target.world);
													structureInfoArray = NameGenerator.newAlienVillageName();
													
													// Gotta copy this thing to each IF condition I think
													String headerTags = structureInfoArray[0];
													namePrefix = structureInfoArray[1];
													nameRoot = structureInfoArray[2];
													nameSuffix = structureInfoArray[3];
													int townColorMeta = 15;
													
													// Make the data bundle to save to NBT
													NBTTagList nbttaglist = new NBTTagList();
													
													NBTTagCompound nbttagcompound1 = new NBTTagCompound();
													signX = structureCoords[0];
													signY = structureCoords[1];
													signZ = structureCoords[2];
													nbttagcompound1.setInteger("signX", signX);
													nbttagcompound1.setInteger("signY", signY);
													nbttagcompound1.setInteger("signZ", signZ);
													nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
													nbttagcompound1.setString("namePrefix", namePrefix);
													nbttagcompound1.setString("nameRoot", nameRoot);
													nbttagcompound1.setString("nameSuffix", nameSuffix);
													nbttaglist.appendTag(nbttagcompound1);
													
													// .getTagList() will return all the entries under the specific village name.
													NBTTagCompound tagCompound = data.getData();
													
													data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
													data.markDirty();
													
												}
												else if (s.equals("KoentusVillage")) {
													VNWorldDataKoentusVillage data = VNWorldDataKoentusVillage.forWorld(target.world);
													structureInfoArray = NameGenerator.newAlienVillageName();
													
													// Gotta copy this thing to each IF condition I think
													String headerTags = structureInfoArray[0];
													namePrefix = structureInfoArray[1];
													nameRoot = structureInfoArray[2];
													nameSuffix = structureInfoArray[3];
													int townColorMeta = 15;
													
													// Make the data bundle to save to NBT
													NBTTagList nbttaglist = new NBTTagList();
													
													NBTTagCompound nbttagcompound1 = new NBTTagCompound();
													signX = structureCoords[0];
													signY = structureCoords[1];
													signZ = structureCoords[2];
													nbttagcompound1.setInteger("signX", signX);
													nbttagcompound1.setInteger("signY", signY);
													nbttagcompound1.setInteger("signZ", signZ);
													nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
													nbttagcompound1.setString("namePrefix", namePrefix);
													nbttagcompound1.setString("nameRoot", nameRoot);
													nbttagcompound1.setString("nameSuffix", nameSuffix);
													nbttaglist.appendTag(nbttagcompound1);
													
													// .getTagList() will return all the entries under the specific village name.
													NBTTagCompound tagCompound = data.getData();
													
													data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
													data.markDirty();
												}
												else if (s.equals("FronosVillage")) {
													VNWorldDataFronosVillage data = VNWorldDataFronosVillage.forWorld(target.world);
													structureInfoArray = NameGenerator.newAlienVillageName();
													
													// Gotta copy this thing to each IF condition I think
													String headerTags = structureInfoArray[0];
													namePrefix = structureInfoArray[1];
													nameRoot = structureInfoArray[2];
													nameSuffix = structureInfoArray[3];
													int townColorMeta = 15;
													
													// Make the data bundle to save to NBT
													NBTTagList nbttaglist = new NBTTagList();
													
													NBTTagCompound nbttagcompound1 = new NBTTagCompound();
													signX = structureCoords[0];
													signY = structureCoords[1];
													signZ = structureCoords[2];
													nbttagcompound1.setInteger("signX", signX);
													nbttagcompound1.setInteger("signY", signY);
													nbttagcompound1.setInteger("signZ", signZ);
													nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
													nbttagcompound1.setString("namePrefix", namePrefix);
													nbttagcompound1.setString("nameRoot", nameRoot);
													nbttagcompound1.setString("nameSuffix", nameSuffix);
													nbttaglist.appendTag(nbttagcompound1);
													
													// .getTagList() will return all the entries under the specific village name.
													NBTTagCompound tagCompound = data.getData();
													
													data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
													data.markDirty();
												}
												else if (s.equals("NibiruVillage")) {
													VNWorldDataNibiruVillage data = VNWorldDataNibiruVillage.forWorld(target.world);
													structureInfoArray = NameGenerator.newAlienVillageName();
													
													// Gotta copy this thing to each IF condition I think
													String headerTags = structureInfoArray[0];
													namePrefix = structureInfoArray[1];
													nameRoot = structureInfoArray[2];
													nameSuffix = structureInfoArray[3];
													int townColorMeta = 15;
													
													// Make the data bundle to save to NBT
													NBTTagList nbttaglist = new NBTTagList();
													
													NBTTagCompound nbttagcompound1 = new NBTTagCompound();
													signX = structureCoords[0];
													signY = structureCoords[1];
													signZ = structureCoords[2];
													nbttagcompound1.setInteger("signX", signX);
													nbttagcompound1.setInteger("signY", signY);
													nbttagcompound1.setInteger("signZ", signZ);
													nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
													nbttagcompound1.setString("namePrefix", namePrefix);
													nbttagcompound1.setString("nameRoot", nameRoot);
													nbttagcompound1.setString("nameSuffix", nameSuffix);
													nbttaglist.appendTag(nbttagcompound1);
													
													// .getTagList() will return all the entries under the specific village name.
													NBTTagCompound tagCompound = data.getData();
													
													data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
													data.markDirty();
												}
												
												structureName = structureInfoArray[1]+" "+structureInfoArray[2]+" "+structureInfoArray[3];
												structureName = structureName.trim();
												
											}
											else {
												//Structure has a name. Unpack it here.
												structureName = structureInfoArray[0]+" "+structureInfoArray[1]+" "+structureInfoArray[2];
												structureName = structureName.trim();
											}
											
											signX = structureCoords[0];
											signY = structureCoords[1];
											signZ = structureCoords[2];
											
											// Actually form the book contents and write the book
											
											//Here are the contents of the book up front
											//String bookContents = "\n\u00a7l";
											String bookContents = "{\"text\":\""; //As of 1.9.4 I need to enclose book contents in {"text":"lorem ipsum"}
											bookContents += "\n\u00a7r\u00a70";
											
											// I don't care if the structure has a sign. We have to cut the name up into arbitrary sign strings for the book.
											String topLine;
											String structureType = "";
											if (entityNameString.equals(Reference.GCAlienVillagerClass)) {structureType = "MoonVillage";}
											else if (entityNameString.equals(Reference.MPKoentusVillagerClass) || entityNameString.equals(Reference.MPKoentusVillagerClassModern)) {structureType = "KoentusVillage";}
											else if (entityNameString.equals(Reference.MPFronosVillagerClass) || entityNameString.equals(Reference.MPFronosVillagerClassModern)) {structureType = "FronosVillage";}
											else if (entityNameString.equals(Reference.MPNibiruVillagerClass) || entityNameString.equals(Reference.MPNibiruVillagerClassModern)) {structureType = "NibiruVillage";}
											
											// These lines split top lines into two words
											if (structureType.equals("MoonVillage")) {topLine = "Moon Village:";}
											else if (structureType.equals("KoentusVillage")) {topLine = "Koentus Village:";}
											else if (structureType.equals("FronosVillage")) {topLine = "Fronos Village:";}
											else if (structureType.equals("NibiruVillage")) {topLine = "Nibiru Village:";}
											else {topLine = structureType+":";}
											
											String sign0 = new String();
											String sign1 = new String();
											String sign2 = new String();
											String sign3 = new String();
											
											String headerTags = GeneralConfigHandler.headerTags;
											
											if ( (namePrefix.length() + 1 + nameRoot.length()) > 15 ) {
												// Prefix+Root is too long, so move prefix to line 1
												sign0 = headerTags+ topLine.trim();
												sign1 = namePrefix.trim();
												if ( (nameRoot.length() + 1 + nameSuffix.length()) > 15 ) {
													// Root+Suffix is too long, so move suffix to line 3
													sign2 = nameRoot.trim();
													sign3 = nameSuffix.trim();
												}
												else {
													// Fit Root+Suffix onto line 2
													sign2 = (nameRoot+" "+nameSuffix).trim();
												}
											}
											else if ( (namePrefix.length() + 1 + nameRoot.length() + 1 + nameSuffix.length()) <= 15 ) {
												// Whole name fits on one line! Put it all on line 2.
												sign1 = headerTags+ topLine;
												sign2 = (namePrefix+" "+nameRoot+" "+nameSuffix).trim();
											}
											else {
												// Only Prefix and Root can fit together on line 2.
												sign1 = headerTags+ topLine.trim();
												sign2 = (namePrefix+" "+nameRoot).trim();
												sign3 = nameSuffix.trim();
											}
										
											// Add name of town
											bookContents += "\u00a7r"+topLine;
											
											if (sign0.length()==0) {
												bookContents += "\n" + "\u00a7l"+sign2;
											}
											else {
												bookContents += "\u00a7l"+sign1 + "\n" + "\u00a7l"+sign2;
											}
											bookContents +=
													"\n" + "\u00a7l"+sign3 + 
													"\n\n" +
													"\u00a7rLocated at:\n"+
													"\u00a7rx = \u00a7l"+signX+"\u00a7r\ny = \u00a7l"+signY+"\u00a7r\nz = \u00a7l"+signZ;
											
											// These lines clarify when a feature is not on the Overworld
											if (player.dimension==-1) {
												bookContents +=
														"\n" +
														"\u00a7r(Nether)\n";
											}
											else if (player.dimension==1) {
												bookContents +=
														"\n" +
														"\u00a7r(End dimension)\n";
											}
											else if (structureType.equals("MoonVillage")) {
												bookContents +=
														"\n" +
														"\u00a7r(Moon)\n";
											}
											else if (structureType.equals("KoentusVillage")) {
												bookContents +=
														"\n" +
														"\u00a7r(Koentus)\n";
											}
											else if (structureType.equals("FronosVillage")) {
												bookContents +=
														"\n" +
														"\u00a7r(Fronos)\n";
											}
											else if (structureType.equals("NibiruVillage")) {
												bookContents +=
														"\n" +
														"\u00a7r(Nibiru)\n";
											}
											bookContents += "\"}"; //As of 1.9.4 I need to enclose book contents in {"text":"lorem ipsum"}
											
											List<String> pages = new ArrayList<String>();
											
											ItemStack book = new ItemStack(ModItems.moonvillagebook);
											
											// Change the icon up depending on surroundings
											if (structureType.equals("MoonVillage")) {book = new ItemStack(ModItems.moonvillagebook);}
											else if (structureType.equals("KoentusVillage")) {book = new ItemStack(ModItems.koentusvillagebook);}
											else if (structureType.equals("FronosVillage")) {book = new ItemStack(ModItems.fronosvillagebook);}
											else if (structureType.equals("NibiruVillage")) {book = new ItemStack(ModItems.nibiruvillagebook);}
											
											if (book.getTagCompound() == null) {
												book.setTagCompound(new NBTTagCompound());
											}
											
											String nameCompound = namePrefix + " " +  nameRoot + " " + nameSuffix;
											String authorName = customName;
											
											// Set the title
											book.getTagCompound().setString("title", nameCompound.trim() );
											// Set the author
											//book.getTagCompound().setString("author", authorName );
											boolean villagerCustomNameVisible = compound.getBoolean("CustomNameVisible");
											if ( customName!=null && !customName.equals("") ) {
												book.getTagCompound().setString("author", customName.indexOf("(")!=-1 ? customName.substring(0, customName.indexOf("(")-1) : customName );
											}
											else {
												book.getTagCompound().setString("author", "" );
											}
											
											// Set the book's contents
											NBTTagList pagesTag = new NBTTagList();
											
											// Page 1, with the feature information
											pagesTag.appendTag(new NBTTagString(bookContents));
											
											book.getTagCompound().setTag("pages", pagesTag);
											
											// Don't make a book if the sign coords are all -1.
											
											if (signX!=-1 && signY!=-1 && signZ!=-1) {
												// Consume Book
												player.inventory.clearMatchingItems(Items.BOOK, -1, 1, null);
												
												// Give the book to the player
												EntityItem eitem = (player).entityDropItem(book, 1);
												eitem.setNoPickupDelay(); //No delay: directly into the inventory!
											}
											
											
											
										}
									
									}
									catch (Exception e) {
										// There's a tag like [23,-3] (chunk location) but there's no bounding box tag.
									}
									
								}
								
							}
						}
						catch (Exception e) {
							LogHelper.info("Failed to make a village book");
						}
						
						
						
						if (!targetIsInsideAlienVillage) {
							// No nearby alien villages found: let the user know that s/he can't get a Village Book.
							// Just to randomize the response the villager gives you if he can't locate a village ;)
							String[] cantHelpArray = new String[]{
									"baffled",
									"befuddled",
									"bewildered",
									"clueless",
									"confused",
									"dumbfounded",
									"mystified",
									"nonplussed",
									"perplexed",
									"puzzled",
								};
							String cantHelpAdjective = cantHelpArray[random.nextInt(cantHelpArray.length)];
							player.sendMessage(new TextComponentString( "The villager gives you a " + cantHelpAdjective + " look." ) );
						}
						
					}
				}
				// Alien Villager name pool
				else if (
							(
								(nameGCAlienVillagers && entityNameString.equals(Reference.GCAlienVillagerClass))
								|| (nameMPKoentusVillagers && (entityNameString.equals(Reference.MPKoentusVillagerClass) || entityNameString.equals(Reference.MPKoentusVillagerClassModern)) )
							)
						&& !player.isSneaking()
						) {
					if ( customName.equals("") ) {
						// Name the entity
						String nameRoot = NameGenerator.newAlienVillagerName();
						target.setCustomNameTag(nameRoot);
					}
				}
				// Hobgoblin name pool
				else if (
							(
								(nameMPFronosVillagers && (entityNameString.equals(Reference.MPFronosVillagerClass) || entityNameString.equals(Reference.MPFronosVillagerClassModern)) )
								|| (nameWitcheryHobgoblins && entityNameString.equals(Reference.WitcheryHobgoblinClass))
							)
						&& !player.isSneaking()
						) {
					if ( customName.equals("") ) {
						// Name the entity
						String nameRoot = NameGenerator.newHobgoblinName();
						target.setCustomNameTag(nameRoot);
					}
				}
				// Nibiru villagers can have a profession tag
				else if ( 
						(nameMPNibiruVillagers && (entityNameString.equals(Reference.MPNibiruVillagerClass) || entityNameString.equals(Reference.MPNibiruVillagerClassModern) ))
						&& !player.isSneaking()
						) {
					if (customName == "") { // Empty name field is represented by "", and not: null
						// Name the villager
						String nameRoot = NameGenerator.newHobgoblinName();
						if (addJobToName) { // Add job tag in parentheses after the name
							String careerTag = NameGenerator.getNibiruCareerTag(villagerProfession, villagerCareer);
							if (careerTag.length()>2) { // If the parentheses aren't empty, display them
								nameRoot += " "+careerTag;
							}
						}
						target.setCustomNameTag(nameRoot);
					}
					else { // Villager already has a name. You may want to add (or remove) a career tag.
						if (customName.indexOf("(")==-1 && addJobToName) { // Villager is named but does not have job tag: add one!
							String careerTag = NameGenerator.getNibiruCareerTag(villagerProfession, villagerCareer);
							if (careerTag.length()>2) { // If the parentheses aren't empty, display them
								String nameRoot = customName+" "+careerTag;
								target.setCustomNameTag(nameRoot);
							}
						}
						else if (customName.indexOf("(")!=-1 && !addJobToName) { // Villager has a job tag: remove it...
							String nameRoot = customName.substring(0, customName.indexOf("(")-1);
							target.setCustomNameTag(nameRoot);
						}
					}
				}
			}
			
		}
	}
	
	// This method consumes a player's book and gives them a village book.
	private void writeNewVillageBook(EntityInteract event, NBTTagCompound tagList, Village villageNearVillager, int villagerProfession, int villagerCareer) {
		
		String sign0 = tagList.getString("sign0");
        String sign1 = tagList.getString("sign1");
        String sign2 = tagList.getString("sign2");
        String sign3 = tagList.getString("sign3");
        
        int radius = villageNearVillager.getVillageRadius();
		int popSize = villageNearVillager.getNumVillagers();
		int CX = villageNearVillager.getCenter().getX(); // Village X position
		int CY = villageNearVillager.getCenter().getY(); // Village Y position
		int CZ = villageNearVillager.getCenter().getZ(); // Village Z position
		
		int townX = tagList.getInteger("signX");
        int townY = tagList.getInteger("signY");
        int townZ = tagList.getInteger("signZ");
        int townColor = tagList.getInteger("townColor");
        String namePrefix = tagList.getString("namePrefix");
        String nameRoot = tagList.getString("nameRoot");
        String nameSuffix = tagList.getString("nameSuffix");
        String headerTags = GeneralConfigHandler.headerTags;
        String topLine = "Welcome to";
        
        String nameCompound = namePrefix + " " +  nameRoot + " " + nameSuffix;
		
        // cast the entity to type EntityVillager
		EntityVillager villager = (EntityVillager)event.getTarget();
		
		// and read its NBT data
		NBTTagCompound compound = new NBTTagCompound();
		villager.writeEntityToNBT(compound);
		String customName = event.getTarget().getName();
		int villagerAge = compound.getInteger("Age");
        
		//Here are the contents of the book up front
		String bookContents = "{\"text\":\""; //As of 1.9.4 I need to enclose book contents in {"text":"lorem ipsum"}
		bookContents += "\n\u00a7r\u00a70";
        if ( (sign0+sign1+sign2+sign3).length()==0) {
        	
        	// This town has no sign. We have to cut the name up into arbitrary sign strings.
        	if ( (namePrefix.length() + 1 + nameRoot.length()) > 15 ) {
        		// Prefix+Root is too long, so move prefix to line 1
        		sign0 = headerTags+ topLine.trim();
        		sign1 = namePrefix.trim();
        		if ( (nameRoot.length() + 1 + nameSuffix.length()) > 15 ) {
        			// Root+Suffix is too long, so move suffix to line 3
        			sign2 = nameRoot.trim();
        			sign3 = nameSuffix.trim();
        		}
        		else {
        			// Fit Root+Suffix onto line 2
        			sign2 = (nameRoot+" "+nameSuffix).trim();
        		}
        	}
        	else if ( (namePrefix.length() + 1 + nameRoot.length() + 1 + nameSuffix.length()) <= 15 ) {
        		// Whole name fits on one line! Put it all on line 2.
        		sign1 = headerTags+ topLine;
        		sign2 = (namePrefix+" "+nameRoot+" "+nameSuffix).trim();
        	}
        	else {
        		// Only Prefix and Root can fit together on line 2.
        		sign1 = headerTags+ topLine.trim();
        		sign2 = (namePrefix+" "+nameRoot).trim();
        		sign3 = nameSuffix.trim();
        	}
        }
        
        // Add name of town, but avoid the "welcome to" line
        
        if ( sign0.length() == 0 || sign0.equals("\u00a7r") ) {
        	bookContents += "\u00a7r\u00a70\u00a7l"+sign2;
        }
        else {
        	bookContents += "\u00a7r\u00a70\u00a7l"+sign1 + "\n" + "\u00a7r\u00a70\u00a7l"+sign2;
        }
        bookContents +=
        		"\n" + "\u00a7r\u00a70\u00a7l"+sign3 + 
        		"\n\n" +
        		"\u00a7r\u00a70Located at:\n"+
        		"\u00a7r\u00a70x = \u00a7l"+CX+"\u00a7r\ny = \u00a7l"+CY+"\u00a7r\nz = \u00a7l"+CZ+
        		"\n\n" +
        		"\u00a7r\u00a70Population: \u00a7l" + popSize + "\n" +
        		"\u00a7r\u00a70Radius: \u00a7l" + radius +
        		"\"}"; //As of 1.9.4 I need to enclose book contents in {"text":"lorem ipsum"}
        List<String> pages = new ArrayList<String>();
        
		ItemStack book = new ItemStack(ModItems.villagebook);//ModItems.villageBook);
		
		if (book.getTagCompound() == null) {
            book.setTagCompound(new NBTTagCompound());
        }
        
		book.getTagCompound().setString("title", nameCompound.trim() ); // Set the title
		// Set the author
		boolean villagerCustomNameVisible = compound.getBoolean("CustomNameVisible");
		if ( customName!=null && !customName.equals("") ) {
			book.getTagCompound().setString("author", customName.indexOf("(")!=-1 ? customName.substring(0, customName.indexOf("(")-1) : customName );
		}
		else {
			book.getTagCompound().setString("author", "" );
		}
        
		// Set the book's contents
		NBTTagList pagesTag = new NBTTagList();
		
		// Page 1, with the town information
		pagesTag.appendTag(new NBTTagString(bookContents));


		/**
		 *  All the machinery to make a second page should only work if the villager is named.
		 *  That makes the villager more "personable."
		 */

		if ( (customName != "" && customName != null) || !nameVillagers ) {
			double radiusCoef = 64.0f; // Feature search radius is playerRep x tradeCount x radiusCoef
			double strongholdCoefSquared = 0.5f; // Multiplier to reduce chances of locating a stronghold
			double nitwitCoef = 2.5f; // Multiplier
			String structureFeature = "";
			int playerRep = villageNearVillager.getPlayerReputation(event.getEntityPlayer().getUniqueID() );
			int tradeCount = (villager.getRecipes(event.getEntityPlayer())).size();
			double maxStructureDistance = playerRep * Math.sqrt(tradeCount/2.0d+1) * radiusCoef; // Maximum radius villager is allowed to report a structure
			//maxStructureDistance = Double.MAX_VALUE; // Guarantees search result
			
			// Page 2 will be information about nearby features
			double dx;
			double dy;
			double dz;
			double mineshaftDistSq = 0;
			double strongholdDistSq = 0;
			double templeDistSq = 0;
			double villageDistSq = 0;
			double monumentDistSq = 0;
			double mansionDistSq = 0;
			int[] nearestMineshaftXYZ;
			int[] nearestStrongholdXYZ;
			int[] nearestTempleXYZ;
			int[] nearestVillageXYZ;// = new int[3];
			int[] nearestMonumentXYZ;
			int[] nearestMansionXYZ;
			
			double[] villagerCoords = {event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ};
			double[] villageCoords = {villageNearVillager.getCenter().getX(), villageNearVillager.getCenter().getY(), villageNearVillager.getCenter().getZ()};
			
			EntityPlayer playerIn = event.getEntityPlayer();
			World worldIn = playerIn.world;
			BlockPos enderEyeBlockPos = ((WorldServer)worldIn).getChunkProvider().getNearestStructurePos(worldIn, "Stronghold", new BlockPos(playerIn), false);
			
			
			String closestStructure = "";
			int[] closestCoords = new int[3];

			// Convert a non-vanilla profession into a vanilla one for the purposes of generating a hint page
			int villagerMappedProfession = 0; //On a failure, direct it to "Farmer"
			try {villagerMappedProfession = (villagerProfession >= 0 && villagerProfession <= 5) ? villagerProfession : vanillaProfMaps[indexOfIntArr(otherModIDs, villagerProfession)];}
			catch (Exception e){}
			
			switch (villagerMappedProfession) {
				case 0: // Villager is a Farmer
					if (villagerCareer == 2) {
						// Villager is a fisherman. Find an ocean monument.
						if (!useStructureChunkSearch) {
							nearestMonumentXYZ = nearestStructureLoc("Monument", event);
						}
						else {
							nearestMonumentXYZ = getStructureFromChunk("Monument", event);
						}
						dx = nearestMonumentXYZ[0] - villagerCoords[0];
						dy = nearestMonumentXYZ[1] - villagerCoords[1];
						dz = nearestMonumentXYZ[2] - villagerCoords[2];
						
						if (
								!(nearestMonumentXYZ[0] == 0 &&
								nearestMonumentXYZ[1] == 0 &&
								nearestMonumentXYZ[2] == 0) &&
								(dx*dx) + (dy*dy) + (dz*dz) <= maxStructureDistance*maxStructureDistance
								) {
							// Monument found. Write a page about it.
							closestStructure = "Monument";
							closestCoords = nearestMonumentXYZ;
						}
					}
					else {
						// Villager is another kind of farmer. Find another village.
						// FIND NEXT NEAREST VILLAGE: THIS WILL BE AWESOME ;____;
						
						// Let's try using the 1.11.2 stronghold locater command.
						if (!useStructureChunkSearch) {
							nearestVillageXYZ = nearestStructureLoc("Village", event);
						}
						else {
							nearestVillageXYZ = getStructureFromChunk("Village", event);
						}
						
						dx = nearestVillageXYZ[0] - villagerCoords[0];
						dy = nearestVillageXYZ[1] - villagerCoords[1];
						dz = nearestVillageXYZ[2] - villagerCoords[2];
						
						if (
								!(nearestVillageXYZ[0] == 0 &&
									nearestVillageXYZ[1] == 0 &&
									nearestVillageXYZ[2] == 0) &&
								(dx*dx) + (dy*dy) + (dz*dz) <= maxStructureDistance*maxStructureDistance
								) {
							// Village found. Write a page about it.
							closestStructure = "Village";
							closestCoords = nearestVillageXYZ;
						}
					}
					
					break;
					
				case 1: // Villager is a Librarian. Find a Stronghold or a Woodland Mansion.
					if (!useStructureChunkSearch) {
						nearestStrongholdXYZ = nearestStructureLoc("Stronghold", event);
					}
					else {
						nearestStrongholdXYZ = nearestStructureLoc("Stronghold", event);
					}
					
					dx = nearestStrongholdXYZ[0] - villagerCoords[0];
					dy = nearestStrongholdXYZ[1] - villagerCoords[1];
					dz = nearestStrongholdXYZ[2] - villagerCoords[2];
					
					if (
							!(
									nearestStrongholdXYZ[0] == 0 &&
									nearestStrongholdXYZ[1] == 0 &&
									nearestStrongholdXYZ[2] == 0) &&
							(dx*dx) + (dy*dy) + (dz*dz) <= maxStructureDistance*maxStructureDistance*strongholdCoefSquared
							) {
						// Stronghold found.
						strongholdDistSq = (dx*dx) + (dy*dy) + (dz*dz);
					}
					
					if (!useStructureChunkSearch) {
						nearestMansionXYZ = nearestStructureLoc("Mansion", event);
					}
					else {
						nearestMansionXYZ = getStructureFromChunk("Mansion", event);
					}
					dx = nearestMansionXYZ[0] - villagerCoords[0];
					dy = nearestMansionXYZ[1] - villagerCoords[1];
					dz = nearestMansionXYZ[2] - villagerCoords[2];
					
					if (
							!(
									nearestMansionXYZ[0] == 0 &&
									nearestMansionXYZ[1] == 0 &&
									nearestMansionXYZ[2] == 0) &&
							(dx*dx) + (dy*dy) + (dz*dz) <= maxStructureDistance*maxStructureDistance
							) {
						// Mansion found.
						mansionDistSq = (dx*dx) + (dy*dy) + (dz*dz);
					}
					
					if (strongholdDistSq==0 && mansionDistSq ==0) {
						// Nothing legal has been detected.
					}
					else if (strongholdDistSq==0 || (strongholdDistSq >= mansionDistSq && mansionDistSq!=0) ) {
						// Only a Mansion has been legally detected. Report that.
						closestStructure = "Mansion";
						closestCoords = nearestMansionXYZ;
					}
					else if (mansionDistSq==0 || (strongholdDistSq < mansionDistSq && strongholdDistSq!=0) ) {
						// Only a Stronghold has been legally detected. Report that.
						closestStructure = "Stronghold";
						closestCoords = nearestStrongholdXYZ;
					}
					else {
						// I can't think of any other legal cases.
					}
					break;
					
				case 2: // Villager is a Priest. Find a temple.
					
					if (!useStructureChunkSearch) {
						nearestTempleXYZ = nearestStructureLoc("Temple", event);
					}
					else {
						nearestTempleXYZ = getStructureFromChunk("Temple", event);
					}
					dx = nearestTempleXYZ[0] - villagerCoords[0];
					dy = nearestTempleXYZ[1] - villagerCoords[1];
					dz = nearestTempleXYZ[2] - villagerCoords[2];
					
					if (
							!(
									nearestTempleXYZ[0] == 0 &&
									nearestTempleXYZ[1] == 0 &&
									nearestTempleXYZ[2] == 0) &&
							(dx*dx) + (dy*dy) + (dz*dz) <= maxStructureDistance*maxStructureDistance
							) {
						// Temple found. Write a page about it.
						closestStructure = "Temple";
						closestCoords = nearestTempleXYZ;
					}
					break;
					
				case 3: // Villager is a Blacksmith. Find a mineshaft.
					
					if (!useStructureChunkSearch) {
						nearestMineshaftXYZ = nearestStructureLoc("Mineshaft", event);
					}
					else {
						nearestMineshaftXYZ = getStructureFromChunk("Mineshaft", event);
					}
					dx = nearestMineshaftXYZ[0] - villagerCoords[0];
					dy = nearestMineshaftXYZ[1] - villagerCoords[1];
					dz = nearestMineshaftXYZ[2] - villagerCoords[2];
					
					if (
							!(
									nearestMineshaftXYZ[0] == 0 &&
									nearestMineshaftXYZ[1] == 0 &&
									nearestMineshaftXYZ[2] == 0) &&
							(dx*dx) + (dy*dy) + (dz*dz) <= maxStructureDistance*maxStructureDistance
							) {
						// Mineshaft found. Write a page about it.
						closestStructure = "Mineshaft";
						closestCoords = nearestMineshaftXYZ;
					}
					break;
					
				case 4: // Villager is a Butcher. Find a temple or a village.
					if (!useStructureChunkSearch) {
						nearestTempleXYZ = nearestStructureLoc("Temple", event);
					}
					else {
						nearestTempleXYZ = getStructureFromChunk("Temple", event);
					}
					dx = nearestTempleXYZ[0] - villagerCoords[0];
					dy = nearestTempleXYZ[1] - villagerCoords[1];
					dz = nearestTempleXYZ[2] - villagerCoords[2];
					
					if (
							!(
									nearestTempleXYZ[0] == 0 &&
									nearestTempleXYZ[1] == 0 &&
									nearestTempleXYZ[2] == 0) &&
							(dx*dx) + (dy*dy) + (dz*dz) <= maxStructureDistance*maxStructureDistance
							) {
						// Temple found.
						templeDistSq = (dx*dx) + (dy*dy) + (dz*dz);
					}
					
					
					if (!useStructureChunkSearch) {
						nearestVillageXYZ = nearestStructureLoc("Village", event);
					}
					else {
						nearestVillageXYZ = getStructureFromChunk("Village", event);
					}
					
					
					dx = nearestVillageXYZ[0] - villagerCoords[0];
					dy = nearestVillageXYZ[1] - villagerCoords[1];
					dz = nearestVillageXYZ[2] - villagerCoords[2];
					
					if (
							!(nearestVillageXYZ[0] == 0 &&
								nearestVillageXYZ[1] == 0 &&
								nearestVillageXYZ[2] == 0) &&
							(dx*dx) + (dy*dy) + (dz*dz) <= maxStructureDistance*maxStructureDistance
							) {
						// Village found.
						villageDistSq = (dx*dx) + (dy*dy) + (dz*dz);
					}
					
					if (templeDistSq==0 && villageDistSq ==0) {
						// Nothing legal has been detected.
					}
					else if (templeDistSq==0 || (templeDistSq >= villageDistSq && villageDistSq!=0) ) {
						// Only a Village has been legally detected. Report that.
						closestStructure = "Village";
						closestCoords = nearestVillageXYZ;
					}
					else if (villageDistSq==0 || (templeDistSq < villageDistSq && templeDistSq!=0) ) {
						// Only a Temple has been legally detected. Report that.
						closestStructure = "Temple";
						closestCoords = nearestTempleXYZ;
					}
					else {
						// I can't think of any other legal cases.
					}
					break;
					
				case 5: // Villager is a Nitwit
					// No structure planned at this time
					double nitwitRadius = playerRep * radiusCoef * nitwitCoef;
					//nitwitRadius = Double.MAX_VALUE; // Guarantees search result
					double nitwitMax = Double.MAX_VALUE;
					
					//Check village radius
					
					// Let's try using the 1.11.2 stronghold locater command.
					if (!useStructureChunkSearch) {
						nearestVillageXYZ = nearestStructureLoc("Village", event);
					}
					else {
						nearestVillageXYZ = getStructureFromChunk("Village", event);
					}
					dx = nearestVillageXYZ[0] - villagerCoords[0];
					dy = nearestVillageXYZ[1] - villagerCoords[1];
					dz = nearestVillageXYZ[2] - villagerCoords[2];
					
					if ( 
							!(nearestVillageXYZ[0]==0 && nearestVillageXYZ[1]==0 && nearestVillageXYZ[2]==0)
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitRadius*nitwitRadius )
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitMax )
							) {
						// Village may be the closest structure.
						nitwitMax = (dx*dx)+(dy*dy)+(dz*dz);
						closestStructure = "Village";
						closestCoords = nearestVillageXYZ;
					}
					
					// Check Mineshaft radius
					if (!useStructureChunkSearch) {
						nearestMineshaftXYZ = nearestStructureLoc("Mineshaft", event);
					}
					else {
						nearestMineshaftXYZ = getStructureFromChunk("Mineshaft", event);
					}
					dx = nearestMineshaftXYZ[0] - villagerCoords[0];
					dy = nearestMineshaftXYZ[1] - villagerCoords[1];
					dz = nearestMineshaftXYZ[2] - villagerCoords[2];
					
					if ( 
							!(nearestMineshaftXYZ[0]==0 && nearestMineshaftXYZ[1]==0 && nearestMineshaftXYZ[2]==0)
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitRadius*nitwitRadius )
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitMax )
							) {
						// Mineshaft may be the closest structure.
						nitwitMax = (dx*dx)+(dy*dy)+(dz*dz);
						closestStructure = "Mineshaft";
						closestCoords = nearestMineshaftXYZ;
					}
					
					// Check Stronghold radius
					if (!useStructureChunkSearch) {
						nearestStrongholdXYZ = nearestStructureLoc("Stronghold", event);
					}
					else {
						nearestStrongholdXYZ = nearestStructureLoc("Stronghold", event);
					}
					dx = nearestStrongholdXYZ[0] - villagerCoords[0];
					dy = nearestStrongholdXYZ[1] - villagerCoords[1];
					dz = nearestStrongholdXYZ[2] - villagerCoords[2];
					
					if ( 
							!(nearestStrongholdXYZ[0]==0 && nearestStrongholdXYZ[1]==0 && nearestStrongholdXYZ[2]==0)
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitRadius*nitwitRadius*strongholdCoefSquared )
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitMax )
							) {
						// Stronghold may be the closest structure.
						nitwitMax = (dx*dx)+(dy*dy)+(dz*dz);
						closestStructure = "Stronghold";
						closestCoords = nearestStrongholdXYZ;
					}
					
					// Check Temple radius
					if (!useStructureChunkSearch) {
						nearestTempleXYZ = nearestStructureLoc("Temple", event);
					}
					else {
						nearestTempleXYZ = getStructureFromChunk("Temple", event);
					}
					dx = nearestTempleXYZ[0] - villagerCoords[0];
					dy = nearestTempleXYZ[1] - villagerCoords[1];
					dz = nearestTempleXYZ[2] - villagerCoords[2];
					
					if ( 
							!(nearestTempleXYZ[0]==0 && nearestTempleXYZ[1]==0 && nearestTempleXYZ[2]==0)
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitRadius*nitwitRadius )
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitMax )
							) {
						// Temple may be the closest structure.
						nitwitMax = (dx*dx)+(dy*dy)+(dz*dz);
						closestStructure = "Temple";
						closestCoords = nearestTempleXYZ;
					}
					
					// Check Monument radius
					if (!useStructureChunkSearch) {
						nearestMonumentXYZ = nearestStructureLoc("Monument", event);
					}
					else {
						nearestMonumentXYZ = getStructureFromChunk("Monument", event);
					}
					dx = nearestMonumentXYZ[0] - villagerCoords[0];
					dy = nearestMonumentXYZ[1] - villagerCoords[1];
					dz = nearestMonumentXYZ[2] - villagerCoords[2];
					
					if ( 
							!(nearestMonumentXYZ[0]==0 && nearestMonumentXYZ[1]==0 && nearestMonumentXYZ[2]==0)
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitRadius*nitwitRadius )
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitMax )
							) {
						// Monument may be the closest structure.
						nitwitMax = (dx*dx)+(dy*dy)+(dz*dz);
						closestStructure = "Monument";
						closestCoords = nearestMonumentXYZ;
					}
					
					// Check Mansion radius
					if (!useStructureChunkSearch) {
						nearestMansionXYZ = nearestStructureLoc("Mansion", event);
					}
					else {
						nearestMansionXYZ = getStructureFromChunk("Mansion", event);
					}
					dx = nearestMansionXYZ[0] - villagerCoords[0];
					dy = nearestMansionXYZ[1] - villagerCoords[1];
					dz = nearestMansionXYZ[2] - villagerCoords[2];
					
					if ( 
							!(nearestMansionXYZ[0]==0 && nearestMansionXYZ[1]==0 && nearestMansionXYZ[2]==0)
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitRadius*nitwitRadius )
							&& ( (dx*dx)+(dy*dy)+(dz*dz) <= nitwitMax )
							) {
						// Mansion may be the closest structure.
						nitwitMax = (dx*dx)+(dy*dy)+(dz*dz);
						closestStructure = "Mansion";
						closestCoords = nearestMansionXYZ;
					}
					break;
			}
			
			if (!closestStructure.equals("")) {
				//As of 1.9.4 I need to enclose book contents in {"text":"lorem ipsum"}
				String structureHintPageText = "{\"text\":\"" + "\n\n" + writeStructureHintPage(closestStructure, closestCoords, villagerProfession, villageCoords, radius, event ) + "\"}";
				
				pagesTag.appendTag(new NBTTagString(structureHintPageText));
			}
			
			
		}
		//String testString = "lel LEL lel LEL ";
		//pagesTag.appendTag(new NBTTagString("La la la-la la-la herp derp derpadee "+testString+testString+testString+testString+testString));
		
		book.getTagCompound().setTag("pages", pagesTag);

		// Consume the book held by the player
		event.getEntityPlayer().inventory.clearMatchingItems(Items.BOOK, -1, 1, null);
		// Give the book to the player
		EntityItem eitem = (villagerDropBook ? event.getTarget() : event.getEntityPlayer()).entityDropItem(book, 1);
		eitem.setNoPickupDelay(); //No delay: directly into the inventory!
	}
	
	
	/**
	 * Used to find the nearest structure to the target entity by structure name. Returns [0,0,0] if none found.
	 * You can also enter an X and Z position offset for the search. Useful for villages.
	 */
	private int[] nearestStructureLoc(String structureName, EntityInteract event, double xOffset, double zOffset){
		
		boolean getStrongholdGenBool = false; // When true, this means "don't include the feature you're IN." Useful for Villages.
		
		int[] structurePos = new int[3];
		
		// Ignore all the crap below and use the stronghold generator
		
		// This partiucular block came into 1.11.2 already commented out
				
		if (structureName.equals("Village")) {
			// This SHOULD find the nearest village that isn't this one...
			getStrongholdGenBool = true;
		}
		
		BlockPos pos = ((WorldServer)event.getEntityPlayer().world).getChunkProvider().getNearestStructurePos(event.getEntityPlayer().world, structureName, new BlockPos(event.getEntityPlayer()), getStrongholdGenBool);
		
		structurePos[0] = pos.getX();
		structurePos[1] = pos.getY();
		structurePos[2] = pos.getZ();
		
		return structurePos;
	}

	/**
	 * Used to find the nearest structure to the target entity by structure name. Returns [0,0,0] if none found.
	 */
	private int[] nearestStructureLoc(String structureName, EntityInteract event){
		return nearestStructureLoc(structureName, event, 0.0f, 0.0f);
	}

	/**
	 * This method generates the additional page about a nearby structure
	 */
	private String writeStructureHintPage(String nearbyStructure, int[] structureCoords, int villagerProfession, double[] villageCoords, int villageRadius, EntityInteract event ) {
		String structureHintPage = "";
		
		// Determine the cardinal direction from the coordinates
		double dx = structureCoords[0] - villageCoords[0];
		double dy = structureCoords[1] - villageCoords[1];
		double dz = structureCoords[2] - villageCoords[2];
		// Distances across surface (2D) or absolute (3d)
		double featureDistance2D = Math.sqrt( (dx*dx)+(dz*dz) );
		double featureDistance3D = Math.sqrt( (dx*dx)+(dy*dy)+(dz*dz) );
		
		double thetaPolar;
		thetaPolar = Math.atan2(-dz, dx);
		
		//Convert angle into a cardinal direction
		String directionString;
		double directionDegrees = (thetaPolar*180/Math.PI);///11.25;
		while (directionDegrees<0) {
			directionDegrees += 360d;
		}
		// Flag for whether structure is under village
		boolean isInVillageBounds=false;
		if ( (dx*dx)+(dz*dz) <= villageRadius*villageRadius ) {
			isInVillageBounds=true;
		}
		
		boolean isBelowGround=false;
		if (nearbyStructure.equals("Mineshaft") || nearbyStructure.equals("Stronghold")){
			isBelowGround=true;
		}
		
		if (directionDegrees <= 11.25){
			directionString = "east";
		}
		else if (directionDegrees <= 33.75){
			directionString = "east-northeast";
		}
		else if (directionDegrees <= 56.25){
			directionString = "northeast";
		}
		else if (directionDegrees <= 78.75){
			directionString = "north-northeast";
		}
		else if (directionDegrees <= 101.25){
			directionString = "north";
		}
		else if (directionDegrees <= 123.75){
			directionString = "north-northwest";
		}
		else if (directionDegrees <= 146.25){
			directionString = "northwest";
		}
		else if (directionDegrees <= 168.75){
			directionString = "west-northwest";
		}
		else if (directionDegrees <= 191.25){
			directionString = "west";
		}
		else if (directionDegrees <= 213.75){
			directionString = "west-southwest";
		}
		else if (directionDegrees <= 236.25){
			directionString = "southwest";
		}
		else if (directionDegrees <= 258.75){
			directionString = "south-southwest";
		}
		else if (directionDegrees <= 281.25){
			directionString = "south";
		}
		else if (directionDegrees <= 303.75){
			directionString = "south-southeast";
		}
		else if (directionDegrees <= 326.25){
			directionString = "southeast";
		}
		else if (directionDegrees <= 348.75){
			directionString = "east-southeast";
		}
		else {
			directionString = "east";
		}
		
		/*
		 * Here's where we'll name the structure.
		 */
		String structureName;
		String[] structureNameArray = tryGetStructureName(nearbyStructure, structureCoords, event);
		
		if (structureNameArray[0]==null && structureNameArray[1]==null && structureNameArray[2]==null) {
			//Structure has no name. Generate it here.
			
			if (nearbyStructure.equals("Village")) {
				VNWorldDataVillage data = VNWorldDataVillage.forWorld(event.getEntity().world);
				structureNameArray = NameGenerator.newVillageName();
				
				// Gotta copy this thing to each IF condition I think
				String headerTags = structureNameArray[0];
				String namePrefix = structureNameArray[1];
				String nameRoot = structureNameArray[2];
				String nameSuffix = structureNameArray[3];
				int townColorMeta = 15;
				
				// Make the data bundle to save to NBT
				NBTTagList nbttaglist = new NBTTagList();
				
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setInteger("signX", structureCoords[0]);
				nbttagcompound1.setInteger("signY", structureCoords[1]);
				nbttagcompound1.setInteger("signZ", structureCoords[2]);
				nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
				nbttagcompound1.setString("namePrefix", namePrefix);
				nbttagcompound1.setString("nameRoot", nameRoot);
				nbttagcompound1.setString("nameSuffix", nameSuffix);
				nbttaglist.appendTag(nbttagcompound1);
				
				// .getTagList() will return all the entries under the specific village name.
				NBTTagCompound tagCompound = data.getData();
				
				data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
				data.markDirty();
				
			}
			else if (nearbyStructure.equals("Mineshaft")) {
				VNWorldDataMineshaft data = VNWorldDataMineshaft.forWorld(event.getEntity().world);
				structureNameArray = NameGenerator.newMineshaftName();
				
				// Gotta copy this thing to each IF condition I think
				String headerTags = structureNameArray[0];
				String namePrefix = structureNameArray[1];
				String nameRoot = structureNameArray[2];
				String nameSuffix = structureNameArray[3];
				int townColorMeta = 15;
				
				// Make the data bundle to save to NBT
				NBTTagList nbttaglist = new NBTTagList();
				
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setInteger("signX", structureCoords[0]);
				nbttagcompound1.setInteger("signY", structureCoords[1]);
				nbttagcompound1.setInteger("signZ", structureCoords[2]);
				nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
				nbttagcompound1.setString("namePrefix", namePrefix);
				nbttagcompound1.setString("nameRoot", nameRoot);
				nbttagcompound1.setString("nameSuffix", nameSuffix);
				nbttaglist.appendTag(nbttagcompound1);
				
				// .getTagList() will return all the entries under the specific village name.
				NBTTagCompound tagCompound = data.getData();
				
				data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
				data.markDirty();
				
			}
			else if (nearbyStructure.equals("Stronghold")) {
				VNWorldDataStronghold data = VNWorldDataStronghold.forWorld(event.getEntity().world);
				structureNameArray = NameGenerator.newStrongholdName();
				
				// Gotta copy this thing to each IF condition I think
				String headerTags = structureNameArray[0];
				String namePrefix = structureNameArray[1];
				String nameRoot = structureNameArray[2];
				String nameSuffix = structureNameArray[3];
				int townColorMeta = 15;
				
				// Make the data bundle to save to NBT
				NBTTagList nbttaglist = new NBTTagList();
				
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setInteger("signX", structureCoords[0]);
				nbttagcompound1.setInteger("signY", structureCoords[1]);
				nbttagcompound1.setInteger("signZ", structureCoords[2]);
				nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
				nbttagcompound1.setString("namePrefix", namePrefix);
				nbttagcompound1.setString("nameRoot", nameRoot);
				nbttagcompound1.setString("nameSuffix", nameSuffix);
				nbttaglist.appendTag(nbttagcompound1);
				
				// .getTagList() will return all the entries under the specific village name.
				NBTTagCompound tagCompound = data.getData();
				
				data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
				data.markDirty();
				
			}
			else if (nearbyStructure.equals("Temple")) {
				VNWorldDataTemple data = VNWorldDataTemple.forWorld(event.getEntity().world);
				structureNameArray = NameGenerator.newTempleName();
				
				// Gotta copy this thing to each IF condition I think
				String headerTags = structureNameArray[0];
				String namePrefix = structureNameArray[1];
				String nameRoot = structureNameArray[2];
				String nameSuffix = structureNameArray[3];
				int townColorMeta = 15;
				
				// Make the data bundle to save to NBT
				NBTTagList nbttaglist = new NBTTagList();
				
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setInteger("signX", structureCoords[0]);
				nbttagcompound1.setInteger("signY", structureCoords[1]);
				nbttagcompound1.setInteger("signZ", structureCoords[2]);
				nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
				nbttagcompound1.setString("namePrefix", namePrefix);
				nbttagcompound1.setString("nameRoot", nameRoot);
				nbttagcompound1.setString("nameSuffix", nameSuffix);
				nbttaglist.appendTag(nbttagcompound1);
				
				// .getTagList() will return all the entries under the specific village name.
				NBTTagCompound tagCompound = data.getData();
				
				data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
				data.markDirty();
				
			}
			else if (nearbyStructure.equals("Monument")) {
				VNWorldDataMonument data = VNWorldDataMonument.forWorld(event.getEntity().world);
				structureNameArray = NameGenerator.newMonumentName();
				
				// Gotta copy this thing to each IF condition I think
				String headerTags = structureNameArray[0];
				String namePrefix = structureNameArray[1];
				String nameRoot = structureNameArray[2];
				String nameSuffix = structureNameArray[3];
				int townColorMeta = 15;
				
				// Make the data bundle to save to NBT
				NBTTagList nbttaglist = new NBTTagList();
				
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setInteger("signX", structureCoords[0]);
				nbttagcompound1.setInteger("signY", structureCoords[1]);
				nbttagcompound1.setInteger("signZ", structureCoords[2]);
				nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
				nbttagcompound1.setString("namePrefix", namePrefix);
				nbttagcompound1.setString("nameRoot", nameRoot);
				nbttagcompound1.setString("nameSuffix", nameSuffix);
				nbttaglist.appendTag(nbttagcompound1);
				
				// .getTagList() will return all the entries under the specific village name.
				NBTTagCompound tagCompound = data.getData();
				
				data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
				data.markDirty();
				
			}
			else if (nearbyStructure.equals("Mansion")) {
				VNWorldDataMansion data = VNWorldDataMansion.forWorld(event.getEntity().world);
				structureNameArray = NameGenerator.newMansionName();
				
				// Gotta copy this thing to each IF condition I think
				String headerTags = structureNameArray[0];
				String namePrefix = structureNameArray[1];
				String nameRoot = structureNameArray[2];
				String nameSuffix = structureNameArray[3];
				int townColorMeta = 15;
				
				// Make the data bundle to save to NBT
				NBTTagList nbttaglist = new NBTTagList();
				
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setInteger("signX", structureCoords[0]);
				nbttagcompound1.setInteger("signY", structureCoords[1]);
				nbttagcompound1.setInteger("signZ", structureCoords[2]);
				nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
				nbttagcompound1.setString("namePrefix", namePrefix);
				nbttagcompound1.setString("nameRoot", nameRoot);
				nbttagcompound1.setString("nameSuffix", nameSuffix);
				nbttaglist.appendTag(nbttagcompound1);
				
				// .getTagList() will return all the entries under the specific village name.
				NBTTagCompound tagCompound = data.getData();
				
				data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromvillager", nbttaglist);
				data.markDirty();
				
			}
			
			structureName = structureNameArray[1]+" "+structureNameArray[2]+" "+structureNameArray[3];
			structureName = structureName.trim();
			
		}
		else {
			//Structure has a name. Unpack it here.
			structureName = structureNameArray[0]+" "+structureNameArray[1]+" "+structureNameArray[2];
			structureName = structureName.trim();
		}
		
		
		
		int approxDist2D = (int)Math.round(featureDistance2D/100)*100; //Approximates the distance to the structure
		
		String structureString="";
		
		if (villagerProfession!=5) {
			
			// Villager is NOT a nitwit
			
			if (nearbyStructure.equals("Village")) {
				String[] structureStringArray = new String[]{
						"We trade with " + structureName + ", ",
						"The villagers of this town trade with " + structureName + ", ",
						"Our trading partner, " + structureName + ", is ",
						"There's another settlement named " + structureName + " that we trade with, ",
						"The village of " + structureName + " is ",
						"A village named " + structureName + " is ",
						"There is a village, " + structureName + ", "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Stronghold")) {
				String[] structureStringArray = new String[]{
						"We have records of a stronghold, " + structureName + ", ",
						"An underground fortress, " + structureName + ", is ",
						"Our records list a stronghold, " + structureName + ", ",
						"A mysterious labyrinth, " + structureName + ", is "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Temple")) {
				String[] structureStringArray = new String[]{
						"An abandoned temple called " + structureName + " is ",
						"A former religious ritual site, " + structureName + ", is ",
						"Some bygone religious sect constructed a temple at the " + structureName + " site. It's ",
						"A previous civilization built the temple of " + structureName + ", "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Mineshaft")) {
				String[] structureStringArray = new String[]{
						"An underground mining site, " + structureName + ", is ",
						"Previous settlers built an underground mining site, " + structureName + ", ",
						"There's a long-deserted mine at the old " + structureName + " site ",
						"Back at the old " + structureName + " site, there should be an abandoned mineshaft. It's ",
						"An abandoned mining structure known as " + structureName + " is "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Monument")) {
				String[] structureStringArray = new String[]{
						"Rumors on the wind speak of a sunken temple, " + structureName + ", ",
						"There are rumors of " + structureName + ", under the sea, ",
						"The monument of " + structureName + "was said to have been dragged into the sea long ago. It's rumored to be ",
						"An old fisherman's tale mentions " + structureName + ", "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Mansion")) {
				String[] structureStringArray = new String[]{
						"We have records of a cult mansion, " + structureName + ", ",
						"We have records of a cult operating at " + structureName + ", ",
						"Our records list a cult mansion, " + structureName + ", ",
						"A mysterious cult meets at a mansion called " + structureName + ", "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else {
				//There's nothing left, man
			}
			
		}
		else {
			// Special text for nitwit
			
			if (nearbyStructure.equals("Mineshaft")) {
				String[] structureStringArray = new String[]{
						"I hear there used to be a mine called " + structureName + ", ",
						"There used to be a mine over at the old " + structureName + " site, ",
						"If the wind is still over at the " + structureName + 
							" site, you can hear faint sounds coming from deep underground. You can go check it out if you want: it's "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Stronghold")) {
				String[] structureStringArray = new String[]{
						"I've heard rumors about " + structureName + ", an underground stronghold ",
						"I get really weird supernatural sensations over at the old " + structureName + 
							" site. If you want to dig around under there, it's "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Temple")) {
				String[] structureStringArray = new String[]{
						"In my wanderings I've stumbled upon an abandoned site called "	+ structureName + ", ",
						"I hear there's a temple or something like that over at the "	+ structureName + " religious site, "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Village")) {
				String[] structureStringArray = new String[]{
						"There is another village named " + structureName + ", ",
						"Everyone knows about the town of " + structureName + ", ",
						"My buddy once lost a shoe while visiting " + structureName + ", "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Monument")) {
				String[] structureStringArray = new String[]{
						"I've heard fishermen mention a sunken monument called " + structureName + ", ",
						"The darkest sea tales, whispered in hushed tones, mention " + structureName + 
							", a sunken temple filled with treasure. If you dare to look for it, rumor says it's "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
			else if (nearbyStructure.equals("Mansion")) {
				String[] structureStringArray = new String[]{
						"I've heard a lot of very bad things about the cult that practices in "	+ structureName + ", ",
						"Others don't like to talk about it, but everyone here knows about the eerie cult that gathers at "
								+ structureName + ", deep in the dark wood. If you're foolish enough to look, it's "
					};
				structureString = structureStringArray[random.nextInt(structureStringArray.length)];
			}
		}
		
		structureHintPage += "" + structureString;
		
		/*
		 * Location of the feature
		 */
		
		String[] thisVillageArray = new String[]{
				"this village",
				"this very village"
			};
		String thisVillage = thisVillageArray[random.nextInt(thisVillageArray.length)];
		
		if (isInVillageBounds && isBelowGround){
			// Structure is under this very village.
			structureHintPage += "located underneath "+thisVillage+". ";
		}
		else if (isInVillageBounds && !isBelowGround) {
			// It's in this village and should be visible.
			structureHintPage += "located within "+thisVillage+". ";
		}
		else {
			// Is outside of village.
			
			if (approxDist2D>=100){
				
				String[] approxStringArray = new String[]{
						"approximately",
						"roughly",
						"about"
					};
				String approxString = approxStringArray[random.nextInt(approxStringArray.length)];
				
				structureHintPage += "located "+approxString;
				structureHintPage += " "+approxDist2D+" meters ";
			}
			else {
				String[] approxStringArray = new String[]{
						"less than",
						"under",
						"short of"
					};
				String approxString = approxStringArray[random.nextInt(approxStringArray.length)];
				
				structureHintPage += "located "+approxString;
				structureHintPage += " 100 meters ";
			}
			String[] wordArray = new String[]{
					"due",
					"to the"
				};
			String randomWord = wordArray[random.nextInt(wordArray.length)];
			structureHintPage += randomWord + " " + directionString + ". ";
			
		}
		return structureHintPage;
	}

	/**
	 * This method searches the feature you're interested in to see if a name already exists for it
	 * @return {namePrefix, nameRoot, nameSuffix} if something is found; {null, null, null} otherwise
	 */
	private String[] tryGetStructureName(String nearbyStructure, int[] structureCoords, EntityInteract event) {
		
		// Load in names data
		VNWorldData data=null;
		if (nearbyStructure.equals("Village")) {
			data = VNWorldDataVillage.forWorld(event.getEntity().world);
		}
		else if (nearbyStructure.equals("Mineshaft")) {
			data = VNWorldDataMineshaft.forWorld(event.getEntity().world);
		}
		else if (nearbyStructure.equals("Stronghold")) {
			data = VNWorldDataStronghold.forWorld(event.getEntity().world);
		}
		else if (nearbyStructure.equals("Temple")) {
			data = VNWorldDataTemple.forWorld(event.getEntity().world);
		}
		else if (nearbyStructure.equals("Monument")) {
			data = VNWorldDataMonument.forWorld(event.getEntity().world);
		}
		else if (nearbyStructure.equals("Mansion")) {
			data = VNWorldDataMansion.forWorld(event.getEntity().world);
		}
		
		// .getTagList() will return all the entries under the specific village name.
		NBTTagCompound tagCompound = data.getData();
		
		Set tagmapKeyset = tagCompound.getKeySet(); //Gets the town key list: "coordinates"
		
		Iterator itr = tagmapKeyset.iterator();
		String townSignEntry;
		
		String namePrefix=null;
		String nameRoot=null;
		String nameSuffix=null;
		while(itr.hasNext()) {
			Object element = itr.next();
			
			townSignEntry = element.toString(); //Text name of village header (e.g. "x535y80z39")
			//The only index that has data is 0:
			NBTTagCompound tagList = tagCompound.getTagList(townSignEntry, tagCompound.getId()).getCompoundTagAt(0);
			
			// Town's location
			int featureX = tagList.getInteger("signX");
			int featureY = tagList.getInteger("signY");
			int featureZ = tagList.getInteger("signZ");
			
			double sdx = featureX - structureCoords[0];
			double sdy = (featureY==64) ? 0.d : featureY - structureCoords[1]; // A signY of 64 likely means it was detected pre-generation.
			double sdz = featureZ - structureCoords[2];

			if ( (sdx*sdx)+(sdy*sdy)+(sdz*sdz) <= 100*100 ) {
				//This entry has been correctly matched to the structure in question
				namePrefix = tagList.getString("namePrefix"); 
				nameRoot = tagList.getString("nameRoot"); 
				nameSuffix = tagList.getString("nameSuffix"); 
			}
			
		}
		
		return new String[] {namePrefix, nameRoot, nameSuffix};
	}
		

	/**
	 * Enter an int[] and a value, and this will return the first index matching that value, or -1 on a failure.
	 * 
	 */
	public static int indexOfIntArr(int[] intArray, int valToIndex) {
		for (int i=0; i<intArray.length; i++) {
			if(intArray[i]==valToIndex)
				return i;
		}
		return -1;
	}



	/**
	 * Input player entity and structure to search for (as a string)
	 * Returns X, Y, Z ints that represent the center of the bounding box
	 */
	private int[] getStructureFromChunk(String structureName, EntityInteract event) {
		
		MapGenStructureData structureData;
		World worldIn = event.getEntityPlayer().world;
		Entity target = event.getTarget();
		
		// Player chunk coordinates
		double targetX = target.posX;
		double targetY = target.posY;
		double targetZ = target.posZ;
		int outX;
		int outZ;
		int[ ] posXYZ = new int[3];
		
		try {
			structureData = (MapGenStructureData)worldIn.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, structureName);
			
			NBTTagCompound nbttagcompound = structureData.getTagCompound();
			
			double rSearch = Double.MAX_VALUE;
			
			for (String s : nbttagcompound.getKeySet()) {
				NBTBase nbtbase = nbttagcompound.getTag(s);
				
				if (nbtbase.getId() == 10) {
					NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbtbase;
					
					if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ")) {
						int[] boundingBox = nbttagcompound1.getIntArray("BB");
						int BBX=(boundingBox[0]+boundingBox[3])/2;
						int BBY=(boundingBox[1]+boundingBox[4])/2;
						int BBZ=(boundingBox[2]+boundingBox[5])/2;
						double dX = BBX - targetX;
						double dY = BBY - targetY;
						double dZ = BBZ - targetZ;
						if (dX*dX + dY*dY + dZ*dZ <= rSearch) {
							// This will give back the closest X and Z
							rSearch = dX*dX + dY*dY + dZ*dZ;
							posXYZ[0] = BBX;
							posXYZ[1] = BBY;
							posXYZ[2] = BBZ;
						}
					}
				}
			}
		}
		catch (Exception e) {
			// No structures were found
		}
		return posXYZ;
	}

	/**
	 * Gets the bounding box of the nearest structure of a declared type.
	 * @param structureName: String name of the structure to locate
	 * @param event: entity interact event
	 * @return int[] of six values that form the structure's bounding box [x0, y0, z0, x1, y1, z1]
	 */
	private int[] getBoundingBoxFromChunk(String structureName, EntityInteract event) {
		
		MapGenStructureData structureData;
		World worldIn = event.getEntityPlayer().world;
		Entity target = event.getTarget();
		
		// Player chunk coordinates
		double targetX = target.posX;
		double targetY = target.posY;
		double targetZ = target.posZ;
		int outX;
		int outZ;
		int[ ] BB = new int[6];
		
		try {
			structureData = (MapGenStructureData)worldIn.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, structureName);
			
			NBTTagCompound nbttagcompound = structureData.getTagCompound();
			
			double rSearch = Double.MAX_VALUE;
			
			for (String s : nbttagcompound.getKeySet()) {
				NBTBase nbtbase = nbttagcompound.getTag(s);
				
				if (nbtbase.getId() == 10) {
					NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbtbase;
					
					if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ")) {
						int[] boundingBox = nbttagcompound1.getIntArray("BB");
						int BBX=(boundingBox[0]+boundingBox[3])/2;
						int BBY=(boundingBox[1]+boundingBox[4])/2;
						int BBZ=(boundingBox[2]+boundingBox[5])/2;
						double dX = BBX - targetX;
						double dY = BBY - targetY;
						double dZ = BBZ - targetZ;
						if (dX*dX + dY*dY + dZ*dZ <= rSearch) {
							// This will give back the closest X and Z
							rSearch = dX*dX + dY*dY + dZ*dZ;
							BB = boundingBox;
						}
					}
				}
			}
		}
		catch (Exception e) {
			// No structures were found
		}
		return BB;
	}

	/**
	 * Use this to re-calibrate the official reputation with the unofficially saved one
	 */
	private void repairPlayerReputation(EntityPlayer player, Entity target, World world) {
		
		String playerName = player.getDisplayNameString();
		double villagerX = target.posX;
		double villagerY = target.posY;
		double villagerZ = target.posZ;
		int unofficialPlayerRep = 0;
		String townSignEntry = null;
		
		Village villageNearVillager = target.world.getVillageCollection().getNearestVillage(new BlockPos(villagerX, villagerY, villagerZ), villageRadiusBuffer);
		if (villageNearVillager != null && player.dimension==0) {
			// You've found a village.
			// First check to see if there's an Un-official name for this player and this village.
			
			VNWorldDataVillage data = VNWorldDataVillage.forWorld(world); //event.entity.world
			
			// .getTagList() will return all the entries under the specific village name.
			NBTTagCompound tagCompound = data.getData();
			
			Set tagmapKeyset = tagCompound.getKeySet(); //Gets the town key list: "coordinates"
			
			Iterator itr = tagmapKeyset.iterator();
			
			
			//Placeholders for villagenames.dat tags
			boolean signLocated = false; //Use this to record whether or not a sign was found
			
			while(itr.hasNext()) {
				Object element = itr.next();
				
				townSignEntry = element.toString(); //Text name of village header (e.g. "x535y80z39")
				//The only index that has data is 0:
				NBTTagCompound tagList = tagCompound.getTagList(townSignEntry, tagCompound.getId()).getCompoundTagAt(0);
				
				int townX = tagList.getInteger("signX");
				int townY = tagList.getInteger("signY");
				int townZ = tagList.getInteger("signZ");
				
				
				Village villageNearSign = player.world.getVillageCollection().getNearestVillage(new BlockPos(townX, townY, townZ), villageRadiusBuffer);
				
				if (villageNearSign == villageNearVillager) {
					signLocated = true;
					if (!player.world.isRemote) {
						// Does an unofficial rep value exist?
						try {
							// At least one player has unofficial rep in this town. Read it in.
							NBTTagCompound playerReps = tagList.getTagList("playerReps", tagList.getId()).getCompoundTagAt(0);
							// Return the stored value or 0 if there isn't any
							unofficialPlayerRep = playerReps.getInteger(playerName);
						}
						catch (Exception e) {
							// No players have unofficial rep for this town.
							unofficialPlayerRep = 0;
						}
						
					}
					break;
				}
			}
			
			// At this point you've loaded in a value for unofficialPlayerRep. Now compare it to the official version.
			int officialPlayerRep = villageNearVillager.getPlayerReputation(playerName);
			
			if ( 
					officialPlayerRep!=unofficialPlayerRep
					) {
				
				NBTTagCompound specificVillageTag = data.getData().getTagList(townSignEntry, tagCompound.getId()).getCompoundTagAt(0);
				
				NBTTagList nbttaglist = new NBTTagList();
				
				NBTTagCompound tagList = tagCompound.getTagList(townSignEntry, tagCompound.getId()).getCompoundTagAt(0);
				NBTTagCompound nbttagcompound1 = tagList.getTagList("playerReps", tagList.getId()).getCompoundTagAt(0);
				nbttagcompound1.setInteger(playerName, officialPlayerRep);
				nbttaglist.appendTag(nbttagcompound1);
				
				specificVillageTag.setTag("playerReps", nbttaglist);
				
				NBTTagList dummyTagList = new NBTTagList();
				
				dummyTagList.appendTag(specificVillageTag);
				
				// Save the data under a "Villages" entry with unique name based on sign coords
				try {
					data.getData().setTag(townSignEntry, dummyTagList);
					data.markDirty();
				}
				catch (Exception e) {
					// There was never a village to save to.
				}
			}
			
		}
	}
	
	/**
	 * Generates a "baby villager can't write book" string.
	 * 
	 */
	public static String babyCantHelpString() {
		Random random = new Random();
		String[] babyCantHelpArray = new String[]{
				"This villager is too young to help you.",
				"The child looks uncomfortable with you.",
				"This child is not interested in busywork.",
				"You should probably ask an adult.",
				"This child just wants to play!",
				"This child just wants to frolick!",
				"The child looks around nervously.",
				"Who wants to do homework? Not this kid.",
				"This child is still developing language.",
				"Why would you ask a child for such information? That's a bit odd.",
				"The child reaches out with soiled hands. Perhaps you should find another villager.",
				"Stop bothering children with this.",
				"The child looks away sheepishly.",
				"The child sticks out its tongue. This is not productive."
			};
		return babyCantHelpArray[random.nextInt(babyCantHelpArray.length)];
	}
	
	/**
     * This method searches the feature you're interested in to see if a name already exists for it
     * @return {namePrefix, nameRoot, nameSuffix} if something is found; {null, null, null} otherwise
     */
    public static String[] tryGetStructureInfo(String structureType, int[] structureBB, World world) {
    	
    	// Load in names data
    	VNWorldData data=null;
    	if (structureType.equals("Village")) {
    		data = VNWorldDataVillage.forWorld(world);
    	}
    	else if (structureType.equals("Mineshaft")) {
    		data = VNWorldDataMineshaft.forWorld(world);
    	}
    	else if (structureType.equals("Stronghold")) {
    		data = VNWorldDataStronghold.forWorld(world);
    	}
    	else if (structureType.equals("Temple")) {
    		data = VNWorldDataTemple.forWorld(world);
    	}
    	else if (structureType.equals("Monument")) {
    		data = VNWorldDataMonument.forWorld(world);
    	}
    	else if (structureType.equals("Mansion")) {
    		data = VNWorldDataMansion.forWorld(world);
    	}
    	else if (structureType.equals("Fortress")) {
    		data = VNWorldDataFortress.forWorld(world);
    	}
    	else if (structureType.equals("EndCity")) {
    		data = VNWorldDataEndCity.forWorld(world);
    	}
    	else if (structureType.equals("MoonVillage")) {
    		data = VNWorldDataMoonVillage.forWorld(world);
    	}
    	else if (structureType.equals("KoentusVillage")) {
    		data = VNWorldDataKoentusVillage.forWorld(world);
    	}
    	else if (structureType.equals("hardcoreenderdragon_EndTower")) {
    		data = VNWorldDataEndTower.forWorld(world);
    	}
    	else if (structureType.equals("hardcoreenderdragon_EndIsland")) {
    		data = VNWorldDataEndIsland.forWorld(world);
    	}
    	else if (structureType.equals("FronosVillage")) {
    		data = VNWorldDataFronosVillage.forWorld(world);
    	}
    	else if (structureType.equals("NibiruVillage")) {
    		data = VNWorldDataNibiruVillage.forWorld(world);
    	}
    	else if (structureType.equals("GC_AbandonedBase")) {
    		data = VNWorldDataAbandonedBase.forWorld(world);
    	}
    	
    	// .getTagList() will return all the entries under the specific village name.
    	NBTTagCompound tagCompound = data.getData();
    	
    	Set tagmapKeyset = tagCompound.getKeySet(); //Gets the town key list: "coordinates"
    	
    	Iterator itr = tagmapKeyset.iterator();
    	String featureSignLoc;
    	
    	String namePrefix=null;
    	String nameRoot=null;
    	String nameSuffix=null;
    	String signX=null;
    	String signY=null;
    	String signZ=null;
    	
    	while(itr.hasNext()) {
    		Object element = itr.next();
    		
    		featureSignLoc = element.toString(); //Text name of feature header (e.g. "x535y80z39")
    		//The only index that has data is 0:
    		NBTTagCompound tagList = tagCompound.getTagList(featureSignLoc, tagCompound.getId()).getCompoundTagAt(0);
    		
    		int[] structureCoords = new int[] {tagList.getInteger("signX"), tagList.getInteger("signY"), tagList.getInteger("signZ")};
    		
    		// A signY of 64 likely means it was detected pre-generation.
    		// The below code detects if you're in the 3D bounding box if and only if signY is not 64.
    		// If signY is 64, the code detects if you're in the 2D (x vs z) bounding box.
    		    		
    		if (
    				structureCoords[0] >= structureBB[0]
    			 && structureCoords[2] >= structureBB[2]
    			 && structureCoords[0] <= structureBB[3]
    			 && structureCoords[2] <= structureBB[5]
    			 && ( structureCoords[1] == 64  ||  (structureCoords[1] >= structureBB[1] && structureCoords[1] <= structureBB[4] ) )
    			) {
    			//This entry has been correctly matched to the structure in question
    			namePrefix = tagList.getString("namePrefix"); 
    			nameRoot = tagList.getString("nameRoot"); 
    			nameSuffix = tagList.getString("nameSuffix"); 
    			signX = ""+structureCoords[0];
    			signY = ""+structureCoords[1];
    			signZ = ""+structureCoords[2];
    		}
    		
    	}
    	
    	return new String[] {namePrefix, nameRoot, nameSuffix, signX, signY, signZ};
    }
	
}
