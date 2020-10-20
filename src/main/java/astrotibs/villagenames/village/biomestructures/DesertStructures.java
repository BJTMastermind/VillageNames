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
import net.minecraft.block.BlockFlowerPot;
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

public class DesertStructures
{
	// -------------------- //
    // --- Start Pieces --- //
	// -------------------- //
	
	// --- Fountain with building --- //
	
	public static class DesertMeetingPoint1 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"   PPPPPPP",
            	" PPPPPPPPP",
            	" PPFFFPPPP",
            	"PPFFFFFPPP",
            	"PPFFFFFPPP",
            	"PPFFFFFPPP",
            	" PPFFFPPPP",
            	" PPPPPPPPP",
            	"   PPPPPPP",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
    	public DesertMeetingPoint1() {}
    	
    	public DesertMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
			if (this.getCoordBaseMode().getHorizontalIndex()%2!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 3, EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.getCoordBaseMode().getHorizontalIndex()%2==0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 3, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 3, EnumFacing.WEST, this.getComponentType());
			
			// Attach a non-road structure
			int strucX=0; int strucZ=0; EnumFacing coordBaseMode=EnumFacing.SOUTH;
			
			if (this.getCoordBaseMode().getHorizontalIndex()%2==0)
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
            				true, (byte)7, this.getCoordBaseMode().getHorizontalIndex());
            		
                    if (this.averageGroundLvl < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeSmoothSandstoneState = StructureVillageVN.getBiomeSpecificBlockState(ModObjects.chooseModSmoothSandstoneState(false), this.materialType, this.biome, this.disallowModSubs);

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
        	
        	// Set sandstone ground and clear area above
			this.fillWithBlocks(world, structureBB, 3, 0, 0, 9, 0, 8, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 1, 0, 1, 2, 0, 7, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 0, 0, 3, 0, 0, 5, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	
        	// Set well rim
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 2, 1, 2, 6, 1, 6, concreteBlockstate, concreteBlockstate, false);
        	}
        	else
        	{
        		this.fillWithBlocks(world, structureBB, 2, 1, 2, 6, 1, 6, biomeLogVertState, biomeLogVertState, false);
        	}
        	// Air in the corners
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 1, 2, structureBB);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 1, 6, structureBB);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 6, 1, 6, structureBB);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 6, 1, 2, structureBB);
            
            // Sand underneath the rim
            this.fillWithBlocks(world, structureBB, 3, 0, 2, 5, 0, 2, biomeDirtState, biomeDirtState, false);
            this.fillWithBlocks(world, structureBB, 3, 0, 6, 5, 0, 6, biomeDirtState, biomeDirtState, false);
            this.fillWithBlocks(world, structureBB, 2, 0, 3, 2, 0, 5, biomeDirtState, biomeDirtState, false);
            this.fillWithBlocks(world, structureBB, 6, 0, 3, 6, 0, 5, biomeDirtState, biomeDirtState, false);
            
            // Water in the fountain
            this.fillWithBlocks(world, structureBB, 3, 1, 3, 5, 1, 5, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
            
            // Spout
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 3, 4, biomeLogVertState, biomeLogVertState, false);
            if (GeneralConfig.useVillageColors)
        	{
        		/*Object[] tryConcrete = ModObjects.chooseModConcrete(townColor2);
            	Block concreteBlock = Blocks.stained_hardened_clay; int concreteMeta = townColor2;
            	if (tryConcrete != null) {concreteBlock = (Block) tryConcrete[0]; concreteMeta = (Integer) tryConcrete[1];}
            	
            	this.setBlockState(world, concreteBlock, concreteMeta, 4, 4, 4, structureBB);*/
            	this.setBlockState(world, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), 4, 4, 4, structureBB);
        	}
        	else
        	{
        		this.setBlockState(world, Blocks.HARDENED_CLAY.getDefaultState(), 4, 4, 4, structureBB);
        	}
            // Just the tip
            for (int[] uvwo : new int[][]{
            	{4, 5, 4, -1},
            })
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            // Cactus
            int tileU = 5;
            int tileV = 2;
            int tileW = 2;
            
            BlockPos potPos = new BlockPos(this.getXWithOffset(tileU, tileW), this.getYWithOffset(tileV), this.getZWithOffset(tileU, tileW));
            
            TileEntity flowerPotWithCactus = (new BlockFlowerPot()).createNewTileEntity(world, 9); // 9 is cactus
            world.setBlockState(potPos, Blocks.FLOWER_POT.getDefaultState(), 2);
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
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
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeSmoothSandstoneState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                
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
	        			{1, 1, 1, -1, 0},
	        			{5, 1, 0, -1, 0},
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
	
	
	// --- Desert Well --- //
	
	public static class DesertMeetingPoint2 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	"     PPP    ",
            	" PPPPPPPPPP ",
            	" PPPPPPPPPP ",
            	" PPFFFFFFPP ",
            	"PPPFFFFFFPP ",
            	"PPPFFFFFFPPP",
            	"PPPFFFFFFPPP",
            	" PPFFFFFFPPP",
            	" PPFFFFFFPP ",
            	" PPPPPPPPPP ",
            	" PPPPPPPPPP ",
            	"    PPP     ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
    	public DesertMeetingPoint2() {}
    	
    	public DesertMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()>=2 ? 5 : 4), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()>=2 ? 5 : 4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.getCoordBaseMode().getHorizontalIndex()<=1 ? 5 : 4), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.getCoordBaseMode().getHorizontalIndex()<=1 ? 5 : 4), EnumFacing.WEST, this.getComponentType());
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
        	
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeSmoothSandstoneState = StructureVillageVN.getBiomeSpecificBlockState(ModObjects.chooseModSmoothSandstoneState(false), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeSmoothSandstoneSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(ModObjects.chooseModSmoothSandstoneSlab(false, false), this.materialType, this.biome, this.disallowModSubs);
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
        	
        	// Set sandstone ground
        	this.fillWithBlocks(world, structureBB, 1, 0, 1, 10, 0, 10, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	
        	// Set sand underneath the fountain
        	this.fillWithBlocks(world, structureBB, 3, 0, 3, 8, 0, 8, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	
        	// Set well rim
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 3, 1, 3, 8, 1, 8, concreteBlockstate, concreteBlockstate, false);
        	}
        	else
        	{
        		this.fillWithBlocks(world, structureBB, 3, 1, 3, 8, 1, 8, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	}
        	
        	// Water in the fountain
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 7, 1, 7, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
            
            // Sandstone slab roof
            this.fillWithBlocks(world, structureBB, 4, 4, 4, 7, 4, 7, biomeSmoothSandstoneSlabBottomState, biomeSmoothSandstoneSlabBottomState, false);
            
        	// Columns
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 4, 4, biomeLogVertState, biomeLogVertState, false);
            this.fillWithBlocks(world, structureBB, 4, 1, 7, 4, 4, 7, biomeLogVertState, biomeLogVertState, false);
            this.fillWithBlocks(world, structureBB, 7, 1, 7, 7, 4, 7, biomeLogVertState, biomeLogVertState, false);
            this.fillWithBlocks(world, structureBB, 7, 1, 4, 7, 4, 4, biomeLogVertState, biomeLogVertState, false);
            
            // Torches
            for (int[] uvwo : new int[][]{
            	{4, 5, 4, -1},
            	{4, 5, 7, -1},
            	{7, 5, 7, -1},
            	{7, 5, 4, -1},
            })
            {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Roof of the well
            if (GeneralConfig.useVillageColors)
            {
            	BlockPos uvw = new BlockPos(5, 4, 5); // Starting position of the block cluster. Use lowest X, Z.
            	
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
            		this.fillWithBlocks(world, structureBB, 5, 4, 5, 6, 4, 6, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2), false);
            	}
            }
            else
            {
            	this.fillWithBlocks(world, structureBB, 5, 4, 5, 6, 4, 6, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
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
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeSmoothSandstoneState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.getCoordBaseMode().getHorizontalIndex(), false)));
				
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
	        			{10, 1, 8, -1, 0},
	        			{1, 1, 10, -1, 0},
	        			{7, 1, 10, -1, 0},
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
	
	
	// --- Desert Market --- //

	public static class DesertMeetingPoint3 extends StartVN
    {
        // Make foundation with blanks as empty air and F as foundation spaces
        private static final String[] foundationPattern = new String[]{
            	" FF FFFFFF     ",
            	"   FFFFFFFFFF  ",
            	"FFFFFFFFFFFFFF ",
            	"FFFFFFFFFFFFFFF",
            	"FFFFFFFFFFFFFFF",
            	"FFFFFFFFPPPPPPF",
            	"FFFFFFFFPFFFFPP",
            	"FFFFFFFFPFPPFPP",
            	" FFFFFFFPFPPFPP",
            	" FFFFFFFPFFFFPF",
            	" FFFFFFPPPPPPPF",
            	"   FFFFPPPPFFFF",
            	"   FFFFPFFPFFF ",
            	"   F FFPPPPFFF ",
            	"     FFPFFPF F ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	
    	public DesertMeetingPoint3() {}
    	
    	public DesertMeetingPoint3(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
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
    		if (this.getCoordBaseMode().getHorizontalIndex()!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + new int[]{1,5,1,7}[this.getCoordBaseMode().getHorizontalIndex()], this.boundingBox.maxY - 5, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
        	// Eastward
        	if (this.getCoordBaseMode().getHorizontalIndex()!=1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + new int[]{6,4,6,1}[this.getCoordBaseMode().getHorizontalIndex()], EnumFacing.EAST, this.getComponentType());}
			// Southward
        	if (this.getCoordBaseMode().getHorizontalIndex()!=2) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + new int[]{1,6,4,6}[this.getCoordBaseMode().getHorizontalIndex()], this.boundingBox.maxY - 5, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.getCoordBaseMode().getHorizontalIndex()!=3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + new int[]{7,1,5,4}[this.getCoordBaseMode().getHorizontalIndex()], EnumFacing.WEST, this.getComponentType());}
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
        	
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STANDING_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeSandstoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs); // TODO - Check for modded sandstone walls
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeSmoothSandstoneState = StructureVillageVN.getBiomeSpecificBlockState(ModObjects.chooseModSmoothSandstoneState(false), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeSmoothSandstoneSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(ModObjects.chooseModSmoothSandstoneSlab(false, false), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeSandstoneSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_SLAB.getStateFromMeta(1), this.materialType, this.biome, this.disallowModSubs);

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
        	
        	// Set ground and clear area above
        	int fillXmin; int fillZmin; int fillXmax; int fillZmax; int clearToHeight = 5;
        	
        	for (Object[] o : new Object[][]{
        		// minX, maxX, minZ, maxZ, groundY, structureHeight, surfaceBlock, surfaceMeta, subsurfaceBlock, subsurfaceMeta 
        		{0, 0, 7, 12, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{1, 2, 4, 12, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{1, 2, 14, 14, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{0, 2, 13, 13, -1, biomeSmoothSandstoneState, biomeSmoothSandstoneState},
        		{2, 2, 3, 3, -1, biomeSmoothSandstoneState, biomeSmoothSandstoneState},
        		{3, 3, 14, 14, -1, biomeSmoothSandstoneState, biomeSmoothSandstoneState},
        		{3, 3, 1, 13, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{3, 3, 0, 0, -1, biomeSmoothSandstoneState, biomeSmoothSandstoneState},
        		{4, 4, 2, 14, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{4, 4, 0, 1, -1, biomeSmoothSandstoneState, biomeSmoothSandstoneState},
        		{5, 9, 0, 14, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{10, 11, 0, 13, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{12, 12, 1, 13, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{12, 12, 0, 0, -1, biomeSmoothSandstoneState, biomeSmoothSandstoneState},
        		{13, 13, 0, 12, 0, biomeDirtState, biomeSmoothSandstoneState},
        		{14, 14, 3, 11, 0, biomeDirtState, biomeSmoothSandstoneState},
        	})
        	{
        		this.fillWithBlocks(world, structureBB, (Integer)o[0], (Integer)o[4], (Integer)o[2], (Integer)o[1], (Integer)o[4], (Integer)o[3], ((Block)o[6]).getStateFromMeta((Integer)o[7]), ((Block)o[6]).getStateFromMeta((Integer)o[7]), false);
            	/*
        		for (int x = (Integer)o[0]; x <= (Integer)o[1]; ++x) {for (int z = (Integer)o[2]; z <= (Integer)o[3]; ++z)
            	{
            		this.replaceAirAndLiquidDownwards(world, ((Block)o[8]).getStateFromMeta((Integer)o[9]), x, (Integer)o[4]-1, z, structureBB); // Foundation
            		this.clearCurrentPositionBlocksUpwards(world, (Integer)o[0], Math.max((Integer)o[4], 0)+1, (Integer)o[2], structureBB);
            	}}*/
        	}
        	// Set sandstone in certain places
        	this.fillWithBlocks(world, structureBB, 7, 0, 0, 7, 0, 4, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 10, 0, 0, 10, 0, 3, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 1, 9, 0, 1, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 3, 9, 0, 3, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 4, 13, 0, 4, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 9, 13, 0, 9, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 8, 0, 5, 8, 0, 8, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 13, 0, 5, 13, 0, 8, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 10, 0, 6, 11, 0, 7, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	this.fillWithBlocks(world, structureBB, 14, 0, 6, 14, 0, 8, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	
        	
        	// Fountain
        	
        	// Rim
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor2);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.CONCRETE.getStateFromMeta(townColor2);
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
        		this.fillWithBlocks(world, structureBB, 9, 1, 5, 12, 1, 8, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
        	}
        	
        	
        	// Corner posts
    		this.setBlockState(world, biomeLogVertState, 9, 1, 5, structureBB);
    		this.setBlockState(world, biomeLogVertState, 9, 1, 8, structureBB);
    		this.setBlockState(world, biomeLogVertState, 12, 1, 8, structureBB);
    		this.setBlockState(world, biomeLogVertState, 12, 1, 5, structureBB);
        	// Top the corners
    		this.setBlockState(world, biomeSandstoneSlabBottomState, 9, 2, 5, structureBB);
    		this.setBlockState(world, biomeSandstoneSlabBottomState, 9, 2, 8, structureBB);
    		this.setBlockState(world, biomeSandstoneSlabBottomState, 12, 2, 8, structureBB);
    		this.setBlockState(world, biomeSandstoneSlabBottomState, 12, 2, 5, structureBB);
        	
    		// Fill with water
    		this.fillWithBlocks(world, structureBB, 10, 1, 6, 11, 1, 7, Blocks.FLOWING_WATER.getDefaultState(), Blocks.FLOWING_WATER.getDefaultState(), false);
    		
    		
    		// Market stalls
    		
    		// Frames
    		this.fillWithBlocks(world, structureBB, 7, 1, 0, 7, 3, 0, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 10, 1, 0, 10, 3, 0, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 10, 1, 2, 10, 3, 2, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 7, 1, 2, 7, 3, 2, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 7, 4, 0, 10, 4, 2, biomeSmoothSandstoneSlabBottomState, biomeSmoothSandstoneSlabBottomState, false);
    		this.fillWithAir(world, structureBB, 8, 4, 1, 9, 4, 1);
    		
    		this.fillWithBlocks(world, structureBB, 1, 1, 5, 1, 4, 5, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 5, 1, 5, 5, 4, 5, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 5, 1, 7, 5, 4, 7, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 1, 1, 7, 1, 4, 7, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 1, 5, 5, 5, 5, 7, biomeSmoothSandstoneSlabBottomState, biomeSmoothSandstoneSlabBottomState, false);
    		this.fillWithAir(world, structureBB, 2, 5, 6, 4, 5, 6);
    		
    		this.fillWithBlocks(world, structureBB, 4, 1, 11, 4, 3, 11, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 7, 1, 11, 7, 3, 11, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 7, 1, 14, 7, 3, 14, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 4, 1, 14, 4, 3, 14, biomeSandstoneWallState, biomeSandstoneWallState, false);
    		this.fillWithBlocks(world, structureBB, 4, 4, 11, 7, 4, 14, biomeSmoothSandstoneSlabBottomState, biomeSmoothSandstoneSlabBottomState, false);
    		this.fillWithAir(world, structureBB, 5, 4, 12, 6, 4, 13);
    		
    		// Stall contents
    		
    		// Glazed terracotta
    		if (GeneralConfig.addConcrete)
        	{
        		// Square under square awning
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor:0, (0 + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex()<2 ? 1 : 0))%4), 5, 1, 13, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor:0, (1 + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex()<2 ? 3 : 0))%4), 6, 1, 13, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor:0, (2 + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex()<2 ? 1 : 0))%4), 6, 1, 12, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor:0, (3 + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex()<2 ? 3 : 0))%4), 5, 1, 12, structureBB);
        		
        		// Halved square under strip awning
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor2:0, (0 + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex()<2 ? 1 : 0))%4), 8, 1, 2, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor2:0, (1 + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex()<2 ? 3 : 0))%4), 9, 1, 2, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor2:0, (2 + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex()<2 ? 1 : 0))%4), 9, 1, 0, structureBB);
        		this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(GeneralConfig.useVillageColors? townColor2:0, (3 + this.getCoordBaseMode().getHorizontalIndex() + (this.getCoordBaseMode().getHorizontalIndex()<2 ? 3 : 0))%4), 8, 1, 0, structureBB);
        	}
        	else
        	{
        		// Square under awning
        		this.fillWithBlocks(world, structureBB, 5, 1, 12, 6, 1, 13, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor:0), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 0), false);
        		
        		// Halved square under strip awning
        		this.fillWithBlocks(world, structureBB, 8, 1, 0, 9, 1, 0, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2:0), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), false);
        		this.fillWithBlocks(world, structureBB, 8, 1, 2, 9, 1, 2, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2:0), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), false);
        	}
    		
        	// Cut stone and stairs
        	this.fillWithBlocks(world, structureBB, 2, 1, 6, 4, 1, 6, biomeLogVertState, biomeLogVertState, false);
        	this.setBlockState(world, Blocks.SANDSTONE_STAIRS.getStateFromMeta(0), 1, 1, 6, structureBB);
        	this.setBlockState(world, Blocks.SANDSTONE_STAIRS.getStateFromMeta(1), 5, 1, 6, structureBB);
        	
        	// Various decorations
        	this.setBlockState(world, Blocks.HAY_BLOCK.getDefaultState(), 5, 1, 0, structureBB);
        	this.setBlockState(world, Blocks.HAY_BLOCK.getStateFromMeta(this.getCoordBaseMode().getHorizontalIndex()%2==1 ? 8 : 4), 3, 1, 2, structureBB);
        	
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
                world.setBlockState(potPos, Blocks.FLOWER_POT.getDefaultState(), 2);
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
            	ArrayList<BlueprintData> decorBlueprint = getRandomDesertDecorBlueprint(this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ);//, townColor);
            	
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.getCoordBaseMode().getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
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
                this.fillWithBlocks(world, structureBB, bannerXBB, bannerYBB-2, bannerZBB, bannerXBB, bannerYBB-1, bannerZBB, biomeSmoothSandstoneState, biomeSmoothSandstoneState, false);
                this.replaceAirAndLiquidDownwards(world, biomeSmoothSandstoneState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                
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
	        			{6, 1, 5, -1, 0},
	        			{8, 1, 10, -1, 0},
	        			{11, 1, 10, -1, 0},
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
	public static ArrayList<BlueprintData> getRandomDesertDecorBlueprint(MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		int decorCount = 1;
		return getDesertDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);//, townColor);
	}
	public static ArrayList<BlueprintData> getDesertDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
    	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), materialType, biome, disallowModSubs);

		
        switch (decorType)
        {
    	case 0: // Torch on stained terracotta and cut sandstone
    		
    		BlueprintData.addFillWithBlocks(blueprint, 0, 0, 0, 0, 1, 0, biomeLogVertState);
    		//BlueprintData.addPlaceBlock(blueprint, 0, 2, 0, GeneralConfig.decorateVillageCenter ? Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColor) : Blocks.HARDENED_CLAY.getDefaultState());
    		BlueprintData.addPlaceBlock(blueprint, 0, 2, 0, Blocks.HARDENED_CLAY.getDefaultState());
    		BlueprintData.addPlaceBlock(blueprint, 0, 3, 0, Blocks.TORCH.getStateFromMeta(0));
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
