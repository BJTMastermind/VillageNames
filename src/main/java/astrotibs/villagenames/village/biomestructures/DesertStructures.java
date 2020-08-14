package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
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

public class DesertStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Fountain with building --- //
	
	public static class DesertMeetingPoint1 extends StartVN
    {
    	public DesertMeetingPoint1() {}
    	
    	public DesertMeetingPoint1(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);

    		int width = 9;
    		int depth = 8;
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
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			if (this.coordBaseMode.getHorizontalIndex()%2!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 3, EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.coordBaseMode.getHorizontalIndex()%2==0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 3, EnumFacing.WEST, this.getComponentType());
			
			// Attach a non-road structure
			int strucX=0; int strucZ=0; EnumFacing coordBaseMode=EnumFacing.SOUTH;
			
			if (this.coordBaseMode.getHorizontalIndex()%2==0)
			{
				strucX=this.boundingBox.maxX + 1; strucZ=this.boundingBox.minZ + random.nextInt(3)+1; coordBaseMode=EnumFacing.EAST;
			}
			else 
			{
				strucX=this.boundingBox.minX + random.nextInt(3)+1; strucZ=this.boundingBox.maxZ + 1; coordBaseMode=EnumFacing.SOUTH;
			}
			
			StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, strucX, this.boundingBox.minY, strucZ, coordBaseMode, this.getComponentType());
		}
    	
		/*
		 * Construct the structure
		 */
    	@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
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
        			this.getXWithOffset(6, 4),
        			this.getYWithOffset(2),
        			this.getZWithOffset(6, 4));
        	this.townColor = villageNBTtag.getInteger("townColor");
        	this.townColor2 = villageNBTtag.getInteger("townColor2");
        	// Generate additional colors to be used in the town
        	this.townColorA = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2}, random, false);
        	this.townColorB = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA}, random, false);
        	this.townColorC = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA, this.townColorB}, random, false);
        	
        	// Set sandstone ground and clear area above
        	this.fillWithBlocks(world, structureBB, 3, 0, 0, 9, 0, 8, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	for (int x = 3; x <= 9; ++x) {for (int z = 0; z <= 8; ++z)
        	{
        		this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), x, -1, z, structureBB); // Foundation
        		this.clearCurrentPositionBlocksUpwards(world, x, 1, z, structureBB);
        	}}
        	this.fillWithBlocks(world, structureBB, 1, 0, 1, 2, 0, 7, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	for (int x = 1; x <= 2; ++x) {for (int z = 1; z <= 7; ++z)
        	{
        		this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), x, -1, z, structureBB); // Foundation
        		this.clearCurrentPositionBlocksUpwards(world, x, 1, z, structureBB);
        	}}
        	this.fillWithBlocks(world, structureBB, 0, 0, 3, 0, 0, 5, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	for (int x = 0; x <= 0; ++x) {for (int z = 3; z <= 5; ++z)
        	{
        		this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), x, -1, z, structureBB); // Foundation
        		this.clearCurrentPositionBlocksUpwards(world, x, 1, z, structureBB);
        	}}
        	
        	// Set well rim
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 2, 1, 2, 6, 1, 6, concreteBlockstate, concreteBlockstate, false);
        	}
        	else
        	{
        		this.fillWithBlocks(world, structureBB, 2, 1, 2, 6, 1, 6, Blocks.sandstone.getStateFromMeta(2), Blocks.sandstone.getStateFromMeta(2), false);
        	}
        	// Air in the corners
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 1, 2, structureBB);
            this.setBlockState(world, Blocks.air.getDefaultState(), 2, 1, 6, structureBB);
            this.setBlockState(world, Blocks.air.getDefaultState(), 6, 1, 6, structureBB);
            this.setBlockState(world, Blocks.air.getDefaultState(), 6, 1, 2, structureBB);
            
            // Sand underneath the rim
            this.fillWithBlocks(world, structureBB, 3, 0, 2, 5, 0, 2, Blocks.sand.getDefaultState(), Blocks.sand.getDefaultState(), false);
            this.fillWithBlocks(world, structureBB, 3, 0, 6, 5, 0, 6, Blocks.sand.getDefaultState(), Blocks.sand.getDefaultState(), false);
            this.fillWithBlocks(world, structureBB, 2, 0, 3, 2, 0, 5, Blocks.sand.getDefaultState(), Blocks.sand.getDefaultState(), false);
            this.fillWithBlocks(world, structureBB, 6, 0, 3, 6, 0, 5, Blocks.sand.getDefaultState(), Blocks.sand.getDefaultState(), false);
            
            // Water in the fountain
            this.fillWithBlocks(world, structureBB, 3, 1, 3, 5, 1, 5, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
            
            // Spout
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 3, 4, Blocks.sandstone.getStateFromMeta(2), Blocks.sandstone.getStateFromMeta(2), false);
            if (GeneralConfig.decorateVillageCenter)
        	{
        		/*Object[] tryConcrete = ModObjects.chooseModConcrete(townColor2);
            	Block concreteBlock = Blocks.stained_hardened_clay; int concreteMeta = townColor2;
            	if (tryConcrete != null) {concreteBlock = (Block) tryConcrete[0]; concreteMeta = (Integer) tryConcrete[1];}
            	
            	this.setBlockState(world, concreteBlock, concreteMeta, 4, 4, 4, structureBB);*/
            	this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 4, 4, 4, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, Blocks.hardened_clay.getDefaultState(), 4, 4, 4, structureBB);
        	}
            // Just the tip
            for (int[] uvwm : new int[][]{
            	{4, 5, 4, 0},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
            }
            
            // Cactus
            int tileU = 5;
            int tileV = 2;
            int tileW = 2;
            
            BlockPos potPos = new BlockPos(this.getXWithOffset(tileU, tileW), this.getYWithOffset(tileV), this.getZWithOffset(tileU, tileW));
            
            TileEntity flowerPotWithCactus = (new BlockFlowerPot()).createNewTileEntity(world, 9); // 9 is cactus
            world.setBlockState(potPos, Blocks.flower_pot.getDefaultState(), 2);
            world.setTileEntity(potPos, flowerPotWithCactus);
            
            
        	// Sign
            int signXBB = 6;
			int signYBB = 2;
			int signZBB = 4;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
    			int bannerXBB = 7;
    			int bannerZBB = 1;
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
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), bannerXBB, bannerYBB-3, bannerZBB, structureBB);
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
        			{1, 1, 1, -1, 0},
        			{5, 1, 0, -1, 0},
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
	
	
	// --- Desert Well --- //
	
	public static class DesertMeetingPoint2 extends StartVN
    {
    	public DesertMeetingPoint2() {}
    	
    	public DesertMeetingPoint2(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);

    		int width = 11;
    		int depth = 11;
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
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()>=2 ? 5 : 4), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()>=2 ? 5 : 4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()<=1 ? 5 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()<=1 ? 5 : 4), EnumFacing.WEST, this.getComponentType());
		}
    	
		/*
		 * Construct the structure
		 */
    	@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
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
        			this.getXWithOffset(8, 1),
        			this.getYWithOffset(1),
        			this.getZWithOffset(8, 1));
        	this.townColor = villageNBTtag.getInteger("townColor");
        	this.townColor2 = villageNBTtag.getInteger("townColor2");
        	// Generate additional colors to be used in the town
        	this.townColorA = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2}, random, false);
        	this.townColorB = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA}, random, false);
        	this.townColorC = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA, this.townColorB}, random, false);
        	
        	// Set sandstone ground and clear area above
        	this.fillWithBlocks(world, structureBB, 1, 0, 1, 10, 0, 10, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	
        	// Fill foundation
        	for (int x = 1; x <= 10; ++x)
            {
	        	for (int z = 1; z <= 10; ++z)
	            {
	                this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), x, -1, z, structureBB); // Foundation
	                this.clearCurrentPositionBlocksUpwards(world, x, 1, z, structureBB);
                }
            }
        	
        	// Path hitches at the ends
        	this.fillWithBlocks(world, structureBB, 0, 0, 5, 0, 0, 7, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	for (int w = 5; w <= 7; ++w)
        	{
        		this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), 0, -1, w, structureBB); // Foundation
        		this.clearCurrentPositionBlocksUpwards(world, 0, 1, w, structureBB);
        	}
        	this.fillWithBlocks(world, structureBB, 11, 0, 4, 11, 0, 6, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	for (int w = 4; w <= 6; ++w)
        	{
        		this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), 11, -1, w, structureBB); // Foundation
        		this.clearCurrentPositionBlocksUpwards(world, 11, 1, w, structureBB);
        	}
        	this.fillWithBlocks(world, structureBB, 4, 0, 0, 6, 0, 0, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	for (int w = 4; w <= 6; ++w)
        	{
        		this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), w, -1, 0, structureBB); // Foundation
        		this.clearCurrentPositionBlocksUpwards(world, w, 1, 0, structureBB);
        	}
        	this.fillWithBlocks(world, structureBB, 5, 0, 11, 7, 0, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	for (int w = 5; w <= 7; ++w)
        	{
        		this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), w, -1, 11, structureBB); // Foundation
        		this.clearCurrentPositionBlocksUpwards(world, w, 1, 11, structureBB);
        	}
        	
        	// Set sand underneath the fountain
        	this.fillWithBlocks(world, structureBB, 3, 0, 3, 8, 0, 8, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	
        	// Set well rim
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 3, 1, 3, 8, 1, 8, concreteBlockstate, concreteBlockstate, false);
        	}
        	else
        	{
        		this.fillWithBlocks(world, structureBB, 3, 1, 3, 8, 1, 8, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	}
        	
        	// Water in the fountain
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 7, 1, 7, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
            
            // Sandstone slab roof
            this.fillWithBlocks(world, structureBB, 4, 4, 4, 7, 4, 7, Blocks.stone_slab.getStateFromMeta(1), Blocks.stone_slab.getStateFromMeta(1), false);
            
        	// Columns
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 4, 4, Blocks.sandstone.getStateFromMeta(2), Blocks.sandstone.getStateFromMeta(2), false);
            this.fillWithBlocks(world, structureBB, 4, 1, 7, 4, 4, 7, Blocks.sandstone.getStateFromMeta(2), Blocks.sandstone.getStateFromMeta(2), false);
            this.fillWithBlocks(world, structureBB, 7, 1, 7, 7, 4, 7, Blocks.sandstone.getStateFromMeta(2), Blocks.sandstone.getStateFromMeta(2), false);
            this.fillWithBlocks(world, structureBB, 7, 1, 4, 7, 4, 4, Blocks.sandstone.getStateFromMeta(2), Blocks.sandstone.getStateFromMeta(2), false);
            
            // Torches
            for (int[] uvwm : new int[][]{
            	{4, 5, 4, 0},
            	{4, 5, 7, 0},
            	{7, 5, 7, 0},
            	{7, 5, 4, 0},
            })
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2])), Blocks.torch.getStateFromMeta(uvwm[3]), 2);
            }
            
            
            // Roof of the well
            if (GeneralConfig.decorateVillageCenter)
            {
            	BlockPos uvw = new BlockPos(5, 4, 5); // Starting position of the block cluster. Use lowest X, Z.
            	
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
            		this.fillWithBlocks(world, structureBB, 5, 4, 5, 6, 4, 6, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), Blocks.stained_hardened_clay.getStateFromMeta(townColor2), false);
            	}
            }
            else
            {
            	this.fillWithBlocks(world, structureBB, 5, 4, 5, 6, 4, 6, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            }
            
            
        	// Sign
            int signXBB = 8;
			int signYBB = 1;
			int signZBB = 1;
            int signX = this.getXWithOffset(signXBB, signZBB);
            int signY = this.getYWithOffset(signYBB);
            int signZ = this.getZWithOffset(signXBB, signZBB);
    		
    		String namePrefix = villageNBTtag.getString("namePrefix");
    		String nameRoot = villageNBTtag.getString("nameRoot");
    		String nameSuffix = villageNBTtag.getString("nameSuffix");
    		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
    		
			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
    		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
    		
    		
			// Banner
    		if (GeneralConfig.decorateVillageCenter)
    		{
    			int bannerXBB = 10;
    			int bannerZBB = 10;
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
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
            	// Set the banner and its orientation
				world.setBlockState(new BlockPos(bannerX, bannerY, bannerZ), Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.coordBaseMode.getHorizontalIndex(), false)));
				
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
        			{10, 1, 8, -1, 0},
        			{1, 1, 10, -1, 0},
        			{7, 1, 10, -1, 0},
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
	
	
	// --- Desert Market --- //

	public static class DesertMeetingPoint3 extends StartVN
    {
    	public DesertMeetingPoint3() {}
    	
    	public DesertMeetingPoint3(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);

    		int width = 14;
    		int depth = 14;
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
    		if (this.coordBaseMode.getHorizontalIndex()!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + new int[]{1,5,1,7}[this.coordBaseMode.getHorizontalIndex()], this.boundingBox.maxY - 5, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
        	// Eastward
        	if (this.coordBaseMode.getHorizontalIndex()!=1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + new int[]{6,4,6,1}[this.coordBaseMode.getHorizontalIndex()], EnumFacing.EAST, this.getComponentType());}
			// Southward
        	if (this.coordBaseMode.getHorizontalIndex()!=2) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + new int[]{1,6,4,6}[this.coordBaseMode.getHorizontalIndex()], this.boundingBox.maxY - 5, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.coordBaseMode.getHorizontalIndex()!=3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + new int[]{7,1,5,4}[this.coordBaseMode.getHorizontalIndex()], EnumFacing.WEST, this.getComponentType());}
		}
    	
		/*
		 * Construct the structure
		 */
    	@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome);
        	IBlockState biomeSandstoneWallState = StructureVillageVN.getBiomeSpecificBlock(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome); // TODO - Check for modded sandstone walls
        	
        	if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+2, this.boundingBox.minZ+2,
        						this.boundingBox.maxX-2, this.boundingBox.maxZ-2), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(8, 2),
        			this.getYWithOffset(3),
        			this.getZWithOffset(8, 2));
        	this.townColor = villageNBTtag.getInteger("townColor");
        	this.townColor2 = villageNBTtag.getInteger("townColor2");
        	// Generate additional colors to be used in the town
        	this.townColorA = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2}, random, false);
        	this.townColorB = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA}, random, false);
        	this.townColorC = StructureVillageVN.generateUnusedColor(new int[]{this.townColor, this.townColor2, this.townColorA, this.townColorB}, random, false);
        	
        	
        	// Set ground and clear area above
        	int fillXmin; int fillZmin; int fillXmax; int fillZmax; int clearToHeight = 5;
        	
        	for (Object[] o : new Object[][]{
        		// minX, maxX, minZ, maxZ, groundY, structureHeight, surfaceBlock, surfaceMeta, subsurfaceBlock, subsurfaceMeta 
        		{0, 0, 7, 12, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{1, 2, 4, 12, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{1, 2, 14, 14, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{0, 2, 13, 13, -1, 5, Blocks.sandstone, 0, Blocks.sandstone, 0},
        		{2, 2, 3, 3, -1, 5, Blocks.sandstone, 0, Blocks.sandstone, 0},
        		{3, 3, 14, 14, -1, 5, Blocks.sandstone, 0, Blocks.sandstone, 0},
        		{3, 3, 1, 13, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{3, 3, 0, 0, -1, 5, Blocks.sandstone, 0, Blocks.sandstone, 0},
        		{4, 4, 2, 14, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{4, 4, 0, 1, -1, 5, Blocks.sandstone, 0, Blocks.sandstone, 0},
        		{5, 9, 0, 14, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{10, 11, 0, 13, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{12, 12, 1, 13, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{12, 12, 0, 0, -1, 5, Blocks.sandstone, 0, Blocks.sandstone, 0},
        		{13, 13, 0, 12, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        		{14, 14, 3, 11, 0, 5, Blocks.sand, 0, Blocks.sandstone, 0},
        	})
        	{
        		this.fillWithBlocks(world, structureBB, (Integer)o[0], (Integer)o[4], (Integer)o[2], (Integer)o[1], (Integer)o[4], (Integer)o[3], ((Block)o[6]).getStateFromMeta((Integer)o[7]), ((Block)o[6]).getStateFromMeta((Integer)o[7]), false);
            	for (int x = (Integer)o[0]; x <= (Integer)o[1]; ++x) {for (int z = (Integer)o[2]; z <= (Integer)o[3]; ++z)
            	{
            		this.replaceAirAndLiquidDownwards(world, ((Block)o[8]).getStateFromMeta((Integer)o[9]), x, (Integer)o[4]-1, z, structureBB); // Foundation
            		this.clearCurrentPositionBlocksUpwards(world, (Integer)o[0], Math.max((Integer)o[4], 0)+1, (Integer)o[2], structureBB);
            	}}
        	}
        	// Set sandstone in certain places
        	this.fillWithBlocks(world, structureBB, 7, 0, 0, 7, 0, 4, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 10, 0, 0, 10, 0, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 1, 9, 0, 1, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 3, 9, 0, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 4, 13, 0, 4, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 9, 13, 0, 9, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 5, 8, 0, 8, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 13, 0, 5, 13, 0, 8, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 10, 0, 6, 11, 0, 7, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	this.fillWithBlocks(world, structureBB, 14, 0, 6, 14, 0, 8, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	
        	
        	// Fountain
        	
        	// Rim
        	if (GeneralConfig.decorateVillageCenter)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor2);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor2);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 9, 1, 5, 12, 1, 8, concreteBlockstate, concreteBlockstate, false);
            	
            	/*
            	tryConcrete = ModObjects.chooseModConcrete(townColor2);
            	concreteBlock = Blocks.stained_hardened_clay; concreteMeta = townColor2;
            	if (tryConcrete != null) {concreteBlock = (Block) tryConcrete[0]; concreteMeta = (Integer) tryConcrete[1];}
            	
            	this.fillWithBlocks(world, structureBB, 8, 0, 4, 8, 0, 9, concreteBlock, concreteMeta, concreteBlock, concreteMeta, false);
            	this.fillWithBlocks(world, structureBB, 13, 0, 4, 13, 0, 9, concreteBlock, concreteMeta, concreteBlock, concreteMeta, false);
            	this.fillWithBlocks(world, structureBB, 9, 0, 4, 12, 0, 4, concreteBlock, concreteMeta, concreteBlock, concreteMeta, false);
            	this.fillWithBlocks(world, structureBB, 9, 0, 9, 12, 0, 9, concreteBlock, concreteMeta, concreteBlock, concreteMeta, false);
            	*/
            	//this.fillWithBlocks(world, structureBB, 9, 1, 7, 10, 1, 8, concreteBlock, concreteMeta, concreteBlock, concreteMeta, false);
        	}
        	else
        	{
        		this.fillWithBlocks(world, structureBB, 9, 1, 5, 12, 1, 8, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        	}
        	
        	
        	// Corner posts
    		this.setBlockState(world, Blocks.sandstone.getStateFromMeta(2), 9, 1, 5, structureBB);
    		this.setBlockState(world, Blocks.sandstone.getStateFromMeta(2), 9, 1, 8, structureBB);
    		this.setBlockState(world, Blocks.sandstone.getStateFromMeta(2), 12, 1, 8, structureBB);
    		this.setBlockState(world, Blocks.sandstone.getStateFromMeta(2), 12, 1, 5, structureBB);
        	// Top the corners
    		this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(1), 9, 2, 5, structureBB);
    		this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(1), 9, 2, 8, structureBB);
    		this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(1), 12, 2, 8, structureBB);
    		this.setBlockState(world, Blocks.stone_slab.getStateFromMeta(1), 12, 2, 5, structureBB);
        	
    		// Fill with water
    		this.fillWithBlocks(world, structureBB, 10, 1, 6, 11, 1, 7, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
    		
    		
    		// Market stalls
    		
    		// Frames
    		this.fillWithBlocks(world, structureBB, 7, 1, 0, 7, 3, 0, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 10, 1, 0, 10, 3, 0, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 10, 1, 2, 10, 3, 2, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 7, 1, 2, 7, 3, 2, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 7, 4, 0, 10, 4, 2, Blocks.stone_slab.getStateFromMeta(1), Blocks.stone_slab.getStateFromMeta(1), false);
    		this.fillWithAir(world, structureBB, 8, 4, 1, 9, 4, 1);
    		
    		this.fillWithBlocks(world, structureBB, 1, 1, 5, 1, 4, 5, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 5, 1, 5, 5, 4, 5, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 5, 1, 7, 5, 4, 7, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 1, 1, 7, 1, 4, 7, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 1, 5, 5, 5, 5, 7, Blocks.stone_slab.getStateFromMeta(1), Blocks.stone_slab.getStateFromMeta(1), false);
    		this.fillWithAir(world, structureBB, 2, 5, 6, 4, 5, 6);
    		
    		this.fillWithBlocks(world, structureBB, 4, 1, 11, 4, 3, 11, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 7, 1, 11, 7, 3, 11, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 7, 1, 14, 7, 3, 14, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 4, 1, 14, 4, 3, 14, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 4, 4, 11, 7, 4, 14, Blocks.stone_slab.getStateFromMeta(1), Blocks.stone_slab.getStateFromMeta(1), false);
    		this.fillWithAir(world, structureBB, 5, 4, 12, 6, 4, 13);
    		
    		// Stall contents
    		
    		// Glazed terracotta
    		if (GeneralConfig.addConcrete)
        	{
        		// Square under square awning
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.decorateVillageCenter? townColor:0, (0 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 1 : 0))%4), 5, 1, 13, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.decorateVillageCenter? townColor:0, (1 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 3 : 0))%4), 6, 1, 13, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.decorateVillageCenter? townColor:0, (2 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 1 : 0))%4), 6, 1, 12, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.decorateVillageCenter? townColor:0, (3 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 3 : 0))%4), 5, 1, 12, structureBB);
        		
        		// Halved square under strip awning
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.decorateVillageCenter? townColor2:0, (0 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 1 : 0))%4), 8, 1, 2, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.decorateVillageCenter? townColor2:0, (1 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 3 : 0))%4), 9, 1, 2, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.decorateVillageCenter? townColor2:0, (2 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 1 : 0))%4), 9, 1, 0, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.decorateVillageCenter? townColor2:0, (3 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 3 : 0))%4), 8, 1, 0, structureBB);
        	}
        	else
        	{
        		// Square under awning
        		this.fillWithBlocks(world, structureBB, 5, 1, 12, 6, 1, 13, Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor : 0), false);
        		
        		// Halved square under strip awning
        		this.fillWithBlocks(world, structureBB, 8, 1, 0, 9, 1, 0, Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), false);
        		this.fillWithBlocks(world, structureBB, 8, 1, 2, 9, 1, 2, Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.decorateVillageCenter ? townColor2 : 0), false);
        	}
    		
        	// Cut stone and stairs
        	this.fillWithBlocks(world, structureBB, 2, 1, 6, 4, 1, 6, Blocks.sandstone.getStateFromMeta(2), Blocks.sandstone.getStateFromMeta(2), false);
        	this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.sandstone_stairs, 0)), 1, 1, 6, structureBB);
        	this.setBlockState(world, Blocks.sandstone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.sandstone_stairs, 1)), 5, 1, 6, structureBB);
        	
        	// Various decorations
        	this.setBlockState(world, Blocks.hay_block.getDefaultState(), 5, 1, 0, structureBB);
        	this.setBlockState(world, Blocks.hay_block.getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==1 ? 8 : 4), 3, 1, 2, structureBB);
        	
    		// Flower pots
        	// 1: red flower; 2: yellow flower
        	// 3-6: oak, spruce, birch, jungle saplings
        	// 7-8: red/brown mushrooms
        	// 9: cactus; 10: dead bush; 11: fern
        	// 12-13: acacia, dark oak saplings
        	for (int[] uvwm : new int[][]{
        		// Cactuses
        		{0, 1, 7, 9},
        		{3, 1, 7, 9},
        		{2, 2, 6, 9},
        		{4, 2, 6, 9},
        		{9, 2, 2, 9},
        		// Dead Bushes
        		{1, 1, 4, 10},
        		{2, 1, 5, 10},
        		{2, 1, 7, 10},
        		{3, 2, 6, 10},
        		{4, 1, 5, 10},
        	})
        	{
        		BlockPos potPos = new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2]));
        		TileEntity flowerPotTE = (new BlockFlowerPot()).createNewTileEntity(world, uvwm[3]);
                world.setBlockState(potPos, Blocks.flower_pot.getDefaultState(), 2);
                world.setTileEntity(potPos, flowerPotTE);
        	}
        	
        	
            // Lantern Decor
            int[][] decorUVW = new int[][]{
    			{1, 1, 11},
    			{12, 1, 12},
    			{13, 1, 0},
    			{14, 1, 3},
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
            	
        		// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getRandomDesertDecorBlueprint(this, this.coordBaseMode, randomFromXYZ);//, townColor);
            	
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
        	
        	
        	// Sign
            int signXBB = 8;
			int signYBB = 2;
			int signZBB = 2;
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
    			int bannerZBB = 11;
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
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.replaceAirAndLiquidDownwards(world, Blocks.sandstone.getDefaultState(), bannerXBB, bannerYBB-3, bannerZBB, structureBB);
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
        			{6, 1, 5, -1, 0},
        			{8, 1, 10, -1, 0},
        			{11, 1, 10, -1, 0},
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
	public static ArrayList<BlueprintData> getRandomDesertDecorBlueprint(StartVN startVN, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		int decorCount = 1;
		return getDesertDecorBlueprint(random.nextInt(decorCount), startVN, coordBaseMode, random);//, townColor);
	}
	public static ArrayList<BlueprintData> getDesertDecorBlueprint(int decorType, StartVN startVN, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
        switch (decorType)
        {
    	case 0: // Torch on stained terracotta and cut sandstone
    		
    		BlueprintData.addFillWithBlocks(blueprint, 0, 0, 0, 0, 1, 0, Blocks.sandstone.getStateFromMeta(2));
    		//BlueprintData.addPlaceBlock(blueprint, 0, 2, 0, GeneralConfig.decorateVillageCenter ? Blocks.stained_hardened_clay.getStateFromMeta(townColor) : Blocks.hardened_clay.getDefaultState());
    		BlueprintData.addPlaceBlock(blueprint, 0, 2, 0, Blocks.hardened_clay.getDefaultState());
    		BlueprintData.addPlaceBlock(blueprint, 0, 3, 0, Blocks.torch.getStateFromMeta(0));
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
	
}
