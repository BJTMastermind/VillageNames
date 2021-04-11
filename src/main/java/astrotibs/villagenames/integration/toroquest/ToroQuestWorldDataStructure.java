package astrotibs.villagenames.integration.toroquest;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class ToroQuestWorldDataStructure extends WorldSavedData implements ToroQuestWorldData {

	private NBTTagList data = new NBTTagList();
    
    static String toptag = ""; // top-level tag
    
    public ToroQuestWorldDataStructure(String tagName) {
        super(tagName);
    }
    
    
	public static ToroQuestWorldDataStructure forWorld(World world, String key, String toptagIn) {
		toptag = toptagIn;
		// Retrieves the data instance for the given world, creating it if necessary
		MapStorage storage = world.getPerWorldStorage();//world.perWorldStorage;
		ToroQuestWorldDataStructure result = (ToroQuestWorldDataStructure)storage.getOrLoadData(ToroQuestWorldDataStructure.class, key);//.loadData(VillageNamesWorldData.class, key);
		if (result == null) {
			result = new ToroQuestWorldDataStructure(key);
			storage.setData(key, result);
		}
	return result;
	}
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
    	
    	NBTTagList list;
    	try {
			list = (NBTTagList) compound.getTag("provinces");
		}
    	catch (Exception e) {
    		list = new NBTTagList();
    	}
    	data = list;
    	//data = compound.getCompoundTag(toptag);
    }
    
    // Here's the default one it wants so badly
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	compound.setTag(toptag, data);
        return compound;
    }
    
    @Override
	public NBTTagList getData() {
        return data;
    }
}
