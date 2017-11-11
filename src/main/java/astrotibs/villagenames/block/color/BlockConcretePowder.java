package astrotibs.villagenames.block.color;

import java.util.List;
import java.util.Random;

import astrotibs.villagenames.block.ModBlocksVN;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
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

public class BlockConcretePowder extends BlockFalling implements IMetaBlockName {
	
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);
	
    public BlockConcretePowder(String unlocalizedName) {
        super(Material.sand);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(0.5F);
        this.setHarvestLevel("shovel", 0);
        this.setStepSound(soundTypeSand);
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
    
    @Override
	public void updateTick(World world, BlockPos pos, IBlockState state,  Random random)
    {
    	// This fires when you place the concrete powder block
        if (!world.isRemote)
        {
            this.checkFallable(world, pos);
        }
    }
    
    public void checkFallable(World world, BlockPos pos)
    {
        if ( canFallInto(world, new BlockPos( pos.getX(), pos.getY() - 1, pos.getZ() ) ) && pos.getY() >= 0 ) // True if block underneath is air, fire, water, or lava
        {
            byte i = 32;
            
            if (!fallInstantly && world.isAreaLoaded( pos.add(-i, -i, -i), pos.add(i, i, i) ) ) // Checks if surrounding chunks exist
            { // idk what fallInstantly actually does but apparently it's always off for this object
            	
                if (!world.isRemote)
                {
                	EntityFallingBlockCP entityfallingblock = new EntityFallingBlockCP(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, world.getBlockState(pos));
                    this.onStartFalling(entityfallingblock);
                    world.spawnEntityInWorld(entityfallingblock);
                }
            }
            else
            {
            	// Does not fire apparently
                world.setBlockToAir(pos);
                BlockPos blockpos;
                
                for (blockpos = pos.down(); canFallInto(world, blockpos) && blockpos.getY() > 0; blockpos = blockpos.down())
                {
                    ;
                }
                
                if (blockpos.getY() > 0)
                {
                	world.setBlockState(blockpos.up(), this.getDefaultState());
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
        	
            if (worldIn.getBlockState(new BlockPos(xo, yo, zo)).getBlock().getMaterial() == Material.water)
            {
                flag = true;
                break;
            }
        }
        
        if (flag)
        {
        	int meta = worldIn.getBlockState(pos).getBlock().getDamageValue(worldIn, pos);
        	worldIn.setBlockState(pos, ModBlocksVN.blockConcrete.getStateFromMeta(meta), 3);
        }

        return flag;
    }
    
    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
    	if (!this.tryTouchWater(worldIn, pos)) {
    		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
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

