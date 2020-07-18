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

public class SnowyStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Ice Spire --- //
    
    public static class SnowyMeetingPoint1 extends StartVN
    {
	    public SnowyMeetingPoint1() {}
		
		public SnowyMeetingPoint1(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 11;
    		int depth = 7;
    		int height = 7;
    		
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
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (new int[]{8,1,2,4})[this.coordBaseMode.getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (new int[]{4,8,1,2})[this.coordBaseMode.getHorizontalIndex()], EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (new int[]{2,1,8,4})[this.coordBaseMode.getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (new int[]{4,2,1,8})[this.coordBaseMode.getHorizontalIndex()], EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeLanternState = ModObjects.chooseModLanternBlockState(true);
        	
        	// For stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogState;//null; int biomeStrippedWoodOrLogOrLogVerticMeta = 0;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAlongState = biomeLogState;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAcrossState = biomeLogState;
        	// If we're dealing with actual logs, set rotations
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.log || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.log2)
        	{
        		biomeStrippedWoodOrLogOrLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.coordBaseMode.getHorizontalIndex(), false);
            	biomeStrippedWoodOrLogOrLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.coordBaseMode.getHorizontalIndex(), true);
        	}
        	
        	
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
        			this.getXWithOffset(2, 5),
        			this.getYWithOffset(2),
        			this.getZWithOffset(2, 5));
        	this.townColor = villageNBTtag.getInteger("townColor");
        	this.townColor2 = villageNBTtag.getInteger("townColor2");
        	
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
        	this.setBlockState(world, Blocks.snow_layer.getStateFromMeta(0), 0, 1, 7, structureBB);
        	this.fillWithBlocks(world, structureBB, 0, 1, 1, 0, 1, 3, Blocks.snow_layer.getStateFromMeta(0), Blocks.snow_layer.getStateFromMeta(0), false);
        	this.fillWithBlocks(world, structureBB, 0, 1, 0, 5, 1, 0, Blocks.snow_layer.getStateFromMeta(0), Blocks.snow_layer.getStateFromMeta(0), false);
        	this.setBlockState(world, Blocks.snow_layer.getStateFromMeta(0), 8, 1, 7, structureBB);
        	this.fillWithBlocks(world, structureBB, 10, 1, 7, 11, 1, 7, Blocks.snow_layer.getStateFromMeta(0), Blocks.snow_layer.getStateFromMeta(0), false);
        	this.fillWithBlocks(world, structureBB, 11, 1, 0, 11, 1, 3, Blocks.snow_layer.getStateFromMeta(0), Blocks.snow_layer.getStateFromMeta(0), false);
        	this.fillWithBlocks(world, structureBB, 5, 2, 2, 8, 2, 5, Blocks.snow_layer.getStateFromMeta(0), Blocks.snow_layer.getStateFromMeta(0), false);
        	
        	// Ice spire
        	this.fillWithBlocks(world, structureBB, 6, 1, 3, 7, 2, 4, Blocks.packed_ice.getDefaultState(), Blocks.packed_ice.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 6, 3, 4, 6, 7, 4, Blocks.packed_ice.getDefaultState(), Blocks.packed_ice.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 7, 3, 3, 7, 5, 3, Blocks.packed_ice.getDefaultState(), Blocks.packed_ice.getDefaultState(), false);
        	
        	
        	// Place the sign base. Concrete if requested/allowed, ice otherwise
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
            	}
            	
            	this.setBlockState(world, concreteBlockstate, 2, 1, 5, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, Blocks.packed_ice.getDefaultState(), 2, 1, 5, structureBB);
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
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
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
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)), 2);
				
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
    							this.getZWithOffset(uvw[0], uvw[2])
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
            	
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getRandomSnowyDecorBlueprint(this, this.coordBaseMode, randomFromXYZ);
            	
            	for (BlueprintData b : decorBlueprint)
            	{
            		// Place block indicated by blueprint
            		this.setBlockState(world, b.getBlockState(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos(), uvw[2]+b.getWPos(), structureBB);
            		
            		// Fill below if flagged
            		if ((b.getfillFlag()&1)!=0)
            		{
            			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos()-1, uvw[2]+b.getWPos(), structureBB);
            		}
            		
            		// Clear above if flagged
            		if ((b.getfillFlag()&2)!=0)
            		{
            			this.clearCurrentPositionBlocksUpwards(world, uvw[0]+b.getUPos(), decorHeightY+b.getVPos()+1, uvw[2]+b.getWPos(), structureBB);
            		}            		
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
    
    
    
	// --- Frozen Fountain --- //
    
    public static class SnowyMeetingPoint2 extends StartVN
    {
	    public SnowyMeetingPoint2() {}
		
		public SnowyMeetingPoint2(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 10;
    		int depth = 8;
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
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()%2==0?4:3), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()%2==0?3:4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()%2==0?4:3), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()%2==0?3:4), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeWoodenStairsState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(9, 4),
        			this.getYWithOffset(1),
        			this.getZWithOffset(9, 4));
        	this.townColor = villageNBTtag.getInteger("townColor");
        	this.townColor2 = villageNBTtag.getInteger("townColor2");
        	
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
        	this.setBlockState(world, Blocks.stonebrick.getStateFromMeta(0), 6, 0, 3, structureBB);
        	
        	// Ice fountain
        	
        	// Ice
        	this.fillWithBlocks(world, structureBB, 5, 1, 3, 5, 2, 5, Blocks.packed_ice.getDefaultState(), Blocks.packed_ice.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, Blocks.packed_ice.getDefaultState(), Blocks.packed_ice.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 4, 6, 2, 4, Blocks.packed_ice.getDefaultState(), Blocks.packed_ice.getDefaultState(), false);
        	this.setBlockState(world, Blocks.packed_ice.getDefaultState(), 5, 3, 4, structureBB);
        	// Torch
        	world.setBlockState(new BlockPos(this.getXWithOffset(5, 4), this.getYWithOffset(4), this.getZWithOffset(5, 4)), Blocks.torch.getStateFromMeta(0), 2);
        	
        	// Rim
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{2,1,3,0})[this.coordBaseMode.getHorizontalIndex()]), 6, 1, 2, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{2,1,3,0})[this.coordBaseMode.getHorizontalIndex()]), 5, 1, 2, structureBB); // Front-center
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?0:2),              4, 1, 2, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{2,1,3,0})[this.coordBaseMode.getHorizontalIndex()]), 4, 1, 3, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?0:2),              3, 1, 3, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?0:2),              3, 1, 4, structureBB); // Left-center
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{3,0,2,1})[this.coordBaseMode.getHorizontalIndex()]), 3, 1, 5, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?0:2),              4, 1, 5, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{3,0,2,1})[this.coordBaseMode.getHorizontalIndex()]), 4, 1, 6, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{3,0,2,1})[this.coordBaseMode.getHorizontalIndex()]), 5, 1, 6, structureBB); // Back-center
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?1:3),              6, 1, 6, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{3,0,2,1})[this.coordBaseMode.getHorizontalIndex()]), 6, 1, 5, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?1:3),              7, 1, 5, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?1:3),              7, 1, 4, structureBB); // Right-center
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{2,1,3,0})[this.coordBaseMode.getHorizontalIndex()]), 7, 1, 3, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?1:3),              6, 1, 3, structureBB);
        	
        	
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
                    	this.clearCurrentPositionBlocksUpwards(world, uw[0], k+2-this.boundingBox.minY, uw[1], structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(uw[0], uw[1]), k, this.getZWithOffset(uw[0], uw[1]));
                   	}
        		}
            }
        	
        	
        	// Place the sign base. Concrete if requested/allowed, ice otherwise
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
        		// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
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
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), Blocks.standing_sign.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
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
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)), 2);
				
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
    
    
	// --- Snowy Pavilion --- //
    
    public static class SnowyMeetingPoint3 extends StartVN
    {
	    public SnowyMeetingPoint3() {}
		
		public SnowyMeetingPoint3(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 6;
    		int depth = 6;
    		int height = 5;
    		
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
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.wall_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeLanternState = ModObjects.chooseModLanternBlockState(true);
        	
        	// For stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogState;//null; int biomeStrippedWoodOrLogOrLogVerticMeta = 0;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAlongState = biomeLogState;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAcrossState = biomeLogState;
        	// If we're dealing with actual logs, set rotations
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.log || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.log2)
        	{
        		biomeStrippedWoodOrLogOrLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.coordBaseMode.getHorizontalIndex(), false);
            	biomeStrippedWoodOrLogOrLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.coordBaseMode.getHorizontalIndex(), true);
        	}
        	
        	
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
        			this.getXWithOffset(3, 3),
        			this.getYWithOffset(3),
        			this.getZWithOffset(3, 3));
        	this.townColor = villageNBTtag.getInteger("townColor");
        	this.townColor2 = villageNBTtag.getInteger("townColor2");
        	
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
                    	this.clearCurrentPositionBlocksUpwards(world, uw[0], k+2-this.boundingBox.minY, uw[1], structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(uw[0], uw[1]), k, this.getZWithOffset(uw[0], uw[1]));
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
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);

            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
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
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(3, this.coordBaseMode.getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
            int signXBB2 = 4;
            int signX2 = this.getXWithOffset(signXBB2, signZBB);
            int signZ2 = this.getZWithOffset(signXBB2, signZBB);
            
            // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
    		TileEntitySign signContents2 = new TileEntitySign();
    		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
            
			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(1, this.coordBaseMode.getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX2, signY, signZ2), signContents2);
    		
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
                int bannerXBB = 5;
    			int bannerZBB = 0;
    			int bannerYBB = -1;
    			if (this.bannerY==0)
    			{
    				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(bannerXBB, bannerZBB), 0, this.getZWithOffset(bannerXBB, bannerZBB))).getY()-this.boundingBox.minY +1;
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
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)), 2);
				
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
	protected static ArrayList<BlueprintData> getRandomSnowyDecorBlueprint(StartVN startVN, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 3;
		return getSnowyDecorBlueprint(random.nextInt(decorCount), startVN, coordBaseMode, random);
	}
	protected static ArrayList<BlueprintData> getSnowyDecorBlueprint(int decorType, StartVN startVN, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), startVN.materialType, startVN.biome);
    	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
    	
    	boolean genericBoolean=false;
    	
    	int lanternX; int lanternY; int lanternZ;
    	
    	int decorOrientation = random.nextInt(4);
    	
    	// Make lantern base
    	switch (decorType)
    	{
    	case 2: // Lateral lanterns
    		lanternX =  decorOrientation==3 ? -1 : decorOrientation==1 ? 1 : 0;
    		lanternZ =  decorOrientation==0 ? -1 : decorOrientation==2 ? 1 : 0;
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 3, lanternZ, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 2, lanternZ, biomeHangingLanternState);
    		
    		lanternX =  decorOrientation==3 ? 1 : decorOrientation==1 ? -1 : 0;
    		lanternZ =  decorOrientation==0 ? 1 : decorOrientation==2 ? -1 : 0;
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 3, lanternZ, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 2, lanternZ, biomeHangingLanternState);
    		
    	case 1: // Second lantern opposite
    		lanternX =  decorOrientation==0 ? -1 : decorOrientation==2 ? 1 : 0;
    		lanternZ =  decorOrientation==3 ? -1 : decorOrientation==1 ? 1 : 0;
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 3, lanternZ, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 2, lanternZ, biomeHangingLanternState);
    		
    	case 0: // Single lantern
    		lanternX =  decorOrientation==0 ? 1 : decorOrientation==2 ? -1 : 0;
    		lanternZ =  decorOrientation==3 ? 1 : decorOrientation==1 ? -1 : 0;
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 3, lanternZ, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 2, lanternZ, biomeHangingLanternState);
    		
    		// Base post
    		BlueprintData.addFillWithBlocks(blueprint, 0, 0, 0, 0, 3, 0, biomeFenceState);
    	}
    	
        
        // Return the decor blueprint
        return blueprint;
	}
}
