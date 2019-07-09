package astrotibs.villagenames.capabilities;

import astrotibs.villagenames.utility.Reference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

/**
 * Constructed following McHorse's tutorial at:
 * https://www.planetminecraft.com/blog/forge-tutorial-capability-system/
 * @author AstroTibs
 */

// Added in v3.1
public class ModularSkinStorage implements IStorage<IModularSkin>
{
	public final static String KEY_PROFESSION  = "Profession";
	public final static String KEY_CAREER = "Career";
	public final static String KEY_BIOMETYPE = "BiomeType";
	public final static String KEY_PROFESSIONLEVEL = "ProfessionLevel";
	public final static String KEY_SKINTONE = "SkinTone"; // Added in v3.2
	
	
	@Override
	public NBTBase writeNBT(Capability<IModularSkin> capability, IModularSkin instance, EnumFacing side) {
		
		NBTTagCompound compound = new NBTTagCompound();
		
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setInteger(KEY_PROFESSION, instance.getProfession());
        nbttagcompound1.setInteger(KEY_CAREER, instance.getCareer());
        nbttagcompound1.setInteger(KEY_BIOMETYPE, instance.getBiomeType());
        nbttagcompound1.setInteger(KEY_PROFESSIONLEVEL, instance.getProfessionLevel());
        nbttagcompound1.setInteger(KEY_SKINTONE, instance.getSkinTone()); // Added in v3.2
        
        compound.setTag(Reference.MOD_ID.toLowerCase(), nbttagcompound1); 
        
		return compound;
	}

	@Override
	public void readNBT(Capability<IModularSkin> capability, IModularSkin instance, EnumFacing side, NBTBase nbt) {
		
		NBTTagCompound compound = ((NBTTagCompound)nbt);
		
		NBTTagCompound nbttagcompound1 = (NBTTagCompound)compound.getTag(Reference.MOD_ID.toLowerCase());
		
		instance.setProfession(nbttagcompound1.getInteger(KEY_PROFESSION));
		instance.setCareer(nbttagcompound1.getInteger(KEY_CAREER));
		instance.setBiomeType(nbttagcompound1.hasKey(KEY_BIOMETYPE) ? nbttagcompound1.getInteger(KEY_BIOMETYPE) : -1);
		instance.setProfessionLevel(nbttagcompound1.getInteger(KEY_PROFESSIONLEVEL));
		instance.setSkinTone(nbttagcompound1.hasKey(KEY_SKINTONE) ? nbttagcompound1.getInteger(KEY_SKINTONE) : -99); // Added in v3.2
		
	}

}
