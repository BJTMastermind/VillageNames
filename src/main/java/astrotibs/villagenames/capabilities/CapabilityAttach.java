package astrotibs.villagenames.capabilities;

import astrotibs.villagenames.utility.Reference;
import net.minecraft.entity.monster.EntityZombie;
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
//Revised in v3.1dragon
public class CapabilityAttach
{
	public static final ResourceLocation MODULAR_SKIN = new ResourceLocation(Reference.MOD_ID, "modularskin");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent.Entity event)
	{
		
		if (
				(event.getEntity() instanceof EntityVillager)
				||
				(event.getEntity() instanceof EntityZombie) // Don't bother with the isVillager condition.
				)
		{
			event.addCapability(MODULAR_SKIN, new ModularSkinProvider());
		}
	}
}