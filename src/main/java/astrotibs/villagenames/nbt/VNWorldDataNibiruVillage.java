package astrotibs.villagenames.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.World;

public class VNWorldDataNibiruVillage extends WorldSavedData implements VNWorldData {
    private NBTTagCompound data = new NBTTagCompound();
    
    final static String key = "villagenames_mpnv"; // .dat file being saved to
    final static String toptag = "NibiruVillages"; // top-level tag, under "data"
    
    public VNWorldDataNibiruVillage(String tagName) {
        super(tagName);
    }
    
    
	public static VNWorldDataNibiruVillage forWorld(World world) {
		// Retrieves the data instance for the given world, creating it if necessary
		MapStorage storage = world.getPerWorldStorage();
		VNWorldDataNibiruVillage result = (VNWorldDataNibiruVillage)storage.getOrLoadData(VNWorldDataNibiruVillage.class, key);
		if (result == null) {
			result = new VNWorldDataNibiruVillage(key);
			storage.setData(key, result);
		}
	return result;
	}
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
    	data = compound.getCompoundTag(toptag);
    }
    
    // Here's the default one it wants so badly
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag(toptag, data);
        return compound;
    }
    
    @Override
	public NBTTagCompound getData() {
        return data;
    }
}