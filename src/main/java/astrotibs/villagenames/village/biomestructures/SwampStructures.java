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
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
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
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.ChestGenHooks;

public class SwampStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Swamp Willow --- //
	// designed by AstroTibs
    
    public static class SwampWillow extends StartVN
    {
        // Make foundation with blanks as empty air, F as foundation spaces, and P as path
        private static final String[] foundationPattern = new String[]{
            	"F PPPPP F",
            	" PPFFFPP ",
            	"PPFFFFFPP",
            	"PFFFFFFFP",
            	"PFFFFFFFP",
            	"PFFFFFFFP",
            	"PPFFFFFPP",
            	" PPFFFPP ",
            	"F PPPPP F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 13;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2 + 4 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
	    public SwampWillow() {}
		
		public SwampWillow(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, float villageSize)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, villageSize);
    		
		    // Establish orientation and bounding box
    		this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode)
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
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
			if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
        				true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
            
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
        	
        	WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{1,0,3, 1,0,5}, 
            	{2,0,2, 2,0,3}, {2,0,5, 2,0,5}, 
            	{3,0,1, 5,0,2}, {3,0,7, 5,0,7}, 
            	{6,0,2, 6,0,3}, {6,0,6, 6,0,6}, 
            	{7,0,3, 7,0,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
            }
        	
        	
        	// Mossy Cobblestone
        	IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{2,0,4, 2,0,4}, {2,0,6, 5,0,6}, {6,0,4, 6,0,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);
            }
        	
        	
        	// For stripped logs specifically
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStrippedLogVertState = biomeLogVertState;
        	// Try to see if stripped logs exist
        	biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
        	for (int[] uuvvww : new int[][]{
        		{3,0,5, 3,1,5}, {4,0,5, 4,2,5}, {5,0,5, 5,0,5}, 
        		{3,0,4, 3,3,4}, {4,0,4, 4,9,4}, {5,0,4, 5,2,4}, 
        		{3,0,3, 3,1,3},                 {5,0,3, 5,1,3}, 
        		// Everything above the base
        		{4,10,5, 4,10,5}, {3,11,4, 3,11,4}, 
        		{6,7,4, 6,8,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
            }
        	
        	
        	// Stripped Wood
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	if (biomeLogVertState.getBlock() == Blocks.log)
        	{
        		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
        	}
        	else if (biomeLogVertState.getBlock() == Blocks.log2)
        	{
        		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
        	}
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.log || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.log2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.log)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.log2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	{3,2,5}, {4,3,5}, {5,1,5}, 
            	{3,4,4},          {5,3,4}, 
            	{3,2,3}, {4,0,3}, {4,2,3}, {4,3,3}, {5,2,3}, 
            	})
            {
            	this.setBlockState(world, biomeStrippedWoodOrLogOrLogVerticState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);	
            }
            
            
        	// Stripped Log (Across)
            IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true);
        	IBlockState biomeStrippedLogHorizAcrossState = biomeLogHorAcrossState;
        	// Try to see if stripped logs exist
        	biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            for(int[] uuvvww : new int[][]{
            	{2,7,4, 3,7,4}, 
            	{5,6,4, 5,6,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
            
            // Oak Leaves
        	for (int[] uuvvww : new int[][]{
        		{1,5,3, 1,7,3}, 
        		{1,7,4, 1,7,4}, {1,11,4, 1,11,4}, 
        		{1,6,5, 1,8,5}, 
        		
        		{2,7,3, 2,7,3}, {2,11,3, 2,11,3}, 
        		{2,8,4, 2,8,4}, {2,10,4, 2,12,4}, 
        		{2,7,5, 2,8,5}, {2,11,5, 2,11,5}, 
        		{2,4,6, 2,7,6}, 
        		
        		{3,4,2, 3,5,2}, {3,8,2, 3,10,2}, 
        		{3,5,3, 3,6,3}, {3,9,3, 3,11,3}, 
        		{3,10,4, 3,10,4}, {3,12,4, 3,12,4}, 
        		{3,6,5, 3,6,5}, {3,10,5, 3,12,5}, 
        		{3,9,6, 3,10,6}, 
        		
        		{4,8,3, 4,9,3}, {4,11,3, 4,11,3}, 
        		{4,10,4, 4,11,4}, 
        		{4,6,5, 4,7,5}, {4,9,5, 4,9,5}, {4,11,5, 4,11,5}, 
        		{4,5,6, 4,11,6}, 
        		
        		{5,5,2, 5,9,2}, 
        		{5,6,3, 5,6,3}, {5,9,3, 5,10,3}, 
        		{5,9,4, 5,9,4}, 
        		{5,6,5, 5,6,5}, {5,10,5, 5,11,5}, 
        		{5,8,6, 5,10,6}, 
        		{5,4,7, 5,9,7}, 
        		
        		{6,3,1, 6,7,1}, 
        		{6,6,2, 6,9,2}, 
        		{6,4,3, 6,10,3}, 
        		{6,6,4, 6,6,4}, {6,9,4, 6,9,4}, 
        		{6,3,5, 6,7,5}, 
        		
        		{7,7,2, 7,8,2}, 
        		{7,2,3, 7,9,3}, 
        		{7,5,4, 7,7,4}, 
        		{7,7,5, 7,7,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.leaves.getStateFromMeta(4),
        				Blocks.leaves.getStateFromMeta(4), 
        				false);
            }
            
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Left side
        			{3,5,4, 3}, {3,6,4, 3}, {3,8,4, 3}, {3,9,4, 3}, 
        			// Back side
        			{4,4,5, 0}, {4,5,5, 0}, {4,8,5, 0}, {6,8,5, 0}, 
        			// Right side
        			{5,5,4, 1}, {5,7,4, 1}, {5,8,4, 1}, {7,8,4, 1}, 
        			// Front side
        			{4,4,3, 2}, {4,5,3, 2}, {4,6,3, 2}, {4,7,3, 2}, {6,4,2, 2}, {6,5,2, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);	
        			}
                }
        	}
            
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{0,1,0}, 
            	{8,1,0}, 
            	{0,1,0}, 
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
            IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            if (GeneralConfig.nameSign)
            {
            	int signU = 3;
    			int signV = 1;
    			int signW = 2;
    			int signO = 8;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
                boolean hanging=false;
                
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);

    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.coordBaseMode.getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
        	
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerU = 4;
    			int bannerV = 3;
    			int bannerW = 2;
    			int bannerO = 2; // Facing toward you
    			boolean hanging=true;
    			
    			int bannerX = this.getXWithOffset(bannerU, bannerW);
    			int bannerY = this.getYWithOffset(bannerV);
                int bannerZ = this.getZWithOffset(bannerU, bannerW);

                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);

            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.wall_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(bannerO, this.coordBaseMode.getHorizontalIndex(), true)), 2);
				
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
					modifystanding.setInteger("Base", 15 - 15);
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
	        			{1, 1, 3, -1, 0}, 
	        			{2, 1, 8, -1, 0}, 
	        			{7, 1, 7, -1, 0}, 
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
	        			if (GeneralConfig.enableNitwit && random.nextInt(3)==0) {entityvillager.setProfession(5);}
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
    
    
    
	// --- Swamp Statue --- //
	// designed by AstroTibs
    
    public static class SwampStatue extends StartVN
    {
        // Make foundation with blanks as empty air, F as foundation spaces, and P as path
        private static final String[] foundationPattern = new String[]{
            	" F  PFF   ",
            	"    FPP  F",
            	"   PPPP   ",
            	"F PFFFFPF ",
            	"PPPFFFFPP ",
            	"PPPFFFFFPP",
            	"PPFFFFFFPP",
            	"  FFPPPPPP",
            	"F FFFPPF  ",
            	"    PPP  F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2 + 4 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
	    public SwampStatue() {}
		
		public SwampStatue(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, float villageSize)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, villageSize);
    		
		    // Establish orientation and bounding box
    		this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode)
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode + ", horiz index: " + this.coordBaseMode.getHorizontalIndex()
    					);
    		}

			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + new int[]{4,4,4,3}[this.coordBaseMode.getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + new int[]{2,4,5,4}[this.coordBaseMode.getHorizontalIndex()], EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + new int[]{4,5,4,2}[this.coordBaseMode.getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + new int[]{3,4,4,4}[this.coordBaseMode.getHorizontalIndex()], EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
        				true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
            
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

        	WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
        	
        	// Dark Prismarine Base
			IBlockState darkPrismarineState = Blocks.prismarine.getStateFromMeta(2);
			for (int[] uuvvww : new int[][]{
            	{3,1,3, 6,2,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], darkPrismarineState, darkPrismarineState, false);
            }
        	
        	
        	// --- Statue proper --- //
            
        	// Try to establish prismarine blocks used for the statue. If any kind doesn't exist, use default for all.
        	Block biomePrismarineStairsBlock=null;
        	IBlockState biomePrismarineBlockState=null;
        	IBlockState biomePrismarineSlabUpperState=null;
        	IBlockState biomePrismarineSlabLowerState=null;
        	IBlockState biomePrismarineWallState=null;
        	
    		boolean useOnlyStone = false; // This flag will indicate to use stone instead of diorite, should we need to.
        	while (true)
        	{
            	// Prismarine Stairs
            	if (useOnlyStone) {biomePrismarineStairsBlock = Blocks.stone_stairs;} // Set to cobblestone stairs
            	else
            	{
            		biomePrismarineStairsBlock = ModObjects.chooseModPrismarineStairsBlock();
            		if (biomePrismarineStairsBlock==null) {useOnlyStone=true; continue;} // Trigger flag and reset
            	}
            	biomePrismarineStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomePrismarineStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            	
            	// Prismarine blocks
            	if (useOnlyStone) {biomePrismarineBlockState = Blocks.cobblestone.getStateFromMeta(0);} // Set to cobblestone
            	else
            	{
                	if (biomePrismarineBlockState==null) {useOnlyStone=true; continue;} // Trigger flag and reset
            	}
            	biomePrismarineBlockState = StructureVillageVN.getBiomeSpecificBlockState(biomePrismarineBlockState, this.materialType, this.biome, this.disallowModSubs);
            	
            	
            	// Prismarine Slabs lower
            	if (useOnlyStone) {biomePrismarineSlabLowerState = Blocks.stone_slab.getStateFromMeta(3);} // Set to cobblestone slab
            	else
            	{
            		biomePrismarineSlabLowerState = ModObjects.chooseModPrismarineSlabState(false); 
                	if (biomePrismarineSlabLowerState==null) {useOnlyStone=true; continue;} // Trigger flag and reset
            	}
            	biomePrismarineSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomePrismarineSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
            	
                
            	// Prismarine Slabs upper
            	if (useOnlyStone) {biomePrismarineSlabUpperState = Blocks.stone_slab.getStateFromMeta(3+8);} // Set to cobblestone slab
            	else
            	{
            		biomePrismarineSlabUpperState = ModObjects.chooseModPrismarineSlabState(true); 
                	if (biomePrismarineSlabUpperState==null) {useOnlyStone=true; continue;} // Trigger flag and reset
            	}
            	biomePrismarineSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(biomePrismarineSlabUpperState, this.materialType, this.biome, this.disallowModSubs);
            	
            	
            	// Prismarine wall
            	if (useOnlyStone) {biomePrismarineWallState = Blocks.cobblestone_wall.getStateFromMeta(0);} // Set to cobblestone wall
            	else
            	{
            		biomePrismarineWallState = ModObjects.chooseModPrismarineWallState();
                	if (biomePrismarineWallState==null) {useOnlyStone=true; continue;} // Trigger flag and reset
            	}
            	biomePrismarineWallState = StructureVillageVN.getBiomeSpecificBlockState(biomePrismarineWallState, this.materialType, this.biome, this.disallowModSubs);
            	
            	
            	// If you make it here, all blocks are either prismarine-type or stone-type.
            	break;
            }
        	
        	// Now, construct the statue with either all diorite blocks, or all stone
        	
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Feet
        		{4,3,3, 3}, {5,3,3, 3}, 
        		// Butt
        		{4,3,5, 3+4}, {5,3,5, 3+4}, 
        		{4,3,6, 2+4}, {5,3,6, 2+4},
        		// Hands
        		{3,5,4, 3}, {6,5,4, 3}, 
        		// Lips
        		{4,6,3, 3}, {5,6,3, 3},
        		// Neck
        		{4,6,6, 2}, {5,6,6, 2},
        		})
            {
        		this.setBlockState(world, biomePrismarineStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	for (int[] uuvvww : new int[][]{
        		// Knees
        		{4,4,4, 5,4,4}, 
        		// Back
        		{4,4,6, 5,5,6}, 
        		// Head
        		{4,6,4, 5,7,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomePrismarineBlockState,
        				biomePrismarineBlockState, 
        				false);	
            }
        	for (int[] uuvvww : new int[][]{
        		// Thighs
        		{4,4,5, 5,4,5}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomePrismarineSlabLowerState,
        				biomePrismarineSlabLowerState, 
        				false);	
            }
        	for (int[] uuvvww : new int[][]{
        		// Meaty fists
        		{3,4,4, 3,4,4}, {6,4,4, 6,4,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomePrismarineSlabUpperState,
        				biomePrismarineSlabUpperState, 
        				false);	
            }
        	for (int[] uuvvww : new int[][]{
        		// Right arm
        		{3,5,5, 3,5,6}, 
        		// Left arm
        		{6,5,5, 6,5,6}, 
        		// Wings
        		{4,5,7, 5,6,7}, 
        		// Jowl
        		{4,5,4, 5,5,4}, 
        		// Calves
        		{4,3,4, 5,3,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomePrismarineWallState,
        				biomePrismarineWallState, 
        				false);	
            }
        	
        	
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	{4,5,3, 5,5,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            }
        	
        	
        	// Polished Blackstone Buttons
            Block polishedBlackstoneButtonBlock = ModObjects.chooseModPolishedBlackstoneButton(); 
        	if (polishedBlackstoneButtonBlock==null) {polishedBlackstoneButtonBlock = Blocks.stone_button;} // Stone button if not found
        	for(int[] uuvvwwo : new int[][]{
        		// Rim
        		// Front
        		{3,1,2, 6,1,2, 2},  
        		// Left
        		{2,1,3, 2,1,6, 3}, 
        		// Right
        		{7,1,3, 7,1,6, 1}, 
        		// Back
        		{3,1,7, 6,1,7, 0}, 
        		
        		// Eyes
        		{4,7,3, 5,7,3, 2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], polishedBlackstoneButtonBlock.getStateFromMeta(StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode)), polishedBlackstoneButtonBlock.getStateFromMeta(StructureVillageVN.chooseButtonMeta(uuvvwwo[6], this.coordBaseMode)), false);	
            }
        	
        	
        	// Terracotta
        	for(int[] uuvvww : new int[][]{
            	{2,0,1}, {7,0,1}, {0,0,6}, {8,0,6}, {5,0,9}, 
            	})
            {
            	this.setBlockState(world,
            			Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 15), // Black
            			uuvvww[0], uuvvww[1], uuvvww[2], structureBB);	
            }
        	
        	
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Staff
            	{2,1,1}, {7,1,1}, {0,1,6}, {8,1,6}, {5,1,9}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{0,2,6, -1}, {8,2,6, -1}, {5,2,9, -1}, 
        	})
        	{
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
        	}
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{0,1,2}, 
            	{9,1,0}, 
            	{1,1,9}, 
            	{9,1,8}, 
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
        	int signU = 2;
			int signV = 2;
			int signW = 1;
            IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            if (GeneralConfig.nameSign)
            {

    			int signO = 4;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
                boolean hanging=false;
                
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);

    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.coordBaseMode.getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            else {this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(-1, this.coordBaseMode.getHorizontalIndex())), signU, signV, signW, structureBB);} // Substitute a torch if signs are disabled
        	
    		
			// Banner    		
            int bannerU = 7;
			int bannerV = 2;
			int bannerW = 1;
    		if (GeneralConfig.villageBanners)
    		{
    			int bannerO = 12;
    			boolean hanging=false;
    			
    			int bannerX = this.getXWithOffset(bannerU, bannerW);
    			int bannerY = this.getYWithOffset(bannerV);
                int bannerZ = this.getZWithOffset(bannerU, bannerW);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
                world.setBlockState(bannerPos, Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(bannerO, this.coordBaseMode.getHorizontalIndex(), hanging)), 2);

				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(bannerPos, tilebanner);
    		}
    		else {this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(-1, this.coordBaseMode.getHorizontalIndex())), bannerU, bannerV, bannerW, structureBB);} // Substitute a torch if banners are disabled
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{6, 1, 1, -1, 0}, 
	        			{8, 1, 4, -1, 0}, 
	        			{4, 1, 8, -1, 0}, 
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
	        			if (GeneralConfig.enableNitwit && random.nextInt(3)==0) {entityvillager.setProfession(5);}
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
    
    
    
	// --- Swamp Pavilion --- //
	// designed by AstroTibs
    
    public static class SwampPavilion extends StartVN
    {
        // Make foundation with blanks as empty air, F as foundation spaces, and P as path
        private static final String[] foundationPattern = new String[]{
            	"   PPF  ",
            	" PPPPPP ",
            	"PPFPPFP ",
            	"PFPFPPPP",
            	"PPPPPFFP",
            	" PFPPFPP",
            	" PPPPPP ",
            	"  FPP   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2 + 4 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
	    public SwampPavilion() {}
		
		public SwampPavilion(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, float villageSize)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, villageSize);
    		
		    // Establish orientation and bounding box
    		this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode)
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode + ", horiz index: " + this.coordBaseMode.getHorizontalIndex()
    					);
    		}

			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()<2 ? 2:3), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()<2 ? 2:3), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()<2 ? 3:2), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()<2 ? 3:2), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
        				true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
            
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

        	WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
			
			
        	// For stripped logs specifically
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStrippedLogVertState = biomeLogVertState;
        	// Try to see if stripped logs exist
        	biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
        	for (int[] uuvvww : new int[][]{
        		{2,1,5, 2,3,5}, {5,1,5, 5,3,5}, 
        		{2,1,2, 2,3,2}, {5,1,2, 5,3,2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,4,1, 6,4,6}, {2,5,2, 5,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{1,3,6}, {6,3,6}, 
            	{1,3,1}, {6,3,1}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,4,3, 0, GeneralConfig.useVillageColors ? this.townColor3 : 10}, // Purple
        		{3,4,4, 1, GeneralConfig.useVillageColors ? this.townColor3 : 10}, // Purple
        		{4,4,3, 3, GeneralConfig.useVillageColors ? this.townColor3 : 10}, // Purple
        		{4,4,4, 2, GeneralConfig.useVillageColors ? this.townColor3 : 10}, // Purple
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
        	
        	
            // Sign
        	int signU = 2;
			int signV = 3;
			int signW = 1;
            IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wall_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            if (GeneralConfig.nameSign)
            {
    			int signO = 2;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
                boolean hanging=true;
                
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);

    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.coordBaseMode.getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            else {this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(-1, this.coordBaseMode.getHorizontalIndex())), signU, signV, signW, structureBB);} // Substitute a torch if signs are disabled
            
    		
    		// Banner - patterend or solid depending on configs
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{2,2,1, 2, 10}, // Purple
				{5,2,1, 2, 10}, // Purple
			})
			{
    			int bannerU = uvwoc[0];
    			int bannerV = uvwoc[1];
    			int bannerW = uvwoc[2];
    			boolean hanging=true;
    			
    			int bannerX = this.getXWithOffset(bannerU, bannerW);
    			int bannerY = this.getYWithOffset(bannerV);
                int bannerZ = this.getZWithOffset(bannerU, bannerW);

                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.wall_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.coordBaseMode.getHorizontalIndex(), true)), 2);
				
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
					modifystanding.setInteger("Base", 15 - uvwoc[4]);
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
	        			{3, 1, 3, -1, 0}, 
	        			{1, 1, 2, -1, 0}, 
	        			{0, 1, 3, -1, 0}, 
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
	        			if (GeneralConfig.enableNitwit && random.nextInt(3)==0) {entityvillager.setProfession(5);}
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
    
    
    
	// --- Swamp Monolith --- //
	// designed by AstroTibs
    
    public static class SwampMonolith extends StartVN
    {
        // Make foundation with blanks as empty air, F as foundation spaces, and P as path
        private static final String[] foundationPattern = new String[]{
            	"F   PPP    F",
            	"  PPPPPFP   ",
            	" PFPPFPPPP  ",
            	" PPPFFFFPPP ",
            	"PPPFFFFFFPF ",
            	"PPPFFFFFFPPP",
            	"FPPFFFFFFPPP",
            	" PPFFFFFFFPP",
            	" FPPFFFFPPP ",
            	"  PPPPPPPPP ",
            	"   PPPPPPP  ",
            	"F    PPP   F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1 + 2 + 4 + 8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
	    public SwampMonolith() {}
		
		public SwampMonolith(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, float villageSize)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, villageSize);
    		
		    // Establish orientation and bounding box
    		this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(random);
            switch (this.coordBaseMode)
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
    					+ this.worldChunkMngr.getBiomeGenerator(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)).biomeName
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.coordBaseMode + ", horiz index: " + this.coordBaseMode.getHorizontalIndex()
    					);
    		}

			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + new int[]{5,4,4,5}[this.coordBaseMode.getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + new int[]{4,5,5,4}[this.coordBaseMode.getHorizontalIndex()], EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + new int[]{3,5,5,4}[this.coordBaseMode.getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + new int[]{5,4,4,5}[this.coordBaseMode.getHorizontalIndex()], EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.field_143015_k < 0)
            {
        		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
        				true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
            
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

        	WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
        	
        	// Dark Prismarine Monolith that becomes green terracotta otherwise
			IBlockState darkPrismarineState = Blocks.prismarine.getStateFromMeta(2);
        	for (int[] uuvvww : new int[][]{
            	{5,1,5, 6,6,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], darkPrismarineState, darkPrismarineState, false);
            }
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	{4,1,7, 7,1,7}, 
            	{4,1,5, 4,1,6}, {7,1,5, 7,1,6}, 
            	{4,1,4, 7,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);	
            }
            
            
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{4,2,7, -1}, {7,2,7, -1}, 
            	{4,2,4, -1}, {7,2,4, -1}, 
        	})
        	{
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
        	}
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{0,1,11}, {11,1,11}, 
            	{0,1,0}, {11,1,0}, 
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
            IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wall_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            if (GeneralConfig.nameSign)
            {
            	int signU = 6;
    			int signV = 1;
    			int signW = 3;
    			int signO = 2;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
                boolean hanging=true;
                
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);

    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(signO, this.coordBaseMode.getHorizontalIndex(), hanging)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		// Banners
            if (GeneralConfig.villageBanners)
    		{
            	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            	
    			for (int[] uvwo : new int[][]{ // u, v, w, orientation
    				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    				
    				{4,1,1, 4}, 
    				{8,1,1, 12}, 
    			})
    			{
        			int bannerU = uvwo[0];
        			int bannerV = uvwo[1];
        			int bannerW = uvwo[2];
        			boolean hanging=false;
        			
        			int bannerX = this.getXWithOffset(bannerU, bannerW);
        			int bannerY = this.getYWithOffset(bannerV);
                    int bannerZ = this.getZWithOffset(bannerU, bannerW);

                    BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                    
                    // Cobblestone foundation
        			this.setBlockState(world, biomeCobblestoneState, uvwo[0], uvwo[1]-1, uvwo[2], structureBB);

                	// Set the banner and its orientation
    				world.setBlockState(bannerPos, Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex(), false)), 2);
    				
    				// Set the tile entity
    				TileEntity tilebanner = new TileEntityBanner();
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
	        			{6, 1, 2, -1, 0}, 
	        			{1, 1, 4, -1, 0}, 
	        			{4, 1, 9, -1, 0}, 
	        			{10, 1, 5, -1, 0}, 
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			
	        			// Nitwits more often than not
	        			if (GeneralConfig.enableNitwit && random.nextInt(3)==0) {entityvillager.setProfession(5);}
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
    
    
    // --- Animal Pen 1 --- //
    // designed by THASSELHOFF
    
    public static class SwampAnimalPen1 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"        ",
    			" F    F ",
    			"        ",
    			" F    F ",
    			"  F     ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 4;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 3;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampAnimalPen1() {}

    	public SwampAnimalPen1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampAnimalPen1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampAnimalPen1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		// Front Awning
        		{1,0,3, 1,1,3}, {6,0,3, 6,1,3}, 
        		{1,0,1, 1,1,1}, {6,0,1, 6,1,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,1,2, 1,1,2}, {2,1,3, 5,1,3}, {6,1,2, 6,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,0,0, 2,0,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,0,3, 5,0,3}, 
    			{1,0,2, 6,0,2}, 
    			{2,0,1, 5,0,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,2,1, 1,2,2}, {2,2,1, 5,2,1}, {6,2,1, 6,2,3}, {2,2,3, 5,2,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,2,3, 1,2,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,2,1, 0,2,4}, 
    			{1,2,4, 2,2,4}, {5,2,4, 7,2,4}, 
    			{7,2,0, 7,2,4}, 
    			{2,2,0, 6,2,0}, 
    			{2,3,2, 3,3,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyStoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
        		biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{3,2,4, 4,2,4}, 
    			{4,3,2, 5,3,2}, 
    			{0,2,0, 1,2,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Trapdoor (Top Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,1, 2}, {3,1,1, 2}, {4,1,1, 2}, {5,1,1, 2}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		{0,1,4}, {7,1,4}, 
        		{0,1,0}, {7,1,0}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
    		
    		// Entities
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
            	// Chickens
            	for (int[] uvw : new int[][]{
        			{3, 1, 2},
        			{5, 1, 2},
        			})
        		{
    				EntityChicken animal = new EntityChicken(world);
    				IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
            		
    				animal.setLocationAndAngles(getXWithOffset(uvw[0], uvw[2]) + 0.5D, getYWithOffset(uvw[1]) + 0.5D, getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat() * 360.0F, 0.0F);
    				world.spawnEntityInWorld((Entity)animal);
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Animal Pen 2 --- //
    // designed by THASSELHOFF
    
    public static class SwampAnimalPen2 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1+2+4+8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampAnimalPen2() {}

    	public SwampAnimalPen2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampAnimalPen2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampAnimalPen2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
    			{0,0,0, 0,0,13}, 
    			{1,0,0, 1,0,3}, {1,0,7, 3,0,13}, 
    			{2,0,0, 3,0,2}, 
    			{4,0,0, 4,0,1}, {4,0,6, 4,0,7}, {4,0,10, 4,0,13}, 
    			{5,0,0, 6,0,0}, {5,0,5, 5,0,6}, {5,0,11, 5,0,13}, 
    			{6,0,4, 6,0,7}, {6,0,12, 8,0,13}, 
    			{7,0,0, 7,0,4}, {7,0,6, 7,0,9}, 
    			{8,0,0, 8,0,3}, {8,0,7, 8,0,10}, 
    			{9,0,0, 10,0,2}, {9,0,8, 9,0,13}, 
    			{10,0,10, 10,0,13}, 
    			{11,0,0, 11,0,3}, {11,0,10, 11,0,13}, 
    			{12,0,0, 12,0,4}, {12,0,8, 12,0,13}, 
    			{13,0,0, 13,0,13}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		// Front Awning
        		{0,1,13, 0,4,13}, {13,1,13, 13,5,13}, 
        		{0,1,0, 0,4,0}, {13,1,0, 13,5,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Lantern posts
            	{0,4,12}, {1,5,13}, {0,5,13}, {12,5,13}, {13,5,12}, 
            	{0,5,0}, {0,5,1}, {1,4,0}, {12,5,0}, {13,4,1}, 
            	// Fence
            	{1,1,13}, {2,1,13}, {3,1,13}, {4,1,13}, {5,1,13}, {6,1,13}, {9,1,13}, {10,1,13}, {11,1,13}, {12,1,13}, 
            	{0,1,1}, {0,1,2}, {0,1,3}, {0,1,6}, {0,1,7}, {0,1,8}, {0,1,9}, {0,1,10}, {0,1,11}, {0,1,12}, 
            	{13,1,1}, {13,1,2}, {13,1,5}, {13,1,6}, {13,1,7}, {13,1,8}, {13,1,9}, {13,1,10}, {13,1,11}, {13,1,12}, 
            	{1,1,0}, {2,1,0}, {3,1,0}, {4,1,0}, {5,1,0}, {6,1,0}, {7,1,0}, {8,1,0}, {9,1,0}, {12,1,0}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
        	// Fence Gate (Across)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence_gate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
        		{7,1,13}, {8,1,13}, 
        		
        		{10,1,0}, {11,1,0}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlockstate.getBlock(), biomeFenceGateBlockstate.getBlock().getMetaFromState(biomeFenceGateBlockstate), this.coordBaseMode)), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Fence Gate (Along)
        	for(int[] uvw : new int[][]{
        		{0,1,4}, {0,1,5}, {13,1,3}, {13,1,4}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlockstate.getBlock(), (biomeFenceGateBlockstate.getBlock().getMetaFromState(biomeFenceGateBlockstate)+1)%8, this.coordBaseMode)), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		{0,3,12}, {1,4,13}, 
        		{12,4,13}, {13,4,12}, 
        		{12,4,0}, {13,3,1}, 
        		{1,3,0}, {0,4,1}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			// Back left
    			{1,0,7, 1,0,8}, {2,0,7, 2,0,9}, {3,0,7, 3,0,11}, {4,0,8, 4,0,12}, {5,0,9, 5,0,12}, {6,0,10, 6,0,12}, 
    			// Back right
    			{7,0,8, 7,0,8}, {8,0,7, 9,0,9}, {10,0,6, 10,0,8}, {11,0,5, 11,0,7}, 
    			// Front pond
    			{3,0,3, 3,0,4}, {4,0,2, 4,0,5}, {5,0,1, 5,0,6}, {6,0,1, 6,0,5}, {7,0,1, 7,0,4}, {8,0,2, 9,0,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);	
    		}
    		
    		
    		// Lilypad
    		for (int[] uvw : new int[][]{
    			{2,1,8}, 
    			{3,1,10}, {3,1,11}, 
    			{4,1,5}, {4,1,10}, 
    			{5,1,3}, {5,1,9}, 
    			{7,1,3}, 
    			{9,1,7}, {9,1,9}, 
    			{11,1,6}, 
    			}) {
    			this.setBlockState(world, Blocks.waterlily.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
            // Hay bales (vertical)
        	for (int[] uvw : new int[][]{
        		{9,1,10}, {10,1,10}, 
        		})
            {
        		this.setBlockState(world, Blocks.hay_block.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Hay bales (along)
        	for (int[] uvw : new int[][]{
        		{10,1,9}, 
        		})
            {
        		this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(Blocks.hay_block.getStateFromMeta(0), this.coordBaseMode.getHorizontalIndex(), false), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Unkempt Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			{1,1,5, 0}, {1,1,11, 2}, {1,1,12, 1}, 
    			{2,1,4, 0}, {2,1,12, 1}, 
    			{7,1,11, 0}, 
    			{9,1,5, 1}, 
    			{10,1,1, 2}, {10,1,3, 0}, {10,1,4, 0}, 
    			{11,1,1, 1}, {11,1,4, 0}, 
    			{12,1,1, 2}, 
    		})
    		{
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==1) // Tall grass
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==2) // Fern
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Large Fern
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(3), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    		}
        	
    		
    		// Entities
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Livestock
            	for (int[] uvw : new int[][]{
        			{2, 1, 10}, 
        			{8, 1, 5}, 
        			})
        		{
            		BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM);
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
    			// Horse
            	for (int[] uvw : new int[][]{
            		{2, 1, 2}, 
        			})
        		{
    				EntityHorse animal = new EntityHorse(world);
    				IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
    				
                	if (VillageGeneratorConfigHandler.nameVillageHorses && GeneralConfig.nameEntities)
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
			
    		
    		// Clean items
    		if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
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
    // designed by AstroTibs
    
    public static class SwampArmorerHouse extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"         ",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFF ",
    			"FFFFFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = -1;
    	private static final int DECREASE_MAX_U = 5;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampArmorerHouse() {}

    	public SwampArmorerHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampArmorerHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampArmorerHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Brick Blocks
    		IBlockState biomeBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.brick_block.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uuvvww : new int[][]{
        		{0,0,3, 7,0,3}, 
        		{0,0,0, 0,0,2}, 
        		// Furnace
        		{7,0,2, 8,1,2}, {8,0,3, 8,1,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBrickBlockState, biomeBrickBlockState, false);	
    		}
            
            
            // Brick Walls
        	IBlockState biomeBrickWallState = null;
        	
        	// First, attempt to obtain modded brick wall
        	biomeBrickWallState = ModObjects.chooseModBrickWallState();
        	if (biomeBrickWallState==null)
        	{
        		// Use cobblestone
        		biomeBrickWallState = Blocks.cobblestone_wall.getStateFromMeta(0);
        	}
        	// Convert to biome-specific versions
        	biomeBrickWallState = StructureVillageVN.getBiomeSpecificBlockState(biomeBrickWallState, this.materialType, this.biome, this.disallowModSubs);
        	biomeBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(biomeBrickBlockState, this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uuvvww : new int[][]{
    			// Furnace chimney
    			{7,2,3, 7,3,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBrickWallState, biomeBrickWallState, false);	
    		}
            
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{7,1,3, 3}, 
        		})
            {
        		IBlockState blastFurnaceState = ModObjects.chooseModBlastFurnaceState(uvwo[3], this.coordBaseMode);
                this.setBlockState(world, blastFurnaceState.getBlock().getStateFromMeta(0), uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), blastFurnaceState, 2);
            }
    		
			
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor
    			{0,0,4, 8,0,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
        	
        	
        	// Terracotta (Green)
        	for(int[] uuvvww : new int[][]{
        		{0,1,7, 8,1,7}, 
        		{0,1,5, 0,1,6}, {8,1,5, 8,1,6}, 
        		{0,1,4, 3,1,4}, {5,1,4, 8,1,4}, 
            	})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
    					Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 13), 
    					Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 13), 
    					false);	
            }
        	
        	
        	// Terracotta (Black)
        	for(int[] uuvvww : new int[][]{
        		{0,3,4, 8,3,4}, 
        		{0,2,4, 1,2,4}, {7,2,4, 8,2,4}, 
        		{0,3,7, 8,3,7}, 
        		{0,2,7, 1,2,7}, {3,2,7, 3,2,7}, {5,2,7, 5,2,7}, {7,2,7, 8,2,7}, 
        		{0,2,5, 0,3,6}, {8,2,5, 8,3,6}, 
            	})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
    					Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 15), 
    					Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 15), 
    					false);	
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,2,4, 2, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
        		{5,2,4, 0, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		// Front Awning
        		{1,0,1, 1,2,1}, {3,0,1, 3,2,1}, {5,0,1, 5,2,1}, {7,0,1, 7,2,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
            // Water boundary
    		// This puts up a dirt/grass wall to keep in the water if it's not level with the surrounding ground
            for(int[] uwuwvs : new int[][]{ // u/w box corners, v height, and the side that is to be evaluated.
            	// Side - 0:forward (away from you), 1:rightward, 2:backward (toward you), 3: leftward
            	
    			{7,0, 7,0, 0, 1}, // Right side
    			{0,0, 8,0, 0, 2}, // Back side
    			})
    		{
            	int u_offset=0, w_offset=0;
            	int v = uwuwvs[4];
            	
            	switch(uwuwvs[5])
            	{
            	case 0: w_offset=1; break; // forward
            	case 1: u_offset=1; break; // rightward
            	case 2: w_offset=-1; break; // backward
            	case 3: u_offset=-1; break; // leftward
            	default:
            	}
            	
            	// Scan boundary and add containment if necessary
            	for (int u=uwuwvs[0]; u<=uwuwvs[2]; u++) {for (int w=uwuwvs[1]; w<=uwuwvs[3]; w++)
            	{
            		int x = this.getXWithOffset(u+u_offset, w+w_offset);
            		int y = this.getYWithOffset(v);
            		int z = this.getZWithOffset(u+u_offset, w+w_offset);
            		
            		// If space above bordering block is liquid, fill below with filler and cap with topper
            		if (world.getBlockState(new BlockPos(x, y+1, z)).getBlock().getMaterial().isLiquid())
            		{
            			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u+u_offset, v, w+w_offset, structureBB);
            			this.setBlockState(world, biomeTopState, u+u_offset, v+1, w+w_offset, structureBB);
            		}
            		// If bordering block is air, fill below with filler and cap with topper
            		else if (world.isAirBlock(new BlockPos(x, y, z)))
            		{
            			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u+u_offset, v-1, w+w_offset, structureBB);
            			this.setBlockState(world, biomeTopState, u+u_offset, v, w+w_offset, structureBB);
            		}
            	}}
    		}
            
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			{1,0,2, 6,0,2}, 
    			{2,0,1, 2,0,1}, {4,0,1, 4,0,1}, {6,0,1, 6,0,1}, 
    			{1,0,0, 7,0,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);	
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,4,7, 8,4,7}, 
            	{0,4,5, 0,4,6}, {8,4,5, 8,4,6}, 
            	// Desk
            	{1,1,6, 1,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,4,8, 8,4,8}, 
    			{0,4,4, 8,4,4}, 
    			{0,3,1, 8,3,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,3,3, 6,3,3}, {8,3,3, 8,3,3}, 
    			// Ceiling
    			{1,4,5, 7,4,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Brick Slab (lower)
    		IBlockState biomeBrickSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(4), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,5,6, 8,5,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBrickSlabLowerState, biomeBrickSlabLowerState, false);	
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Chair
    			{7,1,5, 2}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	{7,1,6}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{7,2,6}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Trapdoor (Top Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,7, 2}, {4,2,7, 2}, {6,2,7, 2}, 
            	{2,2,4, 0}, {6,2,4, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,1,4, 2, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{1,1,5, 1,2,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            }
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		{2,1,5, 6,1,6, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet.getStateFromMeta(uuvvww[6]), Blocks.carpet.getStateFromMeta(uuvvww[6]), false);	
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{1,2,2}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{7,2,2}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{4,3,6, 2}, 
    			}) {
    			this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 1;
        	int chestV = 2;
        	int chestW = 6;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_armorer");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Left side
        			{-1,4,5, 3}, {-1,3,5, 3}, {-1,2,5, 3}, 
        			// Right side
        			{9,4,6, 1}, {9,3,6, 1}, {9,2,6, 1}, {9,1,6, 1}, 
        			{9,1,7, 1}, 
        			// Away-facing vines
        			{1,3,8, 0}, {1,2,8, 0}, 
        			{3,3,8, 0}, {3,2,8, 0}, {3,1,8, 0}, 
        			{4,3,8, 0}, 
        			// Player-facing side
        			{1,2,0, 2}, {1,1,0, 2}, 
        			{7,2,0, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(10+3);
    			
    			int u;
    			int v = 3;
    			int w;
    			
				u = s<=9? 2+(s/2) : s-6;
				w = s<=9? 5+(s%2) : 3;
    			
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    // --- Butcher Shop --- //
    // designed by AstroTibs and THASSELHOFF
    
    public static class SwampButcherShop extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"     FFFF    ",
    			"    FFFFFF   ",
    			"  FFFFFFFFF  ",
    			" FFFFFFFFFFF ",
    			"FFFFFFFFFFFF ",
    			"FFFFFFFFFFFFF",
    			" FFFFFFFFFFFF",
    			"  FFFFFFFFFFF",
    			"  FFFFFFFFFFF",
    			"  FFFFFFFFFFF",
    			"  FFFFFFFFFFF",
    			"   FFFFFFFFFF",
    			"   FFFFFFFFF ",
    			"   FFFFFFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 3;
    	private static final int DECREASE_MAX_U = 3;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampButcherShop() {}

    	public SwampButcherShop(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampButcherShop buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampButcherShop(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Grass Path
        	for (int[] uvw : new int[][]{
        		{5,1,2}, {6,1,2}, {7,1,2}, 
        		{5,1,1}, {6,1,1}, {7,1,1}, 
        		{5,1,0}, {6,1,0}, {7,1,0}, 
            	}) {
        		int posX = this.getXWithOffset(uvw[0], uvw[2]);
    			int posY = this.getYWithOffset(uvw[1]);
    			int posZ = this.getZWithOffset(uvw[0], uvw[2]);
        		StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
            }
    		
			
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,0,3, 8,0,9}, 
    			{4,4,4, 4,4,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Front wall
    			{3,4,2, 5,4,2}, {6,4,3, 7,4,3}, 
    			{3,1,3, 8,1,3}, {3,2,3, 5,3,3}, {7,2,3, 8,3,3}, 
    			// Left wall
    			{3,1,4, 3,2,9}, {3,4,3, 3,4,8}, {3,3,4, 3,3,4}, {3,3,7, 3,3,9}, 
    			// Right wall
    			{8,1,4, 8,4,9}, 
    			// Back wall
    			{4,1,9, 7,4,9}, 
    			// Ceiling
    			{5,4,4, 7,4,4}, {4,4,5, 7,4,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Dirt
            for(int[] uuvvww : new int[][]{
    			// Front entrance
            	{4,1,0, 4,1,2}, 
            	// Left hill
            	{1,2,8, 1,2,9}, 
            	{2,2,7, 2,3,9}, {2,2,10, 2,2,10}, 
            	{3,2,10, 4,3,10}, 
            	{4,2,11, 4,2,11}, 
            	{5,2,10, 8,3,11}, {8,4,11, 8,4,11}, {6,2,12, 7,2,12}, 
            	{8,4,2, 8,4,2}, {8,2,1, 8,2,1}, 
            	{9,2,1, 9,2,1}, {9,2,2, 9,3,2}, {9,2,3, 9,3,10}, {9,4,3, 9,4,7}, 
            	{10,2,0, 10,2,1}, {10,2,2, 10,3,7}, {10,2,8, 10,2,9}, 
            	{11,2,2, 11,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);
            }
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
    			{3,5,6, 3,5,8}, {4,5,5, 8,5,9}, {5,5,4, 8,5,4}, {6,5,3, 8,5,3}, {9,5,3, 9,5,7}, {8,5,2, 8,5,2}, 
    			{2,4,7, 2,4,9}, {3,4,9, 3,4,9}, {3,4,10, 8,4,10}, {5,4,11, 7,4,11}, {9,4,2, 9,4,2}, {9,4,8, 9,4,9}, {10,4,2, 10,4,7}, {4,4,3, 5,4,3}, {6,4,2, 7,4,2}, 
    			{1,3,8, 1,3,9}, {2,3,10, 2,3,10}, {4,3,11, 4,3,11}, {6,3,12, 7,3,12}, {9,3,10, 9,3,10}, {10,3,8, 10,3,9}, {11,3,2, 11,3,7}, {10,3,0, 10,3,1}, {8,3,1, 9,3,1}, 
    			{1,2,7, 1,2,7}, {0,2,8, 0,2,9}, {1,2,10, 1,2,10}, {2,2,11, 3,2,11}, {4,2,12, 5,2,12}, {5,2,13, 8,2,13}, {8,2,12, 9,2,12}, {9,2,0, 9,2,0}, {9,2,11, 10,2,11}, {10,2,10, 11,2,10}, 
    			{11,2,8, 11,2,9}, {12,2,2, 12,2,8}, {11,2,1, 11,2,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		{4,2,2, 4,3,2}, {8,2,2, 8,3,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
    		
        	
        	// Clear out basement
            for(int[] uuvvww : new int[][]{
    			{4,1,4, 7,3,8}, 
    			})
    		{
            	this.fillWithAir(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5]);	
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		{6,3,7}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Smooth Stone Block
        	IBlockState smoothStoneBlockState = ModObjects.chooseModSmoothStoneBlockState();
            for (int[] uuvvww : new int[][]{
            	// Counter
            	{5,1,7, 6,1,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], smoothStoneBlockState, smoothStoneBlockState, false);
            }
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{4,5,4, 4,7,4}, {4,2,4, 4,3,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Stairs up to animal pen
    			{8,2,0, 0}, {9,3,0, 0}, {10,4,1, 3}, {9,5,2, 1}, 
    			// Basement
    			{6,1,4, 2}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			{3,6,6, 3,6,8}, {4,6,8, 4,6,9}, {5,6,9, 8,6,9}, 
    			{8,6,7, 8,6,8}, {9,6,3, 9,6,7}, 
    			{4,6,5, 4,6,6}, {5,6,4, 5,6,5}, {6,6,3, 6,6,4}, {7,6,3, 7,6,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{3,7,6, -1}, 
    			{6,7,3, -1}, 
    			{9,7,7, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
        	// Fence Gate (Across)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence_gate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{8,6,3}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlockstate.getBlock(), biomeFenceGateBlockstate.getBlock().getMetaFromState(biomeFenceGateBlockstate), this.coordBaseMode)), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Smoker
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.coordBaseMode);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,4, 1}
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), smokerState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
    		
    		
    		// Trapdoor (Top Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,3,5, 1}, {3,3,6, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{6,2,3, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Unkempt Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			{1,3,7, 1}, {1,4,8, 0}, 
    			{2,4,10, 2}, 
    			{3,3,11, 0}, {3,5,9, 0}, 
    			{5,3,13, 0}, 
    			{6,5,10, 1}, 
    			{7,5,11, 0}, {7,6,8, 0}, {7,6,4, 0}, 
    			{8,3,12, 0}, 
    			{9,3,11, 1}, {9,5,8, 0}, 
    			{10,4,8, 0}, {10,5,4, 0}, 
    			{11,4,3, 0}, {11,4,5, 0}, 
    			{12,3,5, 2}, {12,3,6, 1}, 
    		})
    		{
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==1) // Tall grass
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==2) // Fern
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Large Fern
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(3), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    		}
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{8,5,11}, 
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
    			int s = random.nextInt(16);
    			
    			int u = s<=3 ? 4 : s<=7 ? 5 : s<=10 ? 6 : 7;
    			int v = 1;
    			int w = s<=3 ? s+5 : s<=6 ? s : s<=8 ? 8 : s<=10 ? s-4 : s-7;
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 1, 0); // Butcher
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
    			world.spawnEntityInWorld(entityvillager);
    			
    			
            	// Animals
            	for (int[] uvw : new int[][]{
        			{6, 6, 6},
        			})
        		{
            		BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM);
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    // --- Cartographer House --- //
    // designed by AstroTibs
    
    public static class SwampCartographerHouse extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"          ",
    			" F     F  ",
    			"          ",
    			"   F F    ",
    			"          ",
    			"   F F    ",
    			"          ",
    			" F     F F",
    			"         P",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 6;
    	private static final int DECREASE_MAX_U = -1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampCartographerHouse() {}

    	public SwampCartographerHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampCartographerHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampCartographerHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
        	
        	
        	// Dark Prismarine Base
    		IBlockState darkPrismarineState = Blocks.prismarine.getStateFromMeta(2);
        	for (int[] uuvvww : new int[][]{
            	{1,1,7, 1,2,7}, {7,1,7, 7,2,7}, 
            	{3,1,5, 3,8,5}, {5,1,5, 5,2,5}, 
            	{3,1,3, 3,2,3}, {5,1,3, 5,2,3}, 
            	{1,1,1, 1,2,1}, {7,1,1, 7,2,1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], darkPrismarineState, darkPrismarineState, false);
            }
    		
    		
            // Purple Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Floor
        		{1,3,1, 2,3,8}, 
        		{3,3,1, 3,3,4}, {3,3,6, 3,3,8}, 
        		{4,3,1, 5,3,8}, 
        		{6,3,1, 7,3,7}, 
        		{8,3,4, 8,3,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor3 : 10),
        				Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor3 : 10), 
        				false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Porch
            	{0,3,0, 0,3,1}, {1,3,0, 8,3,0}, {8,3,1, 8,3,3}, {8,3,5, 8,3,8}, {9,3,4, 9,3,5}, {6,3,8, 7,3,8}, 
            	// Left wall
            	{0,4,2, 0,5,8}, 
            	// Right wall
            	{6,4,2, 6,5,4}, {6,4,6, 6,5,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
            	// Porch
            	{0,4,0, 0,4,1}, {1,4,0, 8,4,0}, {8,4,1, 8,4,3}, {8,4,5, 9,4,5}, {8,4,6, 8,4,8}, {7,4,8, 7,4,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{0,6,8, 0}, {1,7,8, 0}, {2,8,8, 0}, {4,8,8, 1}, 
    			{1,7,7, 0}, {2,8,7, 0}, {4,8,7, 1}, 
    			{0,6,6, 0}, {1,7,6, 0}, {2,8,6, 0}, {4,8,6, 1}, {5,7,6, 1}, 
    			{1,7,5, 0}, {2,8,5, 0}, {6,6,5, 1}, 
    			{5,7,4, 1}, {6,6,4, 1}, 
    			{2,8,3, 0}, {4,8,3, 1}, {5,7,3, 1}, 
    			{0,6,2, 0}, {1,7,2, 0}, {2,8,2, 0}, {4,8,2, 1}, {5,7,2, 1}, {6,6,2, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
        	
        	
    		// Mossy Cobblestone stairs
    		Block biomeMossyCobblestoneStairsBlock = ModObjects.chooseModMossyCobblestoneStairsBlock();
    		if (biomeMossyCobblestoneStairsBlock==null) {biomeMossyCobblestoneStairsBlock = Blocks.stone_stairs;}
    		biomeMossyCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{5,7,8, 1}, {6,6,8, 1}, 
    			{0,6,7, 0}, {5,7,7, 1}, {6,6,7, 1}, 
    			{6,6,6, 1}, 
    			{0,6,5, 0}, {4,8,5, 1}, {5,7,5, 1}, 
    			{0,6,4, 0}, {1,7,4, 0}, {2,8,4, 0}, {4,8,4, 1}, 
    			{0,6,3, 0}, {1,7,3, 0}, {6,6,3, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeMossyCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,9,2, 3,9,3}, {3,9,5, 3,9,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{3,9,4, 3,9,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Seat
    			{1,4,3, 1}, {1,4,4, 1}, 
    			// Entryway
    			{9,1,1, 3}, 
    			{9,2,2, 3}, 
    			{9,3,3, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{9,1,2, 9,1,2}, 
    			{9,2,3, 9,2,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Trapdoor (Bottom Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,4,5, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{5,4,7, 2, GeneralConfig.useVillageColors ? this.townColor3 : 10}, // Purple
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
        	
        	
        	// Potted plant
        	for (int[] uvws : new int[][]{ // u,v,w, sapling
        		{5,5,7, random.nextInt(6)},
           		})
        	{
            	StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, 
            			new BlockPos(this.getXWithOffset(uvws[0], uvws[2]), 
            			this.getYWithOffset(uvws[1]), 
            			this.getZWithOffset(uvws[0], uvws[2])), 
            			Blocks.sapling, uvws[3]);
        	}
            
        	
        	// Cartography Table
        	IBlockState cartographyTableState = ModObjects.chooseModCartographyTableState();
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{1,4,7}, 
           		})
        	{
            	this.setBlockState(world, cartographyTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{5,6,7, 5,6,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{6,4,5, 3, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
            
            // Glass blocks
        	for (int[] uuvvww : new int[][]{
        		{1,4,8, 5,6,8}, {2,7,8, 4,7,8}, {3,8,8, 3,8,8}, 
        		{1,4,2, 5,6,2}, {2,7,2, 4,7,2}, {3,8,2, 3,8,2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.glass.getStateFromMeta(0), Blocks.glass.getStateFromMeta(0), false);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{3,6,6, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Right facing
        			{7,5,7, 1}, 
        			// Away-facing
        			{5,6,9, 0}, {5,5,9, 0}, {5,4,9, 0}, 
        			{6,5,9, 0}, {6,4,9, 0}, {6,3,9, 0}, {6,2,9, 0}, {6,1,9, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(20);
    			
    			int u = s<=1? 1 : s<=6? 2 : s<=8? 3 : s<=10? 3 : s<=15? 4 : 5;
    			int v = 4;
    			int w = s<=1? s+5 : s<=6? s+1 : s<=8? s-4 : s<=10? s-3 : s<=15? s-8 : s-13;
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    // --- Fisher Cottage 1 --- //
    // designed by jss2a98aj
    
    public static class SwampFisherCottage1 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"FFF   FFF ",
    			"FFFFFFFFF ",
    			"FFFFFFFFF ",
    			" FFFFFFF  ",
    			" FFFFFFF  ",
    			" FFFFFFF  ",
    			"FFFFFFFFF ",
    			"FFFFFFFFF ",
    			"FFF   FFFF",
    			"      PPPP",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 6;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampFisherCottage1() {}

    	public SwampFisherCottage1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampFisherCottage1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampFisherCottage1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{6,0,9, 8,0,9}, {8,0,8, 8,0,8}, 
    			{0,0,7, 0,0,9}, {1,0,7, 1,0,7}, {1,0,9, 2,0,9}, 
    			{0,0,1, 0,0,2}, {1,0,1, 1,0,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Bases
    			{6,0,7, 7,0,8}, {8,0,7, 8,0,7}, 
    			{1,0,8, 2,0,8}, {2,0,7, 2,0,7}, 
    			{0,0,3, 2,0,3}, {1,0,2, 2,0,2}, {2,0,1, 2,0,1}, 
    			// Rims
    			{1,0,4, 1,0,6}, 
    			{3,0,2, 5,0,2}, {3,0,8, 5,0,8}, 
    			{7,0,4, 7,0,6}, 
    			{6,0,1, 8,0,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
    		
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			{2,0,4, 2,0,6}, {3,0,3, 5,0,7}, {6,0,4, 6,0,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);	
    		}
    		
    		
    		// Lilypad
    		for (int[] uvw : new int[][]{
    			{4,1,3}, {5,1,5}, 
    			}) {
    			this.setBlockState(world, Blocks.waterlily.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Wall corners
    			{7,4,5, 7,6,5}, 
    			{8,4,6, 8,6,6}, 
    			{5,4,9, 5,6,9}, {8,4,9, 8,6,9}, 
    			{4,4,8, 4,6,8}, 
    			{1,4,5, 1,6,5}, 
    			{0,4,4, 0,6,4}, 
    			{0,4,1, 0,6,1}, {3,4,1, 3,6,1}, {4,4,2, 4,6,2}, 
    			// Bases
    			{1,1,8, 1,6,8}, {7,1,8, 7,2,8},
    			{1,1,2, 1,2,2}, {7,1,2, 7,6,2},
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{7,2,3, 0}, {1,2,7, 2}, 
    			}) {
    			this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{6,3,7, 7,3,8}, 
            	{1,3,7, 1,3,7}, {2,3,8, 2,3,8}, 
    			{1,3,2, 2,3,3}, {6,3,2, 6,3,3}, {7,3,3, 7,3,3}, 
    			// Roof
    			{2,7,7, 2,7,7}, {6,7,7, 7,7,8}, 
    			{2,7,5, 3,7,5}, {5,7,5, 6,7,5}, {4,7,3, 4,7,4}, {4,7,6, 4,7,7}, 
    			{1,7,2, 2,7,3}, {6,7,3, 6,7,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Bottom trim
    			// Back-right post
    			{5,3,9, 2+4}, {6,3,9, 2+4}, {7,3,9, 2+4}, {8,3,9, 2+4}, 
    			{5,3,7, 0+4}, {5,3,8, 0+4}, {8,3,7, 1+4}, {8,3,8, 1+4}, 
    			{5,3,6, 3+4}, {6,3,6, 3+4}, {7,3,6, 3+4}, {8,3,6, 3+4}, 
    			// Front-left post
    			{0,3,4, 2+4}, {1,3,4, 2+4}, {2,3,4, 2+4}, {3,3,4, 2+4}, 
    			{0,3,2, 0+4}, {0,3,3, 0+4}, {3,3,2, 1+4}, {3,3,3, 1+4}, 
    			{0,3,1, 3+4}, {1,3,1, 3+4}, {2,3,1, 3+4}, {3,3,1, 3+4}, 
    			// Front-right post
    			{5,3,2, 0+4}, {5,3,3, 0+4}, {5,3,4, 0+4}, {6,3,4, 2+4}, {7,3,4, 2+4}, {8,3,4, 2+4}, 
    			// Back-left post
    			{1,3,6, 3+4}, {3,3,8, 1+4}, 
    			// Front steps
    			{7,1,1, 0}, {8,1,1, 3}, {8,2,2, 3}, {8,3,3, 3}, 
    			{8,1,2, 2+4}, {8,2,3, 2+4}, 
    			// Table
    			{1,4,2, 1+4}, {3,4,2, 0+4}, 
    			// Roof trim
    			{0,7,4, 0}, {0,7,3, 0}, {0,7,2, 0}, 
    			{1,7,7, 0}, {1,7,6, 0}, {1,7,5, 0}, {1,7,4, 2}, 
    			{5,7,8, 2}, {4,7,8, 2}, {3,7,8, 2}, {2,7,8, 2}, {1,7,8, 2}, 
    			{7,7,9, 2}, {6,7,9, 2}, {5,7,9, 0}, 
    			{8,7,7, 1}, {8,7,8, 1}, {8,7,9, 1}, 
    			{7,7,3, 1}, {7,7,4, 1}, {7,7,5, 1}, {7,7,6, 1}, {8,7,6, 3}, 
    			{3,7,2, 1}, {4,7,2, 3}, {5,7,2, 3}, {6,7,2, 3}, {7,7,2, 3}, 
    			{0,7,1, 3}, {1,7,1, 3}, {2,7,1, 3}, {3,7,1, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,3,2, 4,3,5}, {4,3,8, 4,3,8}, 
    			{5,3,5, 8,3,5}, 
    			{1,3,5, 1,3,5}, 
    			// Table
    			{2,4,2, 2,4,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
            // Black Terracotta
        	for (int[] uuvvww : new int[][]{
        		// Windows
        		{8,6,7, 8,6,8}, {6,6,9, 7,6,9}, {2,6,8, 3,6,8}, {1,6,6, 1,6,7}, {0,6,2, 0,6,3}, {1,6,1, 2,6,1}, {5,6,2, 6,6,2}, {8,6,7, 8,6,8}, 
        		{8,4,7, 8,4,8}, {6,4,9, 7,4,9}, {2,4,8, 3,4,8}, {1,4,6, 1,4,7}, {0,4,2, 0,4,3}, {1,4,1, 2,4,1}, {5,4,2, 6,4,2}, {8,4,7, 8,4,8}, 
        		// Door
        		{7,4,3, 7,6,3}, {7,6,4, 7,6,4}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 15),
        				Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 15), 
        				false);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{8,5,7}, {8,5,8}, {6,5,9}, {7,5,9}, {2,5,8}, {3,5,8}, {1,5,6}, {1,5,7}, {0,5,2}, {0,5,3}, {1,5,1}, {2,5,1}, {5,5,2}, {6,5,2}, {8,5,7}, {8,5,8}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Glass blocks
        	for (int[] uvw : new int[][]{
        		{3,7,7}, {5,7,7}, 
        		{2,7,6}, {3,7,6}, {5,7,6}, {6,7,6}, 
        		{2,7,4}, {3,7,4}, {5,7,4}, {6,7,4}, 
        		{3,7,3}, {5,7,3}, 
        		})
            {
    			this.setBlockState(world, Blocks.glass.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	{2,6,5}, {3,6,5}, {4,6,6}, {4,6,7}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Brick Walls
        	IBlockState biomeBrickWallState = null;
        	IBlockState biomeBrickBlockState = Blocks.brick_block.getDefaultState();
        	
        	// First, attempt to obtain modded brick wall
        	biomeBrickWallState = ModObjects.chooseModBrickWallState();
        	if (biomeBrickWallState==null)
        	{
        		// Use cobblestone
        		biomeBrickWallState = Blocks.cobblestone_wall.getStateFromMeta(0);
        		biomeBrickBlockState = Blocks.cobblestone.getStateFromMeta(0);
        	}
        	// Convert to biome-specific versions
        	biomeBrickWallState = StructureVillageVN.getBiomeSpecificBlockState(biomeBrickWallState, this.materialType, this.biome, this.disallowModSubs);
        	biomeBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(biomeBrickBlockState, this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uuvvww : new int[][]{
    			// Furnace chimney
    			{4,5,5, 4,6,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBrickWallState, biomeBrickWallState, false);	
    		}
            // Brick Blocks
        	for(int[] uuvvww : new int[][]{
    			// Roof plug
    			{4,7,5, 4,7,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBrickBlockState, biomeBrickBlockState, false);	
    		}
    		
    		
    		// Trapdoor (Bottom Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{  // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,4,6, 3}, {4,4,7, 3}, 
            	{2,4,5, 0}, {3,4,5, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{2,6,3}, 
            	{6,6,7}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,4,5, 2}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.furnace.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 7;
        	int chestV = 4;
        	int chestW = 7;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_fisher");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            
            // Potted random flower
            for (int[] uvw : new int[][]{
        		{1,5,2}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.yellow_flower, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.red_flower, randomPottedPlant);}          // Every other type of flower
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{7,4,4, 1, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
            
            // Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.leaves.getStateFromMeta(4), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{9,1,1, 9,1,1}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
        	
        	
        	// Barrels
    		IBlockState barrelState = ModObjects.chooseModBarrelBlockState();
    		boolean isChestType=(barrelState==null);
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel: -1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
            	
    			// Underneath
    			{1,1,7, 2,-1}, 
    			{2,2,8, 2,2}, 
    			{2,1,8, 2,1}, {3,1,8, 2,-1}, 
    			{7,1,3, 3,-1}, 
    			// At entrance
    			{8,4,5, 2,-1}, 
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (isChestType) {barrelState = Blocks.chest.getDefaultState();}
    			this.setBlockState(world, barrelState, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2])), barrelState.getBlock().getStateFromMeta(isChestType?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.coordBaseMode):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.coordBaseMode)), 2);
            }
    		
    		
        	// Patterned banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{9,3,5, 1, 15}, // Black
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
				world.setBlockState(bannerPos, Blocks.wall_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.coordBaseMode.getHorizontalIndex(), true)), 2);
				
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
    				ItemStack villageBanner = new ItemStack(Items.banner);
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
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Away-facing vines
        			{4,4,9, 0}, {4,3,9, 0}, {4,2,9, 0}, {4,1,9, 0}, 
        			// Player-facing side
        			{4,4,1, 2}, {4,3,1, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(20);
    			
    			int u;
    			int v = 4;
    			int w;
    			
    			if (s<12) // On the front portion of the building (near the table)
    			{
    				u = 1+(s%6);
    				w = 3+(s/6);
    			}
    			else // On the right portion of the building (near the chest)
    			{
    				u = 5+((s-12)/4);
    				w = 5+((s-12)%4);
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Fisher Cottage 2 --- //
    // designed by AstroTibs
    
    public static class SwampFisherCottage2 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"             ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			" FFFFFFFFFFF ",
    			"  F          ",
    			"             ",
    			"  F F   F    ",
    			"             ",
    			"  FF         ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 7;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampFisherCottage2() {}

    	public SwampFisherCottage2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampFisherCottage2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampFisherCottage2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		// Pier
        		{2,0,8, 2,2,8}, {4,0,8, 4,2,8}, {6,0,8, 6,3,8}, {8,0,8, 8,2,8}, {10,0,8, 10,2,8}, 
        		{2,0,6, 2,2,6}, {4,0,6, 4,5,6}, {6,0,6, 6,7,6}, {8,0,6, 8,5,6}, {10,0,6, 10,2,6}, 
        		{2,1,4, 2,2,4}, 
        		{2,1,2, 2,2,2}, {4,1,2, 4,5,2}, {8,1,2, 8,5,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), true);
        	IBlockState biomeStrippedLogHorizAcrossState = biomeLogHorAcrossState;
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log || biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.log)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.log2)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	{5,5,6, 5,5,6}, {7,5,6, 7,5,6}, 
            	{5,5,2, 7,5,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
            
            // Water boundary
    		// This puts up a dirt/grass wall to keep in the water if it's not level with the surrounding ground
            for(int[] uwuwvs : new int[][]{ // u/w box corners, v height, and the side that is to be evaluated.
            	// Side - 0:forward (away from you), 1:rightward, 2:backward (toward you), 3: leftward
    			
            	{1,10, 11,10, 0, 0}, // Forward side
    			{11,4, 11,11, 0, 1}, // Right side
    			{1,5, 11,5, 0, 2}, // Back side
    			{1,4, 1,11, 0, 3}, // Left side
    			})
    		{
            	int u_offset=0, w_offset=0;
            	int v = uwuwvs[4];
            	
            	switch(uwuwvs[5])
            	{
            	case 0: w_offset=1; break; // forward
            	case 1: u_offset=1; break; // rightward
            	case 2: w_offset=-1; break; // backward
            	case 3: u_offset=-1; break; // leftward
            	default:
            	}
            	
            	// Scan boundary and add containment if necessary
            	for (int u=uwuwvs[0]; u<=uwuwvs[2]; u++) {for (int w=uwuwvs[1]; w<=uwuwvs[3]; w++)
            	{
            		int x = this.getXWithOffset(u+u_offset, w+w_offset);
            		int y = this.getYWithOffset(v);
            		int z = this.getZWithOffset(u+u_offset, w+w_offset);
            		
            		// If space above bordering block is liquid, fill below with filler and cap with topper
            		if (world.getBlockState(new BlockPos(x, y+1, z)).getBlock().getMaterial().isLiquid())
            		{
            			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u+u_offset, v, w+w_offset, structureBB);
            			this.setBlockState(world, biomeTopState, u+u_offset, v+1, w+w_offset, structureBB);
            		}
            		// If bordering block is air, fill below with filler and cap with topper
            		else if (world.isAirBlock(new BlockPos(x, y, z)))
            		{
            			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u+u_offset, v-1, w+w_offset, structureBB);
            			this.setBlockState(world, biomeTopState, u+u_offset, v, w+w_offset, structureBB);
            		}
            	}}
    		}
            
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			{1,0,9, 11,0,10}, 
    			{1,0,8, 1,0,8}, {3,0,8, 3,0,8}, {5,0,8, 5,0,8}, {7,0,8, 7,0,8}, {9,0,8, 9,0,8}, {11,0,8, 11,0,8}, 
    			{1,0,7, 11,0,7}, 
    			{1,0,6, 1,0,6}, {3,0,6, 3,0,6}, {5,0,6, 5,0,6}, {7,0,6, 7,0,6}, {9,0,6, 9,0,6}, {11,0,6, 11,0,6}, 
    			{1,0,5, 11,0,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);	
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{2,2,3, 2,2,3}, {2,2,5, 2,2,5}, {2,2,7, 2,2,7}, 
            	{3,2,2, 3,2,8}, 
            	{4,2,3, 4,3,5}, {4,2,7, 4,2,7}, {4,4,3, 4,4,3}, {4,4,5, 4,4,5}, {4,5,3, 4,5,5}, 
            	{5,2,2, 5,2,8}, {5,3,2, 5,3,2}, {5,6,6, 5,6,6}, 
            	{6,2,2, 6,2,5}, {6,2,7, 6,2,7}, {6,3,2, 6,4,2}, {5,6,2, 7,6,2}, 
            	{7,2,2, 7,2,8}, {7,3,2, 7,3,2}, {7,3,6, 7,3,6}, {7,6,6, 7,6,6}, 
            	{8,2,3, 8,3,5}, {8,4,3, 8,4,3}, {8,4,5, 8,4,5}, {8,5,3, 8,5,5}, {8,2,7, 8,2,7}, 
            	{9,2,6, 9,2,8}, 
            	{10,2,7, 10,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Entrance
    			{2,2,1, 3}, {3,2,1, 3}, 
    			{2,1,0, 3}, {3,1,0, 3}, 
    			// Roof
    			{8,6,2, 1}, {8,6,3, 1}, {8,6,4, 1}, {8,6,5, 1}, {8,6,6, 1}, 
    			{7,7,2, 1}, {7,7,3, 1}, {7,7,4, 1}, {7,7,5, 1}, {7,7,6, 1}, 
    			{5,7,2, 0}, {5,7,3, 0}, {5,7,4, 0}, {5,7,5, 0}, {5,7,6, 0}, 
    			{4,6,2, 0}, {4,6,3, 0}, {4,6,4, 0}, {4,6,5, 0}, {4,6,6, 0}, 
    			// Porch
    			{9,3,7, 2}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Under stairs
    			{2,1,1, 3,1,1}, 
    			// Table
    			{6,3,3, 6,3,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{6,8,2, 6,8,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{5,4,2}, {7,4,2}, 
        		{4,4,4}, {8,4,4}, 
        		{7,4,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	{6,7,4}, {6,6,4}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{6,5,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{6,4,8}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{5,3,3, 1, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
        		{7,3,3, 3, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
        	
        	
        	// Potted plant
        	for (int[] uvws : new int[][]{ // u,v,w, sapling
        		{5,4,3, 5}, // Dark Oak
        		{7,4,3, 0}, // Oak
           		})
        	{
            	StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, 
            			new BlockPos(this.getXWithOffset(uvws[0], uvws[2]), 
            			this.getYWithOffset(uvws[1]), 
            			this.getZWithOffset(uvws[0], uvws[2])), 
            			Blocks.sapling, uvws[3]);
        	}
            
            
        	// Carpet
        	for(int[] uvwc : new int[][]{
        		// Lower
        		{6,3,4, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		})
            {
            	this.setBlockState(world, Blocks.carpet.getStateFromMeta(uvwc[3]), uvwc[0], uvwc[1], uvwc[2], structureBB);
            }
        	
        	
        	// Barrels
    		IBlockState barrelState = ModObjects.chooseModBarrelBlockState();
    		boolean isChestType=(barrelState==null);
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel: -1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
            	
    			{8,3,7, 0,-1}, 
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (isChestType) {barrelState = Blocks.chest.getDefaultState();}
    			this.setBlockState(world, barrelState, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2])), barrelState.getBlock().getStateFromMeta(isChestType?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.coordBaseMode):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.coordBaseMode)), 2);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{5,3,6, 0, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(19);
    			
    			int u;
    			int v = 3;
    			int w;
    			
				u = s<=5? 5+(s%3) : s<=9? 4+((s-6)%2) : s<=10? 6 : s<=12? 7 : s<=15? s-5 : s<=17? 10 : 9;
				w = s<=5? 4+(s/3) : s<=9? 7+((s-6)/2) : s<=10? 7 : s<=12? s-4 : s<=15? 8 : s<=17? s-10 : 6;
    			
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Fletcher House --- //
    // designed by THASSELHOFF
    
    public static class SwampFletcherHouse extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"            ",
    			"      F     ",
    			"   F     F  ",
    			"            ",
    			"      F     ",
    			"  F  FFF  F ",
    			"      F     ",
    			"            ",
    			" F F     F  ",
    			"FF    F     ",
    			"PFFF        ",
    			"PPF         ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 8;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 8;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampFletcherHouse() {}

    	public SwampFletcherHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampFletcherHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampFletcherHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		// Entrance
        		{0,1,2, 0,1,2}, {2,1,0, 2,1,0}, 
        		{1,1,3, 1,2,3}, {3,1,1, 3,2,1}, 
        		{2,3,4, 2,3,4}, {4,3,2, 4,3,2}, 
        		{3,4,5, 3,6,5}, {5,4,3, 5,6,3}, 
        		// Supports
        		{6,1,10, 6,2,10}, 
        		{3,1,9, 3,2,9}, {9,1,9, 9,2,9}, 
        		{6,1,7, 6,2,7},
        		{2,1,6, 2,2,6}, {5,1,6, 5,2,6}, {6,1,6, 6,1,6}, {7,1,6, 7,2,6}, {10,1,6, 10,2,6}, 
        		{6,1,5, 6,2,5}, 
        		{3,1,3, 3,2,3}, {9,1,3, 9,2,3}, 
        		{6,1,2, 6,2,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,3,5, 1,3,7}, 
            	{2,3,8, 2,3,9}, {3,3,9, 3,3,10}, {4,3,10, 4,3,10}, 
            	{5,3,11, 7,3,11}, 
            	{10,3,8, 10,3,9}, {9,3,9, 9,3,10}, {8,3,10, 8,3,10}, 
            	{11,3,5, 11,3,7}, 
            	{8,3,2, 9,3,2}, {9,3,3, 10,3,3}, {10,3,4, 10,3,4}, 
            	{5,3,1, 7,3,1}, 
            	// House supports
            	{7,4,9, 7,6,9}, {9,4,7, 9,6,7}, 
            	// Floor
            	{3,3,5, 3,3,7}, 
            	{4,3,4, 4,3,8}, 
            	{5,3,3, 5,3,9}, 
            	{6,3,3, 6,3,5}, {6,3,7, 6,3,9}, 
            	{7,3,3, 7,3,9}, 
            	{8,3,4, 8,3,8}, 
            	{9,3,5, 9,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Entryway
    			{2,1,1, 2,1,1}, {1,1,2, 1,1,2}, 
    			// Under main area
    			{1,2,5, 1,2,5}, {1,2,7, 1,2,7}, 
    			{2,2,3, 2,2,5}, {2,2,7, 2,2,9}, 
    			{3,2,2, 3,2,2}, {3,2,4, 3,2,8}, {3,2,10, 3,2,10}, 
    			{4,2,2, 4,2,10}, 
    			{5,2,1, 5,2,5}, {5,2,7, 5,2,11}, 
    			{6,2,3, 6,2,4}, {6,2,8, 6,2,9}, 
    			{7,2,1, 7,2,5}, {7,2,7, 7,2,11}, 
    			{8,2,2, 8,2,10}, 
    			{9,2,2, 9,2,2}, {9,2,4, 9,2,8}, {9,2,10, 9,2,10}, 
    			{10,2,3, 10,2,5}, {10,2,7, 2,2,9}, 
    			{11,2,5, 11,2,5}, {11,2,7, 11,2,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Entryway
    			{1,1,1, 1,1,1}, {2,2,2, 2,2,2}, {3,3,3, 3,3,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
    		
    		
    		// Cobblestone Slab (upper)
    		IBlockState biomeCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3+8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,6,5, 1,6,6}, 
    			{2,6,4, 2,6,7}, 
    			{3,6,4, 3,6,4}, 
    			{3,6,9, 4,6,10}, 
    			{5,6,10, 5,6,11}, 
    			{6,6,11, 7,6,11}, 
    			{8,6,10, 9,6,10}, 
    			{9,6,9, 9,6,9}, 
    			
    			{4,6,2, 4,6,2}, 
    			{5,6,1, 6,6,2}, {7,6,1, 7,6,1}, 
    			{8,6,2, 9,6,2}, 
    			{9,6,3, 9,6,3}, 
    			{10,6,3, 10,6,7}, {11,6,5, 11,6,7}, 
    			
    			{5,7,7, 5,7,7}, {7,7,7, 7,7,7}, 
    			{5,7,5, 5,7,5}, {7,7,5, 7,7,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabUpperState, biomeCobblestoneSlabUpperState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (upper)
    		IBlockState biomeMossyCobblestoneSlabUpperState = ModObjects.chooseModMossyCobblestoneSlabState(true);
    		if (biomeMossyCobblestoneSlabUpperState != null)
    		{
    			biomeMossyCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabUpperState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabUpperState = biomeCobblestoneSlabUpperState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{1,6,7, 1,6,7}, 
    			{2,6,3, 2,6,3}, {2,6,8, 2,6,9}, 
    			{3,6,2, 3,6,3}, {3,6,8, 3,6,8}, 
    			{7,6,2, 7,6,2}, 
    			{6,6,10, 7,6,10}, 
    			{10,6,8, 10,6,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabUpperState, biomeMossyCobblestoneSlabUpperState, false);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,7,5, 3,7,8}, 
    			{4,7,3, 4,7,4}, {4,7,8, 4,7,9}, 
    			{5,7,3, 6,7,3}, {5,7,9, 8,7,9}, 
    			{8,7,4, 8,7,4}, {9,7,4, 9,7,8}, {8,7,8, 8,7,8}, 
    			
    			{6,8,5, 6,8,5}, {5,8,6, 7,8,6}, {6,8,7, 6,8,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{3,7,4, 3,7,4}, 
    			{7,7,3, 8,7,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,7,7, 5,7,7}, 
    			{5,7,8, 5,7,8}, 
    			{7,7,7, 7,7,8}, {7,7,4, 7,7,5}, 
    			{8,7,5, 8,7,7}, 
    			{5,7,4, 5,7,5}, {6,7,4, 6,7,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,7,5, 4,7,6}, 
    			{6,7,8, 6,7,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{6,7,6, 6,7,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
            
            
            // Glass blocks
        	for (int[] uuvvww : new int[][]{
        		{3,4,6, 3,6,7}, {4,4,8, 4,6,8}, {5,4,9, 6,6,9}, 
        		{6,4,3, 7,6,3}, {8,4,4, 8,6,4}, {9,4,5, 9,6,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.glass.getStateFromMeta(0), Blocks.glass.getStateFromMeta(0), false);
            }
    		
    		
    		// Trapdoor (Bottom Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{8,4,9, 0}, {8,5,9, 0}, 
            	{9,4,8, 1}, {9,5,8, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Trapdoor (Top Vertical)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{8,6,9, 0}, 
            	{9,6,8, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{6,6,6}, 
            	{6,2,1}, 
            	{1,2,6}, {11,2,6}, 
            	{6,2,11}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{1,3,3}, {3,3,1}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		{4,4,5, 4,4,7, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{5,4,4, 7,4,4, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{8,4,5, 8,4,7, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{5,4,8, 7,4,8, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet.getStateFromMeta(uuvvww[6]), Blocks.carpet.getStateFromMeta(uuvvww[6]), false);	
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{5,3,2, 0, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{6,3,2, 1, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{7,3,2, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{8,3,3, 3, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{9,3,4, 0, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{10,3,5, 1, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{10,3,6, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{10,3,7, 3, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{9,3,8, 0, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{8,3,9, 1, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{7,3,10, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{6,3,10, 3, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{5,3,10, 0, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{4,3,9, 1, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{3,3,8, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{2,3,7, 3, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{2,3,6, 0, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{2,3,5, 1, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{3,3,4, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
        		{4,3,3, 3, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }

            
            // Fletching Table
        	IBlockState fletchingTableState = ModObjects.chooseModFletchingTableState();
        	this.setBlockState(world, fletchingTableState, 8, 4, 8, structureBB);
    		
            
        	// Carpet
        	for(int[] uvwc : new int[][]{
        		// Lower
        		{6,3,4, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		})
            {
            	this.setBlockState(world, Blocks.carpet.getStateFromMeta(uvwc[3]), uvwc[0], uvwc[1], uvwc[2], structureBB);
            }
        	
        	
        	// Water
        	for (int[] uvw : new int[][]{
            	{6,2,6}, 
            	}) {
            	this.setBlockState(world, Blocks.flowing_water.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// TNT
        	for (int[] uvw : new int[][]{
            	{6,3,6}, 
            	}) {
            	this.setBlockState(world, Blocks.tnt.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 6;
        	int chestV = 4;
        	int chestW = 6;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.trapped_chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_fletcher");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Right side
        			{10,1,3, 1}, {11,3,3, 1}, {11,3,8, 1}, {11,3,9, 1}, 
        			// Player-facing side
        			{9,3,1, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(20);
    			
    			int u;
    			int v = 4;
    			int w;
    			
				u = s<=2? 4 : s<=7? 5 : s<=11? 6 : s<=16? 7 : 8;
				w = s<=2? s+5 : s<=7? s+1 : s<=9? s-4 : s<=11? s-3 : s<=16? s-8 : s-12;
    			
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 3, 0); // Shepherd
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Horrible Secret --- //
    // designed by AstroTibs
    
    public static class SwampHorribleSecret extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
//        private static final String[] foundationPattern = new String[]{
//    			"FFFFFFF ",
//    			"FFFFFFF ",
//    			"FFFFFFF ",
//    			"FFFFFFFF",
//    			"FFFFFFFF",
//    			"FFFFFFFF",
//    			"FFFFFFF ",
//    			"FFFFFF  ",
//        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = 8;//foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = 8;//foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 28;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 22; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampHorribleSecret() {}

    	public SwampHorribleSecret(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampHorribleSecret buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampHorribleSecret(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel <= this.STRUCTURE_HEIGHT+5) {return true;} // Do not construct if ground level is too low
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
    		// Clear space above
    		for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
    			this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
    		}}
    		
            
            // Grass
            for(int[] uuvvww : new int[][]{
    			{3,21,6, 5,21,6}, 
    			{2,21,5, 6,21,5}, 
    			{1,21,3, 7,21,4}, 
    			{1,21,2, 2,21,2}, {6,21,2, 7,21,2}, 
    			{2,21,1, 6,21,1}, 
    			{3,21,0, 5,21,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }

    		        	
        	// Clear out underground area
            for(int[] uuvvww : new int[][]{
    			{3,1,0, 5,20,0}, 
    			{0,1,1, 6,9,7}, 
    			{7,1,2, 7,20,4}, 
    			{2,10,1, 6,20,1}, 
    			{1,10,2, 7,20,4}, 
    			{2,10,5, 6,20,5}, 
    			{3,10,6, 5,20,6}, 
    			})
    		{
            	this.fillWithAir(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5]);	
    		}
            
            
            // Coarse Dirt
            for(int[] uuvvww : new int[][]{
            	// Covered up hole in the ground
            	{3,21,2, 5,21,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.dirt.getStateFromMeta(1), Blocks.dirt.getStateFromMeta(1), false);	
            }
            
            
            // Dirt
            for(int[] uuvvww : new int[][]{
            	// Dirt top
    			{3,19,6, 5,20,6}, 
    			{2,19,5, 6,20,5}, 
    			{1,19,2, 2,20,4}, {6,19,2, 7,20,4}, 
    			{2,19,1, 6,20,1}, 
    			{3,19,0, 5,20,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);	
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Central column
    			{3,0,0, 5,18,0}, // Front wall
    			{2,10,1, 6,18,1}, 
    			{1,10,2, 1,18,4}, {2,10,2, 2,18,5}, 
    			{7,0,2, 7,18,4}, {6,10,2, 6,18,5}, 
    			{3,10,5, 5,18,6}, 
    			{0,0,2, 2,9,2}, 
    			{0,0,1, 0,9,7}, 
    			{1,0,3, 1,9,3}, 
    			{1,0,4, 1,1,7}, {1,2,5, 1,2,7}, {1,3,4, 1,5,7}, 
    			{3,0,2, 6,0,2}, 
    			{4,1,2, 6,2,2}, 
    			{3,3,2, 6,6,2},
    			{2,7,3, 2,9,3}, 
    			{6,7,2, 6,9,7}, {6,6,3, 6,6,3}, {6,6,5, 6,6,7}, 
    			{6,0,3, 6,5,7}, 
    			{1,6,7, 5,9,7}, 
    			{1,9,4, 2,9,6}, {3,9,5, 5,9,6}, {5,9,4, 5,9,4}, 
    			{3,7,2, 3,7,2}, 
    			{3,8,4, 3,8,4}, {5,10,2, 5,10,2}, 
    			{3,11,2, 3,11,2}, {3,12,4, 3,12,4}, {5,13,4, 5,13,4}, 
    			{5,14,2, 5,14,2}, {3,15,2, 3,15,2}, 
    			{3,16,4, 3,16,4}, {5,17,4, 5,17,4}, {5,18,2, 5,18,2}, 
    			// Stairwell center
    			{4,7,3, 4,20,3}, 
    			// Basement chamber
    			{1,0,1, 6,9,1}, 
    			{2,0,7, 5,5,7}, 
    			{2,0,6, 2,5,6}, {3,0,6, 3,2,6}, {3,4,6, 3,5,6}, {4,0,6, 5,5,6}, 
    			{5,0,3, 5,5,3}, {5,0,4, 5,3,4}, {5,5,4, 5,5,4}, {5,0,5, 5,5,5}, 
    			{2,0,3, 4,0,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		// Replaces regular stone because I lost my mind trying to set the coordinates
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uvw : new int[][]{
    			// Tower
    			{3,18,6}, {4,18,6}, {5,18,6}, 
    			{2,18,5}, {3,18,5}, {4,18,5}, {5,18,5}, {6,18,5}, 
    			{1,18,4}, {2,18,4}, {6,18,4}, {7,18,4}, 
    			{1,18,3}, {2,18,3}, {6,18,3}, {7,18,3}, 
    			{1,18,2}, {2,18,2}, {6,18,2}, {7,18,2}, 
    			{2,18,1}, {3,18,1}, {4,18,1}, {5,18,1}, {6,18,1}, 
    			{3,18,0}, {4,18,0}, {5,18,0}, 
    			
    			{3,17,6}, {4,17,6}, {5,17,6}, 
    			{2,17,5}, {3,17,5}, {4,17,5}, {5,17,5}, {6,17,5}, 
    			{1,17,4}, {2,17,4}, {6,17,4}, {7,17,4}, 
    			{1,17,2}, {2,17,2}, {6,17,2}, {7,17,2}, 
    			{2,17,1}, {3,17,1}, {4,17,1}, {5,17,1}, {6,17,1}, 
    			{3,17,0}, {4,17,0}, {5,17,0}, 
    			
    			{3,16,6}, {5,16,6}, 
    			{2,16,5}, {3,16,5}, {5,16,5}, {6,16,5}, 
    			{1,16,4}, {2,16,4}, 
    			{1,16,3}, {2,16,3}, {6,16,3}, {7,16,3}, 
    			{6,16,2}, {7,16,2}, 
    			{2,16,1}, {3,16,1}, {4,16,1}, {6,16,1}, 
    			{3,16,0}, {4,16,0}, 
    			
    			{3,18,6}, {4,15,6}, 
    			{2,15,5}, {3,15,5}, {4,15,5}, {6,15,5}, 
    			{6,15,4}, {7,15,4}, 
    			{1,15,3}, {2,15,3}, {6,15,3}, {7,15,3}, 
    			{1,15,2}, {2,15,2}, {6,15,2}, {7,15,2}, 
    			{3,15,1}, {4,15,1}, {5,15,1}, 
    			{3,15,0}, {4,15,0}, {5,15,0}, 
    			
    			{4,14,6}, {5,14,6}, 
    			{2,14,5}, {4,14,5}, {5,14,5}, 
    			{6,14,4}, {7,14,4}, 
    			{1,14,3}, {2,14,3}, 
    			{6,14,2}, {7,14,2}, 
    			{2,14,1}, {4,14,1}, {6,14,1}, 
    			{4,14,0}, 
    			
    			{5,13,6}, 
    			{2,13,5}, {5,13,5}, 
    			{1,13,4}, {2,13,4}, {6,13,4}, {7,13,4}, 
    			{1,13,2}, {2,13,2}, {6,13,2}, {7,13,2}, 
    			{3,13,1}, {6,13,1}, 
    			{3,13,0}, 
    			
    			{3,12,6}, 
    			{3,12,5}, {6,12,5}, 
    			{1,12,4}, {2,12,4}, 
    			{6,12,2}, {7,12,2}, 
    			{2,12,1}, {5,12,1}, 
    			{5,12,0}, 
    			
    			{3,11,6}, 
    			{3,11,5}, {6,11,5}, 
    			{1,11,3}, {2,11,3}, 
    			{6,11,2}, {7,11,2}, 
    			{3,11,1}, 
    			{3,11,0}, 
    			
    			// Chamber
    			
    			{4,10,6}, 
    			{4,10,5}, 
    			{1,10,4}, {2,10,4}, {6,10,4}, {7,10,4}, 
    			{3,10,1}, {5,10,1}, 
    			{3,10,0}, {5,10,0}, 
    			
    			{6,9,2}, {7,9,2}, 
    			{2,8,7}, {4,8,7}, {6,8,6}, 
    			{1,6,3}, {0,6,6}, {1,6,7}, {5,6,7}, 
    			{1,5,6}, {2,5,6}, 
    			
    			{2,0,3}, {4,0,3}, 
    			
    			// Center column
    			{4,18,3}, 
    			{4,16,3}, 
    			{4,12,3}, 
    			{4,7,3}, 
    			})
    		{
            	this.setBlockState(world, biomeMossyCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{3,20,2, 1}, 
    			{4,19,2, 1}, 
    			{5,18,3, 2}, 
    			{4,17,4, 0}, 
    			{3,16,3, 3}, 
    			{4,15,2, 1},  
    			{5,14,3, 2}, 
    			{4,13,4, 0}, 
    			{3,12,3, 3}, 
    			{4,11,2, 1},  
    			{5,10,3, 2}, 
    			{4,9,4, 0}, 
    			{3,8,3, 3}, 
    			{4,7,2, 1}, 
    			{5,6,3, 2}, 
    			
    			{4,16,4, 1+4}, 
    			{4,14,2, 0+4},  
    			{5,13,3, 3+4}, 
    			{3,11,3, 2+4}, 
    			{4,10,2, 0+4},  
    			{5,9,3, 3+4}, 
    			{4,8,4, 1+4}, 
    			{3,7,3, 2+4}, 
        		})
            {
            	this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
    		// Mossy Cobblestone stairs
    		Block biomeMossyCobblestoneStairsBlock = ModObjects.chooseModMossyCobblestoneStairsBlock();
    		if (biomeMossyCobblestoneStairsBlock==null) {biomeMossyCobblestoneStairsBlock = Blocks.stone_stairs;}
    		biomeMossyCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{3,19,2, 0+4}, 
    			{4,18,2, 0+4}, 
    			{5,17,3, 3+4}, 
    			{3,15,3, 2+4}, 
    			{4,12,4, 1+4},
    			
    			{2,8,4, 0+4}, 
    			})
    		{
    			this.setBlockState(world, biomeMossyCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// On the central column
    			{4,19,4, 0}, 
    			{4,17,2, 2}, 
    			{4,15,4, 0}, 
    			{4,13,2, 2}, 
    			{4,11,4, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Redstone Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Walls of the chamber
    			{1,7,5, 1}, 
    			{3,7,6, 2}, 
    			{5,7,5, 3}, 
    			// Walls of the pit
    			{5,3,4, 3}, 
    			{3,3,6, 2}, 
    			{1,2,4, 1}, 
    			{3,1,2, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.redstone_torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
        	
        	
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	{2,1,5, 4,5,5}, 
            	{2,1,4, 2,5,4}, {4,1,4, 4,5,4}, 
            	{2,1,3, 4,5,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            }
        	
        	
        	// Dispenser
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{6,6,4, 3}, 
            	})
            {
            	int x = this.getXWithOffset(uvwo[0], uvwo[2]);
            	int y = this.getYWithOffset(uvwo[1]);
            	int z = this.getZWithOffset(uvwo[0], uvwo[2]);
            	
                // Set contents
            	BlockPos pos = new BlockPos(x, y, z);
                if (structureBB.isVecInside(pos) && world.getBlockState(pos) != Blocks.dispenser)
                {
                	world.setBlockState(new BlockPos(uvwo[0], uvwo[1], uvwo[2]), Blocks.dispenser.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
                	//world.setBlockMetadataWithNotify(x, y, z, StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode), 2);
                	
                    TileEntityDispenser tileentitydispenser = (TileEntityDispenser)world.getTileEntity(pos);

                    if (tileentitydispenser != null)
                    {
                    	// Potion IDs taken from https://www.minecraftinfo.com/IDList.htm
                    	int potionID;
                    	
                    	switch(random.nextInt(5))
                    	{
                    	default:
                    	case 0: potionID = 16385; break; // Splash potion of Regeneration (33 sec)
                    	case 1: potionID = 16389; break; // Splash potion of Healing
                    	case 2: potionID = 16392; break; // Splash potion of Weakness (1m07s)
                    	case 3: potionID = 16394; break; // Splash potion of Slowness (1m07s)
                    	case 4: potionID = 16396; break; // Splash potion of Harming
                    	}
                    	
                    	ItemStack dispenserItem = new ItemStack(Items.potionitem, 1, potionID);
                    	
                    	tileentitydispenser.setInventorySlotContents(random.nextInt(tileentitydispenser.getSizeInventory()), dispenserItem);
                    }
                }
            }
            
                    	
            // Stone pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{5,6,4, 5,6,4}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{4,22,1}, 
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
            	
            	// Spawn Zombie
                int u = 3;
                int v = 1;
                int w = 4;
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
                EntityZombie zombie = new EntityZombie(world);
                zombie.heal(zombie.getMaxHealth());
                zombie.setLocationAndAngles(
                		(double)x + 0.5D,
                		(double)y + 0.5D,
                		(double)z + 0.5D,
                		random.nextFloat()*360, 0.0F);
                zombie.enablePersistence();
                zombie.setVillager(true);
                
                if (GeneralConfig.nameEntities)
            	{
            		String[] villager_name_a = NameGenerator.newRandomName("villager", random);
            		zombie.setCustomNameTag((villager_name_a[1]+" "+villager_name_a[2]+" "+villager_name_a[3]).trim());
            	}
                
                //if(GeneralConfig.modernZombieSkins) (ExtendedZombieVillager.get( zombie )).setProfession(2); // v3.2.3
                                                
                world.spawnEntityInWorld(zombie);
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Hut Farm --- //
    // designed by THASSELHOFF
    
    public static class SwampHutFarm extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"     FFFFFFFF",
    			"     FFFFFFFF",
    			" FFFFFFFFFFFF",
    			" FFFFFFFFFFFF",
    			" FFFFFFFFFFFF",
    			" FFFFFFFFFFFF",
    			" FFFFFFFFFFFF",
    			"   F FFFFFFFF",
    			"   F FFFFFFFF",
    			"   F         ",
    			"   F         ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 7;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampHutFarm() {}

    	public SwampHutFarm(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampHutFarm buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampHutFarm(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		{1,1,8, 1,5,8}, {5,1,8, 5,5,8}, 
        		{1,1,4, 1,5,4}, {5,1,4, 5,5,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Back wall
            	{2,4,8, 2,5,8}, {3,4,8, 3,4,8}, {4,4,8, 4,5,8}, 
            	// Left wall
            	{1,4,5, 1,5,5}, {1,4,6, 1,4,6}, {1,4,7, 1,5,7}, 
            	// Right wall
            	{5,4,5, 5,5,5}, {5,4,6, 5,4,6}, {5,4,7, 5,5,7}, 
            	// Front
            	{2,4,4, 2,5,4}, {4,4,4, 4,5,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Entrance
    			{3,1,1, 3}, {3,2,2, 3}, {3,3,3, 3}, 
    			{3,1,2, 2+4}, {3,2,3, 2+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,3,8, 2,3,8}, {4,3,8, 4,3,8}, 
    			{1,3,7, 5,3,7}, 
    			{2,3,6, 4,3,6}, 
    			{1,3,5, 5,3,5}, 
    			{2,3,4, 4,3,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	{3,5,8}, 
            	{1,5,6}, {5,5,6}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,0,4, 1,0,8}, 
    			{2,0,4, 2,0,4}, {2,0,8, 2,0,8}, 
    			{3,0,0, 3,0,3}, 
    			{5,0,2, 5,0,4}, {4,0,9, 5,0,10}, 
    			{6,0,8, 6,0,8}, 
    			{7,0,5, 7,0,5}, {7,0,7, 7,0,7}, 
    			{9,0,5, 9,0,5}, 
    			{11,0,3, 11,0,3}, {11,0,9, 11,0,9}, 
    			{9,0,10, 10,0,10}, 
    			{8,0,2, 12,0,2}, 
    			{12,0,2, 12,0,2}, {12,0,6, 12,0,7}, 
    			// Hut roof
    			{1,6,8, 4,6,8}, 
    			{1,6,6, 1,6,7}, {5,6,5, 5,6,7}, 
    			{2,6,4, 4,6,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,0,4, 4,0,4}, {3,0,8, 4,0,8}, 
    			{6,0,2, 7,0,2}, {6,0,4, 6,0,4}, {6,0,10, 8,0,10}, 
    			{7,0,7, 7,0,7}, {8,0,8, 8,0,8}, {8,0,4, 8,0,4}, 
    			{9,0,7, 9,0,7}, 
    			{10,0,3, 10,0,5}, {10,0,8, 10,0,10}, 
    			{11,0,10, 11,0,10}, 
    			{12,0,3, 12,0,5}, {12,0,8, 12,0,10}, 
    			// Hut roof
    			{5,6,4, 5,6,4}, {5,6,8, 5,6,8}, 
    			{1,6,4, 1,6,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,6,9, 4,6,9}, 
    			{0,6,6, 0,6,8}, 
    			{6,6,4, 6,6,4}, {6,6,7, 6,6,8}, 
    			{1,6,3, 6,6,3}, 
    			{3,7,5, 4,7,5}, {4,7,6, 4,7,6}, {2,7,7, 4,7,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{5,6,9, 6,6,9}, 
    			{0,6,3, 0,6,5}, {6,6,5, 6,6,6}, 
    			
    			{2,7,5, 2,7,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (upper)
    		IBlockState biomeMossyCobblestoneSlabUpperState = ModObjects.chooseModMossyCobblestoneSlabState(true);
    		if (biomeMossyCobblestoneSlabUpperState != null)
    		{
    			biomeMossyCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabUpperState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3+8), this.materialType, this.biome, this.disallowModSubs);
    		}
    		for(int[] uuvvww : new int[][]{
    			{3,7,6, 3,7,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabUpperState, biomeMossyCobblestoneSlabUpperState, false);	
    		}
    		
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			// Under hut
    			{3,0,5, 3,0,7}, 
    			// Center of patch
    			{8,0,6, 8,0,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);	
    		}
    		
            
            // Moist Farmland with crop above
            Block[] cropPair1 = StructureVillageVN.chooseCropPair(random);
        	Block[] cropPair2 = StructureVillageVN.chooseCropPair(random);
        	for(int[] uvwfcp : new int[][]{
            	// u,v,w, farmland moisture (0:dry, 7:moist), crop, crop progress
            	// Left patch
            	{6,0,5, 7,0,random.nextInt(3)}, {6,0,6, 7,0,random.nextInt(3)}, {6,0,7, 7,0,random.nextInt(3)}, 
            	{7,0,6, 7,0,random.nextInt(3)}, 
            	// Right patch
            	{9,0,6, 7,1,random.nextInt(3)}, 
            	{10,0,5, 7,1,random.nextInt(3)}, {10,0,6, 7,1,random.nextInt(3)}, {10,0,7, 7,1,random.nextInt(3)}, 
            	{11,0,4, 7,1,random.nextInt(3)}, {11,0,5, 7,1,random.nextInt(3)}, {11,0,6, 7,1,random.nextInt(3)}, {11,0,7, 7,1,random.nextInt(3)}, {11,0,8, 7,1,random.nextInt(3)}, 
            	// Front patch
            	{8,0,5, 7,2,random.nextInt(3)}, 
            	{7,0,4, 7,2,random.nextInt(3)}, {8,0,4, 7,2,random.nextInt(3)}, {9,0,4, 7,2,random.nextInt(3)}, 
            	{6,0,3, 7,2,random.nextInt(3)}, {7,0,3, 7,2,random.nextInt(3)}, {8,0,3, 7,2,random.nextInt(3)}, {9,0,3, 7,2,random.nextInt(3)}, {10,0,3, 7,2,random.nextInt(3)}, 
            	// Back patch
            	{8,0,7, 7,3,random.nextInt(3)}, 
            	{7,0,8, 7,3,random.nextInt(3)}, {8,0,8, 7,3,random.nextInt(3)}, {9,0,8, 7,3,random.nextInt(3)}, 
            	{6,0,9, 7,3,random.nextInt(3)}, {7,0,9, 7,3,random.nextInt(3)}, {8,0,9, 7,3,random.nextInt(3)}, {9,0,9, 7,3,random.nextInt(3)}, {10,0,9, 7,3,random.nextInt(3)}, 
            	})
            {
    			int cropProgressMeta = uvwfcp[5]; // Isolate the crop's age meta value
    			IBlockState cropState;
    			
    			while(true)
    			{
    				try {cropState = ((uvwfcp[4]/2==0?cropPair1:cropPair2)[uvwfcp[4]%2]).getStateFromMeta(cropProgressMeta);}
    				catch (IllegalArgumentException e)
    				{
    					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
    					if (cropProgressMeta==0) {cropState = Blocks.wheat.getStateFromMeta(uvwfcp[5]);}
    					// The crop is not allowed to have this value. Cut it in half and try again.
    					else {cropProgressMeta /= 2; continue;}
    				}
    				
    				// Finally, assign the working crop
					this.setBlockState(world, cropState, uvwfcp[0], uvwfcp[1]+1, uvwfcp[2], structureBB);
    				break;
    			}
    			
            	this.setBlockState(world, Blocks.farmland.getStateFromMeta(uvwfcp[3]), uvwfcp[0], uvwfcp[1], uvwfcp[2], structureBB);
            }
    		
    		
    		// Lilypad
    		for (int[] uvw : new int[][]{
    			// Under hut
    			{3,1,5}, {3,1,7}, 
    			// Center of patch
    			{8,1,6}, 
    			}) {
    			this.setBlockState(world, Blocks.waterlily.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
            // Dirt
            for(int[] uuvvww : new int[][]{
    			{2,0,5, 2,0,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);	
            }
            
            
            // Sugarcane
    		for(int[] uuvvww : new int[][]{
    			{2,1,5, 2,1,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.reeds.getDefaultState(), Blocks.reeds.getDefaultState(), false);	
    		}
            
            
            // Pumpkin with dirt underneath
            for(int[] uvw : new int[][]{
            	{4,1,5}, {4,1,6}, {4,1,7}, 
            	})
            {
            	this.setBlockState(world, Blocks.pumpkin.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Pumpkin stem with farmland underneath
            for(int[] uvw : new int[][]{
            	{5,1,5}, {5,1,6}, {5,1,7}, 
            	})
            {
            	this.setBlockState(world, Blocks.farmland.getStateFromMeta(7), uvw[0], uvw[1]-1, uvw[2], structureBB);
            	this.setBlockState(world, Blocks.pumpkin_stem.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            }
    		
    		
    		// Attempt to add GardenCore Compost Bins. If this fails, nothing
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            if (compostBinState != null)
            {
            	for(int[] uvw : new int[][]{
        			{5,1,2}, 
        			})
        		{
        			this.setBlockState(world, compostBinState, uvw[0], uvw[1], uvw[2], structureBB);	
        		}
            }
            
            
            // Hay bales (vertical)
        	for (int[] uvw : new int[][]{
        		{2,4,6}, {2,4,7}, {2,5,7}, 
        		})
            {
        		this.setBlockState(world, Blocks.hay_block.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Hay bales (along)
        	for (int[] uvw : new int[][]{
        		{3,4,7}, 
        		})
            {
        		this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(Blocks.hay_block.getStateFromMeta(0), this.coordBaseMode.getHorizontalIndex(), false), uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{3,6,5, 0}, 
    			}) {
    			this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		{3,3,8}, 
        		{1,3,6}, {5,3,6}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 4;
        	int chestW = 5;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_farm");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{4,1,10}, {12,1,10}, 
            	{12,1,2}, 
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int u = 8;
    			int v = 1;
    			int w = 6;
    			
    			while (
    					(u==8 && w==6)
    					|| (u>=11 && w>=9)
    					|| (u>=11 && w<=3)
    					)
    			{
    				u = 6+random.nextInt(7);
    				w = 2+random.nextInt(9);
    			}
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Large House --- //
    // designed by AstroTibs
    
    public static class SwampLargeHouse extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"         ",
    			" FF    FF",
    			"  F   FF ",
    			"   FFF   ",
    			"   FFF   ",
    			"   FFF   ",
    			"  FFFFFF ",
    			" FFFFFF F",
    			" F FFFF  ",
    			"F FFFFFF ",
    			"   FFFF  ",
    			"    FF   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 14;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampLargeHouse() {}

    	public SwampLargeHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampLargeHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampLargeHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Outside roots
    			{2,0,9, 2,1,9}, {6,0,9, 6,1,9}, {7,0,9, 7,0,9}, 
    			{2,0,5, 2,1,5}, {6,0,5, 6,1,5}, 
    			// Main trunk
    			{3,0,6, 3,5,8}, {4,0,6, 4,2,7}, {4,0,8, 4,8,8}, {5,0,6, 5,5,8}, 
    			{4,0,8, 4,2,8}, {4,5,6, 4,5,6}, 
    			// Ceiling
    			{3,13,6, 5,13,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
    		}
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			// Outside roots
    			{1,0,10, 1,0,10}, {8,0,10, 8,0,10}, 
    			{7,0,5, 7,0,5}, {8,0,4, 8,0,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
    		}
    		
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false);
    		for(int[] uuvvww : new int[][]{
    			// Longer log
    			{2,0,10, 2,0,10}, {7,0,10, 7,0,10}, 
    			{0,0,2, 0,0,2}, {1,0,3, 1,0,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{3,6,5, 3,11,5}, {4,8,5, 4,8,5}, {4,11,5, 4,11,5}, {5,6,5, 5,11,5}, 
            	// Left wall
            	{2,6,6, 2,11,6}, {2,6,7, 2,6,7}, {2,9,7, 2,9,7}, {2,11,7, 2,11,7}, {2,6,8, 2,11,8}, 
            	// Right wall
            	{6,6,6, 6,11,6}, {6,6,7, 6,6,7}, {6,9,7, 6,9,7}, {6,11,7, 6,11,7}, {6,6,8, 6,11,8}, 
            	// Back wall
            	{3,6,9, 5,9,9}, {3,10,9, 3,11,9}, {4,11,9, 4,11,9}, {5,10,9, 5,11,9}, 
            	// Lantern support
            	{3,8,8, 3,8,8}, 
            	// Roof
            	{2,12,5, 6,12,5}, {2,12,6, 2,12,8}, {6,12,6, 6,12,8}, {2,12,9, 6,12,9}, 
            	// Supports
            	{0,11,5, 0,11,5}, {2,11,3, 2,11,3}, {6,11,3, 6,11,3}, {8,11,5, 8,11,5}, {0,11,9, 0,11,9}, {2,11,11, 2,11,11}, {6,11,11, 6,11,11}, {8,11,9, 8,11,9}, 
            	{0,8,5, 0,8,5}, {2,8,3, 2,8,3}, {6,8,3, 6,8,3}, {8,8,5, 8,8,5}, {0,8,9, 0,8,9}, {2,8,11, 2,8,11}, {6,8,11, 6,8,11}, {8,8,9, 8,8,9}, 
            	{0,5,9, 1,5,9}, {2,4,9, 2,4,9}, {6,4,9, 6,4,9}, {7,5,9, 8,5,9}, 
            	{0,5,5, 1,5,5}, {2,4,5, 2,4,5}, {6,4,5, 6,4,5}, {7,5,5, 8,5,5}, 
            	// Entry
            	{3,0,1, 5,0,5}, {5,2,4, 5,2,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Pews
    			{5,0,0, 3}, {5,1,2, 3}, {5,2,3, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Entrance
    			{3,2,5, 5,2,5}, {4,2,4, 4,2,4}, 
    			{5,1,3, 5,1,3}, 
    			// First balcony
    			{0,5,6, 0,5,8}, 
    			{1,5,4, 1,5,4}, {1,5,6, 1,5,8}, {1,5,10, 1,5,10}, 
    			{2,5,3, 2,5,11}, 
    			{3,5,3, 5,5,5}, {3,5,9, 5,5,11}, 
    			{6,5,3, 6,5,11}, 
    			{7,5,4, 7,5,4}, {7,5,6, 7,5,8}, {7,5,10, 7,5,10}, 
    			{8,5,6, 8,5,8}, 
    			// Second balcony
    			{0,8,6, 0,8,8}, 
    			{1,8,4, 1,8,10}, 
    			{2,8,4, 2,8,5}, {2,8,9, 2,8,10}, 
    			{3,8,3, 5,8,4}, {3,8,10, 5,8,11}, 
    			{6,8,4, 6,8,5}, {6,8,9, 6,8,10}, 
    			{7,8,4, 7,8,10}, 
    			{8,8,6, 8,8,8}, 
    			// Second floor
    			{3,8,6, 3,8,7}, {4,8,6, 4,8,6}, {5,8,6, 5,8,8}, 
    			// Roof
    			{1,11,10, 7,11,10}, 
    			{1,11,5, 1,11,9}, {7,11,5, 7,11,9}, 
    			{1,11,4, 7,11,4}, 
    			{2,11,5, 2,11,5}, {6,11,5, 6,11,5}, {2,11,9, 2,11,9}, {6,11,9, 6,11,9}, 
    			{3,11,3, 5,11,3}, {0,11,6, 0,11,8}, {8,11,6, 8,11,8}, {3,11,11, 5,11,11}, 
    			// Front supports
    			{1,4,9, 1,4,9}, {7,4,9, 7,4,9}, 
    			{1,4,5, 1,4,5}, {7,4,5, 7,4,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{2,12,10, 6,12,10}, 
    			{1,12,5, 1,12,9}, {7,12,5, 7,12,9}, 
    			{2,12,4, 6,12,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Front deck
            	{2,0,4}, {6,0,4}, 
            	{2,0,2}, {6,0,2}, 
            	// Front railing
            	{3,1,4}, {3,2,4}, {3,3,4}, {4,3,4}, {3,3,5}, 
            	// First balcony railing
            	{0,6,4}, {0,6,5}, {0,6,6}, {0,6,7}, {0,6,8}, {0,6,9}, {0,6,10}, 
            	{1,6,10}, {1,6,11}, {2,6,11}, {3,6,11}, {4,6,11}, {5,6,11}, {6,6,11}, {7,6,11}, 
            	{7,6,10}, {8,6,10}, {8,6,9}, {8,6,8}, {8,6,7}, {8,6,6}, {8,6,5}, {8,6,4}, 
            	{7,6,4}, {7,6,3}, {6,6,3}, {5,6,3}, {4,6,3}, {3,6,3}, {2,6,3}, {1,6,3}, {1,6,4}, 
            	{0,7,5}, {0,7,9}, 
            	{2,7,3}, {6,7,3}, {2,7,11}, {6,7,11}, 
            	{8,7,5}, {8,7,9}, 
            	// Second balcony railing
            	{0,9,4}, {0,9,5}, {0,9,6}, {0,9,7}, {0,9,8}, {0,9,9}, {0,9,10}, 
            	{1,9,10}, {1,9,11}, {2,9,11}, {3,9,11}, {4,9,11}, {5,9,11}, {6,9,11}, {7,9,11}, 
            	{7,9,10}, {8,9,10}, {8,9,9}, {8,9,8}, {8,9,7}, {8,9,6}, {8,9,5}, {8,9,4}, 
            	{7,9,4}, {7,9,3}, {6,9,3}, {5,9,3}, {4,9,3}, {3,9,3}, {2,9,3}, {1,9,3}, {1,9,4}, 
            	{0,10,5}, {0,10,9}, 
            	{2,10,3}, {6,10,3}, {2,10,11}, {6,10,11}, 
            	{8,10,5}, {8,10,9}, 
            	// Hanging lantern
            	{4,12,7}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		{4,11,7}, 
        		{3,7,8}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,2,9}, {6,2,5}, 
            	{1,7,10}, {7,7,4}, 
            	{7,10,10}, {1,10,4}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,9,5, 2, 1, 0}, 
    			{4,6,5, 2, 1, 0}, 
    			{4,3,6, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Trapdoor (Bottom Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,10,7, 1}, {4,10,9, 2}, {6,10,7, 3}, 
            	{2,7,7, 1}, {6,7,7, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Trapdoor (Top Vertical)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,11,7, 1}, {4,11,9, 2}, {6,11,7, 3}, 
            	{2,8,7, 1}, {6,8,7, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.ladder.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{4,3,7, 4,8,7, 2}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode)), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode)), false);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{5,6,7, 2, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{3,9,7, 2, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			uvwoc[4]);
            	}
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 3;
        	int chestV = 6;
        	int chestW = 8;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Left side
        			{2,13,6, 3}, 
        			{-1,11,9, 3}, {-1,10,9, 3}, {-1,9,9, 3}, 
        			{2,10,9, 3}, {2,9,9, 3}, {2,7,9, 3}, 
        			// Right side
        			{6,13,6, 1}, 
        			{7,7,6, 1}, {7,6,6, 1}, 
        			{6,4,6, 1}, {6,3,6, 1}, 
        			{6,3,7, 1}, {6,2,7, 1}, 
        			{6,3,8, 1}, {6,2,8, 1}, 
        			// Away-facing vines
        			{6,11,12, 0}, {6,10,12, 0}, {6,9,12, 0}, {6,8,12, 0}, {6,7,12, 0}, 
        			{3,7,10, 0}, {4,7,10, 0}, {4,6,10, 0}, 
        			{5,2,9, 0}, {5,1,9, 0}, 
        			{4,2,9, 0}, {4,1,9, 0}, 
        			{3,1,9, 0}, {3,0,9, 0}, 
        			// Player-facing side
        			{3,13,5, 2}, {5,13,5, 2}, 
        			{2,10,5, 2}, 
        			{2,8,2, 2}, {2,7,2, 2}, {2,6,2, 2}, 
        			{6,11,2, 2}, {6,10,2, 2}, {6,9,2, 2}, {6,8,2, 2}, {6,7,2, 2}, {6,6,2, 2}, 
        			{6,7,5, 2}, {6,6,5, 2}, 
        			{5,4,5, 2}, {5,3,5, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
	                	{5,6,6, -1, 0}, 
	                	{3,9,6, -1, 0}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Library --- //
    // designed by Overjay
    
    public static class SwampLibrary extends StructureVillagePieces.Village
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
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFFFFFFF",
    			"FFFPPPFFF",
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
    	
    	public SwampLibrary() {}

    	public SwampLibrary(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampLibrary buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampLibrary(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
            
                    	
            // Polished Granite
            IBlockState polishedGraniteState = ModObjects.chooseModPolishedGraniteBlockState();
            if (polishedGraniteState==null) {polishedGraniteState = ModObjects.chooseModSmoothStoneBlockState();} // Guarantees a vanilla stone if this fails
        	for (int[] uuvvww : new int[][]{
        		{2,1,3, 6,1,10}, 
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], polishedGraniteState, polishedGraniteState, false);	
            }
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{2,2,6, 3,3,6}, {3,2,8, 3,3,9}, {3,4,9, 3,5,9}, 
        		{6,2,3, 6,3,3}, {6,2,5, 6,2,6}, {6,2,8, 6,2,8}, {6,2,10, 6,2,10}, {6,4,10, 6,4,10}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            }
    		
    		
    		// Redstone Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			{6,3,5, -1}, {6,3,8, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.redstone_torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Stone
    		IBlockState biomeStoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Left wall
    			{1,2,3, 1,2,3}, {1,4,3, 1,4,3}, 
    			{1,3,8, 1,4,8}, 
    			// Right wall
    			{7,2,5, 7,3,5}, 
    			{7,3,8, 7,4,8}, 
    			// Front wall
    			{1,3,2, 1,4,2}, {3,5,2, 3,5,2}, {5,5,2, 5,5,2}, {7,3,2, 7,4,2}, 
    			// Back wall
    			{3,2,11, 3,2,11}, {3,4,11, 3,5,11}, 
    			{5,2,11, 5,3,11}, {6,4,11, 6,4,11}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneState, biomeStoneState, false);	
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Left wall
    			{1,1,3, 1,1,3}, {1,3,3, 1,3,3}, 
    			{1,2,5, 1,3,5}, 
    			{1,4,6, 1,4,6}, 
    			{1,1,7, 1,1,7}, {1,3,7, 1,4,7}, 
    			{1,2,8, 1,2,8}, 
    			{1,1,9, 1,1,9}, 
    			{1,2,10, 1,4,10}, 
    			// Right wall
    			{7,1,3, 7,4,3}, 
    			{7,1,5, 7,1,5}, {7,4,5, 7,4,5}, 
    			{7,3,6, 7,4,6}, 
    			{7,1,7, 7,2,7}, 
    			{7,1,8, 7,2,8}, 
    			{7,2,10, 7,4,10}, 
    			// Front wall
    			{1,1,2, 1,2,2}, 
    			{2,2,2, 2,5,2}, 
    			{3,1,2, 3,3,2}, 
    			{4,1,2, 4,1,2}, {4,4,2, 4,5,2}, 
    			{5,2,2, 5,3,2}, 
    			{6,2,2, 6,5,2}, 
    			{7,1,2, 7,2,2}, 
    			// Back wall
    			{1,1,11, 1,4,11}, 
    			{2,1,11, 2,1,11}, {2,5,11, 2,5,11}, 
    			{3,3,11, 3,3,11}, 
    			{5,1,11, 5,1,11}, {5,4,11, 5,5,11}, 
    			{7,1,11, 7,4,11}, 
    			// Ceiling
    			{2,5,5, 2,5,5}, {2,5,8, 2,5,8}, 
    			{6,5,5, 6,5,5}, {6,5,8, 6,5,8}, 
    			// Exterior posts
    			{0,1,2, 0,1,2}, {0,1,11, 0,1,11}, 
    			{4,1,12, 4,1,12}, {7,1,12, 7,1,12}, 
    			{7,1,1, 7,1,1}, 
    			{8,1,2, 8,1,2}, {8,1,11, 8,1,11}, 
    			// Ceiling
    			{3,6,5, 5,6,5}, {3,6,8, 5,6,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Left wall
    			{1,1,4, 1,1,5}, {1,4,5, 1,4,5}, 
    			{1,1,6, 1,3,6}, 
    			{1,2,7, 1,2,7}, 
    			{1,1,8, 1,1,8}, 
    			{1,1,10, 1,1,10}, 
    			{1,1,10, 1,1,10}, 
    			// Right wall 
    			{7,1,4, 7,1,4}, 
    			{7,1,6, 7,2,6}, 
    			{7,3,7, 7,4,7}, 
    			{7,1,9, 7,1,10}, 
    			// Front wall 
    			{2,1,2, 2,1,2}, 
    			{4,6,2, 4,6,2}, 
    			{6,1,2, 6,1,2}, 
    			// Back wall 
    			{3,1,11, 4,1,11}, 
    			{4,6,11, 4,6,11}, 
    			{6,1,11, 6,1,11}, {6,5,11, 6,5,11}, 
    			// Exterior posts
    			{1,1,1, 1,1,1}, 
    			{0,1,5, 0,1,5}, 
    			{0,1,8, 0,1,8}, 
    			{1,1,12, 1,1,12}, 
    			{8,1,5, 8,1,5}, 
    			{8,1,8, 8,1,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	{0,0,1, 8,0,12}, 
            	{0,0,0, 2,0,0}, {6,0,0, 8,0,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);	
            }
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{2,6,11, 0}, {3,6,11, 1}, {3,7,11, 2}, {4,7,12, 2+4}, {4,7,11, 3}, {5,7,11, 2}, {5,6,11, 0}, {6,6,11, 1}, 
    			{2,6,8, 0}, {6,6,8, 1}, 
    			{2,6,5, 0}, {6,6,5, 1}, 
    			{2,6,2, 0}, {3,6,2, 1}, {3,7,2, 3}, {4,7,1, 3+4}, {4,7,2, 2}, {5,7,2, 3}, {5,6,2, 0}, {6,6,2, 1}, 
    			{1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, {1,5,5, 0}, {1,5,6, 0}, {1,5,7, 0}, {1,5,8, 0}, {1,5,9, 0}, {1,5,10, 0}, {1,5,11, 0}, 
    			{7,5,2, 1}, {7,5,3, 1}, {7,5,4, 1}, {7,5,5, 1}, {7,5,6, 1}, {7,5,7, 1}, {7,5,8, 1}, {7,5,9, 1}, {7,5,10, 1}, {7,5,11, 1}, 
    			// Ceiling
    			{2,4,8, 1+4}, {3,5,8, 1+4}, {5,5,8, 0+4}, {6,4,8, 0+4}, 
    			{2,4,5, 1+4}, {3,5,5, 1+4}, {5,5,5, 0+4}, {6,4,5, 0+4},
    			// Windows
    			{7,2,4, 0}, {7,4,4, 0+4}, {7,2,9, 0}, {7,4,9, 0+4}, 
    			{1,2,4, 1}, {1,4,4, 1+4}, {1,2,9, 1}, {1,4,9, 1+4}, 
    			{2,2,11, 3}, {2,4,11, 3+4}, {4,2,11, 3}, {4,5,11, 3+4}, {6,2,11, 3}, {6,4,11, 3+4}, 
    			// Posts
    			{0,2,11, 0+4}, {0,3,11, 0}, 
    			{0,2,8, 0+4}, {0,3,8, 0}, 
    			{0,2,5, 0+4}, {0,3,5, 0}, 
    			{0,2,2, 0+4}, {0,3,2, 0}, 
    			{1,2,1, 3+4}, {1,3,1, 3}, {7,2,1, 3+4}, {7,3,1, 3}, 
    			{8,2,11, 1+4}, {8,3,11, 1}, 
    			{8,2,8, 1+4}, {8,3,8, 1}, 
    			{8,2,5, 1+4}, {8,3,5, 1}, 
    			{8,2,2, 1+4}, {8,3,2, 1}, 
    			{1,2,12, 2+4}, {1,3,12, 2}, {4,2,12, 2+4}, {4,3,12, 2}, {7,2,12, 2+4}, {7,3,12, 2}, 
    			// Front steps
    			{3,1,1, 3+4}, {4,1,1, 3}, {5,1,1, 3+4}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,7,8, 5,7,8}, 
    			{3,7,5, 5,7,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Front entrance
    			{4,4,1, 3}, 
    			// Ceiling trim
    			{2,5,3, 1+4}, {2,5,4, 1+4}, {2,5,6, 1+4}, {2,5,7, 1+4}, {2,5,9, 1+4}, {2,5,10, 1+4}, 
    			{6,5,3, 0+4}, {6,5,4, 0+4}, {6,5,6, 0+4}, {6,5,7, 0+4}, {6,5,9, 0+4}, {6,5,10, 0+4}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,6,3, 4,6,4}, {4,6,6, 4,6,7}, {4,6,9, 4,6,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,6,3, 3,6,4}, {3,6,6, 3,6,7}, {3,6,9, 3,6,10}, 
    			{5,6,3, 5,6,4}, {5,6,6, 5,6,7}, {5,6,9, 5,6,10}, 
    			{3,4,1, 3,4,1}, {5,4,1, 5,4,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Front posts
            	{3,2,1}, {3,3,1}, {5,2,1}, {5,3,1}, 
            	// Front desk
            	{3,2,3}, {3,2,4}, 
            	// Interior shelf posts
            	{3,4,6}, {3,5,6}, {6,3,6}, {6,4,6}, 
            	{3,4,8}, {3,5,10}, 
            	{6,3,10}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
        	// Fence Gate (Along)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence_gate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{3,2,5}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlockstate.getBlock(), (biomeFenceGateBlockstate.getBlock().getMetaFromState(biomeFenceGateBlockstate)+1)%8, this.coordBaseMode)), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{4,5,5}, {4,5,8}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Lower
        		{2,3,3, 3,3,3, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
        		{3,3,4, 3,3,4, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet.getStateFromMeta(uuvvww[6]), Blocks.carpet.getStateFromMeta(uuvvww[6]), false);	
            }
            
            
            // Stained Glass Windows
            for (int[] uvwc : new int[][]{
            	// Player-facing wall
            	{3,4,2, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{5,4,2, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	// Left wall
            	{1,3,4, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{1,3,9, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	// Right wall
            	{7,3,4, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{7,3,9, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	// Back wall
            	{2,3,11, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{4,3,11, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{4,4,11, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{6,3,11, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
        		})
            {
            	this.setBlockState(world, Blocks.stained_glass_pane.getStateFromMeta(uvwc[3]), uvwc[0], uvwc[1], uvwc[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,2,2, 0, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
        	
            // Lectern
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwo : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{6,2,4, 3}, {6,2,7, 3}, 
            })
            {
        		ModObjects.setModLecternState(world,
            			this.getXWithOffset(uvwo[0], uvwo[2]),
            			this.getYWithOffset(uvwo[1]),
            			this.getZWithOffset(uvwo[0], uvwo[2]),
            			uvwo[3],
            			this.coordBaseMode,
            			biomePlankState.getBlock().getMetaFromState(biomePlankState));
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 2;
        	int chestW = 3;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_library");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Left side
        			{0,4,3, 3}, {0,3,3, 3}, {0,2,3, 3}, {0,1,3, 3}, 
        			{0,4,9, 3}, {0,3,8, 3}, {0,2,8, 3}, {0,1,8, 3}, 
        			// Right side
        			{8,4,2, 1}, {8,4,3, 1}, {8,3,3, 1}, {8,2,3, 1}, {8,1,3, 1}, {8,2,4, 1}, 
        			{8,4,6, 1}, {8,3,6, 1}, {8,2,6, 1}, {8,1,6, 1}, 
        			{8,3,7, 1}, {8,2,7, 1}, 
        			{8,4,8, 1}, 
        			{8,1,9, 1}, 
        			{8,4,10, 1}, {8,3,10, 1}, {8,2,10, 1}, {8,1,10, 1}, 
        			// Away-facing vines
        			{2,5,12, 0}, {2,4,12, 0}, 
        			{5,4,12, 0}, {5,3,12, 0}, {5,2,12, 0}, 
        			{6,5,12, 0}, {6,4,12, 0}, {6,2,12, 0}, {6,1,12, 0}, 
        			// Player-facing side
        			{1,4,1, 2}, 
        			{2,4,1, 2}, {2,3,1, 2}, {2,2,1, 2}, {2,1,1, 2}, 
        			{6,4,1, 2}, {6,3,1, 2}, {6,2,1, 2}, {6,1,1, 2}, 
        			{7,4,1, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(25);
    			
    			int u = s<=5? 2 : s<=7? 3 : s<=15? 4 : s<=23? 5 : 6;
    			int v = 2;
    			int w = s<=1? s+4 : s<=5? s+5 : s<=6? 7 : s<=7? 10 : s<=15? s-5 : s<=23? s-13 : 9;
    			
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
    	protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    // --- Mason House --- //
    // designed by AstroTibs
    
    public static class SwampMasonHouse extends StructureVillagePieces.Village
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
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampMasonHouse() {}

    	public SwampMasonHouse(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampMasonHouse buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampMasonHouse(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Outline
    			{0,0,0, 0,0,1}, {0,0,4, 0,0,5}, {0,0,9, 0,0,11}, 
    			{5,0,11, 6,0,11}, 
    			{6,0,8, 6,0,8}, {6,0,0, 6,0,1}, 
    			{4,0,0, 4,0,0}, 
    			// Floor
    			{1,0,1, 5,0,2}, 
    			{1,0,3, 2,0,6}, {4,0,3, 5,0,6}, 
    			{1,0,7, 4,0,9}, {1,0,10, 1,0,10}, 
    			{4,0,10, 5,0,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Outline
    			{0,0,2, 0,0,3}, {0,0,6, 0,0,8}, 
    			{1,0,11, 4,0,11}, {2,0,10, 3,0,10}, 
    			{6,0,9, 6,0,10}, {6,0,2, 6,0,7}, {5,0,8, 5,0,9}, 
    			{1,0,0, 2,0,0}, {5,0,0, 5,0,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,0,3, 1, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{3,0,4, 0, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{3,0,5, 3, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{3,0,6, 2, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{3,0,0, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{0,1,0, 0,1,1}, {0,1,4, 0,1,5}, {0,1,8, 0,1,11}, 
        		{1,1,11, 1,1,11}, {3,1,11, 6,1,11}, {6,1,10, 6,1,10}, {6,1,7, 6,1,8}, {6,1,3, 6,1,3}, {6,1,0, 6,1,0}, {4,1,0, 4,1,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
        	
        	
        	// Mossy Cobblestone wall
        	IBlockState biomeMossyCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getStateFromMeta(1), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{0,1,2, 0,1,3}, {0,1,6, 0,1,7}, 
        		{2,1,11, 2,1,11}, 
        		{6,1,9, 6,1,9}, {6,1,4, 6,1,6}, {6,1,1, 6,1,2}, 
        		{5,1,0, 5,1,0}, {1,1,0, 2,1,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneWallState, biomeMossyCobblestoneWallState, false);
            }
    		
    		
    		// Cobblestone Slab (lower) - not biome adjusted
    		for(int[] uuvvww : new int[][]{
    			{1,2,8, 1,2,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.stone_slab.getStateFromMeta(3), Blocks.stone_slab.getStateFromMeta(3), false);	
    		}
    		
    		
    		// Clay - not biome adjusted
    		for(int[] uuvvww : new int[][]{
    			{1,1,8, 1,1,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.clay.getDefaultState(), Blocks.clay.getDefaultState(), false);	
    		}
    		
    		
    		// Stone Brick Stairs - not biome adjusted
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Central platform
    			{4,1,2, 0},  
    			})
    		{
    			this.setBlockState(world, Blocks.stone_brick_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Polished Andesite - not biome adjusted
    		IBlockState biomePolishedAndesiteState = Blocks.stone.getStateFromMeta(6);
    		for (int[] uvw : new int[][]{
    			// Central platform
    			{4,1,3}, 
    			})
    		{
    			this.setBlockState(world, biomePolishedAndesiteState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Andesite - not biome adjusted
    		IBlockState biomeAndesiteState = Blocks.stone.getStateFromMeta(5);
    		for (int[] uvw : new int[][]{
    			// Central platform
    			{1,1,9},  
    			})
    		{
    			this.setBlockState(world, biomeAndesiteState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Stone Brick Slab (lower) - not biome adjusted
    		IBlockState biomeStoneBrickSlabLowerState = Blocks.stone_slab.getStateFromMeta(5);
    		for(int[] uuvvww : new int[][]{
    			{2,1,5, 2,1,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickSlabLowerState, biomeStoneBrickSlabLowerState, false);	
    		}
    		
    		
    		// Brick block - not biome adjusted
    		for (int[] uvw : new int[][]{
    			// Central platform
    			{2,1,6}, 
    			})
    		{
    			this.setBlockState(world, Blocks.brick_block.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Polished Diorite - not biome adjusted
    		IBlockState polishedDioriteState = Blocks.stone.getStateFromMeta(4);
        	for (int[] uvw : new int[][]{
    			// Central platform
    			{2,1,7}, 
    			})
    		{
    			this.setBlockState(world, polishedDioriteState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
    		// Concrete
    		IBlockState tryConcrete = ModObjects.chooseModConcreteState(GeneralConfig.useVillageColors ? townColor : 15); // Black
        	Block concreteBlock = Blocks.stained_hardened_clay; int concreteMeta = GeneralConfig.useVillageColors ? townColor : 15; // Black
        	if (tryConcrete != null) {concreteBlock = tryConcrete.getBlock(); concreteMeta = tryConcrete.getBlock().getMetaFromState(tryConcrete);}
    		for(int[] uuvvww : new int[][]{
    			// Front wall
    			{1,1,1, 2,4,1}, {3,4,1, 3,4,1}, {4,1,1, 5,4,1}, 
    			// Left wall
    			{1,1,2, 1,1,7}, {1,2,3, 1,3,3}, {1,2,5, 1,3,5}, {1,2,7, 1,3,7}, {1,4,2, 1,4,7}, 
    			// Right wall
    			{5,1,2, 5,1,7}, {5,2,3, 5,3,3}, {5,2,5, 5,3,5}, {5,2,7, 5,3,7}, {5,4,2, 5,4,7}, 
    			// Back section
    			{2,1,8, 2,3,9}, {3,1,9, 4,3,9}, {4,3,8, 4,3,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], concreteBlock.getStateFromMeta(concreteMeta), concreteBlock.getStateFromMeta(concreteMeta), false);	
    		}
    		
    		
    		// Trapdoor (Bottom Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,2,2, 1}, {1,2,4, 1}, {1,2,6, 1},
            	{5,2,2, 3}, {5,2,4, 3}, {5,2,6, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Trapdoor (Top Vertical)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,3,2, 1}, {1,3,4, 1}, {1,3,6, 1},
            	{5,3,2, 3}, {5,3,4, 3}, {5,3,6, 3}, 
            	{3,3,1, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Roof
    			{2,5,2, 4,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
        	
        	// Stone Cutter
        	// Orientation:0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            IBlockState stonecutterState = ModObjects.chooseModStonecutterState(2);
            this.setBlockState(world, stonecutterState, 3, 1, 8, structureBB);
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,4,7, 4,4,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,5,7, 5,5,7}, 
    			{1,5,2, 1,5,6}, {5,5,2, 5,5,6}, 
    			{1,5,1, 5,5,1}, 
    			{3,6,3, 3,6,5}, 
    			{2,4,8, 4,4,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{3,4,3}, {3,3,8} 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{3,1,1, 2, 1, 1}, 
    			{4,1,8, 1, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 1;
        	int chestV = 1;
        	int chestW = 10;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_mason");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Left side
        			{0,4,2, 3}, 
        			{0,4,3, 3}, {0,3,3, 3}, {0,2,3, 3}, 
        			{0,4,5, 3}, 
        			{0,3,7, 3}, {0,2,7, 3}, 
        			{1,3,9, 3}, {1,2,9, 3}, 
        			// Right side
        			{6,3,1, 1}, {6,2,1, 1}, 
        			{6,4,3, 1}, {6,3,3, 1}, {6,2,3, 1}, 
        			{6,4,5, 1}, {6,3,5, 1}, {6,2,5, 1}, 
        			{6,4,6, 1}, 
        			{6,3,7, 1}, 
        			{5,3,8, 1}, 
        			{5,3,9, 1}, {5,2,9, 1}, {5,1,9, 1}, 
        			// Away-facing vines
        			{1,4,8, 0}, {1,3,8, 0}, 
        			{2,3,10, 0}, {2,2,10, 0}, {2,1,10, 0}, 
        			{3,3,10, 0}, 
        			// Player-facing side
        			{1,4,0, 2}, {1,3,0, 2}, {1,2,0, 2}, 
        			{2,3,0, 2}, {2,2,0, 2}, 
        			{5,4,0, 2}, {5,3,0, 2}, {5,2,0, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(20);
    			
    			int u = s<=2? 2 : s<=8? 3 : s<=13? 4 : s<=16? 5 : s-15;
    			int v = 2;
    			int w = s<=2? s-2 : s<=8? s-1 : s<=13? s-5 : s<=16? s-6 : 10;
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    // --- Medium House 1 --- //
    // designed by AstroTibs
    
    public static class SwampMediumHouse1 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"     F    ",
    			" F       F",
    			"          ",
    			"          ",
    			" F   F   F",
    			"          ",
    			"          ",
    			"          ",
    			" F   F   F",
    			"F         ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 5;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampMediumHouse1() {}

    	public SwampMediumHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampMediumHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampMediumHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Under posts
            	{1,0,8}, {5,0,9}, {9,0,8}, 
            	{1,0,5}, {5,0,5}, {9,0,5}, 
            	{1,0,1}, {5,0,1}, {9,0,1}, 
            	// Awning posts
            	{1,2,1}, {1,2,2}, {1,3,1}, {5,2,1}, {5,3,1}, {9,2,1}, {9,3,1}, 
            	// Porch table
            	{7,2,2}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
        	// For stripped logs specifically
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStrippedLogVertState = biomeLogVertState;
        	// Try to see if stripped logs exist
        	biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
        	for (int[] uuvvww : new int[][]{
        		{7,2,7, 7,2,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Entry
    			{0,1,2, 0,1,3}, 
    			// Floor
    			{1,1,1, 9,1,8}, {4,1,9, 6,1,9}, 
    			// Front wall
    			{1,2,4, 1,3,4}, {2,2,4, 3,2,4}, {4,2,4, 4,3,4}, 
    			{6,2,4, 7,3,4}, {8,2,4, 8,2,4}, {9,2,4, 9,3,4}, 
    			{3,4,4, 7,4,4}, {5,5,4, 5,5,4}, 
    			// Back wall
    			{1,2,8, 3,3,8}, {3,4,8, 3,4,8}, {7,4,8, 7,4,8}, {7,2,8, 9,3,8}, 
    			// Left wall
    			{1,2,5, 1,3,7}, 
    			// Right wall
    			{9,2,5, 9,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{2,4,8, 2,4,8}, {4,5,8, 6,5,8}, {6,5,4, 6,5,4}, {8,4,8, 8,4,8}, 
    			// Furnace
    			{4,2,8, 4,2,8}, {6,2,8, 6,2,8}, 
    			{4,2,9, 6,3,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{2,4,4, 2,4,4}, {4,5,4, 4,5,4}, {8,4,4, 8,4,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Awning
    			{1,4,1, 4,4,2}, {2,4,3, 3,4,3}, {6,4,1, 7,4,3}, {8,4,1, 8,4,2}, 
    			{1,4,5, 1,4,7}, {9,4,8, 9,4,8}, 
    			// Roof
    			{3,5,6, 3,5,8}, {5,6,4, 5,6,7}, {7,5,4, 7,5,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			// Awning
    			{1,4,3, 1,4,4}, {1,4,8, 1,4,8}, 
    			{4,4,3, 4,4,3}, {5,4,1, 5,4,3}, 
    			{8,4,3, 8,4,3}, {9,4,1, 9,4,7}, 
    			// Roof
    			{3,5,4, 3,5,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Cobblestone Slab (upper)
    		IBlockState biomeCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3+8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{2,4,5, 2,4,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabUpperState, biomeCobblestoneSlabUpperState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (upper)
    		IBlockState biomeMossyCobblestoneSlabUpperState = ModObjects.chooseModMossyCobblestoneSlabState(true);
    		if (biomeMossyCobblestoneSlabUpperState != null)
    		{
    			biomeMossyCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabUpperState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabUpperState = biomeCobblestoneSlabUpperState;
    		}
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{2,4,6, 2,4,7}, {4,5,5, 4,5,7}, {6,5,5, 6,5,7}, {8,4,5, 8,4,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabUpperState, biomeMossyCobblestoneSlabUpperState, false);	
    		}
    		
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Furnace
        		{4,4,8, 1+4}, {5,4,8, 3+4}, {6,4,8, 0+4}, 
        		{4,3,8, 1+4}, {6,3,8, 0+4}, 
        		// Furnace outside
        		{4,4,9, 2}, {5,4,9, 2}, {6,4,9, 2}, 
        		})
            {
            	this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
    		
    		
            // Stone Brick
    		IBlockState biomeStoneBrickBlockState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stonebrick.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Under the furnace
    			{5,2,8, 5,2,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStoneBrickBlockState, biomeStoneBrickBlockState, false);	
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Entry
    			{0,0,0, 3}, {0,1,1, 3}, 
    			// Porch furniture
    			{2,2,2, 3}, {3,2,2, 3}, {6,2,2, 1}, {8,2,2, 0}, 
    			// Interior seat
    			{2,2,5, 2}, {3,2,5, 2}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{7,3,2},  
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		// Porch
        		{7,3,1}, 
        		// Interior
        		{5,5,7}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{7,3,7}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Lower
        		{2,2,6, 3,2,6, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
        		{3,2,7, 3,2,7, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet.getStateFromMeta(uuvvww[6]), Blocks.carpet.getStateFromMeta(uuvvww[6]), false);	
            }
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{2,2,7, 2,3,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{2,3,4}, {3,3,4}, {8,3,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{5,2,4, 2, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,3,8, 2}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.furnace.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{6,2,6, 2, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{8,2,6, 2, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			uvwoc[4]);
            	}
            }
    		
    		
        	// Patterned banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{7,3,3, 2, 14}, // Red
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
				world.setBlockState(bannerPos, Blocks.wall_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.coordBaseMode.getHorizontalIndex(), true)), 2);
				
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
    				ItemStack villageBanner = new ItemStack(Items.banner);
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
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Left side
        			{0,3,4, 3}, {0,2,4, 3}, {0,1,4, 3}, 
        			// Right side
        			{10,3,4, 1}, {10,3,5, 1}, {10,2,5, 1}, {10,1,5, 1}, {10,3,6, 1}, {10,2,6, 1}, 
        			// Away-facing vines
        			{1,3,9, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
	                	{6,2,5, -1, 0}, 
	                	{8,2,5, -1, 0}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Medium House 2 --- //
    // designed by AstroTibs
    
    public static class SwampMediumHouse2 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			" F     F    ",
    			"            ",
    			"            ",
    			"            ",
    			" F     F    ",
    			"            ",
    			"            ",
    			"  F   F  FF ",
    			"         F  ",
    			"  F F F  FF ",

        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1+8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 8;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 6;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampMediumHouse2() {}

    	public SwampMediumHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampMediumHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampMediumHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		{1,1,9, 1,4,9}, {7,1,9, 7,4,9}, 
        		{1,1,5, 1,4,5}, {7,1,5, 7,4,5}, 
        		{2,1,2, 2,4,2}, {6,1,2, 6,4,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
        	// Stripped Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), true);
        	IBlockState biomeStrippedLogHorizAcrossState = biomeLogHorAcrossState;
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log || biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.log)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.log2)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	{2,1,0, 6,1,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Support posts
    			{1,0,9, 1,0,9}, {7,0,9, 7,0,9}, 
    			{1,0,5, 1,0,5}, {7,0,5, 7,0,5}, 
    			{2,0,2, 2,0,2}, {6,0,2, 6,0,2}, 
    			{2,0,0, 2,0,0}, {4,0,0, 4,0,0}, {6,0,0, 6,0,0}, 
    			// Front hatching
    			{2,4,0, 6,4,0}, 
    			{7,3,2, 8,3,2}, {8,2,2, 10,2,2}, {7,3,0, 8,3,0}, {8,2,0, 10,2,0}, 
    			{2,4,1, 2,4,1}, {6,4,1, 6,4,1}, 
    			{8,1,2, 8,2,2}, {10,0,2, 10,1,2}, 
    			{2,2,0, 2,3,0}, {4,2,0, 4,3,0}, {6,2,0, 6,3,0}, {8,1,0, 8,2,0}, {10,0,0, 10,1,0}, 
    			// Windows
    			{1,3,7, 1,3,7}, {7,3,7, 7,3,7}, 
    			{2,3,3, 2,3,3}, {6,3,3, 6,3,4}, 
    			{3,3,2, 4,3,2}, {4,5,2, 4,5,2}, 
    			{3,5,5, 5,5,5}, {3,5,9, 5,5,9}, 
    			{4,5,6, 4,5,8}, 
    			{3,3,5, 3,3,5}, {5,3,5, 5,3,5}, 
    			// Tables
    			{6,2,6, 6,2,6},
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Front wall
            	{3,2,2, 4,2,2}, {3,4,2, 5,4,2}, 
            	// Left wall
            	{2,1,3, 2,2,3}, {2,1,4, 2,1,4}, {2,4,3, 2,4,3}, 
            	{1,1,6, 1,2,8}, {1,3,6, 1,3,6}, {1,3,8, 1,3,8}, {1,4,6, 1,4,8}, 
            	// Right wall
            	{7,1,6, 7,2,8}, {7,3,6, 7,3,6}, {7,3,8, 7,3,8}, {7,4,6, 7,4,8}, 
            	{6,4,3, 6,4,4}, {6,1,3, 6,2,4}, 
            	// Back wall
            	{2,1,9, 6,4,9}, 
            	// Separator wall
            	{2,4,5, 6,4,5}, 
            	{2,3,5, 2,3,5}, {6,3,5, 6,3,5}, 
            	{2,2,5, 3,2,5}, {5,2,5, 6,2,5}, 
            	// Floor
            	{2,1,5, 6,1,8}, 
            	{3,1,2, 5,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,5,5, 1,5,9}, {3,6,5, 3,6,9}, {5,6,5, 5,6,9}, {7,5,5, 7,5,9}, 
    			{2,5,0, 2,5,3}, {4,6,0, 4,6,4}, {6,5,0, 6,5,4}, 
    			// Entry steps
    			{7,1,0, 7,1,2}, {9,0,0, 9,0,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,4,5, 0,4,9}, {2,5,5, 2,5,9}, {4,6,5, 4,6,9}, {6,5,5, 6,5,9}, {8,4,5, 8,4,9}, 
    			{1,4,0, 1,4,4}, {3,5,0, 3,5,4}, {5,5,0, 5,5,4}, {7,4,0, 7,4,4},
    			// Front porch
    			{2,1,1, 6,1,1},  
            	// Entry steps
    			{8,0,0, 8,0,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{6,3,6}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		// Porch
        		{3,3,0}, 
        		// Interior
        		{4,4,7}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Lower
        		{3,2,3, 5,2,4, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet.getStateFromMeta(uuvvww[6]), Blocks.carpet.getStateFromMeta(uuvvww[6]), false);	
            }
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{4,5,0, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{5,2,2, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
        	
        	// Crafting Table
        	IBlockState biomeCraftingTableState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.crafting_table.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,2,6}, 
           		})
        	{
            	this.setBlockState(world, biomeCraftingTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,2,4, 1}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.furnace.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{2,3,4, 2,4,4}, {2,6,4, 2,6,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,5,4, 2,5,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,2,8, 1, GeneralConfig.useVillageColors ? this.townColor3 : 10}, // Purple
            	{5,2,8, 3, GeneralConfig.useVillageColors ? this.townColor3 : 10}, // Purple
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
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
	                	{3,2,7, -1, 0}, 
	                	{5,2,7, -1, 0}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Shepherd House 1 --- //
    // designed by AstroTibs
    
    public static class SwampShepherdHouse1 extends StructureVillagePieces.Village
    {
    	public boolean entitiesGenerated = false;
    	public ArrayList<Integer> decorHeightY = new ArrayList<Integer>();
    	public FunctionsVN.VillageType villageType = null;
    	public FunctionsVN.MaterialType materialType = null;
    	public boolean disallowModSubs = false;
    	public int townColor = -1;
    	public int townColor2 = -1;
    	public int townColor3 = -1;
    	public int townColor4 = -1;
    	public int townColor5 = -1;
    	public int townColor6 = -1;
    	public int townColor7 = -1;
    	public String namePrefix = "";
    	public String nameRoot = "";
    	public String nameSuffix = "";
    	public BiomeGenBase biome = null;
    	
    	private static final String[] foundationPattern = new String[]{
				"         ", 
				" FFFFFFF ", 
				" FFFFFFF ", 
				" FFFFFFF ", 
				" FFFFFFF ", 
				" FFFFFFF ", 
				" FFFFFFF ", 
				" FFFFFFFF", 
				"        P"
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
		// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1+8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 7;
    	private static final int DECREASE_MAX_U = -1;
    	private static final int INCREASE_MIN_W = -1;
    	private static final int DECREASE_MAX_W = 6;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampShepherdHouse1() {}
    	
    	public SwampShepherdHouse1(StructureVillageVN.StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
    		this.boundingBox = boundingBox;
    		// Additional stuff to be used in the construction
    		if (start != null)
    		{
    			this.villageType = start.villageType;
    			this.materialType = start.materialType;
    			this.disallowModSubs = start.disallowModSubs;
    			this.townColor = start.townColor;
    			this.townColor2 = start.townColor2;
    			this.townColor3 = start.townColor3;
    			this.townColor4 = start.townColor4;
    			this.townColor5 = start.townColor5;
    			this.townColor6 = start.townColor6;
    			this.townColor7 = start.townColor7;
    			this.namePrefix = start.namePrefix;
    			this.nameRoot = start.nameRoot;
    			this.nameSuffix = start.nameSuffix;
    			this.biome = start.biome;
    		} 
    	}
    	
    	public static SwampShepherdHouse1 buildComponent(StructureVillageVN.StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, 8, STRUCTURE_DEPTH, coordBaseMode);
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampShepherdHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
			
			WorldChunkManager chunkManager= world.getWorldChunkManager();
			int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
			BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
			if (this.villageType==null)
			{
				try {
					String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
					if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
					else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
					}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
				try {
					String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
					if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
					else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
					}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
			}
			
			if (!this.disallowModSubs)
			{
				try {
					String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
					if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
					else {this.disallowModSubs = false;}
					}
				catch (Exception e) {this.disallowModSubs = false;}
			}
			// Reestablish biome if start was null or something
			if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
			
			IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			// Establish top and filler blocks, substituting Grass and Dirt if they're null
			IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
			IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
			
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
					this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
				}
				else if (unitLetter.equals("P"))
				{
					// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
					this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
					StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
				}
				else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
				{
					// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
					this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
					this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
				}
			}}
			
			// Yeah so I screwed up and accidentally deleted this, so had to recover it from decompiling :|
			// Sorry if this is illegible
			
			// Grass
    		for (int[] uuvvww : new int[][] { { 1, 0, 1, 7, 0, 7 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);
    		}
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][] { { 1, 1, 7, 1, 5, 7 }, { 7, 1, 7, 7, 5, 7 }, { 1, 1, 1, 1, 5, 1 }, { 7, 1, 1, 7, 5, 1 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false); 
    		}
    		
    		// Logs (Along)
    		IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), false);
    		for (int[] uuvvww : new int[][] { { 1, 6, 1, 1, 6, 7 }, { 7, 6, 1, 7, 6, 7 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false); 
    		}
    		
    		// Planks
    		IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][] { 
    				{ 2, 3, 7, 5, 4, 7 }, { 4, 5, 7, 5, 5, 7 }, { 6, 3, 7, 6, 3, 7 }, { 1, 3, 2, 1, 4, 6 }, { 1, 5, 2, 1, 5, 2 }, { 1, 5, 6, 1, 5, 6 }, { 7, 3, 2, 7, 4, 6 }, { 7, 5, 2, 7, 5, 2 }, { 7, 5, 6, 7, 5, 6 }, { 2, 3, 1, 6, 4, 1 }, 
    				{ 4, 5, 1, 4, 5, 1 }, { 2, 3, 2, 3, 3, 6 }, { 4, 3, 6, 4, 3, 6 }, { 5, 3, 2, 6, 3, 6 }, { 8, 0, 1, 8, 0, 1 }, { 1, 7, 4, 7, 7, 4 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);
    		}
    		
    		// Fence
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][] { { 4, 3, 8, 4, 4, 8 }, { 0, 3, 4, 0, 4, 4 }, { 4, 3, 0, 4, 4, 0 }, { 2, 1, 7, 6, 1, 7 }, { 1, 1, 2, 1, 1, 6 }, { 7, 1, 2, 7, 1, 6 }, { 2, 1, 1, 6, 1, 1 }, { 4, 1, 4, 4, 1, 4 }, { 4, 1, 5, 4, 2, 5 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
    		
    		// Wooden Stairs
    		Block biomeWoodStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][] { { 4, 3, 5, 3 }, { 4, 2, 4, 3 }, { 4, 1, 3, 3 } })
    		{
    			setBlockState(world, biomeWoodStairsBlock.getStateFromMeta(getMetadataWithOffset(Blocks.oak_stairs, uvwo[3] % 4) + uvwo[3] / 4 * 4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		// Wooden Slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][] { { 2, 6, 7, 6, 6, 7 }, { 1, 7, 5, 7, 7, 5 }, { 1, 7, 3, 7, 7, 3 }, { 2, 6, 1, 6, 6, 1 }, { 8, 1, 2, 8, 1, 2 }, { 8, 2, 4, 8, 2, 4 }, { 8, 3, 6, 8, 3, 6 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
    		}
    		
    		// Wooden Slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][] { { 2, 6, 6, 6, 6, 6 }, { 2, 6, 2, 6, 6, 2 }, { 8, 1, 3, 8, 1, 3 }, { 8, 2, 5, 8, 2, 5 }, { 8, 3, 7, 8, 3, 8 }, { 5, 3, 8, 7, 3, 8 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);
    		}
    		
    		// Hanging Lanterns
    		IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
    		for (int[] uvw : new int[][] { { 4, 2, 8 }, { 0, 2, 4 }, { 4, 2, 0 } })
    		{
    			setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		// Sitting Lanterns
    		IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
    		for (int[] uvw : new int[][] { { 7, 2, 4 } })
    		{
    			setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		// Torches
    		for (int[] uvwo : new int[][] { { 2, 6, 4, 1 }, { 6, 6, 4, 3 } })
    		{
    			setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		// Trapdoors (Top Horizontal)
    		Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uuvvww : new int[][] { { 4, 3, 4, 3 }, { 4, 3, 3, 3 }, { 4, 3, 2, 3 } })
    		{
    			setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, false)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
    		}
    		
    		// Wooden Doors
    		Block biomeWoodDoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwoor : new int[][] { { 6, 4, 7, 0, 1, 1 } }) {
    			for (int height = 0; height <= 1; height++)
    			{
    				setBlockState(world, biomeWoodDoorBlock.getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, (uvwoor[4] == 1), (uvwoor[5] == 1))[height]), uvwoor[0], uvwoor[1] + height, uvwoor[2], structureBB);
    			}
    		}
    		
    		// Glass Panes
    		for (int[] uuvvww : new int[][] { { 2, 5, 7, 3, 5, 7 }, { 1, 5, 3, 1, 5, 5 }, { 7, 5, 3, 7, 5, 5 }, { 2, 5, 1, 3, 5, 1 }, { 5, 5, 1, 6, 5, 1 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.glass_pane.getDefaultState(), Blocks.glass_pane.getDefaultState(), false);
    		}
    		
    		// Polished Diorite - not biome adjusted
    		IBlockState polishedDioriteState = Blocks.stone.getStateFromMeta(4);
    		for (int[] uvw : new int[][] { { 6, 4, 2 } })
    		{
    			setBlockState(world, polishedDioriteState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		// Potted Random Flower
    		for (int[] uvw : new int[][] { { 6, 5, 2 } }) {
    			int i = uvw[0], v = uvw[1], j = uvw[2];
    			int x = getXWithOffset(i, j);
    			int y = getYWithOffset(v);
    			int z = getZWithOffset(i, j);
    			
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.yellow_flower, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.red_flower, randomPottedPlant);}          // Every other type of flower
    		}
    		
    		// Loom
        	IBlockState loomState = ModObjects.chooseModLoom();
    		for (int[] uvw : new int[][] { { 2, 4, 2, 0 } })
    		{
    			setBlockState(world, loomState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		// Wool
    		for (int[] uvwo : new int[][]
    				{
    			{ 2, 4, 6, GeneralConfig.useVillageColors ? this.townColor2 : 13 },
    			{ 2, 4, 5, GeneralConfig.useVillageColors ? this.townColor3 : 10 },
    			{ 2, 5, 6, GeneralConfig.useVillageColors ? this.townColor4 : 14 } 
    		})
    		{
    			setBlockState(world, Blocks.wool.getStateFromMeta(uvwo[3]), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		// Carpet
    		for (int[] uuvvww : new int[][] { { 6, 4, 3, 6, 4, 6, GeneralConfig.useVillageColors ? this.townColor4 : 14 }, { 5, 4, 2, 5, 4, 6, GeneralConfig.useVillageColors ? this.townColor4 : 14 } })
    		{
    			fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet.getStateFromMeta(uuvvww[6]), Blocks.carpet.getStateFromMeta(uuvvww[6]), false);
    		}
    		
    		// Unkempt Grass
    		for (int[] uvwg : new int[][]{
    			{2,1,5, 0}, 
    			{3,1,2, 0}, 
    			{5,1,5, 0}, 
    			{6,1,6, 0} 
    			})
    		{
    			if (uvwg[3] == 0) // Tall grass
    			{
    				setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else if (uvwg[3] == 1) // Double-tall grass
    			{
    				setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1] + 1, uvwg[2], structureBB);
    			}
    			else if (uvwg[3] == 2) // Fern
    			{
    				setBlockState(world, Blocks.tallgrass.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Tall fern
    			{
    				setBlockState(world, Blocks.double_plant.getStateFromMeta(3), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1] + 1, uvwg[2], structureBB);
    			} 
    		}
    		
    		// Entities
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated = true;
    			
    			int s = random.nextInt(21);
    			
    			int i = (s <= 1) ? 2 : ((s <= 16) ? ((s - 2) / 5 + 3) : 6);
    			int v = 4;
    			int j = (s <= 1) ? (s - 3) : ((s <= 16) ? ((s - 2) % 5 + 2) : (s - 14));
    			
    			EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 4, 0); // Fletcher
    			entityvillager.setLocationAndAngles(getXWithOffset(i, j) + 0.5D, getYWithOffset(v) + 0.5D, getZWithOffset(i, j) + 0.5D, random.nextFloat() * 360.0F, 0.0F);
    			
    			world.spawnEntityInWorld((Entity)entityvillager);
    			
    			// Sheep
    			for (int[] uvw : new int[][] { { 3, 1, 6 }, { 6, 1, 4 } }) {
    				EntitySheep animal = new EntitySheep(world);
    				IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
    				animal.setLocationAndAngles(getXWithOffset(uvw[0], uvw[2]) + 0.5D, getYWithOffset(uvw[1]) + 0.5D, getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat() * 360.0F, 0.0F);
    				world.spawnEntityInWorld((Entity)animal);
    			} 
    		}
    		
    		// Clean items
    		if (VillageGeneratorConfigHandler.cleanDroppedItems)
    		{
    			StructureVillageVN.cleanEntityItems(world, this.boundingBox);
    		}
    		
    		return true;
    	}
    	
		/**
		 * Returns the villager type to spawn in this component, based on the number
		 * of villagers already spawned.
		 */
    	protected int getVillagerType(int number) {return 0;}
    }
    
    
	// --- Shepherd House 2 --- //
	// designed by AstroTibs

	public static class SwampShepherdHouse2 extends StructureVillagePieces.Village
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
		public BiomeGenBase biome=null;
		
		// Make foundation with blanks as empty air and F as foundation spaces
		private static final String[] foundationPattern = new String[]{
				" FFFFF  ",
				"FFFFFFF ",
				"FFFFFFF ",
				"FFFFFFF ",
				"FFFFFFFF",
				"FFFFFFFF",
				"FFFFFFFF",
				" FFFFFFF",
				" FFFFF P",
				"  FFF  P",
				"   PPPPP",
		};
		// Here are values to assign to the bounding box
		public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
		public static final int STRUCTURE_DEPTH = foundationPattern.length;
		public static final int STRUCTURE_HEIGHT = 8;
		// Values for lining things up
		private static final int GROUND_LEVEL = 4; // Spaces above the bottom of the structure considered to be "ground level"
		public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
		private static final int INCREASE_MIN_U = 3;
		private static final int DECREASE_MAX_U = 0;
		private static final int INCREASE_MIN_W = 0;
		private static final int DECREASE_MAX_W = 0;
		
		private int averageGroundLevel = -1;
		
		public SwampShepherdHouse2() {}

		public SwampShepherdHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
		{
			super();
			this.coordBaseMode = coordBaseMode;
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
		
		public static SwampShepherdHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
		{
			StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
			
			return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampShepherdHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
									this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
									this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
							true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
					
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
			
			WorldChunkManager chunkManager= world.getWorldChunkManager();
			int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
			BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
			if (this.villageType==null)
			{
				try {
					String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
					if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
					else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
					}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
				try {
					String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
					if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
					else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
					}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
			}
			
			if (!this.disallowModSubs)
			{
				try {
					String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
					if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
					else {this.disallowModSubs = false;}
					}
				catch (Exception e) {this.disallowModSubs = false;}
			}
			// Reestablish biome if start was null or something
			if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
			
			IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
			// Establish top and filler blocks, substituting Grass and Dirt if they're null
			IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
			IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
			
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
					this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
				}
				else if (unitLetter.equals("P"))
				{
					// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
					this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
					StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
				}
				else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
				{
					// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
					this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
					this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
				}
			}}
			
			
			// Dirt
			for(int[] uuvvww : new int[][]{
				{1,4,4, 1,4,6}, 
				{2,4,3, 2,4,5}, {2,4,8, 2,4,9}, 
				{3,4,5, 3,4,5}, {3,4,9, 3,4,9}, 
				{4,4,3, 4,4,9}, {4,4,8, 4,4,9}, 
				{5,4,3, 5,4,5}, {5,4,8, 5,4,9}, 
				{6,4,6, 6,4,6}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);	
			}
			
			
			// Grass
			for(int[] uuvvww : new int[][]{
				{0,4,4, 0,4,9}, 
				{1,4,2, 1,4,3}, {1,4,7, 1,4,10}, 
				{2,4,1, 2,4,2}, {2,4,10, 5,4,10}, 
				{4,4,1, 4,4,2}, 
				{5,4,2, 5,4,2}, {5,4,9, 5,4,9}, 
				{6,4,3, 6,4,4}, {6,4,7, 6,4,9}, 
				{7,4,4, 7,4,6}, 
				// Top of hill
				{1,5,4, 1,5,6}, 
				{2,5,3, 4,5,9}, 
				{5,5,3, 5,5,8}, 
				{6,5,6, 6,5,6}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
			}
    		
    		
    		// Grass Path
        	for (int[] uvw : new int[][]{
        		{7,4,4}, {7,4,5}, 
            	}) {
        		int posX = this.getXWithOffset(uvw[0], uvw[2]);
    			int posY = this.getYWithOffset(uvw[1]);
    			int posZ = this.getZWithOffset(uvw[0], uvw[2]);
        		StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
            }

    		        	
        	// Clear out leaves to allow player to access the walkway
            for(int[] uuvvww : new int[][]{
    			// Basement
            	{1,1,6, 5,3,9}, 
            	// Entrance
            	{3,1,4, 3,4,4}, 
            	{3,2,3, 3,4,3}, 
            	{3,3,2, 3,4,2}, 
            	{3,4,1, 3,4,1}, 
    			})
    		{
            	this.fillWithAir(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5]);	
    		}
			
			
			// Cobblestone part 1
			IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for(int[] uuvvww : new int[][]{
				// Entrance
				{3,3,5, 3,3,5}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
			}
			
			
			// Fences
			IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uuvvww : new int[][]{
				{1,6,4, 1,6,6}, 
				{2,6,6, 2,6,9}, 
				{3,6,9, 4,6,9}, 
				{4,6,8, 5,6,8}, 
				{5,6,3, 5,6,4}, {5,6,6, 5,6,7}, 
				{2,6,3, 4,6,3}, 
				{2,6,4, 2,6,4}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
			}
			
			
			// Torches
			for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
				// Entry
				{3,3,4, 2}, 
				// Animal pen
				{2,7,6, -1}, 
				{5,7,3, -1}, 
				{5,7,7, -1}, 
				}) {
				this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
			}
			
			
			// Cobblestone part 2
			for(int[] uuvvww : new int[][]{
				// Walls
				{0,0,5, 0,3,9}, 
				{1,0,9, 1,3,9}, 
				{1,0,10, 5,3,10}, 
				{5,0,9, 5,3,9}, 
				{6,0,5, 6,3,9}, 
				{1,0,5, 2,3,5}, {4,0,5, 5,3,5}, 
				// Floor
				{2,0,9, 4,0,9}, 
				{1,0,6, 5,0,8}, 
				{3,0,4, 3,0,5}, 
				// Left entrance wall
				{2,1,3, 2,3,4}, {4,1,3, 4,3,4}, 
				{2,2,2, 2,3,2}, {4,2,2, 4,3,2}, 
				{2,3,1, 2,3,1}, {4,3,1, 4,3,1}, 
				// Ceiling
				{2,4,6, 4,4,7}, {3,4,8, 3,4,8}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
			}
			
			
			// Cobblestone stairs
			Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
			for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
				// Entry
				{3,3,1, 2}, 
				{3,2,2, 2}, 
				{3,1,3, 2}, 
				// Ceiling trim
				{1,3,6, 1+4}, {1,3,7, 1+4}, {1,3,8, 1+4}, 
				{2,3,8, 3+4}, 
				{2,3,9, 1+4}, 
				{3,3,9, 3+4}, 
				{4,3,9, 0+4}, 
				{4,3,8, 3+4}, 
				{5,3,6, 0+4}, {5,3,7, 0+4}, {5,3,8, 0+4}, 
				})
			{
				this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
			}
            
            
        	// Fence Gate (Along)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence_gate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{5,6,5}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlockstate.getBlock(), (biomeFenceGateBlockstate.getBlock().getMetaFromState(biomeFenceGateBlockstate)+1)%8, this.coordBaseMode)), uvw[0], uvw[1], uvw[2], structureBB);
            }
			
			
			// Wood stairs
			IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
				{7,4,3, 3}, 
				{6,5,5, 1}, 
				// Table
				{5,1,8, 3+4}, 
				{5,1,6, 2+4}, 
				})
			{
				this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
			}
			
			
			// Wooden slabs (Top)
			IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
			for(int[] uuvvww : new int[][]{
				// Table
				{5,1,7, 5,1,7}, 
				})
			{
				this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
			}
            
        	
            // Potted Cactus
            int flowernumber = 9; // Cactus
            
         	int potU = 5;
         	int potV = 2;
         	int potW = 7;
         	int potX = this.getXWithOffset(potU, potW);
         	int potY = this.getYWithOffset(potV);
         	int potZ = this.getZWithOffset(potU, potW);
         	
         	TileEntity flowerPot = (new BlockFlowerPot()).createNewTileEntity(world, flowernumber);
     		BlockPos flowerPotPos = new BlockPos(potX, potY, potZ);
     		world.setBlockState(flowerPotPos, Blocks.flower_pot.getDefaultState());
     		world.setTileEntity(flowerPotPos, flowerPot);
     		 
			
			// Hanging Lanterns
			IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
			for (int[] uvw : new int[][]{
				{3,3,8}, 
				}) {
				this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
			}
			
			
			// Doors
			IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
				// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				{3,1,5, 0, 1, 1}, 
			})
			{
				for (int height=0; height<=1; height++)
				{
					this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
							uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
				}
			}
			
			
			// Loom
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			IBlockState loomState = ModObjects.chooseModLoom();
			for(int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
				{1,1,7, 1}, 
				})
			{
				this.setBlockState(world, loomState, uvw[0], uvw[1], uvw[2], structureBB);
			}
			
			
			// Wool
			for (int[] uvwo : new int[][]{
				{1,1,6, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
			})
			{
				this.setBlockState(world, Blocks.wool.getStateFromMeta(uvwo[3]), uvwo[0], uvwo[1], uvwo[2], structureBB);
			}
			
			
			// Carpet
        	for(int[] uvwc : new int[][]{
        		{2,1,9, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{3,1,9, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
        		{4,1,9, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		
        		{2,1,8, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
        		{3,1,8, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{4,1,8, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
        		
        		{2,1,7, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{3,1,7, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
        		{4,1,7, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		
        		{2,1,6, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
        		{3,1,6, (GeneralConfig.useVillageColors ? this.townColor4 : 14)}, // Red
        		{4,1,6, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
        		})
            {
            	this.setBlockState(world, Blocks.carpet.getStateFromMeta(uvwc[3]), uvwc[0], uvwc[1], uvwc[2], structureBB);
            }
        	
        	
			// Solid color banners
			for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color
				// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
				
				{2,2,9, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
				{3,2,9, 2, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
				{4,2,9, 2, GeneralConfig.useVillageColors ? this.townColor4 : 14}, // Red
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
				world.setBlockState(bannerPos, Blocks.wall_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwoc[3], this.coordBaseMode.getHorizontalIndex(), true)), 2);
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				NBTTagCompound modifystanding = new NBTTagCompound();
				tilebanner.writeToNBT(modifystanding);
				modifystanding.setBoolean("IsStanding", false);
				modifystanding.setInteger("Base", 15 - uvwoc[4]);
				tilebanner.readFromNBT(modifystanding);
				
        		world.setTileEntity(bannerPos, tilebanner);
			}
			
			
			// Unkempt Grass
			for (int[] uvwg : new int[][]{ // g is grass type
				{1,5,8, 0}, 
				{3,6,8, 0}, 
				{2,5,10, 0}, 
				{5,5,9, 0}, 
				{6,5,7, 0}, 
			})
			{
				if (uvwg[3]==0) // Short grass
				{
					this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
				}
				else if (uvwg[3]==1) // Tall grass
				{
					this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
					this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
				}
				else if (uvwg[3]==2) // Fern
				{
					this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
				}
				else // Large Fern
				{
					this.setBlockState(world, Blocks.double_plant.getStateFromMeta(3), uvwg[0], uvwg[1], uvwg[2], structureBB);
					this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
				}
			}
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 1;
        	int chestV = 1;
        	int chestW = 8;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_shepherd");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
			
			
			// Entities
			if (!this.entitiesGenerated)
			{
				this.entitiesGenerated=true;
				
				// Villager
				int u = 2+random.nextInt(3);
				int v = 1;
				int w = 6+random.nextInt(5);
				
				EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 3, 0); // Shepherd
				
				entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
				world.spawnEntityInWorld(entityvillager);
				
				
				// Sheep in the yard
				for (int[] uvw : new int[][]{
					{3,6,6}, 
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
			
			// Clean items
			if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
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
    // designed by AstroTibs
    
    public static class SwampSmallHouse1 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"  FFF",
    			"FFFFF",
    			"FFFFF",
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
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampSmallHouse1() {}

    	public SwampSmallHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampSmallHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampSmallHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{4,1,0, 4,1,0},
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Entry/floor
    			{1,0,0, 1,0,1}, 
    			{0,0,2, 4,0,7}, 
    			// Front wall
    			{0,1,4, 0,3,4}, {1,3,4, 1,3,4}, {2,1,4, 2,3,4}, {3,1,4, 3,1,4}, {3,3,4, 3,3,4}, {4,1,4, 4,3,4}, 
    			// Left wall
    			{0,1,5, 0,1,5}, {0,3,5, 0,3,5}, {0,1,6, 0,3,6}, 
    			// Right wall
    			{4,1,5, 4,1,5}, {4,3,5, 4,3,5}, {4,1,6, 4,3,6}, 
    			// Back wall
    			{0,0,7, 1,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Front deck
            	{0,1,0}, {2,1,0}, 
            	// Awning support
            	{0,1,2}, {0,2,2}, {4,1,2}, {4,2,2}, 
            	// Windows
            	{0,2,5}, {3,2,4}, {4,2,5}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,4,5, 0,4,6}, {4,4,5, 4,4,6}, 
    			// Chimney
    			{2,0,8, 4,3,8}, {2,0,7, 2,0,7}, {4,0,7, 4,0,7}, {2,1,7, 4,3,7}, 
    			{3,4,7, 3,4,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{3,5,7, 3,5,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,3,2, 1,3,2}, {3,3,2, 4,3,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Cobblestone Slab (upper)
    		IBlockState biomeCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3+8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,3,3, 0,3,3}, {4,3,3, 4,3,3}, 
    			// Ceiling
    			{1,4,5, 3,4,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabUpperState, biomeCobblestoneSlabUpperState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{2,3,2, 2,3,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (upper)
    		IBlockState biomeMossyCobblestoneSlabUpperState = ModObjects.chooseModMossyCobblestoneSlabState(true);
    		if (biomeMossyCobblestoneSlabUpperState != null)
    		{
    			biomeMossyCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabUpperState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabUpperState = biomeCobblestoneSlabUpperState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{1,3,3, 3,3,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabUpperState, biomeMossyCobblestoneSlabUpperState, false);	
    		}
    		
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{0,4,7, 2}, {1,4,7, 2}, 
    			{0,5,6, 2}, {1,5,6, 2}, {2,5,6, 2}, {3,5,6, 2}, 
    			{0,5,5, 3}, {1,5,5, 3}, {3,5,5, 3}, 
    			{0,4,4, 3}, {3,4,4, 3}, 
        		})
            {
            	this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
    		// Mossy Cobblestone stairs
    		Block biomeMossyCobblestoneStairsBlock = ModObjects.chooseModMossyCobblestoneStairsBlock();
    		if (biomeMossyCobblestoneStairsBlock==null) {biomeMossyCobblestoneStairsBlock = Blocks.stone_stairs;}
    		biomeMossyCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{2,4,7, 2}, {4,4,7, 2}, 
    			{4,5,6, 2}, 
    			{2,5,5, 3}, {4,5,5, 3}, 
    			{1,4,4, 3}, {2,4,4, 3}, {4,4,4, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeMossyCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		{3,2,2}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{1,1,4, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,7, 2}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.furnace.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,5, 3, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
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
	                	{3,1,6, -1, 0}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Small House 2 --- //
    // designed by AstroTibs
    
    public static class SwampSmallHouse2 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			" FFFFF ",
    			"   FF  ",
    			"   FF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampSmallHouse2() {}

    	public SwampSmallHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampSmallHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampSmallHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			// Side
    			{0,3,10, 0,3,10}, {6,3,10, 6,3,10}, 
    			{0,3,8, 0,3,8}, {6,3,8, 6,3,8}, 
    			{0,3,6, 0,3,6}, {6,3,6, 6,3,6}, 
    			// Front awning
    			{1,3,3, 5,3,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Entry/floor
    			{1,0,3, 5,0,10}, 
    			// Front wall
    			{1,1,5, 2,3,5}, {3,3,5, 3,3,5}, {4,1,5, 5,3,5}, {2,4,6, 4,4,6},
    			// Left wall
    			{1,0,6, 1,3,6}, {1,0,7, 1,1,7}, {1,3,7, 1,3,7}, {1,0,8, 1,3,8}, {1,0,9, 1,1,9}, {1,3,9, 1,3,9}, 
    			// Right wall
    			{5,0,6, 5,3,6}, {5,0,7, 5,1,7}, {5,3,7, 5,3,7}, {5,0,8, 5,3,8}, {5,0,9, 5,1,9}, {5,3,9, 5,3,9}, 
    			// Back wall
    			{1,0,10, 5,3,10}, {2,4,10, 2,4,10}, {4,4,10, 4,4,10}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,0,0, 3,0,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,3,4, 5,3,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Front deck
            	{1,1,3}, {1,2,3}, {5,1,3}, {5,2,3}, 
            	// Windows
            	{3,4,10}, 
            	{1,2,9}, {5,2,9}, 
            	{1,2,7}, {5,2,7}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,4,6, 1,4,8}, {3,5,6, 3,5,6}, {3,5,8, 3,5,8}, {3,5,10, 3,5,10}, {5,4,6, 5,4,6}, {5,4,10, 5,4,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,4,9, 1,4,10}, {5,4,7, 5,4,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,4,6, 0,4,7}, 
    			{2,5,6, 2,5,8}, {2,5,10, 2,5,10}, 
    			{4,5,6, 4,5,7}, {4,5,10, 4,5,10}, 
    			{6,4,6, 6,4,10}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{0,4,8, 0,4,10}, 
    			{2,5,9, 2,5,9}, 
    			{4,5,8, 4,5,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Cobblestone Slab (upper)
    		IBlockState biomeCobblestoneSlabUpperState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3+8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{3,5,7, 3,5,7}, {3,5,9, 3,5,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabUpperState, biomeCobblestoneSlabUpperState, false);	
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		{3,4,8}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uvwc : new int[][]{
        		// Lower
        		{2,1,7, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
        		{3,1,6, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
        		{4,1,7, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
        		{2,1,6, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
        		{3,1,7, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
        		{4,1,6, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
        		})
            {
            	this.setBlockState(world, Blocks.carpet.getStateFromMeta(uvwc[3]), uvwc[0], uvwc[1], uvwc[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{3,1,5, 2, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Interior
    			{4,2,4, 2}, 
    			}) {
    			this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,8, 2, GeneralConfig.useVillageColors ? this.townColor3 : 10}, // Purple
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			uvwoc[4]);
            	}
            }
        	
            
            // Potted random flower
            for (int[] uvw : new int[][]{
        		{4,1,4}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.yellow_flower, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.red_flower, randomPottedPlant);}          // Every other type of flower
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 1;
        	int chestV = 1;
        	int chestW = 4;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Right side
        			{6,3,5, 1}, {6,2,5, 1}, {6,1,5, 1}, {6,0,5, 1}, 
        			// Away-facing vines
        			{1,3,11, 0}, 
        			{4,4,11, 0}, {4,3,11, 0}, {4,2,11, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
	                	{2,1,7, -1, 0}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Small House 3 --- //
    // Shrek's House
    // designed by AstroTibs
    
    public static class SwampSmallHouse3 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			" FFFFFF  ",
    			"FFFFFFFF ",
    			"FFFFFFFF ",
    			"FFFFFFFF ",
    			"FFFFFFF  ",
    			"FFFFFFF  ",
    			"FFFFFFFF ",
    			"FFFFFFFFF",
    			"  FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampSmallHouse3() {}

    	public SwampSmallHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampSmallHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampSmallHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Entry walkway
    			{4,0,0, 4,0,2}, 
    			// Front wall
    			{2,1,3, 3,3,3}, {4,3,3, 4,3,3}, {5,1,3, 5,3,3}, 
    			// Left wall
    			{2,1,4, 2,2,7}, {2,3,5, 2,3,6}, {1,1,7, 1,2,7}, 
    			// Back wall
    			{3,1,7, 6,3,7}, 
    			// Right wall
    			{7,1,5, 7,3,6}, {6,1,4, 6,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,0,3, 5,0,7}, {6,0,4, 6,0,7}, {7,0,5, 7,0,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
            
            
            // Dirt
            for(int[] uuvvww : new int[][]{
    			{0,1,3, 0,1,3}, {0,1,4, 0,2,5}, {0,1,6, 0,1,7}, 
    			{1,1,3, 1,2,3}, {1,1,4, 1,3,5}, {1,1,6, 1,2,6}, 
    			{1,1,8, 2,1,8}, 
    			{3,1,8, 6,2,8}, 
    			{4,4,3, 5,4,3}, 
    			{6,1,3, 6,3,3}, 
    			{7,1,7, 7,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);	
            }
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
    			{1,4,3, 3,4,5}, {2,4,6, 7,4,6}, {2,4,7, 6,4,7}, {2,4,3, 6,4,5}, 
    			{0,2,4, 0,2,5}, {1,3,6, 1,3,7}, {2,3,7, 2,3,7}, {3,3,8, 6,3,8}, {7,3,7, 7,3,7}, 
    			{0,2,3, 0,2,3}, {0,2,6, 0,2,7}, {1,2,8, 2,2,8}, 
    			{0,1,2, 2,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		{4,4,4, 5,6,5}, {4,7,4, 4,7,4}, 
        		{6,3,2, 6,4,2}, {7,1,2, 7,2,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
        	// Stripped Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), true);
        	IBlockState biomeStrippedLogHorizAcrossState = biomeLogHorAcrossState;
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log || biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.log)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.log2)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	{7,3,3, 8,3,3}, 
            	{2,3,4, 4,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
            
        	// Stripped Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false);
        	IBlockState biomeStrippedLogHorizAlongState = biomeLogHorAlongState;
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogHorizAlongState.getBlock()==Blocks.log || biomeStrippedLogHorizAlongState.getBlock()==Blocks.log2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.log)
            	{
            		biomeStrippedLogHorizAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2==0? 1:0));
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.log2)
            	{
            		biomeStrippedLogHorizAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(this.coordBaseMode.getHorizontalIndex()%2==0? 1:0));
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	{1,3,3, 1,3,3}, 
            	{5,5,2, 5,5,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		{5,3,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{3,3,5, 3,3,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            }
            
                    	
            // Polished Granite
            IBlockState polishedGraniteState = ModObjects.chooseModPolishedGraniteBlockState();
            if (polishedGraniteState==null) {polishedGraniteState = ModObjects.chooseModSmoothStoneBlockState();} // Guarantees a vanilla stone if this fails
        	for (int[] uvw : new int[][]{
        		{3,1,6}, 
        		})
            {
            	this.setBlockState(world, polishedGraniteState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Potted random flower
            for (int[] uvw : new int[][]{
        		{3,2,6}, 
        		})
            {
        		int u=uvw[0]; int v=uvw[1]; int w=uvw[2];
                int x = this.getXWithOffset(u, w);
                int y = this.getYWithOffset(v);
                int z = this.getZWithOffset(u, w);
            	
            	IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        		int randomPottedPlant = random.nextInt(10)-1;
        		if (randomPottedPlant==-1) {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.yellow_flower, 0);} // Dandelion specifically
        		else {StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, new BlockPos(x, y, z), Blocks.red_flower, randomPottedPlant);}          // Every other type of flower
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{4,1,3, 2, 1, 0}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Unkempt Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			{0,4,5, 0}, 
    			{1,2,2, 0}, {1,5,4, 0}, {1,4,7, 2}, 
    			{2,5,3, 0}, {2,5,5, 0}, 
    			{3,4,8, 0}, 
    			{5,5,7, 2}, 
    			{6,5,3, 2}, 
    			{7,5,4, 0}, {7,5,6, 0}, 
    		})
    		{
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==1)// Tall grass
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==2) // Fern
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Large Fern
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(3), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    		}
            
            
        	// Blue Orchid
        	for (int[] uvw : new int[][]{
        		// Back window
        		{2,2,2}, 
        		})
            {
        		this.setBlockState(world, Blocks.red_flower.getStateFromMeta(1), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{6,1,5, 2, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			uvwoc[4]);
            	}
            }
        	
        	
            // Decor
            int[][] decorUVW = new int[][]{
            	{2,1,0}, {8,1,0}, 
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
            	
            	//LogHelper.info("Decor spawned at: " + this.getXWithOffset(uvw[0], uvw[2]) + " " + (groundLevelY+this.boundingBox.minY) + " " + this.getZWithOffset(uvw[0], uvw[2]));
            	
            	// Generate decor
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
            
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	                	{5,1,5, -1, 0}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Small House 4 --- //
    // designed by THASSELHOFF
    
    public static class SwampSmallHouse4 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"         ",
    			"         ",
    			"  F   F  ",
    			"         ",
    			"         ",
    			"         ",
    			"  F   F  ",
    			"         ",
    			"  F      ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 4;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampSmallHouse4() {}

    	public SwampSmallHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampSmallHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampSmallHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		{2,0,6, 2,5,6}, {6,0,6, 6,5,6}, 
        		{2,0,2, 2,5,2}, {6,0,2, 6,5,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Front wall
    			{3,3,2, 4,3,2}, {4,4,2, 4,4,2}, {3,5,2, 5,5,2}, 
    			// Left wall
    			{2,3,3, 2,3,5}, {2,4,5, 2,4,5}, {2,5,3, 2,5,5}, 
    			// Right wall
    			{6,3,3, 6,3,5}, {6,4,3, 6,4,3}, {6,5,3, 6,5,5}, 
    			// Back wall
    			{3,3,6, 5,3,6}, {3,5,6, 5,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Balcony
    			{2,2,1, 6,2,1}, 
    			// Floor
    			{2,2,3, 2,2,3}, {2,2,5, 2,2,5},
    			{6,2,3, 6,2,3}, {6,2,5, 6,2,5}, 
    			{3,2,6, 3,2,6}, {5,2,6, 5,2,6}, 
    			{3,2,2, 5,2,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
			
			
			// Wood stairs
			IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
				// Entrance stairs
				{2,0,0, 0}, {3,1,0, 0}, {4,2,0, 0}, 
				{3,0,0, 1+4}, {4,1,0, 1+4}, {5,2,0, 1+4}, 
				// Chairs
				{3,3,3, 2}, {3,3,5, 3}, 
				})
			{
				this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
			}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Posts
            	{2,3,1}, {6,3,1},
            	// Table
            	{3,3,4}, 
            	// Window
            	{3,4,2}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{3,4,4}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{2,2,4}, {6,2,4}, 
            	{4,2,6}, 
            	{3,5,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Sitting Lanterns
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,4,1}, {6,4,1}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{5,3,4, 2, GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			uvwoc[4]);
            	}
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,6,2, 2,6,3}, {2,6,5, 2,6,6}, 
    			{3,6,2, 4,6,5}, 
    			{5,6,4, 5,6,6}, 
    			{6,6,3, 6,6,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{2,6,4, 2,6,4}, {3,6,6, 4,6,6}, {5,6,2, 6,6,2}, {5,6,3, 5,6,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,6,1, 1,6,2}, {1,6,5, 1,6,7}, 
    			{4,6,7, 7,6,7}, 
    			{7,6,3, 7,6,6}, 
    			{2,6,1, 5,6,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{1,6,3, 1,6,4}, 
    			{2,6,7, 3,6,7}, 
    			{6,6,1, 7,6,1}, 
    			{7,6,2, 7,6,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
            
            
            // Grass - not biome adjusted
            for(int[] uuvvww : new int[][]{
    			{1,3,3, 1,3,4}, 
    			{3,3,7, 5,3,7}, 
    			{7,3,4, 7,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.grass.getDefaultState(), Blocks.grass.getDefaultState(), false);	
            }
    		
    		
    		// Trapdoor (Top Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Windows
            	{2,4,3, 1}, {2,4,4, 1}, 
            	{3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
            	{6,4,4, 3}, {6,4,5, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Trapdoor (Bottom Vertical)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Planters
            	{1,3,2, 2}, {0,3,3, 3}, {0,3,4, 3}, {1,3,5, 0}, 
            	{2,3,7, 3}, {3,3,8, 0}, {4,3,8, 0}, {5,3,8, 0}, {6,3,7, 1}, 
            	{7,3,3, 2}, {8,3,4, 1}, {8,3,5, 1}, {7,3,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
    		
    		// Trapdoor (Top Horizontal)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Planters
            	{1,2,3, 3}, {1,2,4, 3}, 
            	{3,2,7, 0}, {4,2,7, 0}, {5,2,7, 0}, 
            	{7,2,4, 1}, {7,2,5, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, true, false)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
        	
        	// Random Flowers in pairs
            IBlockState cornflowerState = ModObjects.chooseModCornflower(); IBlockState lilyOfTheValleyState = ModObjects.chooseModLilyOfTheValley();
        	int flowerindex = random.nextInt(10 + (cornflowerState!=null && lilyOfTheValleyState!=null ? 2:0));
        	int flowerindex2 = flowerindex;
        	int flowerindex3 = flowerindex;
        	while (flowerindex2==flowerindex) {flowerindex2 = random.nextInt(10 + (cornflowerState!=null && lilyOfTheValleyState!=null ? 2:0));}
        	while (flowerindex3==flowerindex || flowerindex3==flowerindex2) {flowerindex3 = random.nextInt(10 + (cornflowerState!=null && lilyOfTheValleyState!=null ? 2:0));}
        	
        	for (int[] uvwf : new int[][]{
        		{1,4,3, flowerindex}, {1,4,4, flowerindex}, 
        		{3,4,7, flowerindex2}, {4,4,7, flowerindex2}, {5,4,7, flowerindex2}, 
        		{7,4,4, flowerindex3}, {7,4,5, flowerindex3}, 
        		})
            {
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10 is cornflower, 11 is lily of the valley
        		IBlockState flowerstate;
            	if (flowerindex==10 && cornflowerState!=null) {flowerstate=cornflowerState;}
            	else if (flowerindex==11 && lilyOfTheValleyState!=null) {flowerstate=lilyOfTheValleyState;}
            	else {flowerstate = (flowerindex==9 ? Blocks.yellow_flower:Blocks.red_flower).getStateFromMeta(new int[]{0,1,2,3,4,5,6,7,8,0}[flowerindex%10]);}
        		
        		this.setBlockState(world, flowerstate, uvwf[0], uvwf[1], uvwf[2], structureBB);	
            }
            
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Left side
        			{1,5,3, 3}, {1,5,4, 3}, 
        			// Back side
        			{2,5,7, 0}, {2,4,7, 0}, {3,5,7, 0}, 
        			// Right side
        			{7,5,2, 1}, {7,4,2, 1}, {7,3,2, 1}, {7,2,2, 1}, 
        			// Front side
        			{6,5,1, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);	
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
	                	{4,3,4, -1, 0}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Small House 5 --- //
    // designed by THASSELHOFF
    
    public static class SwampSmallHouse5 extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"     ",
    			"F   F",
    			"  FF ",
    			"     ",
    			" F   ",
    			"F   F",
    			"F    ",
    			"F    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = -1;
    	private static final int DECREASE_MAX_U = 3;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampSmallHouse5() {}

    	public SwampSmallHouse5(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampSmallHouse5 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampSmallHouse5(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		{0,1,6, 0,6,6}, {4,1,6, 4,6,6}, 
        		{0,1,2, 0,6,2}, {4,1,2, 4,6,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			// Entrance
            	{0,1,1, 0,1,1}, 
            	// Front wall
    			{1,4,2, 2,4,2}, {2,5,2, 2,5,2}, {1,6,2, 3,6,2}, 
    			// Left wall
    			{0,4,3, 0,4,5}, {0,5,5, 0,5,5}, {0,6,3, 0,6,5}, 
    			// Right wall
    			{4,4,3, 4,4,5}, {4,5,5, 4,5,5}, {4,6,3, 4,6,5}, 
    			// Back wall
    			{1,4,6, 3,4,6}, {2,5,6, 2,5,6}, {1,6,6, 3,6,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Balcony
    			{4,3,1, 4,3,1}, 
    			// Floor
    			{0,3,3, 0,3,3}, {0,3,5, 0,3,5}, 
    			{4,3,3, 4,3,3}, {4,3,5, 4,3,5}, 
    			{1,3,6, 1,3,6}, {3,3,6, 3,3,6}, 
    			{1,3,2, 3,3,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
			
			
			// Wood stairs
			IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
			for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
				// Entrance stairs
				{0,1,0, 3}, 
				{1,2,1, 0}, {2,3,1, 0}, 
				{1,1,1, 1+4}, {2,2,1, 1+4}, {3,3,1, 1+4}, 
				})
			{
				this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
			}
        	
        	            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	{4,4,1}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	// Front
        		{0,6,1}, {4,6,1}, 
        		// Interior
            	{1,6,3}, 
            	// Under-farm
            	{2,3,6}, 
            	{0,3,4}, {4,3,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,4,4, 2, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
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
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			uvwoc[4]);
            	}
            }
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,7,6, 4,7,6}, {4,7,2, 4,7,5}, 
    			{0,7,4, 0,7,4}, 
    			{0,7,2, 2,7,2}, 
    			{0,8,4, 2,8,4}, {2,8,5, 2,8,5}, {4,8,4, 4,8,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,7,5, 1,7,5}, 
    			{0,7,3, 0,7,3}, {0,7,3, 0,7,3}, 
    			{3,7,2, 3,7,2}, 
    			{3,8,4, 3,8,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,7,1, 2,7,1}, 
    			{0,7,7, 0,7,7}, {3,7,7, 4,7,7}, 
    			{1,8,3, 4,8,3}, 
    			{3,8,5, 4,8,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{3,7,1, 4,7,1}, 
    			{1,7,7, 2,7,7}, 
    			{0,8,3, 0,8,3}, 
    			{0,8,5, 1,8,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{2,5,5, 2,6,5}, {2,9,5, 2,9,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,4,5, 2}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.furnace.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
			
			
			// Wool
			for (int[] uvwo : new int[][]{
				{1,7,3, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
				{2,7,3, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
				{3,7,3, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
				
				{1,7,4, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
				{2,7,4, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
				{3,7,4, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
				
				{1,7,5, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
				{2,7,5, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
				{3,7,5, (GeneralConfig.useVillageColors ? this.townColor2 : 13)}, // Green
			})
			{
				this.setBlockState(world, Blocks.wool.getStateFromMeta(uvwo[3]), uvwo[0], uvwo[1], uvwo[2], structureBB);
			}
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,5,6}, {3,5,6}, 
        		{0,5,3}, {0,5,4}, 
        		{4,5,3}, {4,5,4}, 
        		{1,5,2}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            // Moist Farmland with crop above
            for(int[] uvwfcp : new int[][]{
            	// u,v,w, farmland moisture (0:dry, 7:moist), crop (0:wheat, 1:potato, 2:carrot, 3:melon, 4:pumpkin), crop progress
            	// Front left
            	{3,0,5, 7, 4, 7}, 
            	{1,0,3, 7, 2, random.nextInt(5)}, 
            	})
            {
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvwfcp[0], uvwfcp[1]-1, uvwfcp[2], structureBB);
            	//this.clearCurrentPositionBlocksUpwards(world, uvwpmc[0], uvwpmc[1]+1, uvwpmc[2], structureBB);
            	this.setBlockState(world, (uvwfcp[4]==0?Blocks.wheat:uvwfcp[4]==1?Blocks.potatoes:uvwfcp[4]==2?Blocks.carrots:uvwfcp[4]==3?Blocks.melon_stem:Blocks.pumpkin_stem).getStateFromMeta(uvwfcp[5]), uvwfcp[0], uvwfcp[1]+1, uvwfcp[2], structureBB); 
            	this.setBlockState(world, Blocks.farmland.getStateFromMeta(uvwfcp[3]), uvwfcp[0], uvwfcp[1], uvwfcp[2], structureBB); // 7 is moist
            }
            
            // Pumpkin
        	for (int[] uvw : new int[][]{
        		{2,1,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.pumpkin.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Water boundary
    		// This puts up a dirt/grass wall to keep in the water if it's not level with the surrounding ground
            for(int[] uwuwvs : new int[][]{ // u/w box corners, v height, and the side that is to be evaluated.
            	// Side - 0:forward (away from you), 1:rightward, 2:backward (toward you), 3: leftward
            	// Right water space
    			{3,2, 3,4, 0, 1}, // Right side
    			{3,3, 3,3, 0, 0}, // Far side
    			{3,3, 3,3, 0, 2}, // Close side
    			{3,2, 3,3, 0, 3}, // Left side
    			// Left water space
    			{2,4, 2,5, 0, 3}, // Left side
    			})
    		{
            	int u_offset=0, w_offset=0;
            	int v = uwuwvs[4];
            	
            	switch(uwuwvs[5])
            	{
            	case 0: w_offset=1; break; // forward
            	case 1: u_offset=1; break; // rightward
            	case 2: w_offset=-1; break; // backward
            	case 3: u_offset=-1; break; // leftward
            	default:
            	}
            	
            	// Scan boundary and add containment if necessary
            	for (int u=uwuwvs[0]; u<=uwuwvs[2]; u++) {for (int w=uwuwvs[1]; w<=uwuwvs[3]; w++)
            	{
            		int x = this.getXWithOffset(u+u_offset, w+w_offset);
            		int y = this.getYWithOffset(v);
            		int z = this.getZWithOffset(u+u_offset, w+w_offset);
            		
            		BlockPos pos = new BlockPos(x, y, z);
            		
            		// If space above bordering block is liquid, fill below with filler and cap with topper
            		if (world.getBlockState(pos.up()).getBlock().getMaterial().isLiquid())
            		{
            			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u+u_offset, v, w+w_offset, structureBB);
            			this.setBlockState(world, biomeTopState, u+u_offset, v+1, w+w_offset, structureBB);
            		}
            		// If bordering block is air, fill below with filler and cap with topper
            		else if (world.isAirBlock(pos))
            		{
            			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u+u_offset, v-1, w+w_offset, structureBB);
            			this.setBlockState(world, biomeTopState, u+u_offset, v, w+w_offset, structureBB);
            		}
            	}}
    		}
            
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			{3,0,3, 3,0,3}, 
    			{2,0,4, 2,0,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);	
    		}
            
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward, 3:leftward
            		// Left side
        			{-1,6,4, 3}, {-1,6,3, 3}, {-1,5,3, 3}, 
        			// Back side
        			{0,6,7, 0}, {0,5,7, 0}, 
        			{1,6,7, 0}, {1,5,7, 0}, {1,4,7, 0}, {1,3,7, 0}, 
        			{2,6,7, 0}, {2,5,7, 0}, 
        			// Right side
        			{5,6,2, 1}, {5,5,2, 1}, {5,4,2, 1}, {5,3,2, 1}, 
        			{5,7,3, 1}, {5,6,3, 1}, {5,5,3, 1}, 
        			// Front side
        			{1,6,1, 2}, {1,5,1, 2}, 
        			{2,6,1, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);	
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
	                	{2,4,4, -1, 0}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Stable --- //
    // designed by THASSELHOFF
    
    public static class SwampStable extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"            FFFF  ",
    			" F      FFFFFFFFF ",
    			"        FFFFFFFFFF",
    			"        FFFFFFFFFF",
    			"        FFFFFFFFFF",
    			"        FFFFFFFFFF",
    			"        FFFFFFFFFF",
    			" F      FFFFFFFFF ",
    			"            FFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 13;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampStable() {}

    	public SwampStable(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampStable buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampStable(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
    			{8,0,2, 8,0,6}, 
    			{9,0,1, 11,0,7}, 
    			{12,0,0, 12,0,8}, 
    			{13,0,0, 13,0,6}, {13,0,8, 14,0,8}, 
    			{14,0,0, 14,0,5}, 
    			{15,0,1, 16,0,3}, {15,0,7, 15,0,8}, {16,0,6, 16,0,7}, 
    			{17,0,2, 17,0,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
    		
    		
    		// Unkempt Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			{13,1,1, 1}, 
    		})
    		{
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==1)// Tall grass
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==2) // Fern
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Large Fern
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(3), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    		}
    		
    		
    		// Stripped logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeStrippedLogVertState = biomeLogVertState;
    		// Try to see if stripped logs exist
    		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    		for (int[] uuvvww : new int[][]{
        		{1,1,7, 1,5,7}, {8,1,7, 8,5,7}, 
        		{1,1,1, 1,5,1}, {8,1,1, 8,5,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Back wall
            	{2,3,7, 7,3,7}, {2,4,7, 2,4,7}, {4,4,7, 5,4,7}, {7,4,7, 7,4,7}, {2,5,7, 7,5,7},
            	// Left wall
            	{1,3,2, 1,3,6}, {1,4,2, 1,4,2}, {1,4,4, 1,4,4}, {1,4,6, 1,4,6}, {1,5,2, 1,5,6},
            	// Right wall
            	{8,3,2, 8,5,2}, {8,3,6, 8,5,6}, 
            	// Front wall
            	{2,3,1, 7,3,1}, {2,4,1, 2,4,1}, {4,4,1, 5,4,1}, {7,4,1, 7,4,1}, {2,5,1, 7,5,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{11,1,3, 11,1,5}, {9,2,3, 9,2,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Floor main
    			{2,2,2, 8,2,6}, 
    			// Floor fringe
    			{2,2,7, 2,2,7}, {4,2,7, 5,2,7}, {7,2,7, 7,2,7}, 
    			{1,2,2, 1,2,3}, {1,2,5, 1,2,6}, 
    			{2,2,1, 2,2,1}, {4,2,1, 5,2,1}, {7,2,1, 7,2,1}, 
    			// Ramp
    			{10,1,3, 10,1,5}, 
    			// Entrance lip
    			{8,5,3, 8,5,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{1,6,1, 1,6,7}, {2,6,7, 2,6,7}, {4,6,7, 7,6,7}, 
    			{8,6,1, 8,6,3}, {8,6,5, 8,6,7}, 
    			{4,6,1, 5,6,1}, 
    			{3,7,3, 6,7,3}, {3,7,4, 3,7,4}, {6,7,4, 6,7,4}, {3,7,5, 6,7,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,6,7, 4,6,7}, {8,6,4, 8,6,4}, 
    			{2,6,1, 3,6,1}, {6,6,1, 7,6,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{0,6,1, 0,6,8}, 
    			{1,6,8, 6,6,8}, 
    			{9,6,0, 9,6,2}, {9,6,5, 9,6,6}, 
    			{2,6,0, 5,6,0}, {7,6,0, 8,6,0}, 
    			{2,7,2, 2,7,6}, {3,7,2, 6,7,2}, {3,7,6, 6,7,6}, {7,7,2, 7,7,6}, 
    			{4,8,4, 4,8,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{0,6,0, 1,6,0}, {6,6,0, 6,6,0}, {7,6,8, 9,6,8}, 
    			{9,6,3, 9,6,4}, {9,6,7, 9,6,7}, 
    			{5,8,4, 5,8,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Trapdoor (Bottom Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{6,3,2, 3}, 
            	{7,3,3, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], this.coordBaseMode, false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
    		
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			// Trough
    			{7,3,2, 7,3,2}, 
    			// Pond
    			{13,0,7, 13,0,7}, 
    			{14,0,6, 14,0,7}, 
    			{15,0,4, 15,0,6}, 
    			{16,0,4, 16,0,5}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);	
    		}
    		
    		
    		// Lilypad
    		for (int[] uvw : new int[][]{
    			{14,1,7}, 
    			{15,1,5}, 
    			{16,1,4}, 
    			}) {
    			this.setBlockState(world, Blocks.waterlily.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		// In hut
        		{2,6,4}, {7,6,4}, 
        		// Under house
        		{3,2,7}, {6,2,7}, 
        		{1,2,4}, 
        		{3,2,1}, {6,2,1}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Pen
    			{9,1,1, 12,1,1}, {12,1,0, 14,1,0}, {14,1,1, 14,1,1}, 
    			{9,3,1, 10,3,1}, {9,2,1, 12,2,1}, 
    			{16,1,1, 16,1,2}, {17,1,2, 17,1,6}, {16,1,6, 16,1,7}, {15,1,7, 15,1,8}, {12,1,8, 14,1,8}, 
    			{9,1,7, 12,1,7}, 
    			{9,3,7, 10,3,7}, {9,2,7, 12,2,7}, 
    			{8,1,2, 8,1,6}, 
    			// Windows
    			{3,4,7, 3,4,7}, {6,4,7, 6,4,7}, 
    			{1,4,3, 1,4,3}, {1,4,5, 1,4,5}, 
    			{3,4,1, 3,4,1}, {6,4,1, 6,4,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
            
            
        	// Fence Gate (Across)
        	IBlockState biomeFenceGateBlockstate = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence_gate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
        		{15,1,1}, 
            	})
            {
        		this.setBlockState(world, biomeFenceGateBlockstate.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateBlockstate.getBlock(), biomeFenceGateBlockstate.getBlock().getMetaFromState(biomeFenceGateBlockstate), this.coordBaseMode)), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Hay bales (vertical)
        	for (int[] uvw : new int[][]{
        		{2,3,6}, {2,4,6}, 
        		{4,3,3}, {4,4,2}, {5,3,2}, 
        		{6,3,6}, {8,3,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.hay_block.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Hay bales (along)
        	for (int[] uvw : new int[][]{
        		{2,3,5}, 
        		{3,3,2}, {4,3,2}, 
        		{4,3,2}, {7,3,5}, 
        		})
            {
        		this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(Blocks.hay_block.getStateFromMeta(0), this.coordBaseMode.getHorizontalIndex(), false), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Hay bales (across)
        	for (int[] uvw : new int[][]{
        		{3,3,6}, 
        		{7,3,6}, {7,4,6}, 
        		})
            {
        		this.setBlockState(world, StructureVillageVN.getHorizontalPillarState(Blocks.hay_block.getStateFromMeta(0), this.coordBaseMode.getHorizontalIndex(), true), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Back-facing vines
        			{1,5,0, 2}, {1,4,0, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
        	
    		
    		// Entities
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Cow
            	for (int[] uvw : new int[][]{
        			{15, 1, 3}, 
        			})
        		{
            		EntityCow animal = new EntityCow(world);
    				IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
    				
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
    			// Horse
            	for (int[] uvw : new int[][]{
            		{13, 1, 4}, 
        			})
        		{
    				EntityHorse animal = new EntityHorse(world);
    				IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
    				
                	if (VillageGeneratorConfigHandler.nameVillageHorses && GeneralConfig.nameEntities)
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
    		
    		// Clean items
    		if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
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
    // designed by AstroTibs
    
    public static class SwampTannery extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"    FFFFFFFF",
    			" FFFFFFFFFFF",
    			"FFF FFFFFFFF",
    			"FFF FFFFFFFF",
    			"FFF FFFFFFFF",
    			"    FFF     ",
    			"    FFF     ",
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
    	private static final int DECREASE_MAX_W = 1;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampTannery() {}

    	public SwampTannery(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampTannery buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampTannery(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Lookout
            	{0,0,2, 0,2,4}, 
            	{1,0,2, 1,0,4}, {1,1,2, 1,2,2}, 
            	{2,0,2, 2,2,4}, 
            	// Main house
            	// Front porch
            	{4,0,0, 6,0,1}, 
            	// Floor
            	{4,0,2, 4,0,6}, 
            	{5,0,2, 6,0,2}, 
            	{5,0,6, 6,0,6}, 
            	{7,0,2, 11,0,6}, 
            	// Left wall
            	{4,1,2, 4,3,4}, {4,3,5, 4,3,5}, {4,1,6, 4,3,6}, 
            	// Back wall
            	{5,1,6, 6,3,6}, 
            	// Outside rim
            	{7,1,2, 11,1,2}, 
            	{11,1,3, 11,1,5}, 
            	{7,1,6, 11,1,6}, 
            	// Front wall
            	{5,3,2, 5,3,2}, {6,1,2, 6,3,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Lookout
    			{0,3,4, 2+4}, {1,3,4, 2+4}, {2,3,4, 2+4}, 
    			{0,3,3, 0+4}, {2,3,3, 1+4}, 
    			{0,3,2, 3+4}, {1,3,2, 3+4}, {2,3,2, 3+4}, 
    			// House roof
    			{3,3,6, 0}, {4,4,6, 0}, {6,4,6, 1}, {7,3,6, 1}, 
    			{3,3,5, 0}, {4,4,5, 0}, {6,4,5, 1}, {7,3,5, 1}, 
    			{3,3,4, 0}, {4,4,4, 0}, {6,4,4, 1}, {7,3,4, 1}, 
    			{3,3,3, 0}, {4,4,3, 0}, {6,4,3, 1}, {7,3,3, 1}, 
    			{3,3,2, 0}, {4,4,2, 0}, {6,4,2, 1}, {7,3,2, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Lookout
    			{0,4,4, 2,4,4}, 
    			{0,4,3, 0,4,3}, {2,4,3, 2,4,3}, 
    			{0,4,2, 2,4,2}, 
    			{1,5,2, 1,6,2}, {1,6,1, 1,6,1}, 
    			// Front porch
    			{4,1,0, 6,1,0}, {4,2,0, 4,2,0}, {6,2,0, 6,2,0}, 
    			// Awning
    			{9,2,6, 9,2,6}, {11,2,6, 11,2,6}, 
    			{9,2,2, 9,2,2}, {11,2,2, 11,2,2}, 
    			// Top windows
    			{5,4,2, 5,4,2}, {5,4,6, 5,4,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Front entrance
    			{4,3,0, 6,3,1}, 
    			// Awning
    			{8,3,2, 11,3,6}, 
    			// Roof
    			{5,5,2, 5,5,6}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
            
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.ladder.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{1,1,3, 1,3,3, 0}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode)), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode)), false);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{5,1,2, 2, 1, 0}, 
    			{4,1,5, 3, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
            
        	
            // Cauldron
        	for (int[] uvw : new int[][]{
        		{10,1,5}, 
        		{10,1,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.cauldron.getStateFromMeta(3), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Potted plant
        	for (int[] uvws : new int[][]{ // u,v,w, sapling
        		{11,2,4, 0}, // Oak
           		})
        	{
            	StructureVillageVN.generateStructureFlowerPot(world, structureBB, random, 
            			new BlockPos(this.getXWithOffset(uvws[0], uvws[2]), 
            			this.getYWithOffset(uvws[1]), 
            			this.getZWithOffset(uvws[0], uvws[2])), 
            			Blocks.sapling, uvws[3]);
        	}
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{5,0,5, 2, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{6,0,5, 3, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{5,0,4, 1, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{6,0,4, 0, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{5,0,3, 2, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{6,0,3, 3, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
        		// In house
        		{6,3,4}, 
        		// Hanging
        		{1,5,1}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Left side
        			{0,2,4, 3}, 
        			// Right side
        			{12,1,5, 1}, 
        			// Forward-facing vines
        			{0,2,5, 0}, {0,1,5, 0}, 
        			{5,3,7, 0}, {5,2,7, 0}, {6,3,7, 0}, 
        			{9,1,7, 0}, {10,1,7, 0}, 
        			// Back-facing vines
        			{0,2,1, 2}, 
        			{1,2,1, 2}, {1,1,1, 2}, 
        			{9,1,1, 2}, {10,1,1, 2}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			int s = random.nextInt(16);
    			
    			int u = s==15 ? 1 : 5+(s/3);
    			int v = 1;
    			int w = s==15 ? 3 : 5+(s%3);
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    // --- Temple --- //
    // designed by AstroTibs
    
    public static class SwampTemple extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			" FFFFFFFFFF",
    			" FPPPPPPPFF",
    			" FFFFFFFPFF",
    			" FFFFFFFPFF",
    			" FFFFFFFPFF",
    			" FFFFFFFPFF",
    			" FFFFFFFPFF",
    			" FFFFFFFFFF",
    			" FFFFFFFFFF",
    			" FFFFFFFFFF",
    			" FFFFFFFFFF",
    			" FFPPFF    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 4;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampTemple() {}

    	public SwampTemple(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampTemple buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampTemple(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
    		
    		
    		// Cobblestone
    		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Left wall
    			{1,1,8, 1,1,8}, {1,4,8, 1,4,8}, 
    			{1,1,7, 1,4,7}, 
    			{1,1,6, 1,1,6}, {1,4,6, 1,4,6}, 
    			{1,1,5, 1,4,5}, 
    			{1,1,4, 1,1,4}, {1,4,4, 1,4,4}, 
    			{1,1,3, 1,4,3}, 
    			{1,4,2, 1,4,2}, 
    			// Right wall
    			{6,1,8, 6,1,8}, {6,4,8, 6,4,8}, 
    			{6,1,7, 6,4,7}, 
    			{6,1,6, 6,1,6}, {6,4,6, 6,4,6}, 
    			{6,1,5, 6,3,5}, 
    			{6,1,4, 6,1,4}, {6,4,4, 6,4,4}, 
    			{6,1,3, 6,3,3}, 
    			{6,1,2, 6,1,2}, 
    			// Front wall
    			{1,2,1, 1,5,1}, 
    			{2,4,1, 2,5,1}, 
    			{3,7,1, 4,8,1}, 
    			{5,4,1, 5,6,1}, 
    			{6,2,1, 6,4,1}, 
    			// Back wall
    			{3,1,9, 3,6,9}, 
    			{4,3,9, 4,6,9}, 
    			{5,1,9, 5,5,9}, 
    			{6,1,9, 6,1,9}, 
    			// Floor
    			{2,0,1, 6,0,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
    		}
    		
    		
    		// Mossy Cobblestone
    		IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Left wall
    			{1,1,2, 1,1,2}, 
    			// Right wall
    			{6,4,2, 6,4,3}, 
    			{6,4,5, 6,4,5}, 
    			// Front wall
    			{1,1,1, 1,1,1}, 
    			{2,6,1, 2,6,1}, 
    			{3,4,1, 4,4,1}, 
    			{6,1,1, 6,1,1}, {6,5,1, 6,5,1}, 
    			// Back wall
    			{1,1,9, 1,4,9}, 
    			{2,1,9, 2,5,9}, 
    			{6,2,9, 6,4,9}, 
    			// Floor
    			{1,0,1, 1,0,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);	
    		}
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	{7,0,2, 9,0,4}, 
            	{7,0,5, 7,0,9}, {9,0,5, 9,0,10}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);	
            }
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Stairs
    			{3,9,1, 0}, {4,9,1, 1}, 
    			{5,7,1, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Pews
    			{2,1,3, 2}, {2,1,5, 2}, 
    			{5,1,3, 2}, {5,1,5, 2}, 
    			// Roof
    			{2,6,2, 0}, {2,6,3, 0}, {2,6,4, 0}, {2,6,5, 0}, {2,6,6, 0}, {2,6,7, 0}, {2,6,8, 0}, {2,6,9, 0}, 
    			{3,7,2, 0}, {3,7,3, 0}, {3,7,4, 0}, {3,7,5, 0}, {3,7,6, 0}, {3,7,7, 0}, {3,7,8, 0}, {3,7,9, 0}, 
    			{4,7,2, 1}, {4,7,3, 1}, {4,7,4, 1}, {4,7,5, 1}, {4,7,6, 1}, {4,7,7, 1}, {4,7,8, 1}, {4,7,9, 1}, 
    			{5,6,2, 1}, {5,6,3, 1}, {5,6,4, 1}, {5,6,5, 1}, {5,6,6, 1}, {5,6,7, 1}, {5,6,8, 1}, {5,6,9, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,5,2, 1,5,9}, {6,5,2, 6,5,9}, 
            	{3,1,8, 3,1,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Roof
    			{0,5,2, 0,5,9}, {7,5,2, 7,5,9}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
    		
    		
    		// Mossy Cobblestone stairs
    		Block biomeMossyCobblestoneStairsBlock = ModObjects.chooseModMossyCobblestoneStairsBlock();
    		if (biomeMossyCobblestoneStairsBlock==null) {biomeMossyCobblestoneStairsBlock = Blocks.stone_stairs;}
    		biomeMossyCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Stairs
    			{2,7,1, 0}, 
    			{2,3,0, 3}, {3,3,0, 3}, {4,3,0, 3}, {5,3,0, 3}, 
    			})
    		{
    			this.setBlockState(world, biomeMossyCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Logs (Vertical)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Vertical
    			{2,1,1, 2,2,1}, {5,1,1, 5,2,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
    		}
    		
    		
    		// Logs (Across)
    		IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true);
    		for(int[] uuvvww : new int[][]{
    			{2,3,1, 5,3,1}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
    		}
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{8,1,1, 9,1,1}, 
        		{10,1,4, 10,1,10}, 
        		{2,1,11, 7,1,11}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
        	
        	
        	// Mossy Cobblestone wall
        	IBlockState biomeMossyCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getStateFromMeta(1), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{7,1,1, 7,1,1}, 
        		{10,1,1, 10,1,3}, 
        		{8,1,11, 10,1,11}, 
        		{1,1,10, 1,1,11}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneWallState, biomeMossyCobblestoneWallState, false);
            }
    		
    		
    		// Torches
    		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
    			// Interior
    			{2,4,3, 1}, {2,4,7, 1}, 
    			{5,4,3, 3}, {5,4,7, 3}, 
    			// On Outside wall
    			{10,2,1, -1}, {10,2,11, -1}, {1,2,11, -1}, 
    			}) {
    			this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
            
            
        	// Carpet
        	for(int[] uuvvww : new int[][]{
        		// Lower
        		{3,1,2, 4,1,7, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
        		})
            {
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.carpet.getStateFromMeta(uuvvww[6]), Blocks.carpet.getStateFromMeta(uuvvww[6]), false);	
            }
            
            
            // Stained Glass Windows
            for (int[] uvwc : new int[][]{
            	// Front wall
            	{3,6,1 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{4,5,1 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{3,5,1 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{4,6,1 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	// Left wall
            	{1,3,2 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{1,3,4 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{1,3,6 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{1,3,8 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{1,2,2 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{1,2,4 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{1,2,6 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{1,2,8 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	// Right wall
            	{6,3,2 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{6,3,4 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{6,3,6 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{6,3,8 , GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
            	{6,2,2 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{6,2,4 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{6,2,6 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
            	{6,2,8 , GeneralConfig.useVillageColors ? this.townColor2 : 13}, // Green
        		})
            {
            	this.setBlockState(world, Blocks.stained_glass_pane.getStateFromMeta(uvwc[3]), uvwc[0], uvwc[1], uvwc[2], structureBB);
            }
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{3,1,1, 0, 1, 0}, {4,1,1, 0, 1, 1}, 
    			{4,1,9, 0, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    					
    		// Brewing stand
    		for (int[] uvw : new int[][]{
    			{3,2,8}, 
    			})
    		{
    			this.setBlockState(world, Blocks.brewing_stand.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
            // Leaves
        	IBlockState biomeLeafState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.leaves.getStateFromMeta(4), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{7,1,2, 7,1,9}, 
        		{8,1,2, 8,1,2}, 
        		{9,1,2, 9,1,10}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], 
        				biomeLeafState,
        				biomeLeafState, 
        				false);
            }
            
        	
        	// Random Flower
        	for (int[] uvw : new int[][]{
        		{8,1,4}, 
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
            	else {flowerstate = (flowerindex==9 ? Blocks.yellow_flower:Blocks.red_flower).getStateFromMeta(new int[]{0,1,2,3,4,5,6,7,8,0}[flowerindex%10]);}
        		
        		this.setBlockState(world, flowerstate, uvw[0], uvw[1], uvw[2], structureBB);	
            }
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Left side
        			{0,5,1, 3}, {0,4,1, 3}, {0,3,1, 3}, {0,2,1, 3}, 
        			{0,4,3, 3}, {0,3,3, 3}, {0,2,3, 3}, {0,1,3, 3}, {0,0,3, 3}, 
        			{0,4,9, 3}, 
        			// Right side
        			{7,4,3, 1}, {7,3,3, 1}, 
        			// Away-facing vines
        			{1,4,10, 0}, {1,3,10, 0}, 
        			{1,5,10, 0}, {1,4,10, 0}, {1,3,10, 0}, {1,2,10, 0}, {1,1,10, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			
    			int u = 3+random.nextInt(2);
    			int v = 1;
    			int w = 2+random.nextInt(6);
    			
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
    	protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 2;}
    }
    
    
    // --- Tool Smithy --- //
    // designed by AstroTibs
    
    public static class SwampToolSmithy extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"   F F F F",
    			"          ",
    			"          ",
    			"          ",
    			"FFFF      ",
    			"FFF F   F ",
    			"FFF       ",
    			"FFF       ",
    			"FFFF F F F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 6;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampToolSmithy() {}

    	public SwampToolSmithy(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampToolSmithy buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampToolSmithy(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
            
            
        	// Trees
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.sapling.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwss : new int[][]{ // u,v,w, ushift,wshift: Which adjacent spaces to use if this is a Dark Oak sapling
        		{1,1,1, -1,1},
        		})
            {
        		Block dirtblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();
        		Block saplingblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();
        		
        		// Don't place if there's no dirt beneath to grow
        		if (dirtblock==null) {continue;}
        		// Don't place if the sapling can't see the sky
        		if (!world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2])))) {continue;}
        		// Dark oak version of the above
        		if (biomeSaplingState.getBlock().getMetaFromState(biomeSaplingState)==5)
        		{
        			Block dirtblock1 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]))).getBlock();
        			Block dirtblock2 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]+uvwss[4]))).getBlock();
        			Block dirtblock3 = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]))).getBlock();
        			
        			if (
        					// Foundation blocks can't see the sky
        					   !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2])))
        					|| !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2]+uvwss[4])))
        					|| !world.canBlockSeeSky(new BlockPos(this.getXWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0]+uvwss[3], uvwss[2]+uvwss[4])))
        					)
        			{
        				continue;
        			}
        			else
        			{
        				// Otherwise, plant grass to allow the Dark Oak sapling
        				this.setBlockState(world, Blocks.grass.getDefaultState(), uvwss[0]+uvwss[3], uvwss[1]-1, uvwss[2], structureBB);
        				this.setBlockState(world, Blocks.grass.getDefaultState(), uvwss[0], uvwss[1]-1, uvwss[2]+uvwss[4], structureBB);
        				this.setBlockState(world, Blocks.grass.getDefaultState(), uvwss[0]+uvwss[3], uvwss[1]-1, uvwss[2]+uvwss[4], structureBB);
        			}
        		}
        		
        		this.setBlockState(world, Blocks.grass.getDefaultState(), uvwss[0], uvwss[1]-1, uvwss[2], structureBB);
        		
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

    		        	
        	// Clear out leaves to allow player to access the walkway
            for(int[] uuvvww : new int[][]{
    			{3,2,4, 3,3,4}, 
    			{3,2,3, 3,3,3}, 
    			{3,3,1, 4,4,2}, 
    			})
    		{
            	this.fillWithAir(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5]);	
    		}
        	
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Entry
            	{0,0,0, 0,0,4}, {1,0,0, 1,0,0}, {1,0,2, 1,0,4}, {2,0,0, 2,0,4}, {3,0,4, 3,0,4}, 
            	// Left wall
            	{3,4,4, 3,5,8}, {3,5,3, 3,5,3}, 
            	// Right wall
            	{9,4,3, 9,5,8}, 
            	// Front wall
            	{4,4,3, 4,6,3}, {5,4,3, 5,7,3}, {6,6,3, 6,8,3}, {7,4,3, 7,7,3}, {8,4,3, 8,6,3}, 
            	// Back wall
            	{4,4,8, 4,6,8}, {5,6,8, 5,7,8}, {6,7,8, 6,8,8}, {7,6,8, 7,7,8}, {8,4,8, 8,6,8}, {5,4,8, 7,4,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            
        	// Stripped Log (Along)
    		IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false);
        	IBlockState biomeStrippedLogHorizAlongState = biomeLogHorAlongState;
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogHorizAlongState.getBlock()==Blocks.log || biomeStrippedLogHorizAlongState.getBlock()==Blocks.log2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.log)
            	{
            		biomeStrippedLogHorizAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2==0? 1:0));
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.log2)
            	{
            		biomeStrippedLogHorizAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(this.coordBaseMode.getHorizontalIndex()%2==0? 1:0));
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	{4,2,3, 4,2,7}, {8,2,3, 8,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), true);
        	IBlockState biomeStrippedLogHorizAcrossState = biomeLogHorAcrossState;
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log || biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.log)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.log2)
            	{
            		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(this.coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	{3,2,0, 9,2,0}, {5,2,3, 7,2,3}, {3,2,8, 9,2,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
    		
    		
    		// Wood stairs
    		IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{5,3,2, 0}, {6,3,2, 3}, {7,3,2, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Wooden slabs (Bottom)
    		IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{3,1,4, 3,1,4}, {3,2,2, 3,2,2}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
    		}
    		
    		
    		// Wooden slabs (Top)
    		IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			// Entryway
    			{3,1,3, 3,1,3}, 
    			{3,2,1, 9,2,1}, {4,2,2, 9,2,2}, 
    			{3,4,3, 3,4,3}, 
    			// Under floor
    			{3,2,5, 9,2,7}, 
    			{5,2,4, 7,2,7}, 
    			{9,2,3, 9,2,7}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
    		}
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Support posts
    			{3,1,8, 3,1,8}, {5,1,8, 5,1,8}, {7,1,8, 7,1,8}, {9,1,8, 9,1,8}, 
    			{4,1,3, 4,1,3}, {8,1,3, 8,1,3}, 
    			{3,1,0, 3,1,0}, {5,1,0, 5,1,0}, {7,1,0, 7,1,0}, {9,1,0, 9,1,0}, 
    			{3,3,0, 3,5,0}, {5,3,0, 5,7,0}, {7,3,0, 7,7,0}, {9,3,0, 9,5,0}, 
    			// Front hatching
    			{4,5,0, 4,6,0}, {6,5,0, 6,8,0}, {8,5,0, 8,6,0}, 
    			{6,7,1, 6,7,2}, 
    			// Windows
    			{6,6,8, 6,6,8}, {5,5,8, 7,5,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
    		
    		
    		// Concrete
    		IBlockState tryConcrete = ModObjects.chooseModConcreteState(GeneralConfig.useVillageColors ? townColor : 15); // Black
        	Block concreteBlock = Blocks.stained_hardened_clay; int concreteMeta = GeneralConfig.useVillageColors ? townColor : 15; // Black
        	if (tryConcrete != null) {concreteBlock = tryConcrete.getBlock(); concreteMeta = tryConcrete.getBlock().getMetaFromState(tryConcrete);}
    		for(int[] uuvvww : new int[][]{
    			{3,3,5, 3,3,8}, {4,3,3, 9,3,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], concreteBlock.getStateFromMeta(concreteMeta), concreteBlock.getStateFromMeta(concreteMeta), false);	
    		}
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{4,7,8, 0}, {5,8,8, 0}, {7,8,8, 1}, {9,6,8, 1}, 
    			{3,6,7, 0}, {9,6,7, 1}, 
    			{3,6,6, 0}, {5,8,6, 0}, {7,8,6, 1}, {8,7,6, 1}, {9,6,6, 1}, 
    			{4,7,5, 0}, {5,8,5, 0}, {7,8,5, 1}, {8,7,5, 1}, {9,6,5, 1}, 
    			{4,7,4, 0}, {5,8,4, 0}, {7,8,4, 1}, {8,7,4, 1}, {9,6,4, 1}, 
    			{3,6,3, 0}, {4,7,3, 0}, {5,8,3, 0}, {7,8,3, 1}, {8,7,3, 1}, {9,6,3, 1}, 
    			{3,6,2, 0}, {4,7,2, 0}, {5,8,2, 0}, {8,7,2, 1}, {9,6,2, 1}, 
    			{3,6,1, 0}, {5,8,1, 0}, {9,6,1, 1}, 
    			{5,8,0, 0}, {7,8,0, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Mossy Cobblestone stairs
    		Block biomeMossyCobblestoneStairsBlock = ModObjects.chooseModMossyCobblestoneStairsBlock();
    		if (biomeMossyCobblestoneStairsBlock==null) {biomeMossyCobblestoneStairsBlock = Blocks.stone_stairs;}
    		biomeMossyCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneStairsBlock.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{3,6,8, 0}, {8,7,8, 1}, 
    			{4,7,7, 0}, {5,8,7, 0}, {7,8,7, 1}, {8,7,7, 1}, 
    			{4,7,6, 0}, 
    			{3,6,5, 0}, 
    			{3,6,4, 0}, 
    			{7,8,2, 1}, 
    			{4,7,1, 0}, {7,8,1, 1}, {8,7,1, 1}, 
    			{3,6,0, 0}, {4,7,0, 0}, {8,7,0, 1}, {9,6,0, 1}, 
    			})
    		{
    			this.setBlockState(world, biomeMossyCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{6,9,1, 6,9,3}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
    		
    		
    		// Mossy Cobblestone Slab (lower)
    		IBlockState biomeMossyCobblestoneSlabLowerState = ModObjects.chooseModMossyCobblestoneSlabState(false);
    		if (biomeMossyCobblestoneSlabLowerState != null)
    		{
    			biomeMossyCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(biomeMossyCobblestoneSlabLowerState, this.materialType, this.biome, this.disallowModSubs);
    		}
    		else
    		{
    			biomeMossyCobblestoneSlabLowerState = biomeCobblestoneSlabLowerState;
    		}
    		for(int[] uuvvww : new int[][]{
    			{6,9,0, 6,9,0}, {6,9,4, 6,9,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneSlabLowerState, biomeMossyCobblestoneSlabLowerState, false);	
    		}
            
    		
    		// Doors
    		IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
    			// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
    			{6,4,3, 2, 1, 1}, 
    		})
    		{
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
    		}
    		
    		
    		// Smithing table
    		IBlockState smithingTableState = ModObjects.chooseModSmithingTable();
    		for (int[] uvw : new int[][]{
    			{4,4,7}, 
    			})
    		{
    			this.setBlockState(world, smithingTableState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{4,6,4, 4,6,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	// Exterior
        		{6,6,1}, 
        		// Interior
            	{4,6,7}, {8,6,5}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Polished Andesite table
        	
        	// First try polished Andesite
        	IBlockState polishedAndesiteSlabUpperState = ModObjects.chooseModPolishedAndesiteSlabState(true);
        	Block polishedAndesiteStairsBlock = ModObjects.chooseModPolishedAndesiteStairsBlock();
        	if (polishedAndesiteSlabUpperState==null || polishedAndesiteStairsBlock==null)
        	{
        		// Try regular Andesite
        		polishedAndesiteSlabUpperState = ModObjects.chooseModAndesiteSlabState(true);
        		polishedAndesiteStairsBlock = ModObjects.chooseModAndesiteStairsBlock();
        		if (polishedAndesiteSlabUpperState==null || polishedAndesiteStairsBlock==null) // Just use stone brick
            	{
        			polishedAndesiteSlabUpperState=Blocks.stone_slab.getStateFromMeta(5+8);
                	polishedAndesiteStairsBlock=Blocks.stone_brick_stairs;
            	}
        	}
        	
    		// Polished Andesite Slab (Upper)
    		for (int[] uvw : new int[][]{
    			{4,4,5}, 
    			})
    		{
    			this.setBlockState(world, polishedAndesiteSlabUpperState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
        	
    		// Polished Andesite Stairs
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			{4,4,4, 2+4}, {4,4,6, 3+4}, 
    			})
    		{
    			this.setBlockState(world, polishedAndesiteStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
    		}
    		
    		
    		// Polished Andesite
    		IBlockState biomePolishedAndesiteState = Blocks.stone.getStateFromMeta(6);
    		biomePolishedAndesiteState = StructureVillageVN.getBiomeSpecificBlockState(biomePolishedAndesiteState, this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uvw : new int[][]{
    			// Counter
    			{7,4,5}, {8,4,5}, 
    			})
    		{
    			this.setBlockState(world, biomePolishedAndesiteState, uvw[0], uvw[1], uvw[2], structureBB);
    		}
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 8;
        	int chestV = 4;
        	int chestW = 7;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_toolsmith");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	{1,0,1, 1,0,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);	
            }
        	
        	
        	// Vines
        	if (this.villageType==FunctionsVN.VillageType.JUNGLE || this.villageType==FunctionsVN.VillageType.SWAMP)
        	{
        		for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1: rightward, 2:backward, 3: leftward
        			// Left side
        			{2,5,5, 3}, {2,4,5, 3}, {2,3,5, 3}, {2,2,5, 3}, 
        			{2,5,8, 3}, {2,4,8, 3}, {2,3,8, 3}, {2,2,8, 3}, {2,1,8, 3}, 
        			// Away-facing vines
        			{3,5,9, 0}, {3,4,9, 0}, 
        			{8,6,9, 0}, {8,5,9, 0}, {8,4,9, 0}, 
            		})
                {
        			// Replace only when air to prevent overwriting stuff outside the bb
        			if (world.isAirBlock(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2]))))
        			{
        				this.setBlockState(world, Blocks.vine.getStateFromMeta(StructureVillageVN.chooseVineMeta(uvwo[3], this.coordBaseMode)), uvwo[0], uvwo[1], uvwo[2], structureBB);
        			}
                }
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(13); 
    			
    			int u = s <=3 ? s+5 : s <=5 ? s+1 : s <=9 ? s-1 : s-5;
    			int v = 4;
    			int w = s <=3 ? 4 : s <=5 ? 5 : s <=9 ? 6 : 7;
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    // --- Weapon Smithy --- //
    // designed by AstroTibs
    
    public static class SwampWeaponSmithy extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			" F    F ",
    			"  FFFF  ",
    			"  FFFF  ",
    			" FFFFFF ",
    			"FFFFFFFF",
    			" FFFFFF ",
    			"  FFFF  ",
    			"   PP   ",
    			"  FPPF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampWeaponSmithy() {}

    	public SwampWeaponSmithy(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampWeaponSmithy buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampWeaponSmithy(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{0,2,4, 0,2,4}, 
        		{1,2,8, 1,2,8}, 
        		{7,2,4, 7,2,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);
            }
        	
        	
        	// Mossy Cobblestone wall
        	IBlockState biomeMossyCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getStateFromMeta(1), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{6,2,8, 6,2,8}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneWallState, biomeMossyCobblestoneWallState, false);
            }
            
            
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{0,3,4, -1}, 
        		{1,3,8, -1}, {6,3,8, -1}, 
        		{7,3,4, -1}, 
        	})
        	{
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
        	}
        	
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{0,1,4, 0,1,4}, 
            	{1,1,3, 1,4,3}, {1,1,4, 1,3,4}, {1,1,5, 1,4,5}, 
            	{2,1,2, 2,4,2}, {2,5,4, 2,5,5}, {2,4,5, 2,4,5}, {2,1,6, 2,7,6}, {2,1,7, 2,3,7}, 
            	{3,1,7, 4,7,7}, {3,5,5, 4,7,5}, 
            	{5,1,2, 5,4,2}, {5,5,4, 5,5,5}, {5,4,5, 5,4,5}, {5,1,6, 5,7,6}, {5,1,7, 5,3,7}, 
            	{6,1,3, 6,4,3}, {6,1,4, 6,3,4}, {6,1,5, 6,4,5}, 
            	{7,1,4, 7,1,4}, 
            	// Upper entry lip
            	{3,4,2, 4,4,2}, 
            	// Lip in front of basin
            	{2,1,5, 5,1,5}, 
            	// Bottom of basin
            	{2,0,5, 5,0,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);
            }
        	
        	
        	// Mossy Cobblestone
        	IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
            	{1,1,8, 1,1,8}, {6,1,8, 6,1,8}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeMossyCobblestoneState, biomeMossyCobblestoneState, false);
            }
    		
    		
    		// Cobblestone stairs
    		Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
    		for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
    			// Roof
    			{1,5,5, 0}, {1,5,4, 0}, {1,5,3, 3}, 
    			{2,5,3, 0}, 
    			{2,5,2, 3}, {3,5,2, 3}, {4,5,2, 3}, {5,5,2, 3}, 
    			{5,5,3, 1}, 
    			{6,5,5, 1}, {6,5,4, 1}, {6,5,3, 3}, 
    			// Ceiling
    			{3,4,5, 3+4}, {4,4,5, 3+4}, 
    			})
    		{
    			this.setBlockState(world, biomeCobblestoneStairsBlock.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
    		}
    		
    		
    		// Cobblestone Slab (lower)
    		IBlockState biomeCobblestoneSlabLowerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
    		for(int[] uuvvww : new int[][]{
    			{1,4,4, 1,4,4}, 
    			{6,4,4, 6,4,4}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneSlabLowerState, biomeCobblestoneSlabLowerState, false);	
    		}
        	
        	
            // Glazed terracotta
        	IBlockState tryGlazedTerracottaState;
        	for (int[] uvwoc : new int[][]{ // u,v,w, orientation, dye color
        		// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,5,3, 2, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{3,5,4, 3, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{4,5,3, 1, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
        		{4,5,4, 0, GeneralConfig.useVillageColors ? this.townColor : 15}, // Black
           		})
        	{
        		tryGlazedTerracottaState = ModObjects.chooseModGlazedTerracottaState(uvwoc[4], (uvwoc[3] + this.coordBaseMode.getHorizontalIndex() + (this.coordBaseMode.getHorizontalIndex() < 2 ? 1 : 0))%4);
        		if (tryGlazedTerracottaState != null)
            	{
        			this.setBlockState(world, tryGlazedTerracottaState, uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
            	}
        		else
        		{
        			this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(uvwoc[4]), uvwoc[0], uvwoc[1], uvwoc[2], structureBB);
        		}
            }
    		
    		
            // Gravel
        	IBlockState biomeGravelState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.gravel.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
    			{2,1,3, 5,1,4}, 
    			{3,1,2, 4,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGravelState, biomeGravelState, false);	
            }
    		
    		
    		// Fences
    		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    		for (int[] uuvvww : new int[][]{
    			// Support posts
    			{2,2,0, 2,3,0}, {5,2,0, 5,3,0}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
    		}
			
			
			// Wool
			for (int[] uvwo : new int[][]{
				{2,4,0, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
				{3,4,0, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
				{4,4,0, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
				{5,4,0, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
				{2,4,1, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
				{3,4,1, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
				{4,4,1, (GeneralConfig.useVillageColors ? this.townColor : 15)}, // Black
				{5,4,1, (GeneralConfig.useVillageColors ? this.townColor3 : 10)}, // Purple
			})
			{
				this.setBlockState(world, Blocks.wool.getStateFromMeta(uvwo[3]), uvwo[0], uvwo[1], uvwo[2], structureBB);
			}
            
            
            // Grindstone
        	for (int[] uvwoh : new int[][]{ // u,v,w, orientation, isHanging
        		// Orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		{2,2,5, 1, 1}, 
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		IBlockState biomeGrindstoneState = ModObjects.chooseModGrindstone(uvwoh[3], this.coordBaseMode, uvwoh[4]==1);
            	
        		this.setBlockState(world, biomeGrindstoneState, uvwoh[0], uvwoh[1], uvwoh[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,3,5, 2}, 
            	{5,2,5, 2}, 
            	})
            {
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.furnace.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
        	
        	
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	{3,7,6, 4,7,6}, 
            	{3,2,6, 4,2,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            }
    		
    		
    		// Lava
    		for(int[] uvw : new int[][]{
    			// Floor
    			{3,1,6}, {4,1,6}, 
    			})
    		{
                this.setBlockState(world,  Blocks.lava.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 2;
        	int chestW = 3;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_weaponsmith");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int s = random.nextInt(9); 
    			
    			int u = s <=0 ? 2 : s <=3 ? 3 : s <=6 ? 4 : 5;
    			int v = 2;
    			int w = s <=0 ? 4 : s <=6 ? 3+((s-1)%3) : s-4;
    			
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    // --- Wild Farm --- //
    // designed by THASSELHOFF
    
    public static class SwampWildFarm extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
    			"      FF       ",
    			"     FFFFFF    ",
    			"   FFFFFFFFF   ",
    			"  FFFFFFFFFFFF ",
    			" FFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFFF",
    			"FFFFFFFFFFFFFFF",
    			" FFFFFFFFFFFFF ",
    			"  FFFFFFFFFFFF ",
    			"  FFFFFFFFFFFF ",
    			"   FFFFFFFFFF  ",
    			"   FFFFFFFFFF  ",
    			"    FFFFFFFFF  ",
    			"     FFFFF     ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	public static final byte MEDIAN_BORDERS = 1+2+4+8; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
    	public SwampWildFarm() {}

    	public SwampWildFarm(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
    	{
    		super();
    		this.coordBaseMode = coordBaseMode;
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
    	
    	public static SwampWildFarm buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
    	{
    		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
    		
    		return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null) ? new SwampWildFarm(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
    	}
    	
    	
    	@Override
    	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
    	{
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	{0,1,7, 0,1,8}, 
            	{1,1,6, 1,1,6}, {1,1,9, 1,1,9}, 
            	{2,1,4, 2,1,4}, {2,1,10, 2,1,10}, 
            	{3,1,2, 3,1,3}, {3,1,10, 3,1,11}, 
            	{4,1,1, 4,1,2}, 
            	{5,1,0, 5,1,1}, {5,1,12, 5,1,12}, 
            	{6,1,0, 6,1,0}, {6,1,13, 7,1,13}, 
            	{8,1,12, 10,1,12}, 
            	{10,1,11, 11,1,11}, 
            	{12,1,10, 13,1,10}, 
            	{13,1,9, 14,1,9}, {13,1,4, 13,1,6}, 
            	{14,1,7, 14,1,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTopState, biomeTopState, false);	
            }
    		
            
    		// Water
    		for(int[] uuvvww : new int[][]{
    			{1,1,7, 2,1,8}, 
    			{2,1,9, 2,1,9}, 
    			{3,1,8, 4,1,9}, {4,1,10, 4,1,10}, 
    			{5,1,9, 6,1,11}, {6,1,12, 6,1,12}, {7,1,9, 7,1,9}, {7,1,11, 7,1,12}, 
    			{3,1,4, 3,1,4}, {4,1,3, 4,1,4}, {5,1,2, 5,1,5}, {6,1,1, 6,1,6}, 
    			{7,1,5, 7,1,5}, 
    			{7,1,1, 8,1,2}, {8,1,4, 8,1,5}, {8,1,8, 8,1,8}, 
    			{9,1,2, 9,1,8}, 
    			{10,1,2, 10,1,3}, {10,1,5, 10,1,6}, 
    			{11,1,2, 11,1,5}, 
    			{12,1,5, 12,1,5}, 
    			{10,1,10, 10,1,10}, {11,1,8, 11,1,10}, {12,1,7, 12,1,9}, {13,1,7, 13,1,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);	
    		}
    		
            
            // Moist Farmland with crop above
            Block[] cropPair1 = StructureVillageVN.chooseCropPair(random);
        	Block[] cropPair2 = StructureVillageVN.chooseCropPair(random);
        	for(int[] uvwfcp : new int[][]{
            	// u,v,w, farmland moisture (0:dry, 7:moist), crop, crop progress
            	// Large left-hand blob
            	{2,1,5, 7,0,random.nextInt(6)}, {2,1,6, 7,0,random.nextInt(6)}, 
            	{3,1,5, 7,0,random.nextInt(6)}, {3,1,6, 7,0,random.nextInt(6)}, {3,1,7, 7,0,random.nextInt(6)}, 
            	{4,1,5, 7,0,random.nextInt(6)}, {4,1,6, 7,0,random.nextInt(6)}, {4,1,7, 7,0,random.nextInt(6)}, 
            	{5,1,6, 7,0,random.nextInt(6)}, {5,1,7, 7,0,random.nextInt(6)}, {5,1,8, 7,0,random.nextInt(6)}, 
            	{6,1,7, 7,0,random.nextInt(6)}, {6,1,8, 7,0,random.nextInt(6)}, 
            	{7,1,6, 7,0,random.nextInt(6)}, {7,1,7, 7,0,random.nextInt(6)}, {7,1,8, 7,0,random.nextInt(6)}, 
            	{8,1,6, 7,0,random.nextInt(6)}, {8,1,7, 7,0,random.nextInt(6)}, 
            	// Small front-left blob
            	{7,1,3, 7,1,random.nextInt(6)}, {7,1,4, 7,1,random.nextInt(6)}, 
            	{8,1,3, 7,1,random.nextInt(6)}, 
            	// Single patch in the back left
            	{4,1,11, 7,1,random.nextInt(6)}, 
            	// Back-right strip
            	{7,1,10, 7,2,random.nextInt(6)}, 
            	{8,1,9, 7,2,random.nextInt(6)}, {8,1,10, 7,2,random.nextInt(6)}, {8,1,11, 7,2,random.nextInt(6)}, 
            	{9,1,9, 7,2,random.nextInt(6)}, {9,1,10, 7,2,random.nextInt(6)}, {9,1,11, 7,2,random.nextInt(6)}, 
            	{10,1,7, 7,2,random.nextInt(6)}, {10,1,8, 7,2,random.nextInt(6)}, {10,1,9, 7,2,random.nextInt(6)}, 
            	{11,1,6, 7,2,random.nextInt(6)}, {11,1,7, 7,2,random.nextInt(6)}, 
            	{12,1,6, 7,2,random.nextInt(6)}, 
            	// Front-right strip
            	{7,1,0, 7,3,random.nextInt(6)}, 
            	{8,1,0, 7,3,random.nextInt(6)}, 
            	{9,1,0, 7,3,random.nextInt(6)}, {9,1,1, 7,3,random.nextInt(6)}, 
            	{10,1,1, 7,3,random.nextInt(6)}, 
            	{11,1,1, 7,3,random.nextInt(6)}, 
            	{12,1,1, 7,3,random.nextInt(6)}, {12,1,2, 7,3,random.nextInt(6)}, {12,1,3, 7,3,random.nextInt(6)}, {12,1,4, 7,3,random.nextInt(6)}, 
            	})
            {
            	this.setBlockState(world, Blocks.farmland.getStateFromMeta(uvwfcp[3]), uvwfcp[0], uvwfcp[1], uvwfcp[2], structureBB);
            	
            	int cropProgressMeta = uvwfcp[5]; // Isolate the crop's age meta value
    			IBlockState cropState;
    			
    			while(true)
    			{
    				try {cropState = ((uvwfcp[4]/2==0?cropPair1:cropPair2)[uvwfcp[4]%2]).getStateFromMeta(cropProgressMeta);}
    				catch (IllegalArgumentException e)
    				{
    					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
    					if (cropProgressMeta==0) {cropState = Blocks.wheat.getStateFromMeta(uvwfcp[5]);}
    					// The crop is not allowed to have this value. Cut it in half and try again.
    					else {cropProgressMeta /= 2; continue;}
    				}
    				
    				// Finally, assign the working crop
					this.setBlockState(world, cropState, uvwfcp[0], uvwfcp[1]+1, uvwfcp[2], structureBB);
    				break;
    			}
            }
    		
    		
    		// Lilypad
    		for (int[] uvw : new int[][]{
    			{2,2,9}, 
    			{3,2,8}, 
    			{5,2,3}, 
    			{6,2,2}, {6,2,5}, {6,2,11}, 
    			{9,2,3}, {9,2,7}, 
    			{11,2,4}, 
    			{12,2,9}, 
    			{13,2,7}, 
    			}) {
    			this.setBlockState(world, Blocks.waterlily.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
    		}
    		
    		
            // Gravel
        	IBlockState biomeGravelState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.gravel.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{7,0,0, 8,0,0}, 
            	{6,0,3, 6,0,4}, {7,0,2, 7,0,3}, 
            	{10,0,2, 10,0,3}, {11,0,2, 11,0,2}, 
            	{0,0,8, 2,0,8}, {1,0,9, 3,0,9}, 
            	{8,0,7, 9,0,8}, {10,0,8, 10,0,8}, 
            	{7,0,11, 8,0,11}, {7,0,12, 7,0,12}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGravelState, biomeGravelState, false);	
            }
            
            
            // Clay
        	IBlockState biomeClayState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.clay.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{5,0,1, 6,0,1}, {6,0,2, 7,0,2}, {7,0,3, 7,0,3}, 
            	{2,0,4, 2,0,4}, {3,0,3, 4,0,4}, {5,0,4, 5,0,5}, 
            	{9,0,6, 10,0,6}, {10,0,7, 11,0,7}, {11,0,8, 11,0,8}, {11,0,9, 13,0,9}, 
            	{4,0,9, 4,0,9}, {3,0,10, 6,0,10}, {3,0,11, 4,0,11}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeClayState, biomeClayState, false);	
            }
    		
    		
    		// Attempt to add GardenCore Compost Bins. If this fails, add farmland with a crop instead
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            for(int[] uvw : new int[][]{
    			{10,2,4}, 
    			})
    		{
            	if (compostBinState != null)
            	{
        			this.setBlockState(world, compostBinState, uvw[0], uvw[1], uvw[2], structureBB);	
        			this.setBlockState(world, biomeFillerState, uvw[0], uvw[1]-1, uvw[2], structureBB);	
            	}
            	else
            	{
                	this.setBlockState(world, Blocks.farmland.getStateFromMeta(7), uvw[0], uvw[1]-1, uvw[2], structureBB);
                	
                	int cropProgressMeta = random.nextInt(6); // Isolate the crop's age meta value
        			IBlockState cropState;
        			
        			while(true)
        			{
        				try {cropState = cropPair1[1].getStateFromMeta(cropProgressMeta);}
        				catch (IllegalArgumentException e)
        				{
        					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
        					if (cropProgressMeta==0) {cropState = Blocks.wheat.getStateFromMeta(cropProgressMeta);}
        					// The crop is not allowed to have this value. Cut it in half and try again.
        					else {cropProgressMeta /= 2; continue;}
        				}
        				
        				// Finally, assign the working crop
    					this.setBlockState(world, cropState, uvw[0], uvw[1], uvw[2], structureBB);
        				break;
        			}
            	}
    		}
            
            
            // Sugarcane
    		for(int[] uuvvww : new int[][]{
    			// Back
    			{5,2,12, 5,3,12}, 
    			{6,2,13, 6,4,13}, 
    			{7,2,13, 7,2,13}, 
    			{8,2,12, 8,3,12}, 
    			{10,2,11, 10,2,11}, 
    			{14,2,8, 14,2,8}, 
    			})
    		{
    			this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.reeds.getDefaultState(), Blocks.reeds.getDefaultState(), false);	
    		}
    		
    		
    		// Unkempt Grass
    		for (int[] uvwg : new int[][]{ // g is grass type
    			{0,2,7, 2}, 
    			{1,2,6, 2}, 
    			{2,2,10, 0}, 
    			{3,2,10, 0}, 
    			{4,2,2, 0}, 
    			{5,2,1, 1}, 
    			{12,2,10, 0}, 
    			{13,2,5, 1}, {13,2,9, 0}, {13,2,10, 1}, 
    		})
    		{
    			if (uvwg[3]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==1) // Tall grass
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    			else if (uvwg[3]==2) // Fern
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(2), uvwg[0], uvwg[1], uvwg[2], structureBB);
    			}
    			else // Large Fern
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(3), uvwg[0], uvwg[1], uvwg[2], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uvwg[0], uvwg[1]+1, uvwg[2], structureBB);
    			}
    		}
    		
    		
    		// Villagers
    		if (!this.entitiesGenerated)
    		{
    			this.entitiesGenerated=true;
    			
    			// Villager
    			int[][] farmerPositions = new int[][]{
                	// On farmland
                	// Large left-hand blob
                	{2,1,5}, 
                	{3,1,5}, 
                	{4,1,5}, 
                	{5,1,6}, 
                	{6,1,7}, 
                	{7,1,6}, 
                	{8,1,6}, 
                	// Small front-left blob
                	{7,1,3}, 
                	{8,1,3}, 
                	// Single patch in the back left
                	{4,1,11}, 
                	// Back-right strip
                	{7,1,10}, 
                	{8,1,9}, 
                	{9,1,9}, 
                	{10,1,7}, 
                	{11,1,6}, 
                	{12,1,6}, 
                	// Front-right strip
                	{7,1,0}, 
                	{8,1,0}, 
                	{9,1,0}, 
                	{10,1,1}, 
                	{11,1,1}, 
                	{12,1,1}, 
                	// On grass
                	{0,1,7}, {0,1,8}, 
                	{1,1,6}, {1,1,9}, 
                	{2,1,4}, {2,1,10}, 
                	{3,1,2}, {3,1,3}, {3,1,10}, {3,1,11}, 
                	{4,1,1}, {4,1,2}, 
                	{5,1,0}, {5,1,1}, {5,1,12}, 
                	{6,1,0}, {6,1,13}, {7,1,13}, 
                	{8,1,12}, {10,1,12}, 
                	{10,1,11}, {11,1,11}, 
                	{12,1,10}, {13,1,10}, 
                	{13,1,9}, {14,1,9}, {13,1,4}, {13,1,6}, 
                	{14,1,7}, {14,1,8}, 
                	};
    			int[] farmerPosition = farmerPositions[random.nextInt(farmerPositions.length)];
    			
    			int u = farmerPosition[0];
    			int v = farmerPosition[1]+1;
    			int w = farmerPosition[2];

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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // ------------------ //
    // --- Road Decor --- //
    // ------------------ //

    
    // --- Road Decor --- //
    
    public static class SwampStreetDecor extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = 3;
    	public static final int STRUCTURE_DEPTH = 3;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SwampStreetDecor() {}

        public SwampStreetDecor(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
            super();
            this.coordBaseMode = coordBaseMode;
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

        public static SwampStreetDecor buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SwampStreetDecor(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
        		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
        						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
        				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
        		
                if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
            
        	WorldChunkManager chunkManager= world.getWorldChunkManager();
        	int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
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
            			new    int[]{-1,0,1}, // Values
            			new double[]{ 1,9,5}, // Weights
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
	            		if (world.getBlockState(pos).getBlock()!=Blocks.air)
	            		{
	            			if (++nonairSurrounding >=4) {decorHeightY++; break;}
	            		}
	            	}
            	}
            	
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvw[0], decorHeightY-1, uvw[2], structureBB);
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
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
            	
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
            			).getBlock().isNormalCube()
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    // --- Straddling Lantern --- //
    // designed by AstroTibs
    
    public static class SwampRoadAccent extends StructureVillagePieces.Village
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
    	public BiomeGenBase biome=null;
    	
    	// Make foundation with blanks as empty air and F as foundation spaces
    	private static final String[] foundationPattern = new String[]{
				" F ",
				"PPP",
				"PPP",
				"PPP",
				" F ",
    	};
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int W_OFFSET = -4; // How much to shift the well to ensure it is positioned onto the road
    	public static final byte MEDIAN_BORDERS = 1 + 4; // Sides of the bounding box to count toward ground level median. +1: front; +2: left; +4: back; +8: right;
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SwampRoadAccent() {}

        public SwampRoadAccent(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
        {
    		super();
    		this.coordBaseMode = coordBaseMode;
    		this.boundingBox = boundingBox;
    		
    		// Offset the bounding box to position it onto the street
			this.boundingBox.offset(
					this.coordBaseMode==EnumFacing.WEST ? -W_OFFSET : this.coordBaseMode==EnumFacing.EAST ? W_OFFSET : 0,
					0,
					this.coordBaseMode==EnumFacing.SOUTH ? W_OFFSET : this.coordBaseMode==EnumFacing.NORTH ? -W_OFFSET: 0);
    		
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
        
        public static SwampRoadAccent buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
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
            		? new SwampRoadAccent(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
			if (this.averageGroundLevel < 0)
			{
				this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
						// Set the bounding box version as this bounding box but with Y going from 0 to 512
						new StructureBoundingBox(
								this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
								this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
						true, MEDIAN_BORDERS, this.coordBaseMode.getHorizontalIndex());
				
				if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void
				
				this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
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
    		
    		WorldChunkManager chunkManager= world.getWorldChunkManager();
    		int bbCenterX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int bbCenterZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
    		BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(bbCenterX, 64, bbCenterZ));
    		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
    		if (this.villageType==null)
    		{
    			try {
    				String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
    				}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (this.materialType==null)
    		{
    			try {
    				String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    				else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
    				}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, bbCenterX, bbCenterZ);}
    		}
    		
    		if (!this.disallowModSubs)
    		{
    			try {
    				String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
    				if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
    				else {this.disallowModSubs = false;}
    				}
    			catch (Exception e) {this.disallowModSubs = false;}
    		}
    		// Reestablish biome if start was null or something
    		if (this.biome==null) {this.biome = world.getBiomeGenForCoords(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
    		
    		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		// Establish top and filler blocks, substituting Grass and Dirt if they're null
    		IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
    		IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
    		
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
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    			else if (unitLetter.equals("P"))
    			{
    				// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().isNormalCube()?-1:0), w, structureBB);
    				StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
    			}
    			else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
    			{
    				// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
    				this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
    				this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
    			}
    		}}
            
        	
        	// Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getDefaultState(), materialType, biome, disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,1,4, 1,3,4}, 
        		{1,4,2, 1,4,2}, 
        		{1,1,0, 1,3,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Hanging Lanterns
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
        	for (int[] uvw : new int[][]{
            	{1,3,2}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,4,3, 1,4,3}, {1,4,1, 1,4,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,4,4, 1,4,4}, {1,4,0, 1,4,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    
	
    // ------------------- //
    // --- Biome Decor --- //
    // ------------------- //
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
	public static ArrayList<BlueprintData> getRandomSwampDecorBlueprint(MaterialType materialType, boolean disallowModSubs, BiomeGenBase biome, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 7;
		return getSwampDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getSwampDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, BiomeGenBase biome, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		// Generate per-material blocks
		
    	// Establish top and filler blocks, substituting Grass and Dirt if they're null
		IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), materialType, biome, disallowModSubs);
		IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), materialType, biome, disallowModSubs);
		IBlockState biomeTopState=biomeGrassState; if (biome!=null && biome.topBlock!=null) {biomeTopState=biome.topBlock;}
		IBlockState biomeFillerState=biomeDirtState; if (biome!=null && biome.fillerBlock!=null) {biomeFillerState=biome.fillerBlock;}
		
    	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
    	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
    	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), materialType, biome, disallowModSubs);
    	
    	
    	// Stripped Log (Vertical)
    	IBlockState biomeStrippedLogVertState = biomeLogVertState;
    	// Try to see if stripped logs exist
    	if (biomeStrippedLogVertState.getBlock()==Blocks.log || biomeStrippedLogVertState.getBlock()==Blocks.log2)
    	{
        	if (biomeLogVertState.getBlock() == Blocks.log)
        	{
        		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
        	}
        	else if (biomeLogVertState.getBlock() == Blocks.log2)
        	{
        		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
        	}
    	}
        // Stripped Log (Across)
    	IBlockState biomeStrippedLogHorizAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), true);
    	// Try to see if stripped logs exist
    	if (biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log || biomeStrippedLogHorizAcrossState.getBlock()==Blocks.log2)
    	{
        	if (biomeLogVertState.getBlock() == Blocks.log)
        	{
        		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
        	}
        	else if (biomeLogVertState.getBlock() == Blocks.log2)
        	{
        		biomeStrippedLogHorizAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(coordBaseMode.getHorizontalIndex()%2!=0? 1:0));
        	}
    	}
    	// Stripped Log (Along)
    	IBlockState biomeStrippedLogHorizAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, coordBaseMode.getHorizontalIndex(), false);
    	// Try to see if stripped logs exist
    	if (biomeStrippedLogHorizAlongState.getBlock()==Blocks.log || biomeStrippedLogHorizAlongState.getBlock()==Blocks.log2)
    	{
        	if (biomeLogVertState.getBlock() == Blocks.log)
        	{
        		biomeStrippedLogHorizAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(coordBaseMode.getHorizontalIndex()%2==0? 1:0));
        	}
        	else if (biomeLogVertState.getBlock() == Blocks.log2)
        	{
        		biomeStrippedLogHorizAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(coordBaseMode.getHorizontalIndex()%2==0? 1:0));
        	}
    	}
    	
    	
    	
		boolean genericBoolean=false;
    	int genericInt=0;
		int postheight=2;
    	int lanternheight;
		
        switch (decorType)
        {
    	case 0: // Tall lantern post by AstroTibs
    	case 1:
    		postheight+=1;
    	case 2: // Medium lantern post by AstroTibs
    	case 3:
    		postheight+=1;
    	case 4: // Short lantern post by AstroTibs
    	case 5:
    		postheight+=1;
    		
    		lanternheight = 2+(random.nextBoolean()?1:0); // Either 2 or 3
    		
    		// Add lantern in front
    		BlueprintData.addFillWithBlocks(blueprint, 0,lanternheight,-1, 0,lanternheight+1,0, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, 0,lanternheight,-1, biomeHangingLanternState);
    		
    		// Add possible second lantern
    		if (random.nextBoolean())
    		{
    			int lantern2height = 2+(random.nextBoolean()?1:0); // Either 2 or 3
    			
    			switch (random.nextInt(5))
    			{
    			// Lantern on right
    			case 0:
    			case 1:
    				BlueprintData.addFillWithBlocks(blueprint, 0,lantern2height,0, 1,lantern2height+1,0, biomeFenceState);
    				BlueprintData.addPlaceBlock(blueprint, 1,lantern2height,0, biomeHangingLanternState);
    				break;
    			// Lantern on left
    			case 2:
    			case 3:
    				BlueprintData.addFillWithBlocks(blueprint, -1,lantern2height,0, 0,lantern2height+1,0, biomeFenceState);
    				BlueprintData.addPlaceBlock(blueprint, -1,lantern2height,0, biomeHangingLanternState);
    				break;
    			// Lantern behind
    			case 4:
    				BlueprintData.addFillWithBlocks(blueprint, 0,lantern2height,0, 0,lantern2height+1,1, biomeFenceState);
    				BlueprintData.addPlaceBlock(blueprint, 0,lantern2height,1, biomeHangingLanternState);
    				break;
    			}
    		}
    		
    		// Post
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Foundation
    		BlueprintData.addFillWithBlocks(blueprint, 0,0,0, 0,postheight-1,0, biomeStrippedLogVertState);
    		break;
    		
    	case 6: // Toppled lantern post by AstroTibs
    		
    		BlueprintData.addFillBelowTo(blueprint, 0, -1, 0, biomeFillerState); // Center foundation
    		
    		switch (random.nextInt(4))
    		{
    		case 0: // Facing you
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeFillerState); // Foundation
    			BlueprintData.addFillWithBlocks(blueprint, 0,0,0, 1,0,0, biomeStrippedLogHorizAcrossState);
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeFenceState); // Fence post
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeTopState); // Foundation for lantern
        		BlueprintData.addPlaceBlock(blueprint, -1, 0, 1, biomeSittingLanternState);
        		break;
    			
    		case 1: // Facing left
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeFillerState); // Foundation
    			BlueprintData.addFillWithBlocks(blueprint, 0,0,-1, 0,0,0, biomeStrippedLogHorizAlongState);
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeFenceState); // Fence post
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 1, biomeTopState); // Foundation for lantern
        		BlueprintData.addPlaceBlock(blueprint, 1, 0, 1, biomeSittingLanternState);
    			break;
    			
    		case 2: // Facing away
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeFillerState); // Foundation
    			BlueprintData.addFillWithBlocks(blueprint, -1,0,0, 0,0,0, biomeStrippedLogHorizAcrossState);
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeFenceState); // Fence post
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, -1, biomeTopState); // Foundation for lantern
        		BlueprintData.addPlaceBlock(blueprint, 1, 0, -1, biomeSittingLanternState);
    			break;
    			
    		case 3: // Facing right
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeFillerState); // Foundation
    			BlueprintData.addFillWithBlocks(blueprint, 0,0,0, 0,0,1, biomeStrippedLogHorizAlongState);
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeFenceState); // Fence post
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, -1, biomeTopState); // Foundation for lantern
        		BlueprintData.addPlaceBlock(blueprint, -1, 0, -1, biomeSittingLanternState);
    			break;
    		}
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
