package astrotibs.villagenames.village.biomestructures;

import java.util.List;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class SnowyStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Ice Spire --- //
    
    public static class SnowyMeetingPoint1 extends StartVN
    {
	    public SnowyMeetingPoint1() {}
		
		public SnowyMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 11;
    		int depth = 7;
    		int height = 7;
    		
		    // Establish orientation
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            switch (this.getCoordBaseMode())
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
			if (GeneralConfig.debugMessages) {LogHelper.info("Village coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex());}
			// Northward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (new int[]{8,1,2,4})[this.getCoordBaseMode().getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (new int[]{4,8,1,2})[this.getCoordBaseMode().getHorizontalIndex()], EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (new int[]{2,1,8,4})[this.getCoordBaseMode().getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (new int[]{4,2,1,8})[this.getCoordBaseMode().getHorizontalIndex()], EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.LOG.getStateFromMeta(0), this);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STANDING_SIGN.getDefaultState(), this);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.COBBLESTONE.getDefaultState(), this);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), this);
        	IBlockState biomeLanternState = StructureVillageVN.getLanternBlock();
        	
        	// For stripped wood specifically // TODO - add modded stripped wood
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogState;//null; int biomeStrippedWoodOrLogOrLogVerticMeta = 0;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), false);
        	IBlockState biomeStrippedWoodOrLogOrLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), true);
        	
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true);
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(2, 5),
        			this.getYWithOffset(2),
        			this.getZWithOffset(2, 5));
        	int townColor = villageNBTtag.getInteger("townColor");
        	int townColor2 = villageNBTtag.getInteger("townColor2");
        	
        	// Top layer is grass path
        	for (int u=0; u<=11; u++) {for (int w=0; w<=7; w++) {
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, -1, w, structureBB); // Foundation
        		
        		StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(u, w), this.getYWithOffset(0), this.getZWithOffset(u, w)); // Path
        		this.clearCurrentPositionBlocksUpwards(world, u, 1, w, structureBB); // Clear above
        	}}
        	
        	// Set grass
        	this.fillWithBlocks(world, structureBB, 0, 0, 7, 1, 0, 7, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 0, 0, 0, 0, 0, 3, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 1, 0, 0, 1, 0, 2, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 2, 0, 0, 2, 0, 1, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 3, 0, 0, 7, 0, 0, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 5, 0, 7, 11, 0, 7, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 11, 0, 0, 11, 0, 3, biomeGrassState, biomeGrassState, false);
        	// Set dirt
        	this.fillWithBlocks(world, structureBB, 5, 0, 2, 8, 0, 5, biomeDirtState, biomeDirtState, false);
        	this.setBlockState(world, biomeDirtState, 2, 0, 5, structureBB);
        	
        	// Place the "stripped wood" rim around the "art"
        	this.fillWithBlocks(world, structureBB, 5, 1, 2, 7, 1, 2, biomeStrippedWoodOrLogOrLogHorAcrossState, biomeStrippedWoodOrLogOrLogHorAcrossState, false);
        	this.fillWithBlocks(world, structureBB, 5, 1, 3, 5, 1, 5, biomeStrippedWoodOrLogOrLogHorAlongState, biomeStrippedWoodOrLogOrLogHorAlongState, false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 5, 8, 1, 5, biomeStrippedWoodOrLogOrLogHorAcrossState, biomeStrippedWoodOrLogOrLogHorAcrossState, false);
        	this.fillWithBlocks(world, structureBB, 8, 1, 2, 8, 1, 4, biomeStrippedWoodOrLogOrLogHorAlongState, biomeStrippedWoodOrLogOrLogHorAlongState, false);
        	
        	// Set snow layer
        	this.setBlockState(world, Blocks.SNOW_LAYER.getStateFromMeta(0), 0, 1, 7, structureBB);
        	this.fillWithBlocks(world, structureBB, 0, 1, 1, 0, 1, 3, Blocks.SNOW_LAYER.getStateFromMeta(0), Blocks.SNOW_LAYER.getStateFromMeta(0), false);
        	this.fillWithBlocks(world, structureBB, 0, 1, 0, 5, 1, 0, Blocks.SNOW_LAYER.getStateFromMeta(0), Blocks.SNOW_LAYER.getStateFromMeta(0), false);
        	this.setBlockState(world, Blocks.SNOW_LAYER.getStateFromMeta(0), 8, 1, 7, structureBB);
        	this.fillWithBlocks(world, structureBB, 10, 1, 7, 11, 1, 7, Blocks.SNOW_LAYER.getStateFromMeta(0), Blocks.SNOW_LAYER.getStateFromMeta(0), false);
        	this.fillWithBlocks(world, structureBB, 11, 1, 0, 11, 1, 3, Blocks.SNOW_LAYER.getStateFromMeta(0), Blocks.SNOW_LAYER.getStateFromMeta(0), false);
        	this.fillWithBlocks(world, structureBB, 5, 2, 2, 8, 2, 5, Blocks.SNOW_LAYER.getStateFromMeta(0), Blocks.SNOW_LAYER.getStateFromMeta(0), false);
        	
        	// Ice spire
        	this.fillWithBlocks(world, structureBB, 6, 1, 3, 7, 2, 4, Blocks.PACKED_ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 6, 3, 4, 6, 7, 4, Blocks.PACKED_ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 7, 3, 3, 7, 5, 3, Blocks.PACKED_ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), false);
        	
        	
        	// Place the sign base. Concrete if requested/allowed, ice otherwise
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
            	}
            	
            	this.setBlockState(world, concreteBlockstate, 2, 1, 5, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, Blocks.PACKED_ICE.getDefaultState(), 2, 1, 5, structureBB);
        	}
        	
        	
        	// Sign
            int signXBB = 2;
			int signYBB = 2;
			int signZBB = 5;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
                int bannerXBB = 10;
    			int bannerZBB = 2;
    			int bannerYBB = 1;
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a grass foundation
                this.setBlockState(world, biomeGrassState, bannerXBB, bannerYBB-1, bannerZBB, structureBB);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
    		}
    		
    		
            // Decor
            int[][] decorUVW = new int[][]{
            	{1, 1, 1},
            	{9, 1, 7},
            };  
            
            for (int j=0; j<decorUVW.length; j++)
            {
            	// Get coordinates
            	int[] uvw = decorUVW[j];
            	
            	// Set random seed
            	Random randomFromXYZ = new Random();
            	randomFromXYZ.setSeed(
    					world.getSeed() +
    					FunctionsVN.getUniqueLongForXYZ(
    							this.getXWithOffset(uvw[0], uvw[2]),
    							this.getYWithOffset(uvw[1]),
    							this.getXWithOffset(uvw[0], uvw[2])
    							)
            			);
            	
            	int decorHeightY = uvw[1];
            	
            	// Get ground level
            	if (this.decorHeightY.size()<(j+1))
            	{
            		// There are fewer stored ground levels than this decor number, so this is being generated for the first time.
            		// Add new ground level
            		//decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uvw[0], uvw[2]), this.getZWithOffset(uvw[0], uvw[2]))-this.boundingBox.minY;
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	int decorType = randomFromXYZ.nextInt(3);
            	int decorOrientation = randomFromXYZ.nextInt(4);
            	
            	boolean genericBoolean=false;
            	
            	int lanternX; int lanternY; int lanternZ;
            	
            	// Make lantern base
            	switch (decorType)
            	{
            	case 2: // Lateral lanterns
            		lanternX =  decorOrientation==3 ? -1 : decorOrientation==1 ? 1 : 0;
            		lanternZ =  decorOrientation==0 ? -1 : decorOrientation==2 ? 1 : 0;
            		this.setBlockState(world, biomeFenceState, uvw[0]+lanternX, decorHeightY+3, uvw[2]+lanternZ, structureBB);
            		this.setBlockState(world, biomeLanternState, uvw[0]+lanternX, decorHeightY+2, uvw[2]+lanternZ, structureBB);
            		lanternX =  decorOrientation==3 ? 1 : decorOrientation==1 ? -1 : 0;
            		lanternZ =  decorOrientation==0 ? 1 : decorOrientation==2 ? -1 : 0;
            		this.setBlockState(world, biomeFenceState, uvw[0]+lanternX, decorHeightY+3, uvw[2]+lanternZ, structureBB);
            		this.setBlockState(world, biomeLanternState, uvw[0]+lanternX, decorHeightY+2, uvw[2]+lanternZ, structureBB);
            	case 1: // Second lantern opposite
            		lanternX =  decorOrientation==0 ? -1 : decorOrientation==2 ? 1 : 0;
            		lanternZ =  decorOrientation==3 ? -1 : decorOrientation==1 ? 1 : 0;
            		this.setBlockState(world, biomeFenceState, uvw[0]+lanternX, decorHeightY+3, uvw[2]+lanternZ, structureBB);
            		this.setBlockState(world, biomeLanternState, uvw[0]+lanternX, decorHeightY+2, uvw[2]+lanternZ, structureBB);
            	case 0: // Single lantern
            		lanternX =  decorOrientation==0 ? 1 : decorOrientation==2 ? -1 : 0;
            		lanternZ =  decorOrientation==3 ? 1 : decorOrientation==1 ? -1 : 0;
            		this.setBlockState(world, biomeFenceState, uvw[0]+lanternX, decorHeightY+3, uvw[2]+lanternZ, structureBB);
            		this.setBlockState(world, biomeLanternState, uvw[0]+lanternX, decorHeightY+2, uvw[2]+lanternZ, structureBB);
            		// Base post
            		this.fillWithBlocks(world, structureBB, uvw[0]+0, decorHeightY+0, uvw[2]+0, uvw[0]+0, decorHeightY+3, uvw[2]+0, biomeFenceState, biomeFenceState, false);
            	}
            }
        	
        	
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{3, 1, 3, -1, 0},
        			{10, 1, 4, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.9+
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
    
    
    
	// --- Frozen Fountain --- //
    
    public static class SnowyMeetingPoint2 extends StartVN
    {
	    public SnowyMeetingPoint2() {}
		
		public SnowyMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 10;
    		int depth = 8;
    		int height = 4;
    		
		    // Establish orientation
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            switch (this.getCoordBaseMode())
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
			if (GeneralConfig.debugMessages) {LogHelper.info("Village coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex());}
			// Northward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()%2==0?4:3), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()%2==0?3:4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()%2==0?4:3), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()%2==0?3:4), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STANDING_SIGN.getDefaultState(), this);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlock(Blocks.PLANKS.getDefaultState(), this);
        	IBlockState biomeWoodenStairsState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_STAIRS.getDefaultState(), this);
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true);
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(9, 4),
        			this.getYWithOffset(1),
        			this.getZWithOffset(9, 4));
        	int townColor = villageNBTtag.getInteger("townColor");
        	int townColor2 = villageNBTtag.getInteger("townColor2");
        	
        	// Top layer is grass path
        	for (int u=1; u<=9; u++) {for (int w=1; w<=7; w++) {
        		
        		if (!(u==1&&w==1) && !(u==1&&w==7) && !(u==9&&w==7) && !(u==9&&w==1)) // Not in the corners
        		{
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, -1, w, structureBB); // Foundation
            		
            		StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(u, w), this.getYWithOffset(0), this.getZWithOffset(u, w)); // Path
            		this.clearCurrentPositionBlocksUpwards(world, u, 1, w, structureBB); // Clear above
        		}
        	}}
        	
        	// Set grass
        	this.setBlockState(world, biomeGrassState, 8, 0, 0, structureBB);
        	this.setBlockState(world, biomeGrassState, 10, 0, 2, structureBB);
        	this.fillWithBlocks(world, structureBB, 4, 0, 2, 6, 0, 6, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 3, 0, 3, 3, 0, 5, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 7, 0, 3, 7, 0, 5, biomeGrassState, biomeGrassState, false);
        	// Set dirt
        	this.fillWithBlocks(world, structureBB, 5, 0, 2, 8, 0, 5, biomeDirtState, biomeDirtState, false);
        	this.setBlockState(world, biomeDirtState, 2, 0, 5, structureBB);
        	this.fillWithBlocks(world, structureBB, 5, 0, 3, 5, 0, 5, biomeDirtState, biomeDirtState, false);
        	this.setBlockState(world, biomeDirtState, 4, 0, 4, structureBB);
        	this.setBlockState(world, biomeDirtState, 6, 0, 4, structureBB);
        	// Stone brick for some reason
        	this.setBlockState(world, Blocks.STONEBRICK.getStateFromMeta(0), 6, 0, 3, structureBB);
        	
        	// Ice fountain
        	
        	// Ice
        	this.fillWithBlocks(world, structureBB, 5, 1, 3, 5, 2, 5, Blocks.PACKED_ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, Blocks.PACKED_ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 4, 6, 2, 4, Blocks.PACKED_ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), false);
        	this.setBlockState(world, Blocks.PACKED_ICE.getDefaultState(), 5, 3, 4, structureBB);
        	// Torch
        	world.setBlockState(new BlockPos(this.getXWithOffset(5, 4), this.getYWithOffset(4), this.getZWithOffset(5, 4)), Blocks.TORCH.getStateFromMeta(0), 2);
        	
        	// Rim
        	for (int uvwm[] : new int[][]{
        		{6,1,2,3},
        		{5,1,2,3}, // Toward player
        		{4,1,2,0},
        		{4,1,3,3},
        		{3,1,3,0},
        		{3,1,4,0}, // Left side, left-facing
        		{3,1,5,2},
        		{4,1,5,0},
        		{4,1,6,2},
        		{5,1,6,2}, // Away from player
        		{6,1,6,1},
        		{6,1,5,2},
        		{7,1,5,1},
        		{7,1,4,1}, // Right side, right-facing
        		{7,1,3,3},
        		{6,1,3,1},
        	})
        	{
        		this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB);
        	}
        	
            // Add path nodules at the end
            for (int i=0; i<3; i++)
            {
        		for (int[] uw: new int[][]{
        			{i+4, 0},
        			{i+4, 8},
        			{0, i+3},
        			{10, i+3},
        		})
        		{
        			int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uw[0], uw[1]), 0, this.getZWithOffset(uw[0], uw[1]))).down().getY();
            		
                    if (k > -1)
                    {
                    	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(uw[0], uw[1]), k, this.getZWithOffset(uw[0], uw[1]));
                    	this.clearCurrentPositionBlocksUpwards(world, uw[0], k+1-this.boundingBox.minY, uw[1], structureBB);
                   	}
        		}
            }
        	
        	
        	// Place the sign base. Concrete if requested/allowed, ice otherwise
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);
        		// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
            	}
            	
            	this.setBlockState(world, concreteBlockstate, 9, 0, 4, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomePlankState, 9, 0, 4, structureBB);
        	}
        	
        	// Sign
            int signXBB = 9;
			int signYBB = 1;
			int signZBB = 4;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), Blocks.STANDING_SIGN.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
                int bannerXBB = 8;
    			int bannerZBB = 7;
    			int bannerYBB = 1;
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a plank foundation
                this.setBlockState(world, biomePlankState, bannerXBB, bannerYBB-1, bannerZBB, structureBB);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
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
        			{7, 1, 1, -1, 0},
        			{9, 1, 2, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.9+
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
    
    
	// --- Snowy Pavilion --- //
    
    public static class SnowyMeetingPoint3 extends StartVN
    {
	    public SnowyMeetingPoint3() {}
		
		public SnowyMeetingPoint3(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 6;
    		int depth = 6;
    		int height = 5;
    		
		    // Establish orientation
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            switch (this.getCoordBaseMode())
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
			if (GeneralConfig.debugMessages) {LogHelper.info("Village coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex());}
			// Northward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 2, EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 2, EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.LOG.getStateFromMeta(0), this);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.WALL_SIGN.getDefaultState(), this);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), this);
        	IBlockState biomeLanternState = StructureVillageVN.getLanternBlock();
        	
        	// For stripped wood specifically // TODO - add modded stripped wood
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogState;//null; int biomeStrippedWoodOrLogOrLogVerticMeta = 0;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), false);
        	IBlockState biomeStrippedWoodOrLogOrLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), true);
        	
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true);
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(3, 3),
        			this.getYWithOffset(3),
        			this.getZWithOffset(3, 3));
        	int townColor = villageNBTtag.getInteger("townColor");
        	int townColor2 = villageNBTtag.getInteger("townColor2");
        	
        	// Top layer is grass path
        	for (int u=1; u<=5; u++) {for (int w=1; w<=5; w++) {
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, -1, w, structureBB); // Foundation
        		
        		StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(u, w), this.getYWithOffset(0), this.getZWithOffset(u, w)); // Path
        		this.clearCurrentPositionBlocksUpwards(world, u, 1, w, structureBB); // Clear above
        	}}
        	
        	// Set grass
        	this.setBlockState(world, biomeGrassState, 2, 0, 2, structureBB);
        	this.setBlockState(world, biomeGrassState, 2, 0, 4, structureBB);
        	this.setBlockState(world, biomeGrassState, 4, 0, 4, structureBB);
        	this.setBlockState(world, biomeGrassState, 4, 0, 2, structureBB);
        	// Set these grass blocks into ground level
    		for (int[] uw: new int[][]{
    			{0, 6},
    			{5, 0},
    			{6, 1},
    		})
    		{
    			int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uw[0], uw[1]), 0, this.getZWithOffset(uw[0], uw[1]))).down().getY();
        		
                if (k > -1) {this.setBlockState(world, biomeGrassState, uw[0], k+1-this.boundingBox.minY, uw[1], structureBB);}
    		}
        	
            // Add path nodules at the end
            for (int i=0; i<3; i++)
            {
        		for (int[] uw: new int[][]{
        			{i+2, 0},
        			{i+2, 6},
        			{0, i+2},
        			{6, i+2},
        		})
        		{
        			int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uw[0], uw[1]), 0, this.getZWithOffset(uw[0], uw[1]))).down().getY();
            		
                    if (k > -1)
                    {
                    	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(uw[0], uw[1]), k, this.getZWithOffset(uw[0], uw[1]));
                    	this.clearCurrentPositionBlocksUpwards(world, uw[0], k+1-this.boundingBox.minY, uw[1], structureBB);
                   	}
        		}
            }
        	
            // Pavilion
            
            // Posts
            this.fillWithBlocks(world, structureBB, 2, 1, 2, 2, 3, 2, biomeFenceState, biomeFenceState, false);
            this.fillWithBlocks(world, structureBB, 2, 1, 4, 2, 3, 4, biomeFenceState, biomeFenceState, false);
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 3, 4, biomeFenceState, biomeFenceState, false);
            this.fillWithBlocks(world, structureBB, 4, 1, 2, 4, 3, 2, biomeFenceState, biomeFenceState, false);
            
        	// Place the "stripped wood" roof
        	this.fillWithBlocks(world, structureBB, 2, 4, 2, 3, 4, 2, biomeStrippedWoodOrLogOrLogHorAcrossState, biomeStrippedWoodOrLogOrLogHorAcrossState, false);
        	this.fillWithBlocks(world, structureBB, 2, 4, 3, 2, 4, 4, biomeStrippedWoodOrLogOrLogHorAlongState, biomeStrippedWoodOrLogOrLogHorAlongState, false);
        	this.fillWithBlocks(world, structureBB, 3, 4, 4, 4, 4, 4, biomeStrippedWoodOrLogOrLogHorAcrossState, biomeStrippedWoodOrLogOrLogHorAcrossState, false);
        	this.fillWithBlocks(world, structureBB, 4, 4, 2, 4, 4, 3, biomeStrippedWoodOrLogOrLogHorAlongState, biomeStrippedWoodOrLogOrLogHorAlongState, false);
        	
        	// Add torches
        	this.setBlockState(world, biomeFenceState, 1, 4, 3, structureBB);
        	this.setBlockState(world, biomeLanternState, 1, 3, 3, structureBB);
        	this.setBlockState(world, biomeFenceState, 5, 4, 3, structureBB);
        	this.setBlockState(world, biomeLanternState, 5, 3, 3, structureBB);
        	this.setBlockState(world, biomeFenceState, 3, 4, 1, structureBB);
        	this.setBlockState(world, biomeLanternState, 3, 3, 1, structureBB);
        	this.setBlockState(world, biomeFenceState, 3, 4, 5, structureBB);
        	this.setBlockState(world, biomeLanternState, 3, 3, 5, structureBB);
        	
        	
        	// Place the sign base. Concrete if requested/allowed
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);

            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 3, 3, 3, 3, 5, 3, concreteBlockstate, concreteBlockstate, false);
        	}
        	else
        	{
        		this.fillWithBlocks(world, structureBB, 3, 3, 3, 3, 5, 3, biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);
        	}
        	
        	
        	
        	// Sign
            int signXBB = 2;
			int signYBB = 3;
			int signZBB = 3;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(3, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
            int signXBB2 = 4;
            int signX2 = this.getXWithOffset(signXBB2, signZBB);
            int signZ2 = this.getZWithOffset(signXBB2, signZBB);
            
            // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
    		TileEntitySign signContents2 = new TileEntitySign();
    		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
            
			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(1, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX2, signY, signZ2), signContents2);
    		
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
                int bannerXBB = 5;
    			int bannerZBB = 0;
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
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
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
        			{5, 1, 1, -1, 0},
        			{5, 1, 5, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.9+
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
}
