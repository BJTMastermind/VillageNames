package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
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

public class SavannaStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Savanna Market --- //
    
    public static class SavannaMeetingPoint1 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"FFFFPPFPPFFPPF",
            	"FFFPPPPFPFPPFP",
            	"FFPFPFPPPFFFFF",
            	"FFFPPPFFPFPPFP",
            	"FFPFPPPPPPPPPF",
            	"FFFPPPFFFPFPPP",
            	"FFFPPFPFPPPFPP",
            	"FFFFFPPFPPPPPP",
            	"FPFPPPFPFFPPFP",
            	"FPPPPPPFPFFFPP",
            	"FPFPFPFPFFFFFF",
            	"FFFPFPFPFPPPPP",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
	    public SavannaMeetingPoint1() {}
		
		public SavannaMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
		    // Establish orientation and bounding box
    		this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            switch (this.getCoordBaseMode())
            {
	            case NORTH: // North
	            case SOUTH: // South
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + STRUCTURE_WIDTH-1, 64 + STRUCTURE_HEIGHT-1, posZ + STRUCTURE_DEPTH-1);
                    break;
                default: // 1: East; 3: West
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + STRUCTURE_DEPTH-1, 64 + STRUCTURE_HEIGHT-1, posZ + STRUCTURE_WIDTH-1);
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
        	if (this.averageGroundLvl < 0)
            {
            	if (this.averageGroundLvl < 0)
                {
            		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX, this.boundingBox.minZ,
            						this.boundingBox.maxX, this.boundingBox.maxZ),
            				true, (byte)13, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodenSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodenStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);

        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            }}
        	
        	// Follow the blueprint to set up the starting foundation
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
        		String unitLetter = foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase();
    			int posX = this.getXWithOffset(u, w);
    			int posY = this.getYWithOffset(GROUND_LEVEL-1);
    			int posZ = this.getZWithOffset(u, w);
    					
        		if (unitLetter.equals("F"))
        		{
        			// If marked with F: fill with dirt foundation
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeDirtState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
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

        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiome(new BlockPos(posX, 64, posZ));
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
        		this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), grass_uw[0], 1, grass_uw[1], structureBB);
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
        	/*
        	// Fences with torches
        	this.setBlockState(world, biomeFenceState, 2, 1, 10, structureBB);
        	world.setBlockState(new BlockPos(this.getXWithOffset(2, 10), this.getYWithOffset(2), this.getZWithOffset(2, 10)), Blocks.TORCH.getDefaultState(), 2);
        	this.setBlockState(world, biomeFenceState, 8, 1, 0, structureBB);
        	world.setBlockState(new BlockPos(this.getXWithOffset(8, 0), this.getYWithOffset(2), this.getZWithOffset(8, 0)), Blocks.TORCH.getDefaultState(), 2);        	
        	*/
        	
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
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.WALL_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.getCoordBaseMode().getHorizontalIndex(), true)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				NBTTagCompound modifystanding = new NBTTagCompound();
				tilebanner.writeToNBT(modifystanding);
				modifystanding.setInteger("Base", 15 - uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{2,1,10},
            	{8,1,0},
            	{13,1,11},
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
            	ArrayList<BlueprintData> decorBlueprint = getRandomSavannaDecorBlueprint(this.materialType, this.biome, this.getCoordBaseMode(), randomFromXYZ);//, townColor);
            	
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
// Line the well with cobblestone                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"), (namePrefix + " " + nameRoot + " " + nameSuffix).trim());
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner, false);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}

    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
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
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
	        		}
            	}
            }
            
            return true;
        }
    }
    

	// --- Savanna Fountain --- //
    
    public static class SavannaMeetingPoint2 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"   FPPPF   ",
            	" FFFPPPFFF ",
            	" FFPPPPPFF ",
            	"FFPPFFFPPFF",
            	"PPPFFFFFPPP",
            	"PPPFFFFFPPP",
            	"PPPFFFFFPPP",
            	"FFPPFFFPPF ",
            	" FFPPPPP   ",
            	" FFFPPP  F ",
            	"    PPP    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
	    public SavannaMeetingPoint2() {}
		
		public SavannaMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
		    // Establish orientation and bounding box
    		this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            switch (this.getCoordBaseMode())
            {
	            case NORTH: // North
	            case SOUTH: // South
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + STRUCTURE_WIDTH-1, 64 + STRUCTURE_HEIGHT-1, posZ + STRUCTURE_DEPTH-1);
                    break;
                default: // 1: East; 3: West
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + STRUCTURE_DEPTH-1, 64 + STRUCTURE_HEIGHT-1, posZ + STRUCTURE_WIDTH-1);
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
        	if (this.averageGroundLvl < 0)
            {
            	if (this.averageGroundLvl < 0)
                {
            		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX, this.boundingBox.minZ,
            						this.boundingBox.maxX, this.boundingBox.maxZ),
            				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);

        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            }}
        	
        	// Follow the blueprint to set up the starting foundation
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
        		String unitLetter = foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase();
    			int posX = this.getXWithOffset(u, w);
    			int posY = this.getYWithOffset(GROUND_LEVEL-1);
    			int posZ = this.getZWithOffset(u, w);
    					
        		if (unitLetter.equals("F"))
        		{
        			// If marked with F: fill with dirt foundation
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeDirtState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
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

        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiome(new BlockPos(posX, 64, posZ));
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
        	
        	// Top layer 
        	
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
        		this.setBlockState(world, biomeGrassState, grass_uw[0], 0, grass_uw[1], structureBB);
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
        	for (int[] uvwo : new int[][]{
        		{1,2,1, -1},
        		{9,2,9, -1},
        	})
        	{
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
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
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
	        		}
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
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.WALL_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.getCoordBaseMode().getHorizontalIndex(), true)), 2);
				
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
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
        	
    		
            return true;
        }
    }
    
    
	// --- Savanna Double Well --- //
    
    public static class SavannaMeetingPoint3 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"   PPP   ",
            	"F PPPPP  ",
            	"  PFFFP  ",
            	" PPFFFPP ",
            	"PPPFFFPPP",
            	"PPPPPPPPP",
            	"PPPFFFPPP",
            	" PPFFFPP ",
            	"  PFFFPF ",
            	" FPPPPP  ",
            	"   PPP   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
	    public SavannaMeetingPoint3() {}
		
		public SavannaMeetingPoint3(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
		    // Establish orientation and bounding box
    		this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            switch (this.getCoordBaseMode())
            {
	            case NORTH: // North
	            case SOUTH: // South
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + STRUCTURE_WIDTH-1, 64 + STRUCTURE_HEIGHT-1, posZ + STRUCTURE_DEPTH-1);
                    break;
                default: // 1: East; 3: West
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + STRUCTURE_DEPTH-1, 64 + STRUCTURE_HEIGHT-1, posZ + STRUCTURE_WIDTH-1);
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
        	if (this.averageGroundLvl < 0)
            {
            	if (this.averageGroundLvl < 0)
                {
            		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX, this.boundingBox.minZ,
            						this.boundingBox.maxX, this.boundingBox.maxZ),
            				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodenSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeBarkState = ModObjects.chooseModBark(biomeLogState);

        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            }}
        	
        	// Follow the blueprint to set up the starting foundation
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
        		String unitLetter = foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase();
    			int posX = this.getXWithOffset(u, w);
    			int posY = this.getYWithOffset(GROUND_LEVEL-1);
    			int posZ = this.getZWithOffset(u, w);
    					
        		if (unitLetter.equals("F"))
        		{
        			// If marked with F: fill with dirt foundation
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeDirtState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
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

        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiome(new BlockPos(posX, 64, posZ));
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
        	
        	// Set Grass blocks
        	for (int[] uw : new int[][]{
        		{0, 9},
        		{1, 1},
        		{9, 2}, 
        	})
        	{
        		this.setBlockState(world, biomeGrassState, uw[0], 0, uw[1], structureBB);
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
        	
        	// Pavilion
        	this.fillWithBlocks(world, structureBB, 3, 2, 4, 5, 3, 6, biomeFenceState, biomeFenceState, false);
        	this.fillWithAir(world, structureBB, 4, 2, 4, 4, 3, 6);
        	this.fillWithAir(world, structureBB, 3, 2, 5, 5, 3, 5);
        	this.fillWithBlocks(world, structureBB, 3, 4, 4, 5, 4, 6, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.setBlockState(world, biomePlankState, 4, 4, 5, structureBB);
        	// Torch
        	for (int[] uvwo : new int[][]{
        		{3,2,2, -1},
        		{5,2,2, -1},
        		{3,2,8, -1},
        		{5,2,8, -1},
        		{4,5,5, -1},
        	})
        	{
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
// Line the well with cobblestone                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"), (namePrefix + " " + nameRoot + " " + nameSuffix).trim());
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner, false);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
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
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
	        		}
            	}
            }
            
            return true;
        }
    }
    

	// --- Savanna Single Well --- //
    
    public static class SavannaMeetingPoint4 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	" F PPP   ",
            	" PPPPPPP ",
            	" PPFFFPP ",
            	"PPFFFFFPP",
            	"PPFFFFFPP",
            	"PPFFFFFPP",
            	" PPFFFPP ",
            	" PPPPPPP ",
            	"F  PPP   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
	    public SavannaMeetingPoint4() {}
		
		public SavannaMeetingPoint4(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
		    // Establish orientation and bounding box
    		this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            switch (this.getCoordBaseMode())
            {
	            case NORTH: // North
	            case SOUTH: // South
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + STRUCTURE_WIDTH-1, 64 + STRUCTURE_HEIGHT-1, posZ + STRUCTURE_DEPTH-1);
                    break;
                default: // 1: East; 3: West
                    this.boundingBox = new StructureBoundingBox(posX, 64, posZ, posX + STRUCTURE_DEPTH-1, 64 + STRUCTURE_HEIGHT-1, posZ + STRUCTURE_WIDTH-1);
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
        	if (this.averageGroundLvl < 0)
            {
            	if (this.averageGroundLvl < 0)
                {
            		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						// Modified to center onto front of house
            						this.boundingBox.minX, this.boundingBox.minZ,
            						this.boundingBox.maxX, this.boundingBox.maxZ),
            				true, (byte)15, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodenStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeBarkState = ModObjects.chooseModBark(biomeLogState);

        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            }}
        	
        	// Follow the blueprint to set up the starting foundation
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
        		String unitLetter = foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase();
    			int posX = this.getXWithOffset(u, w);
    			int posY = this.getYWithOffset(GROUND_LEVEL-1);
    			int posZ = this.getZWithOffset(u, w);
    					
        		if (unitLetter.equals("F"))
        		{
        			// If marked with F: fill with dirt foundation
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeDirtState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
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

        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiome(new BlockPos(posX, 64, posZ));
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
        	
        	// Set Grass blocks
        	for (int[] uw : new int[][]{
        		{0, 0},
        		{1, 10},
        	})
        	{
        		this.setBlockState(world, biomeGrassState, uw[0], 0, uw[1], structureBB);
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
        	for (int[] uvwo : new int[][]{
        		{2,2,4, -1},
        		{4,2,2, -1},
        		{4,2,6, -1},
        		{6,2,4, -1},
        	})
        	{
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
                // Line the well with cobblestone
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"), (namePrefix + " " + nameRoot + " " + nameSuffix).trim());
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner, false);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{7, 1, 1, -1, 0},
	        			{8, 1, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
	        			if (random.nextInt(3)==0) {entityvillager.setProfession(5);}
	        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
	        		}
            	}
            }
            
            return true;
        }
    }
    
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
    public static ArrayList<BlueprintData> getRandomSavannaDecorBlueprint(MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 1;
		return getSavannaDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getSavannaDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
		
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
