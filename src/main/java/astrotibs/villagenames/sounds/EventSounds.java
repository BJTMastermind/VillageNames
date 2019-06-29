package astrotibs.villagenames.sounds;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;


public class EventSounds {
	
	
	private double enderEyeSoundRadius = 18.0D;
	
	// 1.9 requires you to register SoundEvent entries
	public static final SoundEvent ENDEREYE_DEAD;
	public static final SoundEvent ENDEREYE_LAUNCH;
	public static final SoundEvent ENDPORTAL_PLACE;
	public static final SoundEvent ENDPORTAL_OPEN;
	
	
	static {
		ENDEREYE_DEAD = registerSound("entity.endereye.dead");
		ENDEREYE_LAUNCH = registerSound("entity.endereye.endereye_launch");
		ENDPORTAL_PLACE = registerSound("block.end_portal.eyeplace");
		ENDPORTAL_OPEN = registerSound("block.end_portal.endportal");
	}
	
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		
		if (!event.getEntityLiving().worldObj.isRemote) {
			
			
			// --- Ender Eye bursts --- //
			
			if (
					GeneralConfig.enderSounds &&
					event.getEntity() instanceof EntityPlayerMP
					) {
				
				List<EntityPlayerMP> allPlayers = Lists.<EntityPlayerMP>newArrayList();
		        
		        for (Object entity : event.getEntity().worldObj.playerEntities) {
		            if ( (EntityPlayerMP.class).isAssignableFrom(entity.getClass()) ) {
		            	allPlayers.add( (EntityPlayerMP) entity );
		            }
		        }
		        
				for (EntityPlayerMP entityplayermp : allPlayers) {
					
					// --- Ender Eye bursts --- //
					
		            World world = event.getEntity().worldObj;//player.worldObj;
		    		
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
		        		    			entityplayermp.worldObj.playSound(
		        		    					(EntityPlayer)null,
		        		    					  eyeX + 0.5
		        		    					, eyeY + 0.5
		        		    					, eyeZ + 0.5
		        		    					, ENDEREYE_DEAD//new SoundEvent(new ResourceLocation(Reference.MOD_ID, "entity.endereye.dead"))
		        		    					, SoundCategory.MASTER
		        		    					, 1.0F, 1.0F);
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
	public void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
		
		
		if ( !event.getEntityPlayer().worldObj.isRemote ) {
			
			// --- Place an Ender Eye into a portal frame block --- //
			
			if (
					(
						(
								event.getHand() ==  EnumHand.MAIN_HAND
								&& event.getEntityPlayer().getHeldItemMainhand() != null
								&& event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.ENDER_EYE
								)
						||
						(
								event.getHand() ==  EnumHand.OFF_HAND
								&& event.getEntityPlayer().getHeldItemOffhand()  != null 
								&& event.getEntityPlayer().getHeldItemOffhand().getItem()  == Items.ENDER_EYE
								)
					)
					&& GeneralConfig.enderSounds
					) {
				
				if (
						//event.action == Action.RIGHT_CLICK_BLOCK &&
						event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.END_PORTAL_FRAME
						) {
					
					if (
							!(((Boolean)(event.getWorld().getBlockState(event.getPos())).getValue(BlockEndPortalFrame.EYE)).booleanValue()) 
							) {
						
						
						
						// Play end eye insertion sound
						
						event.getWorld().playSound(
								(EntityPlayer)null,
							  	event.getPos().getX() + 0.5
							  , event.getPos().getY() + 0.5
							  , event.getPos().getZ() + 0.5
								, ENDPORTAL_PLACE//new SoundEvent(new ResourceLocation(Reference.MOD_ID, "block.end_portal.eyeplace"))
								, SoundCategory.BLOCKS
								, 1.0F, 1.0F);
						
						// Check to see if a portal is opening
						Integer[] portalCenter = endPortalTriggering(event.getWorld(), event.getPos(), event.getEntityPlayer());
						
						if ( portalCenter != null ) {
							
							event.getWorld().playSound(
									(EntityPlayer)null,
									  portalCenter[0] + 0.5
									, portalCenter[1] + 0.5
									, portalCenter[2] + 0.5
									, ENDPORTAL_OPEN//new SoundEvent(new ResourceLocation(Reference.MOD_ID, "block.end_portal.endportal"))
									, SoundCategory.HOSTILE
									, 1.0F, 1.0F);
						}
					}
				}
				/*
				else {
					event.getEntityPlayer().worldObj.playSoundEffect(
							  event.getEntityPlayer().posX + 0.5
							, event.getEntityPlayer().posY + 0.5
							, event.getEntityPlayer().posZ + 0.5
							, Reference.MOD_ID, "entity.endereye.endereye_launch", 1.0F, 1.0F
							);
				}
				*/
			}
		}
	}
	
	
	
	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		
		// --- Throw Ender Eye --- //
		
		if (
				!event.getEntity().worldObj.isRemote
				&& GeneralConfig.enderSounds
				&& event.getEntity() instanceof EntityEnderEye
				) {
			event.getWorld().playSound(
					(EntityPlayer)null,
					  event.getEntity().posX + 0.5
					, event.getEntity().posY + 0.5
					, event.getEntity().posZ + 0.5
					, ENDEREYE_LAUNCH//new SoundEvent(new ResourceLocation(Reference.MOD_ID, "entity.endereye.endereye_launch"))
					, SoundCategory.MASTER
					, 1.0F, 1.0F);
			}
	}
	
	
	
	/**
	 * This takes in an x, y, z and checks whether that block is a an ender portal frame
	 * WITHOUT an eye, and is a component of a portal where all the OTHER blocks have eyes.
	 * In essence, this is called when right-clicking an empty frame block while holding
	 * an ender eye so it will tell you whether you've just activated an End portal. 
	 */
	private Integer[] endPortalTriggering (World world, BlockPos pos, EntityPlayer player) {
		
		Block targetBlock = world.getBlockState(pos).getBlock();
		int targetMeta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
		
		if ( 
				targetBlock == Blocks.END_PORTAL_FRAME
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
					
					if (possibleFrameBlock == Blocks.END_PORTAL_FRAME) {
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
					// This is a newly-completed End Portal frame!
					
					{	// 1.9.4: Scan the nine interior blocks to see if they're air. The portal wil not open if they're not all air.
						
						int interiorAirBlocks = 0;
						
						for (int i=-1; i<2; i++) {
							for (int j=-1; j<2; j++) {
								if (world.getBlockState(
										new BlockPos(
												pos.getX() + portalCenterOffsetX + portalCenterOffsetNudgeX + i,
												pos.getY(),
												pos.getZ() + portalCenterOffsetZ + portalCenterOffsetNudgeZ + j
												)
										).getBlock() == Blocks.AIR) {interiorAirBlocks++;}
							}
						}
						if (interiorAirBlocks==9) {
							// The interior blocks are all air, so the portal will open.
							return new Integer[]{
									pos.getX() + portalCenterOffsetX + portalCenterOffsetNudgeX,
									pos.getY(),
									pos.getZ() + portalCenterOffsetZ + portalCenterOffsetNudgeZ
											};
						}
						else if (GeneralConfig.debugMessages) {
							// Not all interior blocks are air, so notify the player.
							player.addChatComponentMessage(new TextComponentString("The portal will not open if the interior blocks are not all air. "
									+ "You may have to go into Creative mode to solve this problem."));
						}
						else {
							// Display the message in the log instead.
							LogHelper.warn("The portal will not open if the interior blocks are not all air. This problem has to be solved in Creative mode.");
						}
					}
				}
				
			}
			
		}
		
		return null;
	}
	
	
	/**
	 * Adapted from Choonster:
	 * https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/1.9.4/src/main/java/choonster/testmod3/init/ModSoundEvents.java
	 */
	
	/**
	 * Register the {@link SoundEvent}s.
	 */
	public static void registerSounds() {
		// Dummy method to make sure the static initialiser runs
	}

	/**
	 * Register a {@link SoundEvent}.
	 *
	 * @param soundName The SoundEvent's name without the modid prefix
	 * @return The SoundEvent
	 */
	private static SoundEvent registerSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(Reference.MOD_ID.toLowerCase(), soundName);
		return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
	}
	
	
}
