package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astrotibs.villagenames.banner.TileEntityBanner;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.utility.BlockPos;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.ChestGenHooks;

public class TaigaStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Simple grass patch with two structures --- //
	
    public static class TaigaMeetingPoint1 extends StartVN
    {
    	public TaigaMeetingPoint1() {}
    	
    	public TaigaMeetingPoint1(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
    		int width = 11;
    		int depth = 6;
    		int height = 2;
    		
		    // Establish orientation
            this.coordBaseMode = random.nextInt(4);
            switch (this.coordBaseMode)
            {
	            case 0: // North
	            case 2: // South
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + width, 64+height, posZ + depth);
                    break;
                default: // 1: East; 3: West
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
    					+ this.worldChunkMngr.getBiomeGenAt((this.boundingBox.minX+this.boundingBox.maxX)/2, (this.boundingBox.minZ+this.boundingBox.maxZ)/2).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode
    					);
    		}
    		
			// Southward
			if (this.coordBaseMode!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode%2==1 ? 2 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());}
			// Westward
			if (this.coordBaseMode==3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 4, 1, this.getComponentType());}
			// Northward
			if (this.coordBaseMode==0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());}
			// Eastward
			if (this.coordBaseMode!=3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode%2==0 ? 2 : 4), 3, this.getComponentType());}
			
			
			// Attach non-road structures
			
			// Structure 1, left-hand side, near the bell
			if (this.coordBaseMode==0) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(4)-1), 1, this.getComponentType());}
			if (this.coordBaseMode==1) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(4)+1), this.boundingBox.minY, this.boundingBox.minZ+(-1), 2, this.getComponentType());}
			if (this.coordBaseMode==2) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(4)-1), 1, this.getComponentType());}
			if (this.coordBaseMode==3) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(4)), this.boundingBox.minY, this.boundingBox.minZ+(-1), 2, this.getComponentType());}
			
			// Structure 2, back side, along the longer side
			if (this.coordBaseMode==0) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(6)+3), this.boundingBox.minY, this.boundingBox.maxZ+(1), 0, this.getComponentType());}
			if (this.coordBaseMode==1) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), 1, this.getComponentType());}
			if (this.coordBaseMode==2) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), 2, this.getComponentType());}
			if (this.coordBaseMode==3) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.maxX+(1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), 3, this.getComponentType());}
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
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, this.materialType, this.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, this.materialType, this.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign, 0, this.materialType, this.biome); Block biomeStandingSignBlock = (Block)blockObject[0];
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode);
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(2, 3),
        			this.getYWithOffset(2),
        			this.getZWithOffset(2, 3));
        	this.townColor = villageNBTtag.getInteger("townColor");
        	this.townColor2 = villageNBTtag.getInteger("townColor2");
        	// Generate additional colors to be used in the town
        	if (this.townColorA==-1) {this.townColorA = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2}, random, false);}
        	if (this.townColorB==-1) {this.townColorB = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA}, random, false);}
        	if (this.townColorC==-1) {this.townColorC = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA, this.townColorB}, random, false);}
        	
    		this.namePrefix = villageNBTtag.getString("namePrefix");
    		this.nameRoot = villageNBTtag.getString("nameRoot");
    		this.nameSuffix = villageNBTtag.getString("nameSuffix");
        	
        	// Top layer is grass path
        	for (int i=0; i<=10; i++)
        	{
        		for (int j=1; j<=6; j++)
            	{
        			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, i, 0, j, structureBB);
        			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, i, -1, j, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, i, 1, j, structureBB);
        			// Set grass path after fill so that the area is level
        			StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, j), this.getYWithOffset(0), this.getZWithOffset(i, j));
            	}
        	}
            
        	// Set grass
        	for (int[] offset_xy : new int[][]{
        		{0, 2}, 
        		{1, 3}, 
        		{2, 1}, {2, 2}, {2, 4}, {2, 6},
        		{3, 1}, {3, 3}, {3, 5}, 
        		{5, 2}, {5, 5}, 
        		{6, 3}, 
        		{8, 1}, {8, 3}, {8, 5}, 
        		{9, 2}, {9, 4}, 
        		{10, 3}, {10, 5}, {10, 6}, 
        		{10, 3}, 
        	})
        	{
        		this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, offset_xy[0], 0, offset_xy[1], structureBB);
        	}
        	
        	// Nodules at the end
        	for (int i=4; i<=6; i++) {for (int j=0; j<=0; j++) {
        			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, i, 0, j, structureBB);
        			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, i, -1, j, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, i, 1, j, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, j), this.getYWithOffset(0), this.getZWithOffset(i, j));
            }}
        	for (int i=11; i<=11; i++) {for (int j=2; j<=4; j++) {
        			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, i, 0, j, structureBB);
        			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, i, -1, j, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, i, 1, j, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, j), this.getYWithOffset(0), this.getZWithOffset(i, j));
            }}
        	
        	// Single wood platform for the "bell"
        	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, 2, 0, 3, structureBB);
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		Object[] tryConcrete = ModObjects.chooseModConcrete(townColor);
            	Block concreteBlock = Blocks.stained_hardened_clay; int concreteMeta = townColor;
            	if (tryConcrete != null) {concreteBlock = (Block) tryConcrete[0]; concreteMeta = (Integer) tryConcrete[1];}
            	
            	this.placeBlockAtCurrentPosition(world, concreteBlock, concreteMeta, 2, 1, 3, structureBB);
        	}
        	else
        	{
        		this.placeBlockAtCurrentPosition(world, biomePlankBlock, biomePlankMeta, 2, 1, 3, structureBB);
        	}
        	// Wood trapdoor hatching
        	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, this.coordBaseMode%2==0 ? 6 : 4, 1, 1, 3, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, this.coordBaseMode%2==0 ? 7 : 5, 3, 1, 3, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, (new int[]{4, 7, 5, 6})[this.coordBaseMode], 2, 1, 2, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, (new int[]{5, 6, 4, 7})[this.coordBaseMode], 2, 1, 4, structureBB);
        	
        	// Sign
            int signXBB = 2;
			int signYBB = 2;
			int signZBB = 3;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlock(signX, signY, signZ, biomeStandingSignBlock, StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode, false), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(signX, signY, signZ, signContents);
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
        		Block testForBanner = ModObjects.chooseModBannerBlock(); // Checks to see if supported mod banners are available. Will be null if there aren't any.
        		if (testForBanner!=null)
    			{
                    int bannerXBB = 5;
        			int bannerZBB = 4;
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
        			{2, 1, 5, -1, 0},
        			{4, 1, 2, -1, 0},
        			{9, 1, 4, -1, 0},
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
    
    
    
	// --- Taiga Well --- //
	
	public static class TaigaMeetingPoint2 extends StartVN
    {
    	public TaigaMeetingPoint2() {}
    	
    	public TaigaMeetingPoint2(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);

    		int width = 8;
    		int depth = 8;
    		int height = 6;
    		
		    // Establish orientation
            this.coordBaseMode = random.nextInt(4);
            switch (this.coordBaseMode)
            {
	            case 0: // North
	            case 2: // South
                    this.boundingBox = new StructureBoundingBox(posX, 64+1, posZ, posX + width, 64+1+height, posZ + depth);
                    break;
                default: // 1: East; 3: West
                    this.boundingBox = new StructureBoundingBox(posX, 64+1, posZ, posX + depth, 64+1+height, posZ + width);
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
    		
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 3, 1, this.getComponentType());
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 3, 3, this.getComponentType());
		}
    	
		/*
		 * Construct the structure
		 */
    	@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, this.materialType, this.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, this.materialType, this.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, this.materialType, this.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.mossy_cobblestone, 0, this.materialType, this.biome); Block biomeMossyCobblestoneBlock = (Block)blockObject[0]; int biomeMossyCobblestoneMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wall_sign, 0, this.materialType, this.biome); Block biomeWallSignBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), this.materialType, this.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, this.materialType, this.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode);
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1 -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(8, 1),
        			this.getYWithOffset(1),
        			this.getZWithOffset(8, 1));
        	this.townColor = villageNBTtag.getInteger("townColor");
        	this.townColor2 = villageNBTtag.getInteger("townColor2");
        	// Generate additional colors to be used in the town
        	if (this.townColorA==-1) {this.townColorA = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2}, random, false);}
        	if (this.townColorB==-1) {this.townColorB = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA}, random, false);}
        	if (this.townColorC==-1) {this.townColorC = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA, this.townColorB}, random, false);}
        	
    		this.namePrefix = villageNBTtag.getString("namePrefix");
    		this.nameRoot = villageNBTtag.getString("nameRoot");
    		this.nameSuffix = villageNBTtag.getString("nameSuffix");
        	
        	
            // Encircle the well with path
        	StructureVillagePieces.Start startPiece_reflected = ReflectionHelper.getPrivateValue(StructureVillagePieces.Village.class, this, new String[]{"startPiece"});
        	for (int i = 1; i <= 7; ++i)
            {
                for (int j = 1; j <= 7; ++j)
                {
                    if (j == 1 || j == 7 || i == 1 || i == 7)
                    {
                    	// Gets ground level, so long as it's not leaves or other foliage
                        int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(i, j), this.getZWithOffset(i, j)) - 1;
                        if (k > -1)
                        {
                            StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j));
                        	this.clearCurrentPositionBlocksUpwards(world, i, k+2-this.boundingBox.minY, j, structureBB);
                       	}
                    }
                }
            }
            // Add path nodules at the end
            for (int i : new int[]{3,4,5})
            {
            	for (int j : new int[]{0,8})
            	{
            		int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(i, j), this.getZWithOffset(i, j)) - 1;
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, i, k+2-this.boundingBox.minY, j, structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j));
                   	}
                    
                    k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(j, i), this.getZWithOffset(j, i)) - 1;
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, j, k+2-this.boundingBox.minY, i, structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this, 0, this.getXWithOffset(j, i), k, this.getZWithOffset(j, i));
                   	}
            	}
            }
        	
            // Add odds and ends into the ground
            for (int[] uw : new int[][]{
            	{0, -1, 5},
            	{6, -1, 1},
            	{7, -1, 1},
            	{7, -1, 6},
            	{6, -1, 7},
            	{6, 0, 0},
            	{8, 0, 6},
            	{3, -1, 1},
            	{7, -1, 5},
            })
            {
            	int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uw[0], uw[2]), this.getZWithOffset(uw[0], uw[2])) + uw[1] - 1;
            	if (k > -1)
                {
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uw[0], k - this.boundingBox.minY, uw[2], structureBB);
               	}
            }
            
            // One block of water - probably a mistake but whatever
            /*
            int kw = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(4, 7), this.getZWithOffset(4, 7)) - 2;
            if (kw > -1)
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, 4, kw - this.boundingBox.minY, 7, structureBB);
            }
            */
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0, 2, 7},
            	{8, 2, 0},
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
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getRandomTaigaDecorBlueprint(this, this.coordBaseMode, randomFromXYZ);
            	
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
            
        	
        	// Create well
        	this.fillWithMetadataBlocks(world, structureBB, 2, 1, 2, 6, 2, 6, biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);
        	// Bottom is dirt with inset mossy cobblestone blocks
        	this.fillWithMetadataBlocks(world, structureBB, 2, 0, 2, 6, 0, 6, biomeDirtBlock, biomeDirtMeta, biomeDirtBlock, biomeDirtMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 3, 0, 3, 5, 0, 5, biomeMossyCobblestoneBlock, biomeMossyCobblestoneMeta, biomeMossyCobblestoneBlock, biomeMossyCobblestoneMeta, false);
        	
        	// Clear above and set foundation
        	for (int u=2; u<=6; u++) {for (int w=2; w<=6; w++) {
        			this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, -1, w, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, u, 3, w, structureBB);
            }}
        	
        	// Fill basin with water
        	this.fillWithBlocks(world, structureBB, 3, 1, 3, 5, 2, 5, Blocks.flowing_water, Blocks.flowing_water, false);
        	
        	// Place a couple specific stone/cobblestone blocks in the well
        	this.placeBlockAtCurrentPosition(world, biomeMossyCobblestoneBlock, biomeMossyCobblestoneMeta, 3, 2, 2, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeMossyCobblestoneBlock, biomeMossyCobblestoneMeta, 2, 2, 4, structureBB);
        	this.placeBlockAtCurrentPosition(world, biomeCobblestoneBlock, biomeCobblestoneMeta, 3, 0, 3, structureBB);
        	
        	// Add fence posts
        	this.fillWithBlocks(world, structureBB, 2, 3, 2, 6, 5, 6, biomeFenceBlock, biomeFenceBlock, false);
        	this.fillWithAir(world, structureBB, 2, 3, 3, 6, 4, 5);
        	this.fillWithAir(world, structureBB, 3, 3, 2, 5, 4, 6);
        	this.fillWithAir(world, structureBB, 3, 5, 3, 5, 5, 5);
        	// Add log roof
        	this.fillWithMetadataBlocks(world, structureBB, 3, 6, 2, 5, 6, 6, biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 2, 5, 2, 2, 5, 6, biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);
        	this.fillWithMetadataBlocks(world, structureBB, 6, 5, 2, 6, 5, 6, biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);
        	// Add torches
        	for (int[] uvwm : new int[][]{
        		{2, 5, 1, 0},
        		{6, 5, 1, 0},
        		// Banner side
        		{2, 5, 7, 0},
        		{6, 5, 7, 0},
        	})
        	{
        		world.setBlock(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2]), Blocks.torch, uvwm[3], 2);
        	}
            
            
            // Colored block where bell used to be
            if (GeneralConfig.decorateVillageCenter)
            {
            	int metaBase = ((int)world.getSeed()%4+this.coordBaseMode)%4; // Procedural based on world seed and base mode
            	
            	BlockPos uvw = new BlockPos(4, 5, 4); // Starting position of the block cluster. Use lowest X, Z.
            	int metaCycle = (metaBase+Math.abs(this.getXWithOffset(uvw.getX(), uvw.getZ())%2 - (this.getZWithOffset(uvw.getX(), uvw.getZ())%2)*3) + uvw.getY())%4; // Procedural based on block X, Y, Z 
            	Object[] tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(townColor, metaCycle);
            	
            	if (tryGlazedTerracotta != null)
            	{
            		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            	}
            	else
            	{
            		this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, townColor, 4, 5, 4, structureBB);
            	}
            }
            else
            {
            	this.placeBlockAtCurrentPosition(world, biomePlankBlock, biomePlankMeta, 4, 5, 4, structureBB);
            }
            
        	// Signs
            int signXBB = 4;
			int signYBB = 5;
			int signZBB = 5;
			int signZBB2 = 3;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signX2 = this.getXWithOffset(signXBB, signZBB2);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
            int signZ2 = this.getZWithOffset(signXBB, signZBB2);
    		
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlock(signX, signY, signZ, biomeWallSignBlock, StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode, true), 2); // Facing away from you
			world.setTileEntity(signX, signY, signZ, signContents);
    		
            // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
    		TileEntitySign signContents2 = new TileEntitySign();
    		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
			
			world.setBlock(signX2, signY, signZ2, biomeWallSignBlock, StructureVillageVN.getSignRotationMeta(2, this.coordBaseMode, true), 2); // Facing toward you
			world.setTileEntity(signX2, signY, signZ2, signContents2);
            
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
        		Block testForBanner = ModObjects.chooseModBannerBlock(); // Checks to see if supported mod banners are available. Will be null if there aren't any.
        		if (testForBanner!=null)
    			{
        			int bannerXBB = 7;
        			int bannerZBB = 8;
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
                    
                    // Place a foundation
                    this.fillWithMetadataBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);
                    this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                    // Clear space upward
                    this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                    
                	// Set the banner and its orientation
    				world.setBlock(bannerX, bannerY, bannerZ, testForBanner);
    				world.setBlockMetadataWithNotify(bannerX, bannerY, bannerZ, StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode, false), 2);
    				
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
    		else
    		{
				int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(7, 8), this.getZWithOffset(7, 8)) - 1;
				if (k > -1)
				{
					this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, 7, k-1-this.boundingBox.minY, 8, structureBB);
				    this.clearCurrentPositionBlocksUpwards(world, 7, k-this.boundingBox.minY, 8, structureBB);
				}
    		}
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
        		for (int[] ia : new int[][]{
        			{7, 2, 1, -1, 0},
        			{8, 2, 3, -1, 0},
        			{0, 2, 5, -1, 0},
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
    
    
    // --- Animal Pen --- //
    
    public static class TaigaAnimalPen1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"             ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		"      F      ",
        };
    	// Here are values to assign to the bounding box
    	private static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	private static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaAnimalPen1() {}

        public TaigaAnimalPen1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaAnimalPen1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaAnimalPen1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.func_151554_b(world, biomeDirtBlock, biomeDirtMeta, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
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
            
        	// Grass
        	for(int[] uuvvww : new int[][]{
            	{1,0,1, 11,0,6},  
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.grass, 0, Blocks.grass, 0, false);	
            }
        	
            
            // Fence
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, start.materialType, start.biome); Block biomeFenceBlock = (Block)blockObject[0]; 
            for(int[] uuvvww : new int[][]{
            	{1,1,1, 5,1,1}, {7,1,1, 11,1,1},
            	{1,1,6, 11,1,6}, 
            	{1,1,1, 1,1,6}, {11,1,1, 11,1,6}, 
            	// Posts
            	{1,2,1, 1,2,1}, {1,2,6, 1,2,6}, {11,2,1, 11,2,1}, {11,2,6, 11,2,6}, 
            	{5,2,1, 5,3,1}, {7,2,1, 7,3,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceBlock, 0, biomeFenceBlock, 0, false);	
            }
            
            
        	// Fence Gate
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence_gate, 0, start.materialType, start.biome); Block biomeFenceGateBlock = (Block)blockObject[0]; int biomeFenceGateMeta = (Integer)blockObject[1];
        	for(int[] uvw : new int[][]{
            	{6,1,1}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeFenceGateBlock, StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlock, biomeFenceGateMeta, this.coordBaseMode), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, start.materialType, start.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{6,3,1, 6,3,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{6,4,1, -1}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Rim
            	{1,0,0, 5,0,0, 2}, {7,0,0, 11,0,0, 2}, 
            	{1,0,7, 11,0,7, 0}, 
            	{0,0,1, 0,0,6, 3}, 
            	{12,0,1, 12,0,6, 1},
            	// Trough
            	{3,1,3, 4,1,3, 2}, 
            	{2,1,4, 2,1,4, 3}, 
            	{3,1,5, 4,1,5, 0}, 
            	{5,1,4, 5,1,4, 1}, 
            	// Entrance
            	{6,3,0, 6,3,0, 2}, 
            	{6,3,2, 6,3,2, 0}, 
            	
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
        	
        	
            // Wood stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, start.materialType, start.biome); Block biomeWoodStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		{6,0,0, 6,0,0, 3}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }

            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{6, 1, 3}, 
        			{8, 1, 4}, 
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
    
    
    // --- Armorer Station --- //
    
    public static class TaigaArmorer2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"  FFFFF",
        		" FFFFF ",
        		"FFFFFF ",
        		"FFFFFFF",
        		"FFFFFFF",
        		" FFFFF ",
        		"F FFF F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaArmorer2() {}

        public TaigaArmorer2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaArmorer2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaArmorer2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
            
        	// Grass Path
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModPathBlock(), 0, start.materialType, start.biome); Block grassPathBlock = (Block) blockObject[0]; int grassPathMeta = (Integer) blockObject[1]; 
        	for(int[] uvw : new int[][]{
            	{0,0,2}, 
            	{1,0,1}, {1,0,3}, {1,0,5}, 
            	{2,0,0}, {2,0,1}, 
            	{3,0,0}, {3,0,6}, 
            	{4,0,0}, {4,0,1}, 
            	{5,0,2}, {5,0,3}, {5,0,5}, 
            	{6,0,0}, {6,0,2}, {6,0,6}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, grassPathBlock, grassPathMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	blockObject = ModObjects.chooseModBlastFurnaceBlock(2, this.coordBaseMode); Block blastFurnaceBlock = (Block) blockObject[0]; int blastFurnaceMeta = (Integer) blockObject[1];
        	for (int[] uvw : new int[][]{
        		{3,1,4}
        		})
            {
                this.placeBlockAtCurrentPosition(world, blastFurnaceBlock, 0, uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]), blastFurnaceMeta, 2);
            }
        	
        	
            
            // Cobblestone Wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone_wall, 0, start.materialType, start.biome); Block biomeCobblestoneWallBlock = (Block)blockObject[0]; int biomeCobblestoneWallMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{0,1,3, 0,3,3}, 
            	{3,2,4, 3,7,4}, 
            	{6,1,3, 6,3,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, false);	
            }
            
            
            // Logs (Across)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 0:4), start.materialType, start.biome); Block biomeLogHorAcrossBlock = (Block)blockObject[0]; int biomeLogHorAcrossMeta = (Integer)blockObject[1]; // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{1,4,2, 5,4,2}, 
            	{0,4,3, 6,4,3}, 
            	{1,4,4, 5,4,4}, 
            	{1,5,3, 5,5,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
            
        	
        	// Cobblestone
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, start.materialType, start.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
        	for(int[] uvw : new int[][]{
            	{2,0,2}, 
            	{3,0,2}, {3,0,3}, 
            	{4,0,2}, 
            	// Chimney
            	{3,4,4}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeCobblestoneBlock, biomeCobblestoneMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,4,1, 2}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Campfire
        	blockObject = ModObjects.chooseModCampfireBlock(random.nextInt(4), coordBaseMode); Block campfireBlock = (Block) blockObject[0]; int campfireMeta = (Integer) blockObject[1];
            for(int[] uvw : new int[][]{
            	{5,1,1}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, campfireBlock, campfireMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Cobblestone stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, start.materialType, start.biome); Block biomeCobblestoneStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,1,3, 2,1,3, 3}, {4,1,3, 4,1,3, 3}, 
        		{2,1,4, 2,1,4, 0}, {4,1,4, 4,1,4, 1}, 
        		{2,1,5, 4,1,5, 2}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,4, 1}, 
            	{2,1,6, 1}, 
            	{5,1,6, 0}, 
            })
            {
    			if (uwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 2, uwg[0], uwg[1], uwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 3, uwg[0], uwg[1], uwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uwg[0], uwg[1]+1, uwg[2], structureBB);
    			}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0, 1, 0},
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
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getTaigaDecorBlueprint(1+randomFromXYZ.nextInt(6), this.start, this.coordBaseMode, randomFromXYZ);
            	
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(3);
            	int v = 1;
            	int w = 0+random.nextInt(3);
            	
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
        protected int getVillagerType (int number) {return 3;}
    }
    
    
    // --- Armorer House --- //
    
    public static class TaigaArmorerHouse1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"        ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" F  F  F",
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
    	
        public TaigaArmorerHouse1() {}

        public TaigaArmorerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaArmorerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaArmorerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, start.materialType, start.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{3,0,2, 4,0,2}, 
            	{3,0,7, 4,0,7}, 
            	{2,0,2, 2,0,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
            
            // Cobblestone
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, start.materialType, start.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,1, 1,2,8}, 
            	// Right wall
            	{5,1,1, 5,2,8}, 
            	// Front wall
            	{2,1,1, 4,3,1}, {3,4,1, 3,4,1}, 
            	// Back wall
            	{2,1,8, 4,3,8}, {3,4,8, 3,4,8}, 
            	// Front entrance
            	{3,0,1, 3,0,1}, 
            	// Floor
            	{3,0,3, 3,0,6}, 
            	// Furnace
            	{4,1,3, 4,1,3}, {4,1,6, 4,1,6}, {4,2,4, 4,3,5}, {4,4,5, 4,4,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{4,1,4, 3}, 
        		{4,1,5, 3}, 
        		})
            {
        		blockObject = ModObjects.chooseModBlastFurnaceBlock(uvw[3], this.coordBaseMode); Block blastFurnaceBlock = (Block) blockObject[0]; int blastFurnaceMeta = (Integer) blockObject[1];
                this.placeBlockAtCurrentPosition(world, blastFurnaceBlock, 0, uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]), blastFurnaceMeta, 2);
            }
        	
            
            // Cobblestone Wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone_wall, 0, start.materialType, start.biome); Block biomeCobblestoneWallBlock = (Block)blockObject[0]; int biomeCobblestoneWallMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{4,5,5, 4,6,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, false);	
            }
            
            
            // Logs (Along)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), start.materialType, start.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{0,2,0, 0,2,9}, 
            	{1,3,0, 1,3,9}, 
            	{2,4,0, 2,4,9}, 
            	{3,5,0, 3,5,9}, 
            	{4,4,0, 4,4,9}, 
            	{5,3,0, 5,3,9}, 
            	{6,2,0, 6,2,9}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,0, 2}, {3,3,2, 0}, 
            	{3,3,7, 2}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Cobblestone stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, start.materialType, start.biome); Block biomeCobblestoneStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{4,2,3, 4,2,3, 3}, 
        		{4,2,6, 4,2,6, 2}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
            
            // Trapdoor (Top Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,9, 2,2,9, 0}, 
            	{4,2,9, 4,2,9, 0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, true, true), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{3, 2, 8},  
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, start.materialType, start.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 2, 1, 0},  
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{1, 1, 0}, 
        		{6, 1, 0}, 
        		})
            {
            	int flowerindex = random.nextInt(10 + (Block.getBlockFromName(ModObjects.flowerUTD)==null ? 0 : 2));
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : flowerindex > 9 ? Block.getBlockFromName(ModObjects.flowerUTD) : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.placeBlockAtCurrentPosition(world, flowerblock, flowermeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
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
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.start, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(2);
            	int v = 1;
            	int w = 2+random.nextInt(6);
            	
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
        protected int getVillagerType (int number) {return 3;}
    }
    
    
    // --- Butcher Shop --- //
    
    public static class TaigaButcherShop1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 4;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaButcherShop1() {}

        public TaigaButcherShop1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaButcherShop1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaButcherShop1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
            
            
            // Cobblestone, part 1
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, start.materialType, start.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Front steps
            	{2,1,1, 4,1,1}, 
            	// Front wall
            	{1,1,2, 5,4,2}, {2,5,2, 4,5,2}, {3,6,2, 3,6,2}, 
            	// Back wall, left side
            	{1,1,7, 5,4,7}, 
            	// Far right wall
            	{9,1,4, 9,4,7}, {9,5,5, 9,5,6}, 
            	// Floor under barrel
            	{2,1,6, 2,1,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	{6,1,1, 9,1,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassBlock, biomeGrassMeta, biomeGrassBlock, biomeGrassMeta, false);	
            }
        	
            
            // Fence
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, start.materialType, start.biome); Block biomeFenceBlock = (Block)blockObject[0]; 
            for(int[] uuvvww : new int[][]{
            	{6,2,1, 6,2,2}, 
            	{7,2,1, 9,2,1}, 
            	{9,2,2, 9,2,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceBlock, 0, biomeFenceBlock, 0, false);	
            }

            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,1, 2}, 
            	{3,4,3, 0}, {3,4,6, 2}, 
            	// Back area
            	{8,4,6, 3}, {8,4,5, 3}, 
            	// Yard
            	{9,3,2, -1}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,2, 1,4,7}, {1,5,5, 1,5,6}, 
            	// Middle right wall
            	{5,1,2, 5,4,4}, 
            	// Back wall, right side
            	{6,1,7, 9,4,7}, 
            	// Wall to the yard
            	{6,1,4, 8,4,4}, 
            	// Ceiling
            	{5,5,5, 9,5,6}, 
            	// Chimney
            	{5,6,5, 6,6,5}, {5,6,7, 5,6,7}, {4,6,6, 4,6,6}, 
            	// Roof
            	{2,1,2, 2,1,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
        	
            
            // Cobblestone Wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone_wall, 0, start.materialType, start.biome); Block biomeCobblestoneWallBlock = (Block)blockObject[0]; int biomeCobblestoneWallMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Chimney
            	{5,3,6, 5,4,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, false);	
            }
            
            
            // Logs (Across)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 0:4), start.materialType, start.biome); Block biomeLogHorAcrossBlock = (Block)blockObject[0]; int biomeLogHorAcrossMeta = (Integer)blockObject[1]; // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Left roof
            	{0,4,1, 0,4,3}, 
            	{1,5,1, 1,5,4}, 
            	{2,6,1, 2,6,4}, 
            	{3,7,1, 3,7,4}, 
            	{4,6,1, 4,6,4}, 
            	{5,5,1, 5,5,4}, 
            	{6,4,1, 6,4,3}, 
            	// Back roof
            	{0,4,8, 10,4,8}, 
            	{0,5,7, 10,5,7}, 
            	{0,6,6, 3,6,6}, {6,6,6, 10,6,6}, 
            	{0,6,5, 4,6,5}, {7,6,5, 10,6,5},
            	{0,5,4, 0,5,4}, {6,5,4, 10,5,4}, 
            	{7,4,3, 10,4,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
            
            
            // Logs (Along)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), start.materialType, start.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Left roof
            	{0,4,1, 0,4,3}, 
            	{1,5,1, 1,5,4}, 
            	{2,6,1, 2,6,4}, 
            	{3,7,1, 3,7,4}, 
            	{4,6,1, 4,6,4}, 
            	{5,5,1, 5,5,4}, 
            	{6,4,1, 6,4,3}, 
            	// Sub roof
            	{3,6,3, 3,6,3}, 
            	{2,5,3, 4,5,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
            
            
            // Logs (Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 0, start.materialType, start.biome); Block biomeLogVertBlock = (Block)blockObject[0]; int biomeLogVertMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{3,6,3, 3,6,3}, 
            	// Interior floor
            	{2,1,3, 2,1,5}, {3,1,3, 4,1,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
            
            
            // Smooth Stone Double Slab
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{7,1,6}, 
            	{8,2,6}, {8,2,5}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.double_stone_slab, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Smooth Stone Slab upper
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{5,1,5}, {6,1,5}, {7,1,5}, {8,1,5}, 
            	{5,1,6}, {6,1,6}, {8,1,6}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 8, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Smoker
        	blockObject = ModObjects.chooseModSmokerBlock(3, this.coordBaseMode); Block smokerBlock = (Block) blockObject[0];
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{5,2,6, 2}
            	})
            {
                this.placeBlockAtCurrentPosition(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]), StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode), 2);
            }
            
            
            // Cobblestone stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, start.materialType, start.biome); Block biomeCobblestoneStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{2,1,0, 4,1,0, 3}, 
        		// Roof
        		{4,7,5, 6,7,5, 3}, 
        		{4,7,7, 6,7,7, 2}, 
        		{4,7,6, 4,7,6, 0}, 
        		{6,7,6, 6,7,6, 1}, 
        		{4,6,7, 4,6,7, 4}, 
        		{6,6,7, 6,6,7, 5}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
            
            // Trapdoor (Bottom Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Yard frame
            	{6,1,0, 9,1,0, 2}, 
            	{5,1,1, 5,1,1, 3}, 
            	{10,1,1, 10,1,3, 1}, 
            	// Barrel
            	{2,2,5, 2,2,5, 2}, {3,2,6, 3,2,6, 1}, 
            	//
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
            
            
            // Trapdoor (Top Vertical)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{0,3,4, 0,3,4, 3}, {0,3,7, 0,3,7, 3}, 
            	{2,3,8, 2,3,8, 0}, {4,3,8, 4,3,8, 0}, {6,3,8, 6,3,8, 0}, {8,3,8, 8,3,8, 0}, 
            	// Chimney
            	{5,4,5, 5,4,5, 2}, {6,4,6, 6,4,6, 1}, {4,4,6, 4,4,6, 3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, true, true), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 3, 5}, {1, 3, 6}, 
        		{3, 3, 7}, {7, 3, 7}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, start.materialType, start.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,2, 2, 1, 1}, 
            	{7,2,4, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Campfire
        	blockObject = ModObjects.chooseModCampfireBlock(random.nextInt(4), coordBaseMode); Block campfireBlock = (Block) blockObject[0]; int campfireMeta = (Integer) blockObject[1];
            for(int[] uvw : new int[][]{
            	{5,5,6}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, campfireBlock, campfireMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{1, 1, 0}, 
        		{0, 1, 6}, 
        		{4, 1, 8}, {5, 1, 8}, 
        		})
            {
            	int flowerindex = random.nextInt(10 + (Block.getBlockFromName(ModObjects.flowerUTD)==null ? 0 : 2));
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : flowerindex > 9 ? Block.getBlockFromName(ModObjects.flowerUTD) : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.placeBlockAtCurrentPosition(world, flowerblock, flowermeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,3, 1}, 
            	{1,1,1, 1}, 
            	{5,1,0, 1}, 
            	{10,1,4, 1}, 
            	{10,1,5, 0}, 
            	{10,1,6, 1}, 
            	{3,1,8, 0}, 
            	{8,1,8, 0}, 
            })
            {
    			if (uwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 2, uwg[0], uwg[1], uwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 3, uwg[0], uwg[1], uwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uwg[0], uwg[1]+1, uwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
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
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.start, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}

    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{7, 2, 2},
        			})
        		{
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, random, false);
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
            	// Villager
            	int u = 2+random.nextInt(6);
            	int v = 2;
            	int w = 5;
            	
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
        protected int getVillagerType (int number) {return 4;}
    }
    
    
    // --- Cartographer House --- //
    
    public static class TaigaCartographerHouse1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"       ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"  FFF  ",
        		"  FFF F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaCartographerHouse1() {}

        public TaigaCartographerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaCartographerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaCartographerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
            
            
            // Cobblestone: everything but the front wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, start.materialType, start.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Under front door
            	{3,0,2, 3,0,2}, 
            	// Left
            	{1,1,2, 1,3,6}, 
            	// Right wall
            	{5,1,2, 5,3,6}, 
            	// Floor
            	{2,0,3, 4,0,5}, 
            	// Back wall
            	{2,1,6, 4,3,6}, 
            	// Ceiling
            	{2,3,3, 4,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
        	
            
            // Cobblestone Wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone_wall, 0, start.materialType, start.biome); Block biomeCobblestoneWallBlock = (Block)blockObject[0]; int biomeCobblestoneWallMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{2,1,0, 2,1,0}, 
            	{4,1,0, 4,1,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, false);	
            }
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, start.materialType, start.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,4,2, 1,6,2}, 
            	{1,4,4, 1,4,4}, {1,6,4, 1,6,4}, 
            	{1,4,6, 1,6,6}, 
            	// Right wall
            	{5,4,2, 5,6,2}, 
            	{5,4,4, 5,4,4}, {5,6,4, 5,6,4}, 
            	{5,4,6, 5,6,6}, 
            	// Front wall
            	{3,4,2, 3,4,2}, {3,6,2, 3,6,2}, {3,8,2, 3,8,2}, 
            	// Back wall
            	{3,4,6, 3,4,6}, {3,6,6, 3,8,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
            
            // Logs (Vertical), part 1
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 0, start.materialType, start.biome); Block biomeLogVertBlock = (Block)blockObject[0]; int biomeLogVertMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,4,3, 1,6,3}, {1,4,5, 1,6,5}, 
            	// Right wall
            	{5,4,3, 5,6,3}, {5,4,5, 5,6,5}, 
            	// Back wall
            	{2,4,6, 2,7,6}, {4,4,6, 4,7,6}, 
            	// Front wall, 2nd floor
            	{2,4,2, 2,7,2}, {4,4,2, 4,7,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }

            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front posts
            	{2,2,0, -1}, {4,2,0, -1}, 
            	// Front wall
            	{3,8,1, 2}, 
            	// First floor
            	{2,2,3, 1}, {4,2,3, 3}, 
            	// Second floor
            	{3,6,3, 0}, {3,6,5, 2}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Vertical), part 2
            for(int[] uuvvww : new int[][]{
            	// Front columns
            	{2,1,1, 2,7,1}, {4,1,1, 4,7,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
            
            
            // Logs (Along)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), start.materialType, start.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{0,6,0, 0,6,7}, 
            	{1,7,0, 1,7,7}, 
            	{2,8,0, 2,8,7}, 
            	{3,9,0, 3,9,7}, 
            	{4,8,0, 4,8,7}, 
            	{5,7,0, 5,7,7},
            	{6,6,0, 6,6,7},  
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
            
            
            // Cobblestone, front wall
            for(int[] uuvvww : new int[][]{
            	{2,1,2, 4,3,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            
            
            // Fence
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, start.materialType, start.biome); Block biomeFenceBlock = (Block)blockObject[0]; 
            for(int[] uvw : new int[][]{
            	{2,4,4}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeFenceBlock, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Wood stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, start.materialType, start.biome); Block biomeWoodStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chairs
        		{2,4,3, 2,4,3, 1}, {2,4,5, 2,4,5, 1}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
            
            // Trapdoor (Bottom Upright)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Front planter
            	{3,4,0, 3,4,0, 2}, 
            	//
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
            
            
            // Trapdoor (Top Upright)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{0,2,3, 0,2,3, 3}, {0,2,5, 0,2,5, 3}, 
            	{6,2,3, 6,2,3, 1}, {6,2,5, 6,2,5, 1}, 
            	{2,2,7, 2,2,7, 0}, {4,2,7, 4,2,7, 0}, 
            	//
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, true, true), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
            
            
            // Trapdoor (Top Horizontal)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{3,3,1, 3,3,1, 2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, true, false), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
            
            
            // Trapdoor (Bottom Horizontal)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{2,5,4, 2,5,4, 1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, false), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		// Right wall
        		{1, 2, 4}, {1, 5, 4}, 
        		// Left wall
        		{5, 2, 4}, {5, 5, 4}, 
        		// Back wall
        		{3, 2, 6}, {3, 5, 6}, 
        		// Front wall
        		{3, 5, 2}, {3, 7, 2}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, start.materialType, start.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,2, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
        	// Cartography Table
        	blockObject = ModObjects.chooseModCartographyTable(); Block cartographyTableBlock = (Block) blockObject[0]; int cartographyTableMeta = (Integer) blockObject[1];
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,1,5}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, cartographyTableBlock, cartographyTableMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Ladder
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.ladder, 0, start.materialType, start.biome); Block biomeLadderBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{4,2,5, 4,3,5, 2},  
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderBlock, StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode), biomeLadderBlock, StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode), false);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 4;
        	int chestW = 3;
        	int chestO = 0;
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_cartographer");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Planter grass
            this.placeBlockAtCurrentPosition(world, Blocks.grass, 0, 3,4,1, structureBB);
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{3, 5, 1}, 
        		})
            {
            	int flowerindex = random.nextInt(10 + (Block.getBlockFromName(ModObjects.flowerUTD)==null ? 0 : 2));
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : flowerindex > 9 ? Block.getBlockFromName(ModObjects.flowerUTD) : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.placeBlockAtCurrentPosition(world, flowerblock, flowermeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
            
            // Grass path
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModPathBlock(), 0, start.materialType, start.biome); Block grassPathBlock = (Block) blockObject[0]; int grassPathMeta = (Integer) blockObject[1]; 
            for(int[] uuvvww : new int[][]{
            	{3,0,0, 3,0,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], grassPathBlock, grassPathMeta, grassPathBlock, grassPathMeta, false);	
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{6, 1, 0},
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
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getTaigaDecorBlueprint(1+randomFromXYZ.nextInt(6), this.start, this.coordBaseMode, randomFromXYZ);
            	
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
        		{3, GROUND_LEVEL, -1}, 
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
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.start, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2;
            	int v = 1;
            	int w = 5;
            	
            	while (u==2 && w==5)
            	{
                	u = 2+random.nextInt(3);
                	w = 3+random.nextInt(3);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 2, 0);
    			
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
        protected int getVillagerType (int number) {return 1;}
    }
    
    
    // --- Fisher Cottage --- //
    
    public static class TaigaFisherCottage1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaFisherCottage1() {}

        public TaigaFisherCottage1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaFisherCottage1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaFisherCottage1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
            
        	
        	// Cobblestone, part 1
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, start.materialType, start.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Under front door
            	{4,1,2, 4,1,2}, 
            	// Front wall
            	{3,2,2, 5,5,2}, {4,6,2, 4,6,2}, 
            	// Back wall
            	{3,1,6, 5,5,6}, {4,6,6, 4,6,6}, 
            	// Floor
            	{3,1,3, 5,1,3}, {3,1,5, 5,1,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }

            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front wall
            	{3,3,1, 2}, {5,3,1, 2}, 
            	// Back wall
            	{3,3,7, 0}, {5,3,7, 0}, 
            	// Interior
            	{3,3,3, 0}, {5,3,3, 0}, {4,5,5, 2}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{2,2,3, 2,4,5}, 
            	// Right wall
            	{6,2,3, 6,4,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, start.materialType, start.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,1,4, 5,1,4}, 
            	{4,1,7, 4,1,9}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
            
            // Logs (Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 0, start.materialType, start.biome); Block biomeLogVertBlock = (Block)blockObject[0]; int biomeLogVertMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Front beams
            	{2,2,1, 2,4,1}, {6,2,1, 6,4,1}, 
            	// Left beams
            	{1,2,2, 1,3,2}, {1,1,6, 1,3,6}, 
            	// Right beams
            	{7,2,2, 7,3,2}, {7,1,6, 7,3,6}, 
            	// Back beams
            	{2,1,7, 2,4,7}, {6,1,7, 6,4,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
            
            
            // Logs (Along)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), start.materialType, start.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{1,4,0, 1,4,8}, 
            	{2,5,0, 2,5,8}, 
            	{3,6,0, 3,6,8}, 
            	{4,7,0, 4,7,8}, 
            	{5,6,0, 5,6,8}, 
            	{6,5,0, 6,5,8}, 
            	{7,4,0, 7,4,8}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
            
            
            // Logs (Across)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 0:4), start.materialType, start.biome); Block biomeLogHorAcrossBlock = (Block)blockObject[0]; int biomeLogHorAcrossMeta = (Integer)blockObject[1]; // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{3,5,7, 5,5,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
            
            
            // Fence
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, start.materialType, start.biome); Block biomeFenceBlock = (Block)blockObject[0]; 
            for(int[] uvw : new int[][]{
            	{4,5,2}, 
            	{3,2,5}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeFenceBlock, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Interior basin
            	{4,2,3, 4,2,5, 3}, 
            	// Back railing
            	{3,2,7, 3,2,7, 3}, {5,2,7, 5,2,7, 1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
            
            
            // Trapdoor (Bottom Horizontal)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{3,3,5, 3,3,5, 1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, false), biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[6], this.coordBaseMode, false, true), false);	
            }
        	
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, start.materialType, start.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,2, 2, 1, 0}, 
            	{4,2,6, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	{0,1,3, 0,1,3}, {0,1,5, 0,1,6}, {0,1,8, 0,1,9}, {0,1,11, 0,1,11}, 
            	{1,1,1, 1,1,1}, {1,1,7, 1,1,7}, {1,1,10, 1,1,11}, 
            	{2,1,11, 9,1,11}, 
            	{9,1,8, 9,1,10}, 
            	{8,1,9, 8,1,9}, 
            	{9,1,4, 9,1,6}, 
            	{7,1,4, 8,1,4}, 
            	{5,1,0, 6,1,0}, {9,1,0, 9,1,0}, 
            	{3,1,1, 3,1,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassBlock, biomeGrassMeta, biomeGrassBlock, biomeGrassMeta, false);	
            }
            
            
            // Dirt
            for(int[] uuvvww : new int[][]{
            	{1,1,2, 3,1,2}, 
            	{1,1,6, 1,1,6}, 
            	{2,1,1, 2,1,1}, 
            	{2,1,2, 2,1,6}, {6,1,2, 6,1,6}, 
            	{1,0,4, 1,0,6}, 
            	// Pond bottom
            	{1,0,7, 3,0,9}, {1,0,10, 2,0,10}, {5,0,7, 5,0,7}, 
            	{7,1,10, 8,1,10}, {9,1,7, 9,1,7}, 
            	{6,1,1, 6,1,1}, 
            	{5,1,2, 7,1,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtBlock, biomeDirtMeta, biomeDirtBlock, biomeDirtMeta, false);	
            }
            
            
            // Foundation
            // Sand
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sand, 0, start.materialType, start.biome); Block biomeSandBlock = (Block)blockObject[0]; int biomeSandMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{4,0,11, 5,0,11}, 
            	{7,0,7, 7,0,7}, 
            	{9,0,5, 9,0,10}, {8,0,5, 8,0,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandBlock, biomeSandMeta, biomeSandBlock, biomeSandMeta, false);	
            }
            // Gravel
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.gravel, 0, start.materialType, start.biome); Block biomeGravelBlock = (Block)blockObject[0]; int biomeGravelMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{3,0,10, 3,0,10}, 
            	{4,0,8, 8,0,10}, 
            	{6,0,11, 8,0,11}, {9,0,10, 9,0,10}, {6,0,7, 6,0,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandBlock, biomeSandMeta, biomeSandBlock, biomeSandMeta, false);	
            }
            // Clay
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.clay, 0, start.materialType, start.biome); Block biomeClayBlock = (Block)blockObject[0]; int biomeClayMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{5,0,5, 5,0,5}, {6,0,4, 6,0,5}, {7,0,3, 7,0,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandBlock, biomeSandMeta, biomeSandBlock, biomeSandMeta, false);	
            }
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{3, 2, 1}, 
        		})
            {
            	int flowerindex = random.nextInt(10 + (Block.getBlockFromName(ModObjects.flowerUTD)==null ? 0 : 2));
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : flowerindex > 9 ? Block.getBlockFromName(ModObjects.flowerUTD) : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.placeBlockAtCurrentPosition(world, flowerblock, flowermeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,2,3, 0},
            	{0,2,5, 1}, 
            	{1,2,10, 1}, 
            	{1,2,1, 1}, 
            	{6,2,0, 1}, 
            })
            {
    			if (uwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 2, uwg[0], uwg[1], uwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 3, uwg[0], uwg[1], uwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uwg[0], uwg[1]+1, uwg[2], structureBB);
    			}
            }
        	
        	
        	// Barrels
    		Block barrelBlock = ModObjects.chooseModBarrelBlock();
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel: -1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
    			// TODO - use different barrel meta for different mods
            	
    			// Exterior
    			{8,2,10, 3,-1}, {9,2,7, 0,-1}, 
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (barrelBlock==null) {barrelBlock = Blocks.chest;}
    			this.placeBlockAtCurrentPosition(world, barrelBlock, 0, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2]), barrelBlock==Blocks.chest?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.coordBaseMode):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.coordBaseMode), 2);
            }
            
            
            // Water
            for(int[] uuvvww : new int[][]{
            	// Interior
            	{5,2,3, 5,2,5}, 
            	// Back pond
            	{1,1,8, 3,1,9}, 
            	{3,1,7, 3,1,7}, 
            	{2,1,10, 6,1,10}, 
            	{5,1,8, 7,1,9}, 
            	{5,1,7, 5,1,7}, 
            	{7,1,7, 7,1,7}, 
            	{7,1,5, 7,1,5}, 
            	{8,1,5, 8,1,8}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water, 0, Blocks.flowing_water, 0, false);	
            }
            
            
            // Grass path
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModPathBlock(), 0, start.materialType, start.biome); Block grassPathBlock = (Block) blockObject[0]; int grassPathMeta = (Integer) blockObject[1]; 
            for(int[] uuvvww : new int[][]{
            	{4,1,0, 4,1,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], grassPathBlock, grassPathMeta, grassPathBlock, grassPathMeta, false);	
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
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.start, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{9, 2, 0},
            	{0, 2, 11},
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
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getTaigaDecorBlueprint(1+randomFromXYZ.nextInt(6), this.start, this.coordBaseMode, randomFromXYZ);
            	
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 4;
            	int v = 2;
            	int w = 2+random.nextInt(8);
            	
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 2, 0);
    			
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
    
    
    // --- Fletcher House --- //
    
    public static class TaigaFletcherHouse1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"           ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		"    FFF    ",
        		"    FFF    ",
        		"     F     ",
        		"   F   F   ",
        		"           ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaFletcherHouse1() {}

        public TaigaFletcherHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaFletcherHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaFletcherHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
            
        	
        	// Cobblestone, part 1
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, start.materialType, start.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{4,0,3, 6,3,3}, {5,4,3, 5,4,3}, 
            	// Left wall
            	{1,0,5, 1,3,8}, {1,4,6, 1,4,7}, 
            	// Right wall
            	{9,0,5, 9,3,8}, {9,4,6, 9,4,7}, 
            	// Back wall
            	{2,0,8, 8,3,8}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{4,2,2, 2}, {6,2,2, 2}, 
            	// Over front door
            	{5,3,4, 0}, 
            	// Back wall
            	{3,3,7, 2}, {7,3,7, 2}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Left wing
            	{2,0,5, 3,3,5}, {4,0,4, 4,3,4}, 
            	// Right wing
            	{7,0,5, 8,3,5}, {6,0,4, 6,3,4}, 
            	// Above entryway
            	{5,4,4, 5,4,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            

            // Logs (Vertical)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 0, start.materialType, start.biome); Block biomeLogVertBlock = (Block)blockObject[0]; int biomeLogVertMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	{4,0,5, 4,0,5}, {2,0,6, 3,0,7}, 
            	{6,0,5, 6,0,5}, {7,0,6, 7,0,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
            
            
            // Logs (Along)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), start.materialType, start.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1]; // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{3,3,2, 3,3,4}, 
            	{4,4,2, 4,4,4}, 
            	{5,5,2, 5,5,5}, 
            	{6,4,2, 6,4,4}, 
            	{7,3,2, 7,3,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);
            }
            
            
            // Logs (Across)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 0:4), start.materialType, start.biome); Block biomeLogHorAcrossBlock = (Block)blockObject[0]; int biomeLogHorAcrossMeta = (Integer)blockObject[1]; // Perpendicular to you
            for (int[] uw : new int[][]{
            	{0,3,4, 2,3,4}, {8,3,4, 10,3,4}, 
            	{0,4,5, 10,4,5}, 
            	{0,5,6, 10,5,6}, 
            	{0,5,7, 10,5,7}, 
            	{0,4,8, 10,4,8}, 
            	{0,3,9, 10,3,9}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);
            }
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, start.materialType, start.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Ceiling
            	{5,0,4, 5,0,5}, {4,0,6, 6,0,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
        	
            // Wooden stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, start.materialType, start.biome); Block biomeWoodStairsBlock = (Block)blockObject[0];
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,1,6, 1}, {2,1,7, 1}, 
        		{8,1,6, 0}, {8,1,7, 0}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, start.materialType, start.biome); Block biomeCobblestoneStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{5,0,2, 5,0,2, 3}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
            
            // Planter grass
            for (int[] uvw : new int[][]{
        		{3, 0, 1}, 
        		{7, 0, 1}, 
        		})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.grass, 0, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
            
            // Fences
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, start.materialType, start.biome); Block biomeFenceBlock = (Block)blockObject[0];
            for (int[] uvw : new int[][]{
        		{4, 1, 7}, 
        		{6, 1, 7}, 
        		})
            {
            	this.placeBlockAtCurrentPosition(world, biomeFenceBlock, 0, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
            
            // Carpet
            for (int[] uuvvww : new int[][]{
        		{4,2,7, 4,2,7}, 
        		{6,2,7, 6,2,7}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet, GeneralConfig.decorateVillageCenter ? this.start.townColor : 10, Blocks.carpet, GeneralConfig.decorateVillageCenter ? this.start.townColor : 10, false); // 10 is purple
            }
            
            
            // Fletching Table
        	blockObject = ModObjects.chooseModFletchingTable(); Block fletchingTableBlock = (Block) blockObject[0]; int fletchingTableMeta = (Integer) blockObject[1];
        	this.placeBlockAtCurrentPosition(world, fletchingTableBlock, fletchingTableMeta, 5, 1, 7, structureBB);
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Left wing
        		{2, 2, 5}, 
        		// Right wing
        		{8, 2, 5}, 
        		// Back wall
        		{3, 2, 8}, {5, 2, 8}, {7, 2, 8}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Left planter
            	{3,0,0, 2}, {4,0,1, 1}, {3,0,2, 0}, {2,0,1, 3}, 
            	// Right planter
            	{7,0,0, 2}, {8,0,1, 1}, {7,0,2, 0}, {6,0,1, 3}, 
            	// Left wing
            	{1,2,4, 2}, {3,2,4, 2}, 
            	// Right wing
            	{7,2,4, 2}, {9,2,4, 2}, 
            	// Back wall
            	{2,2,9, 0}, {4,2,9, 0}, {6,2,9, 0}, {8,2,9, 0}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, true), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{3, 1, 1}, 
        		{7, 1, 1}, 
        		})
            {
            	int flowerindex = random.nextInt(10 + (Block.getBlockFromName(ModObjects.flowerUTD)==null ? 0 : 2));
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : flowerindex > 9 ? Block.getBlockFromName(ModObjects.flowerUTD) : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.placeBlockAtCurrentPosition(world, flowerblock, flowermeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, start.materialType, start.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,1,3, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 1;
        	int chestW = 5;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_fletcher");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            /*
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
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.start, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            */
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(5);
            	int v = 1;
            	int w = 6;
            	
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 4, 0);
    			
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
    
        
    // --- Large Farm --- //
    
    public static class TaigaLargeFarm1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"  F   F   ",
        		" FFFFFFFFF",
        		"F  FFFFFF ",
        		"  FFFFFFF ",
        		" FFFFFFFF ",
        		" FFFFFFFFF",
        		" FFFFFFF  ",
        		" FFFFFFF  ",
        		" FFFFFFFF ",
        		"   FF  F  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 2;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaLargeFarm1() {}

        public TaigaLargeFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaLargeFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaLargeFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
        	
            
            // Cobblestone Wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone_wall, 0, start.materialType, start.biome); Block biomeCobblestoneWallBlock = (Block)blockObject[0]; int biomeCobblestoneWallMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{8,1,1, 8,3,1}, 
            	{2,1,8, 2,3,8}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{8,4,1, -1}, {2,4,8, -1}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
                        
            // Moist Farmland
            for(int[] uvw : new int[][]{
            	
            	{1,1,2, 4}, 
            	{1,1,3, 7}, 
            	{1,1,4, 5}, 
            	{1,1,5, 7}, 
            	
            	{2,1,1, 7}, 
            	{2,1,2, 5}, 
            	{2,1,4, 7}, 
            	{2,1,5, 5}, 
            	{2,1,6, 4}, 
            	
            	{3,1,3, 1}, 
            	{3,1,5, 6}, 
            	{3,1,7, 5}, 
            	
            	{4,1,5, 7}, 
            	{4,1,6, 7}, 
            	{4,1,7, 7}, 
            	{4,1,8, 7}, 
            	
            	{5,1,1, 7}, 
            	{5,1,2, 7}, 
            	{5,1,2, 7}, 
            	{5,1,8, 7}, 
            	
            	{6,1,1, 1}, 
            	{6,1,3, 7}, 
            	{6,1,4, 3}, 
            	{6,1,6, 5}, 
            	
            	{7,1,2, 3}, 
            	{7,1,3, 5}, 
            	{7,1,4, 7}, 
            	{7,1,5, 3}, 
            	{7,1,7, 7}, 
            	
            	{8,1,6, 7}, 
            	{8,1,7, 5}, 
            	{8,1,8, 7}, 
            	
            	})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.wheat, uvw[3], uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.placeBlockAtCurrentPosition(world, Blocks.farmland, 7, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Pumpkin with dirt underneath
            for(int[] uvw : new int[][]{
            	{1,2,1}, 
            	{4,1,0}, 
            	{7,1,0}, 
            	{9,1,4}, 
            	{7,2,8}, 
            	{2,1,9}, 
            	{0,1,7}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.pumpkin, random.nextInt(3), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Grass path
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModPathBlock(), 0, start.materialType, start.biome); Block grassPathBlock = (Block) blockObject[0]; int grassPathMeta = (Integer) blockObject[1]; 
            for(int[] uuvvww : new int[][]{
            	{3,0,0, 3,0,2}, 
            	{4,0,1, 4,0,4}, 
            	{5,0,3, 5,0,7}, 
            	{6,0,7, 6,0,9}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], grassPathBlock, grassPathMeta, grassPathBlock, grassPathMeta, false);	
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, place a pumpkin instead.
            Block compostBin = Block.getBlockFromName(ModObjects.compostBinGC);
            for(int[] uvw : new int[][]{
            	{3,1,2}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	if (compostBin != null)
                {
                	this.placeBlockAtCurrentPosition(world, compostBin, 0, uvw[0], uvw[1], uvw[2], structureBB);
                }
            	else
            	{
            		this.placeBlockAtCurrentPosition(world, Blocks.pumpkin, random.nextInt(3), uvw[0], uvw[1], uvw[2], structureBB);
            	}
            }
            
            
            // Dirt
            for(int[] uvw : new int[][]{
            	{3,0,2}, 
            	{3,0,6}, 
            	{6,0,2}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{2,1,3}, 
            	{3,1,6}, 
            	{7,1,6}, 
            	{6,1,2}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
        	
            // Stone stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, start.materialType, start.biome); Block biomeCobblestoneStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{3,1,4, 3,1,4, 1}, 
        		{6,1,5, 6,1,5, 0}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
            
            // Trapdoor (Bottom Upright)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Back facing
            	{1,1,0, 2}, {2,1,0, 2}, {5,1,0, 2}, {6,1,0, 2},
            	// Right facing
            	{7,1,1, 1}, {8,1,2, 1}, {8,1,3, 1}, {8,1,4, 1}, {8,1,5, 1}, {9,1,6, 1}, {9,1,7, 1}, {9,1,8, 1}, 
            	// Forward facing
            	{8,1,9, 0}, {7,1,9, 0}, {5,1,9, 0}, {4,1,9, 0}, 
            	// Left facing
            	{0,1,1, 3}, {0,1,2, 3}, {0,1,3, 3}, {0,1,4, 3}, {0,1,5, 3}, {1,1,6, 3}, {2,1,7, 3}, {3,1,8, 3}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	
            	// Villager
            	int u = 4;
            	int v = 1;
            	int w = 4;
            	
            	int s = random.nextInt(12);
            	
            	if (s==0) {u=3; w=1;}
            	else if (s<=4) {u=4; w=s;}
            	else if (s<=9) {u=5; w=s-2;}
            	else if (s<=11) {u=6; w=s-3;}
            	
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0);
    			
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
    
        
    // --- Medium Farm --- //
    
    public static class TaigaMediumFarm1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		" FF  F  ",
        		"FFFFFFF ",
        		" FFFFF F",
        		" FFFFF  ",
        		"FFFFFF  ",
        		"FFFFFF F",
        		"FFFFFF F",
        		"F FFFFFF",
        		"   FFFF ",
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
    	
        public TaigaMediumFarm1() {}

        public TaigaMediumFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaMediumFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaMediumFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
        	
            
            // Cobblestone Wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone_wall, 0, start.materialType, start.biome); Block biomeCobblestoneWallBlock = (Block)blockObject[0]; int biomeCobblestoneWallMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{6,1,0, 6,2,0}, 
            	{1,1,2, 1,2,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{6,3,0, -1}, {1,3,2, -1}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
                        
            // Moist Farmland
            for(int[] uvw : new int[][]{
            	
            	{1,1,5, 7}, 
            	{1,1,6, 7}, 
            	
            	{2,1,5, 7}, 
            	{2,1,6, 7},
            	{2,1,7, 3},
            	
            	{3,1,3, 7},
            	{3,1,4, 0},
            	{3,1,5, 1},
            	{3,1,7, 7},
            	
            	{4,1,2, 7},
            	{4,1,4, 7},
            	{4,1,5, 7},
            	{4,1,6, 0},
            	
            	{5,1,2, 0},
            	{5,1,3, 0},
            	{5,1,4, 7},
            	{5,1,5, 7},
            	
            	})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.wheat, uvw[3], uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.placeBlockAtCurrentPosition(world, Blocks.farmland, 7, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Pumpkin with dirt underneath
            for(int[] uvw : new int[][]{
            	
            	{0,1,3}, 
            	{0,1,7},
            	{2,1,1},
            	{5,1,8},
            	{7,1,6},
            	
            	})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.pumpkin, random.nextInt(3), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Grass path
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModPathBlock(), 0, start.materialType, start.biome); Block grassPathBlock = (Block) blockObject[0]; int grassPathMeta = (Integer) blockObject[1]; 
            for(int[] uuvvww : new int[][]{
            	{0,0,4, 0,0,4}, 
            	{1,0,3, 1,0,4}, 
            	{2,0,2, 2,0,3}, 
            	{3,0,0, 3,0,2}, 
            	{4,0,1, 7,0,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], grassPathBlock, grassPathMeta, grassPathBlock, grassPathMeta, false);	
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, place a pumpkin instead.
            Block compostBin = Block.getBlockFromName(ModObjects.compostBinGC);
            for(int[] uvw : new int[][]{
            	{5,1,0}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	if (compostBin != null)
                {
                	this.placeBlockAtCurrentPosition(world, compostBin, 0, uvw[0], uvw[1], uvw[2], structureBB);
                }
            	else
            	{
            		this.placeBlockAtCurrentPosition(world, Blocks.pumpkin, random.nextInt(3), uvw[0], uvw[1], uvw[2], structureBB);
            	}
            }
            
            
            // Dirt
            for(int[] uvw : new int[][]{
            	{3,0,6}, 
            	{4,0,3}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeDirtBlock, biomeDirtMeta, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{3,1,6}, 
            	{4,1,3}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
        	
            // Stone stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, start.materialType, start.biome); Block biomeCobblestoneStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{2,1,4, 2,1,4, 3}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
            
            // Trapdoor (Bottom Upright)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Right facing
            	{6,1,2, 1}, {6,1,3, 1}, {6,1,4, 1}, {6,1,4, 1}, {5,1,6, 1}, {4,1,7, 1}, 
            	// Forward facing
            	{2,1,8, 0}, {3,1,8, 0}, 
            	// Left facing
            	{0,1,5, 3}, {0,1,6, 3}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,1, 1},
            	{1,1,8, 1}, 
            	{4,1,0, 1}, 
            	{6,1,7, 1}, 
            	{7,1,3, 0}, 
            })
            {
    			if (uwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 2, uwg[0], uwg[1], uwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 3, uwg[0], uwg[1], uwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uwg[0], uwg[1]+1, uwg[2], structureBB);
    			}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{7, 1, 2},
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
            	ArrayList<BlueprintData> decorBlueprint = getTaigaDecorBlueprint(1+randomFromXYZ.nextInt(6), this.start, this.coordBaseMode, randomFromXYZ);
            	
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	
            	// Villager
            	int u = 3;
            	int v = 1;
            	int w = 1;
            	
            	int s = random.nextInt(9);
            	
            	if (s==0) {u=1; w=4;}
            	else if (s<=2) {u=s; w=3;}
            	else if (s<=4) {u=s-1; w=2;}
            	else if (s<=11) {u=s-2; w=1;}
            	
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0);
    			
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
    
    
    // --- Library --- //
    
    public static class TaigaLibrary1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"  F        ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaLibrary1() {}

        public TaigaLibrary1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaLibrary1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaLibrary1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
            
        	
        	// Cobblestone, part 1
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, start.materialType, start.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Under door
            	{5,0,2, 5,0,2}, 
            	// Front wall
            	{1,1,2, 9,4,2}, {2,5,2, 8,5,2}, {3,6,2, 7,6,2}, {4,7,2, 6,7,2}, {5,8,2, 5,8,2}, 
            	// Back wall
            	{1,1,6, 9,4,6}, {2,5,6, 8,5,6}, {3,6,6, 7,6,6}, {4,7,6, 6,7,6}, {5,8,6, 5,8,6}, 
            	// Floor
            	{2,0,3, 8,0,5}, 
            	// Stairs
            	{5,1,4, 5,2,4}, {6,1,4, 6,1,4}, 
            	// Floor 2
            	{2,3,3, 8,3,3}, {2,3,4, 4,3,4}, {2,3,5, 8,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
        	
            
            // Cobblestone Wall
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone_wall, 0, start.materialType, start.biome); Block biomeCobblestoneWallBlock = (Block)blockObject[0]; int biomeCobblestoneWallMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{4,1,0, 4,1,0}, {6,1,0, 6,1,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, biomeCobblestoneWallBlock, biomeCobblestoneWallMeta, false);	
            }
            
            
            // Logs (Along)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), start.materialType, start.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1]; // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,4,1, 0,4,7}, 
            	{1,5,1, 1,5,7}, 
            	{2,6,1, 2,6,7}, 
            	{3,7,1, 3,7,7}, 
            	{4,8,1, 4,8,7}, 
            	{5,9,1, 5,9,7}, 
            	{6,8,1, 6,8,7}, 
            	{7,7,1, 7,7,7}, 
            	{8,6,1, 8,6,7}, 
            	{9,5,1, 9,5,7}, 
            	{10,4,1, 10,4,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{4,2,0, -1}, {6,2,0, -1}, 
            	// Front wall
            	{5,7,1, 2}, 
            	// Back wall
            	{5,7,7, 0}, 
            	// First floor
            	{4,2,3, 0}, {4,2,5, 2}, {8,2,5, 2}, 
            	// Second floor
            	{3,6,4, 1}, {7,6,4, 3}, 
            	{5,7,3, 0}, {5,7,5, 2}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,3, 1,4,5}, 
            	// Right wall
            	{9,1,3, 9,4,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            
        	
            // Stone stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, start.materialType, start.biome); Block biomeCobblestoneStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{5,3,4, 5,3,4, 1}, {6,2,4, 6,2,4, 1}, {7,1,4, 7,1,4, 1}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
            
            // Planter grass
            for (int[] uuvvww : new int[][]{
        		{1,1,1, 3,1,1}, 
        		{7,1,1, 9,1,1}, 
        		{4,4,1, 6,4,1}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.grass, 0, Blocks.grass, 0, false);
            }
            
            
            // Carpet
            for (int[] uuvvww : new int[][]{
            	// Downstairs
        		{3,1,3, 3,1,5, GeneralConfig.decorateVillageCenter ? this.start.townColorA : 14}, // 14 is red
        		// Upstairs
        		{3,4,3, 4,4,5, GeneralConfig.decorateVillageCenter ? this.start.townColor : 10}, // 10 is purple
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet, uuvvww[6], Blocks.carpet, uuvvww[6], false);
            }
            
        	
            // Lectern
        	blockObject = ModObjects.chooseModLectern(); Block lecternBlock = (Block) blockObject[0]; int lecternMeta = (Integer) blockObject[1];
            this.placeBlockAtCurrentPosition(world, lecternBlock, lecternMeta, 2, 1, 5, structureBB);
        	
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{2,4,3, 2,5,5}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf, 0, Blocks.bookshelf, 0, false);
            }
            
        	
            // Wooden stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_stairs, 0, start.materialType, start.biome); Block biomeWoodStairsBlock = (Block)blockObject[0];
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,1,3, 1}, {2,1,4, 1}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeWoodStairsBlock, this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Front wall
        		{3, 2, 2}, {7, 2, 2}, {4, 5, 2}, {6, 5, 2}, 
        		// Left wall
        		{1, 2, 4}, 
        		// Right wall
        		{9, 2, 4}, 
        		// Back wall
        		{3, 2, 6}, {5, 2, 6}, {7, 2, 6}, 
        		{3, 5, 6}, {5, 5, 6}, {7, 5, 6}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Left planter
            	{0,1,1, 3}, {1,1,0, 2}, {2,1,0, 2}, {3,1,0, 2}, {4,1,1, 1}, 
            	// Right planter
            	{6,1,1, 3}, {7,1,0, 2}, {8,1,0, 2}, {9,1,0, 2}, {10,1,1, 1},  
            	// Top planter
            	{3,4,1, 3}, {4,4,0, 2}, {5,4,0, 2}, {6,4,0, 2}, {7,4,1, 1},  
            	
            	// Front shutters
            	{2,2,1, 2}, {3,5,1, 2}, {5,5,1, 2}, {7,5,1, 2}, {8,2,1, 2}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{6, 5, 1}, 
        		{3, 2, 1}, 
        		})
            {
            	int flowerindex = random.nextInt(10 + (Block.getBlockFromName(ModObjects.flowerUTD)==null ? 0 : 2));
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : flowerindex > 9 ? Block.getBlockFromName(ModObjects.flowerUTD) : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.placeBlockAtCurrentPosition(world, flowerblock, flowermeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{1,2,1, 1},
            	{9,2,1, 1}, 
            })
            {
    			if (uwg[3]==0) // Short grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.tallgrass, 2, uwg[0], uwg[1], uwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 3, uwg[0], uwg[1], uwg[2], structureBB);
    				this.placeBlockAtCurrentPosition(world, Blocks.double_plant, 11, uwg[0], uwg[1]+1, uwg[2], structureBB);
    			}
            }
        	
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, start.materialType, start.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,1,2, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
        	// Grass Path
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModPathBlock(), 0, start.materialType, start.biome); Block grassPathBlock = (Block) blockObject[0]; int grassPathMeta = (Integer) blockObject[1]; 
        	for (int[] uuvvww : new int[][]{
        		{5,0,0, 5,0,1},
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], grassPathBlock, grassPathMeta, grassPathBlock, grassPathMeta, false);	
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
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
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.start, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{10, 1, 0},
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
            	ArrayList<BlueprintData> decorBlueprint = getTaigaDecorBlueprint(1+randomFromXYZ.nextInt(6), this.start, this.coordBaseMode, randomFromXYZ);
            	
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(2);
            	int v = random.nextBoolean()? 1:4;
            	int w = 3+random.nextInt(3);
            	
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 1, 0);
    			
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
        protected int getVillagerType (int number) {return 1;}
    }
    
    
    // --- Mason's House --- //
    
    public static class TaigaMasonsHouse1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"F       F",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		"FFFFFFFFF",
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
    	
        public TaigaMasonsHouse1() {}

        public TaigaMasonsHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            this.start = start;
        }

        public static TaigaMasonsHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaMasonsHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            Object[] blockObject;
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, start.materialType, start.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass, 0, start.materialType, start.biome); Block biomeGrassBlock = (Block)blockObject[0]; int biomeGrassMeta = (Integer)blockObject[1];
        	
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
            
        	
        	// Cobblestone
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, start.materialType, start.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Entrance
            	{3,1,1, 5,4,1}, {4,5,1, 4,5,1}, 
            	// Front wall
            	{1,1,2, 2,2,2}, {2,3,2, 2,3,2}, {6,1,2, 7,2,2}, {6,3,2, 6,3,2}, 
            	// Back wall
            	{1,1,5, 7,2,5}, {2,3,5, 6,3,5}, {3,4,5, 5,4,5}, {4,5,5, 4,5,5}, 
            	// Right wall
            	{1,1,3, 1,1,4}, 
            	// Left wall
            	{7,1,3, 7,1,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneBlock, biomeCobblestoneMeta, biomeCobblestoneBlock, biomeCobblestoneMeta, false);	
            }
            
            
            // Logs (Along)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.log, 4+(this.coordBaseMode%2==0? 4:0), start.materialType, start.biome); Block biomeLogHorAlongBlock = (Block)blockObject[0]; int biomeLogHorAlongMeta = (Integer)blockObject[1]; // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,2,0, 0,2,6}, 
            	{1,3,0, 1,3,6}, 
            	{2,4,0, 2,4,6}, 
            	{3,5,0, 3,5,6}, 
            	{4,6,0, 4,6,6}, 
            	{5,5,0, 5,5,6}, 
            	{6,4,0, 6,4,6}, 
            	{7,3,0, 7,3,6}, 
            	{8,2,0, 8,2,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front wall
            	{3,3,0, 2}, {5,3,0, 2}, 
            	// Interior
            	{4,4,4, 2}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, start.materialType, start.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,1,2, 5,1,2}, {2,1,3, 6,1,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankBlock, biomePlankMeta, biomePlankBlock, biomePlankMeta, false);	
            }
            
            
            // Fences
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, start.materialType, start.biome); Block biomeFenceBlock = (Block)blockObject[0];
            for (int[] uvw : new int[][]{
            	// Posts
            	{0,1,0}, {8,1,0}, 
            	{0,1,6}, {8,1,6}, 
            	// Interior
            	// Right wall
            	{1,2,3}, {1,2,4}, 
            	// Left wall
            	{7,2,3}, {7,2,4}, 
        		})
            {
            	this.placeBlockAtCurrentPosition(world, biomeFenceBlock, 0, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
        	
            // Stone stairs
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, start.materialType, start.biome); Block biomeCobblestoneStairsBlock = (Block)blockObject[0];
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entrance
        		{4,1,0, 4,1,0, 3}, 
        		// interior
        		{5,2,2, 5,2,2, 0}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeCobblestoneStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
        	
        	
            // Stone Cutter
        	blockObject = ModObjects.chooseModStonecutter(); Block stonecutterBlock = (Block) blockObject[0]; int stonecutterMeta = (Integer) blockObject[1];
            this.placeBlockAtCurrentPosition(world, stonecutterBlock, stonecutterMeta, 4, 2, 4, structureBB);
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		{4, 3, 5}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, start.materialType, start.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Left planter
            	{2,1,-1, 2}, {3,1,0, 1}, {2,1,1, 0}, {1,1,0, 3}, 
            	// Right planter
            	{6,1,-1, 2}, {7,1,0, 1}, {6,1,1, 0}, {5,1,0, 3}, 
            	// Back shutter
            	{3,3,6, 0}, {5,3,6, 0}, 
            	})
            {
            	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Spruce sapling in a flower pot
            int flowerPotU = 3;
            int flowerPotV = 2;
            int flowerPotW = 2;
            int xWithOffset = this.getXWithOffset(flowerPotU, flowerPotW);
            int yWithOffset = this.getYWithOffset(flowerPotV);
            int zWithOffset = this.getZWithOffset(flowerPotU, flowerPotW);
            this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, 0, flowerPotU, flowerPotV, flowerPotW, structureBB);
            
            // This is here just in case a flower pot can't be placed at the given position
            TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)world.getTileEntity(xWithOffset, yWithOffset, zWithOffset);
            
            if (tileentityflowerpot != null)
            {
            	// Sets the flower pot's item and meta
                tileentityflowerpot.func_145964_a(Item.getItemFromBlock(Blocks.sapling), 1);
                tileentityflowerpot.markDirty();
                
                if (!world.setBlockMetadataWithNotify(xWithOffset, yWithOffset, zWithOffset, (new ItemStack(Blocks.sapling, 1, 1)).getItemDamage(), 2))
                {
                	world.markBlockForUpdate(xWithOffset, yWithOffset, zWithOffset);
                }
            }
        	
            
            // Doors
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, start.materialType, start.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
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
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.start, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 4;
            	int v = 2;
            	int w = 4;
            	
            	while (u==4 && w==4)
            	{
                	u = 2+random.nextInt(5);
                	w = 3+random.nextInt(2);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 4, 0);
    			
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
        protected int getVillagerType (int number) {return 3;}
    }
	
	
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
	public static ArrayList<BlueprintData> getRandomTaigaDecorBlueprint(StartVN startVN, int coordBaseMode, Random random)
	{
		int decorCount = 7;
		return getTaigaDecorBlueprint(random.nextInt(decorCount), startVN, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getTaigaDecorBlueprint(int decorType, StartVN startVN, int coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		Object[] blockObject;
    	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, startVN.materialType, startVN.biome); Block biomeCobblestoneBlock = (Block)blockObject[0]; int biomeCobblestoneMeta = (Integer)blockObject[1];
    	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt, 0, startVN.materialType, startVN.biome); Block biomeDirtBlock = (Block)blockObject[0]; int biomeDirtMeta = (Integer)blockObject[1];
    	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs, 0, startVN.materialType, startVN.biome); Block biomeStoneStairsBlock = (Block)blockObject[0];
    	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, startVN.materialType, startVN.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
    	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor, 0, startVN.materialType, startVN.biome); Block biomeTrapdoorBlock = (Block)blockObject[0]; int biomeTrapdoorMeta = (Integer)blockObject[1];
    	blockObject = ModObjects.chooseModCampfireBlock(random.nextInt(4), coordBaseMode); Block campfireBlock = (Block) blockObject[0]; int campfireMeta = (Integer) blockObject[1];
    	    	
		boolean genericBoolean=false;
    	
        switch (decorType)
        {
    	case 0: // Wood trough
    		boolean shift=random.nextBoolean();
    		switch (random.nextInt(2))
    		{
    		case 0:
    			
    			// Base and foundation
    			for (int i=-2 ; i<=1; i++)
    			{
    				BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, -1, i+(shift?1:0), biomePlankBlock, biomePlankMeta);
    				BlueprintData.addFillBelowTo(blueprint, 0, -2, i+(shift?1:0), biomeDirtBlock, biomeDirtMeta);
    			}
    			
    			// Left
    			BlueprintData.addFillWithBlocks(blueprint, -1, 0, -2+(shift?1:0), -1, 0, 1+(shift?1:0), biomeTrapdoorBlock, coordBaseMode%2==0 ? 6 : 4);
    			// Right
    			BlueprintData.addFillWithBlocks(blueprint, 1, 0, -2+(shift?1:0), 1, 0, 1+(shift?1:0), biomeTrapdoorBlock, coordBaseMode%2==0 ? 7 : 5);
    			// Front
    			BlueprintData.addFillWithBlocks(blueprint, 0, 0, -3+(shift?1:0), 0, 0, -3+(shift?1:0), biomeTrapdoorBlock, (new int[]{4, 7, 5, 6})[coordBaseMode]);
    			// Back
    			BlueprintData.addFillWithBlocks(blueprint, 0, 0, 2+(shift?1:0), 0, 0, 2+(shift?1:0), biomeTrapdoorBlock, (new int[]{5, 6, 4, 7})[coordBaseMode]);
    			
    			break;
    			
    		case 1:
    			// Base
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), -1, 0, 1+(shift?1:0), -1, 0, biomePlankBlock, biomePlankMeta);
    			// Foundation
    			for (int i=-2 ; i<=1; i++) {BlueprintData.addPlaceBlock(blueprint, i+(shift?1:0), -2, 0, biomeDirtBlock, biomeDirtMeta);}
    			
    			// Left
    			BlueprintData.addFillWithBlocks(blueprint, -3+(shift?1:0), 0, 0, -3+(shift?1:0), 0, 0, biomeTrapdoorBlock, coordBaseMode%2==0 ? 6 : 4);
    			// Right
    			BlueprintData.addFillWithBlocks(blueprint, 2+(shift?1:0), 0, 0, 2+(shift?1:0), 0, 0, biomeTrapdoorBlock, coordBaseMode%2==0 ? 7 : 5);
    			// Front
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), 0, -1, 1+(shift?1:0), 0, -1, biomeTrapdoorBlock, (new int[]{4, 7, 5, 6})[coordBaseMode]);
    			// Back
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), 0, 1, 1+(shift?1:0), 0, 1, biomeTrapdoorBlock, (new int[]{5, 6, 4, 7})[coordBaseMode]);
    			break;
    		}
    		break;
    		
    	case 1: // Large boulder
    		// Central boulder is in the same place
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeCobblestoneBlock, biomeCobblestoneMeta);
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeDirtBlock, biomeDirtMeta); // Foundation
    		
    		switch (random.nextInt(4))
    		{
    		case 0: // Facing you
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeCobblestoneBlock, biomeCobblestoneMeta);
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeDirtBlock, biomeDirtMeta); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 3, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtBlock, biomeDirtMeta); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 3, coordBaseMode));
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 2, coordBaseMode));
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 1, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 1, biomeDirtBlock, biomeDirtMeta); // Foundation
    			break;
    		case 1: // Facing left
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeCobblestoneBlock, biomeCobblestoneMeta);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtBlock, biomeDirtMeta); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 0, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeDirtBlock, biomeDirtMeta); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 0, coordBaseMode));
    			BlueprintData.addPlaceBlock(blueprint, 1, 1, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 1, coordBaseMode));
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, -1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 3, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, -1, biomeDirtBlock, biomeDirtMeta); // Foundation
    			break;
    		case 2: // Facing away
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeCobblestoneBlock, biomeCobblestoneMeta);
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtBlock, biomeDirtMeta); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 2, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeDirtBlock, biomeDirtMeta); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 2, coordBaseMode));
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, -1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 3, coordBaseMode));
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, -1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 0, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeDirtBlock, biomeDirtMeta); // Foundation
    			break;
    		case 3: // Facing right
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeCobblestoneBlock, biomeCobblestoneMeta);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeDirtBlock, biomeDirtMeta); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 1, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtBlock, biomeDirtMeta); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 1, coordBaseMode));
    			BlueprintData.addPlaceBlock(blueprint, -1, 1, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 0, coordBaseMode));
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 2, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeDirtBlock, biomeDirtMeta); // Foundation
    			break;
    		}
    		break;
    	
    	case 2: // Small boulder with spike
    		genericBoolean=true;
    	case 3: // Small boulder without spike
    		// Central boulder is in the same place
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeCobblestoneBlock, biomeCobblestoneMeta);
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeDirtBlock, biomeDirtMeta); // Foundation
    		
    		Block boulderTopperBlock; int boulderTopperMeta;
    		int boulderOrientation = random.nextInt(4);
    		
    		if (genericBoolean)
    		{
    			// Put a spike on top of the boulder
    			
    			boulderTopperBlock = Blocks.cobblestone_wall; boulderTopperMeta = 0;
        		
        		// Test the spike here by seeing if cobblestone remains as cobblestone.
        		if (biomeCobblestoneBlock==Blocks.mossy_cobblestone)
        		{
        			// Try to make mossy cobblestone wall
        			boulderTopperMeta = 1;
        		}
        		else if (biomeCobblestoneBlock==Blocks.sandstone)
        		{
        			// Try a sandstone wall--use a slab otherwise
        			boulderTopperBlock = Block.getBlockFromName(ModObjects.sandstoneWallUTD);
        			if (boulderTopperBlock==null) {boulderTopperBlock = Blocks.sandstone;}
        		}
        		else if (biomeCobblestoneBlock!=Blocks.cobblestone)
        		{
        			boulderTopperBlock = biomeCobblestoneBlock;
        		}
    		}
    		else
    		{
    			// Put stairs on top of the boulder
    			boulderTopperBlock = biomeStoneStairsBlock;
    			boulderTopperMeta = StructureVillageVN.getMetadataWithOffset(boulderTopperBlock, (new int[]{3,0,2,1})[boulderOrientation], coordBaseMode);
    		}
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, boulderTopperBlock, boulderTopperMeta);
    		
    		switch(boulderOrientation)
    		{
    		case 0:
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 2, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeDirtBlock, biomeDirtMeta);
    			break;
    		case 1:
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 1, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtBlock, biomeDirtMeta);
    			break;
    		case 2:
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 3, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtBlock, biomeDirtMeta);
    			break;
    		case 3:
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeStoneStairsBlock, StructureVillageVN.getMetadataWithOffset(biomeStoneStairsBlock, 0, coordBaseMode));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeDirtBlock, biomeDirtMeta);
    			break;
    		}
    		break;
    		
    	case 4: // Campfire
    		
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 0, campfireBlock, campfireMeta);
    		
    		break;
    		
    	case 5: // Campfire over hay in bin
    		
    		// Foundation
    		//for (int i=-1 ; i<=1; i++) {for (int l=-1 ; l<=1; l++) {if (i==0 || l==0) {
    		//	BlueprintData.addPlaceBlock(blueprint, i, -1, l, biomeGrassBlock, biomeGrassMeta);
    		//	BlueprintData.addFillBelowTo(blueprint, i, -2, l, biomeDirtBlock, biomeDirtMeta);
    		//}}}
    		
			// Left
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, -1, 0, 0, biomeTrapdoorBlock, coordBaseMode%2==0 ? 6 : 4);
			// Right
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 1, 0, 0, biomeTrapdoorBlock, coordBaseMode%2==0 ? 7 : 5);
			// Front
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, -1, biomeTrapdoorBlock, (new int[]{4, 7, 5, 6})[coordBaseMode]);
			// Back
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 1, biomeTrapdoorBlock, (new int[]{5, 6, 4, 7})[coordBaseMode]);
    		
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 0, Blocks.hay_block, 0);
			
			// Campfire
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 1, 0, campfireBlock, campfireMeta);
			
    		break;
    		
    	case 6: // Torch on a cobblestone wall
    		
    		boulderTopperBlock=Blocks.cobblestone_wall; boulderTopperMeta=0;
    		if (biomeCobblestoneBlock==Blocks.mossy_cobblestone)
    		{
    			// Try to make mossy cobblestone wall
    			boulderTopperMeta = 1;
    		}
    		else if (biomeCobblestoneBlock==Blocks.sandstone)
    		{
    			// Try a sandstone wall--use a slab otherwise
    			boulderTopperBlock = Block.getBlockFromName(ModObjects.sandstoneWallUTD);
    			if (boulderTopperBlock==null) {boulderTopperBlock = Blocks.sandstone;}
    		}
    		else if (biomeCobblestoneBlock!=Blocks.cobblestone)
    		{
    			boulderTopperBlock = biomeCobblestoneBlock;
    		}
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, boulderTopperBlock, boulderTopperMeta);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 1, 0, Blocks.torch, 0);
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
