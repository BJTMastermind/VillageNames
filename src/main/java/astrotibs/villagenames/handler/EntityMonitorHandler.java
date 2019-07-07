package astrotibs.villagenames.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import astrotibs.villagenames.VillageNames;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.ieep.ExtendedVillageGuard;
import astrotibs.villagenames.ieep.ExtendedVillager;
import astrotibs.villagenames.ieep.ExtendedZombieVillager;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.network.MessageModernVillagerSkin;
import astrotibs.villagenames.network.MessageZombieVillagerProfession;
import astrotibs.villagenames.network.NetworkHelper;
import astrotibs.villagenames.tracker.ClientInfoTracker;
import astrotibs.villagenames.tracker.EventTracker;
import astrotibs.villagenames.tracker.ServerInfoTracker;
import astrotibs.villagenames.tracker.ServerInfoTracker.EventType;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
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
	
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.entity instanceof EntityVillager) {
            if (FunctionsVN.isVanillaZombie(event.source.getEntity())) {

                // A villager was killed by a zombie and may be zombified. Adds to the tracker for future check.
                final EntityVillager villager = (EntityVillager) event.entity;
                ServerInfoTracker.add(villager);

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
    
	
    @SubscribeEvent
    public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
    	
    	if (!event.entity.worldObj.isRemote) // Encased in notremote if - v3.1
    	{
    		
        	// Added in v3.1
        	if (event.target instanceof EntityVillager) // Removed not-remote condition - v3.1
        	{
        		final EntityVillager villager = (EntityVillager) event.target;
        		final ExtendedVillager properties = ExtendedVillager.get(villager);
        		NetworkHelper.sendModernVillagerSkinMessage(villager.getEntityId(), properties, event.entityPlayer);
        	}
        	
            // Check if the player started tracking a zombie villager (happens on server-side).
            if (FunctionsVN.isVanillaZombie(event.target)) { // Removed not-remote condition - v3.1
                final EntityZombie zombie = (EntityZombie) event.target;
                
                if (zombie.isVillager()) {
                    // Check if the zombie has special properties
                    final ExtendedZombieVillager properties = ExtendedZombieVillager.get(zombie);
                    if (properties != null) {
                        NetworkHelper.sendZombieVillagerProfessionMessage(zombie.getEntityId(), properties, event.entityPlayer);
                    }
                }
            }
            
            // Check if the player started tracking a village guard
            else if (event.entity.getClass().toString().substring(6).equals(ModObjects.WitcheryGuardClass)) { // Removed not-remote condition and added ELSE - v3.1
                //final EntityZombie zombie = (EntityZombie) event.target;
            	final EntityLiving guard = (EntityLiving) event.target;


                // Check if the guard has special properties
                final ExtendedVillageGuard properties = ExtendedVillageGuard.get(guard);
                if (properties != null) {
                    NetworkHelper.sendVillageGuardMessage(guard.getEntityId(), properties, event.entityPlayer);
                }

            }
    		
    	}

        
    }
    

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {

    	// Renovated in v3.1
    	
    	// summon Zombie ~ ~ ~ {IsVillager:1}
    	// New entity is a Zombie. Check to see if it came into being via a killed Villager.
        if (
        		FunctionsVN.isVanillaZombie(event.entity)
        		&& ((EntityZombie)event.entity).isVillager()
        		)
        {
            final EntityZombie zombievillager = (EntityZombie) event.entity;
            
            final ExtendedZombieVillager ezv = ExtendedZombieVillager.get(zombievillager);
            
    		// Try to assign a biome number if this villager has none.
            if (ezv.getBiomeType()<0)
            {
            	ezv.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(zombievillager));
            }
            
            // Renovated in v3.1
            if (event.world.isRemote) {
                // Looks for info sent by the server that should be applied to the zombie (e.g. villager profession)
                ClientInfoTracker.SyncZombieMessage(zombievillager);
            }
            else {
            	
                // Looks on the event tracker for a villager that just died
                //final EventTracker tracked = ServerInfoTracker.seek(EventType.VILLAGER, zombie.getPosition(1.0F));//zombie.getPosition());
            	final EventTracker tracked = ServerInfoTracker.seek(EventType.VILLAGER,
            			//Vec3.createVectorHelper(zombie.posX, zombie.posY, zombie.posZ)
            			new Vec3i(zombievillager.posX, zombievillager.posY + 0.5D, zombievillager.posZ)
            			); // Replaced because of mp server-side crash
            	
                if (tracked != null) {
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("EntityMonitorHandler > Found info on the tracker--must copy to zombie");
                    }

                    // If found, copy the data from the villager
                    tracked.updateZombie(event, ezv);
                }
                else if (ezv.getProfession() == -1) {
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("EntityMonitorHandler > No info on the tracker--assigning a random profession");
                    }

                    // If not, assign a random profession
                    ezv.pickRandomProfessionAndCareer();
                    //properties.pickRandomCareer();
                }

                if (GeneralConfig.debugMessages) {
                    LogHelper.info("EntityMonitorHandler > Custom name [" + zombievillager.getCustomNameTag() + "]");
                    LogHelper.info("EntityMonitorHandler > Profession [" + ezv.getProfession() + "]");
                    LogHelper.info("EntityMonitorHandler > Career [" + ezv.getCareer() + "]");
                    LogHelper.info("EntityMonitorHandler > ProfessionLevel [" + ezv.getProfessionLevel() + "]");
                    LogHelper.info("EntityMonitorHandler > BiomeType [" + ezv.getBiomeType() + "]");
                }
            }

        }
        // New entity is a villager. Check to see if it came into being via a cured villager-zombie.
        else if (event.entity instanceof EntityVillager) {
        	
            final EntityVillager villager = (EntityVillager) event.entity;
            
        	// Added in v3.1
            ExtendedVillager ev = ExtendedVillager.get(villager);
            
            // Renovated in v3.1
            if (event.world.isRemote)
            {
                // Looks for info sent by the server that should be applied to the zombie (e.g. villager profession)
                ClientInfoTracker.syncModernVillagerMessage(villager);
            }
            else
            {
                // Looks on the event tracker for a zombie that was cured
                final EventTracker tracked = ServerInfoTracker.seek(
                		EventType.ZOMBIE,
                		//Vec3.createVectorHelper(villager.posX, villager.posY, villager.posZ) // Replaced because of mp server-side crash
                		new Vec3i(villager.posX, villager.posY + 0.5D, villager.posZ)
                		);//.getPosition());

                if (tracked != null) {
                	// This is a cured Villager Zombie.
                	
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("EntityMonitorHandler > Found info on the tracker--must copy to villager");
                    }

                    // If found, copy the data from the zombie
                    tracked.updateVillager(villager, ev);

                    // Sends info to the special track list
                    ServerInfoTracker.endedCuringZombie(tracked.getEntityID(), villager.getEntityId());

                    ServerInfoTracker.removeCuredZombiesFromTracker(event.world, tracked.getEntityID());

                }
                
                // Added in v3.1
                // Initialize trades so that the villager will be forced to pick a career
                MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
                
                if (buyingList==null || buyingList.size()==0)
                {
                	// Villager has not yet been initialized
            		try {
                    	Method populateBuyingList_m = ReflectionHelper.findMethod(EntityVillager.class, villager, new String[]{"populateBuyingList", "func_175554_cu"});
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
                		
                		if (merchantrecipe.getItemToBuy()==null || merchantrecipe.getItemToBuy().getItem()==Item.getItemFromBlock(Blocks.air)
								|| merchantrecipe.getItemToSell()==null || merchantrecipe.getItemToSell().getItem()==Item.getItemFromBlock(Blocks.air))
                		{
                			if (GeneralConfig.debugMessages) {LogHelper.info("Removing illegal trade at index " + i);} // Added in v3.1trades
                			buyingList.remove(i);
                			} // Remove the offending trade
                	}
                }
                
            }
            
    		// Try to assign a biome number if this villager has none.
            if (
            		ev != null
            		)
            {
        		if (ev.getProfessionLevel() == -1 ) {ev.setProfessionLevel(1);}
        		if (ev.getBiomeType() == -1 ) {ev.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(villager));}
            }
            
        }

    }
    


    @SubscribeEvent
    public void onLivingUpdateEvent(LivingUpdateEvent event) {
    	
        // Check if a zombie is about to convert to villager
        if (FunctionsVN.isVanillaZombie(event.entity)) {
            final EntityZombie zombie = (EntityZombie) event.entity;

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

                                if (block == Blocks.iron_bars || block == Blocks.bed)
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
            	//if (GeneralConfigHandler.debugMessages && (vanillaRollbackTicks != 0 || modTickAdjustment != 0) ) {
            	//	LogHelper.info("Total tick adjustment: " + (vanillaRollbackTicks+modTickAdjustment));
            	//	}
            	//this.accumulatedticks += (vanillaRollbackTicks+modTickAdjustment);
            	//if (GeneralConfigHandler.debugMessages && (vanillaRollbackTicks != 0 || modTickAdjustment != 0) ) {
            	//	LogHelper.info("Cumulative advanced ticks: "+accumulatedticks);
            	//	}
            	
            	
            	int conversionTime=0;
            	
            	try{
            		conversionTime = ReflectionHelper.getPrivateValue(EntityZombie.class, (EntityZombie)event.entity, new String[]{"conversionTime", "field_82234_d"}); // The MCP mapping for this field name
            		// Increment conversion time
            		conversionTime -= (vanillaRollbackTicks+modTickAdjustment);
            		// Cap at 5 minutes
            		conversionTime = MathHelper.clamp_int(conversionTime, 1, 6000);
            		// Set the conversion value to this modified value
            		ReflectionHelper.setPrivateValue(EntityZombie.class, (EntityZombie)event.entity, conversionTime, new String[]{"conversionTime", "field_82234_d"});
            		//if (GeneralConfigHandler.debugMessages) {LogHelper.warn("Setting conversion timer to "+conversionTime);}
    			}
            	catch (Exception e) {
    				//if (GeneralConfigHandler.debugMessages) LogHelper.warn("EntityMonitorHandler > Could not reflect Entity.conversionTime field");
    			}
            	
            	Method getConversionTimeBoost_m = ReflectionHelper.findMethod(EntityZombie.class, (EntityZombie)event.entity, new String[]{"getConversionTimeBoost", "func_82233_q"}); // The MCP mapping for this method name
            	
            	int getConversionTimeBoost=0;
            	
            	try {
            		getConversionTimeBoost = (Integer)getConversionTimeBoost_m.invoke((EntityZombie)event.entity);
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
                        LogHelper.info("EntityMonitorHandler > Zombie " + zombie.toString() + " is about to be cured in tick " + MinecraftServer.getServer().getTickCounter());
                    }
                    ServerInfoTracker.add(zombie);
                }
            }
            
            // Added in v3.1
            if (
            		zombie.isVillager()
            		&& !zombie.worldObj.isRemote
            		)
            {
            	ExtendedZombieVillager ezv = ExtendedZombieVillager.get(zombie);
            	if (ezv.getBiomeType()==-1) {ezv.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(zombie));}
            	
            	zombie.setCanPickUpLoot(false);
            	
            	// Strip gear
                for (int slot=0; slot <=4; slot++) {zombie.setCurrentItemOrArmor(slot, null);}
                // summon Zombie ~ ~ ~ {IsVillager:1}
    			if ((zombie.ticksExisted + zombie.getEntityId())%5 == 0) // Ticks intermittently, modulated so villagers don't deliberately sync.
    			{
    				// Sends a ping to everyone within 80 blocks
    				NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(zombie.dimension, zombie.lastTickPosX, zombie.lastTickPosY, zombie.lastTickPosZ, 16*5);
    				VillageNames.VNNetworkWrapper.sendToAllAround(
    						new MessageZombieVillagerProfession(zombie.getEntityId(), ezv.getProfession(), ezv.getCareer(), ezv.getBiomeType(), ezv.getProfessionLevel()),
    						targetPoint);
    			}
            }
            
        }
        
        

        // New entity is a village guard. Check to see if it came into being via a player's recruitment.
        else if (
        		Loader.isModLoaded("witchery")
        		&& event.entity instanceof EntityLiving
        		&& event.entity.getClass().toString().substring(6).equals(ModObjects.WitcheryGuardClass)
        		&& (EventType.GUARD).getTracker().size() > 0
        		) {
        	
            final EntityLiving guard = (EntityLiving) event.entity;
            
            if (event.entity.worldObj.isRemote) {
                // Looks for info sent by the server that should be applied to the zombie (e.g. villager profession)
                ClientInfoTracker.SyncGuardMessage(guard);
            }
            else {
                // Looks on the event tracker for a villager that just died
                //final EventTracker tracked = ServerInfoTracker.seek(EventType.VILLAGER, zombie.getPosition(1.0F));//zombie.getPosition());
            	final EventTracker tracked = ServerInfoTracker.seek(EventType.GUARD,
            			//Vec3.createVectorHelper(zombie.posX, zombie.posY, zombie.posZ)
            			new Vec3i(guard.posX, guard.posY + 0.5D, guard.posZ)
            			); // Replaced because of mp server-side crash
            	
            	final ExtendedVillageGuard properties = ExtendedVillageGuard.get(guard);

                if (tracked != null) {
                    if (GeneralConfig.debugMessages) {
                        LogHelper.info("EntityMonitorHandler > Found villager info on the tracker--must copy to guard");
                    }

                    // If found, copy the data from the villager
                    tracked.updateGuard(event, properties);
                }

            }
        	
        }
        
        
        // --- Monitoring villager trades --- //
        
        else if (
        		event.entity instanceof EntityVillager
        		//&& GeneralConfigHandler.enableCartographer
        		&& !event.entity.worldObj.isRemote
        		) {
        	
        	final EntityVillager villager = (EntityVillager) event.entity; // Added final tag in v3.1
        	ExtendedVillager ev = ExtendedVillager.get(villager);
        	Random random = villager.worldObj.rand;
        	NBTTagCompound compound = new NBTTagCompound();
        	villager.writeEntityToNBT(compound);
			int profession = compound.getInteger("Profession");
			int career = compound.getInteger("Career");
			//int careerLevel = compound.getInteger("CareerLevel");
			int careerLevel = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerLevel", "field_175562_bw"});
			//career = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"});
			MerchantRecipeList buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
			
			
        	// Try modifying trades
            // Modified in v3.1trades
            // Check trading list against modern trades
            if (GeneralConfig.modernVillagerTrades // Added condition in v3.1trades
            		//&& villager.ticksExisted%4==0 // Check only ever four ticks
            		&& (buyingList!=null && buyingList.size()>0) )
            {
            	// Update the villager's VN career -- used mostly to create the Cartographer or Mason.
            	if (ev.getCareerVN() == 0 )
            	{
        			ev.setCareerVN(
        					(villager.getProfession() == 1 && random.nextBoolean()) ? 2 : // 50% chance a Prof1 will be a Cartographer
        					(villager.getProfession() == 3 && random.nextInt(4)==0) ? 4 : // 25% chance a Prof3 will be a Mason
        					ev.getCareer()
        					);
        		}
            	
            	// Then, modernize the trades
            	if (ev.getProfessionLevelVN() < ev.getProfessionLevel())
            	{
            		FunctionsVN.modernizeVillagerTrades(villager);
            	}
            	
            }
            
			
			// If you're talking to a vanilla Villager, check the trades list
			if (profession == 1) {
				
				// summon Villager ~ ~ ~ {Profession:1}
				
				if (
						career == 1 && careerLevel >= 3
						&& GeneralConfig.writtenBookTrade
						) { // Fix the Librarian's written book trade
					
					try {
						
						// Get the current buying list
						
						for (int i=5; i < buyingList.size(); i++) { // The written book trade is at least at index 5
							
							MerchantRecipe extractedRecipe = buyingList.get(i);
							ItemStack itemToBuy1 = extractedRecipe.getItemToBuy();
							ItemStack itemToBuy2 = extractedRecipe.getSecondItemToBuy();
							ItemStack itemToSell = extractedRecipe.getItemToSell();
							
							if (
									itemToBuy1.getItem() == Items.written_book && itemToBuy1.stackSize == 2
									&& itemToBuy2 == null
									&& itemToSell.getItem() == Items.emerald
									) { // This is the malformed trade. Fix it below.
								buyingList.set(i, new MerchantRecipe( new ItemStack(Items.written_book, 1), null, new ItemStack(Items.emerald, 1) ));
								if (GeneralConfig.debugMessages) {LogHelper.info("Replacing malformed written book trade for Librarian with ID " + villager.getEntityId());}
								break;
							}
						}
					}
					catch (Exception e) {}
				}
				
				// How many high levels are devoted to treasure trades. This limits you from being able to extract every trade from a single Librarian.
				int treasureTradeCount = 3; 
				if ( !GeneralConfig.modernVillagerTrades &&
						(
							   (career == 1 && careerLevel > 6 && careerLevel <= 6 + treasureTradeCount)
							|| (career == 2 && careerLevel > 3 && careerLevel <= 3 + treasureTradeCount)
								)
						&& GeneralConfig.treasureTrades
						) { // Villager is a Cartographer. Weed out the higher-level trades
					
					try {
						
						// Get the current buying list
						buyingList = ReflectionHelper.getPrivateValue( EntityVillager.class, villager, new String[]{"buyingList", "field_70963_i"} );
						
						// If the librarian's career level is greater than a certain trade threshold, add new trades.
						if ( buyingList.size() < careerLevel + (career==2 ? 0 : 5) ) {
							//if (GeneralConfigHandler.debugMessages) {LogHelper.info("Librarian careerLevel is " + careerLevel + " and list length is " + buyingList.size() + ". Adding VN trades.");}
							
							
							
							// Librarian treasure trades
							
							int enchantLevel = 1; //used for level-based enchant books 
							
							// --- MINESHAFT --- //
							enchantLevel = 4 + villager.worldObj.rand.nextInt(2);
					        MerchantRecipe mineshaftForEnchantBook = new MerchantRecipe(
			        				new ItemStack(ModItems.mineshaftbook, 1),
			        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
			        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.efficiency, enchantLevel))
					        		);
							buyingList.add( mineshaftForEnchantBook );
					        
					        enchantLevel = 3;//2 + villager.worldObj.rand.nextInt(2);
					        MerchantRecipe mineshaftForFortuneBook = new MerchantRecipe(
			        				new ItemStack(ModItems.mineshaftbook, 1),
			        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
			        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.fortune, enchantLevel))
					        		);
							buyingList.add( mineshaftForFortuneBook );
					        
					        
					        // --- STRONGHOLD --- //
							enchantLevel = 1;
					        MerchantRecipe strongholdForInfinity = new MerchantRecipe(
			        				new ItemStack(ModItems.strongholdbook, 1),
			        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
			        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.infinity, enchantLevel))
					        		);
							buyingList.add( strongholdForInfinity );
					        
					        
					        // --- FORTRESS --- //
							enchantLevel = 3 + villager.worldObj.rand.nextInt(2);
					        MerchantRecipe fortressForFeatherBook = new MerchantRecipe(
			        				new ItemStack(ModItems.fortressbook, 1),
			        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
			        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.featherFalling, enchantLevel))
					        		);
							buyingList.add( fortressForFeatherBook );
					        
					        
					        // --- MONUMENT --- //
							enchantLevel = 1;
					        MerchantRecipe monumentForAquaBook = new MerchantRecipe(
			        				new ItemStack(ModItems.monumentbook, 1),
			        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
			        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.aquaAffinity, enchantLevel))
					        		);
							buyingList.add( monumentForAquaBook );
					        
							
					        // --- JUNGLE TEMPLE --- //
							enchantLevel = 4 + villager.worldObj.rand.nextInt(2);
					        MerchantRecipe jungleTempleForBaneBook = new MerchantRecipe(
			        				new ItemStack(ModItems.jungletemplebook, 1),
			        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
			        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.baneOfArthropods, enchantLevel))
					        		);
							buyingList.add( jungleTempleForBaneBook );
							
					        
					        // --- DESERT PYRAMID --- //
					        enchantLevel = 3 + villager.worldObj.rand.nextInt(2);
					        MerchantRecipe desertPyramidForBlastProtectionBook = new MerchantRecipe(
			        				new ItemStack(ModItems.desertpyramidbook, 1),
			        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
			        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.blastProtection, enchantLevel))
					        		);
							buyingList.add( desertPyramidForBlastProtectionBook );
							
					        enchantLevel = 4 + villager.worldObj.rand.nextInt(2);
					        MerchantRecipe desertPyramidForSmiteBook = new MerchantRecipe(
			        				new ItemStack(ModItems.desertpyramidbook, 1),
			        				new ItemStack(Items.emerald, Math.min( (villager.worldObj.rand.nextInt(1 + enchantLevel * 9) + 3 * enchantLevel), 64) ),
			        				Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.smite, enchantLevel))
					        		);
							buyingList.add( desertPyramidForSmiteBook );
							
							
							// --- VILLAGE -- //
							
							// Book author
							String bookAuthor = villager.getCustomNameTag();
							
							// Book title
							String bookTitle = "Custom Structure Name";
							switch ( random.nextInt(16) ) {
							case 0:
								// limit:   "--------------------------------"
								bookTitle = "Your Own Unique Location Name";
								//bookTitle = "Your Very Own Unique Location Name"; // Too long
								break;
							case 1:
								// limit:   "--------------------------------"
								bookTitle = "A Random Gobbledygook Name";
								//bookTitle = "A Random Nonsense Gobbledygook Name"; // Too long
								break;
							case 2:
								// limit:   "--------------------------------"
								bookTitle = "Name Things And Influence People";
								//bookTitle = "How To Name Things And Influence People"; // Too long
								break;
							case 3:
								// limit:   "--------------------------------"
								bookTitle = "Deed To A Non-Existent Place";
								//bookTitle = "Deed To A Place That Doesn't Exist"; // Too long
								break;
							case 4:
								bookTitle = "A Brand-New Structure Name";
								break;
							case 5:
								bookTitle = "A New Structure Name For You!";
								break;
							case 6:
								bookTitle = "Naming Things For Dummies";
								break;
							case 7:
								bookTitle = "Naming Things And You";
								break;
							case 8:
								bookTitle = "Live, Laugh, Name, Love";
								break;
							case 9:
								bookTitle = "Markovian Name Generation";
								break;
							case 10:
								bookTitle = "A Tale Of One City Name";
								break;
							case 11:
								bookTitle = "The Case of the Un-Named Thing";
								break;
							case 12:
								bookTitle = "The Unnamed";
								bookAuthor = "H.P. Lovenames";
								break;
							case 13:
								bookTitle = "Custom Structure Name";
								break;
							case 14:
								bookTitle = "Name Inspiration";
								break;
							case 15:
								bookTitle = "A One-Of-A-Kind Title";
								break;
							}
							
							// Book contents
							String bookContents = ""; // Start on line 2
							switch (random.nextInt(8)) {
							case 0:
								bookContents = "If you've gone and built something grand, but don't know what name to give it--why not use this name:";
								break;
							case 1:
								bookContents = "Here's a custom-generated name for you to use, if you wish:";
								break;
							case 2:
								bookContents = "Coming up with names can be difficult. If you're drawing a blank, why not use this name:";
								break;
							case 3:
								bookContents = "Here's a unique name you can give to something if you need some inspiration:";
								break;
							case 4:
								bookContents = Reference.MOD_NAME+" uses a Markov chain to generate names for entities and structures."
										+ " If you've built something and you want to use VN to generate a new name for it, you can use this one:";
								bookAuthor = "AstroTibs";
								break;
							case 5:
								bookContents = "Feeling uninspired? Have writer's block? Feel free to use this customized location name:";
								break;
							case 6:
								bookContents = "Maybe you've just built or discovered something, and you're not sure what to name it. Why not name it this:";
								break;
							case 7:
								bookContents = "Coming up with a good, authentic location name can be hard. Well, this name might be neither good nor authentic, but maybe you'll use it anyway:";
								break;
							}
							
							// Generated name
							String[] locationName = NameGenerator.newRandomName("village-mineshaft-stronghold-temple-fortress-monument-endcity-mansion-alienvillage");
							bookContents += "\n\n" + (locationName[1]+" "+locationName[2]+" "+locationName[3]).trim();
							
							// Put it all together
							ItemStack bookWithName = new ItemStack(Items.written_book);
							if (bookWithName.getTagCompound() == null) {bookWithName.setTagCompound(new NBTTagCompound());} // Priming the book to receive information
							
							bookWithName.getTagCompound().setString("title", bookTitle ); // Set the title
							
							if (bookAuthor!=null && !bookAuthor.equals("")) { // Set the author
								try { bookWithName.getTagCompound().setString("author", bookAuthor.indexOf("(")!=-1 ? bookAuthor.substring(0, bookAuthor.indexOf("(")).trim() : bookAuthor ); }
								// If the target's name starts with a parenthesis for some reason, this will crash with an index OOB exception. In that case, add no author name.
								catch (Exception e) {}
							}
							else {bookWithName.getTagCompound().setString("author", "");}
							
							NBTTagList pagesTag = new NBTTagList();
							pagesTag.appendTag(new NBTTagString(bookContents));
							bookWithName.getTagCompound().setTag("pages", pagesTag);
							
							// Add the trade
					        buyingList.add( new MerchantRecipe(
									new ItemStack(ModItems.villagebook, 1),
									bookWithName) );
							
							
							
							// Cartographer treasure trades
							
							// Potion IDs taken from https://www.minecraftinfo.com/IDList.htm
							
							// --- STRONGHOLD --- //
					        MerchantRecipe strongholdForEnderEye = new MerchantRecipe(new ItemStack(ModItems.strongholdbook, 1), new ItemStack(Items.ender_eye, 2));
					        buyingList.add( strongholdForEnderEye ); // Ender Eye
							
					        // --- FORTRESS --- //
					        MerchantRecipe fortressForFireResistance = new MerchantRecipe( new ItemStack(ModItems.fortressbook, 1), new ItemStack(Items.potionitem, 1, 8259));
							buyingList.add( fortressForFireResistance ); // Fire Resistance (8:00)
							
							// --- SWAMP HUT --- //
							MerchantRecipe swampHutForHarmingPotion = new MerchantRecipe(new ItemStack(ModItems.swamphutbook, 1), new ItemStack(Items.potionitem, 1, 16428) );
							buyingList.add( swampHutForHarmingPotion ); // Splash Harming II
							
							MerchantRecipe swampHutForHealingPotion = new MerchantRecipe(new ItemStack(ModItems.swamphutbook, 1), new ItemStack(Items.potionitem, 1, 8229) );
							buyingList.add( swampHutForHealingPotion ); // Healing II
							
							// --- MONUMENT --- //
							MerchantRecipe monumentForWaterBreathing = new MerchantRecipe(new ItemStack(ModItems.monumentbook, 1), new ItemStack(Items.potionitem, 1, 8269));
							buyingList.add( monumentForWaterBreathing ); // Water Breathing (8:00)
							
							// --- JUNGLE TEMPLE --- //
							MerchantRecipe jungleTempleForStrength = new MerchantRecipe(new ItemStack(ModItems.jungletemplebook, 1), new ItemStack(Items.potionitem, 1, 8233));
							buyingList.add( jungleTempleForStrength ); // Strength II (1:30)
							
							// --- IGLOO --- //
							if (GeneralConfig.addIgloos) {
								MerchantRecipe iglooForGoldenApple = new MerchantRecipe(new ItemStack(ModItems.igloobook, 1), new ItemStack(Items.golden_apple, 1));
								//iglooForGoldenApple.func_82783_a(-6);
								buyingList.add( iglooForGoldenApple );
								
								MerchantRecipe iglooForSplashWeakness = new MerchantRecipe(new ItemStack(ModItems.igloobook, 1), new ItemStack(Items.potionitem, 1, 16456));
								//iglooForSplashWeakness.func_82783_a(-6);
								buyingList.add( iglooForSplashWeakness ); // Splash Weakness (3:00)
							}
							
							// --- VILLAGE -- //
							String[] entityName = NameGenerator.newRandomName("villager-alien-angel-demon-dragon-goblin-golem");
					        ItemStack tagWithName = new ItemStack(Items.name_tag, 1).setStackDisplayName( (entityName[1]+" "+entityName[2]+" "+entityName[3]).trim() );
							tagWithName.setRepairCost(99);
							buyingList.add( new MerchantRecipe(
									new ItemStack(ModItems.villagebook, 1),
									tagWithName) );
					        
						}
						
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
											&& stackSellToCompare.getMetadata() == stackSellToEvaluate.getMetadata()
											&& enchantmentCompare.equals(enchantmentEvaluate) // Compares the enchantments of the trades. Both are -1 and so returns "true" if not both are enchanted books.
											) {
										// This is a duplicate trade. Remove it.
										//if (GeneralConfigHandler.debugMessages) {LogHelper.info("Buying list length " + buyingList.size() + ". Duplicate trade detected at index " + j);}
										buyingList.remove(j);
									}
								}
							}
							
							// Then, randomly remove entries from the end until the trading list is the right length.
							int loopKiller = 0;
							
							while ( buyingList.size() > careerLevel + (career==2 ? 0 : 5) ) {
								
								int indexToRemove = (careerLevel-1 + (career==2 ? 0 : 5)) + villager.worldObj.rand.nextInt( buyingList.size() - (careerLevel-1 + (career==2 ? 0 : 5)) );
								//if (GeneralConfigHandler.debugMessages) {LogHelper.info("Buying list length " + buyingList.size() + ". Removing excess trade at index " + indexToRemove);}
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
        	
			if (ev.getBiomeType()==-1) {ev.setBiomeType(FunctionsVN.returnBiomeTypeForEntityLocation(villager));}
			
			// Added in v3.1
			if (
					(villager.ticksExisted + villager.getEntityId())%5 == 0 // Ticks intermittently, modulated so villagers don't deliberately sync.
					&& ev.getProfession() >= 0 && (ev.getProfession() <=5 || GeneralConfig.professionID_a.indexOf(ev.getProfession())>-1) // This villager ID is specified in the configs
					)
					{
				
				//(ExtendedVillager.get( villager )).setProfessionLevel(ExtendedVillager.get( villager ).getProfessionLevel());
				// Sends a ping to everyone within 80 blocks
				NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(villager.dimension, villager.lastTickPosX, villager.lastTickPosY, villager.lastTickPosZ, 16*5);
				VillageNames.VNNetworkWrapper.sendToAllAround(
						new MessageModernVillagerSkin(villager.getEntityId(), profession, career, ExtendedVillager.get(villager).getBiomeType(), careerLevel,
								ev.getProfessionLevelVN(), ev.getCareerVN()
								),
						targetPoint);
					}
			
        }
        
        // Monitor the player for purposes of the village reputations achievements
        else if (event.entity instanceof EntityPlayerMP
        		&& !event.entity.worldObj.isRemote
        		&& event.entity.dimension == 0 // Only applies to the Overworld
        		&& event.entity.ticksExisted % (tickRate) == 0) { // Only check every few seconds so as not to bog down the server with Village.dat scans
        	
        	EntityPlayerMP player = (EntityPlayerMP)event.entity;
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
                		player.triggerAchievement(VillageNames.minrep);
                		AchievementReward.allFiveAchievements(player);
                	}
                	
                	// --- Maximum Rep Achievement --- //
                	
                	else if (
                			playerRep >=10 // Town rep is maximum
                			&& !player.getStatFile().hasAchievementUnlocked(VillageNames.maxrep) // Copied over from EntityPlayerMP
                			) {
                		player.triggerAchievement(VillageNames.maxrep);
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
    
    
    @SubscribeEvent
    public void onEntityConstructing(EntityConstructing event) {
        
    	// Adds the Extended Properties to zombies
        if (FunctionsVN.isVanillaZombie(event.entity) && ExtendedZombieVillager.get((EntityZombie) event.entity) == null) {
            ExtendedZombieVillager.register((EntityZombie) event.entity);
        }
        
        // Added in v3.1
        else if (
        		event.entity instanceof EntityVillager
        		) {
        	
            final EntityVillager villager = (EntityVillager)event.entity;
            
            // Adds the extended properties to villagers
            if (
            		ExtendedVillager.get(villager) == null // Removed careers condition for v3.1 so that villagers always render
            		) {
            	ExtendedVillager.register(villager);
            }
            
        }
        
        else if (
        		Loader.isModLoaded("witchery") &&
        		event.entity.getClass().toString().substring(6).equals(ModObjects.WitcheryGuardClass)
        		) {
        	
            EntityLiving guard = (EntityLiving)event.entity;
            
            // Adds the extended properties to guards
            if (ExtendedVillageGuard.get(guard) == null) {
            	
            	ExtendedVillageGuard.register(guard);
            	
            }
            
        }
        
    }
    
}
