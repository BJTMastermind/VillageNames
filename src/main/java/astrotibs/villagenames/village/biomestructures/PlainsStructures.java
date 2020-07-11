package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PlainsStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Fountain --- //
	
    public static class PlainsFountain01 extends StartVN
    {
    	public PlainsFountain01() {}
    	
    	public PlainsFountain01(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);

    		int width = 8;
    		int depth = 8;
    		int height = 3;
    		
		    // Establish orientation
    		this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
    		
            switch (this.coordBaseMode)
            {
	            case NORTH:
	            case SOUTH:
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + width, 64+height, posZ + depth);
                    break;
                default:
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + depth, 64+height, posZ + width);
            }
            
            //StructureVillageVN.establishBiomeBlocks(this, posX, posZ);
    	}

		/*
		 * Add the paths that lead outward from this structure
		 */
    	@Override
		public void buildComponent(StructureComponent start, List components, Random random)
		{
    		if (GeneralConfig.debugMessages)
    		{
    			LogHelper.info(
    					this.materialType + " " +  this.villageType + " village generated in "
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode + ", horiz index: " + this.coordBaseMode.getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 3, EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 3, EnumFacing.WEST, this.getComponentType());
		}
    	
		/*
		 * Construct the structure
		 */
    	@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void
                
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
        			this.getXWithOffset(4, 4),
        			this.getYWithOffset(2),
        			this.getZWithOffset(4, 4));
        	int townColor = villageNBTtag.getInteger("townColor");
        	int townColor2 = villageNBTtag.getInteger("townColor2");
        	
        	
        	// Clear out area
        	this.fillWithAir(world, structureBB, 2, 1, 2, 6, 4, 6);
        	
            // Basin bottom
        	this.fillWithBlocks(world, structureBB, 2, -1, 2, 6, 0, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            
            // Torches with the correct metas
            for (int[] uvwm : new int[][]{
            	{2, 1, 2, 0},
            	{2, 1, 6, 0},
            	{6, 1, 2, 0},
            	{6, 1, 6, 0},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
            }
            
            if (GeneralConfig.decorateVillageCenter)
            {
            	IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 2, 1, 3, 6, 1, 5, concreteBlockstate, concreteBlockstate, false);
                this.fillWithBlocks(world, structureBB, 3, 1, 2, 5, 1, 6, concreteBlockstate, concreteBlockstate, false);
                
                // Under-torch GT
            	BlockPos uvw = new BlockPos(2, 0, 2); // Starting position of the block cluster. Use lowest X, Z.
            	
            	if (GeneralConfig.addConcrete)
            	{
                	int metaBase = ((int)world.getSeed()%4+this.coordBaseMode.getHorizontalIndex())%4; // Procedural based on world seed and base mode
            		int metaCycle = (metaBase+Math.abs(this.getXWithOffset(uvw.getX(), uvw.getZ())%2 - (this.getZWithOffset(uvw.getX(), uvw.getZ())%2)*3) + uvw.getY())%4; // Procedural based on block X, Y, Z 
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor2, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            		
            		uvw = uvw.south(4); metaCycle = (metaCycle+1)%4;
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor2, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            		
            		uvw = uvw.east(4); metaCycle = (metaCycle+1)%4;
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor2, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            		
            		uvw = uvw.north(4); metaCycle = (metaCycle+1)%4;
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor2, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            	}
                else
                {
                    this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 2, 0, 2, structureBB);
                    this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 2, 0, 6, structureBB);
                    this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 6, 0, 2, structureBB);
                    this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 6, 0, 6, structureBB);
                }
            }
            else
            {
            	// Basin rim
                this.fillWithBlocks(world, structureBB, 2, 1, 3, 6, 1, 5, biomeCobblestoneState, biomeCobblestoneState, false);
                this.fillWithBlocks(world, structureBB, 3, 1, 2, 5, 1, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            
            this.fillWithAir(world, structureBB, 3, 1, 3, 5, 1, 5);
            
            // Spout
            if (GeneralConfig.decorateVillageCenter)
            {
            	IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor2);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor2);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, concreteBlockstate, concreteBlockstate, false);
            }
            else
            {
            	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            this.setBlockState(world, Blocks.flowing_water.getDefaultState(), 4, 3, 4, structureBB);
            
            // Encircle the fountain with path
        	StructureVillagePieces.Start startPiece_reflected = ReflectionHelper.getPrivateValue(StructureVillagePieces.Village.class, this, new String[]{"startPiece"});
        	for (int i = 1; i <= 7; ++i)
            {
                for (int j = 1; j <= 7; ++j)
                {
                    if (j == 1 || j == 7 || i == 1 || i == 7)
                    {
                    	// Gets ground level, so long as it's not leaves or other foliage
                    	int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i, j), 0, this.getZWithOffset(i, j))).down().getY();
                        if (k > -1)
                        {
                        	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j));
                        	this.clearCurrentPositionBlocksUpwards(world, i, k+1-this.boundingBox.minY, j, structureBB);
                       	}
                    }
                }
            }
        	
            // Add path nodules at the end
            for (int i : new int[]{1,2,3,4,5,6,7})
            {
            	for (int j : new int[]{0,8})
            	{
            		int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i, j), 0, this.getZWithOffset(i, j))).down().getY();
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, i, k+2-this.boundingBox.minY, j, structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j));
                   	}
                    
                    k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(j, i), 0, this.getZWithOffset(j, i))).down().getY();
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, j, k+2-this.boundingBox.minY, i, structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(j, i), k, this.getZWithOffset(j, i));
                   	}
            	}
            }
            
            
        	// Sign
            int signXBB = 4;
			int signYBB = 2;
			int signZBB = 2;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
    		
			// Banner    		
    		if (GeneralConfig.decorateVillageCenter)
    		{
    			int bannerXBB = 8;
    			int bannerZBB = 6;
    			int bannerYBB = -1;
    			if (this.bannerY==-1)
    			{
    				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(bannerXBB, bannerZBB), 0, this.getZWithOffset(bannerXBB, bannerZBB))).getY()-this.boundingBox.minY;
    				bannerYBB = this.bannerY;
    			}
    			else {bannerYBB = this.bannerY;}
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a cobblestone foundation
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode.getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
    		}
        	
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	            	
        		for (int[] ia : new int[][]{
        			{6, 1, 1, -1, 0},
        			{1, 1, 2, -1, 0},
        			{1, 1, 7, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.11
        			if (false && random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(ia[0], ia[2]), 0, this.getZWithOffset(ia[0], ia[2]))).getY();
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(entityvillager);
        		}
            }
            
            return true;
        }
    }
    
    
    
    // --- Well --- //
    
    public static class PlainsMeetingPoint1 extends StartVN
    {
	    int wellDepthDecrease=7;
	    
		public PlainsMeetingPoint1() {}
		
		public PlainsMeetingPoint1(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);

    		int width = 9;
    		int depth = 9;
    		
		    // Establish orientation
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode)
            {
                case NORTH:
                case SOUTH:
                    this.boundingBox = new StructureBoundingBox(posX, 64+wellDepthDecrease, posZ, posX + width, 79, posZ + depth);
                    break;
                default:
                    this.boundingBox = new StructureBoundingBox(posX, 64+wellDepthDecrease, posZ, posX + depth, 79, posZ + width);
            }
            
            //StructureVillageVN.establishBiomeBlocks(this, posX, posZ);
		}
		
		/*
		 * Add the paths that lead outward from this structure
		 */
		@Override
		public void buildComponent(StructureComponent start, List components, Random random)
		{
    		if (GeneralConfig.debugMessages)
    		{
    			LogHelper.info(
    					this.materialType + " " +  this.villageType + " village generated in "
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode + ", horiz index: " + this.coordBaseMode.getHorizontalIndex()
    					);
    		}
    		
        	// Northward
        	StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()<=1? 3 : 4), this.boundingBox.maxY - 5, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
        	// Eastward
        	StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()<=1? 3 : 4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()<=1? 4 : 3), this.boundingBox.maxY - 5, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()<=1? 4 : 3), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeCobblestoneSlabState = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.field_143015_k < 0)
            {
                //this.field_143015_k = StructureVillagePiecesVN.getMedianGroundLevel(world, structureBB, true);//this.getAverageGroundLevel(world, structureBoundingBox);
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + (5-1) - wellDepthDecrease, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
        			this.getXWithOffset(4, 4),
        			this.getYWithOffset(12),
        			this.getZWithOffset(4, 4));
        	int townColor = villageNBTtag.getInteger("townColor");
        	int townColor2 = villageNBTtag.getInteger("townColor2");
        	
        	
            // The well gets filled completely with water first
            //this.fillWithBlocks(world, structureBoundingBox, 3, 0+wellDepthDecrease, 3, 6, 12, 6, this.biomeCobblestoneBlock, Blocks.flowing_water, false);
            this.fillWithBlocks(world, structureBB, 3, 0+wellDepthDecrease, 3, 6, 12, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 4, 1+wellDepthDecrease, 4, 5, 12, 5, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false); // Water
            
            // Well rim
            if (GeneralConfig.decorateVillageCenter)
            {
            	this.fillWithBlocks(world, structureBB, 4, 12, 3, 5, 12, 3, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 4, 12, 6, 5, 12, 6, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 3, 12, 4, 3, 12, 5, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 6, 12, 4, 6, 12, 5, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            }
            
            // I believe this replaces the top water level with air
            this.setBlockState(world, Blocks.air.getDefaultState(), 4, 12, 4, structureBB);
            this.setBlockState(world, Blocks.air.getDefaultState(), 5, 12, 4, structureBB);
            this.setBlockState(world, Blocks.air.getDefaultState(), 4, 12, 5, structureBB);
            this.setBlockState(world, Blocks.air.getDefaultState(), 5, 12, 5, structureBB);
            
            // Well support posts
            for (int i : new int[]{3, 6})
            {
                for (int j : new int[]{3, 6})
                {
                	this.fillWithBlocks(world, structureBB, i, 13, j, i, 14, j, biomeFenceState, biomeFenceState, false);
                }
            }
            
            // Roof of the well
            this.fillWithBlocks(world, structureBB, 3, 15, 3, 6, 15, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            
            if (GeneralConfig.decorateVillageCenter)
            {
            	BlockPos uvw = new BlockPos(4, 15, 4); // Starting position of the block cluster. Use lowest X, Z.
            	
            	if (GeneralConfig.addConcrete)
            	{
            		int metaBase = ((int)world.getSeed()%4+this.coordBaseMode.getHorizontalIndex())%4; // Procedural based on world seed and base mode
            		int metaCycle = (metaBase+Math.abs(this.getXWithOffset(uvw.getX(), uvw.getZ())%2 - (this.getZWithOffset(uvw.getX(), uvw.getZ())%2)*3) + uvw.getY())%4; // Procedural based on block X, Y, Z 
                	
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor2, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            		
            		uvw = uvw.south(); metaCycle = (metaCycle+1)%4;
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor2, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            		
            		uvw = uvw.east(); metaCycle = (metaCycle+1)%4;
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor2, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            		
            		uvw = uvw.north(); metaCycle = (metaCycle+1)%4;
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor2, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            	}
            	else
            	{
            		this.fillWithBlocks(world, structureBB, 4, 15, 4, 5, 15, 5, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), Blocks.stained_hardened_clay.getStateFromMeta(townColor2), false);
            	}
            }
            
            // Line the well with cobblestone and ensure the spaces above are clear
            for (int i = 2; i <= 7; ++i)
            {
                for (int j = 2; j <= 7; ++j)
                {
                    if (j == 2 || j == 7 || i == 2 || i == 7)
                    {
                    	if (GeneralConfig.decorateVillageCenter)
                    	{
                    		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
                        	
                        	// Basin rim
                        	if (GeneralConfig.addConcrete)
                        	{
                        		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
                        	}
                        	
                    		this.fillWithBlocks(world, structureBB, j, 0+wellDepthDecrease, i, j, 11, i, concreteBlockstate, concreteBlockstate, false);
                    	}
                    	else
                    	{
                    		this.fillWithBlocks(world, structureBB, j, 0+wellDepthDecrease, i, j, 11, i, biomeCobblestoneState, biomeCobblestoneState, false);
                    	}
                        this.clearCurrentPositionBlocksUpwards(world, j, 12, i, structureBB);
                    }
                }
            }
            
            // How to place mod doors
            /*
            for (int i : new int[]{0,1})
            {
            	this.setBlockState(world, ModObjects.chooseModDoor(2), this.getMetadataWithOffset(Blocks.wooden_door, 0) + 8*i, 7, 12+i, 4, structureBB);
            	this.setBlockState(world, ModObjects.chooseModDoor(3), this.getMetadataWithOffset(Blocks.wooden_door, 1) + 8*i, 4, 12+i, 2, structureBB);
            	this.setBlockState(world, ModObjects.chooseModDoor(4), this.getMetadataWithOffset(Blocks.wooden_door, 2) + 8*i, 2, 12+i, 4, structureBB);
            	this.setBlockState(world, ModObjects.chooseModDoor(5), this.getMetadataWithOffset(Blocks.wooden_door, 3) + 8*i, 4, 12+i, 7, structureBB);
            }
            */
            
            // Over-lid torches
            for (int[] uvwm : new int[][]{
            	{3, 16, 3, 0},
            	{3, 16, 6, 0},
            	{6, 16, 3, 0},
            	{6, 16, 6, 0},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
            }
            
            
            // Encircle the well with path
        	StructureVillagePieces.Start startPiece_reflected = ReflectionHelper.getPrivateValue(StructureVillagePieces.Village.class, this, new String[]{"startPiece"});
        	for (int i = 1; i <= 8; ++i)
            {
                for (int j = 1; j <= 8; ++j)
                {
                    if (j == 1 || j == 8 || i == 1 || i == 8)
                    {
                    	// Gets ground level, so long as it's not leaves or other foliage
                        //int k = world.getTopSolidOrLiquidBlock(this.getBoundingBox().minX+i, this.getBoundingBox().minZ+j) - 1;
                    	int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i, j), 0, this.getZWithOffset(i, j))).down().getY();
                        if (k > -1)
                        {
                            StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j));
                        	this.clearCurrentPositionBlocksUpwards(world, i, k+1-this.boundingBox.minY, j, structureBB);
                       	}
                    }
                }
            }
            // Add path nodules at the end
            for (int i : new int[]{3,4,5})
            {
            	int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(0, i+1), 0, this.getZWithOffset(0, i+1))).down().getY();
                if (k > -1)
                {
                	this.clearCurrentPositionBlocksUpwards(world, 0, k+2-this.boundingBox.minY, i+1, structureBB);
                	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(0, i+1), k, this.getZWithOffset(0, i+1));
               	}
                
                k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(9, i), 0, this.getZWithOffset(9, i))).down().getY();
                if (k > -1)
                {
                	this.clearCurrentPositionBlocksUpwards(world, 9, k+2-this.boundingBox.minY, i, structureBB);
                	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(9, i), k, this.getZWithOffset(9, i));
               	}

                k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i, 0), 0, this.getZWithOffset(i, 0))).down().getY();
                if (k > -1)
                {
                	this.clearCurrentPositionBlocksUpwards(world, i, k+2-this.boundingBox.minY, 0, structureBB);
                	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, 0), k, this.getZWithOffset(i, 0));
               	}
                
                k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i+1, 9), 0, this.getZWithOffset(i+1, 9))).down().getY();
                if (k > -1)
                {
                	this.clearCurrentPositionBlocksUpwards(world, i+1, k+2-this.boundingBox.minY, 9, structureBB);
                	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i+1, 9), k, this.getZWithOffset(i+1, 9));
               	}
            }
            
            
            // Sign
            int signXBB = 6;
			int signYBB = 12;
			int signZBB = 7;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
                int bannerXBB = 8;
    			int bannerZBB = 6;
    			int bannerYBB = -1;
    			if (this.bannerY==-1)
    			{
    				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(bannerXBB, bannerZBB), 0, this.getZWithOffset(bannerXBB, bannerZBB))).getY()-this.boundingBox.minY;
    				bannerYBB = this.bannerY;
    			}
    			else {bannerYBB = this.bannerY;}
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a cobblestone foundation
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
    		}
    		
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{1, 11, 8, -1, 0},
        			{8, 11, 8, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.11
        			if (false && random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(ia[0], ia[2]), 0, this.getZWithOffset(ia[0], ia[2]))).getY();
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(entityvillager);
        		}
            }
            
            return true;
        }
    }
    
    

    // --- Market --- //
    
    public static class PlainsMeetingPoint2 extends StartVN
    {
	    public PlainsMeetingPoint2() {}
		
		public PlainsMeetingPoint2(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 7;
    		int depth = 14;
    		int height = 4;
    		
		    // Establish orientation
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random); 
            switch (this.coordBaseMode)
            {
                case NORTH:
                case SOUTH:
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + width, 64+height, posZ + depth);
                    break;
                default:
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + depth, 64+height, posZ + width);
            }
		}
		
		/*
		 * Add the paths that lead outward from this structure
		 */
		@Override
		public void buildComponent(StructureComponent start, List components, Random random)
		{
    		if (GeneralConfig.debugMessages)
    		{
    			LogHelper.info(
    					this.materialType + " " +  this.villageType + " village generated in "
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode + ", horiz index: " + this.coordBaseMode.getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()==0 ? 1 : this.coordBaseMode.getHorizontalIndex()==1 ? 10 : this.coordBaseMode.getHorizontalIndex()==2 ? 1 : 2), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()==0 ? 6 : this.coordBaseMode.getHorizontalIndex()==1 ? 1 : this.coordBaseMode.getHorizontalIndex()==2 ? 6 : 1), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()==0 ? 1 : this.coordBaseMode.getHorizontalIndex()==1 ? 6 : this.coordBaseMode.getHorizontalIndex()==2 ? 1 : 6), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()==0 ? 2 : this.coordBaseMode.getHorizontalIndex()==1 ? 1 : this.coordBaseMode.getHorizontalIndex()==2 ? 10 : 1), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeWoodenSlabState = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
        			this.getXWithOffset(4, 4),
        			this.getYWithOffset(12),
        			this.getZWithOffset(4, 4));
        	int townColor = villageNBTtag.getInteger("townColor");
        	int townColor2 = villageNBTtag.getInteger("townColor2");
        	
        	// Top layer is grass
        	this.fillWithBlocks(world, structureBB, 0, 0, 0, 7, 0, 14, biomeGrassState, biomeGrassState, false);
        	// Clear above
        	for (int i=0; i<=7; i++)
        	{
        		for (int j=0; j<=14; j++)
            	{
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, i, -1, j, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, i, 1, j, structureBB);
            	}
        	}
        	
        	// Set grass paths
        	for (int[] offset_xy : new int[][]{
        		{0, 2}, {0, 3}, 
        		{1, 0}, {1, 2}, {1, 3}, {1, 4}, {1, 13}, {1, 14}, 
        		{2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 12}, {2, 13}, {2, 14}, 
        		{3, 0}, {3, 1}, {3, 2}, {3, 4}, {3, 7}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {3, 13}, 
        		{4, 3}, {4, 4}, {4, 5}, {4, 6}, {4, 7}, {4, 9}, 
        		{5, 2}, {5, 3}, {5, 4}, {5, 5}, {5, 7}, {5, 8}, {5, 10}, {5, 12}, 
        		{6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 11}, {6, 12},
        		{7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 9}, {7, 11}, 
        	})
        	{
        		StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(offset_xy[0], offset_xy[1]), this.getYWithOffset(0), this.getZWithOffset(offset_xy[0], offset_xy[1]));
        	}
        	
        	// Unkempt grass
        	this.setBlockState(world, Blocks.tallgrass.getDefaultState(), 0, 1, 8, structureBB);
        	this.setBlockState(world, Blocks.tallgrass.getDefaultState(), 1, 1, 7, structureBB);
        	this.setBlockState(world, Blocks.tallgrass.getDefaultState(), 1, 1, 12, structureBB);
        	this.setBlockState(world, Blocks.tallgrass.getDefaultState(), 4, 1, 10, structureBB);
        	this.setBlockState(world, Blocks.tallgrass.getDefaultState(), 4, 1, 11, structureBB);
        	
        	// Stalls
        	this.fillWithBlocks(world, structureBB, 4, 1, 1, 7, 1, 1, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 4, 0, 1, 7, 0, 1, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 0, 4, 3, 0, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 2, 4, 3, 2, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 2, 7, 3, 2, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 0, 7, 3, 0, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 4, 0, 7, 4, 2, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 5, 4, 0, 6, 4, 2, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor : 4), Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor : 4), false);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 5, 4, 0, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 6, 4, 1, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 5, 4, 2, structureBB);
            
        	// Torches
            for (int[] uvwm : new int[][]{
            	{4, 2, 1, 0},
            	{7, 2, 1, 0},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
            }
        	
        	this.fillWithBlocks(world, structureBB, 2, 1, 5, 2, 1, 8, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 2, 0, 5, 2, 0, 8, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 1, 1, 5, 1, 3, 5, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 1, 1, 8, 1, 3, 8, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 8, 3, 3, 8, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 5, 3, 3, 5, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 5, 3, 4, 8, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 6, 3, 4, 7, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor : 4), Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor : 4), false);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 1, 4, 7, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 2, 4, 6, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 3, 4, 7, structureBB);

            // Torches
            for (int[] uvwm : new int[][]{
            	{2, 2, 5, 0},
            	{2, 2, 8, 0},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
            }
        	
        	this.fillWithBlocks(world, structureBB, 4, 1, 13, 7, 1, 13, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 4, 0, 13, 7, 0, 13, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 12, 4, 3, 12, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 14, 4, 3, 14, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 14, 7, 3, 14, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 12, 7, 3, 12, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 4, 12, 7, 4, 14, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 5, 4, 12, 6, 4, 14, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor : 4), Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor : 4), false);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 5, 4, 12, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 6, 4, 13, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), 5, 4, 14, structureBB);
        	
            // Torches
            for (int[] uvwm : new int[][]{
            	{4, 2, 13, 0},
            	{7, 2, 13, 0},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
            }
        	
        	        	        	
            // Sign
            int signXBB = 2;
			int signYBB = 2;
			int signZBB = 10;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
    		this.setBlockState(world, biomePlankState, signXBB, signYBB-1, signZBB, structureBB);
    		this.setBlockState(world, biomeDirtState, signXBB, signYBB-2, signZBB, structureBB);
        	
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
                int bannerXBB = 6;
    			int bannerZBB = 4;
    			int bannerYBB = -1;
    			if (this.bannerY==-1)
    			{
    				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(bannerXBB, bannerZBB), 0, this.getZWithOffset(bannerXBB, bannerZBB))).getY()-this.boundingBox.minY;
    				bannerYBB = this.bannerY;
    			}
    			else {bannerYBB = this.bannerY;}
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a grass foundation
                this.setBlockState(world, biomeGrassState, bannerXBB, bannerYBB-1, bannerZBB, structureBB);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
    		}
    		
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{6, 1, 6, -1, 0},
        			{5, 1, 8, -1, 0},
        			{5, 1, 10, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.11
        			if (false && random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(ia[0], ia[2]), 0, this.getZWithOffset(ia[0], ia[2]))).getY();
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(entityvillager);
        		}
            }
            
            return true;
        }
    }
    
    
	// --- Tree --- //
	
    public static class PlainsMeetingPoint3 extends StartVN
    {
    	public PlainsMeetingPoint3() {}
    	
    	public PlainsMeetingPoint3(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
    		int width = 10;
    		int depth = 10;
    		int height = 8;
    		
		    // Establish orientation
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode)
            {
	            case NORTH:
	            case SOUTH:
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + width, 64+height, posZ + depth);
                    break;
                default:
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + depth, 64+height, posZ + width);
            }

            //StructureVillageVN.establishBiomeBlocks(this, posX, posZ);
    	}

		/*
		 * Add the paths that lead outward from this structure
		 */
    	@Override
		public void buildComponent(StructureComponent start, List components, Random random)
		{
    		if (GeneralConfig.debugMessages)
    		{
    			LogHelper.info(
    					this.materialType + " " +  this.villageType + " village generated in "
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode + ", horiz index: " + this.coordBaseMode.getHorizontalIndex()
    					);
    		}
    		
			// Northward
			if (this.coordBaseMode.getHorizontalIndex()!=0) {StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			// Eastward
			if (this.coordBaseMode.getHorizontalIndex()!=1) {StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.coordBaseMode.getHorizontalIndex()!=2) {StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.coordBaseMode.getHorizontalIndex()!=3) {StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.WEST, this.getComponentType());}
		}
    	
		/*
		 * Construct the structure
		 */
    	@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.wall_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeStoneStairsState = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
        			this.getXWithOffset(5, 5),
        			this.getYWithOffset(2),
        			this.getZWithOffset(5, 5));
        	int townColor = villageNBTtag.getInteger("townColor");
        	int townColor2 = villageNBTtag.getInteger("townColor2");
        	
        	
        	// Level the ground with grass and then insert grass paths
        	
        	// Top layer is grass
        	this.fillWithBlocks(world, structureBB, 0, 0, 0, 10, 0, 10, biomeGrassState, biomeGrassState, false);
        	// Clear above
        	for (int i=0; i<=10; i++)
        	{
        		for (int j=0; j<=10; j++)
            	{
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, i, -1, j, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, i, 1, j, structureBB);
            	}
        	}
        	
        	// Set grass paths
        	for (int[] offset_xy : new int[][]{
        		{0, 2}, {0, 5}, {0, 8}, {0, 9}, 
        		{1, 0}, {1, 1}, {1, 3}, {1, 4}, {1, 7}, 
        		{2, 0}, {2, 2}, {2, 6}, {2, 8}, {2, 9}, 
        		{3, 0}, {3, 2}, {3, 5}, {3, 8}, 
        		{4, 7}, {4, 10}, 
        		{5, 0}, {5, 3}, {5, 7}, {5, 8}, {5, 9}, 
        		{6, 0}, 
        		{7, 4}, {7, 6}, {7, 7}, {7, 10}, 
        		{8, 1}, {8, 6}, {8, 9}, 
        		{9, 2}, {9, 3}, {9, 5}, {9, 8}, 
        		{10, 1}, {10, 4}, {10, 5}, 
        	})
        	{
        		StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(offset_xy[0], offset_xy[1]), this.getYWithOffset(0), this.getZWithOffset(offset_xy[0], offset_xy[1]));
        	}

        	// Set cobblestone
        	for (int[] offset_xy : new int[][]{
        		{0, 1}, {0, 4}, 
        		{1, 5}, {1, 8}, 
        		{2, 1}, {2, 4}, {2, 5}, {2, 7}, {2, 10}, 
        		{3, 3}, {3, 6}, {3, 9}, 
        		{4, 3}, {4, 8}, 
        		{5, 1}, {5, 2}, {5, 10}, 
        		{6, 2}, {6, 3}, {6, 9}, 
        		{7, 1}, {7, 3}, {7, 5}, {7, 8}, 
        		{8, 2}, {8, 4}, 
        		{9, 6}, {9, 9}, 
        		{10, 7}, 
        	})
        	{
        		this.setBlockState(world, biomeCobblestoneState, offset_xy[0], 0, offset_xy[1], structureBB);
        	}
        	
        	// Unkempt grass
        	for (int[] offset_xy : new int[][]{
        		{0, 3}, 
        		{1, 2}, {1, 6}, 
        		{3, 4}, {3, 10}, 
        		{4, 2}, 
        		{6, 7}, {6, 8}, 
        		{7, 0}, 
        		{8, 0}, {8, 5}, {8, 8},
        		{10, 0}, {10, 3}, 
        	})
        	{
        		this.setBlockState(world, Blocks.tallgrass.getDefaultState(), offset_xy[0], 1, offset_xy[1], structureBB);
        	}
        	this.setBlockState(world, Blocks.yellow_flower.getDefaultState(), 3, 1, 1, structureBB);
        	
        	
        	// Tree
        	for (int uvwm[] : new int[][]{
        		{4, 1, 4, 3}, // Corner
        		{5, 1, 4, 3},
        		{6, 1, 4, 1}, // Corner
        		{6, 1, 5, 1},
        		{6, 1, 6, 2}, // Corner
        		{5, 1, 6, 2},
        		{4, 1, 6, 0}, // Corner
        		{4, 1, 5, 0},
        	})
        	{
        		this.setBlockState(world, biomeStoneStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(biomeStoneStairsState.getBlock(), uvwm[3])), uvwm[0], uvwm[1], uvwm[2], structureBB);
        	}
        	

        	// Dirt block
        	world.setBlockState(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(0), this.getZWithOffset(5, 5)), Blocks.dirt.getStateFromMeta(0), 2);
        	
        	// Leaves placed into world
        	for (int u=3; u<=7; u++) {for (int v=5; v<=6; v++) {for (int w=4; w<=6; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.leaves.getStateFromMeta(0), 2);}}}
        	for (int u=4; u<=6; u++) {for (int v=5; v<=6; v++) {for (int w=3; w<=7; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.leaves.getStateFromMeta(0), 2);}}}
        	for (int u=4; u<=6; u++) {for (int v=7; v<=8; v++) {for (int w=4; w<=6; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.leaves.getStateFromMeta(0), 2);}}}

        	// Logs need to be set in world so as not to be replaced with sandstone
        	for (int v=1; v<=7; v++) {world.setBlockState(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(v), this.getZWithOffset(5, 5)), Blocks.log.getStateFromMeta(0), 2);}
        	
        	// Carve out chunks of leaves using air
            for (int[] uvw : new int[][]{
            	{4, 5, 3},
            	{4, 5, 7},
            	{6, 5, 7},
            	{5, 6, 7},
            	{4, 8, 4},
            	{6, 8, 4},
            	{6, 8, 6},
            	{4, 8, 6},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), Blocks.air.getDefaultState(), 2);
            }
            
            for (int[] uvwm : new int[][]{
            	{5, 3, 4, StructureVillageVN.getTorchRotationMeta(2, this.coordBaseMode.getHorizontalIndex())},
            	{4, 3, 5, StructureVillageVN.getTorchRotationMeta(3, this.coordBaseMode.getHorizontalIndex())},
            	{5, 3, 6, StructureVillageVN.getTorchRotationMeta(0, this.coordBaseMode.getHorizontalIndex())},
            	{6, 3, 5, StructureVillageVN.getTorchRotationMeta(1, this.coordBaseMode.getHorizontalIndex())},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
            }
                    	        	
            // Posts
        	this.fillWithBlocks(world, structureBB, 4, 1, 1, 4, 4, 1, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 1, 6, 4, 1, biomeFenceState, biomeFenceState, false);
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
            	}
            	
        		this.setBlockState(world, concreteBlockstate, 5, 4, 1, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomeCobblestoneState, 5, 4, 1, structureBB);
        	}
        	
        	this.fillWithBlocks(world, structureBB, 4, 1, 9, 4, 4, 9, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 9, 6, 4, 9, biomeFenceState, biomeFenceState, false);
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor2);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor2);
            	}
            	
        		this.setBlockState(world, concreteBlockstate, 5, 4, 9, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomeCobblestoneState, 5, 4, 9, structureBB);
        	}
        	
        	// Signs
            int signXBB = 5;
			int signYBB = 4;
			int signZBB = 0;
			int signZBB2 = 10;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signX2 = this.getXWithOffset(signXBB, signZBB2);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
            int signZ2 = this.getZWithOffset(signXBB, signZBB2);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(2, this.coordBaseMode.getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
			world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
            // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
    		TileEntitySign signContents2 = new TileEntitySign();
    		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
    		
			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode.getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX2, signY, signZ2), signContents2);
    		
    		
			// Banner    		
    		if (GeneralConfig.decorateVillageCenter)
    		{
                int bannerXBB = 7;
    			int bannerZBB = 8;
    			int bannerYBB = -1;
    			if (this.bannerY==-1)
    			{
    				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(bannerXBB, bannerZBB), 0, this.getZWithOffset(bannerXBB, bannerZBB))).getY()-this.boundingBox.minY;
    				bannerYBB = this.bannerY;
    			}
    			else {bannerYBB = this.bannerY;}
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a cobblestone foundation
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
    		}
    		
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{8, 1, 6, -1, 0},
        			{9, 1, 2, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.11
        			if (false && random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(ia[0], ia[2]), 0, this.getZWithOffset(ia[0], ia[2]))).getY();
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(entityvillager);
        		}
            }
            
            return true;
        }
    }
    
    
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
	protected static ArrayList<BlueprintData> getRandomPlainsDecorBlueprint(StartVN startVN, EnumFacing coordBaseMode, Random random, int townColor)
	{
		int decorCount = 1;
		return getPlainsDecorBlueprint(random.nextInt(decorCount), startVN, coordBaseMode, random, townColor);
	}
	protected static ArrayList<BlueprintData> getPlainsDecorBlueprint(int decorType, StartVN startVN, EnumFacing coordBaseMode, Random random, int townColor)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), startVN.materialType, startVN.biome);
    	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), startVN.materialType, startVN.biome);
    	
    	// For stripped wood specifically
    	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogState; 

    	// Try to see if stripped wood exists
    	if (biomeLogState.getBlock() == Blocks.log)
    	{
    		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWood(biomeLogState.getBlock().getMetaFromState(biomeLogState), 0);
    	}
    	else if (biomeLogState.getBlock() == Blocks.log2)
    	{
    		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWood(biomeLogState.getBlock().getMetaFromState(biomeLogState)+4, 0);
    	}
    	// If it doesn't exist, try stripped logs
    	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.log || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.log2)
    	{
        	if (biomeLogState.getBlock() == Blocks.log)
        	{
        		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLog(biomeLogState.getBlock().getMetaFromState(biomeLogState), 0);
        	}
        	else if (biomeLogState.getBlock() == Blocks.log2)
        	{
        		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLog(biomeLogState.getBlock().getMetaFromState(biomeLogState)+4, 0);
        	}
    	}
    	
		
        switch (decorType)
        {
    	case 0: // Plains Lamp 1
    		
    		BlueprintData.addFillWithBlocks(blueprint, 0, 0, 0, 0, 2, 0, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, 0, 3, 0, biomeStrippedWoodOrLogOrLogVerticState);
    		for (int[] lamp_uwm : new int[][]{
    			{0,-1,2},
    			{-1,0,3},
    			{0,1,0},
    			{1,1,1}
    			}) {
    			BlueprintData.addPlaceBlock(blueprint, lamp_uwm[0], 3, lamp_uwm[1], Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(lamp_uwm[2], coordBaseMode.getHorizontalIndex())));
    		}
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
