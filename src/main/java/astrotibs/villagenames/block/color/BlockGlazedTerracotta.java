package astrotibs.villagenames.block.color;

import astrotibs.villagenames.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGlazedTerracotta extends Block {
	
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public EnumDyeColor dyeColor = EnumDyeColor.WHITE;
	
    public BlockGlazedTerracotta(EnumDyeColor color) {
    	super(Material.ROCK);
    	String s = color.getName();
    	if (s.length() > 1)
        {
    		this.setUnlocalizedName( Reference.MOD_ID.toLowerCase() + ":glazed_terracotta_" + s);
        }
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(1.4F);
        this.setHarvestLevel("pickaxe", 0);
        this.setSoundType(SoundType.STONE);
        this.dyeColor = color;
    }
    
    // Orientation block
    
    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    
    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        
    	return this.getDefaultState()
    			.withProperty(FACING, placer.getHorizontalFacing().getOpposite())
    			;
    }
    
    // End orientation block
    
    
    // returns a BlockState which contains every property the block has
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }
    
    // used to convert a IBlockState into metadata and the other way round
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        return i;
    }
        
    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
	public MapColor getMapColor(IBlockState state)
    {
        return dyeColor.getMapColor();
    }
    
}