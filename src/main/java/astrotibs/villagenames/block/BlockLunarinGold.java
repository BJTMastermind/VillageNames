package astrotibs.villagenames.block;

import astrotibs.villagenames.material.ModMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;

public class BlockLunarinGold extends Block {
	
	public BlockLunarinGold(String name) {
		super(ModMaterial.gold);
		this.setUnlocalizedName(name);
		// Stuff I pulled out of Block.class for the gold block :P
		this.setHardness(2.0F); // Gold block is fully 3.0
		this.setResistance(6.0F); // Gold block is fully 10.0F
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 2);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
}