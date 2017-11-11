package astrotibs.villagenames.block.color;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockConcrete extends Block implements IMetaBlockName {
	
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);
	
    public BlockConcrete(String unlocalizedName) {
    	super(Material.rock);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(1.8F);
        this.setHarvestLevel("pickaxe", 0);
        this.setStepSound(soundTypeStone);
        // need to set the default block state
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
    }
    
    // returns a BlockState which contains every property the block has
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { COLOR });
    }
    
    // used to convert a IBlockState into metadata and the other way round
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta) );
    }
    @Override
    public int getMetaFromState(IBlockState state) {
    	EnumDyeColor type = state.getValue(COLOR);
        return type.getMetadata();
    }
    
    // the block drops the item with the corresponding metadata
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
    
    // want the block to appear in the creative tab in all states
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (int m=0; m<16; m++) {
    		list.add(new ItemStack(itemIn, 1, m));
    	}
    }
    
    // required for meta-item
    @Override
    public String getSpecialName(ItemStack stack) {
    	return EnumDyeColor.byMetadata(stack.getItemDamage()).getName();
    }
    
    // Pick block behavior
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }
    
    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
	public MapColor getMapColor(IBlockState state)
    {
        return state.getValue(COLOR).getMapColor();
    }
    
}

