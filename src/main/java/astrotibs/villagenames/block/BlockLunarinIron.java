package astrotibs.villagenames.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockLunarinIron extends BlockVN {
	
	public BlockLunarinIron() {
		super(Material.IRON);
		
		this.setUnlocalizedName("lunarinIronBrick"); // This one alone seems to work I guess?
		// Stuff I pulled out of Block.class for the gold block :P
		this.setHardness(3.3F); // Gold block is fully 3.0
		this.setResistance(6.0F); // Gold block is fully 10.0F
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 1);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
}