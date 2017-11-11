package astrotibs.villagenames.block;

import astrotibs.villagenames.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockLunarinIron extends Block{
	
	public BlockLunarinIron(Material materialIn, String name) {
		super(materialIn);
		this.setUnlocalizedName( Reference.MOD_ID.toLowerCase() + ":" + name);
		// Stuff I pulled out of Block.class for the gold block :P
		this.setHardness(3.3F); // Gold block is fully 3.0
		this.setResistance(6.0F); // Gold block is fully 10.0F
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 1);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
}