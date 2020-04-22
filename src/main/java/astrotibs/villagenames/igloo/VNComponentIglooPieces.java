package astrotibs.villagenames.igloo;

import java.util.Random;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.ieep.ExtendedZombieVillager;
import astrotibs.villagenames.igloo.utils.Rotation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraftforge.common.ChestGenHooks;

public class VNComponentIglooPieces
{
    private static final String __OBFID = "CL_00000473";

    public static void registerScatteredFeaturePieces()
    {
        MapGenStructureIO.registerStructureComponent(VNComponentIglooPieces.Igloo.class, "Iglu");
    }
    
    
    abstract static class Feature extends StructureComponent
        {
            /** The size of the bounding box for this feature in the X axis */
            protected int scatteredFeatureSizeX;
            /** The size of the bounding box for this feature in the Y axis */
            protected int scatteredFeatureSizeY;
            /** The size of the bounding box for this feature in the Z axis */
            protected int scatteredFeatureSizeZ;
            protected int horizontalPos = -1;
            private static final String __OBFID = "CL_00000479";

            public Feature() {}

            protected Feature(Random rand, int x, int y, int z, int sizeX, int sizeY, int sizeZ)
            {
                super(0);
                this.scatteredFeatureSizeX = sizeX;
                this.scatteredFeatureSizeY = sizeY;
                this.scatteredFeatureSizeZ = sizeZ;
                this.coordBaseMode = EnumFacing.getHorizontal(
                		(rand.nextInt(4)+2+
                				1
                				)%4
                		); // In 1.9 is: this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

                switch (this.coordBaseMode.getHorizontalIndex()) // In 1.9 is: (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
                {
                    case 0: // North
                    case 2: // South
                        this.boundingBox = new StructureBoundingBox(x, y, z, x + sizeX - 3, y + sizeY - 1, z + sizeZ - 3);
                        break;
                    default: // 1: East; 3: West
                        this.boundingBox = new StructureBoundingBox(x, y, z, x + sizeZ - 3, y + sizeY - 1, z + sizeX - 3);
                }
            }

            protected void writeStructureToNBT(NBTTagCompound tagCompound) // 1.9's writeStructureToNBT
            {
                tagCompound.setInteger("Width", this.scatteredFeatureSizeX);
                tagCompound.setInteger("Height", this.scatteredFeatureSizeY);
                tagCompound.setInteger("Depth", this.scatteredFeatureSizeZ);
                tagCompound.setInteger("HPos", this.horizontalPos);
            }

            protected void readStructureFromNBT(NBTTagCompound tagCompound) // 1.9's readStructureFromNBT
            {
                this.scatteredFeatureSizeX = tagCompound.getInteger("Width");
                this.scatteredFeatureSizeY = tagCompound.getInteger("Height");
                this.scatteredFeatureSizeZ = tagCompound.getInteger("Depth");
                this.horizontalPos = tagCompound.getInteger("HPos");
            }

            protected boolean func_74935_a(World worldIn, StructureBoundingBox structurebb, int yOffset) // In 1.9: offsetToAverageGroundLevel()
            {
                if (this.horizontalPos >= 0)
                {
                    return true;
                }
                else
                {
                    int i = 0;
                    int j = 0;
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                    for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k)
                    {
                        for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l)
                        {
                            blockpos$mutableblockpos.set(l, 64, k);

                            if (structurebb.isVecInside(blockpos$mutableblockpos))
                            {
                                i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel());
                                ++j;
                            }
                        }
                    }

