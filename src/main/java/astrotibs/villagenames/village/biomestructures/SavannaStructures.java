package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astrotibs.villagenames.banner.TileEntityBanner;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
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
		
		public SavannaMeetingPoint1(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 13;
    		int depth = 11;
    		int height = 4;
    		
		    // Establish orientation
            this.coordBaseMode = random.nextInt(4);
            switch (this.coordBaseMode)
            {
                case 0:
                case 2:
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
    					+ this.worldChunkMngr.getBiomeGenAt((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode
    					);
    		}
    		
			// Northward
			if (this.coordBaseMode%2!=1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode==0 ? 5 : this.coordBaseMode==2 ? 4 : 0), this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());}
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode==1 || this.coordBaseMode==2 ? 5 : 4), 3, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode==1 || this.coordBaseMode==2 ? 5 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
			// Westward
			if (this.coordBaseMode%2!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode==1 ? 4 : 5), 1, this.getComponentType());}
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_slab, 0, this.materialType, this.biome); Block biomeWoodSlabBlock = (Block)blockObject[0]; int biomeWoodSlabMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, this.materialType, this.biome); Block biomeWoodenStairsBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 0, this.materialType, this.biome); Block biomeLogVertBlock = (Block)blockObject[0]; int biomeLogVertMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign, 0, this.materialType, this.biome); Block biomeStandingSignBlock = (Block)blockObject[0];
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode);
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
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
        	this.fillWithMetadataBlocks(world, structureBB, 0, 0, 0, 13, 0, 11, biomeGrassBlock, biomeGrassMeta, biomeGrassBlock, biomeGrassMeta, false);
        	
        	// Clear above
        	for (int u=0; u<=13; u++) {for (int w=0; w<=11; w++) {
        			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, -1, w, structureBB); // Foundation
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
        		this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 0, grass_uw[0], 1, grass_uw[1], structureBB);
        	}
        	// Tall Grass
        	this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 2, 0, 1, 5, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 8, 0, 2, 5, structureBB);
        	
        	// Stalls
        	
        	// Posts
        	this.fillWithBlocks(world, structureBB, 1, 1, 4, 1, 3, 4, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 4, 3, 3, 4, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 7, 3, 3, 7, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 1, 1, 7, 1, 3, 7, biomeFenceBlock, biomeFenceBlock, false);
        	// Awning
        	this.fillWithMetadataBlocks(world, structureBB, 1, 4, 4, 3, 4, 7, biomeWoodSlabBlock, biomeWoodSlabMeta, biomeWoodSlabBlock, biomeWoodSlabMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 1, 4, 5, 3, 4, 5, biomeWoodenStairsBlock, (new int[]{2,1,3,0})[this.coordBaseMode], biomeWoodenStairsBlock, (new int[]{2,1,3,0})[this.coordBaseMode], false);
        	this.fillWithMetadataBlocks(world, structureBB, 1, 4, 6, 3, 4, 6, biomeWoodenStairsBlock, (new int[]{3,0,2,1})[this.coordBaseMode], biomeWoodenStairsBlock, (new int[]{3,0,2,1})[this.coordBaseMode], false);
        	// Logs
        	this.fillWithMetadataBlocks(world, structureBB, 2, 1, 5, 2, 1, 6, biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);
        	
        	this.fillWithBlocks(world, structureBB, 9, 1, 1, 9, 3, 1, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 9, 1, 3, 9, 3, 3, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 12, 1, 3, 12, 3, 3, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 12, 1, 1, 12, 3, 1, biomeFenceBlock, biomeFenceBlock, false);
        	// Awning
        	this.fillWithMetadataBlocks(world, structureBB, 9, 4, 1, 12, 4, 3, biomeWoodSlabBlock, biomeWoodSlabMeta, biomeWoodSlabBlock, biomeWoodSlabMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 10, 4, 1, 10, 4, 3, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 0 : 2, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 0 : 2, false);
        	this.fillWithMetadataBlocks(world, structureBB, 11, 4, 1, 11, 4, 3, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 1 : 3, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 1 : 3, false);
        	// Logs
        	this.fillWithMetadataBlocks(world, structureBB, 10, 1, 2, 11, 1, 2, biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);
        	
        	this.fillWithBlocks(world, structureBB, 9, 1, 8, 9, 3, 8, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 9, 1, 10, 9, 3, 10, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 12, 1, 10, 12, 3, 10, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithBlocks(world, structureBB, 12, 1, 8, 12, 3, 8, biomeFenceBlock, biomeFenceBlock, false);        	
        	// Awning
        	this.fillWithMetadataBlocks(world, structureBB, 9, 4, 8, 12, 4, 10, biomeWoodSlabBlock, biomeWoodSlabMeta, biomeWoodSlabBlock, biomeWoodSlabMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 10, 4, 8, 10, 4, 10, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 0 : 2, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 0 : 2, false);
        	this.fillWithMetadataBlocks(world, structureBB, 11, 4, 8, 11, 4, 10, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 1 : 3, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 1 : 3, false);
        	// Logs
        	this.fillWithMetadataBlocks(world, structureBB, 10, 1, 9, 11, 1, 9, biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);
        	
        	// Fences with torches
        	this.placeBlockAtCurrentPosition(world, biomeFenceBlock, 0, 2, 1, 10, structureBB);
        	world.setBlock(this.getXWithOffset(2, 10), this.getYWithOffset(2), this.getZWithOffset(2, 10), Blocks.torch, 0, 2);
        	this.placeBlockAtCurrentPosition(world, biomeFenceBlock, 0, 8, 1, 0, structureBB);
        	world.setBlock(this.getXWithOffset(8, 0), this.getYWithOffset(2), this.getZWithOffset(8, 0), Blocks.torch, 0, 2);        	
        	
        	
			// Banners on the market stalls
    		Block testForBanner = ModObjects.chooseModBannerBlock(); // Checks to see if supported mod banners are available. Will be null if there aren't any.
    		if (testForBanner!=null)
			{
    			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
    				{0, 4, 6, 3, townColor},
    				{0, 4, 5, 3, townColor2},
    				{10, 4, 0, 2, townColor},
    				{11, 4, 0, 2, townColor2},
    				{11, 4, 11, 0, townColor},
    				{10, 4, 11, 0, townColor2},
    			})
    			{
        			int bannerXBB = uvwoc[0];
        			int bannerYBB = uvwoc[1];
        			int bannerZBB = uvwoc[2];
        			
        			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
        			int bannerY = this.getYWithOffset(bannerYBB);
                    int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                    
                	// Set the banner and its orientation
    				world.setBlock(bannerX, bannerY, bannerZ, testForBanner);
    				world.setBlockMetadataWithNotify(bannerX, bannerY, bannerZ, StructureVillageVN.getSignRotationMeta(uvwoc[3], this.coordBaseMode, true), 2);
    				
    				// Set the tile entity
    				TileEntity tilebanner = new TileEntityBanner();
    				NBTTagCompound modifystanding = new NBTTagCompound();
    				tilebanner.writeToNBT(modifystanding);
    				modifystanding.setBoolean("IsStanding", false);
    				modifystanding.setInteger("Base", GeneralConfig.useVillageColors ? uvwoc[4] : 12);
    				tilebanner.readFromNBT(modifystanding);
    				
            		world.setTileEntity(bannerX, bannerY, bannerZ, tilebanner);
    			}
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
        		
    			world.setBlock(signX, signY, signZ, biomeStandingSignBlock, StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode, false), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(signX, signY, signZ, signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
        		testForBanner = ModObjects.chooseModBannerBlock(); // Checks to see if supported mod banners are available. Will be null if there aren't any.
        		if (testForBanner!=null)
    			{
                    int bannerXBB = 11;
        			int bannerZBB = 6;
        			int bannerYBB = -1;
        			if (this.bannerY==0)
        			{
        				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(bannerXBB, bannerZBB), this.getZWithOffset(bannerXBB, bannerZBB))-this.boundingBox.minY +1;
        				bannerYBB = this.bannerY;
        			}
        			else {bannerYBB = this.bannerY;}
        			
        			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
        			int bannerY = this.getYWithOffset(bannerYBB);
                    int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                    
                    // Place a grass foundation
                    this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, bannerXBB, bannerYBB-1, bannerZBB, structureBB);
                    this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
                    // Clear space upward
                    this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                    
                	// Set the banner and its orientation
    				world.setBlock(bannerX, bannerY, bannerZ, testForBanner);
    				world.setBlockMetadataWithNotify(bannerX, bannerY, bannerZ, StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode, false), 2);
    				
    				// Set the tile entity
    				TileEntity tilebanner = new TileEntityBanner();
    				NBTTagCompound modifystanding = new NBTTagCompound();
    				tilebanner.writeToNBT(modifystanding);
    				modifystanding.setBoolean("IsStanding", true);
    				tilebanner.readFromNBT(modifystanding);
    				ItemStack villageBanner = ModObjects.chooseModBannerItem();
    				villageBanner.setTagInfo("BlockEntityTag", villageNBTtag.getCompoundTag("BlockEntityTag"));
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
            		
            		world.setTileEntity(bannerX, bannerY, bannerZ, tilebanner);
    			}
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
        			if (GeneralConfig.enableNitwit && random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(ia[0], ia[2]), this.getZWithOffset(ia[0], ia[2]));
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(entityvillager);
        		}
            }
            
            return true;
        }
    }
    

	// --- Savanna Fountain --- //
    
    public static class SavannaMeetingPoint2 extends StartVN
    {
	    public SavannaMeetingPoint2() {}
		
		public SavannaMeetingPoint2(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 10;
    		int depth = 10;
    		int height = 5;
    		
		    // Establish orientation
            this.coordBaseMode = random.nextInt(4);
            switch (this.coordBaseMode)
            {
                case 0:
                case 2:
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
    					+ this.worldChunkMngr.getBiomeGenAt((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 4, 3, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 4, 1, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign, 0, this.materialType, this.biome); Block biomeStandingSignBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, this.materialType, this.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode);
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
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
        		{0, 5}, {0, 6}, {0, 7}, 
        		{1, 5}, {1, 6}, {1, 7}, 
        		{2, 4}, {2, 5}, {2, 6}, {2, 7}, {2, 8},
        		{3, 3}, {3, 4}, {3, 8}, {3, 9},  
        		{4, 0}, {4, 1}, {4, 2}, {4, 8}, {4, 9}, {4, 10}, 
        		{5, 0}, {5, 1}, {5, 2}, {5, 8}, {5, 9}, {5, 10}, 
        		{6, 0}, {6, 1}, {6, 2}, {6, 8}, {6, 9}, {6, 10},
        		{7, 3}, {7, 4}, {7, 8}, {7, 9},  
        		{8, 4}, {8, 5}, {8, 6}, {8, 7}, {8, 8},
        		{9, 5}, {9, 6}, {9, 7}, 
        		{10, 5}, {10, 6}, {10, 7}, 
        	})
        	{
        		this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, grass_uw[0], -1, grass_uw[1], structureBB); // Foundation
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(grass_uw[0], grass_uw[1]), this.getYWithOffset(0), this.getZWithOffset(grass_uw[0], grass_uw[1])); // Path
        		this.clearCurrentPositionBlocksUpwards(world, grass_uw[0], 1, grass_uw[1], structureBB); // Clear above
        	}
        	// Set Grass blocks
        	for (int[] grass_uw : new int[][]{
        		{0, 3}, {0, 7},
        		{1, 1}, {1, 2}, {1, 3}, {1, 7}, {1, 8}, {1, 9}, 
        		{2, 1}, {2, 2}, {2, 8}, {2, 9}, 
        		{3, 1}, {3, 9}, {3, 10}, 
        		{4, 4}, {4, 5}, {4, 6},
        		{5, 4}, {5, 5}, {5, 6}, 
        		{6, 4}, {6, 5}, {6, 6}, 
        		{7, 9}, {7, 10}, 
        		{8, 8}, {8, 9}, 
        		{9, 1}, {9, 3}, {9, 7}, {9, 8}, {9, 9}, 
        		{10, 7}, 
        	})
        	{
        		this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, grass_uw[0], -1, grass_uw[1], structureBB); // Foundation
        		this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, grass_uw[0], 0, grass_uw[1], structureBB);
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
        		this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uw[0], 0, uw[1], structureBB);
        	}
        	
        	// Set unkempt grass
        	for (int[] grass_uw : new int[][]{
        		{7, 10},
        		{8, 8}, {8, 9}, 
        		{9, 7}, 
        		{10, 7},
        	})
        	{
        		this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 0, grass_uw[0], 1, grass_uw[1], structureBB);
        	}
        	
        	
        	// Fountain
        	
        	// Set rim
        	this.fillWithMetadataBlocks(world, structureBB, 3, 1, 4, 7, 1, 6, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor2 : 1, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor2 : 1, false);
        	this.fillWithMetadataBlocks(world, structureBB, 4, 1, 3, 6, 1, 7, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor2 : 1, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor2 : 1, false);
        	// Set water
        	this.fillWithBlocks(world, structureBB, 4, 1, 4, 6, 1, 6, Blocks.flowing_water, Blocks.flowing_water, false);
        	// Set spire
        	this.fillWithMetadataBlocks(world, structureBB, 5, 1, 5, 5, 5, 5, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, false);
        	// Place individual clay blocks here and there
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 1, 1, 1, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 9, 1, 1, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 9, 1, 9, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 1, 1, 9, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 5, 1, 3, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 5, 1, 7, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 3, 1, 5, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 7, 1, 5, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor2 : 1, 5, 4, 5, structureBB);
        	
        	// Torches
        	world.setBlock(this.getXWithOffset(1, 1), this.getYWithOffset(2), this.getZWithOffset(1, 1), Blocks.torch, 0, 2);     
        	world.setBlock(this.getXWithOffset(9, 9), this.getYWithOffset(2), this.getZWithOffset(9, 9), Blocks.torch, 0, 2);     
        	
        	// Banners
    		Block testForBanner = ModObjects.chooseModBannerBlock(); // Checks to see if supported mod banners are available. Will be null if there aren't any.
			if (testForBanner!=null)
			{
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
    				world.setBlock(bannerX, bannerY, bannerZ, testForBanner);
    				world.setBlockMetadataWithNotify(bannerX, bannerY, bannerZ, StructureVillageVN.getSignRotationMeta(uvwoc[3], this.coordBaseMode, true), 2);
    				
    				// Set the tile entity
    				TileEntity tilebanner = new TileEntityBanner();
    				NBTTagCompound modifystanding = new NBTTagCompound();
    				tilebanner.writeToNBT(modifystanding);
    				modifystanding.setBoolean("IsStanding", false);
    				
    				if (GeneralConfig.useVillageColors)
    				{
        				tilebanner.readFromNBT(modifystanding);
        				ItemStack villageBanner = ModObjects.chooseModBannerItem();
        				villageBanner.setTagInfo("BlockEntityTag", villageNBTtag.getCompoundTag("BlockEntityTag"));
        				
            			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
    				}
    				else
    				{
    					modifystanding.setInteger("Base", uvwoc[4]);
        				tilebanner.readFromNBT(modifystanding);
    				}
    				
            		world.setTileEntity(bannerX, bannerY, bannerZ, tilebanner);
    			}
			}
        	
    		
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

    			world.setBlock(signX, signY, signZ, biomeStandingSignBlock, StructureVillageVN.getSignRotationMeta(12, this.coordBaseMode, false), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(signX, signY, signZ, signContents);
        		
                int signXBB2 = 1;
    			int signZBB2 = 9;
                int signX2 = this.getXWithOffset(signXBB2, signZBB2);
                int signZ2 = this.getZWithOffset(signXBB2, signZBB2);
                
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
                
    			world.setBlock(signX2, signY, signZ2, biomeStandingSignBlock, StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode, false), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(signX2, signY, signZ2, signContents2);
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
        			if (GeneralConfig.enableNitwit && random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(ia[0], ia[2]), this.getZWithOffset(ia[0], ia[2]));
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(entityvillager);
        		}
            }
            
            return true;
        }
    }
    
    
	// --- Savanna Double Well --- //
    
    public static class SavannaMeetingPoint3 extends StartVN
    {
	    public SavannaMeetingPoint3() {}
		
		public SavannaMeetingPoint3(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 8;
    		int depth = 10;
    		int height = 5;
    		
		    // Establish orientation
            this.coordBaseMode = random.nextInt(4);
            switch (this.coordBaseMode)
            {
                case 0:
                case 2:
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
    					+ this.worldChunkMngr.getBiomeGenAt((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode%2==0 ? 3 : 4), this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode%2==0 ? 4 : 3), 3, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode%2==0 ? 3 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode%2==0 ? 4 : 3), 1, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wall_sign, 0, this.materialType, this.biome); Block biomeWallSignBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, this.materialType, this.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_slab, 0, this.materialType, this.biome); Block biomeWoodSlabBlock = (Block)blockObject[0]; int biomeWoodSlabMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, this.materialType, this.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), this.materialType, this.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1]; // Toward you
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 0:4), this.materialType, this.biome); Block biomeLogHorAcrossBlock = (Block)blockObject[0]; int biomeLogHorAcrossMeta = (Integer)blockObject[1]; // Perpendicular to you
        	blockObject = ModObjects.chooseModBarkBlock(biomeLogHorAlongBlock, biomeLogHorAlongMeta); Block biomeBarkOrLogHorAlongBlock = (Block)blockObject[0]; int biomeBarkOrLogHorAlongMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModBarkBlock(biomeLogHorAcrossBlock, biomeLogHorAcrossMeta); Block biomeBarkOrLogHorAcrossBlock = (Block)blockObject[0]; int biomeBarkOrLogHorAcrossMeta = (Integer)blockObject[1];
        	
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode);
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
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
        			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, -1, w, structureBB); // Foundation
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
        		this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, uw[0], -1, uw[1], structureBB); // Foundation
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
        		this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, uw[0], -1, uw[1], structureBB); // Foundation
        		this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uw[0], 0, uw[1], structureBB);
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
        			int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uw[0], uw[1]), this.getZWithOffset(uw[0], uw[1])) - 1;
            		
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, uw[0], k+2-this.boundingBox.minY, uw[1], structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(uw[0], uw[1]), k, this.getZWithOffset(uw[0], uw[1]));
                   	}
        		}
            }
        	
        	
        	// Establish wells
        	
        	// Dirt
        	this.fillWithMetadataBlocks(world, structureBB, 3, 0, 2, 5, 0, 4, biomeDirtBlock, biomeDirtMeta, biomeDirtBlock, biomeDirtMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 3, 0, 6, 5, 0, 8, biomeDirtBlock, biomeDirtMeta, biomeDirtBlock, biomeDirtMeta, false);
        	
        	// Basins
        	this.fillWithMetadataBlocks(world, structureBB, 3, 1, 2, 3, 1, 4, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 5, 1, 2, 5, 1, 4, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, false);
        	this.placeBlockAtCurrentPosition(world, biomeBarkOrLogHorAcrossBlock, biomeBarkOrLogHorAcrossMeta, 4, 1, 2, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeBarkOrLogHorAcrossBlock, biomeBarkOrLogHorAcrossMeta, 4, 1, 4, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, 4, 0, 3, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, 4, 1, 3, structureBB);
        	
        	this.fillWithMetadataBlocks(world, structureBB, 3, 1, 6, 3, 1, 8, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 5, 1, 6, 5, 1, 8, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, false);
        	this.placeBlockAtCurrentPosition(world, biomeBarkOrLogHorAcrossBlock, biomeBarkOrLogHorAcrossMeta, 4, 1, 6, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeBarkOrLogHorAcrossBlock, biomeBarkOrLogHorAcrossMeta, 4, 1, 8, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeBarkOrLogHorAcrossBlock, biomeBarkOrLogHorAcrossMeta, 4, 0, 7, structureBB);
        	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, 4, 1, 7, structureBB);
        	
        	// Torches on the corners
        	for (int[] uvwm : new int[][]{
        		{3, 2, 2, 0},
        		{5, 2, 2, 0},
        		{3, 2, 8, 0},
        		{5, 2, 8, 0},
        	})
        	{
        		world.setBlock(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2]), Blocks.torch, uvwm[3], 2);
        	}
        	
        	// Pavilion
        	this.fillWithBlocks(world, structureBB, 3, 2, 4, 5, 3, 6, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithAir(world, structureBB, 4, 2, 4, 4, 3, 6);
        	this.fillWithAir(world, structureBB, 3, 2, 5, 5, 3, 5);
        	this.fillWithMetadataBlocks(world, structureBB, 3, 4, 4, 5, 4, 6, biomeWoodSlabBlock, biomeWoodSlabMeta, biomeWoodSlabBlock, biomeWoodSlabMeta, false);
        	this.placeBlockAtCurrentPosition(world, biomePlankBlock, biomePlankMeta, 4, 4, 5, structureBB);
        	// Torch
        	for (int[] uvwm : new int[][]{
        		{4, 5, 5, 0},
        	})
        	{
        		world.setBlock(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2]), Blocks.torch, uvwm[3], 2);
        	}
        	
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
        		Block testForBanner = ModObjects.chooseModBannerBlock(); // Checks to see if supported mod banners are available. Will be null if there aren't any.
        		if (testForBanner!=null)
    			{
                    int bannerXBB = 7;
        			int bannerZBB = 7;
        			int bannerYBB = 1;
        			
        			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
        			int bannerY = this.getYWithOffset(bannerYBB);
                    int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                    
                    // Place a cobblestone foundation
                    this.fillWithMetadataBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);
                    this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                    // Clear space upward
                    this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                    
                	// Set the banner and its orientation
    				world.setBlock(bannerX, bannerY, bannerZ, testForBanner);
    				world.setBlockMetadataWithNotify(bannerX, bannerY, bannerZ, StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode, false), 2);
    				
    				// Set the tile entity
    				TileEntity tilebanner = new TileEntityBanner();
    				NBTTagCompound modifystanding = new NBTTagCompound();
    				tilebanner.writeToNBT(modifystanding);
    				modifystanding.setBoolean("IsStanding", true);
    				tilebanner.readFromNBT(modifystanding);
    				ItemStack villageBanner = ModObjects.chooseModBannerItem();
    				villageBanner.setTagInfo("BlockEntityTag", villageNBTtag.getCompoundTag("BlockEntityTag"));
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
            		
            		world.setTileEntity(bannerX, bannerY, bannerZ, tilebanner);
    			}
    		}

        	// Sign support
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 4, 3, 5, structureBB);
        	
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
        		
    			world.setBlock(signX, signY, signZ, biomeWallSignBlock, StructureVillageVN.getSignRotationMeta(3, this.coordBaseMode, true), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(signX, signY, signZ, signContents);
        		
                int signXBB2 = 5;
                int signX2 = this.getXWithOffset(signXBB2, signZBB);
                int signZ2 = this.getZWithOffset(signXBB2, signZBB);
                
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
                
    			world.setBlock(signX2, signY, signZ2, biomeWallSignBlock, StructureVillageVN.getSignRotationMeta(1, this.coordBaseMode, true), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(signX2, signY, signZ2, signContents2);
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
        			if (GeneralConfig.enableNitwit && random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(ia[0], ia[2]), this.getZWithOffset(ia[0], ia[2]));
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(entityvillager);
        		}
            }
            
            return true;
        }
    }
    

	// --- Savanna Single Well --- //
    
    public static class SavannaMeetingPoint4 extends StartVN
    {
	    public SavannaMeetingPoint4() {}
		
		public SavannaMeetingPoint4(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 8;
    		int depth = 8;
    		int height = 5;
    		
		    // Establish orientation
            this.coordBaseMode = random.nextInt(4);
            switch (this.coordBaseMode)
            {
                case 0:
                case 2:
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
    					+ this.worldChunkMngr.getBiomeGenAt((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 3, 3, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 3, 1, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wall_sign, 0, this.materialType, this.biome); Block biomeWallSignBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, this.materialType, this.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, this.materialType, this.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), this.materialType, this.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1]; // Toward you
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, this.materialType, this.biome); Block biomeWoodenStairsBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseModBarkBlock(biomeLogHorAlongBlock, biomeLogHorAlongMeta); Block biomeBarkOrLogHorAlongBlock = (Block)blockObject[0]; int biomeBarkOrLogHorAlongMeta = (Integer)blockObject[1];
        	
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode);
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
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
        		this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, -1, w, structureBB); // Foundation
        		if (u<3 || u>5 || w<3 || w>5) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(u, w), this.getYWithOffset(0), this.getZWithOffset(u, w));} // Path
        		else {this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, u, 0, w, structureBB);}
        		this.clearCurrentPositionBlocksUpwards(world, u, 1, w, structureBB); // Clear above
        	}}
        	// Set Grass blocks
        	for (int[] uw : new int[][]{
        		{0, 0},
        		{1, 10},
        	})
        	{
        		this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, uw[0], -1, uw[1], structureBB); // Foundation
        		this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uw[0], 0, uw[1], structureBB);
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
        			int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uw[0], uw[1]), this.getZWithOffset(uw[0], uw[1])) - 1;
            		
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, uw[0], k+2-this.boundingBox.minY, uw[1], structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(uw[0], uw[1]), k, this.getZWithOffset(uw[0], uw[1]));
                   	}
        		}
            }
        	
        	
        	// Establish wells
        	
        	// Dirt
        	this.fillWithMetadataBlocks(world, structureBB, 3, 0, 2, 5, 0, 6, biomeDirtBlock, biomeDirtMeta, biomeDirtBlock, biomeDirtMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 2, 0, 3, 5, 0, 5, biomeDirtBlock, biomeDirtMeta, biomeDirtBlock, biomeDirtMeta, false);
        	
        	// Basins
        	this.fillWithMetadataBlocks(world, structureBB, 3, 1, 2, 5, 1, 6, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 2, 1, 3, 6, 1, 5, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 4, 0, 3, 4, 0, 5, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 3, 0, 4, 5, 0, 4, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, biomeBarkOrLogHorAlongBlock, biomeBarkOrLogHorAlongMeta, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 3, 4, 1, 5, Blocks.flowing_water, Blocks.flowing_water, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 4, 5, 1, 4, Blocks.flowing_water, Blocks.flowing_water, false);
        	
        	// Torches on the corners
        	for (int[] uvwm : new int[][]{
        		{2, 2, 4, 0},
        		{4, 2, 2, 0},
        		{4, 2, 6, 0},
        		{6, 2, 4, 0},
        	})
        	{
        		world.setBlock(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2]), Blocks.torch, uvwm[3], 2);
        	}
        	
        	// Pavilion
        	// Supports
        	this.fillWithBlocks(world, structureBB, 3, 2, 3, 5, 3, 5, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithAir(world, structureBB, 4, 2, 3, 4, 3, 5);
        	this.fillWithAir(world, structureBB, 3, 2, 4, 5, 3, 4);
        	// Roof
        	this.placeBlockAtCurrentPosition(world, biomeWoodenStairsBlock, (new int[]{2,1,3,0})[this.coordBaseMode], 3, 4, 3, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeWoodenStairsBlock, (new int[]{2,1,3,0})[this.coordBaseMode], 4, 4, 3, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 1 : 3, 5, 4, 3, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 1 : 3, 5, 4, 4, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeWoodenStairsBlock, (new int[]{3,0,2,1})[this.coordBaseMode], 5, 4, 5, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeWoodenStairsBlock, (new int[]{3,0,2,1})[this.coordBaseMode], 4, 4, 5, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 0 : 2, 3, 4, 5, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeWoodenStairsBlock, this.coordBaseMode%2==0 ? 0 : 2, 3, 4, 4, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomePlankBlock, biomePlankMeta, 4, 4, 4, structureBB);
        	
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
        		Block testForBanner = ModObjects.chooseModBannerBlock(); // Checks to see if supported mod banners are available. Will be null if there aren't any.
        		if (testForBanner!=null)
    			{
                    int bannerXBB = 7;
        			int bannerZBB = 7;
        			int bannerYBB = 1;
        			
        			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
        			int bannerY = this.getYWithOffset(bannerYBB);
                    int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                    
                    // Place a cobblestone foundation
                    this.fillWithMetadataBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);
                    this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                    // Clear space upward
                    this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                    
                	// Set the banner and its orientation
    				world.setBlock(bannerX, bannerY, bannerZ, testForBanner);
    				world.setBlockMetadataWithNotify(bannerX, bannerY, bannerZ, StructureVillageVN.getSignRotationMeta(12, this.coordBaseMode, false), 2);
    				
    				// Set the tile entity
    				TileEntity tilebanner = new TileEntityBanner();
    				NBTTagCompound modifystanding = new NBTTagCompound();
    				tilebanner.writeToNBT(modifystanding);
    				modifystanding.setBoolean("IsStanding", true);
    				tilebanner.readFromNBT(modifystanding);
    				ItemStack villageBanner = ModObjects.chooseModBannerItem();
    				villageBanner.setTagInfo("BlockEntityTag", villageNBTtag.getCompoundTag("BlockEntityTag"));
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
            		
            		world.setTileEntity(bannerX, bannerY, bannerZ, tilebanner);
    			}
    		}
    		
    		
        	// Sign support
        	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? townColor : 4, 4, 3, 4, structureBB);
        	
        	
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
        		
    			world.setBlock(signX, signY, signZ, biomeWallSignBlock, StructureVillageVN.getSignRotationMeta(2, this.coordBaseMode, true), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(signX, signY, signZ, signContents);
        		
                int signZBB2 = 5;
                int signX2 = this.getXWithOffset(signXBB, signZBB2);
                int signZ2 = this.getZWithOffset(signXBB, signZBB2);
                
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
                
    			world.setBlock(signX2, signY, signZ2, biomeWallSignBlock, StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode, true), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(signX2, signY, signZ2, signContents2);
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
        			if (GeneralConfig.enableNitwit && random.nextInt(3)==0) {entityvillager.setProfession(5);}
        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
        			
        			int villagerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(ia[0], ia[2]), this.getZWithOffset(ia[0], ia[2]));
        			
        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)villagerY + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
                    		random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(entityvillager);
        		}
            }
            
            return true;
        }
    }
    
    
    // --- Covered Animal Pen --- //
    
    public static class SavannaAnimalPen1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public int townColor=-1;
    	public int townColor2=-1;
    	public int townColor3=-1;
    	public int townColor4=-1;
    	public int townColor5=-1;
    	public int townColor6=-1;
    	public int townColor7=-1;
    	public String namePrefix="";
    	public String nameRoot="";
    	public String nameSuffix="";
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"    F    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaAnimalPen1() {}

        public SavannaAnimalPen1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.townColor=start.townColor;
            	this.townColor2=start.townColor2;
            	this.townColor3=start.townColor3;
            	this.townColor4=start.townColor4;
            	this.townColor5=start.townColor5;
            	this.townColor6=start.townColor6;
            	this.townColor7=start.townColor7;
            	this.namePrefix=start.namePrefix;
            	this.nameRoot=start.nameRoot;
            	this.nameSuffix=start.nameSuffix;
            	this.biome=start.biome;
            }
        }
        
        public static SavannaAnimalPen1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaAnimalPen1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
            	if (this.averageGroundLevel < 0)
                {
            		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX+(this.coordBaseMode%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode);
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.villageType==null
                	|| this.materialType==null
                	|| this.townColor==-1
                	|| this.townColor2==-1
                	|| this.townColor3==-1
                	|| this.townColor4==-1
                	|| this.townColor5==-1
                	|| this.townColor6==-1
                	|| this.townColor7==-1
                	|| this.nameRoot.equals("")
            		)
            {
            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
            	
            	// Load the values of interest into memory
            	this.townColor = villageNBTtag.getInteger("townColor");
            	this.townColor2 = villageNBTtag.getInteger("townColor2");
            	this.townColor3 = villageNBTtag.getInteger("townColor3");
            	this.townColor4 = villageNBTtag.getInteger("townColor4");
            	this.townColor5 = villageNBTtag.getInteger("townColor5");
            	this.townColor6 = villageNBTtag.getInteger("townColor6");
            	this.townColor7 = villageNBTtag.getInteger("townColor7");
            	this.namePrefix = villageNBTtag.getString("namePrefix");
            	this.nameRoot = villageNBTtag.getString("nameRoot");
            	this.nameSuffix = villageNBTtag.getString("nameSuffix");
            	this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            	this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            }
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2);}
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            		{
            			// If marked with F: fill with dirt foundation
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
            		else if (world.getBlock(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w))==biomeDirtBlock)
            		{
            			// Otherwise, if dirt, add dirt foundation and then cap with grass:
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            

            // Logs (Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 0, this.materialType, this.biome); Block biomeLogVertBlock = (Block)blockObject[0]; int biomeLogVertMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Frame
            	{0,0,1, 8,0,1},  
            	{0,0,8, 8,0,8},  
            	{0,0,2, 0,0,7},  
            	{8,0,2, 8,0,7},  
            	// Trough backside
            	{2,1,7, 6,1,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	// Frame
            	{1,0,2, 7,0,7},  
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassBlock, biomeGrassMeta, biomeGrassBlock, biomeGrassMeta, false);	
            }
        	
            
            // Dirt
            for(int[] uvw : new int[][]{
            	{1,0,5}, 
            	{3,0,2}, {3,0,3}, 
            	{4,0,2}, 
            	{5,0,4}, 
            	{6,0,2}, 
            	{7,0,2}, {7,0,3}, {7,0,4}, {7,0,5}, {7,0,6}, {7,0,7}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Fences
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
            for (int[] uuvvww : new int[][]{
            	// Frame
            	{0,1,1, 8,1,1}, 
            	{0,1,8, 8,1,8}, 
            	{0,1,2, 0,1,7}, 
            	{8,1,2, 8,1,7}, 
            	// Awning supports
            	{0,2,4, 0,3,4}, {4,1,4, 4,3,4}, {8,2,4, 8,3,4}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceBlock, 0, biomeFenceBlock, 0, false);
            }
            
            
        	// Fence Gate
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence_gate, 0, this.materialType, this.biome); Block biomeFenceGateBlock = (Block)blockObject[0]; int biomeFenceGateMeta = (Integer)blockObject[1];
        	for(int[] uvw : new int[][]{
            	{4,1,1}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeFenceGateBlock, StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlock, biomeFenceGateMeta, this.coordBaseMode), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, this.materialType, this.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Trough
            	{2,1,5, 6,1,5}, {2,1,6, 2,1,6}, {6,1,6, 6,1,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{3,1,6}, 
            	{4,1,6}, 
            	{5,1,6}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Wooden slabs (Bottom)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_slab, 0, this.materialType, this.biome); Block biomeWoodSlabBottomBlock = (Block)blockObject[0]; int biomeWoodSlabBottomMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{0,2,8, 8,2,8}, 
            	{0,3,6, 8,3,6}, 
            	{0,4,4, 8,4,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomBlock, biomeWoodSlabBottomMeta, biomeWoodSlabBottomBlock, biomeWoodSlabBottomMeta, false);	
            }
            
            
            // Wooden slabs (Top)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_slab, 8, this.materialType, this.biome); Block biomeWoodSlabTopBlock = (Block)blockObject[0]; int biomeWoodSlabTopMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{0,2,7, 1,2,7}, {7,2,7, 8,2,7}, 
            	{0,3,5, 8,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopBlock, biomeWoodSlabTopMeta, biomeWoodSlabTopBlock, biomeWoodSlabTopMeta, false);	
            }
            
            
            // Double wood slab
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.double_wooden_slab, 0, this.materialType, this.biome); Block biomeWoodDoubleSlabBlock = (Block)blockObject[0]; int biomeWoodDoubleSlabMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{2,2,7, 6,2,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodDoubleSlabBlock, biomeWoodDoubleSlabMeta, biomeWoodDoubleSlabBlock, biomeWoodDoubleSlabMeta, false);	
            }
            
        	
            // Wood stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, this.materialType, this.biome); Block biomeWoodStairsBlock = (Block)blockObject[0];
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chairs
        		{4,0,0, 3}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, pathU, pathV-1, pathW, structureBB);
                // Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}

            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{5,1,2}, 
        			})
        		{
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, random, true);
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int getVillagerType (int number) {return 0;}
    }
    
    
    // --- Large Animal Pen --- //
    
    public static class SavannaAnimalPen2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public int townColor=-1;
    	public int townColor2=-1;
    	public int townColor3=-1;
    	public int townColor4=-1;
    	public int townColor5=-1;
    	public int townColor6=-1;
    	public int townColor7=-1;
    	public String namePrefix="";
    	public String nameRoot="";
    	public String nameSuffix="";
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"  FFFFFF     ",
        		"  FFFFFF   F ",
        		"  FFFFFF     ",
        		"  FFFFFFFFF  ",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"  FFFFFFFFFF ",
        		"   FFFFFFFFFF",
        		"   FFFFFFFF  ",
        		"      FFFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 4;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaAnimalPen2() {}

        public SavannaAnimalPen2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.townColor=start.townColor;
            	this.townColor2=start.townColor2;
            	this.townColor3=start.townColor3;
            	this.townColor4=start.townColor4;
            	this.townColor5=start.townColor5;
            	this.townColor6=start.townColor6;
            	this.townColor7=start.townColor7;
            	this.namePrefix=start.namePrefix;
            	this.nameRoot=start.nameRoot;
            	this.nameSuffix=start.nameSuffix;
            	this.biome=start.biome;
            }
        }
        
        public static SavannaAnimalPen2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaAnimalPen2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
            	if (this.averageGroundLevel < 0)
                {
            		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX+(this.coordBaseMode%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode);
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.villageType==null
                	|| this.materialType==null
                	|| this.townColor==-1
                	|| this.townColor2==-1
                	|| this.townColor3==-1
                	|| this.townColor4==-1
                	|| this.townColor5==-1
                	|| this.townColor6==-1
                	|| this.townColor7==-1
                	|| this.nameRoot.equals("")
            		)
            {
            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
            	
            	// Load the values of interest into memory
            	this.townColor = villageNBTtag.getInteger("townColor");
            	this.townColor2 = villageNBTtag.getInteger("townColor2");
            	this.townColor3 = villageNBTtag.getInteger("townColor3");
            	this.townColor4 = villageNBTtag.getInteger("townColor4");
            	this.townColor5 = villageNBTtag.getInteger("townColor5");
            	this.townColor6 = villageNBTtag.getInteger("townColor6");
            	this.townColor7 = villageNBTtag.getInteger("townColor7");
            	this.namePrefix = villageNBTtag.getString("namePrefix");
            	this.nameRoot = villageNBTtag.getString("nameRoot");
            	this.nameSuffix = villageNBTtag.getString("nameSuffix");
            	this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            	this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            }
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2);}
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            		{
            			// If marked with F: fill with dirt foundation
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
            		else if (world.getBlock(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w))==biomeDirtBlock)
            		{
            			// Otherwise, if dirt, add dirt foundation and then cap with grass:
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
            // Dirt with water over it
            for(int[] uvw : new int[][]{
            	{4,0,6}, {5,0,5}, {9,0,6}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uvw[0], uvw[1], uvw[2], structureBB); 
            	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, uvw[0], uvw[1]+1, uvw[2], structureBB); 
            }
            
            
            // Fences
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
            for (int[] uuvvww : new int[][]{
            	{6,2,0, 7,2,0}, 
            	{7,3,0, 7,4,0}, 
            	{8,4,0, 9,4,0}, 
            	{9,2,0, 9,3,0}, 
            	{10,2,0, 10,2,2}, 
            	{11,2,2, 11,2,4}, 
            	{12,2,4, 12,2,7}, 
            	{10,2,7, 11,2,7}, 
            	{7,2,8, 10,2,8}, 
            	{7,2,9, 7,2,11}, 
            	{2,2,11, 6,2,11}, 
            	{2,2,7, 2,2,10}, 
            	{0,2,7, 1,2,7}, 
            	{0,2,4, 0,2,6}, 
            	{1,2,4, 2,2,4}, 
            	{2,2,3, 3,2,3}, 
            	{3,2,1, 3,2,2}, 
            	{4,2,1, 6,2,1}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceBlock, 0, biomeFenceBlock, 0, false);
            }
            
            
        	// Fence Gate
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence_gate, 0, this.materialType, this.biome); Block biomeFenceGateBlock = (Block)blockObject[0]; int biomeFenceGateMeta = (Integer)blockObject[1];
        	for(int[] uvw : new int[][]{
            	{8,2,0}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeFenceGateBlock, StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlock, biomeFenceGateMeta, this.coordBaseMode), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Over the entrance
            	{7,5,0, -1},
            	{9,5,0, -1}, 
            	// Scattered about
            	{12,3,4, -1}, 
            	{7,3,8, -1}, 
            	{0,3,4, -1}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{1,2,5, 0}, {1,2,6, 0}, 
            	{2,2,6, 1}, 
            	{3,2,5, 0}, {3,2,6, 0}, {3,2,8, 0}, {3,2,9, 0}, 
            	{4,2,2, 0}, {4,2,3, 0}, {4,2,4, 0}, {4,2,5, 0}, {4,2,9, 1}, {4,2,10, 1}, 
            	{5,2,3, 0}, {5,2,8, 0}, {5,2,10, 0}, 
            	{6,2,4, 1}, {6,2,6, 0}, {6,2,8, 0}, {6,2,9, 1}, {6,2,10, 1}, 
            	{7,2,2, 0}, {7,2,3, 0}, {7,2,4, 0}, {7,2,6, 0}, 
            	{8,2,1, 0}, {8,2,2, 0}, {8,2,3, 0}, {8,2,5, 0}, {8,2,6, 0}, {8,2,7, 0}, 
            	{9,2,1, 0}, {9,2,2, 0}, {9,2,3, 1}, {9,2,5, 0}, 
            	{10,2,5, 0}, 
            	{11,2,5, 0}, {11,2,6, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 1, uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 2, uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{5,2,6},
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
            	
            	int decorHeightY;
            	
            	// Get ground level
            	if (this.decorHeightY.size()<(j+1))
            	{
            		// There are fewer stored ground levels than this decor number, so this is being generated for the first time.
            		// Add new ground level
            		decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uvw[0], uvw[2]), this.getZWithOffset(uvw[0], uvw[2]))-this.boundingBox.minY;
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor - no trough
            	ArrayList<BlueprintData> decorBlueprint = getRandomSavannaDecorBlueprint(this.materialType, this.biome, this.coordBaseMode, randomFromXYZ);
            	
            	for (BlueprintData b : decorBlueprint)
            	{
            		// Place block indicated by blueprint
            		this.placeBlockAtCurrentPosition(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos(), uvw[2]+b.getWPos(), structureBB);
            		
            		// Fill below if flagged
            		if ((b.getfillFlag()&1)!=0)
            		{
            			this.func_151554_b(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos()-1, uvw[2]+b.getWPos(), structureBB);
            		}
            		
            		// Clear above if flagged
            		if ((b.getfillFlag()&2)!=0)
            		{
            			this.clearCurrentPositionBlocksUpwards(world, uvw[0]+b.getUPos(), decorHeightY+b.getVPos()+1, uvw[2]+b.getWPos(), structureBB);
            		}            		
            	}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{8, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, pathU, pathV-1, pathW, structureBB);
                // Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}

            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{4,2,8}, 
        			{8,2,4}, 
        			})
        		{
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, random, true);
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int getVillagerType (int number) {return 0;}
    }
    
    
    // --- Medium Animal Pen --- //
    
    public static class SavannaAnimalPen3 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public int townColor=-1;
    	public int townColor2=-1;
    	public int townColor3=-1;
    	public int townColor4=-1;
    	public int townColor5=-1;
    	public int townColor6=-1;
    	public int townColor7=-1;
    	public String namePrefix="";
    	public String nameRoot="";
    	public String nameSuffix="";
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaAnimalPen3() {}

        public SavannaAnimalPen3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.townColor=start.townColor;
            	this.townColor2=start.townColor2;
            	this.townColor3=start.townColor3;
            	this.townColor4=start.townColor4;
            	this.townColor5=start.townColor5;
            	this.townColor6=start.townColor6;
            	this.townColor7=start.townColor7;
            	this.namePrefix=start.namePrefix;
            	this.nameRoot=start.nameRoot;
            	this.nameSuffix=start.nameSuffix;
            	this.biome=start.biome;
            }
        }
        
        public static SavannaAnimalPen3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaAnimalPen3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
            	if (this.averageGroundLevel < 0)
                {
            		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX+(this.coordBaseMode%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode);
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.villageType==null
                	|| this.materialType==null
                	|| this.townColor==-1
                	|| this.townColor2==-1
                	|| this.townColor3==-1
                	|| this.townColor4==-1
                	|| this.townColor5==-1
                	|| this.townColor6==-1
                	|| this.townColor7==-1
                	|| this.nameRoot.equals("")
            		)
            {
            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
            	
            	// Load the values of interest into memory
            	this.townColor = villageNBTtag.getInteger("townColor");
            	this.townColor2 = villageNBTtag.getInteger("townColor2");
            	this.townColor3 = villageNBTtag.getInteger("townColor3");
            	this.townColor4 = villageNBTtag.getInteger("townColor4");
            	this.townColor5 = villageNBTtag.getInteger("townColor5");
            	this.townColor6 = villageNBTtag.getInteger("townColor6");
            	this.townColor7 = villageNBTtag.getInteger("townColor7");
            	this.namePrefix = villageNBTtag.getString("namePrefix");
            	this.nameRoot = villageNBTtag.getString("nameRoot");
            	this.nameSuffix = villageNBTtag.getString("nameSuffix");
            	this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            	this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            }
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2);}
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            		{
            			// If marked with F: fill with dirt foundation
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
            		else if (world.getBlock(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w))==biomeDirtBlock)
            		{
            			// Otherwise, if dirt, add dirt foundation and then cap with grass:
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
            
            // Fences
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
            for (int[] uuvvww : new int[][]{
            	// Front fence
            	{0,1,0, 1,1,0}, {3,1,0, 3,1,0}, {5,1,0, 7,1,0}, 
            	// Back fence
            	{0,1,8, 7,1,8}, 
            	// Left fence
            	{0,1,1, 0,1,7}, 
            	// Right fence
            	{7,1,1, 7,1,7}, 
            	// Awning posts
            	{2,1,5, 2,3,5}, {5,1,5, 5,3,5},
            	{2,2,8, 2,3,8}, {5,2,8, 5,3,8}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceBlock, 0, biomeFenceBlock, 0, false);
            }
            
            
        	// Fence Gate
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence_gate, 0, this.materialType, this.biome); Block biomeFenceGateBlock = (Block)blockObject[0]; int biomeFenceGateMeta = (Integer)blockObject[1];
        	for(int[] uvw : new int[][]{
            	{2,1,0}, {4,1,0}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeFenceGateBlock, StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlock, biomeFenceGateMeta, this.coordBaseMode), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,2,8, -1}, {4,2,8, -1}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_slab, 0, this.materialType, this.biome); Block biomeWoodSlabBottomBlock = (Block)blockObject[0]; int biomeWoodSlabBottomMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{2,4,5, 5,4,5}, {2,4,8, 5,4,8}, 
            	{2,4,6, 2,4,7}, {5,4,6, 5,4,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomBlock, biomeWoodSlabBottomMeta, biomeWoodSlabBottomBlock, biomeWoodSlabBottomMeta, false);	
            }
            
            
            // Double wood slab
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.double_wooden_slab, 0, this.materialType, this.biome); Block biomeWoodDoubleSlabBlock = (Block)blockObject[0]; int biomeWoodDoubleSlabMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{3,4,6, 4,4,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodDoubleSlabBlock, biomeWoodDoubleSlabMeta, biomeWoodDoubleSlabBlock, biomeWoodDoubleSlabMeta, false);	
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{1,1,5, 0}, {1,1,6, 0}, 
            	{2,1,3, 0}, {2,1,4, 0}, {2,1,6, 0}, 
            	{3,1,2, 0}, {3,1,5, 0}, {3,1,6, 0}, 
            	{4,1,6, 0}, {4,1,7, 0}, 
            	{5,1,2, 0}, {5,1,4, 0}, 
            	{6,1,1, 0}, {6,1,2, 0}, {6,1,5, 0}, {6,1,7, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 1, uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 2, uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{2,1,2},
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
            	
            	int decorHeightY;
            	
            	// Get ground level
            	if (this.decorHeightY.size()<(j+1))
            	{
            		// There are fewer stored ground levels than this decor number, so this is being generated for the first time.
            		// Add new ground level
            		decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uvw[0], uvw[2]), this.getZWithOffset(uvw[0], uvw[2]))-this.boundingBox.minY;
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor - no trough
            	ArrayList<BlueprintData> decorBlueprint = getRandomSavannaDecorBlueprint(this.materialType, this.biome, this.coordBaseMode, randomFromXYZ);
            	
            	for (BlueprintData b : decorBlueprint)
            	{
            		// Place block indicated by blueprint
            		this.placeBlockAtCurrentPosition(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos(), uvw[2]+b.getWPos(), structureBB);
            		
            		// Fill below if flagged
            		if ((b.getfillFlag()&1)!=0)
            		{
            			this.func_151554_b(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos()-1, uvw[2]+b.getWPos(), structureBB);
            		}
            		
            		// Clear above if flagged
            		if ((b.getfillFlag()&2)!=0)
            		{
            			this.clearCurrentPositionBlocksUpwards(world, uvw[0]+b.getUPos(), decorHeightY+b.getVPos()+1, uvw[2]+b.getWPos(), structureBB);
            		}            		
            	}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, pathU, pathV-1, pathW, structureBB);
                // Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}

            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{4,1,3}, 
        			})
        		{
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, random, true);
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int getVillagerType (int number) {return 0;}
    }
    
    
    // --- Armorer House --- //
    
    public static class SavannaArmorer1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public int townColor=-1;
    	public int townColor2=-1;
    	public int townColor3=-1;
    	public int townColor4=-1;
    	public int townColor5=-1;
    	public int townColor6=-1;
    	public int townColor7=-1;
    	public String namePrefix="";
    	public String nameRoot="";
    	public String nameSuffix="";
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaArmorer1() {}

        public SavannaArmorer1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.townColor=start.townColor;
            	this.townColor2=start.townColor2;
            	this.townColor3=start.townColor3;
            	this.townColor4=start.townColor4;
            	this.townColor5=start.townColor5;
            	this.townColor6=start.townColor6;
            	this.townColor7=start.townColor7;
            	this.namePrefix=start.namePrefix;
            	this.nameRoot=start.nameRoot;
            	this.nameSuffix=start.nameSuffix;
            	this.biome=start.biome;
            }
        }
        
        public static SavannaArmorer1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaArmorer1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
            	if (this.averageGroundLevel < 0)
                {
            		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX+(this.coordBaseMode%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode);
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.villageType==null
                	|| this.materialType==null
                	|| this.townColor==-1
                	|| this.townColor2==-1
                	|| this.townColor3==-1
                	|| this.townColor4==-1
                	|| this.townColor5==-1
                	|| this.townColor6==-1
                	|| this.townColor7==-1
                	|| this.nameRoot.equals("")
            		)
            {
            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
            	
            	// Load the values of interest into memory
            	this.townColor = villageNBTtag.getInteger("townColor");
            	this.townColor2 = villageNBTtag.getInteger("townColor2");
            	this.townColor3 = villageNBTtag.getInteger("townColor3");
            	this.townColor4 = villageNBTtag.getInteger("townColor4");
            	this.townColor5 = villageNBTtag.getInteger("townColor5");
            	this.townColor6 = villageNBTtag.getInteger("townColor6");
            	this.townColor7 = villageNBTtag.getInteger("townColor7");
            	this.namePrefix = villageNBTtag.getString("namePrefix");
            	this.nameRoot = villageNBTtag.getString("nameRoot");
            	this.nameSuffix = villageNBTtag.getString("nameSuffix");
            	this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            	this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            }
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2);}
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            		{
            			// If marked with F: fill with dirt foundation
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
            		else if (world.getBlock(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w))==biomeDirtBlock)
            		{
            			// Otherwise, if dirt, add dirt foundation and then cap with grass:
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
            
            // Logs (Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 0, this.materialType, this.biome); Block biomeLogVertBlock = (Block)blockObject[0]; int biomeLogVertMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1, 2,3,1}, {4,1,1, 4,3,1}, 
            	// Back wall
            	{2,1,5, 4,3,5}, 
            	// Left wall
            	{1,1,2, 1,3,4}, 
            	// Right wall
            	{5,1,2, 5,3,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
            
            
            // Logs (Along)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), this.materialType, this.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Below front door
            	{3,0,1, 3,0,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
            
            
            // Logs (Across)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 0:4), this.materialType, this.biome); Block biomeLogHorAcrossBlock = (Block)blockObject[0]; int biomeLogHorAcrossMeta = (Integer)blockObject[1]; // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Above front door
            	{3,3,1, 3,3,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,2, 0}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, this.materialType, this.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,2, 4,0,2}, 
            	// Ceiling rim
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	// Top of ceiling
            	{2,5,2, 2,5,4}, {3,5,2, 3,5,3}, {4,5,2, 4,5,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
        	
            // Wood stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, this.materialType, this.biome); Block biomeWoodStairsBlock = (Block)blockObject[0];
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Lower ceiling rims
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		// Right
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, 
        		// Upper ceiling rims
        		// Front
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		// Back
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, 
        		// Left
        		{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, 
        		// Right
        		{5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,1,4, 2}, 
        		})
            {
        		blockObject = ModObjects.chooseModBlastFurnaceBlock(uvw[3], this.coordBaseMode); Block blastFurnaceBlock = (Block) blockObject[0]; int blastFurnaceMeta = (Integer) blockObject[1];
                this.placeBlockAtCurrentPosition(world, blastFurnaceBlock, 0, uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]), blastFurnaceMeta, 2);
            }
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Floor
        		{2,0,3, 4,0,3}, 
        		// Furnace
        		{2,1,4, 2,2,4}, {3,3,4, 3,5,4}, {4,1,4, 4,2,4}, 
        		})
            {
        		// Orange
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], 
        				Blocks.stained_hardened_clay, (GeneralConfig.useVillageColors ? this.townColor2 : 1),
        				Blocks.stained_hardened_clay, (GeneralConfig.useVillageColors ? this.townColor2 : 1), 
        				false);
            }
        	
        	
            // Glazed terracotta
        	Object[] tryGlazedTerracotta;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,2,4, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
           		})
        	{
        		tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(uvwoc[4], (uvwoc[3] + this.coordBaseMode + (this.coordBaseMode < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracotta != null)
            	{
        			this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, uvwoc[4], uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
            
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, 
            	{1,1,0, 0}, {1,1,1, 0}, {1,1,6, 0}, 
            	{2,1,6, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 1, uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 2, uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
            }
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(3);
            	int v = 1;
            	int w = 2+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int getVillagerType (int number) {return 0;}
    }
    
    
    // --- Butcher Shop 1 --- //
    
    public static class SavannaButchersShop1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public int townColor=-1;
    	public int townColor2=-1;
    	public int townColor3=-1;
    	public int townColor4=-1;
    	public int townColor5=-1;
    	public int townColor6=-1;
    	public int townColor7=-1;
    	public String namePrefix="";
    	public String nameRoot="";
    	public String nameSuffix="";
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 2;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaButchersShop1() {}

        public SavannaButchersShop1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.townColor=start.townColor;
            	this.townColor2=start.townColor2;
            	this.townColor3=start.townColor3;
            	this.townColor4=start.townColor4;
            	this.townColor5=start.townColor5;
            	this.townColor6=start.townColor6;
            	this.townColor7=start.townColor7;
            	this.namePrefix=start.namePrefix;
            	this.nameRoot=start.nameRoot;
            	this.nameSuffix=start.nameSuffix;
            	this.biome=start.biome;
            }
        }
        
        public static SavannaButchersShop1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaButchersShop1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
            	if (this.averageGroundLevel < 0)
                {
            		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX+(this.coordBaseMode%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode);
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.villageType==null
                	|| this.materialType==null
                	|| this.townColor==-1
                	|| this.townColor2==-1
                	|| this.townColor3==-1
                	|| this.townColor4==-1
                	|| this.townColor5==-1
                	|| this.townColor6==-1
                	|| this.townColor7==-1
                	|| this.nameRoot.equals("")
            		)
            {
            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
            	
            	// Load the values of interest into memory
            	this.townColor = villageNBTtag.getInteger("townColor");
            	this.townColor2 = villageNBTtag.getInteger("townColor2");
            	this.townColor3 = villageNBTtag.getInteger("townColor3");
            	this.townColor4 = villageNBTtag.getInteger("townColor4");
            	this.townColor5 = villageNBTtag.getInteger("townColor5");
            	this.townColor6 = villageNBTtag.getInteger("townColor6");
            	this.townColor7 = villageNBTtag.getInteger("townColor7");
            	this.namePrefix = villageNBTtag.getString("namePrefix");
            	this.nameRoot = villageNBTtag.getString("nameRoot");
            	this.nameSuffix = villageNBTtag.getString("nameSuffix");
            	this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(villageNBTtag.getString("villageType"), FunctionsVN.VillageType.getVillageTypeFromBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            	this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(villageNBTtag.getString("materialType"), FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, (this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));
            }
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2);}
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            		{
            			// If marked with F: fill with dirt foundation
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
            		else if (world.getBlock(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w))==biomeDirtBlock)
            		{
            			// Otherwise, if dirt, add dirt foundation and then cap with grass:
            			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
            
            // Logs (Across)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 0:4), this.materialType, this.biome); Block biomeLogHorAcrossBlock = (Block)blockObject[0]; int biomeLogHorAcrossMeta = (Integer)blockObject[1]; // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Below front door
            	{4,1,1, 4,1,1}, 
            	// Above front door
            	{4,4,1, 4,4,1}, 
            	// Front right window
            	{7,4,3, 7,4,3}, {7,2,3, 7,2,3}, 
            	// Back windows
            	{3,4,6, 3,4,6}, {3,2,6, 3,2,6}, 
            	{7,4,6, 7,4,6}, {7,2,6, 7,2,6}, 
            	// Above back door
            	{5,4,6, 5,4,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front right window
        		{7,3,3}, 
        		// Back windows
        		{3,3,6}, {7,3,6}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Torches, part 1
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{4,4,2, 0}, {5,4,5, 2}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 0, this.materialType, this.biome); Block biomeLogVertBlock = (Block)blockObject[0]; int biomeLogVertMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{3,1,1, 3,4,1}, {5,1,1, 5,4,2}, 
            	// Right wing front wall
            	{6,1,3, 6,4,3}, {7,1,3, 7,1,3}, {8,1,3, 8,4,3}, 
            	// Right wing right wall
            	{9,1,4, 9,4,5}, 
            	// Back wall
            	{2,1,6, 2,4,6}, {3,1,6, 3,1,6}, {4,1,6, 4,4,6},
            	{5,1,6, 5,1,6}, 
            	{6,1,6, 6,4,6}, {7,1,6, 7,1,6}, {8,1,6, 8,4,6}, 
            	// Left wall
            	{1,1,4, 1,4,5}, {2,1,2, 2,4,3}, 
            	// Backyard
            	{2,1,7, 2,1,10}, {3,1,10, 6,1,10}, {7,1,7, 7,1,10}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	// Frame
            	{3,1,7, 6,1,9},  
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassBlock, biomeGrassMeta, biomeGrassBlock, biomeGrassMeta, false);	
            }
            
            
            // Fences
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
            for (int[] uuvvww : new int[][]{
            	// Backyard
            	{2,2,7, 2,2,10}, {3,2,10, 6,2,10}, {7,2,7, 7,2,10}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceBlock, 0, biomeFenceBlock, 0, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Out front
            	{3,3,0, 2}, {5,3,0, 2}, 
            	// Yard
            	{2,3,10, -1}, {7,3,10, -1}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, this.materialType, this.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Ceiling rim
            	{2,5,1, 5,5,1}, {5,5,2, 6,5,2}, 
            	{6,5,3, 9,5,3}, {9,5,4, 9,5,6}, 
            	{1,5,6, 8,5,6}, {1,5,3, 1,5,5}, 
            	{2,5,2, 2,5,3}, 
            	// Ceiling
            	{3,6,2, 4,6,2}, {3,6,3, 5,6,3}, {3,6,4, 8,6,4}, {2,6,5, 8,6,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
        	
            // Wood stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, this.materialType, this.biome); Block biomeWoodStairsBlock = (Block)blockObject[0];
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front Steps
        		{4,1,0, 3}, {3,1,0, 0}, {5,1,0, 1}, 
        		// Lower roof rim
        		{1,5,0, 3}, {2,5,0, 3}, {3,5,0, 3}, {4,5,0, 3}, {5,5,0, 3}, {6,5,0, 3}, 
        		{6,5,1, 1},
        		{7,5,2, 3}, {8,5,2, 3}, {9,5,2, 3}, 
        		{10,5,3, 1}, {10,5,4, 1}, {10,5,5, 1}, {10,5,6, 1}, 
        		{1,5,4, 2}, {2,5,4, 2}, {3,5,4, 2}, {4,5,4, 2}, {5,5,4, 2}, {6,5,4, 2}, {7,5,4, 2}, {8,5,4, 2}, {9,5,4, 2}, 
        		{1,5,1, 0}, {1,5,2, 0}, {0,5,2, 3}, 
        		{0,5,3, 0}, {0,5,4, 0}, {0,5,5, 0}, {0,5,6, 0}, 
        		{1,5,7, 2}, {2,5,7, 2}, {3,5,7, 2}, {4,5,7, 2}, {5,5,7, 2}, {6,5,7, 2}, {7,5,7, 2}, {8,5,7, 2}, {9,5,7, 2}, 
        		// Higher roof rim
        		{2,6,1, 3}, {3,6,1, 3}, {4,6,1, 3}, {5,6,1, 1}, 
        		{5,6,2, 3}, {6,6,2, 1}, 
        		{6,6,3, 3}, {7,6,3, 3}, {8,6,3, 3}, 
        		{9,6,3, 1}, {9,6,4, 1}, {9,6,5, 1}, {9,6,6, 1}, 
        		{8,6,6, 2}, {7,6,6, 2}, {6,6,6, 2}, {5,6,6, 2}, {4,6,6, 2}, {3,6,6, 2}, {2,6,6, 2}, {1,6,6, 2}, 
        		{1,6,5, 0}, {1,6,4, 0}, {1,6,3, 3}, 
        		{2,6,3, 0}, {2,6,2, 0}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            

            // Stained terracotta
            for(int[] uvwc : new int[][]{
            	// Checkered floor
            	{2,1,4, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	{2,1,5, GeneralConfig.useVillageColors? this.townColor2 : 1}, // Orange
            	{3,1,5, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	{3,1,4, GeneralConfig.useVillageColors? this.townColor2 : 1}, // Orange
            	{3,1,3, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	{3,1,2, GeneralConfig.useVillageColors? this.townColor2 : 1}, // Orange
            	{4,1,5, GeneralConfig.useVillageColors? this.townColor2 : 1}, // Orange
            	{4,1,4, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	{4,1,3, GeneralConfig.useVillageColors? this.townColor2 : 1}, // Orange
            	{4,1,2, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	{5,1,3, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	{5,1,4, GeneralConfig.useVillageColors? this.townColor2 : 1}, // Orange
            	{5,1,5, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	{6,1,5, GeneralConfig.useVillageColors? this.townColor2 : 1}, // Orange
            	{6,1,4, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	{8,1,5, GeneralConfig.useVillageColors? this.townColor2 : 1}, // Orange
            	{8,1,4, GeneralConfig.useVillageColors? this.townColor  : 4}, // Yellow
            	})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, uvwc[3], uvwc[0], uvwc[1], uvwc[2], structureBB);
            }
            
            
            // Smoker
        	blockObject = ModObjects.chooseModSmokerBlock(3, this.coordBaseMode); Block smokerBlock = (Block) blockObject[0];
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,4, 1}
            	})
            {
                this.placeBlockAtCurrentPosition(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]), StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode), 2);
            }
            
        	
        	// Cobblestone
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, this.materialType, this.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Counter
            	{2,2,5, 2,2,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            
            
            // Cobblestone Wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone_wall, 0, this.materialType, this.biome); Block biomeCobblestoneWallBlock = (Block)blockObject[0]; int biomeCobblestoneWallMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Chimney
            	{2,3,4, 2,7,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, false);	
            }
            
            
            // Smooth Stone Slab (Upper)
            for(int[] uuvvww : new int[][]{
            	// In front of counter
            	{7,1,4, 7,1,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.stone_slab, 8, Blocks.stone_slab, 8, false);
            }
            
            
            // Double Smooth Stone Slab Counter
        	for (int[] uuvvwwo : new int[][]{ 
        		{8,2,4, 8,2,5}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.double_stone_slab, 0, Blocks.double_stone_slab, 0, false);
            }
            
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,1, 0, 1, 1}, 
            	{5,2,6, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,2, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,8, 0}, {0,1,10, 0}, 
            	{1,1,2, 0}, {1,1,3, 0}, {1,1,6, 0}, {1,1,7, 0}, {1,1,8, 0}, {1,1,9, 0}, 
            	{2,1,0, 0}, {2,1,1, 0}, 
            	{6,1,1, 0}, 
            	{8,1,1, 0}, {8,1,7, 0}, {8,1,9, 0}, {8,1,10, 0}, 
            	{9,1,1, 0}, {9,1,6, 0}, 
            	{10,1,1, 0}, {10,1,3, 0}, {10,1,4, 0}, {10,1,5, 0}, {10,1,7, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 1, uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 2, uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, pathU, pathV-1, pathW, structureBB);
                // Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{5, 2, 8},
        			})
        		{
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, random, false);
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
            	// Villager
            	int u = 3;
            	int v = 2;
            	int w = 5;
            	
            	int s = random.nextInt(15);
            	
            	if (s>=11)
            	{
            		u=6 + s%2==0? 0:1;
            		w=4 + s>12? 1:0;
            	}
            	else if (s>=8)
            	{
            		u=5;
            		w=s-3;
            	}
            	else
            	{
            		u=3 + s>3? 1:0;
            		w=2 + s%4;
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int getVillagerType (int number) {return 0;}
    }
    
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
    public static ArrayList<BlueprintData> getRandomSavannaDecorBlueprint(MaterialType materialType, BiomeGenBase biome, int coordBaseMode, Random random)
	{
		int decorCount = 1;
		return getSavannaDecorBlueprint(random.nextInt(decorCount), materialType, biome, coordBaseMode, random);
	}
    public static ArrayList<BlueprintData> getSavannaDecorBlueprint(int decorType, MaterialType materialType, BiomeGenBase biome, int coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		Object[] blockObject;
    	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, materialType, biome); Block biomeFenceBlock = (Block)blockObject[0];
    	
        switch (decorType)
        {
    	case 0: // Torch on fence
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeFenceBlock);
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, Blocks.torch, 0);
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
