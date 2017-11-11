package astrotibs.villagenames.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.World;

public class VNWorldDataVillage extends WorldSavedData implements VNWorldData {
    private NBTTagCompound data = new NBTTagCompound();
    
    final static String key = "villagenames"; // .dat file being saved to
    final static String toptag = "Villages"; // top-level tag, under "data"
    
    public VNWorldDataVillage(String tagName) {
        super(tagName);
    }
    
    
	public static VNWorldDataVillage forWorld(World world) {
		// Retrieves the data instance for the given world, creating it if necessary
		MapStorage storage = world.getPerWorldStorage();//world.perWorldStorage;
		VNWorldDataVillage result = (VNWorldDataVillage)storage.getOrLoadData(VNWorldDataVillage.class, key);//.loadData(VillageNamesWorldData.class, key);
		if (result == null) {
			result = new VNWorldDataVillage(key);
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
