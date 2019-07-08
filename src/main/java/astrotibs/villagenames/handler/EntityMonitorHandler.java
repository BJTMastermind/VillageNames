package astrotibs.villagenames.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import astrotibs.villagenames.VillageNames;
import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.network.MessageModernVillagerSkin;
import astrotibs.villagenames.network.MessageZombieVillagerProfession;
import astrotibs.villagenames.network.NetworkHelper;
import astrotibs.villagenames.tracker.ClientInfoTracker;
import astrotibs.villagenames.tracker.EventTracker;
import astrotibs.villagenames.tracker.ServerInfoTracker;
import astrotibs.villagenames.tracker.ServerInfoTracker.EventType;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Adapted from Villager Tweaks by sidben:
 * https://github.com/sidben/VillagerTweaks/blob/master/src/main/java/sidben/villagertweaks/handler/EntityMonitorHandler.java
 * @author AstroTibs
 */
public class EntityMonitorHandler
{
	protected static int tickRate = 50; // Number of ticks between monitoring
	protected final int failuresToForceAcceptance = 100; // How many invalid trade cycles are performed until we can assume this is an infinite loop
	
	//TODO - for villager zombies
	
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityVillager) {
            if (FunctionsVN.isVanillaZombie(event.getSource().getEntity())) {

                // A villager was killed by a zombie and may be zombified. Adds to the tracker for future check.
                final EntityVillager villager = (EntityVillager) event.getEntity();
                
                
                ServerInfoTracker.add(villager, villager.worldObj);

                if (GeneralConfig.debugMessages) {
                    LogHelper.info("EntityMonitorHandler > A zombie just killed villager " 
                    		+ ( villager.getCustomNameTag().equals("")||villager.getCustomNameTag().equals(null) ? "(None)" : villager.getCustomNameTag() ) 
                    		+ " [" + villager.getEntityId() + "] "
                    		+ "at [" + 
                    		//villager.getPosition(1.0F)
                    		//Vec3.createVectorHelper(villager.posX, villager.posY, villager.posZ) // Changed because of server crash
                    		new Vec3i(villager.posX, villager.posY + 0.5D, villager.posZ)
                    		+ "], profession [" + villager.getProfession() + "]");
                }
            }
        }
    }

    // Re-added in v3.1
    @SubscribeEvent
    public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
    	

    	if (!event.getEntity().worldObj.isRemote)
    	{

        	if (event.getTarget() instanceof EntityVillager)
        	{
        		final EntityVillager villager = (EntityVillager) event.getTarget();
        		IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
        		
        		NetworkHelper.sendModernVillagerSkinMessage(villager.getEntityId(), ims, event.getEntityPlayer());
        	}
        	
            // Check if the player started tracking a zombie villager (happens on server-side).
            if (FunctionsVN.isVanillaZombie(event.getTarget())  && !event.getEntity().worldObj.isRemote)
            {
                final EntityZombie zombie = (EntityZombie) event.getTarget();
                IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
                
                if (zombie.isVillager()) {

                    // Check if the zombie has special properties
                    //if (zombie.hasCapability(ModularSkinProvider.MODULAR_SKIN, null)) {
                        NetworkHelper.sendZombieVillagerProfessionMessage(zombie.getEntityId(), ims, event.getEntityPlayer());
                    //}
                }
            }
            
            // Check if the player started tracking a village guard
            else if (event.getEntity().getClass().toString().substring(6).equals(ModObjects.WitcheryGuardClass)) {
            	final EntityLiving guard = (EntityLiving) event.getTarget();
            	IModularSkin ims = guard.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
            	
                // Check if the guard has special properties
                if (guard.hasCapability(ModularSkinProvider.MODULAR_SKIN, null)) {
                    NetworkHelper.sendVillageGuardMessage(guard.getEntityId(), ims, event.getEntityPlayer());
                }

            }
    		
    	}
    	

    }
    
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {

    	// Renovated in v3.1
    	
    	// New entity is a Zombie. Check to see if it came into being via a killed Villager.
        if (
        		FunctionsVN.isVanillaZombie(event.getEntity())
        		&& ((EntityZombie)event.getEntity()).isVillager()
        		)
        {
            final EntityZombie zombievillager = (EntityZombie) event.getEntity();

        	IModularSkin ims = zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
        	
    		// Try to assign a biome number if this villager has none.
            if (ims.getBiomeType() <0)
            {
            	ims.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(zombievillager));
            }

            // Added in v3.2
            if (ims.getSkinTone() == -99)
            {ims.setSkinTone(FunctionsVN.returnSkinToneForEntityLocation(zombievillager));}
            
            // Renovated in v3.1
            if (event.getWorld().isRemote) {
                // Looks for info sent by the server that should be applied to the zombie (e.g. villager profession)
                ClientInfoTracker.SyncZombieMessage(zombievillager);
            }
            else {

                // Looks on the event tracker for a villager that just died
                //final EventTracker tracked = ServerInfoTracker.seek(EventType.VILLAGER, zombie.getPosition(1.0F));//zombie.getPosition());
            	final EventTracker tracked = ServerInfoTracker.seek(EventType.VILLAGER,
            			//Vec3.createVectorHelper(zombie.posX, zombie.posY, zombie.posZ)
            			new Vec3i(zombievillager.posX, zombievillager.posY + 0.5D, zombievillager.posZ),
            			zombievillager.worldObj
            			); // Replaced because of mp server-side crash
            	
                if (tracked != null) {
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("EntityMonitorHandler > Found info on the tracker--must copy to zombie");
                    }

                    // If found, copy the data from the villager
                    tracked.updateZombie(event, ims);
                }
                else if (ims.getProfession() == -1) {
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("EntityMonitorHandler > No info on the tracker--assigning a random profession");
                    }

                    // If not, assign a random profession
                    int profession = zombievillager.worldObj.rand.nextInt(6);
                    ims.setProfession(profession);
                    ims.setCareer(FunctionsVN.pickRandomCareerForProfession(profession, zombievillager.worldObj.rand));
                }

                if (GeneralConfig.debugMessages) {
                    LogHelper.info("EntityMonitorHandler > Custom name [" + zombievillager.getCustomNameTag() + "]");
                    LogHelper.info("EntityMonitorHandler > Profession [" + ims.getProfession() + "]");
                    LogHelper.info("EntityMonitorHandler > Career [" + ims.getCareer() + "]");
                    LogHelper.info("EntityMonitorHandler > ProfessionLevel [" + ims.getProfessionLevel() + "]");
                    LogHelper.info("EntityMonitorHandler > BiomeType [" + ims.getBiomeType() + "]");
                    
                }
            }
            

        }
        
        // New entity is a villager. Check to see if it came into being via a cured villager-zombie.
        else if (event.getEntity() instanceof EntityVillager) {

            final EntityVillager villager = (EntityVillager) event.getEntity();
            
        	// Added in v3.1
    		IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
    		
            // Renovated in v3.1
            if (event.getWorld().isRemote)
            {
                // Looks for info sent by the server that should be applied to the zombie (e.g. villager profession)
                ClientInfoTracker.syncModernVillagerMessage(villager);
            }
            else
    		{
                // Looks on the event tracker for a zombie that was cured
                final EventTracker tracked = ServerInfoTracker.seek(
                		EventType.ZOMBIE,
                		new Vec3i(villager.posX, villager.posY + 0.5D, villager.posZ), villager.worldObj
                		);
            	
                if (tracked != null) {
                	// This is a cured Villager Zombie.
                	
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("EntityMonitorHandler > Found info on the tracker--must copy to villager");
                    }

                    // If found, copy the data from the zombie
                    tracked.updateVillager(villager, ims);

                    // Sends info to the special track list
                    ServerInfoTracker.endedCuringZombie(tracked.getEntityID(), villager.getEntityId(), villager.worldObj);

                    ServerInfoTracker.removeCuredZombiesFromTracker(event.getWorld(), tracked.getEntityID());
                    
                }
                
                // Initialize trades so that the villager will be forced to pick a career
                MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
                
                if (buyingList==null || buyingList.size()==0)
                {
                	// Villager has not yet been initialized
            		try {
                    	Method populateBuyingList_m = ReflectionHelper.findMethod(EntityVillager.class, villager, new String[]{"populateBuyingList", "func_175554_cu"}); // Updated 1.11 FindMethod version and changed to EntityVillagerZombie
                    	populateBuyingList_m.invoke(villager);
            			}
            		catch (Exception e) {
            			if (GeneralConfig.debugMessages) LogHelper.warn("Could not invoke EntityVillager.populateBuyingList method");
            		}
                }
                // Added in v3.1trades
                else
                {
                	// Remove illegal trades on the off-chance that a dependent mod was removed.
                	for (int i=buyingList.size()-1; i >= 0; i--) {
                		
                		MerchantRecipe merchantrecipe = (MerchantRecipe)buyingList.get(i);
                		
                		if (merchantrecipe.getItemToBuy()==null || merchantrecipe.getItemToBuy().getItem()==Item.getItemFromBlock(Blocks.AIR)
								|| merchantrecipe.getItemToSell()==null || merchantrecipe.getItemToSell().getItem()==Item.getItemFromBlock(Blocks.AIR))
                		{
                			if (GeneralConfig.debugMessages) {LogHelper.info("Removing illegal trade at index " + i);} // Added in v3.1trades
                			buyingList.remove(i);
                			} // Remove the offending trade
                	}
                }
    		}
    		
    		// Try to assign a biome number if this villager has none.
    		if (ims.getProfession() == -1 ) {ims.setProfession(villager.getProfession());}
    		if (ims.getCareer() == -1 ) {ims.setCareer((Integer)ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"}));}
    		if (ims.getProfessionLevel() == -1 ) {ims.setProfessionLevel(0);} // Changed in v3.1trades
    		if (ims.getBiomeType() == -1 ) {ims.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(villager));}
    		if (ims.getSkinTone() == -99 ) {ims.setSkinTone(FunctionsVN.returnSkinToneForEntityLocation(villager));} // Added in v3.2
    		
        }

    }
    


    @SubscribeEvent
    public void onLivingUpdateEvent(LivingUpdateEvent event) {
    	
        // Check if a zombie is about to convert to villager
        if (
        		FunctionsVN.isVanillaZombie(event.getEntity())
        		&& ((EntityZombie)event.getEntity()).isVillager()
        		) {
            final EntityZombie zombie = (EntityZombie) event.getEntity();

            // Based on the [onUpdate] event from zombies
            if (!zombie.worldObj.isRemote && zombie.isConverting()) {
            	
            	double checkfactor = 10; // This determines how (many times) frequently to check as compared to vanilla
            	
        		//summon Zombie ~ ~ ~ {IsVillager:1}
            	
            	// Check the spaces around the zombie, and speed up or slow down the conversion process based on keyed blocks
                int vanillaRollbackTicks = 0;
            	// First, undo the official vanilla entries
            	if (zombie.worldObj.rand.nextFloat() < (0.01F*checkfactor) ) {
                	
                    int countedBedsOrBars = 0;

                    for (int k = (int)zombie.posX - 4; k < (int)zombie.posX + 4 && countedBedsOrBars < 14; ++k)
                    {
                        for (int l = (int)zombie.posY - 4; l < (int)zombie.posY + 4 && countedBedsOrBars < 14; ++l)
                        {
                            for (int i1 = (int)zombie.posZ - 4; i1 < (int)zombie.posZ + 4 && countedBedsOrBars < 14; ++i1)
                            {
                                Block block = zombie.worldObj.getBlockState(new BlockPos(k, l, i1)).getBlock();

                                if (block == Blocks.IRON_BARS || block == Blocks.BED)
                                {
                                    if (zombie.worldObj.rand.nextFloat() < (0.3F/checkfactor) ) {
                                        --vanillaRollbackTicks;
                                    }
                                    
                                    ++countedBedsOrBars;
                                }
                            }
                        }
                    }
                }
            	//if (vanillaRollbackTicks!=0 && GeneralConfigHandler.debugMessages) {LogHelper.info("Counteracting vanilla effects resulting in a " + vanillaRollbackTicks + " tick adjustment");}
            	
            	// Next, apply the values as per the config entries
            	Map<String, ArrayList> zombieCureCatalysts = GeneralConfig.unpackZombieCureCatalysts(GeneralConfig.zombieCureCatalysts);
            	Map<String, ArrayList> zombieCureGroups = GeneralConfig.unpackZombieCureGroups(GeneralConfig.zombieCureGroups);
            	
            	// Finally, update the conversion value. Do this once every ten ticks I suppose.
            	
            	int modTickAdjustment = 0;
            	
            	if (zombie.worldObj.rand.nextFloat() < (0.01F*checkfactor) ) {
            		
                	for ( int groupi=0 ; groupi < zombieCureGroups.get("Groups").size(); groupi++ ) { // Go through all the groups in zombieCureGroups
                		
                		String group = (String) zombieCureGroups.get("Groups").get(groupi);
                		int groupLimit = (Integer) zombieCureGroups.get("Limits").get(groupi);
                		double groupSpeedup = ((Double) zombieCureGroups.get("Speedups").get(groupi))/checkfactor;
                		
                		// Extract sign and apply it later
                		int speedupSign = groupSpeedup<0?-1:1;
                		groupSpeedup = Math.abs(groupSpeedup); 
                		
                        int countedGroupBlocks = 0;

                        for (int k = (int)zombie.posX - 4; k < (int)zombie.posX + 4 && countedGroupBlocks < groupLimit; ++k) {
                            for (int l = (int)zombie.posY - 4; l < (int)zombie.posY + 4 && countedGroupBlocks < groupLimit; ++l) {
                                for (int i1 = (int)zombie.posZ - 4; i1 < (int)zombie.posZ + 4 && countedGroupBlocks < groupLimit; ++i1) {
                                    
                                	IBlockState blockState = zombie.worldObj.getBlockState(new BlockPos(k, l, i1));
                                	Block block = blockState.getBlock();
                        			int blockmeta = block.getMetaFromState(blockState);
                                    String blockClassPath = block.getClass().toString().substring(6);
                                    String blockUnlocName = block.getUnlocalizedName();
                                    
                                    for ( int blocki=0 ; blocki < zombieCureCatalysts.get("Groups").size(); blocki++ ) { // Go through all the custom block entries
                                    	
                                    	String catalystGroup = (String) zombieCureCatalysts.get("Groups").get(blocki);
                                    	String catalystClassPath = (String) zombieCureCatalysts.get("ClassPaths").get(blocki);
                                    	String catalystUnlocName = (String) zombieCureCatalysts.get("UnlocNames").get(blocki);
                                    	int catalystMeta = (Integer) zombieCureCatalysts.get("Metas").get(blocki);
                                    	
                                    	if (
                                    			catalystGroup.equals(group)
                                    			&& catalystClassPath.equals(blockClassPath)
                                    			&& (catalystUnlocName.equals("") || catalystUnlocName.equals(blockUnlocName))
                                    			&& (catalystMeta==-1 || blockmeta==catalystMeta)
                                    			) {
                                    		
                                    		//if (GeneralConfigHandler.debugMessages) {
                                        	//	LogHelper.info("Ticked match at " + k + " " + l + " " + i1);
                                        	//	}
                                    		
                                    		for (int i=1; i<groupSpeedup; i++) {
                                        		// Increment time jump
                                        		modTickAdjustment += speedupSign; 
                                        	}
                                        	// Then, deal with the fractional leftover
                                            if (zombie.worldObj.rand.nextFloat() < groupSpeedup % 1) {
                                            	modTickAdjustment += speedupSign; 
                                            }
                                            
                                            ++countedGroupBlocks;
                                            break;
                                    	}
                                    }
                                }
                            }
                        }
                        //if (countedGroupBlocks!=0 &&
                        //		GeneralConfigHandler.debugMessages) {LogHelper.info("Incrementing conversion as per " + countedGroupBlocks + " blocks from " + group + " group.");}
                	}
            	}
            	
            	//if (GeneralConfigHandler.debugMessages && modTickAdjustment != 0) {
            	//	LogHelper.info("Zombie conversion advanced by " + modTickAdjustment + " ticks from custom blocks.");
            	//	}
            	
            	// TODO - use this to diagnose tick balances 
            	//if (GeneralConfigHandler.debugMessages && (vanillaRollbackTicks != 0 || modTickAdjustment != 0) ) {LogHelper.info("Total tick adjustment: " + (vanillaRollbackTicks+modTickAdjustment));}
            	
            	//this.accumulatedticks += (vanillaRollbackTicks+modTickAdjustment);
            	//if (GeneralConfigHandler.debugMessages && (vanillaRollbackTicks != 0 || modTickAdjustment != 0) ) {
            	//	LogHelper.info("Cumulative advanced ticks: "+accumulatedticks);
            	//	}
            	
            	int conversionTime=0;
            	
            	try{
            		conversionTime = ReflectionHelper.getPrivateValue(EntityZombie.class, (EntityZombie)event.getEntity(), new String[]{"conversionTime", "field_82234_d"}); // The MCP mapping for this field name
            		// Increment conversion time
            		conversionTime -= (vanillaRollbackTicks+modTickAdjustment);
            		// Cap at 5 minutes
            		conversionTime = MathHelper.clamp_int(conversionTime, 1, 6000);
            		// Set the conversion value to this modified value
            		ReflectionHelper.setPrivateValue(EntityZombie.class, (EntityZombie)event.getEntity(), conversionTime, new String[]{"conversionTime", "field_82234_d"});
            		//TODO - use this to monitor tick progress
            		//if (GeneralConfigHandler.debugMessages) {LogHelper.warn("Setting conversion timer to "+conversionTime);}
    			}
            	catch (Exception e) {
    				//if (GeneralConfigHandler.debugMessages) LogHelper.warn("EntityMonitorHandler > Could not reflect Entity.conversionTime field");
    			}
            	
            	Method getConversionTimeBoost_m = ReflectionHelper.findMethod(EntityZombie.class, (EntityZombie)event.getEntity(), new String[]{"getConversionTimeBoost", "func_82233_q"}); // The MCP mapping for this method name
            	
            	int getConversionTimeBoost=0;
            	
            	try {
            		getConversionTimeBoost = (Integer)getConversionTimeBoost_m.invoke((EntityZombie)event.getEntity());
            	}
            	catch (Exception e) {
            		//if (GeneralConfigHandler.debugMessages) LogHelper.warn("EntityMonitorHandler > Could not reflect EntityZombie.getConversionTimeBoost");
            	}
            	
                final int nextConversionTime = conversionTime - getConversionTimeBoost;//zombie.conversionTime - zombie.getConversionTimeBoost();
                
                if (GeneralConfig.debugMessages 
                		&& nextConversionTime <= 500 // Starts counting down 25 seconds before conversion
                		&& nextConversionTime % 20 == 0 // Confirmation message every second
                		) { // Counts down 25 seconds until a zombie villager is cured
                	LogHelper.info("EntityMonitorHandler > Zombie [" + zombie.getEntityId() + "] being cured in " + conversionTime + " ticks");
                }

                // NOTE: if [conversionTime] is zero, the zombie already converted and it's too late to track
                if (nextConversionTime <= 0 && conversionTime > 0) {
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("EntityMonitorHandler > Zombie " + zombie.toString() + " is about to be cured in tick " + event.getEntity().getServer().getTickCounter());
                    }
                    ServerInfoTracker.add(zombie, event.getEntity().worldObj);
                }
            }
            

            // Added in v3.1
            if (!zombie.worldObj.isRemote)
            {
            	IModularSkin ims = zombie.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
            	if (ims.getBiomeType()==-1) {ims.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(zombie));}
            	if (ims.getSkinTone()==-1) {ims.setSkinTone(FunctionsVN.returnSkinToneForEntityLocation(zombie));} // Added in v3.2
            	
            	zombie.setCanPickUpLoot(false);
            	
            	// Strip gear
            	zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, null);
            	zombie.setItemStackToSlot(EntityEquipmentSlot.CHEST, null);
            	zombie.setItemStackToSlot(EntityEquipmentSlot.LEGS, null);
            	zombie.setItemStackToSlot(EntityEquipmentSlot.FEET, null);
            	
                // summon Zombie ~ ~ ~ {IsVillager:1}
    			if ((zombie.ticksExisted + zombie.getEntityId())%5 == 0) // Ticks intermittently, modulated so villagers deliberately don't sync.
    					{
    				// Sends a ping to everyone within 80 blocks
    				NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(zombie.dimension, zombie.lastTickPosX, zombie.lastTickPosY, zombie.lastTickPosZ, 16*5);
    				VillageNames.VNNetworkWrapper.sendToAllAround(
    						new MessageZombieVillagerProfession(zombie.getEntityId(), ims.getProfession(), ims.getCareer(), ims.getBiomeType(), ims.getProfessionLevel(), ims.getSkinTone()), // v3.2
    						targetPoint);
    					}
            }
            
            
        }
        
        

        // New entity is a village guard. Check to see if it came into being via a player's recruitment.
        else if (
        		Loader.isModLoaded("witchery")
        		&& event.getEntity() instanceof EntityLiving
        		&& event.getEntity().getClass().toString().substring(6).equals(ModObjects.WitcheryGuardClass)
        		&& (EventType.GUARD).getTracker().size() > 0
        		) {
        	
            final EntityLiving guard = (EntityLiving) event.getEntity();
            
            // Re-enabled in v3.1
            
            if (event.getEntity().worldObj.isRemote)
            {
            	// Looks for info sent by the server that should be applied to the zombie (e.g. villager profession)
                ClientInfoTracker.SyncGuardMessage(guard);
            }
            else
            {
                // Looks on the event tracker for a villager that just "died"
            	final EventTracker tracked = ServerInfoTracker.seek(EventType.GUARD,
            			new Vec3i(guard.posX, guard.posY + 0.5D, guard.posZ), guard.worldObj
            			); // Replaced because of mp server-side crash
            	
            	IModularSkin ims = guard.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
            	
            	if (tracked != null)
            	{
            		if (GeneralConfig.debugMessages) {LogHelper.info("EntityMonitorHandler > Found villager info on the tracker--must copy to guard");}
            		
            		// If found, copy the data from the villager
                    tracked.updateGuard(event, ims);
            	}
            }
            
        }
        
        
        
        
        
        // --- Monitoring villager trades --- //
        
        else if (
        		event.getEntity() instanceof EntityVillager
        		//&& GeneralConfigHandler.enableCartographer
        		&& !event.getEntity().worldObj.isRemote
        		) {
        	
        	final EntityVillager villager = (EntityVillager) event.getEntity(); // Added final tag in v3.1
        	NBTTagCompound compound = new NBTTagCompound();
        	villager.writeEntityToNBT(compound);
			int profession = compound.getInteger("Profession");
			int career = compound.getInteger("Career");
			int careerLevel = compound.getInteger("CareerLevel");
			//career = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"});
			
        	// Try modifying trades
			// Modified in v3.1trades
            // Check trading list against modern trades
    		IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
			
    		// Get the current buying list
			MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
			if (GeneralConfig.modernVillagerTrades // Added condition in v3.1trades
            		//&& villager.ticksExisted%4==0 // Check only ever four ticks
            		&& (buyingList!=null && buyingList.size()>0)
            		&& ims.getProfessionLevel() < careerLevel)
            {
            	// Modernize the trades
				FunctionsVN.modernizeVillagerTrades(villager);
            }
			
			// If you're talking to a vanilla Villager, check the trades list
			if (profession == 1) {
				
				// summon Villager ~ ~ ~ {Profession:1}
				
				if (
						career == 1 && careerLevel >= 3
						&& GeneralConfig.writtenBookTrade
						) { // Fix the Librarian's written book trade
					
					try {
						
						for (int i=5; i < buyingList.size(); i++) { // The written book trade is at least at index 5
							
							MerchantRecipe extractedRecipe = buyingList.get(i);
							ItemStack itemToBuy1 = extractedRecipe.getItemToBuy();
							ItemStack itemToBuy2 = extractedRecipe.getSecondItemToBuy();
							ItemStack itemToSell = extractedRecipe.getItemToSell();
							
							if (
									itemToBuy1.getItem() == Items.WRITTEN_BOOK && itemToBuy1.stackSize == 2
									&& itemToBuy2 == null
									&& itemToSell.getItem() == Items.EMERALD
									) { // This is the malformed trade. Fix it below.
								buyingList.set(i, new MerchantRecipe( new ItemStack(Items.WRITTEN_BOOK, 1), null, new ItemStack(Items.EMERALD, 1) ));
								if (GeneralConfig.debugMessages) {LogHelper.info("Replacing malformed written book trade for Librarian with ID " + villager.getEntityId());}
								break;
							}
						}
					}
					catch (Exception e) {}
				}
				
				if (
						(
							   (career == 2 && careerLevel > 3)
							|| (career == 1 && careerLevel > 6)
								)
						&& GeneralConfig.enableCartographer
						&& !GeneralConfig.modernVillagerTrades // Added in v3.1trades
						) { // Villager is a Cartographer. Weed out the higher-level trades
					
					try {
						
						if ( buyingList.size() > careerLevel + (career==2 ? 0 : 5) ) {
							
							// First, do a scan and remove duplicates.
							
							for (int i=(career==2 ? 3 : 11); i < buyingList.size(); i++) {
								
								ItemStack stackBuyToCompare  = buyingList.get(i).getItemToBuy();  // Villager BUYS item from you
								ItemStack stackSellToCompare = buyingList.get(i).getItemToSell(); // Villager SELLS item to you
								
								for (int j=buyingList.size()-1; j>i; j--) {
									
									ItemStack stackBuyToEvaluate  = buyingList.get(j).getItemToBuy();
									ItemStack stackSellToEvaluate = buyingList.get(j).getItemToSell();
									
									Set enchantmentCompare  = EnchantmentHelper.getEnchantments(stackSellToCompare).keySet();
									Set enchantmentEvaluate  = EnchantmentHelper.getEnchantments(stackSellToEvaluate).keySet();
									
									if (
											stackBuyToCompare.getItem()  == stackBuyToEvaluate.getItem()
											&& stackSellToCompare.getItem() == stackSellToEvaluate.getItem()
											&& enchantmentCompare.equals(enchantmentEvaluate) // Compares the enchantments of the trades. Both are -1 and so returns "true" if not both are enchanted books.
											) {
										// This is a duplicate trade. Remove it.
										buyingList.remove(j);
									}
								}
							}
							
							// Then, randomly remove entries from the end until the trading list is the right length.
							int loopKiller = 0;
							
							while ( buyingList.size() > careerLevel + (career==2 ? 0 : 5) ) {
								
								int indexToRemove = (careerLevel-1 + (career==2 ? 0 : 5)) + villager.worldObj.rand.nextInt( buyingList.size() - (careerLevel-1 + (career==2 ? 0 : 5)) );
								buyingList.remove( indexToRemove );
								
								loopKiller++;
								if (loopKiller >=100) {
									if (GeneralConfig.debugMessages) {
										LogHelper.warn("Infinite loop suspected while pruning librarian trade list.");
									}
									break;
								}
							}
						}
						
						// Finally, cram the adjusted buying list back into the cartographer.
						ReflectionHelper.setPrivateValue(EntityVillager.class, villager, buyingList, new String[]{"buyingList", "field_70963_i"});
						
					}
					catch (Exception e) {}//Something went wrong.
					
				}
			}
			
			if (ims.getBiomeType()==-1) {ims.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(villager));}
			if (ims.getSkinTone()==-99) {ims.setSkinTone(FunctionsVN.returnSkinToneForEntityLocation(villager));} // v3.2

			// Added in v3.1
			if (
					(villager.ticksExisted + villager.getEntityId())%5 == 0 // Ticks intermittently, modulated so villagers don't deliberately sync.
					// v3.2: changed 5 to 4 because there are no Nitwits, changed profession to forge lookup
					&& ims.getProfession() >= 0 && (ims.getProfession() <=4 || GeneralConfig.professionID_a.indexOf(villager.getProfessionForge().getRegistryName().toString()) != -1) // This villager ID is specified in the configs
					)
					{
				
				//(ExtendedVillager.get( villager )).setProfessionLevel(ExtendedVillager.get( villager ).getProfessionLevel());
				// Sends a ping to everyone within 80 blocks
				NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(villager.dimension, villager.lastTickPosX, villager.lastTickPosY, villager.lastTickPosZ, 16*5);
				VillageNames.VNNetworkWrapper.sendToAllAround(
						new MessageModernVillagerSkin(villager.getEntityId(), profession, career, ims.getBiomeType(), careerLevel, ims.getSkinTone()),
						targetPoint);
			}
			
        }

        // Monitor the player for purposes of the village reputations achievements
        else if (event.getEntity() instanceof EntityPlayerMP
        		&& !event.getEntity().worldObj.isRemote
        		&& event.getEntity().dimension == 0 // Only applies to the Overworld
        		&& event.getEntity().ticksExisted % (tickRate) == 0) { // Only check every few seconds so as not to bog down the server with Village.dat scans
        	
        	EntityPlayerMP player = (EntityPlayerMP)event.getEntity();
        	World world = player.worldObj;
        	
        	try {
        		
            	String villageTopTagPlayerIsIn = ReputationHandler.getVillageTagPlayerIsIn(player);
            	
        		Village villageObjPlayerIsIn = world.villageCollectionObj.getNearestVillage(player.getPosition(), EntityInteractHandler.villageRadiusBuffer);
            	
            	if (
            			!villageTopTagPlayerIsIn.equals("none")
            			|| villageObjPlayerIsIn!=null
            			) { // Player is in a valid Village.dat village.
            		
                	
                	int playerRep = ReputationHandler.getVNReputationForPlayer(player, villageTopTagPlayerIsIn, villageObjPlayerIsIn);
                	
                	// ---- Maximum Rep Achievement ---- //
                	// - Must also be checked onEntity - //
                	if (
                			playerRep <=-30 // Town rep is minimum
                			&& !player.getStatFile().hasAchievementUnlocked(VillageNames.minrep) // Copied over from EntityPlayerMP
                			) {
                		//player.triggerAchievement(VillageNames.minrep);
                		player.addStat(VillageNames.minrep);
                		AchievementReward.allFiveAchievements(player);
                	}
                	
                	// --- Maximum Rep Achievement --- //
                	
                	else if (
                			playerRep >=10 // Town rep is maximum
                			&& !player.getStatFile().hasAchievementUnlocked(VillageNames.maxrep) // Copied over from EntityPlayerMP
                			) {
                		//player.triggerAchievement(VillageNames.maxrep);
                		player.addStat(VillageNames.maxrep);
                		AchievementReward.allFiveAchievements(player);
                	}
                	
                	if (tickRate < 50) tickRate+=2;
                	else if (tickRate > 50) tickRate=50;
                	
            	}
            	else { // Player is not in a valid village.dat village.
            		tickRate = 100; // Slow down the checker when you're not in a village.
            	}
        		
        	}
        	catch (Exception e) {} // Could not verify village status
        	
        	
        }
    }
    
}
