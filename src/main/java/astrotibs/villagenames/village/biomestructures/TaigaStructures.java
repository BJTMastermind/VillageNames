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
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFlowerPot;
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
    	
    	public TaigaMeetingPoint1(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
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
        		IBlockState concreteBlockstate = Blocks.CONCRETE.getStateFromMeta(townColor);
            	
            	// Basin rim
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
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);
// Line the well with cobblestone                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.getCoordBaseMode().getHorizontalIndex(), false)));
				
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
	        			{2, 1, 5, -1, 0},
	        			{4, 1, 2, -1, 0},
	        			{9, 1, 4, -1, 0},
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
    	
    	public TaigaMeetingPoint2(BiomeProvider chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
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
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs);
            	
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
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
        	}
            
            
            // Colored block where bell used to be
            if (GeneralConfig.useVillageColors)
            {
            	int metaBase = ((int)world.getSeed()%4+this.getCoordBaseMode().getHorizontalIndex())%4; // Procedural based on world seed and base mode
            	
            	BlockPos uvw = new BlockPos(4, 5, 4); // Starting position of the block cluster. Use lowest X, Z.
            	int metaCycle = (metaBase+Math.abs(this.getXWithOffset(uvw.getX(), uvw.getZ())%2 - (this.getZWithOffset(uvw.getX(), uvw.getZ())%2)*3) + uvw.getY())%4; // Procedural based on block X, Y, Z 
            	
            	this.setBlockState(world, FunctionsVN.getGlazedTerracotaFromMetas(townColor, metaCycle), uvw.getX(), uvw.getY(), uvw.getZ(), structureBB);
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
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                // Line the well with cobblestone                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
                // Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.STANDING_BANNER.getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.getCoordBaseMode().getHorizontalIndex(), false)));
				
				// Set the tile entity
				TileEntity tilebanner = new TileEntityBanner();
				ItemStack villageBanner = BannerGenerator.makeBanner(villageNBTtag.getCompoundTag("BlockEntityTag"), (namePrefix + " " + nameRoot + " " + nameSuffix).trim());
				
    			((TileEntityBanner) tilebanner).setItemValues(villageBanner, false);
        		
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
    
    
    // --- Animal Pen --- //
    
    public static class TaigaAnimalPen1 extends StructureVillagePieces.Village
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
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		"      F      ",
        };
    	// Here are values to assign to the bounding box
    	private static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	private static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaAnimalPen1() {}

        public TaigaAnimalPen1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaAnimalPen1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaAnimalPen1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	for (int w=0; w < foundationPattern.length; w++) {for (int u=0; u < foundationPattern[0].length(); u++) {
        		
            		if (
            				(foundationPattern[foundationPattern.length-1-w].substring(u, u+1).toUpperCase().equals("F"))
            				|| (world.getBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(GROUND_LEVEL-1), this.getZWithOffset(u, w)))==biomeDirtState)
            				)
            		{
            			// If marked with F or if this is biome-style dirt: fill with dirt foundation
            			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
                    	// Top with grass
                    	this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
            		}
                }
            }
            
        	// Grass
        	for(int[] uuvvww : new int[][]{
            	{1,0,1, 11,0,6},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
        	
            
            // Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs); 
            for(int[] uuvvww : new int[][]{
            	{1,1,1, 5,1,1}, {7,1,1, 11,1,1},
            	{1,1,6, 11,1,6}, 
            	{1,1,1, 1,1,6}, {11,1,1, 11,1,6}, 
            	// Posts
            	{1,2,1, 1,2,1}, {1,2,6, 1,2,6}, {11,2,1, 11,2,1}, {11,2,6, 11,2,6}, 
            	{5,2,1, 5,3,1}, {7,2,1, 7,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
            
            
        	// Fence Gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE_GATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwos : new int[][]{
            	{6,1,1, 2, 0},
            	})
            {
            	this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.chooseFenceGateMeta(uvwos[3], uvwos[4]==1)), uvwos[0], uvwos[1], uvwos[2], structureBB);
            }
            
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{6,3,1, 6,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{6,4,1, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Vertical)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Rim
            	{1,0,0, 5,0,0, 2}, {7,0,0, 11,0,0, 2}, 
            	{1,0,7, 11,0,7, 0}, 
            	{0,0,1, 0,0,6, 3}, 
            	{12,0,1, 12,0,6, 1},
            	// Trough
            	{3,1,3, 4,1,3, 2}, 
            	{2,1,4, 2,1,4, 3}, 
            	{3,1,5, 4,1,5, 0}, 
            	{5,1,4, 5,1,4, 1}, 
            	// Entrance
            	{6,3,0, 6,3,0, 2}, 
            	{6,3,2, 6,3,2, 0}, 
            	
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, true)), biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, true)), false);	
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		{6,0,0, 6,0,0, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }

            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{6, 1, 3}, 
        			{8, 1, 4}, 
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
    
    
    // --- Armorer Station --- //
    
    public static class TaigaArmorer2 extends StructureVillagePieces.Village
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
        		"  FPFFP",
        		" PFFFP ",
        		"FFFFFF ",
        		"FPFFFPF",
        		"PFFFFPP",
        		" PPPPF ",
        		"F PPP P",
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
    	
        public TaigaArmorer2() {}

        public TaigaArmorer2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaArmorer2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaArmorer2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
        	
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	IBlockState blastFurnaceState = ModObjects.chooseModBlastFurnaceState(2, this.getCoordBaseMode());
        	for (int[] uvw : new int[][]{
        		{3,1,4}
        		})
            {
                this.setBlockState(world, blastFurnaceState.getBlock().getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), blastFurnaceState, 2);
            }
        	
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{0,1,3, 0,3,3}, 
            	{3,2,4, 3,7,4}, 
            	{6,1,3, 6,3,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{1,4,2, 5,4,2}, 
            	{0,4,3, 6,4,3}, 
            	{1,4,4, 5,4,4}, 
            	{1,5,3, 5,5,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for(int[] uvw : new int[][]{
            	{2,0,2}, 
            	{3,0,2}, {3,0,3}, 
            	{4,0,2}, 
            	// Chimney
            	{3,4,4}, 
            	})
            {
        		this.setBlockState(world, biomeCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,4,1, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Campfire
            IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), this.getCoordBaseMode());
            for(int[] uvw : new int[][]{
            	{5,1,1}, 
            	})
            {
        		this.setBlockState(world, campfireState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,1,3, 2,1,3, 3}, {4,1,3, 4,1,3, 3}, 
        		{2,1,4, 2,1,4, 0}, {4,1,4, 4,1,4, 1}, 
        		{2,1,5, 4,1,5, 2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,4, 1}, 
            	{2,1,6, 1}, 
            	{5,1,6, 0}, 
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
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0, 1, 0},
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 2+random.nextInt(3);
            	int v = 1;
            	int w = 0+random.nextInt(3);
            	
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
    
    
    // --- Armorer House --- //
    
    public static class TaigaArmorerHouse1 extends StructureVillagePieces.Village
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
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" F P  F",
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
    	
        public TaigaArmorerHouse1() {}

        public TaigaArmorerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaArmorerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaArmorerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            	{3,0,2, 4,0,2}, 
            	{3,0,7, 4,0,7}, 
            	{2,0,2, 2,0,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,1, 1,2,8}, 
            	// Right wall
            	{5,1,1, 5,2,8}, 
            	// Front wall
            	{2,1,1, 4,3,1}, {3,4,1, 3,4,1}, 
            	// Back wall
            	{2,1,8, 4,3,8}, {3,4,8, 3,4,8}, 
            	// Front entrance
            	{3,0,1, 3,0,1}, 
            	// Floor
            	{3,0,3, 3,0,6}, 
            	// Furnace
            	{4,1,3, 4,1,3}, {4,1,6, 4,1,6}, {4,2,4, 4,3,5}, {4,4,5, 4,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
        		{4,1,4, 3}, 
        		{4,1,5, 3}, 
        		})
            {
        		IBlockState blastFurnaceState = ModObjects.chooseModBlastFurnaceState(uvw[3], this.getCoordBaseMode());
                this.setBlockState(world, blastFurnaceState.getBlock().getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), blastFurnaceState, 2);
            }
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{4,5,5, 4,6,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	{0,2,0, 0,2,9}, 
            	{1,3,0, 1,3,9}, 
            	{2,4,0, 2,4,9}, 
            	{3,5,0, 3,5,9}, 
            	{4,4,0, 4,4,9}, 
            	{5,3,0, 5,3,9}, 
            	{6,2,0, 6,2,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,0, 2}, {3,3,2, 0}, 
            	{3,3,7, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{4,2,3, 4,2,3, 3}, 
        		{4,2,6, 4,2,6, 2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Trapdoor (Top Vertical)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,9, 2,2,9, 0}, 
            	{4,2,9, 4,2,9, 0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], true, true)), biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], true, true)), false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{3, 2, 8},  
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 2, 1, 0},  
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{1, 1, 0}, 
        		{6, 1, 0}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
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
            	int u = 2+random.nextInt(2);
            	int v = 1;
            	int w = 2+random.nextInt(6);
            	
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
    
    
    // --- Butcher Shop --- //
    
    public static class TaigaButcherShop1 extends StructureVillagePieces.Village
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
    	
        public TaigaButcherShop1() {}

        public TaigaButcherShop1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaButcherShop1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaButcherShop1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
            
            // Cobblestone, part 1
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front steps
            	{2,1,1, 4,1,1}, 
            	// Front wall
            	{1,1,2, 5,4,2}, {2,5,2, 4,5,2}, {3,6,2, 3,6,2}, 
            	// Back wall, left side
            	{1,1,7, 5,4,7}, 
            	// Far right wall
            	{9,1,4, 9,4,7}, {9,5,5, 9,5,6}, 
            	// Floor under barrel
            	{2,1,6, 2,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Grass
            for(int[] uuvvww : new int[][]{
            	{6,1,1, 9,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
        	
            
            // Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs); 
            for(int[] uuvvww : new int[][]{
            	{6,2,1, 6,2,2}, 
            	{7,2,1, 9,2,1}, 
            	{9,2,2, 9,2,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }

            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,5,1, 2}, 
            	{3,4,3, 0}, {3,4,6, 2}, 
            	// Back area
            	{8,4,6, 3}, {8,4,5, 3}, 
            	// Yard
            	{9,3,2, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,2, 1,4,7}, {1,5,5, 1,5,6}, 
            	// Middle right wall
            	{5,1,2, 5,4,4}, 
            	// Back wall, right side
            	{6,1,7, 9,4,7}, 
            	// Wall to the yard
            	{6,1,4, 8,4,4}, 
            	// Ceiling
            	{5,5,5, 9,5,6}, 
            	// Chimney
            	{5,6,5, 6,6,5}, {5,6,7, 5,6,7}, {4,6,6, 4,6,6}, 
            	// Roof
            	{2,1,2, 2,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Chimney
            	{5,3,6, 5,4,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Left roof
            	{0,4,1, 0,4,3}, 
            	{1,5,1, 1,5,4}, 
            	{2,6,1, 2,6,4}, 
            	{3,7,1, 3,7,4}, 
            	{4,6,1, 4,6,4}, 
            	{5,5,1, 5,5,4}, 
            	{6,4,1, 6,4,3}, 
            	// Back roof
            	{0,4,8, 10,4,8}, 
            	{0,5,7, 10,5,7}, 
            	{0,6,6, 3,6,6}, {6,6,6, 10,6,6}, 
            	{0,6,5, 4,6,5}, {7,6,5, 10,6,5},
            	{0,5,4, 0,5,4}, {6,5,4, 10,5,4}, 
            	{7,4,3, 10,4,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Left roof
            	{0,4,1, 0,4,3}, 
            	{1,5,1, 1,5,4}, 
            	{2,6,1, 2,6,4}, 
            	{3,7,1, 3,7,4}, 
            	{4,6,1, 4,6,4}, 
            	{5,5,1, 5,5,4}, 
            	{6,4,1, 6,4,3}, 
            	// Sub roof
            	{3,6,3, 3,6,3}, 
            	{2,5,3, 4,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Vertical)
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{3,6,3, 3,6,3}, 
            	// Interior floor
            	{2,1,3, 2,1,5}, {3,1,3, 4,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Smooth Stone Double Slab
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{7,1,6}, 
            	{8,2,6}, {8,2,5}, 
            	}) {
            	this.setBlockState(world, Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(0), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Smooth Stone Slab upper
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{5,1,5}, {6,1,5}, {7,1,5}, {8,1,5}, 
            	{5,1,6}, {6,1,6}, {8,1,6}, 
            	}) {
            	this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(8), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Smoker
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.getCoordBaseMode());
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{5,2,6, 2}
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
            
            
            // Cobblestone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{2,1,0, 4,1,0, 3}, 
        		// Roof
        		{4,7,5, 6,7,5, 3}, 
        		{4,7,7, 6,7,7, 2}, 
        		{4,7,6, 4,7,6, 0}, 
        		{6,7,6, 6,7,6, 1}, 
        		{4,6,7, 4,6,7, 4}, 
        		{6,6,7, 6,6,7, 5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Trapdoor (Bottom Vertical)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Yard frame
            	{6,1,0, 9,1,0, 2}, 
            	{5,1,1, 5,1,1, 3}, 
            	{10,1,1, 10,1,3, 1}, 
            	// Barrel
            	{2,2,5, 2,2,5, 2}, {3,2,6, 3,2,6, 1}, 
            	//
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, true)), biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, true)), false);	
            }
            
            
            // Trapdoor (Top Vertical)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{0,3,4, 0,3,4, 3}, {0,3,7, 0,3,7, 3}, 
            	{2,3,8, 2,3,8, 0}, {4,3,8, 4,3,8, 0}, {6,3,8, 6,3,8, 0}, {8,3,8, 8,3,8, 0}, 
            	// Chimney
            	{5,4,5, 5,4,5, 2}, {6,4,6, 6,4,6, 1}, {4,4,6, 4,4,6, 3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], true, true)), biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], true, true)), false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 3, 5}, {1, 3, 6}, 
        		{3, 3, 7}, {7, 3, 7}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,2, 2, 1, 1}, 
            	{7,2,4, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Campfire
            IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), this.getCoordBaseMode());
            for(int[] uvw : new int[][]{
            	{5,5,6}, 
            	})
            {
        		this.setBlockState(world, campfireState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{1, 1, 0}, 
        		{0, 1, 6}, 
        		{4, 1, 8}, {5, 1, 8}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,3, 1}, 
            	{1,1,1, 1}, 
            	{5,1,0, 1}, 
            	{10,1,4, 1}, 
            	{10,1,5, 0}, 
            	{10,1,6, 1}, 
            	{3,1,8, 0}, 
            	{8,1,8, 0}, 
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
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{7, 2, 2},
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
            	int u = 2+random.nextInt(6);
            	int v = 2;
            	int w = 5;
            	
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
    
    public static class TaigaCartographerHouse1 extends StructureVillagePieces.Village
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
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"  FPF  ",
        		"  FPF F",
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
    	
        public TaigaCartographerHouse1() {}

        public TaigaCartographerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaCartographerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaCartographerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
            
            // Cobblestone: everything but the front wall
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Under front door
            	{3,0,2, 3,0,2}, 
            	// Left
            	{1,1,2, 1,3,6}, 
            	// Right wall
            	{5,1,2, 5,3,6}, 
            	// Floor
            	{2,0,3, 4,0,5}, 
            	// Back wall
            	{2,1,6, 4,3,6}, 
            	// Ceiling
            	{2,3,3, 4,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{2,1,0, 2,1,0}, 
            	{4,1,0, 4,1,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,4,2, 1,6,2}, 
            	{1,4,4, 1,4,4}, {1,6,4, 1,6,4}, 
            	{1,4,6, 1,6,6}, 
            	// Right wall
            	{5,4,2, 5,6,2}, 
            	{5,4,4, 5,4,4}, {5,6,4, 5,6,4}, 
            	{5,4,6, 5,6,6}, 
            	// Front wall
            	{3,4,2, 3,4,2}, {3,6,2, 3,6,2}, {3,8,2, 3,8,2}, 
            	// Back wall
            	{3,4,6, 3,4,6}, {3,6,6, 3,8,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Logs (Vertical), part 1
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,4,3, 1,6,3}, {1,4,5, 1,6,5}, 
            	// Right wall
            	{5,4,3, 5,6,3}, {5,4,5, 5,6,5}, 
            	// Back wall
            	{2,4,6, 2,7,6}, {4,4,6, 4,7,6}, 
            	// Front wall, 2nd floor
            	{2,4,2, 2,7,2}, {4,4,2, 4,7,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }

            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front posts
            	{2,2,0, -1}, {4,2,0, -1}, 
            	// Front wall
            	{3,8,1, 2}, 
            	// First floor
            	{2,2,3, 1}, {4,2,3, 3}, 
            	// Second floor
            	{3,6,3, 0}, {3,6,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Vertical), part 2
            for(int[] uuvvww : new int[][]{
            	// Front columns
            	{2,1,1, 2,7,1}, {4,1,1, 4,7,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	{0,6,0, 0,6,7}, 
            	{1,7,0, 1,7,7}, 
            	{2,8,0, 2,8,7}, 
            	{3,9,0, 3,9,7}, 
            	{4,8,0, 4,8,7}, 
            	{5,7,0, 5,7,7},
            	{6,6,0, 6,6,7},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Cobblestone, front wall
            for(int[] uuvvww : new int[][]{
            	{2,1,2, 4,3,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs); 
            for(int[] uvw : new int[][]{
            	{2,4,4}, 
            	})
            {
        		this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chairs
        		{2,4,3, 2,4,3, 1}, {2,4,5, 2,4,5, 1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Trapdoor (Bottom Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Front planter
            	{3,4,0, 3,4,0, 2}, 
            	//
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, true)), biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, true)), false);	
            }
            
            
            // Trapdoor (Top Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{0,2,3, 0,2,3, 3}, {0,2,5, 0,2,5, 3}, 
            	{6,2,3, 6,2,3, 1}, {6,2,5, 6,2,5, 1}, 
            	{2,2,7, 2,2,7, 0}, {4,2,7, 4,2,7, 0}, 
            	//
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], true, true)), biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], true, true)), false);	
            }
            
            
            // Trapdoor (Top Horizontal)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{3,3,1, 3,3,1, 2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], true, false)), biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], true, false)), false);	
            }
            
            
            // Trapdoor (Bottom Horizontal)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{2,5,4, 2,5,4, 1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, false)), biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, false)), false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		// Right wall
        		{1, 2, 4}, {1, 5, 4}, 
        		// Left wall
        		{5, 2, 4}, {5, 5, 4}, 
        		// Back wall
        		{3, 2, 6}, {3, 5, 6}, 
        		// Front wall
        		{3, 5, 2}, {3, 7, 2}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,2, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
        	// Cartography Table
        	IBlockState cartographyTableState = ModObjects.chooseModCartographyTableState(1, this.getCoordBaseMode());
            for (int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
        		{2,1,5}, 
           		})
        	{
            	this.setBlockState(world, cartographyTableState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LADDER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{4,2,5, 4,3,5, 2},  
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), false);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 4;
        	int chestW = 3;
        	int chestO = 0;
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_cartographer");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Planter grass
            this.setBlockState(world, biomeGrassState, 3,4,1, structureBB);
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{3, 5, 1}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{6, 1, 0},
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
            	int u = 2;
            	int v = 1;
            	int w = 5;
            	
            	while (u==2 && w==5)
            	{
                	u = 2+random.nextInt(3);
                	w = 3+random.nextInt(3);
            	}
            	
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
    
    
    // --- Fisher Cottage --- //
    
    public static class TaigaFisherCottage1 extends StructureVillagePieces.Village
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
        		" FFFFFFFFF",
        		"FFFFFFFFFF",
        		"FFFFFFFFFF",
        		" FFFFFFFFF",
        		"FFFFFFFFFF",
        		"F FFFFFFFF",
        		"  FFFFFFFF",
        		"F FFFFF   ",
        		" FFFFFFF  ",
        		" FFFP F   ",
        		"    PFF  F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 1;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaFisherCottage1() {}

        public TaigaFisherCottage1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaFisherCottage1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaFisherCottage1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone, part 1
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Under front door
            	{4,1,2, 4,1,2}, 
            	// Front wall
            	{3,2,2, 5,5,2}, {4,6,2, 4,6,2}, 
            	// Back wall
            	{3,1,6, 5,5,6}, {4,6,6, 4,6,6}, 
            	// Floor
            	{3,1,3, 5,1,3}, {3,1,5, 5,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }

            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front wall
            	{3,3,1, 2}, {5,3,1, 2}, 
            	// Back wall
            	{3,3,7, 0}, {5,3,7, 0}, 
            	// Interior
            	{3,3,3, 0}, {5,3,3, 0}, {4,5,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{2,2,3, 2,4,5}, 
            	// Right wall
            	{6,2,3, 6,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,1,4, 5,1,4}, 
            	{4,1,7, 4,1,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front beams
            	{2,2,1, 2,4,1}, {6,2,1, 6,4,1}, 
            	// Left beams
            	{1,2,2, 1,3,2}, {1,1,6, 1,3,6}, 
            	// Right beams
            	{7,2,2, 7,3,2}, {7,1,6, 7,3,6}, 
            	// Back beams
            	{2,1,7, 2,4,7}, {6,1,7, 6,4,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false);
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{1,4,0, 1,4,8}, 
            	{2,5,0, 2,5,8}, 
            	{3,6,0, 3,6,8}, 
            	{4,7,0, 4,7,8}, 
            	{5,6,0, 5,6,8}, 
            	{6,5,0, 6,5,8}, 
            	{7,4,0, 7,4,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Roof
            	{3,5,7, 5,5,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Fence
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs); 
            for(int[] uvw : new int[][]{
            	{4,5,2}, 
            	{3,2,5}, 
            	})
            {
        		this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Interior basin
            	{4,2,3, 4,2,5, 3}, 
            	// Back railing
            	{3,2,7, 3,2,7, 3}, {5,2,7, 5,2,7, 1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, true)), biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, true)), false);	
            }
            
            
            // Trapdoor (Bottom Horizontal)
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{3,3,5, 3,3,5, 1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, false)), biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[6], false, false)), false);	
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,2,2, 2, 1, 0}, 
            	{4,2,6, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Grass or dirt placed one block below surface.
            // This way, the surface block can be air, but if it is, the block below it will not be.
            for(int[] uvwb : new int[][]{
            	{0,1,0, 1}, {0,1,1, 1}, {0,1,2, 1}, {0,1,4, 1}, {0,1,7, 1}, {0,1,10, 1}, 
            	{1,1,0, 1}, {1,1,3, 1}, {1,1,4, 0}, {1,1,5, 0}, 
            	{2,1,0, 1}, 
            	{3,1,0, 1}, 
            	{5,1,1, 1}, 
            	{7,1,0, 1}, {7,1,1, 1}, {7,1,3, 1}, 
            	{8,1,0, 1}, {8,1,1, 1}, {8,1,2, 1}, {8,1,3, 1}, 
            	{9,1,1, 1}, {9,1,2, 1}, {9,1,3, 1}, 
            	})
            {
            	//this.setBlockState(world, Blocks.AIR.getDefaultState(), uvwb[0], uvwb[1], uvwb[2], structureBB);
            	this.setBlockState(world, uvwb[0]==0?biomeDirtState:biomeGrassState, uvwb[0], uvwb[1]-1, uvwb[2], structureBB);
            }
            
            
            // Dirt
            for(int[] uuvvww : new int[][]{
            	{1,1,2, 3,1,2}, 
            	{1,1,6, 1,1,6}, 
            	{2,1,1, 2,1,1}, 
            	{2,1,2, 2,1,6}, {6,1,2, 6,1,6}, 
            	{1,0,4, 1,0,6}, 
            	// Pond bottom
            	{1,0,7, 3,0,9}, {1,0,10, 2,0,10}, {5,0,7, 5,0,7}, 
            	{7,1,10, 8,1,10}, {9,1,7, 9,1,7}, 
            	{6,1,1, 6,1,1}, 
            	{5,1,2, 7,1,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeDirtState, biomeDirtState, false);	
            }
            
            
            // Foundation
            // Sand
        	IBlockState biomeSandState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SAND.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{4,0,11, 5,0,11}, 
            	{7,0,7, 7,0,7}, 
            	{9,0,5, 9,0,10}, {8,0,5, 8,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeSandState, biomeSandState, false);	
            }
            // Gravel
        	IBlockState biomeGravelState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{3,0,10, 3,0,10}, 
            	{4,0,8, 8,0,10}, 
            	{6,0,11, 8,0,11}, {9,0,10, 9,0,10}, {6,0,7, 6,0,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGravelState, biomeGravelState, false);	
            }
            // Clay
        	IBlockState biomeClayState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.CLAY.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{5,0,5, 5,0,5}, {6,0,4, 6,0,5}, {7,0,3, 7,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeClayState, biomeClayState, false);	
            }
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{3, 2, 1}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,2,3, 0},
            	{0,2,5, 1}, 
            	{1,2,10, 1}, 
            	{1,2,1, 1}, 
            	{6,2,0, 1}, 
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
        	
        	
        	// Barrels
    		Block barrelBlock = ModObjects.chooseModBarrelBlockState();
    		boolean isChestType=(barrelBlock==null);
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel: -1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
    			// TODO - use different barrel meta for different mods
            	
    			// Exterior
    			{8,2,10, 3,-1}, {9,2,7, 0,-1}, 
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (barrelBlock==null) {barrelBlock = Blocks.CHEST;}
    			//this.setBlockState(world, barrelState, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2])), barrelBlock.getStateFromMeta(barrelBlock==Blocks.CHEST?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.getCoordBaseMode()):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.getCoordBaseMode())), 2);
            }
            
            
            // Water
            for(int[] uuvvww : new int[][]{
            	// Interior
            	{5,2,3, 5,2,5}, 
            	// Back pond
            	{1,1,8, 3,1,9}, 
            	{3,1,7, 3,1,7}, 
            	{2,1,10, 6,1,10}, 
            	{5,1,8, 7,1,9}, 
            	{5,1,7, 5,1,7}, 
            	{7,1,7, 7,1,7}, 
            	{7,1,5, 7,1,5}, 
            	{8,1,5, 8,1,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.FLOWING_WATER.getStateFromMeta(0), Blocks.FLOWING_WATER.getStateFromMeta(0), false);	
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
        	
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{9, 2, 0},
            	{0, 2, 11},
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 4;
            	int v = 2;
            	int w = 2+random.nextInt(8);
            	
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 2, 0);
    			
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
    
    public static class TaigaFletcherHouse1 extends StructureVillagePieces.Village
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
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		"    FFF    ",
        		"    FFF    ",
        		"    FFF    ",
        		"   FFFFF   ",
        		"    FFF    ",
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
    	
        public TaigaFletcherHouse1() {}

        public TaigaFletcherHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaFletcherHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaFletcherHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone, part 1
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{4,0,3, 6,3,3}, {5,4,3, 5,4,3}, 
            	// Left wall
            	{1,0,5, 1,3,8}, {1,4,6, 1,4,7}, 
            	// Right wall
            	{9,0,5, 9,3,8}, {9,4,6, 9,4,7}, 
            	// Back wall
            	{2,0,8, 8,3,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{4,2,2, 2}, {6,2,2, 2}, 
            	// Over front door
            	{5,3,4, 0}, 
            	// Back wall
            	{3,3,7, 2}, {7,3,7, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Left wing
            	{2,0,5, 3,3,5}, {4,0,4, 4,3,4}, 
            	// Right wing
            	{7,0,5, 8,3,5}, {6,0,4, 6,3,4}, 
            	// Above entryway
            	{5,4,4, 5,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            

            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{4,0,5, 4,0,5}, {2,0,6, 3,0,7}, 
            	{6,0,5, 6,0,5}, {7,0,6, 8,0,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{3,3,2, 3,3,4}, 
            	{4,4,2, 4,4,4}, 
            	{5,5,2, 5,5,5}, 
            	{6,4,2, 6,4,4}, 
            	{7,3,2, 7,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{0,3,4, 2,3,4}, {8,3,4, 10,3,4}, 
            	{0,4,5, 10,4,5}, 
            	{0,5,6, 10,5,6}, 
            	{0,5,7, 10,5,7}, 
            	{0,4,8, 10,4,8}, 
            	{0,3,9, 10,3,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Ceiling
            	{5,0,4, 5,0,5}, {4,0,6, 6,0,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,1,6, 1}, {2,1,7, 1}, 
        		{8,1,6, 0}, {8,1,7, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{5,0,2, 5,0,2, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Planter grass
            for (int[] uvw : new int[][]{
        		{3, 0, 1}, 
        		{7, 0, 1}, 
        		})
            {
            	this.setBlockState(world, biomeGrassState, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{
        		{4, 1, 7}, 
        		{6, 1, 7}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
            
            // Carpet
            for (int[] uuvvww : new int[][]{
        		{4,2,7, 4,2,7}, 
        		{6,2,7, 6,2,7}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.CARPET.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 10), Blocks.CARPET.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 10), false); // 10 is purple
            }
            
            
            // Fletching Table
        	IBlockState fletchingTableState = ModObjects.chooseModFletchingTableState(2, this.getCoordBaseMode());
        	this.setBlockState(world, fletchingTableState, 5, 1, 7, structureBB);
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Left wing
        		{2, 2, 5}, 
        		// Right wing
        		{8, 2, 5}, 
        		// Back wall
        		{3, 2, 8}, {5, 2, 8}, {7, 2, 8}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Left planter
            	{3,0,0, 2}, {4,0,1, 1}, {3,0,2, 0}, {2,0,1, 3}, 
            	// Right planter
            	{7,0,0, 2}, {8,0,1, 1}, {7,0,2, 0}, {6,0,1, 3}, 
            	// Left wing
            	{1,2,4, 2}, {3,2,4, 2}, 
            	// Right wing
            	{7,2,4, 2}, {9,2,4, 2}, 
            	// Back wall
            	{2,2,9, 0}, {4,2,9, 0}, {6,2,9, 0}, {8,2,9, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{3, 1, 1}, 
        		{7, 1, 1}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,1,3, 2, 1, 1}, 
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
        	int chestW = 5;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_fletcher");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(5);
            	int v = 1;
            	int w = 6;
            	
            	
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
    
        
    // --- Large Farm --- //
    
    public static class TaigaLargeFarm1 extends StructureVillagePieces.Village
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
        		"  F   P   ",
        		" FFFFFPFFF",
        		"F  FFPPFF ",
        		"  FFFPFFF ",
        		" FFFFPFFF ",
        		" FFFPPFFFF",
        		" FFFPPFF  ",
        		" FFFPFFF  ",
        		" FFPPFFFF ",
        		"   PF  F  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 2;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaLargeFarm1() {}

        public TaigaLargeFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaLargeFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaLargeFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{8,1,1, 8,3,1}, 
            	{2,1,8, 2,3,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{8,4,1, -1}, {2,4,8, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
                        
            // Moist Farmland
            for(int[] uvw : new int[][]{
            	
            	{1,1,2, 4}, 
            	{1,1,3, 7}, 
            	{1,1,4, 5}, 
            	{1,1,5, 7}, 
            	
            	{2,1,1, 7}, 
            	{2,1,2, 5}, 
            	{2,1,4, 7}, 
            	{2,1,5, 5}, 
            	{2,1,6, 4}, 
            	
            	{3,1,3, 1}, 
            	{3,1,5, 6}, 
            	{3,1,7, 5}, 
            	
            	{4,1,5, 7}, 
            	{4,1,6, 7}, 
            	{4,1,7, 7}, 
            	{4,1,8, 7}, 
            	
            	{5,1,1, 7}, 
            	{5,1,2, 7}, 
            	{5,1,2, 7}, 
            	{5,1,8, 7}, 
            	
            	{6,1,1, 1}, 
            	{6,1,3, 7}, 
            	{6,1,4, 3}, 
            	{6,1,6, 5}, 
            	
            	{7,1,2, 3}, 
            	{7,1,3, 5}, 
            	{7,1,4, 7}, 
            	{7,1,5, 3}, 
            	{7,1,7, 7}, 
            	
            	{8,1,6, 7}, 
            	{8,1,7, 5}, 
            	{8,1,8, 7}, 
            	
            	})
            {
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvw[3]), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Pumpkin with dirt underneath
            for(int[] uvw : new int[][]{
            	{1,2,1}, 
            	{4,1,0}, 
            	{7,1,0}, 
            	{9,1,4}, 
            	{7,2,8}, 
            	{2,1,9}, 
            	{0,1,7}, 
            	})
            {
            	this.setBlockState(world, Blocks.PUMPKIN.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, place a pumpkin instead.
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            for(int[] uvw : new int[][]{
            	{3,1,2}, 
            	})
            {
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	if (compostBinState != null)
                {
                	this.setBlockState(world, compostBinState.getBlock().getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
                }
            	else
            	{
            		this.setBlockState(world, Blocks.PUMPKIN.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB);
            	}
            }
            
            
            // Dirt
            for(int[] uvw : new int[][]{
            	{3,0,2}, 
            	{3,0,6}, 
            	{6,0,2}, 
            	})
            {
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{2,1,3}, 
            	{3,1,6}, 
            	{7,1,6}, 
            	{6,1,2}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{3,1,4, 3,1,4, 1}, 
        		{6,1,5, 6,1,5, 0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Trapdoor (Bottom Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Back facing
            	{1,1,0, 2}, {2,1,0, 2}, {5,1,0, 2}, {6,1,0, 2},
            	// Right facing
            	{7,1,1, 1}, {8,1,2, 1}, {8,1,3, 1}, {8,1,4, 1}, {8,1,5, 1}, {9,1,6, 1}, {9,1,7, 1}, {9,1,8, 1}, 
            	// Forward facing
            	{8,1,9, 0}, {7,1,9, 0}, {5,1,9, 0}, {4,1,9, 0}, 
            	// Left facing
            	{0,1,1, 3}, {0,1,2, 3}, {0,1,3, 3}, {0,1,4, 3}, {0,1,5, 3}, {1,1,6, 3}, {2,1,7, 3}, {3,1,8, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	
            	// Villager
            	int u = 4;
            	int v = 1;
            	int w = 4;
            	
            	int s = random.nextInt(12);
            	
            	if (s==0) {u=3; w=1;}
            	else if (s<=4) {u=4; w=s;}
            	else if (s<=9) {u=5; w=s-2;}
            	else if (s<=11) {u=6; w=s-3;}
            	
            	
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
    
        
    // --- Medium Farm --- //
    
    public static class TaigaLargeFarm2 extends StructureVillagePieces.Village
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
        		" FF  F  ",
        		"FFFFFFF ",
        		" FFFFF F",
        		" FFFFF  ",
        		"PPFFFF  ",
        		"FPPFFF F",
        		"FFPPFF F",
        		"F FPPPPP",
        		"   PFFF ",
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
    	
        public TaigaLargeFarm2() {}

        public TaigaLargeFarm2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaLargeFarm2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaLargeFarm2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{6,1,0, 6,2,0}, 
            	{1,1,2, 1,2,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{6,3,0, -1}, {1,3,2, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
                        
            // Moist Farmland
            for(int[] uvw : new int[][]{
            	
            	{1,1,5, 7}, 
            	{1,1,6, 7}, 
            	
            	{2,1,5, 7}, 
            	{2,1,6, 7},
            	{2,1,7, 3},
            	
            	{3,1,3, 7},
            	{3,1,4, 0},
            	{3,1,5, 1},
            	{3,1,7, 7},
            	
            	{4,1,2, 7},
            	{4,1,4, 7},
            	{4,1,5, 7},
            	{4,1,6, 0},
            	
            	{5,1,2, 0},
            	{5,1,3, 0},
            	{5,1,4, 7},
            	{5,1,5, 7},
            	
            	})
            {
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvw[3]), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Pumpkin with dirt underneath
            for(int[] uvw : new int[][]{
            	
            	{0,1,3}, 
            	{0,1,7},
            	{2,1,1},
            	{5,1,8},
            	{7,1,6},
            	
            	})
            {
            	this.setBlockState(world, Blocks.PUMPKIN.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB); 
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, place a pumpkin instead.
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            for(int[] uvw : new int[][]{
            	{5,1,0}, 
            	})
            {
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	if (compostBinState != null)
                {
                	this.setBlockState(world, compostBinState.getBlock().getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
                }
            	else
            	{
            		this.setBlockState(world, Blocks.PUMPKIN.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB);
            	}
            }
            
            
            // Dirt
            for(int[] uvw : new int[][]{
            	{3,0,6}, 
            	{4,0,3}, 
            	})
            {
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{3,1,6}, 
            	{4,1,3}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{2,1,4, 2,1,4, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Trapdoor (Bottom Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Right facing
            	{6,1,2, 1}, {6,1,3, 1}, {6,1,4, 1}, {6,1,4, 1}, {5,1,6, 1}, {4,1,7, 1}, 
            	// Forward facing
            	{2,1,8, 0}, {3,1,8, 0}, 
            	// Left facing
            	{0,1,5, 3}, {0,1,6, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,1, 1},
            	{1,1,8, 1}, 
            	{4,1,0, 1}, 
            	{6,1,7, 1}, 
            	{7,1,3, 0}, 
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
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{7, 1, 2},
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
            	
            	// Generate decor - no trough
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	
            	// Villager
            	int u = 3;
            	int v = 1;
            	int w = 1;
            	
            	int s = random.nextInt(9);
            	
            	if (s==0) {u=1; w=4;}
            	else if (s<=2) {u=s; w=3;}
            	else if (s<=4) {u=s-1; w=2;}
            	else if (s<=11) {u=s-2; w=1;}
            	
            	
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
    
    
    // --- Library --- //
    
    public static class TaigaLibrary1 extends StructureVillagePieces.Village
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
        		"  F    F F ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFPFFFF ",
        		" FFFFPFFFFF",
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
    	
        public TaigaLibrary1() {}

        public TaigaLibrary1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaLibrary1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaLibrary1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone, part 1
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Under door
            	{5,0,2, 5,0,2}, 
            	// Front wall
            	{1,1,2, 9,4,2}, {2,5,2, 8,5,2}, {3,6,2, 7,6,2}, {4,7,2, 6,7,2}, {5,8,2, 5,8,2}, 
            	// Back wall
            	{1,1,6, 9,4,6}, {2,5,6, 8,5,6}, {3,6,6, 7,6,6}, {4,7,6, 6,7,6}, {5,8,6, 5,8,6}, 
            	// Floor
            	{2,0,3, 8,0,5}, 
            	// Stairs
            	{5,1,4, 5,2,4}, {6,1,4, 6,1,4}, 
            	// Floor 2
            	{2,3,3, 8,3,3}, {2,3,4, 4,3,4}, {2,3,5, 8,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
        	
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{4,1,0, 4,1,0}, {6,1,0, 6,1,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,4,1, 0,4,7}, 
            	{1,5,1, 1,5,7}, 
            	{2,6,1, 2,6,7}, 
            	{3,7,1, 3,7,7}, 
            	{4,8,1, 4,8,7}, 
            	{5,9,1, 5,9,7}, 
            	{6,8,1, 6,8,7}, 
            	{7,7,1, 7,7,7}, 
            	{8,6,1, 8,6,7}, 
            	{9,5,1, 9,5,7}, 
            	{10,4,1, 10,4,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front entrance
            	{4,2,0, -1}, {6,2,0, -1}, 
            	// Front wall
            	{5,7,1, 2}, 
            	// Back wall
            	{5,7,7, 0}, 
            	// First floor
            	{4,2,3, 0}, {4,2,5, 2}, {8,2,5, 2}, 
            	// Second floor
            	{3,6,4, 1}, {7,6,4, 3}, 
            	{5,7,3, 0}, {5,7,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,3, 1,4,5}, 
            	// Right wall
            	{9,1,3, 9,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{5,3,4, 5,3,4, 1}, {6,2,4, 6,2,4, 1}, {7,1,4, 7,1,4, 1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Planter grass
            for (int[] uuvvww : new int[][]{
        		{1,1,1, 3,1,1}, 
        		{7,1,1, 9,1,1}, 
        		{4,4,1, 6,4,1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);
            }
            
            
            // Carpet
            for (int[] uuvvww : new int[][]{
            	// Downstairs
        		{3,1,3, 3,1,5, GeneralConfig.useVillageColors ? this.townColor3 : 14}, // 14 is red
        		// Upstairs
        		{3,4,3, 4,4,5, GeneralConfig.useVillageColors ? this.townColor : 10}, // 10 is purple
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.CARPET.getStateFromMeta(uuvvww[6]), Blocks.CARPET.getStateFromMeta(uuvvww[6]), false);
            }
            
        	
            // Lecterns
            /*
        	blockObject = ModObjects.chooseModLectern(); Block lecternBlock = (Block) blockObject[0]; int lecternMeta = (Integer) blockObject[1];
            this.setBlockState(world, lecternBlock, lecternMeta, 2, 1, 5, structureBB);
        	*/
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwo : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,5, 1},
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
        		{2,4,3, 2,5,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,1,3, 1}, {2,1,4, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Front wall
        		{3, 2, 2}, {7, 2, 2}, {4, 5, 2}, {6, 5, 2}, 
        		// Left wall
        		{1, 2, 4}, 
        		// Right wall
        		{9, 2, 4}, 
        		// Back wall
        		{3, 2, 6}, {5, 2, 6}, {7, 2, 6}, 
        		{3, 5, 6}, {5, 5, 6}, {7, 5, 6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Left planter
            	{0,1,1, 3}, {1,1,0, 2}, {2,1,0, 2}, {3,1,0, 2}, {4,1,1, 1}, 
            	// Right planter
            	{6,1,1, 3}, {7,1,0, 2}, {8,1,0, 2}, {9,1,0, 2}, {10,1,1, 1},  
            	// Top planter
            	{3,4,1, 3}, {4,4,0, 2}, {5,4,0, 2}, {6,4,0, 2}, {7,4,1, 1},  
            	
            	// Front shutters
            	{2,2,1, 2}, {3,5,1, 2}, {5,5,1, 2}, {7,5,1, 2}, {8,2,1, 2}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{6, 5, 1}, 
        		{3, 2, 1}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{1,2,1, 1},
            	{9,2,1, 1}, 
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
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,1,2, 2, 1, 0}, 
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
        	
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{10, 1, 0},
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
            	
            	// Generate decor - no trough
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
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(2);
            	int v = random.nextBoolean()? 1:4;
            	int w = 3+random.nextInt(3);
            	
            	
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
    
    
    // --- Mason House --- //
    
    public static class TaigaMasonsHouse1 extends StructureVillagePieces.Village
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
        		"F       F",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
        		" FFFFFFFF",
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
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaMasonsHouse1() {}

        public TaigaMasonsHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaMasonsHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaMasonsHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Entrance
            	{3,1,2, 5,4,2}, {4,5,2, 4,5,2}, 
            	// Front wall
            	{1,1,3, 2,2,3}, {2,3,3, 2,3,3}, {6,1,3, 7,2,3}, {6,3,3, 6,3,3}, 
            	// Back wall
            	{1,1,6, 7,2,6}, {3,3,6, 7,3,6}, {3,4,6, 6,4,6}, {4,5,6, 4,5,6}, 
            	// Right wall
            	{1,1,4, 1,1,5}, 
            	// Left wall
            	{7,1,4, 7,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,2,1, 0,2,7}, 
            	{1,3,1, 1,3,7}, 
            	{2,4,1, 2,4,7}, 
            	{3,5,1, 3,5,7}, 
            	{4,6,1, 4,6,7}, 
            	{5,5,1, 5,5,7}, 
            	{6,4,1, 6,4,7}, 
            	{7,3,1, 7,3,7}, 
            	{8,2,1, 8,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front wall
            	{3,3,1, 2}, {5,3,1, 2}, 
            	// Interior
            	{4,4,5, 2},  
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,1,3, 5,1,3}, {2,1,4, 6,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{
            	// Posts
            	{0,1,1}, {8,1,1}, 
            	{0,1,7}, {8,1,7}, 
            	// Interior
            	// Right wall
            	{1,2,4}, {1,2,5}, 
            	// Left wall
            	{7,2,4}, {7,2,5}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entrance
        		{4,1,1, 4,1,1, 3}, 
        		// interior
        		{5,2,3, 5,2,3, 0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
        	
            // Stone Cutter
        	// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	IBlockState stonecutterState = ModObjects.chooseModStonecutterState(2, this.getCoordBaseMode());
            this.setBlockState(world, stonecutterState, 4, 2, 5, structureBB);
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		{4, 3, 6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Left planter
            	{2,1,0, 2}, {3,1,1, 1}, {2,1,2, 0}, {1,1,1, 3}, 
            	// Right planter
            	{6,1,0, 2}, {7,1,1, 1}, {6,1,2, 0}, {5,1,1, 3}, 
            	// Back shutter
            	{3,3,7, 0}, {5,3,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Spruce sapling in a flower pot
            int flowerPotU = 3;
            int flowerPotV = 2;
            int flowerPotW = 3;
            int xWithOffset = this.getXWithOffset(flowerPotU, flowerPotW);
            int yWithOffset = this.getYWithOffset(flowerPotV);
            int zWithOffset = this.getZWithOffset(flowerPotU, flowerPotW);
            this.setBlockState(world, Blocks.FLOWER_POT.getDefaultState(), flowerPotU, flowerPotV, flowerPotW, structureBB);
            
            // This is here just in case a flower pot can't be placed at the given position
            TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)world.getTileEntity(new BlockPos(xWithOffset, yWithOffset, zWithOffset));
            
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.SAPLING.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            if (tileentityflowerpot != null)
            {
            	// Sets the flower pot's item and meta
                tileentityflowerpot.setItemStack(new ItemStack( // This method extracts the item and meta from the itemstack
                		Item.getItemFromBlock(biomeSaplingState.getBlock()), // Item
                		1, // Amount (unused here)
                		biomeSaplingState.getBlock().getMetaFromState(biomeSaplingState))); // Meta
                tileentityflowerpot.markDirty();
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
            
            /*
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{4, GROUND_LEVEL, 0}, 
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
        	*/
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 4;
            	int v = 2;
            	int w = 5;
            	
            	while (u==4 && w==5)
            	{
                	u = 2+random.nextInt(5);
                	w = 4+random.nextInt(2);
            	}
            	
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
    
    public static class TaigaMediumHouse1 extends StructureVillagePieces.Village
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
        		"  FFFFFP",
        		"  FFF  P",
        		"  FFF PP",
        		"  FFF PF",
        		" FFFFFP ",
        		" F   FPF",
        		"     FP ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 11;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 3; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaMediumHouse1() {}

        public TaigaMediumHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaMediumHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaMediumHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Dirt with dirt foundation and four air blocks above
            for(int[] uvw : new int[][]{
            	{6,2,6}, 
            	})
            {
    			this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvw[0], uvw[1]-1, uvw[2], structureBB);
    			this.fillWithAir(world, structureBB, uvw[0], uvw[1]+1, uvw[2], uvw[0], uvw[1]+4, uvw[2]);
    			this.setBlockState(world, biomeDirtState, uvw[0], uvw[1], uvw[2], structureBB);
    			
            }
            
        	
        	// Grass with dirt foundation
            for(int[] uvw : new int[][]{
            	{0,1,0}, {0,1,1}, {0,1,2}, {0,1,3}, {0,1,4}, {0,1,5}, {0,1,6}, 
            	{1,1,0}, {1,1,3}, {1,1,4}, {1,1,5}, {1,1,6}, 
            	{2,0,0}, {2,0,1}, 
            	{3,0,0}, {3,0,1}, 
            	{4,0,1}, 
            	{5,1,3}, {5,1,4}, {5,1,5}, 
            	{6,1,5}, 
            	{7,1,0}, {7,1,2}, 
            	})
            {
    			this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvw[0], uvw[1]-1, uvw[2], structureBB);
    			this.fillWithAir(world, structureBB, uvw[0], uvw[1]+1, uvw[2], uvw[0], uvw[1]+4, uvw[2]);
    			this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Line the basement to make sure it isn't over a huge ravine
            for(int[] uvw : new int[][]{
            	{2,-1,6}, {3,-1,6}, {4,-1,6}, 
            	{2,-1,5}, {3,-1,5}, {4,-1,5}, 
            	{2,-1,4}, {3,-1,4}, {4,-1,4}, 
            	// Back wall
            	{2,1,7}, {3,1,7}, {4,1,7},
            	})
            {
    			this.replaceAirAndLiquidDownwards(world, biomeFillerState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Air
            for(int[] uuvvww : new int[][]{
            	// Above stair
            	{4,2,0, 4,2,0}, 
            	// Basement
            	{2,1,4, 4,1,6}, {3,0,5, 3,0,6}, {3,0,4, 4,0,4}, 
            	// Basement entrance
            	{3,2,3, 3,2,4}, 
            	{3,1,3, 3,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);	
            }
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Below lower door
            	{3,0,2, 3,0,2},
            	// Front wall
            	{2,1,2, 4,4,2}, 
            	// Back wall
            	{2,3,4, 4,4,4}, 
            	// Left wall
            	{2,0,3, 2,4,3}, 
            	// Right wall
            	{4,0,3, 4,4,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Basement bench
        		{2,0,5, 1}, {2,0,6, 1}, 
        		// Walkway up to loft
        		{6,3,6, 1}, 
        		{5,4,6, 1}, 
        		{4,5,6, 1}, 
        		// Loft bottom rim
        		{1,5,1, 7}, {2,5,1, 7}, {3,5,1, 7}, {4,5,1, 7}, {5,5,1, 7}, 
        		{1,5,5, 6}, {2,5,5, 6}, {3,5,5, 6}, {4,5,5, 6}, {5,5,5, 6}, 
        		{1,5,2, 4}, {1,5,3, 4}, {1,5,4, 4}, 
        		{5,5,2, 5}, {5,5,3, 5}, {5,5,4, 5}, 
        		// Loft chairs
        		{4,6,2, 2}, {4,6,4, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,6,1, 4,8,1}, {3,9,1, 3,9,1}, 
            	// Back wall
            	{2,6,5, 4,8,5}, {3,9,5, 3,9,5}, 
            	// Left wall
            	{1,6,2, 1,7,4}, 
            	// Right wall
            	{5,6,2, 5,7,4}, 
            	// Floor
            	{2,5,2, 4,5,4}, {3,5,5, 3,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }

            
            // Top wood slab
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Back stairs
            	{5,3,6, 5,3,6}, {4,4,6, 4,4,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{
            	// Posts
            	{3,3,6}, {3,4,6}, 
            	// Table
            	{4,6,3}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0],uvw[1],uvw[2], structureBB);
            }
        	
        	
            // Wooden pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{4,7,3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            

            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,6,1, 1,7,1}, {5,6,1, 5,7,1}, 
            	{1,6,5, 1,7,5}, {5,6,5, 5,7,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,7,0, 0,7,6}, 
            	{1,8,0, 1,8,6}, 
            	{2,9,0, 2,9,6}, 
            	{3,10,0, 3,10,6}, 
            	{4,9,0, 4,9,6}, 
            	{5,8,0, 5,8,6}, 
            	{6,7,0, 6,7,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Basement exterior
            	{3,3,1, 2}, 
            	// Basement interior
            	{3,3,3, 0}, 
            	// Left exterior
            	{1,4,3, 3}, 
            	// Right exterior
            	{5,4,3, 1}, 
            	// Back exterior
            	{3,8,6, 0}, 
            	// Interior
            	{3,8,2, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entrance
        		{4,1,0, 4,1,0, 0}, {5,2,0, 5,2,0, 0}, 
        		// Basement
        		{3,0,3, 3,0,3, 2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		{3, 7, 1}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{2,7,0, 2}, {4,7,0, 2}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,2, 2, 1, 1}, 
            	{3,6,5, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{1,3,1, 1},
            	{1,3,2, 1}, 
            	{5,3,2, 1}, 
            	{5,3,1, 0}, 
            	{7,3,1, 0}, 
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
            	{4,0,5, 2, GeneralConfig.useVillageColors ? this.townColor : 10}, // 10 is purple
            	{2,6,3, 0, GeneralConfig.useVillageColors ? this.townColor2 : 11}, // 11 is blue
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
        	int chestV = 0;
        	int chestW = 4;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
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
        	
        	
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{2,6,4, -1, 0},
	        			{3,6,3, -1, 0},
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
    
    public static class TaigaMediumHouse2 extends StructureVillagePieces.Village
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
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"FFFFF  ",
        		"FFFFF  ",
        		"PFFP   ",
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
    	
        public TaigaMediumHouse2() {}

        public TaigaMediumHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaMediumHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaMediumHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Below lower door
            	{3,0,3, 3,0,3}, 
            	// Front wall
            	{1,1,3, 5,8,3}, 
            	// Back wall
            	{1,1,6, 5,8,6}, 
            	// Left wall
            	{1,1,4, 1,9,5}, 
            	// Right wall
            	{5,1,4, 5,9,5}, 
            	// Floor
            	{2,0,4, 4,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Torches, part 1
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Lower antechamber
            	{3,3,2, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{2,1,1, 2,2,2}, 
            	// Right wall
            	{4,1,1, 4,2,2}, 
            	// Floor
            	{3,0,1, 3,0,2}, 
            	// Above door
            	{3,3,1, 3,3,1}, 
            	// Top of stairs
            	{0,4,5, 0,4,5}, 
            	// Loft floor
            	{2,4,4, 4,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Torches, part 2
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Lower level exterior
            	{3,3,0, 2}, 
            	// Lower level interior
            	{3,3,4, 0}, 
            	// Floor 2 entrance
            	{0,6,4, 3}, 
            	// Floor 2 interior
            	{2,9,4, 1}, {2,9,5, 1}, {4,9,4, 3}, {4,9,5, 3}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
            // Stairway
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Basement bench
        		{0,1,1, 3}, {0,1,2, 6}, 
        		{0,2,2, 3}, {0,2,3, 6}, 
        		{0,3,3, 3}, {0,3,4, 6}, 
        		{0,4,4, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{1,2,0, 1,2,2}, 
            	{2,3,0, 2,3,2}, 
            	{3,4,0, 3,4,2}, 
            	{4,3,0, 4,3,2}, 
            	{5,2,0, 5,2,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{0,8,2, 6,8,2}, 
            	{0,9,3, 6,9,3}, 
            	{0,10,4, 6,10,4}, 
            	{0,10,5, 6,10,5}, 
            	{0,9,6, 6,9,6}, 
            	{0,8,7, 6,8,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		{2,6,3}, {4,6,3}, 
        		{2,6,6}, {4,6,6}, 
        		{2,2,6}, {4,2,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{1,6,2, 2}, {3,6,2, 2}, {5,6,2, 2}, 
            	{1,6,7, 0}, {3,6,7, 0}, {5,6,7, 0}, 
            	{1,2,7, 0}, {3,2,7, 0}, {5,2,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 2, 1, 0}, 
            	{3,1,3, 2, 1, 0}, 
            	{1,5,5, 3, 1, 0}, 
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
            	{3,1,5, 1, GeneralConfig.useVillageColors ? this.townColor2 : 11}, // 11 is blue
            	{3,5,4, 3, GeneralConfig.useVillageColors ? this.townColor : 10}, // 10 is purple
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
            
            
            // Crafting Table
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 4,1,5, structureBB);
            
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 5;
        	int chestW = 4;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{0, GROUND_LEVEL, -1}, 
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
	        			{4,1,4, -1, 0},
	        			{3,5,5, -1, 0},
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
    
    public static class TaigaMediumHouse3 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		"F FFF   FFF  ",
        		"  FFF F FFF  ",
        		"   P  F  P   ",
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
    	
        public TaigaMediumHouse3() {}

        public TaigaMediumHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaMediumHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaMediumHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left wall
            	{1,1,3, 1,3,6}, {1,4,4, 1,4,5}, 
            	// Right wall
            	{11,1,3, 11,3,6}, {11,4,4, 11,4,5}, 
            	// Front wall
            	{5,1,3, 7,3,3}, 
            	// Back wall
            	{2,1,6, 10,3,6}, 
            	// Left entrance
            	{3,0,1, 3,0,2}, 
            	// Right entrance
            	{9,0,1, 9,0,2}, 
            	// Floor
            	{2,0,3, 4,0,5}, {5,0,4, 7,0,5}, {8,0,3, 10,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks, part 1
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left front wall
            	{2,1,1, 4,3,1}, {3,4,1, 3,4,2}, 
            	// Rigth front wall
            	{8,1,1, 10,3,1}, {9,4,1, 9,4,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Left front
            	{3,3,0, 2}, 
            	// Right front
            	{9,3,0, 2}, 
            	// Interior
            	{3,3,2, 0}, {6,3,4, 0}, {9,3,2, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks, part 2
            for(int[] uuvvww : new int[][]{
            	// Left entrance
            	{2,1,2, 2,3,2}, {4,1,2, 4,3,2}, 
            	// Right entrance
            	{8,1,2, 8,3,2}, {10,1,2, 10,3,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chairs
        		{5,1,5, 1}, {7,1,5, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Left awning
            	{1,3,0, 1,3,2}, 
            	{2,4,0, 2,4,2}, 
            	{3,5,0, 3,5,3}, 
            	{4,4,0, 4,4,2}, 
            	{5,3,0, 5,3,2}, 
            	// Right awning
            	{7,3,0, 7,3,2}, 
            	{8,4,0, 8,4,2}, 
            	{9,5,0, 9,5,3}, 
            	{10,4,0, 10,4,2}, 
            	{11,3,0, 11,3,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{0,3,2, 0,3,2}, {12,3,2, 12,3,2}, 
            	{0,4,3, 12,4,3}, 
            	{0,5,4, 12,5,4}, 
            	{0,5,5, 12,5,5}, 
            	{0,4,6, 12,4,6}, 
            	{0,3,7, 12,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Back windows
        		{3,2,6}, {6,2,6}, {9,2,6}, 
        		// Front window
        		{6,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Front Shutters
            	{5,2,2, 2}, {7,2,2, 2}, 
            	// Back Shutters
            	{2,2,7, 0}, 
            	{4,2,7, 0}, 
            	{5,2,7, 0}, 
            	{7,2,7, 0}, 
            	{8,2,7, 0}, 
            	{10,2,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{
            	// Tables
            	{3,1,5}, 
            	{6,1,5}, 
            	{9,1,5}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Flat)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Tables
            	{3,2,5, 1}, 
            	{6,2,5, 2}, 
            	{9,2,5, 2}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, false)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 2, 1, 1}, 
            	{9,1,1, 2, 1, 0}, 
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
            	{2,1,4, 2, GeneralConfig.useVillageColors ? this.townColor2 : 11}, // 11 is blue
            	{10,1,4, 2, GeneralConfig.useVillageColors ? this.townColor2 : 11}, // 11 is blue
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
            
            
            // Crafting Table
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 10,1,3, structureBB);
            
            
            // Bookshelves
            for (int[] uuvvww : new int[][]{
        		{2,4,4, 2,4,5}, 
        		{10,4,4, 10,4,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
            
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 1;
        	int chestW = 3;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,6, 1},
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
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{6, 1, 1},
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
            	
            	// Generate decor - no trough
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
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{9, GROUND_LEVEL, -1}, 
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
	        			{4,1,4, -1, 0},
	        			{8,1,4, -1, 0},
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
    
    
    // --- Medium House 4 --- //
    
    public static class TaigaMediumHouse4 extends StructureVillagePieces.Village
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
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
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
    	
        public TaigaMediumHouse4() {}

        public TaigaMediumHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaMediumHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaMediumHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front door entrance
            	{4,0,1, 4,0,1}, 
            	// Front wall
            	{2,1,1, 6,2,1}, {3,3,1, 5,3,1}, 
            	// Front wall
            	{2,1,7, 6,2,7}, {3,3,7, 5,3,7}, 
            	// Left wall
            	{1,1,2, 1,2,6}, 
            	// Right wall
            	{7,1,2, 7,2,6}, 
            	// Floor
            	{4,0,3, 4,0,3}, 
            	// Chimney
            	{4,2,4, 4,3,4}, {3,5,3, 5,5,3}, {3,5,5, 5,5,5}, {3,5,4, 3,5,4}, {5,5,4, 5,5,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{4,0,2, 4,0,2}, 
            	{2,0,2, 3,0,6}, 
            	{5,0,2, 6,0,6}, 
            	{4,0,5, 4,0,6}, 
            	{4,1,6, 4,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            

            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,1,1, 1,2,1}, {2,3,1, 2,3,1}, {6,3,1, 6,3,1}, {7,1,1, 7,2,1}, 
            	{1,1,7, 1,2,7}, {7,1,7, 7,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,2,0, 0,2,8}, 
            	{1,3,0, 1,3,8}, 
            	{2,4,0, 2,4,8}, 
            	{3,5,0, 3,5,2}, {3,5,6, 3,5,8}, 
            	{4,5,0, 4,5,2}, {4,5,6, 4,5,8}, 
            	{5,5,0, 5,5,2}, {5,5,6, 5,5,8}, 
            	{6,4,0, 6,4,8}, 
            	{7,3,0, 7,3,8}, 
            	{8,2,0, 8,2,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	// Front
            	{3,4,1, 5,4,1}, 
            	// Back
            	{3,4,7, 5,4,7}, 
            	{2,3,7, 2,3,7}, {6,3,7, 6,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{4,4,0, 2}, 
            	// Back
            	{4,4,8, 0}, 
            	// Chimney
            	{4,2,3, 2}, {3,2,4, 3}, {5,2,4, 1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chairs
        		{2,1,2, 1}, {2,1,3, 1}, 
        		{6,1,2, 0}, {6,1,3, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chimney bottom
        		{3,4,3, 5,4,3, 7}, {3,4,5, 5,4,5, 6}, {3,4,4, 3,4,4, 4}, {5,4,4, 5,4,4, 5}, 
        		// Chimney top
        		{3,6,3, 5,6,3, 3}, {3,6,5, 5,6,5, 2}, {3,6,4, 3,6,4, 0}, {5,6,4, 5,6,4, 1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Back windows
        		{3,2,7}, {5,2,7}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Back Shutters
            	{2,2,8, 0}, 
            	{4,2,8, 0}, 
            	{6,2,8, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,4, 2}, 
            	})
            {
                //this.setBlockState(world, Blocks.FURNACE, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.FURNACE.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.getCoordBaseMode())), 2);
            }
        	
            
            // Campfire
            IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), this.getCoordBaseMode());
            for(int[] uvw : new int[][]{
            	{4,4,4}, 
            	})
            {
        		this.setBlockState(world, campfireState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Furnace
            	{3,1,4, 3}, 
            	{4,1,5, 0}, 
            	{5,1,4, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{4,1,1, 2, 1, 0}, 
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
            	{3,1,6, 1, GeneralConfig.useVillageColors ? this.townColor : 10}, // 10 is purple
            	{5,1,6, 3, GeneralConfig.useVillageColors ? this.townColor : 10}, // 10 is purple
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
	        			{3,1,2, -1, 0},
	        			{6,1,4, -1, 0},
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
    
    public static class TaigaShepherdsHouse1 extends StructureVillagePieces.Village
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
        		"  FFFFFFF  ",
        		"F FFFFFFF  ",
        		"  FFFFFFF  ",
        		"  FFFFFFF  ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" F F F F F ",
        		"   P   P   ",
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
    	
        public TaigaShepherdsHouse1() {}

        public TaigaShepherdsHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaShepherdsHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaShepherdsHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,2, 9,2,2}, 
            	// Back wall
            	{1,1,5, 9,2,5}, 
            	// Left wall
            	{1,1,3, 1,2,4}, 
            	// Right wall
            	{9,1,3, 9,2,4}, 
            	// Pen
            	{2,1,6, 2,1,9}, {3,1,9, 7,1,9}, {8,1,6, 8,1,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front left wall
            	{2,3,2, 3,4,2}, {3,5,2, 3,5,2}, 
            	// Between
            	{5,3,2, 5,3,2}, 
            	// Front right wall
            	{7,3,2, 8,4,2}, {7,5,2, 7,5,2}, 
            	// Back left wall
            	{2,3,5, 4,4,5}, {3,5,5, 3,5,5}, 
            	// Back right wall
            	{6,3,5, 8,4,5}, {7,5,5, 7,5,5}, 
            	// Leftmost wall
            	{1,3,2, 1,4,5}, 
            	// Rightmost wall
            	{9,3,2, 9,4,5}, 
            	// Floor
            	{2,1,3, 8,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{
            	// Pen
            	{2,2,6}, {2,2,7}, {2,2,8}, {2,2,9}, 
            	{3,2,9}, {4,2,9}, {5,2,9}, {6,2,9}, {7,2,9}, 
            	{8,2,6}, {8,2,7}, {8,2,8}, {8,2,9}, 
            	// Tables
            	{8,2,3}, {8,2,4}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
            
            // Logs (Along), part 1
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Center roof beam
            	{5,4,0, 5,4,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Left Front
            	{3,5,1, 2}, 
            	// Right Front
            	{7,5,1, 2}, 
            	// Interior
            	{4,4,3, 3}, {6,4,3, 1}, 
            	// Yard
            	{2,3,9, -1}, {8,3,9, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks, part 2
            for(int[] uuvvww : new int[][]{
            	// Front left wall
            	{4,3,2, 4,4,2}, 
            	// Front right wall
            	{6,3,2, 6,4,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            

            // Logs (Vertical)
            for(int[] uuvvww : new int[][]{
            	// Posts
            	{1,1,1, 1,3,1}, {5,1,1, 5,3,1}, {9,1,1, 9,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along), part 2
            for (int[] uw : new int[][]{
            	// Roof
            	{0,3,0, 0,3,6}, 
            	{1,4,0, 1,4,6}, 
            	{2,5,0, 2,5,6}, 
            	{3,6,0, 3,6,6}, 
            	{4,5,0, 4,5,6}, 
            	{6,5,0, 6,5,6}, 
            	{7,6,0, 7,6,6}, 
            	{8,5,0, 8,5,6}, 
            	{9,4,0, 9,4,6}, 
            	{10,3,0, 10,3,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	// Front
            	{2,4,1, 4,4,1}, {6,4,1, 8,4,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Entrance
        		{3,1,1, 3,1,1, 3}, {7,1,1, 7,1,1, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
        	
        	// Grass
            for (int[] uw : new int[][]{
            	// Front
            	{3,1,6, 7,1,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeGrassState, biomeGrassState, false);
            }
            
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Back windows
        		{3,3,5}, {7,3,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Back Shutters
            	{2,3,6, 0}, 
            	{4,3,6, 0}, 
            	{6,3,6, 0}, 
            	{8,3,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uvwo[3], true, true)), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Wooden Pressure Plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{8,3,3}, {8,3,4}, 
        		})
            {
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Loom
        	IBlockState loomState = ModObjects.chooseModLoom(1, this.getCoordBaseMode());
            for(int[] uvw : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Back Shutters
            	{2,2,3}, 
            	{2,2,4}, 
            	})
            {
            	this.setBlockState(world, loomState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		{4,2,3, (GeneralConfig.useVillageColors ? this.townColor : 10)}, // 10 is purple
        		{5,2,4, (GeneralConfig.useVillageColors ? this.townColor : 10)}, // 10 is purple
        		{6,2,3, (GeneralConfig.useVillageColors ? this.townColor : 10)}, // 10 is purple
        		{4,2,4, (GeneralConfig.useVillageColors ? this.townColor4 : 0)}, // 0 is white
        		{5,2,3, (GeneralConfig.useVillageColors ? this.townColor4 : 0)}, // 0 is white
        		{6,2,4, (GeneralConfig.useVillageColors ? this.townColor4 : 0)}, // 0 is white
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,2, 2, 1, 1}, 
            	{7,2,2, 2, 1, 0}, 
            	{5,2,5, 0, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0,1,8},
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
            	
            	// Generate decor - no trough
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
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{3, GROUND_LEVEL, -1}, 
        		{7, GROUND_LEVEL, -1}, 
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
            	int u = 3+random.nextInt(5);
            	int v = 2;
            	int w = 3+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 3, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntity(entityvillager);
                
                // Sheep in the yard
            	for (int[] uvw : new int[][]{
        			{6, 2, 8},
        			})
        		{
            		EntityLiving animal = new EntitySheep(world);
            		IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2]))), null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
            		
                    animal.setLocationAndAngles((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D, random.nextFloat()*360F, 0.0F);
                    world.spawnEntity(animal);
                    
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
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
        
    // --- Small Farm --- //
    
    public static class TaigaSmallFarm1 extends StructureVillagePieces.Village
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
        		"FFFF FF",
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
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaSmallFarm1() {}

        public TaigaSmallFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaSmallFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaSmallFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvw : new int[][]{
            	{0,1,0}, {0,1,1}, {0,1,4}, {0,1,6}, 
            	{3,1,7}, 
            	{5,1,7}, 
            	{6,1,1}, {6,1,4}, {6,1,5}, {6,1,7}, 
            	})
            {
            	this.setBlockState(world, biomeCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB); 
            }
        	
            
            // Mossy Cobblestone
        	IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.MOSSY_COBBLESTONE.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uvw : new int[][]{
            	{0,1,2}, {0,1,3}, {0,1,5}, {0,1,7}, 
            	{1,1,0}, {1,1,5}, {1,1,7}, 
            	{2,1,0}, {2,1,3}, {2,1,7}, 
            	{3,1,1}, 
            	{4,1,0}, {4,1,7}, 
            	{5,1,0}, {5,1,5}, 
            	{6,1,0}, {6,1,2}, {6,1,3}, {6,1,6}, 
            	})
            {
            	this.setBlockState(world, biomeMossyCobblestoneState, uvw[0], uvw[1], uvw[2], structureBB); 
            }
        	
            
            // Dirt
            for(int[] uvw : new int[][]{
            	{1,0,1}, {1,1,3}, {1,0,6}, 
            	{2,1,6}, 
            	{3,0,3}, 
            	{4,1,3}, 
            	{5,0,1}, {5,1,3}, 
            	})
            {
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{2,2,0, 2,2,0}, 
            	{4,2,0, 4,2,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,3,0, -1}, {4,3,0, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
                        
            // Moist Farmland
            for(int[] uvw : new int[][]{
            	{1,1,2, 7}, 
            	{1,1,4, 7}, 
            	{2,1,1, 7}, 
            	{2,1,2, 7}, 
            	{2,1,4, 7}, 
            	{2,1,5, 7}, 
            	{3,1,2, 7}, 
            	{3,1,4, 7}, 
            	{3,1,5, 7}, 
            	{3,1,6, 7}, 
            	{4,1,1, 7}, 
            	{4,1,2, 7}, 
            	{4,1,4, 7}, 
            	{4,1,6, 7}, 
            	{5,1,2, 7}, 
            	{5,1,4, 7}, 
            	{5,1,6, 7}, 
            	})
            {
            	this.setBlockState(world, Blocks.WHEAT.getStateFromMeta(uvw[3]), uvw[0], uvw[1]+1, uvw[2], structureBB); 
            	this.setBlockState(world, Blocks.FARMLAND.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Pumpkin 
            for(int[] uvw : new int[][]{
            	{1,2,3}, 
            	{2,2,6}, 
            	{4,2,3}, 
            	{5,2,3}, 
            	})
            {
            	this.setBlockState(world, Blocks.PUMPKIN.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            }
            
            
            // Pumpkin stem
            for(int[] uvw : new int[][]{
            	{1,2,2}, {1,2,4}, 
            	{2,2,1}, {2,2,2}, {2,2,4}, {2,2,5}, 
            	{3,2,2}, {3,2,4}, {3,2,5}, {3,2,6}, 
            	{4,2,1}, {4,2,2}, {4,2,4}, {4,2,6}, 
            	{5,2,2}, {5,2,4}, {5,2,6}, 
            	})
            {
            	this.setBlockState(world, Blocks.PUMPKIN_STEM.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB); // Random pumpkin orientation
            }
            
            
            // Water
            for(int[] uvw : new int[][]{
            	{1,1,1}, 
            	{1,1,6}, 
            	{3,1,3}, 
            	{4,1,5}, 
            	{5,1,1}, 
            	})
            {
            	this.setBlockState(world, Blocks.FLOWING_WATER.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB); 
            }
            
            
            // Attempt to add GardenCore Compost Bins. If this fails, place a pumpkin instead.
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            for(int[] uvw : new int[][]{
            	{2,2,3}, 
            	})
            {
            	this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
            	if (compostBinState != null)
                {
                	this.setBlockState(world, compostBinState.getBlock().getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
                }
            	else
            	{
            		this.setBlockState(world, Blocks.PUMPKIN.getStateFromMeta(random.nextInt(3)), uvw[0], uvw[1], uvw[2], structureBB);
            	}
            }
            
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Walkway
        		{3,1,0, 3,1,0, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	
            	// Villager
            	int u = 1+random.nextInt(5);
            	int v = 2;
            	int w = random.nextBoolean() ? 2:4;
            	
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
    
    
    // --- Small House 1 --- //
    
    public static class TaigaSmallHouse1 extends StructureVillagePieces.Village
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
        		" F   F ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"   P   ",
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
    	
        public TaigaSmallHouse1() {}

        public TaigaSmallHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaSmallHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaSmallHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            	// Floor
            	{2,1,3, 4,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	// Front
            	{2,5,1, 4,5,2}, 
            	// Back
            	{2,5,6, 4,5,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{2,1,1, 2,1,1}, {4,1,1, 4,1,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front posts
            	{2,2,1, -1}, {4,2,1, -1}, 
            	// Interior
            	{3,5,3, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,2, 4,4,2}, {3,6,2, 3,6,2}, 
            	// Back wall
            	{2,1,6, 4,4,6}, {3,6,6, 3,6,6}, 
            	// Left wall
            	{1,2,3, 1,5,5}, 
            	// Right wall
            	{5,2,3, 5,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            

            // Logs (Vertical)
            for(int[] uuvvww : new int[][]{
            	{1,1,1, 1,4,2}, {5,1,1, 5,4,2}, 
            	{1,1,6, 1,4,7}, {5,1,6, 5,4,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,4,0, 0,4,8}, 
            	{1,5,0, 1,5,8}, 
            	{2,6,0, 2,6,8}, 
            	{3,7,0, 3,7,8}, 
            	{4,6,0, 4,6,8}, 
            	{5,5,0, 5,5,8}, 
            	{6,4,0, 6,4,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chair
        		{2,2,4, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
        	// Hanging Sign (Blank)
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.WALL_SIGN.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		// Chair arm rests
        		{2,2,3, 2}, {2,2,5, 0}, 
        		})
            {
                int signX = this.getXWithOffset(uvwo[0], uvwo[2]);
                int signY = this.getYWithOffset(uvwo[1]);
                int signZ = this.getZWithOffset(uvwo[0], uvwo[2]);
                
            	world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(uvwo[3], this.getCoordBaseMode().getHorizontalIndex(), true)), 2); // Facing away from you
            }
        	
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front stairs
        		{3,1,1, 3,1,1, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Back windows
        		{1,3,4}, {3,3,6}, {5,3,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{0,3,3, 3}, {0,3,5, 3}, 
            	{6,3,3, 1}, {6,3,5, 1}, 
            	{2,3,7, 0}, {4,3,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,2,2, 2, 1, 0}, 
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
            	{4,2,4, 2, GeneralConfig.useVillageColors ? this.townColor : 10}, // 10 is purple
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
	        			{4,2,3, -1, 0},
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
    
    public static class TaigaSmallHouse2 extends StructureVillagePieces.Village
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
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaSmallHouse2() {}

        public TaigaSmallHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaSmallHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaSmallHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            	// Floor
            	{3,1,2, 3,1,4}, {2,1,3, 2,1,3}, {4,1,3, 4,2,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{2,1,0, 2,1,0}, {4,1,0, 4,1,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front posts
            	{2,2,0, -1}, {4,2,0, -1}, 
            	// Interior
            	{4,3,3, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,1, 5,4,1}, {2,5,1, 4,5,1}, {3,6,1, 3,6,1}, 
            	// Back wall
            	{1,1,5, 5,4,5}, {2,5,5, 4,5,5}, {3,6,5, 3,6,5}, 
            	// Left wall
            	{1,1,2, 1,4,4}, 
            	// Right wall
            	{5,1,2, 5,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            

            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floors
            	{2,1,2, 2,1,2}, {4,1,2, 4,1,2}, 
            	{2,1,4, 2,1,4}, {4,1,4, 4,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,4,0, 0,4,6}, 
            	{1,5,0, 1,5,6}, 
            	{2,6,0, 2,6,6}, 
            	{3,7,0, 3,7,6}, 
            	{4,6,0, 4,6,6}, 
            	{5,5,0, 5,5,6}, 
            	{6,4,0, 6,4,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Chair
        		{4,2,2, 0}, {4,2,4, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front stairs
        		{3,1,0, 3,1,0, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Back windows
        		{1,3,3}, {3,3,5}, {5,3,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{0,3,2, 3}, {0,3,4, 3}, 
            	{6,3,2, 1}, {6,3,4, 1}, 
            	{2,3,6, 0}, {4,3,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
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
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,3, 2, GeneralConfig.useVillageColors ? this.townColor : 10}, // 10 is purple
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
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{1,1,0, 1},
            	{5,1,0, 0},
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
    
    public static class TaigaSmallHouse3 extends StructureVillagePieces.Village
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
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"  FFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaSmallHouse3() {}

        public TaigaSmallHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static TaigaSmallHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaSmallHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            

            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floors
            	{1,0,1, 1,3,1}, {5,0,1, 5,3,1}, 
            	{1,0,5, 1,3,5}, {5,0,5, 5,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
        	
        	// Cobblestone, part 1
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,3,1, 4,4,1}, {3,5,1, 3,5,1}, 
            	// Back wall
            	{2,3,5, 4,4,5}, {3,5,5, 3,5,5}, 
            	// Left wall
            	{1,0,2, 1,3,4}, 
            	// Right wall
            	{5,0,2, 5,3,4}, 
            	// Floor
            	{2,0,1, 4,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,3,0, 0,3,6}, 
            	{1,4,0, 1,4,6}, 
            	{2,5,0, 2,5,6}, 
            	{3,6,0, 3,6,6}, 
            	{4,5,0, 4,5,6}, 
            	{5,4,0, 5,4,6}, 
            	{6,3,0, 6,3,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{2,0,0, 2,1,0}, {4,0,0, 4,1,0}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front posts
            	{2,2,0, -1}, {4,2,0, -1}, 
            	// Interior
            	{4,4,2, 0}, {4,4,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1, 4,2,1}, 
            	// Back wall
            	{2,1,5, 4,2,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Bench
        		{2,1,3, 1}, {2,1,4, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front stairs
        		{3,0,0, 3,0,0, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Back windows
        		{1,2,3}, {3,2,5}, {5,2,3}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{0,2,2, 3}, {0,2,4, 3}, 
            	{6,2,2, 1}, {6,2,4, 1}, 
            	{2,2,6, 0}, {4,2,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
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
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,3, 2, GeneralConfig.useVillageColors ? this.townColor2 : 11}, // 11 is blue
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
            
            
            // Crafting Table
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 2,1,2, structureBB);
            
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
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
    
    
    // --- Small House 4 --- //
    
    public static class TaigaSmallHouse4 extends StructureVillagePieces.Village
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
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"  FFF  ",
        		"F FFF  ",
        		"   P   ",
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
    	
        public TaigaSmallHouse4() {}

        public TaigaSmallHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static TaigaSmallHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaSmallHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,3, 5,3,3}, {3,0,3, 3,0,3},  
            	// Back wall
            	{1,1,6, 5,3,6}, 
            	// Left wall
            	{1,1,4, 1,4,5}, 
            	// Right wall
            	{5,1,4, 5,4,5}, 
            	// Floor
            	{2,0,4, 4,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,0,1, 3,0,2},
            	// Left wall
            	{2,1,1, 2,3,2}, 
            	// Right wall
            	{4,1,1, 4,3,2}, 
            	// Ceiling
            	{3,3,1, 3,4,2},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{1,3,0, 1,3,2}, 
            	{2,4,0, 2,4,2}, 
            	{3,5,0, 3,5,3}, {3,4,3, 3,4,3}, 
            	{4,4,0, 4,4,2}, 
            	{5,3,0, 5,3,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,3,2, 0,3,2}, {6,3,2, 6,3,2}, 
            	{0,4,3, 2,4,3}, {4,4,3, 6,4,3}, 
            	{0,5,4, 6,5,5}, 
            	{0,4,6, 6,4,6}, 
            	{0,3,7, 6,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{3,3,0, 2}, 
            	// Interior
            	{3,4,4, 0}, 
            	{3,4,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Windows
        	for (int[] uw : new int[][]{
        		// Back windows
        		{2,2,6}, {4,2,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Shutters
            	{1,2,7, 0}, {3,2,7, 0}, {5,2,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,1, 2, 1, 0}, 
            	{3,1,3, 2, 1, 0}, 
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
            	{4,1,4, 2, GeneralConfig.useVillageColors ? this.townColor2 : 11}, // 11 is blue
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
            
            
            // Crafting Table
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 2,1,5, structureBB);
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0,1,1},
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
            	
            	// Generate decor - no trough
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
	        			{2,1,4, -1, 0},
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
    
    public static class TaigaSmallHouse5 extends StructureVillagePieces.Village
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
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaSmallHouse5() {}

        public TaigaSmallHouse5(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static TaigaSmallHouse5 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaSmallHouse5(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,2, 4,5,2}, {3,6,2, 3,6,2}, 
            	// Back wall
            	{2,1,6, 4,5,6}, {3,6,6, 3,6,6}, 
            	// Left wall
            	{1,1,2, 1,4,6}, 
            	// Right wall
            	{5,1,2, 5,4,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,1,3, 4,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            

            // Logs (Vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floors
            	{1,1,1, 1,4,1}, {5,1,1, 5,4,1}, 
            	// Floors
            	{1,1,7, 1,4,7}, {5,1,7, 5,4,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,4,0, 0,4,8}, 
            	{1,5,0, 1,5,8}, 
            	{2,6,0, 2,6,8}, 
            	{3,7,0, 3,7,8}, 
            	{4,6,0, 4,6,8}, 
            	{5,5,0, 5,5,8}, 
            	{6,4,0, 6,4,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{3,4,1, 2}, 
            	// Interior
            	{3,4,3, 0}, 
            	{3,4,5, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
            
            // Glass Panes
        	for (int[] uw : new int[][]{
        		// Back windows
        		{1,3,4}, 
        		{3,3,6}, 
        		{5,3,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Left shutters
            	{0,3,3, 3}, {0,3,5, 3}, 
            	// Right shutters
            	{6,3,3, 1}, {6,3,5, 1}, 
            	// Back shutters
            	{2,3,7, 0}, {4,3,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front stairs
        		{4,2,5, 4,2,5, 3}, 
        		{3,1,1, 3,1,1, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
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
            
            
            // Beds
            for (int[] uvwoc : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,4, 2, GeneralConfig.useVillageColors ? this.townColor : 10}, // 10 is purple
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
        	int chestW = 3;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{2,1,1, 1},
            	{4,1,1, 1},
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
    
    
    // --- Tannery --- //
    
    public static class TaigaTannery1 extends StructureVillagePieces.Village
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
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		"      F  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 4;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaTannery1() {}

        public TaigaTannery1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static TaigaTannery1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaTannery1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone, part 1
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front posts
            	{5,0,1, 5,0,1}, {7,0,1, 7,0,1}, 
            	// Left wall
            	{1,0,3, 1,2,6}, 
            	// Left wall
            	{7,0,3, 7,2,6}, 
            	// Floor
            	{2,0,3, 6,0,6}, 
            	// Ceiling
            	{4,4,4, 4,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,2,1, 0,2,8}, 
            	{1,3,1, 1,3,8}, 
            	{2,4,1, 2,4,8}, 
            	{3,5,1, 5,5,8}, 
            	{6,4,1, 6,4,8}, 
            	{7,3,1, 7,3,8}, 
            	{8,2,1, 8,2,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{5,1,1, -1}, {7,1,1, -1}, 
            	// Interior
            	{4,4,3, 2}, 
            	{4,4,6, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,0,2, 7,2,2}, {2,3,2, 6,3,2}, {3,4,2, 5,4,2}, 
            	// Back wall
            	{1,0,7, 7,2,7}, {2,3,7, 6,3,7}, {3,4,7, 5,4,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
        	
        	
        	// Grass
            for (int[] uw : new int[][]{
            	// Planter
            	{1,0,1, 4,0,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeGrassState, biomeGrassState, false);
            }
        	
            
            // Glass Panes
        	for (int[] uw : new int[][]{
        		// Front windows
        		{3,2,2}, 
        		// Back windows
        		{3,2,7}, {5,2,7}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Front shutters
            	{2,2,1, 2}, {4,2,1, 2}, 
            	// Back shutters
            	{2,2,8, 0}, {4,2,8, 0}, {6,2,8, 0}, 
            	// Planter
            	{0,0,1, 3}, {1,0,0, 2}, {2,0,0, 2}, {3,0,0, 2}, {4,0,0, 2}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 2;
        	int chestV = 1;
        	int chestW = 6;
        	int chestO = 1; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_tannery");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Trapdoor (Bottom Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Trough
            	{3,1,3, 2}, {4,1,4, 1}, {4,1,5, 1}, 
            	{3,1,6, 0}, {2,1,4, 3}, {2,1,5, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front stairs
        		{6,0,1, 6,0,1, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{6,1,2, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Cauldron
        	for (int[] uuvvww : new int[][]{
        		{6,1,5}, 
        		{6,1,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.CAULDRON.getStateFromMeta(3), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{1,1,1, 1},
            	{3,1,1, 1},
            	{4,1,1, 0},
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
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{2,1,1}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 4+random.nextInt(2);
            	int v = 1;
            	int w = 3+random.nextInt(4);
            	
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
    
    public static class TaigaTemple1 extends StructureVillagePieces.Village
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
        		"        F FFF",
        		"        FFFFF",
        		"    FFFFFFFFF",
        		"    FFFFFFFFF",
        		" FFFFFFFFFFFF",
        		" FFFFFFFFFFFF",
        		" FFFFFFFFFF  ",
        		" FFFFFFFFF   ",
        		" FFFFFFFFF   ",
        		"FFFFFFP      ",
        		"FFFFFPPP     ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 14;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 5;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaTemple1() {}

        public TaigaTemple1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static TaigaTemple1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaTemple1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone, part 1
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Under entry door
            	{6,0,2, 6,0,2}, 
            	// Front wall
            	{1,1,2, 7,3,2}, 
            	// Left wall
            	{1,1,3, 1,4,5}, 
            	// Back wall
            	{1,1,6, 6,3,6}, 
            	// Ceiling block
            	{6,3,5, 6,3,5}, 
            	// Place for a torch
            	{7,4,4, 7,4,4}, 
            	// Left Floor
            	{2,0,3, 6,0,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Torches, part 1
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{2,4,4, 1}, 
            	{7,3,5, 1}, 
            	{6,4,4, 3}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Cobblestone, part 2
            for(int[] uuvvww : new int[][]{
            	// Front right wall
            	{7,0,3, 7,3,4}, 
            	// Left section ceiling
            	{2,4,3, 7,4,3}, 
            	{1,5,4, 6,5,4}, 
            	{2,4,5, 6,4,5}, 
            	// Front section left wall
            	{7,1,4, 7,3,4}, 
            	// Tower section
            	// Front wall
            	{8,1,4, 9,4,4}, {7,5,4, 9,10,4}, 
            	// Left wall
            	{6,1,7, 6,3,7}, {6,4,5, 6,10,7}, 
            	// Back wall
            	{7,1,8, 9,10,8}, 
            	// Right wall
            	{10,1,5, 10,10,7}, 
            	// Tower floor
            	{7,0,5, 9,0,7}, 
            	// Tower second floor
            	{7,5,5, 9,5,7}, 
            	// Right torch spire
            	{11,1,6, 11,1,6}, {12,1,6, 12,2,6}, 
            	// Back torch spire
            	{8,1,9, 8,2,10}, 
            	// Flower pot stand
            	{9,1,6, 9,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Torch posts
            	{0,1,0, 0,1,0}, 
            	{12,3,6, 12,3,6}, 
            	{8,3,10, 8,3,10}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Torches, part 2
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior posts
            	{0,2,0, -1}, 
            	{12,4,6, -1}, 
            	{8,4,10, -1}, 
            	// Tower
            	{7,10,6, 1}, 
            	{9,10,6, 3}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
        	
        	// Grass
            for (int[] uw : new int[][]{
            	// Planters
            	{2,1,1, 4,1,1}, 
            	{8,1,2, 8,1,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeGrassState, biomeGrassState, false);
            }
        	
            
            // Glass Panes
        	for (int[] uw : new int[][]{
        		// Front windows
        		{3,2,2}, 
        		// Back windows
        		{2,2,6}, {4,2,6}, 
        		// Left windows
        		{1,2,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Back trough
            	{11,0,9, 11,0,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LADDER.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		{7,2,7, 7,5,7, 2},  
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseLadderMeta(uuvvwwo[6])), false);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{8,2,3, 1}, 
            	{10,1,4, 1}, 
            	{12,1,5, 1}, 
            	{10,1,8, 1}, 
            	{12,1,10, 1}, 
            	{4,1,8, 1}, 
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
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Front shutters
            	{2,2,1, 2}, {4,2,1, 2}, 
            	// Left shutters
            	{0,2,3, 3}, {0,2,5, 3}, 
            	// Back shutters
            	{1,2,7, 0}, {3,2,7, 0}, {5,2,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Trapdoor (Bottom Upright)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Front planter
            	{2,1,0, 2}, {3,1,0, 2}, {4,1,0, 2}, 
            	{1,1,1, 3}, 
            	{5,1,1, 1}, 
            	// Right planter
            	{8,1,1, 2}, 
            	{9,1,2, 1}, {9,1,3, 1}, 
            	// Backyard trough
            	{11,1,8, 2}, 
            	{10,1,9, 3}, 
            	{12,1,9, 1}, 
            	{11,1,10, 0}, 
            	// Tower shutters
            	// Front
            	{7,7,3, 2}, {9,7,3, 2}, {7,9,3, 2}, {9,9,3, 2}, 
            	// Back
            	{7,7,9, 0}, {9,7,9, 0}, {7,9,9, 0}, {9,9,9, 0}, 
            	// Left
            	{5,7,5, 3}, {5,7,7, 3}, {5,9,5, 3}, {5,9,7, 3}, 
            	// Right
            	{11,7,5, 1}, {11,7,7, 1}, {11,9,5, 1}, {11,9,7, 1}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Interior
        		{2,1,3, 2,1,3, 1}, {2,1,5, 2,1,5, 1}, 
        		// Torch wing
        		{11,2,6, 11,2,6, 0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{
            	// Tower windows
            	{8,7,4}, {6,7,6}, {8,7,8}, {10,7,6}, 
            	{8,9,4}, {6,9,6}, {8,9,8}, {10,9,6}, 
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
            
            // Wood with bark on all sides
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodState = ModObjects.chooseModBarkState(biomeLogVertState);
            for (int[] uw : new int[][]{
            	// Tower roof
            	{6,11,3, 10,11,4}, 
            	{6,11,8, 10,11,9}, 
            	{6,11,5, 6,11,7}, {5,11,4, 5,11,8}, 
            	{10,11,5, 10,11,7}, {11,11,4, 11,11,8}, 
            	
            	{6,12,4, 10,12,5}, 
            	{6,12,7, 10,12,8}, 
            	{6,12,6, 7,12,6}, {9,12,6, 10,12,6}, 
            	
            	{7,13,5, 9,13,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeWoodState, biomeWoodState, false);
            }
            
            
        	// Carpet
        	for(int[] uvwm : new int[][]{
        		{8,1,5, (GeneralConfig.useVillageColors ? this.townColor : 10)}, // 10 is purple
        		{8,1,6, (GeneralConfig.useVillageColors ? this.townColor : 10)}, // 10 is purple
        		{8,1,7, (GeneralConfig.useVillageColors ? this.townColor : 10)}, // 10 is purple
        		{9,1,7, (GeneralConfig.useVillageColors ? this.townColor : 10)}, // 10 is purple
        		{9,1,5, (GeneralConfig.useVillageColors ? this.townColor : 10)}, // 10 is purple
        		})
            {
        		this.setBlockState(world, Blocks.CARPET.getStateFromMeta(uvwm[3]), uvwm[0], uvwm[1], uvwm[2], structureBB); 
            }
        	
        	// Flower pot
            // 0: nuffin
            // 1: poppy
            // 2: dandelion
            int flowernumber = random.nextInt(2)+1;
            
            // Flower pot
        	int potU = 9;
        	int potV = 2;
        	int potW = 6;
        	int potX = this.getXWithOffset(potU, potW);
        	int potY = this.getYWithOffset(potV);
        	int potZ = this.getZWithOffset(potU, potW);
        	
        	TileEntity flowerPot = (new BlockFlowerPot()).createNewTileEntity(world, flowernumber);
    		BlockPos flowerPotPos = new BlockPos(potX, potY, potZ);
    		world.setBlockState(flowerPotPos, Blocks.FLOWER_POT.getDefaultState());
    		world.setTileEntity(flowerPotPos, flowerPot);
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{6,1,2, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
        	
            // Brewing Stand
        	for (int[] uvw : new int[][]{
        		{2,1,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.BREWING_STAND.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{3,2,1}, 
        		{8,2,2}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{5, GROUND_LEVEL, -1}, 
        		{6, GROUND_LEVEL, -1}, 
        		{7, GROUND_LEVEL, -1}, 
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
            	int u=0;
            	int v=1;
            	int w=0;
            	
            	// Brewing stand side
            	if (random.nextInt(18) < 12)
            	{
            		u = 3+random.nextInt(4);
            		w = 3+random.nextInt(3);
            	}
            	else // Flower pot side
            	{
            		u = 7+random.nextInt(2);
            		w = 5+random.nextInt(3);
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
    
    
    // --- Tool Smithy --- //
    
    public static class TaigaToolSmith1 extends StructureVillagePieces.Village
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
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		"    FFF    ",
        		"    FFF    ",
        		" F   P   F ",
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
    	
        public TaigaToolSmith1() {}

        public TaigaToolSmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static TaigaToolSmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaToolSmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Under entry door
            	{5,0,3, 5,0,3}, 
            	// Front wall
            	{1,1,3, 9,3,3}, 
            	// Back wall
            	{1,1,6, 9,3,6}, 
            	// Left wall
            	{1,1,4, 1,4,5}, 
            	// Right wall
            	{9,1,4, 9,4,5}, 
            	// Floor
            	{2,0,4, 8,0,5}, 
            	// Counter
            	{2,1,4, 2,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Entryway front
            	{4,1,1, 6,3,1}, 
            	// Entryway left
            	{4,1,2, 4,3,2}, 
            	// Entryway right
            	{6,1,2, 6,3,2}, 
            	// Entryway ceiling
            	{5,4,1, 5,4,2}, 
            	// Entryway floor
            	{5,0,1, 5,0,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Torches, part 1
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Interior
            	{5,3,0, 2}, 
            	{5,3,4, 0}, 
            	{2,4,4, 1}, {2,4,5, 1}, 
            	{8,4,4, 3}, {8,4,5, 3}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{3,3,0, 3,3,1}, 
            	{4,4,0, 4,4,3}, 
            	{5,5,0, 5,5,3}, 
            	{6,4,0, 6,4,3}, 
            	{7,3,0, 7,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,3,2, 3,3,2}, {7,3,2, 10,3,2}, 
            	{0,4,3, 3,4,3}, {5,4,3, 5,4,3}, {7,4,3, 10,4,3}, 
            	{0,5,4, 10,5,4}, 
            	{0,5,5, 10,5,5}, 
            	{0,4,6, 10,4,6}, 
            	{0,3,7, 10,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
        	
            
            // Glass Panes
        	for (int[] uw : new int[][]{
        		// Front windows
        		{2,2,3}, {8,2,3}, 
        		// Back windows
        		{3,2,6}, {5,2,6}, {7,2,6}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Front shutters
            	{1,2,2, 2}, {3,2,2, 2}, {7,2,2, 2}, {9,2,2, 2}, 
            	// Back shutters
            	{2,2,7, 0}, {4,2,7, 0}, {6,2,7, 0}, {8,2,7, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
        	
        	
            // Stone stairs
        	Block biomeCobblestoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Interior benches
        		{8,1,4, 8,1,5, 0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), biomeCobblestoneStairsBlock.getStateFromMeta(uuvvwwo[6]%4+(uuvvwwo[6]/4)*4), false);	
            }
        	
            
            // Smithing table
        	IBlockState smithingTableBlockState = ModObjects.chooseModSmithingTable(1, this.getCoordBaseMode());
        	for (int[] uvw : new int[][]{
        		{2,1,5}, 
        		})
            {
        		this.setBlockState(world, smithingTableBlockState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 6;
        	int chestV = 1;
        	int chestW = 4;
        	int chestO = 0; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_toolsmith");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{5,1,3, 2, 1, 0}, 
            	{5,1,1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.getCoordBaseMode(), uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{1,1,0},
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
            	
            	// Generate decor - no trough
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
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
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
        	
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u=6;
            	int v=1;
            	int w=4;
            	
            	while (u==6 && w==4)
            	{
            		u = 3 + random.nextInt(5);
            		w = 4 + random.nextInt(2);
            	}
            	
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
    
    
    // --- Weapon Smith House --- //
    
    public static class TaigaWeaponsmith1 extends StructureVillagePieces.Village
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
        		"FPFPFPF",
        		"FPPPPPF",
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
    	
        public TaigaWeaponsmith1() {}

        public TaigaWeaponsmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static TaigaWeaponsmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaWeaponsmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
        	
        	// Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{1,1,2, 5,3,2}, 
            	// Back wall
            	{1,1,5, 5,3,5}, 
            	// Left wall
            	{1,1,3, 1,3,4}, 
            	// Right wall
            	{5,1,3, 5,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Under entry door
            	{3,0,2, 3,0,2}, 
            	// Floor
            	{2,0,3, 4,0,4}, 
            	// Front wall
            	{1,4,2, 5,5,2}, {2,6,2, 4,6,2}, {3,7,2, 3,7,2}, 
            	// Back wall
            	{1,4,5, 5,5,5}, {2,6,5, 4,6,5}, {3,7,5, 3,7,5}, 
            	// Left wall
            	{1,4,3, 1,5,4}, 
            	// Right wall
            	{5,4,3, 5,5,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{3,3,1, 2}, {3,6,1, 2}, 
            	// Interior
            	{3,5,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Logs (Along)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	// Roof
            	{0,5,1, 0,5,6}, 
            	{1,6,1, 1,6,6}, 
            	{2,7,1, 2,7,6}, 
            	{3,8,1, 3,8,6}, 
            	{4,7,1, 4,7,6}, 
            	{5,6,1, 5,6,6}, 
            	{6,5,1, 6,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
        	
            
            // Glass Panes
        	for (int[] uw : new int[][]{
        		// Front windows
        		{2,5,2}, {4,5,2}, 
        		// Back windows
        		{2,2,5}, {4,2,5}, 
        		{2,5,5}, {4,5,5}, 
        		})
            {
        		this.setBlockState(world, Blocks.GLASS_PANE.getStateFromMeta(0), uw[0], uw[1], uw[2], structureBB);
            }
        	
        	
        	// Grass
            for (int[] uw : new int[][]{
            	// Planters
            	{1,4,1, 5,4,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeGrassState, biomeGrassState, false);
            }
            
            
            // Trapdoor (Top Upright)
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Balcony planter
            	{0,4,1, 3}, {1,4,0, 2}, {2,4,0, 2}, {3,4,0, 2}, {4,4,0, 2}, {5,4,0, 2}, {6,4,1, 1}, 
            	// Back shutters
            	{1,2,6, 0}, {3,2,6, 0}, {5,2,6, 0}, 
            	{1,5,6, 0}, {3,5,6, 0}, {5,5,6, 0}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], true, true)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvw : new int[][]{
            	// Table
            	{4,1,4},  
        		})
            {
            	this.setBlockState(world, biomeFenceState, uvw[0],uvw[1],uvw[2], structureBB);
            }
            
            
            // Trapdoor (Bottom flat)
        	Block biomeTrapdoorBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), this.materialType, this.biome, this.disallowModSubs).getBlock();
            for(int[] uuvvww : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	// Table
            	{4,2,4, 3}, 
            	})
            {
            	this.setBlockState(world, biomeTrapdoorBlock.getStateFromMeta(StructureVillageVN.getTrapdoorMeta(uuvvww[3], false, false)), uuvvww[0], uuvvww[1], uuvvww[2], structureBB);
            }
            
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Bench
        		{2,1,3, 1}, {2,1,4, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(uvwo[3]%4+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
            
            // Grindstone
        	for (int[] uvwo : new int[][]{
        		{2,1,1, 2, 0}, // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		{4,1,1, 2, 0}, 
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		IBlockState biomeGrindstoneState = ModObjects.chooseModGrindstone(uvwo[3], this.getCoordBaseMode(), uvwo[4]==1);
            	
        		this.setBlockState(world, biomeGrindstoneState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 1;
        	int chestW = 3;
        	int chestO = 3; // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.CHEST.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.getCoordBaseMode())), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_weaponsmith");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,0, 0},
            	{6,1,2, 0},
            	{1,5,1, 0},
            	{3,5,1, 0},
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
            
            
            // Flowers
            for (int[] uvw : new int[][]{
        		{2,5,1}, 
        		{5,5,1}, 
        		})
            {
            	int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.OAK_DOOR.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// orientation: 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
            	{3,1,2, 2, 1, 1}, 
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
            	
            	// Villager
            	int u=3;
            	int v=1;
            	int w=3;
            	
            	int s=random.nextInt(11);
            	
            	if (s>=8) // Inside the house
            	{
            		u=3;
            		w=2+random.nextInt(3);
            	}
            	else if (s>2) // Directly out front
            	{
            		u=0; w=s-2;
            	}
            	else // Three notches next to or between the grindstones
            	{
            		u=1; w=2*s+1;
            	}
            	
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
    
    
    // --- Weapon Smith Station --- //
    
    public static class TaigaWeaponsmith2 extends StructureVillagePieces.Village
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
        		" FFFFF",
        		" FFFFF",
        		" FPFPF",
        		"FFPPPF",
        		"FFPPFF",
        		" PPPPF",
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
    	
        public TaigaWeaponsmith2() {}

        public TaigaWeaponsmith2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static TaigaWeaponsmith2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaWeaponsmith2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0,1,2},
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
            	
            	// Generate decor - no trough
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
            	// Under the grindstone
            	{3,0,3, 3,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Cobblestone Wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE_WALL.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left beam
            	{1,1,1, 1,1,3}, {1,2,3, 1,3,3},
            	// Right beam
            	{5,1,1, 5,1,3}, {5,2,3, 5,3,3},
            	// Rear beams
            	{1,1,5, 1,1,5}, {3,1,5, 3,1,5}, {5,1,5, 5,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneWallState, biomeCobblestoneWallState, false);	
            }
            
            
            // Logs (Across)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.getCoordBaseMode().getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	// Roof
            	{1,4,3, 5,4,3}, 
            	{1,3,4, 5,3,4}, 
            	{1,2,5, 5,2,5}, 
            	{1,1,6, 5,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Posts
            	{1,2,1, -1}, {5,2,1, -1},  
            	}) {
            	this.setBlockState(world, Blocks.TORCH.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3])), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Grindstone
        	for (int[] uvwo : new int[][]{
        		{3,1,3, 2, 0}, // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		IBlockState biomeGrindstoneState = ModObjects.chooseModGrindstone(uvwo[3], this.getCoordBaseMode(), uvwo[4]==1);
            	
        		this.setBlockState(world, biomeGrindstoneState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	

            // Ferns
            for (int[] uwg : new int[][]{ // g is grass type
            	{0,1,1, 1},
            	{1,1,4, 0},
            	{2,1,4, 1},
            	{4,1,4, 0},
            	{4,1,5, 0},
            	{5,1,0, 0},
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
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u=3;
            	int v=1;
            	int w=3;
            	
            	while (u==3 && w==3)
            	{
            		u = 2 + random.nextInt(3);
            		w = 0 + random.nextInt(4);
            	}
            	
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
    
    public static class TaigaStreetDecor1 extends StructureVillagePieces.Village
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
    	public static final int STRUCTURE_DEPTH = 9;
    	public static final int STRUCTURE_HEIGHT = 2;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public TaigaStreetDecor1() {}

        public TaigaStreetDecor1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static TaigaStreetDecor1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new TaigaStreetDecor1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            			new    int[]{-2,-1, 0, 1, 2, 3, 4, 5}, // Values
            			new double[]{ 9, 2, 1, 6, 5, 5, 1, 1}, // Weights
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
            	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs);
            	
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
	public static ArrayList<BlueprintData> getRandomTaigaDecorBlueprint(MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		int decorCount = 7;
		return getTaigaDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);
	}
	public static ArrayList<BlueprintData> getTaigaDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		// Generate per-material blocks
		IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeStoneStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.TRAPDOOR.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState campfireState = ModObjects.chooseModCampfireBlockState(random.nextInt(4), coordBaseMode);
    	
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
