package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import net.minecraft.block.Block;
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
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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
			if (this.coordBaseMode.getHorizontalIndex()==0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			// Eastward
			if (this.coordBaseMode.getHorizontalIndex()!=3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()%2==0 ? 2 : 4), EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.coordBaseMode.getHorizontalIndex()!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()%2==1 ? 2 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.coordBaseMode.getHorizontalIndex()==3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.WEST, this.getComponentType());}
			
			
			// Attach non-road structures
			
			// Structure 1, left-hand side, near the bell
			if (this.coordBaseMode.getHorizontalIndex()==0) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(4)-1), EnumFacing.WEST, this.getComponentType());}
			if (this.coordBaseMode.getHorizontalIndex()==1) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(4)+1), this.boundingBox.minY, this.boundingBox.minZ+(-1), EnumFacing.NORTH, this.getComponentType());}
			if (this.coordBaseMode.getHorizontalIndex()==2) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(4)-1), EnumFacing.WEST, this.getComponentType());}
			if (this.coordBaseMode.getHorizontalIndex()==3) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(4)), this.boundingBox.minY, this.boundingBox.minZ+(-1), EnumFacing.NORTH, this.getComponentType());}
			
			// Structure 2, back side, along the longer side
			if (this.coordBaseMode.getHorizontalIndex()==0) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(6)+3), this.boundingBox.minY, this.boundingBox.maxZ+(1), EnumFacing.SOUTH, this.getComponentType());}
			if (this.coordBaseMode.getHorizontalIndex()==1) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), EnumFacing.WEST, this.getComponentType());}
			if (this.coordBaseMode.getHorizontalIndex()==2) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), EnumFacing.NORTH, this.getComponentType());}
			if (this.coordBaseMode.getHorizontalIndex()==3) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.maxX+(1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), EnumFacing.EAST, this.getComponentType());}
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
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(2, 3),
        			this.getYWithOffset(2),
        			this.getZWithOffset(2, 3));
        	
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
        	
        	// Top layer is grass path
        	for (int i=0; i<=10; i++)
        	{
        		for (int j=1; j<=6; j++)
            	{
        			this.setBlockState(world, biomeGrassState, i, 0, j, structureBB);
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, i, -1, j, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, i, 1, j, structureBB);
        			// Set grass path after fill so that the area is level
        			StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(i, j), this.getYWithOffset(0), this.getZWithOffset(i, j));
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
        		this.setBlockState(world, biomeGrassState, offset_xy[0], 0, offset_xy[1], structureBB);
        	}
        	
        	// Nodules at the end
        	for (int i=4; i<=6; i++) {for (int j=0; j<=0; j++) {
        			this.setBlockState(world, biomeGrassState, i, 0, j, structureBB);
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, i, -1, j, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, i, 1, j, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(i, j), this.getYWithOffset(0), this.getZWithOffset(i, j));
            }}
        	for (int i=11; i<=11; i++) {for (int j=2; j<=4; j++) {
        			this.setBlockState(world, biomeGrassState, i, 0, j, structureBB);
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, i, -1, j, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, i, 1, j, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(i, j), this.getYWithOffset(0), this.getZWithOffset(i, j));
            }}
        	
        	// Single wood platform for the "bell"
        	this.setBlockState(world, biomeDirtState, 2, 0, 3, structureBB);
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
            	}
            	
            	this.setBlockState(world, concreteBlockstate, 2, 1, 3, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomePlankState, 2, 1, 3, structureBB);
        	}
        	// Wood trapdoor hatching
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 6 : 4), 1, 1, 3, structureBB);
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 7 : 5), 3, 1, 3, structureBB);
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[this.coordBaseMode.getHorizontalIndex()]), 2, 1, 2, structureBB);
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{5, 6, 4, 7})[this.coordBaseMode.getHorizontalIndex()]), 2, 1, 4, structureBB);
        	
        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 2;
    			int signYBB = 2;
    			int signZBB = 3;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerXBB = 5;
    			int bannerZBB = 4;
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
        			{2, 1, 5, -1, 0},
        			{4, 1, 2, -1, 0},
        			{9, 1, 4, -1, 0},
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
    		this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
    		
            switch (this.coordBaseMode)
            {
	        	case NORTH:
	        	case SOUTH:
                    this.boundingBox = new StructureBoundingBox(posX, 64+1, posZ, posX + width, 64+1+height, posZ + depth);
                    break;
                default:
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode + ", horiz index: " + this.coordBaseMode.getHorizontalIndex()
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
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.wall_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks.getDefaultState(), this.materialType, this.biome);
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1 -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(8, 1),
        			this.getYWithOffset(1),
        			this.getZWithOffset(8, 1));
        	
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
        	
            // Encircle the well with path
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
                            StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j));
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
            		int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i, j), 0, this.getZWithOffset(i, j))).down().getY();
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, i, k+2-this.boundingBox.minY, j, structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j));
                   	}
                    
                    k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(j, i), 0, this.getZWithOffset(j, i))).down().getY();
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, j, k+2-this.boundingBox.minY, i, structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(j, i), k, this.getZWithOffset(j, i));
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
            	int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uw[0], uw[2]), 0, this.getZWithOffset(uw[0], uw[2]))).down().getY() + uw[1];
            	if (k > -1)
                {
                	this.setBlockState(world, biomeGrassState, uw[0], k - this.boundingBox.minY, uw[2], structureBB);
               	}
            }
            
            // One block of water - probably a mistake but whatever
            /*
            int kw = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(4, 7), this.getZWithOffset(4, 7)) - 2;
            if (kw > -1)
            {
            	this.setBlockState(world, Blocks.flowing_water, 0, 4, kw - this.boundingBox.minY, 7, structureBB);
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
            		decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 0, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.boundingBox.minY;
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
            	ArrayList<BlueprintData> decorBlueprint = getRandomTaigaDecorBlueprint(this.materialType, this.biome, this.coordBaseMode, randomFromXYZ);
            	
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
            
        	
        	// Create well
        	this.fillWithBlocks(world, structureBB, 2, 1, 2, 6, 2, 6, biomeCobblestoneState, biomeCobblestoneState, false);
        	// Bottom is dirt with inset mossy cobblestone blocks
        	this.fillWithBlocks(world, structureBB, 2, 0, 2, 6, 0, 6, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 3, 0, 3, 5, 0, 5, biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);
        	
        	// Clear above and set foundation
        	for (int u=2; u<=6; u++) {for (int w=2; w<=6; w++) {
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, -1, w, structureBB); // Foundation
        			this.clearCurrentPositionBlocksUpwards(world, u, 3, w, structureBB);
            }}
        	
        	// Fill basin with water
        	this.fillWithBlocks(world, structureBB, 3, 1, 3, 5, 2, 5, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
        	
        	// Place a couple specific stone/cobblestone blocks in the well
        	this.setBlockState(world, biomeMossyCobblestoneState, 3, 2, 2, structureBB);
        	this.setBlockState(world, biomeMossyCobblestoneState, 2, 2, 4, structureBB);
        	this.setBlockState(world, biomeCobblestoneState, 3, 0, 3, structureBB);
        	
        	// Add fence posts
        	this.fillWithBlocks(world, structureBB, 2, 3, 2, 6, 5, 6, biomeFenceState, biomeFenceState, false);
        	this.fillWithAir(world, structureBB, 2, 3, 3, 6, 4, 5);
        	this.fillWithAir(world, structureBB, 3, 3, 2, 5, 4, 6);
        	this.fillWithAir(world, structureBB, 3, 5, 3, 5, 5, 5);
        	// Add log roof
        	//IBlockState roofLogState = biomeLogHorAlongState.getBlock().getStateFromMeta(biomeLogHorAlongState.getBlock().getMetaFromState(biomeLogHorAlongState) + (this.coordBaseMode.getHorizontalIndex()%2==0 ? 4 : 0));
        	IBlockState roofLogState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.coordBaseMode.getHorizontalIndex(), false);
        	
        	this.fillWithBlocks(world, structureBB, 3, 6, 2, 5, 6, 6, roofLogState, roofLogState, false);
        	this.fillWithBlocks(world, structureBB, 2, 5, 2, 2, 5, 6, roofLogState, roofLogState, false);
        	this.fillWithBlocks(world, structureBB, 6, 5, 2, 6, 5, 6, roofLogState, roofLogState, false);
        	// Add torches
        	for (int[] uvwm : new int[][]{
        		{2, 5, 1, new int[]{4,1,3,2}[this.coordBaseMode.getHorizontalIndex()%4]},
        		{6, 5, 1, new int[]{4,1,3,2}[this.coordBaseMode.getHorizontalIndex()%4]},
        		// Banner side
        		{2, 5, 7, new int[]{3,2,4,1}[this.coordBaseMode.getHorizontalIndex()%4]},
        		{6, 5, 7, new int[]{3,2,4,1}[this.coordBaseMode.getHorizontalIndex()%4]},
        	})
        	{
        		world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
        	}
            
            
            // Colored block where bell used to be
            if (GeneralConfig.useVillageColors)
            {
            	int metaBase = ((int)world.getSeed()%4+this.coordBaseMode.getHorizontalIndex())%4; // Procedural based on world seed and base mode
            	
            	BlockPos uvw = new BlockPos(4, 5, 4); // Starting position of the block cluster. Use lowest X, Z.
            	int metaCycle = (metaBase+Math.abs(this.getXWithOffset(uvw.getX(), uvw.getZ())%2 - (this.getZWithOffset(uvw.getX(), uvw.getZ())%2)*3) + uvw.getY())%4; // Procedural based on block X, Y, Z 
            	
            	if (GeneralConfig.addConcrete)
            	{
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            	}
            	else
            	{
            		this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor), 4, 5, 4, structureBB);
            	}
            }
            else
            {
            	this.setBlockState(world, biomePlankState, 4, 5, 4, structureBB);
            }
            
            
            // Signs
            if (GeneralConfig.nameSign)
            {
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode.getHorizontalIndex(), true)), 2); // Facing away from you
    			world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
    			
    			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(2, this.coordBaseMode.getHorizontalIndex(), true)), 2); // Facing toward you
    			world.setTileEntity(new BlockPos(signX2, signY, signZ2), signContents2);
            }
            
            
    		
			
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
    			int bannerXBB = 7;
    			int bannerZBB = 8;
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
                
                // Place a foundation
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
    		else
    		{
				int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(7, 8), 0, this.getZWithOffset(7, 8))).down().getY();
				if (k > -1)
				{
					this.setBlockState(world, biomeGrassState, 7, k-1-this.boundingBox.minY, 8, structureBB);
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
	public static ArrayList<BlueprintData> getRandomTaigaDecorBlueprint(MaterialType materialType, BiomeGenBase biome, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 7;
		return getTaigaDecorBlueprint(random.nextInt(decorCount), materialType, biome, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getTaigaDecorBlueprint(int decorType, MaterialType materialType, BiomeGenBase biome, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		int horizIndex = coordBaseMode.getHorizontalIndex();
		
		// Generate per-material blocks
		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone.getDefaultState(), materialType, biome);
    	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getDefaultState(), materialType, biome);
    	IBlockState biomeStoneStairsState = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_stairs.getDefaultState(), materialType, biome);
    	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks.getDefaultState(), materialType, biome);
    	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor.getDefaultState(), materialType, biome);
    	IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), horizIndex);
    	
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
    				BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, -1, i+(shift?1:0), biomePlankState);
    				BlueprintData.addFillBelowTo(blueprint, 0, -2, i+(shift?1:0), biomeDirtState);
    			}
    			
    			// Left
    			BlueprintData.addFillWithBlocks(blueprint, -1, 0, -2+(shift?1:0), -1, 0, 1+(shift?1:0), biomeTrapdoorState.getBlock().getStateFromMeta(horizIndex%2==0 ? 6 : 4));
    			// Right
    			BlueprintData.addFillWithBlocks(blueprint, 1, 0, -2+(shift?1:0), 1, 0, 1+(shift?1:0), biomeTrapdoorState.getBlock().getStateFromMeta(horizIndex%2==0 ? 7 : 5));
    			// Front
    			BlueprintData.addFillWithBlocks(blueprint, 0, 0, -3+(shift?1:0), 0, 0, -3+(shift?1:0), biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[horizIndex]));
    			// Back
    			BlueprintData.addFillWithBlocks(blueprint, 0, 0, 2+(shift?1:0), 0, 0, 2+(shift?1:0), biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{5, 6, 4, 7})[horizIndex]));
    			
    			break;
    			
    		case 1:
    			// Base
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), -1, 0, 1+(shift?1:0), -1, 0, biomePlankState);
    			// Foundation
    			for (int i=-2 ; i<=1; i++) {BlueprintData.addPlaceBlock(blueprint, i+(shift?1:0), -2, 0, biomeDirtState);}
    			
    			// Left
    			BlueprintData.addFillWithBlocks(blueprint, -3+(shift?1:0), 0, 0, -3+(shift?1:0), 0, 0, biomeTrapdoorState.getBlock().getStateFromMeta(horizIndex%2==0 ? 6 : 4));
    			// Right
    			BlueprintData.addFillWithBlocks(blueprint, 2+(shift?1:0), 0, 0, 2+(shift?1:0), 0, 0, biomeTrapdoorState.getBlock().getStateFromMeta(horizIndex%2==0 ? 7 : 5));
    			// Front
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), 0, -1, 1+(shift?1:0), 0, -1, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[horizIndex]));
    			// Back
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), 0, 1, 1+(shift?1:0), 0, 1, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{5, 6, 4, 7})[horizIndex]));
    			break;
    		}
    		break;
    		
    	case 1: // Large boulder
    		// Central boulder is in the same place
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeCobblestoneState);
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeDirtState); // Foundation
    		
    		switch (random.nextInt(4))
    		{
    		case 0: // Facing you
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeCobblestoneState);
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 3, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 3, coordBaseMode)));
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 2, coordBaseMode)));
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 1, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 1, biomeDirtState); // Foundation
    			break;
    		case 1: // Facing left
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeCobblestoneState);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 0, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 0, coordBaseMode)));
    			BlueprintData.addPlaceBlock(blueprint, 1, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 1, coordBaseMode)));
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, -1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 3, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, -1, biomeDirtState); // Foundation
    			break;
    		case 2: // Facing away
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeCobblestoneState);
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 2, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 2, coordBaseMode)));
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, -1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 3, coordBaseMode)));
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, -1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 0, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeDirtState); // Foundation
    			break;
    		case 3: // Facing right
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeCobblestoneState);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 1, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 1, coordBaseMode)));
    			BlueprintData.addPlaceBlock(blueprint, -1, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 0, coordBaseMode)));
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 2, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeDirtState); // Foundation
    			break;
    		}
    		break;
    	
    	case 2: // Small boulder with spike
    		genericBoolean=true;
    	case 3: // Small boulder without spike
    		// Central boulder is in the same place
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeCobblestoneState);
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeDirtState); // Foundation
    		
    		Block boulderTopperBlock; int boulderTopperMeta;
    		int boulderOrientation = random.nextInt(4);
    		
    		if (genericBoolean)
    		{
    			// Put a spike on top of the boulder
    			
    			boulderTopperBlock = Blocks.cobblestone_wall; boulderTopperMeta = 0;
        		
        		// Test the spike here by seeing if cobblestone remains as cobblestone.
    			Block biomeCobblestoneBlock = biomeCobblestoneState.getBlock();
        		if (biomeCobblestoneBlock==Blocks.mossy_cobblestone)
        		{
        			// Try to make mossy cobblestone wall
        			boulderTopperMeta = 1;
        		}
        		else if (biomeCobblestoneBlock==Blocks.sandstone)
        		{
        			// Try a sandstone wall--use a slab otherwise
        			// TODO - mod sandstone slabs?
        			boulderTopperBlock=null;
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
    			boulderTopperBlock = biomeStoneStairsState.getBlock();
    			boulderTopperMeta = StructureVillageVN.getMetadataWithOffset(boulderTopperBlock, (new int[]{3,0,2,1})[boulderOrientation], coordBaseMode);
    		}
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, boulderTopperBlock.getStateFromMeta(boulderTopperMeta));
    		
    		switch(boulderOrientation)
    		{
    		case 0:
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 2, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeDirtState);
    			break;
    		case 1:
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 1, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtState);
    			break;
    		case 2:
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 3, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtState);
    			break;
    		case 3:
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeStoneStairsState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeStoneStairsState.getBlock(), 0, coordBaseMode)));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeDirtState);
    			break;
    		}
    		break;
    		
    	case 4: // Campfire
    		
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 0, campfireState);
    		
    		break;
    		
    	case 5: // Campfire over hay in bin
    		
    		// Foundation
    		//for (int i=-1 ; i<=1; i++) {for (int l=-1 ; l<=1; l++) {if (i==0 || l==0) {
    		//	BlueprintData.addFillBelowTo(blueprint, i, -2, l, biomeDirtState);
    		//	BlueprintData.addPlaceBlock(blueprint, i, -1, l, biomeGrassState);
    		//}}}
    		
			// Left
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, -1, 0, 0, biomeTrapdoorState.getBlock().getStateFromMeta(horizIndex%2==0 ? 6 : 4));
			// Right
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 1, 0, 0, biomeTrapdoorState.getBlock().getStateFromMeta(horizIndex%2==0 ? 7 : 5));
			// Front
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, -1, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[horizIndex]));
			// Back
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 1, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{5, 6, 4, 7})[horizIndex]));
    		
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 0, Blocks.hay_block.getDefaultState());
			
			// Campfire
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 1, 0, campfireState);
			
    		break;
    		
    	case 6: // Torch on a cobblestone wall
    		
    		boulderTopperBlock=Blocks.cobblestone_wall; boulderTopperMeta=0;
    		if (biomeCobblestoneState.getBlock()==Blocks.mossy_cobblestone)
    		{
    			// Try to make mossy cobblestone wall
    			boulderTopperMeta = 1;
    		}
    		else if (biomeCobblestoneState.getBlock()==Blocks.sandstone)
    		{
    			// Try a sandstone wall--use a slab otherwise
    			// TODO - mod standstone slab?
    			boulderTopperBlock=null;
    			if (boulderTopperBlock==null) {boulderTopperBlock = Blocks.sandstone;}
    		}
    		else if (biomeCobblestoneState.getBlock()!=Blocks.cobblestone)
    		{
    			boulderTopperBlock = biomeCobblestoneState.getBlock();
    		}
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, boulderTopperBlock.getStateFromMeta(boulderTopperMeta));
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 1, 0, Blocks.torch.getStateFromMeta(0));
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
