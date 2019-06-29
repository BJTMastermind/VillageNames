package astrotibs.villagenames.handler;

import java.util.ArrayList;
import java.util.Map;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnNamingHandler {

	@SubscribeEvent()
	public void onPopulating(EntityJoinWorldEvent event) {
		
		Map<String, ArrayList> mappedNamesAutomatic = GeneralConfig.unpackMappedNames(GeneralConfig.modNameMappingAutomatic);
		
		if (event.getEntity() instanceof EntityLiving
				&& !(event.getEntity() instanceof EntityPlayer)
				&& !event.getEntity().world.isRemote
				//&& !(event.getEntity().dimension==1 && event.getEntity() instanceof EntityDragon && event.getEntity().getPosition().equals( new BlockPos(0,128,0) ) )
				) { // Only looks at living entities in order to reduce grind
			
			EntityLiving entity = (EntityLiving)(event.getEntity()); // Makes things easier to manage
			NBTTagCompound compound = new NBTTagCompound();
			entity.writeEntityToNBT(compound);
			int targetAge = compound.getInteger("Age");
			
			String entityClassPath = entity.getClass().toString().substring(6);
						
			// keys: "NameTypes", "Professions", "ClassPaths", "AddOrRemove"
			if (mappedNamesAutomatic.get("ClassPaths").contains(entityClassPath) ) {
				// "true" means "add"
				String addOrRemove = (String) ((mappedNamesAutomatic.get("AddOrRemove")).get( mappedNamesAutomatic.get("ClassPaths").indexOf(entityClassPath) ));
				
				// The spawning entity matches the list. Now check to see if it should GAIN or LOSE its tag.
				
				// Entity does not have a tag and should...
				if (addOrRemove.trim().equals("add") && GeneralConfig.nameEntities) { // Add a new name?
					if ( entity.getCustomNameTag().trim().equals("") || entity.getCustomNameTag().trim() == null
							||
							 (entityClassPath.equals("net.daveyx0.primitivemobs.entity.passive.EntityTravelingMerchant")
							   && entity.getCustomNameTag().trim().equals("Traveling Merchant")) // Contingency in there specifically for PM's Traveling Merchants
							) {
						
						String nameType = (String) ((mappedNamesAutomatic.get("NameTypes")).get( mappedNamesAutomatic.get("ClassPaths").indexOf(entityClassPath) ));
						
						String[] newNameA = NameGenerator.newRandomName(nameType);
						String newName = (newNameA[1]+" "+newNameA[2]+" "+newNameA[3]).trim();
						// Generate profession tag
						if (
								GeneralConfig.addJobToName
								&& ( !(entity instanceof EntityVillager) || targetAge>=0 )
								) {
							String careerTag = (String) ((mappedNamesAutomatic.get("Professions")).get( mappedNamesAutomatic.get("ClassPaths").indexOf(entityClassPath) ));
							newName += ( (careerTag.trim().equals("") || careerTag.trim().equals(null)) ? "" : " ("+careerTag+")" );
						}
						// Apply the name
						entity.setCustomNameTag( newName );
					}
					// Clickable Entity already has a name. You may want to add (or remove) a career tag.
					else if (
							entity.getCustomNameTag().trim().indexOf("(")==-1 && GeneralConfig.addJobToName
							&& ( !(entity instanceof EntityVillager) || targetAge>=0 )
							) { // Target is named but does not have job tag: add one!
						String careerTag = (String) ((mappedNamesAutomatic.get("Professions")).get( mappedNamesAutomatic.get("ClassPaths").indexOf(entityClassPath) ));
						String newCustomName = entity.getCustomNameTag().trim()
								+ ( (careerTag.trim().equals("") || careerTag.trim().equals(null)) ? "" : " ("+careerTag+")" );
						// Apply the name
						entity.setCustomNameTag( newCustomName.trim() );
					}
					else if (entity.getCustomNameTag().trim().indexOf("(")!=-1 && !GeneralConfig.addJobToName) { // Target has a job tag: remove it...
						entity.setCustomNameTag(entity.getCustomNameTag().trim().substring(0, entity.getCustomNameTag().trim().indexOf("(")).trim());
					}
				}
				else if (addOrRemove.trim().equals("remove")) { // Remove an assigned name?
					if ( !entity.getCustomNameTag().trim().equals("") && !(entity.getCustomNameTag().trim() == null) ) {
						entity.setCustomNameTag("");
					}
				}
				
				if (
						!entity.isNonBoss() 
						&& entity.hasCustomName()
						) {
					// Set name to always display
					//entity.setAlwaysRenderNameTag(false);
					if (entity instanceof EntityDragon) {
						LogHelper.warn("Ender Dragon custom names do not work properly.");
					}
					
				}
			}
		}
	}
}
