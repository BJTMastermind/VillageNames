package astrotibs.villagenames.capabilities;

import astrotibs.villagenames.utility.Reference;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Constructed following McHorse's tutorial at:
 * https://www.planetminecraft.com/blog/forge-tutorial-capability-system/
 * @author AstroTibs
 */

//Added in v3.1
public class CapabilityAttach
{
	public static final ResourceLocation MODULAR_SKIN = new ResourceLocation(Reference.MOD_ID, "modularskin");
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent event)
	{
	if ( //TODO - AttachCapabilitiesEvent.Entity and .getEntity() is deprecated?
			(event.getObject() instanceof EntityVillager)
			||
			(event.getObject() instanceof EntityZombieVillager) // Don't bother with the isVillager condition.
			//(
			//	(event.getEntity() instanceof EntityZombie)
			//	&& ((EntityZombie)event.getEntity()).isVillager()
			//	)
			) {event.addCapability(MODULAR_SKIN, new ModularSkinProvider());}
	}
	
}