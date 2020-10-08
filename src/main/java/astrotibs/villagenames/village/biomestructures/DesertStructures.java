package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.config.village.VillageGeneratorConfigHandler;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
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
        	if (GeneralConfig.useVillageColors)
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
            if (GeneralConfig.useVillageColors)
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
            for (int[] uvwo : new int[][]{
            	{4, 5, 4, -1},
            })
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 6;
    			int signYBB = 2;
    			int signZBB = 4;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
    			int bannerXBB = 7;
    			int bannerZBB = 1;
    			int bannerYBB = 1;
    			/*
    			if (this.bannerY==0)
    			{
    				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(bannerXBB, bannerZBB), 0, this.getZWithOffset(bannerXBB, bannerZBB))).getY()-this.boundingBox.minY +1;
    				bannerYBB = this.bannerY;
    			}
    			else {bannerYBB = this.bannerY;}
    			*/
    			
    			
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
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
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
        	if (GeneralConfig.useVillageColors)
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
            for (int[] uvwo : new int[][]{
            	{4, 5, 4, -1},
            	{4, 5, 7, -1},
            	{7, 5, 7, -1},
            	{7, 5, 4, -1},
            })
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Roof of the well
            if (GeneralConfig.useVillageColors)
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
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 8;
    			int signYBB = 1;
    			int signZBB = 1;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
    			int bannerXBB = 10;
    			int bannerZBB = 10;
    			int bannerYBB = 1;
    			/*
    			if (this.bannerY==0)
    			{
    				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(bannerXBB, bannerZBB), 0, this.getZWithOffset(bannerXBB, bannerZBB))).getY()-this.boundingBox.minY +1;
    				bannerYBB = this.bannerY;
    			}
    			else {bannerYBB = this.bannerY;}
    			*/
    			
    			
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
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
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
        	if (GeneralConfig.useVillageColors)
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
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor:0, (0 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 1 : 0))%4), 5, 1, 13, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor:0, (1 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 3 : 0))%4), 6, 1, 13, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor:0, (2 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 1 : 0))%4), 6, 1, 12, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor:0, (3 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 3 : 0))%4), 5, 1, 12, structureBB);
        		
        		// Halved square under strip awning
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor2:0, (0 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 1 : 0))%4), 8, 1, 2, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor2:0, (1 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 3 : 0))%4), 9, 1, 2, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor2:0, (2 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 1 : 0))%4), 9, 1, 0, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor2:0, (3 + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex()<2 ? 3 : 0))%4), 8, 1, 0, structureBB);
        	}
        	else
        	{
        		// Square under awning
        		this.fillWithBlocks(world, structureBB, 5, 1, 12, 6, 1, 13, Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? townColor:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 0), false);
        		
        		// Halved square under strip awning
        		this.fillWithBlocks(world, structureBB, 8, 1, 0, 9, 1, 0, Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), false);
        		this.fillWithBlocks(world, structureBB, 8, 1, 2, 9, 1, 2, Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), false);
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

            	int decorHeightY = uvw[1];
            	/*
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
            	*/
            	
        		// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getRandomDesertDecorBlueprint(this.materialType, this.biome, this.coordBaseMode, randomFromXYZ);//, townColor);
            	
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
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 8;
    			int signYBB = 2;
    			int signZBB = 2;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
        	
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
    			int bannerXBB = 10;
    			int bannerZBB = 11;
    			int bannerYBB = 1;
    			/*
    			if (this.bannerY==0)
    			{
    				this.bannerY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(bannerXBB, bannerZBB), 0, this.getZWithOffset(bannerXBB, bannerZBB))).getY()-this.boundingBox.minY +1;
    				bannerYBB = this.bannerY;
    			}
    			else {bannerYBB = this.bannerY;}
    			*/
    			
    			
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
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
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
            }
            
            return true;
        }
    }
    
    
    
    // ------------------ //
    // --- Components --- //
    // ------------------ //
    
    
    // --- Small Animal Pen --- //
    
    public static class DesertAnimalPen1 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"    FF    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	
    	private int averageGroundLevel = -1;
    	
        public DesertAnimalPen1() {}

        public DesertAnimalPen1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static DesertAnimalPen1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertAnimalPen1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
        		// Use these to re-center the side when determining ground level
            	if (this.averageGroundLevel < 0)
                {
            		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	// Sandstone wall that defaults to fence
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoFenceBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoFenceMeta = (Integer)blockObject[1];
        	// Smooth Sandstone Block
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false);
        	if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);}
        	else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);}
        	Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	// Smooth Sandstone Stairs
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence_gate, 0, this.materialType, this.biome); Block biomeFenceGateBlock = (Block)blockObject[0]; int biomeFenceGateMeta = (Integer)blockObject[1];
        	Object[] tryObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)tryObject[0], (Integer)tryObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
        	
        	// Grass
        	for(int[] uuvvww : new int[][]{
            	{1,0,2, 8,0,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.grass, 0, Blocks.grass, 0, false);	
            }
        	
        	
        	// Cut sandstone
        	for(int[] uuvvww : new int[][]{
            	{0,0,1, 9,0,1}, 
            	{0,0,7, 9,0,7}, 
            	{0,0,2, 0,0,6}, 
            	{9,0,2, 9,0,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
        	
        	
        	// Sandstone Walls
        	for(int[] uuvvww : new int[][]{
        		// Perimeter
            	{0,1,1, 9,1,1}, 
            	{0,1,7, 9,1,7}, 
            	{0,1,2, 0,1,6}, 
            	{9,1,2, 9,1,6},
            	// Pavilion posts
            	{0,2,3, 0,3,3}, {2,1,3, 2,3,3}, {0,2,7, 0,3,7}, {2,2,7, 2,3,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, false);	
        		//this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.glowstone, 0, Blocks.glowstone, 0, false);
            }
        	
        	
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
            	{4,0,3, 6,1,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab
        	for(int[] uuvvww : new int[][]{
            	{0,4,3, 2,4,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
            
        	
            // Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{4,0,0, 5,0,0, 3},  
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
            
        	
            // Fence Gates
        	for (int[] uuvvww : new int[][]{
        		{4,1,1, 5,1,1},
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceGateBlock, StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlock, biomeFenceGateMeta, this.coordBaseMode), biomeFenceGateBlock, StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlock, biomeFenceGateMeta, this.coordBaseMode), false);	
            }
        	
        	
            // Water in basin
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{5,1,4}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Clear path for easier entry
            int pathV = GROUND_LEVEL;
            int pathW = -1;
            
            for (int pathU=4; pathU<=5; pathU++)
            {
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
            }
            
            
            // Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{7, 1, 3}, 
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM); // Because horses can escape the pen
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
        		}
            }
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Covered Animal Pen --- //
    
    public static class DesertAnimalPen2 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"    FF    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertAnimalPen2() {}

        public DesertAnimalPen2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static DesertAnimalPen2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertAnimalPen2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	// Sandstone wall that defaults to fence
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoFenceBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoFenceMeta = (Integer)blockObject[1];
        	// Smooth Sandstone Block
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false);
        	if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);}
        	else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);}
        	Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	// Smooth Sandstone Stairs
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence_gate, 0, this.materialType, this.biome); Block biomeFenceGateBlock = (Block)blockObject[0]; int biomeFenceGateMeta = (Integer)blockObject[1];
        	Object[] tryObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)tryObject[0], (Integer)tryObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
        	
        	// Grass
        	for(int[] uuvvww : new int[][]{
            	{1,0,2, 9,0,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.grass, 0, Blocks.grass, 0, false);	
            }
        	
        	
        	// Cut sandstone
        	for(int[] uuvvww : new int[][]{
            	{0,0,1, 9,0,1}, 
            	{0,0,8, 9,0,8}, 
            	{0,0,2, 0,0,7}, 
            	{9,0,2, 9,0,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
        	
        	
        	// Sandstone Walls
        	for(int[] uuvvww : new int[][]{
        		// Perimeter
            	{0,1,1, 9,1,1}, 
            	{0,1,8, 9,1,8}, 
            	{0,1,2, 0,1,7}, 
            	{9,1,2, 9,1,7},
            	// Pavilion posts
            	{0,2,4, 0,3,4}, {9,2,4, 9,3,4}, {0,2,8, 0,3,8}, {9,2,8, 9,3,8}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, false);	
        		//this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.glowstone, 0, Blocks.glowstone, 0, false);
            }
        	
        	
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
            	{4,0,4, 7,1,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab
        	for(int[] uuvvww : new int[][]{
            	{0,4,4, 9,4,8}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
            
        	
            // Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{4,0,0, 5,0,0, 3},  
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
            
        	
            // Fence Gates
        	for (int[] uuvvww : new int[][]{
        		{4,1,1, 5,1,1},
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceGateBlock, StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlock, biomeFenceGateMeta, this.coordBaseMode), biomeFenceGateBlock, StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlock, biomeFenceGateMeta, this.coordBaseMode), false);	
            }
        	
        	
            // Water in basin
            for (int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{5,1,5, 6,1,5}, 
            	}) {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water, 0, Blocks.flowing_water, 0, false);
            }
            
            
            // Clear path for easier entry
            int pathV = GROUND_LEVEL;
            int pathW = -1;
            
            for (int pathU=4; pathU<=5; pathU++)
            {
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
            }
            
            
            // Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{3, 1, 3}, 
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM); // Because horses can escape the pen
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
        		}
            }
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Armorer House --- //
    
    public static class DesertArmorer1 extends StructureVillagePieces.Village
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
        		" FFFFF ",
        		" FFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertArmorer1() {}

        public DesertArmorer1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static DesertArmorer1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertArmorer1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence_gate, 0, this.materialType, this.biome); Block biomeFenceGateBlock = (Block)blockObject[0]; int biomeFenceGateMeta = (Integer)blockObject[1];
        	Object[] tryObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)tryObject[0], (Integer)tryObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
        	
        	// Furnace first because we put the torches on it
        	Object[] graniteBlockObject = ModObjects.chooseModGraniteBlock();
        	Block graniteStairsBlock = ModObjects.chooseModGraniteStairsBlock();
        	Object[] graniteWallObject = ModObjects.chooseModGraniteWallBlock();
        	if (graniteBlockObject==null || graniteStairsBlock==null || graniteWallObject==null)
        	{
            	graniteBlockObject = new Object[]{Blocks.cobblestone, 0};
            	graniteStairsBlock = Blocks.stone_stairs;
            	graniteWallObject = new Object[]{Blocks.cobblestone_wall, 0};
        	}
        	Block graniteBlock=(Block)graniteBlockObject[0]; int graniteMeta=(Integer)graniteBlockObject[1];
        	Block graniteWallBlock=(Block)graniteWallObject[0]; int graniteWallMeta=(Integer)graniteWallObject[1];
        	
        	// Granite block
        	for(int[] uuvvww : new int[][]{
            	{2,1,5, 4,2,5}, {3,3,5, 3,4,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], graniteBlock, graniteMeta, graniteBlock, graniteMeta, false);	
            }
        	// Granite stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,3,5, 2,3,5, 0}, {4,3,5, 4,3,5, 1}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], graniteStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, graniteStairsBlock, this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
        	// Granite wall
        	for(int[] uuvvww : new int[][]{
            	{3,5,5, 3,6,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], graniteWallBlock, graniteWallMeta, graniteWallBlock, graniteWallMeta, false);	
            }
        	
        	
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	blockObject = ModObjects.chooseModBlastFurnaceBlock(2, this.coordBaseMode); Block blastFurnaceBlock = (Block) blockObject[0]; int blastFurnaceMeta = (Integer) blockObject[1];
        	for (int[] uvw : new int[][]{{3,1,5}})
            {
                this.placeBlockAtCurrentPosition(world, blastFurnaceBlock, 0, uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]), blastFurnaceMeta, 2);
            }
            
        	
        	// Stone buttons
        	// Granite wall
        	for(int[] uuvvww : new int[][]{
            	{2,2,4, 4,2,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.stone_button, StructureVillageVN.chooseButtonMeta(2, this.coordBaseMode), Blocks.stone_button, StructureVillageVN.chooseButtonMeta(2, this.coordBaseMode), false);	
            }
        	
            
        	// Smooth Sandstone on the front wall specifically to place the torch
        	for(int[] uuvvww : new int[][]{
            	{2,1,0, 4,3,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
            
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// On the furnace
            	{1,2,5, 3}, {5,2,5, 1}, 
            	// Behind the front door
            	{3,3,1, 0}, 
            	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Floor
        		{3,0,0, 3,0,0}, {2,0,1, 4,0,2}, {1,0,3, 5,0,4}, {1,0,5, 1,0,5}, {5,0,5, 5,0,5}, 
        		// Back wall
            	{1,1,6, 5,4,6}, 
            	// Left wall
            	{0,1,3, 0,4,5}, {1,1,1, 1,3,2}, 
            	// Right wall
            	{6,1,3, 6,4,5}, {5,1,1, 5,3,2}, 
            	// Ceiling
            	{1,4,2, 5,4,2}, 
            	// parapet
            	{0,5,4, 0,5,4}, {6,5,4, 6,5,4}, {2,5,6, 2,5,6}, {4,5,6, 4,5,6}, {2,5,2, 2,5,2}, {4,5,2, 4,5,2}, {3,4,0, 3,4,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Cut sandstone - using logs
        	for(int[] uuvvww : new int[][]{
            	{0,1,2, 0,5,2}, {0,1,6, 0,5,6}, {6,1,2, 6,5,2}, {6,1,6, 6,5,6}, {1,1,0, 1,4,0}, {5,1,0, 5,4,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab
        	for(int[] uuvvww : new int[][]{
        		// Lower front roof
            	{1,4,1, 5,4,1}, 
            	// Larger back roof
            	{1,5,3, 5,5,4}, {1,5,5, 2,5,5}, {4,5,5, 5,5,5}, 
            	// parapets
            	{2,4,0, 2,4,0}, {4,4,0, 4,4,0}, {1,4,1, 1,4,1}, {5,4,1, 5,4,1}, 
            	{1,5,2, 1,5,2}, {3,5,2, 3,5,2}, {5,5,2, 5,5,2}, {1,5,6, 1,5,6}, {3,5,6, 3,5,6}, {5,5,6, 5,5,6}, 
            	{0,5,3, 0,5,3}, {0,5,5, 0,5,5}, {6,5,3, 6,5,3}, {6,5,5, 6,5,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{1,1,3, 1,1,3, 1}, {5,1,3, 5,1,3, 0}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
            
        	
            // Fences
        	for (int[] uuvvww : new int[][]{
        		{1,2,1, 1,2,1}, {5,2,1, 5,2,1}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceBlock, 0, biomeFenceBlock, 0, false);	
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3,1,0, 2, 1, 1},  
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Clear path for easier entry
            int pathU = 3;
            int pathV = GROUND_LEVEL;
            int pathW = -1;
            
            this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
        	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
        	/*// Place dirt if the block to be set as path is empty
        	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
        	{
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
        	}*/
        	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));            

            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	int u = 2+random.nextInt(3);
            	int v = 1;
            	int w = 1+random.nextInt(4);
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // --- Butcher Shop --- //
    
    public static class DesertButcherShop1 extends StructureVillagePieces.Village
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
        		"  F     ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 3; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertButcherShop1() {}

        public DesertButcherShop1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static DesertButcherShop1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertButcherShop1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence_gate, 0, this.materialType, this.biome); Block biomeFenceGateBlock = (Block)blockObject[0]; int biomeFenceGateMeta = (Integer)blockObject[1];
        	Object[] tryObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)tryObject[0], (Integer)tryObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome); Block biomeFenceBlock = (Block)blockObject[0];
        	// Sandstone wall that defaults to fence
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoFenceBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoFenceMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmokerBlock(3, this.coordBaseMode); Block smokerBlock = (Block) blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone
        	for(int[] uuvvww : new int[][]{
            	// Base frame
        		{0,0,1, 0,0,7}, {7,0,1, 7,0,7}, {1,0,1, 6,0,1}, {1,0,7, 6,0,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
        	
        	
        	// Cut sandstone - vertical logs
        	for(int[] uuvvww : new int[][]{
        		// Vertical corners
        		{0,1,1, 0,3,1}, {4,1,1, 4,3,1}, {0,1,4, 0,3,4}, {4,1,4, 4,3,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	// Cut sandstone - along logs
        	for(int[] uuvvww : new int[][]{
        		// Ceiling frame
        		{0,4,2, 0,4,3}, {4,4,2, 4,4,3},  
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
        	// Cut sandstone - across logs
        	for(int[] uuvvww : new int[][]{
        		// Ceiling frame
        		{1,4,1, 3,4,1}, {1,4,4, 3,4,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Front wall
        		{1,1,1, 3,3,1}, 
        		// Left wall
        		{0,1,2, 0,3,3}, 
        		// Right wall
        		{4,1,2, 4,3,3}, 
        		// Back wall
        		{1,1,4, 3,3,4}, 
        		// Floor
        		{1,0,2, 3,0,4}, {2,0,1, 2,0,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab
        	for(int[] uuvvww : new int[][]{
        		// Left window
            	{0,2,2, 0,2,3},
            	// Right window
            	{4,2,2, 4,2,3},
            	// Ceiling
        		{1,4,2, 3,4,3}, 
        		// Ceiling corners
        		{0,4,1, 0,4,1}, {0,4,4, 0,4,4}, {4,4,1, 4,4,1}, {4,4,4, 4,4,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
        	
        	
        	// Sandstone wall into fence
        	for(int[] uuvvww : new int[][]{
            	{0,1,5, 0,1,7}, {1,1,7, 6,1,7}, {7,1,1, 7,1,7}, {5,1,1, 6,1,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, false);	
            }
        	
        	
        	// Grass
        	for(int[] uuvvww : new int[][]{
            	{1,0,5, 4,0,6}, {5,0,2, 6,0,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.grass, 0, Blocks.grass, 0, false);	
            }
            
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,3,0, 2}, 
        		{2,3,3, 2}, 
        		{0,2,7, -1}, {7,2,7, -1}, {7,2,1, -1}, 
           	}) {
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,0,0, 2,0,0, 3},  
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{2,1,1, 2, 1, 1}, 
            	{2,1,4, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Double Smooth Stone Slab Counter
        	for (int[] uuvvwwo : new int[][]{ 
        		{2,0,2, 2,0,3}, {3,1,2, 3,1,3}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.double_stone_slab, 0, Blocks.double_stone_slab, 0, false);
            }
            
        	
        	// Fireplace
        	// Smoker
            for (int[] uvwo : new int[][]{{1,1,3, 1}})
            {
                this.placeBlockAtCurrentPosition(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]), StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode), 2);
            }
            // Chimney
        	for(int[] uuvvww : new int[][]{
            	{1,2,3, 1,4,3},
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.hardened_clay, 0, Blocks.hardened_clay, 0, false);	
            }
        	
            
            // Clear path for easier entry
            int pathU = 3;
            int pathV = GROUND_LEVEL;
            int pathW = -1;
            
            this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
        	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
        	/*// Place dirt if the block to be set as path is empty
        	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
        	{
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
        	}*/
        	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));            

            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;

            	// Animals
            	for (int[] uvw : new int[][]{
        			{6, 1, 6},
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM);
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.placeBlockAtCurrentPosition(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
            	// Villager
            	int u = 2;
            	int v = 1;
            	int w = 1+random.nextInt(3);
            	
            	if (w<2){w++; u--;}
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    
    // --- Cartographer House --- //
    
    public static class DesertCartographerHouse1 extends StructureVillagePieces.Village
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
        		"FFFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertCartographerHouse1() {}

        public DesertCartographerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertCartographerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertCartographerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)5, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	Object[] tryObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)tryObject[0], (Integer)tryObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModCartographyTable(); Block cartographyTableBlock = (Block) blockObject[0]; int cartographyTableMeta = (Integer) blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Front Stairs
        		{1,0,0, 2,1,0}, {3,0,0, 3,0,0}, 
        		// Front Door
        		{1,4,1, 1,4,1}, 
        		// Front wall
        		{3,0,1, 5,2,1}, 
        		// Front floor
        		{1,1,1, 2,1,3}, 
        		// Walkway
        		{4,2,2, 4,2,2},
        		// First room floor
        		{1,1,1, 2,1,3}, 
        		// Back floor
        		{3,2,3, 5,2,5}, 
        		// Left wall
        		{0,0,2, 0,4,3}, 
        		// Back wall of first floor
        		{1,0,4, 1,4,4}, {2,0,4, 3,2,4}, 
        		// Basement
        		{2,0,1, 2,0,3}, {3,0,2, 3,1,2},
        		// Second floor front wall
        		{5,3,3, 5,4,3}, 
        		// Second floor roof trim
        		{4,5,3, 5,5,3}, {3,5,4, 3,5,5}, 
        		// Back Stairs
        		{0,0,5, 1,0,5}, {2,0,5, 3,1,5}, 
        		// Back Wall
        		{4,0,6, 5,5,6}, 
        		// Right Wall
        		{5,0,2, 6,2,2}, {6,0,4, 6,5,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Cut sandstone - vertical logs (back-right building)
        	for(int[] uuvvww : new int[][]{
        		// Back-right building
        		{3,0,6, 3,6,6}, {6,0,6, 6,6,6}, {3,3,3, 3,6,3}, {6,0,3, 6,6,3}, 
        		// Extra pillars
        		{6,0,1, 6,3,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
            
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,3,3, 3}, {4,1,2, 1}, {4,5,5, 1}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Cut sandstone - vertical logs (left-front building)
        	for(int[] uuvvww : new int[][]{
        		// Front-left building
        		{0,0,1, 0,5,1}, {2,2,1, 2,5,1}, {0,0,4, 0,5,4}, {2,3,4, 2,5,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab
        	for(int[] uuvvww : new int[][]{
        		// Left-front roof
        		{0,5,2, 0,5,3}, {1,5,1, 1,5,4}, {2,5,2, 2,5,3}, 
        		// Right-back roof
        		{3,6,4, 3,6,5}, {4,6,3, 4,6,6}, {5,6,3, 5,6,6}, {6,6,4, 6,6,5}, 
        		// Back railing
        		{3,3,1, 5,3,1}, {6,3,2, 6,3,2}, 
        		// Back walkway
        		{1,0,6, 2,0,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{4,0,0, 4,0,0, 1}, {3,1,0, 3,1,0, 1},
        		// Steps out of first room
        		{3,2,2, 3,2,2, 0}, 
        		// Back steps
        		{1,1,5, 1,1,5, 0}, {2,2,5, 2,2,5, 0}, {0,0,6, 0,0,6, 2}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{1,2,1, 0, 1, 0}, 
            	{4,3,3, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
        	// Cartography Table
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,2,3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, cartographyTableBlock, cartographyTableMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Potted Cactus
            for (int[] uvw : new int[][]{
        		{0,0,0}, {5,3,2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, 9, uvw[0], uvw[1], uvw[2], structureBB); // 9 is cactus
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{5,GROUND_LEVEL,-1}, {6,GROUND_LEVEL,-1}, 
        		{0,GROUND_LEVEL,7}, {1,GROUND_LEVEL,7}, {2,GROUND_LEVEL,7}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 1;
            	int v = 2;
            	int w = 1+random.nextInt(3);
            	
            	if (w<2){w++; u++;}
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    
    // --- Small Farm --- //
    
    public static class DesertFarm1 extends StructureVillagePieces.Village
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertFarm1() {}

        public DesertFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - vertical logs (back-right building)
        	for(int[] uuvvww : new int[][]{
        		{2,0,0, 4,0,0}, {2,0,4, 4,0,4}, 
        		{0,0,2, 0,0,2}, {6,0,2, 6,0,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{1,0,0, 1,0,0, 3}, {5,0,0, 5,0,0, 3}, 
        		// Left steps
        		{0,0,0, 0,0,1, 0}, {0,0,3, 0,0,4, 0}, 
        		// Right steps
        		{6,0,0, 6,0,1, 1}, {6,0,3, 6,0,4, 1}, 
        		// Back steps
        		{1,0,4, 1,0,4, 2}, {5,0,4, 5,0,4, 2}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
        	
        	// Tilled, Moist Farmland
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{1,0,1, 1,0,3}, {3,0,1, 3,0,3}, {5,0,1, 5,0,3}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.farmland, 7, Blocks.farmland, 7, false);
            }
        	
        	
        	// Water
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{2,0,1, 2,0,3}, {4,0,1, 4,0,3},  
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.flowing_water, 0, Blocks.flowing_water, 0, false);
            }
        	
        	
            // Crops
        	int[][] cropAgeArray = {
        			{7,0,7},
        			{7,7,0},
        			{7,7,0},
        	};
        	int[] ua = new int[]{1, 3, 5};
            for (int i=0; i<3; i++)
            {
            	Block[] cropBlocks = StructureVillageVN.chooseCropPair(random);
            	for (int j=0; j<3; j++)
            	{
            		this.placeBlockAtCurrentPosition(world, cropBlocks[0], cropAgeArray[i][j], ua[i], 1, j+1, structureBB);
            	}
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, do nothing
            Block compostBin = Block.getBlockFromName(ModObjects.compostBinGC);
            if (compostBin != null)
            {
            	this.placeBlockAtCurrentPosition(world, compostBin, 0, 5, 1, 2, structureBB);
            	this.placeBlockAtCurrentPosition(world, Blocks.dirt, 0, 5, 0, 2, structureBB);
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{0,GROUND_LEVEL,-1}, 
        		{1,GROUND_LEVEL,-1}, 
        		{2,GROUND_LEVEL,-1}, 
        		{3,GROUND_LEVEL,-1}, 
        		{4,GROUND_LEVEL,-1}, 
        		{5,GROUND_LEVEL,-1}, 
        		{6,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2;
            	int v = 1;
            	int w = 2;
            	
            	while ((u>0 && u<(STRUCTURE_WIDTH-1)) && (w>0 && w<(STRUCTURE_DEPTH-1)))
            	{
            		u = random.nextInt(STRUCTURE_WIDTH);
            		w = random.nextInt(STRUCTURE_DEPTH);
            	}
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Medium Farm --- //
    
    public static class DesertFarm2 extends StructureVillagePieces.Village
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
        		"FFFFFFF   ",
        		"FFFFFFF   ",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFF   ",
        		"FFFFFFF   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 3; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertFarm2() {}

        public DesertFarm2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertFarm2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertFarm2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlock(Blocks.trapdoor.getStateFromMeta(0), this.materialType, this.biome);
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{1,1,0, 5,1,0, 3}, {7,1,2, 8,1,2, 3}, 
        		// Left steps
        		{0,1,0, 0,1,6, 0}, 
        		// Right steps
        		{6,1,0, 6,1,2, 1}, {9,1,2, 9,1,4, 1}, {6,1,4, 6,1,6, 1}, 
        		// Back steps
        		{1,1,6, 5,1,6, 2}, {7,1,4, 8,1,4, 2}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Side platform
        		{6,1,3, 8,1,3}, 
        		// Foundation
        		{3,0,2, 3,0,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Tilled, Moist Farmland
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{1,1,1, 5,1,5}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.farmland, 7, Blocks.farmland, 7, false);
            }
        	
        	
        	// Water
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{3,1,3, 3,1,3}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.flowing_water, 0, Blocks.flowing_water, 0, false);
            }
        	
        	
        	// Pairs of crops are generated together. These are pairs of ages arrays.
        	int[][][] uPairArray = {
    			{{1,2,3,4,5},
    			 {1,2,3,4,5}},
    			{{1,2},{4,5}},
    			{{1,2,3,4,5},
    			 {1,2,3,4,5}},
        	};
        	int[][][] vPairArray = {
    			{{2,2,2,2,2},
    			 {2,2,2,2,2}},
    			{{2,2},{2,2}},
    			{{2,2,2,2,2},
    			 {2,2,2,2,2}},
        	};
        	int[][][] wPairArray = {
    			{{5,5,5,5,5},
    			 {4,4,4,4,4}},
    			{{3,3},{3,3}},
    			{{2,2,2,2,2},
    			 {1,1,1,1,1}},
        	};
        	int[][][] cropPairAgeArray = {
    			{{0,1,0,1,3},
    			 {1,0,0,1,0}},
    			{{1,1},{1,0}},
    			{{0,0,2,2,0},
    			 {2,0,0,1,2}},
        	};
        	// Iterate through the pairs of crops
            for (int crop_pair=0; crop_pair<cropPairAgeArray.length; crop_pair++)
            {
            	Block[] cropBlocks = StructureVillageVN.chooseCropPair(random);
            	
            	// Iterate through the members of a crop pair
            	for (int crop_member=0; crop_member<2; crop_member++)
            	{
            		// Iterate through u positions
            		for (int i=0; i<cropPairAgeArray[crop_pair][crop_member].length; i++)
                	{
            			this.placeBlockAtCurrentPosition(world, cropBlocks[crop_member],
            					cropPairAgeArray[crop_pair][crop_member][i], 
            					uPairArray[crop_pair][crop_member][i],
            					vPairArray[crop_pair][crop_member][i],
            					wPairArray[crop_pair][crop_member][i],
            					structureBB);
                	}
            	}
            }
            
        	
        	// Bin
            
            // Attempt to add GardenCore Compost Bins. If this fails, put a hay bale down instead
            Block compostBin = Block.getBlockFromName(ModObjects.compostBinGC);
            this.placeBlockAtCurrentPosition(world, compostBin!=null?compostBin:Blocks.hay_block, 0, 8, 2, 3, structureBB);
            // Trapdoor rim
        	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, this.coordBaseMode.getHorizontalIndex()%2==0?6:4, 7,2,3, structureBB); // Left
        	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, this.coordBaseMode.getHorizontalIndex()%2==0?7:5, 9,2,3, structureBB); // Right
        	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, (new int[]{4,7,5,6})[this.coordBaseMode], 8,2,2, structureBB); // Front
        	this.placeBlockAtCurrentPosition(world, biomeTrapdoorBlock, (new int[]{5,6,4,7})[this.coordBaseMode], 8,2,4, structureBB); // Back
        	
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{0,GROUND_LEVEL,-1}, 
        		{1,GROUND_LEVEL,-1}, 
        		{2,GROUND_LEVEL,-1}, 
        		{3,GROUND_LEVEL,-1}, 
        		{4,GROUND_LEVEL,-1}, 
        		{5,GROUND_LEVEL,-1}, 
        		{6,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2;
            	int v = 2;
            	int w = 2;
            	
            	while ((u>0 && u<6) && (w>0 && w<6))
            	{
            		u = random.nextInt(7);
            		w = random.nextInt(7);
            	}
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Fisher Cottage --- //
    
    public static class DesertFisher1 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFF  F  ",
        		"FFFFFFFF FF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 3; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertFisher1() {}

        public DesertFisher1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertFisher1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertFisher1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(true, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabTopMeta = (Integer)blockObject[1];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone
        	for(int[] uuvvww : new int[][]{
        		// Fishing basin
            	{0,1,0, 4,1,0}, {0,1,4, 4,1,4}, {0,1,1, 0,1,3}, {4,1,1, 4,1,3}, {1,0,1, 3,0,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
        	
        	// Water
        	for(int[] uuvvww : new int[][]{
            	{1,1,1, 3,1,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water, 0, Blocks.flowing_water, 0, false);	
            }
        	
        	
        	// Cut sandstone - using logs
        	for(int[] uuvvww : new int[][]{
        		// Pool columns
            	{0,2,4, 0,5,4}, {2,2,4, 2,5,4}, {4,2,4, 4,5,4}, {4,2,2, 4,5,2}, {4,2,0, 4,5,0}, 
            	// House columns
            	{5,1,3, 5,4,3}, {9,1,3, 9,4,3}, {9,1,7, 9,4,7}, {3,1,5, 3,4,5}, {3,1,7, 3,4,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
        	// Smooth Sandstone phase 1
        	for(int[] uuvvww : new int[][]{
        		// Walkway
        		{7,0,0, 7,0,3}, 
        		// Front wall
        		{6,1,3, 8,3,3}, 
        		// Floor
        		{5,0,4, 8,0,6}, 
        		// Right wall
        		{9,1,4, 9,3,6}, 
        		// Left wall
        		{3,1,6, 3,3,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{7,3,2, 2}, 
        		{8,2,5, 3}, 
        		{4,2,6, 1}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
        	// Smooth Sandstone phase 2
        	for(int[] uuvvww : new int[][]{
        		// Back wall
        		{4,1,7, 8,3,7}, 
        		// Interior
        		{5,1,4, 5,3,4}, {4,1,5, 4,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
            
        	// Smooth Sandstone slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Roof
        		{3,4,6, 3,4,6}, {4,4,5, 4,4,7}, {5,4,4, 8,4,7}, {6,4,3, 8,4,3}, {9,4,4, 9,4,6}, 
        		// Windows
        		{9,3,5, 9,3,5}, {7,3,7, 7,3,7}, {5,3,7, 5,3,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
        	
            
        	// Smooth Sandstone slab (top)
        	for(int[] uuvvww : new int[][]{
        		// Columns
        		{1,5,4, 1,5,4}, {3,5,4, 3,5,4}, {4,5,3, 4,5,3}, {4,5,1, 4,5,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, false);	
            }
        	
        	
            // Hay bales
        	for (int[] uvwo : new int[][]{ // 0:Vertical;  4+(this.coordBaseMode.getHorizontalIndex()%2==0? 0:4):Along;  4+(this.coordBaseMode.getHorizontalIndex()%2==0? 4:0):Across
        		{10,1,0, 4+(this.coordBaseMode.getHorizontalIndex()%2==0? 4:0)}, 
        		{8,1,1, 4+(this.coordBaseMode.getHorizontalIndex()%2==0? 0:4)}, 
        		{9,1,2, 0}, 
        		{9,2,2, 4+(this.coordBaseMode.getHorizontalIndex()%2==0? 4:0)}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.hay_block, uvwo[3], uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Barrels
    		Block barrelBlock = ModObjects.chooseModBarrelBlock();
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel: -1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
    			// TODO - use different barrel meta for different mods
            	
    			// Exterior
    			{5,1,2, 1,1}, {5,2,2, 2,-1}, 
            	// Interior
            	{4,1,6, 1,-1}, 
            	{8,1,6, 3,2}, {8,1,5, 3,0}, {8,2,6, 3,3}, 
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (barrelBlock==null) {barrelBlock = Blocks.chest;}
    			this.placeBlockAtCurrentPosition(world, barrelBlock, 0, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2]), barrelBlock==Blocks.chest?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.coordBaseMode):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.coordBaseMode), 2);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{7,1,3, 0, 1, 0},  
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Potted plants
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{0,2,3, 10}, {2,2,0, 9}, 
        		{5,1,0, 10}, {6,1,2, 9}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
        	
        	
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{7,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 5;
            	int v = 1;
            	int w = 4;
            	
            	while (u==5 && w==4)
            	{
            		u = 5+random.nextInt(3);
            		w = 4+random.nextInt(3);
            	}
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Fletcher House --- //
    
    public static class DesertFletcherHouse1 extends StructureVillagePieces.Village
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
        		"FFFFFFFFF   ",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFF   ",
        		"   F        ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 12;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 3; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertFletcherHouse1() {}

        public DesertFletcherHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertFletcherHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertFletcherHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(true, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabTopMeta = (Integer)blockObject[1];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.cobblestone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoCobblestoneBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoCobblestoneMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseModFletchingTable(); Block fletchingTableBlock = (Block) blockObject[0]; int fletchingTableMeta = (Integer) blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.ladder, 0, this.materialType, this.biome); Block biomeLadderBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	for(int[] uuvvww : new int[][]{
        		// Lowrise
            	{0,1,1, 0,4,1}, {8,1,1, 8,4,1}, {0,1,5, 0,4,5}, {8,1,5, 8,4,5}, 
            	// Tower
            	{9,1,2, 9,9,2}, {11,1,2, 11,9,2}, {9,1,4, 9,9,4}, {11,1,4, 11,9,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
        	// Smooth Sandstone, part 1
        	for(int[] uuvvww : new int[][]{
        		// Walkway
        		{3,0,0, 3,0,1}, 
        		// Front wall
        		{1,1,1, 7,3,1}, 
        		// Back wall
        		{1,1,5, 7,3,5}, 
        		// Floor
        		{1,0,2, 8,0,4}, {9,0,3, 10,0,3}, 
        		// Left wall
        		{0,1,2, 0,3,4}, 
        		// Tower right wall
        		{11,1,3, 11,8,3}, 
        		// Counter
        		{1,1,2, 1,1,2}, {1,1,4, 1,1,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
            
        	// Sandstone wall into cobblestone block
        	for(int[] uuvvww : new int[][]{
        		{10,10,3, 10,10,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneWallIntoCobblestoneBlock, biomeSandstoneWallIntoCobblestoneMeta, biomeSandstoneWallIntoCobblestoneBlock, biomeSandstoneWallIntoCobblestoneMeta, false);	
            }
        	
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front face
        		{2,2,0, 2}, 
        		// Interior
        		{2,2,4, 2}, {6,2,4, 2}, 
        		// Tower
        		{10,11,3, -1}, {10,7,3, 3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
        	// Smooth Sandstone, part 2
        	for(int[] uuvvww : new int[][]{
        		// Front wall
        		{10,1,2, 10,8,2}, 
        		// Back wall
        		{10,1,4, 10,8,4}, 
        		// Tower left wall
        		{9,3,3, 9,8,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
            // Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{8,1,2, 8,1,2, 0}, {8,1,4, 8,1,4, 0}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
            
        	// Smooth Sandstone slab (top)
        	for(int[] uuvvww : new int[][]{
        		// Lowrise ceiling
        		{1,3,2, 8,3,4}, 
        		// Tower top
        		{10,9,3, 10,9,3}, 
        		// Windows
        		{0,3,3, 0,3,3}, {6,3,1, 6,3,1}, {4,3,5, 4,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, false);	
            }
        	
            
        	// Smooth Sandstone slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Lowrise parapets
        		{2,4,1, 2,4,1}, {4,4,1, 4,4,1}, {6,4,1, 6,4,1}, 
        		{2,4,5, 2,4,5}, {4,4,5, 4,4,5}, {6,4,5, 6,4,5}, 
        		{0,4,3, 0,4,3}, 
        		// Highrise parapets
        		{9,9,3, 9,9,3}, {11,9,3, 11,9,3}, 
        		{10,9,2, 10,9,2}, {10,9,4, 10,9,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
            
            
            // Ladder
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{10,1,3, 10,6,3, 3},  
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderBlock, StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode), biomeLadderBlock, StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode), false);
            }
        	
        	
            // Fletching Table
            this.placeBlockAtCurrentPosition(world, fletchingTableBlock, fletchingTableMeta, 1, 1, 3, structureBB);
            
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            	{9,1,3, 3, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Potted plants
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{0,2,3, 9}, {6,2,1, 9}, {4,2,5, 9}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
        	
        	
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(6);
            	int v = 1;
            	int w = 2+random.nextInt(3);
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Large Farm --- //
    
    public static class DesertLargeFarm1 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertLargeFarm1() {}

        public DesertLargeFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertLargeFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertLargeFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
        	// Sand via Dirt
        	for(int[] uuvvww : new int[][]{
        		{2,1,0, 2,1,2}, 
        		{3,1,1, 4,1,1}, 
        		{5,1,0, 7,1,0}, 
        		{8,1,1, 10,1,1}, 
        		{10,1,2, 12,1,5}, 
        		{9,1,5, 9,1,5}, 
        		{11,1,6, 12,1,6}, 
        		{12,1,7, 12,1,8}, 
        		{10,1,8, 11,1,8}, 
        		{10,1,9, 10,1,9}, 
        		{6,1,9, 9,1,10}, 
        		{6,1,7, 6,1,7}, 
        		{2,1,7, 5,1,10}, 
        		{1,1,7, 1,1,9}, 
        		{1,1,6, 2,1,6}, 
        		{1,1,3, 3,1,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);	
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Basin
        		{2,2,7, 4,2,7}, 
        		{1,2,8, 1,2,8}, {2,1,8, 4,1,8}, {5,2,8, 5,2,8}, 
        		{2,2,9, 4,2,9}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Tilled, Moist Farmland with planted wheat
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{5,1,1, 7,1,1}, 
        		{3,1,2, 3,1,2}, 
        		{4,1,2, 8,1,3}, 
        		{9,1,2, 9,1,2}, 
        		{5,1,4, 9,1,4}, 
        		{4,1,5, 8,1,5}, 
        		{3,1,6, 10,1,6}, 
        		{7,1,7, 11,1,7}, 
        		{6,1,8, 9,1,8}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.farmland, 7, Blocks.farmland, 7, false);
            }
        	
        	
        	// Planted wheat - of the form meta,w,v,u because that was easier to read out of the structure file
        	for (int[] uvwm : new int[][]{
        		{5,1,2,5}, 
        		{1,1,2,6}, 
        		{3,1,2,7}, 
        		{2,2,2,3}, 
        		{6,2,2,4}, 
        		{4,2,2,5}, 
        		{1,2,2,7}, 
        		{3,2,2,8}, 
        		{7,2,2,9}, 
        		{7,3,2,4}, 
        		{5,3,2,5}, 
        		{6,3,2,6}, 
        		{2,3,2,7}, 
        		{6,3,2,8}, 
        		{7,4,2,5}, 
        		{7,4,2,6}, 
        		{7,4,2,7}, 
        		{7,4,2,8}, 
        		{7,4,2,9}, 
        		{7,5,2,4}, 
        		{7,5,2,5}, 
        		{7,5,2,6}, 
        		{7,5,2,7}, 
        		{7,5,2,8}, 
        		{7,6,2,3}, 
        		{7,6,2,4}, 
        		{7,6,2,5}, 
        		{7,6,2,6}, 
        		{7,6,2,7}, 
        		{7,6,2,8}, 
        		{7,6,2,9}, 
        		{4,6,2,10}, 
        		{7,7,2,7}, 
        		{7,7,2,9}, 
        		{7,7,2,10}, 
        		{7,7,2,11}, 
        		{7,8,2,6}, 
        		{5,8,2,7}, 
        		{7,8,2,8}, 
        		{7,8,2,9}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.wheat, uvwm[0], uvwm[3], uvwm[2], uvwm[1], structureBB);
            }
        	
        	
        	// Water
        	for(int[] uuvvww : new int[][]{
        		// Basin
        		{2,2,8, 4,2,8}, 
        		{8,1,7, 8,1,7}, 
        		{6,1,2, 6,1,2}, 
            	})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water, 0, Blocks.flowing_water, 0, false);
            }
        	
        	
            // Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Basin
        		{1,2,7, 1,2,7, 0}, {1,2,9, 1,2,9, 0}, 
        		{5,2,7, 5,2,7, 3}, {5,2,9, 5,2,9, 2}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
        	
            // Hay bales
        	int[] baleorientation = new int[]{0, 4+(this.coordBaseMode.getHorizontalIndex()%2==0? 0:4), 4+(this.coordBaseMode.getHorizontalIndex()%2==0? 4:0)};
        	for (int[] uvwo : new int[][]{ // 0=Vertical;  1=Along;  2=Across
        		// Left cluster
        		{2,2,0, 2}, 
        		{1,2,3, 2}, 
        		{3,2,3, 1}, 
        		{2,2,4, 2}, 
        		{4,2,4, 2}, 
        		{2,3,4, 0}, 
        		{1,2,5, 0}, 
        		// Right cluster
        		{9,2,3, 2}, 
        		{11,2,3, 0}, 
        		{12,2,2, 1}, 
        		{10,2,4, 1}, 
        		{10,2,5, 0}, 
        		{11,2,4, 0}, 
        		{11,3,4, 2}, 
        		{12,2,4, 2}, 
        		{12,2,5, 0}, 
        		{12,2,7, 2}, 
        		// Back cluster
        		{9,2,9, 2}, 
        		{6,2,9, 1}, 
        		{7,2,10, 0}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.hay_block, baleorientation[uvwo[3]], uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Bin
        	for (int[] uvw : new int[][]{
        		{3,2,4}, 
        		{9,2,5}, 
           		})
        	{
        		// Attempt to add GardenCore Compost Bins. If this fails, put a hay bale down instead
                Block compostBin = Block.getBlockFromName(ModObjects.compostBinGC);
                this.placeBlockAtCurrentPosition(world, compostBin!=null?compostBin:Blocks.hay_block, 0, uvw[0], uvw[1], uvw[2], structureBB);
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 5+random.nextInt(4);
            	int v = 2;
            	int w = 3+random.nextInt(4);
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Library --- //
    
    public static class DesertLibrary1 extends StructureVillagePieces.Village
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertLibrary1() {}

        public DesertLibrary1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertLibrary1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertLibrary1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome); Block biomeSandstoneBlock = (Block)blockObject[0]; int biomeSandstoneMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 9, this.materialType, this.biome); Block biomeSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSandstoneSlabTopMeta = (Integer)blockObject[1];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	// Vertical
        	for(int[] uuvvww : new int[][]{
            	{0,1,0, 0,6,0}, {8,1,0, 8,6,0}, {0,1,4, 0,6,4}, {8,1,4, 8,6,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	// Along
        	for(int[] uuvvww : new int[][]{
            	{0,5,1, 0,5,3}, {8,5,1, 8,5,3},  
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
        	// Across
        	for(int[] uuvvww : new int[][]{
            	{1,5,0, 7,5,0}, {1,5,4, 7,5,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
        	
        	
        	// Smooth Sandstone, part 1
        	for(int[] uuvvww : new int[][]{
        		// Walkway
        		{4,0,0, 4,0,0}, 
        		// Floor
        		{2,0,1, 6,0,3}, {7,0,2, 7,0,2},
        		// Front wall
        		{1,1,0, 7,4,0}, 
        		// Front wall
        		{1,1,4, 7,4,4}, 
        		// Left wall
        		{0,1,1, 0,4,3}, 
        		// Right wall
        		{8,1,1, 8,4,3}, 
        		// Ceiling
        		{1,4,1, 7,4,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Interior
        		{4,2,3, 2}, {7,2,2, 3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
        	// Sandstone
        	for(int[] uuvvww : new int[][]{
        		{4,3,0, 4,3,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneBlock, biomeSandstoneMeta, biomeSandstoneBlock, biomeSandstoneMeta, false);	
            }
            
        	
        	// Windows
        	for(int[] uvw : new int[][]{
        		{2,2,0}, {6,2,0}, {2,2,4}, {6,2,4}, 
            	})
            {
        		// Cactus
        		this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, 9, uvw[0], uvw[1], uvw[2], structureBB);
        		// Sandstone slab
        		this.placeBlockAtCurrentPosition(world, biomeSandstoneSlabTopBlock, biomeSandstoneSlabTopMeta, uvw[0], uvw[1]+1, uvw[2], structureBB);	
            }
        	
        	
            // Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{7,1,1, 7,1,1, 0}, {7,1,3, 7,1,3, 0}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
        	
        	// Lecterns
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.planks, 0, this.materialType, this.biome); Block biomePlankBlock = (Block)blockObject[0]; int biomePlankMeta = (Integer)blockObject[1];
            for (int[] uvwo : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{7,1,2, 3},
            })
            {
        		ModObjects.setModLecternBlock(world,
            			this.getXWithOffset(uvwo[0], uvwo[2]),
            			this.getYWithOffset(uvwo[1]),
            			this.getZWithOffset(uvwo[0], uvwo[2]),
            			uvwo[3],
            			this.coordBaseMode,
            			biomePlankMeta);
            }
        	
        	// Bookshelves
        	for(int[] uuvvww : new int[][]{
        		{1,1,1, 1,3,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf, 0, Blocks.bookshelf, 0, false);	
            }
        	
        	
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		{3,1,2, (GeneralConfig.useVillageColors ? this.townColor2 : 0)}, // White
        		{4,1,2, (GeneralConfig.useVillageColors ? this.townColor : 5)}, // Lime
        		{5,1,2, (GeneralConfig.useVillageColors ? this.townColor2 : 0)}, // White
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.carpet, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,0, 0, 1, 1}, 
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
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(5);
            	int v = 1;
            	int w = 1+random.nextInt(3);
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    
    // --- Mason House --- //
    
    public static class DesertMason1 extends StructureVillagePieces.Village
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
        		"   FF   ",
        		"  FFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertMason1() {}

        public DesertMason1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertMason1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertMason1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoFenceBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoFenceMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	// Vertical
        	for(int[] uuvvww : new int[][]{
            	{0,1,2, 0,4,2}, {0,1,6, 0,4,6}, {7,1,2, 7,4,2}, {7,1,6, 7,4,6}, 
            	// Entry frame
            	{2,1,2, 2,2,2}, {5,1,2, 5,2,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	// Across
        	for(int[] uuvvww : new int[][]{
            	{2,3,2, 5,3,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
        	
        	
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Walkway
        		{3,0,0, 4,0,2}, 
        		// Floor
        		{1,0,3, 6,0,5}, 
        		// Front wall
        		{1,1,2, 1,3,2}, {6,1,2, 6,3,2}, 
        		// Back wall
        		{1,1,6, 6,3,6}, 
        		// Left wall
        		{0,1,3, 0,3,5}, 
        		// Right wall
        		{7,1,3, 7,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{2,2,1, 2}, {5,2,1, 2}, 
        		// Interior
        		{1,3,4, 1}, {6,3,4, 3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Sandstone wall into fence
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,1,0, 2,3,0}, {5,1,0, 5,3,0}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, false);
            }
        	
        	
            // Smooth Sandstone Slab (Lower)
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Awning
        		{2,4,0, 5,4,1}, 
        		// Roof proper
        		{1,4,2, 6,4,2}, {0,4,3, 7,4,5}, {1,4,6, 6,4,6}, 
        		// Windows
        		{0,2,4, 0,2,4}, {7,2,4, 7,2,4}, {2,2,6, 5,2,6}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);
            }
        	
        	
        	// Stonecutter
        	// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	blockObject = ModObjects.chooseModStonecutter(3); Block stonecutterBlock = (Block) blockObject[0]; int stonecutterMeta = (Integer) blockObject[1];
        	for(int[] uvw : new int[][]{
        		{6,1,4}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, stonecutterBlock, stonecutterMeta, uvw[0], uvw[1], uvw[2], structureBB); 
            }
        	
        	
        	// Clay
        	for(int[] uvw : new int[][]{
        		{1,1,5}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.clay, 0, uvw[0], uvw[1], uvw[2], structureBB); 
            }
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{1,1,3, 1,1,4}, {1,2,3, 1,2,3}, 
        		})
            {
        		// Lime
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.stained_hardened_clay, (GeneralConfig.useVillageColors ? this.townColor : 5), Blocks.stained_hardened_clay, (GeneralConfig.useVillageColors ? this.townColor : 5), false);
            }
        	
        	
    		// Glazed terracotta
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,1,3, 1}, 
        		})
            {
        		// White
        		Object[] tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(GeneralConfig.useVillageColors ? this.townColor2 : 0, StructureVillageVN.chooseGlazedTerracottaMeta(uvwo[3], this.coordBaseMode));
            	
            	if (tryGlazedTerracotta != null) {this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], uvwo[0], uvwo[1], uvwo[2], structureBB);}
            	else {this.placeBlockAtCurrentPosition(world, Blocks.stained_hardened_clay, GeneralConfig.useVillageColors ? this.townColor2 : 0, uvwo[0], uvwo[1], uvwo[2], structureBB);} // White
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,2, 0, 1, 0}, {4,1,2, 0, 1, 1}, 
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
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(4);
            	int v = 1;
            	int w = 4+random.nextInt(2);
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // --- Medium House --- //
    
    public static class DesertMediumHouse1 extends StructureVillagePieces.Village
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertMediumHouse1() {}

        public DesertMediumHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertMediumHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertMediumHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 1, this.materialType, this.biome); Block biomeSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 9, this.materialType, this.biome); Block biomeSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSandstoneSlabTopMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	
        	// Across
        	for(int[] uuvvww : new int[][]{
            	{0,4,2, 6,4,2}, 
            	{0,4,4, 6,4,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
        	
        	
        	// Cut sandstone
        	for(int[] uuvvww : new int[][]{
            	{0,4,1, 6,4,1}, 
            	{0,4,3, 6,4,3}, 
            	{0,4,5, 6,4,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
        	
        	
        	// Smooth Sandstone, part 1
        	for(int[] uuvvww : new int[][]{
        		// Walkway
        		{3,0,1, 3,0,1}, 
        		// Floor
        		{1,0,2, 5,0,4}, 
        		// Front wall
        		{0,1,1, 6,3,1}, 
        		// Back wall
        		{0,1,5, 6,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{2,2,0, 2}, {4,2,0, 2}, 
        		// Interior
        		{3,3,2, 0}, 
        		{1,3,4, 2}, {5,3,4, 2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Smooth Sandstone, part 2
        	for(int[] uuvvww : new int[][]{
        		// Left wall
        		{0,1,2, 0,3,4}, 
        		// Right wall
        		{6,1,2, 6,3,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
            // Windows
        	for (int[] uvw : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{1,2,1}, {5,2,1}, 
        		{0,2,3}, {6,2,3}, 
        		{2,2,5}, {4,2,5}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.placeBlockAtCurrentPosition(world,
        					new Block[]{biomeSandstoneSlabBottomBlock, biomeSandstoneSlabTopBlock}[i],
        					new int[]{biomeSandstoneSlabBottomMeta, biomeSandstoneSlabTopMeta}[i],
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
        	
        	
            // Sandstone Slab (Lower)
        	for (int[] uvw : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{0,5,1}, {3,5,1}, {6,5,1}, 
        		{0,5,3}, {6,5,3}, 
        		{0,5,5}, {3,5,5}, {6,5,5}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
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
        	
        	
            // Terracotta Table
        	for (int[] uvw : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{3,1,4}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.hardened_clay, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Potted plants
        	for(int[] uvw : new int[][]{
        		{1,1,0}, {3,2,4},  
            	})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, 9, uvw[0], uvw[1], uvw[2], structureBB); // Cactus
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 1;
        	int chestV = 1;
        	int chestW = 2;
        	int chestO = 1;
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,1,3,2}, 
            	{5,1,3,2}, 
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor : 5); // Lime
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{2,1,3, -1, 0},
	        			{4,1,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Big House --- //
    
    public static class DesertMediumHouse2 extends StructureVillagePieces.Village
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 1; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 1; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertMediumHouse2() {}

        public DesertMediumHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertMediumHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertMediumHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	Object[] tryObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)tryObject[0], (Integer)tryObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_button, 0, this.materialType, this.biome); Block biomeWoodButtonBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.ladder, 0, this.materialType, this.biome); Block biomeLadderBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
        	// Smooth Sandstone, Part 1
        	for(int[] uuvvww : new int[][]{
        		// Front Stairs
        		{2,1,0, 4,1,0}, {3,2,0, 3,2,0}, 
        		// Front wall
        		{1,1,1, 5,5,1}, {6,1,1, 9,3,1}, 
        		// Right wall
        		{9,1,1, 9,3,2}, {9,1,3, 9,7,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{3,5,0, 2}, 
        		// Inside Upper
        		{8,5,4, 3}, 
        		// Inside Middle
        		{2,4,2, 0}, {4,4,2, 0},
        		// Inside Lower
        		{8,2,3, 3},
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
        	// Smooth Sandstone, Part 2
        	for(int[] uuvvww : new int[][]{
        		// Central wall
        		{5,1,1, 5,6,2}, {5,1,3, 5,7,5}, 
        		// Left unit left wall
        		{1,1,1, 1,5,4}, 
        		// Left unit back wall
        		{1,1,4, 4,5,4}, 
        		// Right units back wall
        		{6,1,5, 9,7,5}, 
        		// Right top unit front wall
        		{6,4,3, 9,7,3}, 
        		// Lower unit door
        		{6,0,2, 8,0,4},
        		// Lower unit floor
        		{6,0,1, 6,0,1}, 
        		// Upper unit floor
        		{6,3,2, 8,3,4}, 
        		// Middle unit floor
        		{2,1,1, 4,2,3}, 
        		// Middle unit ceiling
        		{2,5,2, 4,5,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab
        	for(int[] uuvvww : new int[][]{
        		// Mid unit ceiling
        		{1,6,1, 1,6,1}, {1,6,4, 1,6,4}, 
        		{5,6,1, 5,6,1}, {4,6,4, 4,6,4}, 
        		// Top unit ceiling
        		{5,7,4, 5,7,4}, {6,7,3, 8,7,5}, {9,7,4, 9,7,4}, 
        		// Balcony railing
        		{6,4,1, 8,4,1}, {9,4,1, 9,4,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{1,1,0, 1,1,0, 0}, {2,2,0, 2,2,0, 0}, {5,1,0, 5,1,0, 1}, {4,2,0, 4,2,0, 1}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{7,4,3, 0, 1, 0}, 
            	{3,3,1, 0, 1, 0}, 
            	{6,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Ladder
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{6,2,4, 6,3,4, 1},  
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderBlock, StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode), biomeLadderBlock, StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode), false);
            }
            
            
            // Potted Cactus
            for (int[] uvw : new int[][]{
        		{7,1,0},
        		{6,4,2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, 9, uvw[0], uvw[1], uvw[2], structureBB); // 9 is cactus
            }
            
        	
        	// Wood buttons
        	for(int[] uuvvwwo : new int[][]{
        		// Lower front
        		{1,3,0, 2,3,0, 2}, {4,3,0, 9,3,0, 2}, 
        		// Lower left
        		{0,3,1, 0,3,4, 3}, 
        		// Lower right
        		{10,3,1, 10,3,5, 1}, 
        		// Lower back
        		{1,3,5, 4,3,5, 0}, {5,3,6, 9,3,6, 0}, 
        		// Upper front
        		{5,6,2, 9,6,2, 2}, 
        		// Upper left
        		{4,6,3, 4,6,3, 3}, {4,6,5, 4,6,5, 3}, 
        		// Upper right
        		{10,6,3, 10,6,5, 1}, 
        		// Upper back
        		{5,6,6, 9,6,6, 0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodButtonBlock, StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode), Blocks.wooden_button, StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode), false);	
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 3;
        	int chestW = 3;
        	int chestO = 2;
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,3,3, 3, GeneralConfig.useVillageColors ? this.townColor : 5}, // Lime
            	{8,1,3, 2, GeneralConfig.useVillageColors ? this.townColor3 : 13}, // Green
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{6,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,3,2, -1, 0},
	        			{6,1,2, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Shepherd's House --- //
    
    public static class DesertShepherdHouse1 extends StructureVillagePieces.Village
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 2; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertShepherdHouse1() {}

        public DesertShepherdHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertShepherdHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertShepherdHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 1, this.materialType, this.biome); Block biomeSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 9, this.materialType, this.biome); Block biomeSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSandstoneSlabTopMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoFenceBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoFenceMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModLoom(); Block loomBlock = (Block) blockObject[0]; int loomMeta = (Integer) blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	// Vertical
        	for(int[] uuvvww : new int[][]{
            	// Pen
        		{0,1,1, 0,4,1}, {0,1,4, 0,4,4}, 
        		// House
        		{4,1,1, 4,5,1}, {4,1,4, 4,5,4}, {10,1,1, 10,5,1}, {10,1,4, 10,5,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
        	// Smooth Sandstone, Part 1
        	for(int[] uuvvww : new int[][]{
        		// Front Door
        		{6,0,1, 6,0,1}, 
        		// Floor
        		{5,0,2, 5,0,3}, 
        		// Left wall
        		{4,3,2, 4,4,3}, 
        		// Right wall
        		{10,1,2, 10,4,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
            // Torches, part 1
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Left side
        		{3,3,1, 3}, {3,3,4, 3}, 
        		// Interior
        		{5,3,3, 1}, {9,3,3, 3}, {9,3,2, 3}, 

           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
        	// Smooth Sandstone, Part 2
        	for(int[] uuvvww : new int[][]{
        		// Front wall
        		{5,1,1, 9,4,1}, 
        		// Back wall
        		{5,1,4, 9,4,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
            // Torches, part 1
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front wall
        		{6,3,0, 2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Cut Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Planter bases
        		{4,1,0, 4,1,0}, {8,1,0, 8,1,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
        	
        	
        	// Sandstone Wall
        	for(int[] uuvvww : new int[][]{
        		// Planter bases
        		{1,1,1, 3,1,1}, {1,1,4, 3,1,4}, {0,1,2, 0,1,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, false);	
            }
        	
        	
            // Windows
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{8,2,1}, 
        		// Back wall
        		{8,2,4}, {6,2,4}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.placeBlockAtCurrentPosition(world,
        					new Block[]{biomeSandstoneSlabBottomBlock, biomeSandstoneSlabTopBlock}[i],
        					new int[]{biomeSandstoneSlabBottomMeta, biomeSandstoneSlabTopMeta}[i],
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
        	
        	
            // Sandstone slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Sheep pen
        		{0,4,2, 0,4,3}, {1,4,1, 3,4,4}, 
        		// House roof
        		{4,5,2, 4,5,3}, {5,5,1, 9,5,4}, {10,5,2, 10,5,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, false);	
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{6,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Potted Cactus
            for (int[] uvw : new int[][]{
            	{4,2,0}, {8,2,0}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, 9, uvw[0], uvw[1], uvw[2], structureBB); // 9 is cactus
            }
            
            
            // Water
            for (int[] uvw : new int[][]{
            	{1,0,3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flowing_water, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hay bales
        	int[] baleorientation = new int[]{0, 4+(this.coordBaseMode.getHorizontalIndex()%2==0? 0:4), 4+(this.coordBaseMode.getHorizontalIndex()%2==0? 4:0)};
        	for (int[] uvwo : new int[][]{ // 0=Vertical;  1=Along;  2=Across
        		{4,1,3, 0}, {5,1,3, 2}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.hay_block, baleorientation[uvwo[3]], uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Loom
            for (int[] uvw : new int[][]{
            	{9,1,2}, {9,1,3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, loomBlock, loomMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{6,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 6+random.nextInt(3);
            	int v = 1;
            	int w = 2+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 3, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
                
                // Sheep in the yard
            	for (int[] uvw : new int[][]{
        			{3, 1, 2},
        			})
        		{
            		EntityLiving animal = new EntitySheep(world);
            		IEntityLivingData ientitylivingdata = animal.onSpawnWithEgg(null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
            		
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.placeBlockAtCurrentPosition(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 1 --- //
    
    public static class DesertSmallHouse1 extends StructureVillagePieces.Village
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
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"  F  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertSmallHouse1() {}

        public DesertSmallHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertSmallHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertSmallHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome); Block biomeSandstoneBlock = (Block)blockObject[0]; int biomeSandstoneMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 1, this.materialType, this.biome); Block biomeSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 9, this.materialType, this.biome); Block biomeSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSandstoneSlabTopMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Front door walkway
        		{2,0,1, 2,0,1}, 
        		// Floor
        		{1,0,2, 3,0,4}, 
        		// Front wall
        		{0,1,1, 4,4,1}, 
        		// Left wall
        		{0,1,2, 0,4,4}, 
        		// Right wall
        		{4,1,2, 4,4,4}, 
        		// Back wall
        		{0,1,5, 4,4,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
            
        	// Sandstone
        	for(int[] uvw : new int[][]{
        		// Bottom corners
        		{0,1,1}, {4,1,1}, {0,1,5}, {4,1,5}, 
        		// Above front door
        		{2,3,1}, 
        		// Back wall
        		{2,1,5}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeSandstoneBlock, biomeSandstoneMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{2,3,0, 2}, 
        		// Inside Upper
        		{2,4,2, 0}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Sandstone Slab, bottom
        	for(int[] uuvvww : new int[][]{
        		// Mid unit ceiling
        		{0,4,1, 0,4,1}, {4,4,1, 4,4,1}, {0,4,5, 0,4,5}, {4,4,5, 4,4,5}, 
        		// Roof
        		{0,5,2, 0,5,4}, {1,5,1, 3,5,5}, {4,5,2, 4,5,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, false);	
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{2,1,1, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
        	
            // Windows
        	for (int[] uvw : new int[][]{
        		// Back wall
        		{2,2,5}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.placeBlockAtCurrentPosition(world,
        					new Block[]{Blocks.flower_pot, biomeSandstoneSlabTopBlock}[i],
        					new int[]{9, biomeSandstoneSlabTopMeta}[i],
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{1,1,4, 1,1,4, 3},  
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,3, 2, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{2,1,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 2 --- //
    
    public static class DesertSmallHouse2 extends StructureVillagePieces.Village
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
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		" FF  ",
        		"F FFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertSmallHouse2() {}

        public DesertSmallHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertSmallHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertSmallHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 1, this.materialType, this.biome); Block biomeChiseledSandstoneBlock = (Block)blockObject[0]; int biomeChiseledSandstoneMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(true, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabTopMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoFenceBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoFenceMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
        	// Smooth Sandstone, part 1
        	for(int[] uuvvww : new int[][]{
        		// Front door walkway
        		{2,0,0, 2,0,2}, 
        		// Front wall
        		{1,1,2, 3,4,2}, 
        		// Floor
        		{1,0,3, 3,0,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
            
        	// Chiseled Sandstone
        	for(int[] uvw : new int[][]{
        		// Table
        		{1,1,5}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeChiseledSandstoneBlock, biomeChiseledSandstoneMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{2,3,1, 2}, 
        		// Inside Upper
        		{1,2,5, -1}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Sandstone Wall into fence
        	for(int[] uuvvww : new int[][]{
        		//Posts
        		{0,1,0, 0,3,0}, {4,1,0, 4,3,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab, bottom
        	for(int[] uuvvww : new int[][]{
        		// Awning
        		{0,4,0, 4,4,0}, 
        		// Ceiling
        		{1,5,2, 3,5,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab, top
        	for(int[] uuvvww : new int[][]{
        		// Awning
        		{1,4,1, 3,4,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, false);	
            }
        	
            
        	// Smooth Sandstone, part 2
        	for(int[] uuvvww : new int[][]{
        		// Back wall
        		{1,1,6, 3,3,6}, 
        		// Left wall
        		{0,1,3, 0,3,5}, 
        		// Right wall
        		{4,1,3, 4,3,5}, 
        		// Awning
        		{0,4,1, 0,4,1}, {4,4,1, 4,4,1}, 
        		// Ceiling accents
        		{2,5,2, 2,5,2}, {2,5,6, 2,5,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Cut sandstone - using logs
        	// Vertical
        	for(int[] uuvvww : new int[][]{
            	// Corner columns
        		{0,1,2, 0,4,2}, {4,1,2, 4,4,2}, {0,1,6, 0,4,6}, {4,1,6, 4,4,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	// Along
        	for(int[] uuvvww : new int[][]{
            	// Corner columns
        		{0,4,3, 0,4,5}, {4,4,3, 4,4,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
        	// Across
        	for(int[] uuvvww : new int[][]{
            	// Corner columns
        		{1,4,6, 3,4,6},  
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{2,1,2, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
        	
            // Windows
        	for (int[] uvw : new int[][]{
        		// Back wall
        		{2,2,6}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.placeBlockAtCurrentPosition(world,
        					new Block[]{Blocks.flower_pot, biomeSmoothSandstoneSlabTopBlock}[i],
        					new int[]{9, biomeSmoothSandstoneSlabTopMeta}[i],
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
        	
        	
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		{1,1,3, (GeneralConfig.useVillageColors ? this.townColor3 : 13)}, // Green
        		{3,1,3, (GeneralConfig.useVillageColors ? this.townColor3 : 13)}, // Green
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.carpet, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,5, 3, GeneralConfig.useVillageColors ? this.townColor3 : 13}, // Green
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{1,1,1, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 3 --- //
    
    public static class DesertSmallHouse3 extends StructureVillagePieces.Village
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
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertSmallHouse3() {}

        public DesertSmallHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertSmallHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertSmallHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(true, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabTopMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_button, 0, this.materialType, this.biome); Block biomeWoodButtonBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	// Vertical
        	for(int[] uuvvww : new int[][]{
            	// Corner columns
        		{0,1,1, 0,3,1}, {4,1,1, 4,3,1}, {0,1,5, 0,3,5}, {4,1,5, 4,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
        	// Smooth Sandstone, part 1
        	for(int[] uuvvww : new int[][]{
        		// Entryway
        		{2,0,0, 2,0,1}, 
        		// Front wall
        		{1,1,1, 3,3,1}, 
        		// Back wall
        		{1,1,5, 3,3,5}, 
        		// Floor
        		{1,0,2, 3,0,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
            
        	// Cut Sandstone
        	for(int[] uvw : new int[][]{
        		// Table
        		{1,1,3}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, biomeCutSandstoneBlock, biomeCutSandstoneMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{1,2,2, 0}, {3,2,2, 0}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
        	// Smooth Sandstone, part 2
        	for(int[] uuvvww : new int[][]{
        		// Left wall
        		{0,1,2, 0,3,4}, 
        		// Right wall
        		{4,1,2, 4,3,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab, top
        	for(int[] uuvvww : new int[][]{
        		{1,3,2, 3,3,2}, 
        		{0,3,3, 4,3,3}, 
        		{1,3,4, 3,3,4}, 
        		{2,3,5, 2,3,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Slab, bottom
        	for(int[] uuvvww : new int[][]{
        		{2,4,1, 2,4,1}, {2,4,5, 2,4,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		// Front accent
        		{0,4,1, 0,4,1, 0}, {1,4,1, 1,4,1, 1}, {3,4,1, 3,4,1, 0}, {4,4,1, 4,4,1, 1}, 
        		// Backaccent
        		{0,4,5, 0,4,5, 0}, {1,4,5, 1,4,5, 1}, {3,4,5, 3,4,5, 0}, {4,4,5, 4,4,5, 1}, 
        		// Interior seats
        		{1,1,2, 1,1,2, 2}, {1,1,4, 1,1,4, 3}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
            
        	
        	// Wood buttons
        	for(int[] uuvvwwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
        		{1,3,0, 3,3,0, 2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodButtonBlock, StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode), Blocks.wooden_button, StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode), false);	
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{2,1,1, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Potted plants
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{1,2,3, 10}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,3, 2, GeneralConfig.useVillageColors ? this.townColor3 : 13}, // Green
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{2,1,4, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 4 --- //
    
    public static class DesertSmallHouse4 extends StructureVillagePieces.Village
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
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertSmallHouse4() {}

        public DesertSmallHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertSmallHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertSmallHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(true, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabTopMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	// Vertical
        	for(int[] uuvvww : new int[][]{
            	// Corner columns
        		{0,1,0, 0,3,0}, {4,1,0, 4,3,0}, {0,1,4, 0,3,4}, {4,1,4, 4,3,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Entryway
        		{2,0,0, 2,0,0}, 
        		// Front wall
        		{1,1,0, 3,3,0}, 
        		// Back wall
        		{1,1,4, 1,3,4}, {2,1,4, 2,1,4}, {3,1,4, 3,3,4}, 
        		// Floor
        		{1,0,1, 3,0,3}, 
        		// Left wall
        		{0,1,1, 0,3,3}, 
        		// Right wall
        		{4,1,1, 4,3,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,3,1, 0},
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Smooth Sandstone Slab, top
        	for(int[] uuvvww : new int[][]{
        		{1,4,1, 3,4,3}, 
        		{2,3,4, 2,3,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		// Roof
        		{1,4,0, 1,4,0, 3}, {3,4,0, 3,4,0, 3}, {1,4,4, 1,4,4, 2}, {3,4,4, 3,4,4, 2}, 
        		{0,4,1, 0,4,1, 0}, {0,4,3, 0,4,3, 0}, {4,4,1, 4,4,1, 1}, {4,4,3, 4,4,3, 1}, 
        		})
            {
        		this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{2,1,0, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
        	
            // Terracotta
        	for (int[] uvw : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Floor
        		{2,0,2}, 
        		// Roof
        		{0,4,0}, {2,4,0}, {4,4,0}, 
        		{0,4,2}, {4,4,2}, 
        		{0,4,4}, {2,4,4}, {4,4,4}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.hardened_clay, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3, 1, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 3;
        	int chestV = 1;
        	int chestW = 3;
        	int chestO = 2;
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,1,2, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 5 --- //
    
    public static class DesertSmallHouse5 extends StructureVillagePieces.Village
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
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertSmallHouse5() {}

        public DesertSmallHouse5(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertSmallHouse5 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertSmallHouse5(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_button, 0, this.materialType, this.biome); Block biomeWoodButtonBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	// Vertical
        	for(int[] uuvvww : new int[][]{
            	// Left wall
        		{0,1,2, 0,3,2}, {0,1,4, 0,3,4}, 
        		// Right wall
        		{4,1,2, 4,3,2}, {4,1,4, 4,3,4}, 
        		// Back wall
        		{1,1,5, 1,2,5}, {3,1,5, 3,2,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Entryway
        		{2,0,0, 2,0,1}, 
        		// Floor
        		{1,0,2, 3,0,4}, 
        		// Front wall
        		{0,1,1, 4,3,1}, 
        		// Back wall
        		{0,1,5, 0,3,5}, {2,1,5, 2,3,5}, {4,1,5, 4,3,5}, 
        		// Left wall
        		{0,1,3, 0,3,3}, 
        		// Right wall
        		{4,1,3, 4,3,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
        	// Cut Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Roof
        		{0,4,1, 4,4,5}, 
        		// Ramparts
        		{0,5,1, 0,5,1}, {2,5,1, 2,5,1}, {4,5,1, 4,5,1}, 
        		{0,5,3, 0,5,3}, {4,5,3, 4,5,3}, 
        		{0,5,5, 0,5,5}, {2,5,5, 2,5,5}, {4,5,5, 4,5,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
            
        	
        	// Wood Buttons
        	for(int[] uuvvwwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
        		{1,3,0, 3,3,0, 2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodButtonBlock, StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode), Blocks.wooden_button, StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode), false);	
            }
        	
        	
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,3,4, 2},
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Terracotta
        	for (int[] uvw : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Floor
        		{1,0,2}, {3,0,2}, {2,0,3}, {1,0,4}, {3,0,4}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.hardened_clay, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Potted plants
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{3,1,0, 9}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
            
        	
            // Crafting Table
        	for (int[] uvwm : new int[][]{
        		{3,1,2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.crafting_table, 0, uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{2,1,1, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,3, 2, GeneralConfig.useVillageColors ? this.townColor : 5}, // Lime
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{1,1,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 6 --- //
    
    public static class DesertSmallHouse6 extends StructureVillagePieces.Village
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
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 18;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertSmallHouse6() {}

        public DesertSmallHouse6(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertSmallHouse6 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertSmallHouse6(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
        	
        	// Cut sandstone - using logs
        	// Vertical
        	for(int[] uuvvww : new int[][]{
        		// Corner posts
        		{0,1,1, 0,6,1}, {4,1,1, 4,6,1}, {0,1,5, 0,6,5}, {4,1,5, 4,6,5}, 
        		{0,8,1, 0,14,1}, {4,8,1, 4,14,1}, {0,8,5, 0,14,5}, {4,8,5, 4,14,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
        	
            
        	// Smooth Sandstone, floor 1
        	for(int[] uuvvww : new int[][]{
        		// Entryway
        		{2,0,0, 2,0,1}, 
        		// Floor
        		{1,0,2, 3,0,4}, 
        		// Front wall
        		{1,1,1, 1,3,1}, {3,1,1, 3,3,1}, 
        		// Corner stairwell block
        		{3,3,2, 3,3,2}, 
        		// Back wall
        		{1,1,5, 3,3,5}, 
        		// Stairs
        		{1,1,4, 3,1,4}, {3,2,4, 3,2,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	// Torches, floor 1
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{1,2,2, 0},
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Smooth Sandstone, floor 1 and 2
        	for(int[] uuvvww : new int[][]{
        		// Floor 1
        		// Left wall
        		{0,1,2, 0,3,4}, 
        		// Right wall
        		{4,1,2, 4,3,4}, 
        		
        		// Floor 2
        		// Back wall
        		{1,4,5, 3,6,5}, 
        		// Front wall
        		{1,4,1, 1,6,1}, {3,4,1, 3,6,1}, {2,4,1, 2,4,1}, {2,6,1, 2,6,1}, 
        		// Corner stairwell block
        		{1,4,2, 1,4,2}, 
        		// Central block
        		{2,5,3, 2,5,3}, 
        		// Corner stairwell block
        		{1,5,4, 1,5,4}, 
        		// Corner stairwell block
        		{3,6,4, 3,6,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	// Torches, floor 2
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{3,4,4, 2},
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Smooth Sandstone, floor 2 and 3
        	for(int[] uuvvww : new int[][]{
        		// Floor 2
        		// Left wall
        		{0,4,2, 0,6,4}, 
        		// Right wall
        		{4,4,2, 4,6,4}, 
        		
        		// Floor 3
        		// Left wall
        		{0,7,2, 0,9,4}, 
        		// Right wall
        		{4,7,2, 4,9,4}, 
        		// Corner stairwell block
        		{3,7,2, 3,7,2}, {1,8,2, 1,8,2}, {1,9,4, 1,9,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	// Torches, floor 3
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{1,7,4, 1}, 
        		{3,9,2, 3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Smooth Sandstone, floor 3 and 4
        	for(int[] uuvvww : new int[][]{
        		// Floor 3
        		// Front wall
        		{1,7,1, 3,8,1}, {1,9,1, 1,9,1}, {3,9,1, 3,9,1}, 
        		// Back wall
        		{1,7,5, 3,9,5}, 
        		
        		// Floor 4
        		// Left wall
        		{0,10,2, 0,12,4}, 
        		// Right wall
        		{4,10,2, 4,12,4}, 
        		// Corner stairwell block
        		{3,10,4, 3,10,4}, {3,11,2, 3,11,2}, {1,12,2, 1,12,2}, 
        		// Central block
        		{2,12,3, 2,12,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	// Torches, floor 4
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{3,12,4, 3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Smooth Sandstone, floor 4 and 5
        	for(int[] uuvvww : new int[][]{
        		// Floor 4
        		// Front wall
        		{1,10,1, 3,12,1}, 
        		// Back wall
        		{1,10,5, 3,12,5}, 
        		
        		// Floor 5
        		// Left wall
        		{0,13,2, 0,14,4}, 
        		// Right wall
        		{4,13,2, 4,14,4}, 
        		// Front wall
        		{1,13,1, 1,13,1}, {3,13,1, 3,13,1}, {1,14,1, 3,14,1}, 
        		// Back wall
        		{1,13,5, 3,14,5}, 
        		// Corner stairwell block
        		{1,13,4, 1,13,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
        	// Cut Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Roof
        		{0,15,1, 0,15,5}, {4,15,1, 4,15,5}, {1,15,1, 3,15,1}, {1,15,5, 3,15,5}, {2,15,2, 3,15,3}, 
        		// Ramparts
        		{0,16,1, 0,16,1}, {2,16,1, 2,16,1}, {4,16,1, 4,16,1}, 
        		{0,16,3, 0,16,3}, {4,16,3, 4,16,3}, 
        		{0,16,5, 0,16,5}, {2,16,5, 2,16,5}, {4,16,5, 4,16,5}, 
        		// Between log pillars
        		{0,7,1, 0,7,1}, {4,7,1, 4,7,1}, {0,7,5, 0,7,5}, {4,7,5, 4,7,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
        	
        	
        	// Cut sandstone - using logs
        	// Across
        	for(int[] uuvvww : new int[][]{
        		// Corner posts
        		{2,3,1, 2,3,1}, 
        		{2,7,1, 2,7,1}, 
        		{2,11,1, 2,11,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
        	
        	
            // Torches on the roof
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Roof
        		{0,17,1, -1}, {4,17,1, -1}, {0,17,5, -1}, {4,17,5, -1}, 
        		// Front
        		{2,3,0, -1}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		{1,1,3, 3}, 
        		{2,2,4, 0}, 
        		{3,3,3, 2}, 
        		{2,4,2, 1},
        		{1,5,3, 3}, 
        		{2,6,4, 0}, 
        		{3,7,3, 2}, 
        		{2,8,2, 1},
        		{1,9,3, 3}, 
        		{2,10,4, 0}, 
        		{3,11,3, 2}, 
        		{2,12,2, 1},
        		{1,13,3, 3}, 
        		{2,14,4, 0}, 
        		{3,15,4, 0},
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{2,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3, 3, GeneralConfig.useVillageColors ? this.townColor3 : 13}, // Green
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 13;
        	int chestW = 3;
        	int chestO = 2;
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,1,2, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 7 --- //
    
    public static class DesertSmallHouse7 extends StructureVillagePieces.Village
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
        		"FFF   F",
        		"FFF F F",
        		"FFF   F",
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 1; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 3; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertSmallHouse7() {}

        public DesertSmallHouse7(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertSmallHouse7 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertSmallHouse7(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.fence, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoFenceBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoFenceMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
        	// Sandstone Wall
        	for(int[] uuvvww : new int[][]{
        		{0,1,0, 0,1,3}, {2,1,0, 2,1,3}, {6,1,0, 6,1,3}, 
        		{3,1,0, 5,1,0}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, biomeSandstoneWallIntoFenceBlock, biomeSandstoneWallIntoFenceMeta, false);	
            }
            
        	
        	// Cut Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Roof
        		{2,1,6, 2,1,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
        	
        	
        	// Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front entrance
        		{0,2,2, -1}, {2,2,2, -1}, 
        		// On the table
        		{2,2,6, -1}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Walkway
        		{1,0,0, 1,0,4}, 
        		// Back door
        		{4,0,4, 4,0,4}, 
        		// Floor
        		{1,0,5, 5,0,6}, 
        		// Front wall
        		{0,1,4, 6,3,4}, 
        		// Back wall
        		{0,1,7, 6,3,7}, 
        		// Left wall
        		{0,1,5, 0,1,6}, 
        		// Right wall
        		{6,1,5, 6,1,6}, 
        		// Ceiling
        		{0,3,5, 6,3,6}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
            
        	// Smooth Sandstone Slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Windows
        		{0,2,5, 0,2,6}, 
        		{6,2,5, 6,2,6}, 
        		{2,2,7, 2,2,7}, {4,2,7, 4,2,7}, 
        		// Ramparts
        		{0,4,4, 0,4,4}, {2,4,4, 2,4,4}, {4,4,4, 4,4,4}, {6,4,4, 6,4,4}, 
        		{0,4,7, 0,4,7}, {2,4,7, 2,4,7}, {4,4,7, 4,4,7}, {6,4,7, 6,4,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		{1,1,6, 1}, 
        		{3,1,6, 0}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Sand under the cactus
        	for (int[] uvwo : new int[][]{ 
        		{4,0,2}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.sand, this.materialType==MaterialType.MESA?1:0, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	// Cactus
        	for(int[] uuvvww : new int[][]{
        		{4,1,2, 4,3,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.cactus, 0, Blocks.cactus, 0, false);	
            }
            
        	
            // Potted plants
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{4,2,0, 10}, 
        		{6,2,2, 10}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{1,1,4, 0, 1, 1}, 
            	{4,1,4, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,6, 3, GeneralConfig.useVillageColors ? this.townColor : 5}, // Lime
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{1,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,1,5, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 8 --- //
    
    public static class DesertSmallHouse8 extends StructureVillagePieces.Village
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
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        		"FFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertSmallHouse8() {}

        public DesertSmallHouse8(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertSmallHouse8 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertSmallHouse8(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 1, this.materialType, this.biome); Block biomeSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(true, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabTopMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome); Block biomeSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseGreenCoralOrPottedCactus(); Block biomeGreenCoralOrPottedCactusBlock = (Block)blockObject[0]; int biomeGreenCoralOrPottedCactusMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Walkway
        		{2,0,0, 2,0,0}, 
        		// Floor
        		{1,0,1, 3,0,3}, 
        		// Front wall
        		{1,1,0, 3,3,0}, 
        		// Back wall
        		{1,1,4, 3,3,4}, 
        		// Left wall
        		{0,1,1, 0,3,3}, 
        		// Right wall
        		{4,1,1, 4,3,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Terracotta
        	for(int[] uuvvww : new int[][]{
        		// Corner posts
        		{0,1,0, 0,4,0}, {4,1,0, 4,4,0}, {0,1,4, 0,4,4}, {4,1,4, 4,4,4}, 
        		// Table
        		{3,1,2, 3,1,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.hardened_clay, 0, Blocks.hardened_clay, 0, false);	
            }
        	
        	
        	// Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,3,1, 0}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
        	// Sandstone Slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Windows
        		{0,4,1, 0,4,3}, {1,4,0, 3,4,4}, {4,4,1, 4,4,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, false);	
            }
        	
            
        	// Smooth Sandstone Slab (top)
        	for(int[] uuvvww : new int[][]{
        		{0,3,2, 0,3,2}, 
        		{2,3,4, 2,3,4}, 
        		{4,3,2, 4,3,2}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, false);	
            }
        	
        	
        	// Sandstone Stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		{3,1,1, 2}, 
        		{3,1,3, 3}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
            // Green coral
        	for (int[] uvwm : new int[][]{ 
        		{3,2,2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, biomeGreenCoralOrPottedCactusBlock, biomeGreenCoralOrPottedCactusMeta, uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
            
        	
            // Crafting Table
        	for (int[] uvwm : new int[][]{
        		{1,1,3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.crafting_table, 0, uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{2,1,0, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.placeBlockAtCurrentPosition(world, biomeWoodDoorBlock, StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height],
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,1,2, 0, GeneralConfig.useVillageColors ? this.townColor : 5}, // Lime
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwoc[3];
            		int u = uvwoc[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwoc[1];
            		int w = uvwoc[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead), uvwoc[4]);
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{2,1,2, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	                	
	        		for (int[] ia : villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Tannery --- //
    
    public static class DesertTannery1 extends StructureVillagePieces.Village
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertTannery1() {}

        public DesertTannery1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertTannery1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertTannery1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 9, this.materialType, this.biome); Block biomeSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSandstoneSlabTopMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	
            
        	// Logs
        	// Across
        	for(int[] uuvvww : new int[][]{
        		{0,4,0, 6,4,0}, {0,4,5, 6,4,5}, 
        		{0,7,2, 6,7,2},  
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, biomeLogHorAcrossBlock, biomeLogHorAcrossMeta, false);	
            }
        	// Along
        	for(int[] uuvvww : new int[][]{
        		// Entryway
        		{0,4,1, 0,4,4}, {0,7,3, 0,7,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongBlock, biomeLogHorAlongMeta, biomeLogHorAlongBlock, biomeLogHorAlongMeta, false);	
            }
            
        	
        	// Smooth Sandstone, part 1
        	for(int[] uuvvww : new int[][]{
        		// Entryway
        		{0,1,0, 6,1,0}, {1,1,1, 5,1,1}, 
        		// Under bottom doors
        		{1,1,2, 1,1,2}, {3,1,2, 3,1,2}, {5,1,2, 5,1,2}, 
        		// Floor
        		{0,1,3, 6,1,4}, 
        		// Bottom front wall
        		{1,2,2, 5,3,2}, 
        		// Left wall
        		{0,2,0, 0,3,5}, {0,5,2, 0,6,5}, 
        		// Right wall
        		{6,2,0, 6,3,5}, {6,4,1, 6,4,4}, {6,5,2, 6,6,5}, {6,7,3, 6,7,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
        	// Sand
        	for(int[] uuvvww : new int[][]{
        		// Front pillars
        		{0,1,1, 0,1,5}, {1,1,5, 5,1,5}, {6,1,1, 6,1,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);	
            }
        	
        	
        	// Torches, part 1
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{2,3,1, 2}, {4,3,1, 2}, 
        		// Interior
        		{1,6,3, 1}, {5,6,3, 3}, 
        		// By the cauldron
        		{5,3,4, 3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Smooth Sandstone, part 2
        	for(int[] uuvvww : new int[][]{
        		// Front pillars
        		{2,2,0, 2,3,0}, {4,2,0, 4,3,0}, 
        		// First floor/ceiling separation
        		{1,4,1, 5,4,3}, {5,4,3, 5,4,3}, 
        		// Top front wall
        		{1,5,2, 5,6,2}, 
        		// Back wall
        		{1,2,5, 5,3,5}, {0,5,5, 6,6,5}, {1,7,5, 6,7,5}, 
        		// Roof
        		{0,8,2, 6,8,5}, {2,9,2, 4,9,2}, {2,9,5, 4,9,5}, {1,9,3, 1,9,4}, {5,9,3, 5,9,4}, 
        		// Stairwell
        		{1,2,4, 2,2,4}, {1,3,4, 1,3,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Torches, part 2
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{1,6,1, 2}, {5,6,1, 2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		{3,2,4, 1}, {2,3,4, 1}, {1,4,4, 1}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Terracotta
        	for(int[] uvw : new int[][]{
        		// Roof accent
        		{0,8,2}, {1,9,2}, {2,8,2}, {3,9,2}, {4,8,2}, {5,9,2}, {6,8,2}, 
        		{0,8,5}, {1,9,5}, {2,8,5}, {3,9,5}, {4,8,5}, {5,9,5}, {6,8,5}, 
            	})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.hardened_clay, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Windows - Smooth sandstone slab bottom, regular sandstone slab top
        	for (int[] uvw : new int[][]{
        		{1,5,5}, {3,5,5}, {5,5,5}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.placeBlockAtCurrentPosition(world,
        					new Block[]{biomeSmoothSandstoneSlabBottomBlock, biomeSandstoneSlabTopBlock}[i],
        					new int[]{biomeSmoothSandstoneSlabBottomMeta, biomeSandstoneSlabTopMeta}[i],
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
            
        	
            // Potted plants
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{1,5,0, 9}, 
        		{5,5,1, 9}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
            
        	
            // Cauldron
        	for (int[] uuvvww : new int[][]{
        		{5,2,4}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.cauldron, 3, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// Bottom
            	{1,2,2, 0, 1, 1}, {3,2,2, 0, 1, 1}, {5,2,2, 0, 1, 0}, 
            	// Top
            	{2,5,2, 0, 1, 0}, {4,5,2, 0, 1, 0}, 
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
        		{0,GROUND_LEVEL,-1}, 
        		{1,GROUND_LEVEL,-1}, 
        		{2,GROUND_LEVEL,-1}, 
        		{3,GROUND_LEVEL,-1}, 
        		{4,GROUND_LEVEL,-1},
        		{5,GROUND_LEVEL,-1}, 
        		{6,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 1+random.nextInt(5);
            	int v = 2+(random.nextBoolean()?3:0);
            	int w = 3;
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 2, 0);
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    
    // --- Temple 1 --- //
    
    public static class DesertTemple1 extends StructureVillagePieces.Village
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
        		"FFF    FFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		" FFFFFFFF ",
        		" FFFFFFFF ",
        		" FFFFFFFF ",
        		" FFFFFFFF ",
        		" FFFFFFFF ",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertTemple1() {}

        public DesertTemple1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertTemple1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertTemple1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 9, this.materialType, this.biome); Block biomeSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSandstoneSlabTopMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 1, this.materialType, this.biome); Block biomeSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
        	
        	// Smooth Sandstone, part 1
        	for(int[] uuvvww : new int[][]{
        		// Front wall
        		{3,1,1, 3,3,1}, {4,3,1, 5,3,1}, {6,1,1, 6,3,1}, 
        		// Left wall
        		{1,1,3, 1,3,7}, 
        		// Right wall
        		{8,1,3, 8,3,7}, 
        		// Back wall
        		{3,1,9, 6,3,9}, 
        		// Floor
        		{1,0,1, 2,0,2}, {7,0,1, 8,0,2}, {1,0,8, 2,0,9}, {7,0,8, 8,0,9}, 
        		{2,0,3, 2,0,7}, {3,0,2, 3,0,8}, {7,0,3, 7,0,7}, {6,0,2, 6,0,8}, 
        		{4,0,7, 5,0,8}, {4,0,4, 5,0,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Torches, part 1
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{3,2,0, 2}, {6,2,0, 2}, 
        		// Interior
        		{2,2,4, 1}, {2,2,6, 1}, {7,2,4, 3}, {7,2,6, 3}, 
        		{4,2,8, 2}, {5,2,8, 2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cut Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Corners
        		// Front left
        		{0,0,0, 2,0,0}, {3,0,1, 3,0,1}, {0,0,1, 0,0,2}, 
        		// Front right
        		{7,0,0, 9,0,0}, {6,0,1, 6,0,1}, {9,0,1, 9,0,2}, 
        		// Back left
        		{0,0,8, 0,0,10}, {1,0,10, 2,0,10}, {9,0,8, 9,0,10}, {7,0,10, 8,0,10}, 
        		// Side bases
        		{1,0,3, 1,0,7}, {8,0,3, 8,0,7}, {3,0,9, 6,0,9}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
            
        	
        	// Smooth Sandstone, part 2
        	for(int[] uuvvww : new int[][]{
        		// Corners
        		// Front left
        		{0,1,0, 0,5,0}, {2,1,0, 2,5,0}, {0,1,2, 0,5,2}, {2,5,2, 2,5,2}, {0,1,1, 0,1,1}, {0,4,1, 0,4,1}, {1,1,0, 1,1,0}, {1,4,0, 1,4,0}, 
        		{1,4,2, 1,4,2}, {2,4,1, 2,4,2}, 
        		// Front right
        		{9,1,0, 9,5,0}, {7,1,0, 7,5,0}, {9,1,2, 9,5,2}, {7,5,2, 7,5,2}, {9,1,1, 9,1,1}, {9,4,1, 9,4,1}, {8,1,0, 8,1,0}, {8,4,0, 8,4,0}, 
        		{8,4,2, 8,4,2}, {7,4,1, 7,4,2}, 
        		// Back left
        		{0,1,10, 0,5,10}, {2,1,10, 2,5,10}, {0,1,8, 0,5,8}, {2,5,8, 2,5,8}, {0,1,9, 0,1,9}, {0,4,9, 0,4,9}, {1,1,10, 1,1,10}, {1,4,10, 1,4,10}, 
        		{1,4,8, 1,4,8}, {2,4,8, 2,4,9}, 
        		// Back right
        		{9,1,10, 9,5,10}, {7,1,10, 7,5,10}, {9,1,8, 9,5,8}, {7,5,8, 7,5,8}, {9,1,9, 9,1,9}, {9,4,9, 9,4,9}, {8,1,10, 8,1,10}, {8,4,10, 8,4,10}, 
        		{8,4,8, 8,4,8}, {7,4,8, 7,4,9}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		// Archway
        		{4,2,1, 5}, {5,2,1, 4}, 
        		// Entrance
        		{4,0,3, 3}, {5,0,3, 3}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
    		// Glazed terracotta
        	int gtColor = GeneralConfig.useVillageColors ? this.townColor2 : 0; // White
    		Object[] tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(gtColor, (0 + this.coordBaseMode + (this.coordBaseMode < 2 ? 1 : 0))%4);
    		
        	if (tryGlazedTerracotta != null)
        	{
        		// Square under square awning
        		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], 4, 0, 6, structureBB);
        		
        		tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(gtColor, (1 + this.coordBaseMode + (this.coordBaseMode < 2 ? 3 : 0))%4);
        		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], 5, 0, 6, structureBB);
        		
        		tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(gtColor, (2 + this.coordBaseMode + (this.coordBaseMode < 2 ? 1 : 0))%4);
        		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], 5, 0, 5, structureBB);
        		
        		tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(gtColor, (3 + this.coordBaseMode + (this.coordBaseMode < 2 ? 3 : 0))%4);
        		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], 4, 0, 5, structureBB);
        	}
        	else
        	{
        		this.fillWithMetadataBlocks(world, structureBB, 4, 0, 5, 5, 0, 6, Blocks.stained_hardened_clay, gtColor, Blocks.stained_hardened_clay, gtColor, false);
        	}
        	
        	
            // Windows - Potted cactus bottom, regular sandstone slab top
        	for (int[] uvw : new int[][]{
        		{1,2,0}, {8,2,0}, {9,2,1}, {9,2,9}, {8,2,10}, {1,2,10}, {0,2,9}, {0,2,1}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.placeBlockAtCurrentPosition(world,
        					// Bottom
        					new Block[]{Blocks.flower_pot, biomeSandstoneSlabTopBlock}[i],
        					// Top
        					new int[]{9, biomeSandstoneSlabTopMeta}[i],
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
            
        	
        	// Sandstone Slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Ramparts
        		{0,6,0, 0,6,0}, {2,6,0, 2,6,0}, {2,6,2, 2,6,2}, {0,6,2, 0,6,2}, {1,5,1, 1,5,1}, 
        		{7,6,0, 7,6,0}, {9,6,0, 9,6,0}, {9,6,2, 9,6,2}, {7,6,2, 7,6,2}, {8,5,1, 8,5,1}, 
        		{0,6,8, 0,6,8}, {2,6,8, 2,6,8}, {2,6,10, 2,6,10}, {0,6,10, 0,6,10}, {1,5,9, 1,5,9}, 
        		{7,6,8, 7,6,8}, {9,6,8, 9,6,8}, {9,6,10, 9,6,10}, {7,6,10, 7,6,10}, {8,5,9, 8,5,9}, 
        		// Roof
        		{1,4,3, 2,4,7}, {7,4,3, 8,4,7}, {3,4,1, 6,4,9}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, biomeSandstoneSlabBottomBlock, biomeSandstoneSlabBottomMeta, false);	
            }
            
        	
        	// Smooth Sandstone Slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Towers
        		{0,5,1, 0,5,1}, {1,5,0, 1,5,0}, {2,5,1, 2,5,1}, {1,5,2, 1,5,2}, 
        		{7,5,1, 7,5,1}, {8,5,0, 8,5,0}, {9,5,1, 9,5,1}, {8,5,2, 8,5,2}, 
        		{0,5,9, 0,5,9}, {1,5,8, 1,5,8}, {2,5,9, 2,5,9}, {1,5,10, 1,5,10}, 
        		{7,5,9, 7,5,9}, {8,5,8, 8,5,8}, {9,5,9, 9,5,9}, {8,5,10, 8,5,10}, 
        		// Ceiling accent
        		{3,4,4, 3,4,4}, {3,4,6, 3,4,6}, {4,4,5, 4,4,5}, 
        		{6,4,4, 6,4,4}, {6,4,6, 6,4,6}, {5,4,5, 5,4,5}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
            
        	
            // Brewing Stand
        	for (int[] uvw : new int[][]{
        		{1,1,1}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.brewing_stand, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 8;
        	int chestV = 1;
        	int chestW = 1;
        	int chestO = 0;
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_temple");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{0,GROUND_LEVEL,-1}, 
        		{1,GROUND_LEVEL,-1}, 
        		{2,GROUND_LEVEL,-1}, 
        		{3,GROUND_LEVEL,-1}, 
        		{4,GROUND_LEVEL,-1},
        		{5,GROUND_LEVEL,-1}, 
        		{6,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(6);
            	int v = 1;
            	int w = 3+random.nextInt(6);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 2, 1, 0);
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 2;}
    }
    
    
    
    // --- Temple 2 --- //
    
    public static class DesertTemple2 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        		"FFFFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertTemple2() {}

        public DesertTemple2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertTemple2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertTemple2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 9, this.materialType, this.biome); Block biomeSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSandstoneSlabTopMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 1, this.materialType, this.biome); Block biomeSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(true, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabTopMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
        	
        	// Smooth Sandstone, part 1
        	for(int[] uuvvww : new int[][]{
        		// Left wall
        		{0,1,1, 0,4,1}, {0,1,2, 0,3,5}, 
        		// Right wall
        		{11,1,1, 11,4,1}, {11,1,2, 11,3,5}, 
        		// Front pillars
        		{2,1,1, 2,3,1}, {4,1,1, 4,3,1}, {7,1,1, 7,3,1}, {9,1,1, 9,3,1}, 
        		// Walls behind wings
        		{0,1,5, 0,3,5}, {1,1,5, 1,1,5}, {2,1,5, 2,3,5}, 
        		{11,1,5, 11,3,5}, {10,1,5, 10,1,5}, {9,1,5, 9,3,5}, 
        		// Interior
        		{3,1,5, 3,4,5}, {4,3,5, 4,3,5}, {7,3,5, 7,3,5}, {8,1,5, 8,4,5}, 
        		// Back wing
        		{3,1,6, 3,1,6}, {3,4,6, 3,4,6}, {3,1,7, 3,4,7}, {4,1,8, 4,4,8}, 
        		{8,1,6, 8,1,6}, {8,4,6, 8,4,6}, {8,1,7, 8,4,7}, {7,1,8, 7,4,8}, 
        		// Roof
        		{1,4,1, 4,4,4}, {5,4,4, 6,4,4}, {7,4,1, 10,4,4}, 
        		{4,4,5, 4,4,5}, {7,4,5, 7,4,5}, 
        		{5,5,6, 6,5,7}, 
        		// Floor
        		{1,0,2, 10,0,4}, {5,0,5, 6,0,5}, {5,0,8, 6,0,8}, {4,0,6, 4,0,7}, {7,0,6, 7,0,7}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{4,3,0, 2}, {7,3,0, 2}, 
        		// Interior
        		{3,3,4, 2}, {8,3,4, 2}, 
        		// Back wing
        		{5,3,8, 1}, {6,3,8, 3}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Smooth Sandstone, part 2
        	for(int[] uuvvww : new int[][]{
        		// Back wall
        		{5,1,9, 6,4,9}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		// Archway bases
        		{0,1,0, 3}, {4,1,0, 3}, {7,1,0, 3}, {11,1,0, 3}, 
        		// Front archway
        		{5,3,1, 5}, {6,3,1, 4}, 
        		// Top accents
        		{0,5,1, 0}, {1,5,1, 1}, {3,5,1, 0}, {4,5,1, 1}, {5,4,1, 1}, {6,4,1, 0}, {7,5,1, 0}, {8,5,1, 1}, {10,5,1, 0}, {11,5,1, 1}, 
        		// Interior archway
        		{4,2,5, 5}, {5,3,5, 5}, {6,3,5, 4}, {7,2,5, 4}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
    		// Glazed terracotta
        	int gtColor = GeneralConfig.useVillageColors ? this.townColor : 5; // Lime
    		Object[] tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(gtColor, (0 + this.coordBaseMode + (this.coordBaseMode < 2 ? 1 : 0))%4);
        	
        	if (tryGlazedTerracotta != null)
        	{
        		// Square under square awning
        		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], 5, 0, 7, structureBB);
        		
        		tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(gtColor, (1 + this.coordBaseMode + (this.coordBaseMode < 2 ? 3 : 0))%4);
        		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], 6, 0, 7, structureBB);
        		
        		tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(gtColor, (2 + this.coordBaseMode + (this.coordBaseMode < 2 ? 1 : 0))%4);
        		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], 6, 0, 6, structureBB);
        		
        		tryGlazedTerracotta = ModObjects.chooseModGlazedTerracotta(gtColor, (3 + this.coordBaseMode + (this.coordBaseMode < 2 ? 3 : 0))%4);
        		this.placeBlockAtCurrentPosition(world, (Block)tryGlazedTerracotta[0], (Integer)tryGlazedTerracotta[1], 5, 0, 6, structureBB);
        	}
        	else
        	{
        		// Square under awning
        		this.fillWithMetadataBlocks(world, structureBB, 5, 0, 6, 6, 0, 7, Blocks.stained_hardened_clay, gtColor, Blocks.stained_hardened_clay, gtColor, false);
        	}
        	
        	
            // Potted cactus
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{2,1,0, 9}, 
        		{9,1,0, 9}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
        	
        	
            // Windows - Sandstone slab bottom, Sandstone slab top
        	for (int[] uvw : new int[][]{
        		{0,2,3}, {1,2,5}, {3,2,6}, 
        		{11,2,3}, {10,2,5}, {8,2,6}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.placeBlockAtCurrentPosition(world,
        					new Block[]{biomeSandstoneSlabBottomBlock, biomeSandstoneSlabTopBlock}[i],
        					new int[]{biomeSandstoneSlabBottomMeta, biomeSandstoneSlabTopMeta}[i],
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
            
        	
        	// Smooth Sandstone Slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Interior
        		{3,1,4, 4,1,4}, {7,1,4, 8,1,4}, {10,1,3, 10,1,4}, 
        		// Roof
        		{0,4,2, 0,4,4}, {5,4,2, 6,4,3}, {11,4,2, 11,4,4}, 
        		{1,4,5, 2,4,5}, {9,4,5, 10,4,5}, 
        		// Dome
        		{5,5,5, 6,5,5}, {5,5,8, 6,5,8}, {4,5,6, 4,5,7}, {7,5,6, 7,5,7}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
            
        	
        	// Smooth Sandstone Slab (top)
        	for(int[] uuvvww : new int[][]{
        		{1,3,1, 1,3,1}, {10,3,1, 10,3,1}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, false);	
            }
            
        	
            // Brewing Stand
        	for (int[] uvw : new int[][]{
        		{1,1,4}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, Blocks.brewing_stand, 0, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{0,GROUND_LEVEL,-1}, 
        		{1,GROUND_LEVEL,-1}, 
        		{2,GROUND_LEVEL,-1}, 
        		{3,GROUND_LEVEL,-1}, 
        		{4,GROUND_LEVEL,-1},
        		{5,GROUND_LEVEL,-1}, 
        		{6,GROUND_LEVEL,-1}, 
        		{7,GROUND_LEVEL,-1}, 
        		{8,GROUND_LEVEL,-1}, 
        		{9,GROUND_LEVEL,-1}, 
        		{10,GROUND_LEVEL,-1}, 
        		{11,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(6);
            	int v = 1;
            	int w = 3+random.nextInt(6);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 2, 1, 0);
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 2;}
    }
    
    
    
    // --- Tool Smithy --- //
    
    public static class DesertToolSmith1 extends StructureVillagePieces.Village
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
        		"FFFFF    ",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFF F F",
        		"FFFFF FF ",
        		"FFFFFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 3; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 4; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 0; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertToolSmith1() {}

        public DesertToolSmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertToolSmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertToolSmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(true, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabTopMeta = (Integer)blockObject[1];
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_button, 0, this.materialType, this.biome); Block biomeWoodButtonBlock = (Block)blockObject[0];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.wooden_door, 0, this.materialType, this.biome); Block biomeWoodDoorBlock = (Block)blockObject[0];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
        	
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Right walkway
        		{6,2,0, 6,2,3}, 
        		// Left walkway base
        		{1,3,1, 3,3,2}, {2,4,2, 2,4,2}, 
        		// Front wall
        		{0,1,3, 3,7,3}, {4,3,3, 4,7,3}, {5,3,3, 8,5,3}, 
        		
        		// Right unit floor
        		{5,2,4, 7,2,4}, {7,2,5, 7,2,5}, {5,2,6, 7,2,6}, 
        		// Right unit right wall
        		{8,3,3, 8,5,7}, 
        		// Right unit back wall
        		{5,3,7, 7,5,7}, 
        		
        		// Separator wall
        		{4,1,4, 4,7,4}, {4,4,5, 4,7,5}, {4,1,6, 4,7,7}, {4,3,8, 4,7,8}, 
        		
        		// Back wall
        		{0,1,8, 3,7,8}, 
        		//Left wall
        		{0,0,4, 0,7,7}, 
        		
        		// Upper unit floor
        		{1,4,4, 3,4,7},
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
            
        	
        	// Air
        	for(int[] uuvvww : new int[][]{
        		// Basement
        		{1,1,4, 3,3,7},
        		// Stairway
        		{4,1,5, 4,3,5}, 
        		{5,2,5, 5,3,5}, 
            	})
            {
        		this.fillWithAir(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5]);
            }
        	
        	
        	// Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		// Front
        		{1,6,2, 2}, {3,6,2, 2}, {5,4,2, 2}, {7,4,2, 2}, 
        		// Inside left
        		{2,6,7, 2}, 
        		// Inside right
        		{6,4,6, 2}, 
        		// Basement
        		{2,3,4, 0}, {2,3,7, 2}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Platformed walkway
        		{0,3,0, 4,3,0, 3}, {0,3,1, 0,3,2, 0}, {4,3,1, 4,3,2, 1}, 
        		{1,4,1, 3,4,1, 3}, {1,4,2, 1,4,2, 0}, {3,4,2, 3,4,2, 1}, 
        		// To the BASEMENT
        		{6,2,5, 6,2,5, 0}, {5,1,5, 5,1,5, 0}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
        	
        	// Terracotta
        	for(int[] uuvvww : new int[][]{
        		// Interior
        		{0,6,4, 0,6,7}, {1,6,8, 3,6,8}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.hardened_clay, 0, Blocks.hardened_clay, 0, false);	
            }
        	

        	
            // Glazed terracotta
        	Object[] tryGlazedTerracotta;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{2,0,4, 0, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{2,0,6, 0, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{1,0,4, 1, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{1,0,6, 1, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{3,0,6, 1, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{3,0,5, 2, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{1,0,5, 2, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{1,0,7, 2, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{3,0,7, 2, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{3,0,4, 3, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{2,0,5, 3, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
        		{2,0,7, 3, GeneralConfig.useVillageColors ? this.townColor5 : 3}, // Light Blue
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
        	
        	
            // Potted cactus
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{7,3,1, 9}, 
        		{8,3,2, 9}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
            
        	
        	// Smooth Sandstone Slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Ramparts
        		// Left building
        		{0,8,3, 0,8,3}, {0,8,5, 0,8,6}, {0,8,8, 0,8,8}, 
        		{2,8,3, 2,8,3}, {2,8,8, 2,8,8}, 
        		{4,8,3, 4,8,3}, {4,8,5, 4,8,6}, {4,8,8, 4,8,8}, 
        		// Right building
        		{6,6,3, 6,6,3}, {8,6,3, 8,6,3}, {8,6,5, 8,6,5}, {8,6,7, 8,6,7}, {6,6,7, 6,6,7}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
            
        	
        	// Smooth Sandstone Slab (top)
        	for(int[] uuvvww : new int[][]{
        		// Left roof
        		{1,7,4, 3,7,7}, 
        		// Right roof
        		{5,5,4, 7,5,6}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, biomeSmoothSandstoneSlabTopBlock, biomeSmoothSandstoneSlabTopMeta, false);	
            }
        	
        	
        	// Wood Buttons
        	for(int[] uuvvwwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
        		{2,2,7, 3,2,7, 2}, 
        		{2,2,4, 3,2,4, 0}, 
        		{1,2,4, 1,2,7, 1}, 
        		{3,2,6, 3,2,6, 3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodButtonBlock, StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode), Blocks.wooden_button, StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode), false);	
            }
            
            
            // Smithing table
        	blockObject = ModObjects.chooseModSmithingTable(); Block smithingTableBlock = (Block) blockObject[0]; int smithingTableMeta = (Integer) blockObject[1];
        	for (int[] uvw : new int[][]{
        		{1,5,6}, 
        		})
            {
        		this.placeBlockAtCurrentPosition(world, smithingTableBlock, smithingTableMeta, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 1;
        	int chestV = 5;
        	int chestW = 7;
        	int chestO = 1;
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_toolsmith");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            
            // Doors
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	
            	// Left
            	{2,5,3, 0, 1, 0}, 
            	// Right
            	{6,3,3, 0, 1, 1}, 
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
        		{0,GROUND_LEVEL,-1}, 
        		{1,GROUND_LEVEL,-1}, 
        		{2,GROUND_LEVEL,-1}, 
        		{3,GROUND_LEVEL,-1}, 
        		{4,GROUND_LEVEL,-1},
        		{5,GROUND_LEVEL,-1}, 
        		{6,GROUND_LEVEL,-1}, 
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 1;
            	int v = 5;
            	int w = 7;
            	
            	while (u==1 && v==5 && (w==6||w==7))
            	{
                	u = 1 + random.nextInt(3);
                	v = 1 + (random.nextBoolean()? 4:0);
                	w = 4 + random.nextInt(4);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 3, 0);
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // --- Weapon Smithy --- //
    
    public static class DesertWeaponsmith1 extends StructureVillagePieces.Village
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
        		"FFFFFF    ",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"  FFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0; // How far "rightward" to start scanning the structure's front edge to determine the median ground level
    	private static final int DECREASE_MAX_U = 3; // How far "leftward" to start scanning the structure's front edge to determine the median ground level
    	
    	private int averageGroundLevel = -1;
    	
        public DesertWeaponsmith1() {}

        public DesertWeaponsmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertWeaponsmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertWeaponsmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 2, this.materialType, this.biome); Block biomeCutSandstoneBlock = (Block)blockObject[0]; int biomeCutSandstoneMeta = (Integer)blockObject[1];
        	blockObject = ModObjects.chooseModSmoothSandstoneBlock(false); if (blockObject==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSmoothSandstoneBlock = (Block) blockObject[0]; int biomeSmoothSandstoneMeta = (Integer) blockObject[1]; 
        	blockObject = ModObjects.chooseModSmoothSandstoneSlab(false, false); blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome); Block biomeSmoothSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSmoothSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 9, this.materialType, this.biome); Block biomeSandstoneSlabTopBlock = (Block)blockObject[0]; int biomeSandstoneSlabTopMeta = (Integer)blockObject[1];
        	blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.stone_slab, 1, this.materialType, this.biome); Block biomeSandstoneSlabBottomBlock = (Block)blockObject[0]; int biomeSandstoneSlabBottomMeta = (Integer)blockObject[1];
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlock(Blocks.log.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	if (ModObjects.chooseModSmoothSandstoneStairs(false)==null) {blockObject = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone_stairs, 0, this.materialType, this.biome);} else {blockObject = StructureVillageVN.getBiomeSpecificBlock(ModObjects.chooseModSmoothSandstoneStairs(false), 0, this.materialType, this.biome);} Block biomeSmoothSandstoneStairsBlock = (Block)blockObject[0];
        	blockObject = ModObjects.chooseModSandstoneWall(this.materialType==MaterialType.MESA); if (blockObject==null) {blockObject = new Object[]{Blocks.cobblestone_wall, 0};} else {blockObject = StructureVillageVN.getBiomeSpecificBlock((Block)blockObject[0], (Integer)blockObject[1], this.materialType, this.biome);} Block biomeSandstoneWallIntoCobblestoneWallBlock = (Block)blockObject[0]; int biomeSandstoneWallIntoCobblestoneWallMeta = (Integer)blockObject[1];
        	
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
                    	// Top with grass
                    	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
        	
        	// Cut Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Floor
        		{0,0,1, 4,0,2}, 
        		{0,0,6, 4,0,6}, 
        		{5,0,0, 9,0,5}, 
        		// Frame
        		{0,1,1, 1,1,1}, 
        		{0,1,2, 0,1,4}, 
        		{0,1,5, 0,5,6}, 
        		{5,1,3, 5,4,3}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCutSandstoneBlock, biomeCutSandstoneMeta, biomeCutSandstoneBlock, biomeCutSandstoneMeta, false);	
            }
            
        	
        	// Explicit Cut Sandstone
        	for(int[] uuvvww : new int[][]{
        		// basin
        		{1,0,3, 4,0,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.sandstone, 2, Blocks.sandstone, 2, false);	
            }
            
        	
        	// Vertical Log
        	for(int[] uuvvww : new int[][]{
        		{5,1,0, 5,5,0}, 
        		{9,1,0, 9,5,0}, 
        		{5,1,6, 5,5,6}, 
        		{9,1,5, 9,5,5}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertBlock, biomeLogVertMeta, biomeLogVertBlock, biomeLogVertMeta, false);	
            }
            
        	
        	// Smooth Sandstone
        	for(int[] uuvvww : new int[][]{
        		// Front wall
        		{6,1,0, 8,4,0}, 
        		// Left wall
        		{5,4,1, 5,4,2}, {5,1,4, 5,4,4},
        		// Back wall
        		{6,1,5, 8,4,5}, 
        		// Right wall
        		{9,1,1, 9,4,4}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, biomeSmoothSandstoneBlock, biomeSmoothSandstoneMeta, false);	
            }
        	
        	
        	// Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{6,3,3, 1}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.torch, StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Smooth Sandstone Stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entryway
        		{2,0,0, 4,0,0, 3}, 
        		// Archway
        		{5,3,1, 5,3,1, 6}, {5,3,2, 5,3,2, 7}, 
        		// Interior
        		{8,1,1, 8,1,1, 0}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, biomeSmoothSandstoneStairsBlock, this.getMetadataWithOffset(Blocks.sandstone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4, false);	
            }
            
        	
        	// Smooth Sandstone Slab (bottom)
        	for(int[] uuvvww : new int[][]{
        		// Left roof
        		{1,5,1, 1,5,6}, {4,5,1, 4,5,6}, 
        		{2,5,1, 3,5,2}, {2,5,5, 3,5,6}, 
        		// Right roof
        		{5,5,1, 5,5,5}, {6,5,0, 8,5,5}, {9,5,1, 9,5,4}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, biomeSmoothSandstoneSlabBottomBlock, biomeSmoothSandstoneSlabBottomMeta, false);	
            }
            
        	
        	// Cobblestone basin
        	for(int[] uuvvww : new int[][]{
          		// Basin
        		{1,1,2, 1,1,4}, 
          		{2,1,3, 4,1,3}, 
          		{4,1,4, 4,1,4}, 
          		// Back wall
          		{1,1,5, 4,4,6}, 
        		})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.cobblestone, 0, Blocks.cobblestone, 0, false);	
            }
        	
        	
        	// Lava
            this.fillWithMetadataBlocks(world, structureBB, 2,1,4, 3,1,4, Blocks.lava, 0, Blocks.lava, 0, false);
        	
            
            // Iron gate
            this.fillWithMetadataBlocks(world, structureBB, 1,2,2, 1,4,4, Blocks.iron_bars, 0, Blocks.iron_bars, 0, false);
        	
            
        	// Sandstone Wall
        	for(int[] uuvvww : new int[][]{
        		{1,2,1, 1,4,1}, 
            	})
            {
            	this.fillWithMetadataBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandstoneWallIntoCobblestoneWallBlock, biomeSandstoneWallIntoCobblestoneWallMeta, biomeSandstoneWallIntoCobblestoneWallBlock, biomeSandstoneWallIntoCobblestoneWallMeta, false);	
            }
            
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,4, 2}, 
            	{4,3,4, 2}, 
            	})
            {
                this.placeBlockAtCurrentPosition(world, Blocks.furnace, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockMetadataWithNotify(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]), StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode), 2);
            }
        	
        	
            // Windows - Sandstone slab bottom, Sandstone slab top
        	for (int[] uvw : new int[][]{
        		{7,2,0}, {7,2,5}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.placeBlockAtCurrentPosition(world,
        					new Block[]{biomeSandstoneSlabBottomBlock, biomeSandstoneSlabTopBlock}[i],
        					new int[]{biomeSandstoneSlabBottomMeta, biomeSandstoneSlabTopMeta}[i],
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
        	
        	
            // Potted cactus
        	for (int[] uvwm : new int[][]{ // 9:cactus,  10:dead bush
        		{0,2,2, 9}, 
           		})
        	{
            	this.placeBlockAtCurrentPosition(world, Blocks.flower_pot, uvwm[3], uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
            
            
            // Grindstone
        	for (int[] uvwo : new int[][]{
        		{7,1,4, 2}, // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		blockObject = ModObjects.chooseModGrindstone(uvwo[3], this.coordBaseMode); Block biomeGrindstoneBlock = (Block)blockObject[0]; int biomeGrindstoneMeta = (Integer)blockObject[1];
            	
        		this.placeBlockAtCurrentPosition(world, biomeGrindstoneBlock, biomeGrindstoneMeta, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 8;
        	int chestV = 1;
        	int chestW = 2;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	this.placeBlockAtCurrentPosition(world, Blocks.chest, 0, chestU, chestV, chestW, structureBB);
            world.setBlockMetadataWithNotify(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW), StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode), 2);
        	TileEntity te = world.getTileEntity(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_weaponsmith");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2,GROUND_LEVEL,-1}, 
        		{3,GROUND_LEVEL,-1}, 
        		{4,GROUND_LEVEL,-1},
           		})
        	{
            	int pathU = uvw[0];
                int pathV = uvw[1];
                int pathW = uvw[2];
                
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-1, pathW, structureBB);
            	/*// Place dirt if the block to be set as path is empty
            	if (world.isAirBlock(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW)))
            	{
                	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);
            	}*/
            	
            	// Only add path blocks in the front. Otherwise add "regular" grass
            	if (pathW==-1) {StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW));}
            	else {this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, pathU, pathV-1, pathW, structureBB);}
        	}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 5;
            	int v = 1;
            	int w = 3;
            	
            	while (
            			(u==7 && w==4) // Inside the grindstone
            			|| (u==8 && w==2) // Inside the chest
            			|| (u==8 && w==1) // Inside the stairs
            			|| (u<=5 && w>=3) // Inside the lava basin
            			)
            	{
                	u = 2 + random.nextInt(7);
                	w = 1 + random.nextInt(4);
                }
            	
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 2, 0);
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // ------------------ //
    // --- Road Decor --- //
    // ------------------ //
    
    
    // --- Road Decor --- //
    
    public static class DesertStreetDecor1 extends StructureVillagePieces.Village
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
    	
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = 3;
    	public static final int STRUCTURE_DEPTH = 3;
    	public static final int STRUCTURE_HEIGHT = 4;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public DesertStreetDecor1() {}

        public DesertStreetDecor1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static DesertStreetDecor1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new DesertStreetDecor1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	/*
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < STRUCTURE_DEPTH; w++) {for (int u=0; u < STRUCTURE_WIDTH; u++) {
        		
        			if (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            		{
            			// If dirt, add dirt foundation and then cap with grass:
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            */
            
            // Decor
        			
            int[][] decorUVW = new int[][]{
            	{1, 0, 1},
            };  
            
            for (int j=0; j<decorUVW.length; j++)
            {
            	// Get coordinates
            	int[] uvw = decorUVW[j];
            	
            	// Select a random distance from the path
            	// Set random seed
            	Random randomFromXYZ = new Random();
            	randomFromXYZ.setSeed(
    					world.getSeed() +
    					FunctionsVN.getUniqueLongForXYZ(
    							this.getXWithOffset(uvw[0], uvw[2]),
    							this.getYWithOffset(uvw[1]),
    							this.getZWithOffset(uvw[0], uvw[2]))
    							);
            	int decorDepth = (Integer) FunctionsVN.weightedRandom(
            			new    int[]{0,1,2}, // Values
            			new double[]{5,6,1}, // Weights
            			randomFromXYZ);
            	
            	uvw[2] = decorDepth;
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uvw[0], uvw[2]), this.getZWithOffset(uvw[0], uvw[2]))-this.boundingBox.minY;
            	
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], decorHeightY+1, uvw[2], structureBB);
            	
            	// Get ground level
            	if (this.decorHeightY.size()<(j+1))
            	{
            		// There are fewer stored ground levels than this decor number, so this is being generated for the first time.
            		// Add new ground level
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getRandomDesertDecorBlueprint(this.materialType, this.biome, this.coordBaseMode, randomFromXYZ);
            	
            	for (BlueprintData b : decorBlueprint)
            	{
            		// Place block indicated by blueprint
            		this.placeBlockAtCurrentPosition(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos(), uvw[2]+b.getWPos(), structureBB);
            		
            		// Fill below if flagged
            		if ((b.getfillFlag()&1)!=0)
            		{
            			this.replaceAirAndLiquidDownwards(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos()-1, uvw[2]+b.getWPos(), structureBB);
            		}
            		
            		// Clear above if flagged
            		if ((b.getfillFlag()&2)!=0)
            		{
            			this.clearCurrentPositionBlocksUpwards(world, uvw[0]+b.getUPos(), decorHeightY+b.getVPos()+1, uvw[2]+b.getWPos(), structureBB);
            		}            		
            	}
            	
            	// Grass base
            	if (!world.getBlock(
            			this.getXWithOffset(uvw[0], uvw[2]),
            			this.getYWithOffset(decorHeightY-1),
            			this.getZWithOffset(uvw[0], uvw[2])
            			).isNormalCube()
            			) {
            		this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	}
            }
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Road Terracotta Accent 1 --- //
    
    public static class DesertStreetSubstitute1 extends StructureVillagePieces.Village
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
        
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = 7;
    	public static final int STRUCTURE_DEPTH = 2;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	// Values related to structures straddling streets
        
    	private int averageGroundLevel = -1;
    	
        public DesertStreetSubstitute1() {}

        public DesertStreetSubstitute1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertStreetSubstitute1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            // Structure on the other side of the street
            StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, -Reference.STREET_WIDTH-1, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox)
            		&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
            		&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
            		? new DesertStreetSubstitute1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	/*
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < STRUCTURE_DEPTH; w++) {for (int u=0; u < STRUCTURE_WIDTH; u++) {
        		
        			if (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            		{
            			// If dirt, add dirt foundation and then cap with grass:
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            */
        	// Setting terracotta into ground level
        	for (int[] uw : new int[][]{
        		{0, 0}, {1, 0}, {1, 1},
        		{5, 1}, {5, 0}, {6, 0}, 
        		{0, -(Reference.STREET_WIDTH+1)}, {1, -(Reference.STREET_WIDTH+1)}, {1, -(Reference.STREET_WIDTH+1)-1},
        		{5, -(Reference.STREET_WIDTH+1)-1}, {5, -(Reference.STREET_WIDTH+1)}, {6, -(Reference.STREET_WIDTH+1)}, 
        	})
        	{
        		// Determine ground level
        		int groundY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uw[0], uw[1]), this.getZWithOffset(uw[0], uw[1]))-1;
        		int groundV = groundY-this.boundingBox.minY;
        		
        		// Make foundation
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, uw[0], groundV-1, uw[1], structureBB);
        		// Set the block
        		this.placeBlockAtCurrentPosition(world, Blocks.hardened_clay, 0, uw[0], groundV, uw[1], structureBB);
        		// Clear upwards
        		this.clearCurrentPositionBlocksUpwards(world, uw[0], groundV+1, uw[1], structureBB);
        	}
            
        	// Setting path into the ground
        	for (int[] uw : new int[][]{
        		{2, 0}, {3, 0}, {4, 0}, 
        		{2, 1}, {3, 1}, {4, 1}, 
        		{2, -(Reference.STREET_WIDTH+1)-0}, {3, -(Reference.STREET_WIDTH+1)-0}, {4, -(Reference.STREET_WIDTH+1)-0}, 
        		{2, -(Reference.STREET_WIDTH+1)-1}, {3, -(Reference.STREET_WIDTH+1)-1}, {4, -(Reference.STREET_WIDTH+1)-1}, 
        	})
        	{
        		// Determine ground level
        		int groundY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uw[0], uw[1]), this.getZWithOffset(uw[0], uw[1]))-1;
        		int groundV = groundY-this.boundingBox.minY;
        		
        		// Make foundation
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, uw[0], groundV-1, uw[1], structureBB);
        		// Set the block
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(uw[0], uw[1]), groundY, this.getZWithOffset(uw[0], uw[1]));
        		// Clear upwards
        		this.clearCurrentPositionBlocksUpwards(world, uw[0], groundV+1, uw[1], structureBB);
        	}
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{3, 0, -2},
            };  
            
            for (int j=0; j<decorUVW.length; j++)
            {
            	// Get coordinates
            	int[] uvw = decorUVW[j];
            	
            	// Select a random distance from the path
            	// Set random seed
            	Random randomFromXYZ = new Random();
            	randomFromXYZ.setSeed(
    					world.getSeed() +
    					FunctionsVN.getUniqueLongForXYZ(
    							this.getXWithOffset(uvw[0], uvw[2]),
    							this.getYWithOffset(uvw[1]),
    							this.getZWithOffset(uvw[0], uvw[2]))
    							);
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uvw[0], uvw[2]), this.getZWithOffset(uvw[0], uvw[2]))-this.boundingBox.minY;
            	
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], decorHeightY+1, uvw[2], structureBB);
            	
            	// Get ground level
            	if (this.decorHeightY.size()<(j+1))
            	{
            		// There are fewer stored ground levels than this decor number, so this is being generated for the first time.
            		// Add new ground level
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getRandomDesertDecorBlueprint(this.materialType, this.biome, this.coordBaseMode, randomFromXYZ);
            	
            	for (BlueprintData b : decorBlueprint)
            	{
            		// Place block indicated by blueprint
            		this.placeBlockAtCurrentPosition(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos(), uvw[2]+b.getWPos(), structureBB);
            		
            		// Fill below if flagged
            		if ((b.getfillFlag()&1)!=0)
            		{
            			this.replaceAirAndLiquidDownwards(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos()-1, uvw[2]+b.getWPos(), structureBB);
            		}
            		
            		// Clear above if flagged
            		if ((b.getfillFlag()&2)!=0)
            		{
            			this.clearCurrentPositionBlocksUpwards(world, uvw[0]+b.getUPos(), decorHeightY+b.getVPos()+1, uvw[2]+b.getWPos(), structureBB);
            		}            		
            	}
            	
            	// Grass base
            	if (!world.getBlock(
            			this.getXWithOffset(uvw[0], uvw[2]),
            			this.getYWithOffset(decorHeightY-1),
            			this.getZWithOffset(uvw[0], uvw[2])
            			).isNormalCube()
            			) {
            		this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	}
            }
            
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Road Terracotta Accent 2 --- //
    
    public static class DesertStreetSubstitute2 extends StructureVillagePieces.Village
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
    	
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = 13;
    	public static final int STRUCTURE_DEPTH = 2;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public DesertStreetSubstitute2() {}

        public DesertStreetSubstitute2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertStreetSubstitute2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            // Structure on the other side of the street
            StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, -Reference.STREET_WIDTH-1, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox)
            		&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
            		&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
            		? new DesertStreetSubstitute2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	/*
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < STRUCTURE_DEPTH; w++) {for (int u=0; u < STRUCTURE_WIDTH; u++) {
        		
        			if (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            		{
            			// If dirt, add dirt foundation and then cap with grass:
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	 */
        	// Setting path into the ground
        	for (int[] uw : new int[][]{
        		{1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {8, 0}, {9, 0}, {10, 0}, {11, 0}, 
        		{2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {7, 1}, {8, 1}, {9, 1}, {10, 1}, 
        		{1, -(Reference.STREET_WIDTH+1)-0}, {2, -(Reference.STREET_WIDTH+1)-0}, {3, -(Reference.STREET_WIDTH+1)-0}, {4, -(Reference.STREET_WIDTH+1)-0}, {5, -(Reference.STREET_WIDTH+1)-0}, {6, -(Reference.STREET_WIDTH+1)-0}, 
        		{7, -(Reference.STREET_WIDTH+1)-0}, {8, -(Reference.STREET_WIDTH+1)-0}, {9, -(Reference.STREET_WIDTH+1)-0}, {10, -(Reference.STREET_WIDTH+1)-0}, {11, -(Reference.STREET_WIDTH+1)-0}, 
        		{2, -(Reference.STREET_WIDTH+1)-1}, {3, -(Reference.STREET_WIDTH+1)-1}, {4, -(Reference.STREET_WIDTH+1)-1}, {5, -(Reference.STREET_WIDTH+1)-1}, {6, -(Reference.STREET_WIDTH+1)-1}, {7, -(Reference.STREET_WIDTH+1)-1}, 
        	})
        	{
        		// Determine ground level
        		int groundY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uw[0], uw[1]), this.getZWithOffset(uw[0], uw[1]))-1;
        		int groundV = groundY-this.boundingBox.minY;
        		
        		// Make foundation
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, uw[0], groundV-1, uw[1], structureBB);
        		// Set the block
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(uw[0], uw[1]), groundY, this.getZWithOffset(uw[0], uw[1]));
        		// Clear upwards
        		this.clearCurrentPositionBlocksUpwards(world, uw[0], groundV+1, uw[1], structureBB);
        	}
            
        	// Setting terracotta into ground level
        	for (int[] uw : new int[][]{
        		{4, -1}, {6, -1}, {8, -1}, 
        		{5, -2}, {7, -2}, 
        		{4, -3}, {6, -3}, {8, -3}, 
        	})
        	{
        		// Determine ground level
        		int groundY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uw[0], uw[1]), this.getZWithOffset(uw[0], uw[1]))-1;
        		int groundV = groundY-this.boundingBox.minY;
        		
        		// Make foundation
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, uw[0], groundV-1, uw[1], structureBB);
        		// Set the block
        		this.placeBlockAtCurrentPosition(world, Blocks.hardened_clay, 0, uw[0], groundV, uw[1], structureBB);
        		// Clear upwards
        		this.clearCurrentPositionBlocksUpwards(world, uw[0], groundV+1, uw[1], structureBB);
        	}
            
        	// Set random seed
        	Random randomFromXYZ = new Random();
        	randomFromXYZ.setSeed(
					world.getSeed() +
					FunctionsVN.getUniqueLongForXYZ(
							this.getXWithOffset(0,0),
							this.getYWithOffset(0),
							this.getZWithOffset(0,0)
							));
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{1+randomFromXYZ.nextInt(3), 0, 0-randomFromXYZ.nextInt(5)}, 
            	{9+randomFromXYZ.nextInt(3), 0, 0-randomFromXYZ.nextInt(3)}, 
            };  
            
            for (int j=0; j<decorUVW.length; j++)
            {
            	// Get coordinates
            	int[] uvw = decorUVW[j];
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uvw[0], uvw[2]), this.getZWithOffset(uvw[0], uvw[2]))-this.boundingBox.minY;
            	
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], decorHeightY+1, uvw[2], structureBB);
            	
            	// Get ground level
            	if (this.decorHeightY.size()<(j+1))
            	{
            		// There are fewer stored ground levels than this decor number, so this is being generated for the first time.
            		// Add new ground level
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getRandomDesertDecorBlueprint(this.materialType, this.biome, this.coordBaseMode, randomFromXYZ);
            	
            	for (BlueprintData b : decorBlueprint)
            	{
            		// Place block indicated by blueprint
            		this.placeBlockAtCurrentPosition(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos(), uvw[2]+b.getWPos(), structureBB);
            		
            		// Fill below if flagged
            		if ((b.getfillFlag()&1)!=0)
            		{
            			this.replaceAirAndLiquidDownwards(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos()-1, uvw[2]+b.getWPos(), structureBB);
            		}
            		
            		// Clear above if flagged
            		if ((b.getfillFlag()&2)!=0)
            		{
            			this.clearCurrentPositionBlocksUpwards(world, uvw[0]+b.getUPos(), decorHeightY+b.getVPos()+1, uvw[2]+b.getWPos(), structureBB);
            		}            		
            	}
            	
            	// Grass base
            	if (!world.getBlock(
            			this.getXWithOffset(uvw[0], uvw[2]),
            			this.getYWithOffset(decorHeightY-1),
            			this.getZWithOffset(uvw[0], uvw[2])
            			).isNormalCube()
            			) {
            		this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	}
            }
			
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Road Decor 2 --- //
    
    public static class DesertStreetSubstitute3 extends StructureVillagePieces.Village
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
    	
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = 9;
    	public static final int STRUCTURE_DEPTH = 2;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	// Values related to structures straddling streets
    	
    	private int averageGroundLevel = -1;
    	
        public DesertStreetSubstitute3() {}

        public DesertStreetSubstitute3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static DesertStreetSubstitute3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            // Structure on the other side of the street
            StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, -Reference.STREET_WIDTH-1, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox)
            		&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
            		&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
            		? new DesertStreetSubstitute3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.coordBaseMode.getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.coordBaseMode.getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.coordBaseMode.getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.coordBaseMode.getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
            Object[] blockObject;
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.dirt.getStateFromMeta(0), this.materialType, this.biome);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.grass.getStateFromMeta(0), this.materialType, this.biome);
        	/*
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            	// top with grass
            	this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            }}
            
            // Make foundation with blanks as empty air and F as foundation spaces
            
        	for (int w=0; w < STRUCTURE_DEPTH; w++) {for (int u=0; u < STRUCTURE_WIDTH; u++) {
        		
        			if (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            		{
            			// If dirt, add dirt foundation and then cap with grass:
            			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-2, w, structureBB);
            			this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
        	*/
        	// Setting path into the ground
        	for (int[] uw : new int[][]{
        		{1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, 
        		{2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, 
        		{1, -(Reference.STREET_WIDTH+1)-0}, {2, -(Reference.STREET_WIDTH+1)-0}, {3, -(Reference.STREET_WIDTH+1)-0}, {4, -(Reference.STREET_WIDTH+1)-0}, {5, -(Reference.STREET_WIDTH+1)-0}, {6, -(Reference.STREET_WIDTH+1)-0}, {7, -(Reference.STREET_WIDTH+1)-0}, 
        		{2, -(Reference.STREET_WIDTH+1)-1}, {3, -(Reference.STREET_WIDTH+1)-1}, {4, -(Reference.STREET_WIDTH+1)-1}, {5, -(Reference.STREET_WIDTH+1)-1}, {6, -(Reference.STREET_WIDTH+1)-1}, 
        	})
        	{
        		// Determine ground level
        		int groundY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uw[0], uw[1]), this.getZWithOffset(uw[0], uw[1]))-1;
        		int groundV = groundY-this.boundingBox.minY;
        		
        		// Make foundation
        		this.replaceAirAndLiquidDownwards(world, biomeDirtState, uw[0], groundV-1, uw[1], structureBB);
        		// Set the block
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, 0, this.getXWithOffset(uw[0], uw[1]), groundY, this.getZWithOffset(uw[0], uw[1]));
        		// Clear upwards
        		this.clearCurrentPositionBlocksUpwards(world, uw[0], groundV+1, uw[1], structureBB);
        	}
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{4, 0, -2}, 
            };  
            
            for (int j=0; j<decorUVW.length; j++)
            {
            	// Get coordinates
            	int[] uvw = decorUVW[j];
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uvw[0], uvw[2]), this.getZWithOffset(uvw[0], uvw[2]))-this.boundingBox.minY;
            	
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], decorHeightY+1, uvw[2], structureBB);
            	
            	// Get ground level
            	if (this.decorHeightY.size()<(j+1))
            	{
            		// There are fewer stored ground levels than this decor number, so this is being generated for the first time.
            		// Add new ground level
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = getRandomDesertDecorBlueprint(this.materialType, this.biome, this.coordBaseMode, random);
            	
            	for (BlueprintData b : decorBlueprint)
            	{
            		// Place block indicated by blueprint
            		this.placeBlockAtCurrentPosition(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos(), uvw[2]+b.getWPos(), structureBB);
            		
            		// Fill below if flagged
            		if ((b.getfillFlag()&1)!=0)
            		{
            			this.replaceAirAndLiquidDownwards(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos()-1, uvw[2]+b.getWPos(), structureBB);
            		}
            		
            		// Clear above if flagged
            		if ((b.getfillFlag()&2)!=0)
            		{
            			this.clearCurrentPositionBlocksUpwards(world, uvw[0]+b.getUPos(), decorHeightY+b.getVPos()+1, uvw[2]+b.getWPos(), structureBB);
            		}            		
            	}
            	
            	// Grass base
            	if (!world.getBlock(
            			this.getXWithOffset(uvw[0], uvw[2]),
            			this.getYWithOffset(decorHeightY-1),
            			this.getZWithOffset(uvw[0], uvw[2])
            			).isNormalCube()
            			) {
            		this.placeBlockAtCurrentPosition(world, biomeGrassBlock, biomeGrassMeta, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	}
            }
			
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }    
    
    
    // ------------------- //
    // --- Biome Decor --- //
    // ------------------- //
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
	public static ArrayList<BlueprintData> getRandomDesertDecorBlueprint(MaterialType materialType, BiomeGenBase biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		int decorCount = 1;
		return getDesertDecorBlueprint(random.nextInt(decorCount), materialType, biome, coordBaseMode, random);//, townColor);
	}
	public static ArrayList<BlueprintData> getDesertDecorBlueprint(int decorType, MaterialType materialType, BiomeGenBase biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeCutSandstoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.sandstone.getStateFromMeta(2), materialType, biome);
		
		
        switch (decorType)
        {
    	case 0: // Torch on stained terracotta and cut sandstone
    		
    		BlueprintData.addFillWithBlocks(blueprint, 0, 0, 0, 0, 1, 0, biomeCutSandstoneState);
    		//BlueprintData.addPlaceBlock(blueprint, 0, 2, 0, GeneralConfig.decorateVillageCenter ? Blocks.stained_hardened_clay.getStateFromMeta(townColor) : Blocks.hardened_clay.getDefaultState());
    		BlueprintData.addPlaceBlock(blueprint, 0, 2, 0, Blocks.hardened_clay.getDefaultState());
    		BlueprintData.addPlaceBlock(blueprint, 0, 3, 0, Blocks.torch.getStateFromMeta(0));
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
	
}
