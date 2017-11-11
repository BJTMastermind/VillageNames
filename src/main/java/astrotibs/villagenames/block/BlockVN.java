package astrotibs.villagenames.block;

import astrotibs.villagenames.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockVN extends Block {
	
	// Specific type declaration
	public BlockVN(Material material) {
		super(material);
	}
	
	@Override
	public String getUnlocalizedName() {
		return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	// Block name?
	protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.lastIndexOf(".") + 1);
	}

}
