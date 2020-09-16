package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class SavannaStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Savanna Market --- //
    
    public static class SavannaMeetingPoint1 extends StartVN
    {
	    public SavannaMeetingPoint1() {}
		
		public SavannaMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 13;
    		int depth = 11;
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
    		if (GeneralConfig.debugMessages)
    		{
    			LogHelper.info(
    					this.materialType + " " +  this.villageType + " village generated in "
    					+ this.worldChunkMngr.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			if (this.getCoordBaseMode().getHorizontalIndex()%2!=1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 5 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 4 : 0), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==1 || this.getCoordBaseMode().getHorizontalIndex()==2 ? 5 : 4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==1 || this.getCoordBaseMode().getHorizontalIndex()==2 ? 5 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			if (this.getCoordBaseMode().getHorizontalIndex()%2!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==1 ? 4 : 5), EnumFacing.WEST, this.getComponentType());}
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeWoodenSlabState = StructureVillageVN.getBiomeSpecificBlock(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeWoodenStairsState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_STAIRS.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(6, 7),
        			this.getYWithOffset(1),
        			this.getZWithOffset(6, 7));
        	
        	// Load the values of interest into memory
        	if (this.townColor==-1) {this.townColor = villageNBTtag.getInteger("townColor");}
        	if (this.townColor2==-1) {this.townColor2 = villageNBTtag.getInteger("townColor2");}
        	if (this.townColor3==-1) {this.townColor3 = villageNBTtag.getInteger("townColor3");}
        	if (this.townColor4==-1) {this.townColor4 = villageNBTtag.getInteger("townColor4");}
        	if (this.townColor5==-1) {this.townColor5 = villageNBTtag.getInteger("townColor5");}
        	if (this.townColor6==-1) {this.townColor6 = villageNBTtag.getInteger("townColor6");}
        	if (this.townColor7==-1) {this.townColor7 = villageNBTtag.getInteger("townColor7");}
        	if (this.namePrefix.equals("")) {this.namePrefix = villageNBTtag.getString("namePrefix");}
        	if (this.nameRoot.equals("")) {this.nameRoot = villageNBTtag.getString("nameRoot");}
        	if (this.nameSuffix.equals("")) {this.nameSuffix = villageNBTtag.getString("nameSuffix");}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	// Top layer is grass
        	this.fillWithBlocks(world, structureBB, 0, 0, 0, 13, 0, 11, biomeGrassState, biomeGrassState, false);
        	
        	// Clear above
        	for (int u=0; u<=13; u++) {for (int w=0; w<=11; w++) {
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, -1, w, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, u, 1, w, structureBB);
            }}
        	
        	// Set grass paths
        	for (int[] grass_uw : new int[][]{
        		{1, 1}, {1, 2}, {1, 3}, 
        		{2, 2}, {2, 7}, {2, 9}, 
        		{3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 5}, {3, 6}, {3, 8}, {3, 10}, 
        		{4, 2}, {4, 3}, {4, 5}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {4, 10}, {4, 11},
        		{5, 0}, {5, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 6}, {5, 7}, {5, 8}, {5, 10}, {5, 11}, 
        		{6, 0}, {6, 2}, {6, 4}, {6, 5}, {6, 7}, {6, 9}, {6, 10},
        		{7, 0}, {7, 1}, {7, 3}, {7, 7}, {7, 7}, {7, 9}, {7, 11}, 
        		{8, 2}, {8, 4}, {8, 5}, {8, 7}, {8, 8}, {8, 9}, {8, 10}, {8, 11}, 
        		{9, 0}, {9, 4}, {9, 5}, {9, 6}, {9, 7}, 
        		{10, 0}, {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {10, 10}, 
        		{11, 0}, {11, 3}, {11, 4}, {11, 6}, {11, 7}, {11, 8}, {11, 10}, {11, 11}, 
        		{12, 0}, {12, 2}, {12, 4}, {12, 5}, {12, 6}, {12, 7}, {12, 11}, 
        		{13, 0}, {13, 2}, {13, 3}, {13, 4}, {13, 5}, {13, 6}, {13, 8}, {13, 10},
        	})
        	{
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(grass_uw[0], grass_uw[1]), this.getYWithOffset(0), this.getZWithOffset(grass_uw[0], grass_uw[1]));
        	}
        	
        	// Set unkempt grass
        	for (int[] grass_uw : new int[][]{
        		{2, 0}, {2, 1},
        		{3, 9},
        		{4, 0}, {4, 1}, {4, 4}, 
        		{5, 5}, 
        		{6, 6}, {6, 8}, 
        		{7, 0}, {7, 8}, {7, 10},
        		{8, 1}, {8, 3}, {8, 6}, 
        		{9, 2}, {9, 9}, {9, 11}, 
        		{10, 11}, 
        		{11, 1}, {11, 5}, 
        		{12, 9}, 
        		{13, 1}, {13, 7}, {13, 9}, 
        	})
        	{
        		this.setBlockState(world, Blocks.TALLGRASS.getDefaultState(), grass_uw[0], 1, grass_uw[1], structureBB);
        	}
        	// Tall Grass
        	this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), 0, 1, 5, structureBB);
        	this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(8), 0, 2, 5, structureBB);
        	
        	// Stalls
        	
        	// Posts
        	this.fillWithBlocks(world, structureBB, 1, 1, 4, 1, 3, 4, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 4, 3, 3, 4, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 7, 3, 3, 7, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 1, 1, 7, 1, 3, 7, biomeFenceState, biomeFenceState, false);
        	// Awning
        	this.fillWithBlocks(world, structureBB, 1, 4, 4, 3, 4, 7, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 5, 3, 4, 5, biomeWoodenStairsState.getBlock().getStateFromMeta(3), biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{2,1,3,0})[this.getCoordBaseMode().getHorizontalIndex()]), false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 6, 3, 4, 6, biomeWoodenStairsState.getBlock().getStateFromMeta(2), biomeWoodenStairsState.getBlock().getStateFromMeta((new int[]{3,0,2,1})[this.getCoordBaseMode().getHorizontalIndex()]), false);
        	// Logs
        	this.fillWithBlocks(world, structureBB, 2, 1, 5, 2, 1, 6, biomeLogState, biomeLogState, false);
        	
        	this.fillWithBlocks(world, structureBB, 9, 1, 1, 9, 3, 1, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 9, 1, 3, 9, 3, 3, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 12, 1, 3, 12, 3, 3, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 12, 1, 1, 12, 3, 1, biomeFenceState, biomeFenceState, false);
        	// Awning
        	this.fillWithBlocks(world, structureBB, 9, 4, 1, 12, 4, 3, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 10, 4, 1, 10, 4, 3, biomeWoodenStairsState.getBlock().getStateFromMeta(0), biomeWoodenStairsState.getBlock().getStateFromMeta(this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 0 : 2), false);
        	this.fillWithBlocks(world, structureBB, 11, 4, 1, 11, 4, 3, biomeWoodenStairsState.getBlock().getStateFromMeta(1), biomeWoodenStairsState.getBlock().getStateFromMeta(this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 1 : 3), false);
        	// Logs
        	this.fillWithBlocks(world, structureBB, 10, 1, 2, 11, 1, 2, biomeLogState, biomeLogState, false);
        	
        	this.fillWithBlocks(world, structureBB, 9, 1, 8, 9, 3, 8, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 9, 1, 10, 9, 3, 10, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 12, 1, 10, 12, 3, 10, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 12, 1, 8, 12, 3, 8, biomeFenceState, biomeFenceState, false);        	
        	// Awning
        	this.fillWithBlocks(world, structureBB, 9, 4, 8, 12, 4, 10, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 10, 4, 8, 10, 4, 10, biomeWoodenStairsState.getBlock().getStateFromMeta(0), biomeWoodenStairsState.getBlock().getStateFromMeta(this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 0 : 2), false);
        	this.fillWithBlocks(world, structureBB, 11, 4, 8, 11, 4, 10, biomeWoodenStairsState.getBlock().getStateFromMeta(1), biomeWoodenStairsState.getBlock().getStateFromMeta(this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 1 : 3), false);
        	// Logs
        	this.fillWithBlocks(world, structureBB, 10, 1, 9, 11, 1, 9, biomeLogState, biomeLogState, false);
        	
        	// Fences with torches
        	this.setBlockState(world, biomeFenceState, 2, 1, 10, structureBB);
        	world.setBlockState(new BlockPos(this.getXWithOffset(2, 10), this.getYWithOffset(2), this.getZWithOffset(2, 10)), Blocks.TORCH.getDefaultState(), 2);
        	this.setBlockState(world, biomeFenceState, 8, 1, 0, structureBB);
        	world.setBlockState(new BlockPos(this.getXWithOffset(8, 0), this.getYWithOffset(2), this.getZWithOffset(8, 0)), Blocks.TORCH.getDefaultState(), 2);        	
        	
        	
			// Banners on the market stalls
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				{0, 4, 6, 3, GeneralConfig.useVillageColors ? townColor3: 12}, // Brown by default
				{0, 4, 5, 3, GeneralConfig.useVillageColors ? townColor6 : 12}, // Brown by default
				
				{10, 4, 0, 2, GeneralConfig.useVillageColors ? townColor2 : 12}, // Brown by default
				{11, 4, 0, 2, GeneralConfig.useVillageColors ? townColor7 : 12}, // Brown by default
				
				{11, 4, 11, 0, GeneralConfig.useVillageColors ? townColor4 : 12}, // Brown by default
				{10, 4, 11, 0, GeneralConfig.useVillageColors ? townColor5 : 12}, // Brown by default
			})
			{
    			int bannerXBB = uvwoc[0];
    			int bannerYBB = uvwoc[1];
    			int bannerZBB = uvwoc[2];
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.WALL_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.getCoordBaseMode().getHorizontalIndex(), true)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				NBTTagCompound modifystanding = new NBTTagCompound();
				tilebanner.writeToNBT(modifystanding);
				modifystanding.setInteger("Base", 15 - uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
			}
        	
        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 7;
    			int signYBB = 1;
    			int signZBB = 6;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerXBB = 11;
    			int bannerZBB = 6;
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
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"), (namePrefix + " " + nameRoot + " " + nameSuffix).trim());
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner, false);
        		
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
    		}

    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{3, 1, 2, -1, 0},
        			{5, 1, 4, -1, 0},
        			{8, 1, 4, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not
        			if (random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(ia[0], ia[2]), 0, this.getZWithOffset(ia[0], ia[2]))).getY();
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(entityvillager);
        		}
            }
            
            return true;
        }
    }
    

	// --- Savanna Fountain --- //
    
    public static class SavannaMeetingPoint2 extends StartVN
    {
	    public SavannaMeetingPoint2() {}
		
		public SavannaMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 10;
    		int depth = 10;
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
    		if (GeneralConfig.debugMessages)
    		{
    			LogHelper.info(
    					this.materialType + " " +  this.villageType + " village generated in "
    					+ this.worldChunkMngr.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(9, 1),
        			this.getYWithOffset(2),
        			this.getZWithOffset(9, 1));
        	
        	// Load the values of interest into memory
        	if (this.townColor==-1) {this.townColor = villageNBTtag.getInteger("townColor");}
        	if (this.townColor2==-1) {this.townColor2 = villageNBTtag.getInteger("townColor2");}
        	if (this.townColor3==-1) {this.townColor3 = villageNBTtag.getInteger("townColor3");}
        	if (this.townColor4==-1) {this.townColor4 = villageNBTtag.getInteger("townColor4");}
        	if (this.townColor5==-1) {this.townColor5 = villageNBTtag.getInteger("townColor5");}
        	if (this.townColor6==-1) {this.townColor6 = villageNBTtag.getInteger("townColor6");}
        	if (this.townColor7==-1) {this.townColor7 = villageNBTtag.getInteger("townColor7");}
        	if (this.namePrefix.equals("")) {this.namePrefix = villageNBTtag.getString("namePrefix");}
        	if (this.nameRoot.equals("")) {this.nameRoot = villageNBTtag.getString("nameRoot");}
        	if (this.nameSuffix.equals("")) {this.nameSuffix = villageNBTtag.getString("nameSuffix");}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	// Top layer 
        	
        	// Set grass paths
        	for (int[] grass_uw : new int[][]{
        		{0, 4}, {0, 5}, {0, 6}, 
        		{1, 4}, {1, 5}, {1, 6}, 
        		{2, 3}, {2, 4}, {2, 5}, {2, 6}, {2, 7},
        		{3, 2}, {3, 3}, {3, 7}, {3, 8},  
        		{4, 0}, {4, 1}, {4, 2}, {4, 8}, {4, 9}, {4, 10}, 
        		{5, 0}, {5, 1}, {5, 2}, {5, 8}, {5, 9}, {5, 10}, 
        		{6, 0}, {6, 1}, {6, 2}, {6, 8}, {6, 9}, {6, 10},
        		{7, 2}, {7, 3}, {7, 7}, {7, 8},  
        		{8, 3}, {8, 4}, {8, 5}, {8, 6}, {8, 7},
        		{9, 4}, {9, 5}, {9, 6}, 
        		{10, 4}, {10, 5}, {10, 6}, 
        	})
        	{
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, grass_uw[0], -1, grass_uw[1], structureBB); // Foundation
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(grass_uw[0], grass_uw[1]), this.getYWithOffset(0), this.getZWithOffset(grass_uw[0], grass_uw[1])); // Path
        		this.clearCurrentPositionBlocksUpwards(world, grass_uw[0], 1, grass_uw[1], structureBB); // Clear above
        	}
        	// Set Grass blocks
        	for (int[] grass_uw : new int[][]{
        		{0, 3}, {0, 7},
        		{1, 2}, {1, 3}, {1, 7}, {1, 8},  
        		{2, 1}, {2, 2}, {2, 8}, {2, 9}, 
        		{3, 1}, {3, 9}, {3, 10}, 
        		{4, 4}, {4, 5}, {4, 6},
        		{5, 4}, {5, 6}, 
        		{6, 4}, {6, 5}, {6, 6}, 
        		{7, 9}, {7, 10}, 
        		{8, 8}, {8, 9}, 
        		{9, 3}, {9, 7}, {9, 8}, 
        		{10, 7}, 
        	})
        	{
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, grass_uw[0], -1, grass_uw[1], structureBB); // Foundation
        		this.setBlockState(world, biomeGrassState, grass_uw[0], 0, grass_uw[1], structureBB);
        		this.clearCurrentPositionBlocksUpwards(world, grass_uw[0], 1, grass_uw[1], structureBB); // Clear above
        	}
        	// Set dirt
        	for (int[] uw : new int[][]{
        		{1, 1}, {1, 9}, 
        		{3, 4}, {3, 5}, {3, 6},
        		{4, 3}, {4, 7}, 
        		{5, 3}, {5, 5}, {5, 7},
        		{6, 3}, {6, 7},
        		{7, 4}, {7, 5}, {7, 6},
        		{9, 1}, {9, 9},
        	})
        	{
        		this.setBlockState(world, biomeDirtState, uw[0], 0, uw[1], structureBB);
        	}
        	
        	// Set unkempt grass
        	for (int[] grass_uw : new int[][]{
        		{7, 10},
        		{8, 8}, {8, 9}, 
        		{9, 7}, 
        		{10, 7},
        	})
        	{
        		this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(0), grass_uw[0], 1, grass_uw[1], structureBB);
        	}
        	
        	
        	// Fountain
        	
        	// Set rim
        	this.fillWithBlocks(world, structureBB, 3, 1, 4, 7, 1, 6, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 1), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 1), false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 3, 6, 1, 7, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 1), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 1), false);
        	// Set water
        	this.fillWithBlocks(world, structureBB, 4, 1, 4, 6, 1, 6, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
        	// Set spire
        	this.fillWithBlocks(world, structureBB, 5, 1, 5, 5, 5, 5, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), false);
        	// Place individual clay blocks here and there
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 1, 1, 1, structureBB);
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 9, 1, 1, structureBB);
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 9, 1, 9, structureBB);
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 1, 1, 9, structureBB);
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 5, 1, 3, structureBB);
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 5, 1, 7, structureBB);
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 3, 1, 5, structureBB);
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 7, 1, 5, structureBB);
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 1), 5, 4, 5, structureBB);
        	
        	// Torches
        	world.setBlockState(new BlockPos(this.getXWithOffset(1, 1), this.getYWithOffset(2), this.getZWithOffset(1, 1)), Blocks.TORCH.getStateFromMeta(0), 2);     
        	world.setBlockState(new BlockPos(this.getXWithOffset(9, 9), this.getYWithOffset(2), this.getZWithOffset(9, 9)), Blocks.TORCH.getStateFromMeta(0), 2);     
        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 9;
    			int signYBB = 2;
    			int signZBB = 1;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);

    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		
                int signXBB2 = 1;
    			int signZBB2 = 9;
                int signX2 = this.getXWithOffset(signXBB2, signZBB2);
                int signZ2 = this.getZWithOffset(signXBB2, signZBB2);
                
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
                
    			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX2, signY, signZ2), signContents2);
            }
            
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{3, 1, 8, -1, 0},
        			{6, 1, 9, -1, 0},
        			{9, 1, 6, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not
        			if (random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(ia[0], ia[2]), 0, this.getZWithOffset(ia[0], ia[2]))).getY();
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(entityvillager);
        		}
            }
            
        	// Banners
            // Moved to after sign so that you have the village name info
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				{5, 4, 6, 0, 12},
				{6, 4, 5, 1, 12},
				{5, 4, 4, 2, 12},
				{4, 4, 5, 3, 12},
			})
			{
    			int bannerXBB = uvwoc[0];
    			int bannerYBB = uvwoc[1];
    			int bannerZBB = uvwoc[2];
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.WALL_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.getCoordBaseMode().getHorizontalIndex(), true)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				NBTTagCompound modifystanding = new NBTTagCompound();
				tilebanner.writeToNBT(modifystanding);
				
				if (GeneralConfig.useVillageColors)
				{
    				tilebanner.readFromNBT(modifystanding);
    				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"), (namePrefix + " " + nameRoot + " " + nameSuffix).trim());
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner, false);
				}
				else
				{
					modifystanding.setInteger("Base", 15 - (GeneralConfig.useVillageColors ? uvwoc[4] : 12));
    				tilebanner.readFromNBT(modifystanding);
				}
				
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
			}
        	
    		
            return true;
        }
    }
    
    
	// --- Savanna Double Well --- //
    
    public static class SavannaMeetingPoint3 extends StartVN
    {
	    public SavannaMeetingPoint3() {}
		
		public SavannaMeetingPoint3(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 8;
    		int depth = 10;
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
    		if (GeneralConfig.debugMessages)
    		{
    			LogHelper.info(
    					this.materialType + " " +  this.villageType + " village generated in "
    					+ this.worldChunkMngr.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 3 : 4), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 4 : 3), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 3 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 4 : 3), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeWoodenSlabState = StructureVillageVN.getBiomeSpecificBlock(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlock(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeBarkState = ModObjects.chooseModBark(biomeLogState);
        	
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(4, 5),
        			this.getYWithOffset(3),
        			this.getZWithOffset(4, 5));
        	
        	// Load the values of interest into memory
        	if (this.townColor==-1) {this.townColor = villageNBTtag.getInteger("townColor");}
        	if (this.townColor2==-1) {this.townColor2 = villageNBTtag.getInteger("townColor2");}
        	if (this.townColor3==-1) {this.townColor3 = villageNBTtag.getInteger("townColor3");}
        	if (this.townColor4==-1) {this.townColor4 = villageNBTtag.getInteger("townColor4");}
        	if (this.townColor5==-1) {this.townColor5 = villageNBTtag.getInteger("townColor5");}
        	if (this.townColor6==-1) {this.townColor6 = villageNBTtag.getInteger("townColor6");}
        	if (this.townColor7==-1) {this.townColor7 = villageNBTtag.getInteger("townColor7");}
        	if (this.namePrefix.equals("")) {this.namePrefix = villageNBTtag.getString("namePrefix");}
        	if (this.nameRoot.equals("")) {this.nameRoot = villageNBTtag.getString("nameRoot");}
        	if (this.nameSuffix.equals("")) {this.nameSuffix = villageNBTtag.getString("nameSuffix");}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	// Top grass path 
        	for (int u=2; u<=6; u++) {for (int w=1; w<=9; w++) {
        		if (!(u==4 && w==3) && !(u==4 && w==7)) // To prevent path overwriting the water in the wells
        		{
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, -1, w, structureBB); // Foundation
            		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(u, w), this.getYWithOffset(0), this.getZWithOffset(u, w)); // Path
            		this.clearCurrentPositionBlocksUpwards(world, u, 1, w, structureBB); // Clear above	
        		}
        	}}
        	
        	// Set grass paths
        	for (int[] uw : new int[][]{
        		{1, 3}, {1, 4}, {1, 5}, {1, 6}, {1, 7}, 
        		{7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, 
        	})
        	{
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, uw[0], -1, uw[1], structureBB); // Foundation
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(uw[0], uw[1]), this.getYWithOffset(0), this.getZWithOffset(uw[0], uw[1])); // Path
        		this.clearCurrentPositionBlocksUpwards(world, uw[0], 1, uw[1], structureBB); // Clear above
        	}
        	// Set Grass blocks
        	for (int[] uw : new int[][]{
        		{0, 9},
        		{1, 1},
        		{9, 2}, 
        	})
        	{
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, uw[0], -1, uw[1], structureBB); // Foundation
        		this.setBlockState(world, biomeGrassState, uw[0], 0, uw[1], structureBB);
        		this.clearCurrentPositionBlocksUpwards(world, uw[0], 1, uw[1], structureBB); // Clear above
        	}
        	
        	
            // Add path nodules at the end
            for (int i=0; i<3; i++)
            {
        		for (int[] uw: new int[][]{
        			{i+3, 0},
        			{i+3, 10},
        			{0, i+4},
        			{8, i+4},
        		})
        		{
        			int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uw[0], uw[1]), 0, this.getZWithOffset(uw[0], uw[1]))).down().getY();
            		
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, uw[0], k+2-this.boundingBox.minY, uw[1], structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(uw[0], uw[1]), k, this.getZWithOffset(uw[0], uw[1]));
                   	}
        		}
            }
        	
        	
        	// Establish wells
        	
        	// Dirt
        	this.fillWithBlocks(world, structureBB, 3, 0, 2, 5, 0, 4, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 3, 0, 6, 5, 0, 8, biomeDirtState, biomeDirtState, false);
        	
        	// Basins
        	this.fillWithBlocks(world, structureBB, 3, 1, 2, 3, 1, 4, biomeBarkState, biomeBarkState, false);
        	this.fillWithBlocks(world, structureBB, 5, 1, 2, 5, 1, 4, biomeBarkState, biomeBarkState, false);
        	this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), true), 4, 1, 2, structureBB);
        	this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), true), 4, 1, 4, structureBB);
        	this.setBlockState(world, biomeBarkState, 4, 0, 3, structureBB);
        	this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), 4, 1, 3, structureBB);
        	
        	this.fillWithBlocks(world, structureBB, 3, 1, 6, 3, 1, 8, biomeBarkState, biomeBarkState, false);
        	this.fillWithBlocks(world, structureBB, 5, 1, 6, 5, 1, 8, biomeBarkState, biomeBarkState, false);
        	this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), true), 4, 1, 6, structureBB);
        	this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), true), 4, 1, 8, structureBB);
        	this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), true), 4, 0, 7, structureBB);
        	this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), 4, 1, 7, structureBB);
        	
        	// Torches on the corners
        	for (int[] uvwm : new int[][]{
        		{3, 2, 2, 0},
        		{5, 2, 2, 0},
        		{3, 2, 8, 0},
        		{5, 2, 8, 0},
        	})
        	{
        		world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.TORCH.getStateFromMeta(uvwm[3]), 2);
        	}
        	
        	// Pavilion
        	this.fillWithBlocks(world, structureBB, 3, 2, 4, 5, 3, 6, biomeFenceState, biomeFenceState, false);
        	this.fillWithAir(world, structureBB, 4, 2, 4, 4, 3, 6);
        	this.fillWithAir(world, structureBB, 3, 2, 5, 5, 3, 5);
        	this.fillWithBlocks(world, structureBB, 3, 4, 4, 5, 4, 6, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.setBlockState(world, biomePlankState, 4, 4, 5, structureBB);
        	// Torch
        	for (int[] uvwm : new int[][]{
        		{4, 5, 5, 0},
        	})
        	{
        		world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.TORCH.getStateFromMeta(uvwm[3]), 2);
        	}
    		
        	// Sign support
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 4, 3, 5, structureBB);
        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 3;
    			int signYBB = 3;
    			int signZBB = 5;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(3, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		
                int signXBB2 = 5;
                int signX2 = this.getXWithOffset(signXBB2, signZBB);
                int signZ2 = this.getZWithOffset(signXBB2, signZBB);
                
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
                
    			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(1, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX2, signY, signZ2), signContents2);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerXBB = 7;
    			int bannerZBB = 7;
    			int bannerYBB = 1;
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a cobblestone foundation
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"), (namePrefix + " " + nameRoot + " " + nameSuffix).trim());
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner, false);
        		
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
    		}
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{6, 1, 5, -1, 0},
        			{7, 1, 3, -1, 0},
        			{9, 1, 6, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not
        			if (random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(ia[0], ia[2]), 0, this.getZWithOffset(ia[0], ia[2]))).getY();
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(entityvillager);
        		}
            }
            
            return true;
        }
    }
    

	// --- Savanna Single Well --- //
    
    public static class SavannaMeetingPoint4 extends StartVN
    {
	    public SavannaMeetingPoint4() {}
		
		public SavannaMeetingPoint4(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 8;
    		int depth = 8;
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
    		if (GeneralConfig.debugMessages)
    		{
    			LogHelper.info(
    					this.materialType + " " +  this.villageType + " village generated in "
    					+ this.worldChunkMngr.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 3, EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 3, EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlock(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeWoodenStairsState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_STAIRS.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeBarkState = ModObjects.chooseModBark(biomeLogState);
        	
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(4, 4),
        			this.getYWithOffset(3),
        			this.getZWithOffset(4, 4));
        	
        	// Load the values of interest into memory
        	if (this.townColor==-1) {this.townColor = villageNBTtag.getInteger("townColor");}
        	if (this.townColor2==-1) {this.townColor2 = villageNBTtag.getInteger("townColor2");}
        	if (this.townColor3==-1) {this.townColor3 = villageNBTtag.getInteger("townColor3");}
        	if (this.townColor4==-1) {this.townColor4 = villageNBTtag.getInteger("townColor4");}
        	if (this.townColor5==-1) {this.townColor5 = villageNBTtag.getInteger("townColor5");}
        	if (this.townColor6==-1) {this.townColor6 = villageNBTtag.getInteger("townColor6");}
        	if (this.townColor7==-1) {this.townColor7 = villageNBTtag.getInteger("townColor7");}
        	if (this.namePrefix.equals("")) {this.namePrefix = villageNBTtag.getString("namePrefix");}
        	if (this.nameRoot.equals("")) {this.nameRoot = villageNBTtag.getString("nameRoot");}
        	if (this.nameSuffix.equals("")) {this.nameSuffix = villageNBTtag.getString("nameSuffix");}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	// Set grass paths
        	for (int u=1; u<=7; u++) {for (int w=1; w<=7; w++) {
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, -1, w, structureBB); // Foundation
        		if (u<3 || u>5 || w<3 || w>5) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(u, w), this.getYWithOffset(0), this.getZWithOffset(u, w));} // Path
        		else {this.setBlockState(world, biomeDirtState, u, 0, w, structureBB);}
        		this.clearCurrentPositionBlocksUpwards(world, u, 1, w, structureBB); // Clear above
        	}}
        	// Set Grass blocks
        	for (int[] uw : new int[][]{
        		{0, 0},
        		{1, 10},
        	})
        	{
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, uw[0], -1, uw[1], structureBB); // Foundation
        		this.setBlockState(world, biomeGrassState, uw[0], 0, uw[1], structureBB);
        		this.clearCurrentPositionBlocksUpwards(world, uw[0], 1, uw[1], structureBB); // Clear above
        	}
        	
        	
            // Add path nodules at the end
            for (int i=0; i<3; i++)
            {
        		for (int[] uw: new int[][]{
        			{i+3, 0},
        			{i+3, 8},
        			{0, i+3},
        			{8, i+3},
        		})
        		{
        			int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uw[0], uw[1]), 0, this.getZWithOffset(uw[0], uw[1]))).down().getY();
            		
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, uw[0], k+2-this.boundingBox.minY, uw[1], structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(uw[0], uw[1]), k, this.getZWithOffset(uw[0], uw[1]));
                   	}
        		}
            }
        	
        	
        	// Establish wells
        	
        	// Dirt
        	this.fillWithBlocks(world, structureBB, 3, 0, 2, 5, 0, 6, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 2, 0, 3, 5, 0, 5, biomeDirtState, biomeDirtState, false);
        	
        	// Basins
        	this.fillWithBlocks(world, structureBB, 3, 1, 2, 5, 1, 6, biomeBarkState, biomeBarkState, false);
        	this.fillWithBlocks(world, structureBB, 2, 1, 3, 6, 1, 5, biomeBarkState, biomeBarkState, false);
        	this.fillWithBlocks(world, structureBB, 4, 0, 3, 4, 0, 5, biomeBarkState, biomeBarkState, false);
        	this.fillWithBlocks(world, structureBB, 3, 0, 4, 5, 0, 4, biomeBarkState, biomeBarkState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 3, 4, 1, 5, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 4, 5, 1, 4, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
        	
        	// Torches on the corners
        	for (int[] uvwm : new int[][]{
        		{2, 2, 4, 0},
        		{4, 2, 2, 0},
        		{4, 2, 6, 0},
        		{6, 2, 4, 0},
        	})
        	{
        		world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.TORCH.getStateFromMeta(uvwm[3]), 2);
        	}
        	
        	// Pavilion
        	// Supports
        	this.fillWithBlocks(world, structureBB, 3, 2, 3, 5, 3, 5, biomeFenceState, biomeFenceState, false);
        	this.fillWithAir(world, structureBB, 4, 2, 3, 4, 3, 5);
        	this.fillWithAir(world, structureBB, 3, 2, 4, 5, 3, 4);
        	// Roof
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(3), 3, 4, 3, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(3), 4, 4, 3, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(1), 5, 4, 3, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(1), 5, 4, 4, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(2), 5, 4, 5, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(2), 4, 4, 5, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(0), 3, 4, 5, structureBB);
        	this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(0), 3, 4, 4, structureBB);
        	this.setBlockState(world, biomePlankState, 4, 4, 4, structureBB);

    		
        	// Sign support
        	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), 4, 3, 4, structureBB);
        	
        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 4;
    			int signYBB = 3;
    			int signZBB = 3;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(2, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		
                int signZBB2 = 5;
                int signX2 = this.getXWithOffset(signXBB, signZBB2);
                int signZ2 = this.getZWithOffset(signXBB, signZBB2);
                
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
                
    			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX2, signY, signZ2), signContents2);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerXBB = 7;
    			int bannerZBB = 7;
    			int bannerYBB = 1;
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a cobblestone foundation
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"), (namePrefix + " " + nameRoot + " " + nameSuffix).trim());
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner, false);
        		
        		world.setTileEntity(new BlockPos(bannerX, bannerY, bannerZ), tilebanner);
    		}
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{7, 1, 1, -1, 0},
        			{8, 1, 3, -1, 0},
        			})
        		{
        			EntityVillager entityvillager = new EntityVillager(world);
        			
        			// Nitwits more often than not
        			if (random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(ia[0], ia[2]), 0, this.getZWithOffset(ia[0], ia[2]))).getY();
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(entityvillager);
        		}
            }
            
            return true;
        }
    }
    
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
    public static ArrayList<BlueprintData> getRandomSavannaDecorBlueprint(MaterialType materialType, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 1;
		return getSavannaDecorBlueprint(random.nextInt(decorCount), materialType, biome, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getSavannaDecorBlueprint(int decorType, MaterialType materialType, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), materialType, biome);
		
        switch (decorType)
        {
    	case 0: // Torch on fence
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, Blocks.TORCH.getStateFromMeta(0));
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
