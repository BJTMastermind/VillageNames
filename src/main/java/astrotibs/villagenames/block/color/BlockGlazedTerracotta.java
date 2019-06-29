package astrotibs.villagenames.block.color;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockGlazedTerracotta extends Block {
	
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public EnumDyeColor dyeColor = EnumDyeColor.WHITE;
	
    // These are used to reference unlocalized names
    public static String[] subBlockUnloc = {
    		"White", //0
    		"Orange", //1
    		"Magenta", //2
    		"LightBlue", //3
    		"Yellow", //4
    		"Lime", //5
    		"Pink", //6
    		"Gray", //7
    		"Silver", //8
    		"Cyan", //9
    		"Purple", //10
    		"Blue", //11
    		"Brown", //12
    		"Green", //13
    		"Red", //14
    		"Black" //15
    		};
	
    public BlockGlazedTerracotta(EnumDyeColor color) {
    	super(Material.rock);
    	//String s = color.getName();
    	//if (s.length() > 1)
        //{
    		//this.setUnlocalizedName( Reference.MOD_ID.toLowerCase() + ":glazed_terracotta_" + s);
    		this.setUnlocalizedName( "glazedTerracotta" + subBlockUnloc[color.getMetadata()]);
    	 	//this.setUnlocalizedName( "glazed_terracotta_" + s);
        //}
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(1.4F);
        this.setHarvestLevel("pickaxe", 0);
        this.setStepSound(soundTypeStone);
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
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { FACING });
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

