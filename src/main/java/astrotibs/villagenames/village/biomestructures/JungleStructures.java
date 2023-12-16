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
import astrotibs.villagenames.utility.FunctionsVN.VillageType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import astrotibs.villagenames.village.chestloot.ChestGenHooks;
import astrotibs.villagenames.village.chestloot.WeightedRandomChestContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class JungleStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Jungle Statue --- //
	// designed by AstroTibs
    
    public static class JungleStatue extends StructureVillageVN.StartVN
    {
        // Make foundation with blanks as empty air, F as foundation spaces, and P as path
        private static final String[] foundationPattern = new String[]{
            	"  PPFFPPF  ",
            	" PPPPPPPPP ",
            	" PFPPPPPPF ",
            	"PPPPFFFFPPP",
            	"PPPFFFFFPPP",
            	"PPPFFFFFPPP",
            	" PPPFFFFPPF",
            	" FPPPPPPPP ",
            	"  FPPPPPP  ",
            	"    PPPFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
	    public JungleStatue() {}
		
		public JungleStatue(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, float villageSize)
		{
		    super(biomeProvider, componentType, random, posX, posZ, components, villageSize);
    		
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
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
    		// No roads behind the structure
    		
			// Northward
			if (this.getCoordBaseMode().getHorizontalIndex()!=2) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==1 ? 3 : 4), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			// Eastward
			if (this.getCoordBaseMode().getHorizontalIndex()!=3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==2 ? 3 : 4), EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.getCoordBaseMode().getHorizontalIndex()!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==1 ? 3 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.getCoordBaseMode().getHorizontalIndex()!=1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==2 ? 3 : 4), EnumFacing.WEST, this.getComponentType());}
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
        				true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
            
        	// Generate or otherwise obtain village name and banner and colors
        	BlockPos signpos = new BlockPos(6,2,2);
        	
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(signpos.getX(), signpos.getZ()),
        			this.getYWithOffset(signpos.getY()),
        			this.getZWithOffset(signpos.getX(), signpos.getZ()));
        	
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
        	
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0,1,0}, 
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
            		decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.boundingBox.minY;
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
        	
			
        	// Set Grass blocks
        	for (int[] grass_uw : new int[][]{
        		{1, 2}, 
        		{2, 1}, {2, 7}, 
        		{3, 4}, {3, 5}, 
        		{4, 9}, 
        		{5, 9}, 
        		{7, 0}, 
        		{8, 0}, {8, 9}, 
        		{9, 7}, 
        		{10, 3}, 
        	})
        	{
        		this.setBlockState(world, biomeGrassState, grass_uw[0], 0, grass_uw[1], structureBB);
        	}
			
        	// Set unkempt grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{8,GROUND_LEVEL,0, 0},  
            	{10,GROUND_LEVEL,3, 0},
            	{7,GROUND_LEVEL,0, 1},
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
        	
            
            // Statue
            
            // Terracotta foundation
        	for (int[] uuvvww : new int[][]{
        		{4,0,3, 7,2,6}, 
        		})
            {
        		// Orange
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor5 : 1)),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor5 : 1)), 
        				false);
            }
            
        	// Try to establish diorite blocks used for the statue. If any kind doesn't exist, use default for all.
        	Block biomePolishedDioriteStairsBlock;
        	IBlockState biomePolishedDioriteBlockState;
        	Block biomeDioriteStairsBlock;
        	IBlockState biomeDioriteBlockState;
        	IBlockState biomeDioriteWallState;
        	
    		boolean useOnlyStone = false; // This flag will indicate to use stone instead of diorite, should we need to.
        	while (true)
        	{
            	// Polished diorite stairs
            	if (useOnlyStone) {biomePolishedDioriteStairsBlock = Blocks.STONE_BRICK_STAIRS;} // Set to stone brick stairs
            	else
            	{
            		biomePolishedDioriteStairsBlock = ModObjects.chooseModPolishedDioriteStairsBlock(); 
                	if (biomePolishedDioriteStairsBlock==null) // Try diorite brick stairs
                	{
                		biomePolishedDioriteStairsBlock = ModObjects.chooseModDioriteBrickStairsBlock();
                		if (biomePolishedDioriteStairsBlock==null) // Try regular diorite brick stairs
                    	{
                    		biomePolishedDioriteStairsBlock = ModObjects.chooseModDioriteStairsBlock();
                    		if (biomePolishedDioriteStairsBlock==null) {useOnlyStone=true; continue;} // Trigger flag and reset
                    	}
                	}
            	}
            	biomePolishedDioriteStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomePolishedDioriteStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            	
            	
            	// Polished diorite blocks
            	biomePolishedDioriteBlockState = Blocks.STONE.getStateFromMeta(4);
            	if (useOnlyStone) {biomePolishedDioriteBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);} // Set to stone brick
            	            	
                
            	// Regular diorite stairs
            	if (useOnlyStone) {biomeDioriteStairsBlock = Blocks.STONE_STAIRS;} // Set to cobblestone stairs
            	else
            	{
            		biomeDioriteStairsBlock = ModObjects.chooseModDioriteStairsBlock(); 
                	if (biomeDioriteStairsBlock==null) {useOnlyStone=true; continue;} // Trigger flag and reset
            	}
            	biomeDioriteStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomeDioriteStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            	
            	
            	// Diorite blocks
            	biomeDioriteBlockState = Blocks.STONE.getStateFromMeta(3);
            	if (useOnlyStone) {biomeDioriteBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);} // Set to cobblestone
            	
            	
            	// Diorite wall
            	if (useOnlyStone) {biomeDioriteWallState = Blocks.COBBLESTONE_WALL.getStateFromMeta(0);} // Set to cobblestone wall
            	else
            	{
            		biomeDioriteWallState = ModObjects.chooseModDioriteWallState();
                	if (biomeDioriteWallState==null) {biomeDioriteWallState = Blocks.COBBLESTONE_WALL.getStateFromMeta(0); useOnlyStone=true; continue;} // Trigger flag and reset
            	}
            	biomeDioriteWallState = StructureVillageVN.getBiomeSpecificBlockState(biomeDioriteWallState, this.materialType, this.biome, this.disallowModSubs);
            	
            	
            	// If you make it here, all blocks are either diorite-type or stone-type.
            	break;
            }
        	
        	// Now, construct the statue with either all diorite blocks, or all stone
        	
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Base
        		{4,3,3, 3}, {5,3,3, 3}, {6,3,3, 3}, 
        		{7,3,3, 1}, {7,3,4, 1}, {7,3,5, 1}, 
        		{7,3,6, 2}, {6,3,6, 2}, {5,3,6, 2}, 
        		{4,3,6, 0}, {4,3,5, 0}, {4,3,4, 0}, 
        		// Boots
        		{5,4,4, 3}, {6,4,5, 3}, 
        		// Hat
        		{5,9,4, 3}, {6,9,4, 1}, {6,9,5, 2}, {5,9,5, 0}, 
        		})
            {
        		this.setBlockState(world, biomePolishedDioriteStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	for (int[] uuvvww : new int[][]{
        		// Base
        		{5,3,4, 6,3,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomePolishedDioriteBlockState,
        				biomePolishedDioriteBlockState, 
        				false);
            }
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Leg
        		{5,5,5, 2+4}, {5,5,4, 3+4}, {5,6,4, 3},
        		})
            {
        		this.setBlockState(world, biomeDioriteStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	for (int[] uuvvww : new int[][]{
        		// Torso
        		{5,6,5, 6,7,5}, 
        		// Leg
        		{6,5,5, 6,5,5},
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeDioriteBlockState,
        				biomeDioriteBlockState, 
        				false);
            }
        	for (int[] uuvvww : new int[][]{
        		// Staff arm
        		{4,6,4, 4,6,5}, {4,7,5, 4,7,5}, 
        		// Flower arm
        		{7,7,3, 7,7,5}, 
        		// Head
        		{5,8,4, 6,8,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeDioriteWallState,
        				biomeDioriteWallState, 
        				false);
            }
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Staff
            	{4,4,4, 4,5,4}, {4,7,4, 4,7,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Campfire
        	IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), this.getCoordBaseMode());
            for(int[] uvw : new int[][]{
            	{4,8,4}, 
            	})
            {
        		this.setBlockState(world, campfireState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Flower Pot
            int u=7; int v=8; int w=3;
            int x = this.getXWithOffset(u, w);
            int y = this.getYWithOffset(v);
            int z = this.getZWithOffset(u, w);
            
            IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
    		int randomPottedPlant = random.nextInt(10)-1;
    		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
    		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
            
    		
            // Sign
    		IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		if (GeneralConfig.nameSign)
            {
    			// Village sign
    			
            	int signU = 6;
    			int signV = 2;
    			int signW = 2;
    			int signO = 2;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
                boolean hanging=true;
                
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.getCoordBaseMode().getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		        		
        		
        		
        		// "Founder" sign
        		
        		signU = 5;
        		signX = this.getXWithOffset(signU, signW);
                signZ = this.getZWithOffset(signU, signW);
        		                
                signContents = new TileEntitySign();
        		
            	String topLine = "Founder:";
        		topLine = topLine.trim();
        		
        		String[] founderName = NameGenerator.newRandomName("villager", random);
        		
        		String founderPrefix = founderName[1];
        		String founderRoot = founderName[2];
        		String founderSuffix = founderName[3];
        		
        		if ( (founderPrefix.length() + 1 + founderRoot.length()) > 15 )
        		{
        			// Prefix+Root is too long, so move prefix to line 1
        			signContents.signText[0] = new TextComponentString(GeneralConfig.headerTags.trim() + topLine.trim());
        			signContents.signText[1] = new TextComponentString(founderPrefix.trim());
        			if ( (founderRoot.length() + 1 + founderSuffix.length()) > 15 )
        			{
        				// Root+Suffix is too long, so move suffix to line 3
        				signContents.signText[2] = new TextComponentString(founderRoot.trim());
        				signContents.signText[3] = new TextComponentString(founderSuffix.trim());
        			}
        			else
        			{
        				// Fit Root+Suffix onto line 2
        				signContents.signText[2] = new TextComponentString((founderRoot+" "+founderSuffix).trim());
        			}
        		}
        		else if ( (founderPrefix.length() + 1 + founderRoot.length() + 1 + founderSuffix.length()) <= 15 )
        		{
        			// Whole name fits on one line! Put it all on line 2.
        			signContents.signText[1] = new TextComponentString(GeneralConfig.headerTags.trim() + topLine);
        			signContents.signText[2] = new TextComponentString((founderPrefix+" "+founderRoot+" "+founderSuffix).trim());
        		}
        		else
        		{
        			// Only Prefix and Root can fit together on line 2.
        			signContents.signText[1] = new TextComponentString(GeneralConfig.headerTags.trim() + topLine.trim());
        			signContents.signText[2] = new TextComponentString((founderPrefix+" "+founderRoot).trim());
        			signContents.signText[3] = new TextComponentString(founderSuffix.trim());
        		}
        		// If top line is blank, roll everything up one line:
        		if (topLine.equals(""))
        		{
        			for (int isign=0; isign <3; isign++)
        			{
        				signContents.signText[isign] = signContents.signText[isign+1];
        			}
        			signContents.signText[3] = new TextComponentString("");
        		}
                
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.getCoordBaseMode().getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
        	
    		
			// Banner
    		if (GeneralConfig.villageBanners)
    		{
    			for(int capeU : new int[]{5, 6})
                {
                    int bannerU = capeU;
        			int bannerV = 7;
        			int bannerW = 6;
        			int bannerO = 0; // Facing away from you
        			boolean hanging=true;
        			
        			int bannerX = this.getXWithOffset(bannerU, bannerW);
        			int bannerY = this.getYWithOffset(bannerV);
                    int bannerZ = this.getZWithOffset(bannerU, bannerW);
                    
                    BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                    
                	// Set the banner and its orientation
                    world.setBlockState(bannerPos, Blocks.WALL_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(bannerO, this.getCoordBaseMode().getHorizontalIndex(), true)), 2);
                    
    				// Set the tile entity
    				TileEntity tilebanner = new TileEntityBanner();
    				NBTTagCompound modifystanding = new NBTTagCompound();
    				tilebanner.writeToNBT(modifystanding);
    				
    				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
    				
        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
    				
            		world.setTileEntity(bannerPos, tilebanner);
                }
    		}
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{2, 1, 3, -1, 0},
	        			{2, 1, 3, -1, 0},
	        			{5, 1, 8, -1, 0},
	        			{9, 1, 5, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
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
	
    
	// --- Cocoa Tree --- //
	// designed by Roadhog360
    
    public static class JungleCocoaTree extends StructureVillageVN.StartVN
    {
        // Make foundation with blanks as empty air, F as foundation spaces, and P as path
        private static final String[] foundationPattern = new String[]{
            	"    PPP    ",
            	"  PPPPPPP  ",
            	" PPFFFFFPP ",
            	" PFFFFFFFP ",
            	"PPFFFFFFFPP",
            	"PPFFFFFFFPP",
            	"PPFFFFFFFPP",
            	" PFFFFFFFP ",
            	" PPFFFFFPP ",
            	" PPPPPPPPP ",
            	"    PPP    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 11;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2 + 4 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
	    public JungleCocoaTree() {}
		
		public JungleCocoaTree(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, float villageSize)
		{
		    super(biomeProvider, componentType, random, posX, posZ, components, villageSize);
    		
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
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
    		// No roads behind the structure
    		
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
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
        				true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
            
        	// Generate or otherwise obtain village name and banner and colors
        	BlockPos signpos = new BlockPos(6,2,2);
        	
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(signpos.getX(), signpos.getZ()),
        			this.getYWithOffset(signpos.getY()),
        			this.getZWithOffset(signpos.getX(), signpos.getZ()));
        	
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
        	
            
            // Cobblestone stairs
			Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{4,1,2, 6,1,2, 3}, 
        		{7,1,2, 7,1,3, 1}, 
        		{8,1,3, 8,1,3, 3}, 
        		
        		{8,1,4, 8,1,6, 1}, 
        		{7,1,7, 8,1,7, 2}, 
        		{7,1,8, 7,1,8, 1}, 
        		
        		{4,1,8, 6,1,8, 2}, 
        		{3,1,7, 3,1,8, 0}, 
        		{2,1,7, 2,1,7, 2}, 
        		
        		{2,1,4, 2,1,6, 0}, 
        		{2,1,3, 3,1,3, 3}, 
        		{3,1,2, 3,1,2, 0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);
            }
        	
			
        	// Grass blocks
        	for (int[] grass_uw : new int[][]{
        		{3, 1, 4}, {3, 1, 5}, 
        		{4, 1, 4}, {4, 1, 6}, {4, 1, 7}, 
        		{5, 1, 3}, {5, 1, 5}, {5, 1, 7}, 
        		{6, 1, 3}, {6, 1, 6}, 
        		{7, 1, 5}, 
        	})
        	{
        		this.setBlockState(world, biomeGrassState, grass_uw[0], grass_uw[1], grass_uw[2], structureBB);
        	}
        	
        	
        	// Podzol
        	IBlockState biomePodzolState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getStateFromMeta(2), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] grass_uw : new int[][]{
        		{3, 1, 6}, 
        		{4, 1, 3}, {4, 1, 5}, 
        		{5, 1, 4}, {5, 1, 6}, 
        		{6, 1, 4}, {6, 1, 5}, {6, 1, 7}, 
        		{7, 1, 4}, {7, 1, 6}, 
        	})
        	{
        		this.setBlockState(world, biomePodzolState, grass_uw[0], grass_uw[1], grass_uw[2], structureBB);
        	}
        	
        	
        	// Vertical jungle logs
            for (int[] uw : new int[][]{
            	{5,2,5, 5,9,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], Blocks.LOG.getStateFromMeta(3), Blocks.LOG.getStateFromMeta(3), false);
            }
            
            
            // Jungle Leaves
        	for (int[] uuvvww : new int[][]{
        		// At the base
        		{3,2,4, 3,2,4}, 
        		{3,2,6, 3,2,6}, 
        		{4,2,5, 4,2,7}, {4,3,6, 4,3,6}, 
        		{5,2,3, 5,2,4}, {5,3,4, 5,3,4}, {5,2,6, 5,2,6}, 
        		{6,2,4, 6,2,5}, 
        		{7,2,5, 7,2,5}, 
        		// As part of the tree itself
        		{3,7,3, 4,8,7}, 
        		{5,7,3, 5,8,4}, {5,7,6, 5,8,7},
        		{6,7,3, 6,8,3}, {6,7,4, 7,8,7},
        		{4,9,5, 4,10,5}, {5,9,4, 5,10,4}, {6,9,5, 6,10,5}, {5,9,6, 5,10,6},
        		{5,10,5, 5,10,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.LEAVES.getStateFromMeta(3),
        				Blocks.LEAVES.getStateFromMeta(3), 
        				false);
            }
            
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Left side
        			{4,3,5, 3}, 
        			// Back side
        			{5,3,6, 0}, {5,5,6, 0}, {5,6,6, 0}, 
        			// Right side
        			{6,5,5, 1},
        			// Front side
        			{5,5,4, 2}, {5,6,4, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
        	// Cocoa pods - random number and positions
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int i=0; i<5; i++)
                {
        			// Choose a random side for the pod
        			int side = random.nextInt(4);
        			
        			int u,v,w;
        			
        			switch (side)
        			{
        			case 0: // Forward facing
        				u=5; w=6; v=4+(random.nextBoolean()?1:0)+(random.nextBoolean()?1:0);
        				break;
        			case 1: // Right facing
        				u=6; w=5; v=4+(random.nextBoolean()?1:0)+(random.nextBoolean()?1:0);
        				break;
        			default:
        			case 2: // Back facing
        				u=5; w=4; v=4+(random.nextBoolean()?1:0)+(random.nextBoolean()?1:0);
        				break;
        			case 3: // Left facing
        				u=4; w=5; v=4+(random.nextBoolean()?1:0)+(random.nextBoolean()?1:0);
        				break;
        			}
        			
            		this.setBlockState(world, Blocks.COCOA.getStateFromMeta(StructureVillageVN.getCocoaPodOrientationMeta(side, this.getCoordBaseMode(), random.nextInt(3))), u, v, w, structureBB);
            		
        			// Randomly stop placing pods
        			if (random.nextInt(4)==0) {break;}
                }
        	}
            
        	
        	// Flowers
        	for (int[] uvw : new int[][]{
    			{3,2,5},
    			{5,2,7}, 
    			{6,2,6}, 
        		})
            {
        		IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
            	int flowerindex = random.nextInt(10 + (cornflowerState!=null && lilyOfTheValleyState!=null ? 2:0));
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10 is cornflower, 11 is lily of the valley
        		IBlockState flowerstate;
            	if (flowerindex==10 && cornflowerState!=null) {flowerstate=cornflowerState;}
            	else if (flowerindex==11 && lilyOfTheValleyState!=null) {flowerstate=lilyOfTheValleyState;}
            	else {flowerstate = (flowerindex==9 ? Blocks.YELLOW_FLOWER:Blocks.RED_FLOWER).getStateFromMeta(new int[]{0,1,2,3,4,5,6,7,8,0}[flowerindex%10]);}
        		
        		this.setBlockState(world, flowerstate, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	
            // Sign
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            if (GeneralConfig.nameSign)
            {
            	int signU = 2;
    			int signV = 1;
    			int signW = 2;
    			int signO = 8;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
                boolean hanging=false;
                
                // Cobblestone foundation
            	this.setBlockState(world, biomeCobblestoneState, signU, signV-1, signW, structureBB);
    			
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);

    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.getCoordBaseMode().getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
        	
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerU = 8;
    			int bannerV = 1;
    			int bannerW = 2;
    			int bannerO = 8;
    			boolean hanging=false;
    			
                // Cobblestone foundation
    			this.setBlockState(world, biomeCobblestoneState, bannerU, bannerV-1, bannerW, structureBB);
    			
    			
    			int bannerX = this.getXWithOffset(bannerU, bannerW);
    			int bannerY = this.getYWithOffset(bannerV);
                int bannerZ = this.getZWithOffset(bannerU, bannerW);

                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);

            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(bannerO, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);
				
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
	        			{1, 1, 5, -1, 0},
	        			{8, 1, 8, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
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
    
    
	// --- Jungle Garden --- //
    // designed by AstroTibs
    
    public static class JungleGarden extends StructureVillageVN.StartVN
    {
        // Make foundation with blanks as empty air, F as foundation spaces, and P as path
        private static final String[] foundationPattern = new String[]{
            	"F     F     F     ",
            	" FFFFFFFFFFFFFFFFF",
            	" FFPFPFPFPFPFPFPPF",
            	" FPPPPPPPPPPPPPPPF",
            	" FFPFPFPFPFPFPFPPF",
            	"FFPPPPPPPPPPPPPPPF",
            	" FFPFPFFFFFFFFFPPP",
            	"PPPPPPFFFFFFFFFPPP",
            	"PPPPPPFFFFFFFFFPPP",
            	"PPPPPPFFFFFFFFFPFF",
            	"FFFFPPFFFFFFFFFPF ",
            	"   FPPFFFFFFFFFPF ",
            	" F FPPFFFFFFFFFPF ",
            	"   FPPFFFFFFFFFPF ",
            	"   FPPFFFFFFFFFPF ",
            	"   FPPPPPPPPPPPPF ",
            	"   FPPPFFFFFFPPPF ",
            	"   FPPPFFFFFFPPPF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;

    	
	    public JungleGarden() {}
		
		public JungleGarden(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, float villageSize)
		{
		    super(biomeProvider, componentType, random, posX, posZ, components, villageSize);
    		
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
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
    		// No roads behind the structure
    		
			// Northward
			if (this.getCoordBaseMode().getHorizontalIndex()!=2)
			{
				StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 4 : this.getCoordBaseMode().getHorizontalIndex()==1 ? 7 : this.getCoordBaseMode().getHorizontalIndex()==3 ? 8 : 0), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
				// Add a second road
				if (this.getCoordBaseMode().getHorizontalIndex()==0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 13, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			}
			// Eastward
			if (this.getCoordBaseMode().getHorizontalIndex()!=3)
			{
				StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 9 : this.getCoordBaseMode().getHorizontalIndex()==1 ? 4 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 6 : 0), EnumFacing.EAST, this.getComponentType());
				// Add a second road
				if (this.getCoordBaseMode().getHorizontalIndex()==1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 13, EnumFacing.EAST, this.getComponentType());}
			}
			// Southward
			if (this.getCoordBaseMode().getHorizontalIndex()!=0)
			{
				StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==1 ? 6 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 4 : this.getCoordBaseMode().getHorizontalIndex()==3 ? 9 : 0), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
				// Add a second road
				if (this.getCoordBaseMode().getHorizontalIndex()==2) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 13, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			}
			// Westward
			if (this.getCoordBaseMode().getHorizontalIndex()!=1)
			{
				StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 8 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 7 : this.getCoordBaseMode().getHorizontalIndex()==3 ? 4 : 0), EnumFacing.WEST, this.getComponentType());
				// Add a second road
				if (this.getCoordBaseMode().getHorizontalIndex()==3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 13, EnumFacing.WEST, this.getComponentType());}
			}
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
        				true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
            
        	// Generate or otherwise obtain village name and banner and colors
        	BlockPos signpos = new BlockPos(6,2,2);
        	
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(signpos.getX(), signpos.getZ()),
        			this.getYWithOffset(signpos.getY()),
        			this.getZWithOffset(signpos.getX(), signpos.getZ()));
        	
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
        	
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{1,1,5}, 
            	{3,1,18}, 
            	{9,1,18}, 
            	{15,1,18}, 
            	{18,1,5}, 
            	// Within the front face
            	{10, 1, -1},
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
            		decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.boundingBox.minY;
            		this.decorHeightY.add(decorHeightY);
            	}
            	else
            	{
            		// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
            		// Retrieve ground level
            		decorHeightY = this.decorHeightY.get(j);
            	}
            	
            	//LogHelper.info("Decor with horizIndex "+this.coordBaseMode+" spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (decorHeightY + this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
        	
			
        	// Set Grass blocks
        	for (int[] grass_uw : new int[][]{
        		{0, 7}, {0, 12}, {0, 17}, 
        		{1, 7}, {1, 11}, {1, 12}, {1, 13}, {1, 14}, {1, 15}, {1, 16}, 
        		{2, 7}, {2, 11}, {2, 13}, {2, 15}, {2, 16}, 
        		{3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {3, 7}, {3, 16}, 
        		{4, 11}, {4, 13}, {4, 15}, {4, 16}, 
        		{5, 16}, 
        		{6, 13}, {6, 15}, {6, 16}, {6, 17}, 
        		{7, 0}, {7, 1}, {7, 16}, 
        		{8, 0}, {8, 1}, {8, 13}, {8, 15}, {8, 16}, 
        		{9, 0}, {9, 1}, {9, 16}, 
        		{10, 0}, {10, 1}, {10, 13}, {10, 15}, {10, 16}, 
        		{11, 0}, {11, 1}, {11, 16}, 
        		{12, 0}, {12, 1}, {12, 13}, {12, 15}, {12, 16}, {12, 17}, 
        		{13, 16}, 
        		{14, 13}, {14, 15}, {14, 16}, 
        		{15, 16}, 
        		{16, 0}, {16, 1}, {16, 2}, {16, 3}, {16, 4}, {16, 5}, {16, 6}, {16, 7}, {16, 8}, {16, 16}, 
        		{17, 8}, {17, 12}, {17, 13}, {17, 14}, {17, 15}, {17, 16}, 
        	})
        	{
        		this.setBlockState(world, biomeGrassState, grass_uw[0], 0, grass_uw[1], structureBB);
        	}
        	
        	
        	// Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Front left shrubs
        		{2,1,7, 3,1,7}, {3,1,1, 3,1,6}, 
        		// Front shrubs
        		{7,1,1, 12,1,1},
        		// Front right
        		{16,1,1, 16,1,8}, {16,1,1, 17,1,1}, 
        		// Back bushes
        		{1,1,12, 1,1,15}, 
        		{2,1,16, 16,1,16}, 
        		{17,1,12, 17,1,15}, 
        		// Bushes around the fountain
        		{6,1,3, 6,1,3}, {6,1,11, 6,1,11}, {14,1,3, 14,1,3}, {14,1,11, 14,1,11}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
        	
        	
        	// Polished diorite blocks
        	IBlockState biomePolishedDioriteBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE.getStateFromMeta(4), this.materialType, this.biome, this.disallowModSubs);;
        	for (int[] uuvvww : new int[][]{
        		// Base
        		{6,0,3, 6,0,3}, {6,0,11, 6,0,11}, {14,0,3, 14,0,3}, {14,0,11, 14,0,11}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomePolishedDioriteBlockState,
        				biomePolishedDioriteBlockState, 
        				false);
            }
            
        	
        	// Diorite wall
        	IBlockState biomeDioriteWallState = ModObjects.chooseModDioriteWallState();
        	if (biomeDioriteWallState==null) {biomeDioriteWallState = Blocks.COBBLESTONE_WALL.getDefaultState();}; // Try cobblestone wall
        	biomeDioriteWallState = StructureVillageVN.getBiomeSpecificBlockState(biomeDioriteWallState, this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Pavilion posts
        		{2,1,15, 2,2,15}, {4,1,15, 4,2,15}, {6,1,15, 6,2,15}, {8,1,15, 8,2,15}, {10,1,15, 10,2,15}, {12,1,15, 12,2,15}, {14,1,15, 14,2,15}, 
        		{2,1,13, 2,2,13}, {4,1,13, 4,2,13}, {6,1,13, 6,2,13}, {8,1,13, 8,2,13}, {10,1,13, 10,2,13}, {12,1,13, 12,2,13}, {14,1,13, 14,2,13}, 
        		{2,1,11, 2,2,11}, {4,1,11, 4,2,11}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeDioriteWallState,
        				biomeDioriteWallState, 
        				false);
            }
        	
        	
        	// Clear out basin
        	this.fillWithAir(world, structureBB, 7,0,4, 13,0,10);
        	
        	
        	// Diorite blocks
        	IBlockState biomeDioriteBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Pavilion toppers
        		{2,3,15, 2,3,15}, {4,3,15, 4,3,15}, {6,3,15, 6,3,15}, {8,3,15, 8,3,15}, {10,3,15, 10,3,15}, {12,3,15, 12,3,15}, {14,3,15, 14,3,15}, 
        		{2,3,13, 2,3,13}, {4,3,13, 4,3,13}, {6,3,13, 6,3,13}, {8,3,13, 8,3,13}, {10,3,13, 10,3,13}, {12,3,13, 12,3,13}, {14,3,13, 14,3,13}, 
        		{2,3,11, 2,3,11}, {4,3,11, 4,3,11}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeDioriteBlockState,
        				biomeDioriteBlockState, 
        				false);
            }
        	
        	
        	// Diorite slabs upper
        	IBlockState biomeDioriteSlabUpperState = ModObjects.chooseModDioriteSlabState(true);
        	if (biomeDioriteSlabUpperState==null) {biomeDioriteSlabUpperState = Blocks.STONE_SLAB.getStateFromMeta(3+8);} // Set to cobblestone slab
        	biomeDioriteSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(biomeDioriteSlabUpperState, this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{2,3,12}, {2,3,14}, 
        		{3,3,11}, {3,3,12}, {3,3,13}, {3,3,14}, {3,3,15}, 
        		{4,3,12}, {4,3,14}, 
        		{5,3,13}, {5,3,14}, {5,3,15}, 
        		{6,3,14}, 
        		{7,3,13}, {7,3,14}, {7,3,15}, 
        		{8,3,14}, 
        		{9,3,13}, {9,3,14}, {9,3,15}, 
        		{10,3,14}, 
        		{11,3,13}, {11,3,14}, {11,3,15}, 
        		{12,3,14}, 
        		{13,3,13}, {13,3,14}, {13,3,15}, 
        		{14,3,14}, 
        		})
            {
        		this.setBlockState(world, biomeDioriteSlabUpperState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Polished diorite slabs lower
        	IBlockState biomePolishedDioriteSlabLowerState = ModObjects.chooseModPolishedDioriteSlabState(false);
        	if (biomePolishedDioriteSlabLowerState==null)
        	{
        		biomePolishedDioriteSlabLowerState = ModObjects.chooseModDioriteBrickSlabState(false); // Try diorite brick slab
        		if (biomePolishedDioriteSlabLowerState==null) // Set to regular diorite slab
            	{
            		biomePolishedDioriteSlabLowerState = ModObjects.chooseModDioriteSlabState(false);
                	if (biomePolishedDioriteSlabLowerState==null) {biomePolishedDioriteSlabLowerState = Blocks.STONE_SLAB.getStateFromMeta(0);} // Set to polished stone slab
            	}
        	}
        	biomePolishedDioriteSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomePolishedDioriteSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{2,4,11, 4,4,12}, {2,4,13, 14,4,15}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePolishedDioriteSlabLowerState, biomePolishedDioriteSlabLowerState, false);
            }
        	
        	
        	// Regular diorite stairs
        	Block biomeDioriteStairsBlock = ModObjects.chooseModDioriteStairsBlock(); 
        	if (biomeDioriteStairsBlock==null) {biomeDioriteStairsBlock = Blocks.STONE_STAIRS;} // Set to cobblestone stairs
        	biomeDioriteStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomeDioriteStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Fountain outer lining
        		{7,0,3, 2}, {8,0,3, 2}, {9,0,3, 2}, {10,0,3, 2}, {11,0,3, 2}, {12,0,3, 2}, {13,0,3, 2}, 
        		{6,0,4, 1}, {6,0,5, 1}, {6,0,6, 1}, {6,0,7, 1}, {6,0,8, 1}, {6,0,9, 1}, {6,0,10, 1}, 
        		{7,0,11, 3}, {8,0,11, 3}, {9,0,11, 3}, {10,0,11, 3}, {11,0,11, 3}, {12,0,11, 3}, {13,0,11, 3}, 
        		{14,0,4, 0}, {14,0,5, 0}, {14,0,6, 0}, {14,0,7, 0}, {14,0,8, 0}, {14,0,9, 0}, {14,0,10, 0}, 
        		// Fountain inner lining
        		{8,0,5, 3}, {9,0,5, 3}, {10,0,5, 3}, {11,0,5, 3}, 
        		{12,0,5, 1}, {12,0,6, 1}, {12,0,7, 1}, {12,0,8, 1}, 
        		{9,0,9, 2}, {10,0,9, 2}, {11,0,9, 2}, {12,0,9, 2}, 
        		{8,0,6, 0}, {8,0,7, 0}, {8,0,8, 0}, {8,0,9, 0}, 
        		})
            {
        		this.setBlockState(world, biomeDioriteStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
            // Sand
        	IBlockState biomeSandBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SAND.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	{7,-1,4, 13,-1,4}, {7,-1,5, 7,-1,9}, {7,-1,10, 13,-1,10}, {13,-1,5, 13,-1,9}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandBlockState, biomeSandBlockState, false);
            }
            
        	
            // Stone brick
        	IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Basin bottom
            	{8,-1,5, 12,-1,9}, 
            	// Fountain
            	{10,0,7, 10,0,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
            }
            
            
            // Chiseled Stone brick
            IBlockState biomeChiseledStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Fountain head
            	{10,1,7, 10,1,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeChiseledStoneBrickState, biomeChiseledStoneBrickState, false);
            }
            
            
            // Water
            this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), 10, 2, 7, structureBB);
            
            
        	// Trees
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SAPLING.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwss : new int[][]{ // u,v,w, ushift,wshift: Which adjacent spaces to use if this is a Dark Oak sapling
        		// Around pavilion
        		{0,1,12, -1,1},
        		{0,1,17, -1,1},
        		{6,1,17, -1,1},
        		{12,1,17, -1,1},
        		// In front of fountain
        		{8,1,0, -1,-1},
        		{11,1,0, 1,-1},
        		})
            {
        		Block dirtblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();
        		
        		// Don't place if there's no dirt beneath to grow
        		if (dirtblock==null || (dirtblock != Blocks.DIRT && dirtblock != Blocks.GRASS)) {continue;}
        		// Don't place if the sapling can't see the sky
        		if (!world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2])))) {continue;}
        		// Dark oak version of the above
        		if (biomeSaplingState.getBlock().getMetaFromState(biomeSaplingState)==5)
        		{
        			Block dirtblock1 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]))).getBlock();
        			Block dirtblock2 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]+uvwss[4]))).getBlock();
        			Block dirtblock3 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]))).getBlock();
        			
        			if (dirtblock1==null || dirtblock2==null || dirtblock3==null || // Foundation blocks are null
        					// Foundation blocks can't support growing a tree
        					(dirtblock1 != Blocks.DIRT && dirtblock1 != Blocks.GRASS)
        					|| (dirtblock2 != Blocks.DIRT && dirtblock2 != Blocks.GRASS)
        					|| (dirtblock3 != Blocks.DIRT && dirtblock3 != Blocks.GRASS)
        					// Foundation blocks can't see the sky
        					|| !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2])))
        					|| !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2]+uvwss[4])))
        					|| !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4])))
        					)
        			{
        				continue;
        			}
        		}
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
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
            		// Left side
        			{1,2,13, 3}, {1,3,13, 3}, 
        			// Back side
        			{4,2,16, 0}, {4,3,16, 0}, 
        			{6,2,16, 0}, {6,3,16, 0}, 
        			{8,2,16, 0}, {8,3,16, 0}, 
        			{12,3,16, 0}, 
        			{12,3,16, 0},
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
            
            // Sign
            IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            if (GeneralConfig.nameSign)
            {
            	int signU = 1;
    			int signV = 1;
    			int signW = 11;
    			int signO = 8;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
                boolean hanging=false;
                
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);

    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.getCoordBaseMode().getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
        	
            
            // Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerU = 1;
    			int bannerV = 1;
    			int bannerW = 7;
    			int bannerO = 0;
    			
    			int bannerX = this.getXWithOffset(bannerU, bannerW);
    			int bannerY = this.getYWithOffset(bannerV);
                int bannerZ = this.getZWithOffset(bannerU, bannerW);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
                world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(bannerO, this.getCoordBaseMode().getHorizontalIndex(), false)), 2);

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
	        			{5, 1, 2, -1, 0}, 
	        			{7, 1, 14, -1, 0}, 
	        			{11, 1, 4, -1, 0}, 
	        			{15, 1, 10, -1, 0}, 
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
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
    
    
	// --- Jungle Villa --- //
    // designed by AstroTibs
    
    public static class JungleVilla extends StructureVillageVN.StartVN
    {
        // Make foundation with blanks as empty air, F as foundation spaces, and P as path
        private static final String[] foundationPattern = new String[]{
            	"                ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF ",
            	"PPPPFFFFFFFFFFF ",
            	"PPPPPFFFFFFFFFF ",
            	"PPPPPPPFFFFFFFF ",
            	"  PPPPPFFFFFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 8;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 10;

    	
	    public JungleVilla() {}
		
		public JungleVilla(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, float villageSize)
		{
		    super(biomeProvider, componentType, random, posX, posZ, components, villageSize);
    		
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
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			if (this.getCoordBaseMode().getHorizontalIndex()!=2) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 3 : this.getCoordBaseMode().getHorizontalIndex()==1 ? 11 : 1), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			// Eastward
			if (this.getCoordBaseMode().getHorizontalIndex()==1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (3), EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.getCoordBaseMode().getHorizontalIndex()==2) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (3), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.getCoordBaseMode().getHorizontalIndex()!=1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()==0 ? 1 : this.getCoordBaseMode().getHorizontalIndex()==2 ? 11 : 3), EnumFacing.WEST, this.getComponentType());}
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLvl < 0)
            {
        		this.averageGroundLvl = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
        				true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
            
        	// Generate or otherwise obtain village name and banner and colors
        	BlockPos signpos = new BlockPos(6,2,2);
        	
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(signpos.getX(), signpos.getZ()),
        			this.getYWithOffset(signpos.getY()),
        			this.getZWithOffset(signpos.getX(), signpos.getZ()));
        	
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
        	for (int[] grass_uw : new int[][]{
        		{0, 4}, {1, 4}, {2, 4}, {3, 4}, 
        	})
        	{
        		this.setBlockState(world, biomeGrassState, grass_uw[0], 0, grass_uw[1], structureBB);
        	}
        	
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	// Ground-level foundation
            	{0,1,5, 4,1,13}, 
            	{5,1,4, 5,1,13}, 
            	{6,1,3, 6,1,13}, 
            	{7,1,0, 7,1,13}, 
            	{8,1,0, 14,1,6}, 
            	{8,1,9, 12,1,9}, 
            	{8,1,12, 12,1,13}, 
            	{10,1,7, 10,1,8}, 
            	{10,1,10, 10,1,11}, 
            	{13,1,0, 14,1,13}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
            }
        	
            
        	// Cobblestone stairs
            Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward; +4:inverted
        		// Front stairs
        		{4,1,4, 0},
        		{4,1,3, 3}, {5,1,3, 3}, 
        		{5,1,2, 0},
        		{6,1,2, 3}, 
        		})
            {
        		this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{0,2,5, 4,2,5}, 
            	{0,2,6, 0,2,13}, 
            	{1,2,13, 1,2,13}, 
            	{14,2,0, 14,2,2}, 
            	{7,2,0, 13,2,0}, 
            	{7,2,1, 7,2,2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{10,2,1, 10,3,1}, 
            	{12,2,1, 12,3,1}, 
            	{14,3,1, 14,3,1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		
        		// Front left
        		{8,1,7, 1, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{9,1,7, 0, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{8,1,8, 2, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{9,1,8, 3, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		
        		// Front right
        		{11,1,7, 0, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{12,1,7, 3, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{11,1,8, 1, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{12,1,8, 2, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		
        		// Back left
        		{8,1,10, 2, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{9,1,10, 1, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{8,1,11, 3, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{9,1,11, 0, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		
        		// Back right
        		{11,1,10, 3, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{12,1,10, 2, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{11,1,11, 0, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{12,1,11, 1, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], StructureVillageVN.chooseGlazedTerracottaMeta(uvwoc[3], this.getCoordBaseMode()));
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
        	
        	
            // Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Lower floor
        		{2,2,9, 2,2,13}, 
        		{2,2,9, 2,3,9}, {2,2,11, 2,3,11}, {2,2,13, 2,3,13}, 
        		{2,4,9, 2,4,13}, 
        		{3,4,9, 6,4,9}, {4,2,9, 4,3,9}, {6,2,9, 6,3,9}, 
        		{7,4,6, 7,4,8}, {7,2,8, 7,3,8}, {7,2,6, 7,3,6}, 
        		{8,4,6, 9,4,6}, {9,2,6, 9,3,6}, 
        		{10,4,1, 10,4,5}, {10,2,5, 10,3,5}, {10,2,4, 10,2,4}, {10,2,3, 10,3,3}, 
        		{10,4,1, 14,4,3}, {11,2,3, 11,3,3}, {13,2,3, 14,3,3}, 
        		{14,2,4, 14,2,13}, {14,3,5, 14,3,5}, {14,3,7, 14,3,7}, {14,3,9, 14,3,9}, {14,3,11, 14,3,11}, {14,3,13, 14,3,13}, {14,4,4, 14,4,13}, 
        		{3,2,13, 13,2,13}, {4,3,13, 4,3,13}, {6,3,13, 6,3,13}, {8,3,13, 8,3,13}, {10,3,13, 10,3,13}, {12,3,13, 12,3,13}, {3,4,13, 13,4,13},
        		// Upper floor
        		{2,6,9, 2,6,9}, {2,6,11, 2,6,11}, {2,6,13, 2,6,13}, {2,7,9, 2,7,13}, 
        		{4,6,9, 4,6,9}, {6,6,9, 6,6,9}, {8,6,9, 10,6,9}, {3,7,9, 10,7,9}, 
        		{10,6,5, 10,7,8}, {10,7,4, 10,7,4}, {10,6,3, 10,7,3}, 
        		{11,7,3, 11,7,3}, {12,6,3, 12,8,3}, {13,7,3, 13,7,3}, {14,6,3, 14,7,3}, 
        		{14,6,5, 14,7,5}, {14,6,7, 14,7,7}, {14,6,9, 14,7,9}, {14,6,11, 14,7,11}, {14,6,13, 14,7,13}, 
        		{14,7,4, 14,7,4}, {14,7,6, 14,7,6}, {14,7,8, 14,7,8}, {14,7,10, 14,7,10}, {14,7,12, 14,7,12}, 
        		{12,6,13, 12,7,13}, {10,6,13, 10,7,13}, {8,6,13, 8,7,13}, {6,6,13, 6,7,13}, {4,6,13, 4,7,13}, 
        		{13,7,13, 13,7,13}, {11,7,13, 11,7,13}, {9,7,13, 9,7,13}, {7,7,13, 7,7,13}, {5,7,13, 5,7,13}, {3,7,13, 3,7,13}, 
        		})
            {
        		// White
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0), 
        				false);
            }
            
        	
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Downstairs supports
        		{7,2,9, 7,4,9}, {10,2,6, 10,4,6}, 
        		// Floor separator
        		{2,5,9, 10,5,9}, {10,5,3, 10,5,8}, {12,5,3, 14,5,3}, {14,5,4, 14,5,13}, {2,5,13, 13,5,13}, {2,5,10, 2,5,12}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front awning
            	{7,5,6, 7,5,8}, {8,5,6, 9,5,6}, {9,6,8, 9,6,8}, 
            	// Roof
            	{11,8,3, 11,8,3}, {13,8,3, 13,8,3}, 
            	{11,8,5, 13,8,5}, {11,8,7, 13,8,7}, {11,8,9, 13,8,9}, {12,8,11, 13,8,11}, {12,8,12, 12,8,12}, 
            	{10,8,10, 10,8,12}, {8,8,10, 8,8,12}, {6,8,9, 6,8,13}, {4,8,10, 4,8,12}, 
            	{11,8,11, 11,8,11}, {12,8,10, 12,8,10},
            	// Downstairs
            	{3,2,12, 3,2,12}, // Desk with flower pot
            	{8,2,12, 8,2,12}, // Under the stairs
            	{10,2,12, 10,2,12}, {13,2,9, 13,2,9}, // Couch ends
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Couch
        		{11,2,12, 3}, {12,2,12, 3}, {13,2,12, 3}, {13,2,11, 0}, {13,2,10, 0}, 
        		// Stairwell
        		{7,2,12, 0}, {8,3,12, 0}, {9,4,12, 0}, 
        		// Table
        		{13,5,10, 3+4}, {13,5,8, 2+4}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof trim
            	{9,7,2, 9,7,8}, {1,7,8, 8,7,8}, {1,7,9, 1,7,14}, {2,7,14, 15,7,14}, {15,7,2, 15,7,13}, 
            	// Roof panels
            	{11,8,2, 11,8,2}, {13,8,2, 13,8,2}, 
            	{11,8,4, 13,8,4}, {11,8,6, 13,8,6}, {11,8,8, 13,8,8}, {11,8,10, 11,8,10}, {13,8,10, 13,8,10}, {11,8,12, 11,8,12}, {13,8,12, 13,8,12}, 
            	{9,8,10, 9,8,12}, {7,8,9, 7,8,13}, {5,8,9, 5,8,13}, {3,8,10, 3,8,12},
            	// floor separator
            	{3,4,10, 4,4,12}, {5,4,10, 9,4,11}, {8,4,9, 9,4,9}, {10,4,7, 10,4,12}, {10,4,7, 10,4,12}, {11,4,4, 13,4,12}, 
            	// Table
            	{13,5,9, 13,5,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Awning
            	{6,5,5, 6,5,8}, {7,5,5, 9,5,5}, {8,6,7, 8,6,8}, {9,6,7, 9,6,7}, 
            	// Roof
            	{10,8,2, 10,8,9}, {8,8,9, 9,8,9}, {2,8,9, 4,8,9}, {2,8,10, 2,8,13}, {3,8,13, 4,8,13}, {8,8,13, 14,8,13}, {14,8,2, 14,8,12}, 
            	{12,9,2, 12,9,11}, {4,9,11, 11,9,11}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
            }
        	
        	
            // Table
            IBlockState[] tableComponentBlockstates = ModObjects.chooseModWoodenTable(biomePlankState.getBlock()==Blocks.PLANKS ? biomePlankState.getBlock().getMetaFromState(biomePlankState) : 0);
        	for (int[] uuvvww : new int[][]{
        		{13,5,12}, 
        		{13,5,6}, 
        		})
            {
        		for (int i=1; i>=0; i--)
        		{
        			this.setBlockState(world, tableComponentBlockstates[i], uuvvww[0], uuvvww[1]+1-i, uuvvww[2], structureBB);
        		}
            }
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{10,2,9, 10,4,9}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
        	
            
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	// Interior railing
            	{4,5,11, 4,5,12}, {4,5,11, 9,5,11}, 
            	// Exterior railing
            	{10,5,1, 10,5,2}, {11,5,1, 13,5,1}, {14,5,1, 14,5,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Downstairs
        		{2,3,10}, {2,3,12}, 
        		{3,3,13}, {5,3,13}, {7,3,13}, {9,3,13}, {11,3,13}, {13,3,13}, 
        		{14,3,12}, {14,3,10}, {14,3,8}, {14,3,6}, {14,3,4}, 
        		{10,3,4}, 
        		// Upstairs
        		{2,6,10}, {2,6,12}, 
        		{3,6,13}, {5,6,13}, {7,6,13}, {9,6,13}, {11,6,13}, {13,6,13}, 
        		{14,6,12}, {14,6,10}, {14,6,8}, {14,6,6}, {14,6,4}, {13,6,3}, 
        		{10,6,4}, 
        		{3,6,9}, {5,6,9}, {7,6,9}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{4,3,5, -1}, {0,3,5, -1}, {0,3,13, -1}, {14,3,0, -1}, {7,3,0, -1}, {7,3,2, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	// Interior
            	{4,7,11}, {12,7,11}, {12,7,5}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// On the couch ends
            	{10,3,12}, {13,3,9}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Potted Cactus
            int flowernumber = 9; // Cactus
            
        	int potU = 13;
        	int potV = 6;
        	int potW = 9;
        	int potX = this.getXWithOffset(potU, potW);
        	int potY = this.getYWithOffset(potV);
        	int potZ = this.getZWithOffset(potU, potW);
        	
        	TileEntity flowerPot = (new BlockFlowerPot()).createNewTileEntity(world, flowernumber);
    		BlockPos flowerPotPos = new BlockPos(potX, potY, potZ);
    		world.setBlockState(flowerPotPos, Blocks.FLOWER_POT.getDefaultState());
    		world.setTileEntity(flowerPotPos, flowerPot);
    		
            
            // Potted random flower
            int u=3; int v=3; int w=12;
            int x = this.getXWithOffset(u, w);
            int y = this.getYWithOffset(v);
            int z = this.getZWithOffset(u, w);
            
            IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
    		int randomPottedPlant = random.nextInt(10)-1;
    		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
    		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 3;
        	int chestV = 5;
        	int chestW = 12;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            
        	// Trees
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SAPLING.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwss : new int[][]{ // u,v,w, ushift,wshift: Which adjacent spaces to use if this is a Dark Oak sapling
        		{-2,1,6, -1,1},
        		})
            {
        		Block dirtblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();
        		Block saplingblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();
        		
        		// Don't place if there's no dirt beneath to grow
        		if (dirtblock==null || (dirtblock != Blocks.DIRT && dirtblock != Blocks.GRASS)) {continue;}
        		// Don't place if the sapling can't see the sky
        		if (!world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2])))) {continue;}
        		// Dark oak version of the above
        		if (biomeSaplingState.getBlock().getMetaFromState(biomeSaplingState)==5)
        		{
        			Block dirtblock1 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]))).getBlock();
        			Block dirtblock2 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]+uvwss[4]))).getBlock();
        			Block dirtblock3 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]))).getBlock();
        			
        			if (dirtblock1==null || dirtblock2==null || dirtblock3==null || // Foundation blocks are null
        					// Foundation blocks can't support growing a tree
        					(dirtblock1 != Blocks.DIRT && dirtblock1 != Blocks.GRASS)
        					|| (dirtblock2 != Blocks.DIRT && dirtblock2 != Blocks.GRASS)
        					|| (dirtblock3 != Blocks.DIRT && dirtblock3 != Blocks.GRASS)
        					// Foundation blocks can't see the sky
        					|| !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2])))
        					|| !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2]+uvwss[4])))
        					|| !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4])))
        					)
        			{
        				continue;
        			}
        		}
        		
        		
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
        	
            
            // Sign
            IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            if (GeneralConfig.nameSign)
            {
            	int signU = 1;
    			int signV = 1;
    			int signW = 4;
    			int signO = 8;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
                boolean hanging=false;
                
                // Generate sign contents manually so that you can substitute the "Village of..." stuff
        		TileEntitySign signContents = new TileEntitySign();
        		
            	String topLine = "Villa at";
        		topLine = topLine.trim();
        		
        		if ( (namePrefix.length() + 1 + nameRoot.length()) > 15 )
        		{
        			// Prefix+Root is too long, so move prefix to line 1
        			signContents.signText[0] = new TextComponentString(GeneralConfig.headerTags.trim() + topLine.trim());
        			signContents.signText[1] = new TextComponentString(namePrefix.trim());
        			if ( (nameRoot.length() + 1 + nameSuffix.length()) > 15 )
        			{
        				// Root+Suffix is too long, so move suffix to line 3
        				signContents.signText[2] = new TextComponentString(nameRoot.trim());
        				signContents.signText[3] = new TextComponentString(nameSuffix.trim());
        			}
        			else
        			{
        				// Fit Root+Suffix onto line 2
        				signContents.signText[2] = new TextComponentString((nameRoot+" "+nameSuffix).trim());
        			}
        		}
        		else if ( (namePrefix.length() + 1 + nameRoot.length() + 1 + nameSuffix.length()) <= 15 )
        		{
        			// Whole name fits on one line! Put it all on line 2.
        			signContents.signText[1] = new TextComponentString(GeneralConfig.headerTags.trim() + topLine);
        			signContents.signText[2] = new TextComponentString((namePrefix+" "+nameRoot+" "+nameSuffix).trim());
        		}
        		else
        		{
        			// Only Prefix and Root can fit together on line 2.
        			signContents.signText[1] = new TextComponentString(GeneralConfig.headerTags.trim() + topLine.trim());
        			signContents.signText[2] = new TextComponentString((namePrefix+" "+nameRoot).trim());
        			signContents.signText[3] = new TextComponentString(nameSuffix.trim());
        		}
        		// If top line is blank, roll everything up one line:
        		if (topLine.equals(""))
        		{
        			for (int isign=0; isign <3; isign++)
        			{
        				signContents.signText[isign] = signContents.signText[isign+1];
        			}
        			signContents.signText[3] = new TextComponentString("");
        		}
        		
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.getCoordBaseMode().getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
        	
            
            // Banner
    		if (GeneralConfig.villageBanners)
    		{
                int bannerU = 9;
    			int bannerV = 4;
    			int bannerW = 1;
    			int bannerO = 3;
    			boolean hanging=true;
    			
    			int bannerX = this.getXWithOffset(bannerU, bannerW);
    			int bannerY = this.getYWithOffset(bannerV);
                int bannerZ = this.getZWithOffset(bannerU, bannerW);

                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
                world.setBlockState(bannerPos, Blocks.WALL_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(bannerO, this.getCoordBaseMode().getHorizontalIndex(), true)), 2);
				
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
					modifystanding.setInteger("Base", 15 - 0);
    				tilebanner.readFromNBT(modifystanding);
				}
				
        		world.setTileEntity(bannerPos, tilebanner);
    		}
			
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{12, 5, 2, -1, 0}, // On the balcony 
	        			{6, 2, 4, -1, 0}, // At the entrance 
	        			{12, 2, 11, -1, 0}, // On the couch 
	        			{3, 5, 11, -1, 0}, // In front of the chest 
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
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
    
    
    // --- Armorer House --- //
    // designed by jss2a98aj
    
    public static class JungleArmorerHouse extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"  FFFFF  ", 
    			" FFFFFFF ", 
    			"FFFFFFFFF", 
    			"FFFFFFFFF", 
    			"FFFFFFFFF", 
    			"FFFFFFFFF", 
    			" FFFFFFF ", 
    			"  FPPPF  ", 
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleArmorerHouse() {}

        public JungleArmorerHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleArmorerHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleArmorerHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{0,0,3, 0,0,5}, 
    			{1,0,2, 1,0,6}, 
    			{2,0,1, 2,0,7}, 
    			{3,0,1, 3,0,2}, {3,0,5, 3,0,7}, 
    			{4,0,1, 4,0,2}, {4,0,6, 4,0,7}, 
    			{5,0,1, 5,0,3}, {5,0,6, 5,0,7}, 
    			{6,0,1, 6,0,7}, 
    			{7,0,2, 7,0,6}, 
    			{8,0,3, 8,0,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
            // Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Front wall
        		{2,1,1, 2,2,1}, {4,1,1, 4,2,1}, {6,1,1, 6,2,1}, {2,3,1, 6,3,1}, 
        		// Left wall 
        		{0,1,3, 0,3,3}, {0,1,5, 0,3,5}, {0,1,4, 0,1,4}, {0,3,4, 0,3,4}, 
        		// Right wall 
        		{8,1,3, 8,3,3}, {8,1,5, 8,3,5}, {8,1,4, 8,1,4}, {8,3,4, 8,3,4}, 
        		// Back wall 
        		{2,1,7, 2,3,7}, {6,1,7, 6,3,7}, 
        		})
            {
        		// White
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0), 
        				false);
            }
    		
    		
    		// For stripped logs specifically
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
    		for (int[] uuvvww : new int[][]{
    			// Corner pillars
    			{1,1,6, 1,3,6}, {7,1,6, 7,3,6}, 
    			{1,1,2, 1,3,2}, {7,1,2, 7,3,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			// Ceiling support
    			{2,4,6, 6,4,6}, 
    			{2,4,2, 6,4,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
    		}
    		
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			// Ceiling support
    			{1,4,3, 1,4,5}, 
    			{4,4,3, 4,4,5}, 
    			{7,4,3, 7,4,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Front lantern mount
    			{4,4,1, 4,4,1}, 
    			// Roof
    			{3,6,4, 5,6,4}, 
    			// Abutting the chimney
    			{4,5,6, 4,5,6}, 
    			// Top of stripped log columns
    			{1,4,6, 1,4,6}, {7,4,6, 7,4,6}, 
    			{1,4,2, 1,4,2}, {7,4,2, 7,4,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Exterior
    			// Front
    			{1,4,1, 3}, {2,4,1, 3}, {3,4,1, 3}, {5,4,1, 3}, {6,4,1, 3}, {7,4,1, 3}, 
    			{1,5,2, 3}, {2,5,2, 3}, {3,5,2, 3}, {4,5,2, 3}, {5,5,2, 3}, {6,5,2, 3}, {7,5,2, 3}, 
    			// Left
    			{0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, {0,4,6, 0}, 
    			{1,5,3, 0}, {1,5,4, 0}, {1,5,5, 0}, 
    			// Right
    			{8,4,2, 1}, {8,4,3, 1}, {8,4,4, 1}, {8,4,5, 1}, {8,4,6, 1}, 
    			{7,5,3, 1}, {7,5,4, 1}, {7,5,5, 1}, 
    			// Back
    			{1,4,7, 2}, {2,4,7, 2}, {3,4,7, 2}, {5,4,7, 2}, {6,4,7, 2}, {7,4,7, 2}, 
    			{1,5,6, 2}, {2,5,6, 2}, {3,5,6, 2}, {5,5,6, 2}, {6,5,6, 2}, {7,5,6, 2}, 
    			// Interior
    			{2,5,3, 1+4}, {2,5,4, 1+4}, {2,5,5, 1+4}, 
    			{6,5,3, 0+4}, {6,5,4, 0+4}, {6,5,5, 0+4}, 
    			// Desks
    			{1,1,3, 2+4}, {1,1,5, 3+4}, 
    			{7,1,3, 2+4}, {7,1,5, 3+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{2,6,5, 6,6,5}, 
    			{2,6,4, 2,6,4}, {6,6,4, 6,6,4}, 
    			{2,6,3, 6,6,3}, 
    			// Tables
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Desks
    			{1,1,4, 1,1,4}, {7,1,4, 7,1,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{4,4,0, 4,4,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Furnace
    			{3,1,6, 3,1,6}, {4,2,6, 4,2,6}, {5,1,6, 5,1,6}, {3,1,7, 5,3,7}, 
    			// Chimney
    			{4,4,7, 4,6,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
    		}
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Furnace
    			{3,2,6, 0}, {4,3,6, 3}, {5,2,6, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Trapdoor (Top Horizontal)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shelves
            	{2,2,6, 2}, {6,2,6, 2}, 
            	{2,1,6, 2}, {6,1,6, 2}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, false)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		// Front door
        		{4,3,0}, 
        		// Interior
        		{4,3,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Left wall
        		{0,2,4}, 
        		// Right wall
        		{8,2,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		
        		// Floor
        		{3,0,4, 0, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{5,0,4, 0, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{4,0,4, 1, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{4,0,3, 2, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{4,0,5, 2, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{3,0,3, 3, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{5,0,5, 3, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], StructureVillageVN.chooseGlazedTerracottaMeta(uvwoc[3], this.getCoordBaseMode()));
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
            
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{4,1,6, 2}, 
        		})
            {
        		IBlockState blastFurnaceState = ModObjects.chooseModBlastFurnaceState(uvwo[3], this.getCoordBaseMode());
                this.setBlockState(world, blastFurnaceState.getBlock().getStateFromMeta(0), uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), blastFurnaceState, 2);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{3,1,1, 2, 1, 1}, 
    			{5,1,1, 2, 1, 0}, 
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
				
				{2,4,4, 1, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
				{6,4,4, 3, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
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
				modifystanding.setInteger("Base", 15 - uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
        	
        	
        	// Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Front shrubs
        		{0,1,2, 0,1,2}, {1,1,1, 1,1,1}, {2,1,0, 2,1,0}, 
        		{8,1,2, 8,1,2}, {7,1,1, 7,1,1}, {6,1,0, 6,1,0}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Forward
        			{2,1,8, 0}, 
        			{3,3,8, 0}, {3,2,8, 0}, {3,1,8, 0}, 
        			{4,4,8, 0}, {4,3,8, 0}, 
        			{7,2,7, 0}, {7,1,7, 0}, 
        			// Rightward
        			{8,3,6, 1}, {8,2,6, 1}, {8,1,6, 1}, 
        			// Backward
        			{0,3,2, 2}, {7,3,1, 2}, {7,2,1, 2}, 
        			// Leftward
        			{1,2,1, 3}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int u = 2+random.nextInt(5);
    			int v = 1;
    			int w = 2+random.nextInt(4);
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 1, 0); // Armorer
    			
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
    
    
    // --- Butcher Shop --- //
    // designed by Lonemind
    
    public static class JungleButcherShop extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"FFFFFFFFFF", 
    			"FFFFFFFFFF", 
    			"FFPFFFFFFF", 
    			"FFFFPPFFFF", 
    			"FFFFFFFPFF", 
    			"FFFPFFFFFF", 
    			"FFPFFFFFF ", 
    			"FFFFFFFFF ", 
    			"FFFFFFFFF ", 
    			"FFFFFFFFF ", 
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 11;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 4;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleButcherShop() {}

        public JungleButcherShop(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleButcherShop buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleButcherShop(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	// Pen
        		{2,1,1, 4,1,1}, 
        		{1,1,2, 1,1,7}, 
        		{2,1,8, 7,1,8}, 
        		{8,1,5, 8,1,7}, 
        		{6,1,4, 7,1,4}, 
        		// Smoker chimney
        		{2,6,7, 2,7,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Chimney
    			{2,9,6, 3}, {1,9,7, 0}, {2,9,8, 2}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Torch posts
        		{4,1,5, 4,1,5}, {5,1,0, 5,1,0}, {8,1,0, 8,1,0}, 
        		// Windows
        		{2,6,1, 4,6,1}, 
        		{1,6,3, 1,6,6}, 
        		{3,6,8, 6,6,8}, 
        		{5,6,2, 5,6,3}, 
        		{8,6,6, 8,6,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Smooth Stone Double Slab
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,5,2}, {3,5,2}, {4,5,2}, 
            	{4,5,3}, {4,5,4}, {4,5,6}, {4,5,7}, 
            	}) {
            	this.setBlockState(world, Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(0), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
    		
    		
    		// Logs (Vertical) Part 1
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{5,1,4, 5,7,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// On fence posts
    			{4,2,5, -1}, {5,2,0, -1}, {8,2,0, -1}, 
    			// On counter
    			{4,6,4, 3}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Foundation
    			{5,1,2, 5,1,3}, 
    			// Chimney
    			{1,8,7, 1,8,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Logs (Vertical) Part 2
    		for(int[] uuvvww : new int[][]{
    			// Corner beams
    			{1,1,8, 1,7,8}, {8,1,8, 8,7,8}, 
    			{8,1,4, 8,7,4}, 
    			{1,1,1, 1,7,1}, {5,1,1, 5,7,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			// House support
    			{2,4,8, 7,4,8}, 
    			{2,4,1, 4,4,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
    		}
    		
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			// Ceiling support
    			{1,4,2, 1,4,7}, 
    			{5,4,2, 5,4,3}, {8,4,5, 8,4,7}, 
    			// Ceiling singletons
    			{3,8,1, 3,8,1}, 
    			{3,8,8, 3,8,8}, {6,8,8, 6,8,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Under the house
    			{5,2,2, 5,3,3}, 
    			// Floor
    			{2,4,2, 4,4,7}, {5,4,5, 7,4,7}, 
    			// Window rim
    			{1,5,2, 1,5,7}, {1,6,2, 1,6,2}, {1,6,7, 1,6,7}, {1,7,2, 1,7,7}, 
    			{2,5,1, 4,5,1}, {2,8,1, 2,8,1}, {4,8,1, 4,8,1}, {2,7,1, 4,7,1}, 
    			{5,5,2, 5,5,3}, {5,7,2, 5,7,3}, 
    			{8,5,5, 8,5,7}, {8,6,5, 8,6,5}, {8,6,7, 8,6,7}, {8,7,5, 8,7,7}, 
    			{2,5,8, 7,5,8}, {2,6,8, 2,6,8}, {7,6,8, 7,6,8}, {2,7,8, 7,7,8}, {2,8,8, 2,8,8}, {4,8,8, 5,8,8}, {7,8,8, 7,8,8}, 
    			// Ceiling
    			{2,8,2, 4,8,6}, {2,8,8, 2,8,8}, {5,8,5, 7,8,7}, {3,8,7, 4,8,7}, 
    			// Roof
    			{3,9,0, 3,9,5}, {3,9,6, 6,9,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Entry
    			{6,4,4, 3}, {7,4,4, 3}, 
    			{6,3,3, 3}, {7,3,3, 3}, 
    			{6,2,2, 3}, {7,2,2, 3}, 
    			{6,1,1, 3}, {7,1,1, 3}, 
    			// Underneath entry
    			{6,1,2, 2+4}, {7,1,2, 2+4}, 
    			{6,2,3, 2+4}, {7,2,3, 2+4}, 
    			{6,3,4, 2+4}, {7,3,4, 2+4}, 
    			// Interior ceiling trim
    			{2,7,2, 2+4}, {3,7,2, 2+4}, {4,7,2, 2+4}, 
    			{4,7,3, 0+4}, {4,7,4, 0+4}, 
    			{4,7,5, 2+4}, {5,7,5, 2+4}, {6,7,5, 2+4}, 
    			{7,7,6, 0+4}, {7,7,5, 0+4}, 
    			{3,7,7, 3+4}, {4,7,7, 3+4}, {5,7,7, 3+4}, {6,7,7, 3+4}, {7,7,7, 3+4}, 
    			{2,7,6, 1+4}, {2,7,5, 1+4}, {2,7,4, 1+4}, {2,7,3, 1+4}, 
    			// Roof
    			{0,7,0, 0}, {0,7,1, 0}, {0,7,2, 0}, {0,7,3, 0}, {0,7,4, 0}, {0,7,5, 0}, {0,7,6, 0}, {0,7,7, 0}, {0,7,8, 0}, {0,7,9, 0}, 
    			{1,8,0, 0}, {1,8,1, 0}, {1,8,2, 0}, {1,8,3, 0}, {1,8,4, 0}, {1,8,5, 0}, {1,8,6, 0}, {1,8,8, 0}, {1,8,9, 0}, 
    			{6,7,0, 1}, {6,7,1, 1}, {6,7,2, 1}, {6,7,3, 1}, 
    			{5,8,0, 1}, {5,8,1, 1}, {5,8,2, 1}, {5,8,3, 1}, {5,8,4, 1}, 
    			{7,7,3, 3}, {8,7,3, 3}, {9,7,3, 3}, 
    			{6,8,4, 3}, {7,8,4, 3}, {8,8,4, 3}, 
    			{9,7,4, 1}, {9,7,5, 1}, {9,7,6, 1}, {9,7,7, 1}, {9,7,8, 1}, {9,7,9, 1}, 
    			{8,8,5, 1}, {8,8,6, 1}, {8,8,7, 1}, {8,8,8, 1}, {8,8,9, 1}, 
    			// Roof trim
    			{1,7,0, 1+4}, {2,8,0, 1+4}, {4,8,0, 0+4}, {5,7,0, 0+4}, // Front
    			{1,7,9, 1+4}, {2,8,9, 1+4}, {7,8,9, 0+4}, {8,7,9, 0+4}, // Rear
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{2,9,0, 2,9,5}, 
    			{2,9,9, 7,9,9}, 
    			{7,9,5, 7,9,8}, 
    			{4,9,5, 6,9,5}, 
    			{4,9,0, 4,9,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Put in place so the butcher can't jump the counter and run away
    			{4,7,6, 4,7,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
            
            
            // Trapdoor (Top Horizontal)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Counter
            	{4,5,5, 2}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, false)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Vertical)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Chimney
            	{2,10,6, 2}, 
            	{3,10,7, 1}, 
            	{2,10,8, 0}, 
            	{1,10,7, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
                    	
            // Table
            IBlockState[] tableComponentBlockstates = ModObjects.chooseModWoodenTable(biomePlankState.getBlock()==Blocks.PLANKS ? biomePlankState.getBlock().getMetaFromState(biomePlankState) : 0);
        	for (int[] uuvvww : new int[][]{
        		{2,5,5}, 
        		})
            {
        		for (int i=1; i>=0; i--)
        		{
        			this.setBlockState(world, tableComponentBlockstates[i], uuvvww[0], uuvvww[1]+1-i, uuvvww[2], structureBB);
        		}
            }
            
            
            // Smoker
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.getCoordBaseMode());
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,5,7, 2}
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), smokerState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{6,5,5, 2, 1, 1}, 
    			{7,5,5, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
        	
        	// Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Animal pen
        		{4,1,3, 4,1,4}, 
        		{4,2,4, 4,2,4}, 
        		{5,1,5, 5,1,5}, 
        		// Shrub
        		{8,1,1, 8,1,3}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
        	
        	
        	// Dirt
        	for (int[] uuvvww : new int[][]{
        		{2,0,5, 2,0,5}, 
        		{3,0,6, 3,0,6}, 
        		{6,0,5, 6,0,5}, {6,0,7, 6,0,7}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeDirtState,
        				biomeDirtState, 
        				false);
            }
            
            
            // Moist Farmland
            for(int[] uuvvww : new int[][]{
            	{2,0,6, 2,0,6}, 
        		{3,0,7, 4,0,7}, 
        		{7,0,6, 7,0,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FARMLAND.getStateFromMeta(7), Blocks.FARMLAND.getStateFromMeta(7), false);
            }
        	
            
        	// Crops - carrot
            for(int[] uuvvww : new int[][]{
        		{7,1,6, 7,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.CARROTS.getStateFromMeta(7), Blocks.CARROTS.getStateFromMeta(7), false);
            }
            
            
        	// Water
            for (int[] uvw : new int[][]{
        		{6,0,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Campfires
    		IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), this.getCoordBaseMode());
    		for(int[] uvw : new int[][]{
    			{2,8,7}, 
    			})
    		{
    			this.setBlockState(world, campfireState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
            		// Front side
        			{1,3,0, 2}, {1,4,0, 2}, {1,5,0, 2}, {1,6,0, 2}, 
        			{2,2,0, 2}, {2,3,0, 2}, {2,4,0, 2}, 
        			{4,5,0, 2}, {4,6,0, 2}, {4,7,0, 2}, 
        			{5,3,0, 2}, {5,4,0, 2}, {5,5,0, 2}, 
        			{8,4,3, 2}, {8,5,3, 2}, {8,6,3, 2}, 
        			// Left side
        			{0,5,1, 3}, {0,6,1, 3}, 
        			{0,4,2, 3}, {0,5,2, 3}, {0,6,2, 3}, 
        			{0,2,3, 3}, {0,3,3, 3}, {0,4,3, 3}, {0,5,3, 3}, 
        			{0,3,6, 3}, {0,4,6, 3}, {0,5,6, 3}, 
        			{0,5,7, 3}, {0,6,7, 3}, 
        			// Back side
        			{2,3,9, 0}, {2,4,9, 0}, {2,5,9, 0}, {2,6,9, 0}, 
        			{3,5,9, 0}, {3,6,9, 0}, {3,7,9, 0}, {3,8,9, 0}, 
        			{6,2,9, 0}, {6,3,9, 0}, {6,4,9, 0}, 
        			{7,5,9, 0}, 
        			{8,5,9, 0}, {8,6,9, 0}, 
        			// Right side
        			{6,2,1, 1}, {6,3,1, 1}, 
        			{6,4,2, 1}, {6,5,2, 1}, 
        			{9,3,5, 1}, {9,4,5, 1}, {9,5,5, 1}, 
        			{9,3,8, 1}, {9,4,8, 1}, {9,5,8, 1}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
    		
    		// Entities
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int u = 2;
    			int v = 5;
    			int w = 5;
    			
    			while (u==2 && (w==5 || w==7))
    			{
    				u=2+random.nextInt(2); w=3+random.nextInt(5);
    			}
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 1, 0); // Butcher
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
    			world.spawnEntityInWorld(entityvillager);
    			
            	if (VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1)
            	{
	                // Pigs in the yard
	            	for (int[] uvw : new int[][]{
	        			{3,1,5},
	        			{5,1,6},
	        			})
	        		{
	            		EntityLiving animal = new EntityPig(world);
	            		IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
	            		
	                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(animal);
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
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    // --- Cartographer House 1 --- //
    // designed by jss2a98aj
    
    public static class JungleCartographerHouse1 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"         ", 
    			"         ", 
    			"         ", 
    			"         ", 
    			"         ", 
    			"         ", 
    			"         ", 
    			"         ", 
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 1;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleCartographerHouse1() {}

        public JungleCartographerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleCartographerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleCartographerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Feet
    			{0,0,5, 1,0,5}, {1,0,6, 1,0,6},   {5,0,5, 6,0,5}, {5,0,6, 5,0,6}, 
    			{0,0,1, 1,0,1}, {1,0,0, 1,0,0},   {5,0,1, 6,0,1}, {5,0,0, 5,0,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		// Foot foundation
    		for(int[] uvw : new int[][]{
    			// Feet
    			{0,-1,5}, {0,-1,6}, {1,-1,5}, {1,-1,6},   {5,-1,5}, {5,-1,6}, {6,-1,5}, {6,-1,6}, 
    			{0,-1,0}, {0,-1,1}, {1,-1,0}, {1,-1,1},   {0,-1,5}, {0,-1,6}, {1,-1,5}, {1,-1,6}, 
    			})
    		{
    			this.replaceAirAndLiquidDownwards(world, biomeCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Feet
    			{0,0,6, 0,0,6}, {6,0,6, 6,0,6}, 
    			{0,0,0, 0,0,0}, {6,0,0, 6,0,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);
    		}
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Top of front columns
    			{5,1,1, 5,1,1}, 
    			{1,1,5, 1,1,5}, 
    			{5,1,5, 5,1,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{2,1,2, 4,1,4}, 
    			// Platform
    			{3,2,6, 6,2,6}, {5,2,5, 5,2,5}, {6,2,3, 6,2,5}, 
    			// Front wall
    			{3,2,1, 3,4,1}, {4,2,1, 4,2,1}, {4,4,1, 4,4,1}, {5,2,1, 5,4,1}, 
    			// Front-right corner
    			{6,2,2, 6,5,2}, 
    			// Right wall
    			{7,3,3, 7,5,3}, {7,3,4, 7,3,6}, {7,5,4, 7,5,6}, {7,3,7, 7,5,7}, 
    			// Back wall
    			{3,3,7, 3,5,7}, {4,3,7, 6,3,7}, {4,5,7, 6,5,7},
    			// Left-back corner
    			{2,2,6, 2,5,6}, 
    			// Left wall
    			{1,2,3, 1,4,3}, {1,2,4, 1,2,4}, {1,4,4, 1,4,4}, {1,2,5, 1,4,5}, 
    			// Roof
    			{2,5,2, 5,5,2}, {2,5,3, 2,5,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Above entryway
    			{1,4,2, 0+4}, {2,4,1, 3+4},  
    			// Interior split-level stairs
    			{4,2,4, 3}, {5,2,4, 3}, {4,2,5, 0}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Entry steps
    			{1,1,1, 1,1,1}, 
    			// Roof trim
    			{1,5,1, 5,5,1}, {1,5,2, 1,5,5}, 
    			{3,6,3, 7,6,3}, {3,6,4, 3,6,6}, {7,6,4, 7,6,6}, {3,6,7, 7,6,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Above doors
    			{1,4,1, 1,4,1}, 
    			// Outside trimming
    			{2,1,1, 4,1,1}, {2,1,5, 4,1,5}, 
    			{1,1,2, 1,1,4}, {5,1,2, 5,1,4}, 
    			{7,2,3, 7,2,7}, {3,2,7, 6,2,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{3,5,3}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{4,3,1},  
        		// Back wall
        		{4,4,7}, {5,4,7}, {6,4,7}, 
        		// Left wall
        		{1,3,4},  
        		// Right wall
        		{7,4,4}, {7,4,5}, {7,4,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass blocks
        	for (int[] uuvvww : new int[][]{
        		// Sunroof
        		{4,6,4, 6,6,6}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.GLASS.getStateFromMeta(0), Blocks.GLASS.getStateFromMeta(0), false);
            }
            
        	
        	// Cartography Table
        	IBlockState cartographyTableState = ModObjects.chooseModCartographyTableState();
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{5,3,5}, 
           		})
        	{
            	this.setBlockState(world, cartographyTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{2,2,1, 0, 1, 1}, 
    			{1,2,2, 1, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{3,3,6, 3,3,6}, {6,3,3, 6,3,3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		// Lower
        		{3,2,3, (GeneralConfig.useVillageColors ? this.townColor4 : 9)}, // 9 is cyan
        		{4,2,3, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // 0 is white
        		{5,2,3, (GeneralConfig.useVillageColors ? this.townColor4 : 9)}, // 9 is cyan
        		{3,2,4, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // 0 is white
        		{3,2,5, (GeneralConfig.useVillageColors ? this.townColor4 : 9)}, // 9 is cyan
        		// Upper
        		{4,3,6, (GeneralConfig.useVillageColors ? this.townColor4 : 9)}, // 9 is cyan
        		{5,3,6, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // 0 is white
        		{6,3,6, (GeneralConfig.useVillageColors ? this.townColor4 : 9)}, // 9 is cyan
        		{6,3,5, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // 0 is white
        		{6,3,4, (GeneralConfig.useVillageColors ? this.townColor4 : 9)}, // 9 is cyan
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
        	// Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CRAFTING_TABLE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{5,2,2}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
    		
            // Flower Pot
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{3,4,6}, 
        		{6,4,3}, 
           		})
        	{
                int x = this.getXWithOffset(uvw[0], uvw[2]);
                int y = this.getYWithOffset(uvw[1]);
                int z = this.getZWithOffset(uvw[0], uvw[2]);
                
                IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
            }
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Left side
        			{0,2,4, 3}, {0,1,4, 3}, 
        			// Back side
        			{6,3,8, 0}, {6,2,8, 0}, {6,1,8, 0}, 
        			// Corner
        			{6,4,1, 2}, {6,3,1, 2}, {6,2,1, 2}, 
        			{7,5,2, 1}, {7,4,2, 1}, {7,3,2, 1}, {7,2,2, 1}, {7,1,2, 1}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
            
            
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 2;
        	int chestW = 5;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(Reference.VN_CARTOGRAPHER);
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(10);
    			
    			int u = s<3 ? 3 : s<5 ? s+1 : s<8 ? 6 : 13-s;
    			int v = s<5 ? 2 : 3;
    			int w = s<3 ? 5-s : s<5 ? 3 : s<8 ? s-1 : 6;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 2, 0); // Cartographer
    			
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
    
    
    // --- Cartographer House 2 --- //
    // designed by AstroTibs
    
    public static class JungleCartographerHouse2 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"FFFFFFFFFF",
    			"FFFFFFFFFF",
    			"FFFFFPPPFF",
    			"FFFFFFFPFF",
    			"FFFFFFFPFF",
    			"FFFFFFFFFF",
    			"FFFFFFFFFF",
    			"FFPPPFFFFF",
    			"FFPFFFFFFF",
    			"FFPFFFFFFF",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 5;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleCartographerHouse2() {}

        public JungleCartographerHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleCartographerHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleCartographerHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Grass
    		for(int[] uuvvww : new int[][]{
    			// Front garden
    			{0,0,0, 1,0,4}, 
    			{2,0,3, 4,0,4}, 
    			{3,0,0, 4,0,1}, 
    			// Back garden
    			{5,0,8, 9,0,9}, 
    			{5,0,5, 6,0,6}, 
    			{8,0,5, 9,0,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{5,0,0, 5,0,4}, {6,0,0, 7,0,1}, {6,0,4, 7,0,4}, {8,0,0, 9,0,4}, 
    			{0,0,5, 0,0,9}, {1,0,5, 3,0,5}, {1,0,9, 3,0,9}, {4,0,5, 4,0,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Stripped logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
    		for (int[] uuvvww : new int[][]{
    			// Front-right
    			{5,1,4, 5,3,4}, {9,1,4, 9,3,4}, 
    			{5,1,0, 5,3,0}, {9,1,0, 9,3,0}, 
    			// Back-left
    			{0,1,9, 0,3,9}, {4,1,9, 4,3,9}, 
    			{0,1,5, 0,3,5}, {4,1,5, 4,3,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
    		
    		// Concrete
    		IBlockState tryConcrete = ModObjects.chooseModConcreteState(GeneralConfig.useVillageColors ? townColor : 0); // White
        	Block concreteBlock = Blocks.STAINED_HARDENED_CLAY; int concreteMeta = GeneralConfig.useVillageColors ? townColor : 0; // White
        	if (tryConcrete != null) {concreteBlock = tryConcrete.getBlock(); concreteMeta = tryConcrete.getBlock().getMetaFromState(tryConcrete);}
    		for(int[] uuvvww : new int[][]{
    			// Left-back
    			{4,1,6, 4,3,6}, {4,3,7, 4,3,7}, {4,1,8, 4,3,8}, 
    			{1,1,9, 3,1,9}, {1,3,9, 3,3,9}, 
    			{0,1,6, 0,1,8}, {0,3,6, 0,3,8}, 
    			{1,1,5, 3,1,5}, {1,3,5, 3,3,5}, 
    			// Front-right
    			{6,1,4, 6,3,4}, {7,3,4, 7,3,4}, {8,1,4, 8,3,4}, 
    			{5,1,1, 5,3,1}, {5,3,2, 5,3,2}, {5,1,3, 5,3,3}, 
    			{9,1,1, 9,1,3}, {9,3,1, 9,3,3}, 
    			{6,1,0, 8,1,0}, {6,3,0, 8,3,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], concreteBlock.getStateFromMeta(concreteMeta), concreteBlock.getStateFromMeta(concreteMeta), false);
    		}
    		
        	
        	// Terracotta
        	IBlockState biomeTerracottaState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.HARDENED_CLAY.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{0,4,5, 4,4,5}, {0,4,5, 0,4,9}, {0,4,9, 4,4,9}, {4,4,5, 4,4,9}, 
        		{5,4,0, 9,4,0}, {5,4,0, 5,4,4}, {5,4,4, 9,4,4}, {9,4,0, 9,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTerracottaState, biomeTerracottaState, false);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{4,3,2, 3}, 
    			{6,4,2, 1}, 
    			{5,3,7, 1}, 
    			{3,4,7, 3}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Counter
    			{6,1,1, 8,1,1}, {8,1,2, 8,1,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Back-left
    			{0,5,9, 2}, {1,5,9, 2}, {2,5,9, 2}, {3,5,9, 2}, {4,5,9, 2}, 
    			{0,5,8, 0}, {4,5,8, 1}, 
    			{0,5,7, 0}, {4,5,7, 1}, 
    			{0,5,6, 0}, {4,5,6, 1}, 
    			{0,5,5, 3}, {1,5,5, 3}, {2,5,5, 3}, {3,5,5, 3}, {4,5,5, 3}, 
    			// Front-right
    			{5,5,4, 2}, {6,5,4, 2}, {7,5,4, 2}, {8,5,4, 2}, {9,5,4, 2}, 
    			{5,5,3, 0}, {9,5,3, 1}, 
    			{5,5,2, 0}, {9,5,2, 1}, 
    			{5,5,1, 0}, {9,5,1, 1}, 
    			{5,5,0, 3}, {6,5,0, 3}, {7,5,0, 3}, {8,5,0, 3}, {9,5,0, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Glass Panes
        	for (int[] uuvvww : new int[][]{
        		// Front-right
        		{9,2,1, 9,2,3}, 
        		{6,2,0, 8,2,0}, 
        		// Back-left
        		{1,2,9, 3,2,9}, 
        		{0,2,6, 0,2,8}, 
        		{1,2,5, 3,2,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.GLASS_PANE.getDefaultState(), Blocks.GLASS_PANE.getDefaultState(), false);
            }
            
            
            // Glass blocks
        	for (int[] uvw : new int[][]{
        		// Back-left
        		{1,5,8, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{2,5,8, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{3,5,8, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{1,5,7, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{2,5,7, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{3,5,7, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{1,5,6, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{2,5,6, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{3,5,6, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		// Front-right
        		{6,5,3, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{7,5,3, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{8,5,3, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{6,5,2, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{7,5,2, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{8,5,2, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{6,5,1, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{7,5,1, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{8,5,1, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		})
            {
            	this.setBlockState(world, Blocks.STAINED_GLASS.getStateFromMeta(uvw[3]), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Wool - carpet prevented villagers from going through the door
        	for(int[] uvwm : new int[][]{
        		// Back-left
        		{1,0,8, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{2,0,8, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{3,0,8, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{1,0,7, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{2,0,7, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{3,0,7, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{1,0,6, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{2,0,6, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{3,0,6, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		// Front-right
        		{6,0,3, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		{7,0,3, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{6,0,2, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{7,0,2, GeneralConfig.useVillageColors ? this.townColor5 : 1}, // Orange
        		})
            {
        		this.setBlockState(world, Blocks.WOOL.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
    		// Potted sapling
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{8,2,1}, 
           		})
        	{
                int potted_sapling_x = this.getXWithOffset(uvw[0], uvw[2]);
                int potted_sapling_y = this.getYWithOffset(uvw[1]);
                int potted_sapling_z = this.getZWithOffset(uvw[0], uvw[2]);
                StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(potted_sapling_x, potted_sapling_y, potted_sapling_z), Blocks.SAPLING, biomePlankState.getBlock().getMetaFromState(biomePlankState));
        	}
        	
        	
        	// Cartography Table
        	IBlockState cartographyTableState = ModObjects.chooseModCartographyTableState();
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{1,1,7}, 
           		})
        	{
            	this.setBlockState(world, cartographyTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{5,1,2, 3, 1, 0}, 
    			{7,1,4, 0, 1, 1}, 
    			{4,1,7, 1, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{5,1,9, 9,1,9}, 
        		{9,1,5, 9,1,8}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
        	
        	// Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Front shrubs
        		{1,1,4, 3,1,4}, 
        		// Back shrubs
        		{5,2,9, 9,2,9}, 
        		{9,2,5, 9,2,8}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
    		
    		
            // Poppies
            for (int[] uvw : new int[][]{
            	{5,1,8}, {7,1,8}, {8,1,7}, {8,1,5}, 
            	{5,1,6}, {6,1,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.RED_FLOWER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Tall Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			// Back garden
            	{6,1,8, 0}, {8,1,8, 0}, {8,1,6, 0}, 
            	{6,1,6, 0}, {5,1,5, 0}, 
            	// Front garden
            	{0,1,4, 0}, {4,1,4, 0}, 
            	{0,1,3, 0}, {1,1,3, 0}, {2,1,3, 0}, {3,1,3, 0}, {4,1,3, 0}, 
            	{0,1,2, 0}, {1,1,2, 0}, 
            	{0,1,1, 0}, {1,1,1, 0}, {3,1,1, 0}, {4,1,1, 0}, 
            	{0,1,0, 0}, {1,1,0, 0}, {3,1,0, 0}, {4,1,0, 0}, 
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
        	
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(28);
    			
    			int u = s<=2 ? s+1 : s<=4 ? s-1 : s<=7 ? s-4 : s<=11 ? s-3 : s<=15 ? s-7 : s<=19 ? s-11 : s<=23 ? s-15 : s<=25 ? s-18 : s-20;
    			int v = 1;
    			int w = s<=2 ? 8 : s<=4 ? 7 : s<=7 ? 6 : s<=11 ? 8 : s<=15 ? 7 : s<=19 ? 6 : s<=23 ? 5 : s<=25 ? 3 : 2;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 2, 0); // Cartographer
    			
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
    // designed by jss2a98aj
    
    public static class JungleFisherCottage extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"FFFFFFFFFF",
    			"FFFFFFFFFF",
    			"FFFFFFFFFF",
    			"FFFFFFFFFF",
    			"FFFFFFFFFF",
    			"FFFFFFFFF ",
    			"    FFFFP ",
    			"    FFFFP ",
    			"    FFPPP ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 4;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 5;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleFisherCottage() {}

        public JungleFisherCottage(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleFisherCottage buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleFisherCottage(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{0,0,3, 5,0,8}, 
    			// Front steps
    			{4,0,0, 5,0,2}, {6,0,1, 7,0,4}, {8,0,3, 8,0,5}, 
    			// Pool rim
    			{9,0,4, 9,0,8}, {6,0,8, 8,0,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Water
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{6,0,5, 7,0,7}, {8,0,6, 8,0,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
    		}
    		
    		
            // Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Front wall
        		{1,1,3, 1,3,3}, {2,1,3, 2,1,3}, {2,3,3, 2,3,3}, {3,1,3, 3,3,3}, 
        		// Left wall
        		{0,1,4, 0,3,4}, {0,1,5, 0,1,6}, {0,3,5, 0,3,6}, {0,1,7, 0,3,7}, 
        		// Right wall
        		{5,1,5, 5,3,5}, {5,1,6, 5,1,6}, {5,3,6, 5,3,6}, {5,1,7, 5,3,7}, 
        		// Back wall
        		{1,1,8, 1,3,8}, {2,1,8, 3,1,8}, {2,3,8, 3,3,8}, {4,1,8, 4,3,8}, 
        		})
            {
        		// Cyan
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor4 : 9)),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor4 : 9)), 
        				false);
            }
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,4,7, 1,4,7}, {4,4,7, 4,4,7}, 
    			{1,4,4, 1,4,4}, {4,4,4, 4,4,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
            
        	
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{0,1,3, 0,3,3}, 
        		{0,1,8, 0,3,8}, 
        		{5,1,8, 5,3,8}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{4,3,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Outside
            	{9,1,4}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Awning supports
        		{4,1,0, 4,2,0}, 
        		{7,1,1, 7,2,1}, 
        		{8,1,4, 8,2,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Above front door
    			{4,3,3, 3+4}, {5,3,4, 1+4}, 
    			// Table
    			{1,1,5, 2+4}, {1,1,7, 3+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Awning
    			{4,3,0, 5,3,0}, 
    			{6,3,1, 7,3,1}, {7,3,2, 7,3,2}, {8,3,3, 8,3,4}, 
    			// Roof trim
    			{0,4,3, 1,4,3}, {0,4,4, 0,4,4}, 
    			{2,4,4, 3,4,4}, 
    			{4,4,3, 5,4,3}, {5,4,4, 5,4,4}, 
    			{4,4,5, 4,4,6}, 
    			{5,4,7, 5,4,8}, {4,4,8, 4,4,8}, 
    			{2,4,7, 3,4,7}, 
    			{0,4,7, 0,4,8}, {1,4,8, 1,4,8}, 
    			{1,4,5, 1,4,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Awning
    			{4,3,1, 5,3,2}, 
    			{5,3,3, 5,3,3}, 
    			{6,3,2, 6,3,2}, 
    			{6,3,3, 7,3,4}, 
    			// Table
    			{1,1,6, 1,1,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{2,2,3}, 
        		// Back wall
        		{2,2,8}, {3,2,8}, 
        		// Left wall
        		{0,2,5}, {0,2,6}, 
        		// Right wall
        		{5,2,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass blocks
        	for (int[] uuvvww : new int[][]{
        		// Sunroof
        		{2,4,5, 3,4,6}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.GLASS.getStateFromMeta(0), Blocks.GLASS.getStateFromMeta(0), false);
            }
        	
        	
        	// Barrels
    		IBlockState barrelState = ModObjects.chooseModBarrelBlockState();
    		boolean isChestType=(barrelState==null);
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel: -1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
            	
    			// Front porch
    			{7,1,3, 0,-1}, 
    			// By the pool
    			{6,1,8, 0,1}, {7,1,8, 0,-1}, 
    			{6,2,8, 1,2}, 
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (isChestType) {barrelState = Blocks.CHEST.getDefaultState();}
    			this.setBlockState(world, barrelState, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2])), barrelState.getBlock().getStateFromMeta(isChestType?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.getCoordBaseMode()):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.getCoordBaseMode())), 2);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,1,3, 0, 1, 0}, 
    			{5,1,4, 3, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
    		
            // Flower Pot
    		int flower_u=1; int flower_v=2; int flower_w=5;
            int x = this.getXWithOffset(flower_u, flower_w);
            int y = this.getYWithOffset(flower_v);
            int z = this.getZWithOffset(flower_u, flower_w);
            
            IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
    		int randomPottedPlant = random.nextInt(10)-1;
    		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
    		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 1;
        	int chestW = 7;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(Reference.VN_FISHER);
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Left side
        			{-1,1,7, 3}, {-1,2,7, 3}, {-1,3,7, 3},
        			{-1,1,8, 3}, {-1,2,8, 3}, 
        			// Back side
        			{3,1,9, 0}, {3,2,9, 0}, {3,3,9, 0}, 
        			// Front side
        			{1,1,2, 2}, {1,2,2, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int u=random.nextInt(3);
				int v=1;
				int w=random.nextInt(3);
    			
				if (random.nextBoolean())
				{
					// Inside the cottage
					u+=2; w+=4;
				}
				else
				{
					// Outside the cottage
					u+=4; w+=1;
				}
				
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 2, 0); // Fisherman
    			
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
    
    
    // --- Fletcher House 1 --- //
    // designed by AstroTibs
    
    public static class JungleFletcherHouse1 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"         ",
    			" FFFFFF  ",
    			" FFFFFF  ",
    			" FFFFFF  ",
    			" FFFFFF  ",
    			" FFFFFFF ",
    			" FFFFFFF ",
    			"  FPFFFF ",
    			"   P FFF ",
    			"   P     ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 4;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleFletcherHouse1() {}

        public JungleFletcherHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleFletcherHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleFletcherHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{3,0,3, 3,0,3}, 
    			{1,0,4, 5,0,7}, 
    			{6,0,2, 6,0,4}, {5,0,3, 5,0,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Dirt
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{1,1,3, 1,1,8}, 
    			{2,1,3, 2,1,3}, 
    			{2,1,8, 5,1,8}, 
    			{6,1,5, 6,1,8}, 
    			{7,1,1, 7,1,4}, 
    			{5,1,1, 6,1,1}, 
    			{5,1,2, 5,1,2}, 
    			{4,1,3, 4,1,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Back wall
    			{2,2,8, 2,3,8}, {3,3,8, 4,3,8}, {5,2,8, 5,3,8}, 
    			// Left wall
    			{1,2,4, 1,3,4}, {1,3,5, 1,3,6}, {1,2,7, 1,3,7}, 
    			// Front wall
    			{2,2,3, 2,3,3}, {4,2,3, 4,3,3}, {6,3,1, 6,3,1}, {5,2,2, 5,3,2}, 
    			// Right wall
    			{7,3,2, 7,3,3}, {6,3,5, 6,3,7}, {6,2,5, 6,2,5}, {6,2,7, 6,2,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
            
        	
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,2,3, 1,4,3}, 
        		{5,2,1, 5,4,1}, {7,2,1, 7,4,1}, 
        		{1,2,8, 1,4,8}, {6,2,8, 6,4,8}, {7,2,4, 7,4,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
    		
    		
    		// Stripped logs (Vertical)
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
    		for (int[] uuvvww : new int[][]{
    			{6,1,2, 6,1,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Entry desk
    			{2,1,4, 2+4}, 
    			// Above door
    			{3,3,3, 3+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{3,3,4, 0}, 
    			{3,3,7, 2}, 
    			{5,3,6, 3}, 
    			{6,3,3, 3}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Windows
        		{1,2,5, 1,2,6}, 
        		{3,2,8, 4,2,8}, 
        		{6,2,6, 6,2,6}, 
        		{7,2,2, 7,2,3}, 
        		{6,2,1, 6,2,1}, 
        		{5,2,2, 5,2,2}, 
        		// Ceiling supports
        		{1,4,4, 1,4,7}, 
        		{2,4,8, 5,4,8}, 
        		{6,4,5, 6,4,7}, 
        		{7,4,2, 7,4,3}, 
        		{6,4,1, 6,4,1}, 
        		{5,4,2, 5,4,2}, 
        		{2,4,3, 4,4,3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvwo : new int[][]{
    			// Roof trim
    			{0,4,3}, {0,4,5}, {0,4,7}, {0,4,9}, 
    			{2,4,9}, {4,4,9}, {6,4,9}, 
    			{7,4,8}, {7,4,6}, 
    			{8,4,5}, {8,4,3}, {8,4,1}, 
    			{7,4,0}, {5,4,0}, 
    			{4,4,1}, 
    			{3,4,2}, {1,4,2}, 
    			// Ceiling
    			{1,5,3}, {1,5,4}, {1,5,5}, {1,5,6}, {1,5,7}, {1,5,8}, 
    			{2,5,8}, {3,5,8}, {4,5,8}, {5,5,8}, {6,5,8},
    			{6,5,7}, {6,5,6}, {6,5,5}, {6,5,4}, 
    			{7,5,4}, {7,5,3}, {7,5,2}, {7,5,1}, 
    			{6,5,1}, {5,5,1}, 
    			{5,5,2}, {5,5,3}, 
    			{4,5,3}, {3,5,3}, {2,5,3}, 
    			{3,6,5}, {4,6,5}, {3,6,6}, {4,6,6}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvwo : new int[][]{
    			// Roof trim
    			{0,4,2}, {0,4,4}, {0,4,6}, {0,4,8}, 
    			{1,4,9}, {3,4,9}, {5,4,9}, {7,4,9}, 
    			{7,4,9}, {7,4,7}, {7,4,5}, 
    			{8,4,4}, {8,4,2}, {8,4,0}, 
    			{6,4,0}, {4,4,0}, 
    			{2,4,2}, {4,4,2}, 
    			// Ceiling
    			{2,5,4}, {2,5,5}, {2,5,6}, {2,5,7}, 
    			{3,5,7}, {4,5,7}, {5,5,7}, 
    			{5,5,6}, {5,5,5}, {5,5,4}, 
    			{4,5,4}, {3,5,4}, 
    			{6,5,3}, {6,5,2}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}

            
            // Fletching Table
        	IBlockState fletchingTableState = ModObjects.chooseModFletchingTableState();
        	this.setBlockState(world, fletchingTableState, 2, 1, 7, structureBB);
    		
    		
            // Table
            IBlockState[] tableComponentBlockstates = ModObjects.chooseModWoodenTable(biomePlankState.getBlock()==Blocks.PLANKS ? biomePlankState.getBlock().getMetaFromState(biomePlankState) : 0);
        	for (int[] uuvvww : new int[][]{
        		{5,1,7}, 
        		})
            {
        		for (int i=1; i>=0; i--)
        		{
        			this.setBlockState(world, tableComponentBlockstates[i], uuvvww[0], uuvvww[1]+1-i, uuvvww[2], structureBB);
        		}
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{3,1,3, 0, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
            
            // Potted random flower
            for (int[] uvw : new int[][]{
        		{6,2,2}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
            }
    		
    		
    		// Tall Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			{2,1,2, 1}, {4,1,2, 1}, 
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
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Lower
        		{3,1,6, 4,1,6, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White
        		{3,1,5, 3,1,5, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White
        		{4,1,5, 4,1,5, (GeneralConfig.useVillageColors ? this.townColor4 : 9)}, // Cyan
        		{5,1,5, 5,1,5, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White
        		{4,1,4, 5,1,4, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.CARPET.getStateFromMeta(uuvvww[6]), Blocks.CARPET.getStateFromMeta(uuvvww[6]), false);
            }
        	
        	
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s=random.nextInt(16);
    			
    			int u = s<=1 ? 2 : s<=5 ? 3 : s<=9 ? 4 : s<=13 ? 5 : 6;
				int v=1;
				int w = s<=1 ? s-5 : s<=5 ? s+2 : s<=9 ? s-2 : s<=13 ? s-7 : s-9;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 4, 0); // Fletcher
    			
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
    
    
    // --- Fletcher House 2 --- //
    // designed by AstroTibs
    
    public static class JungleFletcherHouse2 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"FFFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFFF",
    			"FFFFFPPPPPFFFFF",
    			"FFFFFFFPFFFFFFF",
    			"FFFFFFFPFFFFFFF",
    			"  FFFFFPFFFFF  ",
    			"  FFFFFPFFFFF  ",
    			"  FFFFFPFFFFF  ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 5;
    	private static final int DECREASE_MAX_U = 5;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleFletcherHouse2() {}

        public JungleFletcherHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleFletcherHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleFletcherHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,0,3, 4,0,7}, {10,0,3, 14,0,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Outside
        		{5,1,7, 9,1,7}, 
        		{5,1,3, 6,1,3}, {8,1,3, 9,1,3}, 
        		// Left roof
        		{1,6,6, 1,6,6}, {3,6,6, 3,6,6}, 
        		{1,6,4, 1,6,4}, {3,6,4, 3,6,4}, 
        		// Right roof
        		{11,6,6, 11,6,6}, {13,6,6, 13,6,6}, 
        		{11,6,4, 11,6,4}, {13,6,4, 13,6,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Fence Gate (Across)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{7,1,3}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(2, false)), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Left wing
        		// Windows
        		{2,1,7, 2,1,7}, {2,3,7, 2,3,7}, 
        		{0,1,5, 0,1,5}, {0,3,5, 0,3,5}, 
        		{4,3,5, 4,3,5}, 
        		{2,1,3, 2,1,3}, {2,3,3, 2,3,3}, 
        		// Roof
        		{1,5,6, 1,5,6}, {2,6,6, 2,6,6}, {3,5,6, 3,5,6}, 
        		{1,6,5, 1,6,5}, {3,6,5, 3,6,5}, 
        		{1,5,4, 1,5,4}, {2,6,4, 2,6,4}, {3,5,4, 3,5,4}, 
        		
        		// Right wing
        		// Windows
        		{12,1,7, 12,1,7}, {12,3,7, 12,3,7}, 
        		{10,3,5, 10,3,5}, 
        		{14,1,5, 14,1,5}, {14,3,5, 14,3,5}, 
        		{12,1,3, 12,1,3}, {12,3,3, 12,3,3}, 
        		// Roof
        		{11,5,6, 11,5,6}, {12,6,6, 12,6,6}, {13,5,6, 13,5,6}, 
        		{11,6,5, 11,6,5}, {13,6,5, 13,6,5}, 
        		{11,5,4, 11,5,4}, {12,6,4, 12,6,4}, {13,5,4, 13,5,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Outside
    			{5,2,7, -1}, {9,2,7, -1}, 
    			{5,2,3, -1}, {9,2,3, -1}, 
    			// Inside
    			{1,3,5, 1}, {13,3,5, 3}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
            // Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Left wing
    			{0,1,7, 0,4,7}, {4,1,7, 4,4,7}, 
    			{0,1,3, 0,4,3}, {4,1,3, 4,4,3}, 
    			{1,4,3, 3,4,3}, {0,4,4, 0,4,6}, {1,4,7, 3,4,7}, {4,4,4, 4,4,6}, 
    			// Right wing
    			{10,1,7, 10,4,7}, {14,1,7, 14,4,7}, 
    			{10,1,3, 10,4,3}, {14,1,3, 14,4,3}, 
    			{11,4,3, 13,4,3}, {10,4,4, 10,4,6}, {11,4,7, 13,4,7}, {14,4,4, 14,4,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Left wing
    			{1,1,3, 1,3,3}, {3,1,3, 3,3,3}, 
    			{0,1,4, 0,3,4}, {0,1,6, 0,3,6}, {4,1,6, 4,3,6}, {4,1,4, 4,3,4}, 
    			{1,1,7, 1,3,7}, {3,1,7, 3,3,7}, 
    			{2,5,7, 2,5,7}, 
    			{0,5,5, 0,5,5}, {4,5,5, 4,5,5}, 
    			{2,5,3, 2,5,3}, 
    			// Right wing
    			{11,1,3, 11,3,3}, {13,1,3, 13,3,3}, 
    			{10,1,4, 10,3,4}, {10,1,6, 10,3,6}, {14,1,6, 14,3,6}, {14,1,4, 14,3,4}, 
    			{11,1,7, 11,3,7}, {13,1,7, 13,3,7}, 
    			{12,5,7, 12,5,7}, 
    			{10,5,5, 10,5,5}, {14,5,5, 14,5,5}, 
    			{12,5,3, 12,5,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Left wing
    			{1,5,7, 0}, {3,5,7, 1}, 
    			{0,5,6, 2}, {4,5,6, 2}, 
    			{0,5,4, 3}, {4,5,4, 3}, 
    			{1,5,3, 0}, {3,5,3, 1}, 
    			// Right wing
    			{11,5,7, 0}, {13,5,7, 1}, 
    			{10,5,6, 2}, {14,5,6, 2}, 
    			{10,5,4, 3}, {14,5,4, 3}, 
    			{11,5,3, 0}, {13,5,3, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvwo : new int[][]{
    			// Left wing
    			{0,5,7}, {4,5,7}, 
    			{0,5,3}, {4,5,3}, 
    			// Right wing
    			{10,5,7}, {14,5,7}, 
    			{10,5,3}, {14,5,3}, 
    			// Connecting awning
    			{5,4,6}, {6,4,6}, {7,4,6}, {8,4,6}, {9,4,6}, 
    			{5,4,4}, {6,4,4}, {7,4,4}, {8,4,4}, {9,4,4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvwo : new int[][]{
    			// Connecting awning
    			{5,3,7}, {6,3,7}, {7,3,7}, {8,3,7}, {9,3,7}, 
    			{5,4,5}, {6,4,5}, {7,4,5}, {8,4,5}, {9,4,5}, 
    			{5,3,3}, {6,3,3}, {7,3,3}, {8,3,3}, {9,3,3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Left wing
        		{2,2,7}, 
        		{0,2,5}, 
        		{2,2,3}, 
        		// Right wing
        		{12,2,7}, 
        		{14,2,5}, 
        		{12,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass blocks
        	for (int[] uvw : new int[][]{
        		{2,6,5}, {12,6,5}, 
        		})
            {
    			this.setBlockState(world, Blocks.GLASS.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }

            
            // Fletching Table
        	IBlockState fletchingTableState = ModObjects.chooseModFletchingTableState();
        	this.setBlockState(world, fletchingTableState, 13,1,6, structureBB);
    		
    		
    		// Grass - NOT biome-swapped
    		for (int[] uuvvww : new int[][]{
        		{1,1,6, 1,1,6}, 
        		{2,0,0, 6,0,2}, {8,0,0, 11,0,2}, 
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.GRASS.getDefaultState(), Blocks.GRASS.getDefaultState(), false);
            }
            
        	
        	// Random Flower
        	for (int[] uvw : new int[][]{
    			{1,2,6}, 
        		})
            {
        		IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
            	int flowerindex = random.nextInt(10 + (cornflowerState!=null && lilyOfTheValleyState!=null ? 2:0));
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10 is cornflower, 11 is lily of the valley
        		IBlockState flowerstate;
            	if (flowerindex==10 && cornflowerState!=null) {flowerstate=cornflowerState;}
            	else if (flowerindex==11 && lilyOfTheValleyState!=null) {flowerstate=lilyOfTheValleyState;}
            	else {flowerstate = (flowerindex==9 ? Blocks.YELLOW_FLOWER:Blocks.RED_FLOWER).getStateFromMeta(new int[]{0,1,2,3,4,5,6,7,8,0}[flowerindex%10]);}
        		
        		this.setBlockState(world, flowerstate, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,1,5, 2}, 
            	{2,1,6, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CRAFTING_TABLE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{1,1,4}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 13;
        	int chestV = 1;
        	int chestW = 4;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(Reference.VN_FLETCHER);
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,1,5, 1, 1, 1}, {10,1,5, 3, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
        	
        	// Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Front shrubs
        		{5,1,0, 5,1,2}, {9,1,0, 9,1,2}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
    		
    		
    		// Tall Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			// Walkway
    			{7,1,6, 0}, 
    			{5,1,6, 1}, {9,1,6, 1}, 
    			{6,1,6, 0}, {8,1,6, 0}, 
    			{5,1,4, 0}, {9,1,4, 0}, 
    			// Front lawn
    			{2,1,2, 0}, {3,1,2, 0}, {4,1,2, 0}, {6,1,2, 0}, {8,1,2, 0}, {10,1,2, 0}, {11,1,2, 0}, {12,1,2, 0}, 
    			{2,1,1, 0}, {3,1,1, 0}, {4,1,1, 0}, {6,1,1, 0}, {8,1,1, 0}, {10,1,1, 0}, {11,1,1, 0}, {12,1,1, 0}, 
    			{2,1,0, 0}, {3,1,0, 0}, {4,1,0, 0}, {6,1,0, 0}, {8,1,0, 0}, {10,1,0, 0}, {11,1,0, 0}, {12,1,0, 0}, 
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
        	
        	
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s=random.nextInt(29);
    			
    			int u = s<=0 ? 1 : s<=6 ? 2+(s-1)/3 : s<=21 ? 3+(s-1)/3 : s<=27 ? 4+(s-1)/3 : 13;
				int v=1;
				int w = s<=0 ? 5 : s<=27 ? 4+(s-1)%3 : 5;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 4, 0); // Fletcher
    			
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
    
    
    // --- Large House --- //
    // designed by AstroTibs and jss2a98aj
    
    public static class JungleLargeHouse extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"                      ",
    			"       FFFFFFFFF      ",
    			"       FFFFFFFFF      ",
    			"FFF    FFFFFFFFF      ",
    			"FFF    FFFFFFFFF      ",
    			"FFF    FFFFFFFFF      ",
    			"FFF    FFFFFFFFFFFFFF ",
    			"PPP    FFFFFFFFFFFFFF ",
    			"PPPP   FFFFFFFFFFFFFF ",
    			" PPP   FFFFFFFFFFFFFF ",
    			" PPPP  FFFFFFFFFFFFFF ",
    			"  PPP  FFFFFFFFFFFFFF ",
    			"  PPPP FFFFFFFFFFFFFF ",
    			"   PPP FFFFFFFFF      ",
    			"   PPP                ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 12;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 15;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleLargeHouse() {}

        public JungleLargeHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleLargeHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleLargeHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Foundation
    			{7,0,1, 15,0,13}, {16,0,2, 20,0,8}, 
    			// Entry staircase
    			{0,1,9, 2,1,11}, {0,2,10, 2,2,11}, 
    			// Cobblestone underneath balcony walkway
    			{6,3,9, 6,3,11}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
        	
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entryway
        		{0,1,8, 1}, {1,1,8, 3}, {2,1,8, 0}, 
        		{0,2,9, 1}, {1,2,9, 3}, {2,2,9, 0}, 
        		{2,3,10, 0}, {3,3,9, 1+4}, {3,3,10, 1+4}, {3,3,11, 1+4}, 
        		{3,4,10, 0}, {4,4,9, 1+4}, {4,4,10, 1+4}, {4,4,11, 1+4}, 
        		{5,4,9, 0+4}, {5,4,10, 0+4}, {5,4,11, 0+4}, 
        		{6,2,9, 0+4}, {6,2,10, 0+4}, {6,2,11, 0+4}, 
        		})
            {
            	this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
    		
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{0,2,8, 0,2,8}, 
            	{0,3,8, 0,3,11}, {1,3,11, 2,3,11}, {2,4,11, 3,4,11}, {3,5,11, 4,5,11}, 
            	{2,2,8, 2,2,8}, 
            	{2,3,8, 2,3,9}, {2,4,9, 3,4,9}, {3,5,9, 4,5,9}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
    		
    		
    		// Stripped logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
    		for (int[] uuvvww : new int[][]{
    			// Two-floor beams
    			{15,1,2, 15,8,2}, {15,1,8, 15,8,8}, 
    			// Top-floor beam
    			{11,5,8, 11,7,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStrippedLogHorizAcrossState = biomeLogHorAcrossState;
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogHorizAcrossState.getBlock()==Blocks.LOG || biomeStrippedLogHorizAcrossState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Top
            	// Front wall
            	{7,8,1, 15,8,1}, {16,8,2, 20,8,2}, 
            	// Back wall
            	{7,8,13, 15,8,13}, {16,8,8, 20,8,8}, 
            	
            	// Bottom
            	// Front wall
            	{7,4,1, 15,4,1}, {16,4,2, 20,4,2}, 
            	// Back wall
            	{7,4,13, 15,4,13}, {16,4,8, 20,4,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);
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
            	// Left wall
            	{7,4,2, 7,4,12}, 
            	{7,8,2, 7,8,12}, 
            	// Right wall 
            	{20,4,3, 20,4,7}, {15,4,9, 15,4,12}, 
            	{20,8,3, 20,8,7}, {15,8,9, 15,8,12}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);
            }
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Stairs
    			{8,4,3, 8,4,3}, 
    			// Downstairs lamp
    			{11,4,5, 13,4,5}, {12,4,4, 12,4,4}, {12,4,6, 12,4,6}, 
    			// Downstairs couch
    			{8,1,12, 8,1,12}, {14,1,12, 14,1,12}, 
    			// Beneath beds
    			{18,5,3, 19,5,7}, 
    			// Top of walls
    			{8,9,1, 8,9,1}, {10,10,1, 10,10,1}, {12,10,1, 12,10,1}, {14,9,1, 14,9,1}, 
    			{20,9,3, 20,9,3}, {20,10,5, 20,10,5}, {20,9,7, 20,9,7}, 
    			{8,9,13, 8,9,13}, {10,10,13, 10,10,13}, {12,10,13, 12,10,13}, {14,9,13, 14,9,13}, 
    			// Ceiling
    			{17,10,5, 17,10,5}, 
    			// Under main entrance
    			{6,4,9, 6,4,11}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Balcony
        		{6,5,1, 6,5,1}, 
        		{5,5,1, 5,5,9}, {5,5,11, 5,5,13}, 
        		{6,5,13, 6,5,13}, 
        		{5,6,1, 5,7,1}, {5,6,3, 5,7,3}, {5,6,5, 5,7,5}, {5,6,7, 5,7,7}, {5,6,9, 5,7,9}, {5,6,11, 5,7,11}, {5,6,13, 5,7,13}, 
        		// Interior ceiling trims
        		{11,8,8, 11,8,12}, {12,8,8, 14,8,8}, 
        		{15,8,3, 15,8,7}, 
        		// Stairwell railing
        		{9,5,3, 13,5,3}, {13,5,2, 13,5,2}, 
        		// Light fixtures
        		{11,10,5, 11,10,5}, {10,9,5, 12,9,5}, {11,8,4, 11,8,6}, 
        		{17,9,5, 17,9,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Entry stairwell
    			{0,4,8, -1}, {0,4,11, -1}, {4,6,11, -1}, 
    			{2,4,8, -1}, {4,6,9, -1}, 
    			// Balcony
    			{6,6,1, -1}, {6,6,13, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
            // White Terracotta
        	for (int[] uuvvww : new int[][]{
        		
        		// Downstairs
        		// Front wall
        		{7,1,1, 7,3,1}, {8,1,1, 8,1,1}, {9,1,1, 9,3,1}, {10,1,1, 10,1,1}, {11,1,1, 11,3,1}, {12,1,1, 12,1,1}, {13,1,1, 13,3,1}, {14,1,1, 14,1,1}, {15,1,1, 15,3,1}, 
        		{16,1,2, 16,3,2}, {17,1,2, 17,1,2}, {18,1,2, 18,3,2}, {19,1,2, 19,1,2}, {20,1,2, 20,3,2}, 
        		// Right wall 
        		{20,1,3, 20,1,3}, {20,1,4, 20,3,4}, {20,1,5, 20,1,5}, {20,1,6, 20,3,6}, {20,1,7, 20,1,7}, {20,1,8, 20,3,8}, 
        		{15,1,9, 15,3,9}, {15,1,10, 15,1,10}, {15,1,11, 15,3,11}, {15,1,12, 15,1,12}, 
        		// Left wall 
        		{7,1,2, 7,1,2}, {7,1,3, 7,3,3}, {7,1,4, 7,1,4}, {7,1,5, 7,3,5}, {7,1,6, 7,1,6}, {7,1,7, 7,3,13}, 
        		// Back wall 
        		{19,1,8, 19,1,8}, {18,1,8, 18,3,8}, {17,1,8, 17,1,8}, {16,1,8, 16,3,8}, 
        		{15,1,13, 15,3,13}, {14,1,13, 14,1,13}, {13,1,13, 13,3,13}, {12,1,13, 12,1,13}, {11,1,13, 11,3,13}, {10,1,13, 10,1,13}, {9,1,13, 9,3,13}, {8,1,13, 8,1,13}, 
        		
        		// Upstairs
        		// Front wall
        		{7,5,1, 7,7,1}, {8,5,1, 8,5,1}, {9,5,1, 9,7,1}, {10,5,1, 10,5,1}, {11,5,1, 11,7,1}, {12,5,1, 12,5,1}, {13,5,1, 13,7,1}, {14,5,1, 14,5,1}, {15,5,1, 15,7,1}, 
        		{16,5,2, 16,7,2}, {17,5,2, 17,5,2}, {18,5,2, 18,7,2}, {19,5,2, 19,5,2}, {20,5,2, 20,7,2}, 
        		// Right wall 
        		{20,5,3, 20,5,3}, {20,5,4, 20,7,4}, {20,5,5, 20,5,5}, {20,5,6, 20,7,6}, {20,5,7, 20,5,7}, {20,5,8, 20,7,8}, 
        		{15,5,9, 15,7,9}, {15,5,10, 15,5,10}, {15,5,11, 15,7,11}, {15,5,12, 15,5,12}, 
        		// Left wall 
        		{7,5,2, 7,5,2}, {7,5,3, 7,7,3}, {7,5,4, 7,5,4}, {7,5,5, 7,7,5}, {7,5,6, 7,5,6}, {7,5,7, 7,7,7}, {7,5,8, 7,5,8}, {7,5,9, 7,7,9}, {7,5,11, 7,7,11}, {7,5,12, 7,5,12}, {7,5,13, 7,7,13}, 
        		// Back wall 
        		{19,5,8, 19,5,8}, {18,5,8, 18,7,8}, {17,5,8, 17,5,8}, {16,5,8, 16,7,8}, 
        		{15,5,13, 15,7,13}, {14,5,13, 14,5,13}, {13,5,13, 13,7,13}, {12,5,13, 12,5,13}, {11,5,13, 11,7,13}, {10,5,13, 10,5,13}, {9,5,13, 9,7,13}, {8,5,13, 8,5,13}, 
        		
        		// Roof
        		{9,9,1, 10,9,1}, {11,10,1, 11,10,1}, {12,9,1, 13,9,1}, 
        		{20,9,4, 20,9,4}, {20,9,6, 20,9,6}, 
        		{9,9,13, 10,9,13}, {11,10,13, 11,10,13}, {12,9,13, 13,9,13}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0), 
        				false);
            }
    		
    		
            // Orange Terracotta
        	for (int[] uuvvww : new int[][]{
        		{14,5,12, 14,5,12}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor5 : 1),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor5 : 1), 
        				false);
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{5,8,0, 5,8,14}, {7,9,0, 7,9,14}, {9,10,0, 9,10,14}, {11,11,0, 11,11,14}, 
    			{13,10,0, 13,10,4}, {13,10,6, 13,10,14}, {14,10,4, 21,10,4}, {14,10,6, 21,10,6}, 
    			{15,9,0, 15,9,2}, {15,9,8, 15,9,14}, {16,9,2, 21,9,2}, {16,9,8, 21,9,8}, 
    			// In front of beds
    			{17,5,3, 17,5,7}, 
    			// Inside stairs
    			{8,4,2, 8,4,2}, {10,3,2, 10,3,2}, {12,2,2, 12,2,2}, {14,1,2, 14,1,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{6,8,0, 6,8,14}, {8,9,0, 8,9,0}, {8,9,2, 8,9,12}, {8,9,14, 8,9,14}, {10,10,0, 10,10,0}, {10,10,2, 10,10,12}, {10,10,14, 10,10,14}, 
    			{12,10,0, 12,10,0}, {12,10,2, 12,10,12}, {12,10,14, 12,10,14}, {13,10,5, 16,10,5}, {18,10,5, 19,10,5}, {21,10,5, 21,10,5}, 
    			{14,9,0, 14,9,0}, {14,9,2, 14,9,3}, {14,9,7, 14,9,12}, {14,9,14, 14,9,14}, {15,9,3, 19,9,3}, {21,9,3, 21,9,3}, {15,9,7, 19,9,7}, {21,9,7, 21,9,7}, 
    			{16,8,0, 16,8,1}, {16,8,9, 16,8,14}, {17,8,1, 21,8,1}, {17,8,9, 21,8,9}, 
    			// Balcony
    			{5,4,1, 6,4,8}, {5,4,12, 6,4,13}, 
    			// Dividing floor
    			{8,4,4, 8,4,12}, {9,4,3, 10,4,12}, {11,4,3, 11,4,4}, {11,4,6, 11,4,12}, {12,4,3, 12,4,3}, {12,4,7, 12,4,12}, {13,4,2, 13,4,4}, {13,4,6, 13,4,12}, {14,4,2, 14,4,12}, {14,4,3, 19,4,7}, 
    			// Inside stairs
    			{9,3,2, 9,3,2}, {11,2,2, 11,2,2}, {13,1,2, 13,1,2}, 
    			// Cake table
    			{11,1,9, 12,1,10}, 
    			// Shelves
    			{19,1,4, 19,2,4}, {19,1,6, 19,2,6}, 
    			// Second floor arches
    			{11,5,9, 11,5,9}, {11,5,11, 11,5,11}, 
    			{11,6,10, 11,6,10}, 
    			{11,7,9, 11,7,9}, {11,7,11, 11,7,11}, 
    			{13,7,8, 13,7,8}, 
    			{15,7,5, 15,7,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Room separator
    			{11,7,12, 3+4}, {11,7,10, 3+4}, 
    			{11,6,12, 3+4}, {11,6,11, 3+4}, {11,6,9, 3+4}, 
    			{11,5,12, 3+4}, {11,5,10, 3+4}, 
    			{12,5,8, 0+4}, {12,6,8, 0+4}, {12,7,8, 0+4}, 
    			{14,5,8, 1+4}, {14,6,8, 1+4}, {14,7,8, 1+4}, 
    			{15,5,7, 2+4}, {15,6,7, 2+4}, {15,7,7, 2+4}, {15,7,6, 3+4}, 
    			{15,5,3, 3+4}, {15,6,3, 3+4}, {15,7,3, 3+4}, {15,7,4, 2+4}, 
    			// Cake table
    			{10,1,10, 1+4}, {13,1,10, 0+4}, 
    			{10,1,9, 1+4}, {13,1,9, 0+4}, 
    			// Couch
    			{9,1,12, 3}, {10,1,12, 3}, {11,1,12, 3}, {12,1,12, 3}, {13,1,12, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	// Interior
            	{11,7,4}, {11,7,6}, 
            	{10,8,5}, {12,8,5}, 
            	{17,8,5}, 
            	{8,3,3}, {12,3,5}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{14,6,12}, 
            	{8,2,12}, 
            	{19,3,4}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Upstairs
        		{8,6,1}, {8,7,1}, {10,6,1}, {10,7,1}, {12,6,1}, {12,7,1}, {14,6,1}, {14,7,1}, 
        		{17,6,2}, {17,7,2}, {19,6,2}, {19,7,2}, 
        		{20,6,3}, {20,7,3}, {20,6,5}, {20,7,5}, {20,6,7}, {20,7,7}, 
        		{19,6,8}, {19,7,8}, {17,6,8}, {17,7,8}, 
        		{15,6,10}, {15,7,10}, {15,6,12}, {15,7,12}, 
        		{14,6,13}, {14,7,13}, {12,6,13}, {12,7,13}, {10,6,13}, {10,7,13}, {8,6,13}, {8,7,13}, 
        		{7,6,12}, {7,7,12}, {7,6,8}, {7,7,8}, {7,6,6}, {7,7,6}, {7,6,4}, {7,7,4}, {7,6,2}, {7,7,2}, 
        		// Downstairs
        		{8,2,1}, {8,3,1}, {10,2,1}, {10,3,1}, {12,2,1}, {12,3,1}, {14,2,1}, {14,3,1}, 
        		{17,2,2}, {17,3,2}, {19,2,2}, {19,3,2}, 
        		{20,2,3}, {20,3,3}, {20,2,5}, {20,3,5}, {20,2,7}, {20,3,7}, 
        		{19,2,8}, {19,3,8}, {17,2,8}, {17,3,8}, 
        		{15,2,10}, {15,3,10}, {15,2,12}, {15,3,12}, 
        		{14,2,13}, {14,3,13}, {12,2,13}, {12,3,13}, {10,2,13}, {10,3,13}, {8,2,13}, {8,3,13}, 
        		{7,2,6}, {7,3,6}, {7,2,4}, {7,3,4}, {7,2,2}, {7,3,2}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Trapdoor (Top Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Ceiling vents
            	{11,9,0, 2}, {11,9,2, 0}, 
            	{19,9,5, 3}, {21,9,5, 1}, 
            	{11,9,12, 2}, {11,9,14, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
        	// Orange Carpet
            int carpetMeta = (GeneralConfig.useVillageColors ? this.townColor5 : 1);
        	for(int[] uvwm : new int[][]{
        		// Entrance
        		{8,5,12, 8,5,12}, {10,5,12, 10,5,12}, 
        		{9,5,11, 9,5,11}, 
        		{8,5,10, 8,5,10}, {10,5,10, 10,5,10}, 
        		{9,5,9, 9,5,9}, 
        		{8,5,8, 8,5,8}, {10,5,8, 10,5,8}, 
        		// Downstairs rug
        		{9,1,4, 9,1,6}, {10,1,6, 10,1,7}, {11,1,7, 13,1,7}, {14,1,6, 14,1,7}, {15,1,4, 15,1,6}, 
        		{10,1,3, 10,1,4}, {11,1,3, 13,1,3}, {14,1,3, 14,1,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uvwm[0], uvwm[1], uvwm[2], uvwm[3], uvwm[4], uvwm[5], Blocks.CARPET.getStateFromMeta(carpetMeta), Blocks.CARPET.getStateFromMeta(carpetMeta), false);
            }
            
            
        	// Yellow Carpet
            carpetMeta = (GeneralConfig.useVillageColors ? this.townColor2 : 4);
        	for(int[] uvwm : new int[][]{
        		// Entrance
        		{9,5,12, 9,5,12}, 
        		{8,5,11, 8,5,11}, {10,5,11, 10,5,11}, 
        		{9,5,10, 9,5,10}, 
        		{8,5,9, 8,5,9}, {10,5,9, 10,5,9}, 
        		{9,5,8, 9,5,8}, 
        		// Downstairs rug
        		{11,1,6, 11,1,6}, {13,1,6, 13,1,6}, 
        		{10,1,5, 10,1,5}, {12,1,5, 12,1,5}, {14,1,5, 14,1,5}, 
        		{11,1,4, 11,1,4}, {13,1,4, 13,1,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uvwm[0], uvwm[1], uvwm[2], uvwm[3], uvwm[4], uvwm[5], Blocks.CARPET.getStateFromMeta(carpetMeta), Blocks.CARPET.getStateFromMeta(carpetMeta), false);
            }
            
            
        	// White Carpet
            carpetMeta = (GeneralConfig.useVillageColors ? this.townColor : 0);
        	for(int[] uvwm : new int[][]{
        		// Downstairs rug
        		{12,1,6, 12,1,6}, 
        		{11,1,5, 11,1,5}, {13,1,5, 13,1,5}, 
        		{12,1,4, 12,1,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uvwm[0], uvwm[1], uvwm[2], uvwm[3], uvwm[4], uvwm[5], Blocks.CARPET.getStateFromMeta(carpetMeta), Blocks.CARPET.getStateFromMeta(carpetMeta), false);
            }
            
            
            // Stone pressure plate
        	IBlockState biomeStonePressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_PRESSURE_PLATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{11,2,10}, {12,2,10}, 
        		})
            {
        		this.setBlockState(world, biomeStonePressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{17,1,3, 0}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
        	
        	
            // Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CRAFTING_TABLE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{17,1,7}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{8,1,9, 8,3,10}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
        	
        	
            // Cake
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{12,2,9}, 
           		})
        	{
            	this.setBlockState(world, Blocks.CAKE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Potted random flower
            for (int[] uvw : new int[][]{
        		{19,3,6}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{18,6,4, 3, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
            	{18,6,6, 3, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
            	{13,5,11, 2, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
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
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	                	{18,6,3, -1, 0}, 
	                	{18,6,7, -1, 0}, 
	                	{13,5,10, -1, 0}, 
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
    
    
    // --- Library --- //
    // designed by AstroTibs
    
    public static class JungleLibrary extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"           ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			"    FPF    ",
    			"    PPP    ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 14;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleLibrary() {}

        public JungleLibrary(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleLibrary buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleLibrary(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{2,0,11, 8,0,11}, 
    			{1,0,2, 1,0,11}, {9,0,2, 9,0,11}, 
    			{2,0,2, 8,0,2}, 
    			// Wall foundation
    			{1,1,2, 4,1,2}, {6,1,2, 9,1,2}, 
    			{1,1,3, 1,1,10}, {9,1,3, 9,1,10},
    			{1,1,11, 9,1,11},
    			// Column bottoms
    			{4,0,1, 4,1,1}, {6,0,1, 6,1,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,6,2, 9,6,2}, 
    			{1,7,4, 9,7,4}, 
    			{1,8,6, 3,8,7}, {7,8,6, 9,8,7}, 
    			{1,7,9, 9,7,9}, 
    			{1,6,11, 9,6,11}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			// Ceiling support
    			{1,7,8, 9,7,8}, 
    			{1,7,5, 9,7,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
    		}
    		
    		
    		// Concrete
    		IBlockState tryConcrete = ModObjects.chooseModConcreteState(GeneralConfig.useVillageColors ? townColor : 0); // White
        	Block concreteBlock = Blocks.STAINED_HARDENED_CLAY; int concreteMeta = GeneralConfig.useVillageColors ? townColor : 0; // White
        	if (tryConcrete != null) {concreteBlock = tryConcrete.getBlock(); concreteMeta = tryConcrete.getBlock().getMetaFromState(tryConcrete);}
    		for(int[] uuvvww : new int[][]{
    			// Front wall
    			{1,2,2, 2,3,2}, {4,2,2, 4,3,2}, 
    			{5,3,2, 5,3,2}, 
    			{8,2,2, 9,3,2}, {6,2,2, 6,3,2}, 
    			{1,4,2, 9,5,2}, 
    			// Left wall
    			{1,2,3, 1,3,3}, {1,2,5, 1,3,8}, {1,2,10, 1,3,10}, 
    			{1,4,3, 1,6,10}, 
    			// Left window frame
    			{0,7,5, 0,7,5}, {0,7,8, 0,7,8}, 
    			// Right wall
    			{9,2,3, 9,3,3}, {9,2,5, 9,3,8}, {9,2,10, 9,3,10}, 
    			{9,4,3, 9,6,10}, 
    			// Right window frame
    			{10,7,5, 10,7,5}, {10,7,8, 10,7,8}, 
    			// Back wall
    			{1,2,11, 2,3,11}, {4,2,11, 4,3,11}, 
    			{8,2,11, 9,3,11}, {6,2,11, 6,3,11}, 
    			{1,4,11, 9,5,11}, 
    			// Steeple front
    			{4,8,5, 6,9,5}, 
    			// Steeple back
    			{4,8,8, 6,9,8}, 
    			// Steeple left
    			{4,8,6, 4,9,7}, 
    			// Steeple right
    			{6,8,6, 6,9,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], concreteBlock.getStateFromMeta(concreteMeta), concreteBlock.getStateFromMeta(concreteMeta), false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Front entrance
    			{5,3,1, 2},  
    			// Interior
    			{5,4,3, 0}, // Front wall
    			{5,4,10, 2}, // Back wall
    			{2,4,4, 1}, {2,4,9, 1}, // Left wall
    			{8,4,4, 3}, {8,4,9, 3}, // Right wall
    			// Steeple
    			{5,8,6, 0},
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Logs (Vertical)
    		for(int[] uuvvww : new int[][]{
    			// Top of front columns
    			{4,5,1, 4,5,1}, {6,5,1, 6,5,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// For stripped logs specifically
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
    		for (int[] uuvvww : new int[][]{
    			// Front pillars
    			{4,2,1, 4,4,1}, {6,2,1, 6,4,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Window awnings
    			{3,4,1, 3}, {5,4,1, 3}, {7,4,1, 3}, // Front wall
    			{3,4,12, 2}, {5,4,12, 2}, {7,4,12, 2}, // Back wall
    			{0,4,3, 3}, {0,4,4, 0}, {0,4,5, 2}, {0,4,8, 3}, {0,4,9, 0}, {0,4,10, 2}, // Left wall
    			{10,4,3, 3}, {10,4,4, 1}, {10,4,5, 2}, {10,4,8, 3}, {10,4,9, 1}, {10,4,10, 2}, // Right wall
    			// Roof trim
    			{0,6,2, 0+4}, {0,7,4, 0+4}, {0,8,6, 0+4}, {10,6,2, 1+4}, {10,7,4, 1+4}, {10,8,6, 1+4}, 
    			{0,6,11, 0+4}, {0,7,9, 0+4}, {0,8,7, 0+4}, {10,6,11, 1+4}, {10,7,9, 1+4}, {10,8,7, 1+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,6,1, 10,6,1}, {0,7,3, 10,7,3}, {0,8,5, 3,8,5}, {7,8,5, 10,8,5}, 
    			{0,6,12, 10,6,12}, {0,7,10, 10,7,10}, {0,8,8, 3,8,8}, {7,8,8, 10,8,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Ceiling
    			{2,6,3, 8,6,3}, {2,6,10, 8,6,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{4,8,4, 4,8,4}, {6,8,4, 6,8,4}, 
            	{4,8,9, 4,8,9}, {6,8,9, 6,8,9}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
        	
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof trim
        		{0,6,3, 0,6,10, 0+4}, {10,6,3, 10,6,10, 1+4}, 
        		// Steeple
        		// Lower rim
        		{4,10,5, 6,10,5, 3}, 
        		{4,10,8, 6,10,8, 2}, 
        		{4,10,6, 4,10,7, 0}, 
        		{6,10,6, 6,10,7, 1}, 
        		// Upper rim
        		{4,12,5, 6,12,5, 3}, 
        		{4,12,8, 6,12,8, 2}, 
        		{4,12,6, 4,12,7, 0}, 
        		{6,12,6, 6,12,7, 1}, 
        		// Topmost
        		{5,13,6, 5,13,6, 3}, 
        		{5,13,7, 5,13,7, 2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{3,2,2}, {3,3,2}, {7,2,2}, {7,3,2}, 
        		// Back wall
        		{3,2,11}, {3,3,11}, {5,2,11}, {5,3,11}, {7,2,11}, {7,3,11}, 
        		// Left wall
        		{1,2,4}, {1,3,4}, {1,2,9}, {1,3,9}, {0,7,6}, {0,7,7}, 
        		// Right wall
        		{9,2,4}, {9,3,4}, {9,2,9}, {9,3,9}, {10,7,6}, {10,7,7}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	// Interior railing
            	{4,11,5, 6,11,5}, 
            	{4,11,6, 4,11,7}, {6,11,6, 6,11,7}, 
            	{4,11,8, 6,11,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            }
    		
        	
            // Lectern
            for (int[] uvwo : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{5,1,10, 2},
            })
            {
        		ModObjects.setModLecternState(world,
            			this.getXWithOffset(uvwo[0], uvwo[2]),
            			this.getYWithOffset(uvwo[1]),
            			this.getZWithOffset(uvwo[0], uvwo[2]),
            			uvwo[3],
            			this.getCoordBaseMode(),
            			biomePlankState.getBlock().getMetaFromState(biomePlankState),
            			-1 // Carpet color
        				);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{5,1,2, 0, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{2,1,3, 2,4,3}, {2,1,6, 2,4,7}, {2,1,10, 2,4,10}, 
        		{8,1,3, 8,4,3}, {8,1,6, 8,4,7}, {8,1,10, 8,4,10}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
            
            
        	// Wool - carpet in front of the door prevents villagers from passing through
            int woolMeta = (GeneralConfig.useVillageColors ? this.townColor3 : 14); // Red
        	for(int[] uvwm : new int[][]{
        		{2,0,3, 8,0,10}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uvwm[0], uvwm[1], uvwm[2], uvwm[3], uvwm[4], uvwm[5], Blocks.WOOL.getStateFromMeta(woolMeta), Blocks.WOOL.getStateFromMeta(woolMeta), false);
            }
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
				int u = 3+random.nextInt(5);
				int v = 1;
				int w = 3+random.nextInt(7);
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 1, 0); // Librarian
    			
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
    // designed by Lonemind
    
    public static class JungleMasonHouse extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"FFFFFFFFFFF  ",
    			"FFFFFFFFFFF  ",
    			"FFFFFFFFFFF  ",
    			"FFFFFFFFFFF  ",
    			"FFFFFFFFFFF  ",
    			"FFFFFFFFFFF  ",
    			"FFFFFFFFFFFF ",
    			"FFPFPFFFFFFFF",
    			"FFPPFPFFFFFFF",
    			"FFFPPFFFFFFFF",
    			"FFFFPFFFFFFF ",
    			"    FFFFFFF  ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 5;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleMasonHouse() {}

        public JungleMasonHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleMasonHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleMasonHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
            // Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{1,1,6, 1,1,8}, 
    			{2,1,6, 2,1,6}, {2,1,8, 2,1,10}, 
    			{3,1,6, 3,1,6}, {3,1,8, 3,1,8}, {3,1,10, 3,1,10}, 
    			{4,1,6, 4,1,6}, {4,1,9, 4,1,9}, 
    			{5,1,9, 5,1,9}, 
    			{6,1,7, 6,1,7}, {6,1,9, 6,1,10}, 
    			{7,1,7, 7,1,7}, {7,1,9, 7,1,9}, 
    			{8,1,6, 8,1,6}, {8,1,8, 8,1,9}, 
    			{9,1,7, 9,1,8}, {9,1,10, 9,1,10}, 
    			{10,1,7, 10,1,7}, {10,1,9, 10,1,10}, 
    			// Frame
    			{1,3,6, 1,3,6}, 
    			{1,2,10, 1,2,10}, 
    			{5,3,10, 5,3,10}, 
    			// Roof
    			{1,5,7, 1,5,7}, {2,5,10, 3,5,10}, {3,5,6, 3,5,6}, {5,5,9, 5,5,9}, 
    			{3,6,8, 3,6,8}, 
    			// Pile
    			{6,1,1, 7,1,1}, {9,1,1, 9,1,1}, 
    			{5,1,2, 11,1,2}, 
    			{7,2,2, 9,2,2}, 
    			{7,1,3, 9,1,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
    		}
    		
    		
    		// Mossy Stone Brick
    		IBlockState biomeMossyStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(1), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{1,1,9, 1,1,10}, 
    			{2,1,7, 2,1,7}, 
    			{3,1,7, 3,1,7}, {3,1,9, 3,1,9}, 
    			{4,1,7, 4,1,8}, {4,1,10, 4,1,10}, 
    			{5,1,6, 5,1,8}, {5,1,10, 5,1,10}, 
    			{6,1,6, 7,1,6}, {6,1,8, 7,1,8}, 
    			{7,1,10, 7,1,10}, 
    			{8,1,7, 8,1,7}, {8,1,10, 8,1,10}, 
    			{9,1,6, 9,1,6}, {9,1,9, 9,1,9}, 
    			{10,1,6, 10,1,6}, {10,1,8, 10,1,8}, 
    			// Frame
    			{1,2,6, 1,2,6}, 
    			{1,3,10, 1,3,10}, 
    			{5,1,6, 5,3,6}, 
    			{5,2,10, 5,2,10}, 
    			// Roof
    			{1,5,8, 1,5,9}, {2,5,6, 3,5,6}, {4,5,6, 4,5,6}, {4,5,10, 4,5,10}, {5,5,7, 5,5,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyStoneBrickState, biomeMossyStoneBrickState, false);
    		}
    		
    		
    		// Chiseled Stone Brick
    		IBlockState biomeChiseledStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			// Frame
    			{1,4,10}, {5,4,10}, 
    			{1,4,6}, {5,4,6}, 
    			})
    		{
    			this.setBlockState(world, biomeChiseledStoneBrickState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Front Steps
    			{2,1,5, 0}, {3,1,5, 3}, {4,1,5, 1}, 
    			// Roof
    			{2,6,9, 0}, {3,6,9, 2}, {4,6,9, 1}, 
    			{2,6,8, 0}, {4,6,8, 1}, 
    			{2,6,7, 0}, {3,6,7, 3}, {4,6,7, 1}, 
    			// Workspace
    			{9,2,7, 3}, 
    			// Pile
    			{6,1,3, 0}, {8,1,1, 0}, {8,1,4, 2}, {10,1,1, 3}, {10,2,2, 2}, {10,1,3, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Stone Brick Slab (lower)
    		IBlockState biomeStoneBrickSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(5), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvwo : new int[][]{
    			{7,2,9}, {8,2,3}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickSlabLowerState, uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Smooth Stone Double Slab
    		IBlockState biomePolishedStoneSlabDoubleState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwo : new int[][]{
            	{0,1,1}, {0,1,3}, {1,1,4}, 
            	}) {
            	this.setBlockState(world, biomePolishedStoneSlabDoubleState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
    		
    		
    		// Smooth Stone Slab (lower)
    		IBlockState biomePolishedStoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvwo : new int[][]{
    			{0,2,3}, 
    			{1,1,2}, 
    			{6,2,2}, 
    			})
    		{
    			this.setBlockState(world, biomePolishedStoneSlabLowerState, uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
        	
        	// Stone Brick wall
    		IBlockState biomeStoneBrickWallStone = ModObjects.chooseModStoneBrickWallState();
        	biomeStoneBrickWallStone = StructureVillageVN.getBiomeSpecificBlockState(biomeStoneBrickWallStone!=null?biomeStoneBrickWallStone:Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{6,2,6, 10,2,6}, {10,2,7, 10,2,9}, {6,2,10, 10,2,10}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickWallStone, biomeStoneBrickWallStone, false);
            }
    		
    		
            // Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Front wall
        		{2,2,6, 2,4,6}, {3,4,6, 3,4,6}, {4,2,6, 4,4,6}, 
        		// Right wall
        		{5,2,7, 5,4,7}, {5,4,8, 5,4,8}, {5,2,9, 5,4,9}, 
        		// Left wall
        		{1,2,7, 1,4,7}, {1,2,8, 1,2,8}, {1,4,8, 1,4,8}, {1,2,9, 1,4,9}, 
        		// Back wall
        		{2,2,10, 2,4,10}, {3,2,10, 3,2,10}, {3,4,10, 3,4,10}, {4,2,10, 4,4,10}, 
        		})
            {
        		// Cyan
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor4 : 9)),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor4 : 9)), 
        				false);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Front wall
    			{2,3,5, 2}, {4,3,5, 2}, 
        		// Right wall
    			{6,3,7, 1}, {6,3,9, 1}, 
        		// Left wall
    			{0,3,7, 3}, {0,3,9, 3}, 
        		// Back wall
    			{2,3,11, 0}, {4,3,11, 0}, 
    			// Interior
    			{3,4,9, 2}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Window awnings
    			{2,2,9, 1}, {4,2,9, 0}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{1,3,8}, 
        		// Back wall
        		{3,3,10}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Rails
    		for(int[] uvwo : new int[][]{ // Orientation where track increases in elevation: 0=right-facing; 1=back-facing (toward you); 2=left-facing; 3=fore-facing (away from you)
    			// Left rail
    			{7,1,4, StructureVillageVN.chooseFurnaceMeta(1, this.getCoordBaseMode())},
    			{7,2,3, StructureVillageVN.chooseFurnaceMeta(1, this.getCoordBaseMode())},
    			{7,3,2, 1}, // Along
    			{7,2,1, StructureVillageVN.chooseFurnaceMeta(3, this.getCoordBaseMode())},
    			{7,1,0, StructureVillageVN.chooseFurnaceMeta(3, this.getCoordBaseMode())},
    			// Right rail
    			{9,1,4, StructureVillageVN.chooseFurnaceMeta(1, this.getCoordBaseMode())},
    			{9,2,3, StructureVillageVN.chooseFurnaceMeta(1, this.getCoordBaseMode())},
    			{9,3,2, 1}, // Along
    			{9,2,1, StructureVillageVN.chooseFurnaceMeta(3, this.getCoordBaseMode())},
    			{9,1,0, StructureVillageVN.chooseFurnaceMeta(3, this.getCoordBaseMode())},
    			})
    		{
    			this.setBlockState(world, Blocks.RAIL.getStateFromMeta(uvwo[3]), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
        	
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{3,2,6, 2, 1, 0}, 
    			{5,2,8, 1, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
        	
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            // Table
            IBlockState[] tableComponentBlockstates = ModObjects.chooseModWoodenTable(biomePlankState.getBlock()==Blocks.PLANKS ? biomePlankState.getBlock().getMetaFromState(biomePlankState) : 0);
        	for (int[] uuvvww : new int[][]{
        		{3,2,9}, 
        		})
            {
        		for (int i=1; i>=0; i--)
        		{
        			this.setBlockState(world, tableComponentBlockstates[i], uuvvww[0], uuvvww[1]+1-i, uuvvww[2], structureBB);
        		}
            }
        	
    		
            // Stone Cutter
        	// Orientation:0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	IBlockState stonecutterState = ModObjects.chooseModStonecutterState(3);
            this.setBlockState(world, stonecutterState, 8, 2, 8, structureBB);
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s = random.nextInt(30);
    			
    			// Villager
				int u = s<=3? 2 : s<=7? 3 : s<=11? 4 : s<=13? 2 : s<=15? 3 : s<=17? 4 : s<=20? 6 : s<=22? 7 : s<=24? 8 : 9;
				int v = s<=11? 1 : 2;
				int w = s<=3? s+1 : s<=7? s-3 : s<=11? s-10 : s<=13? s-5 : s<=15? s-7 : s<=17? s-9 : s<=20? s-11 : s<=22? s-14 : s<=23? s-16 : s<=24? s-15 : s-17;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 4, 0); // Mason
    			
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
    // designed by AstroTibs, DelirusCrux, and THASSELHOFF
    
    public static class JungleMediumHouse1 extends StructureVillageVN.VNComponent
    {
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
    			"FFFFFFFFFFFF",
    			"FFFFPPFFFFFF",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 13;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleMediumHouse1() {}

        public JungleMediumHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleMediumHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleMediumHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Next to entry path
    			{3,0,0, 3,0,0}, {6,0,0, 6,0,0}, 
    			// Lower level garden foundation
    			{0,1,0, 3,1,0}, {6,1,0, 11,1,0}, 
    			{0,1,1, 0,1,2}, {11,1,1, 11,1,10}, 
    			{0,1,11, 11,1,11}, 
    			// Front stairs
    			{4,1,2, 5,1,2}, 
    			// Back stairs
    			{0,1,10, 1,2,10}, {0,1,11, 2,2,11}, {2,1,10, 2,1,10}, 
    			// Foundation and floor
    			{0,1,3, 9,2,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
        	
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entryway
        		{4,1,1, 5,1,1, 3}, {4,2,2, 5,2,2, 3}, 
        		// Back
        		{2,2,10, 2,2,10, 1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);
            }
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Lower wall
        		{0,2,1, 0,2,2}, 
            	{0,2,0, 3,2,0}, {6,2,0, 11,2,0}, 
            	{11,2,1, 11,2,10}, 
            	{3,2,11, 11,2,11}, 
            	// Higher wall
            	{0,3,3, 3,3,3}, {6,3,3, 9,3,3}, {9,3,4, 9,3,4}, 
            	{0,3,11, 2,3,11}, {0,3,4, 0,3,11}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Vertical
    			{3,3,5, 3,9,5}, {9,3,5, 9,9,5}, 
    			{3,3,9, 3,9,9}, {9,3,9, 9,9,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
            // Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Front wall
        		{4,3,5, 4,7,5}, {4,9,5, 4,9,5}, 
        		{5,3,5, 5,3,5}, {5,5,5, 5,9,5}, 
        		{6,3,5, 6,6,5}, {6,9,5, 6,9,5}, 
        		{7,5,5, 7,9,5}, 
        		{8,3,5, 8,7,5}, {8,9,5, 8,9,5}, 
        		// Right wall
        		{3,3,6, 3,9,6}, 
        		{3,3,7, 3,3,7}, {3,5,7, 3,7,7}, {3,9,7, 3,9,7}, 
        		{3,3,8, 3,9,8}, 
        		// Left wall
        		{9,3,6, 9,9,6}, 
        		{9,3,7, 9,3,7}, {9,5,7, 9,7,7}, {9,9,7, 9,9,7}, 
        		{9,3,8, 9,9,8}, 
        		// Back wall
        		{4,3,9, 4,4,9}, {4,6,9, 4,7,9}, {4,9,9, 4,9,9}, 
        		{5,3,9, 5,9,9}, 
        		{6,3,9, 6,5,9}, {6,8,9, 6,9,9}, 
        		{7,3,9, 7,9,9}, 
        		{8,3,9, 8,4,9}, {8,6,9, 8,7,9}, {8,9,9, 8,9,9}, 
        		})
            {
        		// White
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0), 
        				false);
            }
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Tops of support posts
    			{3,6,3, 3,6,3}, {6,6,3, 6,6,3}, {9,6,3, 9,6,3}, 
    			// Roof
    			{3,10,3, 9,10,9}, {5,11,5, 7,11,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Side awning
    			{1,5,3, 1,5,10}, 
    			// Upper balcony
    			{4,6,3, 5,6,3}, {7,6,3, 8,6,3}, {3,6,4, 9,6,4}, 
    			// Interior floors
    			{4,6,6, 8,6,7}, {4,6,8, 4,6,8}, 
    			// Table
    			{8,3,8, 8,3,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Side awning
    			{0,5,3, 0,5,11}, {2,6,3, 2,6,9}, 
    			// Roof
    			{2,10,2, 10,10,2}, {2,10,10, 10,10,10}, {2,10,3, 2,10,9}, {10,10,3, 10,10,9}, 
    			{4,11,4, 8,11,4}, {4,11,8, 8,11,8}, {4,11,5, 4,11,7}, {8,11,5, 8,11,7}, 
    			{6,12,6, 6,12,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Awning
        		{0,4,3, 0,4,3}, {0,4,11, 0,4,11}, 
        		// Balcony railing
        		{3,7,3, 3,7,4}, {4,7,3, 8,7,3}, {9,7,3, 9,7,4}, 
        		// Supports
        		{3,4,3, 3,5,3}, {6,4,3, 6,5,3}, {9,4,3, 9,5,3}, 
        		{3,8,3, 3,9,3}, {9,8,3, 9,9,3}, 
        		// Interior
        		{5,9,8, 5,9,8}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Interior staircase
    			{5,3,8, 0}, {6,4,8, 0}, {7,5,8, 0}, {8,6,8, 0}, 
    			{6,3,8, 1+4}, {7,4,8, 1+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Exterior fence
    			{0,3,0, -1}, {3,3,0, -1}, {6,3,0, -1}, {11,3,0, -1}, {2,4,11, -1}, {11,3,0, -1}, {11,3,11, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// On the table
            	{8,4,8}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	// Interior
            	{5,8,8}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{5,4,5}, 
        		{4,8,5}, {8,8,5}, 
        		// Back wall
        		{4,5,9}, {8,5,9}, 
        		{6,6,9}, {6,7,9}, 
        		{4,8,9}, {8,8,9}, 
        		// Right wall
        		{3,4,7}, {3,8,7}, 
        		// Left wall
        		{9,4,7}, {9,8,7}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Table
            	{8,3,7, 2}, 
            	{7,3,8, 3}, 
            	// Upstairs railing
            	{5,7,7, 2}, {6,7,7, 2}, {7,7,7, 2}, {5,7,8, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{7,3,5, 2, 1, 0}, 
    			{6,7,5, 2, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Grass - NOT biome-swapped
    		for (int[] uuvvww : new int[][]{
        		// Front shrubs
        		{1,1,1, 3,1,2}, {6,1,1, 10,1,2}, {10,1,3, 10,1,10}, {3,1,10, 9,1,10}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.GRASS.getDefaultState(),
        				Blocks.GRASS.getDefaultState(), 
        				false);
            }
    		
    		
    		// Tall Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			{10,2,4, 0}, 
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
    		
        	
        	// Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Front shrubs
        		{1,2,1, 1,2,1}, {10,2,1, 10,2,1}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,7,7, 2, GeneralConfig.useVillageColors ? this.townColor3 : 0}, // Red
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
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
            		// Front side
        			{3,2,2, 2}, {3,3,2, 2}, {3,4,2, 2}, {3,5,2, 2}, {3,6,2, 2}, 
        			{9,2,2, 2}, {9,3,2, 2}, {9,4,2, 2}, {9,5,2, 2}, {9,6,2, 2}, 
        			// Right side
        			{10,8,6, 1}, {10,9,6, 1}, 
        			{10,9,7, 1}, 
        			// Back side
        			{5,9,10, 0}, 
        			{5,9,10, 0}, 
        			{5,3,10, 0}, {5,4,10, 0}, {5,5,10, 0}, {5,6,10, 0}, 
        			{6,6,10, 0}, {6,7,10, 0}, {6,8,10, 0}, {6,9,10, 0}, {6,10,10, 0}, 
        			{7,2,10, 0}, {7,3,10, 0}, {7,4,10, 0}, {7,5,10, 0}, {7,6,10, 0}, {7,7,10, 0}, {7,8,10, 0}, {7,9,10, 0}, 
        			{8,6,10, 0}, {8,7,10, 0}, 
        			{8,9,10, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{4,7,6, -1, 0},
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
    // designed by AstroTibs, DelirusCrux, and THASSELHOFF
    
    public static class JungleMediumHouse2 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"             ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			"    F PP F   ",
    			"      PP     ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 12;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 3;
    	private static final int DECREASE_MAX_U = 4;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleMediumHouse2() {}

        public JungleMediumHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleMediumHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleMediumHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Lined foundation
    			{1,1,2, 5,1,2}, {8,1,2, 11,1,2}, 
    			{1,1,3, 1,1,9}, {11,1,3, 11,1,9}, 
    			{1,1,10, 11,1,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Vertical
    			{1,5,2, 11,5,2}, {1,5,3, 1,5,9}, {11,5,3, 11,5,9}, {1,5,10, 11,5,10}, 
    			{1,8,2, 11,8,2}, {1,8,3, 1,8,9}, {11,8,3, 11,8,9}, {1,8,10, 11,8,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// Stripped logs (Vertical)
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
    		for (int[] uuvvww : new int[][]{
    			{1,2,2, 1,4,2}, {11,2,2, 11,4,2}, {1,2,10, 1,4,10}, {11,2,10, 11,4,10}, 
    			{1,6,2, 1,7,2}, {11,6,2, 11,7,2}, {1,6,10, 1,7,10}, {11,6,10, 11,7,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
    		
            // Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Front wall
        		{2,4,2, 10,4,2}, 
        		{2,3,2, 2,3,2}, {4,3,2, 5,3,2}, {8,3,2, 8,3,2}, {10,3,2, 10,3,2}, 
        		{2,2,2, 5,2,2}, {8,2,2, 10,2,2}, 
        		{2,6,2, 2,7,2}, {4,6,2, 4,7,2}, {6,6,2, 6,7,2}, {8,6,2, 8,7,2}, {10,6,2, 10,7,2}, 
        		// Right wall 
        		{1,4,3, 1,4,9}, 
        		{1,3,3, 1,3,3}, {1,3,5, 1,3,5}, {1,3,7, 1,3,7}, {1,3,9, 1,3,9}, 
        		{1,2,3, 1,2,9}, 
        		{1,6,3, 1,7,3}, {1,6,5, 1,7,5}, {1,6,7, 1,7,7}, {1,6,9, 1,7,9}, 
        		{1,9,4, 1,9,5}, {1,10,6, 1,10,6},  {1,9,7, 1,9,8}, 
        		// Left wall 
        		{11,4,3, 11,4,9}, 
        		{11,3,3, 11,3,3}, {11,3,5, 11,3,5}, {11,3,7, 11,3,7}, {11,3,9, 11,3,9}, 
        		{11,2,3, 11,2,9}, 
        		{11,6,3, 11,7,3}, {11,6,5, 11,7,5}, {11,6,7, 11,7,7}, {11,6,9, 11,7,9}, 
        		{11,9,4, 11,9,5}, {11,10,6, 11,10,6}, {11,9,7, 11,9,8}, 
        		// Back wall 
        		{2,4,10, 10,4,10}, 
        		{2,3,10, 2,3,10}, {4,3,10, 4,3,10}, {6,3,10, 6,3,10}, {8,3,10, 8,3,10}, {10,3,10, 10,3,10}, 
        		{2,2,10, 10,2,10}, 
        		{2,6,10, 2,7,10}, {4,6,10, 4,7,10}, {6,6,10, 6,7,10}, {8,6,10, 8,7,10}, {10,6,10, 10,7,10}, 
        		})
            {
        		// White
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0), 
        				false);
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		
        		// Above entryway
        		{6,3,2, 1, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
        		{7,3,2, 2, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
        		// Entry floor
        		{7,0,2, 0, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
        		{6,0,2, 1, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
        		{6,0,3, 2, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
        		{7,0,3, 3, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], StructureVillageVN.chooseGlazedTerracottaMeta(uvwoc[3], this.getCoordBaseMode()));
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Tops of walls to connect to roof
    			{1,9,3, 1,9,3}, {1,10,5, 1,10,5}, {1,10,7, 1,10,7}, {1,9,9, 1,9,9}, 
    			{11,9,3, 11,9,3}, {11,10,5, 11,10,5}, {11,10,7, 11,10,7}, {11,9,9, 11,9,9}, 
    			// Floor
    			{2,1,3, 5,1,9}, {6,1,5, 7,1,9}, {8,1,3, 8,1,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,8,1, 12,8,1}, 
    			{0,9,3, 0,9,3}, {2,9,3, 10,9,3}, {12,9,3, 12,9,3}, 
    			{0,10,5, 0,10,5}, {2,10,5, 10,10,5}, {12,10,5, 12,10,5}, 
    			{0,10,7, 0,10,7}, {2,10,7, 10,10,7}, {12,10,7, 12,10,7}, 
    			{0,9,9, 0,9,9}, {2,9,9, 10,9,9}, {12,9,9, 12,9,9}, 
    			{0,8,11, 12,8,11}, 
    			// Upstairs floor
    			{2,4,3, 2,4,8}, 
    			{3,4,3, 5,4,9}, 
    			{6,4,3, 7,4,4}, {6,4,9, 7,4,9}, 
    			{8,4,3, 10,4,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,9,2, 12,9,2}, 
    			{0,10,4, 12,10,4}, 
    			{0,11,6, 12,11,6}, 
    			{0,10,8, 12,10,8}, 
    			{0,9,10, 12,9,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Ceiling lantern supports
        		{4,9,6, 4,10,6}, {8,9,6, 8,10,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Front entrance
    			{6,1,4, 3}, {7,1,4, 3}, 
    			// Chairs
    			{2,2,3, 2}, {2,2,5, 3}, 
    			// Upstairs
    			{6,2,6, 3}, {7,2,6, 3}, {6,2,7, 2+4}, {7,2,7, 2+4}, 
    			{6,3,7, 3}, {7,3,7, 3}, {6,3,8, 2+4}, {7,3,8, 2+4}, 
    			{6,4,8, 3}, {7,4,8, 3}, {6,4,9, 2+4}, {7,4,9, 2+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// First floor
    			{4,3,3, 0}, 
    			{4,3,9, 2}, {8,3,9, 2}, 
    			{10,3,5, 3}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	// Hanging from ceiling
            	{4,8,6}, {8,8,6},  
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{3,3,2}, {9,3,2}, 
        		{3,6,2}, {3,7,2}, {5,6,2}, {5,7,2}, {7,6,2}, {7,7,2}, {9,6,2}, {9,7,2}, 
        		// Back wall
        		{3,3,10}, {5,3,10}, {7,3,10}, {9,3,10}, 
        		{3,6,10}, {3,7,10}, {5,6,10}, {5,7,10}, {7,6,10}, {7,7,10}, {9,6,10}, {9,7,10}, 
        		// Right wall
        		{1,3,4}, {1,3,6}, {1,3,8}, {1,6,4}, {1,7,4}, {1,6,6}, {1,7,6}, {1,6,8}, {1,7,8}, 
        		// Left wall
        		{11,3,4}, {11,3,6}, {11,3,8}, {11,6,4}, {11,7,4}, {11,6,6}, {11,7,6}, {11,6,8}, {11,7,8}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Trapdoor (Top Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Front planter
            	{3,1,1, 3}, {4,1,0, 2}, {5,1,1, 1}, {8,1,1, 3}, {9,1,0, 2}, {10,1,1, 1}, 
            	// Stairs
            	{5,2,7, 3}, {5,3,8, 3}, 
            	{8,2,7, 1}, {8,3,8, 1}, 
            	// Left attic vent
            	{0,9,6, 3}, {2,9,6, 1}, 
            	// Right attic vent
            	{10,9,6, 3}, {12,9,6, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Table
            IBlockState[] tableComponentBlockstates = ModObjects.chooseModWoodenTable(biomePlankState.getBlock()==Blocks.PLANKS ? biomePlankState.getBlock().getMetaFromState(biomePlankState) : 0);
        	for (int[] uuvvww : new int[][]{
        		{2,2,4}, 
        		{10,2,4}, 
        		{10,2,6}, 
        		})
            {
        		for (int i=1; i>=0; i--)
        		{
        			this.setBlockState(world, tableComponentBlockstates[i], uuvvww[0], uuvvww[1]+1-i, uuvvww[2], structureBB);
        		}
            }
            
        	
        	// Hanging Sign (Blank)
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		// Chair arm rests
        		{3,2,3, 1}, {3,2,5, 1}, 
        		})
            {
                int signX = this.getXWithOffset(uvwo[0], uvwo[2]);
                int signY = this.getYWithOffset(uvwo[1]);
                int signZ = this.getZWithOffset(uvwo[0], uvwo[2]);
                
            	world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // Facing away from you
            }
            
        	
        	// Brick furnace chimney
        	
            // Brick Walls
        	IBlockState biomeBrickWallState = null;
        	IBlockState biomeBrickBlockState = Blocks.BRICK_BLOCK.getDefaultState();
        	
        	// First, attempt to obtain modded brick wall
        	biomeBrickWallState = ModObjects.chooseModBrickWallState();
        	if (biomeBrickWallState==null)
        	{
        		// Use cobblestone
        		biomeBrickWallState = Blocks.COBBLESTONE_WALL.getStateFromMeta(0);
        		biomeBrickBlockState = Blocks.COBBLESTONE.getStateFromMeta(0);
        	}
        	
        	// Convert to biome-specific versions
        	biomeBrickWallState = StructureVillageVN.getBiomeSpecificBlockState(biomeBrickWallState, this.materialType, this.biome, this.disallowModSubs);
        	biomeBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(biomeBrickBlockState, this.materialType, this.biome, this.disallowModSubs);
        	
        	
        	for(int[] uuvvww : new int[][]{
    			// Furnace chimney
    			{2,3,9, 2,3,9}, {2,5,9, 2,8,9}, {2,10,9, 2,10,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBrickWallState, biomeBrickWallState, false);
    		}
        	
            // Brick Blocks
        	for(int[] uuvvww : new int[][]{
    			// Roof plugs
    			{2,4,9, 2,4,9}, {2,9,9, 2,9,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBrickBlockState, biomeBrickBlockState, false);
    		}
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,2,9, 2}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
                    	
            // Polished Granite
            IBlockState polishedGraniteState = Blocks.STONE.getStateFromMeta(2);
        	for (int[] uvw : new int[][]{
        		{3,2,9}, 
        		})
            {
        		this.setBlockState(world, polishedGraniteState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CRAFTING_TABLE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{4,2,9}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{9,2,9, 9,2,9}, 
        		{10,2,9, 10,3,9}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
            
            
            // Wool
            IBlockState woolBlockState = Blocks.WOOL.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0); // White
            for (int[] uuvvww : new int[][]{
            	{9,1,3, 10,1,9}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], woolBlockState, woolBlockState, false);
            }
    		
    		
        	// Patterned banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{5,3,1, 2, 0}, // White
				{8,3,1, 2, 0}, // White
				{5,3,3, 0, 0}, // White
				{8,3,3, 0, 0}, // White
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
					modifystanding.setInteger("Base", 15 - uvwoc[4]);
    				tilebanner.readFromNBT(modifystanding);
				}
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{6,1,2, 2, 1, 1}, 
    			{7,1,2, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Grass - NOT biome-swapped
    		for (int[] uuvvww : new int[][]{
        		// Front planters
        		{4,1,1, 4,1,1}, {9,1,1, 9,1,1}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.GRASS.getDefaultState(),
        				Blocks.GRASS.getDefaultState(), 
        				false);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{4,2,1, 0},
            	{9,2,1, 0},
            })
            {
    			if (uwg[3]==0) // Fern
    			{
    				this.setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(2), uwg[0], uwg[1], uwg[2], structureBB);
    			}
    			else // Large Fern
    			{
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(3), uwg[0], uwg[1], uwg[2], structureBB);
    				this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uwg[0], uwg[1]+1, uwg[2], structureBB);
    			}
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,5,8, 2, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
            	{9,5,8, 2, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
            	{3,5,4, 0, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
            	{9,5,4, 0, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
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
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{4,5,8, -1, 0}, {8,5,8, -1, 0}, 
	        			{4,5,4, -1, 0}, {8,5,4, -1, 0}, 
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
    
    
    // --- Medium House 3 --- //
    // designed by AstroTibs
    
    public static class JungleMediumHouse3 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"    FFF    ",
    			"   FPPPF   ",
    			"  FPPPPPF  ",
    			" FPPPPPPPF ",
    			"FFPPFPFPPFF",
    			"FFPPPPPPPFF",
    			"FFPPFPFPPFF",
    			" FPPPPPPPF ",
    			"  FPPPPPF  ",
    			"   FPPPF   ",
    			"    FFF    ",
    			"    PPP    ",
    			"  FFPPPFF  ",
    			"  FPPPPPF  ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1+4; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 3;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleMediumHouse3() {}

        public JungleMediumHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleMediumHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleMediumHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Hay bales (vertical)
    		IBlockState biomeHayBaleVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.HAY_BLOCK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,1,3, 4,2,3}, {6,1,3, 6,2,3}, 
    			{3,1,4, 3,2,4}, {7,1,4, 7,2,4}, 
    			{2,1,5, 2,2,5}, {8,1,5, 8,2,5}, 
    			{1,1,6, 1,2,6}, {9,1,6, 9,2,6}, 
    			{0,1,7, 0,2,9}, {10,1,7, 10,2,9}, 
    			{1,1,10, 1,2,10}, {9,1,10, 9,2,10}, 
    			{2,1,11, 2,2,11}, {8,1,11, 8,2,11}, 
    			{3,1,12, 3,2,12}, {7,1,12, 7,2,12}, 
    			{4,1,13, 4,2,13}, {6,1,13, 6,2,13}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeHayBaleVertState, biomeHayBaleVertState, false);
    		}
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Front garden
    			{2,1,0, 2,1,0}, {3,1,1, 3,1,1}, 
    			{8,1,0, 8,1,0}, {7,1,1, 7,1,1}, 
    			// Wall toppers
    			{1,3,6, 1,3,6}, {2,3,5, 2,3,5}, {8,3,5, 8,3,5}, {9,3,6, 9,3,6}, 
    			{1,3,10, 1,3,10}, {2,3,11, 2,3,11}, {8,3,11, 8,3,11}, {9,3,10, 9,3,10}, 
    			{1,3,6, 1,3,6}, {2,3,5, 2,3,5}, {8,3,5, 8,3,5}, {9,3,6, 9,3,6}, 
    			// Central spire
    			{4,5,9, 4,6,9}, {6,5,9, 6,6,9}, 
    			{4,5,7, 4,6,7}, {6,5,7, 6,6,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			{3,3,4, 7,3,4}, 
    			{3,3,12, 7,3,12}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
    		}
    		
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			{0,3,7, 0,3,9}, {10,3,7, 10,3,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,4,11, 7,4,11}, 
    			{2,4,10, 3,4,10}, {7,4,10, 8,4,10}, 
    			{1,4,8, 1,4,8}, {2,4,7, 2,4,9}, {8,4,7, 8,4,9}, {9,4,8, 9,4,8}, 
    			{2,4,6, 3,4,6}, {7,4,6, 8,4,6}, 
    			{3,4,5, 7,4,5}, 
    			
    			{5,5,10, 5,5,10}, 
    			{3,5,8, 3,5,8}, {7,5,8, 7,5,8}, 
    			{5,5,6, 5,5,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{5,7,8, 5,7,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{4,7,9, 4,7,9}, {6,7,9, 6,7,9}, 
    			{4,7,7, 4,7,7}, {6,7,7, 6,7,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
    			{4,1,9, 4,4,9}, {6,1,9, 6,4,9}, 
    			{4,1,7, 4,4,7}, {6,1,7, 6,4,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	
        	// Dirt
        	for (int[] uuvvww : new int[][]{
    			{4,0,9, 3,0,9}, {6,0,9, 5,0,9}, 
    			{4,0,7, 3,0,7}, {6,0,7, 5,0,7}, 
            	{1,1,7, 1,1,9}, 
            	{9,1,7, 9,1,9}, 
            	// Under doors
            	{5,0,13, 5,0,13}, 
            	{5,0,3, 5,0,3}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeFillerState,
        				biomeFillerState, 
        				false);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Over back entrance
    			{4,3,13, 2}, {5,3,13, 2}, {6,3,13, 2}, 
    			// Roof
    			{3,4,12, 2}, {4,4,12, 2}, {5,4,12, 2}, {6,4,12, 2}, {7,4,12, 2}, 
    			{2,4,11, 2}, {3,4,11, 0}, {7,4,11, 1}, {8,4,11, 2}, 
    			{1,4,10, 2}, {2,4,10, 0}, {8,4,10, 1}, {9,4,10, 2}, 
    			{0,4,9, 2}, {1,4,9, 0}, {9,4,9, 1}, {10,4,9, 2}, 
    			{0,4,8, 0}, {10,4,8, 1}, 
    			{0,4,7, 3}, {1,4,7, 0}, {9,4,7, 1}, {10,4,7, 3}, 
    			{1,4,6, 3}, {2,4,6, 0}, {8,4,6, 1}, {9,4,6, 3}, 
    			{2,4,5, 3}, {3,4,5, 0}, {7,4,5, 1}, {8,4,5, 3}, 
    			{3,4,4, 3}, {4,4,4, 3}, {5,4,4, 3}, {6,4,4, 3}, {7,4,4, 3}, 
    			
    			{3,5,9, 0}, {7,5,9, 1}, 
    			{3,5,7, 0}, {7,5,7, 1}, 
    			
    			{4,5,11, 2}, {5,5,11, 2}, {6,5,11, 2}, 
    			{3,5,10, 2}, {4,5,10, 0}, {6,5,10, 1}, {7,5,10, 2}, 
    			{2,5,9, 2}, {8,5,9, 2}, 
    			{2,5,8, 0}, {8,5,8, 1}, 
    			{2,5,7, 3}, {8,5,7, 3}, 
    			{3,5,6, 3}, {4,5,6, 0}, {6,5,6, 1}, {7,5,6, 3}, 
    			{4,5,5, 3}, {5,5,5, 3}, {6,5,5, 3}, 
    			
    			{5,7,9, 2}, 
    			{4,7,8, 0}, {6,7,8, 1}, 
    			{5,7,7, 3}, 
    			
    			// Over front entrance
    			{4,3,3, 3}, {5,3,3, 3}, {6,3,3, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// First floor
    			{5,4,10, 2}, 
    			{1,3,8, 1}, {9,3,8, 3}, 
    			{5,4,6, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
        	
        	
            // Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CRAFTING_TABLE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{1,1,9}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 9;
        	int chestV = 1;
        	int chestW = 9;
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
            	{1,1,7, 2, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
            	{9,1,7, 2, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
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
    			{5,1,3, 2, 1, 1}, 
    			{5,1,13, 0, 1, 0}, 
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
            	{2,1,1, 1}, {8,1,1, 1}, 
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
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{2,1,7, -1, 0}, {8,1,7, -1, 0}, 
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
    
    
    // --- Medium House 4 --- //
    // designed by jss2a98aj
    
    public static class JungleMediumHouse4 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"  FFFFF  ",
    			" FFFFFFF ",
    			" FFFFFFF ",
    			" FFFFFFF ",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFPPPFFF",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1+4; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleMediumHouse4() {}

        public JungleMediumHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleMediumHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleMediumHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,1,2, 8,1,6}, 
    			// Back porch
    			{1,0,7, 1,0,9}, {2,0,9, 2,0,10}, {4,0,7, 4,0,7}, {6,0,9, 6,0,10}, {7,0,7, 7,0,9}, 
    			{3,0,8, 5,0,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{3,1,1, 1}, {4,1,1, 3}, {5,1,1, 0}, 
    			{4,1,7, 2}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Grass - not biome swapped
    		for(int[] uuvvww : new int[][]{
    			{1,1,8, 1,1,8}, {7,1,8, 7,1,8}, 
    			{1,1,7, 2,1,7}, {6,1,7, 7,1,7}, 
    			{1,1,1, 2,1,1}, {6,1,1, 7,1,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.GRASS.getDefaultState(), Blocks.GRASS.getDefaultState(), false);
    		}
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Front garden
    			{0,1,0, 0,1,0}, {2,1,0, 2,1,0}, {6,1,0, 6,1,0}, {8,1,0, 8,1,0}, 
    			// House frames
    			{0,2,6, 0,4,6}, {3,2,6, 3,4,6}, {5,2,6, 5,4,6}, {8,2,6, 8,4,6}, 
    			{0,2,2, 0,4,2}, {3,2,2, 3,4,2}, {5,2,2, 5,4,2}, {8,2,2, 8,4,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			{3,5,2, 3,5,6}, {5,5,2, 5,5,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Walls
    			{1,4,6, 2,4,6}, {6,4,6, 7,4,6}, {1,2,6, 2,2,6}, {6,2,6, 7,2,6}, 
    			{1,4,2, 2,4,2}, {6,4,2, 7,4,2}, {1,2,2, 2,2,2}, {6,2,2, 7,2,2},
    			{0,2,3, 0,4,5}, {8,2,3, 8,4,5}, 
    			// Roof
    			{2,5,2, 2,5,6}, {6,5,2, 6,5,6}, 
    			// Above doorways
    			{4,4,6, 4,4,6}, 
    			{4,4,2, 4,4,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
            
            
            // Glass blocks
        	for (int[] uvw : new int[][]{
        		{1,3,2}, {2,3,2}, {6,3,2}, {7,3,2}, 
        		{1,3,6}, {2,3,6}, {6,3,6}, {7,3,6}, 
        		})
            {
    			this.setBlockState(world, Blocks.GLASS.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,5,2, 0,5,6}, {3,6,2, 5,6,6}, {8,5,2, 8,5,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Under front fence
    			{0,1,1, 0+4}, {1,1,0, 3+4}, {7,1,0, 3+4}, {8,1,1, 1+4}, 
    			// Roof
    			{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, {1,5,5, 0}, {1,5,6, 0}, 
    			{7,5,2, 1}, {7,5,3, 1}, {7,5,4, 1}, {7,5,5, 1}, {7,5,6, 1},
    			// Table
    			{7,2,3, 2+4}, {7,2,5, 3+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
    			{0,2,0, 0,2,1}, {1,2,0, 2,2,0}, {6,2,0, 7,2,0}, {8,2,0, 8,2,1}, 
    			{4,5,1, 4,5,2}, {4,5,4, 4,5,4}, {4,5,6, 4,5,7}, 
    			// Back
    			{1,1,7, 1,1,9}, {2,1,9, 2,1,10}, {3,1,10, 3,1,10}, 
    			{7,1,7, 7,1,9}, {6,1,9, 6,1,10}, {5,1,10, 5,1,10}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Fence Gate (Across)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{4,1,10}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(2, false)), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{4,4,1}, {4,4,4}, {4,4,7}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CRAFTING_TABLE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{7,2,4}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Lower
        		{4,2,5, 4,2,5, (GeneralConfig.useVillageColors ? this.townColor5 : 1)}, // Orange
        		{3,2,4, 5,2,4, (GeneralConfig.useVillageColors ? this.townColor5 : 1)}, // Orange
        		{4,2,3, 4,2,3, (GeneralConfig.useVillageColors ? this.townColor5 : 1)}, // Orange
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.CARPET.getStateFromMeta(uuvvww[6]), Blocks.CARPET.getStateFromMeta(uuvvww[6]), false);
            }
            
            
            // Trapdoor (Bottom Horizontal)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{6,2,4, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,5, 1, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
            	{2,2,3, 1, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
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
    			{4,2,2, 2, 1, 0}, 
    			{4,2,6, 0, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
            // Poppies
            for (int[] uvw : new int[][]{
            	{2,1,8}, {3,1,7}, {5,1,7}, {6,1,8}, 
        		})
            {
        		this.setBlockState(world, Blocks.RED_FLOWER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
            // Peony
            for (int[] uvw : new int[][]{
        		{1,2,1}, {2,2,1}, {6,2,1}, {7,2,1}, 
        		})
            {
        		this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(5), uvw[0], uvw[1], uvw[2], structureBB);
        		this.setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvw[0], uvw[1]+1, uvw[2], structureBB);
            }
            
            
            // Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{2,1,7}, {6,1,7}, 
        		})
            {
        		this.setBlockState(world, biomeLeafState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{-1,4,4, 3}, {-1,4,5, 3}, {-1,3,5, 3}, {-1,3,6, 3}, {-1,2,6, 3}, {-1,1,6, 3}, 
        			// Rightward
        			{9,4,2, 1}, {9,3,2, 1}, {9,2,2, 1}, {9,1,2, 1}, {9,4,3, 1}, {9,3,3, 1}, {9,4,5, 1}, {9,4,6, 1}, {9,3,6, 1}, 
        			// Forward
        			{5,2,7, 0}, {5,3,7, 0}, {5,4,7, 0}, {8,4,7, 0}, 
        			// Backward
        			{1,4,1, 2}, {2,4,1, 2}, {2,5,1, 2}, {3,5,1, 2}, {8,3,1, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villagers
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,2,5, -1, 0}, 
	        			{3,2,3, -1, 0}, 
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
            	
                
            	// Painting
                for(int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
                	{1,3,4, 1}, 
                	})
                {
                	int x = this.getXWithOffset(uvwo[0], uvwo[2]);
                	int y = this.getYWithOffset(uvwo[1]);
                	int z = this.getZWithOffset(uvwo[0], uvwo[2]);
                	EntityPainting painting = new EntityPainting(world, new BlockPos(x, y, z), EnumFacing.getHorizontal(StructureVillageVN.chooseHangingMeta(uvwo[3], this.getCoordBaseMode())));
                	
                	if (painting.onValidSurface())
                	{
                		// Set art
                		EntityPainting.EnumArt[] a_1x1_paintings = new EntityPainting.EnumArt[]{
                    			EntityPainting.EnumArt.KEBAB,
                    			EntityPainting.EnumArt.AZTEC,
                    			EntityPainting.EnumArt.ALBAN,
                    			EntityPainting.EnumArt.AZTEC_2,
                    			EntityPainting.EnumArt.BOMB,
                    			EntityPainting.EnumArt.PLANT,
                    			EntityPainting.EnumArt.WASTELAND,
                    	};
                    	painting.art = a_1x1_paintings[random.nextInt(a_1x1_paintings.length)];
                    	
                    	world.spawnEntityInWorld(painting);
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
    // designed by AstroTibs
    
    public static class JungleShepherdHouse extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"  FFFFFF ",
    			" FFFFFFF ",
    			"FFFFFFFF ",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"   FFFFFF",
    			"      P  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 4;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public JungleShepherdHouse() {}

    	public JungleShepherdHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
    	}
    	
    	public static JungleShepherdHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new JungleShepherdHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,1,6, 7,1,6}, 
    			{3,1,2, 3,1,3}, {3,1,5, 3,1,5}, {8,1,2, 8,1,5}, 
    			{4,1,1, 5,1,1}, {7,1,1, 7,1,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{3,0,2, 3,0,5}, {4,0,1, 7,0,6}, {8,0,2, 8,0,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Wood stairs 1
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Table
    			{4,1,2, 1+4}, {4,1,3, 3+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Interior
        		{4,1,5, 4,4,5}, {7,4,5, 7,4,5}, 
        		{4,4,2, 4,4,2}, {7,4,2, 7,4,2}, 
        		// Exterior
        		{3,5,6, 3,5,6}, {8,5,6, 8,5,6}, 
        		{3,5,1, 3,5,1}, {8,5,1, 8,5,1}, 
        		{3,3,0, 3,3,0}, {8,3,0, 8,3,0}, 
        		// Sheep pen
        		{3,1,9, 7,1,9}, {7,1,7, 7,1,8}, 
        		{2,1,8, 2,1,9}, 
        		{1,1,7, 1,1,8}, 
        		{0,1,3, 0,1,7}, 
        		{0,1,2, 2,1,2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
    		
    		// Unkempt Grass
    		for (int[] uvwg : new int[][]{
    			{1,1,3, 0}, {2,1,3, 0}, 
    			{1,1,4, 0}, {2,1,4, 0}, 
    			{1,1,5, 0}, {2,1,5, 0}, 
    			{1,1,6, 0}, {2,1,6, 0}, 
    			{2,1,7, 0}, {3,1,7, 0}, {4,1,7, 0}, {5,1,7, 0}, {6,1,7, 0}, 
    			{3,1,8, 0}, {4,1,8, 0}, {5,1,8, 0}, {6,1,8, 0}, 
    			})
    		{
    			if (uvwg[3] == 0) // Tall grass
    			{
    				setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else if (uvwg[3] == 1) // Double-tall grass
    			{
    				setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1] + 1, uvwg[2], structureBB);
    			}
    			else if (uvwg[3] == 2) // Fern
    			{
    				setBlockState(world, Blocks.TALLGRASS.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall fern
    			{
    				setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(3), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				setBlockState(world, Blocks.DOUBLE_PLANT.getStateFromMeta(11), uvwg[0], uvwg[1] + 1, uvwg[2], structureBB);
    			} 
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Inside
    			{4,2,3, -1}, 
    			// Sheep pen
    			{0,2,2, -1}, 
    			{0,2,7, -1}, {2,2,9, -1}, {7,2,9, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			// Shed
    			{4,4,6, 7,4,6}, 
    			{4,4,1, 7,4,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
    		}
    		
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			// Longer log
    			{3,4,2, 3,4,5}, {8,4,2, 8,4,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
    		}
    		
    		
    		// For stripped logs specifically
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
    		for (int[] uuvvww : new int[][]{
    			{3,0,6, 3,4,6}, {8,0,6, 8,4,6}, 
    			{3,0,1, 3,4,1}, {8,0,1, 8,4,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
    		
    		// Wood stairs 2
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Front face
    			{4,3,1, 3+4}, {5,3,1, 3}, {6,3,1, 3+4}, {7,3,1, 3}, 
    			{4,2,1, 3+4}, {5,2,1, 3}, {7,2,1, 3}, 
    			// Back face
    			{4,3,6, 2}, {5,3,6, 2+4}, {6,3,6, 2}, {7,3,6, 2+4}, 
    			{4,2,6, 2}, {5,2,6, 2+4}, {6,2,6, 2}, {7,2,6, 2+4}, 
    			// Left wall
    			{3,3,2, 0}, {3,3,3, 0+4}, {3,3,4, 0}, {3,3,5, 0+4}, 
    			{3,2,2, 0}, {3,2,3, 0+4}, {3,2,5, 0+4}, 
    			// Right wall
    			{8,3,2, 1+4}, {8,3,3, 1}, {8,3,4, 1+4}, {8,3,5, 1}, 
    			{8,2,2, 1+4}, {8,2,3, 1}, {8,2,4, 1+4}, {8,2,5, 1}, 
    			// Roof
    			{4,5,6, 1+4}, {7,5,6, 0+4}, 
    			{3,5,5, 3+4}, {8,5,5, 3+4}, 
    			{3,5,2, 2+4}, {8,5,2, 2+4}, 
    			{4,5,1, 1+4}, {7,5,1, 0+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{5,5,6, 6,5,6}, 
    			{3,5,3, 3,5,4}, {8,5,3, 8,5,4}, 
    			{5,5,1, 6,5,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{4,6,2, 7,6,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{6,1,1, 0, 1, 0}, 
    			{3,1,4, 1, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{3,2,0}, {8,2,0}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		// Lower
        		{5,1,3, (GeneralConfig.useVillageColors ? this.townColor3 : 14)}, // Red
        		{6,1,3, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White
        		{6,1,4, (GeneralConfig.useVillageColors ? this.townColor3 : 14)}, // Red
        		{5,1,4, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
            // Loom
        	IBlockState loomState = ModObjects.chooseModLoom();
            for(int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Back Shutters
            	{7,1,5, 2}, 
            	})
            {
            	this.setBlockState(world, loomState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 2;
        	int chestW = 2;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(Reference.VN_SHEPHERD);
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{2,4,1, 3}, {2,3,1, 3}, 
        			// Rightward
        			{9,4,3, 1}, {9,3,3, 1}, {9,2,3, 1}, {9,1,3, 1}, {9,0,3, 1}, 
        			{9,4,4, 1}, {9,3,4, 1}, 
        			// Forward
        			{4,4,7, 0}, {5,4,7, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Entities
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int u = 7;
    			int v = 1;
    			int w = 5;
    			
    			while (u==7 && w==5)
    			{
    				u = 5+random.nextInt(3);
    				w = 2+random.nextInt(4);
    			}
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 3, 0); // Shepherd
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
    			world.spawnEntityInWorld(entityvillager);
    			
            	if (VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1)
            	{
	                // Sheep in the yard
	            	for (int[] uvw : new int[][]{
	        			{5, 1, 7},
	        			})
	        		{
	            		EntityLiving animal = new EntitySheep(world);
	            		IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
	            		            		
	                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(animal);
	                    
	                    // Dirt block underneath
	                    //this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
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
    
    
    // --- Small House 1 --- //
    // designed by AstroTibs
    
    public static class JungleSmallHouse1 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"            ",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF",
    			"  FFFFFFFFFF",
    			"  FFFFFFFFFF",
    			"  FFF  FFFFF",
    			"  FFF  FFFFF",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSmallHouse1() {}

        public JungleSmallHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSmallHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSmallHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
            // Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Side porch
        		{0,0,4, 1,0,8}, 
        		{2,0,1, 2,0,8}, 
        		// Front porch
        		{4,0,1, 4,0,1}, 
        		{3,0,2, 8,0,4}, 
        		{7,0,0, 8,0,1}, 
        		{9,0,1, 9,0,2}, 
        		{10,0,0, 11,0,2}, 
        		// Front wall
        		{2,1,4, 2,6,4}, 
        		{3,3,4, 3,3,4}, {3,6,4, 3,6,4}, 
        		{4,1,4, 4,6,4}, 
        		{5,1,4, 6,1,4}, {5,3,4, 6,4,4}, 
        		{7,1,4, 8,6,4}, 
        		{8,1,2, 8,3,3}, {8,4,2, 8,5,2}, 
        		{10,1,2, 11,3,2}, {10,4,2, 10,5,2}, 
        		{9,3,2, 9,3,2}, 
        		{8,6,2, 10,6,2}, 
        		// Left wall
        		{2,1,5, 2,6,5}, {2,3,6, 2,6,7}, {2,1,6, 2,6,8}, 
        		// Right wall
        		{11,0,3, 11,3,3}, 
        		{11,0,4, 11,1,4}, {11,3,4, 11,3,4}, 
        		{11,0,5, 11,3,5}, 
        		{11,0,6, 11,1,6}, {11,3,6, 11,3,6}, 
        		{11,0,7, 11,3,8}, 
        		// Upstairs right wall
        		{10,4,4, 10,4,4}, {10,4,6, 10,4,6}, 
        		{10,4,3, 10,6,3}, 
        		{10,4,4, 10,4,4}, {10,5,4, 10,6,4}, 
        		{10,4,5, 10,6,5}, 
        		{10,4,6, 10,4,6}, {10,5,6, 10,6,6}, 
        		{10,4,7, 10,6,8}, 
        		// Back wall
        		{3,0,8, 3,1,8}, {3,3,8, 3,4,8}, {3,6,8, 3,6,8}, 
        		{4,0,8, 4,6,8}, 
        		{5,0,8, 6,1,8}, {5,3,8, 6,4,8}, {5,6,8, 6,6,8},
        		{7,0,8, 8,6,8}, 
        		{9,0,8, 9,1,8}, {9,3,8, 9,4,8}, {9,6,8, 9,6,8},
        		{10,0,8, 10,3,8}, 
        		// Upper floor balcony
        		// Front
        		{2,3,2, 7,3,3}, 
        		{7,3,0, 11,3,1}, 
        		})
            {
        		// Yellow
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 4),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 4), 
        				false);
            }
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{3,0,5, 10,0,7}, 
    			{9,0,3, 10,0,4}, 
    			// Roof
    			{4,8,6, 8,8,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Interior
    			// First floor
    			{4,2,7, 2}, {10,2,5, 3}, 
    			// Second floor
    			{3,6,6, 1}, {9,6,6, 3}, {9,6,3, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// First floor posts
        		{0,1,4, 0,2,4}, {0,1,8, 0,2,8}, 
        		{2,1,2, 2,2,2}, {4,1,2, 4,2,2}, {6,1,2, 6,2,2}, {7,1,0, 7,2,0}, {11,1,0, 11,2,0}, 
        		// Railing
        		{2,4,2, 2,4,3}, {3,4,2, 7,4,2}, {7,4,0, 7,4,1}, {8,4,0, 11,4,0}, {11,4,0, 11,4,8}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Left awning
    			{0,3,4, 3}, {1,3,4, 3}, 
    			{0,3,5, 0}, {0,3,6, 0}, {0,3,7, 0}, 
    			{0,3,8, 2}, {1,3,8, 2}, 
    			{1,4,5, 3}, {1,4,6, 0}, {1,4,7, 2}, 
    			// Roof
    			{1,6,3, 3}, {2,6,3, 3}, {3,6,3, 3}, {4,6,3, 3}, {5,6,3, 3}, {6,6,3, 3}, {7,6,3, 3}, 
    			{7,6,2, 0},
    			{7,6,1, 3}, {8,6,1, 3}, {9,6,1, 3}, {10,6,1, 3}, {11,6,1, 3}, 
    			{11,6,2, 1}, {11,6,2, 1}, {11,6,3, 1}, {11,6,4, 1}, {11,6,5, 1}, {11,6,6, 1}, {11,6,7, 1}, {11,6,8, 1}, 
    			{11,6,9, 2}, {10,6,9, 2}, {9,6,9, 2}, {8,6,9, 2}, {7,6,9, 2}, {6,6,9, 2}, {5,6,9, 2}, {4,6,9, 2}, {3,6,9, 2}, {2,6,9, 2}, {1,6,9, 2}, 
    			{1,6,8, 0}, {1,6,7, 0}, {1,6,6, 0}, {1,6,5, 0}, {1,6,4, 0}, {1,6,3, 0},
    			
    			{2,7,4, 3}, {3,7,4, 3}, {4,7,4, 3}, {5,7,4, 3}, {6,7,4, 3}, {7,7,4, 3}, {8,7,4, 3}, 
    			{8,7,3, 0}, 
    			{8,7,2, 3}, {9,7,2, 3}, {10,7,2, 3}, 
    			{10,7,3, 1}, {10,7,4, 1}, {10,7,5, 1}, {10,7,6, 1}, {10,7,7, 1}, 
    			{10,7,8, 2}, {9,7,8, 2}, {8,7,8, 2}, {7,7,8, 2}, {6,7,8, 2}, {5,7,8, 2}, {4,7,8, 2}, {3,7,8, 2}, {2,7,8, 2}, 
    			{2,7,7, 0}, {2,7,6, 0}, {2,7,5, 0}, 
    			
    			{3,8,5, 3}, {4,8,5, 3}, {5,8,5, 3}, {6,8,5, 3}, {7,8,5, 3}, {8,8,5, 3}, {9,8,5, 3}, 
    			{9,8,6, 1}, 
    			{9,8,7, 2}, {8,8,7, 2}, {7,8,7, 2}, {6,8,7, 2}, {5,8,7, 2}, {4,8,7, 2}, {3,8,7, 2}, 
    			{3,8,6, 0}, 
    			
    			// Stairs
    			{6,1,7, 0}, {7,2,7, 0}, {8,3,7, 0}, 
    			{7,1,7, 1+4}, {8,2,7, 1+4}, {9,3,7, 1+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Ceiling
    			{3,3,7, 4,3,7}, {3,3,5, 9,3,6}, {9,3,3, 9,3,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Ceiling
    			{9,8,3, 9,8,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{5,2,4}, {6,2,4}, 
        		{5,5,4}, {6,5,4}, 
        		// Back wall
        		{3,2,8}, {5,2,8}, {6,2,8}, {9,2,8}, 
        		{3,5,8}, {5,5,8}, {6,5,8}, {9,5,8}, 
        		// Right wall
        		{11,2,4}, {11,2,6}, {10,5,4}, {10,5,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{3,1,4, 0, 1, 0}, 
    			{2,1,6, 3, 1, 0}, 
    			{9,1,2, 0, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entryways
        		{3,0,1, 3,0,1, 3}, {9,0,1, 9,0,1, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);
            }
        	
        	
            // Table
            IBlockState[] tableComponentBlockstates = ModObjects.chooseModWoodenTable(biomePlankState.getBlock()==Blocks.PLANKS ? biomePlankState.getBlock().getMetaFromState(biomePlankState) : 0);
        	for (int[] uuvvww : new int[][]{
        		{10,1,7}, 
        		})
            {
        		for (int i=1; i>=0; i--)
        		{
        			this.setBlockState(world, tableComponentBlockstates[i], uuvvww[0], uuvvww[1]+1-i, uuvvww[2], structureBB);
        		}
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 4;
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
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,4,6, 2, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
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
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{4,4,5, -1, 0},
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
    // designed by AstroTibs
    
    public static class JungleSmallHouse2 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"          ",
    			" FFFFFFFF ",
    			" FFFFFFFF ",
    			" FFFFFFFF ",
    			" FFFFFFFF ",
    			" FPFFFFFF ",
    			" FPFFFFFF ",
    			" FPFFFFFF ",
    			" FPFFFFFF ",
    			" FPPPPPPP ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 3;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSmallHouse2() {}

        public JungleSmallHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSmallHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSmallHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Dirt
    		for (int[] uuvvww : new int[][]{
        		{1,0,5, 3,0,8}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeFillerState,
        				biomeFillerState, 
        				false);
            }
    		
    		
            // White Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Front wall
        		{5,2,2, 5,4,2}, {6,4,2, 6,4,2}, {7,2,2, 7,4,2},
        		// Back wall
        		{5,2,8, 5,4,8}, {6,2,8, 6,2,8}, {6,4,8, 6,4,8}, {7,2,8, 7,4,8}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0), 
        				false);
            }
    		
    		
            // Yellow Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Floor
        		{4,1,2, 8,1,8}, 
        		// Front wall
        		{5,6,2, 5,6,2}, {6,7,2, 6,7,2}, {7,6,2, 7,6,2}, 
        		// Left wall
        		{4,2,2, 4,2,8}, 
        		{4,3,2, 4,3,2}, {4,3,4, 4,3,8}, 
        		{4,4,2, 4,4,8}, {4,5,3, 4,5,7}, 
        		// Right wall
        		{8,2,2, 8,2,8}, 
        		{8,3,2, 8,3,2}, {8,3,4, 8,3,6}, {8,3,8, 8,3,8}, 
        		{8,4,2, 8,4,8}, {8,5,3, 8,5,7}, 
        		// Back wall
        		{5,6,8, 5,6,8}, {6,7,8, 6,7,8}, {7,6,8, 7,6,8}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 4),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 4), 
        				false);
            }
    		
    		
            // Orange Terracotta
        	for (int[] uvw : new int[][]{
        		// Desk
        		{7,2,3, 7,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor5 : 1), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Logs (Across)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			// Shed
    			{1,3,5, 3,3,5}, 
    			{1,3,8, 3,3,8}, 
    			// Main house
    			{4,5,2, 8,5,2}, 
    			{4,5,8, 8,5,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Shed interior
    			{2,3,6, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			// Longer log
    			{1,3,6, 1,3,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Front wall
    			{3,1,5, 3,2,5}, {3,4,5, 3,4,5}, 
    			// Side wall
    			{1,1,5, 1,2,8}, 
    			// Back wall
    			{2,1,8, 3,2,8}, {3,4,8, 3,4,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uvwo : new int[][]{
        		{5,5,5}, 
        		})
            {
    			this.setBlockState(world, biomeFenceState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{5,4,5}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Front entrance
    			{5,1,1, 3}, {6,1,1, 3}, {7,1,1, 3}, 
    			// Roof
    			{3,5,1, 0}, {3,5,2, 0}, {3,5,3, 0}, {3,5,4, 0}, {3,5,5, 0}, {3,5,6, 0}, {3,5,7, 0}, {3,5,8, 0}, {3,5,9, 0}, 
    			{4,6,1, 0}, {4,6,2, 0}, {4,6,3, 0}, {4,6,4, 0}, {4,6,5, 0}, {4,6,6, 0}, {4,6,7, 0}, {4,6,8, 0}, {4,6,9, 0}, 
    			{5,7,1, 0}, {5,7,2, 0}, {5,7,3, 0}, {5,7,4, 0}, {5,7,5, 0}, {5,7,6, 0}, {5,7,7, 0}, {5,7,8, 0}, {5,7,9, 0}, 
    			{7,7,1, 1}, {7,7,2, 1}, {7,7,3, 1}, {7,7,4, 1}, {7,7,5, 1}, {7,7,6, 1}, {7,7,7, 1}, {7,7,8, 1}, {7,7,9, 1}, 
    			{8,6,1, 1}, {8,6,2, 1}, {8,6,3, 1}, {8,6,4, 1}, {8,6,5, 1}, {8,6,6, 1}, {8,6,7, 1}, {8,6,8, 1}, {8,6,9, 1}, 
    			{9,5,1, 1}, {9,5,2, 1}, {9,5,3, 1}, {9,5,4, 1}, {9,5,5, 1}, {9,5,6, 1}, {9,5,7, 1}, {9,5,8, 1}, {9,5,9, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Shed roof
    			{0,3,5, 0,3,8}, 
    			{2,4,5, 2,4,8}, 
    			// Shelves
    			{3,1,6, 3,1,6}, {3,2,7, 3,2,7}, {3,3,6, 3,3,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Shed roof
    			{1,4,5, 1,4,8}, 
    			// House roof
    			{6,8,1, 6,8,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Front wall
        		{6,6,2}, 
        		// Left wall
        		{4,3,3}, 
        		// Back wall
        		{6,3,8}, {6,6,8}, 
        		// Left wall
        		{8,3,3}, {8,3,7}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Lower
        		{5,2,4, 7,2,5, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.CARPET.getStateFromMeta(uuvvww[6]), Blocks.CARPET.getStateFromMeta(uuvvww[6]), false);
            }
        	
            
            // Potted Ferns
        	for (int[] uvwm : new int[][]{ // 11:fern
        		{4,1,1, 11}, {8,1,1, 11}, 
           		})
        	{
        		TileEntity flowerPot = (new BlockFlowerPot()).createNewTileEntity(world, uvwm[3]);
        		BlockPos flowerPotPos = new BlockPos(this.getXWithOffset(uvwm[0], uvwm[2]), this.getYWithOffset(uvwm[1]), this.getZWithOffset(uvwm[0], uvwm[2]));
        		world.setBlockState(flowerPotPos, Blocks.FLOWER_POT.getDefaultState());
        		world.setTileEntity(flowerPotPos, flowerPot);
            }
        	
            
            // Potted random flower
            for (int[] uvw : new int[][]{
        		{7,3,3}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			// Shed door
    			{2,1,5, 2, 1, 0}, 
    			// House door
    			{6,2,2, 2, 1, 0}, 
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
            int chestU = 3;
        	int chestV = 1;
        	int chestW = 7;
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
            	{6,2,6, 2, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
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
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{5,2,5, -1, 0},
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
    // designed by AstroTibs
    
    public static class JungleSmallHouse3 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"       ",
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			" FPPPF ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSmallHouse3() {}

        public JungleSmallHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSmallHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSmallHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,6,4, 4,6,4}, 
    			{1,0,1, 5,0,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	// Chimney
        		{4,2,4, 4,5,4}, {4,7,4, 4,7,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uvwo : new int[][]{
        		// Windows
        		{1, 2, 3}, {5, 2, 3}, 
        		{3, 5, 1}, 
        		{3, 5, 5}, {3, 2, 5}, 
        		})
            {
    			this.setBlockState(world, biomeFenceState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,1,5, 1,4,5}, {5,1,5, 5,4,5}, 
        		{1,1,1, 1,4,1}, {5,1,1, 5,4,1}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Back wall
    			{2,1,5, 2,3,5}, {3,1,5, 3,1,5}, {3,3,5, 3,3,5}, {4,1,5, 4,3,5}, 
    			{2,5,5, 2,5,5}, {4,5,5, 4,5,5}, 
    			// Left wall
    			{1,1,2, 1,3,2}, {1,1,3, 1,1,3}, {1,3,3, 1,3,3}, {1,1,4, 1,3,4}, 
    			// Right wall
    			{5,1,2, 5,3,2}, {5,1,3, 5,1,3}, {5,3,3, 5,3,3}, {5,1,4, 5,3,4}, 
    			// Front wall
    			{2,1,1, 2,3,1}, {3,1,1, 3,1,1}, {3,3,1, 3,3,1}, {4,1,1, 4,3,1}, 
    			// Ceiling
    			{3,5,1, 3,5,1}, {3,5,5, 3,5,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,3,3, 1}, {4,3,3, 3}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{1,5,0, 0}, {1,5,1, 0}, {1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, {1,5,5, 0}, {1,5,6, 0}, 
    			{2,6,0, 0}, {2,6,1, 0}, {2,6,2, 0}, {2,6,3, 0}, {2,6,4, 0}, {2,6,5, 0}, {2,6,6, 0}, 
    			{4,6,0, 1}, {4,6,1, 1}, {4,6,2, 1}, {4,6,3, 1}, {4,6,5, 1}, {4,6,6, 1}, 
    			{5,5,0, 1}, {5,5,1, 1}, {5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, {5,5,5, 1}, {5,5,6, 1}, 
    			// Roof trim
    			{2,5,0, 1+4}, {3,6,0, 3+4}, {4,5,0, 0+4}, 
    			{2,5,6, 1+4}, {3,6,6, 2+4}, {4,5,6, 0+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{3,7,0, 3,7,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,4,0, 0,4,6}, {6,4,0, 6,4,6}, 
    			// Wall trim
    			{1,4,2, 1,4,4}, {2,4,1, 4,4,1}, {2,4,5, 4,4,5}, {5,4,2, 5,4,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
        	
            
            // Potted random flower
        	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
    		int randomPottedPlant = random.nextInt(10)-1;
            for (int[] uvw : new int[][]{
        		{1,1,0}, {5,1,0}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
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
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,4, 2}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
            
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
    // designed by AstroTibs
    
    public static class JungleSmallHouse4 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"        ",
    			" FFFFFF ",
    			" FFFFFFF",
    			" FFFFFFF",
    			" FFFFFFF",
    			" FFFFFF ",
    			"F PPP F ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSmallHouse4() {}

        public JungleSmallHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSmallHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSmallHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,0,1, 5,0,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{0,1,0, 0,1,0}, {6,1,0, 6,1,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
    		
    		
            // Cyan Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Front wall
        		{1,1,1, 1,3,2}, {2,1,1, 2,1,1}, {2,3,1, 2,3,1}, {3,1,1, 3,3,1}, {4,3,1, 4,3,1}, 
        		// Right wall
        		{5,1,1, 5,3,5}, 
        		// Left wall
        		{1,1,3, 1,1,3}, {1,3,3, 1,3,3}, {1,1,4, 1,3,4}, 
        		// Back wall
        		{1,1,5, 2,3,5}, {3,1,5, 3,2,5}, {4,1,5, 5,3,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor4 : 9)),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor4 : 9)), 
        				false);
            }
    		
    		
    		// Hay bales (vertical)
    		IBlockState biomeHayBaleVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.HAY_BLOCK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,5,1, 5,5,1}, {1,5,2, 1,5,4}, {5,5,2, 5,5,4}, {1,5,5, 5,5,5}, 
    			{2,6,2, 4,6,2}, {2,6,3, 2,6,3}, {4,6,3, 4,6,3}, {2,6,4, 4,6,4}, 
    			{0,4,0, 6,4,0}, {0,4,1, 0,4,5}, {6,4,1, 6,4,5}, {0,4,6, 6,4,6}, 
    			{3,7,3, 3,7,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeHayBaleVertState, biomeHayBaleVertState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uvwo : new int[][]{
        		// Windows
        		{2, 2, 1}, 
        		{1, 2, 3}, 
        		{3, 3, 5}, 
        		// Ceiling support
        		{2, 5, 4}, {3, 5, 4}, {4, 5, 4}, 
        		{2, 5, 3}, {3, 6, 3}, {4, 5, 3}, 
        		{2, 5, 2}, {3, 5, 2}, {4, 5, 2}, 
        		})
            {
    			this.setBlockState(world, biomeFenceState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{0,2,0, -1}, {6,2,0, -1}, 
        		{4,3,3, 3}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Upper wall
    			{1,4,1, 2}, {2,4,1, 2}, {3,4,1, 2}, {4,4,1, 2}, {5,4,1, 2}, 
    			{1,4,2, 1}, {1,4,3, 1}, {1,4,4, 1}, 
    			{5,4,2, 0}, {5,4,3, 0}, {5,4,4, 0}, 
    			{1,4,5, 3}, {2,4,5, 3}, {3,4,5, 3}, {4,4,5, 3}, {5,4,5, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Carpet in front of the door prevents villagers from passing through
        		{3,1,3, 3,1,3, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White 
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.CARPET.getStateFromMeta(uuvvww[6]), Blocks.CARPET.getStateFromMeta(uuvvww[6]), false);
            }
        	
            
            // Potted random flower
            for (int[] uvw : new int[][]{
        		{3,1,4}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,1,1, 2, 1, 1}, 
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
            int chestU = 4;
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
            	{2,1,3, 0, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
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
    		
            
            // Moist Farmland with crop above
            for(int[] uvwmcp : new int[][]{
            	// u,v,w, farmland moisture (0:dry, 7:moist), crop progress
            	{6,0,1, 7,0}, 
            	{7,0,2, 7,0}, {7,0,3, 7,0}, {7,0,4, 7,0}, 
            	{6,0,5, 7,0}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvwmcp[0], uvwmcp[1]-1, uvwmcp[2], structureBB);
            	//this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvwmcp[4]), uvwmcp[0], uvwmcp[1]+1, uvwmcp[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwmcp[3]), uvwmcp[0], uvwmcp[1], uvwmcp[2], structureBB); // 7 is moist
            }
            
            
    		// Water
    		for(int[] uuvvww : new int[][]{ 
    			{6,0,2, 6,0,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
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
    
    
    // --- Small House 5 --- //
    // designed by AstroTibs
    
    public static class JungleSmallHouse5 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"  F F  ",
    			" FFFFF ",
    			"FFFFFFF",
    			" FFFFF ",
    			"FFFFFFF",
    			" FFFFF ",
    			" FFPFF ",
    			" FFPFF ",
    			"  PPP  ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSmallHouse5() {}

        public JungleSmallHouse5(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSmallHouse5 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSmallHouse5(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
            // Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Entryway
    			{3,0,3, 3,0,3}, 
    			{2,0,3, 2,2,3}, {4,0,3, 4,2,3}, 
    			{2,0,2, 2,1,2}, {4,0,2, 4,1,2}, 
    			{2,0,1, 2,2,1}, {4,0,1, 4,2,1}, 
    			{2,3,1, 2,3,3}, {3,3,1, 3,3,1}, {3,3,3, 3,3,3}, {4,3,1, 4,3,3}, 
    			// Left wall
    			{1,0,6, 1,3,6}, {5,0,6, 5,3,6}, 
    			{1,0,5, 1,1,5}, {5,0,5, 5,1,5}, {1,3,5, 1,3,5}, {5,3,5, 5,3,5}, 
    			{1,0,4, 1,3,4}, {5,0,4, 5,3,4}, 
    			// Back wall
    			{1,0,7, 5,3,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
    		}
    		
    		
    		// Stone Brick Slab (upper)
    		IBlockState biomeStoneBrickSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(5+8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,3,2, 3,3,2}
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickSlabUpperState, biomeStoneBrickSlabUpperState, false);
    		}
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{2,4,3, 3}, {3,4,3, 3}, {4,4,3, 3}, 
    			{1,4,4, 0}, {1,4,5, 0}, {1,4,6, 0}, 
    			{2,4,7, 2}, {3,4,7, 2}, {4,4,7, 2}, 
    			{5,4,4, 1}, {5,4,5, 1}, {5,4,6, 1}, 
    			// Top of roof
    			{2,5,6, 2}, {3,5,6, 2}, {4,5,6, 2}, 
    			{2,5,5, 0}, {4,5,5, 1}, 
    			{2,5,4, 3}, {3,5,4, 3}, {4,5,4, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,0,4, 4,0,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
        	
        	// Stone Brick wall
    		IBlockState biomeStoneBrickWallStone = ModObjects.chooseModStoneBrickWallState();
        	biomeStoneBrickWallStone = StructureVillageVN.getBiomeSpecificBlockState(biomeStoneBrickWallStone!=null?biomeStoneBrickWallStone:Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{3,4,6, 3,4,6}, //{3,5,5, 3,5,5}, 
            	{2,1,8, 2,3,8}, {4,1,8, 4,3,8}, 
            	{0,1,6, 0,3,6}, {6,1,6, 6,3,6}, 
            	{0,1,4, 0,3,4}, {6,1,4, 6,3,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickWallStone, biomeStoneBrickWallStone, false);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,2,5}, {5,2,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	{3,5,5, 3,5,5}
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            }
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Lower
        		{3,1,5, 3,1,5, (GeneralConfig.useVillageColors ? this.townColor4 : 9)}, // Cyan
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.CARPET.getStateFromMeta(uuvvww[6]), Blocks.CARPET.getStateFromMeta(uuvvww[6]), false);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,1,6, 1}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
        	
            
            // Potted random flower
            for (int[] uvw : new int[][]{
        		{2,2,6}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{3,1,3, 0, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{3,3,6}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,5, 2, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
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
    // designed by jss2a98aj
    
    public static class JungleSmallHouse6 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			" FFFF ",
    			"FFFFFF",
    			"FFFFFF",
    			"FFFFFF",
    			"FFFFFF",
    			" FFFFF",
    			" PPP  ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSmallHouse6() {}

        public JungleSmallHouse6(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSmallHouse6 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSmallHouse6(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
        		{1,0,6, 4,0,6}, 
        		{0,0,3, 5,0,5}, 
        		{0,0,2, 4,0,2}, 
        		{1,0,1, 3,0,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
            // Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Front wall
    			{1,1,1, 1,2,1}, {2,3,1, 2,3,1}, {3,1,1, 3,2,1}, 
    			// Left wall
    			{0,1,2, 0,2,2}, {0,1,3, 0,1,4}, {0,3,3, 0,3,4}, {0,1,5, 0,2,5}, 
    			// Back wall
    			{1,1,6, 1,2,6}, {2,1,6, 3,1,6}, {2,3,6, 3,3,6}, {4,1,6, 4,2,6}, 
    			// Right wall
    			{5,1,3, 5,2,5}, {5,3,4, 5,3,4}, {4,1,2, 4,1,2}, {4,3,2, 4,3,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
    		}
    		
    		
    		// Chiseled Stone Brick
    		IBlockState biomeChiseledStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			// Front wall
    			{1,3,1}, {3,3,1}, 
    			// Left wall
    			{0,3,2}, {0,3,5}, 
    			// Back wall
    			{1,3,6}, {4,3,6}, 
    			// Right wall
    			{5,3,5}, {5,3,3}, 
    			})
    		{
    			this.setBlockState(world, biomeChiseledStoneBrickState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
            // Glass blocks
        	for (int[] uvw : new int[][]{
        		{0,2,3}, {0,2,4}, 
        		{2,2,6}, {3,2,6}, 
        		{4,2,2}, 
        		})
            {
    			this.setBlockState(world, Blocks.GLASS.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,4, 3}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
    		
    		
    		// Brick Slab (upper)
    		IBlockState biomeBrickSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(4 + 8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvwo : new int[][]{
    			{4,1,3}, {4,1,5}, 
    			})
    		{
    			this.setBlockState(world, biomeBrickSlabUpperState, uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Web
    		for(int[] uvwo : new int[][]{
    			{4,4,4}, 
    			})
    		{
    			this.setBlockState(world, Blocks.WEB.getDefaultState(), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
        	
            
            // Potted flower
            for (int[] uvwf : new int[][]{
        		{4,2,5, 8}, // 8 is a brown mushroom
        		})
            {
        		TileEntity flowerPot = (new BlockFlowerPot()).createNewTileEntity(world, uvwf[3]);
        		BlockPos flowerPotPos = new BlockPos(this.getXWithOffset(uvwf[0], uvwf[2]), this.getYWithOffset(uvwf[1]), this.getZWithOffset(uvwf[0], uvwf[2]));
        		world.setBlockState(flowerPotPos, Blocks.FLOWER_POT.getDefaultState());
        		world.setTileEntity(flowerPotPos, flowerPot);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{2,4,1, 3}, 
    			{0,4,3, 0}, {0,4,4, 0}, 
    			{2,4,6, 2}, {3,4,6, 2}, 
    			{5,4,4, 1}, 
    			// Shelves
    			{4,3,3, 2+4}, {4,3,5, 3+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,4,1, 1,4,1}, {3,4,1, 3,4,1}, 
    			{0,4,2, 0,4,2}, {4,4,2, 4,4,2}, 
    			{5,4,3, 5,4,3}, 
    			{0,4,5, 0,4,5}, {5,4,5, 5,4,5}, 
    			{1,4,6, 1,4,6}, {4,4,6, 4,4,6}, 
    			{2,5,2, 2,5,2}, 
    			{1,5,3, 3,5,3}, 
    			{1,5,4, 4,5,4}, 
    			{2,5,5, 3,5,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,4,2, 1,4,2}, {3,4,2, 3,4,2}, 
    			{4,4,3, 4,4,3}, 
    			{1,4,5, 1,4,5}, {4,4,5, 4,4,5}, 
    			// Shelf
    			{4,3,4, 4,3,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{2,4,4}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// On fence posts
    			{1,2,0, 2}, {3,2,0, 2}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{2,3,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,1,4, 2, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
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
    			{2,1,1, 2, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
            
            // Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{4,1,1, 5,1,1}, {5,1,2, 5,1,2}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{-1,1,2, 3}, {-1,1,5, 3}, {-1,2,5, 3}, {-1,3,5, 3}, 
        			// Forward
        			{0,1,6, 0}, {0,2,6, 0}, {3,1,7, 0}, {4,1,7, 0}, {4,2,7, 0}, 
        			// Rightward
        			{6,2,3, 1}, 
        			{6,1,3, 1}, {6,1,4, 1}, 
        			{6,1,5, 1}, {6,2,5, 1}, {6,3,5, 1}, 
        			// Backward
        			{0,1,1, 2}, {0,2,1, 2}, {0,3,1, 2}, 
        			{3,1,0, 2}, {5,2,2, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
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
    // designed by AstroTibs
    
    public static class JungleSmallHouse7 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"  FFFF  ",
    			" FFFFFF ",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			" FFFFFF ",
    			"  FPPF  ",
    			"   PP   ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSmallHouse7() {}

        public JungleSmallHouse7(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSmallHouse7 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSmallHouse7(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
            // Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Front
    			{2,1,1, 2,1,1}, {2,1,2, 2,2,2}, 
    			{5,1,1, 5,1,1}, {5,1,2, 5,2,2}, 
    			// Back
    			{2,1,8, 2,1,8}, {2,1,7, 2,2,7}, 
    			{3,1,7, 4,1,7}, 
    			{5,1,8, 5,1,8}, {5,1,7, 5,2,7}, 
    			// Left
    			{0,1,3, 0,1,3}, {1,1,3, 1,2,3},
    			{1,1,4, 1,1,5}, 
    			{0,1,6, 0,1,6}, {1,1,6, 1,2,6}, 
    			// Right
    			{7,1,3, 7,1,3}, {6,1,3, 6,2,3}, 
    			{6,1,4, 6,1,5}, 
    			{7,1,6, 7,1,6}, {6,1,6, 6,2,6}, 
    			
    			// Floor
    			{2,1,6, 5,1,6}, 
    			{2,1,4, 2,1,5}, {5,1,4, 5,1,5}, 
    			{2,1,3, 5,1,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
    		}
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		
        		{3,1,5, 1, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{4,1,5, 2, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{3,1,4, 0, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
        		{4,1,4, 3, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // Red
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], StructureVillageVN.chooseGlazedTerracottaMeta(uvwoc[3], this.getCoordBaseMode()));
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
    		
        	
        	// Terracotta part 1
        	IBlockState biomeTerracottaState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.HARDENED_CLAY.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{2,2,6, 2,4,6}, {5,2,6, 5,4,6}, 
        		{2,2,3, 2,4,3}, {5,2,3, 5,4,3}, 
        		{3,4,2, 4,4,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTerracottaState, biomeTerracottaState, false);
            }
    		
        	
        	// Stone Brick wall
    		IBlockState biomeStoneBrickWallStone = ModObjects.chooseModStoneBrickWallState();
        	biomeStoneBrickWallStone = StructureVillageVN.getBiomeSpecificBlockState(biomeStoneBrickWallStone!=null?biomeStoneBrickWallStone:Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,1,7, 1,2,7}, {2,3,7, 2,4,7}, {5,3,7, 5,4,7}, {6,1,7, 6,2,7}, 
        		{1,3,6, 1,4,6}, {6,3,6, 6,4,6}, 
        		{1,3,3, 1,4,3}, {6,3,3, 6,4,3}, 
        		{1,1,2, 1,2,2}, {2,3,2, 2,4,2}, {5,3,2, 5,4,2}, {6,1,2, 6,2,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickWallStone, biomeStoneBrickWallStone, false);
            }
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Back
    			{3,1,8, 2}, {4,1,8, 2}, 
    			// Left
    			{0,1,4, 0}, {0,1,5, 0}, 
    			// Right
    			{7,1,4, 1}, {7,1,5, 1}, 
    			// Front
    			{3,1,2, 3}, {4,1,2, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Exterior
    			{1,3,7, -1}, {6,3,7, -1}, 
    			{1,3,2, -1}, {6,3,2, -1}, 
    			// Interior
    			{2,4,5, 2}, {5,4,4, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
        	
        	// Terracotta part 2
        	for (int[] uuvvww : new int[][]{
        		{3,2,7, 4,4,7}, 
        		{1,2,4, 1,4,5}, {6,2,4, 6,4,5}, 
        		{3,4,2, 4,4,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTerracottaState, biomeTerracottaState, false);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{2,5,7, 2}, {5,5,7, 2}, 
    			{1,5,6, 0}, {6,5,6, 1}, 
    			{1,5,3, 0}, {6,5,3, 1}, 
    			{2,5,2, 3}, {5,5,2, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{3,5,7, 4,5,7}, 
    			{1,5,4, 1,5,5}, {6,5,4, 6,5,5}, 
    			{3,5,2, 4,5,2}, 
    			
    			{2,6,6, 5,6,6}, 
    			{2,6,4, 2,6,5}, {5,6,4, 5,6,5}, 
    			{2,6,3, 5,6,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{2,5,6}, {5,5,6}, 
        		{2,5,3}, {5,5,3}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Table
    			{5,2,5, 5,2,5}, 
    			// Roof
    			{3,6,4, 4,6,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
        	
            
            // Potted random flower
            int flower_u=5; int flower_v=3; int flower_w=5;
            int x = this.getXWithOffset(flower_u, flower_w);
            int y = this.getYWithOffset(flower_v);
            int z = this.getZWithOffset(flower_u, flower_w);
            
            IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
    		int randomPottedPlant = random.nextInt(10)-1;
    		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
    		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
        	
        	
        	// Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CRAFTING_TABLE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{4,2,6}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,4, 2, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
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
    			{3,2,3, 2, 1, 1}, {4,2,3, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{0,2,3, 3}, 
        			{0,4,4, 3}, {0,3,4, 3}, {0,2,4, 3}, 
        			// Forward
        			// Rightward
        			{7,4,5, 1}, {7,3,5, 1}, 
        			// Backward
        			{3,4,8, 0}, {4,4,8, 0}, {4,3,8, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,2,4, -1, 0},
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
    // designed by AstroTibs
    
    public static class JungleSmallHouse8 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"       ",
    			"       ",
    			"       ",
    			"       ",
    			"       ",
    			"       ",
    			"  PF   ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSmallHouse8() {}

        public JungleSmallHouse8(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSmallHouse8 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSmallHouse8(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{1,1,5}, {1,2,5}, {5,1,5}, {5,2,5}, 
        		{1,1,1}, {1,2,1}, {5,1,1}, {5,2,1}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		// Foot foundation
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvw : new int[][]{
    			// Feet
        		{1,0,5}, {5,0,5}, 
        		{1,0,1}, {5,0,1}, 
    			})
    		{
    			this.replaceAirAndLiquidDownwards(world, biomeCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Stripped Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
    		for (int[] uuvvww : new int[][]{
    			{1,3,5, 1,8,5}, {5,3,5, 5,8,5}, 
    			{1,3,1, 1,8,1}, {5,3,1, 5,8,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Entry
    			{6,3,0, 6,3,2}, {5,3,2, 5,3,2}, 
    			// Walls
    			{2,3,5, 4,4,5}, {3,5,5, 3,5,5}, {2,6,5, 4,6,5}, 
    			{1,3,2, 1,4,4}, {1,5,3, 1,5,3}, {1,6,2, 1,6,4}, 
    			{5,3,3, 5,4,4}, {5,5,3, 5,5,3}, {5,6,2, 5,6,4}, 
    			{2,3,1, 4,4,1}, {3,5,1, 3,5,1}, {2,6,1, 4,6,1}, 
    			// Floor
    			{2,3,2, 4,3,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Exterior
    			{3,6,6, 0}, 
    			{0,6,3, 3}, {6,6,3, 1}, 
    			{3,6,0, 2}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Entry
    			{3,1,0, 0}, {4,2,0, 0}, {5,3,0, 0}, 
    			{4,1,0, 1+4}, {5,2,0, 1+4}, 
    			// Top of walls
    			{1,7,2, 2+4}, {1,7,4, 3+4}, 
    			{2,7,5, 1+4}, {4,7,5, 0+4}, 
    			{5,7,2, 2+4}, {5,7,4, 3+4}, 
    			{2,7,1, 1+4}, {4,7,1, 0+4}, 
    			// Lower roof
    			{1,8,2, 0}, {1,8,4, 0}, 
    			{2,8,5, 2}, {4,8,5, 2}, 
    			{5,8,2, 1}, {5,8,4, 1}, 
    			{2,8,1, 3}, {4,8,1, 3}, 
    			// Top-center roof
    			{2,9,4, 2}, {3,9,4, 2}, {4,9,4, 2}, 
    			{2,9,3, 0}, {4,9,3, 1}, 
    			{2,9,2, 3}, {3,9,2, 3}, {4,9,2, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,8,6, 6,8,6}, 
    			{0,8,1, 0,8,5}, {6,8,1, 6,8,5}, 
    			{0,8,0, 6,8,0}, 
    			{3,8,1, 3,8,1}, {3,8,5, 3,8,5}, {1,8,3, 1,8,3}, {5,8,3, 5,8,3}, 
    			
    			{1,9,5, 5,9,5}, 
    			{1,9,2, 1,9,4}, {5,9,2, 5,9,4}, 
    			{1,9,1, 5,9,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,9,3, 3,9,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{2,5,5}, {4,5,5}, 
        		{1,5,2}, {1,5,4}, 
        		{5,5,4}, 
        		{2,5,1}, {4,5,1}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
            // Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Desk
        		{2,4,2, 2,4,2}, 
        		})
            {
        		// White
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0),
        				Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 0), 
        				false);
            }
        	
            
            // Potted random flower
            int flower_u=2; int flower_v=5; int flower_w=2;
            int x = this.getXWithOffset(flower_u, flower_w);
            int y = this.getYWithOffset(flower_v);
            int z = this.getZWithOffset(flower_u, flower_w);
            
            IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
    		int randomPottedPlant = random.nextInt(10)-1;
    		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.YELLOW_FLOWER, 0);} // Dandelion specifically
    		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.RED_FLOWER, randomPottedPlant);}          // Every other type of flower
        	
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,4,4, 3, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
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
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 4;
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
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{5,4,2, 1, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{0,7,5, 3}, {0,6,5, 3}, 
        			// Forward
        			{1,7,1, 0}, {1,6,1, 0}, {1,5,1, 0}, {1,4,1, 0}, {1,3,1, 0}, {1,2,1, 0}, {1,1,1, 0}, 
        			{5,7,1, 0}, {5,6,1, 0}, 
        			// Rightward
        			{6,7,1, 1}, {6,6,1, 1}, {6,7,5, 1}, 
        			// Backward
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,4,3, -1, 0},
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
    
    
    // --- Stable --- //
    // designed by AstroTibs
    
    public static class JungleStable extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"FFFFF ",
    			"FFFFF ",
    			"FFFFF ",
    			"FFFFF ",
    			"FFFFF ",
    			"FFFFF ",
    			"FFFFFF",
    			"FFFFF ",
    			"  P   ",
    			"F P F ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleStable() {}

        public JungleStable(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleStable buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleStable(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,1,9, 2,2,9}, 
    			{0,1,9, 0,2,9}, {4,1,9, 4,2,9}, 
    			{0,1,7, 0,2,7}, {4,1,7, 4,2,7}, 
    			{0,1,5, 0,2,5}, {4,1,5, 4,2,5}, 
    			{0,1,3, 0,2,3}, {4,1,3, 4,2,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{2,2,8, 2}, 
    			{0,3,3, -1}, {4,3,3, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,1,9, 1,1,9}, {3,1,9, 3,1,9}, 
        		{0,1,8, 0,1,8}, {4,1,8, 4,1,8}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			// Ceiling support
    			{1,1,7, 3,1,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
    		}
			
            
			// Fence
			IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{1,2,9, 1,2,9}, {3,2,9, 3,2,9}, 
				{0,1,0, 0,4,0}, {0,1,2, 0,3,2}, {0,1,4, 0,3,4}, {0,1,6, 0,2,6}, {0,2,8, 0,2,8}, 
				{4,1,0, 4,4,0}, {4,1,2, 4,3,2}, {4,1,4, 4,3,4}, {4,1,6, 4,2,6}, {4,2,8, 4,2,8}, 
				{1,1,2, 1,1,2}, {3,1,2, 3,1,2}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
			}
            
            
        	// Fence Gate (Across)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{2,1,2}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(2, false)), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{0,4,2, 0,4,2}, {4,4,2, 4,4,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,3,8, 4,3,9},
            	{0,4,4, 4,4,5},
            	{0,5,0, 4,5,1},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,3,6, 4,3,7},
            	{0,4,3, 4,4,3}, {1,4,2, 3,4,2},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
            }
    		
        	
        	// Grass blocks with grass patches atop
        	for (int[] uuvvww : new int[][]{
				{1,0,3, 3,0,6}, 
        	})
        	{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1]+1, uuvvww[2], uuvvww[3], uuvvww[4]+1, uuvvww[5], Blocks.TALLGRASS.getStateFromMeta(1), Blocks.TALLGRASS.getStateFromMeta(1), false);
        	}
    		
        	
        	// Grass blocks under the fence
        	for (int[] uuvvww : new int[][]{
				{0,0,3, 0,0,9}, {4,0,3, 4,0,9}, 
				{0,0,2, 4,0,2}, {1,0,7, 3,0,9}, 
        	})
        	{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);
        	}
			
			
			// Water
			for(int[] uuvvww : new int[][]{
				{1,1,8, 3,1,8}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
			}
    		
    		
    		// Hay bales (vertical)
    		IBlockState biomeHayBaleVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.HAY_BLOCK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvw : new int[][]{
    			{5,1,3}, 
    			})
    		{
        		this.setBlockState(world, biomeHayBaleVertState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{-1,2,9, 3}, 
        			// Forward
        			{2,2,10, 0}, {0,2,10, 0}, {0,1,10, 0}, 
        			// Rightward
        			// Backward
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
            
            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1)
            	{
	            	// Animals
	            	for (int[] uvw : new int[][]{
	        			{2,1,4}, 
	        			})
	        		{
	            		BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
	                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, true, this.materialType==MaterialType.MUSHROOM);
	                	if (VillageGeneratorConfigHandler.nameVillageHorses && GeneralConfig.nameEntities && animal instanceof EntityHorse)
	                	{
	                		String[] petname_a = NameGenerator.newRandomName("pet", random);
	                		animal.setCustomNameTag((petname_a[1]+" "+petname_a[2]+" "+petname_a[3]).trim());
	                	}
	                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(animal);
	                    
	                    // Dirt block underneath
	                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
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
    
    
    // --- Stepped Farm --- //
    // designed by Lonemind
    
    public static class JungleSteppedFarm extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			" FFFFF         ",
    			"FFFFFFFF   FFF ",
    			"FFFFFFFF  FFFFF",
    			"FFFFFFF   FFFFF",
    			"FFFFFFF   FF  F",
    			"FFFFFFF    FFF ",
    			" FFFFF         ",
    			"        FFFFF  ",
    			"       FFFFFFF ",
    			"       FFFFFFF ",
    			"  FFF  FFFFFFF ",
    			" FFF F FFFFFFF ",
    			" FFF F FFFFFFF ",
    			" FFFFF FFFFFF  ",
    			"  FFF   F      ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1+2+4+8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleSteppedFarm() {}

        public JungleSteppedFarm(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleSteppedFarm buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleSteppedFarm(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
    								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
    								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
    						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
    				
    				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
    				
    				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
    			}
    		}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
        	
        	// Dirt
        	for (int[] uuvvww : new int[][]{
        		// Back left
        		{1,0,9, 5,2,13}, 
        		// Front right 
        		{8,0,2, 12,1,6}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeDirtState,
        				biomeDirtState, 
        				false);
            }
    		
    		
            // Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Back left 
    			{0,1,9, 0,1,10}, {0,2,10, 0,2,10}, {0,0,11, 0,0,11}, {0,2,12, 0,2,12}, {0,1,13, 0,1,13}, // Left wall
    			{1,0,14, 1,0,14}, {2,1,14, 2,2,14}, {3,2,14, 3,2,14}, {4,1,14, 4,2,14}, {5,0,14, 5,0,14}, // Back wall
    			{6,0,13, 6,1,13}, {6,0,11, 6,0,11}, {6,2,9, 6,2,11}, {6,3,11, 6,3,11}, {6,1,10, 6,1,10}, {6,3,9, 6,3,9}, {6,0,9, 6,0,9}, // Right wall
    			{1,1,8, 1,1,8}, {2,2,8, 4,2,8}, {4,1,8, 5,1,8}, {5,0,8, 5,0,8}, // Front wall
    			// Spout
    			{3,3,11, 3,3,11}, 
    			// Back right post
    			{10,0,10, 10,1,10}, 
    			// Front right
    			{7,0,2, 7,0,3}, {7,2,2, 7,2,2}, {7,2,4, 7,2,4}, {7,1,4, 7,1,5}, {7,0,6, 7,0,6}, // Left wall
    			{8,0,7, 9,0,7}, {9,1,7, 9,1,7}, {10,2,7, 10,2,7}, {11,1,7, 11,1,7}, {12,0,7, 12,0,7}, // Back wall
    			{13,0,5, 13,0,5}, {13,1,3, 13,1,4}, {13,0,3, 13,0,3}, // Right wall
    			{8,0,1, 9,0,1}, {10,1,1, 11,1,1}, {12,0,1, 12,0,1}, // Front wall
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
    		}
    		
    		
    		// Chiseled Stone Brick
    		IBlockState biomeChiseledStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			// Back left 
    			{0,2,9}, {0,0,10}, {0,1,11}, {0,0,12}, {0,2,13}, // Left wall
    			{1,2,14}, {2,0,14}, {3,1,14}, {4,0,14}, {5,2,14}, // Back wall
    			{6,2,13}, {6,0,12}, {6,1,11}, {6,0,10}, {6,2,9}, // Right wall
    			{1,2,8}, {2,0,8}, {3,1,8}, {4,0,8}, {5,2,8}, // Front wall
    			// Spout
    			{3,4,11}, 
    			// Back right post
    			{10,2,10}, 
    			// Front right
    			{7,1,2}, {7,0,4}, {7,1,6}, // Left wall
    			{8,1,7}, {10,0,7}, {12,1,7}, // Back wall
    			{13,1,2}, {13,0,4}, {13,1,6}, // Right wall
    			{8,1,1}, {10,0,1}, {12,1,1}, // Front wall
    			})
    		{
    			this.setBlockState(world, biomeChiseledStoneBrickState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvw : new int[][]{
    			// Back left 
    			{0,0,9}, {0,2,11}, {0,1,12}, {0,0,13}, // Left wall
    			{1,1,14}, {3,0,14}, {5,1,14}, // Back wall
    			{6,1,9}, {6,1,12}, {6,2,12}, // Right wall
    			{1,0,8}, {2,1,8}, {3,0,8}, // Front wall
    			// Front right
    			{7,1,3}, {7,0,5}, // Left wall
    			{10,1,7}, {11,0,7}, // Back wall
    			{13,0,2}, {13,1,5}, {13,0,6}, // Right wall
    			{9,1,1}, {11,0,1}, // Front wall
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Back left
    			{0,3,9, 0}, {0,3,12, 0},  // Left wall
    			{1,3,14, 2}, {4,3,14, 2},  // Back wall
    			{6,3,13, 1},  // Right wall
    			{1,3,8, 3}, {3,3,8, 3}, {4,3,8, 3},  // Front wall
    			// Back chute
    			{7,3,11, 2+4}, {9,3,11, 2+4}, {10,3,11, 2+4}, {11,3,11, 2+4}, {11,3,7, 1+4},  // Outside Rim
    			{7,3,9, 3+4},  // Inside Rim
    			// Front right
    			{7,2,6, 0},  // Left wall
    			{12,2,7, 2},  // Back wall
    			{13,2,6, 1}, {13,2,5, 1}, {13,2,3, 1},  // Right wall
    			{9,2,1, 3}, {11,2,1, 3}, {12,2,1, 3},  // Front wall
    			// Front chute
    			{6,2,4, 2+4},  // Back Rim
    			{5,2,2, 3+4},  // Front Rim
    			// Front left
    			{1,0,1, 0},  // Left wall
    			{2,0,4, 2}, {3,0,4, 2},  // Back wall
	   			 // Right wall
    			{3,0,0, 3},  // Front wall
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Back left
    			{0,3,10, 0}, {0,3,11, 0}, {0,3,13, 0},  // Left wall
    			{2,3,14, 2}, {3,3,14, 2}, {5,3,14, 2},  // Back wall
    			{6,3,12, 1},  // Right wall
    			{2,3,8, 3}, {5,3,8, 3},  // Front wall
	   			// Back chute
    			{8,3,11, 2+4}, {11,3,9, 3+4}, {11,3,8, 1+4},  // Outside Rim
    			{8,3,9, 3+4}, {9,3,9, 3+4}, {9,3,8, 0+4}, {9,3,7, 0+4},  // Inside Rim
    			{11,2,10, 1+4}, // Spout
	   			// Back right
    			{10,0,11, 0}, {10,0,12, 0},  // Left wall
    			{11,0,13, 2}, {12,0,13, 2}, {13,0,13, 2},  // Back wall
    			{14,0,12, 1}, {14,0,11, 1}, {14,0,10, 1},  // Right wall
    			{11,0,9, 3}, {12,0,9, 3}, {13,0,9, 3},  // Front wall
	   			// Front right
    			{7,2,5, 0},  // Left wall
    			{8,2,7, 2}, {9,2,7, 2}, {11,2,7, 2},  // Back wall
    			{13,2,4, 1}, {13,2,2, 1},  // Right wall
    			{8,2,1, 3}, {10,2,1, 3},  // Front wall
	   			// Front chute
    			{6,2,2, 3+4},  // Front Rim
    			{5,2,4, 2+4},  // Back Rim
    			{5,1,3, 1+4},  // Bottom lip
	   			// Front left
    			{1,0,2, 0}, {1,0,3, 0},  // Left wall
    			{4,0,4, 2},  // Back wall
    			{5,0,3, 1}, {5,0,2, 1}, {5,0,1, 1},  // Right wall
    			{2,0,0, 3}, {4,0,0, 3},  // Front wall
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Stone Brick Slab (upper)
    		IBlockState biomeStoneBrickSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(5+8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Back chute
    			{7,2,10, 9,2,10}, {10,2,8, 10,2,9}, 
    			// Front chute
    			{6,1,3, 6,1,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickSlabLowerState, biomeStoneBrickSlabLowerState, false);
    		}
    		
            
            // Moist Farmland with crop above
        	Block[] cropBlocksTemp1 = StructureVillageVN.chooseCropPair(random);
        	Block[] cropBlocksTemp2 = StructureVillageVN.chooseCropPair(random);
        	Block[] cropBlocks = new Block[]{cropBlocksTemp1[0],cropBlocksTemp1[1],cropBlocksTemp2[0],cropBlocksTemp2[1]};
            for(int[] uvwmcp : new int[][]{
            	// u,v,w, farmland moisture (0:dry, 7:moist), crop (0-2), crop progress
            	// Back left
            	{1,3,13, 7,0,0}, {2,3,13, 7,0,0}, {3,3,13, 7,0,0}, {4,3,13, 7,0,0}, {5,3,13, 7,0,0}, 
            	{1,3,12, 7,0,0}, {2,3,12, 7,0,0}, {4,3,12, 7,0,0}, {5,3,12, 7,0,0}, 
            	{1,3,11, 7,0,0}, {5,3,11, 7,0,0}, 
            	{1,3,10, 7,0,0}, {2,3,10, 7,0,0}, 
            	{1,3,9, 7,0,0}, {2,3,9, 7,0,0}, {3,3,9, 7,0,0}, {4,3,9, 7,0,0}, {5,3,9, 7,0,0}, 
            	// Back right
            	{11,0,12, 7,1,0}, {12,0,12, 7,1,0}, {13,0,12, 7,1,0}, 
            	{11,0,11, 7,1,0}, {12,0,11, 7,1,0}, {13,0,11, 7,1,0}, 
            	{11,0,10, 7,1,0}, 
            	// Front right
            	{11,2,6, 7,2,0}, {12,2,6, 7,2,0}, 
            	{9,2,5, 7,2,0}, {10,2,5, 7,2,0}, {11,2,5, 7,2,0}, {12,2,5, 7,2,0}, 
            	{9,2,4, 7,2,0}, {10,2,4, 7,2,0}, {11,2,4, 7,2,0}, {12,2,4, 7,2,0}, 
            	{9,2,3, 7,2,0}, {10,2,3, 7,2,0}, {11,2,3, 7,2,0}, {12,2,3, 7,2,0}, 
            	{8,2,2, 7,2,0}, {9,2,2, 7,2,0}, {10,2,2, 7,2,0}, {11,2,2, 7,2,0}, {12,2,2, 7,2,0}, 
            	// Front left
            	{2,0,3, 7,3,0}, {3,0,3, 7,3,0}, 
            	{2,0,2, 7,3,0}, {3,0,2, 7,3,0}, 
            	{2,0,1, 7,3,0}, {3,0,1, 7,3,0}, {4,0,1, 7,3,0}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvwmcp[0], uvwmcp[1]-1, uvwmcp[2], structureBB);
            	//this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	
            	int cropProgressMeta = uvwmcp[5]; // Isolate the crop's age meta value
            	IBlockState cropState;
            	
    			while(true)
    			{
    				try {cropState = cropBlocks[0].getStateFromMeta(cropProgressMeta);}
    				catch (IllegalArgumentException e)
    				{
    					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
    					if (cropProgressMeta==0) {cropState = Blocks.WHEAT.getStateFromMeta(uvwmcp[5]);}
    					// The crop is not allowed to have this value. Cut it in half and try again.
    					else {cropProgressMeta /= 2; continue;}
    				}
    				
    				// Finally, assign the working crop
					this.setBlockState(world, cropState, uvwmcp[0], uvwmcp[1]+1, uvwmcp[2], structureBB);
    				break;
    			}
            	
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwmcp[3]), uvwmcp[0], uvwmcp[1], uvwmcp[2], structureBB); // 7 is moist
            }
            
            
    		// Water
    		for(int[] uuvvww : new int[][]{ 
    			// Top of back-left fountain
    			{3,5,11, 3,5,11}, 
    			// Back-left fountain and chute
    			{2,3,11, 2,3,11}, {3,3,12, 3,3,12}, {4,3,11, 4,3,11}, {3,3,10, 7,3,10}, 
    			// Back-right fountain
    			{12,0,10, 12,0,10}, 
    			// Front-right fountain
    			{8,2,6, 10,2,6}, 
    			// Front-left fountain
    			{4,0,2, 4,0,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
    		}
    		// Lower-leveled water
    		for(int[] uvw : new int[][]{ 
    			// Top of back-left fountain
    			{8,3,10, 1}, 
    			{9,3,10, 2}, 
    			{10,3,10, 3}, 
    			{10,3,9, 4}, 
    			})
    		{
    			this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(uvw[3]), uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Attempt to add GardenCore Compost Bins. If this fails, do nothing
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            if (compostBinState != null)
            {
            	for(int[] uvw : new int[][]{
        			{7,0,1}, 
        			{7,0,12}, 
        			})
        		{
        			this.setBlockState(world, compostBinState, uvw[0], uvw[1], uvw[2], structureBB);
        			this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LADDER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{8,0,0, 8,1,0, 2},  
        		{7,0,13, 7,2,13, 1},  
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), false);
            }
        	
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{-1,0,9, 3}, {-1,1,9, 3}, {-1,1,10, 3}, {-1,2,10, 3}, {-1,1,13, 3}, {-1,2,13, 3}, // Back-left
        			{6,1,5, 3}, // Front-right
        			// Forward
        			{0,0,14, 0}, {0,1,14, 0}, {0,2,14, 0}, {2,0,15, 0}, {2,1,15, 0}, {3,1,15, 0}, {3,2,15, 0}, // Back-left
        			{8,1,8, 0}, // Front-right
        			// Rightward
        			{7,2,11, 1}, {7,1,11, 1},  // Back-left
        			{13,0,1, 1}, {13,1,1, 1}, {14,0,3, 1}, {14,1,3, 1}, {14,1,4, 1}, // Front-right
        			// Backward
        			{1,1,7, 2}, {1,2,7, 2}, {4,1,7, 2}, {4,2,7, 2}, {5,1,7, 2},  // Back-left
        			{9,0,0, 2}, {10,0,0, 2}, {10,1,0, 2}, // Front-right
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int u = 2+random.nextInt(2);
				int v=1;
    			int w = 1+random.nextInt(3);
				
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0); // Farmer
    			
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
    
    
    // --- Stone Animal Pen --- //
    // designed by AstroTibs
    
    public static class JungleStoneAnimalPen extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"    FFF    ",
    			"FFFFFFFFFFF",
    			"FFFFFFFFFFF",
    			"FFFFFPFFFFF",
    			"FFFFFPFFFFF",
    			"FFFFFPFFFFF",
    			"FFFFFPFFFFF",
    			"FFFFFPFFFFF",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 4;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleStoneAnimalPen() {}

        public JungleStoneAnimalPen(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleStoneAnimalPen buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleStoneAnimalPen(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			{0,0,0}, {0,0,2}, {0,0,3}, {0,0,5}, 
    			{1,0,6}, {4,0,7}, {5,1,7}, {8,0,6}, {10,0,1}, {10,0,2}, {10,0,6}, 
    			{2,0,0}, {4,1,0}, {6,0,0}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			{0,0,1}, 
    			{0,0,4}, 
    			{0,0,6}, 
    			{2,0,6}, {3,0,6}, {4,0,6}, {4,1,6}, {6,0,6}, {6,1,6}, {7,0,6}, {9,0,6}, {10,0,5}, {10,0,4}, {10,0,3}, 
    			{4,1,7}, {5,0,7}, {6,0,7}, {6,1,7}, 
    			{10,0,0}, {9,0,0}, {8,0,0}, {7,0,0}, {6,1,0}, {4,0,0}, {3,0,0}, {1,0,0}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickBlockState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Chiseled Stone Brick
    		IBlockState biomeChiseledStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			{4,2,6}, {6,2,6}, 
    			{4,2,0}, {6,2,0}, 
    			{5,0,6}, 
    			})
    		{
    			this.setBlockState(world, biomeChiseledStoneBrickState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
			
			
			// Cobblestone wall
			IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{1,1,6, 3,1,6}, {7,1,6, 9,1,6}, 
				{0,1,1, 0,1,6}, {10,1,1, 10,1,6}, 
				{0,1,0, 3,1,0}, {7,1,0, 10,1,0}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
			}
			
            
			// Fence
			IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{4,1,1, 4,1,1}, {4,1,3, 4,1,5}, 
				{6,1,1, 6,1,1}, {6,1,3, 6,1,5}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
			}
            
            
        	// Fence Gate (Along)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{4,1,2}, {6,1,2}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(1, false)), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{4,3,6, -1}, {6,3,6, -1}, 
    			{4,3,0, -1}, {6,3,0, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
        	
        	// Grass blocks with grass patches atop
        	for (int[] uuvvww : new int[][]{
				{1,0,1, 3,0,4}, {7,0,1, 9,0,4}, 
        	})
        	{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1]+1, uuvvww[2], uuvvww[3], uuvvww[4]+1, uuvvww[5], Blocks.TALLGRASS.getStateFromMeta(1), Blocks.TALLGRASS.getStateFromMeta(1), false);
        	}
    		
        	
        	// Grass blocks under the fence
        	for (int[] uuvvww : new int[][]{
				{4,0,1, 4,0,4}, {6,0,1, 6,0,4}, 
        	})
        	{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);
        	}
			
			
			// Water
			for(int[] uuvvww : new int[][]{
				{1,0,5, 9,0,5}, {5,1,6, 5,1,6}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
			}
            
            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1)
            	{
	            	// Animals
	            	for (int[] uvw : new int[][]{
	        			{2,1,2}, {8,1,3}, 
	        			})
	        		{
	            		BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
	                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM); // Because horses can escape the pen
	                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(animal);
	                    
	                    // Dirt block underneath
	                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
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
    
    
    // --- Tamed Farm --- //
    // designed by Lonemind
    
    public static class JungleTamedFarm extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"    FFFF     ",
    			"    FFFF     ",
    			" FFFFFFFFFF  ",
    			" FFFFFFFFFF  ",
    			" FFFFFFFFFF  ",
    			" FFFFFFFFFF  ",
    			" FFFFFFFFFF  ",
    			" FFFFFFFFFFFF",
    			"FFFFFFFFFFFFF",
    			"FFFFFFFFFFFFF",
    			"FFFFFFFFFFFFF",
    			"FFFFFFFFFFFFF",
    			"   FFFFFF    ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 3;
    	private static final int DECREASE_MAX_U = 4;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleTamedFarm() {}

        public JungleTamedFarm(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleTamedFarm buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleTamedFarm(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
    								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
    								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
    						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
    				
    				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
    				
    				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
    			}
    		}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
        	
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{0,0,1, 0,0,1}, 
        		{0,0,4, 0,0,4}, 
        		{1,0,10, 1,0,10}, 
        		{3,0,0, 3,0,1}, 
        		{4,0,10, 4,0,10}, 
        		{4,0,12, 4,0,12}, 
        		{7,0,10, 7,0,10}, 
        		{7,0,12, 7,0,12}, 
        		{8,0,0, 8,0,1}, 
        		{10,0,5, 10,0,5}, 
        		{10,0,10, 10,0,10}, 
        		{12,0,1, 12,0,1}, 
        		{12,0,5, 12,0,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Front
    			{1,0,1, 3}, {2,0,1, 3}, {4,0,0, 3}, {5,0,0, 3}, {6,0,0, 3}, {7,0,0, 3}, {9,0,1, 3}, {10,0,1, 3}, {11,0,1, 3}, 
    			// Right
    			{12,0,2, 1}, {12,0,3, 1}, {12,0,4, 1}, {11,0,5, 2}, {10,0,6, 1}, {10,0,7, 1}, {10,0,8, 1}, {10,0,9, 1}, 
    			// Back
    			{2,0,10, 2}, {3,0,10, 2}, {4,0,11, 0}, {5,0,12, 2}, {6,0,12, 2}, {7,0,11, 1}, {8,0,10, 2}, {9,0,10, 2}, 
    			// Left
    			{0,0,2, 0}, {0,0,3, 0}, {1,0,4, 2}, {1,0,5, 0}, {1,0,6, 0}, {1,0,7, 0}, {1,0,8, 0}, {1,0,9, 0}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Grass
    		for(int[] uuvvww : new int[][]{
    			{1,0,2, 1,0,3}, 
    			{2,0,8, 3,0,9}, 
    			{4,0,1, 4,0,6}, {4,0,9, 4,0,9}, 
    			{5,0,8, 5,0,8}, 
    			{5,0,1, 6,0,1}, {5,0,11, 6,0,11}, {5,0,10, 5,0,10}, 
    			{7,0,1, 7,0,6}, {7,0,8, 7,0,8}, 
    			{8,0,7, 9,0,9}, 
    			{10,0,2, 11,0,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);
    		}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,1,3, 1,1,3}, 
        		{3,1,9, 3,1,9}, 
        		{4,1,6, 4,1,6}, 
        		{7,1,1, 7,1,1}, 
        		{8,1,7, 8,1,7}, 
        		{10,1,3, 10,1,3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// On fence posts
    			{1,2,3, -1}, 
        		{3,2,9, -1}, 
        		{4,2,6, -1}, 
        		{7,2,1, -1}, 
        		{8,2,7, -1}, 
        		{10,2,3, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
        	
            
            // Moist Farmland with crop above
        	Block[] cropBlocksTemp1 = StructureVillageVN.chooseCropPair(random);
        	Block[] cropBlocksTemp2 = StructureVillageVN.chooseCropPair(random);
        	Block[] cropBlocks = new Block[]{cropBlocksTemp1[0],cropBlocksTemp1[1],cropBlocksTemp2[0]};
            for(int[] uvwmcp : new int[][]{
            	// u,v,w, farmland moisture (0:dry, 7:moist), crop (0-2), crop progress
            	// Left row
            	{2,0,2, 7,0,0}, {2,0,3, 7,0,0}, {2,0,4, 7,0,0}, {2,0,5, 7,0,0}, {2,0,6, 7,0,0}, {2,0,7, 7,0,0}, 
            	{3,0,2, 7,0,0}, {3,0,3, 7,0,0}, {3,0,4, 7,0,0}, {3,0,5, 7,0,0}, {3,0,6, 7,0,0}, {3,0,7, 7,0,0}, 
            	// Center row
            	{5,0,2, 7,1,0}, {5,0,3, 7,1,0}, {5,0,4, 7,1,0}, {5,0,5, 7,1,0}, 
            	{6,0,2, 7,1,0}, {6,0,3, 7,1,0}, {6,0,4, 7,1,0}, {6,0,5, 7,1,0}, 
            	// Right row
            	{8,0,2, 7,2,0}, {8,0,3, 7,2,0}, {8,0,4, 7,2,0}, {8,0,5, 7,2,0}, {8,0,6, 7,2,0}, 
            	{9,0,2, 7,2,0}, {9,0,3, 7,2,0}, {9,0,4, 7,2,0}, {9,0,5, 7,2,0}, {9,0,6, 7,2,0}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvwmcp[0], uvwmcp[1]-1, uvwmcp[2], structureBB);
            	//this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	
            	int cropProgressMeta = uvwmcp[5]; // Isolate the crop's age meta value
            	IBlockState cropState;
            	
    			while(true)
    			{
    				try {cropState = cropBlocks[0].getStateFromMeta(cropProgressMeta);}
    				catch (IllegalArgumentException e)
    				{
    					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
    					if (cropProgressMeta==0) {cropState = Blocks.WHEAT.getStateFromMeta(uvwmcp[5]);}
    					// The crop is not allowed to have this value. Cut it in half and try again.
    					else {cropProgressMeta /= 2; continue;}
    				}
    				
    				// Finally, assign the working crop
					this.setBlockState(world, cropState, uvwmcp[0], uvwmcp[1]+1, uvwmcp[2], structureBB);
    				break;
    			}
            	
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwmcp[3]), uvwmcp[0], uvwmcp[1], uvwmcp[2], structureBB); // 7 is moist
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{5,1,8, 5,2,9}, {5,2,8, 5,3,9}, 
    			{6,3,9, 6,3,9}, {6,5,9, 6,5,9}, 
    			{6,2,10, 6,3,10}, {6,5,10, 6,5,10}, 
    			{7,1,8, 7,1,9}, {7,4,9, 7,5,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
            // Stone Brick
        	IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{5,1,9, 5,1,9}, {5,4,9, 5,5,9}, 
    			{6,1,9, 6,2,9}, {6,4,9, 6,4,9}, 
    			{6,1,10, 6,1,10}, {6,4,10, 6,4,10}, 
    			{7,2,8, 7,3,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
    		}
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{5,3,8, 3}, 
    			{6,6,10, 2}, 
    			{7,6,9, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{5,6,9, 0}, 
    			{7,4,8, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvw : new int[][]{
    			{6,7,9}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneSlabLowerState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			{4,0,7, 4,0,8}, 
    			{5,0,6, 6,0,7}, 
    			{6,0,8, 6,0,8}, 
    			{7,0,7, 7,0,7}, 
    			// Top of fountain
    			{6,6,9, 6,6,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
    		}
    		
    		
    		// Attempt to add GardenCore Compost Bins. If this fails, do nothing
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            if (compostBinState!=null)
            {
            	for(int[] uvw : new int[][]{
        			{4,1,1}, 
        			})
        		{
        			this.setBlockState(world, compostBinState, uvw[0], uvw[1], uvw[2], structureBB);
        			this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
    		// Bamboo
    		IBlockState bambooStalkState = ModObjects.chooseModBambooStalk();
			if (bambooStalkState!=null)
    		{
				for (int[] uuvvww : new int[][]{
					{2,1,8, 2,6,8}, 
					{2,1,9, 2,4,9}, 
					{4,1,9, 4,3,9}, 
					{8,1,9, 8,2,9}, 
					{9,1,8, 9,5,8}, 
					{10,1,2, 10,4,2}, 
            		})
                {
					this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], bambooStalkState, bambooStalkState, false);
                }
				
				// Leaf toppers
				IBlockState bambooLeavesState = ModObjects.chooseModBambooLeaves();
				if (bambooLeavesState!=null)
	    		{
					for (int[] uuvvww : new int[][]{
						{2,7,8}, 
						{2,5,9}, 
						{9,6,8}, 
						{10,5,2}, 
	            		})
	                {
						this.setBlockState(world, bambooLeavesState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
	                }
	    		}
    		}
			else // As sugarcane
			{
				bambooStalkState = Blocks.REEDS.getDefaultState();
				
				// Add water to support the sugarcane
				for(int[] uvw : new int[][]{
	    			{5,0,9}, 
	    			{7,0,9}, 
	    			})
	    		{
	    			this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
	    		}
				
				for (int[] uuvvww : new int[][]{
					{4,1,9, 4,2,9}, 
					{8,1,9, 8,1,9}, 
            		})
                {
					this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], bambooStalkState, bambooStalkState, false);
                }
			}
			
            
    		// Bamboo Shoots
    		ItemStack modItemStack = ModObjects.chooseModBambooShoot();
			if (modItemStack!=null)
    		{
				Block bambooShootBlock = Block.getBlockFromItem(modItemStack.getItem()); int bambooShootMeta = modItemStack.getItemDamage();
				
				if (bambooShootBlock!=null)
	    		{
					for (int[] uvw : new int[][]{
						{8,1,8}, 
						{9,1,7}, 
	            		})
	                {
						this.setBlockState(world, bambooShootBlock.getStateFromMeta(bambooShootMeta), uvw[0], uvw[1], uvw[2], structureBB);
	                }
	    		}
    		}
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{4,1,8, 3}, {4,2,8, 3}, 
        			{4,4,9, 3}, {4,5,9, 3}, 
        			// Backward
        			{7,1,7, 2}, {7,2,7, 2}, {7,3,7, 2}, 
        			// Forward
        			{5,3,10, 0}, {5,4,10, 0}, 
        			{6,1,11, 0}, {6,2,11, 0}, {6,3,11, 0}, 
        			// Rightward
        			{8,2,8, 1}, {8,3,8, 1}, 
        			{8,3,9, 1}, {8,4,9, 1}, {8,5,9, 1}, 
        			{7,1,10, 1}, {7,2,10, 1}, {7,3,10, 1}, {7,4,10, 1}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int u = 2+random.nextInt(8);
				int v=1;
    			int w = 2+random.nextInt(4);
				
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0); // Farmer
    			
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
    
    
    // --- Tannery 1 --- //
    // designed by AstroTibs
    
    public static class JungleTannery1 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"   F  F ",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			"FFFFFFFF",
    			" FFFPPFF",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleTannery1() {}

        public JungleTannery1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleTannery1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleTannery1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Counter
    			{3,0,1, 6,0,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Stripped Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
    		for (int[] uuvvww : new int[][]{
    			{3,1,6, 3,1,6}, {6,1,6, 6,1,6}, 
    			{3,1,4, 3,1,4}, {6,1,4, 6,1,4}, 
    			{3,1,2, 3,1,2}, {6,1,2, 6,1,2}, 
    			
    			{3,4,6, 3,4,6}, {6,4,6, 6,4,6}, 
    			{3,4,4, 3,4,4}, {6,4,4, 6,4,4}, 
    			{3,4,2, 3,4,2}, {6,4,2, 6,4,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
        	
        	// Stone Brick wall
    		IBlockState biomeStoneBrickWallStone = ModObjects.chooseModStoneBrickWallState();
        	biomeStoneBrickWallStone = StructureVillageVN.getBiomeSpecificBlockState(biomeStoneBrickWallStone!=null?biomeStoneBrickWallStone:Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{3,1,8, 3,1,8}, {6,1,8, 6,1,8}, 
            	{2,1,7, 2,1,7}, {7,1,7, 7,1,7}, 
            	{2,1,1, 2,1,1}, {7,1,1, 7,1,1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickWallStone, biomeStoneBrickWallStone, false);
            }
    		
    		
    		// Torches part 1
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,2,8, -1}, {6,2,8, -1}, 
            	{2,2,7, -1}, {7,2,7, -1}, 
            	{2,2,1, -1}, {7,2,1, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
            // Stone brick
        	IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	{3,1,7, 6,4,7}, 
            	{3,1,5, 3,4,5}, {6,1,5, 6,4,5}, 
            	{3,1,3, 3,4,3}, {6,1,3, 6,4,3}, 
            	{3,1,1, 3,4,1}, {4,4,1, 5,4,1}, {6,1,1, 6,4,1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
            }
    		
    		
    		// Torches part 2
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{4,3,5, 1}, {5,3,5, 3}, 
    			{4,3,3, 1}, {5,3,3, 3}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
    			{3,2,6}, {6,2,6}, 
    			{3,2,4}, {6,2,4}, 
    			{3,2,2}, {6,2,2}, 
    			{3,3,6}, {6,3,6}, 
    			{3,3,4}, {6,3,4}, 
    			{3,3,2}, {6,3,2}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Awning scaffold
    			{0,1,7, 0,2,7}, 
    			{0,1,4, 0,2,4}, 
    			{0,1,1, 0,2,1}, 
    			// Roof
    			{4,5,1, 5,5,1}, {4,5,7, 5,5,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Front awning
    			{4,3,0, 3}, {5,3,0, 3}, 
    			// Roof
    			{3,5,7, 0}, {6,5,7, 1}, 
    			{3,5,6, 0}, {6,5,6, 1}, 
    			{3,5,5, 0}, {6,5,5, 1}, 
    			{3,5,4, 0}, {6,5,4, 1}, 
    			{3,5,3, 0}, {6,5,3, 1}, 
    			{3,5,2, 0}, {6,5,2, 1}, 
    			{3,5,1, 0}, {6,5,1, 1}, 
    			{4,6,7, 0}, {5,6,7, 1}, 
    			{4,6,6, 0}, {5,6,6, 1}, 
    			{4,6,5, 0}, {5,6,5, 1}, 
    			{4,6,4, 0}, {5,6,4, 1}, 
    			{4,6,3, 0}, {5,6,3, 1}, 
    			{4,6,2, 0}, {5,6,2, 1}, 
    			{4,6,1, 0}, {5,6,1, 1}, 
    			// Desk
    			{5,1,4, 0+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,3,1, 0,3,7}, {2,4,1, 2,4,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,3,1, 1,3,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
            
        	
            // Cauldron
        	for (int[] uvw : new int[][]{
        		{2,1,6}, 
        		{2,1,5}, 
        		{2,1,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.CAULDRON.getStateFromMeta(3), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Smooth Stone Block
        	IBlockState smoothStoneBlockState = ModObjects.chooseModSmoothStoneBlockState();
        	for (int[] uuvvww : new int[][]{
            	// Counter
            	{4,1,6, 5,1,6}, {5,1,5, 5,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], smoothStoneBlockState, smoothStoneBlockState, false);
            }
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,1,1, 2, 1, 1}, {5,1,1, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
        			// Forward
        			{4,4,8, 0}, {5,4,8, 0}, {5,3,8, 0}, 
        			// Rightward
        			{7,4,4, 1}, 
        			{7,4,5, 1}, {7,3,5, 1}, {7,2,5, 1}, {7,1,5, 1}, 
        			{7,4,6, 1}, {7,3,6, 1}, {7,2,6, 1}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s = random.nextInt(13);
    			
    			int u = s<=4 ? 1 : s<=6 ? 2 : s<=10 ? 4 : 5;
    			int v = 1;
    			int w = s<=4 ? s+2 : s<=6 ? s-3 : s<=10 ? s-5 : s-9;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 2, 0); // Leatherworker
    			
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
    
    
    // --- Tannery 2 --- //
    // designed by AstroTibs
    
    public static class JungleTannery2 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"  F  F  ",
    			" FFFFFF ",
    			"FFFFFFFF",
    			" FFFFFF ",
    			" FFFFFF ",
    			"FFFFFFFF",
    			" FFFFFF ",
    			" F  PPF ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleTannery2() {}

        public JungleTannery2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleTannery2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleTannery2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
        	// Dirt
        	for (int[] uuvvww : new int[][]{
        		{2,2,3, 5,2,5}, 
        		{2,1,2, 5,1,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeFillerState,
        				biomeFillerState, 
        				false);
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Counter
    			{2,3,5, 5,3,5}, 
    			{4,3,4, 5,3,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,3,6, 1, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
        		{4,3,6, 0, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
        		{4,2,6, 3, GeneralConfig.useVillageColors ? this.townColor : 0}, // White
        		{3,2,6, 2, GeneralConfig.useVillageColors ? this.townColor2 : 4}, // Yellow
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], StructureVillageVN.chooseGlazedTerracottaMeta(uvwoc[3], this.getCoordBaseMode()));
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
    		
        	
        	// Stone Brick wall
    		IBlockState biomeStoneBrickWallStone = ModObjects.chooseModStoneBrickWallState();
        	biomeStoneBrickWallStone = StructureVillageVN.getBiomeSpecificBlockState(biomeStoneBrickWallStone!=null?biomeStoneBrickWallStone:Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Left wall
            	{1,1,0, 1,1,0}, {1,1,1, 1,2,1}, {1,1,2, 1,3,2}, {1,1,3, 1,4,3}, 
            	// Right wall
            	{6,1,0, 6,1,0}, {6,2,1, 6,2,1}, {6,3,2, 6,3,2}, {6,4,3, 6,4,3}, 
            	// Outside posts
            	{0,1,2, 0,2,2}, {0,1,5, 0,2,5}, {2,1,7, 2,2,7}, 
            	{7,1,2, 7,2,2}, {7,1,5, 7,2,5}, {5,1,7, 5,2,7}, 
            	// Roof supports
            	{1,5,4, 1,5,4}, {1,5,6, 1,5,6}, 
            	{6,5,4, 6,5,4}, {6,5,6, 6,5,6}, 
            	{3,4,4, 3,5,4}, {6,4,4, 6,5,4}, {3,5,6, 4,5,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickWallStone, biomeStoneBrickWallStone, false);
            }
    		
    		
    		// Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			// Longer log
    			{1,6,5, 1,6,5}, {6,6,5, 6,6,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{0,3,2, -1}, {0,3,5, -1}, {2,3,7, -1}, 
            	{7,3,2, -1}, {7,3,5, -1}, {5,3,7, -1}, 
            	{2,6,5, 1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
            // Stone brick
        	IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,4, 1,4,6}, 
            	// Right wall
            	{6,1,5, 6,4,6}, {6,1,3, 6,3,4}, {6,1,2, 6,2,2}, {6,1,1, 6,1,1}, 
            	// Front steps
            	{2,4,4, 2,4,4}, 
            	{2,3,3, 3,3,4}, 
            	{2,2,2, 3,2,2}, 
            	{2,1,1, 3,1,1}, 
            	// Back wall
            	{2,1,6, 2,4,6}, {5,1,6, 5,4,6}, 
            	{3,1,6, 4,1,6}, {3,4,6, 4,4,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
            }
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Roof support
    			{2,5,4, 2,5,4}, {1,5,5, 1,5,5}, 
    			{2,5,6, 2,5,6}, {5,5,6, 5,5,6}, 
    			{6,5,5, 6,5,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,6,4, 6,6,4}, 
    			{1,7,5, 6,7,5}, 
    			{1,6,6, 6,6,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
            
        	
            // Cauldron
        	for (int[] uvw : new int[][]{
        		{2,4,3}, 
        		{2,3,2}, 
        		{2,2,1}, 
        		})
            {
        		this.setBlockState(world, Blocks.CAULDRON.getStateFromMeta(3), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Stairs
    			{4,3,3, 3}, {5,3,3, 3}, 
    			{4,2,2, 3}, {5,2,2, 3}, 
    			{4,1,1, 3}, {5,1,1, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		

    		// Chest
    		// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
    		int chestU = 2;
    		int chestV = 4;
    		int chestW = 5;
    		int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
    		{
    			ChestGenHooks chestGenHook = ChestGenHooks.getInfo(Reference.VN_TANNERY);
    			WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
    		}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s = random.nextInt(14);
    			
    			int u = s<=3 ? 3 : s<=8 ? 4 : 5;
    			int v = s<=0 ? 2 : s<=1 ? 3 : s<=3 ? 4 : s<=4 ? 2 : s<=5 ? 3 : s<=8 ? 4 : s<=9 ? 2 : s<=10 ? 3 : 4;
    			int w = s<=0 ? 1 : s<=1 ? 2 : s<=2 ? 3 : s<=3 ? 5 : s<=8 ? s-3 : s-8;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 2, 0); // Leatherworker
    			
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
    
    
    // --- Temple --- //
    // designed by Lonemind
    
    public static class JungleTemple extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"    FFFF    ",
    			"   FFFFFF   ",
    			"  FFFFFFFF  ",
    			" FFFFFFFFFF ",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF",
    			" FFFFFFFFFF ",
    			"  FFFFFFFF  ",
    			"   FFFFFF   ",
    			"    FFFF    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2 + 4 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public JungleTemple() {}

    	public JungleTemple(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
    	}
    	
    	public static JungleTemple buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new JungleTemple(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			// Stairs
    			{5,1,1}, {5,2,3}, {6,1,3}, {6,2,3}, {6,2,2}, // Front
    			{3,2,5}, {3,3,6}, // Left
    			{5,1,9}, {5,2,8}, {6,3,8}, {6,1,9}, // Back
    			{8,1,5}, {10,1,5}, {8,2,6}, {10,1,6}, // Right
    			// Spires
    			{3,2,3}, 
    			{3,1,8}, 
    			{8,2,3}, {9,1,2}, 
    			{9,1,9}, 
    			// Set into grass
    			{2,0,4}, {4,0,4}, {6,0,4}, 
    			{5,0,7}, {6,0,7}, {7,0,9}, 
    			{5,0,5}, {5,0,5}, {6,0,6}, 
    			// Top spire
    			{5,4,4}, {5,4,6}, 
    			{4,6,4}, {7,5,4}, {7,5,7},  {4,5,7}, {4,6,7}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			// Stairs
    			{5,1,2}, {5,1,3}, {5,2,2}, {5,3,3}, {6,1,1}, {6,1,2}, {6,3,3}, // Front
    			{1,1,5}, {2,1,5}, {2,2,5}, {3,1,5}, {3,3,5}, {1,1,6}, {2,1,6}, {3,1,6}, {2,2,6}, {3,2,6}, // Left
    			{5,1,8}, {5,1,10}, {5,3,8}, {5,2,9}, {6,1,8}, {6,2,8}, {6,2,9}, {6,1,10}, // Back
    			{8,2,5}, {8,3,5}, {8,1,6}, {8,3,6}, {9,1,5}, {9,2,5}, {9,1,6}, {9,2,6}, // Right
    			// Spires
    			{2,1,2}, {3,1,3}, 
    			{8,1,3}, 
    			{2,1,9}, {3,2,8}, 
    			{8,1,8}, {8,2,8}, 
    			// Set into grass 
    			{3,0,2}, 
    			{4,0,5}, {4,0,7}, 
    			{5,0,4}, 
    			{7,0,4}, {7,0,6}, {7,0,7}, 
    			{8,0,2}, 
    			{9,0,7}, 
    			// Top spire 
    			{4,4,4}, {4,4,5}, {4,4,6}, {4,4,7}, 
    			{5,4,5}, {5,4,7}, 
    			{6,4,4}, {6,4,5}, {6,4,6}, {6,4,7}, 
    			{7,4,4}, {7,4,5}, {7,4,6}, {7,4,7}, 
    			{4,5,4}, 
    			{7,6,4}, {7,6,7}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickBlockState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Chiseled Stone Brick
    		IBlockState biomeChiseledStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			// Outer spires
    			{3,3,8}, {8,3,8}, 
    			{3,3,3}, {8,3,3}, 
    			// Awning corners
    			{4,7,7}, {7,7,7}, 
    			{4,7,4}, {7,7,4}, 
    			})
    		{
    			this.setBlockState(world, biomeChiseledStoneBrickState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Polished Andesite
    		IBlockState biomePolishedAndesiteState = Blocks.STONE.getStateFromMeta(6);
    		biomePolishedAndesiteState = StructureVillageVN.getBiomeSpecificBlockState(biomePolishedAndesiteState, this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			// Central platform
    			{5,1,6}, {6,1,6}, 
    			{5,1,5}, {6,1,5}, 
    			})
    		{
    			this.setBlockState(world, biomePolishedAndesiteState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Polished Andesite Slab (Lower)
    		IBlockState biomePolishedAndesiteSlabBottomState = ModObjects.chooseModPolishedAndesiteSlabState(false);
    		if (biomePolishedAndesiteSlabBottomState==null) // Try brick slab
    		{
    			biomePolishedAndesiteSlabBottomState = ModObjects.chooseModAndesiteBrickSlabState(false);
        		if (biomePolishedAndesiteSlabBottomState==null) // Use basic polished stone slab
        		{
        			biomePolishedAndesiteSlabBottomState = Blocks.STONE_SLAB.getStateFromMeta(0);
        		};
    		};
    		biomePolishedAndesiteSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(biomePolishedAndesiteSlabBottomState, this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			{2,2,2}, {3,4,3}, {9,2,2}, {8,4,3}, 
    			{2,2,9}, {3,4,8}, {9,2,9}, {8,4,8}, 
    			})
    		{
    			this.setBlockState(world, biomePolishedAndesiteSlabBottomState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Stairs
    			{6,1,0, 3}, {5,2,1, 3}, {6,3,2, 3}, // Front
    			{0,1,6, 0}, {1,2,5, 0}, {2,3,5, 0}, {3,4,6, 0}, // Left
    			{5,1,11, 2}, {6,2,10, 2}, {5,3,9, 2}, {6,3,9, 2}, {5,4,8, 2}, // Back
    			{11,1,6, 1}, {10,2,5, 1}, {10,2,6, 1}, {9,3,5, 1}, {8,4,6, 1}, // Right
    			// Lower roof rim
    			{5,7,7, 3+4}, {7,7,6, 0+4}, 
    			// Upper roof rim
    			{4,8,5, 0}, {4,8,7, 2}, {6,8,7, 2}, {7,8,7, 2}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Stairs
    			{5,1,0, 3}, {6,2,1, 3}, {5,3,2, 3}, {5,4,3, 3}, {6,4,3, 3}, // Front
    			{0,1,5, 0}, {1,2,6, 0}, {2,3,6, 0}, {3,4,5, 0}, // Left
    			{6,1,11, 2}, {5,2,10, 2}, {6,4,8, 2}, // Back
    			{11,1,5, 1}, {9,3,6, 1}, {8,4,5, 1}, // Right
    			// Lower roof rim
    			{5,7,4, 2+4}, {6,7,4, 2+4}, {4,7,5, 1+4}, {4,7,6, 1+4}, {7,7,5, 0+4}, {6,7,7, 3+4}, 
    			// Upper roof rim
    			{4,8,4, 3}, {5,8,4, 3}, {6,8,4, 3}, {7,8,4, 3}, 
    			{4,8,6, 0}, {7,8,5, 1}, {7,8,6, 1}, 
    			{5,8,7, 2}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			{0,1,4}, {0,1,7}, 
    			{4,1,0}, {7,1,0}, 
    			{11,1,4}, {11,1,7}, 
    			{4,1,11}, {7,1,11}, 
    			})
    		{
    			this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// On fence posts
    			{0,2,4, -1}, {0,2,7, -1}, 
    			{4,2,0, -1}, {7,2,0, -1}, 
    			{11,2,4, -1}, {11,2,7, -1}, 
    			{4,2,11, -1}, {7,2,11, -1},
    			// On andesite ritual altar
    			{5,2,6, -1},
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Campfires
    		IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), this.getCoordBaseMode());
    		for(int[] uvw : new int[][]{
    			{5,5,6}, {6,5,6}, 
    			{5,5,5}, {6,5,5}, 
    			})
    		{
    			this.setBlockState(world, campfireState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    					
    		// Brewing stand
    		for (int[] uvw : new int[][]{
    			{6,2,5}, 
    			})
    		{
    			this.setBlockState(world, Blocks.BREWING_STAND.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			
    			int s = random.nextInt(12);
    			
    			int u = s<4 ? s+4 : s<6 ? 7 : s<10 ? 13-s : 4;
    			int v = 1;
    			int w = s<4 ? 4 : s<6 ? s+1 : s<10 ? 7 : 16-s;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 2, 1, 0); // Cleric
    			
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
    
    
    // --- Tool Smithy 1 --- //
    // designed by AstroTibs
    
    public static class JungleToolSmithy1 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"       ",
    			"FFFFFF ",
    			"FFFFFFF",
    			"FFFFFFF",
    			"FFFFFFF",
    			"FFFPPFF",
    			"FFFPPFF",
    			"FFFPPFF",
    			"FFFPPFF",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleToolSmithy1() {}

        public JungleToolSmithy1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleToolSmithy1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleToolSmithy1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Vertical
    			{1,1,0, 1,1,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			// Longer log
    			{2,1,0, 2,1,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
    		}
    		
    		
    		// For stripped logs specifically
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
    		for (int[] uuvvww : new int[][]{
    			{0,1,7, 0,2,7}, {5,1,7, 5,2,7}, 
    			{0,1,4, 0,4,4}, {5,1,4, 5,4,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Front wall
    			{1,1,4, 3,4,4}, {4,3,4, 4,4,4}, 
    			// Right wall
    			{5,1,5, 5,3,5}, {5,1,6, 5,2,6}, 
    			// Left wall
    			{0,1,5, 0,3,5}, {0,1,6, 0,2,6}, 
    			// Back wall
    			{1,1,7, 4,2,7}, {0,3,7, 5,3,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Counter
    			{0,0,4, 5,0,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Exterior
    			{4,3,3, 2}, 
    			// Interior
    			{2,3,5, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Front posts
    			{0,1,1, 0,2,1}, {5,1,1, 5,2,1}, 
    			// Awning scaffold
    			{0,3,2, 0,3,3}, {0,4,3, 0,4,3}, 
    			{5,3,2, 5,3,3}, {5,4,3, 5,4,3}, 
    			// Back posts
    			{0,2,8, 0,2,8}, {5,2,8, 5,2,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Front roof
    			{0,3,1, 3}, {1,3,1, 3}, {2,3,1, 3}, {3,3,1, 3}, {4,3,1, 3}, {5,3,1, 3}, 
    			{0,4,2, 3}, {1,4,2, 3}, {2,4,2, 3}, {3,4,2, 3}, {4,4,2, 3}, {5,4,2, 3}, 
    			{0,5,3, 3}, {1,5,3, 3}, {2,5,3, 3}, {3,5,3, 3}, {4,5,3, 3}, {5,5,3, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,5,4, 5,5,4}, 
    			{0,4,6, 5,4,6}, {0,3,6, 0,3,6}, {5,3,6, 5,3,6}, 
    			{0,3,8, 5,3,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,4,5, 5,4,5}, 
    			// Shelves
    			{1,1,5, 1,2,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		
    		// Smithing table
    		IBlockState smithingTableState = ModObjects.chooseModSmithingTable();
    		for (int[] uvw : new int[][]{
    			{2,1,5}, 
    			})
    		{
    			this.setBlockState(world, smithingTableState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,1,4, 0, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Coarse Dirt
    		IBlockState biomeCoarseDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getStateFromMeta(1), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,0,1, 2,0,3}, 
    			{5,0,1, 6,0,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCoarseDirtState, biomeCoarseDirtState, false);
    		}
    		
    		
    		// Gravel - NOT biome-modified
    		for (int[] uvw : new int[][]{
    			{6,1,0}, 
    			{6,1,4}, {6,1,5}, {6,1,6}, 
    			})
    		{
    			this.setBlockState(world, Blocks.GRAVEL.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Tall Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			{5,1,0, 0}, 
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
    		int chestU = 0;
    		int chestV = 1;
    		int chestW = 3;
    		int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof TileEntityChest)
        	{
        		ChestGenHooks chestGenHook = ChestGenHooks.getInfo(Reference.VN_TOOLSMITH);
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s = random.nextInt(3);
    			
    			// Villager
    			int u;
    			int v;
    			int w;
    			
    			if (s==0)
    			{
    				u = 2+random.nextInt(2);
    				v = 1;
    				w = 5+random.nextInt(2);
    			}
    			else
    			{
    				u = 2+random.nextInt(4);
    				v = 1;
    				w = 2+random.nextInt(2);
    			}
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 3, 0); // Tool Smith
    			
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
    
    
    // --- Tool Smithy 2 --- //
    // designed by AstroTibs
    
    public static class JungleToolSmithy2 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"    FFFF    ",
    			"    FFFF    ",
    			"    FFFF    ",
    			"   FFFFFF   ",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF",
    			"FFFFFFFFFFFF ",
    			"FFFFFFFFFFFF",
    			"   FFFFFF   ",
    			"    FFFF    ",
    			"    FFFF    ",
    			"    FFFF    ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 3; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1+2+4+8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleToolSmithy2() {}

        public JungleToolSmithy2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleToolSmithy2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleToolSmithy2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
        	
            // Stone brick part 1
        	IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Under steps
            	{5,3,9, 6,3,9}, 
            	{2,3,5, 2,3,6}, {9,3,5, 9,3,6}, 
            	{5,3,2, 6,3,2}, 
            	// Basement
            	{3,0,3, 4,0,8}, {5,0,3, 6,0,4}, {5,0,7, 6,0,8}, {7,0,3, 8,0,8}, 
            	{3,1,3, 3,4,8}, {4,1,3, 7,4,3}, {4,1,8, 7,4,8}, {8,1,3, 8,4,8}, 
            	// Temple
            	{3,5,4, 3,7,4}, {3,7,5, 3,7,6}, {3,5,7, 3,7,7}, 
            	{8,5,4, 8,7,4}, {8,7,5, 8,7,6}, {8,5,7, 8,7,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
            }
    		
        	
        	// Clear out basin
            for(int[] uuvvww : new int[][]{
    			{4,1,4, 7,3,7}, 
    			})
    		{
            	this.fillWithAir(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5]);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Upstairs
    			{4,6,7, 1}, 
    			// Downstairs
    			{6,3,7, 2}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
            // Stone brick part 2
            for (int[] uuvvww : new int[][]{
            	// Temple
            	{4,5,8, 4,7,8}, {5,7,8, 6,7,8}, {7,5,8, 7,7,8}, 
            	{4,5,3, 4,7,3}, {5,7,3, 6,7,3}, {7,5,3, 7,7,3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
            }
        	
            
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{7,4,5, 7,4,7}, 
    			{5,4,5, 6,4,6}, 
    			{4,4,4, 6,4,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Furnace plugs
    			{7,4,4, 7,4,4}, {7,8,4, 7,8,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
    		}
			
			
			// Cobblestone wall
			IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{7,9,4, 7,9,4}, 
				{7,5,4, 7,7,4}, 
				{7,2,4, 7,3,4}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
			}
    		
        	
        	// Stone Brick wall
    		IBlockState biomeStoneBrickWallStone = ModObjects.chooseModStoneBrickWallState();
        	biomeStoneBrickWallStone = StructureVillageVN.getBiomeSpecificBlockState(biomeStoneBrickWallStone!=null?biomeStoneBrickWallStone:Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Outside decoration
            	{3,5,8, 3,5,8}, {8,5,8, 8,5,8}, 
            	{3,5,3, 3,5,3}, {8,5,3, 8,5,3}, 
            	{5,8,8, 6,8,8}, 
            	{3,8,5, 3,8,6}, {8,8,5, 8,8,6}, 
            	{5,8,3, 6,8,3}, 
            	// Windows
            	{3,6,5, 3,6,5}, {3,5,6, 3,5,6}, 
            	{6,5,8, 6,5,8}, {5,6,8, 5,6,8}, 
            	{8,6,6, 8,6,6}, {8,5,5, 8,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickWallStone, biomeStoneBrickWallStone, false);
            }
        	
            
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	// Windows
            	{3,5,5, 3,5,5}, {3,6,6, 3,6,6}, 
            	{6,6,8, 6,6,8}, {5,5,8, 5,5,8}, 
            	{8,5,6, 8,5,6}, {8,6,5, 8,6,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            }
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Side steps
    			{5,3,1, 3}, {6,3,1, 3}, {5,4,2, 3}, {6,4,2, 3}, 
    			{1,3,5, 0}, {1,3,6, 0}, {2,4,5, 0}, {2,4,6, 0}, 
    			{10,3,5, 1}, {10,3,6, 1}, {9,4,5, 1}, {9,4,6, 1}, 
    			{5,3,10, 2}, {6,3,10, 2}, {5,4,9, 2}, {6,4,9, 2}, 
    			// Roof
    			{4,8,8, 2}, {7,8,8, 2}, 
    			{3,8,4, 0}, {3,8,7, 0}, 
    			{4,8,3, 3}, {7,8,3, 3}, 
    			{8,8,4, 1}, {8,8,7, 1}, 
    			// Downstairs
    			{6,4,7, 0}, {5,3,7, 0}, 
    			{4,2,6, 3}, {4,1,5, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Stone Brick Slab (upper)
    		IBlockState biomeStoneBrickSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(5+8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,8,4, 6,8,4},
    			{4,8,5, 4,8,7},
    			{5,8,7, 7,8,7},
    			{7,8,5, 7,8,6},
    			{4,2,7, 4,2,7},
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickSlabUpperState, biomeStoneBrickSlabUpperState, false);
    		}
    		
    		
    		// Stone Brick Slab (lower)
    		IBlockState biomeStoneBrickSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(5), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{5,9,5, 6,9,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickSlabLowerState, biomeStoneBrickSlabLowerState, false);
    		}
    		
    		
    		// Smithing table
    		IBlockState smithingTableState = ModObjects.chooseModSmithingTable();
    		for (int[] uvw : new int[][]{
    			{7,1,7}, 
    			})
    		{
    			this.setBlockState(world, smithingTableState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{5,5,3, 0, 1, 0}, {6,5,3, 0, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{7,1,4, 0}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		
        		{5,0,5, 2, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{6,0,5, 1, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{6,0,6, 0, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
        		{5,0,6, 3, GeneralConfig.useVillageColors ? this.townColor4 : 9}, // Cyan
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], StructureVillageVN.chooseGlazedTerracottaMeta(uvwoc[3], this.getCoordBaseMode()));
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s = random.nextInt(11);
    			
    			// Villager
    			int u = s<=0 ? 4 : s<=4 ? 5 : s<=8 ? 6 : 7;
    			int v = 1;
    			int w = s<=0 ? 4 : s<=4 ? s+3 : s<=8 ? s-1 : s-4;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 3, 0); // Tool Smith
    			
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
    
    
    // --- Weapon Smithy --- //
    // designed by Lonemind
    
    public static class JungleWeaponSmithy extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			" FFFFFFF",
    			" FFFFFFF",
    			" FFFPFFF",
    			"FFPFFFFF",
    			"FFFFPFFF",
    			"FPPPFPFF",
    			"FFFPFPFF",
    			"FFPFFFFF",
    			"FFFFFFFF",
    			" FFFFFFF",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleWeaponSmithy() {}

        public JungleWeaponSmithy(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleWeaponSmithy buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleWeaponSmithy(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
        	
            // Stone brick
        	IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Frame
            	{1,1,9, 1,2,9}, {5,1,9, 5,2,9}, 
            	{1,1,5, 1,2,5}, {5,1,5, 5,2,5}, 
            	{1,1,1, 1,2,1}, {5,1,1, 5,2,1}, 
            	// Upstairs
            	{1,5,9, 1,6,9}, {5,5,9, 5,6,9}, 
            	{1,5,5, 1,6,5}, {5,5,5, 5,6,5}, 
            	// Level divider
            	{2,4,1, 4,4,1}, 
            	{1,4,2, 5,4,7}, 
            	{1,4,8, 1,4,8}, {3,4,8, 5,4,8}, 
            	{1,4,9, 5,4,9}, 
            	// Roof
            	{2,8,5, 4,8,5}, 
            	{1,8,6, 1,8,8}, 
            	{2,8,9, 4,8,9}, 
            	{5,8,6, 5,8,8}, 
            	{3,9,7, 3,9,7}, 
            	// Basins
            	{6,0,6, 6,0,8}, {6,1,7, 6,1,7}, 
            	{7,1,5, 7,1,5}, {7,1,9, 7,1,9}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);
            }
    		
    		
    		// Stone Brick stairs
    		Block biomeStoneBrickStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Basin
    			{6,1,5, 3}, 
    			{5,1,6, 0}, {5,1,7, 0}, {5,1,8, 0}, 
    			{7,1,6, 1}, {7,1,7, 1}, {7,1,8, 1}, 
    			{6,1,9, 2}, 
    			// Roof
    			{2,9,8, 2}, {3,9,8, 2}, {4,9,8, 2}, 
    			{2,9,7, 0}, {4,9,7, 1}, 
    			{2,9,6, 3}, {3,9,6, 3}, {4,9,6, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeStoneBrickStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
            // Stone
        	IBlockState biomeStoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Building top
            	{2,5,5, 4,5,5}, {2,6,5, 2,6,5}, {4,6,5, 4,6,5}, {2,7,5, 4,7,5}, // Front window
            	{1,5,6, 1,5,8}, {1,6,6, 1,6,6}, {1,6,8, 1,6,8}, {1,7,6, 1,7,8}, // Left window
            	{2,5,9, 4,5,9}, {2,6,9, 2,6,9}, {4,6,9, 4,6,9}, {2,7,9, 4,7,9}, // Back window
            	{5,5,6, 5,5,8}, {5,6,6, 5,6,6}, {5,6,8, 5,6,8}, {5,7,6, 5,7,8}, // Left window
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneState, biomeStoneState, false);
            }
            
        	
            // Chiseled stone brick
        	IBlockState biomeChiseledStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwo : new int[][]{
            	// Front entrance
            	{1,3,9}, {5,3,9}, 
            	{1,3,5}, {5,3,5}, 
            	{1,3,1}, {5,3,1}, 
            	// Building top
            	{1,7,9}, {5,7,9}, 
            	{1,7,5}, {5,7,5}, 
        		})
            {
    			this.setBlockState(world, biomeChiseledStoneBrickState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,1,6, 1,3,8}, 
    			{2,1,9, 4,3,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvwo : new int[][]{
    			// Counter
    			{3,0,8}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneState, uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Outside table
    			{7,1,1, 2+4}, {7,1,4, 3+4}, 
    			// Inside table
    			{4,5,8, 1+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Table
    			{6,1,1, 6,1,1}, {7,1,2, 7,1,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
            
            
            // Grindstone
        	for (int[] uvwoh : new int[][]{ // u,v,w, orientation, isHanging
        		// Orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		{3,1,8, 2, 0}, 
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		IBlockState biomeGrindstoneState = ModObjects.chooseModGrindstone(uvwoh[3], this.getCoordBaseMode(), uvwoh[4]==1);
            	
        		this.setBlockState(world, biomeGrindstoneState, uvwoh[0], uvwoh[1], uvwoh[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{0,1,2, 1}, 
            	{0,1,3, 1}, 
            	{0,1,4, 1}, 
            	{0,2,3, 1}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
    		
    		
    		// Water
    		for(int[] uvw : new int[][]{
    			// Floor
    			{6,1,6},
    			})
    		{
                this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Lava
    		for(int[] uvw : new int[][]{
    			// Floor
    			{6,1,8},
    			})
    		{
                this.setBlockState(world, Blocks.LAVA.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LADDER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{2,1,8, 2,4,8, 2}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), false);
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		// Lower
        		{3,5,6, (GeneralConfig.useVillageColors ? this.townColor3 : 14)}, // Red
        		{4,5,6, (GeneralConfig.useVillageColors ? this.townColor : 0)}, // White
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
        	
        	// Stone Brick wall
    		IBlockState biomeStoneBrickWallStone = ModObjects.chooseModStoneBrickWallState();
        	biomeStoneBrickWallStone = StructureVillageVN.getBiomeSpecificBlockState(biomeStoneBrickWallStone!=null?biomeStoneBrickWallStone:Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{1,3,0, 1,3,0}, {5,3,0, 5,3,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickWallStone, biomeStoneBrickWallStone, false);
            }
        	
        	
    		// Polished Andesite Slab (Lower)
    		IBlockState biomePolishedAndesiteSlabBottomState = ModObjects.chooseModPolishedAndesiteSlabState(false);
    		if (biomePolishedAndesiteSlabBottomState==null) // Try brick slab
    		{
    			biomePolishedAndesiteSlabBottomState = ModObjects.chooseModAndesiteBrickSlabState(false);
        		if (biomePolishedAndesiteSlabBottomState==null) // Use basic polished stone slab
        		{
        			biomePolishedAndesiteSlabBottomState = Blocks.STONE_SLAB.getStateFromMeta(0);
        		};
    		};
    		biomePolishedAndesiteSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(biomePolishedAndesiteSlabBottomState, this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			{7,2,5}, {7,2,9}, 
    			})
    		{
    			this.setBlockState(world, biomePolishedAndesiteSlabBottomState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Coarse Dirt
    		IBlockState biomeCoarseDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getStateFromMeta(1), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvw : new int[][]{
    			{2,0,8}, {4,0,8}, 
    			{2,0,7}, {3,0,7}, 
    			{3,0,6}, {4,0,6}, 
    			{2,0,5}, {3,0,5}, 
    			{4,0,4}, 
    			{2,0,3}, 
    			{4,0,2}, 
    			{3,0,1}, 
    			})
    		{
    			this.setBlockState(world, biomeCoarseDirtState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,6,7}, 
        		{3,6,5}, 
        		{5,6,7}, 
        		{3,6,9}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{1,2,0}, 
            	{5,2,0}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	//{7,2,1}, 
            	{4,6,8}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s = random.nextInt(3);
    			
    			// Villager
    			int u = 2 + random.nextInt(3);
    			int v = 1;
    			int w = 1 + random.nextInt(7);
    			    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 2, 0); // Weapon Smith
    			
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
    
    
    // --- Wild Farm --- //
    // designed by Lonemind
    
    public static class JungleWildFarm extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"   FFF       ",
    			"  FFFFFF     ",
    			" FFFFFFFFF   ",
    			" FFFFFFFFF   ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFFF",
    			" FFFFFFFFFFFF",
    			" FFFFFFFFFFFF",
    			"FFFFFFFFFFFF ",
    			"FFFFFFFFFFF  ",
    			"FFFFFFFFFFF  ",
    			"      FFF    ",
    			"       FF    ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 4 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleWildFarm() {}

        public JungleWildFarm(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleWildFarm buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleWildFarm(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
        	
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,1,2, 1,2,2}, 
        		{1,1,3, 1,2,3}, 
        		{1,1,4, 1,3,4}, 
        		{2,1,5, 2,2,5}, 
        		{2,1,6, 2,1,6}, 
        		{2,1,7, 2,3,7}, 
        		{3,1,8, 3,1,8}, 
        		{2,1,9, 2,2,9}, 
        		{2,1,10, 2,1,10}, 
        		{3,1,11, 3,3,11}, 
        		{4,1,11, 4,1,11}, 
        		{7,1,10, 7,3,10}, 
        		{8,1,9, 8,2,9}, 
        		{9,1,9, 9,2,9}, 
        		{10,1,8, 10,1,8}, 
        		{11,1,7, 11,2,7}, 
        		{11,1,6, 11,1,6}, 
        		{11,1,5, 11,3,5}, 
        		{9,1,2, 9,3,2}, 
        		{8,1,1, 8,2,1}, 
        		{7,1,1, 7,3,1}, 
        		{6,1,1, 6,1,1}, 
        		{5,1,2, 5,2,2}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,3,2, 1,3,2}, 
        		{1,3,3, 1,3,3}, 
        		{1,4,4, 1,4,4}, 
        		{2,3,5, 2,3,5}, 
        		{2,2,6, 2,2,6}, 
        		{2,4,7, 2,4,7}, 
        		{3,2,8, 3,2,8}, 
        		{2,3,9, 2,3,9}, 
        		{2,2,10, 2,2,10}, 
        		{3,4,11, 3,4,11}, 
        		{4,2,11, 4,2,11}, 
        		{7,4,10, 7,4,10}, 
        		{8,3,9, 8,3,9}, 
        		{10,2,8, 10,2,8}, 
        		{11,3,7, 11,3,7}, 
        		{11,2,6, 11,2,6}, 
        		{11,4,5, 11,4,5}, 
        		{9,4,2, 9,4,2}, 
        		{8,3,1, 8,3,1}, 
        		{7,4,1, 7,4,1}, 
        		{6,2,1, 6,2,1}, 
        		{5,3,2, 5,3,2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
        	// Grass
        	for (int[] uuvvww : new int[][]{
        		{8,1,8, 8,1,8}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeGrassState,
        				biomeGrassState, 
        				false);
            }
        	
        	// Dirt
        	for (int[] uuvvww : new int[][]{
        		{6,0,2, 8,0,4}, {7,0,5, 7,0,5}, 
        		{8,0,8, 9,0,8}, {10,0,7, 10,0,7}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeDirtState,
        				biomeDirtState, 
        				false);
            }
            
        	// Coarse Dirt
    		IBlockState biomeCoarseDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getStateFromMeta(1), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
        		{6,1,2, 6,1,4}, 
        		{8,1,3, 8,1,4}, {9,1,3, 9,1,3}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeCoarseDirtState,
        				biomeCoarseDirtState, 
        				false);
            }
            
            // Melon 
            for(int[] uvw : new int[][]{
            	{2,1,2}, {2,1,3}, 
            	{7,1,9}, 
            	})
            {
            	this.setBlockState(world, Blocks.MELON_BLOCK.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            }
            
            // Moist Farmland with crop above
            for(int[] uvwfcp : new int[][]{
            	// u,v,w, farmland moisture (0:dry, 7:moist), crop (0:wheat, 1:potato, 2:carrot, 3:melon), crop progress
            	// Front left
            	{3,0,2, 7,3,7}, 
            	{3,0,3, 7,3,7}, 
            	{4,0,3, 7,0,4}, 
            	{4,0,4, 7,0,0}, 
            	{4,0,5, 7,0,7},
            	{5,0,4, 7,0,7},
            	// Back
            	{7,0,8, 7,3,7}, 
            	{7,0,7, 7,0,0}, 
            	{8,0,7, 7,0,5}, 
            	{9,1,8, 7,1,0}, 
            	{10,1,7, 7,1,0},
            	// Front right
            	{10,0,2, 7,2,4}, 
            	{10,0,3, 7,2,7}, 
            	{11,0,4, 7,2,0}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvwfcp[0], uvwfcp[1]-1, uvwfcp[2], structureBB);
            	//this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	this.setBlockState(world, (uvwfcp[4]==0?Blocks.WHEAT:uvwfcp[4]==1?Blocks.POTATOES:uvwfcp[4]==2?Blocks.CARROTS:Blocks.MELON_STEM).getStateFromMeta(uvwfcp[5]), uvwfcp[0], uvwfcp[1]+1, uvwfcp[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwfcp[3]), uvwfcp[0], uvwfcp[1], uvwfcp[2], structureBB); // 7 is moist
            }
    		
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			// Hidden under fence posts
    			{3,0,8, 3,0,8}, 
    			{4,0,11, 4,0,11},
    			// Fountain source
    			{8,1,2, 8,1,2}, 
    			// Fountain pool
    			{6,0,5, 8,0,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
    		}
    		
    		
    		// Attempt to add GardenCore Compost Bins. If this fails, do nothing
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            if (compostBinState != null)
            {
            	for(int[] uvw : new int[][]{
        			{6,1,6}, 
        			})
        		{
        			this.setBlockState(world, compostBinState, uvw[0], uvw[1], uvw[2], structureBB);
        			this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            // Sugarcane
    		for(int[] uuvvww : new int[][]{
    			// Back
    			{3,1,7, 3,1,7}, 
    			{3,1,9, 3,3,9}, 
    			{4,1,10, 4,1,10}, 
    			{5,1,11, 5,2,11}, 
    			// Front
    			{5,1,5, 5,1,5}, 
    			// On the coarse dirt
    			{6,2,2, 6,3,2}, 
    			{6,2,3, 6,2,3}, 
    			{8,2,3, 8,3,3}, 
    			{8,2,4, 8,2,4}, 
    			// Right
    			{9,1,5, 9,1,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.REEDS.getDefaultState(), Blocks.REEDS.getDefaultState(), false);
    		}
            
    		// Bamboo
    		IBlockState bambooStalkState = ModObjects.chooseModBambooStalk();
			if (bambooStalkState!=null)
    		{
				for (int[] uuvvww : new int[][]{
					{3,1,10, 3,8,10}, 
        			{4,1,8, 4,4,8}, 
        			{5,1,9, 5,2,9}, 
            		})
                {
					this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], bambooStalkState, bambooStalkState, false);
                }
				
				// Leaf toppers
				IBlockState bambooLeavesState = ModObjects.chooseModBambooLeaves();
				if (bambooLeavesState!=null)
	    		{
					for (int[] uuvvww : new int[][]{
						{3,9,10}, 
	        			{4,5,8}, 
	            		})
	                {
						this.setBlockState(world, bambooLeavesState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
	                }
	    		}
    		}
			else // As sugarcane
			{
				bambooStalkState = Blocks.REEDS.getDefaultState();

				// Add water to support the sugarcane
				for(int[] uvw : new int[][]{
	    			{3,0,11}, 
	    			})
	    		{
	    			this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
	    		}
				
				for (int[] uuvvww : new int[][]{
					{3,1,10, 3,4,10}, 
					{4,1,8, 4,2,8}, 
            		})
                {
					this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], bambooStalkState, bambooStalkState, false);
                }
			}
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{0,1,2, 3}, {0,2,2, 3}, 
        			{0,1,4, 3}, {0,2,4, 3}, {0,3,4, 3}, 
        			{1,1,5, 3}, 
        			{1,1,7, 3}, {1,2,7, 3}, {1,3,7, 3}, 
        			{1,1,9, 3}, {1,2,9, 3}, 
        			{2,2,11, 3}, {2,3,11, 3}, 
        			{6,1,10, 3}, {6,2,10, 3}, {6,3,10, 3}, 
        			// Backward
        			{7,1,0, 2}, {7,2,0, 2}, {7,3,0, 2}, 
        			{8,1,0, 2}, {8,2,0, 2}, 
        			{9,1,1, 2}, {9,2,1, 2}, 
        			{11,2,4, 2}, {11,3,4, 2}, 
        			// Forward
        			{3,1,12, 0}, {3,2,12, 0}, 
        			{7,1,11, 0}, {7,2,11, 0}, 
        			{8,2,10, 0}, 
        			{9,1,10, 0}, {9,2,10, 0}, 
        			// Rightward
        			{10,2,2, 1}, {10,3,2, 1}, 
        			{11,1,5, 1}, {11,2,5, 1}, 
        			{11,1,7, 1}, {11,2,7, 1}, 
        			{10,1,8, 1}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
        	
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s=random.nextInt(11);
				
    			int u = s<3 ? 3 : s<5 ? 4 : s<8 ? 5 : 6;
				int v=1;
    			int w = s<3 ? s+4 : s<5 ? s+3 : s<8 ? s+1 : s-1;
				
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0); // Farmer
    			
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
    
    
    // --- Wood Animal Pen --- //
    // designed by AstroTibs
    
    public static class JungleWoodAnimalPen extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
    			"     FFF   ",
    			"   FFFFFF  ",
    			"  FFFFFFFF ",
    			" FFFFFFFFF ",
    			" FFFFFFFFF ",
    			"FFFFFFFFFFF",
    			"FFFFFFFFFFF",
    			"FFFFFFFFFFF",
    			"FFFFFFFFFF ",
    			"   FFFFF   ",
    			"    FFF    ",
    			"     F     ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 3;
    	private static final int DECREASE_MAX_U = 3;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleWoodAnimalPen() {}

        public JungleWoodAnimalPen(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleWoodAnimalPen buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleWoodAnimalPen(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
			
			// Vertical logs
			IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{0,0,3, 0,1,3}, 
				{1,0,3, 1,0,3}, 
				{2,0,3, 2,1,3}, 
				{3,0,2, 3,1,2}, 
				{4,0,1, 6,1,1}, 
				{7,0,2, 7,1,2}, 
				{8,0,3, 8,0,3}, 
				{9,0,3, 9,1,3}, 
				{10,0,4, 10,1,4}, 
				{10,0,5, 10,0,5}, 
				{10,0,6, 10,1,6}, 
				{9,0,7, 9,1,7}, 
				{9,0,8, 9,0,8}, 
				{9,0,9, 9,1,9}, 
				{8,0,10, 8,1,10}, 
				{7,0,11, 7,1,11}, 
				{6,0,11, 6,0,11}, 
				{5,0,11, 5,1,11}, 
				{4,0,10, 4,1,10}, 
				{3,0,10, 3,0,10}, 
				{2,0,9, 2,1,9}, 
				{1,0,8, 1,1,8},  
				{1,0,7, 1,0,7}, 
				{0,0,6, 0,1,6}, 
				{0,0,4, 0,0,5}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
						biomeLogVertState,
						biomeLogVertState, 
						false);
			}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{4,3,1, 0}, {6,3,1, 1}, //{3,4,1, 0}, {7,4,1, 1}, 
    			{5,0,0, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
			
            
			// Fence
			IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{0,2,3, 0,2,7}, 
				{0,1,4, 0,1,5}, 
				{1,1,3, 1,1,3}, 
				{1,1,7, 1,1,7}, 
				{1,2,7, 1,2,9}, 
				{2,2,9, 2,2,10}, 
				{3,2,10, 4,2,10}, 
				{3,1,10, 3,1,10}, 
				{4,2,11, 8,2,11}, 
				{6,1,11, 6,1,11}, 
				{8,2,10, 9,2,10}, 
				{9,2,7, 9,2,9}, 
				{9,1,8, 9,1,8}, 
				{10,2,3, 10,2,7}, 
				{10,1,5, 10,1,5}, 
				{8,2,3, 9,2,3}, 
				{8,1,3, 8,1,3}, 
				{7,2,2, 8,2,2}, 
				{6,1,1, 6,1,1}, 
				{4,1,1, 4,1,1}, 
				{3,2,1, 4,2,1}, 
				{6,2,1, 7,2,1}, 
				{2,2,2, 3,2,2},
				{1,2,3, 2,2,3}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
			}
			
			
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// "Walls"
            	{5,3,1, 5,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
            }
            
            
        	// Fence Gate (Across)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{5,1,1}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(2, false)), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{2,3,3, -1}, {1,3,7, -1}, {4,3,10, -1}, {8,3,10, -1}, {9,3,7, -1}, {8,3,3, -1}, 
    			// Over entrance
    			{5,4,1, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
        	
        	// Grass blocks with grass patches atop
        	for (int[] uuvvww : new int[][]{
				{1,0,6, 1,0,6}, 
				{2,0,6, 2,0,8}, 
				{3,0,3, 7,0,9}, 
				{4,0,2, 6,0,2}, 
				{5,0,10, 7,0,10}, 
				{8,0,4, 8,0,9}, 
				{9,0,4, 9,0,6}, 
        	})
        	{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1]+1, uuvvww[2], uuvvww[3], uuvvww[4]+1, uuvvww[5], Blocks.TALLGRASS.getStateFromMeta(1), Blocks.TALLGRASS.getStateFromMeta(1), false);
        	}
			
			
			// Water
			for(int[] uuvvww : new int[][]{
				{1,0,4, 2,0,5}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
			}
			
			
        	// Set unkempt grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{6,1,10, 1}, {9,1,6, 1},
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
            
            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1)
            	{
	            	// Animals
	            	for (int[] uvw : new int[][]{
	        			{3,1,6}, {7,1,4}, {7,1,8}, 
	        			})
	        		{
	            		BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
	                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM); // Because horses can escape the pen
	                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
	                    world.spawnEntityInWorld(animal);
	                    
	                    // Dirt block underneath
	                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
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
    
    
    
    // ------------------ //
    // --- Road Decor --- //
    // ------------------ //

    
    // --- Road Decor --- //
    
    public static class JungleStreetDecor extends StructureVillageVN.VNComponent
    {
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = 3;
    	public static final int STRUCTURE_DEPTH = 3;
    	public static final int STRUCTURE_HEIGHT = 4;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleStreetDecor() {}

        public JungleStreetDecor(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }

        public static JungleStreetDecor buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new JungleStreetDecor(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
        		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
        				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
            
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
            			new    int[]{-2,-1,0,1,2,3}, // Values
            			new double[]{ 1, 2,3,8,4,2}, // Weights
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
            	
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvw[0], decorHeightY-2, uvw[2], structureBB);
            	this.setBlockState(world, biomeTopState, uvw[0], decorHeightY-1, uvw[2], structureBB);
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
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
            			)
            			).isNormalCube()
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
    
    
    // --- Road Well --- //
    // designed by AstroTibs
	
	public static class JungleRoadAccent1 extends StructureVillageVN.VNComponent
	{
		// Make foundation with blanks as empty air and F as foundation spaces
		private static final String[] foundationPattern = new String[]{
    			"PPPPP",
    			"PFFFP",
    			"PFFFP",
    			"PFFFP",
    			"PPPPP",
		};
		// Here are values to assign to the bounding box
		public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
		public static final int STRUCTURE_DEPTH = foundationPattern.length;
		public static final int STRUCTURE_HEIGHT = 5;
		// Values for lining things up
		private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
		private static final int W_OFFSET = -4; // How much to shift the well to ensure it is positioned onto the road
		public static final byte MEDIAN_BORDERS = 2 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
		private static final int INCREASE_MIN_U = 0;
		private static final int DECREASE_MAX_U = 0;
		private static final int INCREASE_MIN_W = 0;
		private static final int DECREASE_MAX_W = 0;
		
		private int averageGroundLevel = -1;
		
		public JungleRoadAccent1() {}
	
		public JungleRoadAccent1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
		{
			super();
			this.setCoordBaseMode(coordBaseMode);
			this.boundingBox = boundingBox;
			
			// Offset the bounding box to position it onto the street
			this.boundingBox.offset(
					this.getCoordBaseMode()==EnumFacing.WEST ? -W_OFFSET : this.getCoordBaseMode()==EnumFacing.EAST ? W_OFFSET : 0,
					0,
					this.getCoordBaseMode()==EnumFacing.SOUTH ? W_OFFSET : this.getCoordBaseMode()==EnumFacing.NORTH ? -W_OFFSET: 0);
			
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
		}
		
		public static JungleRoadAccent1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 
					0, 0, 0, 
					STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH+W_OFFSET, 
					coordBaseMode);
			
			// Bounding box on the other side of the road
			StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 
					0, -16, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH-W_OFFSET, 
					STRUCTURE_WIDTH, STRUCTURE_HEIGHT+16, -Reference.STREET_WIDTH-1, 
					coordBaseMode);
			
			return canVillageGoDeeper(structureboundingbox)
					&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
					&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
					? new JungleRoadAccent1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
									this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
									this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
							true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
					
					if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
					
					this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
				}
			}
			
			// In the event that this village construction is resuming after being unloaded
			// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
			IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			// Establish top and filler blocks, substituting Grass and Dirt if they're null
			IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
			IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
			
			// Decor - placed outside Bounding Box for simplicity's sake
			int[][] decorUVW = new int[][]{
				{-1,1,5}, 
				// Within the front face
				{5,1,-1}, 
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
					decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), 64, this.getZWithOffset(uvw[0], uvw[2]))).getY()-this.boundingBox.minY;
					this.decorHeightY.add(decorHeightY);
				}
				else
				{
					// There is already (presumably) a value for this ground level, so this decor is being multiply generated.
					// Retrieve ground level
					decorHeightY = this.decorHeightY.get(j);
				}
				
				//LogHelper.info("Decor with horizIndex "+this.coordBaseMode+" spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (decorHeightY + this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
				
				// Generate decor
				ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
				
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
			
			
			// Cobblestone
			IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for(int[] uuvvww : new int[][]{
				{1,0,3, 3,0,3}, 
				{1,0,2, 1,0,2}, {3,0,2, 3,0,2}, 
				{1,0,1, 3,0,1}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
			}
			
			
			// Vertical logs
			IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{2,1,1, 2,1,1}, 
				{2,1,3, 2,1,3}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
						biomeLogVertState,
						biomeLogVertState, 
						false);
			}
			
			
			// Cobblestone wall
			IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{1,1,1, 1,1,3}, {3,1,1, 3,1,3}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
			}
			
			
			// Fence
			IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{2,2,1, 2,3,1}, 
				{2,2,3, 2,3,3},  
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
			}
			
			
			// Wood stairs
			IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
				{1,3,3, 0}, {3,3,3, 1}, 
				{1,3,2, 0}, {3,3,2, 1}, 
				{1,3,1, 0}, {3,3,1, 1}, 
				})
			{
				this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
			}
			
			
			// Wooden slabs (Bottom)
			IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for(int[] uuvvww : new int[][]{
				{2,4,1, 2,4,3}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
			}
			
			
			// Water
			for(int[] uuvvww : new int[][]{
				{2,0,2, 2,0,2}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
			}
			
			
			// Villagers - treated here like a town center
			if (!this.entitiesGenerated)
			{
				this.entitiesGenerated=true;
				
				if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
				{
					for (int[] ia : new int[][]{
						{0, 1, 3, -1, 0},
						})
					{
						EntityVillager entityvillager = new EntityVillager(world);
						
						// Nitwits more often than not
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
		
		/**
		 * Returns the villager type to spawn in this component, based on the number
		 * of villagers already spawned.
		 */
		@Override
		protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
	}
    
    
    // --- Treehouse --- //
    // designed by AstroTibs and js2a98aj
    
    public static class JungleRoadAccent2 extends StructureVillageVN.VNComponent
    {
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
				"       ",
				"       ",
				"PPPPPPP",
				"PPPPPPP",
				"PPPPPPP",
				"  F    ",
				" FF    ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 13;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int W_OFFSET = -5; // How much to shift the well to ensure it is positioned onto the road
    	public static final byte MEDIAN_BORDERS = 2 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public JungleRoadAccent2() {}

        public JungleRoadAccent2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
    		super();
    		this.setCoordBaseMode(coordBaseMode);
    		this.boundingBox = boundingBox;
    		
    		// Offset the bounding box to position it onto the street
			this.boundingBox.offset(
					this.getCoordBaseMode()==EnumFacing.WEST ? -W_OFFSET : this.getCoordBaseMode()==EnumFacing.EAST ? W_OFFSET : 0,
					0,
					this.getCoordBaseMode()==EnumFacing.SOUTH ? W_OFFSET : this.getCoordBaseMode()==EnumFacing.NORTH ? -W_OFFSET: 0);
    		
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
        	
        	if (this.biome==null) {this.biome = start.worldChunkMngr.getBiomeGenerator(new BlockPos((boundingBox.minX + boundingBox.maxX)/2, 64, (boundingBox.minZ + boundingBox.maxZ)/2));}
        	if (this.materialType==null) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(this.biome);}
        	if (this.villageType==null) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(this.biome);}
        }
        
        public static JungleRoadAccent2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 
					0, 0, 0, 
					STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH+W_OFFSET, 
					coordBaseMode);
			
			// Bounding box on the other side of the road
			StructureBoundingBox structureBBOtherSide = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 
					0, -16, -Reference.STREET_WIDTH-1-STRUCTURE_DEPTH-W_OFFSET, 
					STRUCTURE_WIDTH, STRUCTURE_HEIGHT+16, -Reference.STREET_WIDTH-1, 
					coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox)
            		&& StructureComponent.findIntersecting(pieces, structureboundingbox) == null
            		&& StructureComponent.findIntersecting(pieces, structureBBOtherSide) == null
            		? new JungleRoadAccent2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.getCoordBaseMode().getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.getCoordBaseMode().getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.getCoordBaseMode().getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.getCoordBaseMode().getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
			}
    		
    		// In the event that this village construction is resuming after being unloaded
    		// you may need to reestablish the village name/color/type info
        	this.populateVillageFields(world);
        	
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear space above
        	this.clearSpaceAbove(world, structureBB, this.STRUCTURE_WIDTH, this.STRUCTURE_DEPTH, this.GROUND_LEVEL);
            
            // Follow the blueprint to set up the starting foundation
            this.establishFoundation(world, structureBB, this.foundationPattern, this.GROUND_LEVEL, this.materialType, this.disallowModSubs, this.biome, biomeTopState, biomeFillerState);
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Roof support
        		{1,6,5, 1,6,5}, 
        		{1,6,0, 1,6,0}, {5,6,0, 5,6,0}, 
        		// Underneath logs
        		{1,3,5, 1,3,5}, {5,3,0, 5,3,0}, 
        		// Torch posts
        		{0,5,1, 0,5,1}, {6,5,5, 6,5,5}, {5,5,6, 5,5,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
    		
    		
    		// Torches on posts
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{0,6,1, -1}, {6,6,5, -1}, {5,6,6, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
        	
        	// Vertical logs - can not be sandstone
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), materialType, biome, disallowModSubs);
        	// Make sure this is a log (or mushroom stem)
        	biomeLogVertState = biomeLogVertState.getBlock()==Blocks.SANDSTONE ? Blocks.LOG.getStateFromMeta(3) : biomeLogVertState; // Force to be jungle log in a desert
        	for (int[] uuvvww : new int[][]{
        		{1,4,5, 1,5,5}, {5,1,5, 5,11,5}, 
        		{1,1,1, 1,8,1}, {5,4,0, 5,5,0}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLogVertState,
        				biomeLogVertState, 
        				false);
            }
    		// Foot foundation
    		for(int[] uvw : new int[][]{
    			// Feet
    			{5,0,5}, 
    			{1,0,1}, 
    			})
    		{
    			this.replaceAirAndLiquidDownwards(world, biomeLogVertState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Torches on logs
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{5,2,4, 2}, 
    			}) {
    			this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LEAVES.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		// Front tree
        		{0,8,-1, 2,9,3}, {-1,8,0, -1,9,2}, {3,8,0, 3,9,2}, 
        		{1,10,0, 1,11,2}, {0,10,1, 0,11,1}, {2,10,1, 2,11,1}, {2,10,2, 2,10,2}, 
        		// Back tree
        		{4,9,3, 6,10,7}, {3,9,4, 3,10,6}, {7,9,3, 7,10,6}, 
        		{5,11,4, 5,12,6}, {4,11,5, 4,12,5}, {6,11,5, 6,12,5}, {6,11,4, 6,11,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,4,0, 1,5,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Back roof trim
        		{2,7,6, 2}, {4,7,6, 2}, 
        		// Left roof trim
        		{0,7,2, 0}, {0,7,4, 0}, 
        		// Right roof trim
        		{6,7,1, 1}, {6,7,3, 1}, 
        		// Front roof trim
        		{2,7,-1, 3}, {4,7,-1, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// "Walls"
            	{2,4,0, 4,4,0}, // Front
            	{2,4,5, 4,4,5}, // Back
            	{1,4,2, 1,4,4}, // Left
            	{5,4,1, 5,4,4}, // Right
            	// Roof
            	{1,7,0, 5,7,0}, // Front
            	{1,7,5, 4,7,5}, // Back
            	{1,7,2, 1,7,4}, // Left
            	{5,7,1, 5,7,4}, // Right
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// "Walls"
            	{2,5,0, 4,5,0}, // Front
            	{2,5,5, 4,5,5}, // Back
            	{1,5,2, 1,5,4}, // Left
            	{5,5,1, 5,5,4}, // Right
            	// Roof
            	{2,7,1, 4,7,4}, 
            	// Floor
            	{2,3,2, 4,3,4}, 
            	{3,3,1, 4,3,1}, 

            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
            }
            
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LADDER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{2,1,1, 2,3,1, 1},    
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), false);
            }
            
            
            // Trapdoor (Bottom Horizontal)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,4,1, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, false)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
        	// Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CRAFTING_TABLE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{4,4,4}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 4;
        	int chestW = 3;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
			
        	// Grass blocks
        	for (int[] grass_uw : new int[][]{
        		{2,0,1}, 
        		{1,0,0}, {2,0,0}, 
        	})
        	{
        		this.setBlockState(world, biomeGrassState, grass_uw[0], grass_uw[1], grass_uw[2], structureBB);
        	}
        	
        	
        	// Flower
            for (int[] uvw : new int[][]{
            	{1,1,0}, 
            })
            {
            	this.setBlockState(world, (random.nextBoolean()?Blocks.RED_FLOWER:Blocks.YELLOW_FLOWER).getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
        	// Patterned banners
//			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
//				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
//				
//				{0,5,5, 3, 1}, // Orange
//			})
//			{
//    			int bannerXBB = uvwoc[0];
//    			int bannerYBB = uvwoc[1];
//    			int bannerZBB = uvwoc[2];
//    			
//    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
//    			int bannerY = this.getYWithOffset(bannerYBB);
//                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
//                
//                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
//                
//            	// Set the banner and its orientation
//				world.setBlockState(bannerPos, Blocks.WALL_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.getCoordBaseMode().getHorizontalIndex(), true)), 2);
//				
//				// Set the tile entity
//				TileEntity tilebanner = new TileEntityBanner();
//				NBTTagCompound modifystanding = new NBTTagCompound();
//				tilebanner.writeToNBT(modifystanding);
//				modifystanding.setBoolean("IsStanding", false);
//				
//				if (GeneralConfig.useVillageColors)
//				{
//	            	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world, 
//	            			(this.boundingBox.minX+this.boundingBox.maxX)/2,
//	            			(this.boundingBox.minY+this.boundingBox.maxY)/2,
//	            			(this.boundingBox.minZ+this.boundingBox.maxZ)/2);
//					
//    				tilebanner.readFromNBT(modifystanding);
//    				ItemStack villageBanner = new ItemStack(Items.BANNER);
//    				villageBanner.setTagInfo("BlockEntityTag", villageNBTtag.getCompoundTag("BlockEntityTag"));
//    				
//        			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
//				}
//				else
//				{
//					modifystanding.setInteger("Base", 15 - uvwoc[4]);
//    				tilebanner.readFromNBT(modifystanding);
//				}
//				
//        		world.setTileEntity(bannerPos, tilebanner);
//			}
    		
    		
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Leftward
        			{0,1,1, 3}, {0,2,1, 3}, {0,3,1, 3}, {0,4,1, 3}, {0,7,1, 3}, 
        			// Backward
        			{1,4,-1, 2}, {1,5,-1, 2}, 
        			// Forward
        			{5,8,6, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
    
    
	
    // ------------------- //
    // --- Biome Decor --- //
    // ------------------- //
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
	public static ArrayList<BlueprintData> getRandomJungleDecorBlueprint(VillageType villageType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 8;
		return getJungleDecorBlueprint(random.nextInt(decorCount), villageType, materialType, disallowModSubs, biome, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getJungleDecorBlueprint(int decorType, VillageType villageType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		// Generate per-material blocks
		
    	// Establish top and filler blocks, substituting Grass and Dirt if they're null
		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), materialType, biome, disallowModSubs);
		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), materialType, biome, disallowModSubs);
		IBlockState biomeTopState=biomeGrassState; if (biome!=null && biome.topBlock!=null) {biomeTopState=biome.topBlock;}
		IBlockState biomeFillerState=biomeDirtState; if (biome!=null && biome.fillerBlock!=null) {biomeFillerState=biome.fillerBlock;}
		
    	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), materialType, biome, disallowModSubs);
    	IBlockState biomeChiseledStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(3), materialType, biome, disallowModSubs);
    	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
    	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
    	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), materialType, biome, disallowModSubs);
    	
    	
		boolean genericBoolean=false;
    	int genericInt=0;
		
        switch (decorType)
        {
    	case 0: // Hay bales by Roadhog360 and AstroTibs
    		
    		IBlockState hayBlockstate = Blocks.HAY_BLOCK.getStateFromMeta(0); // Meta: 0 for vertical, 4 for E/W, 8 for N/S
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, hayBlockstate);
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		
    		switch (random.nextInt(4))
    		{
    		case 0: // Facing you
    			// Bale in center
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), false));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    			// Bale in front
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), false));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeFillerState); // Foundation
    			// Back-left corner
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 1, hayBlockstate);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeFillerState); // Foundation
    			// Back-right corner
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 1, hayBlockstate);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 1, biomeFillerState); // Foundation
    			// Right side
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), false));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeFillerState); // Foundation
    			break;
    		case 1: // Facing left
    			// Bale in center
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), true));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    			// Bale in front
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), true));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeFillerState); // Foundation
    			// Back-left corner
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 1, hayBlockstate);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 1, biomeFillerState); // Foundation
    			// Back-right corner
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, -1, hayBlockstate);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, -1, biomeFillerState); // Foundation
    			// Right side
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), true));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeFillerState); // Foundation
    			break;
    		case 2: // Facing away
    			// Bale in center
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), false));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    			// Bale in front
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), false));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeFillerState); // Foundation
    			// Back-left corner
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, -1, hayBlockstate);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, -1, biomeFillerState); // Foundation
    			// Back-right corner
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, -1, hayBlockstate);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, -1, biomeFillerState); // Foundation
    			// Right side
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), false));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeFillerState); // Foundation
    			break;
    		case 3: // Facing right
    			// Bale in center
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), true));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    			// Bale in front
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), true));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeFillerState); // Foundation
    			// Back-left corner
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, -1, hayBlockstate);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, -1, biomeFillerState); // Foundation
    			// Back-right corner
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 1, hayBlockstate);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeFillerState); // Foundation
    			// Right side
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, StructureVillageVN.getHorizontalPillarState(hayBlockstate, coordBaseMode.getHorizontalIndex(), true));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeFillerState); // Foundation
    			break;
    		}
    		break;

    	case 1: // Campfire on cobblestone by jss2a98aj
        	IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), coordBaseMode);
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeFillerState); // Foundation
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeFillerState); // Foundation
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeCobblestoneState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 1, 0, 0, biomeCobblestoneState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, -1, 0, 0, biomeCobblestoneState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 1, biomeCobblestoneState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, -1, biomeCobblestoneState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 1, 0, campfireState);
    		break;
    		
    	case 2: // Fence post hanging lantern on stone brick foundation by jss2a98aj
    		// Central column
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeCobblestoneState);
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeChiseledStoneBrickState);
    		BlueprintData.addPlaceBlock(blueprint, 0, 2, 0, biomeFenceState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 3, 0, biomeFenceState);
    		
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 3, -1, biomeFenceState); // Fence post
    		BlueprintData.addPlaceBlock(blueprint, 0, 2, -1, biomeHangingLanternState);
    		
    		// Vines
        	if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP)
        	{ // 0 and 2 are ok
        		for (int[] xyzo : new int[][]{ // Orientation - 1:north, 2:east, 4:south, 8:west
        			{0,0,-1, StructureVillageVN.chooseVineMeta(2)}, // Front
        			{1,0,0, StructureVillageVN.chooseVineMeta(1)}, // Right
        			{-1,0,0, StructureVillageVN.chooseVineMeta(3)}, // Left
            		})
                {
        			BlueprintData.addPlaceBlock(blueprint, xyzo[0], xyzo[1], xyzo[2], Blocks.VINE.getStateFromMeta(xyzo[3]));
                }
        	}
    		break;
    		
    	case 3: // Lantern on stone brick post by jss2a98aj
    		// Central column
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeCobblestoneState);
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeChiseledStoneBrickState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 2, 0, biomeSittingLanternState);
    		
    		// Vines
    		switch (random.nextInt(4))
    		{
    		case 0: // Facing you
        		if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP)
            	{
            		for (int[] xyzo : new int[][]{ // Orientation - 1:north, 2:east, 4:south, 8:west
            			{0,0,-1, StructureVillageVN.chooseVineMeta(2)}, {0,1,-1, StructureVillageVN.chooseVineMeta(2)}, // Front
            			{1,0,0, StructureVillageVN.chooseVineMeta(1)}, // Right
            			{0,0,1, StructureVillageVN.chooseVineMeta(0)}, // Back
            			{-1,1,0, StructureVillageVN.chooseVineMeta(3)}, // Left
                		})
                    {
            			BlueprintData.addPlaceBlock(blueprint, xyzo[0], xyzo[1], xyzo[2], Blocks.VINE.getStateFromMeta(xyzo[3]));
                    }
            	}
        		break;
        		
    		case 1: // Facing left
    			if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP)
            	{
            		for (int[] xyzo : new int[][]{ // Orientation - 1:north, 2:east, 4:south, 8:west
            			{-1,0,0, StructureVillageVN.chooseVineMeta(3)}, {-1,1,0, StructureVillageVN.chooseVineMeta(3)}, // Front
            			{0,0,-1, StructureVillageVN.chooseVineMeta(2)}, // Right
            			{1,0,0, StructureVillageVN.chooseVineMeta(1)}, // Back
            			{0,1,1, StructureVillageVN.chooseVineMeta(0)}, // Left
                		})
                    {
            			BlueprintData.addPlaceBlock(blueprint, xyzo[0], xyzo[1], xyzo[2], Blocks.VINE.getStateFromMeta(xyzo[3]));
                    }
            	}
            	break;
            	
    		case 2: // Facing away
    			if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP)
            	{
            		for (int[] xyzo : new int[][]{ // Orientation - 1:north, 2:east, 4:south, 8:west
            			{0,0,1, StructureVillageVN.chooseVineMeta(0)}, {0,1,1, StructureVillageVN.chooseVineMeta(0)}, // Front
            			{-1,0,0, StructureVillageVN.chooseVineMeta(3)}, // Right
            			{0,0,-1, StructureVillageVN.chooseVineMeta(2)}, // Back
            			{1,1,0, StructureVillageVN.chooseVineMeta(1)}, // Left
                		})
                    {
            			BlueprintData.addPlaceBlock(blueprint, xyzo[0], xyzo[1], xyzo[2], Blocks.VINE.getStateFromMeta(xyzo[3]));
                    }
            	}
    			break;
    			
    		case 3: // Facing right
    			if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP)
            	{
            		for (int[] xyzo : new int[][]{ // Orientation - 1:north, 2:east, 4:south, 8:west
            			{1,0,0, StructureVillageVN.chooseVineMeta(1)}, {1,1,0, StructureVillageVN.chooseVineMeta(1)}, // Front
            			{0,0,1, StructureVillageVN.chooseVineMeta(0)}, // Right
            			{-1,0,0, StructureVillageVN.chooseVineMeta(3)}, // Back
            			{0,1,-1, StructureVillageVN.chooseVineMeta(2)}, // Left
                		})
                    {
            			BlueprintData.addPlaceBlock(blueprint, xyzo[0], xyzo[1], xyzo[2], Blocks.VINE.getStateFromMeta(xyzo[3]));
                    }
            	}
    			break;
    		}
    		break;
    		
    	case 4: // Stone post with lanterns hanging from walls by jss2a98aj
    		// Central column
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addFillWithBlocks(blueprint, 0,0,0, 0,2,0, biomeCobblestoneState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 3, 0, biomeChiseledStoneBrickState);
    		
    		// Belt of vines
    		if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		BlueprintData.addPlaceBlock(blueprint, 0, 1, 1, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(0)));
        		BlueprintData.addPlaceBlock(blueprint, 1, 1, 0, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(1)));
        		BlueprintData.addPlaceBlock(blueprint, 0, 1, -1, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(2)));
        		BlueprintData.addPlaceBlock(blueprint, -1, 1, 0, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(3)));
        	}
    		
    		genericInt = random.nextInt(4);
    		
    		// Hanging lanterns
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getDefaultState(), materialType, biome, disallowModSubs);
        	
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, genericInt%2==0?-1:0, 3, genericInt%2==0?0:-1, biomeCobblestoneWallState);
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, genericInt%2==0?1:0, 3, genericInt%2==0?0:1, biomeCobblestoneWallState);
			BlueprintData.addPlaceBlock(blueprint, genericInt%2==0?-1:0, 2, genericInt%2==0?0:-1, biomeHangingLanternState);
			BlueprintData.addPlaceBlock(blueprint, genericInt%2==0?1:0, 2, genericInt%2==0?0:1, biomeHangingLanternState);
    		
			// Vines
    		if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		BlueprintData.addPlaceBlock(blueprint, genericInt%2!=0?-1:0, 2, genericInt%2!=0?0:-1, Blocks.VINE.getStateFromMeta(genericInt%2!=0?StructureVillageVN.chooseVineMeta(3):StructureVillageVN.chooseVineMeta(2)));
        		BlueprintData.addPlaceBlock(blueprint, genericInt%2!=0?1:0, 2, genericInt%2!=0?0:1, Blocks.VINE.getStateFromMeta(genericInt%2!=0?StructureVillageVN.chooseVineMeta(1):StructureVillageVN.chooseVineMeta(0)));
        		
        		BlueprintData.addPlaceBlock(blueprint, genericInt==1?-1:genericInt==3?1:0, 3, genericInt==0?-1:genericInt==2?1:0, Blocks.VINE.getStateFromMeta(genericInt==0?StructureVillageVN.chooseVineMeta(2):genericInt==1?StructureVillageVN.chooseVineMeta(3):genericInt==2?StructureVillageVN.chooseVineMeta(0):StructureVillageVN.chooseVineMeta(1)));
        	}
    		
    		break;
    		
    	case 5: // Lantern on a stump by jss2a98aj
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, biomeLogVertState);
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeSittingLanternState);
			
    		break;
    		
    	case 6: // Lantern on a log by jss2a98aj
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), true);
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), false);
    		
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		
    		switch (random.nextInt(4))
    		{
    		case 0: // Facing you
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeFillerState); // Foundation
    			BlueprintData.addFillWithBlocks(blueprint, 0,0,0, 1,0,0, biomeLogHorAcrossState);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeFillerState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 1, biomeLogVertState);
    			// Vines
    			if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP) // Orientation - 1:north, 2:east, 4:south, 8:west
            	{
            		BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(2))); // Front
            		BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(1))); // Right
            	}
    			break;
    			
    		case 1: // Facing left
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeFillerState); // Foundation
    			BlueprintData.addFillWithBlocks(blueprint, 0,0,-1, 0,0,0, biomeLogHorAlongState);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 1, biomeFillerState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 1, biomeLogVertState);
    			// Vines
    			if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP) // Orientation - 1:north, 2:east, 4:south, 8:west
            	{
            		BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(3))); // Front
            		BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(2))); // Right
            	}
    			break;
    			
    		case 2: // Facing away
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeFillerState); // Foundation
    			BlueprintData.addFillWithBlocks(blueprint, -1,0,0, 0,0,0, biomeLogHorAcrossState);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, -1, biomeFillerState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, -1, biomeLogVertState);
    			// Vines
    			if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP) // Orientation - 1:north, 2:east, 4:south, 8:west
            	{
            		BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(0))); // Front
            		BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(3))); // Right
            	}
    			break;
    			
    		case 3: // Facing right
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeFillerState); // Foundation
    			BlueprintData.addFillWithBlocks(blueprint, 0,0,0, 0,0,1, biomeLogHorAlongState);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, -1, biomeFillerState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, -1, biomeLogVertState);
    			// Vines
    			if (villageType==FunctionsVN.VillageType.JUNGLE || villageType==FunctionsVN.VillageType.SWAMP) // Orientation - 1:north, 2:east, 4:south, 8:west
            	{
            		BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(1))); // Front
            		BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, Blocks.VINE.getStateFromMeta(StructureVillageVN.chooseVineMeta(0))); // Right
            	}
    			break;
    		}
    		
    		// Lantern
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeSittingLanternState);
    		
    		break;
    		
    	case 7: // Lantern between two posts by jss2a98aj
    		
    		int vertOffset = -1;
    		
    		BlueprintData.addFillBelowTo(blueprint, 0, -1+vertOffset, 0, biomeFillerState); // Foundation
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0+vertOffset, 0, biomeCobblestoneState);
    		
    		genericBoolean = random.nextBoolean();
    		
    		// Foundations
    		BlueprintData.addFillBelowTo(blueprint, genericBoolean?-1:0, -1+vertOffset, genericBoolean?0:-1, biomeFillerState);
    		BlueprintData.addFillBelowTo(blueprint, genericBoolean?1:0, -1+vertOffset, genericBoolean?0:1, biomeFillerState);
    		// Cobblestone
    		BlueprintData.addPlaceBlock(blueprint, genericBoolean?-1:0, 0+vertOffset, genericBoolean?0:-1, biomeCobblestoneState);
    		BlueprintData.addPlaceBlock(blueprint, genericBoolean?1:0, 0+vertOffset, genericBoolean?0:1, biomeCobblestoneState);
    		// Fence posts
    		BlueprintData.addFillWithBlocks(blueprint, genericBoolean?-1:0,1+vertOffset,genericBoolean?0:-1, genericBoolean?-1:0,3+vertOffset,genericBoolean?0:-1, biomeFenceState);
    		BlueprintData.addFillWithBlocks(blueprint, genericBoolean?1:0,1+vertOffset,genericBoolean?0:1, genericBoolean?1:0,3+vertOffset,genericBoolean?0:1, biomeFenceState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, genericBoolean?-1:0, 3+vertOffset, genericBoolean?0:-1, biomeFenceState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, genericBoolean?1:0, 3+vertOffset, genericBoolean?0:1, biomeFenceState);
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 3+vertOffset, 0, biomeFenceState);
    		// Lantern
    		BlueprintData.addPlaceBlock(blueprint, 0, 2+vertOffset, 0, biomeHangingLanternState);
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
