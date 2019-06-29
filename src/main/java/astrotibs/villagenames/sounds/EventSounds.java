package astrotibs.villagenames.sounds;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;


public class EventSounds {
	
	float witchVol = 1.0F;
	float squidvol = -1;
	
	
	@SubscribeEvent
	public void onPlaySoundAtEntityEvent(PlaySoundAtEntityEvent event) {
		
		if (
				!event.entity.worldObj.isRemote
				&& GeneralConfig.witchSounds
				) {
			
			// --- Witch idle --- //
			
			if (event.name.equals("mob.witch.idle")) {
				event.entity.playSound(Reference.MOD_ID+":entity.witch.ambient", witchVol, 1.0F);
			}
			
			// --- Witch hurt --- //
			
			else if (event.name.equals("mob.witch.hurt")) {
				event.entity.playSound(Reference.MOD_ID+":entity.witch.hurt", witchVol, 1.0F);
			}
			
			// --- Witch death --- //
			
			else if (event.name.equals("mob.witch.death")) {
				event.entity.playSound(Reference.MOD_ID+":entity.witch.death", witchVol, 1.0F);
			}
			
		}
		
	}
	
	
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (!event.entityLiving.worldObj.isRemote) {
			
			
			// --- Damage via Thorns --- //
			
			if (
					event.source.damageType.equals("thorns")
					&& GeneralConfig.thornsSound
					) {
				event.source.getEntity().playSound(Reference.MOD_ID+":enchant.thorns", 0.5F, 1.0F);
			}
			
			
			// --- Squid hurt --- //
			
			else if (
					event.entity instanceof EntitySquid && ((EntityLiving)event.entity).getHealth()-event.ammount > 0
					&& GeneralConfig.squidSounds
					) {
				if (squidvol < 0.0F || squidvol > 1.0F) { // This ought to fire just one time during the game.
					this.getSquidVolume(event);
				}
				event.entity.playSound(Reference.MOD_ID+":entity.squid.hurt", squidvol, 1.0F);
			}
			
			
			// --- Witch hurt --- //
			
			else if (
					event.entity instanceof EntityWitch && ((EntityLiving)event.entity).getHealth()-event.ammount > 0
					&& GeneralConfig.witchSounds
					) {
				event.entity.playSound(Reference.MOD_ID+":entity.witch.hurt", witchVol, 1.0F);
			}
			
			
		}
		
	}
	
	
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		if (!event.entityLiving.worldObj.isRemote) {
			
			
			// --- Squid dies --- //
			
			if ( 
					event.entity instanceof EntitySquid
					&& GeneralConfig.squidSounds
					) {
				if (squidvol < 0.0F || squidvol > 1.0F) { // This ought to fire just one time during the game.
					this.getSquidVolume(event);
				}
				event.entity.playSound(Reference.MOD_ID+":entity.squid.death", squidvol, 1.0F);
			}
			
			
			// --- Witch dies --- //
			
			if ( 
					event.entity instanceof EntityWitch
					&& GeneralConfig.witchSounds
					) {
				event.entity.playSound(Reference.MOD_ID+":entity.witch.death", witchVol, 1.0F);
			}
			
			
		}
		
	}
	
	
	
    
	private double enderEyeSoundRadius = 18.0D;
	
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		
		if (!event.entityLiving.worldObj.isRemote) {
			
			
			// --- Squid ambient --- //
			
			if ( 
					event.entity instanceof EntitySquid
					&& GeneralConfig.squidSounds
					) {
				
				if (squidvol < 0.0F || squidvol > 1.0F) { // This ought to fire just one time during the game.
					this.getSquidVolume(event);
				}
				
				Random rand;
				
				try{
					rand = ReflectionHelper.getPrivateValue(Entity.class, event.entity, new String[]{"rand", "field_70146_Z"}); // The MCP mapping for this field name
				} catch (Exception e) {
					rand = event.entity.worldObj.rand;//new Random();
					if (GeneralConfig.debugMessages) LogHelper.warn("Could not reflect Entity.rand field");
				}
				
				if (event.entity.isEntityAlive() && rand.nextInt(1000) < ((EntityLiving)event.entityLiving).livingSoundTime++) {
					event.entity.playSound(Reference.MOD_ID+":entity.squid.ambient", squidvol, 1.0F);
					((EntityLiving)event.entityLiving).livingSoundTime = -((EntityLiving)event.entityLiving).getTalkInterval();
				}
			}
			
			
			
			// --- Witch ambient --- //
			
			if ( 
					event.entity instanceof EntityWitch
					&& GeneralConfig.witchSounds
					) {
				
				Random rand;
				
				try{
					rand = ReflectionHelper.getPrivateValue(Entity.class, event.entity, new String[]{"rand", "field_70146_Z"}); // The MCP mapping for this field name
				} catch (Exception e) {
					rand = event.entity.worldObj.rand;//new Random();
					if (GeneralConfig.debugMessages) LogHelper.warn("Could not reflect Entity.rand field");
				}
				
				if (event.entity.isEntityAlive() && rand.nextInt(1000) < ((EntityLiving)event.entityLiving).livingSoundTime++) {
					event.entity.playSound(Reference.MOD_ID+":entity.witch.ambient", witchVol, 1.0F);
					((EntityLiving)event.entityLiving).livingSoundTime = -((EntityLiving)event.entityLiving).getTalkInterval();
				}
			}
			
			
			
			// --- Ender Eye bursts --- //
			
			else if (
					GeneralConfig.enderSounds &&
					event.entity instanceof EntityPlayerMP
					) {
				
				List<EntityPlayerMP> allPlayers = Lists.<EntityPlayerMP>newArrayList();
		        //LogHelper.info("getPlayers fired");
		        for (Object entity : event.entity.worldObj.playerEntities) {
		            if ( (EntityPlayerMP.class).isAssignableFrom(entity.getClass()) ) {
		            	allPlayers.add( (EntityPlayerMP) entity );
		            }
		        }
		        
				for (EntityPlayerMP entityplayermp : allPlayers) {
					
					// --- Ender Eye bursts --- //
					
		            World world = event.entity.worldObj;//player.worldObj;
		    		
		    		List listEnderEyesInRange = world.getEntitiesWithinAABB(
		    				EntityEnderEye.class, new AxisAlignedBB(
		    						entityplayermp.posX - enderEyeSoundRadius, entityplayermp.posY - enderEyeSoundRadius, entityplayermp.posZ - enderEyeSoundRadius,
		    						entityplayermp.posX + enderEyeSoundRadius, entityplayermp.posY + enderEyeSoundRadius, entityplayermp.posZ + enderEyeSoundRadius
		    						)
		    				);
		    		
		            if (
		            		listEnderEyesInRange != null
		            		) {
		                Iterator iterator = listEnderEyesInRange.iterator();
		                
		                while (iterator.hasNext()) {
		                	
		                	EntityEnderEye entityendereye = (EntityEnderEye)iterator.next();
		                	
		                	double eyeX = entityendereye.posX;
		                	double eyeY = entityendereye.posY;
		                	double eyeZ = entityendereye.posZ;
		                	int despawnTimer = -1;
		                	
		                	
		                	if (
		                			(entityplayermp.posX-eyeX)*(entityplayermp.posX-eyeX) + (entityplayermp.posY-eyeY)*(entityplayermp.posY-eyeY) + (entityplayermp.posZ-eyeZ)*(entityplayermp.posZ-eyeZ) <= enderEyeSoundRadius*enderEyeSoundRadius
		                			) {
		                		try {despawnTimer = ReflectionHelper.getPrivateValue(EntityEnderEye.class, entityendereye, new String[]{"despawnTimer", "field_70223_e"});}
		                        catch (Exception e) {} // Could not extract the despawnTimer value
		                		
		                		if (
		                				despawnTimer==80
		                				) {
		                			
		                			
		                			// Get a list of every player in range of the eye
		                			
		        		    		List listPlayersInRange = world.getEntitiesWithinAABB(
		        		    				EntityPlayerMP.class, new AxisAlignedBB(
		        		    						eyeX - enderEyeSoundRadius, eyeY - enderEyeSoundRadius, eyeZ - enderEyeSoundRadius,
		        		    						eyeX + enderEyeSoundRadius, eyeY + enderEyeSoundRadius, eyeZ + enderEyeSoundRadius
		        		    						)
		        		    				);
		        		    		
		        		    		//java.util.Collections.sort(listPlayersInRange); // Crashes because EntityPlayerMP cannot be cast to java.lang.Comparable
		        		    		
		        		    		if (entityplayermp == listPlayersInRange.get(0)) {
		        		    			entityplayermp.worldObj.playSoundEffect(
		        		    					  eyeX + 0.5
		        		    					, eyeY + 0.5
		        		    					, eyeZ + 0.5
		        		    					, Reference.MOD_ID+":entity.endereye.dead", 1.0F, 1.0F);
		        		    		}
		        		    		
		                		}
		                        
		                	}
		                	
		                }
		            }
				}
			}
		}
		
	}
	
	
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onAttackEntityEvent(AttackEntityEvent event) // Only fires when a player LEFT-CLICKS an entity.
	{
		
		if (!event.target.worldObj.isRemote) {
			
			
			// --- Break an item frame --- //
			
			if (
					event.target instanceof EntityItemFrame
					&& GeneralConfig.hangingSounds
					) { // Only fires when a player LEFT-CLICKS a hanging entity.
				
				EntityItemFrame itemframe = (EntityItemFrame)event.target;
				
				if (
						itemframe.getDisplayedItem() != null
						) { // Knock item out of frame
					event.target.playSound(Reference.MOD_ID+":entity.itemframe.remove_item", 1.0F, 1.0F);
				}
				else { // Knock frame off of wall
					event.target.playSound(Reference.MOD_ID+":entity.itemframe.break", 1.0F, 1.0F);
				}

			}
			
			
			
			// --- Break a Lead Knot --- //
			
			else if (
					event.target instanceof EntityLeashKnot
					&& GeneralConfig.hangingSounds
					) { // Only fires when a player LEFT-CLICKS a hanging entity.
				event.target.playSound(Reference.MOD_ID+":entity.leashknot.break", 1.0F, 1.0F);
			}
			
			
			
			// --- Break a painting (or some other hanging entity) --- //
			
			else if (
					event.target instanceof EntityHanging
					&& GeneralConfig.hangingSounds
					) { // Only fires when a player LEFT-CLICKS a hanging entity.
				event.target.playSound(Reference.MOD_ID+":entity.painting.break", 1.0F, 1.0F);
			}
			
		}
	}
	
	
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		
		if ( !event.entityPlayer.worldObj.isRemote ) {
			
			
			// --- Place a lead knot --- //
			
			Block targetBlock = event.entityPlayer.worldObj.getBlockState(event.pos).getBlock();

	        //if (targetBlock instanceof BlockFence){}
			
			if (	// This has to go FIRST in line, because the sound should play no matter what's in your hand.
					targetBlock != null
					&& targetBlock instanceof BlockFence
					&& isPlayerLeadingAnimal(event.entityPlayer, event.world, event.pos)
					&& GeneralConfig.hangingSounds
					&& event.action == Action.RIGHT_CLICK_BLOCK
					) {event.world.playSoundEffect(
							  event.pos.getX() + 0.5
							, event.pos.getY() + 0.5
							, event.pos.getZ() + 0.5
							, Reference.MOD_ID+":entity.leashknot.place", 1.0F, 1.0F);}
			
			
			
			// --- Place an item frame --- //
			
			else if (
					event.entityPlayer.getHeldItem() != null &&
					event.entityPlayer.getHeldItem().getItem() == Items.item_frame
					&& GeneralConfig.hangingSounds
					&& event.action == Action.RIGHT_CLICK_BLOCK
					&& event.face.getAxis() != EnumFacing.Axis.Y
					//&& event.face >=2 && event.face <=5
					) {
				event.entity.worldObj.playSoundEffect(
						  event.pos.getX() + 0.5 + 0.1*(double)event.face.getFrontOffsetX()
						, event.pos.getY() + 0.5
						, event.pos.getZ() + 0.5 + 0.1*(double)event.face.getFrontOffsetZ()
						, Reference.MOD_ID+":entity.itemframe.place", 1.0F, 1.0F);
				}
			
			
			
			// --- Place a painting --- //
			
			else if (
					event.entityPlayer.getHeldItem() != null &&
					event.entityPlayer.getHeldItem().getItem() == Items.painting
					&& GeneralConfig.hangingSounds
					&& event.action == Action.RIGHT_CLICK_BLOCK
					&& event.face.getAxis() != EnumFacing.Axis.Y
					) {
				event.entity.worldObj.playSoundEffect(
						  event.pos.getX() + 0.5 + 0.1*(double)event.face.getFrontOffsetX()
						, event.pos.getY() + 0.5
						, event.pos.getZ() + 0.5 + 0.1*(double)event.face.getFrontOffsetZ()
						, Reference.MOD_ID+":entity.painting.place", 1.0F, 1.0F);
				}
			
			
			
			// --- Place an Ender Eye into a portal frame block --- //
			
			else if (
					event.entityPlayer.getHeldItem() != null
					&& event.entityPlayer.getHeldItem().getItem() == Items.ender_eye
					&& GeneralConfig.enderSounds
					) {
				
				if (
						event.action == Action.RIGHT_CLICK_BLOCK 
						&& event.world.getBlockState(event.pos).getBlock() == Blocks.end_portal_frame
						) {
					
					if (
							!(((Boolean)(event.world.getBlockState(event.pos)).getValue(BlockEndPortalFrame.EYE)).booleanValue()) 
							) {
						
						// Play end eye insertion sound
						event.entity.worldObj.playSoundEffect(
								  	event.pos.getX() + 0.5
								  , event.pos.getY() + 0.5
								  , event.pos.getZ() + 0.5
								  , Reference.MOD_ID+":block.end_portal.eyeplace", 1.0F, 1.0F);
						
						// Check to see if a portal is opening
						Integer[] portalCenter = endPortalTriggering(event.world, event.pos);
						
						if ( portalCenter != null ) {
							
							event.world.playSoundEffect(
									  portalCenter[0] + 0.5
									, portalCenter[1] + 0.5
									, portalCenter[2] + 0.5
									, Reference.MOD_ID+":block.end_portal.endportal",
									1.0F, 1.0F);
						}
					}
				}
				/*
				else {
					event.entityPlayer.worldObj.playSoundEffect(
							  event.entityPlayer.posX + 0.5
							, event.entityPlayer.posY + 0.5
							, event.entityPlayer.posZ + 0.5
							, Reference.MOD_ID+":entity.endereye.endereye_launch", 1.0F, 1.0F
							);
				}
				*/
			}
		}
	}
	
	
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onEntityInteractEvent(EntityInteractEvent event) { // Only fires when a player RIGHT-CLICKS an entity.
		
		if (!event.target.worldObj.isRemote) {
			
			
			// --- Milk Cow --- //
			
			if (
					event.target instanceof EntityCow
					&& event.entityPlayer.getHeldItem() != null
					&& event.entityPlayer.getHeldItem().getItem() == Items.bucket
					&& !event.entityPlayer.capabilities.isCreativeMode
					&& GeneralConfig.bucketSounds
					) {
				event.target.worldObj.playSoundEffect(
						  event.target.posX
						, event.target.posY
						, event.target.posZ
						, Reference.MOD_ID+":entity.cow.milk", 1.0F, 1.0F);
			}
				
			
			
			// --- Add/Rotate within Item Frame --- //
			
			else if (
					event.target instanceof EntityItemFrame
					&& GeneralConfig.hangingSounds
					) { // Only fires when a player RIGHT-CLICKS a hanging entity.
				EntityItemFrame itemframe = (EntityItemFrame)event.target;
				
				if (
						event.entityPlayer.getHeldItem() != null
						&& itemframe.getDisplayedItem() == null
						) { // Place item into frame
					event.target.worldObj.playSoundEffect(
							  event.target.posX + 0.5
							, event.target.posY + 0.5
							, event.target.posZ + 0.5
							, Reference.MOD_ID+":entity.itemframe.add_item", 1.0F, 1.0F);
				}
				else if (itemframe.getDisplayedItem() != null) { // There is an item in there. Rotate it.
					event.target.worldObj.playSoundEffect(
							  event.target.posX + 0.5
							, event.target.posY + 0.5
							, event.target.posZ + 0.5
							, Reference.MOD_ID+":entity.itemframe.rotate_item", 1.0F, 1.0F);
				}
			}
			
			
			
			// --- Remove a Lead Knot --- //
			
			else if (
					event.target instanceof EntityLeashKnot
					&& GeneralConfig.hangingSounds
					) {
				event.target.worldObj.playSoundEffect(
						  event.target.posX + 0.5
						, event.target.posY + 0.5
						, event.target.posZ + 0.5
						, Reference.MOD_ID+":entity.leashknot.break", 1.0F, 1.0F);
			}
			
		}
		
	}
	
	
	
	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		
		// --- Throw Ender Eye --- //
		
		if (
				!event.entity.worldObj.isRemote
				&& GeneralConfig.enderSounds
				&& event.entity instanceof EntityEnderEye
				) {
			//event.world.playSoundEffect(event.entity.posX, event.entity.posY, event.entity.posZ, Reference.MOD_ID+":entity.endereye.endereye_launch", 1.0F, 1.0F);
			//event.world.playSoundAtEntity(event.entity, Reference.MOD_ID+":entity.endereye.endereye_launch", 1.0F, 1.0F);
			event.world.playSoundEffect(
					  event.entity.posX + 0.5
					, event.entity.posY + 0.5
					, event.entity.posZ + 0.5
					, Reference.MOD_ID+":entity.endereye.endereye_launch", 1.0F, 1.0F);
			}
	}
	
	
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onFillBucketEvent(FillBucketEvent event) {
		
		Block isFull = Blocks.air;
		try {isFull = ReflectionHelper.getPrivateValue(ItemBucket.class, (ItemBucket)event.current.getItem(), new String[]{"isFull", "field_77876_a"});}
		catch (Exception e) {}
		
		
		if (!event.world.isRemote) { // These sounds are played as player events, so should only be played client-side.
			
			
			// --- Pour water --- //
			
			     if ( 
					GeneralConfig.bucketSounds &&
					event.current.getItem()==Items.water_bucket
					) {event.world.playSoundEffect(
							  event.target.getBlockPos().getX() + 0.5
							, event.target.getBlockPos().getY() + 0.5
							, event.target.getBlockPos().getZ() + 0.5
							, Reference.MOD_ID+":item.bucket.pour_water", 1.0F, 1.0F);}
			
			     
			     
			// --- Pour something else --- //
			     
			else if ( 
					GeneralConfig.bucketSounds //&&
					&& !(isFull == Blocks.air)
					) {event.world.playSoundEffect(
							  event.target.getBlockPos().getX() + 0.5
							, event.target.getBlockPos().getY() + 0.5
							, event.target.getBlockPos().getZ() + 0.5
							, Reference.MOD_ID+":item.bucket.pour_lava", 1.0F, 1.0F);}
			     
			     
			     
			// --- Fill with water --- //
			     
			else if ( 
					GeneralConfig.bucketSounds &&
					event.current.getItem()==Items.bucket
					&& event.world.getBlockState(
							event.target.getBlockPos()
							).getBlock().getMaterial() == Material.water
					) {event.world.playSoundEffect(
							  event.target.getBlockPos().getX() + 0.5
							, event.target.getBlockPos().getY() + 0.5
							, event.target.getBlockPos().getZ() + 0.5
							, Reference.MOD_ID+":item.bucket.fill_water", 1.0F, 1.0F);}
			     
			     
			     
			// --- Fill with something else --- //
			     
			else if ( 
					GeneralConfig.bucketSounds &&
					event.current.getItem()==Items.bucket
					&& event.world.getBlockState(
							event.target.getBlockPos()
							).getBlock().getMaterial().isLiquid()
					) {event.world.playSoundEffect(
							  event.target.getBlockPos().getX() + 0.5
							, event.target.getBlockPos().getY() + 0.5
							, event.target.getBlockPos().getZ() + 0.5
							, Reference.MOD_ID+":item.bucket.fill_lava", 1.0F, 1.0F);}
		}
	}
	
	
	/**
	 * Modified from ItemLead.class
	 * This method checks whether you're leading an animal and have clicked on a fence,
	 * causing you to tie the lead to the fence.
	 * If I were to use the original, it would cause issues when called only client-side.
	 * The difference with this version is that it doesn't cause you to tie the lead.
	 */
    public static boolean isPlayerLeadingAnimal(EntityPlayer player, World world, BlockPos pos)
    {
    	
    	EntityLeashKnot entityleashknot = null;
    	try {entityleashknot = EntityLeashKnot.getKnotForPosition(world, pos);;}//EntityLeashKnot.getKnotForPosition(world, pos);}
    	catch (Exception e) {} // There's no leash knot here
        
        boolean flag = false;
        double boxRadius = 7.0D;
        List list = world.getEntitiesWithinAABB(EntityLiving.class,
        		new AxisAlignedBB(
        		(double)pos.getX() - boxRadius, (double)pos.getY() - boxRadius, (double)pos.getZ() - boxRadius,
        		(double)pos.getX() + boxRadius, (double)pos.getY() + boxRadius, (double)pos.getZ() + boxRadius)
        		);
        
        if (list != null)
        {
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                EntityLiving entityliving = (EntityLiving)iterator.next();

                if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == player)
                {
                    
                	if (entityleashknot == null)
                    {
                        flag=true;//entityleashknot = EntityLeashKnot.func_110129_a(world, posX, posY, posZ);
                    }
                	else 
                	{
                		flag=false;
                	}
                    //entityliving.setLeashedToEntity(entityleashknot, true); // THIS is the troublesome line
                    
                }
            }
        }
        return flag;
    }
	
	
	
	/**
	 * This accesses the EntitySquid.getSoundVolume() value, which is 0.4F
	 * This method exists just in case some other mod wants to adjust that value.
	 */
	private void getSquidVolume(LivingEvent event) {
		
		try {
			Method squidvolm = ReflectionHelper.findMethod(EntitySquid.class, (EntitySquid)event.entity, new String[]{"getSoundVolume", "func_70599_aP"}); // The MCP mapping for this method name
			squidvol = (Float)squidvolm.invoke((EntitySquid)event.entity);
			}
		catch (Exception e) {
			squidvol = 0.4F; // The default squid volume
			if (GeneralConfig.debugMessages) LogHelper.warn("Could not reflect EntitySquid.getSoundVolume method");
		}
	}
	
	
	/**
	 * This takes in an x, y, z and checks whether that block is a an ender portal frame
	 * WITHOUT an eye, and is a component of a portal where all the OTHER blocks have eyes.
	 * In essence, this is called when right-clicking an empty frame block while holding
	 * an ender eye so it will tell you whether you've just activated an End portal. 
	 */
	private Integer[] endPortalTriggering (World world, BlockPos pos) {
		
		Block targetBlock = world.getBlockState(pos).getBlock();
		int targetMeta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
		
		if ( 
				targetBlock == Blocks.end_portal_frame
				&& (targetMeta & 4) == 0 // No Ender Eye inserted
				) {
			// This is an end frame without an ender eye.
			
			
			Integer[] xOffsetsToScan = new Integer[]{0,1,2,2,2,1,0,-1,-2,-2,-2,-1};
			Integer[] zOffsetsToScan = new Integer[]{-2,-2,-1,0,1,2,2,2,1,0,-1,-2};
			Integer[] metaRequired   = new Integer[]{4,4,5,5,5,6,6,6,7,7,7,4};
			
			int portalCenterOffsetX = targetMeta==3 ? 2 : (targetMeta==1 ? -2 : 0);
			int portalCenterOffsetZ = targetMeta==0 ? 2 : (targetMeta==2 ? -2 : 0);
			
			Block possibleFrameBlock=null;
			int possibleFrameMeta=-1;
			
			
			for (int nudge = -1; nudge <= 1; nudge++) { // The frame block might not be the center of a side, so check each of the three possible offsets
				
				int portalCenterOffsetNudgeX = targetMeta%2==0 ? nudge : 0;
				int portalCenterOffsetNudgeZ = (targetMeta+1)%2==0 ? nudge : 0;
				
				int eyedCorrectFramePieces = 0;
				int uneyedCorrectFramePieces = 0;
				
				// Scan all 12 blocks around the presumed center of the portal
				for (int i=0; i < xOffsetsToScan.length; i++) {
					
					possibleFrameBlock = world.getBlockState(
							new BlockPos(
							pos.getX() + portalCenterOffsetX + xOffsetsToScan[i] + portalCenterOffsetNudgeX,
							pos.getY(),
							pos.getZ() + portalCenterOffsetZ + zOffsetsToScan[i] + portalCenterOffsetNudgeZ
							)
							).getBlock();
					
					possibleFrameMeta = world.getBlockState(
							new BlockPos(
							pos.getX() + portalCenterOffsetX + xOffsetsToScan[i] + portalCenterOffsetNudgeX,
							pos.getY(),
							pos.getZ() + portalCenterOffsetZ + zOffsetsToScan[i] + portalCenterOffsetNudgeZ
							)
							).getBlock().getMetaFromState(
									world.getBlockState(
											new BlockPos(
											pos.getX() + portalCenterOffsetX + xOffsetsToScan[i] + portalCenterOffsetNudgeX,
											pos.getY(),
											pos.getZ() + portalCenterOffsetZ + zOffsetsToScan[i] + portalCenterOffsetNudgeZ
											)
											)
									);
					
					if (possibleFrameBlock == Blocks.end_portal_frame) {
						if (possibleFrameMeta == metaRequired[i]) {
							eyedCorrectFramePieces++;
						}
						else if (possibleFrameMeta == metaRequired[i]-4) {
							uneyedCorrectFramePieces++;
						}
						else break;
					}
				}
				if (eyedCorrectFramePieces==11 && uneyedCorrectFramePieces==1) {
					// This is a newly-formed End Portal!
					return new Integer[]{
							pos.getX() + portalCenterOffsetX + portalCenterOffsetNudgeX,
							pos.getY(),
							pos.getZ() + portalCenterOffsetZ + portalCenterOffsetNudgeZ
									};
				}
				
			}
			
		}
		
		return null;
	}
	
	
}
