package astrotibs.villagenames.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface VNWorldData {
		//public VNWorldData forWorld(World world);
		public void readFromNBT(NBTTagCompound compound);
		public NBTTagCompound writeToNBT(NBTTagCompound compound);
		public NBTTagCompound getData();
		public void markDirty();
	}