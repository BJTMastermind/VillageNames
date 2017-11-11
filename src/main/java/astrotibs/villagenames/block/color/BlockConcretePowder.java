package astrotibs.villagenames.block.color;

import java.util.List;
import java.util.Random;

import astrotibs.villagenames.block.ModBlocksVN;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockConcretePowder extends BlockFalling implements IMetaBlockName {
	
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);
	
    public BlockConcretePowder(String unlocalizedName) {
        super(Material.SAND);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(0.5F);
        this.setHarvestLevel("shovel", 0);
        this.setSoundType(SoundType.SAND);
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
    
    // Pick block behavior -- Changed significantly from 1.8.9 VN version
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
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
    
    /*
     * All the stuff below is related to conversion to solid concrete
     */
    
    @Override
	public void updateTick(World world, BlockPos pos, IBlockState state,  Random random)
    {
    	// This fires when you place the concrete powder block
        if (!world.isRemote)
        {
            this.checkFallable(world, pos);
        }
    }
    
    public void checkFallable(World worldIn, BlockPos pos)
    {
    	IBlockState state = worldIn.getBlockState(pos);
    	if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) // True if block underneath is air, fire, water, or lava
        {
            byte i = 32;
            
            if (!fallInstantly && worldIn.isAreaLoaded( pos.add(-i, -i, -i), pos.add(i, i, i) ) ) // Checks if surrounding chunks exist
            { // idk what fallInstantly actually does but apparently it's always off for this object
            	
                if (!worldIn.isRemote)
                {
                	EntityFallingBlockCP entityfallingblock = new EntityFallingBlockCP(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    this.onStartFalling(entityfallingblock);
                    worldIn.spawnEntityInWorld(entityfallingblock);
                }
            }
            else
            {
            	// Does not fire apparently
            	worldIn.setBlockToAir(pos);
                BlockPos blockpos;
                
                for (blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down())
                {
                    ;
                }
                
                if (blockpos.getY() > 0)
                {
                	worldIn.setBlockState(blockpos.up(), this.getDefaultState());
                }
                
            }
            
            
        }
    }
    
    
    protected boolean tryTouchWater(World worldIn, BlockPos pos)
    {
    	int xo = pos.getX(); int yo = pos.getY(); int zo = pos.getZ();
        boolean flag = false;
        
        for (int side = 1; side < 6; side++)
        {
        	xo = pos.getX(); yo = pos.getY(); zo = pos.getZ();
        	switch (side) {
        	//case 0: yo--; break;
        	case 1: yo++; break;
        	case 2: zo--; break;
        	case 3: zo++; break;
        	case 4: xo--; break;
        	case 5: xo++; break;
        	}
        	
            if (worldIn.getBlockState(new BlockPos(xo, yo, zo)).getBlock().getMaterial(null) == Material.WATER)
            {
                flag = true;
                break;
            }
        }
        
        if (flag)
        {
        	int meta = worldIn.getBlockState(pos).getBlock().getMetaFromState( worldIn.getBlockState(pos) );
        	worldIn.setBlockState(pos, ModBlocksVN.blockConcrete.getStateFromMeta( meta ), 2);
        }

        return flag;
    }
    
    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock)
    {
    	if (!this.tryTouchWater(worldIn, pos)) {
    		super.neighborChanged(state, worldIn, pos, neighborBlock);
    	}
    }
    
    
    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
    	if (!this.tryTouchWater(worldIn, pos)) {
    		super.onBlockAdded(worldIn, pos, state);
    	}
    }
}