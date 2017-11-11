package astrotibs.villagenames.block.color;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockConcrete extends Block implements IMetaBlockName {
	
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);
	
    public BlockConcrete(String unlocalizedName) {
    	super(Material.ROCK);
    	
    	this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(1.8F);
        this.setHarvestLevel("pickaxe", 0);
        this.setSoundType(SoundType.STONE);
        // need to set the default block state
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
    }
    
    // returns a BlockState which contains every property the block has
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { COLOR });
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
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList list) {
    	for (int m=0; m<16; m++) {
    		list.add(new ItemStack(itemIn, 1, m));
    	}
    }
    
    // required for meta-item
    @Override
    public String getSpecialName(ItemStack stack) {
    	return EnumDyeColor.byMetadata(stack.getItemDamage()).getName();
    }
    
    // Pick block behavior -- Changed significantly from 1.8.9 VN version
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        //return getItem(world, pos, state);
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