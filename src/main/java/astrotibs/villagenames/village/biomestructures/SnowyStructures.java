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
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import astrotibs.villagenames.village.chestloot.ChestGenHooks;
import astrotibs.villagenames.village.chestloot.WeightedRandomChestContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SnowyStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Ice Spire --- //
    
    public static class SnowyMeetingPoint1 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"FFPPPFFFFFFF",
            	"PPPPPPPPPPPP",
            	"PPFPPFFFFPPP",
            	"PPPPPFFFFPPP",
            	"FPPPPFFFFPPF",
            	"FFPPPFFFFPPF",
            	"FFFPPPPPPPPF",
            	"FFFFFFFFPPPF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
	    public SnowyMeetingPoint1() {}
		
		public SnowyMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
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
    					+ ReflectionHelper.getPrivateValue(Biome.class, this.biomeProvider.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)), new String[]{"biomeName","field_76791_y"})
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (new int[]{8,1,2,4})[this.getCoordBaseMode().getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (new int[]{4,8,1,2})[this.getCoordBaseMode().getHorizontalIndex()], EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (new int[]{2,1,8,4})[this.getCoordBaseMode().getHorizontalIndex()], this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (new int[]{4,2,1,8})[this.getCoordBaseMode().getHorizontalIndex()], EnumFacing.WEST, this.getComponentType());
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
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLanternState = ModObjects.chooseModLanternBlockState(true);
        	
        	// For stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogState;//null; int biomeStrippedWoodOrLogOrLogVerticMeta = 0;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAlongState = biomeLogState;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAcrossState = biomeLogState;
        	// Try stripped wood
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogState, 0); 
            	biomeStrippedWoodOrLogOrLogHorAlongState = ModObjects.chooseModStrippedWoodState(biomeLogState, 2);
            	biomeStrippedWoodOrLogOrLogHorAcrossState = ModObjects.chooseModStrippedWoodState(biomeLogState, 1);
        	}
        	// If stripped wood does not exist, try to get stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
        		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogState.getBlock().getMetaFromState(biomeLogState), 0);
        		biomeStrippedWoodOrLogOrLogHorAlongState = ModObjects.chooseModStrippedLogState(biomeLogState.getBlock().getMetaFromState(biomeLogState), 2);
        		biomeStrippedWoodOrLogOrLogHorAcrossState = ModObjects.chooseModStrippedLogState(biomeLogState.getBlock().getMetaFromState(biomeLogState), 1);
        	}

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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
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
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(2, 5),
        			this.getYWithOffset(2),
        			this.getZWithOffset(2, 5));
        	
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
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
        	// Set grass
        	this.fillWithBlocks(world, structureBB, 0, 0, 7, 1, 0, 7, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 0, 0, 0, 0, 0, 3, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 1, 0, 0, 1, 0, 2, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 2, 0, 0, 2, 0, 1, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 3, 0, 0, 7, 0, 0, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 5, 0, 7, 11, 0, 7, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 11, 0, 0, 11, 0, 3, biomeGrassState, biomeGrassState, false);
        	// Set dirt
        	this.fillWithBlocks(world, structureBB, 5, 0, 2, 8, 0, 5, biomeDirtState, biomeDirtState, false);
        	this.setBlockState(world, biomeDirtState, 2, 0, 5, structureBB);
        	
        	// Place the "stripped wood" rim around the "art"
        	this.fillWithBlocks(world, structureBB, 5, 1, 2, 7, 1, 2, biomeStrippedWoodOrLogOrLogHorAcrossState, biomeStrippedWoodOrLogOrLogHorAcrossState, false);
        	this.fillWithBlocks(world, structureBB, 5, 1, 3, 5, 1, 5, biomeStrippedWoodOrLogOrLogHorAlongState, biomeStrippedWoodOrLogOrLogHorAlongState, false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 5, 8, 1, 5, biomeStrippedWoodOrLogOrLogHorAcrossState, biomeStrippedWoodOrLogOrLogHorAcrossState, false);
        	this.fillWithBlocks(world, structureBB, 8, 1, 2, 8, 1, 4, biomeStrippedWoodOrLogOrLogHorAlongState, biomeStrippedWoodOrLogOrLogHorAlongState, false);
        	
        	// Set snow layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	this.setBlockState(world, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, 0, 1, 7, structureBB);
        	this.fillWithBlocks(world, structureBB, 0, 1, 1, 0, 1, 3, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, false);
        	this.fillWithBlocks(world, structureBB, 0, 1, 0, 5, 1, 0, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, false);
        	this.setBlockState(world, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, 8, 1, 7, structureBB);
        	this.fillWithBlocks(world, structureBB, 10, 1, 7, 11, 1, 7, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, false);
        	this.fillWithBlocks(world, structureBB, 11, 1, 0, 11, 1, 3, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, false);
        	this.fillWithBlocks(world, structureBB, 5, 2, 2, 8, 2, 5, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? Blocks.SNOW_LAYER.getStateFromMeta(0) : biomeSnowLayerState, false);
        	
        	// Ice spire
        	IBlockState biomePackedIceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PACKED_ICE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	this.fillWithBlocks(world, structureBB, 6, 1, 3, 7, 2, 4, biomePackedIceState, biomePackedIceState, false);
        	this.fillWithBlocks(world, structureBB, 6, 3, 4, 6, 7, 4, biomePackedIceState, biomePackedIceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 3, 3, 7, 5, 3, biomePackedIceState, biomePackedIceState, false);
        	
        	
        	// Place the sign base. Concrete if requested/allowed, ice otherwise
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.CONCRETE.getStateFromMeta(townColor);
            	
            	// Basin rim
            	this.setBlockState(world, concreteBlockstate, 2, 1, 5, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomePackedIceState, 2, 1, 5, structureBB);
        	}
        	
        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 2;
    			int signYBB = 2;
    			int signZBB = 5;
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
                int bannerXBB = 10;
    			int bannerZBB = 2;
    			int bannerYBB = 1;
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a grass foundation
                this.setBlockState(world, biomeGrassState, bannerXBB, bannerYBB-1, bannerZBB, structureBB);
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
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
    		
    		
            // Decor
            int[][] decorUVW = new int[][]{
            	{1, 1, 1},
            	{9, 1, 7},
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
            	
            	// Get ground level
            	if (this.decorHeightY.size()<(j+1))
            	{
            		// There are fewer stored ground levels than this decor number, so this is being generated for the first time.
            		// Add new ground level
            		//decorHeightY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, this.getXWithOffset(uvw[0], uvw[2]), this.getZWithOffset(uvw[0], uvw[2]))-this.boundingBox.minY;
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
        	
        	
    		
    		
    		// Villagers
            if (!this.villagersGenerated)
            {
            	this.villagersGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{3, 1, 3, -1, 0},
	        			{10, 1, 4, -1, 0},
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
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
    }
    
    
    
	// --- Frozen Fountain --- //
    
    public static class SnowyMeetingPoint2 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"    PPP    ",
            	"  PPPPPPP  ",
            	" PPPFFFPPP ",
            	"PPPFFFFFPPP",
            	"PPPFFFFFPFP",
            	"PPPFFFFFPPP",
            	" PPPFFFPPPF",
            	"  PPPPPPP  ",
            	"    PPP F  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
	    public SnowyMeetingPoint2() {}
		
		public SnowyMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
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
    					+ ReflectionHelper.getPrivateValue(Biome.class, this.biomeProvider.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)), new String[]{"biomeName","field_76791_y"})
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()%2==0?4:3), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()%2==0?3:4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()%2==0?4:3), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()%2==0?3:4), EnumFacing.WEST, this.getComponentType());
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
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodenStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);

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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
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
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(9, 4),
        			this.getYWithOffset(1),
        			this.getZWithOffset(9, 4));
        	
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
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
        	// Set grass
        	this.setBlockState(world, biomeGrassState, 8, 0, 0, structureBB);
        	this.setBlockState(world, biomeGrassState, 10, 0, 2, structureBB);
        	this.fillWithBlocks(world, structureBB, 4, 0, 2, 6, 0, 6, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 3, 0, 3, 3, 0, 5, biomeGrassState, biomeGrassState, false);
        	this.fillWithBlocks(world, structureBB, 7, 0, 3, 7, 0, 5, biomeGrassState, biomeGrassState, false);
        	// Set dirt
        	this.fillWithBlocks(world, structureBB, 5, 0, 2, 8, 0, 5, biomeDirtState, biomeDirtState, false);
        	this.setBlockState(world, biomeDirtState, 2, 0, 5, structureBB);
        	this.fillWithBlocks(world, structureBB, 5, 0, 3, 5, 0, 5, biomeDirtState, biomeDirtState, false);
        	this.setBlockState(world, biomeDirtState, 4, 0, 4, structureBB);
        	this.setBlockState(world, biomeDirtState, 6, 0, 4, structureBB);
        	// Stone brick for some reason
        	IBlockState biomeStoneBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONEBRICK.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	this.setBlockState(world, biomeStoneBrickState, 6, 0, 3, structureBB);
        	
        	// Ice fountain
        	
        	// Ice
        	IBlockState biomePackedIceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PACKED_ICE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	this.fillWithBlocks(world, structureBB, 5, 1, 3, 5, 2, 5, biomePackedIceState, biomePackedIceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, biomePackedIceState, biomePackedIceState, false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 4, 6, 2, 4, biomePackedIceState, biomePackedIceState, false);
        	this.setBlockState(world, biomePackedIceState, 5, 3, 4, structureBB);
        	// Torch
        	for (int[] uvwo : new int[][]{
        		{5,4,4, -1},
        	})
        	{
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        	}
        	
        	// Rim
        	for (int uvwm[] : new int[][]{
        		{6,1,2,3},
        		{5,1,2,3}, // Toward player
        		{4,1,2,0},
        		{4,1,3,3},
        		{3,1,3,0},
        		{3,1,4,0}, // Left side, left-facing
        		{3,1,5,2},
        		{4,1,5,0},
        		{4,1,6,2},
        		{5,1,6,2}, // Away from player
        		{6,1,6,1},
        		{6,1,5,2},
        		{7,1,5,1},
        		{7,1,4,1}, // Right side, right-facing
        		{7,1,3,3},
        		{6,1,3,1},
        	})
        	{
        		this.setBlockState(world, biomeWoodenStairsState.getBlock().getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB);
        	}
        	
        	
        	// Place the sign base. Concrete if requested/allowed, ice otherwise
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.CONCRETE.getStateFromMeta(townColor);
        		
        		// Basin rim
            	this.setBlockState(world, concreteBlockstate, 9, 0, 4, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomePlankState, 9, 0, 4, structureBB);
        	}
        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 9;
    			int signYBB = 1;
    			int signZBB = 4;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), Blocks.STANDING_SIGN.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerXBB = 8;
    			int bannerZBB = 7;
    			int bannerYBB = 1;
    			
    			int bannerX = this.getXWithOffset(bannerXBB, bannerZBB);
    			int bannerY = this.getYWithOffset(bannerYBB);
                int bannerZ = this.getZWithOffset(bannerXBB, bannerZBB);
                
                // Place a plank foundation
                this.setBlockState(world, biomePlankState, bannerXBB, bannerYBB-1, bannerZBB, structureBB);
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
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
	        			{7, 1, 1, -1, 0},
	        			{9, 1, 2, -1, 0},
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
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
    }
    
    
	// --- Snowy Pavilion --- //
    
    public static class SnowyMeetingPoint3 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"F PPP  ",
            	" PPPPP ",
            	"PPFPFPP",
            	"PPPPPPP",
            	"PPFPFPP",
            	" PPPPPF",
            	"  PPPF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
	    public SnowyMeetingPoint3() {}
		
		public SnowyMeetingPoint3(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
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
    					+ ReflectionHelper.getPrivateValue(Biome.class, this.biomeProvider.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2)), new String[]{"biomeName","field_76791_y"})
    					+ " at x=" + (this.boundingBox.minX+this.boundingBox.maxX)/2 + ", y=" + (this.boundingBox.minY+this.boundingBox.maxY)/2 + ", z=" + (this.boundingBox.minZ+this.boundingBox.maxZ)/2
    					+ " with town center: " + start.getClass().toString().substring(start.getClass().toString().indexOf("$")+1) + " and coordBaseMode: " + this.getCoordBaseMode() + ", horiz index: " + this.getCoordBaseMode().getHorizontalIndex()
    					);
    		}
    		
			// Northward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 2, EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 2, EnumFacing.WEST, this.getComponentType());
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
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLanternState = ModObjects.chooseModLanternBlockState(true);
        	
        	// For stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogState;//null; int biomeStrippedWoodOrLogOrLogVerticMeta = 0;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAlongState = biomeLogState;
        	IBlockState biomeStrippedWoodOrLogOrLogHorAcrossState = biomeLogState;
        	// Try stripped wood
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogState, 0); 
            	biomeStrippedWoodOrLogOrLogHorAlongState = ModObjects.chooseModStrippedWoodState(biomeLogState, 2);
            	biomeStrippedWoodOrLogOrLogHorAcrossState = ModObjects.chooseModStrippedWoodState(biomeLogState, 1);
        	}
        	// If stripped wood does not exist, try to get stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
        		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogState.getBlock().getMetaFromState(biomeLogState), 0);
        		biomeStrippedWoodOrLogOrLogHorAlongState = ModObjects.chooseModStrippedLogState(biomeLogState.getBlock().getMetaFromState(biomeLogState), 2);
        		biomeStrippedWoodOrLogOrLogHorAcrossState = ModObjects.chooseModStrippedLogState(biomeLogState.getBlock().getMetaFromState(biomeLogState), 1);
        	}

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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
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
        	NBTTagCompound villageNBTtag = StructureVillageVN.getOrMakeVNInfo(world,
        			this.getXWithOffset(3, 3),
        			this.getYWithOffset(3),
        			this.getZWithOffset(3, 3));
        	
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
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
        	// Set grass
        	this.setBlockState(world, biomeGrassState, 2, 0, 2, structureBB);
        	this.setBlockState(world, biomeGrassState, 2, 0, 4, structureBB);
        	this.setBlockState(world, biomeGrassState, 4, 0, 4, structureBB);
        	this.setBlockState(world, biomeGrassState, 4, 0, 2, structureBB);
        	// Set these grass blocks into ground level
    		for (int[] uw: new int[][]{
    			{0, 6},
    			{5, 0},
    			{6, 1},
    		})
    		{
    			int k = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(this.getXWithOffset(uw[0], uw[1]), 0, this.getZWithOffset(uw[0], uw[1]))).down().getY();
        		
                if (k > -1) {this.setBlockState(world, biomeGrassState, uw[0], k+1-this.boundingBox.minY, uw[1], structureBB);}
    		}
        	
            // Pavilion
            
            // Posts
            this.fillWithBlocks(world, structureBB, 2, 1, 2, 2, 3, 2, biomeFenceState, biomeFenceState, false);
            this.fillWithBlocks(world, structureBB, 2, 1, 4, 2, 3, 4, biomeFenceState, biomeFenceState, false);
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 3, 4, biomeFenceState, biomeFenceState, false);
            this.fillWithBlocks(world, structureBB, 4, 1, 2, 4, 3, 2, biomeFenceState, biomeFenceState, false);
            
        	// Place the "stripped wood" roof
        	this.fillWithBlocks(world, structureBB, 2, 4, 2, 3, 4, 2, biomeStrippedWoodOrLogOrLogHorAcrossState, biomeStrippedWoodOrLogOrLogHorAcrossState, false);
        	this.fillWithBlocks(world, structureBB, 2, 4, 3, 2, 4, 4, biomeStrippedWoodOrLogOrLogHorAlongState, biomeStrippedWoodOrLogOrLogHorAlongState, false);
        	this.fillWithBlocks(world, structureBB, 3, 4, 4, 4, 4, 4, biomeStrippedWoodOrLogOrLogHorAcrossState, biomeStrippedWoodOrLogOrLogHorAcrossState, false);
        	this.fillWithBlocks(world, structureBB, 4, 4, 2, 4, 4, 3, biomeStrippedWoodOrLogOrLogHorAlongState, biomeStrippedWoodOrLogOrLogHorAlongState, false);
        	
        	// Add torches
        	this.setBlockState(world, biomeFenceState, 1, 4, 3, structureBB);
        	this.setBlockState(world, biomeLanternState, 1, 3, 3, structureBB);
        	this.setBlockState(world, biomeFenceState, 5, 4, 3, structureBB);
        	this.setBlockState(world, biomeLanternState, 5, 3, 3, structureBB);
        	this.setBlockState(world, biomeFenceState, 3, 4, 1, structureBB);
        	this.setBlockState(world, biomeLanternState, 3, 3, 1, structureBB);
        	this.setBlockState(world, biomeFenceState, 3, 4, 5, structureBB);
        	this.setBlockState(world, biomeLanternState, 3, 3, 5, structureBB);
        	
        	
        	// Place the sign base. Concrete if requested/allowed
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.CONCRETE.getStateFromMeta(townColor);

            	// Basin rim
            	this.fillWithBlocks(world, structureBB, 3, 3, 3, 3, 5, 3, concreteBlockstate, concreteBlockstate, false);
        	}
        	else
        	{
        		this.fillWithBlocks(world, structureBB, 3, 3, 3, 3, 5, 3, biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);
        	}
        	
        	
        	
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 2;
    			int signYBB = 3;
    			int signZBB = 3;
                int signX = this.getXWithOffset(signXBB, signZBB);
                int signY = this.getYWithOffset(signYBB);
                int signZ = this.getZWithOffset(signXBB, signZBB);
        		
        		TileEntitySign signContents = StructureVillageVN.generateSignContents(namePrefix, nameRoot, nameSuffix);
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(3, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		
                int signXBB2 = 4;
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
                int bannerXBB = 5;
    			int bannerZBB = 0;
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
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
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
	        			{5, 1, 1, -1, 0},
	        			{5, 1, 5, -1, 0},
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
            
            // Clean items
            if (VillageGeneratorConfigHandler.cleanDroppedItems) {StructureVillageVN.cleanEntityItems(world, this.boundingBox);}
            return true;
        }
    }

    
    
    // ------------------ //
    // --- Components --- //
    // ------------------ //
    
    
    // --- Animal Pen 1 --- //
    
    public static class SnowyAnimalPen1 extends StructureVillagePieces.Village
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
        		"FFFFFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyAnimalPen1() {}

        public SnowyAnimalPen1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyAnimalPen1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyAnimalPen1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	
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
            	{2,1,1, 8,1,1}, 
            	{2,1,7, 8,1,7}, 
            	{2,1,2, 2,1,6}, 
            	{8,1,2, 8,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }

            
        	// Grass
        	for(int[] uuvvww : new int[][]{
            	{3,1,2, 7,1,6},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Surrounding
            	// Front
            	{2,2,1, 3,2,1}, {7,2,1, 8,2,1}, 
            	// Back
            	{2,2,7, 8,2,7}, 
            	// Left
            	{2,2,2, 2,2,6}, 
            	// Right
            	{8,2,2, 8,2,6}, 
            	// Torch posts
            	{4,2,1, 4,3,1}, {6,2,1, 6,3,1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Fence Gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwos : new int[][]{
            	{5,2,1, 2, 0}, 
            	})
            {
            	this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(uvwos[3], uvwos[4]==1)), uvwos[0], uvwos[1], uvwos[2], structureBB);
            }
        	
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{4,4,1, -1}, {6,4,1, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Trough
            	{4,2,3, 6,2,3}, 
            	{4,2,4, 4,2,4}, 
            	{4,2,5, 6,2,5}, 
            	{6,2,4, 6,2,4}, 
            	// Trough Bottom
            	{5,1,4, 5,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{5,2,4}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front
        		{4,1,0, 0}, {5,1,0, 3}, {6,1,0, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside of pen
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 5}, {0,1,5, 3}, {0,1,6, 0}, {0,1,7, 0}, 
            	{1,1,0, 0}, {1,1,1, 0}, {1,1,2, 4}, {1,2,5, 2}, {1,1,6, 4}, {1,1,7, 0}, 
            	{2,1,0, 0}, 
            	{3,1,0, 4}, 
            	{7,1,0, 4}, 
            	// Inside of pen
            	{3,2,2, 0}, {3,2,3, 0}, {3,2,4, 0}, {3,2,5, 0}, {3,2,6, 0}, 
            	{4,2,2, 0}, {4,2,6, 0}, 
            	{5,2,6, 0}, 
            	{6,2,2, 0}, {6,2,6, 0}, 
            	{7,2,2, 0}, {7,2,3, 0}, {7,2,4, 0}, {7,2,5, 0}, {7,2,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Snow Block
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{0,1,4}, 
            	{1,1,3}, {1,1,4}, {1,2,4}, {1,1,5}, 
            	})
            {
            	this.setBlockState(world, biomeSnowState, uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
        		{6, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
        			{5,2,2}, 
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM); // Because horses can escape the pen
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(animal);
                    
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
    
    
    // --- Animal Pen 2 --- //
    
    public static class SnowyAnimalPen2 extends StructureVillagePieces.Village
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
        		" FFFFFFFF",
        		" FFFF    ",
        		" FFFF    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 3;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyAnimalPen2() {}

        public SnowyAnimalPen2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyAnimalPen2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyAnimalPen2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
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
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Surrounding
            	{4,1,0, 4,1,2}, 
            	{5,1,2, 8,1,2}, 
            	{8,1,3, 8,1,7}, 
            	{0,1,7, 7,1,7}, 
            	{0,1,3, 0,1,6}, 
            	{1,1,0, 1,1,3}, 
            	{2,1,0, 2,1,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Fence Gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwos : new int[][]{
            	{3,1,0, 2, 0},
            	})
            {
            	this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(uvwos[3], uvwos[4]==1)), uvwos[0], uvwos[1], uvwos[2], structureBB);
            }
        	
            
            // Lantern
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{0,2,7}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{1,0,5}, {1,0,6}, {2,0,6}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }

            
        	// Grass pit in the ground
        	for(int[] uvw : new int[][]{
            	{2,0,3},  
            	})
            {
            	this.clearCurrentPositionBlocksUpwards(world, uvw[0], uvw[1], uvw[2], structureBB); 
            	this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{2,1,4, 0}, 
            	{3,1,2, 0}, {3,1,3, 0}, {3,1,4, 0}, {3,1,5, 3}, 
            	{4,1,3, 0}, {4,1,6, 0}, 
            	{5,1,3, 0}, {5,1,4, 2}, {5,1,6, 0}, 
            	{6,1,3, 0}, {6,1,5, 0}, {6,1,6, 0}, 
            	{7,1,4, 0}, {7,1,5, 0}, {7,1,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Snow Block
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{4,1,4}, 
            	{4,1,5}, 
            	{4,2,5}, 
            	{5,1,5}, 
            	})
            {
            	this.setBlockState(world, biomeSnowState, uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
        			{2,0,3}, 
        			{6,1,4}, 
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM); // Because horses can escape the pen
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(animal);
                    
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
    
    
    // --- Armorer House 1 --- //
    
    public static class SnowyArmorerHouse1 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyArmorerHouse1() {}

        public SnowyArmorerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyArmorerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyArmorerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	{1,1,1, 5,1,3}, 
            	{1,1,4, 1,1,5}, {5,1,4, 5,1,5}, 
            	// Front wall
            	{1,2,1, 1,3,1}, {5,2,1, 5,3,1}, 
            	{2,4,1, 2,5,1}, {4,4,1, 4,5,1}, 
            	{3,5,1, 3,6,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }

        	            
            // Torches, part 1
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{1,2,5, -1}, {5,2,5, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	IBlockState dioriteBlockState = Blocks.STONE.getStateFromMeta(3);
        	Block dioriteStairsBlock = ModObjects.chooseModDioriteStairsBlock();
        	IBlockState dioriteWallState = ModObjects.chooseModDioriteWallState();
        	if (dioriteStairsBlock==null || dioriteWallState==null)
        	{
        		dioriteBlockState = Blocks.COBBLESTONE.getStateFromMeta(0);
            	dioriteStairsBlock = Blocks.STONE_STAIRS;
            	dioriteWallState = Blocks.COBBLESTONE_WALL.getDefaultState();
        	}
        	
        	// Diorite block
        	for(int[] uuvvww : new int[][]{
            	// Furnace base
        		{2,1,4, 4,1,5}, 
        		// Body
        		{2,2,5, 2,2,5}, {4,2,5, 4,2,5}, 
        		// Chimney
        		{3,3,5, 3,7,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], dioriteBlockState, dioriteBlockState, false);	
            }
        	// Diorite stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,3,5, 0}, {4,3,5, 1}, 
        		})
            {
                this.setBlockState(world, dioriteStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	// Diorite wall
        	for(int[] uuvvww : new int[][]{
            	{3,8,5, 3,8,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], dioriteWallState, dioriteWallState, false);	
            }
            
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{3,2,5, 2}, 
        		})
            {
        		IBlockState blastFurnaceState = ModObjects.chooseModBlastFurnaceState(uvw[3], this.getCoordBaseMode());
                this.setBlockState(world, blastFurnaceState.getBlock().getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), blastFurnaceState, 2);
            }

        	            
            // Torches, part 2
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Individual vertical stripped log placements easier for the back wall
            for(int[] uvw : new int[][]{
            	{1,2,6}, 
            	{2,1,6}, {2,3,6}, {2,5,6}, 
            	{3,2,6}, {3,4,6}, {3,6,6}, 
            	{4,1,6}, {4,3,6}, {4,5,6}, 
            	{5,2,6}, 
            	})
            {
            	this.setBlockState(world, biomeStrippedLogVertState, uvw[0], uvw[1], uvw[2], structureBB); 
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
            for(int[] uvw : new int[][]{
            	// Front wall
            	{2,2,1}, {2,3,1}, 
            	{3,4,1}, 
            	{4,2,1}, {4,3,1}, 
            	// Back wall
            	{1,1,6}, {1,3,6}, 
            	{2,2,6}, {2,4,6}, 
            	{3,1,6}, {3,3,6}, {3,5,6}, 
            	{4,2,6}, {4,4,6}, 
            	{5,1,6}, {5,3,6}, 
            	})
            {
            	this.setBlockState(world, biomeStrippedLogHorizAlongState, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,2,0, 0,3,7}, 
            	{1,4,0, 1,5,7}, 
            	{2,6,0, 2,6,7}, 
            	{3,7,0, 3,7,4}, {3,7,6, 3,7,7}, 
            	{4,6,0, 4,6,7}, 
            	{5,4,0, 5,5,7}, 
            	{6,2,0, 6,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Lantern post
            	{3,5,0, 3,6,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,4,0}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
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
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,7, 0}, 
            	{1,1,0, 0}, {1,1,7, 0}, 
            	{2,1,0, 0}, {2,1,7, 0}, 
            	{3,1,7, 0}, 
            	{4,1,0, 0}, {4,1,7, 0}, 
            	{5,1,7, 0}, 
            	{6,1,0, 0}, {6,1,1, 0}, {6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, {6,1,6, 0}, {6,1,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{3,1,0, 3}, 
        		// Surrounding the chimney
        		{2,7,4, 3}, {4,7,4, 3}, 
        		{2,7,5, 0}, 
        		{2,7,6, 2}, {4,7,6, 2}, 
        		{4,7,5, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 5;
        	int chestV = 2;
        	int chestW = 2;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_armorer");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,1, 2, 1, 0}, 
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
        		{1, GROUND_LEVEL, -1}, 
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 5;
            	int v = 2;
            	int w = 2;
            	
            	while (u==5 && w==2)
            	{
                	u = 1+random.nextInt(5);
                	w = 2+random.nextInt(3);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Armorer House 2 --- //
    
    public static class SnowyArmorerHouse2 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyArmorerHouse2() {}

        public SnowyArmorerHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyArmorerHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyArmorerHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
        	// TODO - stripped wood
        	// For Stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,1, 2,3,1}, {4,1,1, 5,3,1}, 
            	{2,4,1, 4,5,1}, 
            	{3,6,1, 3,6,1}, 
            	// Back wall
            	{1,1,5, 5,3,5}, 
            	{2,4,5, 4,5,5}, 
            	{3,6,5, 3,6,5}, 
            	// Left wall
            	{1,1,2, 1,3,4}, 
            	// Right wall
            	{5,1,2, 5,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
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
            	// Below front door
            	{3,1,1, 3,1,1},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,2,1, 0,2,1}, {0,2,3, 0,2,3}, {0,2,5, 0,2,5}, 
            	{0,3,0, 0,3,6}, 
            	{1,4,0, 1,5,6}, 
            	{2,6,0, 2,6,6}, 
            	{3,7,0, 3,7,6}, {3,6,0, 3,6,0}, 
            	{4,6,0, 4,6,6}, 
            	{5,4,0, 5,5,6}, 
            	{6,3,0, 6,3,6}, 
            	{6,2,1, 6,2,1}, {6,2,3, 6,2,3}, {6,2,5, 6,2,5}, 
            	// Floor
            	{2,1,2, 4,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,2,0}, {0,2,2}, {0,2,4}, {0,2,6}, 
            	{2,5,0}, {2,5,6}, 
            	{3,6,6}, 
            	{4,5,0}, {4,5,6}, 
            	{6,2,0}, {6,2,2}, {6,2,4}, {6,2,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,4,0}, {0,4,2}, {0,4,4}, {0,4,6}, 
            	{1,6,3}, 
            	{5,6,3}, 
            	{6,4,0}, {6,4,2}, {6,4,4}, {6,4,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Lantern post
            	{3,6,2, 3,6,2}, 
            	{3,6,4, 3,6,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,0}, 
            	{3,5,2}, 
            	{3,5,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Ground
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, 
            	{1,1,0, 0}, {1,1,6, 0}, 
            	{2,1,6, 0}, 
            	{3,1,6, 0}, 
            	{4,1,6, 0}, 
            	{5,1,0, 0}, {5,1,6, 0}, 
            	{6,1,0, 0}, {6,1,1, 0}, {6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, {6,1,6, 0}, 
            	// Roof
            	{0,4,1, 0}, {0,4,3, 0}, {0,4,5, 0}, 
            	{1,6,0, 0}, {1,6,1, 0}, {1,6,2, 0}, {1,6,5, 0}, {1,6,6, 0}, 
            	{2,7,0, 0}, {2,7,1, 0}, {2,7,2, 0}, {2,7,3, 0}, {2,7,5, 0}, {2,7,6, 0}, 
            	{3,8,0, 0}, {3,8,1, 0}, {3,8,2, 0}, {3,8,3, 0}, {3,8,4, 0}, {3,8,5, 0}, {3,8,6, 0}, 
            	{4,7,0, 0}, {4,7,1, 0}, {4,7,2, 0}, {4,7,3, 0}, {4,7,4, 0}, {4,7,5, 0}, {4,7,6, 0}, 
            	{5,6,0, 0}, {5,6,1, 0}, {5,6,2, 0}, {5,6,4, 0}, {5,6,5, 0}, {5,6,6, 0}, 
            	{6,4,1, 0}, {6,4,3, 0}, {6,4,5, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,1,0, 0}, {3,1,0, 3}, {4,1,0, 1}, 
        		// Interior
        		{4,2,2, 2}, {4,2,4, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,1,3, 2,1,3}, 
            	// Ceiling
            	{2,6,4, 2,6,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{2,2,4, 2}, 
        		})
            {
        		IBlockState blastFurnaceState = ModObjects.chooseModBlastFurnaceState(uvw[3], this.getCoordBaseMode());
                this.setBlockState(world, blastFurnaceState.getBlock().getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), blastFurnaceState, 2);
            }
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Lower chimney
            	{2,3,4, 2,5,4}, 
            	// Upper chimney
            	{2,7,4, 2,8,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,4,3}, 
        		{3,4,5}, 
        		{5,4,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,1, 2, 0, 1}, 
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
        		{1, GROUND_LEVEL, -1}, 
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 2;
            	int v = 2;
            	int w = 4;
            	
            	while (u==2 && w==4)
            	{
                	u = 2+random.nextInt(2);
                	w = 2+random.nextInt(3);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Butcher House --- //
    
    public static class SnowyButchersShop1 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyButchersShop1() {}

        public SnowyButchersShop1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyButchersShop1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyButchersShop1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
        	// TODO - stripped wood
        	// For Stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,1, 2,3,1}, {4,1,1, 5,3,1}, 
            	{2,4,1, 4,5,1}, 
            	{3,6,1, 3,6,1}, 
            	// Back wall
            	{1,1,5, 5,3,5}, 
            	{2,4,5, 4,5,5}, 
            	{3,6,5, 3,6,5}, 
            	// Left wall
            	{1,1,2, 1,3,4}, 
            	// Right wall
            	{5,1,2, 5,3,4}, 
            	// Back yard rim
            	{1,1,6, 1,1,8}, 
            	{2,1,8, 4,1,8}, 
            	{5,1,6, 5,1,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
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
            	// Below front door
            	{3,1,1, 3,1,1},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }

            
        	// Grass
        	for(int[] uuvvww : new int[][]{
            	{2,1,6, 4,1,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,2,1, 0,2,1}, {0,2,3, 0,2,3}, {0,2,5, 0,2,5}, 
            	{0,3,0, 0,3,6}, 
            	{1,4,0, 1,5,6}, 
            	{2,6,0, 2,6,6}, 
            	{3,7,0, 3,7,6}, {3,6,0, 3,6,0}, 
            	{4,6,0, 4,6,6}, 
            	{5,4,0, 5,5,6}, 
            	{6,3,0, 6,3,6}, 
            	{6,2,1, 6,2,1}, {6,2,3, 6,2,3}, {6,2,5, 6,2,5}, 
            	// Floor
            	{2,1,2, 2,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,2,0}, {0,2,2}, {0,2,4}, {0,2,6}, 
            	{2,5,0}, {2,5,6}, 
            	{3,6,6}, 
            	{4,5,0}, {4,5,6}, 
            	{6,2,0}, {6,2,2}, {6,2,4}, {6,2,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,4,0}, {0,4,2}, {0,4,4}, {0,4,6}, 
            	{1,6,3}, 
            	{5,6,3}, 
            	{6,4,0}, {6,4,2}, {6,4,4}, {6,4,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Lantern post
            	{3,6,2, 3,6,2}, 
            	{3,6,4, 3,6,4}, 
            	{3,5,6, 3,5,7}, 
            	// Back yard fence
            	{1,2,6, 1,2,8}, 
            	{2,2,8, 4,2,8}, 
            	{5,2,6, 5,2,8}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,0}, 
            	{3,5,2}, 
            	{3,5,4}, 
            	{3,4,7}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Ground
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, {0,1,8, 0}, 
            	{1,1,0, 0},  
            	{5,1,0, 0},  
            	{6,1,0, 0}, {6,1,1, 0}, {6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, {6,1,6, 0}, {6,1,7, 0}, {6,1,8, 0}, 
            	// Roof
            	{0,4,1, 0}, {0,4,3, 0}, {0,4,5, 0}, 
            	{1,6,0, 0}, {1,6,1, 0}, {1,6,2, 0}, {1,6,5, 0}, {1,6,6, 0}, 
            	{2,7,0, 0}, {2,7,1, 0}, {2,7,2, 0}, {2,7,3, 0}, {2,7,5, 0}, {2,7,6, 0}, 
            	{3,8,0, 0}, {3,8,1, 0}, {3,8,2, 0}, {3,8,3, 0}, {3,8,4, 0}, {3,8,5, 0}, {3,8,6, 0}, 
            	{4,7,0, 0}, {4,7,1, 0}, {4,7,2, 0}, {4,7,3, 0}, {4,7,4, 0}, {4,7,5, 0}, {4,7,6, 0}, 
            	{5,6,0, 0}, {5,6,1, 0}, {5,6,2, 0}, {5,6,4, 0}, {5,6,5, 0}, {5,6,6, 0}, 
            	{6,4,1, 0}, {6,4,3, 0}, {6,4,5, 0}, 
            	// Backyard
            	{2,2,6, 0}, 
            	{4,2,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,1,0, 0}, {3,1,0, 3}, {4,1,0, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,1,3, 2,1,3}, 
            	// Ceiling
            	{2,6,4, 2,6,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Smoker
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.getCoordBaseMode());
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,4, 2}
            	})
            {
                //this.setBlockState(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                //world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), smokerState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
                world.setBlockState(
                		new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])),
                		smokerState.getBlock().getStateFromMeta(smokerState.getBlock() == Block.getBlockFromName(ModObjects.smokerFMC) ?
                				StructureVillageVN.chooseBlastFurnaceMeta(uvwo[3], this.getCoordBaseMode())
                				: StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())
                				)
                		, 2);
            }
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Lower chimney
            	{2,3,4, 2,5,4}, 
            	// Upper chimney
            	{2,7,4, 2,8,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
        	
        	
        	// Smooth Stone Block
        	IBlockState smoothStoneState = ModObjects.chooseModSmoothStoneBlockState();
            for (int[] uuvvww : new int[][]{
            	{4,2,3, 4,2,3},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], smoothStoneState, smoothStoneState, false);
            }
            
            
            // Smooth Stone Slab (Upper)
            for(int[] uuvvww : new int[][]{
            	// In front of counter
            	{3,1,2, 4,1,2}, 
            	{3,1,3, 3,1,3}, 
            	{3,1,4, 4,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.STONE_SLAB.getStateFromMeta(8), Blocks.STONE_SLAB.getStateFromMeta(8), false);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,4,3}, 
        		{5,4,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,1, 2, 1, 1}, 
            	{3,2,5, 0, 1, 0}, 
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
        		{1, GROUND_LEVEL, -1}, 
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
        			{2, 2, 7},
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM);
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
            	
            	// Villager
            	int u = 2;
            	int v = 2;
            	int w = 4;
            	
            	while (
            			(u==2 && w==4)
            			|| (u==4 && w==3)
            			)
            	{
                	u = 2+random.nextInt(3);
                	w = 2+random.nextInt(3);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Butcher Igloo --- //
    
    public static class SnowyButchersShop2 extends StructureVillagePieces.Village
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
        		"FFFFF", // Added so that the yard fence can be pushed back to prevent animal escaping off the back of the smoker
        		"FFFFF",
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
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyButchersShop2() {}

        public SnowyButchersShop2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyButchersShop2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyButchersShop2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
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
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Interior table
            	{1,1,1, 1,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Interior post
            	{3,1,3, 3,1,3}, 
            	// Back yard fence
            	//{0,1,4, 0,1,8}, 
            	//{4,1,4, 4,1,8}, 
            	//{1,1,8, 3,1,8}, 
            	// Pushed back to prevent animals from escaping off the back of the smoker
            	{0,1,4, 0,1,9}, 
            	{4,1,4, 4,1,9}, 
            	{1,1,9, 3,1,9}, 
            	// Hanging yard post
            	{4,2,6, 4,3,6}, {3,4,6, 4,4,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,6}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }

        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,2,3, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,0, 3,3,0}, 
            	// Back wall
            	{1,1,4, 3,3,4}, 
            	// Left
            	{0,1,1, 0,2,3}, 
            	// Right
            	{4,1,1, 4,2,3}, 
            	// Roof
            	{1,3,1, 1,3,3}, 
            	{2,4,1, 2,4,3}, 
            	{3,3,1, 3,3,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Front
            	{0,1,0, 0}, 
            	{4,1,0, 0}, 
            	// Interior
            	{2,1,1, 0}, {2,1,2, 0}, {3,1,1, 0}, 
            	// Yard
            	{1,1,5, 0}, {1,1,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Interior chair
        		{1,1,3, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Smoker
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.getCoordBaseMode());
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,7, 2}, 
            	})
            {
                //this.setBlockState(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                //world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), smokerState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
                world.setBlockState(
                		new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])),
                		smokerState.getBlock().getStateFromMeta(smokerState.getBlock() == Block.getBlockFromName(ModObjects.smokerFMC) ?
                				StructureVillageVN.chooseBlastFurnaceMeta(uvwo[3], this.getCoordBaseMode())
                				: StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())
                				)
                		, 2);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,1,0, 0, 1, 0}, 
            	{2,1,4, 2, 1, 1}, 
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
        		{1, GROUND_LEVEL, -1}, 
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
        			{2, 1, 5}, 
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM);
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
            	
            	// Villager
            	int u = 3;
            	int v = 1;
            	int w = 3;
            	
            	while ((u==3 && w==3))
            	{
                	u = 2+random.nextInt(2);
                	w = 1+random.nextInt(3);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Cartographer House --- //
    
    public static class SnowyCartographerHouse1 extends StructureVillagePieces.Village
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
    	
        public SnowyCartographerHouse1() {}

        public SnowyCartographerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyCartographerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyCartographerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
        	// TODO - stripped wood
        	// Stripped Log (Vertical), part 1
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
            	// Front wall
            	// Center
            	{4,1,2, 4,4,2}, {5,5,2, 5,5,2}, {6,1,2, 6,4,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }

        	            
            // Torches, part 1
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{4,3,1, 2}, 
            	{6,3,1, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Stripped Log (Vertical), part 2
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	// Left wing
            	{1,1,1, 1,5,1}, {2,1,1, 2,1,1}, {2,5,1, 2,6,1}, {3,1,1, 3,5,1}, 
            	// Center
            	{4,1,2, 4,4,2}, {5,5,2, 5,5,2}, {6,1,2, 6,4,2}, 
            	// Right wing
            	{7,1,1, 7,5,1}, {8,1,1, 8,1,1}, {8,5,1, 8,6,1}, {9,1,1, 9,5,1}, 
            	// Back wall
            	{1,1,5, 1,5,5}, {2,1,5, 2,1,5}, {2,5,5, 2,6,5}, {3,1,5, 3,5,5}, 
            	{4,1,5, 4,4,5}, {5,1,5, 5,1,5}, {5,5,5, 5,5,5}, {6,1,5, 6,4,5}, 
            	{7,1,5, 7,5,5}, {8,1,5, 8,1,5}, {8,5,5, 8,6,5}, {9,1,5, 9,5,5}, 
            	// Left wall
            	{1,2,2, 1,2,2}, 
            	{1,1,3, 1,1,3}, {1,3,3, 1,3,3}, 
            	{1,2,4, 1,2,4}, 
            	// Right wall
            	{9,2,2, 9,2,2}, 
            	{9,1,3, 9,1,3}, {9,3,3, 9,3,3}, 
            	{9,2,4, 9,2,4}, 
            	// Floor
            	{2,1,2, 2,1,3}, {5,1,2, 5,1,3}, {8,1,2, 8,1,3}, 
            	{2,1,4, 8,1,4}, 
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
            	// Front Windows
            	{2,2,1, 2,2,1}, {2,4,1, 2,4,1}, 
            	{8,2,1, 8,2,1}, {8,4,1, 8,4,1}, 
            	// Above the front door
            	{5,4,2, 5,4,2}, 
            	// Back Windows
            	{2,2,5, 2,2,5}, {2,4,5, 2,4,5}, 
            	{5,2,5, 5,2,5}, {5,4,5, 5,4,5}, 
            	{8,2,5, 8,2,5}, {8,4,5, 8,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
        	
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
            	// Left wall
            	{1,1,2, 1,1,2}, {1,3,2, 1,3,2}, 
            	{1,2,3, 1,2,3}, 
            	{1,1,4, 1,1,4}, {1,3,4, 1,3,4}, 
            	{1,4,2, 1,5,4}, 
            	// Right wall
            	{9,1,2, 9,1,2}, {9,3,2, 9,3,2}, 
            	{9,2,3, 9,2,3}, 
            	{9,1,4, 9,1,4}, {9,3,4, 9,3,4}, 
            	{9,4,2, 9,5,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }

        	            
            // Torches, part 2
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,5,3, 1}, 
            	{5,4,4, 2}, 
            	{8,5,3, 3}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,4,0, 0,5,6}, 
            	{1,6,0, 1,6,6}, 
            	{2,7,0, 2,7,6}, 
            	{3,6,0, 3,6,6}, 
            	{4,5,0, 4,5,6}, 
            	{5,6,0, 5,6,6}, 
            	{6,5,0, 6,5,6}, 
            	{7,6,0, 7,6,6}, 
            	{8,7,0, 8,7,6}, 
            	{9,6,0, 9,6,6}, 
            	{10,4,0, 10,5,6}, 
            	// Front porch
            	{4,1,1, 6,1,1}, 
            	// Floor
            	{3,1,2, 3,1,3}, {4,1,3, 4,1,3}, 
            	{7,1,2, 7,1,3}, {6,1,3, 6,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	// Front
            	{1,5,0}, {2,6,0}, {3,5,0}, {4,4,0}, {5,5,0}, {6,4,0}, {7,5,0}, {8,6,0}, {9,5,0}, 
            	// Back
            	{1,5,6}, {2,6,6}, {3,5,6}, {4,4,6}, {5,5,6}, {6,4,6}, {7,5,6}, {8,6,6}, {9,5,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	// Front
            	{4,6,0}, {6,6,0}, 
            	// Back
            	{4,6,6}, {6,6,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{0,1,0, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
            	// Front Windows
            	{2,3,1}, 
            	{8,3,1}, 
            	// Back Windows
            	{2,3,5}, 
            	{5,3,5}, 
            	{8,3,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{3,1,0, 0}, {4,1,0, 3}, {5,1,0, 3}, {6,1,0, 3}, {7,1,0, 1}, 
        		// Bench
        		{7,2,2, 2}, {8,2,2, 2}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
        	// Cartography Table
        	IBlockState cartographyTableState = ModObjects.chooseModCartographyTableState(2, this.getCoordBaseMode());
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,2,2}, 
           		})
        	{
            	this.setBlockState(world, cartographyTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 3;
        	int chestV = 2;
        	int chestW = 2;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_cartographer");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,2,2, 2, 1, 0}, 
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
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
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
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 2+random.nextInt(7);
            	int v = 2;
            	int w = 3+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 2, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Square Farm --- //
    
    public static class SnowyFarm1 extends StructureVillagePieces.Village
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
    	
        public SnowyFarm1() {}

        public SnowyFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
        	// TODO - stripped wood
        	// Stripped Log (Vertical), part 1
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
            	// Corners
            	{0,0,0, 0,0,0}, {6,0,0, 6,0,0}, 
            	{0,0,5, 0,0,5}, {6,0,5, 6,0,5}, 
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
            	{0,0,1, 0,0,4}, 
            	{6,0,1, 6,0,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	{1,0,0, 1,0,0}, {5,0,0, 5,0,0}, 
            	{1,0,5, 5,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Fence post
            	{3,1,5, 3,4,5}, {3,4,3, 3,4,4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,3}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,0,0, 3}, {3,0,0, 3}, {4,0,0, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, add nothing
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            for(int[] uvw : new int[][]{
            	{5,1,5}, 
            	})
            {
            	if (compostBinState != null) {this.setBlockState(world, compostBinState.getBlock().getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);}
            }
        	
                        
            // Moist Farmland with random crop above, drawn from two pairs
            Block[][] cropPairPair = new Block[][]{StructureVillageVN.chooseCropPair(random), StructureVillageVN.chooseCropPair(random)};
            for(int[] uvwmc : new int[][]{
            	{1,0,1, 0, 0}, {2,0,1, 1, 0}, {3,0,1, 0, 0}, {4,0,1, 0, 0}, {5,0,1, 0, 0}, 
            	{1,0,4, 1, 1}, {2,0,4, 1, 1}, {3,0,4, 0, 1}, {4,0,4, 0, 1}, {5,0,4, 0, 1}, 
            	{1,0,2, 0, 2}, {1,0,3, 0, 2}, {2,0,2, 0, 2}, {2,0,3, 0, 2}, 
            	{4,0,2, 0, 3}, {4,0,3, 0, 3}, {5,0,2, 0, 3}, {5,0,3, 1, 3}, 
            	})
            {
            	// Crop on top
            	int cropProgressMeta = uvwmc[3]; // Isolate the crop's age meta value
    			IBlockState cropState;
    			
    			while(true)
    			{
    				try {cropState = cropPairPair[uvwmc[4]/2][uvwmc[4]%2].getStateFromMeta(cropProgressMeta);}
    				catch (IllegalArgumentException e)
    				{
    					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
    					if (cropProgressMeta==0) {cropState = Blocks.WHEAT.getStateFromMeta(uvwmc[3]);}
    					// The crop is not allowed to have this value. Cut it in half and try again.
    					else {cropProgressMeta /= 2; continue;}
    				}
    				
    				// Finally, assign the working crop
    				this.setBlockState(world, cropState, uvwmc[0], uvwmc[1]+1, uvwmc[2], structureBB);
    				break;
    			}
            	
            	// Farmland underneath
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(7), uvwmc[0], uvwmc[1], uvwmc[2], structureBB); 
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{3,0,2}, {3,0,3}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 3;
            	int v = 1;
            	int w = 3;
            	
            	while (
            			(u==3 && w==3)
            			|| (u==3 && w==2)
            			)
            	{
                	u = 1+random.nextInt(5);
                	w = 1+random.nextInt(4);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Patch Farm --- //
    
    public static class SnowyFarm2 extends StructureVillagePieces.Village
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 4;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyFarm2() {}

        public SnowyFarm2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyFarm2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyFarm2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
        	
        	
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{0,1,3, 0,1,3}, 
            	{1,1,2, 1,1,5}, {1,2,3, 1,2,3}, 
            	{2,1,5, 2,1,6}, 
            	{3,1,0, 3,1,0}, 
            	{3,1,6, 5,1,6}, 
            	{4,1,0, 4,1,0}, {4,1,3, 4,1,3}, 
            	{5,1,1, 5,1,1}, 
            	{6,1,0, 6,1,0}, {6,1,1, 6,2,1}, {6,1,5, 6,2,5}, 
            	{7,1,3, 7,1,5}, {7,2,4, 7,2,4}, 
            	{8,1,1, 8,1,3}, {8,2,3, 8,2,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, 
            	{1,1,0, 0}, {1,1,1, 4}, {1,2,2, 2}, {1,1,6, 0}, 
            	{2,1,6, 2}, 
            	{5,1,0, 3}, 
            	{6,2,0, 0}, {6,1,6, 3}, 
            	{7,1,0, 0}, {7,1,1, 3}, {7,1,6, 0}, 
            	{8,1,0, 0}, {8,1,4, 4}, {8,1,5, 0}, {8,1,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
                        
            // Moist Farmland
            for(int[] uvwmm : new int[][]{
            	// u, v, w, farmland meta, crop meta
            	{2,1,1, 7, 0}, {2,1,2, 7, 0}, {2,1,3, 7, 0}, {2,1,4, 0, 0}, 
            	{3,1,1, 7, 0}, {3,1,2, 0, 0}, {3,1,4, 7, 0}, {3,1,5, 0, 0}, 
            	{4,1,1, 0, 0}, {4,1,2, 7, 0}, {4,1,4, 7, 0}, {4,1,5, 7, 0}, 
            	{5,1,2, 7, 0}, {5,1,4, 7, 0}, {5,1,5, 0, 0}, 
            	{6,1,2, 7, 0}, {6,1,3, 7, 0}, {6,1,4, 7, 0}, 
            	{7,1,2, 0, 0}, 
            	})
            {
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(uvwmm[3]), uvwmm[0], uvwmm[1], uvwmm[2], structureBB); 
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvwmm[4]), uvwmm[0], uvwmm[1]+1, uvwmm[2], structureBB); 
            }
            
            
            // Water with grass underneath
            for(int[] uvw : new int[][]{
            	{3,1,3}, {5,1,3}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            	this.setBlockState(world, biomeGrassState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
        	
            
            // Lantern (Sitting)
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{4,2,3}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,1,0, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, add nothing
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            for(int[] uvw : new int[][]{
            	{7,2,3}, 
            	})
            {
            	if (compostBinState != null) {this.setBlockState(world, compostBinState.getBlock().getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);}
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{1, GROUND_LEVEL, -1}, 
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 3;
            	int v = 2;
            	int w = 2;
            	
            	int s = random.nextInt(4+4+4+3+3+1);
            	
            	if (s<=3) {u=2; w=s+1;}
            	else if (s<=5) {u=3; w=s-3;}
            	else if (s<=7) {u=3; w=s-2;}
            	else if (s<=9) {u=4; w=s-7;}
            	else if (s<=11) {u=4; w=s-6;}
            	else if (s==12) {u=5; w=2;}
            	else if (s<=14) {u=5; w=s-9;}
            	else if (s<=17) {u=6; w=s-13;}
            	else if (s==18) {u=7; w=2;}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Fisher Cottage --- //
    
    public static class SnowyFisherCottage extends StructureVillagePieces.Village
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
    	
        public SnowyFisherCottage() {}

        public SnowyFisherCottage(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyFisherCottage buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyFisherCottage(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
        	// TODO - stripped wood
        	// Stripped Log (Vertical), part 1
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
            	// Front wall
            	{4,1,1, 4,3,5}, {4,4,2, 4,4,4}, {4,5,3, 4,5,3}, 
            	// Back wall
            	{7,1,1, 7,3,1}, 
            	{7,1,2, 7,2,2}, {7,4,2, 7,4,2}, 
            	{7,1,3, 7,1,3}, {7,5,3, 7,5,3}, 
            	{7,1,4, 7,2,4}, {7,4,4, 7,4,4}, 
            	{7,1,5, 7,3,5}, 
            	// Right wall
            	{5,1,1, 6,3,1}, 
            	// Left wall
            	{5,1,5, 6,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Front wall
            	{4,4,3, 4,4,3}, 
            	// Back wall
            	{7,4,3, 7,4,3}, {7,2,3, 7,2,3}, 
            	{7,3,2, 7,3,2}, {7,3,4, 7,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{3,3,0, 8,3,0}, 
            	{3,4,1, 8,4,1}, 
            	{3,5,2, 8,5,2}, 
            	{3,6,3, 8,6,3}, 
            	{3,5,4, 8,5,4}, 
            	{3,4,5, 8,4,5}, 
            	{3,3,6, 8,3,6}, 
            	// Floor
            	{5,1,2, 6,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{3,2,0}, {5,2,0}, {6,2,0}, {8,2,0}, 
            	{3,2,6}, {5,2,6}, {6,2,6}, {8,2,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{3,4,0}, {5,4,0}, {6,4,0}, {8,4,0}, 
            	{3,5,1}, {5,5,1}, {6,5,1}, {8,5,1}, 
            	{3,6,2}, {5,6,2}, {6,6,2}, {8,6,2}, 
            	{3,7,3}, {5,7,3}, {6,7,3}, {8,7,3}, 
            	{3,6,4}, {5,6,4}, {6,6,4}, {8,6,4}, 
            	{3,5,5}, {5,5,5}, {6,5,5}, {8,5,5}, 
            	{3,4,6}, {5,4,6}, {6,4,6}, {8,4,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Fence post
            	{0,2,2, 0,3,2}, {0,2,5, 0,3,5}, 
            	{0,4,2, 0,4,5}, {1,4,5, 2,4,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{0,3,4}, {1,3,5}, 
            	{6,5,3}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
            	{7,3,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{1,1,1, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
        	// Barrels
    		Block barrelBlock = ModObjects.chooseModBarrelBlockState();
    		boolean isChestType=(barrelBlock==null);
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel: -1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
    			// TODO - use different barrel meta for different mods
            	
    			// Exterior
    			{5,2,4, 2,-1}, 
    			{6,2,4, 2,-1}, 
    			{6,3,4, 2,2}, 
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (barrelBlock==null) {barrelBlock = Blocks.CHEST;}
    			//this.setBlockState(world, barrelState, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2])), barrelBlock.getStateFromMeta(barrelBlock==Blocks.CHEST?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.getCoordBaseMode()):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.getCoordBaseMode())), 2);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,3, 3, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
        	// Grass
            for(int[] uuvvww : new int[][]{
            	{0,1,2, 0,1,5}, {1,1,5, 3,1,5}, {3,1,4, 3,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);
            }
            
            
        	// Grass path
        	IBlockState grassPathState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS_PATH.getDefaultState(), this.materialType, this.biome, this.disallowModSubs); 
            for(int[] uuvvww : new int[][]{
            	{3,1,3, 3,1,3}, 
            	{1,1,2, 3,1,2}, 
            	{1,0,0, 1,0,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], grassPathState, grassPathState, false);
            }
            
            
            // Water
            for(int[] uuvvww : new int[][]{
            	{1,1,3, 2,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getStateFromMeta(0), Blocks.FLOWING_WATER.getStateFromMeta(0), false);
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{0,1,0, 0}, {2,1,0, 0}, {3,1,0, 0}, {4,1,0, 0}, {5,1,0, 0}, {6,1,0, 0}, {7,1,0, 0}, {8,1,0, 0}, 
            	{0,1,1, 0}, {2,1,1, 0}, {3,1,1, 0}, {8,1,1, 0}, 
            	{8,1,2, 0}, 
            	{8,1,3, 0}, 
            	{8,1,4, 0}, 
            	{8,1,5, 0}, 
            	{0,1,6, 0}, {2,1,6, 0}, {3,1,6, 0}, {4,1,6, 0}, {5,1,6, 0}, {6,1,6, 0}, {6,1,6, 0}, {8,1,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{1, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 5+random.nextInt(2);
            	int v = 2;
            	int w = 2+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 4, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    public static class SnowyFletcherHouse1 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyFletcherHouse1() {}

        public SnowyFletcherHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyFletcherHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyFletcherHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
        	// TODO - stripped wood
        	// Stripped Log (Vertical), part 1
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
            	// Front wall
            	{1,1,1, 1,3,1}, {2,5,1, 2,5,1}, 
            	{5,1,1, 5,3,1}, {4,5,1, 4,5,1}, 
            	// Back wall
            	{1,1,7, 1,3,7}, {2,1,7, 2,2,7}, {2,5,7, 2,5,7}, 
            	{3,1,7, 3,1,7}, {3,6,7, 3,6,7}, 
            	{5,1,7, 5,3,7}, {4,1,7, 4,2,7}, {4,5,7, 4,5,7}, 
            	// Left wall
            	{1,1,2, 1,1,6}, 
            	{1,2,3, 1,2,3}, {1,2,5, 1,2,5}, 
            	{1,3,2, 1,3,2}, {1,3,4, 1,3,4}, {1,3,6, 1,3,6}, 
            	// Right wall
            	{5,1,2, 5,1,6}, 
            	{5,2,3, 5,2,3}, {5,2,5, 5,2,5}, 
            	{5,3,2, 5,3,2}, {5,3,4, 5,3,4}, {5,3,6, 5,3,6}, 
            	// Floor
            	{3,1,2, 3,1,2}, 
            	{2,1,3, 2,1,3}, {4,1,3, 4,1,3}, 
            	{3,1,4, 3,1,4}, 
            	{2,1,5, 2,1,5}, {4,1,5, 4,1,5}, 
            	{3,1,6, 3,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Below front door
            	{3,1,1, 3,1,1}, 
            	// Left wall
            	{1,2,2, 1,2,2}, {1,2,4, 1,2,4}, {1,2,6, 1,2,6}, 
            	{1,3,3, 1,3,3}, {1,3,5, 1,3,5}, 
            	// Right wall
            	{5,2,2, 5,2,2}, {5,2,4, 5,2,4}, {5,2,6, 5,2,6}, 
            	{5,3,3, 5,3,3}, {5,3,5, 5,3,5}, 
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
            	// Front
            	{2,1,1, 2,4,1}, 
            	{3,4,1, 3,6,1}, 
            	{4,1,1, 4,4,1}, 
            	// Back
            	{3,2,7, 3,2,7}, 
            	{2,3,7, 2,4,7}, {4,3,7, 4,4,7}, 
            	{3,5,7, 3,5,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,3,0, 0,3,8}, 
            	{1,4,0, 1,5,8}, 
            	{2,6,0, 2,6,8}, 
            	{3,7,0, 3,7,8}, 
            	{4,6,0, 4,6,8}, 
            	{5,4,0, 5,5,8}, 
            	{6,3,0, 6,3,8}, 
            	// Floor
            	{2,1,2, 2,1,2}, {4,1,2, 4,1,2}, 
            	{3,1,3, 3,1,3}, 
            	{2,1,4, 2,1,4}, {4,1,4, 4,1,4}, 
            	{3,1,5, 3,1,5}, 
            	{2,1,6, 2,1,6}, {4,1,6, 4,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Table
            	{2,2,3, 2,2,3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		// Table top
        		{2,3,3, (GeneralConfig.useVillageColors ? this.townColor  : 11)}, // 11 is Blue
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }

        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,2, 0}, 
            	{3,5,6, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
            	{3,4,7}, {3,3,7}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,1,0, 0}, {3,1,0, 3}, {4,1,0, 1}, 
        		// Chairs
        		{2,2,2, 2}, {2,2,4, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Fletching Table
        	IBlockState fletchingTableState = ModObjects.chooseModFletchingTableState(2, this.getCoordBaseMode());
        	this.setBlockState(world, fletchingTableState, 3, 2, 6, structureBB);
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,1, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, {0,1,8, 0}, 
            	{1,1,0, 0}, {1,1,8, 0}, 
            	{2,1,8, 0}, 
            	{3,1,8, 0}, 
            	{4,1,8, 0}, 
            	{5,1,0, 0}, {5,1,8, 0}, 
            	{6,1,0, 0}, {6,1,2, 0}, {6,1,3, 0}, {6,1,5, 0}, {6,1,6, 0}, {6,1,7, 0}, {6,1,8, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{1, GROUND_LEVEL, -1}, 
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
        		{5, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 3+random.nextInt(2);
            	int v = 2;
            	int w = 2+random.nextInt(4);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 4, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    public static class SnowyLibrary1 extends StructureVillagePieces.Village
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
    	public static final int STRUCTURE_HEIGHT = 11;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyLibrary1() {}

        public SnowyLibrary1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyLibrary1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyLibrary1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
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
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,4,1, 0,5,11}, 
            	{1,6,1, 1,7,11}, 
            	{2,8,1, 2,8,11}, 
            	{3,9,1, 3,9,11}, 
            	{4,8,1, 4,8,11}, 
            	{5,6,1, 5,7,11}, 
            	{6,4,1, 6,5,11}, 
            	// Floor
            	{2,1,3, 4,1,9}, 
            	// Ceiling
            	{2,7,6, 2,7,6}, 
            	// Desks
            	{2,2,9, 2,2,9}, {4,2,9, 4,2,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Entry posts
            	{2,2,3, 2,2,3}, {4,2,3, 4,2,3}, 
            	// Ceiling rafters
            	{2,6,3, 2,6,4}, {2,6,6, 2,6,9}, 
            	// Ceiling lantern support
            	{3,7,6, 3,8,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }

        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,3,3, -1}, 
            	{4,3,3, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
        	// TODO - stripped wood
        	// Stripped Wood
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,2, 5,5,2}, {2,6,2, 4,7,2}, {3,8,2, 3,8,2}, 
            	// Back wall
            	{1,1,10, 5,5,10}, {2,6,10, 4,7,10}, {3,8,10, 3,8,10}, 
            	// Left wall
            	{1,1,3, 1,5,9}, 
            	// Right wall
            	{5,1,3, 5,5,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Below front door
            	{3,1,2, 3,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
            	{1,2,4}, {1,3,4}, 
            	{1,2,6}, {1,3,6}, 
            	{1,2,8}, {1,3,8}, 
            	{3,2,10}, {3,3,10}, 
            	{3,5,10}, {3,6,10}, 
            	{5,2,4}, {5,3,4}, 
            	{5,2,6}, {5,3,6}, 
            	{5,2,8}, {5,3,8}, 
            	{3,5,2}, {3,6,2}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{3,1,1, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Lecterns
        	/*
        	blockObject = ModObjects.chooseModLectern(); Block lecternBlock = (Block) blockObject[0]; int lecternMeta = (Integer) blockObject[1];
            this.setBlockState(world, lecternBlock, lecternMeta, 2, 3, 5, structureBB);
            */
            for (int[] uvwo : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,2,7, 2},
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
        		{2,2,5, 2,6,5}, 
        		{2,7,3, 2,7,5}, {2,7,7, 2,7,9}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,6,6}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Lantern (Sitting)
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{4,3,9}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,2, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{1,1,1, 2,1,1}, {2,2,1, 2,2,1}, {4,1,1, 5,1,1}, 
            	// Back side
            	{1,1,11, 3,1,11}, {2,2,11, 2,2,11}, 
            	// Left side
            	{0,1,6, 0,1,6}, 
            	// Right side
            	{6,1,4, 6,2,4}, {6,1,5, 6,1,5}, {6,1,10, 6,1,11}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	// Left
            	{0,1,0, 0}, {0,1,1, 3}, {0,1,2, 3}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 3}, {0,2,6, 2}, {0,1,7, 4}, {0,1,8, 3}, {0,1,9, 0}, {0,1,10, 0}, {0,1,11, 1}, {0,1,12, 0}, 
            	// Right
            	{6,1,0, 0}, {6,1,1, 0}, {6,1,2, 0}, {6,1,3, 4}, {6,1,6, 2}, {6,1,7, 0}, {6,1,8, 0}, {6,1,9, 3}, {6,2,10, 1}, {6,1,12, 0}, 
            	// Front
            	{1,1,0, 3}, {2,1,0, 5}, {4,1,0, 2}, {5,1,0, 0}, 
            	{4,2,1, 2}, 
            	// Back
            	{1,2,11, 3}, {2,3,11, 0}, {3,2,11, 2}, {4,1,11, 0}, {5,1,11, 4}, 
            	{1,1,12, 4}, {2,1,12, 0}, {3,1,12, 4}, {4,1,12, 0}, {5,1,12, 0}, 
            	// Roof
            	{0,6,1, 3}, {0,6,2, 7}, {0,6,3, 3}, {0,6,4, 0}, {0,6,5, 0}, {0,6,6, 0}, {0,6,7, 0}, {0,6,8, 0}, {0,6,9, 0}, {0,6,10, 0}, {0,6,11, 0}, 
            	{1,8,1, 0}, {1,8,2, 0}, {1,8,3, 0}, {1,8,4, 0}, {1,8,5, 0}, {1,8,6, 0}, {1,8,7, 0}, {1,8,8, 0}, {1,8,9, 0}, {1,8,10, 0}, {1,8,11, 0}, 
            	{2,9,1, 0}, {2,9,2, 0}, {2,9,3, 1}, {2,9,4, 0}, {2,9,5, 0}, {2,9,6, 2}, {2,9,7, 1}, {2,9,8, 0}, {2,9,9, 2}, {2,9,10, 0}, {2,9,11, 0}, 
            	{3,10,1, 0}, {3,10,2, 0}, {3,10,3, 1}, {3,10,4, 2}, {3,10,5, 3}, {3,10,6, 0}, {3,10,7, 0}, {3,10,8, 2}, {3,10,9, 0}, {3,10,10, 0}, {3,10,11, 0}, 
            	{4,9,1, 0}, {4,9,2, 0}, {4,9,3, 0}, {4,9,4, 0}, {4,9,5, 0}, {4,9,6, 0}, {4,9,7, 0}, {4,9,8, 0}, {4,9,9, 0}, {4,9,10, 0}, {4,9,11, 0}, 
            	{5,8,1, 1}, {5,8,2, 4}, {5,8,3, 2}, {5,8,4, 1}, {5,8,5, 0}, {5,8,6, 0}, {5,8,7, 0}, {5,8,8, 0}, {5,8,9, 0}, {5,8,10, 0}, {5,8,11, 0}, 
            	{6,6,2, 0}, {6,6,3, 1}, {6,6,4, 2}, {6,6,5, 0}, {6,6,6, 0}, {6,6,7, 0}, {6,6,8, 0}, {6,6,9, 0}, {6,6,10, 0}, {6,6,11, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 3;
            	int v = 2;
            	int w = 7;
            	
            	while (
            			(u==2 && w==5)
            			|| (u==3 && w==7)
            			|| (u==2 && w==9)
            			|| (u==4 && w==9)
            			)
            	{
                	u = 2+random.nextInt(3);
                	w = 4+random.nextInt(6);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 1, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Mason House 1 --- //
    
    public static class SnowyMasonsHouse1 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyMasonsHouse1() {}

        public SnowyMasonsHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyMasonsHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyMasonsHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
        	// TODO - stripped wood
        	// For Stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,2, 3,3,2}, {6,1,2, 7,3,2}, {3,4,2, 6,5,2}, {4,6,2, 5,6,2}, 
            	{4,1,2, 5,1,2}, 
            	// Back wall
            	{2,1,6, 7,3,6}, {3,4,6, 6,5,6}, {4,6,6, 5,6,6}, 
            	// Under floor
            	{3,1,3, 6,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
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
            	// Under front door
            	{4,2,2, 5,2,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{1,2,1, 1,3,7}, 
            	{2,4,1, 2,5,7}, 
            	{3,6,1, 3,6,7}, 
            	{4,7,1, 5,7,7}, 
            	{6,6,1, 6,6,7}, 
            	{7,4,1, 7,5,7}, 
            	{8,2,1, 8,3,7}, 
            	// Floor
            	{2,2,3, 7,2,5}, 
            	// Desks
            	{6,3,3, 6,3,3}, {6,3,5, 6,3,5}, 
            	{7,3,3, 7,3,5}, 
            	// Under front entrance
            	{4,1,1, 5,1,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
            	{4,4,6}, {5,4,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{4,1,0, 3}, {5,1,0, 3}, 
        		{4,2,1, 3}, {5,2,1, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		{4,3,3, (GeneralConfig.useVillageColors ? this.townColor  : 11)}, // Blue
        		{5,3,3, (GeneralConfig.useVillageColors ? this.townColor2 : 14)}, // Red
        		{4,3,4, (GeneralConfig.useVillageColors ? this.townColor2 : 14)}, // Red
        		{5,3,4, (GeneralConfig.useVillageColors ? this.townColor  : 11)}, // Blue
        		{4,3,5, (GeneralConfig.useVillageColors ? this.townColor  : 11)}, // Blue
        		{5,3,5, (GeneralConfig.useVillageColors ? this.townColor2 : 14)}, // Red
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
            // Stone Cutter
        	// Orientation:0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	IBlockState stonecutterState = ModObjects.chooseModStonecutterState(3, this.getCoordBaseMode());
            this.setBlockState(world, stonecutterState, 6, 3, 4, structureBB);
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,3,3, 1}, 
            	{2,3,4, 1}, 
            	{2,3,5, 1}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,4}, 
            	{6,5,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,3,2, 2, 1, 1}, 
            	{5,3,2, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{1,1,0, 2,1,0}, {2,1,1, 3,1,1}, {3,2,1, 3,2,1}, 
            	{6,1,1, 8,1,1}, {7,2,1, 7,2,1}, {7,1,0, 8,1,0}, 
            	// Back side
            	{2,1,7, 2,2,7}, {3,1,7, 6,1,7}, {8,1,7, 8,1,7}, {7,2,7, 7,2,7}, 
            	{3,1,8, 3,1,8}, {6,1,8, 6,1,8}, {8,1,8, 8,2,8}, 
            	// Left side
            	{0,1,1, 0,2,1}, {0,1,3, 0,1,3}, 
            	{1,1,1, 1,1,3}, 
            	// Right side
            	{8,1,6, 9,1,6}, {9,2,6, 9,2,6}, {9,1,7, 9,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 2}, {0,1,2, 1}, {0,2,3, 0}, {0,1,4, 2}, {0,1,5, 1}, {0,1,6, 0}, {0,1,7, 1}, {0,1,8, 1}, 
            	{1,2,0, 4}, {1,1,3, 0}, {1,1,4, 0}, {1,1,5, 0}, {1,1,6, 0}, {1,1,7, 3}, {1,1,8, 1}, 
            	{2,2,0, 0}, {2,2,1, 0}, {2,1,8, 0}, 
            	{3,1,0, 0}, {3,2,7, 0}, {3,2,8, 0}, 
            	{4,1,8, 0}, 
            	{5,1,8, 0}, 
            	{6,1,0, 0}, 
            	{7,1,7, 0}, {7,1,8, 0}, 
            	{8,2,0, 0}, {8,1,2, 0}, {8,1,3, 0}, {8,1,4, 0}, {8,1,5, 0}, {8,3,8, 0}, 
            	{9,1,0, 0}, {9,1,1, 0}, {9,1,2, 0}, {9,1,3, 0}, {9,1,4, 0}, {9,1,5, 0}, {9,3,7, 7}, {9,4,7, 0}, {9,1,8, 0}, 
            	// Roof
            	{1,4,1, 0}, {1,4,2, 7}, {1,5,2, 7}, {1,6,2, 0}, {1,4,3, 0}, {1,4,4, 0}, {1,4,5, 0}, {1,4,6, 0}, {1,4,7, 0}, 
            	{2,6,1, 0}, {2,6,2, 7}, {2,7,2, 0}, {2,6,3, 0}, {2,6,4, 0}, {2,6,5, 0}, {2,6,6, 0}, {2,6,7, 0}, 
            	{3,7,1, 0}, {3,7,2, 0}, {3,7,3, 0}, {3,7,4, 0}, {3,7,5, 0}, {3,7,6, 0}, {3,7,7, 0}, 
            	{4,8,1, 0}, {4,8,2, 0}, {4,8,3, 0}, {4,8,4, 0}, {4,8,5, 0}, {4,8,6, 0}, {4,8,7, 0}, 
            	{5,8,1, 0}, {5,8,2, 0}, {5,8,3, 0}, {5,8,4, 0}, {5,8,5, 0}, {5,8,6, 0}, {5,8,7, 0}, 
            	{6,7,1, 0}, {6,7,2, 0}, {6,7,3, 0}, {6,7,4, 0}, {6,7,5, 0}, {6,7,6, 0}, {6,7,7, 0}, 
            	{7,6,1, 0}, {7,6,2, 0}, {7,6,3, 0}, {7,6,4, 0}, {7,6,5, 0}, {7,6,6, 0}, {7,6,7, 0}, 
            	{8,4,1, 0}, {8,4,2, 7}, {8,5,2, 0}, {8,4,3, 0}, {8,4,5, 0}, {8,4,6, 0}, {8,4,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int w = 3 + random.nextInt(3);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 4, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Mason House 2 --- //
    
    public static class SnowyMasonsHouse2 extends StructureVillagePieces.Village
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
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 11;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyMasonsHouse2() {}

        public SnowyMasonsHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyMasonsHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyMasonsHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            

        	// TODO - stripped wood
        	// For Stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{3,1,2, 3,4,2}, {4,5,2, 4,5,2}, {5,1,2, 5,4,2}, 
            	{1,1,3, 2,4,3}, {6,1,3, 7,4,3}, 
            	{2,5,3, 2,5,3}, {6,5,3, 6,5,3}, 
            	{3,6,3, 3,6,3}, {5,6,3, 5,6,3}, 
            	// Back wall
            	{1,1,6, 2,4,6}, {6,1,6, 7,4,6}, 
            	{2,5,6, 2,5,6}, {6,5,6, 6,5,6}, 
            	// Left wall
            	{1,1,4, 1,3,5}, {7,1,4, 7,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Below front door
            	{3,1,1, 5,1,1}, {4,1,2, 4,1,2}, 
            	// Inside front door
            	{3,5,3, 5,5,3}, {4,7,3, 4,7,3}, 
            	// Left and right walls
            	{1,4,4, 1,4,5}, {7,4,4, 7,4,5}, 
            	// Chimney wall
            	{3,5,6, 3,6,6}, {5,5,6, 5,6,6}, 
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
            	// Front door
            	{4,4,2, 4,4,2}, 
            	{4,6,3, 4,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,3,2, 0,4,8}, 
            	{1,5,2, 1,6,8}, 
            	{2,6,2, 2,7,8}, 
            	{3,7,2, 3,8,8}, 
            	{4,8,2, 4,9,8}, 
            	{5,7,2, 5,8,8}, 
            	{6,6,2, 6,7,8}, 
            	{7,5,2, 7,6,8}, 
            	{8,3,2, 8,4,8}, 
            	// Front awning
            	{2,3,0, 2,4,2}, 
            	{3,5,0, 3,5,2}, 
            	{4,6,0, 4,6,2}, 
            	{5,5,0, 5,5,2}, 
            	{6,3,0, 6,4,2}, 
            	// Floor
            	{3,1,3, 5,1,4}, 
            	{2,1,4, 2,1,5}, {6,1,4, 6,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	// Roof
            	{0,5,2}, {0,5,3}, {0,5,4}, {0,5,5}, {0,5,6}, {0,5,7}, {0,5,8}, 
            	{2,8,2}, {2,8,3}, {2,8,4}, {2,8,5}, {2,8,6}, {2,8,7}, {2,8,8}, 
            	{3,9,2}, {3,9,3}, {3,9,4}, {3,9,5}, {3,9,6}, {3,9,7}, {3,9,8}, 
            	{5,9,2}, {5,9,3}, {5,9,4}, {5,9,5}, {5,9,6}, {5,9,7}, {5,9,8}, 
            	{6,8,2}, {6,8,3}, {6,8,4}, {6,8,5}, {6,8,6}, {6,8,7}, {6,8,8}, 
            	{8,5,2}, {8,5,3}, {8,5,4}, {8,5,5}, {8,5,6}, {8,5,7}, {8,5,8}, 
            	// Front awning
            	{2,5,0}, {2,5,1}, {2,5,2}, // Mistake: official left out the "2"
            	{3,6,0}, {3,6,1}, {3,6,2}, 
            	{5,6,0}, {5,6,1}, {5,6,2}, 
            	{6,5,0}, {6,5,1}, {6,5,2}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{3,1,0, 3}, {4,1,0, 3}, {5,1,0, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
        	IBlockState dioriteBlockState = Blocks.STONE.getStateFromMeta(3);
        	Block dioriteStairsBlock = ModObjects.chooseModDioriteStairsBlock();
        	IBlockState dioriteWallState = ModObjects.chooseModDioriteWallState();
        	if (dioriteStairsBlock==null || dioriteWallState==null)
        	{
        		dioriteBlockState = Blocks.COBBLESTONE.getStateFromMeta(0);
            	dioriteStairsBlock = Blocks.STONE_STAIRS;
            	dioriteWallState = Blocks.COBBLESTONE_WALL.getDefaultState();
        	}
        	
        	// Diorite block
        	for(int[] uuvvww : new int[][]{
            	// Furnace base
        		{3,1,5, 5,1,7}, 
        		// Furnace casing
        		{3,2,6, 3,2,6}, {5,2,6, 5,2,6}, 
        		{3,2,7, 5,4,7}, 
        		// Chimney
        		{4,5,6, 4,7,7}, {4,8,6, 4,9,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], dioriteBlockState, dioriteBlockState, false);	
            }
        	// Diorite stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Seats
        		{2,2,5, 1}, 
        		{6,2,5, 0}, {6,2,4, 0}, 
        		// Exterior chimney
        		{3,5,7, 0}, {5,5,7, 1}, 
        		})
            {
                this.setBlockState(world, dioriteStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	// Diorite wall
        	for(int[] uuvvww : new int[][]{
            	// Chimney
        		{4,3,6, 4,4,6}, 
            	{4,10,6, 4,10,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], dioriteWallState, dioriteWallState, false);	
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,6, 2}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
        	
        	
            // Stone Cutter
        	// Orientation:0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	IBlockState stonecutterState = ModObjects.chooseModStonecutterState(1, this.getCoordBaseMode());
            this.setBlockState(world, stonecutterState, 2, 2, 4, structureBB);
        	
        	            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{3,4,0}, {5,4,0}, 
            	// Back entrance
            	{4,7,8}, 
            	// Interior
            	{4,7,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Lantern (Sitting)
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{5,3,6}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,2, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, {0,1,8, 0}, 
            	{1,1,0, 0}, {1,1,1, 0}, {1,1,2, 0}, {1,1,7, 0}, {1,1,8, 0}, 
            	{2,1,0, 0}, {2,1,1, 0}, {2,1,2, 0}, {2,1,7, 0}, {2,1,8, 0}, 
            	{3,1,8, 0}, 
            	{4,1,8, 0}, 
            	{5,1,8, 0}, 
            	{6,1,0, 0}, {6,1,1, 0}, {6,1,2, 0}, {6,1,7, 0}, {6,1,8, 0}, 
            	{7,1,0, 0}, {7,1,1, 0}, {7,1,2, 0}, {7,1,7, 0}, {7,1,8, 0}, 
            	{8,1,0, 0}, {8,1,1, 0}, {8,1,2, 0}, {8,1,3, 0}, {8,1,4, 0}, {8,1,5, 0}, {8,1,6, 0}, {8,1,7, 0}, {8,1,8, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
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
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int v = 2;
            	int w = 3 + random.nextInt(3);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 4, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    public static class SnowyMediumHouse1 extends StructureVillagePieces.Village
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
    	
        public SnowyMediumHouse1() {}

        public SnowyMediumHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyMediumHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyMediumHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{3,1,0, 5,3,0}, 
            	// Back side
            	{3,1,6, 5,3,6}, 
            	// Left side
            	{1,1,2, 1,2,4}, 
            	{2,1,1, 2,3,1}, {2,1,5, 2,3,5}, {2,3,2, 2,3,4}, 
            	// Right side
            	{7,1,2, 7,2,4}, 
            	{6,1,1, 6,3,1}, {6,1,5, 6,3,5}, {6,3,2, 6,3,4}, 
            	// Roof
            	{3,4,1, 5,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, 
            	{1,1,0, 0}, {1,1,1, 0}, {1,3,4, 0}, {1,1,5, 0}, {1,1,6, 0}, 
            	{2,1,0, 0}, {2,4,4, 0}, {2,4,5, 0}, {2,1,6, 0}, 
            	{3,5,2, 0}, {3,5,3, 0}, {3,5,4, 0}, {3,5,5, 0}, {3,4,6, 0}, 
            	{4,4,0, 0}, {4,5,1, 0}, {4,5,2, 0}, {4,5,3, 0}, {4,5,4, 0}, {4,5,5, 0}, {4,4,6, 0}, 
            	{5,5,1, 0}, {5,5,2, 0}, {5,5,3, 0}, {5,5,4, 0}, {5,5,5, 0}, {5,4,6, 0}, 
            	{6,1,0, 0}, {6,4,1, 0}, {6,4,2, 0}, {6,4,3, 0}, {6,4,4, 0}, {6,4,5, 0}, {6,1,6, 0}, 
            	{7,1,0, 0}, {7,1,1, 0}, {7,3,2, 0}, {7,3,3, 0}, {7,3,4, 0}, {7,1,5, 0}, {7,1,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{4,3,4}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,4, 2, GeneralConfig.useVillageColors ? this.townColor3 : 0}, // White
            	{5,1,4, 2, GeneralConfig.useVillageColors ? this.townColor3 : 0}, // White
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
            	{4,1,0, 0, 1, 0}, 
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
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{5,1,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowyMediumHouse2 extends StructureVillagePieces.Village
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
        		" FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFFF",
        		" FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFFF",
        		" FFFFFFFFFFFFF",
        		"FFFFFFFFFFFFFF",
        		" FFFFFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyMediumHouse2() {}

        public SnowyMediumHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyMediumHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyMediumHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            

        	// TODO - stripped wood
        	// For Stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1, 3,3,1}, {5,1,1, 7,3,1}, {9,1,1, 10,3,1}, 
            	{2,4,1, 10,5,1}, 
            	// Back wall
            	{2,1,4, 10,5,4}, 
            	// Left wall
            	{2,1,2, 2,7,3}, 
            	// Right wall
            	{10,1,2, 10,7,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
            }

        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Bottom floor
            	{6,3,3, 2}, 
            	{8,3,3, 2}, 
            	// Top floor
            	{3,6,3, 1}, {3,6,2, 1}, 
            	{9,6,3, 3}, {9,6,2, 3}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
            	// Front door
            	{4,1,1, 4,1,1}, 
            	{8,1,1, 8,1,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{1,5,0, 11,5,0}, 
            	{2,4,0, 2,4,0}, {4,4,0, 4,4,0}, {6,4,0, 6,4,0}, {8,4,0, 8,4,0}, {10,4,0, 10,4,0}, 
            	{1,6,1, 11,7,1}, 
            	{1,8,2, 11,8,3}, 
            	{1,6,4, 11,7,4}, 
            	{2,4,5, 2,4,5}, {4,4,5, 4,4,5}, {6,4,5, 6,4,5}, {8,4,5, 8,4,5}, {10,4,5, 10,4,5}, 
            	{1,5,5, 11,5,5}, 
            	// Floor
            	{3,1,2, 9,1,3}, 
            	// Stairs
            	{5,2,2, 5,2,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	// Roof
            	{1,4,0}, {3,4,0}, {5,4,0}, {7,4,0}, {9,4,0}, {11,4,0}, 
            	{1,4,5}, {3,4,5}, {5,4,5}, {7,4,5}, {9,4,5}, {11,4,5}, 
            	// Roof side trims
            	{1,5,1}, {1,7,2}, {1,7,3}, {1,5,4}, 
            	{11,5,1}, {11,7,2}, {11,7,3}, {11,5,4}, 
            	// Floor
            	{3,4,3}, {4,4,3}, {5,4,3}, {6,4,3}, {7,4,3}, {8,4,3}, {9,4,3}, 
            	{3,4,2}, {8,4,2}, {9,4,2}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{2,6,0}, {4,6,0}, {5,6,0}, {7,6,0}, {8,6,0}, {10,6,0}, 
            	{3,8,1}, {6,8,1}, {9,8,1}, 
            	{3,8,4}, {6,8,4}, {9,8,4}, 
            	{2,6,5}, {4,6,5}, {5,6,5}, {7,6,5}, {8,6,5}, {10,6,5}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{4,1,0, 3}, {8,1,0, 3}, 
        		// Stairwell
        		{6,2,2, 1}, {5,3,2, 1}, {4,4,2, 1}, 
        		// Bench
        		{9,5,2, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		// Bottom floor
            	{3,3,4}, {5,3,4}, {7,3,4}, {9,3,4}, 
            	// Top floor
            	{3,6,1}, {6,6,1}, {9,6,1}, 
            	{3,6,4}, {6,6,4}, {9,6,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{9,5,3, 3}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{6,1,0, 6,1,0}, {10,1,0, 10,1,0}, 
            	// Back side
            	{5,1,5, 6,1,5}, {6,2,5, 6,2,5}, 
            	// Left side
            	{0,1,1, 0,1,1}, {0,1,3, 0,1,3}, {0,1,5, 0,1,5}, 
            	{1,1,0, 1,1,4}, {1,2,3, 1,2,3}, 
            	// Right side
            	{11,1,1, 11,1,4}, 
            	{11,2,2, 11,2,2}, 
            	{12,1,1, 12,1,1}, {12,1,3, 12,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,2,3, 3}, {0,2,5, 0}, 
            	{1,2,0, 0}, {1,2,1, 3}, {1,2,2, 4}, {1,2,4, 0}, {1,1,5, 3}, {1,1,6, 1}, 
            	{2,1,0, 3}, {2,1,5, 1}, {2,1,6, 0}, 
            	{3,1,0, 1}, {3,1,5, 0}, {3,1,6, 1}, 
            	{4,1,5, 4}, {4,1,6, 1}, 
            	{5,1,0, 1}, {5,2,5, 1}, {5,1,6, 3}, 
            	{6,1,6, 0}, 
            	{7,1,0, 3}, {7,1,5, 5}, {7,1,6, 2}, 
            	{8,1,5, 2}, {8,1,6, 0}, 
            	{9,1,0, 3}, {9,1,5, 0}, {9,1,6, 0}, 
            	{10,1,5, 5}, {10,1,6, 0}, 
            	{11,1,0, 3}, {11,2,1, 1}, {11,2,3, 1}, {11,2,4, 3}, {11,1,5, 1}, {11,1,6, 0}, 
            	{12,1,0, 2}, {12,1,2, 3}, {12,2,3, 0}, {12,1,4, 0}, {12,1,5, 3}, {12,1,6, 0}, 
            	{13,1,0, 0}, {13,1,1, 0}, {13,1,2, 0}, {13,1,3, 4}, {13,1,4, 0}, {13,1,5, 0}, {13,1,6, 0}, 
            	// Roof
            	{1,6,0, 0}, {1,8,1, 0}, {1,9,2, 0}, {1,9,3, 0}, {1,8,4, 0}, {1,6,5, 0}, 
            	{2,8,1, 0}, {2,9,2, 0}, {2,9,3, 0}, {2,8,4, 0},  
            	{3,6,0, 0}, {3,9,2, 0}, {3,9,3, 0}, 
            	{4,8,1, 0}, {4,9,2, 0}, {4,9,3, 0}, {4,8,4, 0}, 
            	{5,8,1, 0}, {5,9,2, 0}, {5,9,3, 0}, {5,8,4, 0}, 
            	{6,6,0, 0}, {6,9,2, 0}, {6,9,3, 0}, {6,6,5, 0}, 
            	{7,8,1, 0}, {7,9,2, 0}, {7,9,3, 0}, {7,8,4, 0}, 
            	{8,8,1, 0}, {8,9,2, 0}, {8,9,3, 0}, {8,8,4, 1}, 
            	{9,6,0, 0}, {9,9,2, 0}, {9,9,3, 0}, {9,6,5, 0}, 
            	{10,8,1, 0}, {10,9,2, 0}, {10,9,3, 0}, {10,8,4, 1}, 
            	{11,6,0, 0}, {11,8,1, 1}, {11,9,2, 0}, {11,9,3, 0}, {11,8,4, 0}, {11,6,5, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,2,2, 2, GeneralConfig.useVillageColors ? this.townColor  : 11}, // Blue
            	{9,2,2, 2, GeneralConfig.useVillageColors ? this.townColor2 : 14}, // Red
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
            	{4,2,1, 2, 1, 0}, 
            	{8,2,1, 2, 1, 1}, 
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
        		{8, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{4,2,3, -1, 0},
	        			{9,2,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowyMediumHouse3 extends StructureVillagePieces.Village
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
    	
        public SnowyMediumHouse3() {}

        public SnowyMediumHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyMediumHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyMediumHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Packed Ice
        	IBlockState biomePackedIceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PACKED_ICE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{2,1,0, 4,3,0}, 
            	// Back side
            	{2,1,4, 4,3,4}, 
            	// Left side
            	{0,1,2, 0,3,2}, {1,3,2, 1,3,2}, 
            	// Right side
            	{6,1,2, 6,3,2}, {5,3,2, 5,3,2}, 
            	// Ceiling
            	{2,3,1, 2,3,3}, {4,3,1, 4,3,3}, 
            	{3,4,2, 3,4,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePackedIceState, biomePackedIceState, false);	
            }
            
            
            // Blue Ice
            IBlockState biomeBlueIceBlockState = StructureVillageVN.getBiomeSpecificBlockState(ModObjects.chooseModBlueIceBlockState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{1,1,0, 1,2,0}, {5,1,0, 5,2,0}, 
            	// Back side
            	{1,1,4, 1,2,4}, {5,1,4, 5,2,4}, 
            	// Left side
            	{0,1,1, 0,2,1}, {0,1,3, 0,2,3}, 
            	// Right side
            	{6,1,1, 6,2,1}, {6,1,3, 6,2,3}, 
            	// Ceiling
            	{1,3,1, 1,3,1}, {1,3,3, 1,3,3}, 
            	{5,3,1, 5,3,1}, {5,3,3, 5,3,3}, 
            	{2,4,2, 2,4,2}, {4,4,2, 4,4,2}, 
            	{3,4,1, 3,4,1}, {3,4,3, 3,4,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBlueIceBlockState, biomeBlueIceBlockState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 0}, {0,1,4, 0}, 
            	{6,1,0, 0}, {6,1,4, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }

        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{1,2,2, 1}, 
            	{5,2,2, 3}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,1,2, 2, GeneralConfig.useVillageColors ? this.townColor  : 11}, // Blue
            	{5,1,2, 2, GeneralConfig.useVillageColors ? this.townColor  : 11}, // Blue
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
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,3, 2}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,0, 0, 1, 0}, 
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
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{2,1,2, -1, 0},
	        			{4,1,2, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowyShepherdsHouse1 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 5;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyShepherdsHouse1() {}

        public SnowyShepherdsHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyShepherdsHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyShepherdsHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	// Yard
            	{0,1,0, 5,1,0}, {0,1,0, 0,1,3}, 
            	// Front wall
            	{6,1,1, 6,3,3}, {7,1,1, 7,1,1}, {8,1,1, 8,3,3}, 
            	{1,1,4, 5,3,4}, 
            	// Back wall
            	{1,1,7, 8,3,7}, 
            	{7,4,7, 7,4,7}, 
            	// Left wall
            	{1,1,5, 1,4,6}, 
            	// Right wall
            	{8,1,2, 8,3,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Above front door
            	{7,4,1, 7,4,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{5,2,0, 5,3,0}, 
            	{6,4,0, 6,4,8}, 
            	{7,5,0, 7,5,8}, 
            	{8,4,0, 8,4,8}, 
            	{9,2,0, 9,3,8}, 
            	// Left roof
            	{0,2,3, 0,3,3}, 
            	{0,4,4, 5,4,4}, 
            	{0,5,5, 6,5,6}, 
            	{0,4,7, 5,4,7}, 
            	{0,2,8, 5,3,8}, 
            	// Floor
            	{2,1,5, 7,1,6}, 
            	{6,1,4, 7,1,4}, 
            	{7,1,2, 7,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{5,4,0}, {5,4,1}, {5,4,2}, {5,4,3}, {4,4,3}, {3,4,3}, {2,4,3}, {1,4,3}, {0,4,3}, 
            	{6,5,0}, {8,5,0}, 
            	{6,5,8}, {8,5,8}, 
            	{9,4,0}, 
            	{5,4,8}, {9,4,8}, 
            	{0,5,4}, {0,5,7}, {0,4,8}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{7,1,0, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Yard
            	{0,2,0, 0,2,2}, {1,2,0, 4,2,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
        	// Grass
            for(int[] uuvvww : new int[][]{
            	{1,1,1, 5,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{8,1,0, 0}, 
            	{9,1,0, 0}, {9,1,1, 0}, {9,1,2, 0}, {9,1,3, 0}, {9,1,4, 0}, {9,1,5, 0}, {9,1,6, 0}, {9,1,7, 0}, {9,1,8, 0}, 
            	{0,1,8, 0}, {1,1,8, 0}, {2,1,8, 0}, {3,1,8, 0}, {4,1,8, 0}, {5,1,8, 0}, {6,1,8, 0}, {7,1,8, 0}, 
            	{0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{8,1,8, 0}, 
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
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{7,3,5, 3}, 
            	{3,3,6, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{7,4,0}, 
            	// Yard
            	{5,3,2}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Lantern (Sitting)
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{0,3,0}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 7;
        	int chestV = 2;
        	int chestW = 6;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_shepherd");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
        	
            // Loom
        	IBlockState loomState = ModObjects.chooseModLoom(1, this.getCoordBaseMode());
            for(int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,5}, {2,2,6}, 
            	})
            {
            	this.setBlockState(world, loomState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,4, 2, 1, 0}, 
            	{7,2,1, 2, 1, 1}, 
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
        		{6, GROUND_LEVEL, -1}, 
        		{7, GROUND_LEVEL, -1}, 
        		{8, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 3;
            	int v = 2;
            	int w = 6;
            	
            	int s = random.nextInt(4+5+2+2);
            	
            	if (s<=3) {u=s+3; w=6;}
            	else if (s<=8) {u=s-1; w=5;}
            	else if (s<=10) {u=s-3; w=4;}
            	else if (s<=12) {u=7; w=s-9;}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 3, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);

                // Sheep in the yard
            	for (int[] uvw : new int[][]{
        			{2, 3, 1},
        			})
        		{
            		EntityLiving animal = new EntitySheep(world);
            		IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
            		
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(animal);
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
    
    public static class SnowySmallHouse1 extends StructureVillagePieces.Village
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
    	
        public SnowySmallHouse1() {}

        public SnowySmallHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowySmallHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowySmallHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	{2,1,4, 2,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,2,4, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Packed Ice
        	IBlockState biomePackedIceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PACKED_ICE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{2,1,1, 4,3,1}, 
            	// Back side
            	{2,1,5, 4,3,5}, 
            	// Left side
            	{1,1,2, 1,3,4}, 
            	// Right side
            	{5,1,2, 5,3,4}, 
            	// Ceiling
            	{2,4,2, 4,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePackedIceState, biomePackedIceState, false);	
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{5,1,1, 5,1,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,1, 2}, {0,1,2, 4}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, 
            	{1,1,0, 0}, {1,1,1, 4}, {1,1,5, 0}, 
            	{2,1,0, 1}, 
            	{4,1,0, 5}, 
            	{5,1,0, 2}, {5,2,1, 1}, {5,1,5, 0}, 
            	{6,1,0, 0}, {6,1,1, 4}, {6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
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
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,4, 1, GeneralConfig.useVillageColors ? this.townColor  : 11}, // Blue
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
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
            	{1,2,3}, {5,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
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
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowySmallHouse2 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowySmallHouse2() {}

        public SnowySmallHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowySmallHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowySmallHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            

        	// TODO - stripped wood
        	// For Stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,1, 2,3,1}, {4,1,1, 5,3,1}, 
            	{2,4,1, 4,5,1}, 
            	{3,6,1, 3,6,1}, 
            	// Back wall
            	{1,1,5, 5,3,5}, 
            	{2,4,5, 4,5,5}, 
            	{3,6,5, 3,6,5}, 
            	// Left wall
            	{1,1,2, 1,3,4}, 
            	// Right wall
            	{5,1,2, 5,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Below front door
            	{3,1,1, 3,1,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{3,4,0, 2}, 
            	// Interior
            	{3,5,2, 0}, 
            	{3,5,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,3,0, 0,3,6}, 
            	{0,2,1, 0,2,1}, {0,2,3, 0,2,3}, {0,2,5, 0,2,5}, 
            	{1,4,0, 1,5,6}, 
            	{2,6,0, 2,6,3}, {2,6,5, 2,6,6}, 
            	{3,7,0, 3,7,6}, 
            	{4,6,0, 4,6,6}, 
            	{5,4,0, 5,5,6}, 
            	{6,2,1, 6,2,1}, {6,2,3, 6,2,3}, {6,2,5, 6,2,5}, 
            	{6,3,0, 6,3,6}, 
            	// Floor
            	{2,1,2, 4,1,4}, 
           	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,4,0}, {0,4,2}, {0,4,4}, {0,4,6}, 
            	{1,6,3}, 
            	{5,6,3}, 
            	{6,4,0}, {6,4,2}, {6,4,4}, {6,4,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,2,0}, {0,2,2}, {0,2,4}, {0,2,6}, 
            	{6,2,0}, {6,2,2}, {6,2,4}, {6,2,6}, 
            	// Roof trim
            	{2,5,0}, {3,6,0}, {4,5,0}, 
            	{2,5,6}, {3,6,6}, {4,5,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,1,0, 0}, 
        		{3,1,0, 3}, 
        		{4,1,0, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, 
            	{1,1,0, 0}, {1,1,6, 0}, 
            	{2,1,6, 0}, 
            	{3,1,6, 0}, 
            	{4,1,6, 0}, 
            	{5,1,0, 0}, {5,1,6, 0}, 
            	{6,1,0, 0}, {6,1,1, 0}, {6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, {6,1,6, 0}, 
            	// Roof
            	{0,4,1, 0}, {0,4,3, 0}, {0,4,5, 0}, 
            	{1,6,0, 0}, {1,6,1, 0}, {1,6,2, 0}, {1,6,5, 0}, {1,6,6, 0}, 
            	{2,7,0, 0}, {2,7,1, 0}, {2,7,2, 0}, {2,7,3, 0}, {2,7,5, 0}, {2,7,6, 0}, 
            	{3,8,0, 0}, {3,8,1, 0}, {3,8,2, 0}, {3,8,3, 0}, {3,8,4, 0}, {3,8,5, 0}, {3,8,6, 0}, 
            	{4,7,0, 0}, {4,7,1, 0}, {4,7,2, 0}, {4,7,3, 0}, {4,7,4, 0}, {4,7,5, 0}, {4,7,6, 0}, 
            	{5,6,0, 0}, {5,6,1, 0}, {5,6,2, 0}, {5,6,4, 0}, {5,6,5, 0}, {5,6,6, 0}, 
            	{6,4,1, 0}, {6,4,3, 0}, {6,4,5, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,2,3, 2, GeneralConfig.useVillageColors ? this.townColor  : 11}, // Blue
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
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{1,4,3}, 
        		{3,4,5}, 
        		{5,4,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,1,3, 2,1,3}, 
            	// Ceiling
            	{2,6,4, 2,6,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,2,4, 2}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Lower chimney
            	{2,3,4, 2,5,4}, 
            	// Upper chimney
            	{2,7,4, 2,8,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,1, 2, 1, 1}, 
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
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{3,2,3, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowySmallHouse3 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowySmallHouse3() {}

        public SnowySmallHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowySmallHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowySmallHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            

        	// TODO - stripped wood
        	// For Stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,2, 1,3,2}, {4,1,2, 4,3,2}, {2,4,2, 3,5,2}, 
            	// Back wall
            	{1,1,5, 4,2,5}, {2,3,5, 3,4,5}, 
            	// Left wall
            	// Right wall
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
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
            	// Front door
            	{2,1,2, 3,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{2,4,3, 0}, 
            	{3,4,3, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,1,1, 0,2,6}, 
            	{1,4,1, 1,4,6}, 
            	{2,5,3, 3,5,6}, 
            	{4,4,1, 4,4,6}, 
            	{5,1,1, 5,2,6}, 
            	// Interior walls
            	{1,1,3, 1,3,4}, {1,3,5, 1,3,6}, 
            	{4,1,3, 4,1,4}, {4,3,3, 4,3,4}, {4,3,5, 4,3,6}, 
            	// Floor
            	{2,1,3, 3,1,4}, 
           	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{2,6,1}, {2,6,2}, {2,6,3}, {2,6,4}, {2,6,5}, {2,6,6}, 
            	{3,6,1}, {3,6,2}, {3,6,3}, {3,6,4}, {3,6,5}, {3,6,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,1,1, 3}, {3,1,1, 3}, 
        		// Roof trim
        		{0,3,1, 0}, {1,3,1, 5}, 
        		{1,5,1, 0}, {2,5,1, 5}, 
        		{3,5,1, 4}, {4,5,1, 1}, 
        		{4,3,1, 4}, {5,3,1, 1}, 
        		{0,3,2, 0}, 
        		{1,5,2, 0}, 
        		{4,5,2, 1}, 
        		{5,3,2, 1}, 
        		{0,3,5, 0}, 
        		{1,5,5, 0}, 
        		{4,5,5, 1}, 
        		{5,3,5, 1}, 
        		{0,3,6, 0}, 
        		{1,5,6, 0}, {2,5,6, 5}, 
        		{3,5,6, 4}, {4,5,6, 1}, 
        		{5,3,6, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Front Entrance
            	{1,4,0, 1,4,0}, 
            	{4,4,0, 4,4,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{1,3,0}, 
            	{4,3,0}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{5,1,0, 5,1,0}, 
            	{4,1,1, 6,1,1}, 
            	{6,1,2, 6,2,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{4,2,1, 3}, 
            	{5,2,0, 3}, 
            	{6,2,1, 0}, {6,3,2, 0}, 
            	// Roof
            	{0,3,3, 0}, {0,3,4, 0}, 
            	{1,5,3, 0}, {1,5,4, 0}, 
            	{4,5,3, 0}, {4,5,4, 3}, 
            	{5,3,3, 3}, {5,3,4, 1}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,3, 2, GeneralConfig.useVillageColors ? this.townColor  : 11}, // Blue
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
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,3, 3}, 
            	{4,2,4, 3}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,2,2, 2, 1, 1}, 
            	{3,2,2, 2, 1, 0}, 
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
        		{1, GROUND_LEVEL, -1}, 
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{3,2,4, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowySmallHouse4 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        		"FFFFFFF",
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
    	
        public SnowySmallHouse4() {}

        public SnowySmallHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowySmallHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowySmallHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Snow Blocks, part 1
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{1,1,2, 1,1,2}, 
            	{2,1,1, 4,3,1}, 
            	{5,1,2, 5,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{2,2,2, 0}, 
            	{4,2,2, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{3,2,6}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Snow Blocks, part 2
            for(int[] uuvvww : new int[][]{
            	// Back side
            	{2,1,7, 3,1,7}, {3,2,7, 3,2,7}, 
            	// Left side
            	{2,3,2, 2,3,4}, 
            	{2,1,5, 2,2,6}, 
            	{1,1,2, 1,2,4}, 
            	{0,1,3, 0,1,3}, 
            	// Right side
            	{4,3,2, 4,3,4}, 
            	{4,1,5, 4,2,6}, 
            	{5,1,1, 5,2,4}, 
            	// Roof
            	{3,3,1, 3,3,1}, {3,3,2, 3,4,4}, 
            	{3,3,5, 3,3,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 5}, {0,1,4, 4}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, 
            	{1,1,0, 0}, {1,1,5, 6}, {1,1,6, 3}, {1,1,7, 0}, 
            	{2,1,0, 3}, {2,4,3, 2}, {2,4,4, 0}, {2,3,5, 0}, {2,3,6, 0}, 
            	{3,4,6, 0}, {3,3,7, 0}, 
            	{4,4,1, 0}, {4,3,5, 0}, {4,3,6, 0}, {4,1,7, 0}, 
            	{5,1,0, 0}, {5,2,1, 1}, {5,3,2, 0}, {5,3,3, 0}, {5,3,4, 0}, {5,1,5, 0}, {5,1,6, 5}, {5,1,7, 0}, 
            	{6,1,0, 0}, {6,1,1, 0}, {6,1,2, 0}, {6,1,3, 4}, {6,1,4, 0}, {6,1,5, 0}, {6,1,6, 0}, {6,1,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Interior
        		{4,1,2, 0}, 
        		{4,1,4, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,3, 3}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3, 2, GeneralConfig.useVillageColors ? this.townColor3 : 0}, // White
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
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{3,1,4, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowySmallHouse5 extends StructureVillagePieces.Village
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
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowySmallHouse5() {}

        public SnowySmallHouse5(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowySmallHouse5 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowySmallHouse5(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{2,3,0, 2,3,0}, 
            	// Back side
            	{2,3,5, 2,3,5}, 
            	// Left side
            	{0,1,1, 0,2,4}, {0,3,2, 0,3,3}, 
            	// Right side
            	{4,1,1, 4,2,4}, {4,3,2, 4,3,3}, 
            	// Roof
            	{2,4,4, 2,4,4}, 
            	{1,4,2, 3,4,3}, 
            	{2,4,1, 2,4,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Blue Ice, part 1
            IBlockState biomeBlueIceBlockState = StructureVillageVN.getBiomeSpecificBlockState(ModObjects.chooseModBlueIceBlockState(), this.materialType, this.biome, this.disallowModSubs);
            
            for(int[] uuvvww : new int[][]{
            	{2,1,6, 2,2,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBlueIceBlockState, biomeBlueIceBlockState, false);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{2,3,1, 0}, 
            	{2,2,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Blue Ice, part 2
            for(int[] uuvvww : new int[][]{
            	{1,1,0, 1,2,0}, {1,3,1, 1,3,1}, 
            	{3,1,0, 3,2,0}, {3,3,1, 3,3,1}, 
            	{1,1,5, 1,2,5}, {1,3,4, 1,3,4}, 
            	{3,1,5, 3,2,5}, {3,3,4, 3,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeBlueIceBlockState, biomeBlueIceBlockState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	{0,1,5, 0}, {0,1,6, 0}, {1,1,6, 0}, 
            	{4,1,5, 0}, {4,1,6, 0}, {3,1,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{1,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 14}, // Red
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
            int chestU = 3;
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
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,1,0, 0, 1, 0}, 
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
        		{2, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{2,1,2, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowySmallHouse6 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
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
    	
        public SnowySmallHouse6() {}

        public SnowySmallHouse6(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowySmallHouse6 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowySmallHouse6(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	// Front wall
            	{1,1,1, 1,3,2}, 
            	{2,5,1, 2,5,1}, 
            	{3,6,1, 3,6,1}, 
            	{4,5,1, 4,5,1}, 
            	{5,1,1, 5,3,2}, 
            	// Back wall
            	{1,1,4, 1,3,5}, 
            	{2,1,5, 2,2,5}, {2,4,5, 2,5,5}, 
            	{3,1,5, 3,1,5}, {3,6,5, 3,6,5}, 
            	{4,1,5, 4,2,5}, {4,4,5, 4,5,5}, 
            	{5,1,4, 5,3,5}, 
            	// Floor
            	{3,1,1, 3,1,2}, 
            	{1,1,3, 2,1,3}, {4,1,3, 5,1,3}, 
            	{3,1,4, 3,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Interior walls
            	{1,2,3, 1,3,3}, 
            	{5,2,3, 5,3,3}, 
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
            	// Front wall
            	{2,1,1, 2,4,1}, {3,4,1, 3,5,1}, {4,1,1, 4,4,1}, 
            	// Back wall
            	{3,2,5, 3,2,5}, {2,3,5, 2,3,5}, {4,3,5, 4,3,5}, {3,4,5, 3,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{3,5,2, 0}, 
            	{3,5,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{3,6,0}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,2,0, 0,3,6}, 
            	{1,4,0, 1,5,6}, 
            	{2,6,0, 2,6,1}, {2,6,3, 2,6,6}, 
            	{3,7,0, 3,7,6}, 
            	{4,6,0, 4,6,6}, 
            	{5,4,0, 5,5,6}, 
            	{6,2,0, 6,3,6}, 
            	// Floor
            	{2,1,2, 2,1,2}, {2,1,4, 2,1,4}, 
            	{3,1,3, 3,1,3}, 
            	{4,1,2, 4,1,2}, {4,1,4, 4,1,4}, 
           	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,1,0, 0}, 
        		{3,1,0, 3}, 
        		{4,1,0, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, 
            	{1,1,0, 0}, {1,1,6, 0}, 
            	{2,1,6, 0}, 
            	{4,1,6, 0}, 
            	{5,1,0, 0}, {5,1,6, 0}, 
            	{6,1,0, 0}, {6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, {6,1,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{3,1,6, 0}, 
            	{6,1,1, 0}, 
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
            	{4,2,3, 2, GeneralConfig.useVillageColors ? this.townColor  : 11}, // Blue
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
        	int chestV = 2;
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
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{3,3,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Ceiling
            	{2,6,2, 2,6,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,2,2, 1}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Lower chimney
            	{2,3,2, 2,5,2}, 
            	// Upper chimney
            	{2,7,2, 2,8,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,1, 2, 1, 1}, 
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
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{3,2,3, -1, 0}, 
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowySmallHouse7 extends StructureVillagePieces.Village
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
    	
        public SnowySmallHouse7() {}

        public SnowySmallHouse7(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowySmallHouse7 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowySmallHouse7(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	// Front wall
            	{1,1,1, 2,3,1}, 
            	{3,1,1, 3,1,1}, 
            	{4,1,1, 5,3,1}, 
            	{2,4,1, 4,4,1}, {3,5,1, 3,5,1}, 
            	// Back wall
            	{1,1,4, 1,3,4}, 
            	{2,1,4, 2,2,4}, {2,4,4, 2,4,4}, 
            	{3,1,4, 3,1,4}, {3,5,4, 3,5,4}, 
            	{4,1,4, 4,2,4}, {4,4,4, 4,4,4}, 
            	{5,1,4, 5,3,4}, 
            	// Left side
            	{1,1,2, 1,3,3}, 
            	// Right side
            	{5,1,2, 5,3,3}, 
            	// Floor
            	{2,1,2, 4,1,3}, 
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
            	// Back wall
            	{3,2,4, 3,2,4}, {2,3,4, 2,3,4}, {4,3,4, 4,3,4}, {3,4,4, 3,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{3,5,3}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Front Entrance
            	{1,1,0, 1,1,0}, 
            	{5,1,0, 5,1,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern (Sitting)
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{1,2,0}, {5,2,0}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,3,0, 0,3,5}, 
            	{1,4,0, 1,4,5}, 
            	{2,5,0, 2,5,5}, 
            	{3,6,0, 3,6,5}, 
            	{4,5,0, 4,5,5}, 
            	{5,4,0, 5,4,5}, 
            	{6,3,0, 6,3,5}, 
           	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,4,0}, {0,4,2}, {0,4,3}, {0,4,5}, 
            	{1,5,0}, {1,5,2}, {1,5,3}, {1,5,5}, 
            	{2,6,0}, {2,6,2}, {2,6,3}, {2,6,5}, 
            	{3,7,0}, {3,7,2}, {3,7,3}, {3,7,5}, 
            	{4,6,0}, {4,6,2}, {4,6,3}, {4,6,5}, 
            	{5,5,0}, {5,5,2}, {5,5,3}, {5,5,5}, 
            	{6,4,0}, {6,4,2}, {6,4,3}, {6,4,5}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,2,0}, {0,2,2}, {0,2,3}, {0,2,5}, 
            	{6,2,0}, {6,2,2}, {6,2,3}, {6,2,5}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{2,1,0, 0}, 
        		{3,1,0, 3}, 
        		{4,1,0, 1}, 
        		// Bench
        		{4,2,2, 0}, 
        		{4,2,3, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, 
            	{1,1,5, 0}, 
            	{2,1,5, 0}, 
            	{4,1,5, 0}, 
            	{5,1,5, 0}, 
            	{6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,2, 2, GeneralConfig.useVillageColors ? this.townColor3 : 0}, // White
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
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
        		{3,3,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,1, 2, 1, 1}, 
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
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
        		{4, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{3,2,2, -1, 0}, 
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowySmallHouse8 extends StructureVillagePieces.Village
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
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowySmallHouse8() {}

        public SnowySmallHouse8(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowySmallHouse8 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowySmallHouse8(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	// Wool
        	for(int[] uvwm : new int[][]{
        		{2,0,0}, 
        		{1,0,1}, {2,0,1}, {3,0,1}, 
        		{1,0,2}, {2,0,2}, {3,0,2}, 
        		{1,0,3}, {2,0,3}, {3,0,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.WOOL.getStateFromMeta( 
        				(GeneralConfig.useVillageColors ? this.townColor4 : 8)), // Light Gray
        				uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	// Table
            	{1,1,2, 1,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{1,2,2, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{1,1,0, 3,3,0}, 
            	// Back side
            	{1,1,4, 3,3,4}, {2,3,4, 2,3,4}, {2,1,5, 2,2,5}, 
            	// Left wall
            	{0,1,1, 0,2,3}, 
            	// Right wall
            	{4,1,1, 4,2,3}, 
            	// Ceiling
            	{1,3,1, 3,3,3}, {2,4,1, 2,4,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chairs
        		{1,1,1, 2}, 
        		{1,1,3, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,2, 2, GeneralConfig.useVillageColors ? this.townColor3 : 0}, // White
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
            	{2,1,0, 2, 1, 0}, 
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
        		{2, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
	        			{2,1,2, -1, 0},
	        			};
	        		int countdownToAdult = 1+random.nextInt(villagerPositions.length); // One of the villagers here is an adult
	            	
	        		for (int[] ia :villagerPositions)
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], (--countdownToAdult)==0?0:Math.min(random.nextInt(24001)-12000,0));
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 1.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
	                    		random.nextFloat()*360F, 0.0F);
	                    world.spawnEntity(entityvillager);
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
    
    public static class SnowyTannery1 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
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
    	
        public SnowyTannery1() {}

        public SnowyTannery1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyTannery1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyTannery1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	// Front wall
            	{1,2,1, 1,3,1}, 
            	{2,1,1, 2,1,1}, {2,3,1, 2,5,1}, 
            	{3,1,1, 3,2,1}, {3,5,1, 3,5,1}, 
            	{4,1,1, 4,1,1}, 
            	{5,1,1, 5,2,1}, {5,5,1, 5,5,1}, 
            	{6,1,1, 6,1,1}, {6,3,1, 6,5,1}, 
            	{7,2,1, 7,3,1}, 
            	// Back wall
            	{1,1,6, 1,3,6}, 
            	{2,1,6, 2,3,6}, {2,5,6, 2,5,6}, 
            	{3,1,6, 3,2,6}, {3,6,6, 3,6,6}, 
            	{4,1,6, 4,3,6}, {4,5,6, 4,7,6}, 
            	{5,1,6, 5,2,6}, {5,6,6, 5,6,6}, 
            	{6,1,6, 6,3,6}, {6,5,6, 6,5,6}, 
            	{7,1,6, 7,3,6}, 
            	// Left wall
            	{1,1,2, 1,3,5}, 
            	// Right wall
            	{7,1,2, 7,3,5}, 
            	// Floor
            	{2,1,2, 2,1,5}, 
            	{3,1,2, 5,1,2}, 
            	{6,1,2, 6,1,5}, 
            	// Behind the furnace
            	{4,2,5, 4,2,5}, 
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
            	// Front wall
            	{1,1,1, 1,1,1}, 
            	{2,2,1, 2,2,1}, 
            	{3,3,1, 3,3,1}, 
            	{3,4,1, 5,4,1}, 
            	{4,5,1, 4,5,1}, 
            	{3,6,1, 5,6,1}, 
            	{4,7,1, 4,7,1}, 
            	{5,3,1, 5,3,1}, 
            	{6,2,1, 6,2,1}, 
            	{7,1,1, 7,1,1}, 
            	// Back wall
            	{3,3,6, 3,3,6}, {5,3,6, 5,3,6}, 
            	{2,4,6, 2,4,6}, {4,4,6, 4,4,6}, {6,4,6, 6,4,6}, 
            	{3,5,6, 3,5,6}, {5,5,6, 5,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,2,1, 0,2,2}, {0,2,5, 0,2,6}, 
            	{0,3,0, 0,3,7}, 
            	{1,4,0, 1,5,7}, 
            	{2,6,0, 2,6,7}, 
            	{3,7,0, 3,7,7}, 
            	{4,8,0, 4,8,7}, 
            	{5,7,0, 5,7,7}, 
            	{6,6,0, 6,6,7}, 
            	{7,4,0, 7,5,7}, 
            	{8,3,0, 8,3,7}, 
            	{8,2,1, 8,2,2}, {8,2,5, 8,2,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	// Front edge
            	{0,4,0}, {2,7,0}, {3,8,0}, {5,8,0}, {6,7,0}, {8,4,0}, 
            	// Back edge
            	{0,4,7}, {2,7,7}, {3,8,7}, {5,8,7}, {6,7,7}, {8,4,7}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,2,0}, {0,2,3}, {0,2,4}, {0,2,7}, 
            	{2,5,0}, 
            	{6,5,0}, 
            	{8,2,0}, {8,2,3}, {8,2,4}, {8,2,7}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Glass Panes
        	for (int[] uvw : new int[][]{
            	{3,4,6}, {5,4,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{4,1,0, 3}, 
        		// Bench
        		{2,2,4, 1}, {2,2,5, 1}, 
        		// Chimney trim
        		{3,8,3, 3}, {3,8,4, 0}, {3,8,5, 2}, 
        		{5,8,3, 3}, {5,8,4, 1}, {5,8,5, 2}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{4,5,2, 0}, 
            	{4,5,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	IBlockState dioriteBlockState = Blocks.STONE.getStateFromMeta(3);
        	Block dioriteStairsBlock = ModObjects.chooseModDioriteStairsBlock();
        	IBlockState dioriteWallState = ModObjects.chooseModDioriteWallState();
        	if (dioriteStairsBlock==null || dioriteWallState==null)
        	{
        		dioriteBlockState = Blocks.COBBLESTONE.getStateFromMeta(0);
            	dioriteStairsBlock = Blocks.STONE_STAIRS;
            	dioriteWallState = Blocks.COBBLESTONE_WALL.getDefaultState();
        	}
        	
        	// Diorite block
        	for(int[] uuvvww : new int[][]{
            	// Furnace base
        		{3,1,3, 5,1,5}, 
        		// Chimney
        		{4,8,4, 4,8,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], dioriteBlockState, dioriteBlockState, false);	
            }
        	// Diorite wall
        	for(int[] uuvvww : new int[][]{
            	// Chimney
        		{4,3,4, 4,7,4}, {4,9,4, 4,9,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], dioriteWallState, dioriteWallState, false);	
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,4, 2}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Lantern post
            	{4,5,0, 4,5,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{4,4,0}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 6;
        	int chestV = 2;
        	int chestW = 5;
        	int chestO = 2; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_tannery");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
        	
            // Cauldron
        	for (int[] uuvvww : new int[][]{
        		{2,2,2}, {6,2,2}, 
        		})
            {
        		this.setBlockState(world, Blocks.CAULDRON.getStateFromMeta(3), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{0,1,0, 0,1,0}, 
            	{0,1,4, 0,1,4}, 
            	{5,1,0, 5,1,0}, 
            	{6,1,0, 6,2,0}, 
            	// Back side
            	// Left side
            	// Right side
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,1, 4}, {0,1,2, 0}, {0,1,3, 3}, {0,1,5, 3}, {0,1,6, 0}, {0,1,7, 0}, 
            	{1,1,0, 4}, {1,1,7, 0}, 
            	{2,1,0, 0}, {2,1,7, 0}, 
            	{3,1,0, 4}, 
            	{5,1,7, 0}, 
            	{6,1,7, 0}, 
            	{7,1,0, 5}, {7,1,7, 0}, 
            	{8,1,0, 0}, {8,1,1, 0}, {8,1,2, 0}, {8,1,3, 0}, {8,1,4, 0}, {8,1,5, 0}, {8,1,6, 0}, {8,1,7, 0}, 
            	// Roof
            	{0,4,1, 7}, {0,4,2, 0}, {0,4,4, 3}, 
            	{1,6,0, 2}, 
            	{2,7,2, 3}, 
            	{7,6,1, 3}, {7,6,2, 7}, {7,6,3, 1}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
            
        	
            // Tall Grass
            for (int[] uvwg : new int[][]{ // g is grass type
            	{3,1,7, 0}, 
            	{4,1,7, 0}, 
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
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int w = 4;
            	
            	while (u==4 && (w==4 || w==5))
            	{
            		u = 3 + random.nextInt(3);
            		w = 2 + random.nextInt(4);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 4, 2, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    public static class SnowyTemple1 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 15;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyTemple1() {}

        public SnowyTemple1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyTemple1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyTemple1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            

        	// TODO - stripped wood
        	// For Stripped wood specifically
        	IBlockState biomeStrippedWoodOrLogOrLogVerticState = biomeLogVertState;
        	
        	// Try to see if stripped wood exists
        	biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState, 0);
        	
        	// If it doesn't exist, try stripped logs
        	if (biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG || biomeStrippedWoodOrLogOrLogVerticState.getBlock()==Blocks.LOG2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.LOG)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
            	{
            		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            	}
        	}
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,1, 1,3,1}, 
            	{2,1,1, 2,5,1}, 
            	{3,4,1, 3,6,1}, 
            	{4,1,1, 4,5,1}, 
            	{5,1,1, 5,3,1}, 
            	// Back wall
            	{1,1,7, 1,3,7}, 
            	{2,1,7, 4,5,7}, 
            	{3,6,7, 3,6,7}, 
            	{5,1,7, 5,3,7}, 
            	// Left wall
            	{1,1,2, 1,1,6}, 
            	// Right wall
            	{5,1,2, 5,1,6}, 
            	// Spire
            	{2,6,3, 2,11,3}, {4,6,3, 4,11,3}, 
            	{2,6,5, 2,11,5}, {4,6,5, 4,11,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedWoodOrLogOrLogVerticState, biomeStrippedWoodOrLogOrLogVerticState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	{3,1,1, 3,1,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAcrossState, biomeStrippedLogHorizAcrossState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,2,0, 0,3,8}, 
            	{1,4,0, 1,5,8}, 
            	{2,6,0, 2,6,2}, {2,6,4, 2,6,4}, {2,6,6, 2,6,8}, 
            	{3,7,0, 3,7,8}, 
            	{4,6,0, 4,6,2}, {4,6,4, 4,6,4}, {4,6,6, 4,6,8}, 
            	{5,4,0, 5,5,8}, 
            	{6,2,0, 6,3,8}, 
            	// Belltower pavilion
            	{1,11,2, 5,11,2}, 
            	{1,11,3, 1,11,5}, {5,11,3, 5,11,5}, 
            	{1,11,6, 5,11,6}, 
            	{2,12,3, 4,12,5}, 
            	{3,13,4, 3,13,4}, 
            	// Floor
            	{2,1,2, 4,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Bench
        		{1,2,2, 1}, {1,2,3, 1}, {1,2,4, 1}, {1,2,5, 1}, {1,2,6, 1}, 
        		{5,2,2, 0}, {5,2,3, 0}, {5,2,4, 0}, {5,2,5, 0}, {5,2,6, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{3,4,0, 2}, 
            	// Roof
            	{3,8,4, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Lantern post
            	{3,6,2, 3,6,2}, 
            	{3,6,6, 3,6,6}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,2}, 
            	{3,5,6}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
            // Brewing Stand
        	for (int[] uvw : new int[][]{
        		{3,2,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.BREWING_STAND.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Snow Blocks
        	IBlockState biomeSnowState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front side
            	{2,1,0, 2,1,0}, 
            	{5,1,0, 5,1,0}, 
            	// Back side
            	{1,1,9, 2,1,9}, 
            	{2,1,8, 4,2,8}, {3,3,8, 3,3,8}, 
            	{4,1,9, 4,1,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSnowState, biomeSnowState, false);	
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 2}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, {0,1,8, 0}, {0,1,9, 0}, 
            	{1,1,0, 4}, {1,1,8, 6}, {1,2,9, 0}, 
            	{2,2,0, 4}, {2,3,8, 0}, {2,2,9, 1}, 
            	{3,1,9, 3}, 
            	{4,1,0, 5}, {4,2,9, 0}, 
            	{5,1,8, 5}, {5,1,9, 1}, 
            	{6,1,0, 4}, {6,1,1, 3}, {6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, {6,1,8, 3}, {6,1,9, 0}, 
            	// Roof
            	{0,4,0, 2}, {0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, {0,4,6, 0}, {0,4,7, 0}, {0,4,8, 0}, 
            	{1,6,0, 0}, {1,6,1, 2}, {1,6,5, 2}, {1,6,6, 2}, {1,6,7, 2}, {1,6,8, 2}, 
            	{2,7,0, 1}, {2,7,1, 4}, {2,7,2, 2}, {2,7,7, 0}, {2,7,8, 0}, 
            	{3,8,0, 1}, {3,8,1, 0}, 
            	{4,7,0, 0}, {4,7,1, 1}, {4,7,2, 0}, {4,7,7, 3}, {4,7,8, 1}, 
            	{5,6,0, 0}, {5,6,1, 0}, {5,6,3, 0}, {5,6,4, 2}, {5,6,7, 1}, {5,6,8, 2}, 
            	{6,4,0, 0}, {6,4,1, 0}, {6,4,2, 0}, {6,4,3, 0}, {6,4,4, 0}, {6,4,5, 3}, {6,4,6, 1}, {6,4,7, 0}, {6,4,8, 0}, 
            	// Pavilion
            	{1,12,2, 0}, {1,12,3, 0}, {1,12,4, 0}, {1,12,5, 0}, {1,12,6, 0}, 
            	{2,12,2, 0}, {2,13,3, 0}, {2,13,4, 0}, {2,13,5, 0}, {2,12,6, 0}, 
            	{3,12,2, 0}, {3,13,3, 0}, {3,14,4, 0}, {3,13,5, 0}, {3,12,6, 0}, 
            	{4,12,2, 0}, {4,13,3, 0}, {4,13,4, 0}, {4,13,5, 0}, {4,12,6, 0}, 
            	{5,12,2, 0}, {5,12,3, 0}, {5,12,4, 0}, {5,12,5, 0}, {5,12,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 3;
            	int v = 2;
            	int w = 6;
            	
            	while (u==4 && (w==3 || w==6))
            	{
            		u = 2 + random.nextInt(3);
            		w = 2 + random.nextInt(5);
            	}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 2, 1, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Tool Smith --- //
    
    public static class SnowyToolSmith1 extends StructureVillagePieces.Village
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
        		"FFFFFFF",
        		"FFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyToolSmith1() {}

        public SnowyToolSmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyToolSmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyToolSmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	// Front wall
            	{1,1,1, 1,4,1}, {2,1,1, 2,1,1}, {2,5,1, 2,6,1}, {3,1,1, 3,4,1}, 
            	// Back wall
            	{2,1,6, 4,3,6}, {5,1,6, 5,4,6}, {1,1,6, 1,4,6}, 
            	{2,5,6, 4,6,6}, 
            	{3,7,6, 3,7,6}, 
            	// Left wall
            	{1,1,2, 1,3,5}, 
            	// Right wall
            	{3,1,2, 3,3,3}, 
            	{4,1,3, 5,3,3}, 
            	{5,1,4, 5,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Front wall
            	{4,4,3, 4,4,3}, 
            	// Left wall
            	{1,4,2, 1,4,2}, {1,4,4, 1,4,4}, 
            	// Right wall
            	{3,4,2, 3,4,2}, {5,4,4, 5,4,4}, 
            	// Back wall
            	{3,4,6, 3,4,6}, 
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
            	// Front wall
            	{2,4,1, 2,4,1}, 
            	// Back wall
            	{2,4,6, 2,4,6}, {4,4,6, 4,4,6}, 
            	// Left wall
            	{1,4,3, 1,4,3}, {1,4,5, 1,4,5}, 
            	// Right wall
            	{3,4,3, 3,4,3}, {5,4,5, 5,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,3,0, 0,4,7}, 
            	{1,5,0, 1,6,7}, 
            	{2,7,0, 2,7,7}, 
            	{3,7,4, 4,7,5}, {4,7,6, 4,7,7}, 
            	{3,8,5, 3,8,7}, 
            	{3,5,0, 3,6,3}, {4,5,3, 5,6,3}, 
            	{5,5,4, 5,6,7}, 
            	{4,3,0, 4,4,2}, {5,3,2, 6,4,2}, {6,3,3, 6,4,7}, 
            	// Floor
            	{2,1,2, 2,1,3}, {2,1,4, 4,1,5}, {4,2,4, 4,2,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,5,0}, {0,5,1}, {0,5,2}, {0,5,3}, {0,5,4}, {0,5,5}, {0,5,6}, {0,5,7}, 
            	{1,7,0}, {1,7,1}, {1,7,2}, {1,7,3}, {1,7,4}, {1,7,5}, {1,7,6}, {1,7,7}, 
            	{3,7,0}, {3,7,1}, {5,7,3}, {5,7,4}, {5,7,5}, {5,7,6}, {5,7,7}, 
            	{4,5,0}, {6,5,2}, {6,5,3}, {6,5,4}, {6,5,5}, {6,5,6}, {6,5,7}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{3,7,7},  
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entry
        		{1,1,0, 0}, {2,1,0, 3}, {3,1,0, 1}, 
        		// Roof
        		{3,7,2, 1}, {3,7,3, 1}, {4,7,3, 3}, 
        		{4,5,1, 1}, {4,5,2, 1}, {5,5,2, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,6,5}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Lantern (Sitting)
        	IBlockState biomeSittingLanternState = ModObjects.chooseModLanternBlockState(false);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{4,3,4}, 
            	}) {
            	this.setBlockState(world, biomeSittingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Smithing table
        	IBlockState smithingTableBlockState = ModObjects.chooseModSmithingTable(3, this.getCoordBaseMode());
        	for (int[] uvw : new int[][]{
        		{4,2,5}, 
        		})
            {
        		this.setBlockState(world, smithingTableBlockState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{2,2,1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, 
            	{1,1,7, 0}, 
            	{2,1,7, 0}, 
            	{3,1,7, 0}, 
            	{4,1,1, 0}, {4,1,2, 0}, 
            	{5,1,0, 0}, {5,1,1, 0}, {5,1,2, 0}, {5,1,7, 0}, 
            	{6,1,0, 0}, {6,1,1, 0}, {6,1,2, 0}, {6,1,3, 0}, {6,1,4, 0}, {6,1,5, 0}, {6,1,6, 0}, {6,1,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{1, GROUND_LEVEL, -1}, 
        		{2, GROUND_LEVEL, -1}, 
        		{3, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 2;
            	int v = 2;
            	int w = 5;
            	
            	int s = random.nextInt(6);
            	
            	if (s<=3) {u=2; w=s+2;}
            	else {u=3; w=s;}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 3, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    
    // --- Weapon Smith --- //
    
    public static class SnowyWeaponSmith1 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 6;
    	private static final int DECREASE_MAX_U = 1;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyWeaponSmith1() {}

        public SnowyWeaponSmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static SnowyWeaponSmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyWeaponSmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
            
        	
        	IBlockState dioriteBlockState = Blocks.STONE.getStateFromMeta(3);
        	Block dioriteStairsBlock = ModObjects.chooseModDioriteStairsBlock();
        	IBlockState dioriteWallState = ModObjects.chooseModDioriteWallState();
        	if (dioriteStairsBlock==null || dioriteWallState==null)
        	{
        		dioriteBlockState = Blocks.COBBLESTONE.getStateFromMeta(0);
            	dioriteStairsBlock = Blocks.STONE_STAIRS;
            	dioriteWallState = Blocks.COBBLESTONE_WALL.getDefaultState();
        	}
        	
        	// Diorite block
        	for(int[] uuvvww : new int[][]{
            	// Entrance
        		{6,1,1, 9,1,2}, 
            	// Basin
        		{6,1,3, 9,2,3}, 
        		{6,3,3, 7,3,3}, 
        		{6,4,3, 6,4,4}, 
        		{9,2,4, 9,2,4}, 
        		{6,1,4, 9,1,4}, 
        		{5,2,4, 5,5,5}, 
        		{6,1,5, 9,4,5}, 
        		// Under grindstone
        		{2,1,4, 4,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], dioriteBlockState, dioriteBlockState, false);	
            }
        	// Diorite stairs
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entrance
        		{7,1,0, 3}, {8,1,0, 3}, 
        		// Basin rims
        		{7,4,3, 1}, 
        		{6,5,3, 1}, {6,5,4, 1}, 
        		})
            {
                this.setBlockState(world, dioriteStairsBlock.getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	// Diorite wall
        	for(int[] uuvvww : new int[][]{
        		{6,1,0, 6,1,0}, {9,1,0, 9,1,0}, 
        		{9,2,0, 9,2,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], dioriteWallState, dioriteWallState, false);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{9,3,0, -1}, 
            	{6,2,1, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
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
            	// Front wall
            	{1,1,1, 5,3,1}, 
            	// Back wall
            	{1,1,6, 8,3,6}, 
            	// Left wall
            	{1,1,2, 1,1,5}, 
            	{1,3,2, 1,3,5}, 
            	{1,5,2, 1,5,5}, 
            	{1,6,3, 1,6,4}, 
            	// Right wall
            	{5,1,2, 5,5,3}, {5,6,3, 5,6,4}, 
            	// Floor
            	{2,1,3, 2,1,3}, {3,1,2, 3,1,2}, {4,1,3, 4,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            
            
        	// Stripped Log (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(4+(this.getCoordBaseMode().getHorizontalIndex()%2!=0? 4:0)), this.materialType, this.biome, this.disallowModSubs);
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
            	// Left wall
            	{1,2,2, 1,2,5}, {1,4,2, 1,4,5}, 
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
            	// Basin
            	{6,5,5, 7,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogHorizAlongState, biomeStrippedLogHorizAlongState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{0,3,0, 6,3,0}, {0,2,0, 0,2,0}, {2,2,0, 2,2,0}, {4,2,0, 4,2,0}, {6,2,0, 6,2,0}, 
            	{0,4,1, 6,5,1}, 
            	{0,6,2, 6,6,2}, 
            	{0,7,3, 6,7,4}, 
            	{0,6,5, 6,6,5}, 
            	{0,5,6, 7,5,6}, {0,4,6, 9,4,6}, 
            	{0,3,7, 9,3,7}, {0,2,7, 0,2,7}, {2,2,7, 2,2,7}, {4,2,7, 4,2,7}, {6,2,7, 6,2,7}, {8,2,7, 8,2,7}, 
            	// Floor
            	{2,1,2, 2,1,2}, {3,1,3, 3,1,3}, {4,1,2, 4,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Wooden slabs (Bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{0,4,0}, {2,4,0}, {4,4,0}, {6,4,0}, 
            	{0,4,7}, {2,4,7}, {4,4,7}, {6,4,7}, {8,4,7}, 
            	{7,6,5}, 
            	{9,5,5}, {9,5,6}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabBottomState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Wooden slabs (Top)
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwo : new int[][]{
            	{1,2,0}, {3,2,0}, {5,2,0}, 
            	{1,2,7}, {3,2,7}, {5,2,7}, {7,2,7}, {9,2,7}, 
            	{9,3,6}, 
            	{6,5,2}, {6,6,3}, {6,6,4}, 
            	{0,5,2}, {0,6,3}, {0,6,4}, {0,5,5}, 
            	})
            {
            	this.setBlockState(world, biomeWoodSlabTopState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		{8,5,5, 1}, {8,5,6, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
            
            // Lantern (Hanging)
        	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,2}, {3,5,5}, 
            	}) {
            	this.setBlockState(world, biomeHangingLanternState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Grindstones
        	for (int[] uvwo : new int[][]{
        		// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		{8,2,1, 2, 0}, 
        		{3,2,5, 2, 0}, 
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		IBlockState biomeGrindstoneState = ModObjects.chooseModGrindstone(uvwo[3], this.getCoordBaseMode(), uvwo[4]==1);
            	
        		this.setBlockState(world, biomeGrindstoneState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 2;
        	int chestW = 2;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
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
            	{5,2,2, 1, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
        	
        	// Lava
            for(int[] uuvvww : new int[][]{
            	{7,2,4, 8,2,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.LAVA.getDefaultState(), Blocks.LAVA.getDefaultState(), false);
            }
            
            
            // Iron bars
            for(int[] uuvvww : new int[][]{
            	{9,3,2, 9,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            }
            
            
            // Snow Layer
        	IBlockState biomeSnowLayerState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SNOW_LAYER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvwm : new int[][]{
            	// Outside ground
            	{0,1,0, 0}, {0,1,1, 0}, {0,1,2, 0}, {0,1,3, 0}, {0,1,4, 0}, {0,1,5, 0}, {0,1,6, 0}, {0,1,7, 0}, {0,1,8, 0}, 
            	{1,1,0, 0}, {1,1,7, 0}, {1,1,8, 0}, 
            	{2,1,0, 0}, {2,1,7, 0}, {2,1,8, 0}, 
            	{3,1,0, 0}, {3,1,7, 0}, {3,1,8, 0}, 
            	{4,1,7, 0}, {4,1,8, 0}, 
            	{5,1,0, 0}, {5,1,7, 0}, {5,1,8, 0}, 
            	{6,1,7, 0}, {6,1,8, 0}, 
            	{7,1,7, 0}, {7,1,8, 0}, 
            	{8,1,7, 0}, {8,1,8, 0}, 
            	{9,1,6, 0}, {9,1,7, 0}, {9,1,8, 0}, 
            	})
            {
            	this.setBlockState(world, biomeSnowLayerState.getBlock().getStateFromMeta(biomeSnowLayerState.getBlock()==Blocks.SNOW_LAYER ? uvwm[3] : 0), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
    		
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{7, GROUND_LEVEL, -1}, 
        		{8, GROUND_LEVEL, -1}, 
           		})
        	{
            	int pathU = uvw[0]; int pathV = uvw[1]; int pathW = uvw[2];
                
                // Clear above and set foundation below
                this.clearCurrentPositionBlocksUpwards(world, pathU, pathV, pathW, structureBB);
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, pathU, pathV-2, pathW, structureBB);
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
            	int u = 3;
            	int v = 2;
            	int w = 4;
            	
            	int s = random.nextInt(3+3+4+3+2);
            	
            	if (s<=2) {u=2; w=s+3;}
            	else if (s<=5) {u=3; w=s-1;}
            	else if (s<=9) {u=4; w=s-4;}
            	else if (s<=12) {u=s-4; w=2;}
            	else if (s<=14) {u=s-7; w=1;}
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 3, 2, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
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
    
    public static class SnowyStreetDecor1 extends StructureVillagePieces.Village
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
    	public static final int STRUCTURE_DEPTH = 7;
    	public static final int STRUCTURE_HEIGHT = 4;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public SnowyStreetDecor1() {}

        public SnowyStreetDecor1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static SnowyStreetDecor1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SnowyStreetDecor1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            Biome biome = biomeProvider.getBiome(new BlockPos(bbCenterX, 64, bbCenterZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null)
			{
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, bbCenterX, bbCenterZ);}
			}
			
			if (this.materialType==null)
			{
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, bbCenterX, bbCenterZ);}
				
			}
			
			if (!this.disallowModSubs)
			{
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
			}
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos((this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}
        	
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	/*
        	// Clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            	this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            	// Make dirt foundation
            	this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
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
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        		}
        		else if (unitLetter.equals("P"))
        		{
        			// If marked with P: fill with dirt foundation and top with block-and-biome-appropriate path
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1+(world.getBlockState(new BlockPos(posX, posY, posZ)).isNormalCube()?-1:0), w, structureBB);
        			StructureVillageVN.setPathSpecificBlock(world, materialType, biome, disallowModSubs, posX, posY, posZ, false);
        		}
        		else if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock()==biomeFillerState.getBlock())
        		{
        			// If the space is blank and the block itself is dirt, add dirt foundation and then cap with grass:
        			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeTopState, u, GROUND_LEVEL-1, w, structureBB);
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
            			new    int[]{-2,-1, 0, 1, 2, 3, 5}, // Values
            			new double[]{10, 3, 4, 4, 3, 3, 1}, // Weights
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
    
    
    
    // ------------------- //
    // --- Biome Decor --- //
    // ------------------- //
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
    public static ArrayList<BlueprintData> getRandomSnowyDecorBlueprint(MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 3;
		return getSnowyDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getSnowyDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeHangingLanternState = ModObjects.chooseModLanternBlockState(true);
    	
    	boolean genericBoolean=false;
    	
    	int lanternX; int lanternY; int lanternZ;
    	
    	int decorOrientation = random.nextInt(4);
    	
    	// Make lantern base
    	switch (decorType)
    	{
    	case 2: // Lateral lanterns
    		lanternX =  decorOrientation==3 ? -1 : decorOrientation==1 ? 1 : 0;
    		lanternZ =  decorOrientation==0 ? -1 : decorOrientation==2 ? 1 : 0;
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 3, lanternZ, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 2, lanternZ, biomeHangingLanternState);
    		
    		lanternX =  decorOrientation==3 ? 1 : decorOrientation==1 ? -1 : 0;
    		lanternZ =  decorOrientation==0 ? 1 : decorOrientation==2 ? -1 : 0;
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 3, lanternZ, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 2, lanternZ, biomeHangingLanternState);
    		
    	case 1: // Second lantern opposite
    		lanternX =  decorOrientation==0 ? -1 : decorOrientation==2 ? 1 : 0;
    		lanternZ =  decorOrientation==3 ? -1 : decorOrientation==1 ? 1 : 0;
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 3, lanternZ, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 2, lanternZ, biomeHangingLanternState);
    		
    	case 0: // Single lantern
    		lanternX =  decorOrientation==0 ? 1 : decorOrientation==2 ? -1 : 0;
    		lanternZ =  decorOrientation==3 ? 1 : decorOrientation==1 ? -1 : 0;
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 3, lanternZ, biomeFenceState);
    		BlueprintData.addPlaceBlock(blueprint, lanternX, 2, lanternZ, biomeHangingLanternState);
    		
    		// Base post
    		BlueprintData.addFillWithBlocks(blueprint, 0, 0, 0, 0, 3, 0, biomeFenceState);
    	}
    	
    	
        // Return the decor blueprint
        return blueprint;
	}
}
