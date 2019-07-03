package astrotibs.villagenames.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import astrotibs.villagenames.advancements.ModTriggers;
import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.integration.ToroQuestWorldDataStructure;
import astrotibs.villagenames.item.ModItems;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.nbt.VNWorldDataStructure;
import astrotibs.villagenames.tracker.ServerInfoTracker;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityInteractHandler {

	//Random random = new Random();
	
	public static int villageRadiusBuffer = 16;
	
	
	// This will only be used for getting the class path to a block
	@SubscribeEvent
	//@SideOnly(Side.CLIENT)
	public void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
		if ( 
				GeneralConfig.debugMessages
				//&& (
				//		event.entityPlayer.inventory.getCurrentItem().getItem() == Items.paper
				//		||event.entityPlayer.inventory.getCurrentItem().getItem() == Items.book )
				&& !event.getWorld().isRemote
				&& event.getHand() == EnumHand.MAIN_HAND
				) {
			IBlockState blockState = event.getWorld().getBlockState(event.getPos());
			Block targetBlock = blockState.getBlock();
			int targetBlockMeta = targetBlock.getMetaFromState(blockState);
			String targetBlockUnlocName = targetBlock.getUnlocalizedName();
			event.getEntityPlayer().sendMessage(new TextComponentString( "Class path of this block: " + targetBlock.getClass().toString().substring(6) ));
			event.getEntityPlayer().sendMessage(new TextComponentString( "Unlocalized name: " + targetBlockUnlocName ));
			event.getEntityPlayer().sendMessage(new TextComponentString( "Meta value: " + targetBlockMeta ));
			event.getEntityPlayer().sendMessage(new TextComponentString( "" ));
		}
	}	
	
	
	@SubscribeEvent(receiveCanceled=true)
	public void onEntityInteract(EntityInteract event) {

		// Added in v3.1
		// This was used to verify server-client syncing of Careers
		/*
		if (GeneralConfig.debugMessages)
		{
			if (event.getTarget() instanceof EntityVillager)
			{
				EntityVillager villager = (EntityVillager)event.getTarget();
				IModularSkin ims = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
				
				// v3.1.1 - Placed into null check to prevent crash
				if (ims != null)
				{
					int profession = ims.getProfession();
					int career = ims.getCareer();
					int biomeType = ims.getBiomeType();
					int professionLevel = ims.getProfessionLevel();
					//int profession = villager.getProfession();
					//int career = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"});
					//int biomeType = 
					//int profLevel = ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerLevel", "field_175562_bw"});
					
					// Modified in v3.1trades
					LogHelper.info("SYNC CHECKING Profession: " + profession +
							", Career: " + ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerId", "field_175563_bv"}) + ", CareerVN: " + career +
							", BiomeType: " + biomeType +
							", careerLevel: " + ReflectionHelper.getPrivateValue(EntityVillager.class, villager, new String[]{"careerLevel", "field_175562_bw"}) + ", ProfessionLevelVN: " + professionLevel);
				}
			}
			
			if (event.getTarget() instanceof EntityZombieVillager)
			{
				EntityZombieVillager zombievillager = (EntityZombieVillager)event.getTarget();
				
				IModularSkin ims = zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
				
				// v3.1.1 - Placed into null check to prevent crash
				if (ims != null)
				{
					int profession = ims.getProfession();
					int career = ims.getCareer();
					int biomeType = ims.getBiomeType();
					int professionLevel = ims.getProfessionLevel();
					LogHelper.info("SYNC CHECKING Profession: " + profession + ", Career: " + career + ", BiomeType: " + biomeType + ", ProfessionLevel: " + professionLevel);
				}
			}
		}
		*/
		
		// summon minecraft:zombie_villager ~ ~ ~
		
		// This is the placeholder for creating Witchery Village Guards
		if (
				Loader.isModLoaded("witchery")
				&& event.getEntity() instanceof EntityPlayer
				&& event.getTarget() instanceof EntityVillager
				&& event.getEntity().isSneaking()
				&& (
						(
								event.getHand() ==  EnumHand.MAIN_HAND
								&& event.getEntityPlayer().getHeldItemMainhand() != null
								&& event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.LEATHER_CHESTPLATE
								)
						||
						(
								event.getHand() ==  EnumHand.OFF_HAND
								&& event.getEntityPlayer().getHeldItemOffhand()  != null 
								&& event.getEntityPlayer().getHeldItemOffhand().getItem()  == Items.LEATHER_CHESTPLATE
								)
					)
				&& event.getEntity().world.villageCollection.getNearestVillage(event.getEntity().getPosition(), villageRadiusBuffer) !=null
				) {
			
            // A player attempted to convert a villager to a witchery guard. Adds to the tracker for future check.
            final EntityVillager villager = (EntityVillager) event.getTarget();
            
            // TODO - for zombie villagers
            ServerInfoTracker.add((EntityLiving)villager, event.getWorld());

            if (GeneralConfig.debugMessages) {
                LogHelper.info("EntityMonitorHandler > A player just attempted to recruit villager " 
                		+ ( villager.getCustomNameTag().equals("")||villager.getCustomNameTag().equals(null) ? "(None)" : villager.getCustomNameTag() ) 
                		+ " [" + villager.getEntityId() + "] "
                		+ "at [" + 
                		//villager.getPosition(1.0F)
                		//Vec3.createVectorHelper(villager.posX, villager.posY, villager.posZ) // Changed because of server crash
                		new Vec3i(villager.posX, villager.posY + 0.5D, villager.posZ)
                		+ "]");
            }
		}
		
		else if (
				event.getEntity() instanceof EntityPlayer		// IF A PLAYER INITIALIZES THIS INTERACTION
				&& !event.getEntityPlayer().world.isRemote	    // WORLD MUST NOT BE CLIENT
				&& event.getTarget() instanceof EntityLiving	// AND THE TARGET IS A LIVING THING
				&& !(event.getTarget() instanceof EntityPlayer)	// BUT NOT A PLAYER
				) {
			
			Random random = event.getEntity().world.rand;
			
			//EntityPlayer player = (EntityPlayer) event.getEntity(); 							// The player
			EntityPlayer player = event.getEntityPlayer();
			//ItemStack itemstack = player.inventory.getCurrentItem();					// What the player is holding
			ItemStack itemstackMain = player.getHeldItemMainhand();
			ItemStack itemstackOff = player.getHeldItemOffhand();
			EntityLiving target = (EntityLiving)event.getTarget();							// The target
			String targetClassPath = event.getTarget().getClass().toString().substring(6);	// The classpath string of the target
			World world = player.world;

			// The coordinates of the target
			double targetX = target.posX;
    		double targetY = target.posY;
    		double targetZ = target.posZ;
			
			String customName = "";
			int targetAge = 0;
			int targetProfession = 0;
			int targetCareer = 0;
			int targetCareerLevel = -1;
			String targetPName = "";
			boolean targetPlayerCreated=false; // Specifically to determine whether an iron golem was made by the player or not
			int villagerMappedProfession = -1; // Should be assigned below
			int tradeSize = 0;

			// Nearest village to the target entity
			Village villageNearTarget = world.getVillageCollection().getNearestVillage(new BlockPos(targetX, targetY, targetZ), villageRadiusBuffer);

			
			// keys: "NameTypes", "Professions", "ClassPaths"
			Map<String, ArrayList> mappedNamesAutomatic = GeneralConfig.unpackMappedNames(GeneralConfig.modNameMappingAutomatic);
			Map<String, ArrayList> mappedNamesClickable = GeneralConfig.unpackMappedNames(GeneralConfig.modNameMappingClickable);

			//if (!world.isRemote) { // Since messages get sent on both sides
				
			// Read the target's NBT data
			NBTTagCompound compound = new NBTTagCompound();
			target.writeEntityToNBT(compound);
			targetProfession = compound.getInteger("Profession");
			targetCareer = compound.getInteger("Career");
			targetCareerLevel = compound.getInteger("CareerLevel");
			targetPName = compound.getString("ProfessionName");
			customName = target.getCustomNameTag();//compound.getString("CustomName");
			targetAge = compound.getInteger("Age");
			targetPlayerCreated = compound.getBoolean("PlayerCreated");
			tradeSize = (target instanceof EntityVillager && (!targetPName.equals(Reference.MOD_ID.toLowerCase()+":nitwit") && targetCareer!=5 )) ?
					( ((EntityVillager)target).getRecipes(player) ).size() : 0;
			
			// Convert a non-vanilla profession into a vanilla one for the purposes of generating a hint page
			Map<String, ArrayList> mappedProfessions = GeneralConfig.unpackMappedProfessions(GeneralConfig.modProfessionMapping);
	    	 // If the below fails, do none

	    	try {
	    		villagerMappedProfession =  
	    				(Integer) ((targetProfession >= 0 && targetProfession <= 5)
	    				? targetProfession : ((mappedProfessions.get("VanillaProfMaps")).get( mappedProfessions.get("IDs").indexOf(targetProfession) )));
	    		}
	    	catch (Exception e) {
	    		if(!event.getEntityLiving().world.isRemote) LogHelper.error("Error evaluating mod profession ID. Check your formatting!");
	    		}
	    	//if (targetPName.equals(Reference.MOD_ID.toLowerCase()+":nitwit")) {villagerMappedProfession = 5;}
	    	
	    	// Primitive Mobs hard coding for career detection
	    	if (targetClassPath.equals( ModObjects.PMTravelingMerchantClass ) )
					{villagerMappedProfession = GeneralConfig.PMMerchantProfessionMap;}
	    	else if (targetClassPath.equals( ModObjects.PMLostMinerClass ) )
					{villagerMappedProfession = GeneralConfig.PMLostMinerProfessionMap;}
	    	else if (targetClassPath.equals( ModObjects.PMSheepmanSmithClass ) )
					{villagerMappedProfession = 3;}

	    	if (target instanceof EntityVillager) {
				targetCareer = ReflectionHelper.getPrivateValue(EntityVillager.class, (EntityVillager)target, new String[]{"careerId", "field_175563_bv"});
				//LogHelper.info("targetCareer = " + targetCareer);
			}
	    	if (GeneralConfig.debugMessages && event.getHand() == EnumHand.MAIN_HAND) {
				player.sendMessage(new TextComponentString("Class path of this entity: " + targetClassPath));
				player.sendMessage(new TextComponentString(""));
				
				
				if (target instanceof EntityVillager) {
					try {
						EntityVillager villager = (EntityVillager)target;
						
						LogHelper.info("Profession: " + targetProfession 
								+ ", Career: " + (villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null)).getCareer()
								+ (GeneralConfig.modernVillagerSkins ? ", BiomeType: " + (villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null)).getBiomeType() // Added in v3.1
										: "")
								+ (GeneralConfig.modernVillagerSkins ? ", Profession Level: " + (villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null)).getProfessionLevel() // Added in v3.1
										: "")
								+ ", Mapped profession: " + villagerMappedProfession);
					}
					catch (Exception e) {}
					
					}
				// Renovated in v3.1
				else if (target instanceof EntityZombieVillager && !(target instanceof EntityPigZombie)) {
					try {
						EntityZombieVillager zombievillager = (EntityZombieVillager)target;
					
					LogHelper.info(
							  (GeneralConfig.modernVillagerSkins ? "Zombie Profession: " + (zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null)).getProfession()
									: "") 
							+ ", Career: " + (zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null)).getCareer()
							+ (GeneralConfig.modernVillagerSkins ? ", BiomeType: " + (zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null)).getBiomeType()
									: "")
							+ (GeneralConfig.modernVillagerSkins ? ", Profession Level: " + (zombievillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null)).getProfessionLevel()
									: "")
							);
					}
					catch (Exception e) {} // This is some kind of non-standard zombie
					}
				
				LogHelper.info("Entity CustomName: " + (customName.equals("")||customName.equals(null) ? "(None)" : customName) + ", Age: " + targetAge +
						(target instanceof EntityIronGolem ? ", PlayerCreated: " + targetPlayerCreated : ""));
				
				try {
					LogHelper.info(player.getDisplayNameString() + " reputation in this village: "
							+ ReputationHandler.getVNReputationForPlayer((EntityPlayerMP) player, ReputationHandler.getVillageTagPlayerIsIn((EntityPlayerMP) player), villageNearTarget)
							);
				}
				catch (Exception e) {}
			}
	    	
	    	
			
	    	//==================================//
	    	//----------- Main Hand ------------//
	    	//==================================//
	    	
	    	// These events check to see if you have particular things in your MAIN hand.
	    	// I have to do it this way so that I can forceably cancel off-hand events.
	    	
	    	
			//----------------------------------//
			//------------ Name Tag ------------//
			//----------------------------------//
	    	
			// If you're holding a name tag in your main hand 
			if (
					(
							(
									//event.getHand() ==  EnumHand.MAIN_HAND
									//&& itemstackMain != null
									   itemstackMain != null
									&& itemstackMain.getItem() == Items.NAME_TAG
									&& itemstackMain.hasDisplayName()
									&& !itemstackMain.getDisplayName().equals(customName)
									&& ( //check to see if the target is a Villager or an entry from the other mod config list.
											(target instanceof EntityVillager && GeneralConfig.nameEntities)
											|| (target instanceof EntityIronGolem && GeneralConfig.nameGolems && !targetPlayerCreated)
											|| mappedNamesAutomatic.get("ClassPaths").contains(targetClassPath)
											|| mappedNamesClickable.get("ClassPaths").contains(targetClassPath)
											)
									)
							//||
							//(
							//		event.getHand() ==  EnumHand.OFF_HAND
							//		&& itemstackOff  != null 
							//		&& itemstackOff.getItem()  == Items.NAME_TAG
							//		&& itemstackOff.hasDisplayName()
							//		&& !itemstackOff.getDisplayName().equals(customName)
							//		)
						)
					&& !player.capabilities.isCreativeMode
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.MAIN_HAND) {
					nameTagEvent(event, world, player);
				}
				else {
					event.setCanceled(true);
				}
				
			}

			//-------------------------------//
			//------------ Poppy ------------//
			//-------------------------------//
			
			// summon minecraft:villager_golem
			
			else if (
					!world.isRemote
					&& event.getEntity().dimension == 0 // Only applies to the Overworld
					//&& itemstack != null
					//&& itemstack.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER) // You present a poppy
					&& (
							(
									//event.getHand() ==  EnumHand.MAIN_HAND
									//&& itemstackMain != null
									itemstackMain != null
									&& itemstackMain.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER)
									)
							//||
							//(
							//		event.getHand() ==  EnumHand.OFF_HAND
							//		&& itemstackOff  != null 
							//		&& itemstackOff.getItem()  == Item.getItemFromBlock(Blocks.RED_FLOWER)
							//		)
						)
					&& target instanceof EntityIronGolem // to an Iron Golem
					&& !targetPlayerCreated // The Golem wasn't created by you
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.MAIN_HAND) {
					poppyEvent(event, player, target, world, villageNearTarget);
				}
				else {
					event.setCanceled(true);
				}
				
			}
			 
			
			//-------------------------------//
			//------------ Codex ------------//
			//-------------------------------//
			
			// If you're holding an emerald or iron/gold ingot,
			else if (
					GeneralConfig.villagerSellsCodex &&
					//itemstack != null
					//&& ( itemstack.getItem() == Items.EMERALD
					//  || itemstack.getItem() == Items.GOLD_INGOT
					//  || itemstack.getItem() == Items.IRON_INGOT )
					(
							itemstackMain != null
						&& (
								(
										//event.getHand() ==  EnumHand.MAIN_HAND
										//&& itemstackMain != null
										itemstackMain.getItem() == Items.EMERALD
										)
								||
								(
										//event.getHand() ==  EnumHand.MAIN_HAND
										//&& itemstackMain != null
										itemstackMain.getItem() == Items.GOLD_INGOT
										)
								||
								(
										//event.getHand() ==  EnumHand.MAIN_HAND
										//&& itemstackMain != null
										itemstackMain.getItem() == Items.IRON_INGOT
										)
							)
						//|| (
						//		(
						//				event.getHand() ==  EnumHand.OFF_HAND
						//				&& itemstackOff  != null 
						//				&& itemstackOff.getItem()  == Items.EMERALD
						//				)
						//		||
						//		(
						//				event.getHand() ==  EnumHand.OFF_HAND
						//				&& itemstackOff  != null 
						//				&& itemstackOff.getItem()  == Items.GOLD_INGOT
						//				)
						//		||
						//		(
						//				event.getHand() ==  EnumHand.OFF_HAND
						//				&& itemstackOff  != null 
						//				&& itemstackOff.getItem()  == Items.IRON_INGOT
						//				)
						//	)
						)
					&& target instanceof EntityVillager
					&& !player.isSneaking() // I'm disallowing you from making a Codex when crouching because it doesn't visually update your itemstacks correctly
					&& targetProfession==1 //check to see if the target is a Villager with Profession 1 (Librarian).
					&& villagerMappedProfession==1 // To prevent things like Traveling Merchant from selling you the Codex
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.MAIN_HAND) {
					codexPurchaseEvent(event, world, player, itemstackMain, itemstackOff, target, targetAge, villageNearTarget);
				}
				else {
					event.setCanceled(true);
				}
				
			}

			
			//------------------------------//
			//------------ Book ------------//
			//------------------------------//
			
			// If you're holding a book...
			else if (
					//itemstack != null
					//&& itemstack.getItem() == Items.BOOK
					(
						(
							//event.getHand() ==  EnumHand.MAIN_HAND
							//&& itemstackMain != null
							itemstackMain != null
							&& itemstackMain.getItem() == Items.BOOK
							)
						//||
						//(
						//	event.getHand() ==  EnumHand.OFF_HAND
						//	&& itemstackOff  != null 
						//	&& itemstackOff.getItem()  == Items.BOOK
						//	)
						)
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.MAIN_HAND) {
					bookEvent(event, world, random, player, target, targetX, targetY, targetZ, targetAge, targetProfession,
				    		targetCareer, targetPName, tradeSize, targetClassPath, villageNearTarget);
				}
				else {
					event.setCanceled(true);
				}
				
			}

			
			//--------------------------------------//
			//------------ Village Book ------------//
			//--------------------------------------//

			// If you're holding a village book, right clicking a villager will return your village reputation. 
			else if (
					//itemstack != null
					//&& itemstack.getItem() == ModItems.villagebook
					(
						(
							//event.getHand() ==  EnumHand.MAIN_HAND
							//&& itemstackMain != null
							itemstackMain != null
							&& itemstackMain.getItem() == ModItems.VILLAGE_BOOK
							)
						//||
						//(
						//	event.getHand() ==  EnumHand.OFF_HAND
						//	&& itemstackOff  != null 
						//	&& itemstackOff.getItem()  == ModItems.VILLAGE_BOOK
						//	)
						)
					&& target instanceof EntityVillager
					&& !world.isRemote
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.MAIN_HAND) {
					villageBookEvent(event, player, villageNearTarget);
				}
				else {
					event.setCanceled(true);
				}
				
			}
	    	
			
			
	    	//==================================//
	    	//------------ Off Hand ------------//
	    	//==================================//
			
			// Does all of the above triggers but checks only the off hand.
			
			
			
			//----------------------------------//
			//------------ Name Tag ------------//
			//----------------------------------//
	    	
			// If you're holding a name tag in your main hand 
			if (
					(
						   itemstackOff != null
						&& itemstackOff.getItem() == Items.NAME_TAG
						&& itemstackOff.hasDisplayName()
						&& !itemstackOff.getDisplayName().equals(customName)
						&& ( //check to see if the target is a Villager or an entry from the other mod config list.
								(target instanceof EntityVillager && GeneralConfig.nameEntities)
								|| (target instanceof EntityIronGolem && GeneralConfig.nameGolems && !targetPlayerCreated)
								|| mappedNamesAutomatic.get("ClassPaths").contains(targetClassPath)
								|| mappedNamesClickable.get("ClassPaths").contains(targetClassPath)
								)
						)
					&& !player.capabilities.isCreativeMode
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.OFF_HAND) {
					nameTagEvent(event, world, player);
				}
				else {
					event.setCanceled(true);
				}
				
			}

			//-------------------------------//
			//------------ Poppy ------------//
			//-------------------------------//
			
			// summon minecraft:villager_golem
			
			else if (
					!world.isRemote
					&& event.getEntity().dimension == 0 // Only applies to the Overworld
					&& (
							itemstackOff != null
							&& itemstackOff.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER)
						)
					&& target instanceof EntityIronGolem // to an Iron Golem
					&& !targetPlayerCreated // The Golem wasn't created by you
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.OFF_HAND) {
					poppyEvent(event, player, target, world, villageNearTarget);
				}
				else {
					event.setCanceled(true);
				}
				
			}
			 
			
			//-------------------------------//
			//------------ Codex ------------//
			//-------------------------------//
			
			// If you're holding an emerald or iron/gold ingot,
			else if (
					GeneralConfig.villagerSellsCodex &&
					(
							itemstackOff != null
						&& (
								   itemstackOff.getItem() == Items.EMERALD
								|| itemstackOff.getItem() == Items.GOLD_INGOT
								|| itemstackOff.getItem() == Items.IRON_INGOT
							)
						)
					&& target instanceof EntityVillager
					&& !player.isSneaking() // I'm disallowing you from making a Codex when crouching because it doesn't visually update your itemstacks correctly
					&& targetProfession==1 //check to see if the target is a Villager with Profession 1 (Librarian).
					&& villagerMappedProfession==1 // To prevent things like Traveling Merchant from selling you the Codex
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.OFF_HAND) {
					codexPurchaseEvent(event, world, player, itemstackMain, itemstackOff, target, targetAge, villageNearTarget);
				}
				else {
					event.setCanceled(true);
				}
				
			}

			
			//------------------------------//
			//------------ Book ------------//
			//------------------------------//
			
			// If you're holding a book...
			else if (
						itemstackOff != null
						&& itemstackOff.getItem() == Items.BOOK
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.OFF_HAND) {
					bookEvent(event, world, random, player, target, targetX, targetY, targetZ, targetAge, targetProfession,
				    		targetCareer, targetPName, tradeSize, targetClassPath, villageNearTarget);
				}
				else {
					event.setCanceled(true);
				}
				
			}

			
			//--------------------------------------//
			//------------ Village Book ------------//
			//--------------------------------------//

			// If you're holding a village book, right clicking a villager will return your village reputation. 
			else if (
						(
								itemstackOff != null
								&& itemstackOff.getItem() == ModItems.VILLAGE_BOOK
							)
					&& target instanceof EntityVillager
					&& !world.isRemote
					) {
				// If all of the above conditions are met, accept the event if it's for the correct hand, and cancel it otherwise.
				if (event.getHand() ==  EnumHand.OFF_HAND) {
					villageBookEvent(event, player, villageNearTarget);
				}
				else {
					event.setCanceled(true);
				}
				
			}
			
			
			
	    	//==================================//
	    	//-------- No more hand BS ---------//
	    	//==================================//
			
			// If neither hand has a triggering item, move on to doing the name generation.
			
			
			//------------------------------//
			//------------ Name ------------//
			//------------------------------//

			// If you're holding anything else (or nothing), check to see if the target is a Villager, Village Golem, or entry from the config list.
			else if (
					!world.isRemote
					&& event.getHand() == EnumHand.MAIN_HAND
					) {

				// Entity is a custom clickable config entry.
				if ( mappedNamesClickable.get("ClassPaths").contains(targetClassPath) ) {

					
					String PMTMUnloc = "Traveling Merchant";
					String PMTMUnlocModern = "Traveling Merchant";
					String PMShUnloc = "Sheepman";
					String PMShUnlocModern = "Sheepman";
					String PMSSUnloc = "Sheepman Smith";
					
					// Unfortunately, I18n are client-side only and will crash the server. So we're going to just have to deal with the English versions.
					if ( (customName.trim()).equals("") || customName.equals(null)
						||
						 (targetClassPath.equals(ModObjects.PMTravelingMerchantClass)
								 //&& customName.equals("Traveling Merchant")
								 && 
								 ( customName.equals( PMTMUnloc )
									|| customName.equals( PMTMUnlocModern ) )// Contingency in there specifically for PM's Traveling Merchants
								 )
						 ||
						 (targetClassPath.equals(ModObjects.PMSheepmanClass)
								 //&& customName.equals("Sheepman")
								 &&
								 ( customName.equals( PMShUnloc )
									|| customName.equals( PMShUnlocModern ) ) // Contingency in there specifically for PM's Sheepman
								 ) // Contingency in there specifically for PM's Sheepmen
						 ||
						 (targetClassPath.equals(ModObjects.PMSheepmanSmithClass)
								 &&
								 ( customName.equals( PMSSUnloc ) ) // Contingency in there specifically for PM's Sheepman Smith
								 ) // Contingency in there specifically for PM's Sheepmen Smith
						 // Hard-wired ToroQuest entries
						 
						 //||
						 //(compound.getString("id")=="toroquest.shopkeeper")
						 //||
						 //(compound.getString("id")=="toroquest.fugitive")
						 
							) {

						// Generate a name type that's defined in the config entry
						
						String mappedNameType = "villager";
						try {
							// Get the index number where targetClassPath is located, and then pull the NameTypes of that same index
							mappedNameType = (String) ((mappedNamesClickable.get("NameTypes")).get( mappedNamesClickable.get("ClassPaths").indexOf(targetClassPath) ));
						}
						catch (Exception e) { // Your config file was poorly formatted
							if(!world.isRemote) LogHelper.error("Your othermods.cfg > Clickable Names entries returned an error. Check to make sure they're formatted properly!");
						}
						
						String[] newNameList = NameGenerator.newRandomName( mappedNameType );
						String newCustomName = ( newNameList[1].trim() + " " + newNameList[2].trim() + " " + newNameList[3].trim() ).trim(); // Generate new name
						// Generate profession tag
						if (
								GeneralConfig.addJobToName
								&& ( !(target instanceof EntityVillager) || targetAge>=0 )
								) {
							newCustomName += " " + NameGenerator.getCareerTag(targetClassPath, targetProfession, targetCareer, targetPName);
						}
						// Apply the name
						target.setCustomNameTag( newCustomName.trim() );
						
					}
					// Clickable Entity already has a name. You may want to add (or remove) a career tag.
					else if (
							customName.indexOf("(")==-1 && GeneralConfig.addJobToName
							&& ( !(target instanceof EntityVillager) || targetAge>=0 )
							) { // Target is named but does not have job tag: add one!
						String newCustomName = customName + " " + NameGenerator.getCareerTag(targetClassPath, targetProfession, targetCareer, targetPName);
						// Apply the name
						target.setCustomNameTag( newCustomName.trim() );
					}
					else if (customName.indexOf("(")!=-1 && !GeneralConfig.addJobToName) { // Target has a job tag: remove it...
						target.setCustomNameTag(customName.substring(0, customName.indexOf("(")).trim());
					}
					
					
				}
				// Entity is a custom automatic config entry.
				else if ( mappedNamesAutomatic.get("ClassPaths").contains(targetClassPath) ) {
					if ( ((customName.trim()).equals("") || customName.equals(null))
							&&
							!((String) ((mappedNamesAutomatic.get("AddOrRemove")).get( mappedNamesAutomatic.get("ClassPaths").indexOf(targetClassPath) ))).trim().equals("remove")
							) {
						
						// Generate a name type that's defined in the config entry
						
						String mappedNameType = "villager";
						try {
							// Get the index number where targetClassPath is located, and then pull the NameTypes of that same index
							mappedNameType = (String) ((mappedNamesAutomatic.get("NameTypes")).get( mappedNamesAutomatic.get("ClassPaths").indexOf(targetClassPath) ));
						}
						catch (Exception e) { // Your config file was poorly formatted
							if(!world.isRemote) LogHelper.error("Your othermods.cfg > Automatic Names entries returned an error. Check to make sure they're formatted properly!");
						}
						
						// Generate a name type that's defined in the config entry
						String[] newNameList = NameGenerator.newRandomName(mappedNameType);
						String newCustomName = ( newNameList[1].trim() + " " + newNameList[2].trim() + " " + newNameList[3].trim() ).trim(); // Generate new name
						// Generate profession tag
						if (
								GeneralConfig.addJobToName
								&& ( !(target instanceof EntityVillager) || targetAge>=0 )
								) {
							newCustomName += " " + NameGenerator.getCareerTag(targetClassPath, targetProfession, targetCareer, targetPName);
						}
						// Apply the name
						target.setCustomNameTag( newCustomName.trim() );
					}
					// Automatic Entity already has a name. You may want to add (or remove) a career tag.
					if (
							customName.indexOf("(")==-1 && GeneralConfig.addJobToName
							&& ( !(target instanceof EntityVillager) || targetAge>=0 )
							) { // Target is named but does not have job tag: add one!
						String newCustomName = customName + " " + NameGenerator.getCareerTag(targetClassPath, targetProfession, targetCareer, targetPName);
						// Apply the name
						target.setCustomNameTag( newCustomName.trim() );
					}
					else if (customName.indexOf("(")!=-1 && !GeneralConfig.addJobToName) { // Target has a job tag: remove it...
						target.setCustomNameTag(customName.substring(0, customName.indexOf("(")).trim());
					}
				}
				// Entity is a Villager
				else if (target instanceof EntityVillager) {
					EntityMonitorHandler.tickRate = 20; // Abruptly speed up the checker to help sync for achievements.
					if (GeneralConfig.nameEntities) {
						// This is a Villager. If it does not have a custom name, add one.
						if ( (customName.trim()).equals("") || customName.equals(null) ) {
							// Generate root name
							String[] newNameList = NameGenerator.newRandomName("Villager");
							String newCustomName = ( newNameList[1].trim() + " " + newNameList[2].trim() + " " + newNameList[3].trim() ).trim(); // Generate new name
							// Generate profession tag
							String careerTag = "";
							if (
									GeneralConfig.addJobToName
									&& ( targetAge>=0 )
									) {
								newCustomName += " " + NameGenerator.getCareerTag(targetClassPath, targetProfession, targetCareer, targetPName);
							}
							// Apply the name
							target.setCustomNameTag( newCustomName.trim() );
						}
						// Villager already has a name. You may want to add (or remove) a career tag.
						else if (
								customName.indexOf("(")==-1 && GeneralConfig.addJobToName
								&& ( targetAge>=0 )
								) { // Villager is named but does not have job tag: add one!
							String newCustomName = customName + " " + NameGenerator.getCareerTag(targetClassPath, targetProfession, targetCareer, targetPName);
							// Apply the name
							target.setCustomNameTag( newCustomName.trim() );
						}
						else if (customName.indexOf("(")!=-1 && !GeneralConfig.addJobToName) { // Villager has a job tag: remove it...
							target.setCustomNameTag(customName.substring(0, customName.indexOf("(")).trim());
						}
						
					}
				}
				// Entity is a Village Golem
				else if (target instanceof EntityIronGolem && GeneralConfig.nameGolems //&& !targetPlayerCreated
						&& ( (customName.trim()).equals("") || customName.equals(null)) ) {
					// This is a village Golem without a name. Determine whether it is player controlled.
					if (targetPlayerCreated) {
						// Send a message stating that it does not (yet) have a name.
						if(!world.isRemote) player.sendMessage(new TextComponentString( "You can give this golem a name tag!" ) );
					}
					else {
						//Determine whether it's in/near a populated village.
						Village villageNearGolem = world.getVillageCollection().getNearestVillage(new BlockPos(targetX, targetY, targetZ), villageRadiusBuffer);
						if (villageNearGolem != null) { // The golem is in/near a village
							if ( (customName.trim()).equals("") || customName.equals(null) ) {
								if (villageNearGolem.getNumVillagers() > 0) {
									// and there is at least one resident
									//If it does not have a custom name, add one.
									String[] newNameList = NameGenerator.newRandomName("Golem");
									String newCustomName = ( newNameList[1].trim() + " " + newNameList[2].trim() + " " + newNameList[3].trim() ).trim(); // Generate new name
									// Does not get a profession tag
									// Apply the name
									target.setCustomNameTag( newCustomName.trim() );
								}
								else if(!world.isRemote) {player.sendMessage(new TextComponentString( "There is nobody left to tell you its name." ) );}
							}
							// Golem already has a name.
						}
						else {
							// Golem is not in a town
							if(!world.isRemote) {player.sendMessage(new TextComponentString( "There is nobody around to tell you its name." ) );}
						}
					}
				}
				
				// Entity is some other type
			}
			
		}

		// Execute healing procedure with Zombie Villager 
		else if (event.getTarget() instanceof EntityZombieVillager) {
            final EntityZombieVillager zombievillager = (EntityZombieVillager) event.getTarget();

            if (!zombievillager.world.isRemote) {

                // Check if the player is holding a regular Golden Apple
                final ItemStack itemstackMain = event.getEntityPlayer().getHeldItemMainhand();
                final ItemStack itemstackOff  = event.getEntityPlayer().getHeldItemOffhand();
                if (
                		//item != null && item.getItem() == Items.GOLDEN_APPLE && item.getItemDamage() == 0
                		(
							(
								event.getHand() ==  EnumHand.MAIN_HAND
								&& itemstackMain != null
								&& itemstackMain.getItem() == Items.GOLDEN_APPLE
								&& itemstackMain.getItemDamage() == 0
								)
							||
							(
								event.getHand() ==  EnumHand.OFF_HAND
								&& itemstackOff  != null 
								&& itemstackOff.getItem()  == Items.GOLDEN_APPLE
								&& itemstackOff.getItemDamage() == 0
								)
							)
                		) {//item.getMetadata() == 0) {

                    // Check if the target is a zombie villager with weakness potion active
                    // Also check if the zombie isn't converting, I only want to track the
                    // player that started the conversion.
                    if (zombievillager.isPotionActive(MobEffects.WEAKNESS) && !zombievillager.isConverting()) {
                    	
                        // Sends info to the special track list
                        ServerInfoTracker.startedCuringZombie(event.getEntityPlayer().getEntityId(), zombievillager.getEntityId(), event.getWorld());
                    }
                }
            }
		}
	}
	
	
    // Used to check against the Mininum Rep achievement.
    // Will also be used to monitor player rep in 1.9+
    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent event) {
    	
    	if (!event.getTarget().world.isRemote
    		&& event.getEntity().dimension == 0 // Only applies to the Overworld
    		&& event.getTarget() instanceof EntityLiving) {
    		EntityLiving target = (EntityLiving) event.getTarget();
        	
        	if (event.getTarget() instanceof EntityVillager
        			|| event.getTarget() instanceof EntityIronGolem
        			|| event.getTarget().getClass().getClass().toString().substring(6).equals(ModObjects.WitcheryGuardClass)
        			) {
        		EntityMonitorHandler.tickRate = 10; // Abruptly speed up the checker to help sync for achievements.
        		Village villageNearTarget = target.world.getVillageCollection().getNearestVillage(
        				new BlockPos(MathHelper.floor(target.posX), MathHelper.floor(target.posY), MathHelper.floor(target.posZ)), villageRadiusBuffer);
        		EntityPlayerMP playerMP = (EntityPlayerMP) event.getEntityPlayer();
        		try {
        			// Get the attacker's reputation
        			int playerRep = ReputationHandler.getVNReputationForPlayer(playerMP, ReputationHandler.getVillageTagPlayerIsIn(playerMP), villageNearTarget);
        			
        			if (
        					playerRep <=-30 // Town rep is minimum
							&& !playerMP.getServer().getPlayerList().getPlayerAdvancements(playerMP).getProgress(
									playerMP.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MOD_ID+":minrep"))
            						).isDone()
                			) {
        				//playerMP.triggerAchievement(VillageNames.minrep);
        				//playerMP.addStat(VillageNames.minrep);
        				ModTriggers.MINREP.trigger(playerMP);
        				AdvancementReward.allFiveAdvancements(playerMP);
                	}
        		}
        		catch (Exception e) {}
        	}
    	}
    	
    }
    
    
    
    

	/**
	 * Generates a "baby villager can't help" string from a pool of possibilities.
	 * Setting forType to "book" or "codex" will add possibilities unique to that type. 
	 */
	public static String babyCantHelpString(String forType) {
		Random random = new Random();
		List<String> babyCantHelpArray = new ArrayList<String>();
		
		// Add in universal messages
		babyCantHelpArray.add("This villager is too young to help you.");
		babyCantHelpArray.add("The child looks uncomfortable with you.");
		babyCantHelpArray.add("You should probably ask an adult.");
		babyCantHelpArray.add("This child just wants to play!");
		babyCantHelpArray.add("This child just wants to frolick!");
		babyCantHelpArray.add("The child looks around nervously.");
		babyCantHelpArray.add("This child is still developing language.");
		babyCantHelpArray.add("Why would you ask a child for such information? That's a bit odd.");
		babyCantHelpArray.add("The child reaches out with dirtied hands. Perhaps you should find another villager.");
		babyCantHelpArray.add("Stop bothering children with this.");
		babyCantHelpArray.add("The child looks away sheepishly.");
		babyCantHelpArray.add("The child sticks out its tongue. This is not productive.");
		// Add in specialized messages
		if ((forType.toLowerCase()).equals("book")) {
			babyCantHelpArray.add("This child is not interested in busywork.");
			babyCantHelpArray.add("Who wants to do homework? Not this kid.");
		}
		else if ((forType.toLowerCase()).equals("codex")) {
			babyCantHelpArray.add("Bookkeeping work is something to grow into.");
			babyCantHelpArray.add("This child is not a full-fledged librarian yet.");
		}
		
		return babyCantHelpArray.get( random.nextInt(babyCantHelpArray.size()) );
	}

    /**
     * This method is called when you have to generate a random confusion message from a villager.
     */
    public static void villagerConfused(EntityPlayer player) {
    	boolean messagetype = new Random().nextInt(2) == 0;
    	
    	List<String> cantHelpAdjective = new ArrayList<String>();
    	cantHelpAdjective.add("baffled");
    	cantHelpAdjective.add("befuddled");
    	cantHelpAdjective.add("bewildered");
    	cantHelpAdjective.add("clueless");
    	cantHelpAdjective.add("confused");
    	cantHelpAdjective.add("dumbfounded");
    	cantHelpAdjective.add("mystified");
    	cantHelpAdjective.add("nonplussed");
    	cantHelpAdjective.add("perplexed");
    	cantHelpAdjective.add("puzzled");
    	cantHelpAdjective.add("disoriented");
    	if (messagetype) {
    		cantHelpAdjective.add("lost");
    	}
    	else{
    		cantHelpAdjective.add("helpless");
    	}
    	
		if (!player.world.isRemote) player.sendMessage(new TextComponentString(
				messagetype ?
				"The villager seems " + cantHelpAdjective.get(new Random().nextInt(cantHelpAdjective.size())) + ", and glances around." :
				"The villager gives you a " + cantHelpAdjective.get(new Random().nextInt(cantHelpAdjective.size())) + " look."
				) );
    }

    /**
     * This method generates a random flavor text depending on the player's village reputation.
     * @return String
     */
    public static String villagerAssessReputation(int playerRep) {
    	// Default strings so that we can have something in case there is an error
    	String[] villagerAssessmentPool = new String[]{
				"You are unable to gauge the villager's opinion of you.",
				"You are unsure how the village feels about you.",
				"This villager isn't interested in gossip.",
				"This villager doesn't know what the others think of you."
			};
    	
    	if (playerRep >= 7) {
    		// Extremely positive reputation
    		villagerAssessmentPool = new String[]{
    				"You have brought great prosperity to this town.",
    				"You are very well regarded in this town!",
    				"You are notably reputable in this village!",
    				"The town views you very favorably!"
    			};
    	}
    	else if (playerRep >= 4) {
    		// Very positive reputation
    		villagerAssessmentPool = new String[]{
    				"The villagers like having you around!",
    				"This villager has heard very good things about you!",
    				"The villagers here would like to get to know you!",
    				"You are well regarded by this town."
    			};
    	}
    	else if (playerRep >= 2) {
    		// Moderately positive reputation
    		villagerAssessmentPool = new String[]{
    				"This villager has heard good things about you!",
    				"There is a general air of cautious trust regarding you.",
    				"You are well regarded by a couple villagers.",
    				"Your presence gives the town some optimism!"
    			};
    	}
    	else if (playerRep >= 1) {
    		// Slightly positive reputation
    		villagerAssessmentPool = new String[]{
    				"The villagers don't mind you being around.",
    				"The villagers are cautious about you, but somewhat interested.",
    				"This villager seems wary, but curious about you.",
    				"You are a stranger, but the villagers seem interested."
    			};
    	}
    	else if (playerRep >= 0) {
    		// Neutral reputation
    		villagerAssessmentPool = new String[]{
    				"The villagers are ambivalent to you.",
    				"The villagers don't know what to make of you.",
    				"Nobody knows what to make of you.",
    				"The villagers are indifferent to you."
    			};
    	}
    	else if (playerRep >= -3) {
    		// Slightly negative reputation
    		villagerAssessmentPool = new String[]{
    				"There are unsavory rumors around town about you.",
    				"This villager seems unhappy with you.",
    				"The village doesn't trust you.",
    				"This villager seems worried about you."
    			};
    	}
    	else if (playerRep >= -9) {
    		// Moderately negative reputation
    		villagerAssessmentPool = new String[]{
    				"You are viewed as a danger.",
    				"Your misdeeds are known throughout the town.",
    				"This villager is visibly afraid of you.",
    				"The village sees you as a menace."
    			};
    	}
    	else if (playerRep >= -18) {
    		// Very negative reputation
    		villagerAssessmentPool = new String[]{
    				"The villagers are all afraid of you.",
    				"Your cruel acts are known to everyone.",
    				"The villagers regard you as evil.",
    				"No villager will sleep soundly until you're gone."
    			};
    	}
    	else {
    		// Extremely negative reputation
    		villagerAssessmentPool = new String[]{
    				"Your vile acts have brought tragedy to this town.",
    				"You have brought devastation to this village.",
    				"You are the stuff of nightmares.",
    				"You are unquestionably wicked."
    			};
    	}
    	return villagerAssessmentPool[new Random().nextInt(villagerAssessmentPool.length)];
    }
    
    
    
    
    
    
    // -------------------------------------- //
    // -------- Double-down methods --------- //
    // -------------------------------------- //
    
    // In 1.12, it seems that I have to fully account for entity event firing for each hand.
    // Below are going to be methods that encapsulate the mechanics for each "if" outcome
    // so that it's easier to write the entire event listener.
    
    private static void nameTagEvent(EntityInteract event, World world, EntityPlayer player) {
    	// If so, you should be prevented from naming the entity.
		event.setCanceled(true);
		if (!world.isRemote) player.sendMessage(new TextComponentString("That is not its name!"));
		//target.setCustomNameTag(customName);
    }
    
    
    private static void poppyEvent(EntityInteract event, EntityPlayer player, EntityLiving target, World world, Village villageNearTarget) {
    	
		int population = -1;
		int reputation = ReputationHandler.getVNReputationForPlayer((EntityPlayerMP) player, ReputationHandler.getVillageTagPlayerIsIn((EntityPlayerMP) player), villageNearTarget);
		
		try {
			population = villageNearTarget.getNumVillagers();
		}
		catch (Exception e) {
			// Failed to evaluate village, or there is none.
		}
		
		// Check if the player is inside a village bounding box
		boolean playerIsInVillage = false;

		try{
			// Load in the vanilla structure file
			MapGenStructureData structureData = (MapGenStructureData)world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Village");
			NBTTagCompound nbttagcompound = structureData.getTagCompound();
			
			// Iterate through the entries
			Iterator itr = nbttagcompound.getKeySet().iterator();
			
			while (itr.hasNext()) {
				Object element = itr.next();
				
				NBTBase nbtbase = nbttagcompound.getTag(element.toString());
				
				if (nbtbase.getId() == 10) {
					NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
					
					try {
						int[] boundingBox = nbttagcompound2.getIntArray("BB");
						// Now check to see if the player is inside the feature
						if (
							   player.posX >= boundingBox[0]
							&& player.posY >= boundingBox[1]
							&& player.posZ >= boundingBox[2]
							&& player.posX <= boundingBox[3]
							&& player.posY <= boundingBox[4]
							&& player.posZ <= boundingBox[5]
							) {
							playerIsInVillage = true;
						}
					}
					catch (Exception e) {
						// There's a valid entry, but there's no bounding box tag. This is an error.
					}
				}
			}
			
		}
		catch (Exception e) {} // Not inside a valid Village BB, so playerIsInVillage will remain as false

		EntityPlayer thePlayer = event.getEntityPlayer();
        
		if (   
				(population == 0 || (playerIsInVillage && population == -1)) // No Villagers in the village
				&& reputation >= -15 // This may be redundant in the event of an empty village
				&& thePlayer instanceof EntityPlayerMP
				&& !thePlayer.getServer().getPlayerList().getPlayerAdvancements((EntityPlayerMP)thePlayer).getProgress(
						thePlayer.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MOD_ID+":laputa"))
						).isDone()
				) {
			EntityIronGolem ironGolemTarget = (EntityIronGolem) target;
			EntityPlayerMP thePlayerMP = (EntityPlayerMP)thePlayer;
			
			// Play a confirmation sound effect
			
			// Consume the poppy
			if (!player.capabilities.isCreativeMode) {player.inventory.clearMatchingItems( Item.getItemFromBlock(Blocks.RED_FLOWER), -1, 1, null );}
			
			// Give the golem the poppy
			ironGolemTarget.setHoldingRose(true);
			
			// Spawn a heart particle
			
			// Switch him over to your side
			ironGolemTarget.setPlayerCreated(true);
			
			// Trigger the achievement
			//player.triggerAchievement(VillageNames.laputa);
			//player.addStat(VillageNames.laputa);
			ModTriggers.LAPUTA.trigger(thePlayerMP);
			AdvancementReward.allFiveAdvancements(thePlayerMP);
		}
    	
    }
	
    
    public static void codexPurchaseEvent(
    		EntityInteract event, World world, EntityPlayer player, ItemStack itemstackMain, ItemStack itemstackOff,
    		EntityLiving target, int targetAge, Village villageNearTarget
    		) {
    	
		event.setCanceled(true);
		// If so, you can do the Codex interaction.
		if ( targetAge < 0 ) {
			// Villager is a baby, so can't make you a codex.
			if (!event.getEntityPlayer().world.isRemote) { // Messages get sent on both sides.
				babyCantHelpString("codex");
			}
		}
		else { // Villager is an adult. Proceed.

			// Finds the nearest village to this target, but only if the villager's coordinates are within its bounding radius plus the buffer
			
			if (villageNearTarget != null) { // The Villager is inside/near a village

				int playerRep = ReputationHandler.getVNReputationForPlayer((EntityPlayerMP) player, ReputationHandler.getVillageTagPlayerIsIn((EntityPlayerMP) player), villageNearTarget);
				
				if (playerRep < 0) { // Your reputation is too low.
					if (!world.isRemote) {player.sendMessage(new TextComponentString( "The villager does not trust you." ) );}
				}
				else {
					// The villager trusts you.
    				// Now we construct a codex based on materials you're offering.
					
    				// Search the player's inventory, and sum up how many resources they have.
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

    				// We have the totals for emeralds and iron/gold ingots, and the player's rep.
    				// Now let's actually do the exchange.
    				
    				// Adjust the material requirements based on your reputation
    				int emeraldRequired = 4 - (playerRep+2)/5;
    				int ironRequired = 8 - ((playerRep+1)*5)/12;
    				int goldRequired = 4 - (playerRep+2)/5;
    				
    				if (GeneralConfig.debugMessages && !world.isRemote) {LogHelper.info("Your reputation: "+playerRep+", Emeralds req: "+emeraldRequired+", Iron req: "+ironRequired+", Gold req: "+goldRequired);}
    				
    				// If the item in your hand is an emerald, trade with just emeralds.
    				if (
    						//itemstack.getItem() == Items.EMERALD
							(
								(
									event.getHand() ==  EnumHand.MAIN_HAND
									&& itemstackMain != null
									&& itemstackMain.getItem() == Items.EMERALD
									)
								||
								(
									event.getHand() ==  EnumHand.OFF_HAND
									&& itemstackOff  != null 
									&& itemstackOff.getItem()  == Items.EMERALD
									)
								)
    						) {

    					if (emeralds >= emeraldRequired+1
    							|| player.capabilities.isCreativeMode) { // Allow Creative players to get Codexes regardless of emerald count
    						
    						if (!player.capabilities.isCreativeMode) { // Consume the emeralds only if you're in survival
    							player.inventory.clearMatchingItems(Items.EMERALD, -1, emeraldRequired+1, null);
    						}
    						
    						EntityItem eitem = (GeneralConfig.villagerDropBook ? target : player).entityDropItem(new ItemStack(ModItems.CODEX), 1);
        			        eitem.setNoPickupDelay(); // No delay: directly into the inventory!
    					}
    					else if (!world.isRemote) {player.sendMessage(new TextComponentString( "The Villager wants more emeralds from you." ) );}
    				}
    				// If the item in your hand is a gold ingot, trade both gold ingots and emeralds.
    				else if (
    						//itemstack.getItem() == Items.GOLD_INGOT
    						(
								(
									event.getHand() ==  EnumHand.MAIN_HAND
									&& itemstackMain != null
									&& itemstackMain.getItem() == Items.GOLD_INGOT
									)
								||
								(
									event.getHand() ==  EnumHand.OFF_HAND
									&& itemstackOff  != null 
									&& itemstackOff.getItem()  == Items.GOLD_INGOT
									)
								)
    						) {

    					if ( (emeralds >= emeraldRequired && goldIngots >= goldRequired)
    							|| player.capabilities.isCreativeMode) { // Allow Creative players to get Codexes regardless of emerald/gold count
    						
    						if (!player.capabilities.isCreativeMode) { // Consume the items only if you're in survival
    							player.inventory.clearMatchingItems(Items.EMERALD, -1, emeraldRequired, null);
            					player.inventory.clearMatchingItems(Items.GOLD_INGOT, -1, goldRequired, null);
    						}
    						
    						EntityItem eitem = (GeneralConfig.villagerDropBook ? target : player).entityDropItem(new ItemStack(ModItems.CODEX), 1);
        			        eitem.setNoPickupDelay(); // No delay: directly into the inventory!
							
    					}
    					else if (!world.isRemote) { // Messages send to both sides
    						if (emeralds < emeraldRequired && goldIngots < goldRequired) {player.sendMessage(new TextComponentString( "The Villager wants more emeralds and gold from you." ) );}
    						else if (emeralds < emeraldRequired) {player.sendMessage(new TextComponentString( "The Villager wants more emeralds from you." ) );}
    						else if (emeralds < goldRequired) {player.sendMessage(new TextComponentString( "The Villager wants more gold from you." ) );}
    					}
    					
    				}
    				// If the item in your hand is an iron ingot, trade both iron ingots and emeralds.
    				else if (
    						//itemstack.getItem() == Items.IRON_INGOT
    						(
								(
									event.getHand() ==  EnumHand.MAIN_HAND
									&& itemstackMain != null
									&& itemstackMain.getItem() == Items.IRON_INGOT
									)
								||
								(
									event.getHand() ==  EnumHand.OFF_HAND
									&& itemstackOff  != null 
									&& itemstackOff.getItem()  == Items.IRON_INGOT
									)
								)
    						) {

    					if ( (emeralds >= emeraldRequired && ironIngots >= ironRequired)
    							|| player.capabilities.isCreativeMode) { // Allow Creative players to get Codexes regardless of emerald/iron count
    						
    						if (!player.capabilities.isCreativeMode) { // Consume the items only if you're in survival
    							player.inventory.clearMatchingItems(Items.EMERALD, -1, emeraldRequired, null);
            					player.inventory.clearMatchingItems(Items.IRON_INGOT, -1, ironRequired, null);
    						}
    						
    						EntityItem eitem = (GeneralConfig.villagerDropBook ? target : player).entityDropItem(new ItemStack(ModItems.CODEX), 1);
        			        eitem.setNoPickupDelay(); // No delay: directly into the inventory!
							
    					}
    					else if (!world.isRemote) { // Messages send to both sides
    						if (emeralds < emeraldRequired && ironIngots < ironRequired) {player.sendMessage(new TextComponentString( "The Villager wants more emeralds and iron from you." ) );}
    						else if (emeralds < emeraldRequired) {player.sendMessage(new TextComponentString( "The Villager wants more emeralds from you." ) );}
    						else if (emeralds < ironRequired) {player.sendMessage(new TextComponentString( "The Villager wants more iron from you." ) );}
    					}
    				}
				}
			}
			else {// No nearby villages found: let the user know that s/he can't get a Codex.
				villagerConfused(player);
			}
		}
    	
    }
    
    
    
    private static void bookEvent(
    		EntityInteract event, World world, Random random, EntityPlayer player,
    		EntityLiving target, double targetX, double targetY, double targetZ, int targetAge, int targetProfession,
    		int targetCareer, String targetPName, int tradeSize, String targetClassPath, Village villageNearTarget
    		) {
    	
		// The target is a Villager
		if (target instanceof EntityVillager) {
			event.setCanceled(true);
			EntityVillager villager = (EntityVillager)target;
			if ( targetAge >= 0 ) { // Villager is an adult.
				if ( villageNearTarget == null || player.dimension != 0 ) { // There is no town.
					if (!world.isRemote) {villagerConfused(player);}
				}
				else { // There is a town.
					int playerRep = ReputationHandler.getVNReputationForPlayer((EntityPlayerMP) player, ReputationHandler.getVillageTagPlayerIsIn((EntityPlayerMP) player), villageNearTarget);
					
					if (playerRep < 0) { // Your reputation is too low.
						if (!world.isRemote) {player.sendMessage(new TextComponentString( "The villager does not trust you." ) );}
        			}
					else { // The Villager trusts you.
						int villageRadius = villageNearTarget.getVillageRadius();
						int popSize = villageNearTarget.getNumVillagers();
						int centerX = villageNearTarget.getCenter().getX(); // Village X position
            			int centerY = villageNearTarget.getCenter().getY(); // Village Y position
            			int centerZ = villageNearTarget.getCenter().getZ(); // Village Z position
            			
            			// Let's see if we can find a sign near that located village center!
            			VNWorldDataStructure data = VNWorldDataStructure.forWorld(world, "villagenames3_Village", "NamedStructures");
            			// .getTagList() will return all the entries under the specific village name.
    					NBTTagCompound tagCompound = data.getData();
    					Set tagmapKeyset = tagCompound.getKeySet(); //Gets the town key list: "coordinates"

    					Iterator itr = tagmapKeyset.iterator();
    			        String townSignEntry;
    			        
    			        //Placeholders for villagenames.dat tags
    			        boolean signLocated = false; //Use this to record whether or not a sign was found
    			        boolean isColony = false; //Use this to record whether or not the village was naturally generated

    			        while(itr.hasNext()) { // Going through the list of VN villages
    			        	Object element = itr.next();
    			        	townSignEntry = element.toString(); //Text name of village header (e.g. "x535y80z39")
    			        	//The only index that has data is 0:
    			            NBTTagCompound tagList = tagCompound.getTagList(townSignEntry, tagCompound.getId()).getCompoundTagAt(0);
    			            // Retrieve the "sign" coordinates
    			            int townX = tagList.getInteger("signX");
    			            int townY = tagList.getInteger("signY");
    			            int townZ = tagList.getInteger("signZ");
    			            String namePrefix = tagList.getString("namePrefix");
    			            String nameRoot = tagList.getString("nameRoot");
    			            String nameSuffix = tagList.getString("nameSuffix");
    			            // Now find the nearest Village to that sign's coordinate, within villageRadiusBuffer blocks outside the radius.
    			            Village villageNearSign = world.getVillageCollection().getNearestVillage(new BlockPos(townX, townY, townZ), villageRadiusBuffer);
    			            
    			            isColony = tagList.getBoolean("isColony");
    			            
    			            if (villageNearSign == villageNearTarget) { // There is a match between the nearest village to this villager and the nearest village to the sign
    			            	signLocated = true;
    			            	if (!world.isRemote) {
    			            		// Generate a new Village Book with all the dressings!
    			            		WriteBookHandler.targetWriteNewVillageBook(
    			            				"village", target.getCustomNameTag(),
    			            				townX, townY, townZ,
    			            				isColony ? "Colony" : "Village", "",
    			            				namePrefix, nameRoot, nameSuffix,
    			            				true, villageNearSign, event,
    			            				targetProfession, targetCareer, targetPName,
    			            				tradeSize, //villageNearSign.getReputationForPlayer(player.getDisplayName()),
    			            				ReputationHandler.getVNReputationForPlayer((EntityPlayerMP) player, ReputationHandler.getVillageTagPlayerIsIn((EntityPlayerMP) player), villageNearSign),
    			            				player, target
    			            				);
    			            		}
    			            	break; // No need to keep comparing villages.
    			            }
    			        }
    			        if (!signLocated) {
    			        	// No well sign was found that matched the villager's village.
    			        	// We can assume this is a village WITHOUT a sign. So let's at least give it a name!
    			        	
    			        	String headerTags = GeneralConfig.headerTags.trim();
                    		String namePrefix = "";
                    		String nameRoot = "";
                    		String nameSuffix = "";
                    		
                    		// Changed color block in v3.1banner
                			// Generate banner info, regardless of if we make a banner.
                    		Object[] newRandomBanner = BannerGenerator.randomBannerArrays(-1);
            				ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
            				ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
            				ItemStack villageBanner = BannerGenerator.makeBanner(patternArray, colorArray);
                    		int townColorMeta = 15-colorArray.get(0);
                    		
                    		
                    		// Make the data bundle to save to NBT
                    		NBTTagList nbttaglist = new NBTTagList();
                    		
                    		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    		
                    		
    			        	// First, check to see if it has a ToroQuest name
                    		if (Loader.isModLoaded("toroquest") && GeneralConfig.TQVillageNames) {

    			        		try {
    			        			ToroQuestWorldDataStructure toroquestData = ToroQuestWorldDataStructure.forWorld(world, "toroquest_civilizations", "provinces");
    			        			NBTTagList TQtagList = toroquestData.getData();
    			        			
    			        			for (int tq_i=0; tq_i< TQtagList.tagCount(); tq_i++) { // Go through each TQ village
    			        				
    			        				NBTTagCompound TQCompound = TQtagList.getCompoundTagAt(tq_i); // Get the TQ village info
    			        				
    			        				int TQ_lX = TQCompound.getInteger("lX");
    			        				int TQ_uX = TQCompound.getInteger("uX");
    			        				int TQ_lZ = TQCompound.getInteger("lZ");
    			        				int TQ_uZ = TQCompound.getInteger("uZ");
    			        				String TQ_name = TQCompound.getString("name");
    			        				
    			        				if (
    			        						   villager.posX >= (TQ_lX << 4)
    			        						&& villager.posX <= (TQ_uX << 4)+15
    			        						&& villager.posZ >= (TQ_lZ << 4)
    			        						&& villager.posZ <= (TQ_uZ << 4)+15
    			        						) {
    			        					if (GeneralConfig.debugMessages) {LogHelper.info("Villager is inside TQ village " + TQ_name);}
    			        					namePrefix = "";
    			        					nameRoot = TQ_name;
    			        					nameSuffix = "";
    			        					nbttagcompound1.setBoolean("fromToroQuest", true);
    			        					break;
    			        				}
    			        			}
    			        			
    			        		}
    			        		catch (Exception e) {
    			        			if (GeneralConfig.debugMessages) {LogHelper.error("There was an issue evaluating ToroQuest villages: " + e);}
    			        		}
                    		}
                    		else {
    			        		
    			        		// Either you're not using ToroQuest, or you don't want to use the TQ name.
    			        		String[] newVillageName = NameGenerator.newRandomName("Village");
	    			        	headerTags = newVillageName[0];
                        		namePrefix = newVillageName[1];
                        		nameRoot = newVillageName[2];
                        		nameSuffix = newVillageName[3];
    			        		
    			        	}

    			        	
                            nbttagcompound1.setInteger("signX", centerX);
                            nbttagcompound1.setInteger("signY", centerY);
                            nbttagcompound1.setInteger("signZ", centerZ);
                            nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
                            nbttagcompound1.setString("namePrefix", namePrefix);
                            nbttagcompound1.setString("nameRoot", nameRoot);
                            nbttagcompound1.setString("nameSuffix", nameSuffix);
                            nbttagcompound1.setBoolean("fromEntity", true); // Record whether this name was generated from interaction with an entity
                            
                            if (!ReputationHandler.getVillageTagPlayerIsIn((EntityPlayerMP)player).equals("none")) {
                                nbttagcompound1.setBoolean("preVN", true); // No Village Names entry was discovered, so presumably this village was generated before including VN
                        	}
                            else {
                                nbttagcompound1.setBoolean("isColony", true); // The village is player-created
                                isColony = true;
                            }

                            // Added in v3.1banner
                            // Form and append banner info
                            nbttagcompound1.setTag("BlockEntityTag", BannerGenerator.getNBTFromBanner(villageBanner));
                            
                            nbttaglist.appendTag(nbttagcompound1);
                            
                            // Save the data under a "Villages" entry with unique name based on sign coords
                            if (
                            		(!nameRoot.equals(null) && !nameRoot.equals(""))
                            		|| isColony
                            		) {
                            	data.getData().setTag((namePrefix + " " + nameRoot + " " + nameSuffix).trim() + ", x" + centerX + " y" + centerY + " z" + centerZ, nbttaglist);
                                
                                data.markDirty();
                                
                                signLocated = false;
                                
                                //event refers to the interaction with the villager
			            		//tagList is the villagename data
			            		//villageNearVillager is the corresponding vanilla data
                                if (!world.isRemote) {
                                	WriteBookHandler.targetWriteNewVillageBook(
    			            				"village", target.getCustomNameTag(),
    			            				centerX, centerY, centerZ,
    			            				isColony ? "Colony" : "Village", "",
    			            				namePrefix, nameRoot, nameSuffix,
    			            				true, villageNearTarget, event,
    			            				targetProfession, targetCareer, targetPName,
    			            				tradeSize, //villageNearTarget.getReputationForPlayer(player.getDisplayName()),
    			            				ReputationHandler.getVNReputationForPlayer((EntityPlayerMP) player, ReputationHandler.getVillageTagPlayerIsIn((EntityPlayerMP) player), villageNearTarget),
    			            				player, target
    			            				);
                                	}
                                
                            }
    			        }
					}
				}
			}
			else { // The Villager is a baby and so can't help you.
				if (!world.isRemote) {player.sendMessage(new TextComponentString( babyCantHelpString("book") ) );}
			}
		}

		else { // The target is a non-vanilla type that has a supported village and book

		    // 
		    // STRUCTURE		NBTname			key					toptag
		    // 
		    // Villages:						villagenames		Villages
		    // Temples:							villagenames_te		Temples
		    // Mineshafts:						villagenames_mi		Mineshafts
		    // Strongholds:						villagenames_st		Strongholds
		    // Monuments:						villagenames_mo		Monuments
		    // Mansions:						villagenames_ma		Mansions
		    // Fortress:						villagenames_fo		Fortresses
		    // End City:						villagenames_ec		EndCities
		    // 
		    // Abandoned Base:					villagenames_gcab	AbandonedBases
		    // End Islands:						villagenames_heei	hardcoreenderdragon_EndIsland
		    // End Towers:						villagenames_heet	hardcoreenderdragon_EndTower
		    // Fronos Villages:	FronosVillage	villagenames_mpfv	FronosVillages
		    // Koentus Vill:	KoentusVillage	villagenames_mpkv	KoentusVillages
		    // Moon Villages:	MoonVillage		villagenames_gcmv	MoonVillages
		    // Nibiru Villages:	NibiruVillage	villagenames_mpnv	NibiruVillages
		    //

			// keys: "NameTypes", "StructureTypes", "StructureTitles", "DimensionNames", "BookTypes", "ClassPaths"
			Map<String, ArrayList> mappedModStructureNames = GeneralConfig.unpackModStructures(GeneralConfig.modStructureNames);
			
			// What kind of name to be generated
			
			String nameType = "alienvillage";
			try {nameType = (String) (mappedModStructureNames.get("NameTypes")).get( mappedModStructureNames.get("ClassPaths").indexOf(targetClassPath) );}
			catch (Exception e) {}
			
			// The name of the NBT structure file generated by the mod (e.g. "FronosVillage")
			String structureType = "";
			try {structureType = (String) (mappedModStructureNames.get("StructureTypes")).get( mappedModStructureNames.get("ClassPaths").indexOf(targetClassPath) );}
			catch (Exception e) {structureType = "";}
			
			// The string name of the structure (e.g. "Fronos Village")
			String structureTitle = "";
			try {structureTitle = (String) (mappedModStructureNames.get("StructureTitles")).get( mappedModStructureNames.get("ClassPaths").indexOf(targetClassPath) );}
			catch (Exception e) {}
			
			// The string name of the dimension the structure is in (e.g. "Fronos")
			String dimensionName = "";
			try {dimensionName = (String) (mappedModStructureNames.get("DimensionNames")).get( mappedModStructureNames.get("ClassPaths").indexOf(targetClassPath) );}
			catch (Exception e) {}
			
			// The type of book to create: (e.g. "fronosvillage")
			String bookType = "village";
			try {bookType = (String) (mappedModStructureNames.get("BookTypes")).get( mappedModStructureNames.get("ClassPaths").indexOf(targetClassPath) );}
			catch (Exception e) {}

			// Now that the relevant info has been set, go through the motions
			if ( targetAge < 0 ) {
				// Target is a baby so can't write you a book.
				if (!world.isRemote) {player.sendMessage(new TextComponentString( babyCantHelpString("book") ) );}
			}
			else { // Target is an adult

				// Prep the data stuff
				MapGenStructureData structureData;
    			int[] BB = new int[6];
    			boolean targetIsInsideAlienVillage=false;

    			try {
    				structureData = (MapGenStructureData)world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, structureType);
    				// Load in entries from the structure class
    				NBTTagCompound nbttagcompound = structureData.getTagCompound();

					Iterator itr = nbttagcompound.getKeySet().iterator();
					
					// Go through the entries
					while (itr.hasNext()) {
						Object element = itr.next();
						
						NBTBase nbtbase = nbttagcompound.getTag(element.toString());
						
						if (nbtbase.getId() == 10) { //10 is "compound tag" I think?
							NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
							
							try{
								int[] boundingBox = nbttagcompound2.getIntArray("BB");
								// Now check to see if the target is inside the feature
								if (
										   targetX >= boundingBox[0]
										&& targetY >= boundingBox[1]
										&& targetZ >= boundingBox[2]
										&& targetX <= boundingBox[3]
										&& targetY <= boundingBox[4]
										&& targetZ <= boundingBox[5]
										) { // Target is inside bounding box.

									targetIsInsideAlienVillage = true;
									
									String structureName;
									
									int ChunkX = nbttagcompound2.getInteger("ChunkX");
									int ChunkZ = nbttagcompound2.getInteger("ChunkZ");
									
									String[] structureInfoArray = WriteBookHandler.tryGetStructureInfo(structureType, boundingBox, world);

									String namePrefix = structureInfoArray[0];
									String nameRoot = structureInfoArray[1];
									String nameSuffix = structureInfoArray[2];

									int signX; int signY; int signZ;

									// If none is found, these strings are "null" which parseInt does not like very much
									try {
										signX = Integer.parseInt(structureInfoArray[3]);
										signY = Integer.parseInt(structureInfoArray[4]);
										signZ = Integer.parseInt(structureInfoArray[5]);
											}
									catch (Exception e) {}

									// If a name was NOT returned, then we need to generate a new one, as is done below:

									int[] structureCoords = new int[] {
											(boundingBox[0]+boundingBox[3])/2,
											(boundingBox[1]+boundingBox[4])/2,
											(boundingBox[2]+boundingBox[5])/2,
											};

									VNWorldDataStructure data = VNWorldDataStructure.forWorld(world, "villagenames3_"+structureType, "NamedStructures");
									NBTTagCompound tagCompound = data.getData();
									
									if (structureInfoArray[0]==null && structureInfoArray[1]==null && structureInfoArray[2]==null) {

										//Structure has no name. Generate it here.
										
										structureInfoArray = NameGenerator.newRandomName(nameType); // Generates name based on table above
										
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
										nbttagcompound1.setBoolean("fromEntity", true); // Record whether this name was generated from interaction with an entity
										nbttaglist.appendTag(nbttagcompound1);
										
										// .getTagList() will return all the entries under the specific village name.
										
										tagCompound.setTag((namePrefix + " " + nameRoot + " " + nameSuffix).trim() + ", x" + signX + " y" + signY + " z" + signZ, nbttaglist);
										data.markDirty();
										
									}
									// Either the structure name already existed, or it was just generated right above.
									
									structureName = structureInfoArray[0]+" "+structureInfoArray[1]+" "+structureInfoArray[2];
									structureName = structureName.trim();
									
									signX = structureCoords[0];
									signY = structureCoords[1];
									signZ = structureCoords[2];
									
					    			// Actually form the book contents and write the book
					    			
					    			//Here are the contents of the book up front
									//String bookContents = "{\"text\":\""; //As of 1.9 I need to enclose book contents in {"text":"lorem ipsum"}
									//bookContents += "\n\u00a7r\u00a70"; --From old 1.9
									//bookContents = "\n\u00a7l"; --From updated 1.8
					    			
					    			// I don't care if the structure has a sign. We have to cut the name up into arbitrary sign strings for the book.
					    			
					    			String sign0 = new String();
					    			String sign1 = new String();
					    			String sign2 = new String();
					    			String sign3 = new String();

					    			String headerTags = GeneralConfig.headerTags;
					    			
					    			if (!world.isRemote) {
	                                	WriteBookHandler.targetWriteNewVillageBook(
	                                			bookType, target.getCustomNameTag(),
	                                			signX, signY, signZ,
	                                			structureTitle, dimensionName,
	    			            				namePrefix, nameRoot, nameSuffix,
	    			            				structureType.equals("Village"), null, event, 
	    			            				targetProfession, targetCareer, targetPName, // Also unused but there's no harm in leaving them
	    			            				-1, -1, // Trade size and player rep are unused outside of vanilla villages
	    			            				player, target
	    			            				);
					    				}
								}
								// Target is not inside bounding box of the particular entry in question
							}
							catch (Exception e) {
								// There's a tag like [23,-3] (chunk location) but there's no bounding box tag. Not a good sign, bruv.
								if (GeneralConfig.debugMessages && !world.isRemote) {LogHelper.error("No bounding box detected!");}
							}
						}
					}
    			}
    			catch (Exception e) {
    				if (!world.isRemote && GeneralConfig.debugMessages) {LogHelper.error("Failed to make a village book.");}
    			}
			}
		}
    	
    }
    
    
    private static void villageBookEvent(EntityInteract event, EntityPlayer player, Village villageNearTarget) {
    	
		event.setCanceled(true);
		if (villageNearTarget != null) { // Villager is in a town, so get the rep message
			int playerRep = ReputationHandler.getVNReputationForPlayer((EntityPlayerMP) player, ReputationHandler.getVillageTagPlayerIsIn((EntityPlayerMP) player), villageNearTarget);
			player.sendMessage(new TextComponentString( villagerAssessReputation(playerRep) ) );
		}
		else { // Villager is not in a town, so return a confusion message.
			villagerConfused(player);
		}
    	
    }
    
    
    
}