package astrotibs.villagenames.integration.toroquest;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public interface ToroQuestWorldData {
	public void readFromNBT(NBTTagCompound compound);
	public NBTTagCompound writeToNBT(NBTTagCompound compound);
	public NBTTagList getData();
	public void markDirty();
}