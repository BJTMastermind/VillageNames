package astrotibs.villagenames.block.color;

import astrotibs.villagenames.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

// from bedrockminer
public class ItemBlockMetaColor extends ItemBlock {

    public ItemBlockMetaColor(Block block) {
        super(block);
        if (!(block instanceof IMetaBlockName)) {
            throw new IllegalArgumentException(String.format("The given Block %s is not an instance of ISpecialBlockName!", block.getUnlocalizedName()));
        }
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
	public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile." + Reference.MOD_ID.toLowerCase() + ":" + super.getUnlocalizedName(stack).substring(5) + "." + ((IMetaBlockName)this.block).getSpecialName(stack);
    }
}