                    if (j == 0)
                    {
                        return false;
                    }
                    else
                    {
                        this.horizontalPos = i / j;
                        this.boundingBox.offset(0, this.horizontalPos - this.boundingBox.minY + yOffset, 0);
                        return true;
                    }
                }
            }
        }

    public static class Igloo extends VNComponentIglooPieces.Feature
        {
           
            // From 1.9
            //private static final ResourceLocation IGLOO_TOP_ID = new ResourceLocation("igloo/igloo_top");
            //private static final ResourceLocation IGLOO_MIDDLE_ID = new ResourceLocation("igloo/igloo_middle");
            //private static final ResourceLocation IGLOO_BOTTOM_ID = new ResourceLocation("igloo/igloo_bottom");
            
            public Igloo() {}

            public Igloo(Random rand, int x, int z)
            {
                super(rand, x, 64, z, 7, 5, 8); // Random, x, y, z, sizeX, sizeY, sizeZ
            }
            
            
            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             * Author: Searge
             * Translated to 1.7/1.8 by: AstroTibs
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                if (!this.func_74935_a(worldIn, structureBoundingBoxIn, -1)) //offsetToAverageGroundLevel()
                {
                    return false;
                }
                else
                {
                	StructureBoundingBox structureboundingbox = this.getBoundingBox();
                	BlockPos blockpos = new BlockPos(structureboundingbox.minX, structureboundingbox.minY, structureboundingbox.minZ);
                	Rotation[] arotation = Rotation.values();
                	
                	
                	// Bounding box stuff to help locate furnace
                	int minX = structureBoundingBoxIn.minX;
                	int minY = structureBoundingBoxIn.minY;
                	int minZ = structureBoundingBoxIn.minZ;
                	int maxX = structureBoundingBoxIn.maxX;
                	int maxY = structureBoundingBoxIn.maxY;
                	int maxZ = structureBoundingBoxIn.maxZ;
                    
                    
                	int xOffset = -1;
                    int zOffset = 0;
                    
                	
                	// Always add the top (above-ground) part
                	
                	// Bottom
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2+xOffset,0,2, 6+xOffset,0,6, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // Floor of igloo
                    // Walls
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3+xOffset,0,1, 5+xOffset,2,1, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // North wall
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3+xOffset,0,7, 5+xOffset,2,7, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // South wall
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7+xOffset,0,3, 7+xOffset,2,5, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // East wall
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1+xOffset,0,3, 1+xOffset,2,5, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // West wall
                    // Corners
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2+xOffset,0,2, 2+xOffset,2,2, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // NW corner
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6+xOffset,0,2, 6+xOffset,2,2, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // NE corner
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6+xOffset,0,6, 6+xOffset,2,6, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // SE corner
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2+xOffset,0,6, 2+xOffset,2,6, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // SW corner
                    // Ceiling "supports"
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3+xOffset,3,2, 5+xOffset,3,2, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // North beam
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3+xOffset,3,6, 5+xOffset,3,6, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // South beam
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6+xOffset,3,3, 6+xOffset,3,5, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // East beam
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2+xOffset,3,3, 2+xOffset,3,5, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // West beam
                    // Ceiling proper
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3+xOffset,4,3, 5+xOffset,4,5, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false); // Floor of igloo
                    
                    // Hollow out the center
                    this.fillWithAir(worldIn, structureBoundingBoxIn, 3+xOffset,1,2, 5+xOffset,2,6);
                    this.fillWithAir(worldIn, structureBoundingBoxIn, 2+xOffset,1,3, 2+xOffset,2,5);
                    this.fillWithAir(worldIn, structureBoundingBoxIn, 6+xOffset,1,3, 6+xOffset,2,5);
                    this.fillWithAir(worldIn, structureBoundingBoxIn, 3+xOffset,3,3, 5+xOffset,3,5);
                    
                    // Add interior
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3+xOffset,1,3, 5+xOffset,1,5, Blocks.carpet.getDefaultState(), Blocks.carpet.getDefaultState(), false); // white carpet
                    
                    // Door
                    this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3+xOffset,0,0, 5+xOffset,2,0, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false);
                	this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4+xOffset,3,0, 4+xOffset,3,1, Blocks.snow.getDefaultState(), Blocks.snow.getDefaultState(), false);
                	this.fillWithAir   (worldIn, structureBoundingBoxIn, 4+xOffset,1,0, 4+xOffset,2,1);
                	// carpet head
                	this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3+xOffset,1,6, 5+xOffset,1,6, Blocks.carpet.getStateFromMeta(8), Blocks.carpet.getStateFromMeta(8), false);
                	// Bed
                	this.setBlockState(worldIn, Blocks.bed.getStateFromMeta(this.coordBaseMode.getHorizontalIndex()+8), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset, 1, 5, structureBoundingBoxIn); // Head
                	this.setBlockState(worldIn, Blocks.bed.getStateFromMeta(this.coordBaseMode.getHorizontalIndex()),   (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset, 1, 4, structureBoundingBoxIn); // Foot
                	// Wall items
                	this.setBlockState(worldIn, Blocks.crafting_table.getDefaultState(), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset, 1, 5, structureBoundingBoxIn);
                	this.setBlockState(worldIn, Blocks.redstone_torch.getStateFromMeta(5), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset, 1, 4, structureBoundingBoxIn);
                	this.setBlockState(worldIn, Blocks.furnace.getStateFromMeta((new int[]{5,3,4,2})[this.coordBaseMode.getHorizontalIndex()]), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset, 1, 3, structureBoundingBoxIn);
                	
                	// Blank out the space next to the furnace to ensure it will face the right direction
                	//this.setBlockState(worldIn, Blocks.furnace.getDefaultState(), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset, 1, 3, structureBoundingBoxIn);
                	//this.setBlockState(worldIn, Blocks.air.getDefaultState(), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset, 1, 4, structureBoundingBoxIn);
                	
                	//this.block
                	// Ice windows
                	this.setBlockState(worldIn, Blocks.ice.getDefaultState(), 1+xOffset, 1, 4, structureBoundingBoxIn);
                	this.setBlockState(worldIn, Blocks.ice.getDefaultState(), 7+xOffset, 1, 4, structureBoundingBoxIn);
                    
                	
                	Block blocktoscan;
                	int   metatoscan  = -1;
                	
                    if (randomIn.nextDouble() < 0.5D) // Add an underground part!
                    {
                    	
                    	// Hatch
                    	this.setBlockState(worldIn, Blocks.trapdoor.getStateFromMeta((new int[]{8,11,9,10})[this.coordBaseMode.getHorizontalIndex()]), 4+xOffset, 0, 5, structureBoundingBoxIn);
                    	
                    	// How deep should the column be
                    	int basementCeilingY= -2 - ((((randomIn.nextInt(8) + 4)+9)%12)*3);
                    	
                    	
                    	// ---------------- //
                    	// --- BASEMENT --- //
                    	// ---------------- //
                    	
                    	
                    	// Hollow out basement
                    	this.fillWithAir(worldIn, structureBoundingBoxIn,
                    			2+xOffset,basementCeilingY-3,1,
                    			6+xOffset,basementCeilingY-1,5
                    			);
                    	
                    	// Basement ceiling
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			2+xOffset,basementCeilingY,1,
                    			6+xOffset,basementCeilingY,5,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	// Portion behind ladder column
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			3+xOffset,basementCeilingY,0,
                    			5+xOffset,basementCeilingY,0,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	// Front wall
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			2+xOffset,basementCeilingY-2,6,
                    			6+xOffset,basementCeilingY-1,6,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	// Front wall moss bottom
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			2+xOffset,basementCeilingY-3,6,
                    			6+xOffset,basementCeilingY-3,6,
                    			Blocks.stonebrick.getStateFromMeta(1), Blocks.stonebrick.getStateFromMeta(1), false);
                    	
                    	// Left wall
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			1+xOffset,basementCeilingY-2,1,
                    			1+xOffset,basementCeilingY-1,5,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	// Left wall moss bottom
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			1+xOffset,basementCeilingY-3,1,
                    			1+xOffset,basementCeilingY-3,5,
                    			Blocks.stonebrick.getStateFromMeta(1), Blocks.stonebrick.getStateFromMeta(1), false);
                    	
                    	// Right wall
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			7+xOffset,basementCeilingY-2,1,
                    			7+xOffset,basementCeilingY-1,5,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	// Right wall moss bottom
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			7+xOffset,basementCeilingY-3,1,
                    			7+xOffset,basementCeilingY-3,5,
                    			Blocks.stonebrick.getStateFromMeta(1), Blocks.stonebrick.getStateFromMeta(1), false);
                    	
                    	// Basement floor
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			2+xOffset,basementCeilingY-4,1,
                    			6+xOffset,basementCeilingY-4,5,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	
                    	
                    	
                    	// ------------ //
                    	// --- CELL --- //
                    	// ------------ //
                    	
                    	// Cell ceiling
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			3+xOffset,basementCeilingY,-1,
                    			5+xOffset,basementCeilingY,0,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	// Right cell wall
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			6+xOffset,basementCeilingY-3,-2,
                    			6+xOffset,basementCeilingY-1,0,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	// Left cell wall
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			2+xOffset,basementCeilingY-3,-2,
                    			2+xOffset,basementCeilingY-1,0,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	// Back cell wall
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			3+xOffset,basementCeilingY-3,-2,
                    			5+xOffset,basementCeilingY-1,-2,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	// Hollow out cell
                    	this.fillWithAir(worldIn, structureBoundingBoxIn,
                    			3+xOffset,basementCeilingY-3,-1,
                    			5+xOffset,basementCeilingY-1,0
                    			);
                    	
                    	// Cell top lip
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			3+xOffset,basementCeilingY-1,0,
                    			5+xOffset,basementCeilingY-1,0,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	// Cell floor
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			3+xOffset,basementCeilingY-4,-1,
                    			5+xOffset,basementCeilingY-4,0,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	
                    	// Cell moss bottom
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			2+xOffset,basementCeilingY-3,0,
                    			6+xOffset,basementCeilingY-3,0,
                    			Blocks.stonebrick.getStateFromMeta(1), Blocks.stonebrick.getStateFromMeta(1), false);
                    	
                    	// Bars
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			3+xOffset,basementCeilingY-3,0,
                    			5+xOffset,basementCeilingY-2,0,
                    			Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
                    	
                    	// Cell divider
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                    			4+xOffset,basementCeilingY-3,-1,
                    			4+xOffset,basementCeilingY-1,-1,
                    			Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	// Mossy divider bottom
                    	this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(1), 4+xOffset,basementCeilingY-3,0, structureBoundingBoxIn); // Left of brewing stand
                    	
                    	
                    	
                    	// ---------------- //
                    	// --- INTERIOR --- //
                    	// -- DECORATIONS --//
                    	// ---------------- //

                    	// Torches beside the ladder
                    	for (int i: new int[]{-1,1}) {
                    		this.setBlockState(worldIn, Blocks.torch.getStateFromMeta((new int[]{4,1,3,2})[this.coordBaseMode.getHorizontalIndex()]), 4+i+xOffset,basementCeilingY-1,5, structureBoundingBoxIn);
                    	}
                    	// Torch between the cells
                    	this.setBlockState(worldIn, Blocks.torch.getStateFromMeta((new int[]{3,2,4,1})[this.coordBaseMode.getHorizontalIndex()]), 4+xOffset,basementCeilingY-1,1, structureBoundingBoxIn);
                    	
                    	// Cobweb
                    	this.setBlockState(worldIn, Blocks.web.getStateFromMeta((new int[]{3,2,4,1})[this.coordBaseMode.getHorizontalIndex()]),
                    			(this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset,basementCeilingY-1,1, structureBoundingBoxIn);
                    	
                    	// Sign
                    	this.setBlockState(worldIn, Blocks.wall_sign.getStateFromMeta((new int[]{3,4,2,5})[this.coordBaseMode.getHorizontalIndex()]), 4+xOffset,basementCeilingY-2,1, structureBoundingBoxIn);
                    	
                    	
                    	// Alchemy Table
                    	this.setBlockState(worldIn, Blocks.spruce_stairs.getStateFromMeta((new int[]{6,5,7,4})[this.coordBaseMode.getHorizontalIndex()]), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset,basementCeilingY-3,5, structureBoundingBoxIn);
                    	this.setBlockState(worldIn, Blocks.wooden_slab.getStateFromMeta(9), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset,basementCeilingY-3,4, structureBoundingBoxIn);
                    	this.setBlockState(worldIn, Blocks.spruce_stairs.getStateFromMeta((new int[]{7,4,6,5})[this.coordBaseMode.getHorizontalIndex()]), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset,basementCeilingY-3,3, structureBoundingBoxIn);
                    	
                    	
                    	// Brewing stand
                    	this.setBlockState(worldIn, Blocks.brewing_stand.getDefaultState(), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset,basementCeilingY-2,4, structureBoundingBoxIn);
                    	/*
            			BlockPos brewingStandPos = new BlockPos( (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset, basementCeilingY-2, 4 );
            			
            			TileEntityBrewingStand brewingStandContents = new TileEntityBrewingStand();
            			brewingStandContents.setInventorySlotContents(1, new ItemStack( Items.potionitem, 1, 16392));
            			
            			worldIn.setBlockState( brewingStandPos, Blocks.brewing_stand.getDefaultState().withProperty(BlockBrewingStand.HAS_BOTTLE[1], Boolean.valueOf(true)) );
            			worldIn.setTileEntity( brewingStandPos, brewingStandContents);
            	    	
            			worldIn.markBlockForUpdate(brewingStandPos);
                    	*/
                    	
                    	
                    	
                    	
                    	// clay pot moved to post cleanup because of tile entity failings
                    	
                    	
                    	// Cauldron
                    	this.setBlockState(worldIn, Blocks.cauldron.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset,basementCeilingY-3,2, structureBoundingBoxIn);
                    	
                    	// Chest
                    	this.setBlockState(worldIn, Blocks.chest.getStateFromMeta((new int[]{5,3,4,2})[this.coordBaseMode.getHorizontalIndex()]), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset,basementCeilingY-3,4, structureBoundingBoxIn);
                    	
                    	// Floor mat
                    	this.setBlockState(worldIn, Blocks.carpet.getStateFromMeta(14), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset,basementCeilingY-3,2, structureBoundingBoxIn);
                    	this.setBlockState(worldIn, Blocks.carpet.getStateFromMeta(14), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset,basementCeilingY-3,1, structureBoundingBoxIn);
                    	
                    	
                    	
                    	// ------------------ //
                    	// ----- STONE ------ //
                    	// -- REPLACEMENTS -- //
                    	// ------------------ //
                    	
                    	// The nine chiseled stone bricks in the floor
                    	for (int chiseledX: new int[]{2,4,6}) {
                    		for (int chiseledZ: new int[]{5, 3, 1}) {
                    			this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(3), chiseledX+xOffset,basementCeilingY-4,chiseledZ, structureBoundingBoxIn);
                    		}
                    	}
                    	
                    	// Cracked stone brick positions
                    	this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(2), 4+xOffset,basementCeilingY-2,0, structureBoundingBoxIn); // Behind the sign
                    	for (int i : new int[]{3,5}) {
                    		this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(2), i+xOffset,basementCeilingY-4,-1, structureBoundingBoxIn); // Floor of a cell
                    	}
                    	this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?1:7)+xOffset,basementCeilingY-2,1, structureBoundingBoxIn); // Above the mat
                    	this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?1:7)+xOffset,basementCeilingY-2,4, structureBoundingBoxIn); // Above the chest
                    	this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(2), 4+xOffset,basementCeilingY-2,6, structureBoundingBoxIn); // Behind the chest
                    	this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset,basementCeilingY-2,6, structureBoundingBoxIn); // Right of brewing stand
                    	this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?7:1)+xOffset,basementCeilingY-2,3, structureBoundingBoxIn); // Left of brewing stand
                    	
                    	// Smooth Andesite under the mat
                    	this.setBlockState(worldIn, Blocks.stone.getStateFromMeta(6), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset,basementCeilingY-4,1, structureBoundingBoxIn);
                    	
                    	
                    	// Silverfish eggs
                    	
                    	// Chiseled: center ceiling
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(5), 4+xOffset,basementCeilingY,3, structureBoundingBoxIn);
                    	// Chiseled: across from the andesite
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(5), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset,basementCeilingY-4,1, structureBoundingBoxIn);
                    	// Mossy: between the ladder and the chest
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(3), (this.coordBaseMode.getHorizontalIndex()<2?3:5)+xOffset,basementCeilingY-3,6, structureBoundingBoxIn);
                    	// Brick: in ceiling
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?3:5)+xOffset,basementCeilingY,4, structureBoundingBoxIn);
                    	// Brick: between the ladder and the chest
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?2:6)+xOffset,basementCeilingY-2,6, structureBoundingBoxIn);
                    	// Brick: behind the brewing stand
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?7:1)+xOffset,basementCeilingY-2,4, structureBoundingBoxIn);
                    	// Brick: beneath the brewing stand
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?6:2)+xOffset,basementCeilingY-4,4, structureBoundingBoxIn);
                    	// Brick: above mat
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?1:7)+xOffset,basementCeilingY-1,2, structureBoundingBoxIn);
                    	// Brick: next to the mat
                    	this.setBlockState(worldIn, Blocks.monster_egg.getStateFromMeta(2), (this.coordBaseMode.getHorizontalIndex()<2?3:5)+xOffset,basementCeilingY-4,2, structureBoundingBoxIn);
                    	
                    	
                    	
                    	// ---------------- //
                    	// --- ENTRANCE --- //
                    	// ---------------- //
                    	
                    	
                    	// Stone column
                    	for (int i: new int[]{0,1,2,3}) {
                   			this.fillWithBlocks(worldIn, structureBoundingBoxIn,
                   					4+xOffset+(new int[]{-1,0,0,1})[i],basementCeilingY,5+(new int[]{0,-1,1,0})[i],
                   					4+xOffset+(new int[]{-1,0,0,1})[i],-1              ,5+(new int[]{0,-1,1,0})[i],
                   					Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
                    	}
                    	// Ladder
                    	this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4+xOffset,basementCeilingY-2,5, 4+xOffset,-1,5,
                    			Blocks.ladder.getStateFromMeta((new int[]{2,5,3,4})[this.coordBaseMode.getHorizontalIndex()]),
                    			Blocks.ladder.getStateFromMeta((new int[]{2,5,3,4})[this.coordBaseMode.getHorizontalIndex()]),
                    			false);
                    	
                    	
                    	
                    	
                    	// ---------------- //
                    	// --- BASEMENT --- //
                    	// --- CLEANUP ---- //
                    	// ---------------- //
                    	
                    	
                    	this.basementTouchup(worldIn, structureBoundingBoxIn, randomIn, minX, maxX, minZ, maxZ, this.coordBaseMode.getHorizontalIndex());
                    	
                    }
                    
                	
                	// ----------------- //
                	// --- TOP-LEVEL --- //
                    // ---- CLEANUP ---- //
                	// ----------------- //
                    
                    // Fix furnace TODO - not sure if this can be done easily...
                    // 1.7.10 does something stupid where the furnace will be automatically re-oriented.
                    
                    /*
                    for (int y = 55; y <= worldIn.getActualHeight(); y++) { // Reasonable span to check for the furnace
                    	for (int x = minX; x <= maxX; x++) {
                    		for (int z = minZ; z <= maxZ; z++) {
                    			if (worldIn.getBlock(x, y, z) == Blocks.crafting_table) {
                    				
                    				// Detected a crafting table. Place the furnace as necessary.
                    				
                    				// Clear out blocks that would have surrounded the furnace
                    				for (int w=0; w <=3; w++) {
                    					worldIn.setBlock(
                        						x + (new int[]{0, 2, 0, -2})[this.coordBaseMode.getHorizontalIndex()] + (new int[]{-1,0,1,0}[w]),
                        						y,
                        						z + (new int[]{-2, 0, 2, 0})[this.coordBaseMode.getHorizontalIndex()] + (new int[]{0,1,0,-1}[w]),
                        						//Blocks.furnace, (new int[]{2,5,3,4})[this.coordBaseMode.getHorizontalIndex()], 2);
                        						Blocks.air, (new int[]{3,4,2,5})[this.coordBaseMode.getHorizontalIndex()], 2);
                    				}
                    				
                    				// Place the furnace
                    				worldIn.setBlock(
                    						x + (new int[]{0, 2, 0, -2})[this.coordBaseMode.getHorizontalIndex()],
                    						y,
                    						z + (new int[]{-2, 0, 2, 0})[this.coordBaseMode.getHorizontalIndex()],
                    						//Blocks.furnace, (new int[]{2,5,3,4})[this.coordBaseMode.getHorizontalIndex()], 2);
                    						Blocks.furnace, (new int[]{3,4,2,5})[this.coordBaseMode.getHorizontalIndex()], 2);
                    						
                    			}
                    		}
                    	}
                    }
                    */
                    
                    return true;
                	
                	
                	
                	
                	// Placing the furnace is tricky
                	/*
                	//Block blocktofix;
                	for (int i=minX; i<=maxX; i++) {
                		for (int k=minZ; k<=maxZ; k++) {
                			for (int j=48; j<=71; j++) {
                				if (worldIn.getBlock(i, j, k) == Blocks.furnace) {
                					worldIn.setBlock(i, j, k, Blocks.furnace, this.coordBaseMode.getHorizontalIndex()==0?3:this.coordBaseMode.getHorizontalIndex()==3?0:(this.coordBaseMode.getHorizontalIndex()+2), 2);
                					LogHelper.info("Furnace meta changed to " + (this.coordBaseMode.getHorizontalIndex()==0?5:this.coordBaseMode.getHorizontalIndex()==3?2:(this.coordBaseMode.getHorizontalIndex()+2)) );
                				}
                			}
                		}
                	}
                	*/
                	
                }
            }
            
            
            /**
             * Called to summon the villager and zombie in the igloo basement ,
             * to set the sign text, and to put the potion in the brewing stand.
             */
            protected void basementTouchup(World worldIn, StructureBoundingBox structureBoundingBoxIn, Random randomIn, int minX, int maxX, int minZ, int maxZ, int coordBaseMode) {
            	
            	for (int y = 1; y <= worldIn.getActualHeight(); y++) {
            		for (int x = minX; x <= maxX; x++) {
                		for (int z = minZ; z <= maxZ; z++) {
                			if (worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.brewing_stand) {
                				
                				
                				// Spawn priest
                				
                                EntityVillager villager = new EntityVillager(worldIn, 2); // 2 is a Priest
                                
                                villager.heal(villager.getMaxHealth());
                                villager.setLocationAndAngles(
                                		(double)(x + (new int[]{-3, 5, 3, -5})[coordBaseMode]) + 0.5D,
                                		(double)y,
                                		(double)(z + (new int[]{-5, -3, 5, 3})[coordBaseMode]) + 0.5D,
                                		0.0F, 0.0F);
                                villager.enablePersistence();
                                worldIn.spawnEntityInWorld(villager);
                                
                                
                                // Spawn Zombie
                                                                
                                EntityZombie zombie = new EntityZombie(worldIn);
                                zombie.heal(zombie.getMaxHealth());
                                zombie.setLocationAndAngles(
                                		(double)(x + (new int[]{-1, 5, 1, -5})[coordBaseMode]) + 0.5D,
                                		(double)y,
                                		(double)(z + (new int[]{-5, -1, 5, 1})[coordBaseMode]) + 0.5D,
                                		90.0F*this.coordBaseMode.getHorizontalIndex(), 0.0F);
                                zombie.enablePersistence();
                                zombie.setVillager(true);
                                if(GeneralConfig.modernVillagerSkins) {
                                	(ExtendedZombieVillager.get( zombie )).setProfession(2);
                                	(ExtendedZombieVillager.get( zombie )).setCareer(1);
                                }
                                worldIn.spawnEntityInWorld(zombie);
                				
                				
                                
                                
                                // Place cactus in a pot
                                
                        		TileEntity flowerPotWithCactus = (new BlockFlowerPot()).createNewTileEntity(worldIn, 9);
                        		BlockPos cactusPotPos = new BlockPos(
                            			(x + (new int[]{0, -1, 0, 1})[coordBaseMode]),
                                		y,
                                		(z + (new int[]{1, 0, -1, 0})[coordBaseMode])
                						);
                        		worldIn.setBlockState(cactusPotPos, Blocks.flower_pot.getDefaultState());
                        		
                        		worldIn.setTileEntity(cactusPotPos, flowerPotWithCactus);
                                
                        		
                                
                                // Set sign contents
                                
                            	TileEntitySign signContents = new TileEntitySign();
                            	signContents.signText[1] = new ChatComponentText("<----");
                            	signContents.signText[2] = new ChatComponentText("---->");
                            	worldIn.setTileEntity(
                            			new BlockPos(
	                            			(x + (new int[]{-2, 3, 2, -3})[coordBaseMode]),
	                                		y,
	                                		(z + (new int[]{-3, -2, 3, 2})[coordBaseMode])
	                                		),
                            			signContents);
                                
                            	
                            	
                            	// Set brewing stand contents
                            	
								BlockPos brewingStandPos = new BlockPos(x, y, z);
								
								worldIn.setBlockState( brewingStandPos, Blocks.brewing_stand.getDefaultState() );
								
								TileEntity tileEntityBrewingStand = worldIn.getTileEntity(brewingStandPos);
								((IInventory) tileEntityBrewingStand).setInventorySlotContents(1, new ItemStack( Items.potionitem, 1, 16392));
								worldIn.setTileEntity( brewingStandPos, tileEntityBrewingStand);
                            	
                            	
                            	
                            	
                            	// Chest stuff
                            	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
                            	
                            	TileEntity tileEntityChest = worldIn.getTileEntity(
                            			new BlockPos(
	                            			(x + (new int[]{-4, 0, 4, 0})[coordBaseMode]),
	                                		y-1,
	                                		(z + (new int[]{0, -4, 0, 4})[coordBaseMode])
	                                		)
                            			);
                            	if (tileEntityChest instanceof IInventory) {
                            		
	                            	ChestGenHooks iglooChest = ChestGenHooks.getInfo("iglooChest");
	                            	WeightedRandomChestContent.generateChestContents(randomIn, iglooChest.getItems(randomIn), (TileEntityChest)tileEntityChest, iglooChest.getCount(randomIn));
	                            	
	                            	ChestGenHooks iglooChestGoldapple = ChestGenHooks.getInfo("iglooChestGoldapple");
	                            	WeightedRandomChestContent.generateChestContents(randomIn, iglooChestGoldapple.getItems(randomIn), (TileEntityChest)tileEntityChest, iglooChestGoldapple.getCount(randomIn));
                            	}
                            	
                				return;
                			}
                		}
                	}
                }
            }
            
            
        }


}