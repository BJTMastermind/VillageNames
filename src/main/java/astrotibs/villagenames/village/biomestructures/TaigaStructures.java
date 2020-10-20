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
import net.minecraft.block.Block;
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

public class TaigaStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Simple grass patch with two structures --- //
	
    public static class TaigaMeetingPoint1 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"PPFPPPPPPPF ",
            	"PPPFPFPPFPF ",
            	"PPFPPPPPPPPP",
            	"PFFFPPFPFPFP",
            	"FPFPPFPPPFPP",
            	"PPFFPPPPFPP ",
            	"    PPP     ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 3;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
    	public TaigaMeetingPoint1() {}
    	
    	public TaigaMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
			if (this.getCoordBaseMode().getHorizontalIndex()==0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			// Eastward
			if (this.getCoordBaseMode().getHorizontalIndex()!=3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 2 : 4), EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.getCoordBaseMode().getHorizontalIndex()!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()%2==1 ? 2 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.getCoordBaseMode().getHorizontalIndex()==3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.WEST, this.getComponentType());}
			
			
			// Attach non-road structures
			
			// Structure 1, left-hand side, near the bell
			if (this.getCoordBaseMode().getHorizontalIndex()==0) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(4)-1), EnumFacing.WEST, this.getComponentType());}
			if (this.getCoordBaseMode().getHorizontalIndex()==1) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(4)+1), this.boundingBox.minY, this.boundingBox.minZ+(-1), EnumFacing.NORTH, this.getComponentType());}
			if (this.getCoordBaseMode().getHorizontalIndex()==2) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(4)-1), EnumFacing.WEST, this.getComponentType());}
			if (this.getCoordBaseMode().getHorizontalIndex()==3) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(4)), this.boundingBox.minY, this.boundingBox.minZ+(-1), EnumFacing.NORTH, this.getComponentType());}
			
			// Structure 2, back side, along the longer side
			if (this.getCoordBaseMode().getHorizontalIndex()==0) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(random.nextInt(6)+3), this.boundingBox.minY, this.boundingBox.maxZ+(1), EnumFacing.SOUTH, this.getComponentType());}
			if (this.getCoordBaseMode().getHorizontalIndex()==1) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), EnumFacing.WEST, this.getComponentType());}
			if (this.getCoordBaseMode().getHorizontalIndex()==2) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), EnumFacing.NORTH, this.getComponentType());}
			if (this.getCoordBaseMode().getHorizontalIndex()==3) {StructureVillageVN.getNextVillageStructureComponent((StartVN)start, components, random, this.boundingBox.maxX+(1), this.boundingBox.minY, this.boundingBox.minZ+(random.nextInt(6)+3), EnumFacing.EAST, this.getComponentType());}
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
            				true, (byte)9, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
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
        	
        	// Single wood platform for the "bell"
        	this.setBlockState(world, biomeDirtState, 2, 0, 3, structureBB);
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
            	}
            	
            	this.setBlockState(world, concreteBlockstate, 2, 1, 3, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, biomePlankState, 2, 1, 3, structureBB);
        	}
        	// Wood trapdoor hatching
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(6), 1, 1, 3, structureBB); // Facing left
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(7), 3, 1, 3, structureBB); // Facing right
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(5), 2, 1, 2, structureBB); // Facing you
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(4), 2, 1, 4, structureBB); // Facing away
        	
        	
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
    		
    		
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
                int bannerXBB = 5;
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
	        			{2, 1, 5, -1, 0},
	        			{4, 1, 2, -1, 0},
	        			{9, 1, 4, -1, 0},
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
            
            return true;
        }
    }
    
    
    
	// --- Taiga Well --- //
	
	public static class TaigaMeetingPoint2 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"   PPP F ",
            	"FPPPPPPP ",
            	" PFFFFFPF",
            	"PPFFFFFPP",
            	"PPFFFFFPP",
            	"PPFFFFFPP",
            	" PFFFFFP ",
            	" PPPPPPP ",
            	"   PPPF F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	
    	public TaigaMeetingPoint2() {}
    	
    	public TaigaMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
        	
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.MOSSY_COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);

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
        			this.getXWithOffset(8, 1),
        			this.getYWithOffset(GROUND_LEVEL),
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
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0, GROUND_LEVEL, 7},
            	{8, GROUND_LEVEL, 0},
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
            	ArrayList<BlueprintData> decorBlueprint = getRandomTaigaDecorBlueprint(this.materialType, this.biome, this.getCoordBaseMode(), randomFromXYZ);
            	
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
        	
        	// Fill basin with water
        	this.fillWithBlocks(world, structureBB, 3, 1, 3, 5, 2, 5, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
        	
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
        	//IBlockState roofLogState = biomeLogHorAlongState.getBlock().getStateFromMeta(biomeLogHorAlongState.getBlock().getMetaFromState(biomeLogHorAlongState) + (this.getCoordBaseMode().getHorizontalIndex()%2==0 ? 4 : 0));
        	IBlockState roofLogState = StructureVillageVN.getHorizontalPillarState(biomeLogState, this.getCoordBaseMode().getHorizontalIndex(), false);
        	
        	this.fillWithBlocks(world, structureBB, 3, 6, 2, 5, 6, 6, roofLogState, roofLogState, false);
        	this.fillWithBlocks(world, structureBB, 2, 5, 2, 2, 5, 6, roofLogState, roofLogState, false);
        	this.fillWithBlocks(world, structureBB, 6, 5, 2, 6, 5, 6, roofLogState, roofLogState, false);
        	// Add torches
        	for (int[] uvwo : new int[][]{
        		{2, 5, 1, 2},
        		{6, 5, 1, 2},
        		// Banner side
        		{2, 5, 7, 0},
        		{6, 5, 7, 0},
        	})
        	{
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
        	}
            
            
            // Colored block where bell used to be
            if (GeneralConfig.useVillageColors)
            {
            	int metaBase = ((int)world.getSeed()%4+this.getCoordBaseMode().getHorizontalIndex())%4; // Procedural based on world seed and base mode
            	
            	BlockPos uvw = new BlockPos(4, 5, 4); // Starting position of the block cluster. Use lowest X, Z.
            	int metaCycle = (metaBase+Math.abs(this.getXWithOffset(uvw.getX(), uvw.getZ())%2 - (this.getZWithOffset(uvw.getX(), uvw.getZ())%2)*3) + uvw.getY())%4; // Procedural based on block X, Y, Z 
            	
            	if (GeneralConfig.addConcrete)
            	{
            		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
            	}
            	else
            	{
            		this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor), 4, 5, 4, structureBB);
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // Facing away from you
    			world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
    			
    			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(2, this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // Facing toward you
    			world.setTileEntity(new BlockPos(signX2, signY, signZ2), signContents2);
            }
            
            
    		
			
			// Banner    		
    		if (GeneralConfig.villageBanners)
    		{
    			int bannerXBB = 7;
    			int bannerZBB = 8;
    			int bannerYBB = 2;
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
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeCobblestoneState, biomeCobblestoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeDirtState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
                // Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.getCoordBaseMode().getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"));
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
        		
        		world.setTileEntity(bannerPos, tilebanner);
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
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInTownCenters)
            	{
	        		for (int[] ia : new int[][]{
	        			{7, 2, 1, -1, 0},
	        			{8, 2, 3, -1, 0},
	        			{0, 2, 5, -1, 0},
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
            
            return true;
        }
        
    }
	
	
	
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
	public static ArrayList<BlueprintData> getRandomTaigaDecorBlueprint(MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 7;
		return getTaigaDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getTaigaDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		int horizIndex = coordBaseMode.getHorizontalIndex();
		
		// Generate per-material blocks
		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeStoneStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), materialType, biome, disallowModSubs);
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
    			BlueprintData.addFillWithBlocks(blueprint, -1, 0, -2+(shift?1:0), -1, 0, 1+(shift?1:0), biomeTrapdoorState.getBlock().getStateFromMeta(6));
    			// Right
    			BlueprintData.addFillWithBlocks(blueprint, 1, 0, -2+(shift?1:0), 1, 0, 1+(shift?1:0), biomeTrapdoorState.getBlock().getStateFromMeta(7));
    			// Front
    			BlueprintData.addFillWithBlocks(blueprint, 0, 0, -3+(shift?1:0), 0, 0, -3+(shift?1:0), biomeTrapdoorState.getBlock().getStateFromMeta(5));
    			// Back
    			BlueprintData.addFillWithBlocks(blueprint, 0, 0, 2+(shift?1:0), 0, 0, 2+(shift?1:0), biomeTrapdoorState.getBlock().getStateFromMeta(4));
    			
    			break;
    			
    		case 1:
    			// Base
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), -1, 0, 1+(shift?1:0), -1, 0, biomePlankState);
    			// Foundation
    			for (int i=-2 ; i<=1; i++) {BlueprintData.addPlaceBlock(blueprint, i+(shift?1:0), -2, 0, biomeDirtState);}
    			
    			// Left
    			BlueprintData.addFillWithBlocks(blueprint, -3+(shift?1:0), 0, 0, -3+(shift?1:0), 0, 0, biomeTrapdoorState.getBlock().getStateFromMeta(6));
    			// Right
    			BlueprintData.addFillWithBlocks(blueprint, 2+(shift?1:0), 0, 0, 2+(shift?1:0), 0, 0, biomeTrapdoorState.getBlock().getStateFromMeta(7));
    			// Front
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), 0, -1, 1+(shift?1:0), 0, -1, biomeTrapdoorState.getBlock().getStateFromMeta(5));
    			// Back
    			BlueprintData.addFillWithBlocks(blueprint, -2+(shift?1:0), 0, 1, 1+(shift?1:0), 0, 1, biomeTrapdoorState.getBlock().getStateFromMeta(4));
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
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeStoneStairsState.getBlock().getStateFromMeta(3));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(3));
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 1, biomeStoneStairsState.getBlock().getStateFromMeta(2));
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 1, biomeStoneStairsState.getBlock().getStateFromMeta(1));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 1, biomeDirtState); // Foundation
    			break;
    		case 1: // Facing left
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeCobblestoneState);
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeStoneStairsState.getBlock().getStateFromMeta(0));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(0));
    			BlueprintData.addPlaceBlock(blueprint, 1, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(1));
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, -1, biomeStoneStairsState.getBlock().getStateFromMeta(3));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, -1, biomeDirtState); // Foundation
    			break;
    		case 2: // Facing away
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeCobblestoneState);
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeStoneStairsState.getBlock().getStateFromMeta(2));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(2));
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, -1, biomeStoneStairsState.getBlock().getStateFromMeta(3));
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, -1, biomeStoneStairsState.getBlock().getStateFromMeta(0));
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 1, biomeDirtState); // Foundation
    			break;
    		case 3: // Facing right
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeCobblestoneState);
    			BlueprintData.addFillBelowTo(blueprint, -1, -1, 0, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeStoneStairsState.getBlock().getStateFromMeta(1));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtState); // Foundation
    			BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(1));
    			BlueprintData.addPlaceBlock(blueprint, -1, 1, 0, biomeStoneStairsState.getBlock().getStateFromMeta(0));
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 1, biomeStoneStairsState.getBlock().getStateFromMeta(2));
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
    			
    			boulderTopperBlock = Blocks.COBBLESTONE_WALL; boulderTopperMeta = 0;
        		
        		// Test the spike here by seeing if cobblestone remains as cobblestone.
    			Block biomeCobblestoneBlock = biomeCobblestoneState.getBlock();
        		if (biomeCobblestoneBlock==Blocks.MOSSY_COBBLESTONE)
        		{
        			// Try to make mossy cobblestone wall
        			boulderTopperMeta = 1;
        		}
        		else if (biomeCobblestoneBlock==Blocks.SANDSTONE)
        		{
        			// Try a sandstone wall--use a slab otherwise
        			// TODO - mod sandstone slabs?
        			boulderTopperBlock=null;
        			if (boulderTopperBlock==null) {boulderTopperBlock = Blocks.SANDSTONE;}
        		}
        		else if (biomeCobblestoneBlock!=Blocks.COBBLESTONE)
        		{
        			boulderTopperBlock = biomeCobblestoneBlock;
        		}
    		}
    		else
    		{
    			// Put stairs on top of the boulder
    			boulderTopperBlock = biomeStoneStairsState.getBlock();
    			boulderTopperMeta = (new int[]{3,0,2,1})[boulderOrientation];
    		}
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 1, 0, boulderTopperBlock.getStateFromMeta(boulderTopperMeta));
    		
    		switch(boulderOrientation)
    		{
    		case 0:
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, 1, biomeStoneStairsState.getBlock().getStateFromMeta(2));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, 1, biomeDirtState);
    			break;
    		case 1:
    			BlueprintData.addPlaceBlock(blueprint, 1, 0, 0, biomeStoneStairsState.getBlock().getStateFromMeta(1));
    			BlueprintData.addFillBelowTo(blueprint, 1, -1, 0, biomeDirtState);
    			break;
    		case 2:
    			BlueprintData.addPlaceBlock(blueprint, 0, 0, -1, biomeStoneStairsState.getBlock().getStateFromMeta(3));
    			BlueprintData.addFillBelowTo(blueprint, 0, -1, -1, biomeDirtState);
    			break;
    		case 3:
    			BlueprintData.addPlaceBlock(blueprint, -1, 0, 0, biomeStoneStairsState.getBlock().getStateFromMeta(0));
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
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, -1, 0, 0, biomeTrapdoorState.getBlock().getStateFromMeta(6));
			// Right
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 1, 0, 0, biomeTrapdoorState.getBlock().getStateFromMeta(7));
			// Front
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, -1, biomeTrapdoorState.getBlock().getStateFromMeta(5));
			// Back
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 1, biomeTrapdoorState.getBlock().getStateFromMeta(4));
    		
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 0, 0, Blocks.HAY_BLOCK.getDefaultState());
			
			// Campfire
			BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 1, 0, campfireState);
			
    		break;
    		
    	case 6: // Torch on a cobblestone wall
    		
    		boulderTopperBlock=Blocks.COBBLESTONE_WALL; boulderTopperMeta=0;
    		if (biomeCobblestoneState.getBlock()==Blocks.MOSSY_COBBLESTONE)
    		{
    			// Try to make mossy cobblestone wall
    			boulderTopperMeta = 1;
    		}
    		else if (biomeCobblestoneState.getBlock()==Blocks.SANDSTONE)
    		{
    			// Try a sandstone wall--use a slab otherwise
    			// TODO - mod standstone slab?
    			boulderTopperBlock=null;
    			if (boulderTopperBlock==null) {boulderTopperBlock = Blocks.SANDSTONE;}
    		}
    		else if (biomeCobblestoneState.getBlock()!=Blocks.COBBLESTONE)
    		{
    			boulderTopperBlock = biomeCobblestoneState.getBlock();
    		}
    		
    		BlueprintData.addPlaceBlock(blueprint, 0, 0, 0, boulderTopperBlock.getStateFromMeta(boulderTopperMeta));
    		BlueprintData.addPlaceBlockAndClearAbove(blueprint, 0, 1, 0, Blocks.TORCH.getStateFromMeta(0));
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
