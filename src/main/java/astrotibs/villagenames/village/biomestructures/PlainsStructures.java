package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.config.village.VillageGeneratorConfigHandler;
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
    	
    	public PlainsFountain01(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);

    		int width = 8;
    		int depth = 8;
    		int height = 3;
    		
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
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
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void
                
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
        			this.getXWithOffset(4, 4),
        			this.getYWithOffset(2),
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

        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
        	// Clear out area
        	this.fillWithAir(world, structureBB, 2, 1, 2, 6, 4, 6);
        	
            // Basin bottom
        	this.fillWithBlocks(world, structureBB, 2, -1, 2, 6, 0, 6, biomeCobblestoneState, biomeCobblestoneState, false);

            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2, 1, 2, -1},
            	{2, 1, 6, -1},
            	{6, 1, 2, -1},
            	{6, 1, 6, -1},
            	}) 
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            if (GeneralConfig.useVillageColors)
            {
            	IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 2, 1, 3, 6, 1, 5, concreteBlockstate, concreteBlockstate, false);
                this.fillWithBlocks(world, structureBB, 3, 1, 2, 5, 1, 6, concreteBlockstate, concreteBlockstate, false);
                
                // Under-torch GT
            	BlockPos uvw = new BlockPos(2, 0, 2); // Starting position of the block cluster. Use lowest X, Z.
            	
            	if (GeneralConfig.addConcrete)
            	{
                	int metaBase = ((int)world.getSeed()%4+this.getCoordBaseMode().getHorizontalIndex())%4; // Procedural based on world seed and base mode
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
                    this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), 2, 0, 2, structureBB);
                    this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), 2, 0, 6, structureBB);
                    this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), 6, 0, 2, structureBB);
                    this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), 6, 0, 6, structureBB);
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
            if (GeneralConfig.useVillageColors)
            {
            	IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor2);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, concreteBlockstate, concreteBlockstate, false);
            }
            else
            {
            	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), 4, 3, 4, structureBB);
            
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
                        	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j), true);
                        	this.clearCurrentPositionBlocksUpwards(world, i, k+2-this.boundingBox.minY, j, structureBB);
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
                    	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j), true);
                   	}
                    
                    k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(j, i), 0, this.getZWithOffset(j, i))).down().getY();
                    if (k > -1)
                    {
                    	this.clearCurrentPositionBlocksUpwards(world, j, k+2-this.boundingBox.minY, i, structureBB);
                    	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(j, i), k, this.getZWithOffset(j, i), true);
                   	}
            	}
            }
            
            
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 4;
    			int signYBB = 2;
    			int signZBB = 2;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
    			int bannerXBB = 8;
    			int bannerZBB = 6;
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
                
                // Place a cobblestone foundation
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.getCoordBaseMode().getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}
        	
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{6, 1, 1, -1, 0},
	        			{1, 1, 2, -1, 0},
	        			{1, 1, 7, -1, 0},
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
            }
            
            return true;
        }
    }
    
    
    
    // --- Well --- //
    
    public static class PlainsMeetingPoint1 extends StartVN
    {
	    int wellDepthDecrease=7;
	    
		public PlainsMeetingPoint1() {}
		
		public PlainsMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);

    		int width = 9;
    		int depth = 9;
    		
		    // Establish orientation
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            switch (this.getCoordBaseMode())
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
        	// Northward
        	StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()<=1? 3 : 4), this.boundingBox.maxY - 5, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
        	// Eastward
        	StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()<=1? 3 : 4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()<=1? 4 : 3), this.boundingBox.maxY - 5, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()<=1? 4 : 3), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneSlabState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STONE_SLAB.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
        	if (this.averageGroundLvl < 0)
            {
                //this.averageGroundLvl = StructureVillagePiecesVN.getMedianGroundLevel(world, structureBB, true);//this.getAverageGroundLevel(world, structureBoundingBox);
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX+1, this.boundingBox.minZ+1,
        						this.boundingBox.maxX-1, this.boundingBox.maxZ-1), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct a well in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + (5-1) - wellDepthDecrease, 0);
            }
        	
        	
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
        			this.getXWithOffset(4, 4),
        			this.getYWithOffset(12),
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

        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
            // The well gets filled completely with water first
            //this.fillWithBlocks(world, structureBoundingBox, 3, 0+wellDepthDecrease, 3, 6, 12, 6, this.biomeCobblestoneBlock, Blocks.flowing_water, false);
            this.fillWithBlocks(world, structureBB, 3, 0+wellDepthDecrease, 3, 6, 12, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 4, 1+wellDepthDecrease, 4, 5, 12, 5, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false); // Water
            
            // Well rim
            if (GeneralConfig.wellSlabs)
            {
            	this.fillWithBlocks(world, structureBB, 4, 12, 3, 5, 12, 3, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 4, 12, 6, 5, 12, 6, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 3, 12, 4, 3, 12, 5, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 6, 12, 4, 6, 12, 5, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            }
            
            // I believe this replaces the top water level with air
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 4, 12, 4, structureBB);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 5, 12, 4, structureBB);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 4, 12, 5, structureBB);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 5, 12, 5, structureBB);
            
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
            
            if (GeneralConfig.useVillageColors)
            {
            	BlockPos uvw = new BlockPos(4, 15, 4); // Starting position of the block cluster. Use lowest X, Z.
            	
            	if (GeneralConfig.addConcrete)
            	{
            		int metaBase = ((int)world.getSeed()%4+this.getCoordBaseMode().getHorizontalIndex())%4; // Procedural based on world seed and base mode
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
            		this.fillWithBlocks(world, structureBB, 4, 15, 4, 5, 15, 5, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), false);
            	}
            }
            
            // Line the well with cobblestone and ensure the spaces above are clear
            for (int i = 2; i <= 7; ++i)
            {
                for (int j = 2; j <= 7; ++j)
                {
                    if (j == 2 || j == 7 || i == 2 || i == 7)
                    {
                    	if (GeneralConfig.useVillageColors)
                    	{
                    		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);
                        	
                        	// Basin rim
                        	if (GeneralConfig.addConcrete)
                        	{
                        		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
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
            	this.setBlockState(world, ModObjects.chooseModDoor(2), StructureVillageVN.getMetadataWithOffset(Blocks.wooden_door, 0) + 8*i, 7, 12+i, 4, structureBB);
            	this.setBlockState(world, ModObjects.chooseModDoor(3), StructureVillageVN.getMetadataWithOffset(Blocks.wooden_door, 1) + 8*i, 4, 12+i, 2, structureBB);
            	this.setBlockState(world, ModObjects.chooseModDoor(4), StructureVillageVN.getMetadataWithOffset(Blocks.wooden_door, 2) + 8*i, 2, 12+i, 4, structureBB);
            	this.setBlockState(world, ModObjects.chooseModDoor(5), StructureVillageVN.getMetadataWithOffset(Blocks.wooden_door, 3) + 8*i, 4, 12+i, 7, structureBB);
            }
            */
            
            // Over-lid torches
            for (int[] uvwo : new int[][]{
            	{3, 16, 3, -1},
            	{3, 16, 6, -1},
            	{6, 16, 3, -1},
            	{6, 16, 6, -1},
            })
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
                            StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(i, j), k, this.getZWithOffset(i, j), true);
                        	this.clearCurrentPositionBlocksUpwards(world, i, k+2-this.boundingBox.minY, j, structureBB);
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
                	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(0, i+1), k, this.getZWithOffset(0, i+1), true);
               	}
                
                k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(9, i), 0, this.getZWithOffset(9, i))).down().getY();
                if (k > -1)
                {
                	this.clearCurrentPositionBlocksUpwards(world, 9, k+2-this.boundingBox.minY, i, structureBB);
                	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(9, i), k, this.getZWithOffset(9, i), true);
               	}

                k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i, 0), 0, this.getZWithOffset(i, 0))).down().getY();
                if (k > -1)
                {
                	this.clearCurrentPositionBlocksUpwards(world, i, k+2-this.boundingBox.minY, 0, structureBB);
                	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(i, 0), k, this.getZWithOffset(i, 0), true);
               	}
                
                k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i+1, 9), 0, this.getZWithOffset(i+1, 9))).down().getY();
                if (k > -1)
                {
                	this.clearCurrentPositionBlocksUpwards(world, i+1, k+2-this.boundingBox.minY, 9, structureBB);
                	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(i+1, 9), k, this.getZWithOffset(i+1, 9), true);
               	}
            }
            
            
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 6;
    			int signYBB = 12;
    			int signZBB = 7;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerXBB = 8;
    			int bannerZBB = 6;
    			int bannerYBB = 11;
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
                
                // Place a cobblestone foundation
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}
    		
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{1, 11, 8, -1, 0},
	        			{8, 11, 8, -1, 0},
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
            }
            
            return true;
        }
    }
    
    

    // --- Market --- //
    
    public static class PlainsMeetingPoint2 extends StartVN
    {
	    public PlainsMeetingPoint2() {}
		
		public PlainsMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
		    
    		int width = 7;
    		int depth = 14;
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 1 : this.getCoordBaseMode().getHorizontalIndex()==1 ? 10 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 1 : 2), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 6 : this.getCoordBaseMode().getHorizontalIndex()==1 ? 1 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 6 : 1), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 1 : this.getCoordBaseMode().getHorizontalIndex()==1 ? 6 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 1 : 6), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 2 : this.getCoordBaseMode().getHorizontalIndex()==1 ? 1 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 10 : 1), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlock(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodenSlabState = StructureVillageVN.getBiomeSpecificBlock(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.getXWithOffset(4, 4),
        			this.getYWithOffset(12),
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

        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
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
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(offset_xy[0], offset_xy[1]), this.getYWithOffset(0), this.getZWithOffset(offset_xy[0], offset_xy[1]), true);
        	}
        	
        	// Unkempt grass
        	this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), 0, 1, 8, structureBB);
        	this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), 1, 1, 7, structureBB);
        	this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), 1, 1, 12, structureBB);
        	this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), 4, 1, 10, structureBB);
        	this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), 4, 1, 11, structureBB);
        	
        	// Stalls
        	this.fillWithBlocks(world, structureBB, 4, 1, 1, 7, 1, 1, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 4, 0, 1, 7, 0, 1, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 0, 4, 3, 0, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 2, 4, 3, 2, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 2, 7, 3, 2, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 0, 7, 3, 0, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 4, 0, 7, 4, 2, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 5, 4, 0, 6, 4, 2, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), false);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 5, 4, 0, structureBB);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 6, 4, 1, structureBB);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 5, 4, 2, structureBB);
            
        	// Torches
            for (int[] uvwo : new int[][]{
            	{4, 2, 1, -1},
            	{7, 2, 1, -1},
            })
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	this.fillWithBlocks(world, structureBB, 2, 1, 5, 2, 1, 8, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 2, 0, 5, 2, 0, 8, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 1, 1, 5, 1, 3, 5, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 1, 1, 8, 1, 3, 8, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 8, 3, 3, 8, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 5, 3, 3, 5, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 5, 3, 4, 8, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 6, 3, 4, 7, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), false);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 1, 4, 7, structureBB);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 2, 4, 6, structureBB);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 3, 4, 7, structureBB);

            // Torches
            for (int[] uvwo : new int[][]{
            	{2, 2, 5, -1},
            	{2, 2, 8, -1},
            })
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	this.fillWithBlocks(world, structureBB, 4, 1, 13, 7, 1, 13, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 4, 0, 13, 7, 0, 13, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 12, 4, 3, 12, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 14, 4, 3, 14, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 14, 7, 3, 14, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 12, 7, 3, 12, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 4, 12, 7, 4, 14, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 5, 4, 12, 6, 4, 14, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), false);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 5, 4, 12, structureBB);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 6, 4, 13, structureBB);
        	this.setBlockState(world, Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 5, 4, 14, structureBB);
        	
            // Torches
            for (int[] uvwo : new int[][]{
            	{4, 2, 13, -1},
            	{7, 2, 13, -1},
            })
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	        	        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 2;
    			int signYBB = 2;
    			int signZBB = 10;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
        		this.setBlockState(world, biomePlankState, signXBB, signYBB-1, signZBB, structureBB);
        		this.setBlockState(world, biomeDirtState, signXBB, signYBB-2, signZBB, structureBB);
            	
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerXBB = 6;
    			int bannerZBB = 4;
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
                
                // Place a grass foundation
                this.setBlockState(world, biomeGrassState, bannerXBB, bannerYBB-1, bannerZBB, structureBB);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}
    		
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{6, 1, 6, -1, 0},
	        			{5, 1, 8, -1, 0},
	        			{5, 1, 10, -1, 0},
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
            }
            
            return true;
        }
    }
    
    
	// --- Tree --- //
	
    public static class PlainsMeetingPoint3 extends StartVN
    {
    	public PlainsMeetingPoint3() {}
    	
    	public PlainsMeetingPoint3(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
    		int width = 10;
    		int depth = 10;
    		int height = 8;
    		
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			if (this.getCoordBaseMode().getHorizontalIndex()!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			// Eastward
			if (this.getCoordBaseMode().getHorizontalIndex()!=1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.getCoordBaseMode().getHorizontalIndex()!=2) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.getCoordBaseMode().getHorizontalIndex()!=3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.WEST, this.getComponentType());}
		}
    	
		/*
		 * Construct the structure
		 */
    	@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlock(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlock(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlock(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlock(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneStoneStairsState = StructureVillageVN.getBiomeSpecificBlock(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
        	if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ), // Set the bounding box version as this bounding box but with Y going from 0 to 512
        				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY -1, 0);
            }
            
        	// Generate or otherwise obtain village name and banner and colors
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
        			this.getXWithOffset(5, 5),
        			this.getYWithOffset(2),
        			this.getZWithOffset(5, 5));
        	
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

        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
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
        		StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(offset_xy[0], offset_xy[1]), this.getYWithOffset(0), this.getZWithOffset(offset_xy[0], offset_xy[1]), true);
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
        		this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), offset_xy[0], 1, offset_xy[1], structureBB);
        	}
        	this.setBlockState(world, Blocks.YELLOW_FLOWER.getDefaultState(), 3, 1, 1, structureBB);
        	
        	
        	// Tree
        	for (int uvwm[] : new int[][]{
        		{4, 1, 4, 3}, 
        		{5, 1, 4, 3}, // Toward player
        		{6, 1, 4, 1}, 
        		{6, 1, 5, 1}, // Right side, right-facing
        		{6, 1, 6, 2}, 
        		{5, 1, 6, 2}, // Away from player
        		{4, 1, 6, 0}, 
        		{4, 1, 5, 0}, // Left side, left-facing
        	})
        	{
        		this.setBlockState(world, biomeCobblestoneStoneStairsState.getBlock().getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB);
        	}
        	
        	
        	// Dirt block
        	world.setBlockState(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(0), this.getZWithOffset(5, 5)), Blocks.DIRT.getDefaultState(), 2);
        	
        	// Leaves placed into world
        	for (int u=3; u<=7; u++) {for (int v=5; v<=6; v++) {for (int w=4; w<=6; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.LEAVES.getStateFromMeta(0), 2);}}}
        	for (int u=4; u<=6; u++) {for (int v=5; v<=6; v++) {for (int w=3; w<=7; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.LEAVES.getStateFromMeta(0), 2);}}}
        	for (int u=4; u<=6; u++) {for (int v=7; v<=8; v++) {for (int w=4; w<=6; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.LEAVES.getStateFromMeta(0), 2);}}}

        	// Logs need to be set in world so as not to be replaced with sandstone
        	for (int v=1; v<=7; v++) {world.setBlockState(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(v), this.getZWithOffset(5, 5)), Blocks.LOG.getStateFromMeta(0), 2);}
        	
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
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), Blocks.AIR.getDefaultState(), 2);
            }
            
            for (int[] uvwo : new int[][]{
            	{5, 3, 4, 2},
            	{4, 3, 5, 3},
            	{5, 3, 6, 0},
            	{6, 3, 5, 1},
            })
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
                    	        	
            // Posts
        	this.fillWithBlocks(world, structureBB, 4, 1, 1, 4, 4, 1, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 1, 6, 4, 1, biomeFenceState, biomeFenceState, false);
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
            	}
            	
        		this.setBlockState(world, concreteBlockstate, 5, 4, 1, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomeCobblestoneState, 5, 4, 1, structureBB);
        	}
        	
        	this.fillWithBlocks(world, structureBB, 4, 1, 9, 4, 4, 9, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 9, 6, 4, 9, biomeFenceState, biomeFenceState, false);
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor2);
            	}
            	
        		this.setBlockState(world, concreteBlockstate, 5, 4, 9, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomeCobblestoneState, 5, 4, 9, structureBB);
        	}
        	
            // Signs
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 5;
    			int signYBB = 4;
    			int signZBB = 0;
    			int signZBB2 = 10;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signX2 = this.getXWithOffset(signXBB, signZBB2);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
                int signZ2 = this.getZWithOffset(signXBB, signZBB2);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(2, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
    			world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		
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
    			int bannerZBB = 8;
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
                
                // Place a cobblestone foundation
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Clear space upward
                this.clearCurrentPositionBlocksUpwards(world, bannerXBB, bannerYBB, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}
    		
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{8, 1, 6, -1, 0},
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
            }
            
            return true;
        }
    }
    
    
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
    public static ArrayList<BlueprintData> getRandomPlainsDecorBlueprint(MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		int decorCount = 1;
		return getPlainsDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);//, townColor);
	}
	public static ArrayList<BlueprintData> getPlainsDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlock(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlock(Blocks.LOG.getStateFromMeta(0), materialType, biome, disallowModSubs);
    	
    	// For stripped wood specifically
    	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogState; 

    	// Try to see if stripped wood exists
    	if (biomeLogState.getBlock() == Blocks.LOG)
    	{
    		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWood(biomeLogState.getBlock().getMetaFromState(biomeLogState), 0);
    	}
    	else if (biomeLogState.getBlock() == Blocks.LOG2)
    	{
    		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWood(biomeLogState.getBlock().getMetaFromState(biomeLogState)+4, 0);
    	}
    	// If it doesn't exist, try stripped logs
    	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
    	{
        	if (biomeLogState.getBlock() == Blocks.LOG)
        	{
        		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLog(biomeLogState.getBlock().getMetaFromState(biomeLogState), 0);
        	}
        	else if (biomeLogState.getBlock() == Blocks.LOG2)
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
    			{-1,0,3},
    			{1,0,1},
    			{0,-1,2},
    			{0,1,0}
    			}) {
    			BlueprintData.addPlaceBlock(blueprint, lamp_uwm[0], 3, lamp_uwm[1], Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(lamp_uwm[2], coordBaseMode.getHorizontalIndex())));
    		}
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
