package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.config.village.VillageGeneratorConfigHandler;
import astrotibs.villagenames.handler.ChestLootHandler;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import astrotibs.villagenames.village.chestloot.ChestGenHooks;
import astrotibs.villagenames.village.chestloot.WeightedRandomChestContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
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
		
		public SavannaMeetingPoint1(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(biomeProvider, componentType, random, posX, posZ, components, terrainType);
    		
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
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
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, false);//, townColor);
            	
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
    		if (GeneralConfig.useVillageColors)
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
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
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
	        			{3, 1, 2, -1, 0},
	        			{5, 1, 4, -1, 0},
	        			{8, 1, 4, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.9+
	        			if (false && random.nextInt(3)==0) {entityvillager.setProfession(5);}
	        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
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
		
		public SavannaMeetingPoint2(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(biomeProvider, componentType, random, posX, posZ, components, terrainType);
    		
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
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
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        	}
        	
        	// Banners
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
    				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
				}
				else
				{
					modifystanding.setInteger("Base", 15 - (GeneralConfig.useVillageColors ? uvwoc[4] : 12));
    				tilebanner.readFromNBT(modifystanding);
				}
				
        		world.setTileEntity(bannerPos, tilebanner);
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
	        			
	        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.9+
	        			if (false && random.nextInt(3)==0) {entityvillager.setProfession(5);}
	        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
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
		
		public SavannaMeetingPoint3(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(biomeProvider, componentType, random, posX, posZ, components, terrainType);
    		
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).getBiomeName()
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
        	IBlockState biomeBarkState = ModObjects.chooseModBarkState(biomeLogState);

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
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(bannerPos, tilebanner);
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
	        			
	        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.9+
	        			if (false && random.nextInt(3)==0) {entityvillager.setProfession(5);}
	        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
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
		
		public SavannaMeetingPoint4(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, int terrainType)
		{
		    super(biomeProvider, componentType, random, posX, posZ, components, terrainType);
    		
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
        	IBlockState biomeBarkState = ModObjects.chooseModBarkState(biomeLogState);

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
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}
    		
    		
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
	        			
	        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.9+
	        			if (false && random.nextInt(3)==0) {entityvillager.setProfession(5);}
	        			else {entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], -12000-random.nextInt(12001));}
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
    }
    
    
    
    // ------------------ //
    // --- Components --- //
    // ------------------ //
    
    
    // --- Covered Animal Pen --- //
    
    public static class SavannaAnimalPen1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
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

        public SavannaAnimalPen1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaAnimalPen1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            

            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
        	
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	// Frame
            	{1,0,2, 7,0,7},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
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
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Fence Gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwos : new int[][]{
            	{4,1,1, 2, 0},
            	})
            {
            	this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(uvwos[3], uvwos[4]==1)), uvwos[0], uvwos[1], uvwos[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Trough
            	{2,1,5, 6,1,5}, {2,1,6, 2,1,6}, {6,1,6, 6,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{3,1,6}, 
            	{4,1,6}, 
            	{5,1,6}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{0,2,8, 8,2,8}, 
            	{0,3,6, 8,3,6}, 
            	{0,4,4, 8,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{0,2,7, 1,2,7}, {7,2,7, 8,2,7}, 
            	{0,3,5, 8,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
            
            
            // Double wood slab
        	IBlockState biomeWoodDoubleSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{2,2,7, 6,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodDoubleSlabState, biomeWoodDoubleSlabState, false);	
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chairs
        		{4,0,0, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
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
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM); // Because horses can escape the pen
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Large Animal Pen --- //
    
    public static class SavannaAnimalPen2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
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

        public SavannaAnimalPen2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaAnimalPen2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
            
            // Dirt with water over it
            for(int[] uvw : new int[][]{
            	{4,0,6}, {5,0,5}, {9,0,6}, 
            	})
            {
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1], uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Fence Gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwos : new int[][]{
            	{8,2,0, 2, 0},
            	})
            {
            	this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(uvwos[3], uvwos[4]==1)), uvwos[0], uvwos[1], uvwos[2], structureBB);
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
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
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
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.getYWithOffset(0);
            	
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, false);
            	
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
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{8, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
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
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, true, this.materialType==MaterialType.MUSHROOM);
                	if (VillageGeneratorConfigHandler.nameVillageHorses && GeneralConfig.nameEntities && animal instanceof EntityHorse)
                	{
                		String[] petname_a = NameGenerator.newRandomName("pet", random);
                		animal.setCustomNameTag((petname_a[1]+" "+petname_a[2]+" "+petname_a[3]).trim());
                	}
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Medium Animal Pen --- //
    
    public static class SavannaAnimalPen3 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
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

        public SavannaAnimalPen3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaAnimalPen3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Fence Gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwos : new int[][]{
            	{2,1,0, 2, 0},
            	{4,1,0, 2, 0}, 
            	})
            {
            	this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(uvwos[3], uvwos[4]==1)), uvwos[0], uvwos[1], uvwos[2], structureBB);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,2,8, -1}, {4,2,8, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{2,4,5, 5,4,5}, {2,4,8, 5,4,8}, 
            	{2,4,6, 2,4,7}, {5,4,6, 5,4,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Double wood slab
        	IBlockState biomeWoodDoubleSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{3,4,6, 4,4,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodDoubleSlabState, biomeWoodDoubleSlabState, false);	
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
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
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
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.getYWithOffset(0);
            	
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, false);
            	
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
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
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
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, true, this.materialType==MaterialType.MUSHROOM);
                	if (VillageGeneratorConfigHandler.nameVillageHorses && GeneralConfig.nameEntities && animal instanceof EntityHorse)
                	{
                		String[] petname_a = NameGenerator.newRandomName("pet", random);
                		animal.setCustomNameTag((petname_a[1]+" "+petname_a[2]+" "+petname_a[3]).trim());
                	}
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Armorer House --- //
    
    public static class SavannaArmorer1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFPFFF",
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

        public SavannaArmorer1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaArmorer1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Below front door
            	{3,0,1, 3,0,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Above front door
            	{3,3,1, 3,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,2, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,2, 4,0,2}, 
            	// Ceiling rim
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	// Top of ceiling
            	{2,5,2, 2,5,4}, {3,5,2, 3,5,3}, {4,5,2, 4,5,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,1,4, 2}, 
        		})
            {
        		IBlockState blastFurnaceState = ModObjects.chooseModBlastFurnaceState(uvw[3], this.getCoordBaseMode());
                this.setBlockState(world, blastFurnaceState.getBlock().getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), blastFurnaceState, 2);
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
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor2 : 1)),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor2 : 1)), 
        				false);
            }
        	
        	
            // Glazed terracotta
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,2,4, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
           		})
        	{
        		IBlockState tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
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
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
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
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    // --- Butcher Shop 1 --- //
    
    public static class SavannaButchersShop1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
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

        public SavannaButchersShop1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaButchersShop1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Across)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
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
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front right window
        		{7,3,3}, 
        		// Back windows
        		{3,3,6}, {7,3,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Torches, part 1
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{4,4,2, 0}, {5,4,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Vertical)
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
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
        	
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	// Frame
            	{3,1,7, 6,1,9},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Backyard
            	{2,2,7, 2,2,10}, {3,2,10, 6,2,10}, {7,2,7, 7,2,10}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Out front
            	{3,3,0, 2}, {5,3,0, 2}, 
            	// Yard
            	{2,3,10, -1}, {7,3,10, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
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
            	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwc[3]), uvwc[0], uvwc[1], uvwc[2], structureBB);
            }
            
            
            // Smoker
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.getCoordBaseMode());
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,4, 1}
            	})
            {
                //this.setBlockState(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), smokerState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Counter
            	{2,2,5, 2,2,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Chimney
            	{2,3,4, 2,7,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Smooth Stone Slab (Upper)
            for(int[] uuvvww : new int[][]{
            	// In front of counter
            	{7,1,4, 7,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.STONE_SLAB.getStateFromMeta(8), Blocks.STONE_SLAB.getStateFromMeta(8), false);
            }
            
            
            // Double Smooth Stone Slab Counter
        	for (int[] uuvvwwo : new int[][]{ 
        		{8,2,4, 8,2,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(0), Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(0), false);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,1, 0, 1, 1}, 
            	{5,2,6, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
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
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
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
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM);
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
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
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    // --- Butcher Shop 2 --- //
    
    public static class SavannaButchersShop2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
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
        		"FFFFFFFFF",
        		"FFFFFFF F",
        		"FFFFFFF  ",
        		"FFFFFFF  ",
        		"FFFFFFF  ",
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
    	
        public SavannaButchersShop2() {}

        public SavannaButchersShop2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaButchersShop2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaButchersShop2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Yard
            	{2,1,1, 2,1,4}, {0,1,4, 1,1,4}, {0,1,5, 0,1,12}, 
            	{1,1,12, 8,1,12}, 
            	{8,1,4, 8,1,11}, 
            	{6,1,4, 7,1,4}, 
            	{6,1,1, 6,1,3}, 
            	// Support posts
            	{2,2,4, 2,2,4}, {2,1,7, 2,2,7}, 
            	{4,1,4, 4,2,4}, {4,1,7, 4,2,7}, 
            	{6,2,4, 6,2,4}, {6,1,7, 6,2,7}, 
            	{5,2,5, 5,2,5},
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front stairs
            	{3,1,1, 5,1,1}, {3,3,3, 5,3,3}, 
            	// Floor
            	{2,3,4, 3,3,7}, {4,3,5, 4,3,6}, {5,3,4, 6,3,7}, 
            	// Wall top rim
            	{2,7,4, 6,7,4}, {2,7,7, 6,7,7}, {2,7,5, 2,7,6}, {6,7,5, 6,7,6}, 
            	// House top
            	{3,8,5, 5,8,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front Steps
        		{3,1,0, 3}, {4,1,0, 3}, {5,1,0, 3}, 
        		{3,2,1, 3}, {4,2,1, 3}, {5,2,1, 3}, 
        		{3,3,2, 3}, {4,3,2, 3}, {5,3,2, 3}, 
        		// Bench
        		{3,4,5, 1}, {3,4,6, 1}, 
        		// Back steps
        		{5,3,8, 1}, {6,2,9, 2}, {6,1,10, 2}, 
        		// Lower roof rim
        		{2,7,3, 3}, {3,7,3, 3}, {4,7,3, 3}, {5,7,3, 3}, {6,7,3, 3}, 
        		{2,7,8, 2}, {3,7,8, 2}, {4,7,8, 2}, {5,7,8, 2}, {6,7,8, 2}, 
        		{1,7,4, 0}, {1,7,5, 0}, {1,7,6, 0}, {1,7,7, 0}, 
        		{7,7,4, 1}, {7,7,5, 1}, {7,7,6, 1}, {7,7,7, 1}, 
        		// Upper roof rim
        		{2,8,4, 3}, {3,8,4, 3}, {4,8,4, 3}, {5,8,4, 3}, {6,8,4, 3}, 
        		{6,8,5, 1}, {6,8,6, 1}, 
        		{2,8,7, 2}, {3,8,7, 2}, {4,8,7, 2}, {5,8,7, 2}, {6,8,7, 2}, 
        		{2,8,5, 0}, {2,8,6, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front steps
            	{3,2,2, 5,2,2}, 
            	// Back steps
            	{3,3,8, 4,3,8}, {6,2,8, 6,2,8}, {6,1,9, 6,1,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,4,4, 3,6,4}, {5,4,4, 6,6,4}, 
            	// Back wall
            	{2,4,7, 3,6,7}, {5,4,7, 6,6,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Above front door
            	{4,6,4, 4,6,4}, 
            	// Above back door
            	{4,6,7, 4,6,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Below front door
            	{4,3,4, 4,3,4}, 
            	// Below back door
            	{4,3,7, 4,3,7}, 
            	// Left wall
            	{2,4,5, 2,4,6}, {2,6,5, 2,6,6}, 
            	// Right wall
            	{6,4,5, 6,4,6}, {6,6,5, 6,6,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Out front
            	{3,5,3, 2}, {5,5,3, 2}, 
            	// Interior
            	{4,6,6, 2}, 
            	// Back
            	{4,6,8, 0}, 
            	// Yard
            	{0,2,12, -1}, {8,2,12, -1}, {8,2,4, -1}, {4,1,5, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Left window
        		{2,5,5}, {2,5,6}, 
        		// Right window
        		{6,5,5}, {6,5,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Smoker
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.getCoordBaseMode());
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,4,8, 0}
            	})
            {
                //this.setBlockState(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), smokerState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,4,4, 0, 1, 1}, 
            	{4,4,7, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Grass block inset into ground
            for (int[] uvwg : new int[][]{ // g is grass type
            	{7,0,0}, {7,0,1}, {7,0,2}, {7,0,3}, 
            	{8,0,0}, {8,0,1}, {8,0,2}, 
            })
            {
            	this.clearCurrentPositionBlocksUpwards(world, uvwg[0], uvwg[1], uvwg[2], structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvwg[0], uvwg[1]-2, uvwg[2], structureBB);
            	// top with grass
            	this.setBlockState(world, biomeGrassState, uvwg[0], uvwg[1]-1, uvwg[2], structureBB);
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,1, 0}, 
            	{1,1,0, 0}, {1,1,2, 0}, {1,1,3, 0}, {1,1,5, 0}, {1,1,7, 0}, {1,1,8, 0}, {1,1,9, 0}, {1,1,10, 0}, 
            	{2,1,0, 0}, {2,1,11, 0}, 
            	{3,1,5, 0}, {3,1,9, 0}, {3,1,10, 0}, {3,1,11, 0}, 
            	{4,1,8, 0}, {4,1,10, 0}, 
            	{5,1,6, 0}, {5,1,9, 0}, {5,1,10, 0}, {5,1,11, 0}, 
            	{6,1,0, 0}, {6,1,5, 0}, {6,1,6, 0}, {6,1,11, 0}, 
            	{7,1,5, 0}, {7,1,6, 0}, {7,1,8, 0}, {7,1,9, 0}, {7,1,10, 0}, 
            	// In the ditch
            	{7,0,0, 0}, {8,0,1, 0}, {8,0,2, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 5;
        	int chestV = 4;
        	int chestW = 6;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_butcher");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{2, 1, 6},
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM);
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
            	// Villager
            	int u = 4;
            	int v = 4;
            	int w = 5;
            	
            	int s = random.nextInt(3);
            	
            	if (s==2)
            	{
            		u=5;
            		w=5;
            	}
            	else
            	{
            		u=4;
            		w=5+s;
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    // --- Cartographer --- //
    
    public static class SavannaCartographer1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"         ",
        		" F F F F ",
        		"         ",
        		" F  F  F ",
        		"         ",
        		" F F F F ",
        		"   FFF   ",
        		"   FFF   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaCartographer1() {}

        public SavannaCartographer1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaCartographer1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaCartographer1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Supports
            	{1,0,2, 1,1,2}, {3,0,2, 3,1,2}, {5,0,2, 5,1,2}, {7,0,2, 7,1,2}, 
            	{1,0,4, 1,1,4}, {7,0,4, 7,1,4}, 
            	{1,0,6, 1,1,6}, {3,0,6, 3,1,6}, {5,0,6, 5,1,6}, {7,0,6, 7,1,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front stairs
            	{3,0,1, 5,0,1}, {4,1,2, 4,1,2}, 
            	// Floor
            	{1,2,2, 1,2,6}, {2,2,3, 6,2,6}, {7,2,2, 7,2,6}, 
            	// Ceiling rim
            	{1,6,2, 7,6,3}, {1,6,6, 7,6,6}, {1,6,4, 1,6,5}, {7,6,4, 7,6,5}, 
            	// Ceiling top
            	{2,7,3, 6,7,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{3,0,0, 3}, {4,0,0, 3}, {5,0,0, 3}, 
        		{3,1,1, 3}, {4,1,1, 3}, {5,1,1, 3}, 
        		// Lower roof rim
        		{0,6,1, 3}, {1,6,1, 3}, {2,6,1, 3}, {3,6,1, 3}, {4,6,1, 3}, {5,6,1, 3}, {6,6,1, 3}, {7,6,1, 3}, {8,6,1, 3}, 
        		{0,6,7, 2}, {1,6,7, 2}, {2,6,7, 2}, {3,6,7, 2}, {4,6,7, 2}, {5,6,7, 2}, {6,6,7, 2}, {7,6,7, 2}, {8,6,7, 2}, 
        		{0,6,2, 0}, {0,6,3, 0}, {0,6,4, 0}, {0,6,5, 0}, {0,6,6, 0}, 
        		{8,6,2, 1}, {8,6,3, 1}, {8,6,4, 1}, {8,6,5, 1}, {8,6,6, 1}, 
        		// Upper roof rim
        		{1,7,2, 3}, {2,7,2, 3}, {3,7,2, 3}, {4,7,2, 3}, {5,7,2, 3}, {6,7,2, 3}, {7,7,2, 3}, 
        		{1,7,6, 2}, {2,7,6, 2}, {3,7,6, 2}, {4,7,6, 2}, {5,7,6, 2}, {6,7,6, 2}, {7,7,6, 2}, 
        		{1,7,3, 0}, {1,7,4, 0}, {1,7,5, 0}, 
        		{7,7,3, 1}, {7,7,4, 1}, {7,7,5, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front steps
            	{2,2,2, 6,2,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Corner posts
            	{1,3,2, 1,5,2}, {7,3,2, 7,5,2}, 
            	{1,3,6, 1,5,6}, {7,3,6, 7,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	// Tower roof
            	{2,3,4, 2,3,4}, {6,3,5, 6,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Torches, part 1
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,4,4, -1}, {6,4,5, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,3,3, 1,5,5}, 
            	// Right wall
            	{7,3,3, 7,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,3,3, 6,5,3}, 
            	// Back wall
            	{2,3,6, 6,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Torches, part 2
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Out front
            	{4,5,2, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Back window
        		{3,4,6}, {5,4,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
        	// Cartography Table
        	IBlockState cartographyTableState = ModObjects.chooseModCartographyTableState();
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,3,5}, 
           		})
        	{
            	this.setBlockState(world, cartographyTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,3,3, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 6;
        	int chestV = 3;
        	int chestW = 4;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_cartographer");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Solid color banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{0,3,3, 3, GeneralConfig.useVillageColors ? this.townColor2 : 12}, // Brown
				{0,3,4, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{0,3,5, 3, GeneralConfig.useVillageColors ? this.townColor4 : 12}, // Brown
				
				{8,3,3, 1, GeneralConfig.useVillageColors ? this.townColor5 : 12}, // Brown 
				{8,3,4, 1, GeneralConfig.useVillageColors ? this.townColor6 : 12}, // Brown
				{8,3,5, 1, GeneralConfig.useVillageColors ? this.townColor7 : 12}, // Brown
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
				
				modifystanding.setInteger("Base", 15 - (GeneralConfig.useVillageColors ? uvwoc[4] : 12));
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
    		
    		
        	// Patterned banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{3,5,2, 2, 12}, // Brown
				{5,5,2, 2, 12}, // Brown
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
				modifystanding.setBoolean("IsStanding", false);
				
				if (GeneralConfig.useVillageColors)
				{
	            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
	            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
	            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
	            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
					
    				tilebanner.readFromNBT(modifystanding);
    				ItemStack villageBanner = new ItemStack(Items.BANNER);
    				villageBanner.setTagInfo("BlockEntityTag", villageNBTtag.getCompoundTag("BlockEntityTag"));
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
				}
				else
				{
					modifystanding.setInteger("Base", uvwoc[4]);
    				tilebanner.readFromNBT(modifystanding);
				}
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
    		
			
			
        	// Patterned banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{3,5,2, 2, 12}, // Brown
				{5,5,2, 2, 12}, // Brown
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
				modifystanding.setBoolean("IsStanding", false);
				
				if (GeneralConfig.useVillageColors)
				{
	            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
	            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
	            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
	            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
					
    				tilebanner.readFromNBT(modifystanding);
    				ItemStack villageBanner = new ItemStack(Items.BANNER);
    				villageBanner.setTagInfo("BlockEntityTag", villageNBTtag.getCompoundTag("BlockEntityTag"));
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
				}
				else
				{
					modifystanding.setInteger("Base", uvwoc[4]);
    				tilebanner.readFromNBT(modifystanding);
				}
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
			
			
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3 + random.nextInt(3);
            	int v = 3;
            	int w = 4 + random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 2, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    // --- Fisher Cottage --- //
    
    public static class SavannaFisherCottage1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFFF",
        		"FFFFFFF ",
        		"FFFFFFF ",
        		" FFFFF  ",
        		"   FFF  ",
        		"   FFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 11;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaFisherCottage1() {}

        public SavannaFisherCottage1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaFisherCottage1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaFisherCottage1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{3,2,0, 3}, {4,2,0, 3}, {5,2,0, 3}, 
        		{3,3,1, 3}, {4,3,1, 3}, {5,3,1, 3}, 
        		{3,4,2, 3}, {4,4,2, 3}, {5,4,2, 3}, 
        		// Lower roof trim
        		{1,9,2, 3}, {2,9,2, 3}, {3,9,2, 3}, {4,9,2, 3}, {5,9,2, 3}, {6,9,2, 3}, {7,9,2, 3}, 
        		{1,9,7, 2}, {2,9,7, 2}, {3,9,7, 2}, {4,9,7, 2}, {5,9,7, 2}, {6,9,7, 2}, {7,9,7, 2},
        		{1,9,3, 0}, {1,9,4, 0}, {1,9,5, 0}, {1,9,6, 0}, 
        		{7,9,3, 1}, {7,9,4, 1}, {7,9,5, 1}, {7,9,6, 1}, 
        		// Higher roof trim
        		{2,10,3, 3}, {3,10,3, 3}, {4,10,3, 3}, {5,10,3, 3}, {6,10,3, 3}, 
        		{2,10,6, 2}, {3,10,6, 2}, {4,10,6, 2}, {5,10,6, 2}, {6,10,6, 2}, 
        		{2,10,4, 0}, {2,10,5, 0}, 
        		{6,10,4, 1}, {6,10,5, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front stairs
            	{3,2,1, 5,2,1}, {3,3,2, 5,3,2}, {3,4,3, 3,4,3}, {5,4,3, 5,4,3}, 
            	// Floor
            	{2,5,3, 2,5,6}, {3,5,4, 5,5,6}, {6,5,3, 6,5,6}, 
            	// Top of walls
            	{2,9,3, 2,9,6}, {6,9,3, 6,9,6}, {3,9,3, 5,9,4}, {3,9,6, 5,9,6}, 
            	// Top of roof
            	{3,10,4, 5,10,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{3,6,4, 5,8,4}, 
            	// Back wall
            	{3,6,6, 5,8,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Out front
            	{3,7,3, 2}, {5,7,3, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Vertical)
            for(int[] uuvvww : new int[][]{
            	// Corner posts
            	{2,6,3, 2,8,3}, {6,6,3, 6,8,3}, 
            	{2,6,6, 2,8,6}, {6,6,6, 6,8,6}, 
            	// Base of posts in the water
            	{2,1,6, 2,1,6}, {4,1,6, 4,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Supports
            	{2,2,3, 2,4,3}, {4,2,3, 4,4,3}, {6,2,3, 6,4,3}, 
            	{2,2,6, 2,4,6}, {4,2,6, 4,4,6}, {6,2,6, 6,4,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front steps
            	{3,5,3, 5,5,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{2,6,4, 2,8,5}, 
            	// Right wall
            	{6,6,4, 6,8,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Back window
        		{4,7,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Barrels
    		IBlockState barrelState = ModObjects.chooseModBarrelBlockState();
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel: -1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
    			// TODO - use different barrel meta for different mods
            	
    			// Exterior
    			{1,2,2, 2,2}, 
    			{5,2,4, 3,3}, 
    			{5,3,4, 0,-1}, 
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (barrelState==null) {barrelState = Blocks.CHEST.getDefaultState();}
    			this.setBlockState(world, barrelState, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2])), barrelState.getBlock().getStateFromMeta(barrelState.getBlock()==Blocks.CHEST?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.getCoordBaseMode()):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.getCoordBaseMode())), 2);
            }
            
            
            // Water with dirt underneath
            for(int[] uvw : new int[][]{
            	{1,1,4}, {1,1,5}, {1,1,6}, {1,1,7}, 
            	{2,1,4}, {2,1,5}, {2,1,7}, 
            	{3,1,4}, {3,1,5}, {3,1,6}, {3,1,7}, 
            	{4,1,5}, {4,1,7}, 
            	{5,1,6}, {5,1,7}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,6,4, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,2,3, 0}, 
            	{1,2,3, 0}, 
            	{5,2,2, 0}, {5,2,8, 0}, 
            	{6,2,7, 0}, 
            	{7,2,6, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 4;
            	int v = 2;
            	int w = 5;
            	
            	int s=random.nextInt(3+2+5+7+2+2+2+1);
            	
            	if (s<(3+2+5+7+2+2+2+1)) // Right edge
            	{
                	u = 5;
                	w = 5;
            	}
            	else if (s<(3+2+5+7+2+2+2)) // Right edge
            	{
                	u = 6;
                	w = s-17;
            	}
            	else if (s<(3+2+5+7+2+2)) // Right edge
            	{
                	u = 7;
                	w = s-14;
            	}
            	else if (s<(3+2+5+7+2)) // Back right corner
            	{
                	u = s-11;
                	w = 7;
            	}
            	else if (s<(3+2+5+7)) // Back edge
            	{
                	u = s-9;
                	w = 8;
            	}
            	else if (s<(3+2+5)) // Left edge
            	{
                	u = 0;
                	w = s-1;
            	}
            	else if (s<(3+2)) // Back left corner
            	{
                	u = s-3;
                	w = 3;
            	}
            	else // In the house
            	{
                	u = s+3;
                	v = 6;
                	w = 5;
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 2, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Fletcher House --- //
    
    public static class SavannaFletcherHouse1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFPFFFPFFF",
        		"FFFPFFFPFFF",
        		"FFFPFFFPFFF",
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
    	
        public SavannaFletcherHouse1() {}

        public SavannaFletcherHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaFletcherHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaFletcherHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
        	
            // Stained Terracotta, part 1
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,1,3, 4,4,3, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow 
        		{6,1,3, 8,4,3, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Counters
            	{4,1,6, 4,1,6}, {6,1,6, 6,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Out front
            	{3,3,2, 2}, {7,3,2, 2}, 
            	// Interior
            	{4,2,6, -1}, {6,2,6, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Stained Terracotta, part 2
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,1,7, 4,4,7, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow 
        		{6,1,7, 8,4,7, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow  
        		// Left wall
        		{1,1,4, 1,4,6, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow  
        		// Right wall
        		{9,1,4, 9,4,6, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow  
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Logs (Vertical)
            for(int[] uuvvww : new int[][]{
            	// Front post
            	{5,1,3, 5,4,3},  
            	// Back post
            	{5,1,7, 5,4,7},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Interior chairs
        		{2,1,4, 2}, {2,1,6, 3}, 
        		{8,1,4, 2}, {8,1,6, 3}, 
        		// Lower roof trim
        		{0,5,2, 3}, {1,5,2, 3}, {2,5,2, 3}, {3,5,2, 3}, {4,5,2, 3}, {5,5,2, 3}, {6,5,2, 3}, {7,5,2, 3}, {8,5,2, 3}, {9,5,2, 3}, {10,5,2, 3}, 
        		{0,5,8, 2}, {1,5,8, 2}, {2,5,8, 2}, {3,5,8, 2}, {4,5,8, 2}, {5,5,8, 2}, {6,5,8, 2}, {7,5,8, 2}, {8,5,8, 2}, {9,5,8, 2}, {10,5,8, 2}, 
        		{0,5,3, 0}, {0,5,4, 0}, {0,5,5, 0}, {0,5,6, 0}, {0,5,7, 0}, 
        		{10,5,3, 1}, {10,5,4, 1}, {10,5,5, 1}, {10,5,6, 1}, {10,5,7, 1}, 
        		// Upper trim
        		{1,6,3, 3}, {2,6,3, 3}, {3,6,3, 3}, {4,6,3, 3}, {5,6,3, 3}, {6,6,3, 3}, {7,6,3, 3}, {8,6,3, 3}, {9,6,3, 3}, 
        		{1,6,7, 2}, {2,6,7, 2}, {3,6,7, 2}, {4,6,7, 2}, {5,6,7, 2}, {6,6,7, 2}, {7,6,7, 2}, {8,6,7, 2}, {9,6,7, 2}, 
        		{1,6,4, 0}, {1,6,5, 0}, {1,6,6, 0}, 
        		{9,6,4, 1}, {9,6,5, 1}, {9,6,6, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Top of pillars
            	{1,4,1, 1,4,1}, {5,4,1, 5,4,1}, {9,4,1, 9,4,1}, 
            	// Top of walls
            	{2,5,3, 8,5,3}, {2,5,7, 8,5,7}, {1,5,3, 1,5,7}, {9,5,3, 9,5,7}, 
            	// Top
            	{2,6,4, 8,6,6}, 
            	// Floor
            	{2,0,4, 8,0,6}, {3,0,3, 3,0,3}, {7,0,3, 7,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Supports
            	{1,1,1, 1,3,1}, {5,1,1, 5,3,1}, {9,1,1, 9,3,1}, 
            	// Tables
            	{2,1,5, 2,1,5}, {8,1,5, 8,1,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{2,2,5}, 
        		{8,2,5}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Awning
            	{2,4,1, 4,4,1}, {6,4,1, 8,4,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Awning
            	{2,4,2, 8,4,2}, {1,4,2, 1,4,3}, {9,4,2, 9,4,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Back window
        		{1,2,5}, {1,3,5}, 
        		{3,2,7}, {3,3,7}, 
        		{7,2,7}, {7,3,7}, 
        		{9,2,5}, {9,3,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Fletching Table
        	IBlockState fletchingTableState = ModObjects.chooseModFletchingTableState();
        	this.setBlockState(world, fletchingTableState, 5, 1, 6, structureBB);
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,3, 0, 1, 0}, 
            	{7,1,3, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
        	
			// Solid color banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				// Front
				{1,4,0, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{5,4,0, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{9,4,0, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				// Back
				{1,4,8, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{5,4,8, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{9,4,8, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				// Left
				{0,4,5, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				// Right
				{10,4,5, 1, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
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
				modifystanding.setBoolean("IsStanding", false);
				modifystanding.setInteger("Base", uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,5, 0}, {0,1,7, 0}, {0,1,8, 0}, 
            	{1,1,0, 0}, {1,1,8, 0}, 
            	{2,1,0, 0}, {2,1,2, 0}, {2,1,8, 0}, 
            	{4,1,2, 0}, 
            	{5,1,2, 0}, 
            	{6,1,1, 0}, {6,1,2, 0}, {6,1,8, 0}, 
            	{8,1,0, 0}, {8,1,1, 0}, 
            	{10,1,0, 0}, {10,1,2, 0}, {10,1,3, 0}, {10,1,4, 0}, {10,1,5, 0}, {10,1,6, 0}, {10,1,7, 0}, {10,1,8, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{7, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(5);
            	int v = 1;
            	int w = 4+random.nextInt(2);
            	
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 4, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Methodical Farm --- //
    
    public static class SavannaLargeFarm1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
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
        		"FFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaLargeFarm1() {}

        public SavannaLargeFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaLargeFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaLargeFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Frame
            	{1,1,0, 3,1,0}, {5,1,0, 7,1,0}, 
            	{0,1,1, 0,1,3}, {0,1,5, 0,1,7}, 
            	{1,1,8, 3,1,8}, {5,1,8, 7,1,8}, 
            	{8,1,1, 8,1,3}, {8,1,5, 8,1,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Interior chairs
        		{4,1,0, 3}, 
        		{4,1,8, 2}, 
        		{0,1,4, 0}, 
        		{8,1,4, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Top of pillars
            	{1,1,4, 3,1,4}, {4,1,1, 4,1,7}, {5,1,4, 7,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Moist Farmland
            for(int[] uuvvww : new int[][]{
            	// Top of pillars
            	{1,1,1, 2,1,3}, {3,1,1, 3,1,2}, 
            	{1,1,5, 2,1,7}, {3,1,6, 3,1,7}, 
            	{5,1,1, 5,1,2}, {6,1,1, 7,1,3}, 
            	{5,1,6, 5,1,7}, {6,1,5, 7,1,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FARMLAND.getStateFromMeta(7), Blocks.FARMLAND.getStateFromMeta(7), false);	
            }
            
            
            // Water with grass underneath
            for(int[] uvw : new int[][]{
            	{3,1,3}, {5,1,3}, 
            	{3,1,5}, {5,1,5}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            	this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, do nothing
            IBlockState compostBinState = ModObjects.chooseModComposter();
            if (compostBinState != null)
            {
            	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
            		// Interior chairs
            		{8,1,0}, 
            		{0,1,8}, 
            		})
                {
            		this.setBlockState(world, compostBinState.getBlock().getDefaultState(), uvwo[0], uvwo[1], uvwo[2], structureBB);	
                }
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
            
        	// Crops
            for(int[][] cropPos : new int[][][]{
            	// Top of pillars
            	{{1,2,1, 2,2,3}, {3,2,1, 3,2,2}}, 
            	{{1,2,5, 2,2,7}, {3,2,6, 3,2,7}}, 
            	{{5,2,1, 5,2,2}, {6,2,1, 7,2,3}}, 
            	{{5,2,6, 5,2,7}, {6,2,5, 7,2,7}}, 
            	})
            {
            	// Grab a random crop
            	Block cropBlock = StructureVillageVN.chooseCropPair(random)[0];
            	
            	for (int i=0; i<2; i++)
            	{
        			int cropProgressMeta = 7; // Isolate the crop's age meta value
        			IBlockState cropState;
        			
        			while(true)
        			{
        				try {cropState = cropBlock.getStateFromMeta(cropProgressMeta);}
        				catch (IllegalArgumentException e)
        				{
        					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
        					if (cropProgressMeta==0) {cropState = Blocks.WHEAT.getStateFromMeta(7);}
        					// The crop is not allowed to have this value. Cut it in half and try again.
        					else {cropProgressMeta /= 2; continue;}
        				}

        				// Finally, assign the working crops
    					this.fillWithBlocks(world, structureBB,
    							cropPos[i][0], cropPos[i][1], cropPos[i][2],
    							cropPos[i][3], cropPos[i][4], cropPos[i][5],
    							cropState, cropState, false);
        				break;
        			}
            	}
            }
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u=3;
            	int v=2;
            	int w=3;
            	
            	while ((u==3 || u==5) && (w==3 || w==5))
            	{
            		u = 1+random.nextInt(STRUCTURE_WIDTH-2);
                	w = 1+random.nextInt(STRUCTURE_DEPTH-2);
            	}
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Haphazard Farm --- //
    
    public static class SavannaLargeFarm2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFF   FFF ",
        		"FFFPFFFFF ",
        		"PFFFPFFFF ",
        		"PPFFFPFFF ",
        		"FFFFFFFFPP",
        		"FFFFFFPFFF",
        		"PFFFFFFPF ",
        		" PFFFFFFFF",
        		"   FFF  F ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 2;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaLargeFarm2() {}

        public SavannaLargeFarm2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaLargeFarm2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaLargeFarm2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
                        
            // Moist Farmland with wheat above
            for(int[] uvw : new int[][]{
            	
            	{0,1,3, 6}, 
            	{0,1,4, 7}, 
            	
            	{1,1,2, 7}, 
            	{1,1,6, 5}, 
            	
            	{2,1,2, 6},
            	{2,1,3, 5},
            	{2,1,4, 5},
            	{2,1,5, 7},
            	{2,1,6, 7},
            	
            	{3,1,1, 7},
            	{3,1,2, 7},
            	{3,1,5, 7},
            	{3,1,6, 7},
            	
            	{4,1,2, 7},
            	{4,1,3, 6},
            	{4,1,4, 7},
            	{4,1,7, 2},
            	
            	{5,1,0, 7},
            	{5,1,1, 7},
            	{5,1,2, 7},
            	{5,1,3, 7},
            	{5,1,4, 6},
            	
            	{6,1,4, 7},
            	{6,1,5, 7},
            	{6,1,6, 7},
            	{6,1,7, 4},
            	
            	{7,1,3, 0},
            	{7,1,4, 7},
            	{7,1,5, 6},
            	{7,1,6, 7},
            	
            	{8,1,0, 7},
            	{8,1,1, 7},
            	{8,1,2, 0},
            	{8,1,6, 4},
            	{8,1,7, 6},
            	
            	})
            {
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvw[3]), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Water with dirt underneath
            for(int[] uvw : new int[][]{
            	{1,1,3}, 
            	{1,1,7}, 
            	{4,1,1}, 
            	{4,1,5}, 
            	{7,1,7}, 
            	{8,1,3}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
        	// Grass
            for(int[] uvw : new int[][]{
            	{1,0,7},
            	})
            {
    			this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, do nothing
            IBlockState compostBinState = ModObjects.chooseModComposter();
            if (compostBinState != null)
            {
            	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
            		// Interior chairs
            		{6,2,2}, 
            		})
                {
            		this.setBlockState(world, compostBinState.getBlock().getDefaultState(), uvwo[0], uvwo[1], uvwo[2], structureBB);	
                }
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,2,7, 0}, 
            	{1,2,4, 1}, 
            	{2,2,1, 0}, {2,2,7, 1}, 
            	{3,2,0, 1}, {3,2,4, 0}, 
            	{5,2,6, 1}, {5,2,7, 0}, 
            	{6,2,1, 1}, 
            	{7,2,1, 0}, 
            	{8,2,5, 1}, 
            	{9,2,1, 0}, {9,2,3, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{3,2,3},
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
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.getYWithOffset(0);
            	
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, false);
            	
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u=3;
            	int v=2;
            	int w=3;
            	
            	while (
            			// Water
            			   (u==1 && w==3)
            			|| (u==1 && w==7)
            			|| (u==4 && w==1)
            			|| (u==4 && w==5)
            			|| (u==7 && w==7)
            			|| (u==8 && w==3)
            			// Composter
            			|| (u==6 && w==2)
            			// Decor
            			|| (u==3 && w==3)
            			)
            	{
            		u = 1+random.nextInt(STRUCTURE_WIDTH-2);
                	w = 1+random.nextInt(STRUCTURE_DEPTH-2);
            	}
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Library --- //
    
    public static class SavannaLibrary1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"  FFFFFFFF",
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
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaLibrary1() {}

        public SavannaLibrary1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaLibrary1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaLibrary1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front planter frame
            	{2,1,0, 3,1,0}, {6,1,0, 7,1,0}, 
            	{1,1,1, 1,1,2}, {8,1,1, 8,1,2}, 
            	// Entry floor
            	{4,2,2, 5,2,2}, {2,1,3, 7,2,3}, 
            	{1,1,4, 1,2,4}, {8,1,4, 8,2,4}, 
            	// Entry frame
            	{3,1,3, 3,5,3}, {6,1,3, 6,5,3}, 
            	// Back foundation patterning
            	{2,1,6, 2,1,6}, {4,1,6, 4,1,6}, {6,1,6, 6,1,6}, 
            	{3,2,6, 3,2,6}, {5,2,6, 5,2,6}, {7,2,6, 7,2,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Back foundation
            	{1,1,5, 1,2,5}, {8,1,5, 8,2,5}, 
            	{2,2,6, 2,2,6}, {4,2,6, 4,2,6}, {6,2,6, 6,2,6}, 
            	{3,1,6, 3,1,6}, {5,1,6, 5,1,6}, {7,1,6, 7,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Entry frame
            	{4,5,3, 5,5,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,3,3, 2,5,3, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange 
        		{7,3,3, 7,5,3, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange 
        		// Left wall
        		{1,3,4, 1,5,5, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange 
        		// Right wall
        		{8,3,4, 8,5,5, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
        		// Back wall
        		{2,3,6, 7,5,6, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Out front
            	{3,2,0, -1}, {6,2,0, -1}, 
            	// Interior
            	{3,5,5, 2}, {6,5,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front stairs
        		{4,1,0, 3}, {5,1,0, 3}, 
        		{4,2,1, 3}, {5,2,1, 3}, 
        		// Lower roof trim
        		{1,6,2, 3}, {2,6,2, 3}, {3,6,2, 3}, {4,6,2, 3}, {5,6,2, 3}, {6,6,2, 3}, {7,6,2, 3}, {8,6,2, 3}, 
        		{1,6,7, 2}, {2,6,7, 2}, {3,6,7, 2}, {4,6,7, 2}, {5,6,7, 2}, {6,6,7, 2}, {7,6,7, 2}, {8,6,7, 2}, 
        		{0,6,3, 0}, {0,6,4, 0}, {0,6,5, 0}, {0,6,6, 0}, 
        		{9,6,3, 1}, {9,6,4, 1}, {9,6,5, 1}, {9,6,6, 1}, 
        		// Upper trim
        		{2,6,3, 3}, {3,6,3, 3}, {4,6,3, 3}, {5,6,3, 3}, {6,6,3, 3}, {7,6,3, 3}, 
        		{2,6,6, 2}, {3,6,6, 2}, {4,6,6, 2}, {5,6,6, 2}, {6,6,6, 2}, {7,6,6, 2}, 
        		{1,6,4, 0}, {1,6,5, 0}, 
        		{8,6,4, 1}, {8,6,5, 1}, 
        		// Interior bench
        		{7,3,4, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front entrance
            	{4,1,1, 5,1,2}, 
            	// Floor
            	{2,1,4, 7,2,5}, 
            	// Wall tops
            	{1,6,3, 8,6,3}, {1,6,6, 8,6,6}, 
            	{1,6,4, 1,6,5}, {8,6,4, 8,6,5}, 
            	// Ceiling
            	{2,7,4, 7,7,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Back window
        		{3,4,6}, {6,4,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Lecterns
        	/*
        	blockObject = ModObjects.chooseModLectern(); Block lecternBlock = (Block) blockObject[0]; int lecternMeta = (Integer) blockObject[1];
            this.setBlockState(world, lecternBlock, lecternMeta, 2, 3, 5, structureBB);
            */
            for (int[] uvwo : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,3,5, 1},
            })
            {
        		ModObjects.setModLecternState(world,
            			this.getXWithOffset(uvwo[0], uvwo[2]),
            			this.getYWithOffset(uvwo[1]),
            			this.getZWithOffset(uvwo[0], uvwo[2]),
            			uvwo[3],
            			this.getCoordBaseMode(),
            			biomePlankState.getBlock().getMetaFromState(biomePlankState));
            }
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{2,3,4, 2,6,4}, 
        		{7,3,5, 7,6,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		{4,3,4, (GeneralConfig.useVillageColors ? this.townColor2 : 1)}, // Orange
        		{5,3,5, (GeneralConfig.useVillageColors ? this.townColor2 : 1)}, // Orange
        		{5,3,4, (GeneralConfig.useVillageColors ? this.townColor4 : 0)}, // White
        		{4,3,5, (GeneralConfig.useVillageColors ? this.townColor4 : 0)}, // White
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,3,3, 0, 1, 0}, 
            	{5,3,3, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
        	// Grass
            for(int[] uvw : new int[][]{
            	{2,1,1}, {2,1,2}, {3,1,1}, {3,1,2}, 
            	{6,1,1}, {6,1,2}, {7,1,1}, {7,1,2}, 
            	})
            {
    			this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,1, 0}, {0,1,3, 0}, {0,1,4, 0}, 
            	{1,1,0, 0}, {1,1,3, 0}, 
            	{2,2,1, 0},  
            	{3,2,1, 0}, {3,2,2, 1}, 
            	{6,2,2, 1}, 
            	{7,2,2, 0}, {7,1,7, 0}, 
            	{8,1,0, 0}, {8,1,7, 0}, 
            	{9,1,1, 0}, {9,1,3, 0}, {9,1,4, 0}, {9,1,7, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
            
            
        	// Poppy
        	for (int[] uvw : new int[][]{
        		// Back window
        		{2,2,2}, {6,2,1}, 
        		})
            {
        		this.setBlockState(world, Blocks.RED_FLOWER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Sapling
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SAPLING.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{2,2,1}, {7,2,1}, 
        		})
            {
        		this.setBlockState(world, biomeSaplingState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3 + random.nextInt(4);
            	int v = 3;
            	int w = 4 + random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    // --- Mason House --- //
    
    public static class SavannaMason1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"          ",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		" FFFFFFFFF",
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
    	
        public SavannaMason1() {}

        public SavannaMason1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaMason1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaMason1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,2, 2,2,2}, 
            	{3,1,1, 3,2,1}, 
            	{6,1,1, 6,2,1}, 
            	{7,1,2, 7,2,2}, 
            	// Left wall
            	{1,1,3, 1,3,3}, 
            	{1,1,4, 1,1,4}, 
            	{1,1,5, 1,3,5}, 
            	// Back wall
            	{2,1,6, 2,3,6}, 
            	{3,1,6, 3,1,6}, 
            	{4,1,6, 5,3,6}, 
            	{6,1,6, 6,1,6}, 
            	{7,1,6, 7,3,6}, 
            	// Right wall
            	{8,1,3, 8,3,3}, 
            	{8,1,4, 8,1,4}, 
            	{8,1,5, 8,3,5},             	
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Under the front door
            	{4,0,1, 5,0,1}, 
            	// Left wall
            	{1,3,4, 1,3,4}, 
            	// Right wall
            	{8,3,4, 8,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Back wall
            	{3,3,6, 3,3,6}, 
            	{6,3,6, 6,3,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
        	
        	
            // Glazed terracotta
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{2,3,2, 1, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow
        		{3,3,1, 1, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow
        		{4,3,1, 0, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow
        		{5,3,1, 1, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow
        		{6,3,1, 0, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow
        		{7,3,2, 0, GeneralConfig.useVillageColors ? this.townColor : 4}, // Yellow
           		})
        	{
        		IBlockState tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{3,3,5, 2}, {6,3,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,3, 7,0,5}, {3,0,2, 6,0,2}, 
            	// Wall tops
            	{3,4,1, 6,4,1}, {2,4,2, 2,4,2}, {1,4,3, 1,4,5}, {2,4,6, 7,4,6}, {8,4,3, 8,4,5}, {7,4,2, 7,4,2}, 
            	// Ceiling
            	{2,5,3, 7,5,5}, {3,5,2, 6,5,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front roof
        		{1,4,2, 3}, {2,4,1, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, {6,4,0, 3}, {7,4,1, 3}, {8,4,2, 3}, 
        		{1,5,3, 3}, {2,5,2, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, {6,5,1, 3}, {7,5,2, 3}, {8,5,3, 3}, 
        		{9,4,3, 1}, {9,4,4, 1}, {9,4,5, 1}, {8,5,4, 1}, 
        		{0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, {1,5,4, 0}, 
        		{1,5,5, 2}, {1,4,6, 2}, {2,5,6, 2}, {3,5,6, 2}, {4,5,6, 2}, {5,5,6, 2}, {6,5,6, 2}, {7,5,6, 2}, {8,4,6, 2}, {8,5,5, 2}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Back window
        		{1,2,4}, {3,2,6}, {6,2,6}, {8,2,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Stone Cutter
        	// Orientation:0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	IBlockState stonecutterState = ModObjects.chooseModStonecutterState(3);
            this.setBlockState(world, stonecutterState, 2, 1, 4, structureBB);
        	
            
            // Clay
            for(int[] uvw : new int[][]{
            	{4,1,5}, {6,1,5}, {7,1,5}, {7,2,5}, {7,3,5}, {7,1,4}, 
            	})
            {
            	this.setBlockState(world, Blocks.CLAY.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);	
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 5;
        	int chestV = 1;
        	int chestW = 5;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_mason");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Tables
            	{3,1,2, 3,1,2}, {6,1,2, 6,1,2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{3,2,2}, 
        		{6,2,2}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,1, 0, 1, 0}, 
            	{5,1,1, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,1, 0}, {0,1,2, 0}, 
            	{1,1,0, 0}, {1,1,2, 0}, 
            	{2,1,0, 0}, 
            	{3,1,0, 0}, 
            	{6,1,0, 0}, 
            	{7,1,0, 0}, {7,1,1, 0}, 
            	{8,1,0, 0}, {8,1,2, 0}, 
            	{9,1,1, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3 + random.nextInt(4);
            	int v = 1;
            	int w = 3 + random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 4, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    // --- Medium House 1 --- //
    
    public static class SavannaMediumHouse1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"    FFFFFFF    ",
        		"    FFFFFFF    ",
        		"  FFFFFFFFFFF  ",
        		" FFFFFFFFFFFFF ",
        		" FFFFFFFFFFFFF ",
        		" FFFFFFFFFFFFF ",
        		"  FFFFFFF FFF  ",
        		"   PFFFFF  P   ",
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
    	
        public SavannaMediumHouse1() {}

        public SavannaMediumHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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

        public static SavannaMediumHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaMediumHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	{1,3,3, 1,3,3},
            	{5,3,3, 5,3,3},
            	{9,3,3, 9,3,3},
            	{13,3,3, 13,3,3},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Back wall
            	{3,3,1, 3,3,1}, {11,3,1, 11,3,1}, 
            	{3,3,5, 3,3,5}, {11,3,5, 11,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Garden path
            	{6,1,2, 8,1,2}, 
            	{4,1,6, 4,1,7}, {5,1,7, 9,1,7}, {10,1,6, 10,1,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            // Wood with bark on all sides
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	// Right house platform
            	{10,1,4, 10,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{3,3,0, 2}, {11,3,0, 2}, 
            	// Interior
            	{3,3,4, 2}, {10,2,4, -1}, 
            	// Garden
            	{6,3,3, 1}, {8,3,3, 3}, 
            	{4,2,7, -1}, {10,2,7, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Vertical)
            for(int[] uuvvww : new int[][]{
            	// Left House
            	
            	// Front wall
            	{2,1,1, 2,3,1}, {4,1,1, 4,3,1}, 
            	// Left
            	{1,1,2, 1,3,2}, {1,1,3, 1,1,3}, {1,1,4, 1,3,4}, 
            	// Back
            	{2,1,5, 2,3,5}, {3,1,5, 3,1,5}, {4,1,5, 4,3,5}, 
            	// Right
            	{5,1,2, 5,3,2}, {5,1,4, 5,3,4}, 
            	
            	// Right House
            	
            	// Front wall
            	{12,1,1, 12,3,1}, {10,1,1, 10,3,1}, 
            	// Left
            	{13,1,2, 13,3,2}, {13,1,3, 13,1,3}, {13,1,4, 13,3,4}, 
            	// Back
            	{12,1,5, 12,3,5}, {11,1,5, 11,1,5}, {10,1,5, 10,3,5}, 
            	// Right
            	{9,1,2, 9,3,2}, {9,1,4, 9,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left house
            	
            	// Floor
            	{3,0,1, 3,0,1}, {2,0,2, 4,0,4}, {5,0,3, 5,0,3}, 
            	// Wall tops
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	// Ceiling
            	{2,5,2, 4,5,4}, {3,6,3, 3,6,3}, 
            	
            	// Right house
            	
            	// Floor
            	{11,0,1, 11,0,1}, {10,0,2, 12,0,4}, {9,0,3, 9,0,3}, 
            	// Wall tops
            	{9,4,1, 13,4,1}, {13,4,2, 13,4,4}, {9,4,5, 13,4,5}, {9,4,2, 9,4,4}, 
            	// Ceiling
            	{10,5,2, 12,5,4}, {11,6,3, 11,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Left roof
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, 
        		
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, 
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, 
        		{5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, 
        		
        		{2,6,2, 3}, {3,6,2, 3}, {4,6,2, 3}, 
        		{2,6,3, 0}, 
        		{2,6,4, 2}, {3,6,4, 2}, {4,6,4, 2}, 
        		{4,6,3, 1}, 
        		
        		// Right roof
        		{9,4,0, 3}, {10,4,0, 3}, {11,4,0, 3}, {12,4,0, 3}, {13,4,0, 3}, 
        		{8,4,1, 0}, {8,4,2, 0}, {8,4,3, 0}, {8,4,4, 0}, {8,4,5, 0}, 
        		{9,4,6, 2}, {10,4,6, 2}, {11,4,6, 2}, {12,4,6, 2}, {13,4,6, 2}, 
        		{14,4,1, 1}, {14,4,2, 1}, {14,4,3, 1}, {14,4,4, 1}, {14,4,5, 1}, 
        		
        		{9,5,1, 3}, {10,5,1, 3}, {11,5,1, 3}, {12,5,1, 3}, {13,5,1, 3}, 
        		{9,5,2, 0}, {9,5,3, 0}, {9,5,4, 0}, 
        		{9,5,5, 2}, {10,5,5, 2}, {11,5,5, 2}, {12,5,5, 2}, {13,5,5, 2}, 
        		{13,5,2, 1}, {13,5,3, 1}, {13,5,4, 1}, 
        		
        		{10,6,2, 3}, {11,6,2, 3}, {12,6,2, 3}, 
        		{10,6,3, 0}, 
        		{10,6,4, 2}, {11,6,4, 2}, {12,6,4, 2}, 
        		{12,6,3, 1}, 
        		
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
                        
            // Moist Farmland with Wheat
            for(int[] uvw : new int[][]{
            	{5,0,5, 7}, {5,0,6, 7}, 
            	{6,0,5, 7}, {6,0,6, 7}, 
            	{7,0,5, 7}, 
            	{8,0,5, 7}, {8,0,6, 7}, 
            	{9,0,5, 7}, {9,0,6, 7}, 
            	})
            {
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvw[3]), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{7,0,6}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,3}, 
        		{3,2,5}, {11,2,5}, 
        		{13,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 1;
        	int chestW = 2;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Crafting Table
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 4,1,4, structureBB);
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 1}, 
            	{5,1,3, 3, 1, 0}, 
            	{9,1,3, 1, 1, 1}, 
            	{11,1,1, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{7,1,1, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
            	{12,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{8,1,0},
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
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.getYWithOffset(0);
            	
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, false);
            	
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
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{11, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,1,3, -1, 0},
	        			{11,1,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Medium House 2 --- //
    
    public static class SavannaMediumHouse2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"           ",
        		"      FFF  ",
        		"  F  FFFFF ",
        		"     FFFFFP",
        		"  FFFFFFFFP",
        		" FFFFFFFF P",
        		" FFFFFFFFP ",
        		" FFFFFFFPP ",
        		"  FFF PP   ",
        		"   PPFP    ",
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
    	
        public SavannaMediumHouse2() {}

        public SavannaMediumHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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

        public static SavannaMediumHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaMediumHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)9, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left House
            	
            	// Front wall
            	{2,1,1, 2,3,1}, {4,1,1, 4,3,1}, 
            	// Left
            	{1,1,2, 1,3,2}, {1,1,4, 1,3,4}, 
            	// Back
            	{2,1,5, 2,3,5}, {4,1,5, 4,3,5}, 
            	// Right
            	{5,1,2, 5,3,2}, {5,1,4, 5,3,4}, 
            	
            	// Right House
            	
            	// Front wall
            	{6,1,4, 6,3,4}, {8,1,4, 8,3,4}, 
            	// Left
            	{5,1,5, 5,3,5}, {5,1,7, 5,3,7}, 
            	// Back
            	{6,1,8, 6,3,8}, {8,1,8, 8,3,8}, 
            	// Right
            	{9,1,5, 9,3,5}, {9,1,7, 9,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	// Left house
            	{1,1,3, 1,1,3}, {1,3,3, 1,3,3}, 
            	{5,1,3, 5,1,3}, {5,3,3, 5,3,3}, 
            	{3,0,1, 3,0,1}, {3,3,1, 3,3,1}, 
            	{3,1,5, 3,1,5}, {3,3,5, 3,3,5}, 
            	// Right house
            	{5,1,6, 5,1,6}, {5,3,6, 5,3,6}, 
            	{9,0,6, 9,0,6}, {9,3,6, 9,3,6}, 
            	{7,1,4, 7,1,4}, {7,3,4, 7,3,4}, 
            	{7,1,8, 7,1,8}, {7,3,8, 7,3,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{3,3,4, 2}, {6,3,6, 1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left house
            	// Floor
            	{2,0,2, 4,0,4}, 
            	// Bench
            	{4,1,4, 4,1,4}, 
            	// Wall tops
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	
            	// Right house
            	// Floor
            	{6,0,5, 8,0,7}, 
            	// Wall tops
            	{5,4,6, 5,4,8}, {6,4,8, 8,4,8}, {9,4,4, 9,4,8}, {6,4,4, 8,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{2,5,2, 4,5,4}, 
            	{6,5,5, 8,5,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Left roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0},
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, 
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 0}, 
        		// Right
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, 
        		{5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, 
        		
        		// Right roof
        		// Front
        		{7,4,3, 3}, {8,4,3, 3}, {9,4,3, 3}, 
        		{6,5,4, 3}, {7,5,4, 3}, {8,5,4, 3}, {9,5,4, 3}, 
        		// Left
        		{4,4,7, 0}, {4,4,8, 0}, 
        		{5,5,6, 0}, {5,5,7, 0}, 
        		// Back
        		{5,4,9, 2}, {6,4,9, 2}, {7,4,9, 2}, {8,4,9, 2}, {9,4,9, 2}, 
        		{5,5,8, 2}, {6,5,8, 2}, {7,5,8, 2}, {8,5,8, 2}, {9,5,8, 2}, 
        		// Right
        		{10,4,4, 1}, {10,4,5, 1}, {10,4,6, 1}, {10,4,7, 1}, {10,4,8, 1}, 
        		{9,5,5, 1}, {9,5,6, 1}, {9,5,7, 1}, 
        		
        		// Interior
        		{4,1,2, 0}, {4,1,3, 0}, 
        		{8,1,5, 2}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
                        
            // Moist Farmland with Wheat
            for(int[] uvw : new int[][]{
            	{6,0,2, 0}, {7,0,2, 0}, {7,0,3, 1}, {8,0,3, 0}, 
            	})
            {
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvw[3]), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{6,0,3}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Left building
        		{1,2,3}, {3,2,5}, {5,2,3}, 
        		// Right building
        		{5,2,6}, {7,2,8}, {7,2,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 6;
        	int chestV = 1;
        	int chestW = 7;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Crafting Table
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 2,1,2, structureBB);
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            	{9,1,6, 3, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
            	{7,1,5, 1, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{2,1,7},
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
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.getYWithOffset(0);
            	
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, false);
            	
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
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{11, GROUND_LEVEL, 6}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,1,3, -1, 0},
	        			{8,1,7, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Shepherd House --- //
    
    public static class SavannaShepherd1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
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
        		" FFFFFFFFFF",
        		"  FFFPFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 13;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaShepherd1() {}

        public SavannaShepherd1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaShepherd1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaShepherd1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{4,1,1, 4,3,1}, {6,1,1, 6,3,1}, 
            	// Left wall
            	{3,1,2, 3,3,2}, {3,1,3, 3,1,3}, {3,1,4, 3,3,4}, 
            	// Back wall
            	{4,1,5, 4,3,5}, {6,1,5, 6,3,5}, 
            	// Right wall
            	{7,1,2, 7,3,2}, {7,1,3, 7,1,3}, {7,1,4, 7,3,4}, 
            	// Floor
            	{5,0,3, 5,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Front entrance
            	{5,0,1, 5,0,1}, 
            	// Left wall
            	{3,3,3, 3,3,3}, 
            	// Right wall
            	{7,3,3, 7,3,3}, 
            	// Back entrance
            	{5,0,5, 5,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Front entrance
            	{5,3,1, 5,3,1}, 
            	// Back entrance
            	{5,3,5, 5,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Backyard
            	{0,1,4, 2,1,4}, {8,1,4, 10,1,4}, 
            	{0,1,5, 0,1,11}, 
            	{10,1,5, 10,1,11}, 
            	{0,1,12, 10,1,12}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{4,2,0, 2}, {6,2,0, 2}, 
            	// Interior
            	{5,3,4, 2}, 
            	// Fence
            	{6,2,12, -1}, {10,2,12, -1}, {10,2,8, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{4,0,2, 6,0,2}, 
            	{4,0,3, 4,0,3}, 
            	{4,0,4, 6,0,4}, 
            	{6,0,3, 6,0,3}, 
            	// Wall tops
            	{3,4,1, 7,4,1}, 
            	{3,4,2, 3,4,4}, 
            	{3,4,5, 7,4,5}, 
            	{7,4,2, 7,4,4}, 
            	// Ceiling
            	{4,5,2, 6,5,4}, {5,6,3, 5,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, {6,4,0, 3}, {7,4,0, 3}, 
        		{3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, {6,5,1, 3}, {7,5,1, 3}, 
        		{4,6,2, 3}, {5,6,2, 3}, {6,6,2, 3}, 
        		// Left
        		{2,4,1, 0}, {2,4,2, 0}, {2,4,3, 0}, {2,4,4, 0}, {2,4,5, 0}, 
        		{3,5,2, 0}, {3,5,3, 0}, {3,5,4, 0}, 
        		{4,6,3, 0}, 
        		// Back
        		{3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, {6,4,6, 2}, {7,4,6, 2}, 
        		{3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, {6,5,5, 2}, {7,5,5, 2}, 
        		{4,6,4, 2}, {5,6,4, 2}, {6,6,4, 2}, 
        		// Right
        		{8,4,1, 1}, {8,4,2, 1}, {8,4,3, 1}, {8,4,4, 1}, {8,4,5, 1}, 
        		{7,5,2, 1}, {7,5,3, 1}, {7,5,4, 1}, 
        		{6,6,3, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{3,2,3}, {7,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Loom
        	IBlockState loomState = ModObjects.chooseModLoom();
            for(int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{6,1,3}, 
            	})
            {
            	this.setBlockState(world, loomState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	{6,1,2, 6,1,2}, 
            	{6,1,4, 6,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,1,1, 0, 1, 0}, 
            	{5,1,5, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{1,1,9, 0}, {1,1,10, 0}, 
            	{2,1,7, 0}, {2,1,8, 0}, {2,1,10, 0}, {2,1,11, 1},
            	{3,1,6, 0}, {3,1,7, 0}, 
            	{4,1,8, 0}, 
            	{5,1,7, 0}, {5,1,8, 0}, {5,1,11, 0}, 
            	{6,1,8, 0}, {6,1,11, 0}, 
            	{7,1,5, 0}, {7,1,6, 0}, {7,1,7, 0}, {7,1,8, 0}, {7,1,9, 0}, {7,1,10, 0}, 
            	{8,1,7, 0}, {8,1,8, 0}, {8,1,11, 0}, 
            	{9,1,5, 0}, {9,1,7, 0}, {9,1,8, 0}, {9,1,10, 0}, {9,1,11, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
            
            
            // Water with dirt underneath
            for(int[] uvw : new int[][]{
            	{2,0,9}, 
            	{3,0,8}, {3,0,9}, {3,0,10}, 
            	{4,0,9}, {4,0,10}, {4,0,11}, 
            	{5,0,10}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
        	// Tree
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SAPLING.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwss : new int[][]{
        		// u,v,w, ushift,wshift: Which adjacent spaces to use if this is a Dark Oak sapling
        		{8,1,10, -1,-1},
        		})
            {
        		// Place the sapling
        		this.setBlockState(world, biomeSaplingState, uvwss[0], uvwss[1], uvwss[2], structureBB);
        		
        		// Grow it into a tree
        		if (biomeSaplingState.getBlock() instanceof BlockSapling)
                {
        			if (biomeSaplingState.getBlock().getMetaFromState(biomeSaplingState)==5) // This is a dark oak. You need four to grow.
        			{
        				this.setBlockState(world, biomeSaplingState, uvwss[0]+uvwss[3], uvwss[1], uvwss[2], structureBB);
        				this.setBlockState(world, biomeSaplingState, uvwss[0], uvwss[1], uvwss[2]+uvwss[4], structureBB);
        				this.setBlockState(world, biomeSaplingState, uvwss[0]+uvwss[3], uvwss[1], uvwss[2]+uvwss[4], structureBB);
        			}
        			
        			((BlockSapling)biomeSaplingState.getBlock()).generateTree(world, new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2])), biomeSaplingState, world.rand);
                }
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 4 + random.nextInt(2);
            	int v = 1;
            	int w = 2 + random.nextInt(3);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 3, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);

                // Sheep in the yard
            	for (int[] uvw : new int[][]{
        			{4, 1, 7},
        			})
        		{
            		EntityLiving animal = new EntitySheep(world);
            		IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
            		
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
        		}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small Farm --- //
    
    public static class SavannaSmallFarm extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		" FFFFF",
        		"FFFFFF",
        		" FFFFF",
        		"FFFFFF",
        		"FFFFFF",
        		"FFFFFF",
        		"FFFFF ",
        		"FFFFF ",
        		"FFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaSmallFarm() {}

        public SavannaSmallFarm(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallFarm buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallFarm(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
                        
            // Moist Farmland
            for(int[] uvw : new int[][]{
            	{0,0,0, 7}, {0,0,1, 7}, {0,0,3, 7}, {0,0,4, 7}, 
            	{1,0,2, 0}, {1,0,3, 0}, {1,0,4, 0}, {1,0,6, 0}, 
            	{2,0,0, 1}, {2,0,1, 7}, {2,0,2, 7}, {2,0,3, 0}, {2,0,4, 1}, {2,0,5, 7}, {2,0,6, 7}, {2,0,7, 0}, {2,0,8, 0}, 
            	{3,0,0, 0}, {3,0,1, 1}, {3,0,3, 7}, {3,0,6, 0}, {3,0,7, 0}, {3,0,8, 0}, 
            	{4,0,2, 7}, {4,0,3, 7}, {4,0,4, 7}, {4,0,5, 5}, {4,0,6, 7}, {4,0,7, 7}, 
            	{5,0,3, 7}, {5,0,4, 7}, {5,0,5, 6}, {5,0,7, 5}, {5,0,8, 6}, 
            	})
            {
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvw[3]), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); 
            }
        	
                        
            // Dry Farmland
            for(int[] uvw : new int[][]{
            	{1,0,0, 0}, 
            	})
            {
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvw[3]), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Melon 
            for(int[] uvw : new int[][]{
            	{1,1,8}, 
            	})
            {
            	this.setBlockState(world, Blocks.MELON_BLOCK.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            }
            
            
            // Water with grass underneath
            for(int[] uvw : new int[][]{
            	{1,0,1}, 
            	{3,0,2}, 
            	{3,0,5}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            	this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, place a melon instead.
            IBlockState compostBinState = ModObjects.chooseModComposter();
            for(int[] uvw : new int[][]{
            	{1,1,5}, 
            	})
            {
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	if (compostBinState != null)
                {
                	this.setBlockState(world, compostBinState.getBlock().getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
                }
            	else
            	{
            		this.setBlockState(world, Blocks.MELON_BLOCK.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,2, 1}, {0,1,5, 1}, {0,1,7, 0}, 
            	{1,1,7, 1}, 
            	{3,1,4, 1}, 
            	{4,1,1, 1}, {4,1,8, 0}, 
            	{5,1,0, 1}, {5,1,6, 1}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 1;
            	int v = 1;
            	int w = 1;
            	
            	while (
            			(u==1 && w==1) // Water
            			|| (u==3 && w==2) // Water
            			|| (u==3 && w==5) // Water
            			|| (u==1 && w==8) // Melon
            			|| (u==1 && w==5) // Composter
            			|| (u==0 && w==8) // Gap
            			|| (u==0 && w==6) // Gap
            			|| (u==5 && w==1) // Gap
            			|| (u==5 && w==2) // Gap
            			)
            	{
            		u = random.nextInt(6);
            		w = random.nextInt(9);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 1 --- //
    
    public static class SavannaSmallHouse1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFPFFF",
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
    	
        public SavannaSmallHouse1() {}

        public SavannaSmallHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,1,1, 4,3,1, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Left wall
        		{1,1,2, 1,3,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Right wall
        		{5,1,2, 5,3,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Back wall
        		{2,1,5, 4,3,5, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	{2,0,2, 4,0,4}, 
            	{3,0,1, 3,0,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{3,3,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	// Ceiling
            	{2,5,2, 4,5,4}, {3,6,3, 3,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		{2,6,2, 3}, {3,6,2, 3}, {4,6,2, 3}, 
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, 
        		{2,6,4, 2}, {3,6,4, 2}, {4,6,4, 2}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, 
        		{2,6,3, 0}, 
        		// Right
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, 
        		{5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, 
        		{4,6,3, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,3}, 
        		{3,2,5}, 
        		{5,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Tables
            	{2,1,4, 2,1,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{2,2,4}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,3, 0}, {0,1,4, 1}, {0,1,6, 0}, 
            	{1,1,0, 0}, {1,1,1, 0}, {1,1,5, 0}, {1,1,6, 0}, 
            	{3,1,6, 0}, 
            	{4,1,0, 0}, {4,1,6, 0}, 
            	{5,1,5, 0}, {5,1,6, 0}, 
            	{6,1,2, 0}, {6,1,4, 0}, {6,1,6, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{2,1,0, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 2 --- //
    
    public static class SavannaSmallHouse2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFPFFF",
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
    	
        public SavannaSmallHouse2() {}

        public SavannaSmallHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,1,1, 4,3,1, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		// Left wall
        		{1,1,2, 1,3,4, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		// Right wall
        		{5,1,2, 5,3,4, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		// Back wall
        		{2,1,5, 4,3,5, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	{2,0,2, 4,0,4}, 
            	{3,0,1, 3,0,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{3,3,0, 2}, 
            	// Interior
            	{3,3,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	// Ceiling
            	{2,5,2, 4,5,4}, {3,6,3, 3,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		{2,6,2, 3}, {3,6,2, 3}, {4,6,2, 3}, 
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, 
        		{2,6,4, 2}, {3,6,4, 2}, {4,6,4, 2}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, 
        		{2,6,3, 0}, 
        		// Right
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, 
        		{5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, 
        		{4,6,3, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,3}, 
        		{3,2,5}, 
        		{5,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 1;
        	int chestW = 4;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,3, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, {0,1,6, 0}, 
            	{1,1,0, 0}, {1,1,1, 0}, {1,1,5, 0}, {1,1,6, 0}, 
            	{3,1,6, 0}, 
            	{4,1,6, 0}, 
            	{5,1,5, 0}, {5,1,6, 0}, 
            	{6,1,2, 0}, {6,1,4, 0}, {6,1,6, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{2,1,0, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 3 --- //
    
    public static class SavannaSmallHouse3 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFPFFF",
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
    	
        public SavannaSmallHouse3() {}

        public SavannaSmallHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,1,1, 4,3,1, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
        		// Left wall
        		{1,1,2, 1,3,4, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
        		// Right wall
        		{5,1,2, 5,3,4, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
        		// Back wall
        		{2,1,5, 4,3,5, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	{2,0,2, 4,0,4}, 
            	{3,0,1, 3,0,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{3,3,4, 2}, 
            	{2,3,2, 0}, {4,3,2, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	// Ceiling
            	{2,5,2, 4,5,4}, {3,6,3, 3,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		{2,6,2, 3}, {3,6,2, 3}, {4,6,2, 3}, 
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, 
        		{2,6,4, 2}, {3,6,4, 2}, {4,6,4, 2}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, 
        		{2,6,3, 0}, 
        		// Right
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, 
        		{5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, 
        		{4,6,3, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,3}, 
        		{3,2,5}, 
        		{5,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Crafting Table
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 4,1,4, structureBB);
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, {0,1,6, 0}, 
            	{1,1,1, 0}, {1,1,5, 0}, {1,1,6, 0}, 
            	{4,1,0, 0}, {4,1,6, 0}, 
            	{5,1,5, 0}, {5,1,6, 0}, 
            	{6,1,2, 0}, {6,1,4, 0}, {6,1,6, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{5,1,0, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 4 --- //
    
    public static class SavannaSmallHouse4 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFPFFF",
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
    	
        public SavannaSmallHouse4() {}

        public SavannaSmallHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{5,1,1, 5,4,1}, {7,1,1, 7,4,1}, 
            	// Left wall
            	{4,1,2, 4,4,2}, {4,1,4, 4,4,4}, 
            	// Back wall
            	{5,1,5, 5,4,5}, {7,1,5, 7,4,5}, 
            	// Right wall
            	{8,1,2, 8,4,2}, {8,1,4, 8,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{4,3,3, 4,3,3}, 
            	// Right wall
            	{8,1,3, 8,1,3}, {8,3,3, 8,3,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Front entrance
            	{6,3,1, 6,3,1}, 
            	// Back entrance
            	{6,1,5, 6,1,5}, {6,3,5, 6,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	{6,0,1, 6,0,1}, 
            	{4,0,3, 4,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Windows
            	{6,4,1, 6,4,1}, 
            	{6,4,5, 6,4,5}, 
            	{4,4,3, 4,4,3}, 
            	{8,4,3, 8,4,3}, 
            	// Yard awning
            	{1,1,1, 1,3,1}, {1,1,5, 1,3,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{6,3,0, 2}, 
            	{6,3,2, 0}, 
            	{6,3,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{5,0,2, 7,0,4}, 
            	// Wall tops
            	{4,5,1, 8,5,1}, {4,5,2, 4,5,4}, {4,5,5, 8,5,5}, {8,5,2, 8,5,4}, 
            	// Ceiling
            	{5,6,2, 7,6,4}, {6,7,3, 6,7,3}, 
            	// Awning posts
            	{1,4,1, 1,4,1}, {1,4,5, 1,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{4,5,0, 3}, {5,5,0, 3}, {6,5,0, 3}, {7,5,0, 3}, {8,5,0, 3}, 
        		{4,6,1, 3}, {5,6,1, 3}, {6,6,1, 3}, {7,6,1, 3}, {8,6,1, 3}, 
        		{5,7,2, 3}, {6,7,2, 3}, {7,7,2, 3}, 
        		// Back
        		{4,5,6, 2}, {5,5,6, 2}, {6,5,6, 2}, {7,5,6, 2}, {8,5,6, 2}, 
        		{4,6,5, 2}, {5,6,5, 2}, {6,6,5, 2}, {7,6,5, 2}, {8,6,5, 2}, 
        		{5,7,4, 2}, {6,7,4, 2}, {7,7,4, 2}, 
        		// Left
        		{3,5,1, 0}, {3,5,2, 0}, {3,5,3, 0}, {3,5,4, 0}, {3,5,5, 0}, 
        		{4,6,2, 0}, {4,6,3, 0}, {4,6,4, 0}, 
        		{5,7,3, 0}, 
        		// Right
        		{9,5,1, 1}, {9,5,2, 1}, {9,5,3, 1}, {9,5,4, 1}, {9,5,5, 1}, 
        		{8,6,2, 1}, {8,6,3, 1}, {8,6,4, 1}, 
        		{7,7,3, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,4,2, 1,4,4}, 
            	{2,4,1, 2,4,5}, 
            	{3,4,1, 3,4,1}, {3,4,5, 3,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{8,2,3}, 
        		{6,2,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
			// Solid color banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{1,4,0, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{2,4,0, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{3,4,0, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				
				{0,4,1, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{0,4,2, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{0,4,3, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{0,4,4, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{0,4,5, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				
				{1,4,6, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{2,4,6, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{3,4,6, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
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
				modifystanding.setBoolean("IsStanding", false);
				modifystanding.setInteger("Base", uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 7;
        	int chestV = 1;
        	int chestW = 2;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{7,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{6,1,1, 0, 1, 1}, 
            	{4,1,3, 1, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, {0,1,2, 0}, {0,1,4, 0}, {0,1,6, 0}, 
            	{1,1,4, 0}, 
            	{2,1,1, 0}, {2,1,3, 0}, 
            	{3,1,4, 0}, {3,1,5, 0}, 
            	{4,1,5, 0}, 
            	{5,1,6, 0}, 
            	{7,1,6, 0}, 
            	{8,1,5, 0}, {8,1,6, 0}, 
            	{9,1,0, 0}, {9,1,4, 0}, {9,1,5, 0}, {9,1,6, 1}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{6, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{5,1,4, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 5 --- //
    
    public static class SavannaSmallHouse5 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFPFFF",
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
    	
        public SavannaSmallHouse5() {}

        public SavannaSmallHouse5(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallHouse5 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallHouse5(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1, 2,3,1}, {4,1,1, 4,3,1}, 
            	// Back wall
            	{2,1,5, 2,3,5}, {4,1,5, 4,3,5}, 
            	// Left wall
            	{1,1,2, 1,3,2}, {1,1,4, 1,3,4}, 
            	// Right wall
            	{5,1,2, 5,3,2}, {5,1,4, 5,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,3, 1,1,3}, {1,3,3, 1,3,3}, 
            	// Right wall
            	{5,1,3, 5,1,3}, {5,3,3, 5,3,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Front entrance
            	{3,3,1, 3,3,1}, 
            	// Back entrance
            	{3,1,5, 3,1,5}, {3,3,5, 3,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,4,1, 4,4,1, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{2,5,2, 4,7,2, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Left wall
        		{1,4,2, 1,4,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{2,5,3, 2,7,3, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Right wall
        		{5,4,2, 5,4,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{4,5,3, 4,7,3, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Back wall
        		{2,4,5, 4,4,5, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{2,5,4, 4,7,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Windows
            	{3,6,2, 3,6,2}, 
            	{2,6,3, 2,6,3}, 
            	{3,6,4, 3,6,4}, 
            	{4,6,3, 4,6,3}, 
            	// Supports
            	{0,1,1, 0,4,1}, {0,1,5, 0,4,5}, 
            	{1,1,0, 1,4,0}, {5,1,0, 5,4,0}, 
            	{6,1,1, 6,4,1}, {6,1,5, 6,4,5}, 
            	{1,1,6, 1,4,6}, {5,1,6, 5,4,6}, 
            	// Table
            	{4,1,4, 4,1,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{4,2,4}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{3,3,0, 2}, 
            	{3,3,2, 0}, 
            	{3,3,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,2, 4,0,4}, {3,0,1, 3,0,1}, 
            	// Ceiling
            	{2,8,2, 4,8,4}, {3,9,3, 3,9,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,8,1, 3}, {2,8,1, 3}, {3,8,1, 3}, {4,8,1, 3}, {5,8,1, 3}, 
        		{2,9,2, 3}, {3,9,2, 3}, {4,9,2, 3}, 
        		// Back
        		{1,8,5, 2}, {2,8,5, 2}, {3,8,5, 2}, {4,8,5, 2}, {5,8,5, 2}, 
        		{2,9,4, 2}, {3,9,4, 2}, {4,9,4, 2}, 
        		// Left
        		{1,8,2, 0}, {1,8,3, 0}, {1,8,4, 0}, 
        		{2,9,3, 0}, 
        		// Right
        		{5,8,2, 1}, {5,8,3, 1}, {5,8,4, 1}, 
        		{4,9,3, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{0,5,1, 0,5,5}, 
            	{1,5,0, 1,5,6}, 
            	{2,5,0, 4,5,1}, {2,5,5, 4,5,6}, 
            	{5,5,0, 5,5,6}, 
            	{6,5,1, 6,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,3}, 
        		{3,2,5}, 
        		{5,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,4, 1, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, {0,1,2, 0}, {0,1,4, 0}, {0,1,6, 0}, 
            	{1,1,1, 0}, 
            	{2,1,0, 0}, {2,1,3, 0}, 
            	{4,1,0, 0}, 
            	{6,1,0, 0}, {6,1,3, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
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
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 6 --- //
    
    public static class SavannaSmallHouse6 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"   F   ",
        		"  FFF  ",
        		" FFFFF ",
        		" FFFFFF",
        		" FFFFF ",
        		"  FFF  ",
        		"   PF  ",
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
    	
        public SavannaSmallHouse6() {}

        public SavannaSmallHouse6(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallHouse6 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallHouse6(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1}, {2,3,1}, {4,1,1}, {4,3,1}, 
            	// Back wall
            	{2,1,5}, {2,3,5}, {4,1,5}, {4,3,5}, {3,1,5}, 
            	// Left wall
            	{1,1,2}, {1,3,2}, {1,1,4}, {1,3,4}, {1,1,3}, 
            	// Right wall
            	{5,1,2}, {5,3,2}, {5,1,4}, {5,3,4}, {5,1,3}, 
            	})
            {
            	this.setBlockState(world, biomeLogVertState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,2,1}, {4,2,1}, 
            	// Back wall
            	{2,2,5}, {4,2,5}, 
            	// Left wall
            	{1,3,3}, 
            	// Right wall
            	{5,3,3}, 
            	})
            {
            	this.setBlockState(world, biomeLogHorAlongState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{3,3,1}, 
            	// Back wall
            	{3,3,5}, 
            	// Left wall
            	{1,2,2}, {1,2,4}, 
            	// Right wall
            	{5,2,2}, {5,2,4}, 
            	})
            {
            	this.setBlockState(world, biomeLogHorAcrossState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	{2,1,3, 2,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{3,3,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,2, 4,0,4}, {3,0,1, 3,0,1}, 
            	// Wall tops
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	// Ceiling
            	{2,5,2, 4,5,4}, {3,6,3, 3,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		{2,6,2, 3}, {3,6,2, 3}, {4,6,2, 3}, 
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, 
        		{2,6,4, 2}, {3,6,4, 2}, {4,6,4, 2}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, 
        		{2,6,3, 0}, 
        		// Right
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, 
        		{5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, 
        		{4,6,3, 1}, 
        		// Chairs
        		{2,1,2, 2}, {2,1,4, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,3}, 
        		{3,2,5}, 
        		{5,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{4,1,0, 0}, {6,1,3, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,1,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 7 --- //
    
    public static class SavannaSmallHouse7 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFPFFF",
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
    	
        public SavannaSmallHouse7() {}

        public SavannaSmallHouse7(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallHouse7 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallHouse7(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1, 2,3,1}, {4,1,1, 4,3,1}, 
            	// Left wall
            	{1,1,2, 1,3,2}, {1,1,4, 1,3,4}, 
            	// Back wall
            	{2,1,5, 2,3,5}, {4,1,5, 4,3,5}, 
            	// Right wall
            	{5,1,2, 5,3,2}, {5,1,4, 5,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	// Walls
            	// Front wall
            	{3,3,1, 3,3,1}, 
            	// Back wall
            	{3,1,5, 3,1,5}, {3,3,5, 3,3,5}, 
            	// Left wall
            	{1,1,3, 1,1,3}, {1,3,3, 1,3,3}, 
            	// Right wall
            	{5,1,3, 5,1,3}, {5,3,3, 5,3,3}, 
            	// Floors
            	{2,0,2, 4,0,4}, {3,0,1, 3,0,1}, 
            	// Table
            	{4,1,3, 4,1,3},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{3,3,0, 2}, 
            	// Interior
            	{4,2,3, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,4}, {1,4,5, 5,4,5}, {5,4,2, 5,4,4}, 
            	// Ceiling
            	{2,5,2, 4,5,4}, {3,6,3, 3,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		{2,6,2, 3}, {3,6,2, 3}, {4,6,2, 3}, 
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, 
        		{2,6,4, 2}, {3,6,4, 2}, {4,6,4, 2}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, 
        		{2,6,3, 0}, 
        		// Right
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, 
        		{5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, 
        		{4,6,3, 1}, 
        		// Chairs
        		{4,1,2, 2}, {4,1,4, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,3}, 
        		{3,2,5}, 
        		{5,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 1;
        	int chestW = 2;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, {0,1,3, 0}, {0,1,6, 0}, 
            	{1,1,0, 0}, {1,1,1, 0}, {1,1,5, 0}, {1,1,6, 0}, 
            	{4,1,0, 0}, 
            	{5,1,5, 0}, {5,1,6, 0}, 
            	{6,1,4, 0}, {6,1,6, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{5,1,0, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 8 --- //
    
    public static class SavannaSmallHouse8 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"       ",
        		"  FFF  ",
        		" FFFFF ",
        		" FFFFF ",
        		"  FFF  ",
        		"   P   ",
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
    	
        public SavannaSmallHouse8() {}

        public SavannaSmallHouse8(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaSmallHouse8 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaSmallHouse8(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,2,1, 2,2,1}, {4,2,1, 4,2,1}, 
            	// Left wall
            	{1,2,2, 1,2,3},  
            	// Back wall
            	{2,2,4, 2,2,4}, {4,2,4, 4,2,4}, 
            	// Right wall
            	{5,2,2, 5,2,3}, 
            	// Floor
            	{3,0,2, 3,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,2, 2,0,3}, {4,0,2, 4,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,1,1, 2,1,1, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{4,1,1, 4,1,1, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{2,3,1, 4,3,1, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Left wall
        		{1,1,2, 1,1,3, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{1,3,2, 1,3,3, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Right wall
        		{5,1,2, 5,1,3, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{5,3,2, 5,3,3, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Back wall
        		{2,1,4, 4,1,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		{2,3,4, 4,3,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{3,3,0, 2}, 
            	// Interior
            	{3,3,2, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{1,4,1, 5,4,1}, {1,4,2, 1,4,3}, {1,4,4, 5,4,4}, {5,4,2, 5,4,3}, 
            	// Ceiling
            	{2,5,2, 4,5,3},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{1,5,1, 3}, {2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, 
        		// Back
        		{1,4,5, 2}, {2,4,5, 2}, {3,4,5, 2}, {4,4,5, 2}, {5,4,5, 2}, 
        		{1,5,4, 2}, {2,5,4, 2}, {3,5,4, 2}, {4,5,4, 2}, {5,5,4, 2}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, 
        		{1,5,2, 0}, {1,5,3, 0}, 
        		// Right
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, 
        		{5,5,2, 1}, {5,5,3, 1}, 
        		// Bench
        		{4,1,2, 0}, {4,1,3, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{3,2,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,2, 2, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.getCoordBaseMode(), isHead),
                			uvwoc[4]);
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,1,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(entityvillager);
	        		}
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Tannery --- //
    
    public static class SavannaTannery1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFPFFFF",
        		"FFFFPFFFF",
        		"PPPPFFPPP",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaTannery1() {}

        public SavannaTannery1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaTannery1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaTannery1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,0,3, 5,0,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{4,0,2, 4,0,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,3, 2,0,4}, {6,0,3, 6,0,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front wall
        		{2,1,2, 6,3,2, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Left wall
        		{1,1,3, 1,3,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Right wall
        		{7,1,3, 7,3,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		// Back wall
        		{2,1,5, 6,3,5, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{4,3,1, 2}, 
            	// Interior
            	{4,3,3, 0}, {4,3,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{2,4,2, 6,4,2}, {1,4,3, 1,4,4}, {7,4,3, 7,4,4}, {2,4,5, 6,4,5}, 
            	// Ceiling
            	{2,5,3, 6,5,4}, 
            	// Awning
            	{3,3,0, 3,3,0}, {5,3,0, 5,3,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,4,1, 3}, {2,4,1, 3}, {3,4,1, 3}, {4,4,1, 3}, {5,4,1, 3}, {6,4,1, 3}, {7,4,1, 3}, 
        		{1,5,2, 3}, {2,5,2, 3}, {3,5,2, 3}, {4,5,2, 3}, {5,5,2, 3}, {6,5,2, 3}, {7,5,2, 3}, 
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, {6,4,6, 2}, {7,4,6, 2}, 
        		{1,5,5, 2}, {2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, {6,5,5, 2}, {7,5,5, 2}, 
        		// Left
        		{0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		{1,5,3, 0}, {1,5,4, 0}, 
        		// Right
        		{8,4,2, 1}, {8,4,3, 1}, {8,4,4, 1}, {8,4,5, 1}, 
        		{7,5,3, 1}, {7,5,4, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Awning
            	{3,1,0, 3,2,0}, {5,1,0, 5,2,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{3,3,1, 3,3,1}, 
            	{4,3,0, 4,3,0}, 
            	{5,3,1, 5,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
        	
        	
			// Banners (solid color)
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				{3,3,-1, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{5,3,-1, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
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
				modifystanding.setBoolean("IsStanding", false);
				modifystanding.setInteger("Base", uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{3,2,5}, {5,2,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Cauldron
        	for (int[] uuvvww : new int[][]{
        		{6,1,3}, 
        		{6,1,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.CAULDRON.getStateFromMeta(3), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 1;
        	int chestW = 4;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_tannery");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Smooth Stone Block
        	IBlockState smoothStoneState = ModObjects.chooseModSmoothStoneBlockState();
            for (int[] uuvvww : new int[][]{
            	{2,1,3, 2,1,3},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], smoothStoneState, smoothStoneState, false);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,2, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
        	// Grass block
            for(int[] uvw : new int[][]{
            	{5,0,-1}, 
            	})
            {
    			this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, {0,1,2, 0}, {0,1,4, 0}, 
            	{1,1,0, 0}, {1,1,2, 0}, 
            	{2,1,0, 0}, {2,1,6, 0}, 
            	{3,1,6, 0}, 
            	{4,1,6, 0}, 
            	{5,1,-1, 0}, {5,1,6, 0}, 
            	{6,1,6, 0}, 
            	{7,1,0, 0}, {7,1,6, 0}, 
            	{8,1,0, 0}, {8,1,1, 0}, {8,1,3, 0}, {8,1,4, 0}, {8,1,5, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(3);
            	int v = 1;
            	int w = 3+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 2, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    
    // --- Temple 1 --- //
    
    public static class SavannaTemple1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFFFFFF",
        		"FFFFPFFFF",
        		"FFFPPPFFF",
        		"FFFPFPFFF",
        		"FFFPPPFFF",
        		"FFFFPFFFF",
        		"FFFFPFFFF",
        		"FFFFPFFFF",
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
    	
        public SavannaTemple1() {}

        public SavannaTemple1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaTemple1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaTemple1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,7, 6,3,7}, 
            	// Back wall
            	{2,1,11, 2,4,11}, {3,1,11, 3,1,11}, {4,1,11, 4,4,11}, {5,1,11, 5,1,11}, {6,1,11, 6,4,11}, 
            	// Right wall
            	{1,1,8, 1,4,8}, {1,1,9, 1,1,9}, {1,1,10, 1,4,10}, 
            	// Left wall
            	{7,1,8, 7,4,8}, {7,1,9, 7,1,9}, {7,1,10, 7,4,10}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{4,0,7, 4,0,7}, {2,4,7, 6,4,7}, 
            	// Right wall
            	{1,4,9, 1,4,9}, 
            	// Left wall
            	{7,4,9, 7,4,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Back wall
            	{3,4,11, 3,4,11}, {5,4,11, 5,4,11}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	// Table
            	{6,1,9, 6,1,9}, 
            	// Altar
            	{2,1,8, 2,1,10}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
            		{0,1,4, 0,2,4, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
            		{0,3,4, 0,3,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		
            		{2,1,1, 2,2,1, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
            		{2,3,1, 2,3,1, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
            		
            		{6,1,1, 6,2,1, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
            		{6,3,1, 6,3,1, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		
            		{8,1,4, 8,2,4, GeneralConfig.useVillageColors ? this.townColor2 : 1}, // Orange
            		{8,3,4, 8,3,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{4,3,8, 0}, 
            	{3,4,10, 2}, {5,4,10, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{1,5,7, 7,5,7}, {1,5,11, 7,5,11}, {1,5,8, 1,5,10}, {7,5,8, 7,5,10}, 
            	// Ceiling
            	{2,6,8, 6,6,10}, 
            	// Plank
            	{2,0,8, 6,0,10}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{1,5,6, 3}, {2,5,6, 3}, {3,5,6, 3}, {4,5,6, 3}, {5,5,6, 3}, {6,5,6, 3}, {7,5,6, 3}, 
        		{1,6,7, 3}, {2,6,7, 3}, {3,6,7, 3}, {4,6,7, 3}, {5,6,7, 3}, {6,6,7, 3}, {7,6,7, 3}, 
        		// Back
        		{1,5,12, 2}, {2,5,12, 2}, {3,5,12, 2}, {4,5,12, 2}, {5,5,12, 2}, {6,5,12, 2}, {7,5,12, 2}, 
        		{1,6,11, 2}, {2,6,11, 2}, {3,6,11, 2}, {4,6,11, 2}, {5,6,11, 2}, {6,6,11, 2}, {7,6,11, 2}, 
        		// Left
        		{0,5,7, 0}, {0,5,8, 0}, {0,5,9, 0}, {0,5,10, 0}, {0,5,11, 0}, 
        		{1,6,8, 0}, {1,6,9, 0}, {1,6,10, 0}, 
        		// Right
        		{8,5,7, 1}, {8,5,8, 1}, {8,5,9, 1}, {8,5,10, 1}, {8,5,11, 1}, 
        		{7,6,8, 1}, {7,6,9, 1}, {7,6,10, 1}, 
        		// Benches
        		{6,1,8, 0}, {6,1,10, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Stained Glass Windows
            for (int[] uvw : new int[][]{
            	// Left
            	{1,2,9}, 
            	// Right
            	{7,2,9}, 
            	// Back
            	{3,2,11}, {5,2,11}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.setBlockState(world, Blocks.STAINED_GLASS_PANE.getStateFromMeta(
        					i==1 
        						? (GeneralConfig.useVillageColors? this.townColor  : 4)  // Yellow (Top pane)
        						: (GeneralConfig.useVillageColors? this.townColor2 : 1)), // Orange (Bottom pane)
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		// Altar
        		{2,2,8, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{2,2,9, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{6,2,9, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
        	
            // Brewing Stand
        	for (int[] uvw : new int[][]{
        		{2,2,10}, 
        		})
            {
        		this.setBlockState(world, Blocks.BREWING_STAND.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,7, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
    		
    		
        	// Patterned banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{2,3,0, 2, 12}, // Brown
				{6,3,0, 2, 12}, // Brown
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
				modifystanding.setBoolean("IsStanding", false);
				
				if (GeneralConfig.useVillageColors)
				{
	            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
	            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
	            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
	            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
					
    				tilebanner.readFromNBT(modifystanding);
    				ItemStack villageBanner = new ItemStack(Items.BANNER);
    				villageBanner.setTagInfo("BlockEntityTag", villageNBTtag.getCompoundTag("BlockEntityTag"));
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
				}
				else
				{
					modifystanding.setInteger("Base", uvwoc[4]);
    				tilebanner.readFromNBT(modifystanding);
				}
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,1, 0}, {0,1,3, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, {0,1,8, 0}, {0,1,9, 0}, {0,1,11, 0}, 
            	{1,1,0, 0}, {1,1,2, 0}, {1,1,3, 0}, {1,1,5, 0}, {1,1,7, 0}, {1,1,12, 0}, 
            	{2,1,2, 0}, {2,1,4, 0}, {2,1,12, 0}, 
            	{3,1,0, 0}, {3,1,2, 0}, {3,1,6, 0}, 
            	{4,1,12, 0}, 
            	{5,1,0, 0}, {5,1,6, 0}, {5,1,12, 0}, 
            	{6,1,3, 0}, {6,1,12, 0}, 
            	{7,1,1, 0}, {7,1,2, 0}, {7,1,3, 0}, {7,1,5, 0}, {7,1,7, 0}, {7,1,12, 0}, {7,1,11, 0}, 
            	{8,1,1, 0}, {8,1,3, 0}, {8,1,6, 0}, {8,1,8, 0}, {8,1,12, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(3);
            	int v = 1;
            	int w = 8+random.nextInt(3);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 2, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 2;}
    }
    
    
    
    // --- Temple 2 --- //
    
    public static class SavannaTemple2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"         ",
        		"  FFFFF  ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		"  FFFFF  ",
        		"    P    ",
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
    	
        public SavannaTemple2() {}

        public SavannaTemple2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaTemple2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaTemple2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1, 3,3,1}, {5,1,1, 6,3,1}, {3,5,1, 5,5,1}, 
            	// Back wall
            	{2,1,5, 2,3,5}, {4,1,5, 4,3,5}, {6,1,5, 6,3,5}, {3,5,5, 5,5,5}, 
            	// Right wall
            	{1,1,2, 1,3,2}, {1,1,4, 1,3,4}, {3,5,2, 3,5,4}, 
            	// Left wall
            	{7,1,2, 7,3,2}, {7,1,4, 7,3,4}, {5,5,2, 5,5,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{4,0,1, 4,0,1}, 
            	// Right wall
            	{1,1,3, 1,1,3}, {1,3,3, 1,3,3}, 
            	// Left wall
            	{7,1,3, 7,1,3}, {7,3,3, 7,3,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{4,3,1, 4,3,1}, 
            	// Back wall
            	{3,1,5, 3,1,5}, {3,3,5, 3,3,5}, {5,1,5, 5,1,5}, {5,3,5, 5,3,5}, 
            	// Floor
            	{2,0,2, 3,0,4}, {5,0,2, 6,0,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
        	
        	
            // Stained Terracotta
        	for (int[] uuvvwwc : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Floor
            	{4,0,2, 4,0,4, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
            	// Decoration
            	{3,4,1, 3,4,1, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
            	{4,4,1, 4,4,1, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
            	{5,4,1, 5,4,1, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
            	{5,4,2, 5,4,2, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
            	{5,4,3, 5,4,3, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
            	{5,4,4, 5,4,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
            	{5,4,5, 5,4,5, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
            	{4,4,5, 4,4,5, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
            	{3,4,5, 3,4,5, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
            	{3,4,4, 3,4,4, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
            	{3,4,3, 3,4,3, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
            	{3,4,2, 3,4,2, GeneralConfig.useVillageColors ? this.townColor  : 4}, // Yellow
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwc[0], uuvvwwc[1], uuvvwwc[2], uuvvwwc[3], uuvvwwc[4], uuvvwwc[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uuvvwwc[6]), false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{4,4,0, 2}, {4,4,6, 0}, 
            	// Interior
            	{3,2,2, 0}, {5,2,2, 0}, 
            	{4,2,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left roof
            	{1,4,1, 2,4,5}, 
            	// Center roof
            	{3,6,1, 5,6,5}, 
            	// Right roof
            	{6,4,1, 7,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Left roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, 
        		// Left
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, 
        		// Back
        		{1,4,6, 2}, {2,4,6, 2}, 
        		
        		// Center roof
        		// Front
        		{3,6,0, 3}, {4,6,0, 3}, {5,6,0, 3}, 
        		// Back
        		{3,6,6, 2}, {4,6,6, 2}, {5,6,6, 2}, 
        		// Left
        		{2,6,1, 0}, {2,6,2, 0}, {2,6,3, 0}, {2,6,4, 0}, {2,6,5, 0}, 
        		// Right
        		{6,6,1, 1}, {6,6,2, 1}, {6,6,3, 1}, {6,6,4, 1}, {6,6,5, 1}, 
        		
        		// Right roof
        		// Front
        		{6,4,0, 3}, {7,4,0, 3}, 
        		// Right
        		{8,4,1, 1}, {8,4,2, 1}, {8,4,3, 1}, {8,4,4, 1}, {8,4,5, 1}, 
        		// Back
        		{6,4,6, 2}, {7,4,6, 2}, 
        		
        		// Bench
        		{6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,3}, {3,2,5}, {5,2,5}, {7,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Brewing Stand
        	for (int[] uvw : new int[][]{
        		{2,1,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.BREWING_STAND.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,1, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(3);
            	int v = 1;
            	int w = 2+random.nextInt(3);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 2, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 2;}
    }
    
    
    
    // --- Tool Smithy --- //
    
    public static class SavannaToolSmith1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFPFFFFF",
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
    	
        public SavannaToolSmith1() {}

        public SavannaToolSmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaToolSmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaToolSmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{4,1,1, 4,3,1}, {6,1,1, 6,3,1}, 
            	// Back wall
            	{4,1,5, 4,3,5}, {5,1,5, 5,1,5}, {6,1,5, 6,3,5}, 
            	// Right wall
            	{7,1,2, 7,3,2}, {7,1,4, 7,3,4}, 
            	// Left wall
            	{3,1,2, 3,3,2}, {3,1,4, 3,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Right wall
            	{7,3,3, 7,3,3}, 
            	// Left wall
            	{3,3,3, 3,3,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{5,3,1, 5,3,1}, 
            	// Back wall
            	{5,3,5, 5,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{5,3,0, 2}, 
            	// Interior
            	{5,3,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{5,0,1, 5,0,1}, {4,0,2, 6,0,2}, {3,0,3, 7,0,3}, {4,0,4, 6,0,4}, 
            	// Wall tops
            	{3,4,1, 7,4,1}, {3,4,5, 7,4,5}, {3,4,2, 3,4,4}, {7,4,2, 7,4,4}, 
            	// Awnings
            	{1,4,2, 2,4,2}, {1,4,2, 1,4,3}, {1,4,4, 2,4,4}, 
            	{8,4,2, 9,4,2}, {9,4,2, 9,4,3}, {8,4,4, 9,4,4}, 
            	// Roof
            	{4,5,2, 6,5,4}, {5,6,3, 5,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front
        		{3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, {6,4,0, 3}, {7,4,0, 3}, 
        		{3,5,1, 3}, {4,5,1, 3}, {5,5,1, 3}, {6,5,1, 3}, {7,5,1, 3}, 
        		{4,6,2, 3}, {5,6,2, 3}, {6,6,2, 3}, 
        		// Left
        		{3,5,2, 0}, {3,5,3, 0}, {3,5,4, 0}, 
        		{4,6,3, 0}, 
        		// Front
        		{3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, {6,4,6, 2}, {7,4,6, 2}, 
        		{3,5,5, 2}, {4,5,5, 2}, {5,5,5, 2}, {6,5,5, 2}, {7,5,5, 2}, 
        		{4,6,4, 2}, {5,6,4, 2}, {6,6,4, 2}, 
        		// Right
        		{7,5,2, 1}, {7,5,3, 1}, {7,5,4, 1}, 
        		{6,6,3, 1}, 
        		// Benches
        		{4,1,4, 3}, {6,1,4, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{2,4,3, 2,4,3}, {8,4,3, 8,4,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Awning
            	// Left
            	{1,1,2, 1,3,2}, {1,1,4, 1,3,4}, 
            	// Right
            	{9,1,2, 9,3,2}, {9,1,4, 9,3,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	
			// Banners (solid color)
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				// Left wing
				// Front
				{1,4,1, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{2,4,1, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				// Side
				{0,4,2, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{0,4,3, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{0,4,4, 3, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				// Back
				{1,4,5, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{2,4,5, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				
				// Right wing
				// Front
				{8,4,1, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{9,4,1, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				// Side
				{10,4,2, 1, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{10,4,3, 1, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{10,4,4, 1, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				// Back
				{8,4,5, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{9,4,5, 0, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown

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
				modifystanding.setBoolean("IsStanding", false);
				modifystanding.setInteger("Base", uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{5,2,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Smithing table
        	IBlockState smithingTableBlockState = ModObjects.chooseModSmithingTable();
        	for (int[] uvw : new int[][]{
        		{5,1,4}, 
        		})
            {
        		this.setBlockState(world, smithingTableBlockState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,1,1, 0, 1, 0}, 
            	{3,1,3, 1, 1, 1}, 
            	{7,1,3, 3, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{0,1,0, 0}, 
            	{1,1,3, 0}, 
            	{2,1,4, 0}, {2,1,5, 0}, 
            	{3,1,0, 0}, {3,1,1, 0}, 
            	{4,1,6, 0}, 
            	{5,1,6, 0}, 
            	{6,1,6, 0}, 
            	{7,1,0, 0}, {7,1,1, 0}, {7,1,5, 0}, {7,1,6, 0}, 
            	{8,1,0, 0}, {8,1,2, 0}, {8,1,3, 0}, {8,1,4, 0}, {8,1,5, 0}, 
            	{9,1,0, 0}, {9,1,1, 0}, {9,1,5, 0}, 
            	{10,1,1, 0}, {10,1,3, 0}, {10,1,4, 0}, {10,1,5, 0}, {10,1,6, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 4+random.nextInt(3);
            	int v = 1;
            	int w = 2+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 3, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // --- Small Weapon Smithy --- //
    
    public static class SavannaWeaponsmith1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"  FFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		"FFFFPFFFF",
        		"FFFFPFFFF",
        		"FFFFPFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaWeaponsmith1() {}

        public SavannaWeaponsmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaWeaponsmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaWeaponsmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,3, 3,3,3}, {5,1,3, 6,3,3}, 
            	// Back wall
            	{2,1,6, 2,3,6}, {3,1,6, 3,1,6}, {4,1,6, 4,3,6}, {5,1,6, 5,1,6}, {6,1,6, 6,3,6}, 
            	// Left wall
            	{1,1,4, 1,3,5}, 
            	// Right wall
            	{7,1,4, 7,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{4,3,3, 4,3,3}, 
            	// Back wall
            	{3,3,6, 3,3,6}, {5,3,6, 5,3,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
        	// TODO - stripped wood
        	// Stripped Log (Vertical)
        	IBlockState biomeStrippedLogVertState = biomeLogVertState;
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogVertState.getBlock()==Blocks.LOG || biomeStrippedLogVertState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,4, 2,0,5}, 
            	{4,0,3, 4,0,5}, 
            	{6,0,4, 6,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
        	// Stripped Log (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
        	IBlockState biomeStrippedLogHorizAlongState = biomeLogHorAlongState;
        	
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogHorizAlongState.getBlock()==Blocks.LOG || biomeStrippedLogHorizAlongState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedLogHorizAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 2);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedLogHorizAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 2);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,0,4, 3,0,5}, 
            	{5,0,4, 5,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
        	
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{3,3,5, 2}, 
            	// Interior
            	{5,3,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{1,4,3, 7,4,3}, {1,4,4, 1,4,5}, {1,4,6, 7,4,6}, {7,4,4, 7,4,5}, 
            	// Roof
            	{2,5,4, 6,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front
        		{1,4,2, 3}, {2,4,2, 3}, {3,4,2, 3}, {4,4,2, 3}, {5,4,2, 3}, {6,4,2, 3}, {7,4,2, 3}, 
        		{1,5,3, 3}, {2,5,3, 3}, {3,5,3, 3}, {4,5,3, 3}, {5,5,3, 3}, {6,5,3, 3}, {7,5,3, 3}, 
        		// Back
        		{1,4,7, 2}, {2,4,7, 2}, {3,4,7, 2}, {4,4,7, 2}, {5,4,7, 2}, {6,4,7, 2}, {7,4,7, 2}, 
        		{1,5,6, 2}, {2,5,6, 2}, {3,5,6, 2}, {4,5,6, 2}, {5,5,6, 2}, {6,5,6, 2}, {7,5,6, 2}, 
        		// Left
        		{0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, {0,4,6, 0}, 
        		{1,5,4, 0}, {1,5,5, 0}, 
        		// Right
        		{8,4,3, 1}, {8,4,4, 1}, {8,4,5, 1}, {8,4,6, 1}, 
        		{7,5,4, 1}, {7,5,5, 1}, 
        		// Chair
        		{6,1,4, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Front fence
            	{2,1,0, 2,1,2}, {6,1,0, 6,1,2}, 
            	{3,1,0, 3,1,0}, {5,1,0, 5,1,0}, 
            	// Table
            	{6,1,5, 6,1,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		// Desktop
        		{6,2,5, (GeneralConfig.useVillageColors ? this.townColor5 : 0)}, // White
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
			// Banners (solid color)
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				{3,3,2, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
				{5,3,2, 2, GeneralConfig.useVillageColors ? this.townColor3 : 12}, // Brown
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
				modifystanding.setBoolean("IsStanding", false);
				modifystanding.setInteger("Base", uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{3,2,6}, {5,2,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Grindstone
        	for (int[] uvwo : new int[][]{
        		// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		{2,1,5, 1}, 
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		IBlockState biomeGrindstoneState = ModObjects.chooseModGrindstone(uvwo[3], this.getCoordBaseMode());
            	
        		this.setBlockState(world, biomeGrindstoneState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,3, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{1,1,2, 0}, 
            	{3,1,1, 0}, {3,1,2, 0}, 
            	{5,1,2, 0}, 
            	{7,1,2, 0}, {7,1,3, 0}, 
            	{8,1,0, 0}, {8,1,2, 0}, 
            })
            {
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(3);
            	int v = 1;
            	int w = 4+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 2, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // --- Large Weapon Smithy --- //
    
    public static class SavannaWeaponsmith2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"             ",
        		"  FFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		"  FFFFFFFFFF ",
        		"  FFFFFFFFFF ",
        		"  FFFFFFFFFF ",
        		"    FFFFFFF  ",
        		"    FFFFF    ",
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
    	
        public SavannaWeaponsmith2() {}

        public SavannaWeaponsmith2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaWeaponsmith2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaWeaponsmith2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Wood with bark on all sides
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	{1,1,5, 1,2,5}, {1,1,6, 1,4,6}, 
            	{2,1,7, 10,4,7}, 
            	{11,1,2, 11,4,6}, 
            	{9,1,1, 9,4,1}, 
            	{7,1,4, 7,4,4}, 
            	{6,1,4, 6,1,4}, 
            	{5,1,4, 5,2,4}, 
           	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{6,4,4, 6,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
        	
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{6,4,3, 2}, {11,3,1, 2}, 
            	// Interior
            	{4,4,6, 2}, {8,4,6, 2}, {9,4,2, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wood with bark on all sides, part 2
            for (int[] uw : new int[][]{
            	{8,1,2, 8,4,3}, 
            	{10,1,1, 10,4,1}, 
           	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Wall tops
            	{1,5,6, 1,5,7}, {2,5,7, 10,5,7}, {11,5,1, 11,5,7}, {9,5,1, 10,5,1}, {8,5,1, 8,5,4}, {6,5,4, 7,5,4}, 
            	// Roof
            	{2,6,5, 10,6,6}, {9,6,2, 10,6,4}, 
            	// Floor
            	{5,1,5, 10,1,6}, {8,1,4, 10,1,4}, {9,1,2, 10,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front
        		{8,5,0, 3}, {9,5,0, 3}, {10,5,0, 3}, {11,5,0, 3}, 
        		{8,6,1, 3}, {9,6,1, 3}, {10,6,1, 3}, {11,6,1, 3}, 
        		{6,5,3, 3}, {7,5,3, 3},
        		{1,6,4, 3}, {2,6,4, 3}, {3,6,4, 3}, {4,6,4, 3}, {5,6,4, 3}, {6,6,4, 3}, {7,6,4, 3}, {8,6,4, 3}, 
        		// Back
        		{1,5,8, 2}, {2,5,8, 2}, {3,5,8, 2}, {4,5,8, 2}, {5,5,8, 2}, {6,5,8, 2}, {7,5,8, 2}, {8,5,8, 2}, {9,5,8, 2}, {10,5,8, 2}, {11,5,8, 2}, 
        		{1,6,7, 2}, {2,6,7, 2}, {3,6,7, 2}, {4,6,7, 2}, {5,6,7, 2}, {6,6,7, 2}, {7,6,7, 2}, {8,6,7, 2}, {9,6,7, 2}, {10,6,7, 2}, {11,6,7, 2}, 
        		// Left
        		{0,5,6, 0}, {0,5,7, 0}, 
        		{1,6,5, 0}, {1,6,6, 0}, 
        		{7,5,1, 0}, {7,5,2, 0}, 
        		{8,6,2, 0}, {8,6,3, 0}, 
        		// Right
        		{12,5,1, 1}, {12,5,2, 1}, {12,5,3, 1}, {12,5,4, 1}, {12,5,5, 1}, {12,5,6, 1}, {12,5,7, 1}, 
        		{11,6,2, 1}, {11,6,3, 1}, {11,6,4, 1}, {11,6,5, 1}, {11,6,6, 1}, 
        		// Chair
        		{8,2,6, 1}, {10,2,6, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Table
            	{9,2,6, 9,2,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{9,3,6}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{4,3,7}, {8,3,7}, 
        		{11,3,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Smooth Stone Block
        	IBlockState smoothStoneState = ModObjects.chooseModSmoothStoneBlockState();
            for (int[] uuvvww : new int[][]{
            	// Entryway
            	{5,1,1, 7,1,3}, 
            	// Basin
            	{2,1,2, 4,2,4}, 
            	// Basin back wall
            	{2,3,4, 5,5,4}, {1,3,5, 1,5,5}, {1,5,4, 1,5,4}, 
            	// Interior grindstone base
            	{2,1,5, 4,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], smoothStoneState, smoothStoneState, false);
            }
            
            
            // Smooth Stone Slab (Bottom)
            for(int[] uuvvww : new int[][]{
            	// Roof trim
            	{0,5,4, 0,5,5}, {1,5,3, 5,5,3}, {2,5,2, 4,5,2}, 
            	// Entryway
            	{4,1,1, 4,1,1}, {8,1,1, 8,1,1}, 
            	{4,1,0, 8,1,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.STONE_SLAB.getStateFromMeta(0), Blocks.STONE_SLAB.getStateFromMeta(0), false);
            }
        	
        	
        	// Lava
            this.fillWithBlocks(world, structureBB, 3,2,3, 3,2,3, Blocks.LAVA.getDefaultState(), Blocks.LAVA.getDefaultState(), false);
        	
            
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	{2,3,2, 2,4,3}, {3,3,2, 4,4,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            }
            
            
            // Grindstones
        	for (int[] uvwo : new int[][]{
        		// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		{6,2,2, 2}, 
        		{3,2,5, 0}, 
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		IBlockState biomeGrindstoneState = ModObjects.chooseModGrindstone(uvwo[3], this.getCoordBaseMode());
            	
        		this.setBlockState(world, biomeGrindstoneState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 9;
        	int chestV = 2;
        	int chestW = 2;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_weaponsmith");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{6,2,4, 0, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
        		{6, GROUND_LEVEL, -1}, 
        		{7, GROUND_LEVEL, -1}, 
        		{8, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, pathU, pathV-2, pathW, structureBB);
            	// Top is grass which is converted to path
            	if (world.isAirBlock(new BlockPos(this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW))))
            	{
            		this.setBlockState(world, biomeGrassState, pathU, pathV-1, pathW, structureBB);
                	
            	}
            	StructureVillageVN.setPathSpecificBlock(world, this.materialType, this.biome, this.disallowModSubs, this.getXWithOffset(pathU, pathW), this.getYWithOffset(pathV-1), this.getZWithOffset(pathU, pathW), false);
            }
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 6;
            	int v = 3;
            	int w = 5;
            	
            	int s = random.nextInt(7);
            	
            	// Inside
            	if (s==0)
            	{
            		u=2;
            		w=5;
            	}
            	else if (s<=6)
            	{
            		u=s-1;
            		w=6;
            	}
            	else if (s<=13)
            	{
            		u=s-3;
            		w=5;
            	}
            	else if (s<=16)
            	{
            		u=s-6;
            		w=4;
            	}
            	else if (s<=18)
            	{
            		u=s-8;
            		w=3;
            	}
            	else if (s==19)
            	{
            		u=9;
            		w=2;
            	}
            	// Outside
            	else if (s<=22)
            	{
            		u=s-15;
            		w=3;
            	}
            	else if (s==23)
            	{
            		u=5;
            		w=2;
            	}
            	else if (s==24)
            	{
            		u=7;
            		w=2;
            	}
            	else if (s<=27)
            	{
            		u=s-20;
            		w=1;
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 2, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // ------------------ //
    // --- Road Decor --- //
    // ------------------ //

    
    // --- Road Decor --- //
    
    public static class SavannaStreetDecor1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = 3;
    	public static final int STRUCTURE_DEPTH = 6;
    	public static final int STRUCTURE_HEIGHT = 2;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SavannaStreetDecor1() {}

        public SavannaStreetDecor1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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

        public static SavannaStreetDecor1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SavannaStreetDecor1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	/*
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
            	// top with grass
            	this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
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
            			new    int[]{-2,0,1,2,3,4,5}, // Values
            			new double[]{ 2,3,4,3,2,1,2}, // Weights
            			randomFromXYZ);
            	
            	uvw[2] = decorDepth;
            	
            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.getYWithOffset(0);

            	// If the decor is ON the road, do a surround check to make sure it isn't sunken into the ground
            	if (decorDepth<0)
            	{
	            	int nonairSurrounding = 0;
	            	int decorY = this.getYWithOffset(decorHeightY);
	            	for (int i=0; i<8; i++)
	            	{
	            		int[][] surroundpos = new int[][]{
	            			{0,0},
	            			{0,1},
	            			{0,2},
	            			{1,2},
	            			{2,2},
	            			{2,1},
	            			{2,0},
	            			{1,0},
	            		};
	            		int u = surroundpos[i][0]; int w = surroundpos[i][0];
	            		int x = this.getXWithOffset(u, w);
	            		int z = this.getZWithOffset(u, w);
	            		BlockPos pos = new BlockPos(x, decorY, z);
	            		if (world.getBlockState(pos).getBlock()!=Blocks.AIR)
	            		{
	            			if (++nonairSurrounding >=4) {decorHeightY++; break;}
	            		}
	            	}
            	}
            	
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
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, true);
            	
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
            	
            	// Grass base
            	if (!world.getBlockState(
            			new BlockPos(
	            			this.getXWithOffset(uvw[0], uvw[2]),
	            			this.getYWithOffset(decorHeightY-1),
	            			this.getZWithOffset(uvw[0], uvw[2])
            			)).isNormalCube()
            			|| decorDepth < 0 // If it's in the center of the road, make sure the base is grass so it doesn't become path -> dirt
            			) {
            		this.setBlockState(world, biomeGrassState, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	}
            }
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Roadside Farm 1 --- //
    
    public static class SavannaStreetSubstitute1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"       FF   ",
        		"    FFFFFFF ",
        		"  FFFFFFFFF ",
        		" FFFFFFFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 3;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
        
    	private int averageGroundLevel = -1;
    	private int otherSideOffset = 0; // Vertical offset of the other side of the road
    	// Range of U values to check the height of the opposite side
    	private static final int OTHERSIDE_MIN_U = 1;
    	private static final int OTHERSIDE_MAX_U = 4;
    	
        public SavannaStreetSubstitute1() {}

        public SavannaStreetSubstitute1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaStreetSubstitute1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
        	StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            // Structure on the other side of the street
            StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, -16, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH, STRUCTURE_WIDTH, STRUCTURE_HEIGHT+16, -Reference.STREET_WIDTH-1, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox)
            		&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
            		&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
            		? new SavannaStreetSubstitute1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// Determine the median ground level offset of the portion to be placed on the other side of the road
        	ArrayList<Integer> otherSideRoadHeights = new ArrayList<Integer>();
        	for (int i=OTHERSIDE_MIN_U; i<=OTHERSIDE_MAX_U; i++)
        	{
        		int aboveTopLevel = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i, -Reference.STREET_WIDTH-1), 64, this.getZWithOffset(i, -Reference.STREET_WIDTH-1))).getY();
        		if (aboveTopLevel != -1) {otherSideRoadHeights.add(aboveTopLevel);}
        	}
        	if (FunctionsVN.medianIntArray(otherSideRoadHeights, true)>0) {this.otherSideOffset = FunctionsVN.medianIntArray(otherSideRoadHeights, true)-this.averageGroundLevel;}
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
        	
        	// Grass Path to buttress water blocks
            for(int[] uvw : new int[][]{
            	{2,0,-1}, {9,0,-1},
            	{3,this.otherSideOffset,-Reference.STREET_WIDTH-0}, 
            	})
            {
            	// Dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	// Grass path
            	this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB);
            	StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]), false);
            	// Clear above
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1]+1, uvw[2], structureBB);
            }
        	
                        
            // Moist Farmland with crop above
            for(int[] uvwpmc : new int[][]{
            	// u,v,w, crop progress, farmland moist (7) or dry (0), crop (0:wheat, 1:melon)
            	
            	// This side of road
            	{1,0,0, 2, 7, 0}, 
            	{2,0,1, 1, 7, 0}, 
            	{3,0,0, 0, 7, 0}, {3,0,1, 0, 7, 0}, 
            	{4,0,2, 2, 7, 0}, {4,0,1, 1, 7, 1}, 
            	{5,0,0, 0, 7, 1}, {5,0,2, 0, 7, 0}, 
            	{6,0,0, 1, 7, 0}, {6,0,2, 1, 7, 0}, 
            	{7,0,0, 0, 7, 0}, {7,0,1, 0, 7, 0}, {7,0,3, 1, 7, 0}, 
            	{8,0,0, 2, 7, 1}, {8,0,1, 2, 7, 0}, {8,0,2, 2, 7, 0}, {8,0,3, 0, 7, 0}, 
            	{9,0,1, 1, 7, 0}, {9,0,2, 0, 7, 0}, 
            	{10,0,0, 0, 7, 0}, {10,0,1, 2, 7, 0}, {10,0,2, 1, 7, 0}, 
            	// Opposite side of road
            	{1,this.otherSideOffset,-Reference.STREET_WIDTH-1, 1, 7, 0}, 
            	{2,this.otherSideOffset,-Reference.STREET_WIDTH-1, 2, 7, 1}, {2,0,-Reference.STREET_WIDTH-2, 0, 0, 0}, 
            	{3,this.otherSideOffset,-Reference.STREET_WIDTH-2, 0, 0, 0}, 
            	{4,this.otherSideOffset,-Reference.STREET_WIDTH-1, 0, 0, 1}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvwpmc[0], uvwpmc[1]-1, uvwpmc[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	this.setBlockState(world, uvwpmc[5]==1?Blocks.MELON_STEM.getDefaultState():Blocks.WHEAT.getStateFromMeta(uvwpmc[3]), uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwpmc[4]), uvwpmc[0], uvwpmc[1], uvwpmc[2], structureBB);
            }
            
            
            // Water with dirt foundation
            for(int[] uvw : new int[][]{
            	// This side of road
            	{2,0,0}, 
            	{5,0,1}, 
            	{6,0,1}, 
            	{9,0,0}, 
            	// Other side of road
            	{3,this.otherSideOffset,-Reference.STREET_WIDTH-1}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1]+1, uvw[2], structureBB);
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{4,1,0}, 
            	{7,1,2}, 
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

            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.getYWithOffset(0);
            	
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, true);
            	
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
            	
            	// Grass base
            	if (!world.getBlockState(
            			new BlockPos(
	            			this.getXWithOffset(uvw[0], uvw[2]),
	            			this.getYWithOffset(decorHeightY-1),
	            			this.getZWithOffset(uvw[0], uvw[2])
            			)).isNormalCube()
            			|| uvw[2] < 0 // If it's in the center of the road, make sure the base is grass so it doesn't become path -> dirt
            			) {
            		this.setBlockState(world, biomeGrassState, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	}
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Roadside Farm 2 --- //
    
    public static class SavannaStreetSubstitute2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"          FF  ",
        		"        FFFFF ",
        		"    FF FFFFFF ",
        		"  FFFFFFFFFFF ",
        		" FFFFFFFFFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 3;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
        
    	private int averageGroundLevel = -1;
    	private int otherSideOffset = 0; // Vertical offset of the other side of the road
    	// Range of U values to check the height of the opposite side
    	private static final int OTHERSIDE_MIN_U = 1;
    	private static final int OTHERSIDE_MAX_U = 11;
    	
        public SavannaStreetSubstitute2() {}

        public SavannaStreetSubstitute2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaStreetSubstitute2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
        	StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            // Structure on the other side of the street
            StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, -16, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH, STRUCTURE_WIDTH, STRUCTURE_HEIGHT+16, -Reference.STREET_WIDTH-1, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox)
            		&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
            		&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
            		? new SavannaStreetSubstitute2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// Determine the median ground level offset of the portion to be placed on the other side of the road
        	ArrayList<Integer> otherSideRoadHeights = new ArrayList<Integer>();
        	for (int i=OTHERSIDE_MIN_U; i<=OTHERSIDE_MAX_U; i++)
        	{
        		int aboveTopLevel = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(i, -Reference.STREET_WIDTH-1), 64, this.getZWithOffset(i, -Reference.STREET_WIDTH-1))).getY();
        		if (aboveTopLevel != -1) {otherSideRoadHeights.add(aboveTopLevel);}
        	}
        	if (FunctionsVN.medianIntArray(otherSideRoadHeights, true)>0) {this.otherSideOffset = FunctionsVN.medianIntArray(otherSideRoadHeights, true)-this.averageGroundLevel;}
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
        	
        	// Grass
            for(int[] uvw : new int[][]{
            	{3,0,0},  
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1]+1, uvw[2], structureBB);
            	this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB); 
            }
        	
        	
        	// Grass Path to buttress water blocks
            for(int[] uvw : new int[][]{
            	{2,0,-1}, 
            	{6,this.otherSideOffset,-Reference.STREET_WIDTH-0}, 
            	{9,this.otherSideOffset,-Reference.STREET_WIDTH-0}, 
            	})
            {
            	// Dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	// Grass path
            	this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB);
            	StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]), false);
            	// Clear above
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1]+1, uvw[2], structureBB);
            }
        	
                        
            // Moist Farmland with crop above
            for(int[] uvwpmc : new int[][]{
            	// u,v,w, crop progress, farmland moist (7) or dry (0), crop (0:wheat, 1:melon)
            	
            	// This side of road
            	{1,0,0, 0, 7, 0}, 
            	{2,0,1, 1, 7, 0}, 
            	{3,0,1, 0, 7, 0}, 
            	{4,0,1, 0, 7, 1}, {4,0,2, 0, 7, 0}, 
            	{5,0,0, 0, 7, 1}, {5,0,2, 0, 7, 0}, 
            	{6,0,1, 0, 7, 1}, {6,0,0, 1, 7, 0}, 
            	{7,0,0, 0, 7, 0}, {7,0,1, 0, 7, 0}, {7,0,2, 1, 7, 0},
            	{8,0,0, 1, 7, 0}, {8,0,2, 1, 7, 0}, {8,0,3, 1, 7, 0}, 
            	{9,0,0, 0, 7, 0}, {9,0,1, 0, 7, 0}, {9,0,2, 0, 7, 0}, {9,0,3, 1, 7, 0}, 
            	{10,0,0, 0, 7, 1}, {10,0,1, 1, 7, 1}, {10,0,2, 2, 7, 0}, {10,0,3, 0, 7, 0}, {10,0,4, 0, 7, 0}, 
            	{11,0,0, 0, 7, 1}, {11,0,1, 2, 7, 1}, {11,0,3, 0, 7, 0}, {11,0,4, 0, 7, 0}, 
            	{12,0,0, 1, 7, 0}, {12,0,1, 0, 7, 0}, {12,0,2, 2, 7, 0}, {12,0,3, 0, 7, 0}, 
            	// Opposite side of road
            	{1,this.otherSideOffset,-Reference.STREET_WIDTH-1, 1, 7, 0}, 
            	{2,this.otherSideOffset,-Reference.STREET_WIDTH-1, 1, 7, 1}, {2,this.otherSideOffset,-Reference.STREET_WIDTH-2, 0, 7, 1}, 
            	{3,this.otherSideOffset,-Reference.STREET_WIDTH-1, 1, 7, 0}, {3,this.otherSideOffset,-Reference.STREET_WIDTH-3, 0, 7, 0}, 
            	{4,this.otherSideOffset,-Reference.STREET_WIDTH-1, 0, 7, 0}, {4,this.otherSideOffset,-Reference.STREET_WIDTH-2, 0, 7, 0}, 
            	{5,this.otherSideOffset,-Reference.STREET_WIDTH-1, 1, 7, 1}, {5,this.otherSideOffset,-Reference.STREET_WIDTH-2, 0, 7, 1}, {5,this.otherSideOffset,-Reference.STREET_WIDTH-3, 0, 7, 1}, 
            	{6,this.otherSideOffset,-Reference.STREET_WIDTH-2, 0, 7, 0}, 
            	{7,this.otherSideOffset,-Reference.STREET_WIDTH-1, 0, 7, 0}, {7,this.otherSideOffset,-Reference.STREET_WIDTH-2, 0, 7, 0}, 
            	{8,this.otherSideOffset,-Reference.STREET_WIDTH-1, 1, 7, 0}, {8,this.otherSideOffset,-Reference.STREET_WIDTH-2, 1, 7, 0}, {8,this.otherSideOffset,-Reference.STREET_WIDTH-3, 0, 7, 0}, 
            	{9,this.otherSideOffset,-Reference.STREET_WIDTH-2, 1, 7, 0}, 
            	{10,this.otherSideOffset,-Reference.STREET_WIDTH-1, 0, 7, 0}, 
            	{11,this.otherSideOffset,-Reference.STREET_WIDTH-1, 0, 7, 0}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvwpmc[0], uvwpmc[1]-1, uvwpmc[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	this.setBlockState(world, uvwpmc[5]==1?Blocks.MELON_STEM.getDefaultState():Blocks.WHEAT.getStateFromMeta(uvwpmc[3]), uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwpmc[4]), uvwpmc[0], uvwpmc[1], uvwpmc[2], structureBB); 
            }
            
            
            // Water with dirt foundation
            for(int[] uvw : new int[][]{
            	// This side of road
            	{2,0,0}, 
            	{5,0,1}, 
            	{8,0,1}, 
            	{11,0,2}, 
            	// Other side of road
            	{3,this.otherSideOffset,-Reference.STREET_WIDTH-2}, 
            	{6,this.otherSideOffset,-Reference.STREET_WIDTH-1}, 
            	{9,this.otherSideOffset,-Reference.STREET_WIDTH-1}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1]+1, uvw[2], structureBB);
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{4,1,0}, 
            	{12,1,-2}, 
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

            	int decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.getYWithOffset(0);
            	
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, true);
            	
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
            	
            	// Fill below with dirt regardless
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], decorHeightY-2, uvw[2], structureBB);
            	
            	// Grass base
            	if (!world.getBlockState(
            			new BlockPos(
	            			this.getXWithOffset(uvw[0], uvw[2]),
	            			this.getYWithOffset(decorHeightY-1),
	            			this.getZWithOffset(uvw[0], uvw[2])
            			)).isNormalCube()
            			|| uvw[2] < 0 // If it's in the center of the road, make sure the base is grass so it doesn't become path -> dirt
            			) {
            		this.setBlockState(world, biomeGrassState, uvw[0], decorHeightY-1, uvw[2], structureBB);
            	}
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Roadside Farm 3 --- //
    
    public static class SavannaStreetSubstitute3 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		" FFF",
        		"FFFF",
        		" FF ",
        		"    ",
        		"    ",
        		"    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 2;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
        
    	private int averageGroundLevel = -1;
    	private int farmGroundLevel = 0;
    	
        public SavannaStreetSubstitute3() {}

        public SavannaStreetSubstitute3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaStreetSubstitute3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
        	StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            // Structure on the other side of the street
            StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, -16, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH, STRUCTURE_WIDTH, STRUCTURE_HEIGHT+16, -Reference.STREET_WIDTH-1, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox)
            		&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
            		&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
            		? new SavannaStreetSubstitute3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// Determine the median ground level offset of the portion to be placed on the other side of the road
        	ArrayList<Integer> otherSideRoadHeights = new ArrayList<Integer>();
        	for (int[] uw : new int[][]{
            	{0,4}, 
            	{1,3}, {1,4}, {1,5}, 
            	{2,3}, {2,4}, {2,5}, 
            	{3,4}, {3,5}, 
        	})
        	{
        		int aboveTopLevel = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uw[0], uw[1]), 64, this.getZWithOffset(uw[0], uw[1]))).getY();
        		if (aboveTopLevel != -1) {otherSideRoadHeights.add(aboveTopLevel);}
        	}
        	if (FunctionsVN.medianIntArray(otherSideRoadHeights, true)>0) {this.farmGroundLevel = FunctionsVN.medianIntArray(otherSideRoadHeights, true)-this.averageGroundLevel;}
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
                        
            // Moist Farmland with crop above
            for(int[] uvwpmc : new int[][]{
            	// u,v,w, crop progress, farmland moist (7) or dry (0), crop (0:wheat, 1:melon)
            	
            	// This side of road
            	{0,this.farmGroundLevel,4, 0, 0, 0}, 
            	{1,this.farmGroundLevel,3, 0, 7, 0}, {1,this.farmGroundLevel,4, 0, 0, 0}, {1,this.farmGroundLevel,5, 0, 7, 0}, 
            	{2,this.farmGroundLevel,3, 0, 7, 0}, {2,this.farmGroundLevel,5, 0, 0, 0}, 
            	{3,this.farmGroundLevel,4, 1, 7, 0}, {3,this.farmGroundLevel,5, 0, 7, 0}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvwpmc[0], uvwpmc[1]-1, uvwpmc[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	this.setBlockState(world, uvwpmc[5]==1?Blocks.MELON_STEM.getDefaultState():Blocks.WHEAT.getStateFromMeta(uvwpmc[3]), uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwpmc[4]), uvwpmc[0], uvwpmc[1], uvwpmc[2], structureBB); 
            }
            
            
            // Water with dirt foundation
            for(int[] uvw : new int[][]{
            	// This side of road
            	{2,this.farmGroundLevel,4}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1]+1, uvw[2], structureBB);
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Roadside Farm 4 --- //
    
    public static class SavannaStreetSubstitute4 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public FunctionsVN.VillageType villageType=null;
    	public FunctionsVN.MaterialType materialType=null;
    	public boolean disallowModSubs=false;
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
    	public Biome biome = null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
        		"  FFF      ",
        		" FFFFF     ",
        		" FFFFFFF   ",
        		"FFFFFFF    ",
        		"  FFFF     ",
        		" FFFF      ",
        		" FFFFF     ",
        		"FFFFFF     ",
        		" FFFFFF    ",
        		"  FFFFF    ",
        		"  FFFFFF   ",
        		" FFFFFFFF  ",
        		" FFFFFFFF  ",
        		"  FFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 2;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 0;
        
    	private int averageGroundLevel = -1;
    	
        public SavannaStreetSubstitute4() {}

        public SavannaStreetSubstitute4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = boundingBox;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
            	this.disallowModSubs=start.disallowModSubs;
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
        
        public static SavannaStreetSubstitute4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
        	StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            // Structure on the other side of the street
            StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, -16, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH, STRUCTURE_WIDTH, STRUCTURE_HEIGHT+16, -Reference.STREET_WIDTH-1, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox)
            		&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
            		&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
            		? new SavannaStreetSubstitute4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            						this.boundingBox.minX+(this.getCoordBaseMode().getHorizontalIndex()%2==0?INCREASE_MIN_U:0), this.boundingBox.minZ+(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:INCREASE_MIN_U),
            						this.boundingBox.maxX-(this.getCoordBaseMode().getHorizontalIndex()%2==0?DECREASE_MAX_U:0), this.boundingBox.maxZ-(this.getCoordBaseMode().getHorizontalIndex()%2==0?0:DECREASE_MAX_U)),
            				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
                    
                    this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.townColor==-1
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
            	
            }
            
        	BiomeProvider biomeProvider = world.getBiomeProvider();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            Biome biome = biomeProvider.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
                        
            // Moist Farmland with crop above
            for(int[] uvwpmc : new int[][]{
            	// u,v,w, crop progress, farmland moist (7) or dry (0), crop (-1:nothing, 0:wheat, 1:melon)
            	
            	// This side of road
            	{2,0,0, 0, 7, 0}, {3,0,0, 0, 7, 0}, {4,0,0, 0, 7, 0}, {5,0,0, 0, 7, 0}, {6,0,0, 0, 7, -1}, {7,0,0, 0, 7, 0}, {9,0,0, 0, 7, 0}, {10,0,0, 1, 7, 0}, 
            	{1,0,1, 0, 7, 0}, {2,0,1, 0, 7, 0}, {3,0,1, 0, 7, 0}, {4,0,1, 0, 7, 0}, {6,0,1, 1, 7, 0}, {7,0,1, 0, 7, -1}, {8,0,1, 0, 7, 0}, 
            	{1,0,2, 0, 7, 0}, {3,0,2, 0, 7, 0}, {4,0,2, 0, 7, 0}, {5,0,2, 0, 7, 0}, {6,0,2, 1, 7, 1}, {7,0,2, 1, 7, 1}, {8,0,2, 0, 7, 0}, 
            	{2,0,3, 0, 7, 0}, {3,0,3, 1, 7, 0}, {4,0,3, 1, 7, 0}, {5,0,3, 0, 7, 1}, {6,0,3, 0, 7, 1}, {7,0,3, 0, 7, 1}, 
            	{2,0,4, 0, 7, 0}, {3,0,4, 0, 7, 0}, {4,0,4, 0, 7, 1}, {6,0,4, 0, 7, 1}, 
            	{1,0,5, 0, 7, 0}, {2,0,5, 0, 7, 0}, {4,0,5, 0, 7, 0}, {5,0,5, 0, 7, 1}, {6,0,5, 0, 7, 1}, 
            	{0,0,6, 0, 7, 0}, {1,0,6, 0, 7, 0}, {2,0,6, 0, 7, 0}, {3,0,6, 0, 7, 0}, {4,0,6, 0, 7, 0}, {5,0,6, 0, 7, 0}, 
            	{1,0,7, 0, 7, 1}, {2,0,7, 0, 7, 1}, {3,0,7, 1, 7, 0}, {4,0,7, 0, 7, 0}, {5,0,7, 0, 7, 0}, 
            	{1,0,8, 0, 7, 1}, {2,0,8, 0, 7, 0}, {3,0,8, 0, 7, 0}, {4,0,8, 0, 7, 0}, 
            	{2,0,9, 0, 7, 0}, {3,0,9, 0, 7, 0}, {4,0,9, 0, 7, 0}, {5,0,9, 0, 7, 0}, 
            	{0,0,10, 0, 7, 0}, {1,0,10, 0, 0, 0}, {3,0,10, 0, 0, 0}, {4,0,10, 0, 7, 0}, {5,0,10, 1, 7, 0}, {6,0,10, 0, 7, 0}, 
            	{1,0,11, 0, 0, 0}, {2,0,11, 0, 0, 0}, {3,0,11, 0, 7, 0}, {5,0,11, 0, 7, 0}, {6,0,11, 0, 7, 0}, {7,0,11, 0, 7, 0}, 
            	{1,0,12, 0, 0, 1}, {2,0,12, 1, 7, 1}, {3,0,12, 0, 7, 0}, {4,0,12, 0, 7, 0}, {5,0,12, 0, 7, 0}, 
            	{2,0,13, 0, 7, 0}, {3,0,13, 0, 7, 0}, {4,0,13, 0, 0, 0}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvwpmc[0], uvwpmc[1]-1, uvwpmc[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	if (uvwpmc[5]!=-1) {this.setBlockState(world, uvwpmc[5]==1?Blocks.MELON_STEM.getDefaultState():Blocks.WHEAT.getStateFromMeta(uvwpmc[3]), uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);} 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwpmc[4]), uvwpmc[0], uvwpmc[1], uvwpmc[2], structureBB); 
            }
        	
        	
        	// Grass Path to buttress water blocks
            for(int[] uvw : new int[][]{
            	{8,0,-1}, 
            	})
            {
            	// Dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	// Grass path
            	this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB);
            	StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]), false);
            	// Clear above
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1]+1, uvw[2], structureBB);
            }
            
            
            // Water with dirt foundation
            for(int[] uvw : new int[][]{
            	// This side of road
            	{8,0,0}, 
            	{5,0,1}, 
            	{2,0,2}, 
            	{5,0,4}, 
            	{3,0,5}, 
            	{2,0,10}, 
            	{4,0,11}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1]+1, uvw[2], structureBB);
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // ------------------- //
    // --- Biome Decor --- //
    // ------------------- //
    
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
