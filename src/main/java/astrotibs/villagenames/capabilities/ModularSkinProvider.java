package astrotibs.villagenames.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Constructed following McHorse's tutorial at:
 * https://www.planetminecraft.com/blog/forge-tutorial-capability-system/
 * @author AstroTibs
 */

public class ModularSkinProvider implements ICapabilitySerializable<NBTBase>
{
	@CapabilityInject(IModularSkin.class)
	public static Capability<IModularSkin> MODULAR_SKIN = null;
	
	private IModularSkin instance = MODULAR_SKIN.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		
		return capability == MODULAR_SKIN;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		
		return capability == MODULAR_SKIN ? MODULAR_SKIN.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		
		return MODULAR_SKIN.getStorage().writeNBT(MODULAR_SKIN, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		
		MODULAR_SKIN.getStorage().readNBT(MODULAR_SKIN, this.instance, null, nbt);
	}

}
