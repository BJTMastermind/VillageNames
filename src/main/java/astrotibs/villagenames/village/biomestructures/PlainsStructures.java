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
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	" PPPPPPP ",
            	"PPPPPPPPP",
            	"PPFFFFFPP",
            	"PPFFFFFPP",
            	"PPFFFFFPP",
            	"PPFFFFFPP",
            	"PPFFFFFPP",
            	"PPPPPPPPP",
            	" PPPPPPP ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 4;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
    	public PlainsFountain01() {}
    	
    	public PlainsFountain01(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
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
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
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
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
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
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"    PP    ",
            	" PPPPPPPP ",
            	" PFFFFFFP ",
            	" PFFFFFFP ",
            	"PPFFFFFFPP",
            	"PPFFFFFFPP",
            	" PFFFFFFP ",
            	" PFFFFFFP ",
            	" PPPPPPPP ",
            	"    PP    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
		public PlainsMeetingPoint1() {}
		
		public PlainsMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
        	IBlockState biomeCobblestoneSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
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
        			this.replaceAirAndLiquidDownwards(world, biomeDirtState, u, GROUND_LEVEL-1, w, structureBB);
        			this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
        		}
            }}
        	
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
        	
            // The well gets filled completely with water first
            //this.fillWithBlocks(world, structureBoundingBox, 3, -3, 3, 6, 12, 6, this.biomeCobblestoneBlock, Blocks.flowing_water, false);
			this.fillWithBlocks(world, structureBB, 3, -3, 3, 6, 2, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 4, -2, 4, 5, 1, 5, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false); // Water
            this.fillWithAir(world, structureBB, 4, 2, 4, 5, 2, 5);
            
            // Well rim
            if (GeneralConfig.wellSlabs)
            {
            	this.fillWithBlocks(world, structureBB, 4, 2, 3, 5, 2, 3, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 4, 2, 6, 5, 2, 6, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 3, 2, 4, 3, 2, 5, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            	this.fillWithBlocks(world, structureBB, 6, 2, 4, 6, 2, 5, biomeCobblestoneSlabState, biomeCobblestoneSlabState, false);
            }
            
            // Well support posts
            for (int i : new int[]{3, 6})
            {
                for (int j : new int[]{3, 6})
                {
                	this.fillWithBlocks(world, structureBB, i, 3, j, i, 4, j, biomeFenceState, biomeFenceState, false);
                }
            }
            
            // Roof of the well
            this.fillWithBlocks(world, structureBB, 3, 5, 3, 6, 5, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            
            if (GeneralConfig.useVillageColors)
            {
            	BlockPos uvw = new BlockPos(4, 5, 4); // Starting position of the block cluster. Use lowest X, Z.
            	
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
            		this.fillWithBlocks(world, structureBB, 4, 5, 4, 5, 5, 5, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), false);
            	}
            }
            
            // Line the well with cobblestone
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
                        	
                    		this.fillWithBlocks(world, structureBB, j, -3, i, j, 1, i, concreteBlockstate, concreteBlockstate, false);
                    	}
                    	else
                    	{
                    		this.fillWithBlocks(world, structureBB, j, -3, i, j, 1, i, biomeCobblestoneState, biomeCobblestoneState, false);
                    	}
                    }
                }
            }
            
            
            // Over-lid torches
            for (int[] uvwo : new int[][]{
            	{3, 6, 3, -1},
            	{3, 6, 6, -1},
            	{6, 6, 3, -1},
            	{6, 6, 6, -1},
            })
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signXBB = 6;
    			int signYBB = 2;
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
	        			{1, 1, 8, -1, 0},
	        			{8, 1, 8, -1, 0},
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
    
    

    // --- Market --- //
    
    public static class PlainsMeetingPoint2 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"FPPFFFFF",
            	"FPPPFFFF",
            	"FFPPFPPF",
            	"FFFPFFPP",
            	"FFFPFPFF",
            	"FFFPPFFP",
            	"FFFFFPPP",
            	"FFFPPPPP",
            	"FFFFPFFP",
            	"FFFFPPPP",
            	"FPFPPPFP",
            	"PPPFPPPP",
            	"PPPPFPPF",
            	"FFPPFFFF",
            	"FPPPFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
	    public PlainsMeetingPoint2() {}
		
		public PlainsMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodenSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
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
	        			
	        			entityvillager.setLocationAndAngles((double)this.getXWithOffset(ia[0], ia[2]) + 0.5D, (double)this.getYWithOffset(ia[1]) + 0.5D, (double)this.getZWithOffset(ia[0], ia[2]) + 0.5D,
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
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"FFFFPFFPFFF",
            	"PFPFFPFFPFF",
            	"PFPPFPFFFPF",
            	"FPFFPPFPFFF",
            	"FFPFFFFPPFF",
            	"PFFPFFFFFPP",
            	"FPFFFFFPFFP",
            	"FPFFFPFFFPF",
            	"PFPPFFFFFPF",
            	"FPFFFFFFPFP",
            	"FPPPFPPFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
    	public PlainsMeetingPoint3() {}
    	
    	public PlainsMeetingPoint3(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
            				true, (byte)14, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneStoneStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);

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
        	
        	// Level the ground with grass and then insert grass paths
        	
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
    public static ArrayList<BlueprintData> getRandomPlainsDecorBlueprint(MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		int decorCount = 1;
		return getPlainsDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);//, townColor);
	}
	public static ArrayList<BlueprintData> getPlainsDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), materialType, biome, disallowModSubs);
    	
    	// For stripped wood specifically

    	// Try to see if stripped wood exists
    	if (biomeLogVertState.getBlock() == Blocks.LOG)
    	{
    		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
    	}
    	else if (biomeLogVertState.getBlock() == Blocks.LOG2)
    	{
    		biomeStrippedWoodOrLogOrLogVerticState = ModObjects.chooseModStrippedWoodState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
    	}
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
