package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * Used to assign data to the decor blueprints
 * @author AstroTibs
 */
public class BlueprintData {
	
	// xyz coordinates in the frame of reference of the structure
	private int upos;
	private int vpos;
	private int wpos;
	
	// Block of interest
	private IBlockState blockstate;
	
	// Flag
	private byte fillFlag;
	
	// Getters
	public int getUPos() {return upos;}
	public int getVPos() {return vpos;}
	public int getWPos() {return wpos;}
	public IBlockState getBlockState() {return blockstate;}
	public byte getfillFlag() {return fillFlag;}
	
	
	// Constructor so you can add values
	/**
	 * Fill flags:
	 * 1 = also fill below with this block
	 * 2 = also clear spaces above this block
	 */
	public BlueprintData(int upos, int vpos, int wpos, IBlockState blockstate, byte fillFlag)
	{
		this.upos = upos; this.vpos = vpos; this.wpos = wpos;
		this.blockstate = blockstate;
		this.fillFlag = fillFlag;
	}
	
	// Assuming fill flag is 0 - ordinary block replacement
	public BlueprintData(int upos, int vpos, int wpos, IBlockState blockstate)
	{
		this(upos, vpos, wpos, blockstate, (byte) 0);
	}
	
	
	
	// Methods for placing a block
	public static void addPlaceBlock(ArrayList<BlueprintData> blueprint, int u, int v, int w, IBlockState blockstate)
	{
		blueprint.add(new BlueprintData(u, v, w, blockstate, (byte) 0));
	}
	
	
	
	// Methods for filling space with blocks
	public static void addFillWithBlocks(ArrayList<BlueprintData> blueprint, int umin, int vmin, int wmin, int umax, int vmax, int wmax, IBlockState blockstate)
	{
		for (int u=umin; u<=umax; u++) {for (int v=vmin; v<=vmax; v++) {for (int w=wmin; w<=wmax; w++) {
			blueprint.add(new BlueprintData(u, v, w, blockstate));
		}}}
	}
	
	
	
	// Methods for filling up to a space with a block
	public static void addFillBelowTo(ArrayList<BlueprintData> blueprint, int u, int v, int w, IBlockState blockstate)
	{
		blueprint.add(new BlueprintData(u, v, w, blockstate, (byte) 1));
	}
	
	

	// Methods for placing a block and clearing the space above
	public static void addPlaceBlockAndClearAbove(ArrayList<BlueprintData> blueprint, int u, int v, int w, IBlockState blockstate)
	{
		blueprint.add(new BlueprintData(u, v, w, blockstate, (byte) 2));
	}
	
	
	
	// Methods for filling up to a space with a block and clearing the space above
	public static void addFillBelowToAndClearAbove(ArrayList<BlueprintData> blueprint, int u, int v, int w, IBlockState blockstate)
	{
		blueprint.add(new BlueprintData(u, v, w, blockstate, (byte) 3));
	}
	
	
	
}
